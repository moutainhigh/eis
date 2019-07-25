package com.maicard.money.service;

import java.util.List;

import org.springframework.scheduling.annotation.Async;

import com.maicard.money.criteria.MoneyLogCriteria;
import com.maicard.money.domain.Money;
import com.maicard.money.domain.MoneyLog;

public interface MoneyLogService {

	@Async
	int insert(MoneyLog moneyLog);
	
	List<MoneyLog> list(MoneyLogCriteria moneyLogCriteria);

	List<MoneyLog> listOnPage(MoneyLogCriteria moneyLogCriteria);	
	
	int count(MoneyLogCriteria moneyLogCriteria);

	@Async
	void insert(String op, String memory, Money moneyBefore, Money moneyAfter);


}
