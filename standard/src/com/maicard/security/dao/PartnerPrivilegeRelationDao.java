package com.maicard.security.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.security.criteria.PrivilegeRelationCriteria;
import com.maicard.security.domain.PrivilegeRelation;

public interface PartnerPrivilegeRelationDao {

	void insert(PrivilegeRelation partnerPrivilegeRelation) throws DataAccessException;

	int update(PrivilegeRelation partnerPrivilegeRelation) throws DataAccessException;

	int delete(int privilegeRelationId) throws DataAccessException;

	PrivilegeRelation select(int privilegeRelationId) throws DataAccessException;

	List<PrivilegeRelation> list(PrivilegeRelationCriteria partnerPrivilegeRelationCriteria) throws DataAccessException;
	
	List<PrivilegeRelation> listOnPage(PrivilegeRelationCriteria partnerPrivilegeRelationCriteria) throws DataAccessException;
	
	int count(PrivilegeRelationCriteria partnerPrivilegeRelationCriteria) throws DataAccessException;

}
