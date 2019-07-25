package com.maicard.ec.service;

import java.util.List;

import com.maicard.ec.criteria.FeeAdjustCriteria;
import com.maicard.ec.domain.FeeAdjust;



public interface FeeAdjustService {
	
	public FeeAdjust select(long feeAdjustId);
	
	public int insert(FeeAdjust feeAdjust);
	
	public int update(FeeAdjust feeAdjust);
	
	public int delete(long feeAdjustId);
	
	public List<FeeAdjust> list(FeeAdjustCriteria feeAdjustCriteria);

	int count(FeeAdjustCriteria feeAdjustCriteria);

	public List<FeeAdjust> listOnPage(FeeAdjustCriteria feeAdjustCriteria);





}
