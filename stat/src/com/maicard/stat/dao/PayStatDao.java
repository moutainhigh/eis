package com.maicard.stat.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.stat.criteria.PayStatCriteria;
import com.maicard.stat.domain.PayStat;

public interface PayStatDao {

	
	List<PayStat> listOnPage(PayStatCriteria payStatCriteria) throws DataAccessException;		

	int count(PayStatCriteria payStatCriteria);

	List<PayStat> list(PayStatCriteria payStatCriteria);

	void calculateProfit();

	void statistic(PayStatCriteria payStatCriteria);
}
