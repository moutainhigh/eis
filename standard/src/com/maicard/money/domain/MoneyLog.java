package com.maicard.money.domain;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.maicard.common.domain.EVEisObject;
import com.maicard.common.domain.EisObject;
import com.maicard.common.util.NumericUtils;
import com.maicard.method.ExtraValueAccess;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MoneyLog  extends EVEisObject implements Cloneable{


	private static final long serialVersionUID = 3267726435984603418L;

	private int moneyLogId;

	private int moneyTypeId;	//资金类型，充值、冻结、收入、积分


	private String op;			//对资金的操作类型，@see CommonStandard.Operate
	
	private String memory;
	

	private long fromAccount;

	private long toAccount;

	private Date enterTime;
	
	private long changeTime;

	
	private String transactionId;
	
	private double amount;
	
	private Money moneyAfter;
	
	private double amountAfter;		//操作执行后最新资金
	
	private Map<String,String> data;
	
	
	@Override
	public MoneyLog clone() {
		try{
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(byteOut);
			out.writeObject(this);

			ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
			ObjectInputStream in =new ObjectInputStream(byteIn);

			return (MoneyLog)in.readObject();

		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public MoneyLog() {
	}

	

	public MoneyLog(Money money) {
		this.fromAccount = money.getUuid();
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

	public long getChangeTime() {
		return changeTime;
	}

	public void setChangeTime(long changeTime) {
		this.changeTime = changeTime;
	}

	public int getMoneyLogId() {
		return moneyLogId;
	}



	public void setMoneyLogId(int moneyLogId) {
		this.moneyLogId = moneyLogId;
	}

	public String getOp() {
		return op;
	}



	public void setOp(String op) {
		this.op = op;
	}



	public double getAmount() {
		return amount;
	}



	public void setAmount(double amount) {
		this.amount = amount;
	}


	public String getTransactionId() {
		return transactionId;
	}



	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}



	public Date getEnterTime() {
		return enterTime;
	}



	public void setEnterTime(Date enterTime) {
		this.enterTime = enterTime;
	}



	public double getAmountAfter() {
		return amountAfter;
	}



	public void setAmountAfter(double amountAfter) {
		this.amountAfter = amountAfter;
	}

	public Money getMoneyAfter() {
		return moneyAfter;
	}
	public void setMoneyAfter(Money moneyAfter) {
		this.moneyAfter = moneyAfter;
	}
	public String getMemory() {
		return memory;
	}
	public void setMemory(String memory) {
		this.memory = memory;
	}
	
}
