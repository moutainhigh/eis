package com.maicard.product.criteria;

import com.maicard.common.base.Criteria;

public class PayTypeProductRelationCriteria extends Criteria {

	private static final long serialVersionUID = 1L;
	
	private int payTypeProductRelationId;
	private int payTypeId;

	public PayTypeProductRelationCriteria() {
	}

	public int getPayTypeProductRelationId() {
		return payTypeProductRelationId;
	}

	public void setPayTypeProductRelationId(int payTypeProductRelationId) {
		this.payTypeProductRelationId = payTypeProductRelationId;
	}

	public int getPayTypeId() {
		return payTypeId;
	}

	public void setPayTypeId(int payTypeId) {
		this.payTypeId = payTypeId;
	}

}
