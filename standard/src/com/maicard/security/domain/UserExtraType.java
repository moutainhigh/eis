package com.maicard.security.domain;

import com.maicard.common.domain.EisObject;

public class UserExtraType extends EisObject {

	private static final long serialVersionUID = 1L;

	private int userExtraTypeId;
	
	private String userExtraTypeName;
	

	public UserExtraType() {
	}

	
	public int getUserExtraTypeId() {
		return userExtraTypeId;
	}

	public void setUserExtraTypeId(int userExtraTypeId) {
		this.userExtraTypeId = userExtraTypeId;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + userExtraTypeId;

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
		final UserExtraType other = (UserExtraType) obj;
		if (userExtraTypeId != other.userExtraTypeId)
			return false;

		return true;
	}
	
	@Override
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
			"(" + 
			"userExtraTypeId=" + "'" + userExtraTypeId + "'" + 
			")";
	}

	public String getUserExtraTypeName() {
		return userExtraTypeName;
	}

	public void setUserExtraTypeName(String userExtraTypeName) {
		this.userExtraTypeName = userExtraTypeName;
	}

}
