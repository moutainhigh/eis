package com.maicard.security.criteria;

import com.maicard.common.base.Criteria;

public class PrivilegeRelationCriteria extends Criteria {

	private static final long serialVersionUID = 1L;
	
	private int privilegeRelationId;	
	private int privilegeId;	
	private int objectTypeId;
	private int objectId;
	
	public PrivilegeRelationCriteria() {
	}

	public int getPrivilegeRelationId() {
		return privilegeRelationId;
	}

	public void setPrivilegeRelationId(int privilegeRelationId) {
		this.privilegeRelationId = privilegeRelationId;
	}

	public int getPrivilegeId() {
		return privilegeId;
	}

	public void setPrivilegeId(int privilegeId) {
		this.privilegeId = privilegeId;
	}

	public int getObjectTypeId() {
		return objectTypeId;
	}

	public void setObjectTypeId(int objectTypeId) {
		this.objectTypeId = objectTypeId;
	}

	public int getObjectId() {
		return objectId;
	}

	public void setObjectId(int objectId) {
		this.objectId = objectId;
	}


}
