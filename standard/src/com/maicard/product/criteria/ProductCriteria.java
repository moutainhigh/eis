package com.maicard.product.criteria;

import com.maicard.common.base.Criteria;
import com.maicard.standard.CommonStandard;

public class ProductCriteria extends Criteria {

	private static final long serialVersionUID = 1L;
	
	public static final  String displayColumnKeyPrefix = "DisplayColumns#product#";


	private int[] productId;
	private String productCode;
	private int parentProductId;
	private int extraStatus;
	private float buyMoney = -1f;
	private int productTypeId;
	private int[] productTypeIds;
	private boolean mustInternalProduct;
	private boolean mustForeignProduct;
	private long supplierId = -1;
	private int nodeId;
	private int productLevel;
	private String dataFetchMode = CommonStandard.DataFetchMode.simple.toString();
	private String order;
	private String searchColumn;
	private String searchCondition;
	private String productName;
	private String fuzzyProductName;		//模糊查询的产品名
	private int displayTypeId;
	private int displayIndex;
	
	

	public int getDisplayTypeId() {
		return displayTypeId;
	}

	public void setDisplayTypeId(int displayTypeId) {
		this.displayTypeId = displayTypeId;
	}

	public int getDisplayIndex() {
		return displayIndex;
	}

	public void setDisplayIndex(int displayIndex) {
		this.displayIndex = displayIndex;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		if(productName == null ||  productName.trim().equals("")){
			return;
		}
		this.productName = productName.trim();
	}

	public ProductCriteria() {
	}

	public ProductCriteria(long ownerId) {
		this.ownerId = ownerId;
	}

	public int[] getProductId() {
		return productId;
	}

	public void setProductId(int... productId) {
		this.productId = productId;
	}

	public int getExtraStatus() {
		return extraStatus;
	}

	public void setExtraStatus(int extraStatus) {
		this.extraStatus = extraStatus;
	}

	public int getParentProductId() {
		return parentProductId;
	}

	public void setParentProductId(int parentProductId) {
		this.parentProductId = parentProductId;
	}

	public int getNodeId() {
		return nodeId;
	}

	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}

	public float getFaceMoney() {
		return buyMoney;
	}

	public void setFaceMoney(float buyMoney) {
		this.buyMoney = buyMoney;
	}

	public int getProductTypeId() {
		return productTypeId;
	}

	public void setProductTypeId(int productTypeId) {
		this.productTypeId = productTypeId;
	}

	public boolean isMustInternalProduct() {
		return mustInternalProduct;
	}

	public void setMustInternalProduct(boolean mustInternalProduct) {
		this.mustInternalProduct = mustInternalProduct;
	}

	public boolean isMustForeignProduct() {
		return mustForeignProduct;
	}

	public void setMustForeignProduct(boolean mustForeignProduct) {
		this.mustForeignProduct = mustForeignProduct;
	}

	public long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		if(productCode == null ||  productCode.trim().equals("")){
			return;
		}
		this.productCode = productCode.trim();
	}

	public int getProductLevel() {
		return productLevel;
	}

	public void setProductLevel(int productLevel) {
		this.productLevel = productLevel;
	}

	public String getDataFetchMode() {
		return dataFetchMode;
	}

	public void setDataFetchMode(String dataFetchMode) {
		this.dataFetchMode = dataFetchMode;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getSearchCondition() {
		return searchCondition;
	}

	public void setSearchCondition(String searchCondition) {
		this.searchCondition = searchCondition;
	}

	public String getSearchColumn() {
		return searchColumn;
	}

	public void setSearchColumn(String searchColumn) {
		this.searchColumn = searchColumn;
	}

	public int[] getProductTypeIds() {
		return productTypeIds;
	}

	public void setProductTypeIds(int... productTypeIds) {
		this.productTypeIds = productTypeIds;
	}

	public String getFuzzyProductName() {
		return fuzzyProductName;
	}

	public void setFuzzyProductName(String fuzzyProductName) {
		this.fuzzyProductName = fuzzyProductName;
	}

}
