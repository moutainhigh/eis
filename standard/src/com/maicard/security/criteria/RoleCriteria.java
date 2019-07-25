package com.maicard.security.criteria;

import com.maicard.common.base.Criteria;

public class RoleCriteria extends Criteria {

	private static final long serialVersionUID = 1L;
	
	
	/**
	 * 该角色是一个部门
	 */
	public static final int ROLE_TYPE_DEPARTMENT = 1;		//组织结构中的部门类型
	
	/**
	 * 该角色是一个用户
	 */
	public static final int ROLE_TYPE_USER = 2;				//组织结构中的岗位、用户类型
	
	/**
	 * 内部角色
	 */
	public static final int ROLE_LEVEL_INTERNAL = 1;	

	/**
	 * 商户merchant可以分配的角色
	 */
	public static final int ROLE_LEVEL_MERCHANT = 2;	
	
	private int roleId;
	private int roleType;
	
	private int roleLevel;
	



	public RoleCriteria() {
	}
	public RoleCriteria(long ownerId) {
		this.ownerId = ownerId;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
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

}
