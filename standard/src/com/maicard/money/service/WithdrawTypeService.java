package com.maicard.money.service;

import java.util.List;

import com.maicard.money.criteria.WithdrawTypeCriteria;
import com.maicard.money.domain.WithdrawType;

public interface WithdrawTypeService {

	int insert(WithdrawType withdrawType);

	int update(WithdrawType withdrawType);

	int delete(int withdrawTypeId);
	
	WithdrawType select(int withdrawTypeId);

	List<WithdrawType> list(WithdrawTypeCriteria withdrawTypeCriteria);

	List<WithdrawType> listOnPage(WithdrawTypeCriteria withdrawTypeCriteria);
	
	int count(WithdrawTypeCriteria withdrawTypeCriteria);

	WithdrawType selectByColumn(WithdrawTypeCriteria withdrawTypeCriteria);

}
