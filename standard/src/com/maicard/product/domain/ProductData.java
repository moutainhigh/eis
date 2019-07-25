package com.maicard.product.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.maicard.common.domain.DataDefine;
import com.maicard.common.domain.ExtraData;
import com.maicard.product.domain.ProductData;
import com.maicard.views.JsonFilterView;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductData extends ExtraData implements Cloneable {

	private static final long serialVersionUID = -7323973755015226023L;

	private int productDataId;

	private long productId;	

	private String dataValue;

	@JsonView({JsonFilterView.Full.class})
	private int index;

	////////////////////////////////////////////////////
	private String formattedDataValue;
	private byte[] binData;


	public ProductData() {
	}

	public ProductData(String dataCode, String dataValue){
		this.dataCode = dataCode;
		this.dataValue = dataValue;
	}
	public ProductData(int productId, String dataCode, String dataValue){
		if(productId > 0){
			this.productId = productId;
		}
		this.dataCode = dataCode;
		this.dataValue = dataValue;
	}

	public ProductData(DataDefine dataDefine) {
		this.setDataCode(dataDefine.getDataCode());
		this.setDataDefineId(dataDefine.getDataDefineId());
		this.setCompareMode(dataDefine.getCompareMode());
	}

	public int getProductDataId() {
		return productDataId;
	}

	public void setProductDataId(int productDataId) {
		this.productDataId = productDataId;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public String getDataValue() {
		return dataValue;
	}

	public void setDataValue(String dataValue) {
		if(dataValue != null)
			this.dataValue = dataValue.trim();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + productDataId;

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
		final ProductData other = (ProductData) obj;
		if (productDataId != other.productDataId)
			return false;

		return true;
	}

	@Override
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
				"(" + 
				"productDataId=" + "'" + productDataId + "'" + 
				"dataDefineId=" + "'" + dataDefineId + "'" + 
				"dataCode=" + "'" + dataCode + "'" + 
				"dataValue=" + "'" + dataValue + "'" + 
				")";
	}

	public String getFormattedDataValue() {
		return formattedDataValue;
	}

	public void setFormattedDataValue(String formattedDataValue) {
		this.formattedDataValue = formattedDataValue;
	}

	@Override
	public ProductData clone() {
		try{
			return (ProductData)super.clone();
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public byte[] getBinData() {
		return binData;
	}

	public void setBinData(byte[] binData) {
		this.binData = binData;
	}

}
