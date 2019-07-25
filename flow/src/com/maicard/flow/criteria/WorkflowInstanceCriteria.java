package com.maicard.flow.criteria;

import com.maicard.common.base.Criteria;
import com.maicard.security.domain.User;

public class WorkflowInstanceCriteria extends Criteria {

	private static final long serialVersionUID = 1L;
	
	private int workflowInstanceId;
	private String targetObjectType;
	private int targetObjectTypeId;
	private String operateCode;
	private long objectId;
	private User user;
	
	public WorkflowInstanceCriteria() {
	}

	public WorkflowInstanceCriteria(long instanceObjectId, String objectType, int objectTypeId, String operateCode, User user) {
		this.objectId = instanceObjectId;
		this.targetObjectType = objectType;
		this.targetObjectTypeId = objectTypeId;
		this.operateCode = operateCode;
		this.user = user;
		this.ownerId = user.getOwnerId();
		// TODO Auto-generated constructor stub
	}

	public int getWorkflowInstanceId() {
		return workflowInstanceId;
	}

	public void setWorkflowInstanceId(int workflowInstanceId) {
		this.workflowInstanceId = workflowInstanceId;
	}

	public String getTargetObjectType() {
		return targetObjectType;
	}

	public void setTargetObjectType(String targetObjectType) {
		this.targetObjectType = targetObjectType;
	}

	public long getObjectId() {
		return objectId;
	}

	public void setObjectId(long objectId) {
		this.objectId = objectId;
	}

	public String getOperateCode() {
		return operateCode;
	}

	public void setOperateCode(String operateCode) {
		this.operateCode = operateCode;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getTargetObjectTypeId() {
		return targetObjectTypeId;
	}

	public void setTargetObjectTypeId(int targetObjectTypeId) {
		this.targetObjectTypeId = targetObjectTypeId;
	}

}
