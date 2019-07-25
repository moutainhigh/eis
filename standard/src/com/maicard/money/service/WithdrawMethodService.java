package com.maicard.money.service;

import java.util.List;

import com.maicard.money.criteria.WithdrawMethodCriteria;
import com.maicard.money.domain.WithdrawMethod;

public interface WithdrawMethodService {

	int insert(WithdrawMethod withdrawMethod);

	int update(WithdrawMethod withdrawMethod);

	int delete(int withdrawMethodId);
	
	WithdrawMethod select(int withdrawMethodId);

	List<WithdrawMethod> list(WithdrawMethodCriteria withdrawMethodCriteria);

	List<WithdrawMethod> listOnPage(WithdrawMethodCriteria withdrawMethodCriteria);
	
	int count(WithdrawMethodCriteria withdrawMethodCriteria);


}
