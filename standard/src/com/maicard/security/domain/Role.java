package com.maicard.security.domain;

import java.util.List;

import com.maicard.common.domain.EisObject;

//角色
public class Role extends EisObject implements Cloneable{

	private static final long serialVersionUID = 1L;

	private int roleId;

	private int parentRoleId;

	private int roleType;//类型:1=组织节点,相当于组,2=节点中的角色，相当于岗位

	/**
	 * 2=商户merchant的角色
	 */
	private int roleLevel;		

	private String roleName;

	private String roleDescription;


	private int flag;

	//非持久化属性	
	private List<Privilege> relatedPrivilegeList;

	@Override
	public Role clone(){
		return (Role)super.clone();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + roleId;

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
		final Role other = (Role) obj;
		if (roleId != other.roleId)
			return false;

		return true;
	}

	@Override
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
				"(" + 
				"roleId=" + "'" + roleId + "'," + 
				"roleName=" + "'" + roleName + "'," + 
				"roleType=" + "'" + roleType + "'," + 
				"parentRoleId=" + "'" + parentRoleId + "'," + 
				"currentStatus=" + "'" + currentStatus + "'" + 
				")";
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getParentRoleId() {
		return parentRoleId;
	}

	public void setParentRoleId(int parentRoleId) {
		this.parentRoleId = parentRoleId;
	}

	public int getRoleType() {
		return roleType;
	}

	public void setRoleType(int roleType) {
		this.roleType = roleType;
	}

	public int getRoleLevel() {
		return roleLevel;
	}

	public void setRoleLevel(int roleLevel) {
		this.roleLevel = roleLevel;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleDescription() {
		return roleDescription;
	}

	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
	}


	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}


	public List<Privilege> getRelatedPrivilegeList() {
		return relatedPrivilegeList;
	}

	public void setRelatedPrivilegeList(List<Privilege> relatedPrivilegeList) {
		this.relatedPrivilegeList = relatedPrivilegeList;
	}



}
