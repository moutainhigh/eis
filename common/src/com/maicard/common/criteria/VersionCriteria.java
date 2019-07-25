package com.maicard.common.criteria;

import java.util.Date;

import com.maicard.common.base.Criteria;

public class VersionCriteria extends Criteria {
	

	private static final long serialVersionUID = 5206153266799675299L;


	/**
	 * 特定的渠道ID
	 */
	private long partnerId;

	
	/**
	 * 发布时间
	 */
	private Date publishTime;


	public long getPartnerId() {
		return partnerId;
	}


	public void setPartnerId(long partnerId) {
		this.partnerId = partnerId;
	}


	public Date getPublishTime() {
		return publishTime;
	}


	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}
	


}
