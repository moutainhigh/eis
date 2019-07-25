package com.maicard.money.criteria;

import java.util.Date;

import com.maicard.common.base.Criteria;

public class RefundCriteria extends Criteria {

	private static final long serialVersionUID = 1L;

	private long uuid;

	private String transactionId;
	
	private String refPayTransactionId;
	

	
	private Date startTimeBegin;

	private Date startTimeEnd;

	
	
	private String inOrderId;

	private String outOrderId;

	public long getUuid() {
		return uuid;
	}

	public void setUuid(long uuid) {
		this.uuid = uuid;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getRefPayTransactionId() {
		return refPayTransactionId;
	}

	public void setRefPayTransactionId(String refPayTransactionId) {
		this.refPayTransactionId = refPayTransactionId;
	}

	public Date getStartTimeBegin() {
		return startTimeBegin;
	}

	public void setStartTimeBegin(Date startTimeBegin) {
		this.startTimeBegin = startTimeBegin;
	}

	public Date getStartTimeEnd() {
		return startTimeEnd;
	}

	public void setStartTimeEnd(Date startTimeEnd) {
		this.startTimeEnd = startTimeEnd;
	}

	public String getInOrderId() {
		return inOrderId;
	}

	public void setInOrderId(String inOrderId) {
		this.inOrderId = inOrderId;
	}

	public String getOutOrderId() {
		return outOrderId;
	}

	public void setOutOrderId(String outOrderId) {
		this.outOrderId = outOrderId;
	}

}
