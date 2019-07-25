package com.maicard.security.criteria;

import com.maicard.common.base.Criteria;

public class UserLevelConditionCriteria extends Criteria {

	private static final long serialVersionUID = 1L;
	
	private int userLevelConditionId;
	private int userLevelProjectId;
	private String userLevelConditionType;


	public UserLevelConditionCriteria() {
	}

	public int getUserLevelConditionId() {
		return userLevelConditionId;
	}

	public void setUserLevelConditionId(int userLevelConditionId) {
		this.userLevelConditionId = userLevelConditionId;
	}

	public int getUserLevelProjectId() {
		return userLevelProjectId;
	}

	public void setUserLevelProjectId(int userLevelProjectId) {
		this.userLevelProjectId = userLevelProjectId;
	}

	public String getUserLevelConditionType() {
		return userLevelConditionType;
	}

	public void setUserLevelConditionType(String userLevelConditionType) {
		this.userLevelConditionType = userLevelConditionType;
	}

}
