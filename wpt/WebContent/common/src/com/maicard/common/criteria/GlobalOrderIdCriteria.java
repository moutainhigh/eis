package com.maicard.common.criteria;

import com.maicard.common.base.Criteria;

public class GlobalOrderIdCriteria extends Criteria {

	private static final long serialVersionUID = 1L;
	
	private String orderId;

	public GlobalOrderIdCriteria() {
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

}
