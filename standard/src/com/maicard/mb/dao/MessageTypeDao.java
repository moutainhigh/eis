package com.maicard.mb.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.mb.criteria.MessageTypeCriteria;
import com.maicard.mb.domain.MessageType;

public interface MessageTypeDao {

	int insert(MessageType messageType) throws DataAccessException;

	int update(MessageType messageType) throws DataAccessException;

	int delete(int ugid) throws DataAccessException;

	MessageType select(int ugid) throws DataAccessException;

	List<MessageType> list(MessageTypeCriteria topicCriteria) throws DataAccessException;
	
	List<MessageType> listOnPage(MessageTypeCriteria topicCriteria) throws DataAccessException;
	
	int count(MessageTypeCriteria topicCriteria) throws DataAccessException;
		


}
