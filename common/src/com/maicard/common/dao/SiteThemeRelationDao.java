package com.maicard.common.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.common.criteria.SiteThemeRelationCriteria;
import com.maicard.common.domain.SiteThemeRelation;

public interface SiteThemeRelationDao {

	int insert(SiteThemeRelation siteThemeRelation) throws DataAccessException;

	int update(SiteThemeRelation siteThemeRelation) throws DataAccessException;

	int delete(int siteThemeRelationId) throws DataAccessException;

	SiteThemeRelation select(int siteThemeRelationId) throws DataAccessException;

	List<SiteThemeRelation> list(SiteThemeRelationCriteria siteThemeRelationCriteria) throws DataAccessException;
	
	List<SiteThemeRelation> listOnPage(SiteThemeRelationCriteria siteThemeRelationCriteria) throws DataAccessException;
	
	int count(SiteThemeRelationCriteria siteThemeRelationCriteria) throws DataAccessException;

	List<Integer> listPkOnPage(SiteThemeRelationCriteria siteThemeRelationCriteria)
			throws DataAccessException;

	List<Integer> listPk(SiteThemeRelationCriteria siteThemeRelationCriteria) throws DataAccessException;

	int updateForUuid(SiteThemeRelation siteThemeRelation);

}
