package com.maicard.money.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.money.criteria.WithdrawMethodCriteria;
import com.maicard.money.dao.WithdrawMethodDao;
import com.maicard.money.domain.WithdrawMethod;
import com.maicard.money.service.WithdrawMethodService;

@Service
public class WithdrawMethodServiceImpl extends BaseService implements WithdrawMethodService {

	@Resource
	private WithdrawMethodDao withdrawMethodDao;
	
	final String DEFAULT_ORDER_BY = "weight DESC, pay_method_id ASC";


	@Override
	public int insert(WithdrawMethod withdrawMethod) {
		return withdrawMethodDao.insert(withdrawMethod);
	}

	@Override
	public int update(WithdrawMethod withdrawMethod) {
		int actualRowsAffected = 0;
		
		int withdrawMethodId = withdrawMethod.getWithdrawMethodId();

		WithdrawMethod _oldWithdrawMethod = withdrawMethodDao.select(withdrawMethodId);
		
		if (_oldWithdrawMethod != null) {
			actualRowsAffected = withdrawMethodDao.update(withdrawMethod);
		}
		
		return actualRowsAffected;
	}

	@Override
	public int delete(int withdrawMethodId) {
		int actualRowsAffected = 0;
		
		WithdrawMethod _oldWithdrawMethod = withdrawMethodDao.select(withdrawMethodId);
		
		if (_oldWithdrawMethod != null) {
			actualRowsAffected = withdrawMethodDao.delete(withdrawMethodId);
		}
		
		return actualRowsAffected;
	}
	
	@Override
	public WithdrawMethod select(int withdrawMethodId) {
		return withdrawMethodDao.select(withdrawMethodId);
	}

	@Override
	public List<WithdrawMethod> list(WithdrawMethodCriteria withdrawMethodCriteria) {
		if(StringUtils.isBlank(withdrawMethodCriteria.getOrderBy())){
			withdrawMethodCriteria.setOrderBy(DEFAULT_ORDER_BY);
				
		}
		return withdrawMethodDao.list(withdrawMethodCriteria);
	}
	
	@Override
	public List<WithdrawMethod> listOnPage(WithdrawMethodCriteria withdrawMethodCriteria) {
		if(StringUtils.isBlank(withdrawMethodCriteria.getOrderBy())){
			withdrawMethodCriteria.setOrderBy(DEFAULT_ORDER_BY);		
		}
		return withdrawMethodDao.listOnPage(withdrawMethodCriteria);
	}
	
	@Override
	public int count(WithdrawMethodCriteria withdrawMethodCriteria) {
		return withdrawMethodDao.count(withdrawMethodCriteria);
	}

	
}
