package com.maicard.security.domain;

import java.util.HashMap;

import com.maicard.common.domain.EisObject;

/**
 * 用户所享受的特定方案
 * 这里只对方案做基本定义，具体如何实施，由程序去实现
 * @author NetSnake
 * @date 2012-9-23
 */
public class UserLevelProject extends EisObject {

	private static final long serialVersionUID = 1L;

	private int userLevelProjectId;

	private int userLevelId;

	private int userLevelType;

	private int userType;

	private String userLevelProjectName;

	private String userLevelProjectDescripition;


	private int flag;

	//非持久化属性	
	
	private HashMap<String, UserLevelCondition> userLevelConditionMap;

	public UserLevelProject() {
	}

	public int getUserLevelProjectId() {
		return userLevelProjectId;
	}

	public void setUserLevelProjectId(int userLevelProjectId) {
		this.userLevelProjectId = userLevelProjectId;
	}

	public int getUserLevelId() {
		return userLevelId;
	}

	public void setUserLevelId(int userLevelId) {
		this.userLevelId = userLevelId;
	}

	public int getUserLevelType() {
		return userLevelType;
	}

	public void setUserLevelType(int userLevelType) {
		this.userLevelType = userLevelType;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public String getUserLevelProjectName() {
		return userLevelProjectName;
	}

	public void setUserLevelProjectName(String userLevelProjectName) {
		this.userLevelProjectName = userLevelProjectName;
	}

	public String getUserLevelProjectDescripition() {
		return userLevelProjectDescripition;
	}

	public void setUserLevelProjectDescripition(String userLevelProjectDescripition) {
		this.userLevelProjectDescripition = userLevelProjectDescripition;
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
		result = prime * result + userLevelProjectId;

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
		final UserLevelProject other = (UserLevelProject) obj;
		if (userLevelProjectId != other.userLevelProjectId)
			return false;

		return true;
	}

	@Override
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
				"(" + 
				"userLevelProjectId=" + "'" + userLevelProjectId + "'" + 
				")";
	}

	public HashMap<String, UserLevelCondition> getUserLevelConditionMap() {
		return userLevelConditionMap;
	}

	public void setUserLevelConditionMap(
			HashMap<String, UserLevelCondition> userLevelConditionMap) {
		this.userLevelConditionMap = userLevelConditionMap;
	}

}
