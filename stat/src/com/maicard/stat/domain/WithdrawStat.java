package com.maicard.stat.domain;

import com.maicard.common.domain.EVEisObject;

public class WithdrawStat extends EVEisObject{

	private static final long serialVersionUID = 1L;

	private long withdrawStatId;		//主键
	private int withdrawMethodId;
	private int successCount;
	private int totalCount;
	private double successMoney;	
	private double totalMoney;				
	private String statTime;	
	private float profit;
	private long inviter;
		
	
	@Override
	public String toString(){
		
			return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
					"(" + 
					"withdrawStatId=" +  withdrawStatId + 
					",withdrawMethodId=" + withdrawMethodId +
					",totalCount=" + totalCount +
					",totalMoney=" + totalMoney +
					",successCount=" + successCount +
					",successMoney=" + successMoney +	
					",statTime=" + statTime +
					",inviter=" + inviter +
					")";
		
	}

	
	public long getWithdrawStatId() {
		return withdrawStatId;
	}

	public void setWithdrawStatId(long withdrawStatId) {
		this.withdrawStatId = withdrawStatId;
	}

	public int getWithdrawMethodId() {
		return withdrawMethodId;
	}

	public void setWithdrawMethodId(int withdrawMethodId) {
		this.withdrawMethodId = withdrawMethodId;
	}

	public int getSuccessCount() {
		return successCount;
	}

	public void setSuccessCount(int successCount) {
		this.successCount = successCount;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public double getSuccessMoney() {
		return successMoney;
	}

	public void setSuccessMoney(double successMoney) {
		this.successMoney = successMoney;
	}

	public double getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(double totalMoney) {
		this.totalMoney = totalMoney;
	}

	public String getStatTime() {
		return statTime;
	}

	public void setStatTime(String statTime) {
		this.statTime = statTime;
	}

	public long getInviter() {
		return inviter;
	}

	public void setInviter(long inviter) {
		this.inviter = inviter;
	}


	public float getProfit() {
		return profit;
	}

	public void setProfit(float profit) {
		this.profit = profit;
	}
	
	




}
