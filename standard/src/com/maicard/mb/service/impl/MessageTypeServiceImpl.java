package com.maicard.mb.service.impl;

import java.util.List;

import javax.annotation.Resource;


import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.mb.criteria.MessageTypeCriteria;
import com.maicard.mb.dao.MessageTypeDao;
import com.maicard.mb.domain.MessageType;
import com.maicard.mb.service.MessageTypeService;
import com.maicard.standard.BasicStatus;

@Service
public class MessageTypeServiceImpl extends BaseService implements MessageTypeService {

	@Resource
	private MessageTypeDao messageTypeDao;

	public int insert(MessageType messageType) {
		int newTopicId = -1;
		try{
			newTopicId = messageTypeDao.insert(messageType);
		}catch(DataIntegrityViolationException e){
			logger.error("必须的字段不完整，或关联数据不完整，无法插入数据库");
			return -1;
		}
		if(newTopicId < 1){
			return -1;
		}
		return newTopicId;
	}
	
	


	public int update(MessageType messageType){
		int actualRowsAffected = 0;
		if(messageType == null){
			return -1;
		}
		int messageTypeId = messageType.getMessageTypeId();
		if(messageTypeId < 1){
			return -1;
		}

		MessageType _oldTopic = messageTypeDao.select(messageTypeId);

		if (_oldTopic != null) {
			actualRowsAffected = messageTypeDao.update(messageType);
		} else {
			return -1;
		}
		
		
		return actualRowsAffected;		
	}

	public int delete(int udid) {
		int actualRowsAffected = 0;

		MessageType _oldTopic = messageTypeDao.select(udid);

		if (_oldTopic != null) {
			actualRowsAffected = messageTypeDao.delete(udid);
		} else {
			return -1;
		}
		
		return actualRowsAffected;
	}
	
	public 	int changeStatus(int udid, int status){
		MessageType messageType = messageTypeDao.select(udid);
		if(messageType == null){
			return 0;
		}
		messageType.setCurrentStatus(status);
		return update(messageType);		
		
	}


	public MessageType select(int messageTypeId) {
		MessageType messageType =  messageTypeDao.select(messageTypeId);
		if(messageType == null){
			return null;
		}
		
		return messageType;

	}

	public List<MessageType> list(MessageTypeCriteria messageTypeCriteria) {
		List<MessageType> messageTypeList= messageTypeDao.list(messageTypeCriteria);
		if(messageTypeList == null){
			return null;
		}	
		/*for(int i =0; i < messageTypeList.size(); i++){
			messageTypeList.get(i).setIndex(i+1);
			messageTypeList.get(i).setId(messageTypeList.get(i).getMessageTypeId());
		}*/
		return messageTypeList;
	}

	public List<MessageType> listOnPage(MessageTypeCriteria messageTypeCriteria) {
		List<MessageType> messageTypeList =  messageTypeDao.listOnPage(messageTypeCriteria);
		if(messageTypeList == null){
			return null;
		}
		/*for(int i =0; i < messageTypeList.size(); i++){
			messageTypeList.get(i).setIndex(i+1);
			messageTypeList.get(i).setId(messageTypeList.get(i).getMessageTypeId());
			messageTypeList.get(i).setCurrentStatusName(BasicStatus.disable.findById(messageTypeList.get(i).getCurrentStatus()).getName());

			
		}*/
		return messageTypeList;
	}


	
	public  int count(MessageTypeCriteria messageTypeCriteria){
		return messageTypeDao.count(messageTypeCriteria);
	}




	@Override
	public MessageType select(String messageTypeName) {
		MessageTypeCriteria messageTypeCriteria = new MessageTypeCriteria();
		List<MessageType> messageTypeList = list(messageTypeCriteria);
		if(messageTypeList != null && messageTypeList.size() == 1){
			return messageTypeList.get(0);
		}
		return null;
	}

}
