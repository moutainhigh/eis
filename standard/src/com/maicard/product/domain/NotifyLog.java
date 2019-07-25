package com.maicard.product.domain;

import java.util.Date;

import com.maicard.common.domain.EisObject;


public class NotifyLog extends EisObject implements Cloneable {

	private static final long serialVersionUID = -8274896655392899062L;
	private String transactionId;	
	private Date sendTime;
	private String response;
	
	public NotifyLog(){	
	}

	public NotifyLog(String transactionId) {
		this.transactionId = transactionId;
		this.sendTime = new Date();
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}


}
