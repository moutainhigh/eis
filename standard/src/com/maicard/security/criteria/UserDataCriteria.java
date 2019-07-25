package com.maicard.security.criteria;

import com.maicard.common.base.Criteria;

public class UserDataCriteria extends Criteria {

	private static final long serialVersionUID = 1L;
	
	private int userDataId;
	private int dataDefineId;
	private String dataCode;
	private String dataValue;
	private long uuid;
	private int userTypeId;
	private int userExtraTypeId;
	private String tableName; //分表存储时的表名
	private boolean correctWithDynamicData = true; //当返回的数据没有数据规范中定义的所有数据项时，是否动态补充数据规范中所定义的项目


	public UserDataCriteria() {
	}

	public int getUserDataId() {
		return userDataId;
	}

	public void setUserDataId(int userDataId) {
		this.userDataId = userDataId;
	}

	public String getDataCode() {
		return dataCode;
	}

	public void setDataCode(String configName) {
		this.dataCode = configName;
	}

	public long getUuid() {
		return uuid;
	}

	public void setUuid(long uuid) {
		this.uuid = uuid;
	}

	public String getDataValue() {
		return dataValue;
	}

	public void setDataValue(String configValue) {
		this.dataValue = configValue;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public int getDataDefineId() {
		return dataDefineId;
	}

	public void setDataDefineId(int dataDefineId) {
		this.dataDefineId = dataDefineId;
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

	public boolean isCorrectWithDynamicData() {
		return correctWithDynamicData;
	}

	public void setCorrectWithDynamicData(boolean correctWithDynamicData) {
		this.correctWithDynamicData = correctWithDynamicData;
	}
	
	@Override
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
				"(" + 
				"userDataId=" + "'" + userDataId + "'," + 
				"dataDefineId=" + "'" + dataDefineId + "'," + 
				"dataCode=" + "'" + dataCode + "'," + 
				"dataValue=" + "'" + dataValue + "'," + 
				"uuid=" + "'" + uuid + "'," + 
				"userTypeId=" + "'" + userTypeId + "'," + 
				"userExtraTypeId=" + "'" + userExtraTypeId + "'," + 
				"tableName=" + "'" + tableName + "'," + 
				")";
	}

}
