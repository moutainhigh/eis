package com.maicard.mb.service.impl;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.jms.Message;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.maicard.annotation.ProcessMessageObject;
import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ConfigService;
import com.maicard.mb.service.EisMessageListener;
import com.maicard.mb.service.JmsHeartBeatService;
import com.maicard.mb.service.MessageService;
import com.maicard.standard.DataName;
import com.maicard.standard.Operate;
import com.maicard.standard.OperateResult;

/**
 * 全局消息消费者实现
 * 1、接收JMS队列中的消息
 * 2、调用容器中的其他服务的onMessage接口
 * 3、提供对队列的数据查询
 * 
 * 
 * @author NetSnake
 * @date 2012-7-31
 */



@Service
@ProcessMessageObject("jmsHeartBeat")
public class JmsHeartBeatServiceImpl extends BaseService implements JmsHeartBeatService, EisMessageListener{


	@Resource
	private ConfigService configService;
	@Resource
	private MessageService messageService;

	private boolean handlerJmsHeartBeat;

	private String messageBusName;
	private String replyQueueName;

	@PostConstruct
	public void init(){
		handlerJmsHeartBeat = configService.getBooleanValue(DataName.handlerJmsHeartBeat.toString(),0);

		messageBusName = configService.getValue(DataName.messageBusUser.toString(),0);
		replyQueueName = configService.getValue(DataName.replyQueueName.toString(),0);
	}

	@Override
	public EisMessage sendJmsHeartBeat(){
		EisMessage m = new EisMessage();
		m.setOperateCode(Operate.create.getId());
		//m.setObjectType(ObjectType.jmsHeartBeat.toString());
		m.setNeedReply(true);		
		EisMessage result = null;
		try{
			result = messageService.syncSend(messageBusName, replyQueueName, m);
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}


	@Async
	public void onMessage(Message message){


	}

	@Override
	@Async
	public void onMessage(EisMessage eisMessage) {
		try{
			
			if(!handlerJmsHeartBeat){
				logger.debug("本节点不负责相应JMS心跳检测");
				eisMessage = null;
				return;
			}
			EisMessage replyMessage = new EisMessage();
			replyMessage.setOperateCode(OperateResult.success.getId());
			if(logger.isDebugEnabled()){
				logger.debug("接收到JMS心跳检测");
			}
		//	messageService.reply(replyQueueName, eisMessage.getMessageId(), replyMessage);


		}catch(Exception e){
			e.printStackTrace();
		}		
		eisMessage = null;		
	}

}
