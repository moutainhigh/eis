package com.maicard.money.service.cp;

import static com.maicard.standard.KeyConstants.LOCKED_COUPON_PREFIX;
import static com.maicard.standard.KeyConstants.NEW_COUPON_PREFIX;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.CenterDataService;
import com.maicard.common.util.JsonUtils;
import com.maicard.money.criteria.CouponCriteria;
import com.maicard.money.criteria.CouponModelCriteria;
import com.maicard.money.domain.Coupon;
import com.maicard.money.domain.CouponModel;
import com.maicard.money.service.CouponModelService;
import com.maicard.money.service.CouponProcessor;
import com.maicard.money.service.CouponService;
import com.maicard.security.domain.User;
import com.maicard.security.service.FrontUserService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.OperateResult;
import com.maicard.standard.TransactionStandard.TransactionStatus;

/**
 * 内部随机串形式生成和发布优惠券<br>
 * 获取时绑定到某个指定渠道inviter<br>
 * 由该渠道自行分发和销售<br>
 * 只有该inviter下的用户才能消费该优惠券
 * 
 *
 *
 * @author NetSnake
 * @date 2017-03-15
 *
 */
public class ChannelBundleCouponProcessor extends BaseService implements CouponProcessor{

	private final int MIN_NUMBER_LENGTH = 4;

	private final int MAX_NUMBER_LENGTH = 32;




	//private final int LOCK_MAX_RETRY = 3;

	@Resource
	private CenterDataService centerDataService;

	@Resource
	private CouponModelService couponModelService;

	@Resource
	private CouponService couponService;
	
	@Resource
	private FrontUserService frontUserService;


	/**
	 * 锁定一张卡券的最长时间
	 */
	private final int LOCK_TTL = 3600;	
	private final int FETCH_RETRY = 10;
	
	private final String DEFAULT_COUPON_CODE = "channel_rand";

	@Override
	public List<CouponModel> list(CouponModelCriteria couponModelCriteria, Object parameter) {
		return null;
	}

	/**
	 * 渠道使用现金购买多个卡券，把这些卡券设置为该渠道所有
	 * 
	 */
	@Override
	public EisMessage fetch(CouponCriteria couponCriteria) {
		Assert.notNull(couponCriteria,"准备获取卡券的条件不能为空");
		Assert.notNull(couponCriteria.getCouponCode(),"准备获取卡券的条件中卡券代码不能为空");
		Assert.isTrue(couponCriteria.getOwnerId() > 0,"准备获取卡券的条件中ownerId不能为0");
		Assert.isTrue(couponCriteria.getUuid() != 0,"准备获取卡券的条件中用户UUID不能为0");
		
		int count = couponCriteria.getFetchCount();

		if(count == 0){
			count = 1;
		}
		
		CouponModelCriteria couponModelCriteria = new CouponModelCriteria(couponCriteria.getOwnerId());
		couponModelCriteria.setCurrentStatus(BasicStatus.normal.id);
		if(StringUtils.isBlank(couponCriteria.getCouponCode())){
			couponModelCriteria.setCouponCode(DEFAULT_COUPON_CODE);
		} else {
			couponModelCriteria.setCouponCode(couponCriteria.getCouponCode());
		}
		List<CouponModel> couponModelList = couponModelService.list(couponModelCriteria);
		logger.debug("查找couponCode=" + couponModelCriteria.getCouponCode() + "的卡券模型返回数量是:" + (couponModelList == null ? "空" : couponModelList.size()));
		if(couponModelList == null || couponModelList.size() < 1){
			return new EisMessage(EisError.productNotExist.id,"指定的卡券不存在");
		}
		CouponModel couponModel = couponModelList.get(0);
		List<Coupon> lockedCouponList = new ArrayList<Coupon>();

		int lockedCount = 0;
		for(int j = 0; j < FETCH_RETRY;){
			Coupon coupon = this.generate(couponModel);
			if(coupon == null){
				j++;
				continue;
			}
			//渠道才能获取优惠券，使用该uuid即渠道的uuid作为新卡券的inviter
			coupon.setInviter(couponCriteria.getUuid());
			int rs = couponService.insert(coupon);
			if(rs != 1){
				j++;
				continue;
			}
			Coupon lockedCoupon = this.lock(coupon);
			logger.debug("第" + (j+1) + "次获取卡券结果:" + lockedCoupon);
			if(lockedCoupon != null){
				lockedCount++;
				logger.debug("第" + (j+1) + "次获取卡券成功，已成功获取" + lockedCount + "个，一共需要获取:" + count + "个");
				lockedCouponList.add(lockedCoupon);
				if(lockedCount >= count){
					break;
				}
			} else {
				j++;
			}
		}
		EisMessage resultMsg = new EisMessage(OperateResult.success.getId(),"获取成功");
		resultMsg.setAttachmentData("couponList", lockedCouponList);
		return resultMsg;

	}

