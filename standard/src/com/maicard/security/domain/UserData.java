package com.maicard.security.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.maicard.common.domain.DataDefine;
import com.maicard.exception.DataInvalidException;
import com.maicard.standard.BasicStatus;


/**
 * 用户的配置数据
 * @author NetSnake
 * @date 2012-9-23
 */

@JsonInclude(Include.NON_EMPTY)
public class UserData extends DataDefine  {

	private static final long serialVersionUID = 1L;

	private Integer userDataId  = 0;
	
	private long uuid = 0;

	private String dataValue;
	
	
	//非持久化属性	
	private String statusName;	
	
	public UserData() {
	}
	
	public UserData(DataDefine dataDefine){
		this.setDataDefineId(dataDefine.getDataDefineId());
		this.setDataCode(dataDefine.getDataCode());
		this.setDataDescription(dataDefine.getDataDescription());
		this.setCurrentStatus(dataDefine.getCurrentStatus());
		this.setDataType(dataDefine.getDataType());
		this.setValidDataEnum(dataDefine.getValidDataEnum());
		this.setFlag(BasicStatus.dynamic.getId());
		this.setInputLevel(dataDefine.getInputLevel());
		this.setInputMethod(dataDefine.getInputMethod());
		
	}

	public UserData(long uuid){
		if(uuid < 1){
			throw new DataInvalidException("UUID必须大于0");
		}
		this.uuid = uuid;
	}
	public UserData(long uuid, String dataCode, String dataValue){
		this.uuid = uuid;
		this.dataCode = dataCode;
		this.dataValue = dataValue;
	}


	public Integer getUserDataId() {
		return userDataId;
	}

	public void setUserDataId(Integer userDataId) {
		this.userDataId = userDataId;
	}

	public long getUuid() {
		return uuid;
	}

	public void setUuid(long uuid) {
		this.uuid = uuid;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + userDataId;

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
		final UserData other = (UserData) obj;
		if (userDataId != other.userDataId)
			return false;

		return true;
	}
	
	@Override
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
			"(" + 
			"userConfigId=" + "'" + userDataId + "'" + 
			")";
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName == null ? null : statusName.trim();
	}




	public String getDataValue() {
		return dataValue;
	}

	public void setDataValue(String dataValue) {
		this.dataValue = dataValue == null ? null : dataValue.trim();
	}

	
}
