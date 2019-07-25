package com.maicard.money.service;

import java.util.List;

import com.maicard.money.criteria.PointExchangeCriteria;
import com.maicard.money.domain.PointExchange;
import com.maicard.product.domain.Item;

public interface PointExchangeService {

	int insert(PointExchange pointExchange);

	int update(PointExchange pointExchange);

	int delete(int pointExchangeId);
	
	PointExchange select(int pointExchangeId);

	List<PointExchange> list(PointExchangeCriteria pointExchangeCriteria);

	List<PointExchange> listOnPage(PointExchangeCriteria pointExchangeCriteria);

	int count(PointExchangeCriteria pointExchangeCriteria);

	int begin(PointExchange pointExchange);

	int applyPrice(Item item);

	int applyPrice(Item item, PointExchange pointExchange);


}
