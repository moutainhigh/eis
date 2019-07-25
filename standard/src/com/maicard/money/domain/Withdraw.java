package com.maicard.money.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.maicard.annotation.NeedJmsDataSyncP2P;
import com.maicard.common.domain.EVEisObject;
import com.maicard.views.JsonFilterView;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@NeedJmsDataSyncP2P
public class Withdraw extends EVEisObject implements Cloneable{

	private static final long serialVersionUID = -883577602876914371L;
	
	private int withdrawId;
	
	private String transactionId;
	
	private int withdrawTypeId;
	
	private int withdrawMethodId;
	/**
	 * 收款账户类型--默认银行卡
	 * bankCard 银行卡
	 * aliPayLoginId
	 * aliPayUserId
	 */
	private String payeeType;
	/**
	 * 请求提现时对应的提现方式的实例，仅用于在后续处理时临时存放该提现方式的一些参数配置，不保存
	 */
    @JsonView({JsonFilterView.Partner.class})
    private WithdrawMethod withdrawMethod;
    
	private long uuid;
	private int bankAccountId;
	private BankAccount bankAccount;
	
	/**
	 * 申请提现的金额
	 */
	private float withdrawMoney;		
	
	/**
	 * 提交到银行的金额
	 */
	private float arriveMoney;
	
	/**
	 * 实际成功金额
	 */
	private float successMoney;		
	
	private float moneyBeforeWithdraw;	//提现钱的收入资金	
	private float moneyAfterWithdraw;	//提现后剩余收入资金
	private Date beginTime;
	private Date endTime;
	private String inOrderId;
	private String outOrderId;	
	private float commission;	//提现手续费，根据手续费类型进行计算	
	private String commissionType;	//提现手续费类型
	private String commissionChargeType;	//提现手续费从哪里收取
	
	private int billingId;
	
	/**
	 * 是否为批付模式，是则会把批付数据转化为多个子订单
	 */
	private boolean batchMode;
	
	/**
	 * 批付时对应的总订单
	 */
	private String parentTransactionId;
	
	/**
	 * 批付的帐号个数
	 */
	private int totalRequest;
	
	/**
	 * 批付的成功帐号数量
	 */
	private int successRequest;
	
	
	/**
	 * 批付的失败数量
	 */
	private int failRequest;
	
	/**
	 * 付款理由
	 */
	private String reason;
	
    @JsonView({JsonFilterView.Full.class})
	private Map<String,String> data;			//其他扩展数据
	
	
	
	
	private long inviter;
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
	 * 交易来源
	 */
	private String tradeSource;

	private String withdrawMode;


	public Withdraw(){
	}

	public Withdraw(long ownerId){
		this.ownerId = ownerId;
	}
	
	@Override
	public Withdraw clone() {
		try{
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(byteOut);
			out.writeObject(this);

			ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
			ObjectInputStream in =new ObjectInputStream(byteIn);

			return (Withdraw)in.readObject();

		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	
	public long getUuid() {
		return uuid;
	}
	public void setUuid(long uuid) {
		this.uuid = uuid;
	}
	
	public int getWithdrawTypeId() {
		return withdrawTypeId;
	}
	public void setWithdrawTypeId(int withdrawTypeId) {
		this.withdrawTypeId = withdrawTypeId;
	}

	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getOutOrderId() {
		return outOrderId;
	}
	public void setOutOrderId(String outOrderId) {
		this.outOrderId = outOrderId;
	}
	
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public float getCommission() {
		return commission;
	}
	public void setCommission(float commission) {
		this.commission = commission;
	}
	public String getCommissionType() {
		return commissionType;
	}
	public void setCommissionType(String commissionType) {
		this.commissionType = commissionType;
	}
	public BankAccount getBankAccount() {
		return bankAccount;
	}
	public void setBankAccount(BankAccount bankAccount) {
		this.bankAccount = bankAccount;
	}

	public int getBankAccountId() {
		return bankAccountId;
	}

	public void setBankAccountId(int bankAccountId) {
		this.bankAccountId = bankAccountId;
	}

	public long getInviter() {
		return inviter;
	}

	public void setInviter(long inviter) {
		this.inviter = inviter;
	}

	public float getWithdrawMoney() {
		return withdrawMoney;
	}

	public void setWithdrawMoney(float withdrawMoney) {
		this.withdrawMoney = withdrawMoney;
	}

	public float getMoneyBeforeWithdraw() {
		return moneyBeforeWithdraw;
	}

	public void setMoneyBeforeWithdraw(float moneyBeforeWithdraw) {
		this.moneyBeforeWithdraw = moneyBeforeWithdraw;
	}

	public float getMoneyAfterWithdraw() {
		return moneyAfterWithdraw;
	}

	public void setMoneyAfterWithdraw(float moneyAfterWithdraw) {
		this.moneyAfterWithdraw = moneyAfterWithdraw;
	}

	public float getArriveMoney() {
		return arriveMoney;
	}

	public void setArriveMoney(float arriveMoney) {
		this.arriveMoney = arriveMoney;
	}

	public String getInOrderId() {
		return inOrderId;
	}

	public void setInOrderId(String inOrderId) {
		this.inOrderId = inOrderId;
	}

	public int getWithdrawId() {
		return withdrawId;
	}

	public void setWithdrawId(int withdrawId) {
		this.withdrawId = withdrawId;
	}

	public String getCommissionChargeType() {
		return commissionChargeType;
	}

	public void setCommissionChargeType(String commissionChargeType) {
		this.commissionChargeType = commissionChargeType;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getParentTransactionId() {
		return parentTransactionId;
	}

	public void setParentTransactionId(String parentTransactionId) {
		this.parentTransactionId = parentTransactionId;
	}

	public int getTotalRequest() {
		return totalRequest;
	}

	public void setTotalRequest(int totalRequest) {
		this.totalRequest = totalRequest;
	}

	public int getSuccessRequest() {
		return successRequest;
	}

	public void setSuccessRequest(int successRequest) {
		this.successRequest = successRequest;
	}

	public int getFailRequest() {
		return failRequest;
	}

	public void setFailRequest(int failRequest) {
		this.failRequest = failRequest;
	}

	public int getWithdrawMethodId() {
		return withdrawMethodId;
	}

	public void setWithdrawMethodId(int withdrawMethodId) {
		this.withdrawMethodId = withdrawMethodId;
	}

	public WithdrawMethod getWithdrawMethod() {
		return withdrawMethod;
	}

	public void setWithdrawMethod(WithdrawMethod withdrawMethod) {
		this.withdrawMethod = withdrawMethod;
	}

	public boolean isBatchMode() {
		return batchMode;
	}

	public void setBatchMode(boolean batchMode) {
		this.batchMode = batchMode;
	}

	public int getBillingId() {
		return billingId;
	}

	public void setBillingId(int billingId) {
		this.billingId = billingId;
	}

	public float getSuccessMoney() {
		return successMoney;
	}

	public void setSuccessMoney(float successMoney) {
		this.successMoney = successMoney;
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

	public String getTradeSource() {
		return tradeSource;
	}

	public void setTradeSource(String tradeSource) {
		this.tradeSource = tradeSource;
	}

	public String getWithdrawMode() {
		return withdrawMode;
	}

	public void setWithdrawMode(String withdrawMode) {
		this.withdrawMode = withdrawMode;
	}

	public String getPayeeType() {
		return payeeType;
	}

	public void setPayeeType(String payeeType) {
		this.payeeType = payeeType;
	}
}
