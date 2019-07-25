package com.maicard.site.criteria;

import com.maicard.common.base.Criteria;

public class StaticizeCriteria extends Criteria {

	private static final long serialVersionUID = 1L;

	private String objectType;

	private long objectId;

	public StaticizeCriteria() {
	}

	public StaticizeCriteria(long ownerId) {
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
}
