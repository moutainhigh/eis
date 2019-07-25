package com.maicard.money.criteria;

import java.util.Date;

import com.maicard.common.base.InviterSupportCriteria;

public class PayCriteria extends InviterSupportCriteria {

	private static final long serialVersionUID = 1L;

	private int payId;
	private String transactionId;
	private String outOrderId;
	private String inOrderId;
	private int moneyTypeId;
	
	private String refBuyTransactionId;

	private int payTypeId;
	
	private String tradeType;

	private int payMethodId;
	private long payFromAccount;
	private long payToAccount;
	
	private String payCardType;
	
	private int payFromAccountType;
	
	private int payToAccountType;
	
	private long inviteByUuid;
	private String otherCondition;
	private boolean superMode;
	private String uuidRange;
	private String startTime;
	private String endTime;
	private String groupBy;

	private Date startTimeBegin;
	private Date startTimeEnd;
	private Date endTimeBegin;
	private Date endTimeEnd;
	
	private float minFaceMoney;
	
	private float maxFaceMoney;
	
	/**
	 * 当有跨月查询时，上上个月的表名
	 */
	private String tableName2;

	public String getTableName2() {
		return tableName2;
	}

	public void setTableName2(String tableName2) {
		this.tableName2 = tableName2;
	}

	public float getMinFaceMoney() {
		return minFaceMoney;
	}

	public void setMinFaceMoney(float minFaceMoney) {
		this.minFaceMoney = minFaceMoney;
	}

	public float getMaxFaceMoney() {
		return maxFaceMoney;
	}

	public void setMaxFaceMoney(float maxFaceMoney) {
		this.maxFaceMoney = maxFaceMoney;
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

	private String tableName;


	public PayCriteria() {
	}

	public PayCriteria(long ownerId) {
		this.ownerId = ownerId;
	}

	public int getPayId() {
		return payId;
	}

	public void setPayId(int payId) {
		this.payId = payId;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		if(transactionId == null || transactionId.trim().equals("")){
			return;
		}
		this.transactionId = transactionId;
	}

	public int getMoneyTypeId() {
		return moneyTypeId;
	}

	public void setMoneyTypeId(int moneyTypeId) {
		this.moneyTypeId = moneyTypeId;
	}

	public int getPayMethodId() {
		return payMethodId;
	}

	public void setPayMethodId(int payMethodId) {
		this.payMethodId = payMethodId;
	}

	public long getPayToAccount() {
		return payToAccount;
	}

	public void setPayToAccount(long toAccount) {
		this.payToAccount = toAccount;
	}

	public String getOutOrderId() {
		return outOrderId;
	}

	public void setOutOrderId(String outOrderId) {
		if(outOrderId == null || outOrderId.trim().equals("")){
			return;
		}
		this.outOrderId = outOrderId;
	}

	public String getOtherCondition() {
		return otherCondition;
	}

	public void setOtherCondition(String otherCondition) {
		this.otherCondition = otherCondition;
	}

	public long getInviteByUuid() {
		return inviteByUuid;
	}

	public void setInviteByUuid(long inviteByUuid) {
		this.inviteByUuid = inviteByUuid;
	}

	public boolean isSuperMode() {
		return superMode;
	}

	public void setSuperMode(boolean superMode) {
		this.superMode = superMode;
	}

	public String getUuidRange() {
		return uuidRange;
	}

	public void setUuidRange(String uuidRange) {
		this.uuidRange = uuidRange;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getGroupBy() {
		return groupBy;
	}

	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}

	public Date getStartTimeBegin() {
		return startTimeBegin;
	}

	public void setStartTimeBegin(Date startTimeBegin) {
		this.startTimeBegin = startTimeBegin;
	}

	public Date getStartTimeEnd() {
		return startTimeEnd;
	}

	public void setStartTimeEnd(Date startTimeEnd) {
		this.startTimeEnd = startTimeEnd;
	}

	public long getPayFromAccount() {
		return payFromAccount;
	}

	public void setPayFromAccount(long payFromAccount) {
		this.payFromAccount = payFromAccount;
	}

	public String getInOrderId() {
		return inOrderId;
	}

	public void setInOrderId(String inOrderId) {
		if(inOrderId == null || inOrderId.trim().equals("")){
			return;
		}
		this.inOrderId = inOrderId;
	}

	public String getRefBuyTransactionId() {
		return refBuyTransactionId;
	}

	public void setRefBuyTransactionId(String refBuyTransactionId) {
		if(refBuyTransactionId == null || refBuyTransactionId.trim().equals("")){
			return;
		}
		this.refBuyTransactionId = refBuyTransactionId;
	}

	public int getPayFromAccountType() {
		return payFromAccountType;
	}

	public void setPayFromAccountType(int payFromAccountType) {
		this.payFromAccountType = payFromAccountType;
	}

	public int getPayToAccountType() {
		return payToAccountType;
	}

	public void setPayToAccountType(int payToAccountType) {
		this.payToAccountType = payToAccountType;
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

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}



}
