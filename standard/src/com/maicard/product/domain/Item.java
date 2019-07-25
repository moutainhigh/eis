package com.maicard.product.domain;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.maicard.common.domain.EisObject;
import com.maicard.common.domain.OpEisObject;
import com.maicard.common.util.NumericUtils;
import com.maicard.method.ExtraValueAccess;
import com.maicard.money.domain.Price;
import com.maicard.product.domain.ProductData;
import com.maicard.serializer.ExtraDataMapSerializer;
import com.maicard.standard.TransactionStandard.TransactionType;
import com.maicard.views.JsonFilterView;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Item extends EisObject implements Cloneable,ExtraValueAccess{

	private static final long serialVersionUID = 4283878036285896773L;
	private long itemId = 0;

	private int transactionTypeId = 0;
	private String transactionId;	
	private String inOrderId;	//提交方的外部订单号
	private String outOrderId;	//处理方的外部订单号
	private String name;
	private String content;
	private long productId = 0;
	private long chargeFromAccount = 0;//从哪个帐号扣钱
	private long chargeToAccount = 0; //充值到哪个帐号
	
	/**
	 * 对应对象的类型ID
	 */
	private long objectTypeId;

	private float rate = 0f;
	private int count = 0;
	private float labelMoney = 0f; //当前订单的总金额
	private float requestMoney = 0f;  //当前需要处理的金额
	private float successMoney = 0f; //当前商品实际已完成的金额
	private float frozenMoney = 0f;	//冻结金额
	private float inMoney	= 0f;	//购入成本
	private float outMoney = 0f;  //售出成本
	private Date enterTime;

	@JsonView({JsonFilterView.Boss.class})
	private int ttl = 0;

	@JsonView({JsonFilterView.Boss.class})
	private int maxRetry;	//处理商品时的最多重复次数
	private Date closeTime;

	@JsonView({JsonFilterView.Boss.class})
	private int extraStatus = 0;
	private int billingStatus = 0;
	private int outStatus = 0;

	@JsonView({JsonFilterView.Boss.class})
	private String lockGlobalUniqueId;
	private long cartId = 0; 					//购物车编号，与第一个商品item_id一致


	//该交易对应的价格
	//@JsonView({JsonFilterView.Partner.class})
	private Price price;

	private long inviter;

	@JsonView({JsonFilterView.Boss.class})
	private int processCount;

	@JsonView({JsonFilterView.Boss.class})
	private long supplyPartnerId;
	/*
	 * 扩展处理规则，由5位代码组成：外部错误码,超时错误码,补单策略,是否返回真实金额，是否只处理一次
	 * 1外部错误码:若大于0，则将所有错误设置为该错误码，例如将卡号错误、余额不足等全部设置为卡密错误
	 * 2超时错误码:当订单超时后又不进行补单或补单失败时该订单的状态，如成功710010或失败710011，
	 * 3补单策略:是否需要补单，@see PaddingPolicy
	 * 4是否返回真实金额：0返回真实金额，1当真实金额大于面额时仅返回面额
	 * 5是否只处理一次：0多次处理直到处理结束；1:只处理一次直接返回未处理
	 */
	@JsonView({JsonFilterView.Boss.class})
	private Map<String,String> config;	

	@JsonView({JsonFilterView.Boss.class})
	private int weight;	//权重，数字越大处理优先级越高

	@JsonView({JsonFilterView.Boss.class})
	private int shareConfigId;

	///////////////////////////////////////////////////
	@JsonSerialize(using = ExtraDataMapSerializer.class) 
	private Map<String, ProductData> itemDataMap;

	@JsonView({JsonFilterView.Boss.class})
	private int lockStatus;

	private String chargeFromAccountName;

	@JsonView({JsonFilterView.Boss.class})
	private int beforeLockStatus;

	@JsonView({JsonFilterView.Boss.class})
	private int afterLockStatus;

	@JsonView({JsonFilterView.Boss.class})
	private long lastEffect;		//最后一次影响业务操作的时间


	public Item(){


	}

	public Item(Product product) {
		this.ttl = product.getTransactionTtl();
		this.productId = product.getProductId();	
		this.labelMoney = product.getLabelMoney();
		this.maxRetry = product.getMaxRetry();
		this.requestMoney = product.getLabelMoney();
		this.ownerId = product.getOwnerId();
		this.setObjectType(product.getObjectType());
	}

	public Item(long ownerId) {
		this.ownerId = ownerId;
	}

	public Item(OpEisObject product) {
		
		this.productId = (int)product.getId();	
		//this.labelMoney = product.getLabelMoney();
		//this.maxRetry = product.getMaxRetry();
		//this.requestMoney = product.getLabelMoney();
		this.ownerId = product.getOwnerId();
		this.setTransactionTypeId(TransactionType.buy.getId());
		this.setObjectType(product.getObjectType());
		this.setName(product.getName());
	}

	public long getItemId() {
		return itemId;
	}

	public void setItemId(long itemId) {
		this.itemId = itemId;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId == null ? null : transactionId.trim();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content == null ? null : content.trim();
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}



	public long getChargeFromAccount() {
		return chargeFromAccount;
	}

	public void setChargeFromAccount(long chargeFromAccount) {
		this.chargeFromAccount = chargeFromAccount;
	}

	public long getChargeToAccount() {
		return chargeToAccount;
	}

	public void setChargeToAccount(long chargeToAccount) {
		this.chargeToAccount = chargeToAccount;
	}


	public Date getEnterTime() {
		return enterTime;
	}

	public void setEnterTime(Date enterTime) {
		this.enterTime = enterTime;
	}

	public int getTtl() {
		return ttl;
	}

	public void setTtl(int ttl) {
		this.ttl = ttl;
	}

	public Date getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(Date closeTime) {
		this.closeTime = closeTime;
	}

	public Map<String, ProductData> getItemDataMap() {
		return itemDataMap;
	}

	public void setItemDataMap(Map<String, ProductData> itemDataMap) {
		this.itemDataMap = itemDataMap;
	}

	public float getRate() {
		return rate;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}

	public int getExtraStatus() {
		return extraStatus;
	}

	public void setExtraStatus(int extraStatus) {
		this.extraStatus = extraStatus;
	}

	public int getTransactionTypeId() {
		return transactionTypeId;
	}

	public void setTransactionTypeId(int transactionTypeId) {
		this.transactionTypeId = transactionTypeId;
	}

	public String getLockGlobalUniqueId() {
		return lockGlobalUniqueId;
	}

	public void setLockGlobalUniqueId(String lockGlobalUniqueId) {
		this.lockGlobalUniqueId = lockGlobalUniqueId == null ? null : lockGlobalUniqueId.trim();
	}

	public int getLockStatus() {
		return lockStatus;
	}

	public void setLockStatus(int lockStatus) {
		this.lockStatus = lockStatus;
	}

	public long getCartId() {
		return cartId;
	}

	public void setCartId(long cartId) {
		this.cartId = cartId;
	}



	@Override
	public Item clone() {
		try{
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(byteOut);
			out.writeObject(this);

			ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
			ObjectInputStream in =new ObjectInputStream(byteIn);

			return (Item)in.readObject();

		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public String getChargeFromAccountName() {
		return chargeFromAccountName;
	}

	public void setChargeFromAccountName(String chargeFromAccountName) {
		this.chargeFromAccountName = chargeFromAccountName;
	}

	public int getProcessCount() {
		return processCount;
	}

	public void setProcessCount(int processCount) {
		this.processCount = processCount;
	}

	public long getSupplyPartnerId() {
		return supplyPartnerId;
	}

	public void setSupplyPartnerId(long supplyPartnerId) {
		this.supplyPartnerId = supplyPartnerId;
	}

	public float getFrozenMoney() {
		return frozenMoney;
	}

	public void setFrozenMoney(float frozenMoney) {
		this.frozenMoney = frozenMoney;
	}

	public String getOutOrderId() {
		return outOrderId;
	}

	public void setOutOrderId(String outOrderId) {
		this.outOrderId = outOrderId;
	}

	public int getMaxRetry() {
		return maxRetry;
	}

	public void setMaxRetry(int maxRetry) {
		this.maxRetry = maxRetry;
	}

	public String getInOrderId() {
		return inOrderId;
	}

	public void setInOrderId(String inOrderId) {
		this.inOrderId = inOrderId;
	}

	public float getLabelMoney() {
		return labelMoney;
	}

	public void setLabelMoney(float labelMoney) {
		this.labelMoney = labelMoney;
	}

	public float getSuccessMoney() {
		return successMoney;
	}

	public void setSuccessMoney(float successMoney) {
		this.successMoney = successMoney;
	}


	public float getRequestMoney() {
		return requestMoney;
	}

	public void setRequestMoney(float requestMoney) {
		this.requestMoney = requestMoney;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(getClass().getName());
		sb.append('@');
		sb.append(Integer.toHexString(hashCode()));
		sb.append('(');
		sb.append("transactionId=");
		sb.append(transactionId);
		sb.append(",count=");
		sb.append(count);
		sb.append(",transactionTypeId=");
		sb.append(transactionTypeId);
		sb.append(",supplyPartnerId=");
		sb.append(supplyPartnerId);
		sb.append(",account=");
		sb.append(chargeFromAccount);
		sb.append("=>");
		sb.append(chargeToAccount);
		sb.append(",productId=");
		sb.append(productId);
		sb.append(",labelMoney=");
		sb.append(labelMoney);
		sb.append(",requestMoney=");
		sb.append(requestMoney);
		sb.append(",frozenMoney=");
		sb.append(frozenMoney);
		sb.append(",successMoney=");
		sb.append(successMoney);
		sb.append(",status=");
		sb.append(currentStatus);
		sb.append('/');
		sb.append(extraStatus);
		sb.append('/');
		sb.append(outStatus);
		sb.append(')');
		return sb.toString();
	}


	public float getInMoney() {
		return inMoney;
	}

	public void setInMoney(float inMoney) {
		this.inMoney = inMoney;
	}

	public float getOutMoney() {
		return outMoney;
	}

	public void setOutMoney(float outMoney) {
		this.outMoney = outMoney;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getShareConfigId() {
		return shareConfigId;
	}

	public void setShareConfigId(int shareConfigId) {
		this.shareConfigId = shareConfigId;
	}

	public int getBillingStatus() {
		return billingStatus;
	}

	public void setBillingStatus(int billingStatus) {
		this.billingStatus = billingStatus;
	}

	public int getOutStatus() {
		return outStatus;
	}

	public void setOutStatus(int outStatus) {
		this.outStatus = outStatus;
	}

	public int getBeforeLockStatus() {
		return beforeLockStatus;
	}

	public void setBeforeLockStatus(int beforeLockStatus) {
		this.beforeLockStatus = beforeLockStatus;
	}

	public int getAfterLockStatus() {
		return afterLockStatus;
	}

	public void setAfterLockStatus(int afterLockStatus) {
		this.afterLockStatus = afterLockStatus;
	}

	public String getConfig(String dataCode) {
		if(this.config == null){
			return null;
		}
		if(!this.config.containsKey(dataCode)){
			return null;
		}
		if(this.config.get(dataCode) != null){
			return this.config.get(dataCode).trim();
		}
		return null;
	}

	public int getIntConfig(String dataCode) {
		if(this.config != null && this.config.containsKey(dataCode) && NumericUtils.isIntNumber(this.config.get(dataCode))){
			return Integer.parseInt(this.config.get(dataCode).trim());
		}
		return 0;
	}

	public long getLongConfig(String dataCode) {
		if(this.config != null && this.config.containsKey(dataCode) && NumericUtils.isNumeric(this.config.get(dataCode))){
			return Long.parseLong(this.config.get(dataCode).trim());
		}
		return 0;
	}

	public boolean getBooleanConfig(String dataCode) {
		if(this.config != null && this.config.containsKey(dataCode) && this.config.get(dataCode).equalsIgnoreCase("true")){
			return true;
		}
		return false;
	}

	@Override
	public void setExtraValue(String dataCode, String dataValue) {
		if(this.itemDataMap == null){
			this.itemDataMap = new HashMap<String, ProductData>();
		}

		if(StringUtils.isBlank(dataCode) || StringUtils.isBlank(dataValue)){
			return;
		}
		this.itemDataMap.put(dataCode, new ProductData(dataCode,dataValue));
	}


	@Override
	public String getExtraValue(String dataCode) {
		if(this.itemDataMap == null){
			return null;
		}
		if(!this.itemDataMap.containsKey(dataCode)){
			return null;
		}
		if(this.itemDataMap.get(dataCode).getDataValue() != null){
			return this.itemDataMap.get(dataCode).getDataValue().trim();
		}
		return null;
	}

	@Override
	public boolean getBooleanExtraValue(String dataCode) {
		if(this.itemDataMap == null){
			return false;
		}
		if(!this.itemDataMap.containsKey(dataCode)){
			return false;
		}
		if(this.itemDataMap.get(dataCode).getDataValue() != null && this.itemDataMap.get(dataCode).getDataValue().trim().equalsIgnoreCase("true")){
			return true;
		}
		return false;
	}


	@Override
	public long getLongExtraValue(String dataCode) {
		if(this.itemDataMap == null){
			return 0;
		}
		if(!this.itemDataMap.containsKey(dataCode)){
			return 0;
		}
		if(this.itemDataMap.get(dataCode).getDataValue() != null && NumericUtils.isNumeric(itemDataMap.get(dataCode).getDataValue())){
			return Long.parseLong(itemDataMap.get(dataCode).getDataValue().trim());
		}
		return 0;
	}

	@Override
	public float getFloatExtraValue(String dataCode) {
		if(this.itemDataMap == null){
			return 0;
		}
		if(!this.itemDataMap.containsKey(dataCode)){
			return 0;
		}
		if(this.itemDataMap.get(dataCode).getDataValue() != null && NumericUtils.isNumeric(itemDataMap.get(dataCode).getDataValue())){
			return Float.parseFloat(itemDataMap.get(dataCode).getDataValue().trim());
		}
		return 0;
	}

	public Price getPrice() {
		return price;
	}

	public void setPrice(Price price) {
		this.price = price;
	}

	public Map<String, String> getConfig() {
		return config;
	}

	public void setConfig(Map<String, String> config) {
		this.config = config;
	}

	public void addConfig(String key, String value) {
		if(this.config == null){
			this.config = new HashMap<String,String>();
		}
		this.config.put(key, value);
	}

	public void addConfig(Map<String, String> otherConfig) {
		if(this.config == null){
			this.config = new HashMap<String,String>();
		}
		this.config.putAll(otherConfig);
	}

	public long getLastEffect() {
		return lastEffect;
	}

	public void setLastEffect(long lastEffect) {
		this.lastEffect = lastEffect;
	}

	public long getInviter() {
		return inviter;
	}

	public void setInviter(long inviter) {
		this.inviter = inviter;
	}

	public void setConfigValue(String dataCode, String dataValue) {
		if(this.config == null){
			this.config = new HashMap<String,String>();
		}
		if(dataValue == null){
			this.config.remove(dataCode);
			return;
		}
		this.config.put(dataCode, dataValue.trim());
	}

	public long getObjectTypeId() {
		return objectTypeId;
	}

	public void setObjectTypeId(long objectTypeId) {
		this.objectTypeId = objectTypeId;
	}



}
