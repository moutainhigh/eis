package com.maicard.common.service.task;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.PerformanceCheckJob;
import com.maicard.standard.DataName;
import com.maicard.standard.ObjectType;
import com.maicard.standard.MessageStandard.MessageLevel;
import com.maicard.standard.MessageStandard.MessageStatus;
import com.maicard.standard.MessageStandard.UserMessageSendMethod;

@Service
public class PerformanceCheckJobImpl extends BaseService implements PerformanceCheckJob {

	@Value("${systemLogPerformance}")
	private boolean logPerformance;

	@Value("${systemAlertMemoryUsage}")
	private boolean systemAlertMemoryUsage;

	@Value("${systemServerId}")
	private int systemServerId;
	
	private String systemName;



	@Resource
	private ApplicationContextService applicationContextService;
	@Resource
	private ConfigService configService;

	//private String[] smsReceivers = null;
	private String[] mailReceivers = null;

	private float heapUsageAlertPercent = 0f;	//堆内存达到这个值，并且持续指定秒数heapUsageAlertContinueTime后报警
	private Date heapUsageAlertFirstReachTime = null;
	private int heapUsageAlertContinueTime = 1800;
	private int heapUsageAlertSendCount = 0;	//堆内存告警发送次数
	private int maxHeapUsageAlertSendCount = 3;	//堆内存告警最多发送次数

	private float permUsageAlertPercent = 0f;	//非堆内存达到这个值，并且持续指定秒数后报警
	private Date permUsageAlertFirstReachTime = null;
	private int permUsageAlertContinueTime = 0;
	private int permUsageAlertSendCount = 0;
	private int maxPermUsageAlertSendCount = 3;


	@PostConstruct
	public void init(){
		String smsReceiver = configService.getValue(DataName.alertSmsReceiver.toString(),0);
		if(smsReceiver != null){
			//smsReceivers = smsReceiver.split(",");
		} else {
			logger.warn("系统未配置告警短信接收人");
		}
		String mailReceiver = configService.getValue(DataName.alertMailReceiver.toString(),0);
		if(mailReceiver != null){
			mailReceivers = mailReceiver.split(",");
			logger.debug("系统配置了[" + mailReceivers.length + "]个告警邮件接收人");
		} else {
			logger.warn("系统未配置告警邮件接收人");
		}
		heapUsageAlertPercent = configService.getFloatValue(DataName.heapUsageAlertPercent.toString(),0);
		permUsageAlertPercent = configService.getFloatValue(DataName.permUsageAlertPercent.toString(),0);
		
		systemName = configService.getValue(DataName.systemName.toString(),0);


	}

	@Override
	//@Scheduled(initialDelay= 60 * 1000, fixedRate = 60 * 1000) 
	public void jvmPerformanceLog() {
		if(logPerformance){
			logger.info(applicationContextService.getPerformanceInfo());
		}
		

	}

	//监控内存使用
	//@Scheduled(initialDelay= 55 * 1000, fixedRate = 55 * 1000) 
	@Override
	public void memoryCheck(){
		//applicationContextService.getTomcatStatus();
		if(systemAlertMemoryUsage){
			_memoryCheck();
		}
	}
	private void _memoryCheck(){
		String result = applicationContextService.getMemoryInfo();
		try{
			int offset = result.indexOf("HEAP:");
			float heapUsage = Float.parseFloat(result.substring(offset + 5, offset + 9));
			logger.debug("系统堆内存已使用[" + heapUsage + ",告警上限:" + heapUsageAlertPercent + "]");
			if( heapUsage > heapUsageAlertPercent){
				if(heapUsageAlertFirstReachTime == null){
					heapUsageAlertFirstReachTime = new Date();
					logger.warn("系统堆内存已使用[" + heapUsage + ",第一次超过告警上限:" + heapUsageAlertPercent + "]");
				} else {
					long continueTime = (new Date().getTime() / 1000 - heapUsageAlertFirstReachTime.getTime() / 1000);
					logger.warn("系统堆内存已使用[" + heapUsage + "],超过告警上限[" + heapUsageAlertPercent + "]的时间已达:" + continueTime + "秒");
					if(continueTime >= heapUsageAlertContinueTime){
						heapUsageAlertSendCount++;
						logger.warn("系统堆内存已使用[" + heapUsage + "],超过告警上限[" + heapUsageAlertPercent + "]的时间已达:" + continueTime + "秒，已超过系统设置持续时间[" + heapUsageAlertContinueTime + "],应发送告警[告警已发送次数:" + heapUsageAlertSendCount + ",总次数:" + maxHeapUsageAlertSendCount + "]");
						if(heapUsageAlertSendCount <= maxHeapUsageAlertSendCount){
							sendAlertSms(systemName + systemServerId + "号服务器堆内存已使用" + heapUsage + "，并已持续" + continueTime + "秒" + heapUsageAlertSendCount + "/" + maxHeapUsageAlertSendCount);
						}else {
							heapUsageAlertFirstReachTime = null;
							logger.warn("堆内存告警次数已超过上线，不再告警.");
						}
					}
				}
			}

			offset = -1;
			offset = result.indexOf("PERM:");
			float permUsage = Float.parseFloat(result.substring(offset + 5, offset + 9));
			logger.debug("系统非堆内存已使用[" + permUsage + ",告警上限:" + permUsageAlertPercent + "]");
			if( permUsage > permUsageAlertPercent){
				if(permUsageAlertFirstReachTime == null){
					permUsageAlertFirstReachTime = new Date();
					logger.warn("系统非堆内存已使用[" + permUsage + ",第一次超过告警上限:" + permUsageAlertPercent + "]");
				} else {
					long continueTime = (new Date().getTime() / 1000 - permUsageAlertFirstReachTime.getTime() / 1000);
					logger.warn("系统非堆内存已使用[" + permUsage + "],超过告警上限[" + permUsageAlertPercent + "]的时间已达:" + continueTime + "秒");
					if(continueTime >= permUsageAlertContinueTime){
						permUsageAlertSendCount++;
						logger.warn("系统非堆内存已使用[" + permUsage + "],超过告警上限[" + permUsageAlertPercent + "]的时间已达:" + continueTime + "秒，已超过系统设置持续时间[" + permUsageAlertContinueTime + "],应发送告警[告警已发送次数:" + permUsageAlertSendCount + ",总次数:" + maxPermUsageAlertSendCount + "]");
						if(permUsageAlertSendCount <= maxPermUsageAlertSendCount){
							sendAlertSms(systemName + systemServerId + "号服务器非堆内存已使用" + permUsage + "，并已持续" + continueTime + "秒" + permUsageAlertSendCount + "/" + maxPermUsageAlertSendCount);
						} else {
							permUsageAlertFirstReachTime = null;
							logger.warn("非堆内存告警次数已超过上线，不再告警.");
						}
					}
				}
			}


		}catch(Exception e){
			e.printStackTrace();
		}

	}

	private void sendAlertSms(String content){
		EisMessage sms = new EisMessage();
		sms.setMessageLevel(MessageLevel.user.getCode());
		sms.setObjectType(ObjectType.message.toString());
		sms.setPerferMethod(new String[]{UserMessageSendMethod.sms.toString()});
		sms.setContent(content);
		sms.setCurrentStatus(MessageStatus.queue.id);
		/*if(smsReceivers != null){
			for(String receiver : smsReceivers){
				sms.setReceiverName(receiver);
			//	messageService.send(configService.getValue(DataName.messageBusSystem.toString(),0), sms);
			}

		} else {
			logger.warn("系统未配置短信告警接收人");
		}*/
	}



}
