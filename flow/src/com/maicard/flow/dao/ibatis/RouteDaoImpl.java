package com.maicard.flow.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.flow.criteria.RouteCriteria;
import com.maicard.flow.dao.RouteDao;
import com.maicard.flow.domain.Route;

@Repository
public class RouteDaoImpl extends BaseDao implements RouteDao {

	public void insert(Route route) throws DataAccessException {
		getSqlSessionTemplate().insert("com.maicard.flow.sql.Route.insert", route);
	}

	public int update(Route route) throws DataAccessException {
		return getSqlSessionTemplate().update("com.maicard.flow.sql.Route.update", route);
	}

	public int delete(int routeId) throws DataAccessException {
		return getSqlSessionTemplate().delete("com.maicard.flow.sql.Route.delete", new Integer(routeId));
	}

	public Route select(int routeId) throws DataAccessException {
		return (Route) getSqlSessionTemplate().selectOne("com.maicard.flow.sql.Route.select", new Integer(routeId));
	}

	public List<Route> list(RouteCriteria routeCriteria) throws DataAccessException {
		Assert.notNull(routeCriteria, "routeCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("com.maicard.flow.sql.Route.list", routeCriteria);
	}

	public List<Route> listOnPage(RouteCriteria routeCriteria) throws DataAccessException {
		Assert.notNull(routeCriteria, "routeCriteria must not be null");
		Assert.notNull(routeCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(routeCriteria);
		Paging paging = routeCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());				
		return getSqlSessionTemplate().selectList("com.maicard.flow.sql.Route.list", routeCriteria, rowBounds);
	}

	public int count(RouteCriteria routeCriteria) throws DataAccessException {
		Assert.notNull(routeCriteria, "routeCriteria must not be null");
		
		return getSqlSessionTemplate().selectOne("com.maicard.flow.sql.Route.count", routeCriteria);
	}

}
