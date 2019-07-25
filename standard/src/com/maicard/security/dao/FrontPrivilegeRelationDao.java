package com.maicard.security.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.security.criteria.PrivilegeRelationCriteria;
import com.maicard.security.domain.PrivilegeRelation;

public interface FrontPrivilegeRelationDao {

	int insert(PrivilegeRelation frontPrivilegeRelation) throws DataAccessException;

	int update(PrivilegeRelation frontPrivilegeRelation) throws DataAccessException;

	int delete(int privilegeId) throws DataAccessException;

	void deleteByFrontPrivilegeId(int frontPrivilegeId)throws DataAccessException;

	PrivilegeRelation select(int privilegeId) throws DataAccessException;

	List<PrivilegeRelation> list(PrivilegeRelationCriteria frontPrivilegeRelationCriteria) throws DataAccessException;
	
	List<PrivilegeRelation> listOnPage(PrivilegeRelationCriteria frontPrivilegeRelationCriteria) throws DataAccessException;
	
	int count(PrivilegeRelationCriteria frontPrivilegeRelationCriteria) throws DataAccessException;

}
