package com.maicard.site.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.site.criteria.DisplayTypeCriteria;
import com.maicard.site.dao.DisplayTypeDao;
import com.maicard.site.domain.DisplayType;
import com.maicard.standard.CommonStandard;

@Repository
public class DisplayTypeDaoImpl extends BaseDao implements DisplayTypeDao {
	
	private final String cacheName = CommonStandard.cacheNameDocument;

	public int insert(DisplayType displayType) throws DataAccessException {
		return ((Integer)getSqlSessionTemplate().insert("DisplayType.insert", displayType)).intValue();
	}

	@CacheEvict(value = cacheName, key = "'DisplayType#' + #displayTypeId")
	public int update(DisplayType displayType) throws DataAccessException {
		return getSqlSessionTemplate().update("DisplayType.update", displayType);

	}

	@CacheEvict(value = cacheName, key = "'DisplayType#' + #displayTypeId")
	public int delete(int displayTypeId) throws DataAccessException {
		return getSqlSessionTemplate().delete("DisplayType.delete", new Integer(displayTypeId));
	}

	@Cacheable(value = cacheName, key = "'DisplayType#' + #displayTypeId")
	public DisplayType select(int displayTypeId) throws DataAccessException {
		return getSqlSessionTemplate().selectOne("DisplayType.select", new Integer(displayTypeId));
	}
	
	@Override
	public List<Integer> listPk(DisplayTypeCriteria displayTypeCriteria) throws DataAccessException {
		Assert.notNull(displayTypeCriteria, "displayTypeCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("DisplayType.listPk", displayTypeCriteria);
	}

	@Override
	public List<Integer> listPkOnPage(DisplayTypeCriteria displayTypeCriteria) throws DataAccessException {
		Assert.notNull(displayTypeCriteria, "displayTypeCriteria must not be null");
		Assert.notNull(displayTypeCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(displayTypeCriteria);
		Paging paging = displayTypeCriteria.getPaging();
		paging.setTotalResults(totalResults); RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		
		return getSqlSessionTemplate().selectList("DisplayType.listPk", displayTypeCriteria, rowBounds);
	}


	public List<DisplayType> list(DisplayTypeCriteria displayTypeCriteria) throws DataAccessException {
		Assert.notNull(displayTypeCriteria, "displayTypeCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("DisplayType.list", displayTypeCriteria);
	}


	public List<DisplayType> listOnPage(DisplayTypeCriteria displayTypeCriteria) throws DataAccessException {
		Assert.notNull(displayTypeCriteria, "displayTypeCriteria must not be null");
		Assert.notNull(displayTypeCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(displayTypeCriteria);
		Paging paging = displayTypeCriteria.getPaging();
		paging.setTotalResults(totalResults); RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		
		return getSqlSessionTemplate().selectList("DisplayType.list", displayTypeCriteria, rowBounds);
	}

	public int count(DisplayTypeCriteria displayTypeCriteria) throws DataAccessException {
		Assert.notNull(displayTypeCriteria, "displayTypeCriteria must not be null");
		
		return ((Integer) getSqlSessionTemplate().selectOne("DisplayType.count", displayTypeCriteria)).intValue();
	}

}
