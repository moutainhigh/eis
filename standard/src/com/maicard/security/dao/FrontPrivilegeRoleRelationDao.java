package com.maicard.security.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.security.criteria.PrivilegeRoleRelationCriteria;
import com.maicard.security.domain.PrivilegeRoleRelation;

public interface FrontPrivilegeRoleRelationDao {

	int insert(PrivilegeRoleRelation privilegeRoleRelation) throws DataAccessException;

	int update(PrivilegeRoleRelation privilegeRoleRelation) throws DataAccessException;

	int delete(int frontPrivilegeRoleRelationId) throws DataAccessException;

	PrivilegeRoleRelation select(int frontPrivilegeRoleRelationId) throws DataAccessException;

	List<PrivilegeRoleRelation> list(PrivilegeRoleRelationCriteria frontPrivilegeRoleRelationCriteria) throws DataAccessException;
	
	List<PrivilegeRoleRelation> listOnPage(PrivilegeRoleRelationCriteria frontPrivilegeRoleRelationCriteria) throws DataAccessException;
	
	int count(PrivilegeRoleRelationCriteria frontPrivilegeRoleRelationCriteria) throws DataAccessException;
	
	void deleteByFrontRoleId(int frontRoleId) throws DataAccessException;

}
