package com.maicard.money.criteria;

import com.maicard.common.base.Criteria;

public class PayMethodCriteria extends Criteria {

	private static final long serialVersionUID = 1L;
	
	private int payMethodId;
	
	private int payTypeId;
	
	private String contextType;
	
	/**
	 * 对应的系统虚拟账户
	 */
	private long referUuid;
	
	public PayMethodCriteria() {
	}

	public PayMethodCriteria(long ownerId) {
		this.ownerId = ownerId;
	}

	public int getPayMethodId() {
		return payMethodId;
	}

	public void setPayMethodId(int payMethodId) {
		this.payMethodId = payMethodId;
	}

	public int getPayTypeId() {
		return payTypeId;
	}

	public void setPayTypeId(int payTypeId) {
		this.payTypeId = payTypeId;
	}

	public String getContextType() {
		return contextType;
	}

	public void setContextType(String contextType) {
		this.contextType = contextType;
	}

	public long getReferUuid() {
		return referUuid;
	}

	public void setReferUuid(long referUuid) {
		this.referUuid = referUuid;
	}
}
