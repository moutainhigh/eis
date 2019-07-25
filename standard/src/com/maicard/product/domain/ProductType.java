package com.maicard.product.domain;

import java.util.Map;

import com.maicard.common.domain.DataDefine;
import com.maicard.common.domain.EisObject;
import com.maicard.product.domain.ProductType;

//import org.codehaus.jackson.annotate.JsonIgnoreProperties;

public class ProductType extends EisObject {

	private static final long serialVersionUID = 1L;

	private int productTypeId;

	private String productTypeName;

	//////////////////////////////////////////
	private Map<String, DataDefine> dataDefineMap;
	

	public ProductType() {
	}

	public int getProductTypeId() {
		return productTypeId;
	}

	public void setProductTypeId(int productTypeId) {
		this.productTypeId = productTypeId;
	}

	public String getProductTypeName() {
		return productTypeName;
	}

	public void setProductTypeName(String productTypeName) {
		this.productTypeName = productTypeName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + productTypeId;

		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ProductType other = (ProductType) obj;
		if (productTypeId != other.productTypeId)
			return false;

		return true;
	}
	
	@Override
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
			"(" + 
			"productTypeId=" + "'" + productTypeId + "'" + 
			")";
	}

	public Map<String, DataDefine> getDataDefineMap() {
		return dataDefineMap;
	}

	public void setDataDefineMap(Map<String, DataDefine> dataDefineMap) {
		this.dataDefineMap = dataDefineMap;
	}

}
