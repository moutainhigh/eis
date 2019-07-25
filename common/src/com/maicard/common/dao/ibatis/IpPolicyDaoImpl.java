package com.maicard.common.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.criteria.IpPolicyCriteria;
import com.maicard.common.dao.IpPolicyDao;
import com.maicard.common.domain.IpPolicy;
import com.maicard.common.util.Paging;
import com.maicard.standard.CommonStandard;

@Repository
public class IpPolicyDaoImpl extends BaseDao implements IpPolicyDao {

	public int insert(IpPolicy ipPolicy) throws DataAccessException {
		return ((Integer)getSqlSessionTemplate().insert("IpPolicy.insert", ipPolicy)).intValue();
	}
	
	public  void updateWrong(IpPolicy ipPolicy) throws DataAccessException {
		getSqlSessionTemplate().insert("IpPolicy.insertwrong", ipPolicy);
	}

	@CacheEvict(value = CommonStandard.cacheNameSupport, key = "'IpPolicy#' + #ipPolicy.ipPolicyId")
	public int update(IpPolicy ipPolicy) throws DataAccessException {
		return getSqlSessionTemplate().update("IpPolicy.update", ipPolicy);
	}

	@CacheEvict(value = CommonStandard.cacheNameSupport, key = "'IpPolicy#' + #ipPolicyId")
	public int delete(int ipPolicyId) throws DataAccessException {
		return getSqlSessionTemplate().delete("IpPolicy.delete", ipPolicyId);
	}

	@Cacheable(value = CommonStandard.cacheNameSupport, key = "'IpPolicy#' + #ipPolicyId")
	public IpPolicy select(int ipPolicyId) throws DataAccessException {
		return getSqlSessionTemplate().selectOne("IpPolicy.select",ipPolicyId);
	}
	
	public List<Integer> listPk(IpPolicyCriteria ipPolicyCriteria) throws DataAccessException {
		Assert.notNull(ipPolicyCriteria, "ipPolicyCriteria must not be null");		
		return getSqlSessionTemplate().selectList("IpPolicy.listPk", ipPolicyCriteria);
	}

	public List<Integer> listPkOnPage(IpPolicyCriteria ipPolicyCriteria) throws DataAccessException {
		Assert.notNull(ipPolicyCriteria, "ipPolicyCriteria must not be null");
		Assert.notNull(ipPolicyCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(ipPolicyCriteria);
		Paging paging = ipPolicyCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());		
		return getSqlSessionTemplate().selectList("IpPolicy.listPk", ipPolicyCriteria, rowBounds);
	}

	public List<IpPolicy> list(IpPolicyCriteria ipPolicyCriteria) throws DataAccessException {
		Assert.notNull(ipPolicyCriteria, "ipPolicyCriteria must not be null");		
		return getSqlSessionTemplate().selectList("IpPolicy.list", ipPolicyCriteria);
	}

	public List<IpPolicy> listOnPage(IpPolicyCriteria ipPolicyCriteria) throws DataAccessException {
		Assert.notNull(ipPolicyCriteria, "ipPolicyCriteria must not be null");
		Assert.notNull(ipPolicyCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(ipPolicyCriteria);
		Paging paging = ipPolicyCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());		
		return getSqlSessionTemplate().selectList("IpPolicy.list", ipPolicyCriteria, rowBounds);
	}

	public int count(IpPolicyCriteria ipPolicyCriteria) throws DataAccessException {
		Assert.notNull(ipPolicyCriteria, "ipPolicyCriteria must not be null");
		
		return ((Integer) getSqlSessionTemplate().selectOne("IpPolicy.count", ipPolicyCriteria)).intValue();
	}

}
