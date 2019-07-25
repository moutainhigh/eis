package com.maicard.mb.service;

import java.util.List;

import com.maicard.mb.criteria.MessageTypeCriteria;
import com.maicard.mb.domain.MessageType;

public interface MessageTypeService {

	int insert(MessageType messageType);

	int update(MessageType messageType);

	int delete(int udid);
		
	MessageType select(int messageTypeId);
	
	MessageType select(String messageTypeName);
	
	List<MessageType> list(MessageTypeCriteria messageTypeCriteria);

	List<MessageType> listOnPage(MessageTypeCriteria messageTypeCriteria);
		
	int count(MessageTypeCriteria messageTypeCriteria);
	
		


}
