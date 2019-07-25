package com.maicard.common.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.criteria.VersionCriteria;
import com.maicard.common.dao.VersionDao;
import com.maicard.common.domain.Version;
import com.maicard.common.util.Paging;
import com.maicard.standard.CommonStandard;

@Repository
public class VersionDaoImpl extends BaseDao implements VersionDao {

	public int insert(Version version) throws DataAccessException {
		return getSqlSessionTemplate().insert("com.maicard.common.sql.Version.insert", version);
	}

	@CacheEvict(value = CommonStandard.cacheNameSupport, key = "'Version#' + #version.versionId")
	public int update(Version version) throws DataAccessException {
		return getSqlSessionTemplate().update("com.maicard.common.sql.Version.update", version);
	}

	@CacheEvict(value = CommonStandard.cacheNameSupport, key = "'Version#' + #versionId")
	public int delete(int versionId) throws DataAccessException {
		return getSqlSessionTemplate().delete("com.maicard.common.sql.Version.delete", versionId);
	}

	@Cacheable(value = CommonStandard.cacheNameSupport, key = "'Version#' + #versionId")
	public Version select(int versionId) throws DataAccessException {
		return getSqlSessionTemplate().selectOne("com.maicard.common.sql.Version.select",versionId);
	}
	
	public List<Integer> listPk(VersionCriteria versionCriteria) throws DataAccessException {
		Assert.notNull(versionCriteria, "versionCriteria must not be null");		
		return getSqlSessionTemplate().selectList("com.maicard.common.sql.Version.listPk", versionCriteria);
	}

	public List<Integer> listPkOnPage(VersionCriteria versionCriteria) throws DataAccessException {
		Assert.notNull(versionCriteria, "versionCriteria must not be null");
		Assert.notNull(versionCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(versionCriteria);
		Paging paging = versionCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());		
		return getSqlSessionTemplate().selectList("com.maicard.common.sql.Version.listPk", versionCriteria, rowBounds);
	}

	public List<Version> list(VersionCriteria versionCriteria) throws DataAccessException {
		Assert.notNull(versionCriteria, "versionCriteria must not be null");		
		return getSqlSessionTemplate().selectList("com.maicard.common.sql.Version.list", versionCriteria);
	}

	public List<Version> listOnPage(VersionCriteria versionCriteria) throws DataAccessException {
		Assert.notNull(versionCriteria, "versionCriteria must not be null");
		Assert.notNull(versionCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(versionCriteria);
		Paging paging = versionCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());		
		return getSqlSessionTemplate().selectList("com.maicard.common.sql.Version.list", versionCriteria, rowBounds);
	}

	public int count(VersionCriteria versionCriteria) throws DataAccessException {
		Assert.notNull(versionCriteria, "versionCriteria must not be null");
		
		return ((Integer) getSqlSessionTemplate().selectOne("com.maicard.common.sql.Version.count", versionCriteria)).intValue();
	}

}
