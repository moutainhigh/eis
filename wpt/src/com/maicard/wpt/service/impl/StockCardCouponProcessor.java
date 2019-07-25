package com.maicard.wpt.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;

import org.springframework.util.Assert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.CenterDataService;
import com.maicard.common.util.JsonUtils;
import com.maicard.common.util.Paging;
import com.maicard.money.criteria.CouponCriteria;
import com.maicard.money.criteria.CouponModelCriteria;
import com.maicard.money.dao.CouponDao;
import com.maicard.money.domain.Coupon;
import com.maicard.money.domain.CouponModel;
import com.maicard.money.service.CouponModelService;
import com.maicard.money.service.CouponProcessor;
import com.maicard.money.service.CouponService;
import com.maicard.standard.EisError;
import com.maicard.standard.OperateResult;
import com.maicard.standard.TransactionStandard.TransactionStatus;

import static com.maicard.standard.KeyConstants.LOCKED_COUPON_PREFIX;

/**
 * 获取已导入的神州行充值卡或其他充值卡作为Coupon返回
 *
 *
 * @author NetSnake
 * @date 2017年1月3日
 *
 */
public class StockCardCouponProcessor extends BaseService implements CouponProcessor{

	@Resource
	private CouponDao couponDao;

	@Resource
	private CenterDataService centerDataService;

	@Resource
	private CouponModelService couponModelService;

	@Resource
	private CouponService couponService;

	private final int MAX_LIST_COUNT = 10;

	/**
	 * 锁定一张卡券的最长时间
	 */
	private final int LOCK_TTL = 3600;
	ObjectMapper om = JsonUtils.getNoDefaultValueInstance();

	@Override
	public List<CouponModel> list(CouponModelCriteria couponModelCriteria, Object parameter) {
		return null;
	}



	@Override
	public int consume(Coupon coupon) {

		return OperateResult.success.getId();
	}

	@Override
	public String getProcessorDesc() {
		return "库存卡密优惠券处理器";
	}

	@Override
	public EisMessage fetch(CouponCriteria couponCriteria) {
		int count = 0;
		Paging paging = null;
		if(couponCriteria.getPaging() != null){
			paging = couponCriteria.getPaging();
			count = couponCriteria.getPaging().getMaxResults();
		} else {
			paging = new Paging(MAX_LIST_COUNT);
			paging.setCurrentPage(1);
		}
		if(count == 0){
			count = 1;
		}
		if(couponCriteria.getCouponModelId() < 1){
			if(couponCriteria.getCouponCode() == null){
				logger.error("获取的卡券条件中既没有couponModelId也没有couponModelCode,无法获取");
				return new EisMessage(EisError.stockEmpty.id,"库存不足，无法获取指定的产品");
			} 
		}

		if(couponCriteria.getCurrentStatus() == null || couponCriteria.getCurrentStatus().length < 1){
			couponCriteria.setCurrentStatus(TransactionStatus.newOrder.id);
		}
		CouponCriteria couponCriteria2 = couponCriteria.clone();
		couponCriteria2.setUuid(0);
		couponCriteria2.setFetchNewCoupon(true);
		List<Coupon> lockedCouponList = new ArrayList<Coupon>();
		int lockedCount = 0;
		//for(int i = 0; i < 5; i++){
		List<Coupon> couponList = couponDao.list(couponCriteria2);
		logger.debug("根据条件[" + couponCriteria2 + "]找到的库存卡券是:" + (couponList == null ? "空" : couponList.size()));
		if(couponList == null || couponList.size() < 1){
			return new EisMessage(EisError.stockEmpty.id,"库存不足，无法获取指定的产品");
		} 

		for(int j = 0; j < couponList.size(); j++){
			Coupon coupon = couponList.get(j);
			coupon.setUuid(couponCriteria.getUuid());
			Coupon lockedCoupon = this.lock(coupon);
			logger.debug("第" + (j+1) + "次获取卡券结果:" + lockedCoupon);
			if(lockedCoupon != null){
				lockedCount++;
				logger.debug("第" + (j+1) + "次获取卡券成功，已成功获取" + lockedCount + "个，一共需要获取:" + count + "个");
				lockedCouponList.add(lockedCoupon);
				if(lockedCount >= count){
					break;
				}
			}
		}


		//}
		logger.warn("根据条件[" + couponCriteria + "]锁定的卡券数量是:" + lockedCouponList.size());
		if(lockedCouponList == null || lockedCouponList.size() < 1){
			return new EisMessage(EisError.stockEmpty.id,"库存不足，无法获取指定的产品");
		}
		EisMessage resultMsg = new EisMessage(OperateResult.success.getId(),"获取成功");
		resultMsg.setAttachmentData("couponList", lockedCouponList);
		return resultMsg;
	}





	/**
	 * 把对应卡券设置为某个用户所有
	 * @param coupon
	 * @return
	 */
	@Override
	public Coupon lock(CouponCriteria couponCriteria) {
		Assert.notNull(couponCriteria, "尝试锁定的卡券条件为空");
		Assert.isTrue(couponCriteria.getUuid() > 0, "尝试锁定的卡券条件,UUID为0");
		Coupon _oldCoupon = null;

		CouponCriteria couponCriteria2 = couponCriteria.clone();
		//不使用用户和状态条件查询
		couponCriteria2.setUuid(0);
		couponCriteria2.setCurrentStatus(null);
		//couponCriteria.setUuid(coupon.getUuid());
		//couponCriteria.setCurrentStatus(Coupon.STATUS_NEW);
		List<Coupon> couponList = couponService.list(couponCriteria2);
		if(couponList != null && couponList.size() > 0){
			_oldCoupon = couponList.get(0);
		}

		if(_oldCoupon == null){
			logger.error("在本地系统中，找不到指定的卡券:" + couponCriteria);
			return null;
		}
		if(_oldCoupon.getCurrentStatus() == TransactionStatus.closed.id){
			logger.debug("在本地系统中，指定的卡券:" + _oldCoupon.getCouponId() + "已使用");
			return null;
		}
		/*if(_oldCoupon.getUuid() !=couponCriteria.getUuid()){
			logger.debug("在本地系统中，指定的卡券:" + _oldCoupon.getCouponId() + "不属于用户[" + couponCriteria.getUuid() + "],而属于:" + _oldCoupon.getUuid());
			return null;
		}
		 */

		return lock(_oldCoupon);
	}

