package com.maicard.money.service;

import java.util.List;

import com.maicard.money.criteria.MoneyBalanceCriteria;
import com.maicard.money.domain.MoneyBalance;


public interface MoneyBalanceService {

	int insert(MoneyBalance moneyBalance);

	int update(MoneyBalance moneyBalance);

	int delete(int moneyBalanceId);
		
	MoneyBalance select(int moneyBalanceId);
	
	List<MoneyBalance> list(MoneyBalanceCriteria moneyBalanceCriteria);

	List<MoneyBalance> listOnPage(MoneyBalanceCriteria moneyBalanceCriteria);

	int count(MoneyBalanceCriteria moneyBalanceCriteria);
	
	
	
}
