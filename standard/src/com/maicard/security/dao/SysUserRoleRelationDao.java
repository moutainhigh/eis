package com.maicard.security.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.security.criteria.UserRoleRelationCriteria;
import com.maicard.security.domain.UserRoleRelation;

public interface SysUserRoleRelationDao {

	int insert(UserRoleRelation sysUserRoleRelation) throws DataAccessException;

	int update(UserRoleRelation sysUserRoleRelation) throws DataAccessException;

	int delete(int id) throws DataAccessException;

	UserRoleRelation select(int id) throws DataAccessException;

	List<UserRoleRelation> list(UserRoleRelationCriteria userRoleRelationCriteria) throws DataAccessException;
	
	List<UserRoleRelation> listOnPage(UserRoleRelationCriteria userRoleRelationCriteria) throws DataAccessException;
	
	int count(UserRoleRelationCriteria userRoleRelationCriteria) throws DataAccessException;
	
	void deleteByUuid(long uuid);

}
