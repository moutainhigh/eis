package com.maicard.mb.service;

import java.util.List;

import com.maicard.common.domain.EisMessage;
import com.maicard.mb.criteria.MessageCriteria;

public interface SubscribeMessageService {

	int insert(EisMessage subscribeMessage);

	int update(EisMessage subscribeMessage);

	int delete(int messageId);
		
	EisMessage select(int messageId);

	List<EisMessage> list(MessageCriteria subscribeMessageCriteria);

	List<EisMessage> listOnPage(MessageCriteria subscribeMessageCriteria);
		
	int count(MessageCriteria subscribeMessageCriteria);
	
		


}
