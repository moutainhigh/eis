package com.maicard.common.criteria;


import com.maicard.common.base.Criteria;



public class UserUniqueCriteria extends Criteria implements Cloneable{

	private static final long serialVersionUID = 806842049807807304L;
	private long uuid;

	public UserUniqueCriteria() {
	}

	public UserUniqueCriteria(long uuid, long ownerId) {
		this.uuid = uuid;
		this.ownerId = ownerId;
	}

	public long getUuid() {
		return uuid;
	}

	public void setUuid(long uuid) {
		this.uuid = uuid;
	}


}


