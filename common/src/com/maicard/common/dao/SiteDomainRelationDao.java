package com.maicard.common.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.common.criteria.SiteDomainRelationCriteria;
import com.maicard.common.domain.SiteDomainRelation;

public interface SiteDomainRelationDao {

	int insert(SiteDomainRelation siteDomainRelation) throws DataAccessException;

	int update(SiteDomainRelation siteDomainRelation) throws DataAccessException;

	int delete(int siteDomainRelationId) throws DataAccessException;

	SiteDomainRelation select(int siteDomainRelationId) throws DataAccessException;

	List<SiteDomainRelation> list(SiteDomainRelationCriteria siteDomainRelationCriteria) throws DataAccessException;
	
	List<SiteDomainRelation> listOnPage(SiteDomainRelationCriteria siteDomainRelationCriteria) throws DataAccessException;
	
	int count(SiteDomainRelationCriteria siteDomainRelationCriteria) throws DataAccessException;

	List<Integer> listPkOnPage(SiteDomainRelationCriteria siteDomainRelationCriteria)
			throws DataAccessException;

	List<Integer> listPk(SiteDomainRelationCriteria siteDomainRelationCriteria) throws DataAccessException;

}
