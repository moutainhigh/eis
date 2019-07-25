package com.maicard.mb.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.mb.criteria.UserSubscribeMessageRelationCriteria;
import com.maicard.mb.domain.UserMessageRelation;

public interface UserSubscribeMessageRelationDao {

	int insert(UserMessageRelation userMessageRelation) throws DataAccessException;

	int update(UserMessageRelation userMessageRelation) throws DataAccessException;

	int delete(int ugid) throws DataAccessException;

	UserMessageRelation select(int ugid) throws DataAccessException;

	List<UserMessageRelation> list(UserSubscribeMessageRelationCriteria userSubscribeMessageRelationCriteria) throws DataAccessException;
	
	List<UserMessageRelation> listOnPage(UserSubscribeMessageRelationCriteria userSubscribeMessageRelationCriteria) throws DataAccessException;
	
	int count(UserSubscribeMessageRelationCriteria userSubscribeMessageRelationCriteria) throws DataAccessException;
		


}
