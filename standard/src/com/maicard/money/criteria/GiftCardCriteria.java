package com.maicard.money.criteria;

import com.maicard.common.base.Criteria;

public class GiftCardCriteria extends Criteria implements Cloneable{

	private static final long serialVersionUID = 1L;

	private String cardNumber;
	private String objectType;
	private int[] objectIds;
	private int objectExtraId;
	private long usedByUuid;
	private int newStatus;
	private String lockGlobalUniqueId;
	private boolean forceLock = false;
	private int maxWaiting = 30;
	private int count;		//生成新卡时的数量
	private int minUseInterval; //被锁定的卡，距上一次使用的最小使用时间间隔
	
	private float	labelMoney;			//礼品卡对应的面额

	private float requestMoney;		//卡余额

	private float frozenMoney;			//卡的冻结金额

	private float successMoney;			//卡的已使用金额

	private int moneyTypeId;

	public GiftCardCriteria() {
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public long getUsedByUuid() {
		return usedByUuid;
	}

	public void setUsedByUuid(long usedByUuid) {
		this.usedByUuid = usedByUuid;
	}

	public int getNewStatus() {
		return newStatus;
	}

	public void setNewStatus(int newStatus) {
		this.newStatus = newStatus;
	}

	public String getLockGlobalUniqueId() {
		return lockGlobalUniqueId;
	}

	public void setLockGlobalUniqueId(String lockGlobalUniqueId) {
		this.lockGlobalUniqueId = lockGlobalUniqueId;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}



	@Override
	public GiftCardCriteria clone() throws CloneNotSupportedException {
		return (GiftCardCriteria)super.clone();
	}

	public int getObjectExtraId() {
		return objectExtraId;
	}

	public void setObjectExtraId(int objectExtraId) {
		this.objectExtraId = objectExtraId;
	}

	public boolean isForceLock() {
		return forceLock;
	}

	public void setForceLock(boolean forceLock) {
		this.forceLock = forceLock;
	}

	public int getMaxWaiting() {
		return maxWaiting;
	}

	public void setMaxWaiting(int maxWaiting) {
		this.maxWaiting = maxWaiting;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getMoneyTypeId() {
		return moneyTypeId;
	}

	public void setMoneyTypeId(int moneyTypeId) {
		this.moneyTypeId = moneyTypeId;
	}

	public int[] getObjectIds() {
		return objectIds;
	}

	public void setObjectIds(int... objectIds) {
		this.objectIds = objectIds;
	}

	public float getLabelMoney() {
		return labelMoney;
	}

	public void setLabelMoney(float labelMoney) {
		this.labelMoney = labelMoney;
	}

	public float getRequestMoney() {
		return requestMoney;
	}

	public void setRequestMoney(float requestMoney) {
		this.requestMoney = requestMoney;
	}

	public float getFrozenMoney() {
		return frozenMoney;
	}

	public void setFrozenMoney(float frozenMoney) {
		this.frozenMoney = frozenMoney;
	}

	public float getSuccessMoney() {
		return successMoney;
	}

	public void setSuccessMoney(float successMoney) {
		this.successMoney = successMoney;
	}

	public int getMinUseInterval() {
		return minUseInterval;
	}

	public void setMinUseInterval(int minUseInterval) {
		this.minUseInterval = minUseInterval;
	}


}
