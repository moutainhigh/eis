package com.maicard.mb.service;

import java.util.List;

import com.maicard.mb.criteria.MessageCriteria;
import com.maicard.mb.domain.UserMessage;

public interface UserMessageService {

	int insert(UserMessage userMessage);

	int delete(String messageId);
		
	UserMessage select(String messageId);

	List<UserMessage> list(MessageCriteria messageCriteria);

	List<UserMessage> listOnPage(MessageCriteria messageCriteria);
		
	int count(MessageCriteria messageCriteria);
	
	int update(UserMessage userMessage);

	int send(UserMessage userMessage);
	
	int insertLocal(UserMessage userMessage);

	List<String> getUniqueIdentify(long ownerId);

	/**
	 * 读取广播消息，并将获取到的消息设置为已读
	 * 
	 *
	 * @author GHOST
	 * @date 2018-01-10
	 */
	List<UserMessage> popBroadcastMessage(MessageCriteria userMessageCriteria);

	/**
	 * 根据条件删除某个用户所有订阅消息的关联
	 * 
	 *
	 * @author GHOST
	 * @date 2018-01-12
	 */
	int deleteSubscribe(MessageCriteria userMessageCriteria);
	
		


}
