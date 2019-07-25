package com.maicard.money.criteria;

import java.util.Date;

import com.maicard.common.base.InviterSupportCriteria;

public class MoneyLogCriteria extends InviterSupportCriteria {

	private static final long serialVersionUID = 1L;
	
	private int moneyTypeId;	//资金类型，充值、冻结、收入、积分


	private String op;			//对资金的操作类型，@see CommonStandard.OperateCode
	
	private String memory;			


	private long fromAccount;

	private long toAccount;
	
	private int toAccountType;

	private Date startTime;
	
	private Date endTime;
	
	private long changeTime;

	
	private String transactionId;
	
	private double amount;
	
	/**
	 * 仅用于Controller中进行处理，不在XML中关联查询
	 */
	private String toUserName;
	

	public MoneyLogCriteria() {
	}


	public int getMoneyTypeId() {
		return moneyTypeId;
	}

	public void setMoneyTypeId(int moneyTypeId) {
		this.moneyTypeId = moneyTypeId;
	}

	
	public long getFromAccount() {
		return fromAccount;
	}

	public void setFromAccount(long fromAccount) {
		this.fromAccount = fromAccount;
	}

	public long getToAccount() {
		return toAccount;
	}

	public void setToAccount(long toAccount) {
		this.toAccount = toAccount;
	}




	public String getOp() {
		return op;
	}


	public void setOp(String op) {
		this.op = op;
	}


	public long getChangeTime() {
		return changeTime;
	}


	public void setChangeTime(long changeTime) {
		this.changeTime = changeTime;
	}


	public String getTransactionId() {
		return transactionId;
	}


	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}


	public double getAmount() {
		return amount;
	}


	public void setAmount(double amount) {
		this.amount = amount;
	}



	public Date getStartTime() {
		return startTime;
	}


	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}


	public Date getEndTime() {
		return endTime;
	}


	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}


	public String getMemory() {
		return memory;
	}


	public void setMemory(String memory) {
		if(memory == null || memory.trim().equals("")){
			return;
		}
		this.memory = memory;
	}


	public int getToAccountType() {
		return toAccountType;
	}


	public void setToAccountType(int toAccountType) {
		this.toAccountType = toAccountType;
	}


	public String getToUserName() {
		return toUserName;
	}


	public void setToUserName(String toUserName) {
		if(toUserName == null || toUserName.trim().equals("")){
			return;
		}
		this.toUserName = toUserName;
	}


}
