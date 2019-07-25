package com.maicard.flow.criteria;

import com.maicard.common.base.Criteria;

public class WorkflowRelationCriteria extends Criteria {

	private static final long serialVersionUID = 1L;
	
	private int workflowId;
	
	private int relatedObjectType;
	
	private int relatedObjectId;
	
	public WorkflowRelationCriteria() {
	}

	public int getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(int workflowId) {
		this.workflowId = workflowId;
	}

	public int getRelatedObjectType() {
		return relatedObjectType;
	}

	public void setRelatedObjectType(int relatedObjectType) {
		this.relatedObjectType = relatedObjectType;
	}

	public int getRelatedObjectId() {
		return relatedObjectId;
	}

	public void setRelatedObjectId(int relatedObjectId) {
		this.relatedObjectId = relatedObjectId;
	}

}
