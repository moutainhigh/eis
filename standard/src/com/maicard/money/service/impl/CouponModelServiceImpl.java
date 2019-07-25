package com.maicard.money.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.money.dao.CouponModelDao;
import com.maicard.money.service.CouponModelService;
import com.maicard.product.service.ActivityService;
import com.maicard.money.criteria.CouponModelCriteria;
import com.maicard.money.domain.CouponModel;

@Service
public class CouponModelServiceImpl extends BaseService implements CouponModelService {

	@Resource
	private CouponModelDao couponModelDao;

	@Resource
	private ActivityService activityService;


	@Override
	public int insert(CouponModel couponModel) {
		try{
			return couponModelDao.insert(couponModel);
		}catch(Exception e){
			logger.error("插入数据失败了1:" + e.getMessage());
		}
		return -1;
	}

	@Override
	public int update(CouponModel couponModel) {
		try{
			return  couponModelDao.update(couponModel);
		}catch(Exception e){
			logger.error("更新数据失败:" + e.getMessage());
		}
		return -1;

	}

	@Override
	public int delete(long couponModelId) {
		try{
			return  couponModelDao.delete(couponModelId);
		}catch(Exception e){
			logger.error("删除数据失败:" + e.getMessage());
		}
		return -1;	
	}

	@Override
	public CouponModel select(long couponModelId) {
		CouponModel couponModel =  couponModelDao.select(couponModelId);
		if(couponModel == null){
			couponModel = new CouponModel();
		}
		return couponModel;
	}




	@Override
	public List<CouponModel> list(CouponModelCriteria couponModelCriteria) {
		return couponModelDao.list(couponModelCriteria);

	}

	@Override
	public List<CouponModel> listOnPage(CouponModelCriteria couponModelCriteria) {
		return couponModelDao.listOnPage(couponModelCriteria);

	}

	@Override
	public int count(CouponModelCriteria couponModelCriteria) {
		return couponModelDao.count(couponModelCriteria);
	}

	@Override
	public CouponModel select(String couponCode, long ownerId) {
		CouponModelCriteria couponModelCriteria = new CouponModelCriteria(ownerId);
		couponModelCriteria.setCouponCode(couponCode);
		List<CouponModel> couponModelList = list(couponModelCriteria);
		if(couponModelList == null || couponModelList.size() < 1){
			return null;
		}
		return couponModelList.get(0);
	}


}
