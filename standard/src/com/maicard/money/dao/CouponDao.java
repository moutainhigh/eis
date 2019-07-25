package com.maicard.money.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.money.criteria.CouponCriteria;
import com.maicard.money.domain.Coupon;


public interface CouponDao {
	int insert(Coupon coupon) throws DataAccessException;

	int update(Coupon coupon) throws DataAccessException;

	int delete(long couponId) throws DataAccessException;

	Coupon select(long couponId) throws DataAccessException;
	
	List<Coupon> list(CouponCriteria couponCriteria) throws DataAccessException;
	
	List<Coupon> listOnPage(CouponCriteria couponCriteria) throws DataAccessException;
	
	int count(CouponCriteria couponCriteria) throws DataAccessException;
}
