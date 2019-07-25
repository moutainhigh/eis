package com.maicard.security.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.security.criteria.UserRelationCriteria;
import com.maicard.security.domain.UserRelation;

public interface PartnerRelationDao {

	int insert(UserRelation partnerRelation) throws DataAccessException;

	int update(UserRelation partnerRelation) throws DataAccessException;

	int delete(int partnerRelationId) throws DataAccessException;

	UserRelation select(int partnerRelationId) throws DataAccessException;

	List<UserRelation> list(UserRelationCriteria partnerRelationCriteria) throws DataAccessException;
	
	List<UserRelation> listOnPage(UserRelationCriteria partnerRelationCriteria) throws DataAccessException;
	
	int count(UserRelationCriteria partnerRelationCriteria) throws DataAccessException;

}
