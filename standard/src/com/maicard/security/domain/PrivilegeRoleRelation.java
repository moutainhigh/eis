package com.maicard.security.domain;


import com.maicard.common.domain.EisObject;

//权限与角色的对应关系
public class PrivilegeRoleRelation extends EisObject {


	private static final long serialVersionUID = -5117226112185050521L;

	private int privilegeRoleRelationId;

	private int privilegeId;

	private int roleId;
	
	public PrivilegeRoleRelation() {
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + privilegeRoleRelationId;

		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final PrivilegeRoleRelation other = (PrivilegeRoleRelation) obj;
		if (privilegeRoleRelationId != other.privilegeRoleRelationId)
			return false;

		return true;
	}
	
	@Override
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
			"(" + 
			"frontPrivilegeRoleRelationId=" + "'" + privilegeRoleRelationId + "'" + 
			")";
	}


	public int getPrivilegeRoleRelationId() {
		return privilegeRoleRelationId;
	}


	public void setPrivilegeRoleRelationId(int privilegeRoleRelationId) {
		this.privilegeRoleRelationId = privilegeRoleRelationId;
	}


	public int getPrivilegeId() {
		return privilegeId;
	}


	public void setPrivilegeId(int privilegeId) {
		this.privilegeId = privilegeId;
	}


	public int getRoleId() {
		return roleId;
	}


	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	
}
