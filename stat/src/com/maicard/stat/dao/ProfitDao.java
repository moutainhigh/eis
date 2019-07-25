package com.maicard.stat.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.stat.criteria.ProfitCriteria;
import com.maicard.stat.domain.Profit;

public interface ProfitDao {

	
	List<Profit> listOnPage(ProfitCriteria profitCriteria) throws DataAccessException;		

	int count(ProfitCriteria profitCriteria);

	List<Profit> list(ProfitCriteria profitCriteria);
	
	int insert(Profit profit);
	
	int deleteBy(ProfitCriteria profitCriteria);
	
	

}
