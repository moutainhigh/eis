package com.maicard.mb.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.mb.criteria.MessageCriteria;
import com.maicard.mb.domain.UserMessage;

public interface UserMessageDao {

	int insert(UserMessage eisMessage) throws DataAccessException;

	int update(UserMessage eisMessage) throws DataAccessException;

	int delete(String messageId) throws DataAccessException;

	List<UserMessage> list(MessageCriteria messageCriteria) throws DataAccessException;
	
	List<UserMessage> listOnPage(MessageCriteria messageCriteria) throws DataAccessException;
	
	int count(MessageCriteria messageCriteria) throws DataAccessException;

	List<String> getUniqueIdentify(MessageCriteria messageCriteria);

	int deleteSubscribe(MessageCriteria userMessageCriteria);
		


}
