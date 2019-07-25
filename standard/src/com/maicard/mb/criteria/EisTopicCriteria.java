package com.maicard.mb.criteria;

import com.maicard.common.base.Criteria;

public class EisTopicCriteria extends Criteria {

	private static final long serialVersionUID = 1L;
	
	private int topicId;
	private String topicCode;
	
	public EisTopicCriteria() {
	}

	public int getTopicId() {
		return topicId;
	}

	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}

	public String getTopicCode() {
		return topicCode;
	}

	public void setTopicCode(String topicCode) {
		this.topicCode = topicCode;
	}
}
