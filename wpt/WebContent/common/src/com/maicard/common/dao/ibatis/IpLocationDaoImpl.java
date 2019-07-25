package com.maicard.common.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.criteria.IpLocationCriteria;
import com.maicard.common.dao.IpLocationDao;
import com.maicard.common.domain.IpLocation;
import com.maicard.common.util.Paging;
import com.maicard.standard.CommonStandard;

@Repository
public class IpLocationDaoImpl extends BaseDao implements IpLocationDao {

	public int insert(IpLocation ipLocation) throws DataAccessException {
		return ((Integer)getSqlSessionTemplate().insert("com.maicard.common.sql.IpLocation.insert", ipLocation)).intValue();
	}
	

	@CacheEvict(value = CommonStandard.cacheNameSupport, key = "'IpLocation#' + #ipLocation.ipLocationId")
	public int update(IpLocation ipLocation) throws DataAccessException {
		return getSqlSessionTemplate().update("com.maicard.common.sql.IpLocation.update", ipLocation);
	}

	@CacheEvict(value = CommonStandard.cacheNameSupport, key = "'IpLocation#' + #ipLocationId")
	public int delete(int ipLocationId) throws DataAccessException {
		return getSqlSessionTemplate().delete("com.maicard.common.sql.IpLocation.delete", ipLocationId);
	}

	@Cacheable(value = CommonStandard.cacheNameSupport, key = "'IpLocation#' + #ipLocationId")
	public IpLocation select(int ipLocationId) throws DataAccessException {
		return getSqlSessionTemplate().selectOne("com.maicard.common.sql.IpLocation.select",ipLocationId);
	}
	
	public List<Integer> listPk(IpLocationCriteria ipLocationCriteria) throws DataAccessException {
		Assert.notNull(ipLocationCriteria, "ipLocationCriteria must not be null");		
		return getSqlSessionTemplate().selectList("com.maicard.common.sql.IpLocation.listPk", ipLocationCriteria);
	}

	public List<Integer> listPkOnPage(IpLocationCriteria ipLocationCriteria) throws DataAccessException {
		Assert.notNull(ipLocationCriteria, "ipLocationCriteria must not be null");
		Assert.notNull(ipLocationCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(ipLocationCriteria);
		Paging paging = ipLocationCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());		
		return getSqlSessionTemplate().selectList("com.maicard.common.sql.IpLocation.listPk", ipLocationCriteria, rowBounds);
	}

	public List<IpLocation> list(IpLocationCriteria ipLocationCriteria) throws DataAccessException {
		Assert.notNull(ipLocationCriteria, "ipLocationCriteria must not be null");		
		return getSqlSessionTemplate().selectList("com.maicard.common.sql.IpLocation.list", ipLocationCriteria);
	}

	public List<IpLocation> listOnPage(IpLocationCriteria ipLocationCriteria) throws DataAccessException {
		Assert.notNull(ipLocationCriteria, "ipLocationCriteria must not be null");
		Assert.notNull(ipLocationCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(ipLocationCriteria);
		Paging paging = ipLocationCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());		
		return getSqlSessionTemplate().selectList("com.maicard.common.sql.IpLocation.list", ipLocationCriteria, rowBounds);
	}

	public int count(IpLocationCriteria ipLocationCriteria) throws DataAccessException {
		Assert.notNull(ipLocationCriteria, "ipLocationCriteria must not be null");
		
		return ((Integer) getSqlSessionTemplate().selectOne("com.maicard.common.sql.IpLocation.count", ipLocationCriteria)).intValue();
	}

}
