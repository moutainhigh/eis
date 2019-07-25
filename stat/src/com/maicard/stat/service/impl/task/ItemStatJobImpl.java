package com.maicard.stat.service.impl.task;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisMessage;
import com.maicard.standard.CommonStandard;
import com.maicard.stat.criteria.ItemStatCriteria;
import com.maicard.stat.service.ItemStatJob;
import com.maicard.stat.service.ItemStatService;


/**
 * 定期执行消费统计
 * 
 * 由自行自动执行改为由Quarts引擎调用
 * @update 2013-10-23
 * 
 * @author NetSnake
 * @date 2012-6-19
 */
@Service
public class ItemStatJobImpl extends BaseService implements ItemStatJob {
	@Resource
	private ItemStatService itemStatService;



	public void startJob(){
		//统计上一个小时的数据
		String startHour = new java.text.SimpleDateFormat(CommonStandard.statHourFormat).format( DateUtils.addHours(new Date(), -1)); 
		String endHour = new java.text.SimpleDateFormat(CommonStandard.statHourFormat).format(new Date()); 
		ItemStatCriteria itemStatCriteria = new ItemStatCriteria();
		itemStatCriteria.setBeginTime(startHour);
		itemStatCriteria.setEndTime(endHour);
		itemStatCriteria.setTableName("item");
		long start = new Date().getTime();
		try{
			logger.info("统计[" + itemStatCriteria.getBeginTime() + "]到[" + itemStatCriteria.getEndTime() + "]的消费数据,更新数据行数：" +  itemStatService.gather(itemStatCriteria) + ",耗时:" + (new Date().getTime() - start) + "毫秒");
		}catch(Exception e){
			logger.error("统计[" + itemStatCriteria.getBeginTime() + "]到[" + itemStatCriteria.getEndTime() + "]的消费数据时出错:" + e.getMessage());
		}
	}

	@Override
	public EisMessage start() {
		startJob();
		return null;
	}


	@Override
	public EisMessage stop() {
		return null;
	}


	@Override
	public EisMessage status() {
		return null;
	}


	@Override
	public EisMessage start(String objectType, int... objectIds) {
		return null;
	}



}
