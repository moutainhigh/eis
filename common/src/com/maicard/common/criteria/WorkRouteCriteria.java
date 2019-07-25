package com.maicard.common.criteria;

import com.maicard.common.base.Criteria;

public class WorkRouteCriteria extends Criteria {

	private static final long serialVersionUID = 1L;
	
	private int workRouteId;
	
	private int workflowId;
	
	public WorkRouteCriteria() {
	}

	public int getWorkRouteId() {
		return workRouteId;
	}

	public void setWorkRouteId(int workRouteId) {
		this.workRouteId = workRouteId;
	}

	public int getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(int workflowId) {
		this.workflowId = workflowId;
	}

}
