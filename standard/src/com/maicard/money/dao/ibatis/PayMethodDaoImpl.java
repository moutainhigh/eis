package com.maicard.money.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.money.criteria.PayMethodCriteria;
import com.maicard.money.dao.PayMethodDao;
import com.maicard.money.domain.PayMethod;
import com.maicard.standard.CommonStandard;

@Repository
public class PayMethodDaoImpl extends BaseDao implements PayMethodDao {

	@Cacheable(value = CommonStandard.cacheNameProduct, key = "'PayMethod#' + #payMethodId")
	public int insert(PayMethod payMethod) throws DataAccessException {
		return getSqlSessionTemplate().insert("com.maicard.money.sql.PayMethod.insert", payMethod);
	}

	public int update(PayMethod payMethod) throws DataAccessException {
		int rs = getSqlSessionTemplate().update("com.maicard.money.sql.PayMethod.update", payMethod);
		return rs;
	}

	@CacheEvict(value = CommonStandard.cacheNameProduct, key = "'PayMethod#' + #payMethodId")
	public int delete(int payMethodId) throws DataAccessException {
		return getSqlSessionTemplate().delete("com.maicard.money.sql.PayMethod.delete", new Integer(payMethodId));
	}

	@Cacheable(value = CommonStandard.cacheNameProduct, key = "'PayMethod#' + #payMethodId")
	public PayMethod select(int payMethodId) throws DataAccessException {
		return (PayMethod) getSqlSessionTemplate().selectOne("com.maicard.money.sql.PayMethod.select", new Integer(payMethodId));
	}

	public List<Integer> listPkOnPage(PayMethodCriteria payMethodCriteria) throws DataAccessException {
		Assert.notNull(payMethodCriteria, "payMethodCriteria must not be null");
		//Assert.notNull(payMethodCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(payMethodCriteria);
		Paging paging = payMethodCriteria.getPaging();
		if(paging == null) {
			return getSqlSessionTemplate().selectList("com.maicard.money.sql.PayMethod.listPk", payMethodCriteria);
		}
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		return getSqlSessionTemplate().selectList("com.maicard.money.sql.PayMethod.listPk", payMethodCriteria, rowBounds);
	}

	public int count(PayMethodCriteria payMethodCriteria) throws DataAccessException {
		Assert.notNull(payMethodCriteria, "payMethodCriteria must not be null");
		
		return ((Integer) getSqlSessionTemplate().selectOne("com.maicard.money.sql.PayMethod.count", payMethodCriteria)).intValue();
	}

}
