package com.maicard.security.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.security.criteria.PrivilegeRoleRelationCriteria;
import com.maicard.security.domain.PrivilegeRoleRelation;

public interface PartnerPrivilegeRoleRelationDao {

	void insert(PrivilegeRoleRelation partnerPrivilegeRoleRelation) throws DataAccessException;

	int update(PrivilegeRoleRelation partnerPrivilegeRoleRelation) throws DataAccessException;

	int delete(int partnerPrivilegeRoleRelationId) throws DataAccessException;

	PrivilegeRoleRelation select(int partnerPrivilegeRoleRelationId) throws DataAccessException;

	List<PrivilegeRoleRelation> list(PrivilegeRoleRelationCriteria partnerPrivilegeRoleRelationCriteria) throws DataAccessException;
	
	List<PrivilegeRoleRelation> listOnPage(PrivilegeRoleRelationCriteria partnerPrivilegeRoleRelationCriteria) throws DataAccessException;
	
	int count(PrivilegeRoleRelationCriteria partnerPrivilegeRoleRelationCriteria) throws DataAccessException;

}
