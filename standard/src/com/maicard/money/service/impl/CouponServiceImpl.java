package com.maicard.money.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.GlobalOrderIdService;
import com.maicard.mb.service.MessageService;
import com.maicard.money.dao.CouponDao;
import com.maicard.money.service.CouponModelService;
import com.maicard.money.service.CouponProcessor;
import com.maicard.money.service.CouponService;
import com.maicard.product.criteria.ActivityCriteria;
import com.maicard.product.domain.Activity;
import com.maicard.product.service.ActivityService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.EisError;
import com.maicard.standard.TransactionStandard.TransactionType;
import com.maicard.money.criteria.CouponCriteria;
import com.maicard.money.criteria.CouponModelCriteria;
import com.maicard.money.domain.Coupon;
import com.maicard.money.domain.CouponModel;

@Service
public class CouponServiceImpl extends BaseService implements CouponService {

	@Resource
	private CouponDao couponDao;

	@Resource
	private MessageService messageService;


	@Resource
	private ActivityService activityService;

	@Resource
	private ApplicationContextService applicationContextService;

	@Resource
	private CouponModelService couponModelService;

	@Resource
	private GlobalOrderIdService globalOrderIdService;


	//private final boolean usingActivityForCoupon = true;

	@Override
	public int insert(Coupon coupon) {
		if(coupon.getFetchTime() == null){
			coupon.setFetchTime(new Date());
		}
		if(coupon.getTransactionId() == null){
			coupon.setTransactionId(globalOrderIdService.generate(TransactionType.coupon.getId()));
		}
		int rs = 0;
		try{
			rs = couponDao.insert(coupon);
		}catch(Exception e){
			logger.error("插入数据失败:" + e.getMessage());
		}
		return rs;
	}

	@Override
	public int update(Coupon coupon) {
		int rs = 0;
		try{
			rs =  couponDao.update(coupon);

		}catch(Exception e){
			logger.error("更新数据失败:" + e.getMessage());
		}

		return rs;

	}

	@Override
	public int delete(long couponId) {
		try{
			return  couponDao.delete(couponId);
		}catch(Exception e){
			logger.error("删除数据失败:" + e.getMessage());
		}
		return -1;	
	}

	@Override
	public Coupon select(long couponId) {
		Coupon coupon =  couponDao.select(couponId);
		if(coupon == null){
			coupon = new Coupon();
		}
		return coupon;
	}




	@Override
	public List<Coupon> list(CouponCriteria couponCriteria) {
		return couponDao.list(couponCriteria);

	}

	@Override
	public List<Coupon> listOnPage(CouponCriteria couponCriteria) {
		return couponDao.listOnPage(couponCriteria);

	}

	@Override
	public int count(CouponCriteria couponCriteria) {
		return couponDao.count(couponCriteria);
	}

	

	/**
	 * 把点券发布到中央缓存以便于其他节点获取
	 */
	@Override
	public void couponPublish(){




	}

