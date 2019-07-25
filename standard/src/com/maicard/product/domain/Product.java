package com.maicard.product.domain;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.maicard.annotation.DisplayColumn;
import com.maicard.annotation.InputLevel;
import com.maicard.common.domain.OpEisObject;
import com.maicard.common.domain.Region;
import com.maicard.common.util.NumericUtils;
import com.maicard.money.domain.Price;
import com.maicard.product.domain.Product;
import com.maicard.product.domain.ProductData;
import com.maicard.views.JsonFilterView;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Product extends OpEisObject {

	private static final long serialVersionUID = 1L;

	@InputLevel(JsonFilterView.Partner.class)
	private int productId;

	@InputLevel(JsonFilterView.Partner.class)
	@DisplayColumn
	private String productCode;

	@InputLevel(JsonFilterView.Partner.class)
	@DisplayColumn
	private String productName;

	@InputLevel(JsonFilterView.Partner.class)
	private String content;
	
	private int parentProductId  = 0;

	@InputLevel(JsonFilterView.Partner.class)
	@DisplayColumn("ProductTypeId")
	private int productTypeId = 0;

	private String alias;

	@JsonView({JsonFilterView.Full.class})
	private int displayTypeId = 0;

	@JsonView({JsonFilterView.Full.class})
	private int displayIndex = 0;

	private int alwaysOnTop = 0;

	@InputLevel(JsonFilterView.Partner.class)
	@DisplayColumn
	private float labelMoney;	//标价、面额

	private float buyMoney;	//销售价

	private String rate;
	
	@JsonView({JsonFilterView.Full.class})
	private int extraStatus = 0;	
	
	@DisplayColumn
	@InputLevel(JsonFilterView.Partner.class)
	private int initCount;		//产品初始数量

	@DisplayColumn
	private int availableCount = 0;		//产品剩余数量

	@DisplayColumn
	private int soldCount = 0;		//产品剩余数量

	@InputLevel(JsonFilterView.Partner.class)
	@DisplayColumn
	private Date createTime;

	private Date validTime;

	@InputLevel(JsonFilterView.Partner.class)
	@DisplayColumn
	private Date publishTime;

	@JsonView({JsonFilterView.Full.class})
	@InputLevel(JsonFilterView.Partner.class)
	@DisplayColumn
	private int transactionTtl = 0;

	@JsonView({JsonFilterView.Full.class})
	@InputLevel(JsonFilterView.Partner.class)
	@DisplayColumn
	private int supplyPartnerId = 0;

	@JsonView({JsonFilterView.Full.class})
	private Date lastModified;	

	@JsonView({JsonFilterView.Full.class})
	private String processClass;

	@JsonView({JsonFilterView.Full.class})
	@InputLevel(JsonFilterView.Partner.class)
	@DisplayColumn
	private int maxRetry;	//处理商品时的最多重复次数

	////////////////////////////////////////////////////////////////
	private String extraStatusName;

	private HashMap<String, ProductData> productDataMap;

	private String productTypeName;
	
	@JsonView({JsonFilterView.Partner.class})
	private String supplyPartnerName;

	private String tags;

	private HashMap<String, Region> regionMap;

	private String transactionToken;

	private Price price;



	public Product() {
	}

	public Product(long ownerId) {
		this.ownerId = ownerId;
	}
	

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName == null ? null : productName.trim();
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content == null ? null : content.trim();
	}

	public int getParentProductId() {
		return parentProductId;
	}

	public void setParentProductId(int parentProductId) {
		this.parentProductId = parentProductId;
	}

	public int getProductTypeId() {
		return productTypeId;
	}

	public void setProductTypeId(int productTypeId) {
		this.productTypeId = productTypeId;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias == null ? null : alias.trim();
	}

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

	public int getAlwaysOnTop() {
		return alwaysOnTop;
	}

	public void setAlwaysOnTop(int alwaysOnTop) {
		this.alwaysOnTop = alwaysOnTop;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public int getExtraStatus() {
		return extraStatus;
	}

	public void setExtraStatus(int extraStatus) {
		this.extraStatus = extraStatus;
	}

	public int getAvailableCount() {
		return availableCount;
	}

	public void setAvailableCount(int availableCount) {
		this.availableCount = availableCount;
	}

	public int getSoldCount() {
		if(this.soldCount < 1){
			return this.initCount - this.availableCount;
		}
		return this.soldCount;
	}

	public void setSoldCount(int soldCount) {
		this.soldCount = soldCount;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getValidTime() {
		return validTime;
	}

	public void setValidTime(Date validTime) {
		this.validTime = validTime;
	}

	public Date getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + productId;

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
		final Product other = (Product) obj;
		if (productId != other.productId)
			return false;

		return true;
	}

	public int getTransactionTtl() {
		return transactionTtl;
	}

	public void setTransactionTtl(int transactionTtl) {
		this.transactionTtl = transactionTtl;
	}

	public String getExtraStatusName() {
		return extraStatusName;
	}

	public void setExtraStatusName(String extraStatusName) {
		this.extraStatusName = extraStatusName;
	}

	public String getProductTypeName() {
		return productTypeName;
	}

	public void setProductTypeName(String productTypeName) {
		this.productTypeName = productTypeName;
	}

	public HashMap<String, ProductData> getProductDataMap() {
		return productDataMap;
	}

	public void setProductDataMap(HashMap<String, ProductData> productDataMap) {
		this.productDataMap = productDataMap;
	}

	public int getSupplyPartnerId() {
		return supplyPartnerId;
	}

	public void setSupplyPartnerId(int supplyPartnerId) {
		this.supplyPartnerId = supplyPartnerId;
	}

	public String getSupplyPartnerName() {
		return supplyPartnerName;
	}

	public void setSupplyPartnerName(String supplyPartnerName) {
		this.supplyPartnerName = supplyPartnerName;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public String getProcessClass() {
		return processClass;
	}

	public void setProcessClass(String processClass) {
		this.processClass = processClass;
	}

	public int getMaxRetry() {
		return maxRetry;
	}

	public void setMaxRetry(int maxRetry) {
		this.maxRetry = maxRetry;
	}

	public float getLabelMoney() {
		return labelMoney;
	}

	public void setLabelMoney(float labelMoney) {
		this.labelMoney = labelMoney;
	}

	public float getBuyMoney() {
		return buyMoney;
	}

	public void setBuyMoney(float buyMoney) {
		this.buyMoney = buyMoney;
	}
	@Override
	public Product clone() {
		try{
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(byteOut);
			out.writeObject(this);

			ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
			ObjectInputStream in =new ObjectInputStream(byteIn);

			return (Product)in.readObject();

		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public HashMap<String, Region> getRegionMap() {
		return regionMap;
	}

	public void setRegionMap(HashMap<String, Region> regionMap) {
		this.regionMap = regionMap;
	}

	public Price getPrice() {
		return price;
	}

	public void setPrice(Price price) {
		this.price = price;
	}

	public String getTransactionToken() {
		return transactionToken;
	}

	public void setTransactionToken(String transactionToken) {
		this.transactionToken = transactionToken;
	}
	
	@Override
	public void setExtraValue(String dataCode, String dataValue) {
		if(this.productDataMap == null){
			this.productDataMap = new HashMap<String, ProductData>();
		}
		if(StringUtils.isBlank(dataCode) || StringUtils.isBlank(dataValue)){
			return;
		}
		this.productDataMap.put(dataCode, new ProductData(dataCode,dataValue));
	}


	@Override
	public String getExtraValue(String dataCode) {
		if(this.productDataMap == null){
			return null;
		}
		if(!this.productDataMap.containsKey(dataCode)){
			return null;
		}
		if(this.productDataMap.get(dataCode).getDataValue() != null){
			return this.productDataMap.get(dataCode).getDataValue().trim();
		}
		return null;
	}

	@Override
	public boolean getBooleanExtraValue(String dataCode) {
		if(this.productDataMap == null){
			return false;
		}
		if(!this.productDataMap.containsKey(dataCode)){
			return false;
		}
		if(this.productDataMap.get(dataCode).getDataValue() != null && this.productDataMap.get(dataCode).getDataValue().trim().equalsIgnoreCase("true")){
			return true;
		}
		return false;
	}



	@Override
	public long getLongExtraValue(String dataCode) {
		if(this.productDataMap == null){
			return 0;
		}
		if(!this.productDataMap.containsKey(dataCode)){
			return 0;
		}
		if(this.productDataMap.get(dataCode).getDataValue() != null && NumericUtils.isNumeric(productDataMap.get(dataCode).getDataValue())){
			return Long.parseLong(productDataMap.get(dataCode).getDataValue().trim());
		}
		return 0;
	}
	
	@Override
	public float getFloatExtraValue(String dataCode) {
		if(this.productDataMap == null){
			return 0;
		}
		if(!this.productDataMap.containsKey(dataCode)){
			return 0;
		}
		if(this.productDataMap.get(dataCode).getDataValue() != null && NumericUtils.isNumeric(productDataMap.get(dataCode).getDataValue())){
			return Float.parseFloat(productDataMap.get(dataCode).getDataValue().trim());
		}
		return 0;
	}

	public int getInitCount() {
		return initCount;
	}

	public void setInitCount(int initCount) {
		this.initCount = initCount;
	}


}
