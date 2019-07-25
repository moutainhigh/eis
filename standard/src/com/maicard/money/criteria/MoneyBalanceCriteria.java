package com.maicard.money.criteria;

import java.util.Date;

import com.maicard.common.base.InviterSupportCriteria;

public class MoneyBalanceCriteria extends InviterSupportCriteria {

	private static final long serialVersionUID = 5205578313365222084L;

	private long uuid;	

	private Date createTimeBegin;
	
	private Date createTimeEnd;
	
	
	
	public MoneyBalanceCriteria(){}
	
	public MoneyBalanceCriteria(long ownerId){
		this.ownerId = ownerId;
	}
	
	
	public long getUuid() {
		return uuid;
	}

	public void setUuid(long uuid) {
		this.uuid = uuid;
	}

	public Date getCreateTimeBegin() {
		return createTimeBegin;
	}

	public void setCreateTimeBegin(Date createTimeBegin) {
		this.createTimeBegin = createTimeBegin;
	}

	public Date getCreateTimeEnd() {
		return createTimeEnd;
	}

	public void setCreateTimeEnd(Date createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}
	
	

	
}
