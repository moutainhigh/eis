package com.maicard.site.criteria;

import java.util.Arrays;

import com.maicard.common.base.Criteria;

public class TagObjectRelationCriteria extends Criteria {

	private static final long serialVersionUID = 1L;

	private long tagObjectRelationId;
	private String objectType;
	private long objectId;
	private long[] tagIds;



	public TagObjectRelationCriteria() {
	}



	public TagObjectRelationCriteria(long ownerId) {
		this.ownerId = ownerId;
	}

	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(getClass().getName()).append('@').append(Integer.toHexString(hashCode())).append('(');
		sb.append("objectType=").append("'").append(objectType).append("',");
		sb.append("objectId=").append("'").append(objectId).append("'");
		if(tagIds != null && tagIds.length > 0){
			sb.append(",").append(Arrays.toString(tagIds));
		}
		return sb.toString();
	}


	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}



	public long[] getTagIds() {
		return tagIds;
	}

	public void setTagIds(long... tagId) {
		this.tagIds = tagId;
	}



	public long getTagObjectRelationId() {
		return tagObjectRelationId;
	}



	public void setTagObjectRelationId(long tagObjectRelationId) {
		this.tagObjectRelationId = tagObjectRelationId;
	}



	public long getObjectId() {
		return objectId;
	}



	public void setObjectId(long objectId) {
		this.objectId = objectId;
	}

}
