package com.maicard.money.criteria;

import com.maicard.common.base.Criteria;

public class PayTypeCriteria extends Criteria {

	private static final long serialVersionUID = 1L;

	private int payTypeId;
	
	private int flag;

	public PayTypeCriteria() {
	}

	public PayTypeCriteria(long ownerId) {
		this.ownerId = ownerId;
	}

	public int getPayTypeId() {
		return payTypeId;
	}

	public void setPayTypeId(int payTypeId) {
		this.payTypeId = payTypeId;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}
}
