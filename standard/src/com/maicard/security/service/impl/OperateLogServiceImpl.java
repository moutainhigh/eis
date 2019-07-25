package com.maicard.security.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maicard.annotation.IgnoreJmsDataSync;
import com.maicard.annotation.ProcessMessageObject;
import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.ConfigService;
import com.maicard.exception.RequiredAttributeIsNullException;
import com.maicard.mb.service.EisMessageListener;
import com.maicard.mb.service.MessageService;
import com.maicard.security.criteria.OperateLogCriteria;
import com.maicard.security.dao.OperateLogDao;
import com.maicard.security.domain.OperateLog;
import com.maicard.security.service.OperateLogService;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.ObjectType;
import com.maicard.standard.Operate;
import com.maicard.standard.MessageStandard.MessageLevel;

@Service
@ProcessMessageObject("operateLog")
public class OperateLogServiceImpl extends BaseService implements OperateLogService,EisMessageListener {

	@Resource
	private OperateLogDao operateLogDao;

	@Resource
	private ApplicationContextService applicationContextService;
	@Resource
	private ConfigService configService;
	@Resource
	private MessageService messageService;


	private String messageBusName;

	private boolean handlerOperateLog;
	private boolean handlerJmsDataSyncToLocal;
	private int keepOperateLogDay;
	private String oldOperateLogSaveDir;

	@PostConstruct
	public void init(){
		handlerOperateLog = configService.getBooleanValue(DataName.handlerOperateLog.toString(),0);
		handlerJmsDataSyncToLocal = configService.getBooleanValue(DataName.handlerJmsDataSyncToLocal.toString(),0);
		keepOperateLogDay = configService.getIntValue(DataName.keepOperateLogDay.toString(),0);
		if(keepOperateLogDay == 0){
			keepOperateLogDay = 30;
		}
		oldOperateLogSaveDir = configService.getValue(DataName.oldOperateLogSaveDir.toString(),0);

	}


	@Override
	@IgnoreJmsDataSync
	public int insert(OperateLog operateLog){
		logger.debug("操作日志：" + operateLog);
		if(handlerOperateLog){
			int rs = 0;
		
			try{
				rs = insertLocal(operateLog);
			}catch(Exception e){
				e.printStackTrace();
			}
			if(rs == 1){
				//messageService.sendJmsDataSyncMessage(messageBusName, "operateLogService", "insertLocal", operateLog);
				//logger.debug("操作日志已插入数据库，并发送同步请求[operateLogService.insertLocal(operateLog)]");
				return 1;
			} else {
				return 0;
			}
		}
		return _insertRemote(operateLog);
	}

	private int _insertRemote(OperateLog operateLog) {
		EisMessage message = new EisMessage(MessageLevel.system.getCode(), ObjectType.operateLog.toString(),  Operate.create.getId());
		message.setAttachment(new HashMap<String, Object>());
		message.getAttachment().put("operateLog", operateLog);
		try{
			logger.debug("发送消息：" + message + "  messageBusName：" + messageBusName);
			messageService.send(messageBusName, message);
			message = null;
		}catch(Exception e){
			e.printStackTrace();
		}
		return 1;
	}

	@Override
	public int insertLocal(OperateLog operateLog) throws Exception{

		try{
			return operateLogDao.insert(operateLog);
		}catch(Exception e){
			logger.error("插入数据失败:" + e.getMessage());
		}
		return -1;

	}

	@Override
	@IgnoreJmsDataSync
	public int update(OperateLog operateLog) throws Exception{
		try{
			return operateLogDao.update(operateLog);
		}catch(Exception e){
			logger.error("更新数据失败:" + e.getMessage());
		}
		return -1;
	}

