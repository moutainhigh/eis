package com.maicard.product.criteria;

import com.maicard.common.base.Criteria;

public class ProductPartnerRelationCriteria extends Criteria {

	private static final long serialVersionUID = 1L;
	
	private long productId;
	
	private long partnerId;

	public ProductPartnerRelationCriteria() {
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public long getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(long partnerId) {
		this.partnerId = partnerId;
	}

}
