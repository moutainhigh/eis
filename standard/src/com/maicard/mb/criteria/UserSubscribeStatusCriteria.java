package com.maicard.mb.criteria;

import com.maicard.common.base.Criteria;

public class UserSubscribeStatusCriteria extends Criteria {

	private static final long serialVersionUID = 1L;
	
	private int userSubscribeStatusId;
	private long uuid;
	private int topicId;
	

	public UserSubscribeStatusCriteria() {
	}


	public int getTopicId() {
		return topicId;
	}

	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}

	public int getUserSubscribeStatusId() {
		return userSubscribeStatusId;
	}

	public void setUserSubscribeStatusId(int userSubscribeStatusId) {
		this.userSubscribeStatusId = userSubscribeStatusId;
	}

	public long getUuid() {
		return uuid;
	}

	public void setUuid(long uuid) {
		this.uuid = uuid;
	}
}
