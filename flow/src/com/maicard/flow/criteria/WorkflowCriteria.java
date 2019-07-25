package com.maicard.flow.criteria;

import com.maicard.common.base.Criteria;

public class WorkflowCriteria extends Criteria {

	private static final long serialVersionUID = 1L;
	
	private int workflowId;
	private String targetObjectType;
	private String objectType;

	public WorkflowCriteria() {
	}
	public WorkflowCriteria(long ownerId) {
		this.ownerId = ownerId;
	}

	public int getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(int workflowId) {
		this.workflowId = workflowId;
	}

	public String getOperateCode() {
		return targetObjectType;
	}

	public void setOperateCode(String operateCode) {
		this.targetObjectType = operateCode;
	}

	public String getTargetObjectType() {
		return objectType;
	}

	public void setTargetObjectType(String targetObjectType) {
		this.objectType = targetObjectType;
	}

}
