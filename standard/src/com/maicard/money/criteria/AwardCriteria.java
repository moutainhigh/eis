package com.maicard.money.criteria;

import com.maicard.common.base.Criteria;

public class AwardCriteria extends Criteria implements Cloneable{


	private static final long serialVersionUID = -3386318167523925284L;

	private String objectType;
	
	private long objectId;
	
	private long uuid;

	public AwardCriteria() {
	}


	public AwardCriteria(long ownerId) {
		this.ownerId = ownerId;
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


	public long getUuid() {
		return uuid;
	}


	public void setUuid(long uuid) {
		this.uuid = uuid;
	}


}
