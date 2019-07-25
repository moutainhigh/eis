package com.maicard.money.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.money.criteria.WithdrawMethodCriteria;
import com.maicard.money.dao.WithdrawMethodDao;
import com.maicard.money.domain.WithdrawMethod;

@Repository
public class WithdrawMethodDaoImpl extends BaseDao implements WithdrawMethodDao {

	public int insert(WithdrawMethod withdrawMethod) throws DataAccessException {
		return getSqlSessionTemplate().insert("com.maicard.money.sql.WithdrawMethod.insert", withdrawMethod);
	}

	public int update(WithdrawMethod withdrawMethod) throws DataAccessException {
		return getSqlSessionTemplate().update("com.maicard.money.sql.WithdrawMethod.update", withdrawMethod);
	}

	public int delete(int withdrawMethodId) throws DataAccessException {
		return getSqlSessionTemplate().delete("com.maicard.money.sql.WithdrawMethod.delete", new Integer(withdrawMethodId));
	}

	public WithdrawMethod select(int withdrawMethodId) throws DataAccessException {
		return (WithdrawMethod) getSqlSessionTemplate().selectOne("com.maicard.money.sql.WithdrawMethod.select", new Integer(withdrawMethodId));
	}

	public List<WithdrawMethod> list(WithdrawMethodCriteria withdrawMethodCriteria) throws DataAccessException {
		Assert.notNull(withdrawMethodCriteria, "withdrawMethodCriteria must not be null");	
		return getSqlSessionTemplate().selectList("com.maicard.money.sql.WithdrawMethod.list", withdrawMethodCriteria);
	}

	public List<WithdrawMethod> listOnPage(WithdrawMethodCriteria withdrawMethodCriteria) throws DataAccessException {
		Assert.notNull(withdrawMethodCriteria, "withdrawMethodCriteria must not be null");
		Assert.notNull(withdrawMethodCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(withdrawMethodCriteria);
		Paging paging = withdrawMethodCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		return getSqlSessionTemplate().selectList("com.maicard.money.sql.WithdrawMethod.list", withdrawMethodCriteria, rowBounds);
	}

	public int count(WithdrawMethodCriteria withdrawMethodCriteria) throws DataAccessException {
		Assert.notNull(withdrawMethodCriteria, "withdrawMethodCriteria must not be null");
		
		return ((Integer) getSqlSessionTemplate().selectOne("com.maicard.money.sql.WithdrawMethod.count", withdrawMethodCriteria)).intValue();
	}

}
