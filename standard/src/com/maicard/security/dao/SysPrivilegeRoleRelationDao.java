package com.maicard.security.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.security.criteria.PrivilegeRoleRelationCriteria;
import com.maicard.security.domain.PrivilegeRoleRelation;

public interface SysPrivilegeRoleRelationDao {

	int insert(PrivilegeRoleRelation sysPrivilegeGroupRelation) throws DataAccessException;

	int update(PrivilegeRoleRelation sysPrivilegeGroupRelation) throws DataAccessException;

	int delete(int sysPrivilegeGroupRelationId) throws DataAccessException;
	
	void deleteAllByGroupId(int groupId) throws DataAccessException;

	PrivilegeRoleRelation select(int sysPrivilegeGroupRelationId) throws DataAccessException;

	List<PrivilegeRoleRelation> list(PrivilegeRoleRelationCriteria privilegeRoleRelationCriteria) throws DataAccessException;
	
	List<PrivilegeRoleRelation> listOnPage(PrivilegeRoleRelationCriteria privilegeRoleRelationCriteria) throws DataAccessException;
	
	int count(PrivilegeRoleRelationCriteria privilegeRoleRelationCriteria) throws DataAccessException;

}
