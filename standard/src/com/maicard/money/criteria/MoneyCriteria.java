package com.maicard.money.criteria;

import com.maicard.common.base.InviterSupportCriteria;

public class MoneyCriteria extends InviterSupportCriteria {

	private static final long serialVersionUID = 1L;
	
	private long uuid;
	private float chargeMoney;
	private float incomingMoney;
	private float marginMoney;
	private float giftMoney;
	private int moneyTypeId;
		
	private int userTypeId;
	
	//private boolean lockRemainMoneyWhenNotEnough;

	public MoneyCriteria() {
	}

	public long getUuid() {
		return uuid;
	}

	public void setUuid(long uuid) {
		this.uuid = uuid;
	}

	

	public double getChargeMoney() {
		return chargeMoney;
	}

	public void setChargeMoney(float needMoney) {
		this.chargeMoney = needMoney;
	}

	public double getIncomingMoney() {
		return incomingMoney;
	}

	public void setIncomingMoney(float incomingMoney) {
		this.incomingMoney = incomingMoney;
	}

	public double getMarginMoney() {
		return marginMoney;
	}

	public void setMarginMoney(float marginMoney) {
		this.marginMoney = marginMoney;
	}

	public float getGiftMoney() {
		return giftMoney;
	}

	public void setGiftMoney(float giftMoney) {
		this.giftMoney = giftMoney;
	}

	public int getMoneyTypeId() {
		return moneyTypeId;
	}

	public void setMoneyTypeId(int moneyTypeId) {
		this.moneyTypeId = moneyTypeId;
	}

	public int getUserTypeId() {
		return userTypeId;
	}

	public void setUserTypeId(int userTypeId) {
		this.userTypeId = userTypeId;
	}

	

}
