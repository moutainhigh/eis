package com.maicard.security.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.security.criteria.UserRelationCriteria;
import com.maicard.security.domain.UserRelation;

public interface UserRelationDao {

	int insert(UserRelation userRelation) throws DataAccessException;

	int update(UserRelation userRelation) throws DataAccessException;

	UserRelation select(long userRelationId) throws DataAccessException;

	List<UserRelation> list(UserRelationCriteria userRelationCriteria) throws DataAccessException;
	
	List<UserRelation> listOnPage(UserRelationCriteria userRelationCriteria) throws DataAccessException;
	
	int count(UserRelationCriteria userRelationCriteria) throws DataAccessException;
	
	int delete(long userRelationId);

	int delete(UserRelationCriteria userRelationCriteria);

	/**
	 * 返回当前数据库中的最大ID
	 * 
	 *
	 * @author NetSnake
	 * @date 2018-05-07
	 */
	long getMaxId();

}
