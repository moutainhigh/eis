package com.maicard.site.criteria;

import com.maicard.common.base.Criteria;

public class TagCriteria extends Criteria {

	private static final long serialVersionUID = 1L;

	private int tagId;
	private String tagName;


	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		if(objectType == null || objectType.trim().equals("")){
			return;
		}
		this.objectType = objectType;
	}

	public int getObjectId() {
		return objectId;
	}

	public void setObjectId(int objectId) {
		this.objectId = objectId;
	}

	private String objectType;
	private int objectId;

	public TagCriteria() {
	}

	public TagCriteria(long ownerId) {
		this.ownerId = ownerId;
	}

	public int getTagId() {
		return tagId;
	}

	public void setTagId(int tagId) {
		this.tagId = tagId;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		if(tagName == null || tagName.trim().equals("")){
			return;
		}
		this.tagName = tagName;
	}

}
