package com.maicard.security.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.security.criteria.UserRoleRelationCriteria;
import com.maicard.security.domain.UserRoleRelation;

public interface FrontUserRoleRelationDao {

	int insert(UserRoleRelation userRoleRelation) throws DataAccessException;

	int update(UserRoleRelation userRoleRelation) throws DataAccessException;

	int delete(int frontUserRoleRelationId) throws DataAccessException;

	UserRoleRelation select(int frontUserRoleRelationId) throws DataAccessException;

	List<UserRoleRelation> list(UserRoleRelationCriteria frontUserRoleRelationCriteria) throws DataAccessException;
	
	List<UserRoleRelation> listOnPage(UserRoleRelationCriteria frontUserRoleRelationCriteria) throws DataAccessException;
	
	int count(UserRoleRelationCriteria frontUserRoleRelationCriteria) throws DataAccessException;
	
	void deleteByUuid(long fuuid) throws DataAccessException;

}
