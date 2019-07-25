package com.maicard.site.dao.ibatis;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.site.criteria.UserReadLogCriteria;
import com.maicard.site.dao.UserReadLogDao;
import com.maicard.site.domain.UserReadLog;
import com.maicard.standard.CommonStandard;

import org.apache.ibatis.session.RowBounds;

@Repository
public class UserReadLogDaoImpl extends BaseDao implements UserReadLogDao {

	private final String cacheName = CommonStandard.cacheNameDocument;

	public int insert(UserReadLog userReadLog) throws DataAccessException {
		return ((Integer)getSqlSessionTemplate().insert("UserReadLog.insert", userReadLog)).intValue();
	}

	@CacheEvict(value = cacheName, key = "'UserReadLog#' + #userReadLog.userReadLogId")
	public int update(UserReadLog userReadLog) throws DataAccessException {
		return getSqlSessionTemplate().update("UserReadLog.update", userReadLog);
	}

	@CacheEvict(value = cacheName, key = "'UserReadLog#' + #userReadLogId")
	public int delete(int userReadLogId) throws DataAccessException {
		return getSqlSessionTemplate().delete("UserReadLog.delete", userReadLogId);

	}

	@Cacheable(value = cacheName, key = "'UserReadLog#' + #userReadLogId")
	public UserReadLog select(int userReadLogId) throws DataAccessException {
		return getSqlSessionTemplate().selectOne("UserReadLog.select", userReadLogId);
	}
	@Override
	public List<Integer> listPk(UserReadLogCriteria userReadLogCriteria) throws DataAccessException {
		Assert.notNull(userReadLogCriteria, "userReadLogCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("UserReadLog.listPk", userReadLogCriteria);
	}

	@Override
	public List<Integer> listPkOnPage(UserReadLogCriteria userReadLogCriteria) throws DataAccessException {
		Assert.notNull(userReadLogCriteria, "userReadLogCriteria must not be null");
		Assert.notNull(userReadLogCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(userReadLogCriteria);
		Paging paging = userReadLogCriteria.getPaging();
		paging.setTotalResults(totalResults); RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		
		return getSqlSessionTemplate().selectList("UserReadLog.listPk", userReadLogCriteria, rowBounds);
	}

	@Override
	public List<UserReadLog> list(UserReadLogCriteria userReadLogCriteria) throws DataAccessException {
		Assert.notNull(userReadLogCriteria, "userReadLogCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("UserReadLog.list", userReadLogCriteria);
	}

	@Override
	public List<UserReadLog> listOnPage(UserReadLogCriteria userReadLogCriteria) throws DataAccessException {
		Assert.notNull(userReadLogCriteria, "userReadLogCriteria must not be null");
		Assert.notNull(userReadLogCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(userReadLogCriteria);
		Paging paging = userReadLogCriteria.getPaging();
		paging.setTotalResults(totalResults); RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		
		return getSqlSessionTemplate().selectList("UserReadLog.list", userReadLogCriteria, rowBounds);
	}

	@Override
	public int count(UserReadLogCriteria userReadLogCriteria) throws DataAccessException {
		Assert.notNull(userReadLogCriteria, "userReadLogCriteria must not be null");
		
		return ((Integer) getSqlSessionTemplate().selectOne("UserReadLog.count", userReadLogCriteria)).intValue();
	}

}
