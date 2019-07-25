package com.maicard.product.criteria;

import com.maicard.common.base.Criteria;

public class ProductTypeCriteria extends Criteria {

	private static final long serialVersionUID = 1L;

	private int productTypeId;


	public ProductTypeCriteria() {
	}

	public ProductTypeCriteria(long ownerId) {
		this.ownerId = ownerId;
	}

	public int getProductTypeId() {
		return productTypeId;
	}

	public void setProductTypeId(int productTypeId) {
		this.productTypeId = productTypeId;
	}
}