	@Override
	public EisMessage fetch(CouponCriteria couponCriteria) {

		CouponModel couponModel  = null;
		CouponProcessor bean = null;
		if(couponCriteria.getCouponModelId() > 0){
			couponModel = couponModelService.select(couponCriteria.getCouponModelId());
		} else {
			CouponModelCriteria couponModelCriteria = new CouponModelCriteria(couponCriteria.getOwnerId());
			couponModelCriteria.setCouponCode(couponCriteria.getCouponCode());
			couponModelCriteria.setCurrentStatus(BasicStatus.normal.getId());
			List<CouponModel> couponModelList = couponModelService.list(couponModelCriteria);
			if(couponModelList == null || couponModelList.size() < 1){
				logger.warn("找不到点券编码为[" + couponCriteria.getCouponCode() + "]的点券产品");
				return new EisMessage(EisError.productNotExist.id, "找不到指定的卡券产品");

			} else if(couponModelList.size() != 1){
				logger.warn("点券编码为[" + couponCriteria.getCouponCode() + "]的点券产品数量异常，为" + couponModelList.size());
				return new EisMessage(EisError.productNotExist.id, "找不到指定的卡券产品");
			} else {
				couponModel = couponModelList.get(0);
			}
		}
		if(couponModel == null){
			logger.warn("根据条件[couponModelId=" + couponCriteria.getCouponModelId() + ",couponCode=" + couponCriteria.getCouponCode() + "]找不到任何点券产品");
			return new EisMessage(EisError.productNotExist.id, "找不到指定的卡券产品");
		}
		if(StringUtils.isBlank(couponModel.getProcessor())){
			logger.error("点券产品[" + couponModel.getCouponModelId() + "]的处理器为空");
			return new EisMessage(EisError.processorIsNull.id, "找不到指定的处理器");
		}
		logger.debug("通过点券编码为[" + couponModel.getCouponModelId() + "]找到对应产品处理器是:" + couponModel.getProcessor());
		bean = applicationContextService.getBeanGeneric(couponModel.getProcessor());





		if(bean == null){
			if(couponCriteria.getActivityId() < 1){
				logger.error("通过点券编码为[" + couponModel.getCouponCode() + "]找不到到对应产品处理器[" + couponModel.getProcessor() + "]，也未提供活动编码");
				return new EisMessage(EisError.processorIsNull.id, "找不到指定的处理器");
			}
			logger.debug("通过点券编码为[" + couponModel.getCouponCode() + "]找不到到对应产品处理器[" + couponModel.getProcessor() + "]，尝试通过活动[" + couponCriteria.getActivityId() + "]进行查找");
			Activity activity = activityService.select(couponCriteria.getActivityId());
			if(activity == null){
				logger.error("找不到点券活动:" + couponCriteria.getActivityId());
				return new EisMessage(EisError.activityNotExist.id, "找不到指定的活动");
			}
			if(activity.getOwnerId() != couponCriteria.getOwnerId()){
				logger.error("点券活动:" + couponCriteria.getActivityId() + "对应的ownerId[" + activity.getOwnerId() + "]与条件中的[" + couponCriteria.getOwnerId() + "]不一致");
				return new EisMessage(EisError.activityNotExist.id, "找不到指定的活动");

			}
			if(activity.getCurrentStatus() != BasicStatus.normal.getId()){
				logger.error("点券对应的活动:" + couponCriteria.getActivityId() + "状态异常:" + activity.getCurrentStatus());
				return new EisMessage(EisError.statusAbnormal.id, "活动状态异常");
			}
			if(!activity.getActivityType().equals(ActivityCriteria.ACTIVITY_TYPE_COUPON)){
				logger.error("点券对应活动:" + couponCriteria.getActivityId() + "不是一个点券活动类型");
				return new EisMessage(EisError.typeError.id, "状态异常");
			}
			if(activity.getProcessor() == null){
				logger.error("点券活动:" + couponCriteria.getActivityId() + "的处理器为空");
				return new EisMessage(EisError.activityNotExist.id, "找不到指定的活动");
			}
			if(activity.getAccountLimit() > 0){
				//检查该用户是否已经领取了点券
				CouponCriteria fetchedCouponCriteria = new CouponCriteria();
				fetchedCouponCriteria.setUuid(couponCriteria.getUuid());
				fetchedCouponCriteria.setCouponCode(couponCriteria.getCouponCode());
				int fetchedCount = count(fetchedCouponCriteria);
				if(fetchedCount >= activity.getAccountLimit()){
					logger.info("用户[" + couponCriteria.getUuid() + "已领取过点券[" + couponCriteria.getCouponCode() + "]" + fetchedCount + "次，超过该点券可领取次数[" + activity.getAccountLimit() + ",不能再次已领取");
					return new EisMessage(EisError.COUNT_LIMIT_EXCEED.id, "已超过领取次数");
				}
			} else {
				logger.debug("点券[" + couponCriteria.getCouponCode() + "]没有领取限制");
			}
			bean = applicationContextService.getBeanGeneric(activity.getProcessor());
			if(bean == null){
				logger.error("找不到点券活动:" + couponCriteria.getActivityId() + "的处理器:" + activity.getProcessor());
				return new EisMessage(EisError.processorIsNull.id, "找不到指定的处理器");
			}
		}
		
		

		return bean.fetch(couponCriteria);
		
	}

}
