package com.maicard.common.dao.ibatis;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.common.criteria.AreaCriteria;
import com.maicard.common.dao.AreaDao;
import com.maicard.common.domain.Area;
import com.maicard.standard.CommonStandard;

import org.apache.ibatis.session.RowBounds;

@Repository
public class AreaDaoImpl extends BaseDao implements AreaDao {
	
	private final String cacheName = CommonStandard.cacheNameDocument;
	
;

	public int insert(Area area) throws DataAccessException {
		Assert.notNull(area, "尝试插入的地区不能为空");
		
		return getSqlSessionTemplate().insert("Area.insert", area);

	}

	@CacheEvict(value = cacheName, key = "'Area#' + #area.areaId")
	public int update(Area area) throws DataAccessException {


		return getSqlSessionTemplate().update("Area.update", area);


	}
	@CacheEvict(value = cacheName, key = "'Area#' + #areaId")
	public int delete(long areaId) throws DataAccessException {
		return getSqlSessionTemplate().delete("Area.delete", areaId);

	}

	@Cacheable(value = cacheName, key = "'Area#' + #areaId")
	public Area select(long areaId) throws DataAccessException {
		if(logger.isDebugEnabled()){
			logger.debug("从数据库选择Area[" + areaId + "]");
		}
		return getSqlSessionTemplate().selectOne("Area.select", areaId);
	}

	@Override
	public List<Long> listPk(AreaCriteria areaCriteria) throws DataAccessException {
		Assert.notNull(areaCriteria, "areaCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("Area.listPk", areaCriteria);
	}


	@Override
	public List<Long> listPkOnPage(AreaCriteria areaCriteria) throws DataAccessException {
		Assert.notNull(areaCriteria, "areaCriteria must not be null");
		Assert.notNull(areaCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(areaCriteria);
		Paging paging = areaCriteria.getPaging();
		paging.setTotalResults(totalResults); RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		
		return getSqlSessionTemplate().selectList("Area.listPk", areaCriteria, rowBounds);
	}

	public List<Area> list(AreaCriteria areaCriteria) throws DataAccessException {
		Assert.notNull(areaCriteria, "areaCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("Area.list", areaCriteria);
	}


	public List<Area> listOnPage(AreaCriteria areaCriteria) throws DataAccessException {
		Assert.notNull(areaCriteria, "areaCriteria must not be null");
		Assert.notNull(areaCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(areaCriteria);
		Paging paging = areaCriteria.getPaging();
		paging.setTotalResults(totalResults); RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		
		return getSqlSessionTemplate().selectList("Area.list", areaCriteria, rowBounds);
	}

	public int count(AreaCriteria areaCriteria) throws DataAccessException {
		Assert.notNull(areaCriteria, "areaCriteria must not be null");
		Assert.isTrue(areaCriteria.getOwnerId() > 0,"ownerId不能为0");

		return ((Integer) getSqlSessionTemplate().selectOne("Area.count", areaCriteria)).intValue();
	}

}
