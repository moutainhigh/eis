package com.maicard.stat.service.impl;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.stat.criteria.WithdrawStatCriteria;
import com.maicard.stat.dao.WithdrawStatDao;
import com.maicard.stat.domain.WithdrawStat;
import com.maicard.stat.service.WithdrawStatService;

@Service
public class WithdrawStatServiceImpl extends BaseService implements WithdrawStatService {
	@Resource
	private WithdrawStatDao withdrawStatDao;


	/*public void setWithdrawStatDao(WithdrawStatDao withdrawStatDao) {
		this.withdrawStatDao = withdrawStatDao;
	}
	 */

	@Override
	public List<WithdrawStat> listOnPage(WithdrawStatCriteria withdrawStatCriteria) {

		if(withdrawStatCriteria == null){
			return Collections.emptyList();
		}
		
		return withdrawStatDao.listOnPage(withdrawStatCriteria);
	}
	@Override
	public List<WithdrawStat> list(WithdrawStatCriteria withdrawStatCriteria) {
		if(withdrawStatCriteria == null){
			return null;
		}
		return withdrawStatDao.list(withdrawStatCriteria);
	}



	@Override
	public int count(WithdrawStatCriteria withdrawStatCriteria)
	{
		//查询开始时间
		if(withdrawStatCriteria.getQueryBeginTime() == null){
			//如果没设置起始时间，设置为一周前
			withdrawStatCriteria.setQueryBeginTime(DateUtils.truncate(DateUtils.addDays(new Date(), -7),Calendar.DAY_OF_MONTH));
		}
		return withdrawStatDao.count(withdrawStatCriteria);    	
	}
	
	@Override
	public void calculateProfit(){
		withdrawStatDao.calculateProfit();
		
	}

}
