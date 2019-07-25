package com.maicard.mb.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.common.domain.EisMessage;
import com.maicard.mb.criteria.MessageCriteria;

public interface SubscribeMessageDao {

	int insert(EisMessage subscribeMessage) throws DataAccessException;

	int update(EisMessage subscribeMessage) throws DataAccessException;

	int delete(int ugid) throws DataAccessException;

	EisMessage select(int ugid) throws DataAccessException;

	List<EisMessage> list(MessageCriteria subscribeMessageCriteria) throws DataAccessException;
	
	List<EisMessage> listOnPage(MessageCriteria subscribeMessageCriteria) throws DataAccessException;
	
	int count(MessageCriteria subscribeMessageCriteria) throws DataAccessException;
		


}
