package com.maicard.mb.service.impl;

import java.util.List;

import javax.annotation.Resource;


import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisMessage;
import com.maicard.mb.criteria.MessageCriteria;
import com.maicard.mb.dao.SubscribeMessageDao;
import com.maicard.mb.service.SubscribeMessageService;
import com.maicard.standard.MessageStandard.MessageStatus;

@Service
public class SubscribeMessageServiceImpl extends BaseService implements SubscribeMessageService {

	@Resource
	private SubscribeMessageDao subscribeMessageDao;


	public int insert(EisMessage subscribeMessage) {
		int newSubscribeMessageId = -1;
		try{
			newSubscribeMessageId = subscribeMessageDao.insert(subscribeMessage);
		}catch(DataIntegrityViolationException e){
			logger.error("必须的字段不完整，或关联数据不完整，无法插入数据库");
			return -1;
		}
		if(newSubscribeMessageId < 1){
			return -1;
		}
		return newSubscribeMessageId;
	}

	public int update(EisMessage subscribeMessage){
		int actualRowsAffected = 0;
		if(subscribeMessage == null){
			return -1;
		}
		/*int messageId = subscribeMessage.getMessageId();
		if(messageId < 1){
			return -1;
		}*/

		//EisMessage _oldSubscribeMessage = subscribeMessageDao.select(messageId);

		/*if (_oldSubscribeMessage != null) {
			actualRowsAffected = subscribeMessageDao.update(subscribeMessage);
		} else {
			return -1;
		}*/
		
		
		return actualRowsAffected;		
	}

	public int delete(int udid) {
		int actualRowsAffected = 0;

		EisMessage _oldSubscribeMessage = subscribeMessageDao.select(udid);

		if (_oldSubscribeMessage != null) {
			actualRowsAffected = subscribeMessageDao.delete(udid);
		} else {
			return -1;
		}
		
		return actualRowsAffected;
	}
	
	public 	int changeStatus(int udid, int status){
		EisMessage subscribeMessage = subscribeMessageDao.select(udid);
		if(subscribeMessage == null){
			return 0;
		}
		subscribeMessage.setCurrentStatus(status);
		return update(subscribeMessage);		
		
	}


	public EisMessage select(int udid) {
		EisMessage subscribeMessage =  subscribeMessageDao.select(udid);
		if(subscribeMessage == null){
			return null;
		}
		
		return subscribeMessage;

	}

	public List<EisMessage> list(MessageCriteria subscribeMessageCriteria) {
		List<EisMessage> subscribeMessageList= subscribeMessageDao.list(subscribeMessageCriteria);
		if(subscribeMessageList == null){
			return null;
		}	
		return subscribeMessageList;
	}

	public List<EisMessage> listOnPage(MessageCriteria subscribeMessageCriteria) {
		List<EisMessage> subscribeMessageList =  subscribeMessageDao.listOnPage(subscribeMessageCriteria);
		if(subscribeMessageList == null){
			return null;
		}
		return subscribeMessageList;
	}


	
	public  int count(MessageCriteria subscribeMessageCriteria){
		return subscribeMessageDao.count(subscribeMessageCriteria);
	}

	
}