	@Override
	public int consume(Coupon coupon) {
		if(StringUtils.isBlank(coupon.getCouponCode())){
			coupon.setCouponCode(DEFAULT_COUPON_CODE);
		}


		String key = LOCKED_COUPON_PREFIX + "#" + coupon.getOwnerId() + "#" + coupon.getCouponCode() + "#" + coupon.getCouponSerialNumber() + "#" + coupon.getCouponPassword();
		String value = centerDataService.get(key);
		if(value == null){
			logger.error("无法从REDIS中获取卡券:" + key);
			return EisError.OBJECT_IS_NULL.id;
		}
		Coupon cachedCoupon = null;
		try {
			cachedCoupon = JsonUtils.getInstance().readValue(value, Coupon.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(cachedCoupon == null){
			logger.error("从REDIS中获取的卡券[" + key + "]数据异常:" + value);
			return EisError.dataError.id;
		}

		User frontUser = frontUserService.select(coupon.getUuid());
		if(frontUser == null){
			logger.error("找不到消费卡券的用户:" + coupon.getUuid());
			return EisError.dataVerifyFail.id;

		}

		if(cachedCoupon.getInviter() != frontUser.getInviter()){
			logger.error("从REDIS中获取的卡券[" + key + "]，其拥有者[" + cachedCoupon.getInviter() + "]与请求提交的用户[" + coupon.getUuid() + "]渠道ID:" + frontUser.getInviter() + "]不一致");
			return EisError.dataVerifyFail.id;
		}
		//获取成功并且判断卡券合法后，再以排他性获取一次，以防止并发冲突
		value = centerDataService.getExclusive(key,true);
		if(value == null){
			logger.error("无法从REDIS中获取卡券:" + key);
			return EisError.OBJECT_IS_NULL.id;
		}
		try {
			cachedCoupon = JsonUtils.getInstance().readValue(value, Coupon.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(cachedCoupon == null){
			logger.error("从REDIS中获取的卡券[" + key + "]数据异常:" + value);
			return EisError.dataError.id;
		}
		cachedCoupon.setUuid(coupon.getUuid());
		cachedCoupon.setCurrentStatus(TransactionStatus.closed.id);
		/*_oldCoupon = cachedCoupon;
		_oldCoupon.setUseTime(new Date());
		_oldCoupon.setCurrentStatus(Coupon.STATUS_USED);
		_oldCoupon.setSyncFlag(0);*/
		cachedCoupon.setSyncFlag(0);
		couponService.update(cachedCoupon);
		BeanUtils.copyProperties(cachedCoupon, coupon);
		return OperateResult.success.getId();
	}

	@Override
	public String getProcessorDesc() {
		return "随机卡券处理器";
	}

	

	/**
	 * 根据指定模型和卡号密码长度生成一个随机串卡券
	 * @param couponModel
	 * @param length
	 * @return
	 */
	public  synchronized Coupon generate(CouponModel couponModel) {
		int snLength = (int)couponModel.getLongExtraValue(DataName.serialNumberLength.toString());
		if(snLength < MIN_NUMBER_LENGTH){
			snLength = MIN_NUMBER_LENGTH;
		}
		if(snLength > MAX_NUMBER_LENGTH){
			snLength = MAX_NUMBER_LENGTH;
		}
		int passwordLength = (int)couponModel.getLongExtraValue(DataName.passwordLength.toString());
		if(passwordLength < MIN_NUMBER_LENGTH){
			passwordLength = MIN_NUMBER_LENGTH;
		}
		if(passwordLength > MAX_NUMBER_LENGTH){
			passwordLength = MAX_NUMBER_LENGTH;
		}		

		String sn = null;
		String password = null;
		int rand = RandomUtils.nextInt();
		//System.out.println(rand);
		String salt = String.valueOf(System.currentTimeMillis() + rand);
		/*		String sign = DigestUtils.md5Hex(salt);
		StringBuilder sb = new StringBuilder();
		System.out.println("salt=" + salt + ",sign=" + sign);
		for(int i = 0; i < sign.length(); i++){
			sb.append(sign.charAt(i));
			if(i < salt.length()){
				sb.append(salt.charAt(i));
			}
		}
		String src = sb.toString();*/
		//	System.out.println("salt=" + salt + ",sign=" + sign + ",src=" + src);
		String src = DigestUtils.md5Hex(salt);
		//System.out.println("src=" + src);
		if(src.length() >= (snLength + passwordLength)){
			sn = src.substring(0, snLength);
			if(passwordLength > 0){
				password = src.substring(snLength, snLength+passwordLength);
			}
		} else {
			sn = src.substring(0, snLength);
			if(passwordLength > 0){

				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				rand = RandomUtils.nextInt();
				salt = String.valueOf(System.currentTimeMillis() + rand);
				src = DigestUtils.md5Hex(String.valueOf(salt));
				password = src.substring(0,passwordLength);
			}

		}


		Coupon coupon = new Coupon(couponModel);
		coupon.setCouponSerialNumber(sn.toUpperCase());
		if(passwordLength > 0){
			coupon.setCouponPassword(password.toUpperCase());
		}
		return coupon;
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

		int validateRs = validate(_oldCoupon);
		if(validateRs == OperateResult.success.getId()){
			logger.debug("卡券[" + _oldCoupon.getCouponId() + "]已经属于用户:" + _oldCoupon.getUuid());
			couponService.update(_oldCoupon);
			return _oldCoupon;
		}


		_oldCoupon.setVersion(_oldCoupon.getVersion()+1);

		String lockKey = "Lock#Coupon#" + _oldCoupon.getCouponId();
		boolean lockSuccess =  centerDataService.lock(lockKey);

		
		if(!lockSuccess){
			logger.error("无法锁定Coupon:" + _oldCoupon.getCouponId());
			return null;
		}

		Coupon cachedCoupon = null;


		String key = NEW_COUPON_PREFIX + "#" + _oldCoupon.getOwnerId() + "#" + _oldCoupon.getCouponCode() + "#" + _oldCoupon.getCouponSerialNumber() + "#" + _oldCoupon.getCouponPassword();
		String value = centerDataService.get(key);
		if(value == null){
			logger.error("无法从REDIS中获取卡券:" + key);		
			return null;
		} 
		try {
			cachedCoupon = JsonUtils.getInstance().readValue(value, Coupon.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(cachedCoupon == null){
			logger.error("从REDIS中获取的卡券[" + key + "]数据异常:" + value);
			centerDataService.delete(lockKey);
			return null;
		}

		long delRs = centerDataService.delete(key);
		if(delRs < 1){
			logger.error("无法从REDIS中删除新状态卡券:" + key);
			return null;
		}

		key = key.replaceAll(NEW_COUPON_PREFIX, LOCKED_COUPON_PREFIX);

		//	long couponId = _oldCoupon.getCouponId();

		//以中心缓存数据为准
		if(cachedCoupon != null){
			_oldCoupon = cachedCoupon;	
		}
		_oldCoupon.setCurrentStatus(Coupon.STATUS_LOCKED);
		_oldCoupon.setUuid(couponCriteria.getUuid());

		ObjectMapper om = JsonUtils.getInstance();
		om.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);

		value = null;
		try {
			value = om.writeValueAsString(_oldCoupon);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		if(value == null){
			logger.error("无法序列化Coupon:" + _oldCoupon.getCouponId());
			centerDataService.delete(lockKey);
			return null;
		}


		boolean setSuccess = centerDataService.setIfNotExist(key, value, -1);		
		if(!setSuccess){
			logger.error("无法添加LOCK缓存[" + key + "]，无法设置卡券[" + _oldCoupon.getCouponId() + "]为用户[" +  _oldCoupon.getUuid() + "]所有");
		}
		centerDataService.delete(lockKey);

		logger.debug("卡券[" + _oldCoupon + "]已被设置为用户[" + _oldCoupon.getUuid() + "]所有");		
		couponService.update(_oldCoupon);
		return _oldCoupon;
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
			return EisError.OBJECT_IS_NULL.id;
		}
		if(_oldCoupon.getCurrentStatus() == TransactionStatus.closed.id){
			logger.debug("在本地系统中，指定的卡券:" + _oldCoupon.getCouponId() + "已使用");
			return EisError.cardUsedBefore.id;
		}
		if(_oldCoupon.getUuid() != coupon.getUuid()){
			logger.debug("在本地系统中，指定的卡券:" + _oldCoupon.getCouponId() + "不属于用户[" + coupon.getUuid() + "],而属于:" + _oldCoupon.getUuid());
			return EisError.userNotFoundInSystem.id;
		}

		String key = LOCKED_COUPON_PREFIX + "#" +  coupon.getOwnerId() + "#" + coupon.getCouponCode() + "#" + coupon.getCouponSerialNumber() + "#" + coupon.getCouponPassword();
		String value = centerDataService.get(key);
		if(value == null){
			logger.error("无法从REDIS中获取卡券:" + key);
			return EisError.OBJECT_IS_NULL.id;
		}
		logger.debug("读取REDIS数据[" + key + "]是:" + value);
		ObjectMapper om = JsonUtils.getNoDefaultValueInstance();
		Coupon cachedCoupon = null;
		try {
			cachedCoupon = om.readValue(value, Coupon.class);
		} catch (IOException e) {
			e.printStackTrace();
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
			return EisError.dataVerifyFail.id;
		}
		if(cachedCoupon.getCurrentStatus() != Coupon.STATUS_LOCKED){
			logger.error("卡券[" + _oldCoupon.getCouponId() + "]状态不是锁定状态，而是:" + cachedCoupon.getCurrentStatus() + "，无效");
			return EisError.dataVerifyFail.id;
		}
		logger.debug("卡券[" + _oldCoupon.getCouponId() + "]属于用户[" + cachedCoupon.getUuid() + "]并且有效");
		couponService.update(_oldCoupon);

		coupon = _oldCoupon;
		coupon.setCouponId(_oldCoupon.getCouponId());

		return OperateResult.success.getId();
	}

	
	@Override
	public Coupon lock(Coupon coupon){

		coupon.setCurrentStatus(TransactionStatus.auctionSuccess.id);


		String key = LOCKED_COUPON_PREFIX + "#" + coupon.getOwnerId() + "#" + coupon.getCouponCode() + "#" + coupon.getCouponSerialNumber() + "#" + coupon.getCouponPassword();
		String value = JsonUtils.toStringApi(coupon);
		
		boolean setSuccess = centerDataService.setIfNotExist(key, value, LOCK_TTL);		
		if(!setSuccess){
			logger.error("无法添加LOCK缓存[" + key + "]，无法设置卡券[" + coupon.getCouponId() + "]为用户[" +  coupon.getUuid() + "]所有");
			return null;
		}

		logger.debug("卡券[" + coupon + "]已被设置为用户=" + coupon.getUuid() + ",渠道=" + coupon.getInviter());		
		couponService.update(coupon);
		return coupon;
	}
}
