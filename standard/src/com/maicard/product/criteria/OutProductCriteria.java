package com.maicard.product.criteria;

import com.maicard.common.base.Criteria;

public class OutProductCriteria extends Criteria {


	private static final long serialVersionUID = 5847754876793399145L;
	
	/**
	 * 我方内部产品ID
	 */
	private long internalProductId;

	/**
	 * 外部产品代码
	 */
	private String outProductCode;
	
	public OutProductCriteria(){}
	
	public OutProductCriteria(long ownerId){
		this.ownerId = ownerId;
	}

	public long getInternalProductId() {
		return internalProductId;
	}

	public void setInternalProductId(long internalProductId) {
		this.internalProductId = internalProductId;
	}

	public String getOutProductCode() {
		return outProductCode;
	}

	public void setOutProductCode(String outProductCode) {
		this.outProductCode = outProductCode;
	}
}
