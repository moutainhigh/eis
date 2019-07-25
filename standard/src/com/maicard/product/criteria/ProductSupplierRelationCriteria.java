package com.maicard.product.criteria;

import com.maicard.common.base.Criteria;

public class ProductSupplierRelationCriteria extends Criteria {

	private static final long serialVersionUID = 1L;
	
	private int productSupplierRelationId;
	private int productId;
	private int supplierId;

	public ProductSupplierRelationCriteria() {
	}

	public int getProductSupplierRelationId() {
		return productSupplierRelationId;
	}

	public void setProductSupplierRelationId(int productSupplierRelationId) {
		this.productSupplierRelationId = productSupplierRelationId;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}

}
