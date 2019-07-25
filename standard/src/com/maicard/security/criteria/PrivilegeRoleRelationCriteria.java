package com.maicard.security.criteria;

import com.maicard.common.base.Criteria;

public class PrivilegeRoleRelationCriteria extends Criteria {

	private static final long serialVersionUID = 1L;
	
	private int privilegeRoleRelationId;
	private int roleId;
	private int privilegeId;
	
	public int getPrivilegeId() {
		return privilegeId;
	}

	public void setPrivilegeId(int privilegeId) {
		this.privilegeId = privilegeId;
	}
	
	public PrivilegeRoleRelationCriteria() {
	}

	public int getPrivilegeRoleRelationId() {
		return privilegeRoleRelationId;
	}

	public void setPrivilegeRoleRelationId(int sysPrivilegeGroupRelationId) {
		this.privilegeRoleRelationId = sysPrivilegeGroupRelationId;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}


}
