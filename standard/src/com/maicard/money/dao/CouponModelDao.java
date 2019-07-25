package com.maicard.money.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.money.criteria.CouponModelCriteria;
import com.maicard.money.domain.CouponModel;


public interface CouponModelDao {
	int insert(CouponModel couponModel) throws DataAccessException;

	int update(CouponModel couponModel) throws DataAccessException;

	int delete(long couponModelId) throws DataAccessException;

	CouponModel select(long couponModelId) throws DataAccessException;
	
	List<CouponModel> list(CouponModelCriteria couponModelCriteria) throws DataAccessException;
	
	List<CouponModel> listOnPage(CouponModelCriteria couponModelCriteria) throws DataAccessException;
	
	int count(CouponModelCriteria couponModelCriteria) throws DataAccessException;
}
