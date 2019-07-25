package com.maicard.security.criteria;

import com.maicard.common.base.Criteria;

public class MenuRoleRelationCriteria extends Criteria {

	private static final long serialVersionUID = 1L;
	
	private int id;
		
	private int[] roleIds;
		
	

	public MenuRoleRelationCriteria() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	public int[] getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(int... roleIds) {
		this.roleIds = roleIds;
	}

}
