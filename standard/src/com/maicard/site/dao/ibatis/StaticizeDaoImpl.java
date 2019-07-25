package com.maicard.site.dao.ibatis;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.site.criteria.StaticizeCriteria;
import com.maicard.site.dao.StaticizeDao;
import com.maicard.site.domain.Staticize;
import com.maicard.standard.CommonStandard;

import org.apache.ibatis.session.RowBounds;

@Repository
public class StaticizeDaoImpl extends BaseDao implements StaticizeDao {

	private final String cacheName = CommonStandard.cacheNameDocument;

	public int insert(Staticize staticize) throws DataAccessException {
		return ((Integer)getSqlSessionTemplate().insert("Staticize.insert", staticize)).intValue();
	}

	@CacheEvict(value = cacheName, key = "'Staticize#' + #staticize.staticizeId")
	public int update(Staticize staticize) throws DataAccessException {
		return getSqlSessionTemplate().update("Staticize.update", staticize);
	}

	@CacheEvict(value = cacheName, key = "'Staticize#' + #staticizeId")
	public int delete(long staticizeId) throws DataAccessException {
		return getSqlSessionTemplate().delete("Staticize.delete", staticizeId);

	}

	@Cacheable(value = cacheName, key = "'Staticize#' + #staticizeId")
	public Staticize select(long staticizeId) throws DataAccessException {
		return getSqlSessionTemplate().selectOne("Staticize.select", staticizeId);
	}
	@Override
	public List<Integer> listPk(StaticizeCriteria staticizeCriteria) throws DataAccessException {
		Assert.notNull(staticizeCriteria, "staticizeCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("Staticize.listPk", staticizeCriteria);
	}

	@Override
	public List<Integer> listPkOnPage(StaticizeCriteria staticizeCriteria) throws DataAccessException {
		Assert.notNull(staticizeCriteria, "staticizeCriteria must not be null");
		Assert.notNull(staticizeCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(staticizeCriteria);
		Paging paging = staticizeCriteria.getPaging();
		paging.setTotalResults(totalResults); RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		
		return getSqlSessionTemplate().selectList("Staticize.listPk", staticizeCriteria, rowBounds);
	}

	@Override
	public List<Staticize> list(StaticizeCriteria staticizeCriteria) throws DataAccessException {
		Assert.notNull(staticizeCriteria, "staticizeCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("Staticize.list", staticizeCriteria);
	}

	@Override
	public List<Staticize> listOnPage(StaticizeCriteria staticizeCriteria) throws DataAccessException {
		Assert.notNull(staticizeCriteria, "staticizeCriteria must not be null");
		Assert.notNull(staticizeCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(staticizeCriteria);
		Paging paging = staticizeCriteria.getPaging();
		paging.setTotalResults(totalResults); RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		
		return getSqlSessionTemplate().selectList("Staticize.list", staticizeCriteria, rowBounds);
	}

	@Override
	public int count(StaticizeCriteria staticizeCriteria) throws DataAccessException {
		Assert.notNull(staticizeCriteria, "staticizeCriteria must not be null");
		
		return ((Integer) getSqlSessionTemplate().selectOne("Staticize.count", staticizeCriteria)).intValue();
	}

}
