package com.maicard.security.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.security.criteria.UserRelationCriteria;
import com.maicard.security.domain.UserRelation;

public interface FrontUserRelationDao {

	int insert(UserRelation userRelation) throws DataAccessException;

	int update(UserRelation userRelation) throws DataAccessException;

	int delete(int front_user_focus_id) throws DataAccessException;

	int delete(UserRelationCriteria frontUserRelationCriteria) throws DataAccessException;
	
	UserRelation select(long front_user_focus_id) throws DataAccessException;

	List<UserRelation> list(UserRelationCriteria frontUserRelationCriteria) throws DataAccessException;
	
	List<UserRelation> listOnPage(UserRelationCriteria frontUserRelationCriteria) throws DataAccessException;
	
	int count(UserRelationCriteria frontUserRelationCriteria) throws DataAccessException;

}