	public List<OperateLog> list(OperateLogCriteria operateLogCriteria){
		List<OperateLog> operateLogList = null;
		try {
			operateLogList =operateLogDao.list(operateLogCriteria);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(operateLogList == null ){
			return Collections.emptyList();
		}
		for(OperateLog operateLog : operateLogList){
			afterFetch(operateLog);
		}
		return operateLogList;
	}

	public List<OperateLog> listOnPage(OperateLogCriteria operateLogCriteria) throws Exception{
		List<OperateLog> operateLogList =  operateLogDao.listOnPage(operateLogCriteria);
		if(operateLogList == null ){
			return Collections.emptyList();
		}
		for(OperateLog operateLog : operateLogList){
			afterFetch(operateLog);
		}
		return operateLogList;	
	}

	private void afterFetch(OperateLog operateLog){


	}

	@Override
	public int count(OperateLogCriteria operateLogCriteria){
		try{
			return operateLogDao.count(operateLogCriteria);
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		return -1;
	}

	@Override
	public int getFrequentObjectId (
			OperateLogCriteria operateLogCriteria) throws Exception{
		if(	operateLogCriteria.getUuid() == 0){
			throw new RequiredAttributeIsNullException("未提供必须的参数UUID");
		}
		if(operateLogCriteria.getBeginTime() == null || operateLogCriteria.getBeginTime().equals("")){
			//operateLogCriteria.setBeginTime( new SimpleDateFormat(CommonStandard.statHourFormat).format(new Date( new Date().getTime() - 3600 * 24 * 30 * 1000)));
		}

		return operateLogDao.getFrequentObjectId(operateLogCriteria);

	}

	@Override
	public int getRecentObjectId(
			OperateLogCriteria operateLogCriteria) throws Exception{
		if(	operateLogCriteria.getUuid() == 0){
			throw new RequiredAttributeIsNullException("未提供必须的参数UUID");
		}
		return operateLogDao.getRecentObjectId(operateLogCriteria);

	}






	@Override
	public void onMessage(EisMessage eisMessage) {
		if(!handlerOperateLog){
			logger.debug("本节点不负责处理业务操作日志数据更新，忽略消息[" + eisMessage.getMessageId() + "].");
			eisMessage = null;
			return;
		}
		if(eisMessage.getOperateCode() == 0){
			logger.debug("消息操作码为空，忽略消息[" + eisMessage.getMessageId() + "].");
			eisMessage = null;
			return;
		}
		if(eisMessage.getOperateCode() == Operate.create.getId()){
			try{
				operate(eisMessage);
			}
			catch(Exception e){
				e.printStackTrace();
			}
			eisMessage = null;
		} else {			 
			logger.debug("消息操作码非法[" + eisMessage.getOperateCode() + "]，忽略消息[" + eisMessage.getMessageId() + "].");
			eisMessage = null;
			return;
		}		
	}

	private void operate(EisMessage eisMessage) throws Exception{

		EisMessage replyMessage = new EisMessage();
		replyMessage.setAttachment(new HashMap<String,Object>());
		OperateLog operateLog = null;
		try{
			Object object = eisMessage.getAttachment().get("operateLog");
			if(object == null){
				return;
			}
			if(object instanceof OperateLog){
				operateLog = (OperateLog)object;
			}
			if(object instanceof LinkedHashMap){
				ObjectMapper om = new ObjectMapper();
				om.setDateFormat(new SimpleDateFormat(CommonStandard.defaultDateFormat));
				String textData = null;
				textData = om.writeValueAsString(object);
				operateLog = om.readValue(textData, OperateLog.class);
				if(operateLog == null){
					logger.warn("无法将请求执行的对象转换为OperateLog");
					return;
				}

			}
		}catch(Exception e){
			e.printStackTrace();
		}


		if(eisMessage.getOperateCode() == Operate.create.getId()){
			if(handlerOperateLog){
				insertLocal(operateLog);
				operateLog.setSyncFlag(0);
				messageService.sendJmsDataSyncMessage(null, "operateLogService", "insertLocal", operateLog);
				eisMessage = null;
				return;
			}
		} else {
			logger.debug("忽略操作[" + eisMessage.getOperateCode() );
			eisMessage = null;
		}
	}

	@Override
	//找出指定条件的两个记录之间的时间差
	public long findPeriod(OperateLogCriteria operateLogCriteria){
		return operateLogDao.findPeriod(operateLogCriteria);
	}

	@Scheduled(cron="0 */5 * * * ?")
	@Override
	public void cleanOldLog(){

		OperateLogCriteria operateLogCriteria = new OperateLogCriteria();
		operateLogCriteria.setEndTime(DateUtils.addDays(new Date(), -keepOperateLogDay));

		if(handlerOperateLog){
			if(StringUtils.isNotBlank(oldOperateLogSaveDir)){
				//应当将清除的日志保存到指定目录
				List<OperateLog> operateLogList = null;
				try{
					operateLogList = operateLogDao.list(operateLogCriteria);
				}catch(Exception e){}
				if(operateLogList != null && operateLogList.size() >0){
					String fileName = oldOperateLogSaveDir + "/" + new SimpleDateFormat(CommonStandard.statHourFormat).format(new Date());
					logger.info("尝试将[" + operateLogCriteria.getEndTime() + "]之前的日志保存到[" + fileName +  "],记录有" + operateLogList + "条");
					File destDir = new File(oldOperateLogSaveDir);
					if(!destDir.exists()){
						logger.debug("保存操作日志的路径不存在，创建目录" + oldOperateLogSaveDir);
						if(!destDir.mkdirs()){
							logger.error("无法创建目录[" + oldOperateLogSaveDir + "]");
						}
					}
					try{
						BufferedWriter bw = new BufferedWriter(new OutputStreamWriter( new FileOutputStream(fileName), "UTF-8"));
						StringBuffer sb = new StringBuffer();
						for(OperateLog operateLog : operateLogList){
							sb.append(operateLog.getObjectType());
							sb.append(",");
							sb.append(operateLog.getObjectId());
							sb.append(",");
							sb.append(operateLog.getUuid());
							sb.append(",");
							sb.append(operateLog.getOperateCode());
							sb.append(",");
							sb.append(operateLog.getOperateResult());
							sb.append(",");
							sb.append(operateLog.getData());
							sb.append(",");
							sb.append(operateLog.getIp());
							sb.append(",");
							sb.append(operateLog.getServerId());
							sb.append("\n");
						}
						bw.write(sb.toString());
						bw.close();
						sb = null;
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		}
		if(handlerJmsDataSyncToLocal || handlerOperateLog){
			//清除指定时间之前的日志			
			int rs = operateLogDao.clearOldLog(operateLogCriteria);
			logger.debug("清除[" + operateLogCriteria.getEndTime() + "]之前的操作日志,删除了[" + rs + "]条");
		}

	}


	@Override
	public void deleteByCriteria(OperateLogCriteria operateLogCriteria) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public OperateLog select(int tagId) {
		// TODO Auto-generated method stub
		return null;
	}

}
