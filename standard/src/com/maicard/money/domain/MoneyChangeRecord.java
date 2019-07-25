package com.maicard.money.domain;

import java.io.Serializable;

/**
 * 用来发送资金变化情况
 *
 *
 * @author NetSnake
 * @date 2017年1月3日
 *
 */
public class MoneyChangeRecord implements Serializable {


	private static final long serialVersionUID = 2130443791231852372L;

	private long uuid;
	private Money money;
	private String moneyType;
	private float changeAmount;
	
	
	public long getUuid() {
		return uuid;
	}
	public void setUuid(long uuid) {
		this.uuid = uuid;
	}
	public Money getMoney() {
		return money;
	}
	public void setMoney(Money money) {
		this.money = money;
	}
	public String getMoneyType() {
		return moneyType;
	}
	public void setMoneyType(String moneyType) {
		this.moneyType = moneyType;
	}
	public float getChangeAmount() {
		return changeAmount;
	}
	public void setChangeAmount(float changeAmount) {
		this.changeAmount = changeAmount;
	}
}
