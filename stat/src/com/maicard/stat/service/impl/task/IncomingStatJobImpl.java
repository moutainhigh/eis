package com.maicard.stat.service.impl.task;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ConfigService;
import com.maicard.mb.domain.EisTopic;
import com.maicard.mb.domain.UserMessage;
import com.maicard.mb.service.EisTopicService;
import com.maicard.mb.service.UserMessageService;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.stat.criteria.PayStatCriteria;
import com.maicard.stat.dao.IncomingStatDao;
import com.maicard.stat.domain.IncomingStat;
import com.maicard.stat.service.IncomingStatJob;


/**
 * 定期执行上个礼拜的收入统计
 * 
 *
 * 由自行自动执行改为由Quarts引擎调用
 * @update 2013-10-23
 * 
 * 
 * @author NetSnake
 * @date 2012-9-26
 */
@Service
public class IncomingStatJobImpl extends BaseService implements IncomingStatJob{


	@Resource
	private ConfigService configService;
	@Resource
	private IncomingStatDao incomingStatDao;
	@Resource
	private UserMessageService userMessageService;
	@Resource
	private EisTopicService eisTopicService;

	private int billingPeriod;
	private int incomingStatTopicId;
	private String incomingStatTopicCode = "incomingStatTopic";

	@PostConstruct
	public void init(){
		billingPeriod = configService.getIntValue(DataName.billingDefaultPeriod.toString(),0);
		EisTopic topic = eisTopicService.select(incomingStatTopicCode);
		if(topic == null){
			logger.warn("系统未定义收入统计报告的消息订阅Topic");
		} else {
			incomingStatTopicId = topic.getTopicId();
			logger.debug("收入统计报告的消息订阅Topic[" + incomingStatTopicCode + "]的topicId=" + incomingStatTopicId);
		}
	}



	private void startJob(){
		//统计上一个周期的收入数据		
		/*PayStatCriteria payStatCriteria = new PayStatCriteria();
		payStatCriteria.setStartTime(new java.text.SimpleDateFormat(CommonStandard.defaultDateFormat).format( DateUtils.truncate(DateUtils.addDays(new Date(), - billingPeriod), Calendar.DAY_OF_MONTH)));
		payStatCriteria.setEndTime(new java.text.SimpleDateFormat(CommonStandard.defaultDateFormat).format( DateUtils.truncate(new Date(),Calendar.DAY_OF_MONTH)));
		List<IncomingStat> incomingStatList = incomingStatDao.incomingStat(payStatCriteria);
		logger.info("统计[" + payStatCriteria.getStartTime() + "]到[" + payStatCriteria.getEndTime() + "]的收入利润数据, 更新数据行数：" +  (incomingStatList == null ? "空" : incomingStatList.size()) );
		if(incomingStatList != null && incomingStatList.size() > 0){
			StringBuffer sb = new StringBuffer();
			sb.append("收入统计:\n");
			for(IncomingStat incomingStat : incomingStatList){
				sb.append("统计时间:" + incomingStat.getStatTime() + ",流水:" + incomingStat.getTotalMoney() + ", 合作分成:" + incomingStat.getShareCost() + ", 其他分成:" + incomingStat.getOtherCost() + ", 毛利:" + incomingStat.getGrossProfit() + "\n");
				logger.info("统计时间:" + incomingStat.getStatTime() + ",流水:" + incomingStat.getTotalMoney() + ", 合作分成:" + incomingStat.getShareCost() + ", 其他分成:" + incomingStat.getOtherCost() + ", 毛利:" + incomingStat.getGrossProfit());
			}
			if(incomingStatTopicId > 0){
				UserMessage userMessage = new UserMessage();
				userMessage.setTopicId(incomingStatTopicId);
				userMessage.setContent(sb.toString());
				userMessage.setTitle("收入统计[" + payStatCriteria.getStartTime() + "]到[" + payStatCriteria.getEndTime() + "]");
				userMessageService.send(userMessage);
			}
		}*/
	}

	@Override
	public EisMessage start() {
		startJob();
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


	@Override
	public EisMessage start(String objectType, int... objectIds) {
		// TODO Auto-generated method stub
		return null;
	}


}
