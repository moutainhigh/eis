package com.maicard.money.service;

import java.util.List;

import com.maicard.money.criteria.CouponModelCriteria;
import com.maicard.money.domain.CouponModel;

public interface CouponModelService {
	int insert(CouponModel couponModel);

	int update(CouponModel couponModel);

	int delete(long couponModelId);
	
	CouponModel select(long couponModelId);
	
	List<CouponModel> list(CouponModelCriteria couponModelCriteria);

	List<CouponModel> listOnPage(CouponModelCriteria couponModelCriteria);

	int count(CouponModelCriteria couponModelCriteria);

	CouponModel select(String couponCode, long ownerId);

}
