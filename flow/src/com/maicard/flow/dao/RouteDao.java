package com.maicard.flow.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.flow.criteria.RouteCriteria;
import com.maicard.flow.domain.Route;

public interface RouteDao {

	void insert(Route route) throws DataAccessException;

	int update(Route route) throws DataAccessException;

	int delete(int routeId) throws DataAccessException;

	Route select(int routeId) throws DataAccessException;

	List<Route> list(RouteCriteria routeCriteria) throws DataAccessException;
	
	List<Route> listOnPage(RouteCriteria routeCriteria) throws DataAccessException;
	
	int count(RouteCriteria routeCriteria) throws DataAccessException;

}
