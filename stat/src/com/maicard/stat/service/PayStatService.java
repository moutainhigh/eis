package com.maicard.stat.service;

import java.util.List;

import com.maicard.stat.criteria.PayStatCriteria;
import com.maicard.stat.domain.PayStat;

public interface PayStatService {

	List<PayStat> listOnPage(PayStatCriteria payStatCriteria);
	
	int count(PayStatCriteria payStatCriteria);

	List<PayStat> list(PayStatCriteria payStatCriteria);

	/**
	 * 请求计算毛利
	 */
	void calculateProfit();

	/**
	 * 执行指定时间段内的统计
	 * @param payStatCriteria
	 */
	void statistic(PayStatCriteria payStatCriteria);
}
