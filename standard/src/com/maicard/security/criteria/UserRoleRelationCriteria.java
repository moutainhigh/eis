package com.maicard.security.criteria;

import com.maicard.common.base.Criteria;

public class UserRoleRelationCriteria extends Criteria {

	private static final long serialVersionUID = 1L;

	private int id;
	private long uuid;
	private int roleId;

	public UserRoleRelationCriteria() {
	}

	public UserRoleRelationCriteria(long ownerId) {
		this.ownerId = ownerId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getUuid() {
		return uuid;
	}

	public void setUuid(long uuid) {
		this.uuid = uuid;
	}
	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

}
