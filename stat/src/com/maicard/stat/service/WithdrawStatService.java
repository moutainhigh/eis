package com.maicard.stat.service;

import java.util.List;

import com.maicard.stat.criteria.WithdrawStatCriteria;
import com.maicard.stat.domain.WithdrawStat;

public interface WithdrawStatService {

	List<WithdrawStat> listOnPage(WithdrawStatCriteria withdrawStatCriteria);
	
	int count(WithdrawStatCriteria withdrawStatCriteria);

	List<WithdrawStat> list(WithdrawStatCriteria withdrawStatCriteria);

	/**
	 * 请求计算毛利
	 */
	void calculateProfit();
}
