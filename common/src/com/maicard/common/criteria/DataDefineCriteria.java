package com.maicard.common.criteria;

import com.maicard.common.base.Criteria;

public class DataDefineCriteria extends Criteria {

	private static final long serialVersionUID = 1L;
	
	protected int dataDefineId;

	protected String dataCode;

	protected String dataType;
	
	protected String objectType;
	
	protected long objectExtraId;
	
	protected long objectId;
	
	protected String inputMethod;
	
	protected String displayLevel;
	

	public DataDefineCriteria() {
	}
	

	public DataDefineCriteria(long ownerId) {
		this.ownerId = ownerId;
	}
	
	public DataDefineCriteria(String objectType){
		this.objectType = objectType;
	}
	
	public DataDefineCriteria(String objectType, long objectId){
		this.objectType = objectType;
		this.objectId = objectId;
	}

	public DataDefineCriteria(String objectType, long objectId, long ownerId) {
		this.objectType = objectType;
		this.objectId = objectId;
		this.ownerId = ownerId;
	}

	
	public String getDataCode() {
		return dataCode;
	}

	public void setDataCode(String dataCode) {
		this.dataCode = dataCode;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}



	public String getInputMethod() {
		return inputMethod;
	}

	public void setInputMethod(String inputMethod) {
		this.inputMethod = inputMethod;
	}

	public String getDisplayLevel() {
		return displayLevel;
	}

	public void setDisplayLevel(String displayLevel) {
		this.displayLevel = displayLevel;
	}

	public int getDataDefineId() {
		return dataDefineId;
	}

	public void setDataDefineId(int dataDefineId) {
		this.dataDefineId = dataDefineId;
	}

	public long getObjectExtraId() {
		return objectExtraId;
	}

	public void setObjectExtraId(long objectExtraId) {
		this.objectExtraId = objectExtraId;
	}

	public long getObjectId() {
		return objectId;
	}

	public void setObjectId(long objectId) {
		this.objectId = objectId;
	}

}
