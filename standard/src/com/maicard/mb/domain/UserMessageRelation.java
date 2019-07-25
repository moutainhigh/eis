package com.maicard.mb.domain;

import com.maicard.common.domain.EisObject;

/**
 * 用户与订阅消息间的关联关系
 * @author NetSnake
 * @date 2012-4-23
 */
public class UserMessageRelation extends EisObject{

	private static final long serialVersionUID = 5355460815975699578L;
	private int userSubscribeMessageRelationId;
	private long uuid;
	private String messageId;

	public UserMessageRelation(){};
	
	public UserMessageRelation(String messageId, long uuid, long ownerId){
		this.messageId = messageId;
		this.uuid = uuid;
		this.ownerId = ownerId;
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
	
	public int getUserSubscribeMessageRelationId() {
		return userSubscribeMessageRelationId;
	}
	public void setUserSubscribeMessageRelationId(int userSubscribeMessageRelationId) {
		this.userSubscribeMessageRelationId = userSubscribeMessageRelationId;
	}
}
