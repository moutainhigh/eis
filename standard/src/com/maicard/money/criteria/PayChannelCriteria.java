package com.maicard.money.criteria;

import com.maicard.common.base.Criteria;

public class PayChannelCriteria extends Criteria {

	private static final long serialVersionUID = 1L;
	
	private int payChannelId;

	public PayChannelCriteria() {
	}

	public int getPayChannelId() {
		return payChannelId;
	}

	public void setPayChannelId(int payChannelId) {
		this.payChannelId = payChannelId;
	}

}
