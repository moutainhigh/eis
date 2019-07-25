package com.maicard.ec.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.ec.criteria.FeeAdjustCriteria;
import com.maicard.ec.dao.FeeAdjustDao;
import com.maicard.ec.domain.FeeAdjust;
import com.maicard.ec.service.FeeAdjustService;
import com.maicard.common.base.BaseService;

@Service
public class FeeAdjustServiceImpl  extends BaseService implements FeeAdjustService{

	@Resource
	private FeeAdjustDao feeAdjustDao;
	
	
	@Override
	public FeeAdjust select(long feeAdjustId) {
		return feeAdjustDao.select(feeAdjustId);
	}

	@Override
	public int insert(FeeAdjust feeAdjust) {
		return feeAdjustDao.insert(feeAdjust);
	}

	@Override
	public int update(FeeAdjust feeAdjust) {
		return feeAdjustDao.update(feeAdjust);
	}

	@Override
	public int delete(long feeAdjustId) {
		return feeAdjustDao.delete(feeAdjustId);
	}

	@Override
	public List<FeeAdjust> list(FeeAdjustCriteria feeAdjustCriteria) {
		return feeAdjustDao.list(feeAdjustCriteria);
	}

	@Override
	public int count(FeeAdjustCriteria feeAdjustCriteria) {
		return feeAdjustDao.count(feeAdjustCriteria);
	}

	@Override
	public List<FeeAdjust> listOnPage(FeeAdjustCriteria feeAdjustCriteria) {
		return feeAdjustDao.listOnPage(feeAdjustCriteria);
	}


}
