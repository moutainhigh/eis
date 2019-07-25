package com.maicard.product.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang.time.DateUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maicard.annotation.ProcessMessageObject;
import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ConfigService;
import com.maicard.mb.service.EisMessageListener;
import com.maicard.mb.service.MessageService;
import com.maicard.product.criteria.NotifyLogCriteria;
import com.maicard.product.dao.NotifyLogDao;
import com.maicard.product.domain.NotifyLog;
import com.maicard.product.service.NotifyLogService;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.ObjectType;
import com.maicard.standard.Operate;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@ProcessMessageObject({"notifyLog"})
public class NotifyLogServiceImpl extends BaseService implements NotifyLogService,EisMessageListener{

	@Resource
	private NotifyLogDao notifyLogDao;
	@Resource
	private ConfigService configService;
	@Resource
	private MessageService messageService;

	private boolean handlerPayNotify = false;
	private String messageBusName;
	private int keepNotifyLogDay;

	@PostConstruct
	public void init(){		
		messageBusName = configService.getValue(DataName.messageBusTransaction.toString(),0);
		handlerPayNotify = configService.getBooleanValue(DataName.handlerPayNotify.toString(),0);
		keepNotifyLogDay = configService.getIntValue(DataName.keepNotifyLogDay.toString(),0);
		if(keepNotifyLogDay == 0){
			keepNotifyLogDay = 10;
		}
	}
	@Override
	public int insert(NotifyLog notifyLog) {
		if(handlerPayNotify){
			if( insertLocal(notifyLog) == 1){
				messageService.sendJmsDataSyncMessage(messageBusName, "notifyLogService", "insertLocal", notifyLog);
				logger.debug("操作日志已插入数据库，并发送同步请求[notifyLogService.insertLocal(notifyLog)]");
				return 1;
			}
			return 0;
		}
		return _insertRemote(notifyLog);
	}

	@Override
	public int insertLocal(NotifyLog notifyLog){
		return notifyLogDao.insert(notifyLog);

	}

	private int _insertRemote(NotifyLog notifyLog) {
		EisMessage m = new EisMessage();
		m.setOperateCode(Operate.create.getId());
		m.setAttachment(new HashMap<String,Object>());
		m.getAttachment().put("notifyLog", notifyLog);	
		m.setObjectType(ObjectType.notifyLog.toString());
		messageService.send(messageBusName, m);
		m = null;
		return 1;
	}
	@Override
	public List<NotifyLog> list(NotifyLogCriteria notifyLogCriteria) {
		return notifyLogDao.list(notifyLogCriteria);
	}


	@Override
	public List<NotifyLog> getFailedNotify(NotifyLogCriteria notifyLogCriteria){		
		return notifyLogDao.getFailedNotify(notifyLogCriteria);
	}

	@Override
	public List<NotifyLog> getUnSendNotify(NotifyLogCriteria notifyLogCriteria){		
		return notifyLogDao.getUnSendNotify(notifyLogCriteria);
	}


	@Override
	public int count(NotifyLogCriteria notifyLogCriteria) {
		return notifyLogDao.count(notifyLogCriteria);
	}

	@Override
	@Async
	public void onMessage(EisMessage eisMessage) {
		if(!handlerPayNotify){
			if(logger.isDebugEnabled()){
				logger.debug("本节点不负责处理notifyLog业务");
			}
			return;
		}
		if(eisMessage == null){
			logger.error("得到的消息是空");
			return;
		}
		if(eisMessage.getObjectType() == null){
			eisMessage = null;
			return;
		}
		if(!eisMessage.getObjectType().equals(ObjectType.notifyLog.toString()) ){
			eisMessage = null;
			return ;
		}
		if(eisMessage.getAttachment() == null){
			if(logger.isDebugEnabled()){
				logger.debug("消息中没有附件");
			}
			eisMessage = null;
			return;
		}
		NotifyLog notifyLog = null;
		Object object = eisMessage.getAttachment().get("notifyLog");
		if(object instanceof NotifyLog){
			notifyLog = (NotifyLog)object;
		} else if(object instanceof LinkedHashMap){
			ObjectMapper om = new ObjectMapper();
			om.setDateFormat(new SimpleDateFormat(CommonStandard.defaultDateFormat));
			String textData = null;
			try{
				textData = om.writeValueAsString(object);
				notifyLog = om.readValue(textData, NotifyLog.class);
			}catch(Exception e){}
			
		}
		
		if(eisMessage.getOperateCode() == Operate.create.getId()){
			if(notifyLog == null){
				if(logger.isDebugEnabled()){
					logger.debug("消息中没有找到需要的对象notifyLog");
				}
				eisMessage = null;
				return;
			}

			try {
				this.insertLocal(notifyLog);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}		
	}

	@Override
	public void cleanOldNotifyLog(){
		NotifyLogCriteria notifyLogCriteria = new NotifyLogCriteria();
		notifyLogCriteria.setEndTime(DateUtils.truncate(DateUtils.addDays(new Date(), -keepNotifyLogDay),Calendar.DAY_OF_MONTH));
		int rs = notifyLogDao.cleanOldNotifyLog(notifyLogCriteria);
		if(logger.isDebugEnabled()){
			logger.debug("清除[" + new SimpleDateFormat(CommonStandard.defaultDateFormat).format(notifyLogCriteria.getEndTime()) + "]之前的通知日志" + rs + "条");
		}
	}

}
