package com.maicard.flow.criteria;

import com.maicard.common.base.Criteria;

public class RouteCriteria extends Criteria {

	private static final long serialVersionUID = 1L;

	private int routeId;
	private int workflowId;

	public RouteCriteria() {
	}

	public RouteCriteria(long ownerId) {
		this.ownerId = ownerId;
	}

	public int getRouteId() {
		return routeId;
	}

	public void setRouteId(int routeId) {
		this.routeId = routeId;
	}

	public int getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(int workflowId) {
		this.workflowId = workflowId;
	}

}
