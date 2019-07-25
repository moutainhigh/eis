package com.maicard.product.criteria;

import java.util.Date;

import com.maicard.common.base.Criteria;

public class FailedNotifyCriteria extends Criteria {

	private static final long serialVersionUID = 1L;
	
	private String transactionId;
	private Date beginTime;
	private Date endTime;
	
	private String objectType;
	

	public FailedNotifyCriteria() {
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	

}
