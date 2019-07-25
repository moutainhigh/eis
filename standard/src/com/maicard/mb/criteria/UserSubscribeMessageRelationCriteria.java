package com.maicard.mb.criteria;

import com.maicard.common.base.Criteria;

public class UserSubscribeMessageRelationCriteria extends Criteria {

	private static final long serialVersionUID = 1L;
	

	private int userSubscribeMessageRelationId;
	private long uuid;
	private String messageId;
	
	public int getUserSubscribeMessageRelationId() {
		return userSubscribeMessageRelationId;
	}
	public void setUserSubscribeMessageRelationId(int userSubscribeMessageRelationId) {
		this.userSubscribeMessageRelationId = userSubscribeMessageRelationId;
	}
	public long getUuid() {
		return uuid;
	}
	public void setUuid(long uuid) {
		this.uuid = uuid;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

}
