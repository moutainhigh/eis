package com.maicard.product.domain;

import java.util.Date;

import com.maicard.common.domain.EisObject;


public class FailedNotify extends EisObject implements Cloneable {

	private static final long serialVersionUID = -2717838536032122134L;
	private String transactionId;	
	private Date firstSendTime;
	private Date lastSendTime;
	private int totalSendCount;
	
	//objectType继承自父类
	
	public FailedNotify(){	
	}


	public FailedNotify(String transactionId) {
		this.transactionId = transactionId;
		this.firstSendTime = new Date();
		this.lastSendTime = new Date();
		this.totalSendCount = 1;
	}


	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}


	public Date getFirstSendTime() {
		return firstSendTime;
	}


	public void setFirstSendTime(Date firstSendTime) {
		this.firstSendTime = firstSendTime;
	}


	public Date getLastSendTime() {
		return lastSendTime;
	}


	public void setLastSendTime(Date lastSendTime) {
		this.lastSendTime = lastSendTime;
	}


	public int getTotalSendCount() {
		return totalSendCount;
	}


	public void setTotalSendCount(int totalSendCount) {
		this.totalSendCount = totalSendCount;
	}




}
