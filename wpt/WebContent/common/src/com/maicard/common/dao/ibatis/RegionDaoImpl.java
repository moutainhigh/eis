package com.maicard.common.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.criteria.RegionCriteria;
import com.maicard.common.dao.RegionDao;
import com.maicard.common.domain.Region;
import com.maicard.common.util.Paging;
import com.maicard.standard.CommonStandard;

@Repository
public class RegionDaoImpl extends BaseDao implements RegionDao {

	public int insert(Region region) throws DataAccessException {
		return getSqlSessionTemplate().insert("Region.insert", region);
	}

	@CacheEvict(value = CommonStandard.cacheNameSupport, key = "'Region#' + #region.regionId")
	public int update(Region region) throws DataAccessException {
		return getSqlSessionTemplate().update("Region.update", region);
	}

	@CacheEvict(value = CommonStandard.cacheNameSupport, key = "'Region#' + #regionId")
	public int delete(int regionId) throws DataAccessException {
		return getSqlSessionTemplate().delete("Region.delete", new Integer(regionId));
	}

	@Cacheable(value = CommonStandard.cacheNameSupport, key = "'Region#' + #regionId")
	public Region select(int regionId) throws DataAccessException {
		return (Region) getSqlSessionTemplate().selectOne("Region.select", new Integer(regionId));
	}
	
	public List<Integer> listPk(RegionCriteria regionCriteria) throws DataAccessException {
		Assert.notNull(regionCriteria, "regionCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("Region.listPk", regionCriteria);
	}

	public List<Integer> listPkOnPage(RegionCriteria regionCriteria) throws DataAccessException {
		Assert.notNull(regionCriteria, "regionCriteria must not be null");
		Assert.notNull(regionCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(regionCriteria);
		Paging paging = regionCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());		
		return getSqlSessionTemplate().selectList("Region.listPk", regionCriteria, rowBounds);
	}

	public List<Region> list(RegionCriteria regionCriteria) throws DataAccessException {
		Assert.notNull(regionCriteria, "regionCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("Region.list", regionCriteria);
	}

	public List<Region> listOnPage(RegionCriteria regionCriteria) throws DataAccessException {
		Assert.notNull(regionCriteria, "regionCriteria must not be null");
		Assert.notNull(regionCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(regionCriteria);
		Paging paging = regionCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());		
		return getSqlSessionTemplate().selectList("Region.list", regionCriteria, rowBounds);
	}

	public int count(RegionCriteria regionCriteria) throws DataAccessException {
		Assert.notNull(regionCriteria, "regionCriteria must not be null");		
		return ((Integer) getSqlSessionTemplate().selectOne("Region.count", regionCriteria)).intValue();
	}

}
