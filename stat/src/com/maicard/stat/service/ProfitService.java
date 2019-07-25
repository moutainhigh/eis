package com.maicard.stat.service;

import java.util.List;

import com.maicard.stat.criteria.ProfitCriteria;
import com.maicard.stat.domain.Profit;

public interface ProfitService {

	List<Profit> listOnPage(ProfitCriteria profitCriteria);
	
	int count(ProfitCriteria profitCriteria);

	List<Profit> list(ProfitCriteria profitCriteria);

	int insert(Profit profit);
	
	
	int deleteBy(ProfitCriteria profitCriteria);
	
}
