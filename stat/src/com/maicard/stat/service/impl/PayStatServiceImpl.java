package com.maicard.stat.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseService;
import com.maicard.standard.CommonStandard;
import com.maicard.stat.criteria.PayStatCriteria;
import com.maicard.stat.dao.PayStatDao;
import com.maicard.stat.domain.PayStat;
import com.maicard.stat.service.PayStatService;


@Service
public class PayStatServiceImpl extends BaseService implements PayStatService {
	@Resource
	private PayStatDao payStatDao;


	/*public void setPayStatDao(PayStatDao payStatDao) {
		this.payStatDao = payStatDao;
	}
	 */

	@Override
	public List<PayStat> listOnPage(PayStatCriteria payStatCriteria) {

		if(payStatCriteria == null){
			return Collections.emptyList();
		}
		
		return payStatDao.listOnPage(payStatCriteria);
	}
	@Override
	public List<PayStat> list(PayStatCriteria payStatCriteria) {
		if(payStatCriteria == null){
			return Collections.emptyList();
		}
		return payStatDao.list(payStatCriteria);
	}



	@Override
	public int count(PayStatCriteria payStatCriteria)
	{
		//查询开始时间
		if(payStatCriteria.getQueryBeginTime() == null){
			//如果没设置起始时间，设置为一周前
			payStatCriteria.setQueryBeginTime(DateUtils.truncate(DateUtils.addDays(new Date(), -7),Calendar.DAY_OF_MONTH));
		}
		return payStatDao.count(payStatCriteria);    	
	}
	
	@Override
	public void calculateProfit(){
		payStatDao.calculateProfit();
		
	}
	@Override
	public void statistic(PayStatCriteria payStatCriteria) {
		Assert.notNull(payStatCriteria,"尝试执行统计的条件不能为空");
		Assert.notNull(payStatCriteria.getQueryBeginTime(),"尝试执行统计的开始时间不能为空");
		Assert.notNull(payStatCriteria.getQueryEndTime(),"尝试执行统计的结束时间不能为空");
		SimpleDateFormat sdf = new SimpleDateFormat(CommonStandard.statHourFormat);
		payStatCriteria.setStatisticTimeBegin(sdf.format(payStatCriteria.getQueryBeginTime()));
		payStatCriteria.setStatisticTimeEnd(sdf.format(payStatCriteria.getQueryEndTime()));
		logger.info("重新执行以下时间段的统计:" + payStatCriteria.getStatisticTimeBegin() + "=>" + payStatCriteria.getStatisticTimeEnd());
		payStatDao.statistic(payStatCriteria);
		
	}

}
