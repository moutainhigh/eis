package com.maicard.security.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.security.criteria.PrivilegeRelationCriteria;
import com.maicard.security.domain.PrivilegeRelation;

public interface SysPrivilegeRelationDao {

	int insert(PrivilegeRelation privilegeRelation) throws DataAccessException;

	int update(PrivilegeRelation privilegeRelation) throws DataAccessException;

	int delete(int privilegeId) throws DataAccessException;
	
	
	void deleteByCriteria(PrivilegeRelationCriteria privilegeRelationCriteria) throws DataAccessException;
	
	PrivilegeRelation select(int privilegeId) throws DataAccessException;

	List<PrivilegeRelation> list(PrivilegeRelationCriteria privilegeRelationCriteria) throws DataAccessException;
	
	List<PrivilegeRelation> listOnPage(PrivilegeRelationCriteria privilegeRelationCriteria) throws DataAccessException;
	
	int count(PrivilegeRelationCriteria privilegeRelationCriteria) throws DataAccessException;

}
