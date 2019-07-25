package com.maicard.money.service;

import java.util.List;

import com.maicard.money.criteria.PointExchangeLogCriteria;
import com.maicard.money.domain.PointExchange;

public interface PointExchangeLogService {

	int insert(PointExchange pointExchangeLog);

	int update(PointExchange pointExchangeLog) throws Exception;

	int delete(int  pointExchangeLogId);
	
	PointExchange select(int pointExchangeLogId);
	
	List<PointExchange> list(PointExchangeLogCriteria pointExchangeLogCriteria);

	List<PointExchange> listOnPage(PointExchangeLogCriteria pointExchangeLogCriteria);
		
	int count(PointExchangeLogCriteria pointExchangeLogCriteria);	

	

}
