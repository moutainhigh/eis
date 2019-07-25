package com.maicard.money.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.money.criteria.PointExchangeLogCriteria;
import com.maicard.money.dao.PointExchangeLogDao;
import com.maicard.money.domain.PointExchange;

@Repository
public class PointExchangeLogDaoImpl extends BaseDao implements PointExchangeLogDao {

	public int insert(PointExchange pointExchangeLog) throws DataAccessException {
		return (Integer)getSqlSessionTemplate().insert("PointExchangeLog.insert", pointExchangeLog);
	}

	public int update(PointExchange pointExchangeLog) throws DataAccessException {
		return getSqlSessionTemplate().update("PointExchangeLog.update", pointExchangeLog);
	}

	public int delete(int pointExchangeLogId) throws DataAccessException {
		return getSqlSessionTemplate().delete("PointExchangeLog.delete", new Integer(pointExchangeLogId));
	}

	public PointExchange select(int pointExchangeLogId) throws DataAccessException {
		return (PointExchange) getSqlSessionTemplate().selectOne("PointExchangeLog.select", new Integer(pointExchangeLogId));
	}

	public List<PointExchange> list(PointExchangeLogCriteria pointExchangeLogCriteria) throws DataAccessException {
		Assert.notNull(pointExchangeLogCriteria, "pointExchangeLogCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("PointExchange.list", pointExchangeLogCriteria);
	}

	public List<PointExchange> listOnPage(PointExchangeLogCriteria pointExchangeLogCriteria) throws DataAccessException {
		Assert.notNull(pointExchangeLogCriteria, "pointExchangeLogCriteria must not be null");
		Assert.notNull(pointExchangeLogCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(pointExchangeLogCriteria);
		Paging paging = pointExchangeLogCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		return getSqlSessionTemplate().selectList("PointExchangeLog.list", pointExchangeLogCriteria, rowBounds);
	}

	public int count(PointExchangeLogCriteria pointExchangeLogCriteria) throws DataAccessException {
		Assert.notNull(pointExchangeLogCriteria, "pointExchangeLogCriteria must not be null");
		
		return getSqlSessionTemplate().selectOne("PointExchangeLog.count", pointExchangeLogCriteria);
	}

}
