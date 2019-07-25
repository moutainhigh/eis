package com.maicard.stat.service.impl.task;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ConfigService;
import com.maicard.product.criteria.ItemCriteria;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.stat.service.ItemMoveToHistoryJob;
import com.maicard.stat.service.ItemStatService;

/**
 * 对指定时间之前的购买商品进行清理，移动到历史表
 * 
 * 由自行自动执行改为由Quarts引擎调用
 * @update 2013-10-23
 * 
 *  
 * @author NetSnake
 * @date 2013-3-9
 */
@Service
public class ItemMoveToHistoryJobImpl extends BaseService implements  ItemMoveToHistoryJob{


	@Resource
	private ConfigService configService;
	@Resource
	private ItemStatService itemStatService;


	boolean handleJob = false;

	@PostConstruct
	public void init(){
		boolean handlerJmsDataSyncToLocal = configService.getBooleanValue(DataName.handlerJmsDataSyncToLocal.toString(),0);
		boolean handlerItemStat = configService.getBooleanValue(DataName.handlerItemStat.toString(),0);
		if(handlerJmsDataSyncToLocal || handlerItemStat){
			handleJob = true;
		}

	}



	@Override
	public EisMessage start() {
		if(handleJob){
			cleanBeforeDay();
		} else {
			logger.info("本节点不负责移动交易到历史表");
		}
		return null;
	}

	@Override
	public EisMessage stop() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EisMessage status() {
		// TODO Auto-generated method stub
		return null;
	}



	//把早于系统设置时间的所有交易移到历史表
	@SuppressWarnings("unused")
	private void cleanBeforeDay(){
		if(CommonStandard.itemMoveToHistoryDay < 1){
			logger.info("系统指定的item历史时间是0，不做任何操作");
		}
		SimpleDateFormat sdf = new SimpleDateFormat(CommonStandard.defaultDateFormat);

		ItemCriteria itemCriteria = new ItemCriteria();
		itemCriteria.setEnterTimeBegin(DateUtils.addDays(new Date(), -(CommonStandard.itemMoveToHistoryDay)));
		itemCriteria.setEnterTimeEnd(DateUtils.addDays(new Date(), -(CommonStandard.itemMoveToHistoryDay)));
		itemCriteria.setTableName(new SimpleDateFormat("MM").format(itemCriteria.getEnterTimeBegin()));
		logger.debug("把时间位于[" + sdf.format(itemCriteria.getEnterTimeBegin()) + "]到[" + sdf.format(itemCriteria.getEnterTimeEnd()) + "]之间的item数据移动到历史表[" + itemCriteria.getTableName() + "]");
		try{
			itemStatService.moveToHistory(itemCriteria);
		}catch(Exception e){
			logger.error("在执行清理任务时发生错误:" + e.getMessage());
			e.printStackTrace();
		}

	}

	//把上一个小时的所有已完成交易[成功或者失败]移到历史表
	/*private void cleanLastHour(){
		ItemCriteria itemCriteria = new ItemCriteria();
		itemCriteria.setBeginTime(new SimpleDateFormat(CommonStandard.statHourFormat.toString()).format(DateUtils.addHours(new Date(), -2)));
		itemCriteria.setEndTime(new SimpleDateFormat(CommonStandard.statHourFormat.toString()).format(DateUtils.addHours(new Date(), -1)));
		try{
			itemService.moveToHistory(itemCriteria);
		}catch(Exception e){
			logger.error("在执行清理任务时发生错误:" + e.getMessage());
			e.printStackTrace();
		}
	}*/

	@Override
	public EisMessage start(String objectType, int... objectIds) {
		// TODO Auto-generated method stub
		return null;
	}

}
