package com.maicard.money.domain;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.maicard.common.domain.EVEisObject;
import com.maicard.views.JsonFilterView;

//import org.codehaus.jackson.annotate.JsonIgnoreProperties;
//@JsonIgnoreProperties({"payTypeId","payMethodId","payToAccount","payFromAccount","moneyTypeId","cardPassword"})

@JsonIgnoreProperties(ignoreUnknown = true)
public class Pay extends EVEisObject{

	private static final long serialVersionUID = 1L;

	private String transactionId;

	private int payTypeId;

	private int payMethodId;
	
	private String payCardType;
	
	 /**
     * 交易类型 退款 或者支付
     */
    private String tradeType;
	
	/**
	 * 请求支付时对应的支付方式的实例，仅用于在后续处理时临时存放该支付方式的一些配置，不保存
	 */
    @JsonView({JsonFilterView.Partner.class})
	private PayMethod payMethod;
	
	private String name;
	
	private String description;

	private long payToAccount;
	
	private int payToAccountType;

	private long payFromAccount;
	
	private int payFromAccountType;

	private int moneyTypeId;

	private Date startTime;

	private Date endTime;

	private String refBuyTransactionId;

	private String parentPayOrderId;
	
	private String inOrderId;

    @JsonView({JsonFilterView.Partner.class})
	private String outOrderId;


	private int flag;

	private float realMoney;

	private float faceMoney;

	private String cardSerialNumber;

	private String cardPassword;
	
    @JsonView({JsonFilterView.Partner.class})
    private int billingId;

    @JsonView({JsonFilterView.Boss.class})
	private float rate;
	
    @JsonView({JsonFilterView.Partner.class})
	private String notifyUrl;
	
    @JsonView({JsonFilterView.Partner.class})
	private String returnUrl;
	
	private String inJumpUrl;	//向支付发起方跳转的URL
	
	private String inNotifyUrl;	//向支付发起方发送通知的URL
	
	
	private long inviter;		//邀请者，或者是属于该订单属于哪个合作伙伴
	
	
	
	
	
	private int lockStatus; //锁定更新时，指定更新前的状态，如果不更新前不是这个状态，则更新失败
	
	private String payResultMessage; //用于给用户或接口返回的数据
	
	private Object parameter;		//用户端可能提交的参数
	
	
	private boolean createOrder = true;		//是否创建支付订单
	
	private String contextType;			//应用环境，微信、手机浏览器、PC或应用程序，@see ContextType
	
	/**
	 * 该笔付款扣除的手续费
	 */
	private float commission;
	
	/**
	 * 付款后账户余额，同时也用来判断一个订单是否已经被结算过，每个订单不能多次结算
	 */
	private double balance;
	
	public Pay() {
		this.startTime = new Date();
	}
	public Pay(long ownerId) {
		this.ownerId = ownerId;
		this.startTime = new Date();
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Pay other = (Pay) obj;
		if (transactionId != other.transactionId)
			return false;

		return true;
	}
	
