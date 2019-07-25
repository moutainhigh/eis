package com.maicard.flow.criteria;

import com.maicard.common.base.Criteria;

public class WorkflowPrivilegeCriteria extends Criteria {

	private static final long serialVersionUID = -1658850965313370596L;

	private long[] roleIds;

	private long workflowId;

	private int step;

	public WorkflowPrivilegeCriteria() {
	}

	public WorkflowPrivilegeCriteria(long ownerId) {
		this.ownerId = ownerId;
	}


	public long getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(long workflowId) {
		this.workflowId = workflowId;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public long[] getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(long... roleIds) {
		this.roleIds = roleIds;
	}

}
