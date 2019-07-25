package com.maicard.ec.dao;

import java.util.List;

import com.maicard.ec.criteria.FeeAdjustCriteria;
import com.maicard.ec.domain.FeeAdjust;


public interface FeeAdjustDao {
	public int insert(FeeAdjust feeAdjust);

	public int update(FeeAdjust feeAdjust);

	public List<FeeAdjust> list(FeeAdjustCriteria feeAdjustCriteria);

	public List<FeeAdjust> listOnPage(FeeAdjustCriteria feeAdjustCriteria);

	int delete(long feeAdjustId);
	
	int count(FeeAdjustCriteria feeAdjustCriteria);

	public FeeAdjust select(long  feeAdjustId);

}
