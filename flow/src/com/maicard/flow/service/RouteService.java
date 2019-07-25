package com.maicard.flow.service;

import java.util.List;

import com.maicard.flow.criteria.RouteCriteria;
import com.maicard.flow.domain.Route;
import com.maicard.security.domain.User;

public interface RouteService {

	void insert(Route route);

	int update(Route route);

	int delete(int routeId);
	
	Route select(int routeId);

	List<Route> list(RouteCriteria routeCriteria);

	List<Route> listOnPage(RouteCriteria routeCriteria);

	int count(RouteCriteria routeCriteria);

	boolean havePrivilege(User user, long objectId, Route route);

}
