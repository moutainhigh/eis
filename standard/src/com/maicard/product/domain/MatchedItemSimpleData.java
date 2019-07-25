package com.maicard.product.domain;

import java.util.Date;

public class MatchedItemSimpleData {
	
	private long uuid;
	private String sourceTransactionId;
	private String destTransactionId;
	private Date processTime;
	
	public String getSourceTransactionId() {
		return sourceTransactionId;
	}
	public void setSourceTransactionId(String sourceTransactionId) {
		this.sourceTransactionId = sourceTransactionId;
	}
	public String getDestTransactionId() {
		return destTransactionId;
	}
	public void setDestTransactionId(String destTransactionId) {
		this.destTransactionId = destTransactionId;
	}
	public Date getProcessTime() {
		return processTime;
	}
	public void setProcessTime(Date processTime) {
		this.processTime = processTime;
	}
	public long getUuid() {
		return uuid;
	}
	public void setUuid(long uuid) {
		this.uuid = uuid;
	}
	

}
