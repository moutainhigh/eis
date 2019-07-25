package com.maicard.product.criteria;

import com.maicard.common.base.Criteria;

public class ProductGroupCriteria extends Criteria {

	private static final long serialVersionUID = 1L;
	
	
	
	private String objectType;
	
	private long objectId;
	
	private int level;
	
	private String groupValue;
	
	private long parentId;



	public ProductGroupCriteria() {
	}



	public ProductGroupCriteria(long ownerId) {
		this.ownerId = ownerId;
	}


	public ProductGroupCriteria(String objectType, long objectId) {
		this.objectType = objectType;
		this.objectId = objectId;
	}



	public String getObjectType() {
		return objectType;
	}



	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}



	public long getObjectId() {
		return objectId;
	}



	public void setObjectId(long objectId) {
		this.objectId = objectId;
	}



	public int getLevel() {
		return level;
	}



	public void setLevel(int level) {
		this.level = level;
	}



	public String getGroupValue() {
		return groupValue;
	}



	public void setGroupValue(String groupValue) {
		this.groupValue = groupValue;
	}



	public long getParentId() {
		return parentId;
	}



	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	
}
