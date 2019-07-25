package com.maicard.billing.criteria;

import java.util.Date;

import com.maicard.common.base.InviterSupportCriteria;

public class BillingCriteria extends InviterSupportCriteria {

	private static final long serialVersionUID = -1840337619819376913L;
	/**
	 * 主键
	 */
	private int billingId;
	/**
	 * 结算开始时间的起始
	 */
	private Date billingBeginTimeBegin;
	
	/**
	 * 结算开始时间的结束
	 */
	private Date billingBeginTimeEnd;

	/**
	 * 结算截至时间的起始
	 */
	private Date billingEndTimeBegin;	
	
	private String billingKey;
	
	/**
	 * 结算截至时间的结束
	 */
	private Date billingEndTimeEnd;	
	
	private int shareConfigId;
	
	private String payCardType;



	
	private Date billingHandlerTime; //结算处理时间
	private long uuid;			//结算账户UUID
	private Float faceMoney;		//结算面额
	private Float commission;		//佣金，手续费
	private Float realMoney;		//实际结算金额
	private String objectType;		//结算对应的业务类型
	private long objectId;		//结算对应的业务ID
	
	/**
	 * 操作该订单的用户
	 */
	private long operator;
	/**
	 * 清算状态
	 */
	private String clearStatus;
	/**
	 * 清算方式
	 */
	private String clearWay;
	/**
	 * 清算类型
	 */
	private String clearType;
	/**
	 * 结算状态
	 */
	private String stateStatus;

	
	public BillingCriteria(){}
	
	public BillingCriteria(long ownerId) {
		this.ownerId = ownerId;
	}
	
	public long getUuid() {
		return uuid;
	}
	public void setUuid(long uuid) {
		this.uuid = uuid;
	}
	public Float getFaceMoney() {
		return faceMoney == null ? 0 : faceMoney;
	}
	public void setFaceMoney(Float faceMoney) {
		this.faceMoney = faceMoney;
	}
	public Float getCommission() {
		return commission == null ? 0: commission;
	}
	public void setCommission(Float commission) {
		this.commission = commission;
	}
	public Float getRealMoney() {
		return realMoney == null ? 0: realMoney;
	}
	public void setRealMoney(Float realMoney) {
		this.realMoney = realMoney;
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
	public int getBillingId() {
		return billingId;
	}
	public void setBillingId(int billingId) {
		this.billingId = billingId;
	}

	public long getOperator() {
		return operator;
	}

	public void setOperator(long operator) {
		this.operator = operator;
	}

	public Date getBillingBeginTimeBegin() {
		return billingBeginTimeBegin;
	}

	public void setBillingBeginTimeBegin(Date billingBeginTimeBegin) {
		this.billingBeginTimeBegin = billingBeginTimeBegin;
	}

	public Date getBillingBeginTimeEnd() {
		return billingBeginTimeEnd;
	}

	public void setBillingBeginTimeEnd(Date billingBeginTimeEnd) {
		this.billingBeginTimeEnd = billingBeginTimeEnd;
	}

	public Date getBillingEndTimeBegin() {
		return billingEndTimeBegin;
	}

	public void setBillingEndTimeBegin(Date billingEndTimeBegin) {
		this.billingEndTimeBegin = billingEndTimeBegin;
	}

	public Date getBillingEndTimeEnd() {
		return billingEndTimeEnd;
	}

	public void setBillingEndTimeEnd(Date billingEndTimeEnd) {
		this.billingEndTimeEnd = billingEndTimeEnd;
	}

	public long getObjectId() {
		return objectId;
	}

	public void setObjectId(long objectId) {
		this.objectId = objectId;
	}

	public int getShareConfigId() {
		return shareConfigId;
	}

	public void setShareConfigId(int shareConfigId) {
		this.shareConfigId = shareConfigId;
	}

	public String getPayCardType() {
		return payCardType;
	}

	public void setPayCardType(String payCardType) {
		this.payCardType = payCardType;
	}

	public String getClearStatus() {
		return clearStatus;
	}

	public void setClearStatus(String clearStatus) {
		this.clearStatus = clearStatus;
	}

	public String getClearWay() {
		return clearWay;
	}

	public void setClearWay(String clearWay) {
		this.clearWay = clearWay;
	}

	public String getClearType() {
		return clearType;
	}

	public void setClearType(String clearType) {
		this.clearType = clearType;
	}

	public String getStateStatus() {
		return stateStatus;
	}

	public void setStateStatus(String stateStatus) {
		this.stateStatus = stateStatus;
	}

	public String getBillingKey() {
		return billingKey;
	}

	public void setBillingKey(String billingKey) {
		this.billingKey = billingKey;
	}
}
