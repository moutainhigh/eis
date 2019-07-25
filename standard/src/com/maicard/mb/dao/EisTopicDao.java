package com.maicard.mb.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.mb.criteria.EisTopicCriteria;
import com.maicard.mb.domain.EisTopic;

public interface EisTopicDao {

	int insert(EisTopic eisTopic) throws DataAccessException;

	int update(EisTopic eisTopic) throws DataAccessException;

	int delete(int ugid) throws DataAccessException;

	EisTopic select(int ugid) throws DataAccessException;

	List<EisTopic> list(EisTopicCriteria topicCriteria) throws DataAccessException;
	
	List<EisTopic> listOnPage(EisTopicCriteria topicCriteria) throws DataAccessException;
	
	int count(EisTopicCriteria topicCriteria) throws DataAccessException;
		


}
