package com.maicard.mb.domain;


import com.maicard.common.domain.EisObject;

/**
 * 订阅型主题
 * @author NetSnake
 * @date 2012-4-24
 */
public class EisTopic extends EisObject{

	private static final long serialVersionUID = -4162384609441851977L;
	
	private int topicId;
	private String topicCode;
	private String topicName;
	private String topicDescription;
	
	
	private String statusName;
	
	public int getTopicId() {
		return topicId;
	}
	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}
	public String getTopicName() {
		return topicName;
	}
	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}
	
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public String getTopicCode() {
		return topicCode;
	}
	public void setTopicCode(String topicCode) {
		this.topicCode = topicCode;
	}
	public String getTopicDescription() {
		return topicDescription;
	}
	public void setTopicDescription(String topicDescription) {
		this.topicDescription = topicDescription;
	}
	

}
