package com.maicard.product.criteria;

import java.util.List;

import com.maicard.common.base.Criteria;
import com.maicard.product.domain.ProductData;

public class ProductDataCriteria extends Criteria {

	private static final long serialVersionUID = 1L;
	
	private int productDataId;
	private int[] productDataIds;
	private long productId;
	private int[] productIds;
	private long dataDefineId;
	private String dataCode;
	
	private String transactionId;
	
	private String beginTime;
	private String endTime;
	private List<ProductData> queryCondition;
	private int queryConditonSize;

	public ProductDataCriteria() {
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

	public long getDataDefineId() {
		return dataDefineId;
	}

	public void setDataDefineId(long dataDefineId) {
		this.dataDefineId = dataDefineId;
	}

	public String getDataCode() {
		return dataCode;
	}

	public void setDataCode(String dataCode) {
		this.dataCode = dataCode;
	}

	public int[] getProductIds() {
		return productIds;
	}

	public void setProductIds(int[] productIds) {
		this.productIds = productIds;
	}

	public int[] getProductDataIds() {
		return productDataIds;
	}

	public void setProductDataIds(int[] productDataIds) {
		this.productDataIds = productDataIds;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public List<ProductData> getQueryCondition() {
		return queryCondition;
	}

	public void setQueryCondition(List<ProductData> queryCondition) {
		this.queryCondition = queryCondition;
	}

	public int getQueryConditonSize() {
		return queryConditonSize;
	}

	public void setQueryConditonSize(int queryConditonSize) {
		this.queryConditonSize = queryConditonSize;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

}
