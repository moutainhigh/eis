package com.maicard.security.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.security.criteria.UserRoleRelationCriteria;
import com.maicard.security.domain.UserRoleRelation;

public interface PartnerRoleRelationDao {

	int insert(UserRoleRelation partnerRoleRelation) throws DataAccessException;

	int update(UserRoleRelation partnerRoleRelation) throws DataAccessException;

	int delete(int partnerRoleRelationId) throws DataAccessException;

	UserRoleRelation select(int partnerRoleRelationId) throws DataAccessException;

	List<UserRoleRelation> list(UserRoleRelationCriteria partnerRoleRelationCriteria) throws DataAccessException;
	
	List<UserRoleRelation> listOnPage(UserRoleRelationCriteria partnerRoleRelationCriteria) throws DataAccessException;
	
	int count(UserRoleRelationCriteria partnerRoleRelationCriteria) throws DataAccessException;

	void deleteByUuid(long uuid);

}
