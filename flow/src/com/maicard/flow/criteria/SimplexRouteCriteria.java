package com.maicard.flow.criteria;

import com.maicard.common.base.Criteria;


public class SimplexRouteCriteria extends Criteria {

	private static final long serialVersionUID = 1L;
	
	private int routeId;

	public SimplexRouteCriteria() {
	}

	public int getRouteId() {
		return routeId;
	}

	public void setRouteId(int routeId) {
		this.routeId = routeId;
	}

}
