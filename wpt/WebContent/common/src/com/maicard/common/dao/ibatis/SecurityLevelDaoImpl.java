package com.maicard.common.dao.ibatis;

import java.util.List;



import org.apache.ibatis.session.RowBounds;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.dao.SecurityLevelDao;
import com.maicard.common.util.Paging;
import com.maicard.common.criteria.SecurityLevelCriteria;
import com.maicard.common.domain.SecurityLevel;
import com.maicard.standard.CommonStandard;

/**
 * 
 *
 * @author NetSnake
 * @date 2014-1-29 
 */
@Repository
public class SecurityLevelDaoImpl extends BaseDao implements SecurityLevelDao {

	private final String cacheName = CommonStandard.cacheNameUser;
	
	
	
	@Override
	public int insert(SecurityLevel securityLevel) throws DataAccessException {
		return getSqlSessionTemplate().insert("SecurityLevel.insert", securityLevel);
	}

	@Override
	@CacheEvict(value=cacheName, key = "'" + SecurityLevelCriteria.cachePrefix + "#' + #level")
	public int update(SecurityLevel securityLevel) throws DataAccessException {
		return getSqlSessionTemplate().update("SecurityLevel.update", securityLevel);
	}

	@Override
	@CacheEvict(value=cacheName, key = "'" + SecurityLevelCriteria.cachePrefix + "#' + #level")
	public int delete(int level) throws DataAccessException {
		return getSqlSessionTemplate().delete("SecurityLevel.delete", level);
	}

	@Override
	@Cacheable(value=cacheName, key = "'" + SecurityLevelCriteria.cachePrefix + "#' + #level")
	public SecurityLevel select(int level) throws DataAccessException {
		SecurityLevel user = getSqlSessionTemplate().selectOne("SecurityLevel.select", level);
		if(user != null){
			afterFetch(user);
		}
		return user;
	}


	@Override
	public List<SecurityLevel> list(SecurityLevelCriteria securityLevelCriteria) throws DataAccessException {
		Assert.notNull(securityLevelCriteria, "SecurityLevelCriteria must not be null");	

		List<SecurityLevel> userList = getSqlSessionTemplate().selectList("SecurityLevel.list", securityLevelCriteria);
		return userList;
	}

	@Override
	public List<Long> listPk(SecurityLevelCriteria securityLevelCriteria) throws DataAccessException {
		Assert.notNull(securityLevelCriteria, "SecurityLevelCriteria must not be null");	

		List<Long> userList = getSqlSessionTemplate().selectList("SecurityLevel.listPk", securityLevelCriteria);
		return userList;
	}



	@Override
	public List<SecurityLevel> listOnPage(SecurityLevelCriteria securityLevelCriteria) throws DataAccessException {
		Assert.notNull(securityLevelCriteria, "SecurityLevelCriteria must not be null");
		Assert.notNull(securityLevelCriteria.getPaging(), "paging must not be null");

		int totalResults = count(securityLevelCriteria);
		Paging paging = securityLevelCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());		
		return getSqlSessionTemplate().selectList("SecurityLevel.list", securityLevelCriteria, rowBounds);
	}

	@Override
	public List<Long> listPkOnPage(SecurityLevelCriteria securityLevelCriteria) throws DataAccessException {
		Assert.notNull(securityLevelCriteria, "SecurityLevelCriteria must not be null");
		Assert.notNull(securityLevelCriteria.getPaging(), "paging must not be null");

		int totalResults = count(securityLevelCriteria);
		Paging paging = securityLevelCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());		
		return getSqlSessionTemplate().selectList("SecurityLevel.listPk", securityLevelCriteria, rowBounds);
	}

	public int count(SecurityLevelCriteria securityLevelCriteria) throws DataAccessException {
		Assert.notNull(securityLevelCriteria, "SecurityLevelCriteria must not be null");

		return ((Integer) getSqlSessionTemplate().selectOne("SecurityLevel.count", securityLevelCriteria)).intValue();
	}


	private void afterFetch(SecurityLevel securityLevel){
		
	}
}
