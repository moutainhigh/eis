package com.maicard.product.criteria;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.maicard.common.base.Criteria;
import com.maicard.common.util.JsonUtils;

public class CartCriteria extends Criteria {

	private static final long serialVersionUID = 1L;
	
	public static final String ORDER_TYPE_TEMP = "TEMP";
	public static final String ORDER_TYPE_STORE = "STORE";
	
	public static final String BUY_TYPE_NORMAL = "NORMAL";
	public static final String BUY_TYPE_DIRIECT = "DIRECT_BUY";
	
	

	private long[] cartId;
	private long uuid;
	private String identify;
	private String orderType;						//订单类型,见常量
	private String priceType;				//订单的价格类型，不同价格类型不能作为同一个订单
	private String buyType;
	
	private Date createTimeBegin;
	private Date createTimeEnd;
	
	public static final String TIMEOUT_ONLY="TIMEOUT_ONLY"; //超时
	public static final String TIMEIN_ONLY="TIMEIN_ONLY";	//未超时
	private String timeoutPolicy; 

	
	private int requireInvoice;		//0:所有 1:需要发票 2:不需要发票

	private long[] productIds;
	
	private Map<String,String> queryExtraMap;
	
	private long[] inviters;
	
	private int[] excludeStatus;

	

	public CartCriteria() {
	}

	public CartCriteria(long ownerId) {
		this.ownerId = ownerId;
	}

	@Override
	public String toString() {
		try {
			return new StringBuffer().append(getClass().getName()).append('@').append(Integer.toHexString(hashCode())).append('(').append(JsonUtils.getNoDefaultValueInstance().writeValueAsString(this)).append(')').toString();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}
	public long[] getCartId() {
		return cartId;
	}

	public void setCartId(long... cartId) {
		this.cartId = cartId;
	}

	public long getUuid() {
		return uuid;
	}

	public void setUuid(long uuid) {
		this.uuid = uuid;
	}

	public String getIdentify() {
		return identify;
	}

	public void setIdentify(String identify) {
		this.identify = identify;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getPriceType() {
		return priceType;
	}

	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}

	public Map<String, String> getQueryExtraMap() {
		if(queryExtraMap == null){
			queryExtraMap = new HashMap<String,String>();
		}
		return queryExtraMap;
	}

	public void setQueryExtraMap(Map<String, String> queryExtraMap) {
		this.queryExtraMap = queryExtraMap;
	}

	public Date getCreateTimeBegin() {
		return createTimeBegin;
	}

	public void setCreateTimeBegin(Date createTimeBegin) {
		this.createTimeBegin = createTimeBegin;
	}

	public Date getCreateTimeEnd() {
		return createTimeEnd;
	}

	public void setCreateTimeEnd(Date createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}

	public int getRequireInvoice() {
		return requireInvoice;
	}

	public void setRequireInvoice(int requireInvoice) {
		this.requireInvoice = requireInvoice;
	}

	public long[] getProductIds() {
		return productIds;
	}

	public void setProductIds(long... productIds) {
		this.productIds = productIds;
	}

	public String getTimeoutPolicy() {
		return timeoutPolicy;
	}

	public void setTimeoutPolicy(String timeoutPolicy) {
		this.timeoutPolicy = timeoutPolicy;
	}

	public long[] getInviters() {
		return inviters;
	}

	public void setInviters(long... inviters) {
		this.inviters = inviters;
	}

	public String getBuyType() {
		return buyType;
	}

	public void setBuyType(String buyType) {
		this.buyType = buyType;
	}

	public int[] getExcludeStatus() {
		return excludeStatus;
	}

	public void setExcludeStatus(int... excludeStatus) {
		this.excludeStatus = excludeStatus;
	}

}
