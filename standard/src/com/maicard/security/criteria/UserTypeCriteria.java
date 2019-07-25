package com.maicard.security.criteria;

import com.maicard.common.base.Criteria;

public class UserTypeCriteria extends Criteria {

	private static final long serialVersionUID = 1L;
	
	private int userTypeId;
	private int userExtraTypeId;

	public UserTypeCriteria() {
	}

	public int getUserTypeId() {
		return userTypeId;
	}

	public void setUserTypeId(int userTypeId) {
		this.userTypeId = userTypeId;
	}

	public int getUserExtraTypeId() {
		return userExtraTypeId;
	}

	public void setUserExtraTypeId(int userExtraTypeId) {
		this.userExtraTypeId = userExtraTypeId;
	}


}
