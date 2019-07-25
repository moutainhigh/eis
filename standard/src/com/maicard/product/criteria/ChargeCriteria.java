package com.maicard.product.criteria;

import com.maicard.common.base.Criteria;

public class ChargeCriteria extends Criteria {

	private static final long serialVersionUID = 1L;
	
	private String chargeOrderId;

	public ChargeCriteria() {
	}

	public String getChargeOrderId() {
		return chargeOrderId;
	}

	public void setChargeOrderId(String chargeOrderId) {
		this.chargeOrderId = chargeOrderId;
	}

}
