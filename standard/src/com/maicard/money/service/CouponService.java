package com.maicard.money.service;

import java.util.List;

import com.maicard.common.domain.EisMessage;
import com.maicard.money.criteria.CouponCriteria;
import com.maicard.money.domain.Coupon;

public interface CouponService {
	int insert(Coupon coupon);

	int update(Coupon coupon);

	int delete(long couponId);
	
	Coupon select(long couponId);
	
	List<Coupon> list(CouponCriteria couponCriteria);

	List<Coupon> listOnPage(CouponCriteria couponCriteria);

	int count(CouponCriteria couponCriteria);


		
	void couponPublish();
	/**
	 * 根据条件返回一组coupon<br/>
	 * 如果未能成功获取，则返回一个EisMessage告知失败原因<br/>
	 * 注意！不能返回一个null<br/>
	 * 如果成功，则该EisMessage中放入一个couponList对象，为获取到的coupon列表<br/>
	 * 就算只有单个coupon，也是一个couponList列表对象<br/>
	 * @param couponCriteria
	 * @return
	 */
	EisMessage fetch(CouponCriteria couponCriteria);
	


}
