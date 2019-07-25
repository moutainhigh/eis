package com.maicard.billing.criteria;

import java.util.Date;

import com.maicard.common.base.Criteria;

public class SettlementCriteria extends Criteria {

private static final long serialVersionUID = -1840337619819376913L;
	
	private int settlementId;			//主键
	private Date billingBeginTime;	//结算开始时间
	private Date billingEndTime;	//结算结束时间
	private Date billingHandlerTime; //结算处理时间
	private long[] uuid;		//结算账户UUID
	
	private Float tradeMoney;		//结算面额
	private Float commission;		//佣金，手续费
	private Float settlementMoney;		//实际结算金额
	private String objectType;		//结算对应的业务类型
	private Integer objectId;		//结算对应的业务ID
	private Float sharePercent;	
	public Float getSharePercent() {
		return sharePercent;
	}
	public void setSharePercent(Float sharePercent) {
		this.sharePercent = sharePercent;
	}
	public Date getBillingBeginTime() {
		return billingBeginTime;
	}
	public void setBillingBeginTime(Date billingBeginTime) {
		this.billingBeginTime = billingBeginTime;
	}
	public Date getBillingEndTime() {
		return billingEndTime;
	}
	public void setBillingEndTime(Date billingEndTime) {
		this.billingEndTime = billingEndTime;
	}

	public long[] getUuid() {
		return uuid;
	}
	public void setUuid(long... uuid) {
		this.uuid = uuid;
	}

	public Float getCommission() {
		return commission == null ? 0: commission;
	}
	public void setCommission(Float commission) {
		this.commission = commission;
	}

	public Float getTradeMoney() {
		return tradeMoney;
	}
	public void setTradeMoney(Float tradeMoney) {
		this.tradeMoney = tradeMoney;
	}
	public Float getSettlementMoney() {
		return settlementMoney;
	}
	public void setSettlementMoney(Float settlementMoney) {
		this.settlementMoney = settlementMoney;
	}
	public Date getBillingHandlerTime() {
		return billingHandlerTime;
	}
	public void setBillingHandlerTime(Date billingHandlerTime) {
		this.billingHandlerTime = billingHandlerTime;
	}
	public String getObjectType() {
		return objectType;
	}
	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}
	public Integer getObjectId() {
		return objectId;
	}
	public void setObjectId(Integer objectId) {
		this.objectId = objectId;
	}
	public int getSettlementId() {
		return settlementId;
	}
	public void setSettlementId(int settlementId) {
		this.settlementId = settlementId;
	}

	
	

}
