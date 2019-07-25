package com.maicard.money.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.money.criteria.PointExchangeCriteria;
import com.maicard.money.dao.PointExchangeDao;
import com.maicard.money.domain.PointExchange;

@Repository
public class PointExchangeDaoImpl extends BaseDao implements PointExchangeDao {

	public int insert(PointExchange pointExchange) throws DataAccessException {
		return (Integer)getSqlSessionTemplate().insert("PointExchange.insert", pointExchange);
	}

	public int update(PointExchange pointExchange) throws DataAccessException {
		return getSqlSessionTemplate().update("PointExchange.update", pointExchange);
	}

	public int delete(int pointExchangeId) throws DataAccessException {
		return getSqlSessionTemplate().delete("PointExchange.delete", new Integer(pointExchangeId));
	}

	public PointExchange select(int pointExchangeId) throws DataAccessException {
		return (PointExchange) getSqlSessionTemplate().selectOne("PointExchange.select", new Integer(pointExchangeId));
	}

	public List<PointExchange> list(PointExchangeCriteria pointExchangeCriteria) throws DataAccessException {
		Assert.notNull(pointExchangeCriteria, "pointExchangeCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("PointExchange.list", pointExchangeCriteria);
	}

	public List<PointExchange> listOnPage(PointExchangeCriteria pointExchangeCriteria) throws DataAccessException {
		Assert.notNull(pointExchangeCriteria, "pointExchangeCriteria must not be null");
		Assert.notNull(pointExchangeCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(pointExchangeCriteria);
		Paging paging = pointExchangeCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		return getSqlSessionTemplate().selectList("PointExchange.list", pointExchangeCriteria, rowBounds);
	}

	public int count(PointExchangeCriteria pointExchangeCriteria) throws DataAccessException {
		Assert.notNull(pointExchangeCriteria, "pointExchangeCriteria must not be null");
		
		return getSqlSessionTemplate().selectOne("PointExchange.count", pointExchangeCriteria);
	}

}