	@Override
	public Coupon lock(Coupon coupon){
		
		Assert.isTrue(coupon.getUuid() != 0, "尝试锁定的库存卡券UUID不能为0");

		coupon.setCurrentStatus(TransactionStatus.auctionSuccess.id);


		String key = LOCKED_COUPON_PREFIX + "#" + coupon.getOwnerId() + "#" + coupon.getCouponCode() + "#" + coupon.getCouponSerialNumber() + "#" + coupon.getCouponPassword();
		

		boolean setSuccess = centerDataService.setIfNotExist(key, key, LOCK_TTL);		
		if(!setSuccess){
			logger.error("无法添加LOCK缓存[" + key + "]，无法设置卡券[" + coupon.getCouponId() + "]为用户[" +  coupon.getUuid() + "]所有");
			return null;
		}

		logger.debug("卡券[" + coupon + "]已被设置为用户[" + coupon.getUuid() + "]所有");		
		couponService.update(coupon);
		return coupon;
	}


	@Override
	public int validate(Coupon coupon) {
		Coupon _oldCoupon = null;
		if(coupon.getCouponId() > 0){
			_oldCoupon = couponService.select(coupon.getCouponId());

		} else {
			CouponCriteria couponCriteria = new CouponCriteria(coupon.getOwnerId());
			couponCriteria.setCouponCode(coupon.getCouponCode());
			couponCriteria.setCouponSerialNumber(coupon.getCouponSerialNumber());
			couponCriteria.setCouponPassword(coupon.getCouponPassword());
			//	couponCriteria.setUuid(coupon.getUuid());
			//	couponCriteria.setCurrentStatus(Coupon.STATUS_LOCKED);
			List<Coupon> couponList = couponService.list(couponCriteria);
			if(couponList != null && couponList.size() > 0){
				_oldCoupon = couponList.get(0);
			} else {
				logger.error("在本地系统中，根据条件找不到指定的卡券:" + couponCriteria );

			}
		}
		if(_oldCoupon == null){
			logger.error("在本地系统中，找不到指定的卡券:" + coupon );
			return -EisError.OBJECT_IS_NULL.id;
		}
		if(_oldCoupon.getCurrentStatus() == TransactionStatus.closed.id){
			logger.debug("在本地系统中，指定的卡券:" + _oldCoupon.getCouponId() + "已使用");
			return -EisError.cardUsedBefore.id;
		}
		if(_oldCoupon.getUuid() != coupon.getUuid()){
			logger.debug("在本地系统中，指定的卡券:" + _oldCoupon.getCouponId() + "不属于用户[" + coupon.getUuid() + "],而属于:" + _oldCoupon.getUuid());
			return -EisError.userNotFoundInSystem.id;
		}

		String key = LOCKED_COUPON_PREFIX + "#" +  coupon.getOwnerId() + "#" + coupon.getCouponCode() + "#" + coupon.getCouponSerialNumber() + "#" + coupon.getCouponPassword();
		String value = centerDataService.get(key);
		Coupon cachedCoupon = null;
		ObjectMapper om = JsonUtils.getNoDefaultValueInstance();

		if(value == null){
			//REDIS没有但是本地有，从本地写入
			logger.info("无法从REDIS中获取卡券，按本地数据为准");
		} else {
			logger.debug("读取REDIS数据[" + key + "]是:" + value);
			try {
				cachedCoupon = om.readValue(value, Coupon.class);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(cachedCoupon == null){
			logger.warn("从REDIS中获取的卡券[" + key + "]数据异常:" + value + ",使用本地版本，并将本地数据放入中心缓存");
			try {
				value = om.writeValueAsString(_oldCoupon);
				boolean setSuccess = centerDataService.setIfNotExist(key, value, -1);
				logger.debug("将卡券[" + key + "]本地数据放入中心缓存，结果:" + setSuccess);

			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		} else {
			logger.debug("缓存中已存在的Coupon:" + cachedCoupon);
			_oldCoupon = cachedCoupon;
		}

		if(_oldCoupon.getUuid() != coupon.getUuid()){
			logger.error("卡券[" + _oldCoupon.getCouponId() + "]，其拥有者[" + cachedCoupon.getUuid() + "]与请求提交的用户[" + coupon.getUuid() + "]不一致");
			return -EisError.dataVerifyFail.id;
		}
		if(cachedCoupon.getCurrentStatus() != Coupon.STATUS_LOCKED){
			logger.error("卡券[" + _oldCoupon.getCouponId() + "]状态不是锁定状态，而是:" + cachedCoupon.getCurrentStatus() + "，无效");
			return -EisError.dataVerifyFail.id;
		}
		logger.debug("卡券[" + _oldCoupon.getCouponId() + "]属于用户[" + cachedCoupon.getUuid() + "]并且有效");
		couponService.update(_oldCoupon);

		coupon = _oldCoupon;
		coupon.setCouponId(_oldCoupon.getCouponId());

		return OperateResult.success.getId();
	}

}
