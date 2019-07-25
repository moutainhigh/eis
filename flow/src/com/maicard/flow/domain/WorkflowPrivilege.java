package com.maicard.flow.domain;

import com.maicard.common.domain.EisObject;
/**
 * 角色是否具备访问某个步骤的权限
 *
 *
 * @author NetSnake
 * @date 2016年6月29日
 *
 */
public class WorkflowPrivilege extends EisObject {
	
	private static final long serialVersionUID = -475596067956264510L;
	
	private long workflowPrivilegeId;	//PK
	
	private long roleId;
	
	private long workflowId;
	
	private int step;
	

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
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

	public long getWorkflowPrivilegeId() {
		return workflowPrivilegeId;
	}

	public void setWorkflowPrivilegeId(long workflowPrivilegeId) {
		this.workflowPrivilegeId = workflowPrivilegeId;
	}

	
	
	

}
