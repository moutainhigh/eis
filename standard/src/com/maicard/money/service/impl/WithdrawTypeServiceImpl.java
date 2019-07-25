package com.maicard.money.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.money.criteria.WithdrawTypeCriteria;
import com.maicard.money.dao.WithdrawTypeDao;
import com.maicard.money.domain.WithdrawType;
import com.maicard.money.service.WithdrawTypeService;

@Service
public class WithdrawTypeServiceImpl extends BaseService implements WithdrawTypeService {

	@Resource
	private WithdrawTypeDao withdrawTypeDao;


	public int insert(WithdrawType withdrawType) {
		return withdrawTypeDao.insert(withdrawType);
	}

	public int update(WithdrawType withdrawType) {
		int actualRowsAffected = 0;
		
		int withdrawTypeId = withdrawType.getWithdrawTypeId();

		WithdrawType _oldWithdrawType = withdrawTypeDao.select(withdrawTypeId);
		
		if (_oldWithdrawType != null) {
			actualRowsAffected = withdrawTypeDao.update(withdrawType);
		}
		
		return actualRowsAffected;
	}

	public int delete(int withdrawTypeId) {
		int actualRowsAffected = 0;
		
		WithdrawType _oldWithdrawType = withdrawTypeDao.select(withdrawTypeId);
		
		if (_oldWithdrawType != null) {
			actualRowsAffected = withdrawTypeDao.delete(withdrawTypeId);
		}
		
		return actualRowsAffected;
	}
	
	//@Cacheable(value = CommonStandard.cacheNameSupport, key = "'WithdrawType#' + #withdrawTypeId")
	public WithdrawType select(int withdrawTypeId) {
		return withdrawTypeDao.select(withdrawTypeId);
	}

	public List<WithdrawType> list(WithdrawTypeCriteria withdrawTypeCriteria) {
		return withdrawTypeDao.list(withdrawTypeCriteria);
	}
	
	public List<WithdrawType> listOnPage(WithdrawTypeCriteria withdrawTypeCriteria) {
		return withdrawTypeDao.listOnPage(withdrawTypeCriteria);
	}
	
	public int count(WithdrawTypeCriteria withdrawTypeCriteria) {
		return withdrawTypeDao.count(withdrawTypeCriteria);
	}

	@Override
	public WithdrawType selectByColumn(WithdrawTypeCriteria withdrawTypeCriteria) {
		return withdrawTypeDao.selectByColumn(withdrawTypeCriteria);
	}
}
