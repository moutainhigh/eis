package com.maicard.stat.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.stat.criteria.WithdrawStatCriteria;
import com.maicard.stat.domain.WithdrawStat;

public interface WithdrawStatDao {

	
	List<WithdrawStat> listOnPage(WithdrawStatCriteria withdrawStatCriteria) throws DataAccessException;		

	int count(WithdrawStatCriteria withdrawStatCriteria);

	List<WithdrawStat> list(WithdrawStatCriteria withdrawStatCriteria);

	void calculateProfit();
}
