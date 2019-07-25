package com.maicard.common.dao.ibatis;

import java.util.List;


import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.criteria.SiteDomainRelationCriteria;
import com.maicard.common.dao.SiteDomainRelationDao;
import com.maicard.common.domain.SiteDomainRelation;
import com.maicard.common.util.Paging;
import com.maicard.standard.CommonStandard;

import org.apache.ibatis.session.RowBounds;

@Repository
public class SiteDomainRelationDaoImpl extends BaseDao implements SiteDomainRelationDao {

	private final String cacheName = CommonStandard.cacheNameSupport;

	public int insert(SiteDomainRelation siteDomainRelation) throws DataAccessException {
		return (Integer)getSqlSessionTemplate().insert("SiteDomainRelation.insert", siteDomainRelation);
	}

	@CacheEvict(value = cacheName, key = "'SiteDomainRelation#' + #siteDomainRelation.siteDomainRelationId")
	public int update(SiteDomainRelation siteDomainRelation) throws DataAccessException {
		return getSqlSessionTemplate().update("SiteDomainRelation.update", siteDomainRelation);
	}

	@CacheEvict(value = cacheName, key = "'SiteDomainRelation#' + #siteDomainRelationId")
	public int delete(int siteDomainRelationId) throws DataAccessException {
		return getSqlSessionTemplate().delete("SiteDomainRelation.delete", siteDomainRelationId);
	}

	@Cacheable(value = cacheName, key = "'SiteDomainRelation#' + #siteDomainRelationId")
	public SiteDomainRelation select(int siteDomainRelationId) throws DataAccessException {
		return (SiteDomainRelation) getSqlSessionTemplate().selectOne("SiteDomainRelation.select", siteDomainRelationId);
	}

	@Override
	public List<Integer> listPk(SiteDomainRelationCriteria siteDomainRelationCriteria) throws DataAccessException {
		Assert.notNull(siteDomainRelationCriteria, "siteDomainRelationsCriteria must not be null");		
		return getSqlSessionTemplate().selectList("SiteDomainRelation.listPk", siteDomainRelationCriteria);
	}

	@Override
	public List<Integer> listPkOnPage(SiteDomainRelationCriteria siteDomainRelationCriteria) throws DataAccessException {
		Assert.notNull(siteDomainRelationCriteria, "siteDomainRelationsCriteria must not be null");
		Assert.notNull(siteDomainRelationCriteria.getPaging(), "paging must not be null");		
		int totalResults = count(siteDomainRelationCriteria);
		Paging paging = siteDomainRelationCriteria.getPaging();
		paging.setTotalResults(totalResults); 
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());

		return getSqlSessionTemplate().selectList("SiteDomainRelation.listPk", siteDomainRelationCriteria, rowBounds);
	}

	@Override
	public List<SiteDomainRelation> list(SiteDomainRelationCriteria siteDomainRelationCriteria) throws DataAccessException {
		Assert.notNull(siteDomainRelationCriteria, "siteDomainRelationsCriteria must not be null");

		return getSqlSessionTemplate().selectList("SiteDomainRelation.list", siteDomainRelationCriteria);
	}


	@Override
	public List<SiteDomainRelation> listOnPage(SiteDomainRelationCriteria siteDomainRelationCriteria) throws DataAccessException {
		Assert.notNull(siteDomainRelationCriteria, "siteDomainRelationsCriteria must not be null");
		Assert.notNull(siteDomainRelationCriteria.getPaging(), "paging must not be null");		
		int totalResults = count(siteDomainRelationCriteria);
		Paging paging = siteDomainRelationCriteria.getPaging();
		paging.setTotalResults(totalResults); 
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());

		return getSqlSessionTemplate().selectList("SiteDomainRelation.list", siteDomainRelationCriteria, rowBounds);
	}

	@Override
	public int count(SiteDomainRelationCriteria siteDomainRelationCriteria) throws DataAccessException {
		Assert.notNull(siteDomainRelationCriteria, "siteDomainRelationsCriteria must not be null");

		return ((Integer) getSqlSessionTemplate().selectOne("SiteDomainRelation.count", siteDomainRelationCriteria)).intValue();
	}

}
