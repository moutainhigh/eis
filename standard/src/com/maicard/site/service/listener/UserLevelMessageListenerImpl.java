package com.maicard.site.service.listener;


import javax.annotation.Resource;
import javax.jms.*;

import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.mb.service.SubscribeMessageService;
import com.maicard.standard.MessageStandard.MessageStatus;

/**
 * 用户级别（包括前台和后台用户）的消息总线监听，监听用户级别消息总线[com.maicard.mb.UserLevelTopic]
 * @author NetSnake
 * @date 2012-7-21
 */

@Service
public class UserLevelMessageListenerImpl extends BaseService implements MessageListener {
	
	@Resource
	private SubscribeMessageService subscribeMessageService;

	@Override
	public void onMessage(Message message) {
		logger.info("用户级别订阅消息总线收到消息");
		try{
			if(message instanceof ObjectMessage){
				ObjectMessage objectMessage = (ObjectMessage)message;
				com.maicard.common.domain.EisMessage subscribeMessage = (com.maicard.common.domain.EisMessage)objectMessage.getObject();
				if(subscribeMessage.getCurrentStatus() == MessageStatus.unread.id){
					int messageId = subscribeMessageService.insert(subscribeMessage);
					logger.info("插入新的消息[messageId=" + messageId + "]");
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}


	}
}
