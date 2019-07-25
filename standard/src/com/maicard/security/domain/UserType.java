package com.maicard.security.domain;

import java.util.HashMap;

import com.maicard.common.domain.DataDefine;
import com.maicard.common.domain.EisObject;

public class UserType extends EisObject {

	private static final long serialVersionUID = 1L;

	private int userTypeId;

	private int userExtraTypeId;

	private String userTypeName;
	
	private String userExtraTypeName;
	
	/////////////////////////////////////
	private HashMap<String,DataDefine> dataDefineMap;


	public UserType() {
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

	public String getUserTypeName() {
		return userTypeName;
	}

	public void setUserTypeName(String userTypeName) {
		this.userTypeName = userTypeName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + userTypeId;

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
		final UserType other = (UserType) obj;
		if (userTypeId != other.userTypeId)
			return false;

		return true;
	}
	
	@Override
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
			"(" + 
			"userTypeId=" + "'" + userTypeId + "'" + 
			")";
	}

	public String getUserExtraTypeName() {
		return userExtraTypeName;
	}

	public void setUserExtraTypeName(String userExtraTypeName) {
		this.userExtraTypeName = userExtraTypeName;
	}

	public HashMap<String, DataDefine> getDataDefineMap() {
		return dataDefineMap;
	}

	public void setDataDefineMap(HashMap<String, DataDefine> dataDefineMap) {
		this.dataDefineMap = dataDefineMap;
	}
}
