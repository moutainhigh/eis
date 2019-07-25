package com.maicard.security.domain;


import com.maicard.common.domain.EisObject;

/**
 * 用户级别的条件配置
 * 即什么样的用户，可以享受什么样的project方案
 * 例如，用户级别==99的用户，可以享受某个特定优惠project1
 * 那么就是：userLevelConditionName = level, userLevelConditionValue = 99, userLevelProjectId = project1所对应的ID
 * 
 * 
 * @author NetSnake
 * @date 2012-9-23
 */
public class UserLevelCondition extends EisObject {

	private static final long serialVersionUID = 1L;

	private int userLevelConditionId;

	private int userLevelProjectId;

	private String userLevelConditionName;
	
	private String userLevelConditionDescription;

	private String userLevelConditionValue;
	
	private String userLevelConditionType; //是升级到下一级别的条件[1]，还是本级别享受的待遇[2]，还是降级[3]


	private int flag;

	public UserLevelCondition() {
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

	public String getUserLevelConditionName() {
		return userLevelConditionName;
	}

	public void setUserLevelConditionName(String userLevelConditionName) {
		this.userLevelConditionName = userLevelConditionName;
	}

	public String getUserLevelConditionValue() {
		return userLevelConditionValue;
	}

	public void setUserLevelConditionValue(String userLevelConditionValue) {
		this.userLevelConditionValue = userLevelConditionValue;
	}
	
	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + userLevelConditionId;

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
		final UserLevelCondition other = (UserLevelCondition) obj;
		if (userLevelConditionId != other.userLevelConditionId)
			return false;

		return true;
	}
	
	@Override
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
			"(" + 
			"userLevelConditionId=" + "'" + userLevelConditionId + "'" + 
			")";
	}

	public String getUserLevelConditionDescription() {
		return userLevelConditionDescription;
	}

	public void setUserLevelConditionDescription(
			String userLevelConditionDescription) {
		this.userLevelConditionDescription = userLevelConditionDescription;
	}

	public String getUserLevelConditionType() {
		return userLevelConditionType;
	}

	public void setUserLevelConditionType(String userLevelConditionType) {
		this.userLevelConditionType = userLevelConditionType;
	}
	
}
