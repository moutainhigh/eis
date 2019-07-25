package com.maicard.common.dao.ibatis;

import java.util.List;


import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.criteria.SiteThemeRelationCriteria;
import com.maicard.common.dao.SiteThemeRelationDao;
import com.maicard.common.domain.SiteThemeRelation;
import com.maicard.common.util.Paging;
import com.maicard.standard.CommonStandard;

import org.apache.ibatis.session.RowBounds;

@Repository
public class SiteThemeRelationDaoImpl extends BaseDao implements SiteThemeRelationDao {

	private final String cacheName = CommonStandard.cacheNameSupport;

	public int insert(SiteThemeRelation siteThemeRelation) throws DataAccessException {
		return (Integer)getSqlSessionTemplate().insert("com.maicard.common.sql.SiteThemeRelation.insert", siteThemeRelation);
	}

	@CacheEvict(value = cacheName, key = "'SiteThemeRelation#' + #siteThemeRelation.siteThemeRelationId")
	public int update(SiteThemeRelation siteThemeRelation) throws DataAccessException {
		return getSqlSessionTemplate().update("com.maicard.common.sql.SiteThemeRelation.update", siteThemeRelation);
	}
	
	@CacheEvict(value = cacheName, key = "'SiteThemeRelation#' + #siteThemeRelation.siteThemeRelationId")
	public int updateForUuid(SiteThemeRelation siteThemeRelation) throws DataAccessException {
		return getSqlSessionTemplate().update("com.maicard.common.sql.SiteThemeRelation.updateForUuid", siteThemeRelation);
	}

	@CacheEvict(value = cacheName, key = "'SiteThemeRelation#' + #siteThemeRelationId")
	public int delete(int siteThemeRelationId) throws DataAccessException {
		return getSqlSessionTemplate().delete("com.maicard.common.sql.SiteThemeRelation.delete", siteThemeRelationId);
	}

	@Cacheable(value = cacheName, key = "'SiteThemeRelation#' + #siteThemeRelationId")
	public SiteThemeRelation select(int siteThemeRelationId) throws DataAccessException {
		return (SiteThemeRelation) getSqlSessionTemplate().selectOne("com.maicard.common.sql.SiteThemeRelation.select", siteThemeRelationId);
	}

	@Override
	public List<Integer> listPk(SiteThemeRelationCriteria siteThemeRelationCriteria) throws DataAccessException {
		Assert.notNull(siteThemeRelationCriteria, "siteThemeRelationsCriteria must not be null");		
		return getSqlSessionTemplate().selectList("com.maicard.common.sql.SiteThemeRelation.listPk", siteThemeRelationCriteria);
	}

	@Override
	public List<Integer> listPkOnPage(SiteThemeRelationCriteria siteThemeRelationCriteria) throws DataAccessException {
		Assert.notNull(siteThemeRelationCriteria, "siteThemeRelationsCriteria must not be null");
		Assert.notNull(siteThemeRelationCriteria.getPaging(), "paging must not be null");		
		int totalResults = count(siteThemeRelationCriteria);
		Paging paging = siteThemeRelationCriteria.getPaging();
		paging.setTotalResults(totalResults); 
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());

		return getSqlSessionTemplate().selectList("com.maicard.common.sql.SiteThemeRelation.listPk", siteThemeRelationCriteria, rowBounds);
	}

	@Override
	public List<SiteThemeRelation> list(SiteThemeRelationCriteria siteThemeRelationCriteria) throws DataAccessException {
		Assert.notNull(siteThemeRelationCriteria, "siteThemeRelationsCriteria must not be null");

		return getSqlSessionTemplate().selectList("com.maicard.common.sql.SiteThemeRelation.list", siteThemeRelationCriteria);
	}


	@Override
	public List<SiteThemeRelation> listOnPage(SiteThemeRelationCriteria siteThemeRelationCriteria) throws DataAccessException {
		Assert.notNull(siteThemeRelationCriteria, "siteThemeRelationsCriteria must not be null");
		Assert.notNull(siteThemeRelationCriteria.getPaging(), "paging must not be null");		
		int totalResults = count(siteThemeRelationCriteria);
		Paging paging = siteThemeRelationCriteria.getPaging();
		paging.setTotalResults(totalResults); 
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());

		return getSqlSessionTemplate().selectList("com.maicard.common.sql.SiteThemeRelation.list", siteThemeRelationCriteria, rowBounds);
	}

	@Override
	public int count(SiteThemeRelationCriteria siteThemeRelationCriteria) throws DataAccessException {
		Assert.notNull(siteThemeRelationCriteria, "siteThemeRelationsCriteria must not be null");

		return ((Integer) getSqlSessionTemplate().selectOne("com.maicard.common.sql.SiteThemeRelation.count", siteThemeRelationCriteria)).intValue();
	}

}
