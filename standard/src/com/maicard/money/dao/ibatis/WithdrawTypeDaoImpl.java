package com.maicard.money.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.money.criteria.WithdrawTypeCriteria;
import com.maicard.money.dao.WithdrawTypeDao;
import com.maicard.money.domain.WithdrawType;

@Repository
public class WithdrawTypeDaoImpl extends BaseDao implements WithdrawTypeDao {

	public int insert(WithdrawType withdrawType) throws DataAccessException {
		return getSqlSessionTemplate().insert("com.maicard.money.sql.WithdrawType.insert", withdrawType);
	}

	public int update(WithdrawType withdrawType) throws DataAccessException {
		return getSqlSessionTemplate().update("com.maicard.money.sql.WithdrawType.update", withdrawType);
	}

	public int delete(int withdrawTypeId) throws DataAccessException {
		return getSqlSessionTemplate().delete("com.maicard.money.sql.WithdrawType.delete", new Integer(withdrawTypeId));
	}

	public WithdrawType select(int withdrawTypeId) throws DataAccessException {
		return (WithdrawType) getSqlSessionTemplate().selectOne("com.maicard.money.sql.WithdrawType.select", new Integer(withdrawTypeId));
	}

	public List<WithdrawType> list(WithdrawTypeCriteria withdrawTypeCriteria) throws DataAccessException {
		Assert.notNull(withdrawTypeCriteria, "withdrawTypeCriteria must not be null");	
		return getSqlSessionTemplate().selectList("com.maicard.money.sql.WithdrawType.list", withdrawTypeCriteria);
	}

	public List<WithdrawType> listOnPage(WithdrawTypeCriteria withdrawTypeCriteria) throws DataAccessException {
		Assert.notNull(withdrawTypeCriteria, "withdrawTypeCriteria must not be null");
		Assert.notNull(withdrawTypeCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(withdrawTypeCriteria);
		Paging paging = withdrawTypeCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		return getSqlSessionTemplate().selectList("com.maicard.money.sql.WithdrawType.list", withdrawTypeCriteria, rowBounds);
	}

	public int count(WithdrawTypeCriteria withdrawTypeCriteria) throws DataAccessException {
		Assert.notNull(withdrawTypeCriteria, "withdrawTypeCriteria must not be null");
		
		return ((Integer) getSqlSessionTemplate().selectOne("com.maicard.money.sql.WithdrawType.count", withdrawTypeCriteria)).intValue();
	}

	@Override
	public WithdrawType selectByColumn(WithdrawTypeCriteria withdrawTypeCriteria) throws DataAccessException {
		Assert.notNull(withdrawTypeCriteria, "withdrawTypeCriteria must not be null");
		return (WithdrawType) getSqlSessionTemplate().selectOne("com.maicard.money.sql.WithdrawType.selectByColumn", withdrawTypeCriteria);

	}
}
