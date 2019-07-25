package com.maicard.security.domain;

import com.maicard.common.domain.EisObject;

//权限的关联对象
public class PrivilegeRelation extends EisObject {

	private static final long serialVersionUID = 1L;

	private int privilegeRelationId;
	private int privilegeId;
	private String objectType;
	private int objectTypeId;
	private String objectId;

	//非持久化属性		
	
	public PrivilegeRelation() {
	}

	public int getPrivilegeId() {
		return privilegeId;
	}

	public void setPrivilegeId(int privilegeId) {
		this.privilegeId = privilegeId;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + privilegeId;

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
		final PrivilegeRelation other = (PrivilegeRelation) obj;
		if (privilegeId != other.privilegeId)
			return false;

		return true;
	}

	@Override
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
				"(" + 
				"privilegeId=" + "'" + privilegeId + "'" + 
				")";
	}

	public int getPrivilegeRelationId() {
		return privilegeRelationId;
	}

	public void setPrivilegeRelationId(int privilegeRelationId) {
		this.privilegeRelationId = privilegeRelationId;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public int getObjectTypeId() {
		return objectTypeId;
	}

	public void setObjectTypeId(int objectTypeId) {
		this.objectTypeId = objectTypeId;
	}
}