	@Override
	public Pay clone() {
		try{
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(byteOut);
			out.writeObject(this);

			ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
			ObjectInputStream in =new ObjectInputStream(byteIn);

			return (Pay)in.readObject();

		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public String toString() {
		return new StringBuffer().append(getClass().getName()).append('@').append(Integer.toHexString(hashCode())).append('(').append("transactionId=").append("'").append(transactionId).append("',").append("payTypeId=").append("'").append(payTypeId).append("',").append("account=").append("'").append(payFromAccount).append("=>").append(payToAccount).append("',").append("currentStatus=").append("'").append(currentStatus).append("'").append(")").toString();
	}



	public int getPayTypeId() {
		return payTypeId;
	}



	public void setPayTypeId(int payTypeId) {
		this.payTypeId = payTypeId;
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



	public long getPayFromAccount() {
		return payFromAccount;
	}



	public void setPayFromAccount(long fromAccount) {
		this.payFromAccount = fromAccount;
	}



	public int getMoneyTypeId() {
		return moneyTypeId;
	}



	public void setMoneyTypeId(int moneyTypeId) {
		this.moneyTypeId = moneyTypeId;
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

	public String getParentPayOrderId() {
		return parentPayOrderId;
	}



	public void setParentPayOrderId(String parentPayOrderId) {
		this.parentPayOrderId = parentPayOrderId;
	}



	public String getOutOrderId() {
		return outOrderId;
	}



	public void setOutOrderId(String outOrderId) {
		this.outOrderId = outOrderId;
	}



	public int getFlag() {
		return flag;
	}



	public void setFlag(int flag) {
		this.flag = flag;
	}



	public float getRealMoney() {
		return realMoney;
	}



	public void setRealMoney(float realMoney) {
		this.realMoney = realMoney;
	}

	public String getCardSerialNumber() {
		return cardSerialNumber;
	}



	public void setCardSerialNumber(String cardSerialNumber) {
		this.cardSerialNumber = cardSerialNumber;
	}



	public String getCardPassword() {
		return cardPassword;
	}



	public void setCardPassword(String cardPassword) {
		this.cardPassword = cardPassword;
	}




	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}



	public float getRate() {
		return rate;
	}



	public void setRate(float rate) {
		this.rate = rate;
	}



	public String getTransactionId() {
		return transactionId;
	}



	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}



	public int getLockStatus() {
		return lockStatus;
	}



	public void setLockStatus(int lockStatus) {
		this.lockStatus = lockStatus;
	}


	public String getPayResultMessage() {
		return payResultMessage;
	}

	public void setPayResultMessage(String payResultMessage) {
		this.payResultMessage = payResultMessage;
	}


	public float getFaceMoney() {
		return faceMoney;
	}


	public void setFaceMoney(float faceMoney) {
		this.faceMoney = faceMoney;
	}

	public String getRefBuyTransactionId() {
		return refBuyTransactionId;
	}


	public void setRefBuyTransactionId(String refBuyTransactionId) {
		this.refBuyTransactionId = refBuyTransactionId;
	}


	public String getNotifyUrl() {
		return notifyUrl;
	}


	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}


	public String getReturnUrl() {
		return returnUrl;
	}


	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}


	public String getInOrderId() {
		return inOrderId;
	}


	public void setInOrderId(String inOrderId) {
		this.inOrderId = inOrderId;
	}


	public String getInJumpUrl() {
		return inJumpUrl;
	}


	public void setInJumpUrl(String inJumpUrl) {
		this.inJumpUrl = inJumpUrl;
	}


	public String getInNotifyUrl() {
		return inNotifyUrl;
	}


	public void setInNotifyUrl(String inNotifyUrl) {
		this.inNotifyUrl = inNotifyUrl;
	}


	public boolean isCreateOrder() {
		return createOrder;
	}


	public void setCreateOrder(boolean createOrder) {
		this.createOrder = createOrder;
	}


	public String getContextType() {
		return contextType;
	}


	public void setContextType(String contextType) {
		this.contextType = contextType;
	}


	public long getInviter() {
		return inviter;
	}


	public void setInviter(long inviter) {
		this.inviter = inviter;
	}


	public int getPayToAccountType() {
		return payToAccountType;
	}


	public void setPayToAccountType(int payToAccountType) {
		this.payToAccountType = payToAccountType;
	}


	public int getPayFromAccountType() {
		return payFromAccountType;
	}


	public void setPayFromAccountType(int payFromAccountType) {
		this.payFromAccountType = payFromAccountType;
	}
	public Object getParameter() {
		return parameter;
	}
	public void setParameter(Object parameter) {
		this.parameter = parameter;
	}
	public int getBillingId() {
		return billingId;
	}
	public void setBillingId(int billingId) {
		this.billingId = billingId;
	}
	public PayMethod getPayMethod() {
		return payMethod;
	}
	public void setPayMethod(PayMethod payMethod) {
		this.payMethod = payMethod;
	}
	public float getCommission() {
		return commission;
	}
	public void setCommission(float commission) {
		this.commission = commission;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
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
