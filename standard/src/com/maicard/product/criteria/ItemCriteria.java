package com.maicard.product.criteria;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.maicard.common.base.InviterSupportCriteria;
import com.maicard.common.util.JsonUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemCriteria extends InviterSupportCriteria implements Cloneable{

	private static final long serialVersionUID = 1L;
	/*
	 * XXX 锁定模式说明
	 * 模式1：卡密不支持部分消费，帐号不支持部分充值
	 * 请求锁定金额 = 卡密未消费金额 = 帐号未充值金额
	 * lockMoney = card.requestMoney = account.requestMoney
	 * 
	 * 模式2：卡密支持部分消费，帐号不支持部分充值
	 * 请求锁定金额 >= 卡密最低消费金额  == 帐号未充值金额， <=卡密未消费金额
	 * lockMoney <= card.requestMoney
	 * minLockMoney >= card.parMiniUseMoney >= account.requestMoney
	 * 
	 * 模式3：卡密不支持部分消费，帐号支持部分充值
	 * 请求锁定金额 >= 卡密未消费金额  >= 帐号最低充值金额
	 * lockMoney >= card.requestMoney >= account.partMinUseMoney
	 * 
	 * 模式4：卡密支持部分消费，帐号支持部分充值
	 * 请求锁定金额 >= 卡密最低消费金额  >= 帐号最低充值金额, <=卡密未消费金额 <=帐号未消费金额
	 * minLockMoney >= card.partMinUseMoney >= account.partMinUseMoney
	 * lockMoney <= card.requestMoney <= account.requestMoney
	 */
	public static final int LOCK_EQ = 1;
	public static final int LOCK_SOURCE_PART = 2;
	public static final int LOCK_DEST_PART = 3;
	public static final int LOCK_BOTH_PART = 4;
	public static final int LOCK_ACCORDING_PRODUCT_MATCH = 5;
	
	public static final String TIMEOUT_ONLY="TIMEOUT_ONLY"; //超时
	public static final String TIMEIN_ONLY="TIMEIN_ONLY";	//未超时
	public static final String TIMEDEAD_ONLY="TIMEDEAD_ONLY";	//超时X3
	
	public static final int CHANNLE_ORDER_OFFICAL_FIRST = 1;
	public static final int CHANNLE_ORDER_NORMAL_WEIGHT = 2;
	public static final int CHANNLE_ORDER_OFFICAL_ONLY = 3;
	
	public static final String ITEM_TABLE_PREFIX = "item";
	public static final String ITEM_DATA_TABLE_PREFIX = "item_data";
	
	public static final String EXTRA_DATA_MODE_NONE = "none";
	public static final String EXTRA_DATA_MODE_FULL = "full";


	private String messageId;
	private String transactionId;
	private String inOrderId;	
	private String outOrderId;
	private long chargeFromAccount;
	private long chargeToAccount;
	private int lockStatus;
	private String lockGlobalUniqueId;
	private int transactionTypeId;
	private long[] productIds;
	private long cartId;	//商品所属购物车的ID
	private String dbType;		//数据库类型
	private String queryProcessingItem = "none"; //none:不查询正在处理的表; only:只查询正在处理的表; both:联合历史表和当前表
	private String[] transactionIds;
	private long[] itemIds;
	private long[] inviters;
	private String timeoutPolicy; 
	private int timeoutSeconds;
	private float needRequestMoney;	//需要锁定的金额
	private float needMinRequestMoney;	//如果需要锁定的金额不可用，需要锁定的最小金额
	private boolean noFrozenMoney = false; //不锁定那些已经有冻结金额的订单
	private boolean noExtraStatusIsNotMatching = true;	 //不查找那些扩展状态是不匹配的订单
	private int extraStatus;
	private int billingStatus = 0;
	private int outStatus = 0;
	private long supplyPartnerId;
	private Date enterTimeBegin;
	private Date enterTimeEnd;
	private Date closeTimeBegin;
	private Date closeTimeEnd;
	private int moneyLockMode;
	private String username;
	private long processUuid;	//卡密匹配逻辑时由哪个终端发起的匹配请求
	private String content;
	private String contentLike;
	private String name;
	private int minWeight = 0;	//最低优先级
	private int maxWeight = 0;	//最高优先级
	private int fixWeight = 0;	//指定优先级
	
	private String objectType;

	/**
	 * 指定rate的范围
	 */
	private int minRate;
	private int maxRate;
	
	/**
	 * 指定交易的购买数量，不是返回数据的数量
	 */
	private int minCount;
	private int maxCount;

	private long lastEffectInterval;		//最后一次产生影响的时间间隔
	
	private int shareConfigId;
	private int perfRegionId;
	private int regionPolicy;
	
	//锁定交易前获取多少个随机交易
	private int maxRandomCount = 1;
	//锁定交易前获取多少页随机交易
	private int maxRandomPage = 1;
	//锁定前获取交易的排序方式
	private String sort = null;
	
	private boolean noUsedItem;	//不获取那些已经使用过的
	
	private boolean forceLockUnMatchMoney = false;
	
	/**
	 * 如何获取扩展数据
	 */
	private String extraDataMode = null;
	
	
	public ItemCriteria() {
	}
	public ItemCriteria(long ownerId) {
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
	
	public int getShareConfigId() {
		return shareConfigId;
	}
	public void setShareConfigId(int shareConfigId) {
		this.shareConfigId = shareConfigId;
	}


	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
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

	public int getLockStatus() {
		return lockStatus;
	}

	public void setLockStatus(int lockStatus) {
		this.lockStatus = lockStatus;
	}

	public String getLockGlobalUniqueId() {
		return lockGlobalUniqueId;
	}

	public void setLockGlobalUniqueId(String lockGlobalUniqueId) {
		this.lockGlobalUniqueId = lockGlobalUniqueId;
	}

	public int getTransactionTypeId() {
		return transactionTypeId;
	}

	public void setTransactionTypeId(int transactionTypeId) {
		this.transactionTypeId = transactionTypeId;
	}


	public long getCartId() {
		return cartId;
	}

	public void setCartId(long cartId) {
		this.cartId = cartId;
	}

	/*public String getBeginTime() {
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
*/
	public String getQueryProcessingItem() {
		return queryProcessingItem;
	}

	public void setQueryProcessingItem(String queryProcessingItem) {
		this.queryProcessingItem = queryProcessingItem;
	}

	public long[] getInviters() {
		return inviters;
	}

	public void setInviters(long... inviters) {
		this.inviters = inviters;
	}

	public String[] getTransactionIds() {
		return transactionIds;
	}

	public void setTransactionIds(String[] transactionIds) {
		this.transactionIds = transactionIds;
	}
	public long[] getItemIds() {
		return itemIds;
	}
	public void setItemIds(long... itemIds) {
		this.itemIds = itemIds;
	}
	
	public float getNeedRequestMoney() {
		return needRequestMoney;
	}
	public void setNeedRequestMoney(float needRequestMoney) {
		this.needRequestMoney = needRequestMoney;
	}
	public int getExtraStatus() {
		return extraStatus;
	}
	public void setExtraStatus(int extraStatus) {
		this.extraStatus = extraStatus;
	}
	public String getOutOrderId() {
		return outOrderId;
	}
	public void setOutOrderId(String outOrderId) {
		this.outOrderId = outOrderId;
	}
	public long getSupplyPartnerId() {
		return supplyPartnerId;
	}
	public void setSupplyPartnerId(long supplyPartnerId) {
		this.supplyPartnerId = supplyPartnerId;
	}
	public Date getEnterTimeBegin() {
		return enterTimeBegin;
	}
	public void setEnterTimeBegin(Date enterTimeBegin) {
		this.enterTimeBegin = enterTimeBegin;
	}
	public Date getEnterTimeEnd() {
		return enterTimeEnd;
	}
	public void setEnterTimeEnd(Date enterTimeEnd) {
		this.enterTimeEnd = enterTimeEnd;
	}
	public Date getCloseTimeBegin() {
		return closeTimeBegin;
	}
	public void setCloseTimeBegin(Date closeTimeBegin) {
		this.closeTimeBegin = closeTimeBegin;
	}
	public Date getCloseTimeEnd() {
		return closeTimeEnd;
	}
	public void setCloseTimeEnd(Date closeTimeEnd) {
		this.closeTimeEnd = closeTimeEnd;
	}
	public String getInOrderId() {
		return inOrderId;
	}
	public void setInOrderId(String inOrderId) {
		if(inOrderId == null || inOrderId.trim().equals("")){
			return;
		}
		this.inOrderId = inOrderId.trim();
	}
	public float getNeedMinRequestMoney() {
		return needMinRequestMoney;
	}
	public void setNeedMinRequestMoney(float needMinRequestMoney) {
		this.needMinRequestMoney = needMinRequestMoney;
	}
	public int getMoneyLockMode() {
		return moneyLockMode;
	}
	public void setMoneyLockMode(int moneyLockMode) {
		this.moneyLockMode = moneyLockMode;
	}

	@Override
	public ItemCriteria clone(){
		try {
			return (ItemCriteria)super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public boolean isNoFrozenMoney() {
		return noFrozenMoney;
	}
	public void setNoFrozenMoney(boolean noFrozenMoney) {
		this.noFrozenMoney = noFrozenMoney;
	}
	public long getProcessUuid() {
		return processUuid;
	}
	public void setProcessUuid(long processUuid) {
		this.processUuid = processUuid;
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getMinWeight() {
		return minWeight;
	}
	public void setMinWeight(int minWeight) {
		this.minWeight = minWeight;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public int getPerfRegionId() {
		return perfRegionId;
	}
	public void setPerfRegionId(int perfRegionId) {
		this.perfRegionId = perfRegionId;
	}
	public int getRegionPolicy() {
		return regionPolicy;
	}
	public void setRegionPolicy(int regionPolicy) {
		this.regionPolicy = regionPolicy;
	}
	public int getTimeoutSeconds() {
		return timeoutSeconds;
	}
	public void setTimeoutSeconds(int timeoutSeconds) {
		this.timeoutSeconds = timeoutSeconds;
	}
	public String getContentLike() {
		return contentLike;
	}
	public void setContentLike(String contentLike) {
		this.contentLike = contentLike;
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
	public int getFixWeight() {
		return fixWeight;
	}
	public void setFixWeight(int fixWeight) {
		this.fixWeight = fixWeight;
	}
	public int getMaxRandomCount() {
		return maxRandomCount;
	}
	public void setMaxRandomCount(int maxRandomCount) {
		this.maxRandomCount = maxRandomCount;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public int getMaxRandomPage() {
		return maxRandomPage;
	}
	public void setMaxRandomPage(int maxRandomPage) {
		this.maxRandomPage = maxRandomPage;
	}
	public int getMaxWeight() {
		return maxWeight;
	}
	public void setMaxWeight(int maxWeight) {
		this.maxWeight = maxWeight;
	}
	public boolean isNoExtraStatusIsNotMatching() {
		return noExtraStatusIsNotMatching;
	}
	public void setNoExtraStatusIsNotMatching(boolean noExtraStatusIsNotMatching) {
		this.noExtraStatusIsNotMatching = noExtraStatusIsNotMatching;
	}
	public boolean isForceLockUnMatchMoney() {
		return forceLockUnMatchMoney;
	}
	public void setForceLockUnMatchMoney(boolean forceLockUnMatchMoney) {
		this.forceLockUnMatchMoney = forceLockUnMatchMoney;
	}
	public boolean isNoUsedItem() {
		return noUsedItem;
	}
	public void setNoUsedItem(boolean noUsedItem) {
		this.noUsedItem = noUsedItem;
	}
	public String getDbType() {
		return dbType;
	}
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}
	public long getLastEffectInterval() {
		return lastEffectInterval;
	}
	public void setLastEffectInterval(long lastEffectInterval) {
		this.lastEffectInterval = lastEffectInterval;
	}
	public int getMinRate() {
		return minRate;
	}
	public void setMinRate(int minRate) {
		this.minRate = minRate;
	}
	public int getMaxRate() {
		return maxRate;
	}
	public void setMaxRate(int maxRate) {
		this.maxRate = maxRate;
	}
	public int getMinCount() {
		return minCount;
	}
	public void setMinCount(int minCount) {
		this.minCount = minCount;
	}
	public int getMaxCount() {
		return maxCount;
	}
	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}
	public String getExtraDataMode() {
		return extraDataMode;
	}
	public void setExtraDataMode(String extraDataMode) {
		this.extraDataMode = extraDataMode;
	}
	public String getObjectType() {
		return objectType;
	}
	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}
	
	
}
