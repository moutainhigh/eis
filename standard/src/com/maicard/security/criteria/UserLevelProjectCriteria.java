package com.maicard.security.criteria;

import com.maicard.common.base.Criteria;

public class UserLevelProjectCriteria extends Criteria {

	private static final long serialVersionUID = 1L;
	
	private int userLevelProjectId;
	private long userLevelId;

	public UserLevelProjectCriteria() {
	}

	public int getUserLevelProjectId() {
		return userLevelProjectId;
	}

	public void setUserLevelProjectId(int userLevelProjectId) {
		this.userLevelProjectId = userLevelProjectId;
	}

	public long getUserLevelId() {
		return userLevelId;
	}

	public void setUserLevelId(long userLevelId) {
		this.userLevelId = userLevelId;
	}

}
