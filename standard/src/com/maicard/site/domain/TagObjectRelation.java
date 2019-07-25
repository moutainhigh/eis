package com.maicard.site.domain;

import java.util.Date;

import com.maicard.common.domain.EisObject;

public class TagObjectRelation extends EisObject {

	private static final long serialVersionUID = 9011122198767839685L;

	private long tagObjectRelationId;

	private long tagId;

	private String objectType;

	private long objectId;

	private Date createTime;


	private int extraStatus;

	public TagObjectRelation() {
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (int)(prime * result + tagObjectRelationId);

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
		final TagObjectRelation other = (TagObjectRelation) obj;
		if (tagObjectRelationId != other.tagObjectRelationId)
			return false;

		return true;
	}
	
	@Override
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
			"(" + 
			"tagObjectRelationId=" + "'" + tagObjectRelationId + "'" + 
			",tagId=" + "'" + tagId + "'" + 
			",objectType=" + "'" + objectType + "'" + 
			",objectId=" + "'" + objectId + "'" + 
			",ownerId=" + "'" + ownerId + "'" + 
			")";
	}


	public long getTagObjectRelationId() {
		return tagObjectRelationId;
	}


	public void setTagObjectRelationId(long tagObjectRelationId) {
		this.tagObjectRelationId = tagObjectRelationId;
	}


	public long getTagId() {
		return tagId;
	}


	public void setTagId(long tagId) {
		this.tagId = tagId;
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


	public Date getCreateTime() {
		return createTime;
	}


	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}


	public int getExtraStatus() {
		return extraStatus;
	}


	public void setExtraStatus(int extraStatus) {
		this.extraStatus = extraStatus;
	}
	
}
