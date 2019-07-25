package com.maicard.mb.domain;

import com.maicard.common.domain.EisObject;

/**
 * 用户订阅关系，指与订阅对象间的关系，而非具体的消息
 * 用户与具体订阅型消息内容的关联，由UserSubscribeMessageRelation确定
 * @author NetSnake
 * @date 2012-4-24
 */
public class UserSubscribeStatus extends EisObject{


	private static final long serialVersionUID = 4286837777646701705L;
	private int userSubscribeStatusId;
	private long uuid;
	private int topicId;
	private String[] perferMethod; //首选优先发送方式
	
	//非持久化属性
	private String subscriberName;
	private String topicName;
	
	public int getTopicId() {
		return topicId;
	}
	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}
	
	public String getSubscriberName() {
		return subscriberName;
	}
	public void setSubscriberName(String subscriberName) {
		this.subscriberName = subscriberName;
	}
	public String getTopicName() {
		return topicName;
	}
	public void setTopicName(String topicName) {
		this.topicName = topicName;
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
	public String[] getPerferMethod() {
		return perferMethod;
	}
	public void setPerferMethod(String[] perferMethod) {
		this.perferMethod = perferMethod;
	}


}
