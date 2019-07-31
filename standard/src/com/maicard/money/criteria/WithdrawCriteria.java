package com.maicard.money.criteria;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.maicard.common.base.InviterSupportCriteria;

public class WithdrawCriteria extends InviterSupportCriteria {


	private static final long serialVersionUID = 7210112251732508695L;
	
	/**
	 * 查询时不包含子订单
	 */
	public static final int NO_SUB_ORDER = 1;
	
	/**
	 * 查询时包含子订单
	 */
	public static final int INCLUDE_SUB_ORDER = 0;
	
	private String transactionId;
	private int withdrawTypeId;

	private String inOrderId;
	private String outOrderId;	
	private float commissionCharge;	//提现手续费，根据手续费类型进行计算	
	private int commissionChargeType;	//提现手续费类型
	private long uuid;
	private int[] bankAccountIds;
	private float minMoney;
	private float maxMoney;
	private Date beginTimeBegin;
	private Date beginTimeEnd;
	private Date endTimeBegin;
	private Date endTimeEnd;
	
	private int billingId;
	
	private String bankName;
	
	private boolean requireSummary;
	
	private String fuzzyBankAccountName;
	
	private String fuzzyBankAccountNumber;
	
	private String parentTransactionId;
	
	private String businessCode;

	/**
	 * 是否包含子订单，默认包含，0为包含，1为不包含
	 */
	private int includeSubOrder;
	/**
	 * 交易版本
	 */
	private String tradeVersion;
	/**
	 * 人工付款操作类型
	 */
	private String manualType;
	/**
	 * 当前渠道请求号
	 */
	public String curChannelRequestNo;
	/**
	 * 出款会员id
	 */
	public String memberNo;
	/**
	 * 渠道状态
	 */
	public String channelStatus;
	/**
	 * 银行卡号
	 */
	public String bankAccountNumber;
	/**
	 * 银行账号名称
	 */
	public String bankAccountName;

	public WithdrawCriteria() {
	}
	public WithdrawCriteria(long ownerId) {
		this.ownerId = ownerId;
	}
	public long getUuid() {
		return uuid;
	}
	public void setUuid(long uuid) {
		this.uuid = uuid;
	}

	public float getMinMoney() {
		return minMoney;
	}
	public void setMinMoney(float minMoney) {
		this.minMoney = minMoney;
	}
	public float getMaxMoney() {
		return maxMoney;
	}
	public void setMaxMoney(float maxMoney) {
		this.maxMoney = maxMoney;
	}
	
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public int getWithdrawTypeId() {
		return withdrawTypeId;
	}
	public void setWithdrawTypeId(int withdrawTypeId) {
		this.withdrawTypeId = withdrawTypeId;
	}
	public String getOutOrderId() {
		return outOrderId;
	}
	public void setOutOrderId(String outOrderId) {
		this.outOrderId = outOrderId;
	}
	public float getCommissionCharge() {
		return commissionCharge;
	}
	public void setCommissionCharge(float commissionCharge) {
		this.commissionCharge = commissionCharge;
	}
	public int getCommissionChargeType() {
		return commissionChargeType;
	}
	public void setCommissionChargeType(int commissionChargeType) {
		this.commissionChargeType = commissionChargeType;
	}
	public String getInOrderId() {
		return inOrderId;
	}
	public void setInOrderId(String inOrderId) {
		if(StringUtils.isNotBlank(inOrderId)){
			this.inOrderId = inOrderId;
		}
	}
	public boolean isRequireSummary() {
		return requireSummary;
	}
	public void setRequireSummary(boolean requireSummary) {
		this.requireSummary = requireSummary;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getFuzzyBankAccountName() {
		return fuzzyBankAccountName;
	}
	public void setFuzzyBankAccountName(String fuzzyBankAccountName) {
		if(StringUtils.isBlank(fuzzyBankAccountName)){
			return;
		}
		this.fuzzyBankAccountName = fuzzyBankAccountName;
	}
	public Date getBeginTimeBegin() {
		return beginTimeBegin;
	}
	public void setBeginTimeBegin(Date beginTimeBegin) {
		this.beginTimeBegin = beginTimeBegin;
	}
	public Date getBeginTimeEnd() {
		return beginTimeEnd;
	}
	public void setBeginTimeEnd(Date beginTimeEnd) {
		this.beginTimeEnd = beginTimeEnd;
	}
	public Date getEndTimeBegin() {
		return endTimeBegin;
	}
	public void setEndTimeBegin(Date endTimeBegin) {
		this.endTimeBegin = endTimeBegin;
	}
	public Date getEndTimeEnd() {
		return endTimeEnd;
	}
	public void setEndTimeEnd(Date endTimeEnd) {
		this.endTimeEnd = endTimeEnd;
	}
	public String getParentTransactionId() {
		return parentTransactionId;
	}
	public void setParentTransactionId(String parentTransactionId) {
		this.parentTransactionId = parentTransactionId;
	}
	public int getIncludeSubOrder() {
		return includeSubOrder;
	}
	public void setIncludeSubOrder(int includeSubOrder) {
		this.includeSubOrder = includeSubOrder;
	}
	public int getBillingId() {
		return billingId;
	}
	public void setBillingId(int billingId) {
		this.billingId = billingId;
	}
	public int[] getBankAccountIds() {
		return bankAccountIds;
	}
	public void setBankAccountIds(int... bankAccountIds) {
		this.bankAccountIds = bankAccountIds;
	}
	public String getFuzzyBankAccountNumber() {
		return fuzzyBankAccountNumber;
	}
	public void setFuzzyBankAccountNumber(String fuzzyBankAccountNumber) {
		this.fuzzyBankAccountNumber = fuzzyBankAccountNumber;
	}

	public String getTradeVersion() {
		return tradeVersion;
	}

	public void setTradeVersion(String tradeVersion) {
		this.tradeVersion = tradeVersion;
	}

	public String getManualType() {
		return manualType;
	}

	public void setManualType(String manualType) {
		this.manualType = manualType;
	}

	public String getCurChannelRequestNo() {
		return curChannelRequestNo;
	}

	public void setCurChannelRequestNo(String curChannelRequestNo) {
		this.curChannelRequestNo = curChannelRequestNo;
	}

	public String getMemberNo() {
		return memberNo;
	}

	public void setMemberNo(String memberNo) {
		this.memberNo = memberNo;
	}

	public String getChannelStatus() {
		return channelStatus;
	}

	public void setChannelStatus(String channelStatus) {
		this.channelStatus = channelStatus;
	}

	public String getBankAccountNumber() {
		return bankAccountNumber;
	}

	public void setBankAccountNumber(String bankAccountNumber) {
		this.bankAccountNumber = bankAccountNumber;
	}

	public String getBankAccountName() {
		return bankAccountName;
	}

	public void setBankAccountName(String bankAccountName) {
		this.bankAccountName = bankAccountName;
	}
	public String getBusinessCode() {
		return businessCode;
	}
	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}
}
