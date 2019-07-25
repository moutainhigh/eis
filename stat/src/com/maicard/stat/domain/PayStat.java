package com.maicard.stat.domain;


import com.maicard.common.domain.EVEisObject;

public class PayStat extends EVEisObject{

	private static final long serialVersionUID = 1L;

	private long payStatId;		//主键
	
	private int payTypeId;
	
	private int payMethodId;
	private int successCount;
	private int totalCount;
	private double successMoney;	
	private double totalMoney;				
	private String statTime;	
	private float profit;
	private long inviter;
	
	/**
	 * 付款卡类型
	 */
	private String payCardType;
	
	
	
	@Override
	public String toString(){
		
			return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
					"(" + 
					"payStatId=" +  payStatId + 
					",payTypeId=" + payTypeId +
					",payMethodId=" + payMethodId +
					",totalCount=" + totalCount +
					",totalMoney=" + totalMoney +
					",successCount=" + successCount +
					",successMoney=" + successMoney +	
					",statTime=" + statTime +
					",inviter=" + inviter +
					")";
		
	}

	

	public long getPayStatId() {
		return payStatId;
	}

	public void setPayStatId(long payStatId) {
		this.payStatId = payStatId;
	}

	public int getPayMethodId() {
		return payMethodId;
	}

	public void setPayMethodId(int payMethodId) {
		this.payMethodId = payMethodId;
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

	public int getPayTypeId() {
		return payTypeId;
	}

	public void setPayTypeId(int payTypeId) {
		this.payTypeId = payTypeId;
	}

	public String getPayCardType() {
		return payCardType;
	}

	public void setPayCardType(String payCardType) {
		this.payCardType = payCardType;
	}
	
	




}
