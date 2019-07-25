package com.maicard.flow.criteria;

import com.maicard.common.base.Criteria;

public class WorkflowInstanceLogCriteria extends Criteria {

	private static final long serialVersionUID = 1L;
	
	private int workflowInstanceLogId;

	public WorkflowInstanceLogCriteria() {
	}

	public int getWorkflowInstanceLogId() {
		return workflowInstanceLogId;
	}

	public void setWorkflowInstanceLogId(int workflowInstanceLogId) {
		this.workflowInstanceLogId = workflowInstanceLogId;
	}

}
