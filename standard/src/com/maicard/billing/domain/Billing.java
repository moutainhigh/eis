package com.maicard.billing.domain;

import java.util.Date;
import com.maicard.common.domain.EVEisObject;
import com.maicard.common.util.StringTools;


public class Billing extends EVEisObject implements Cloneable{

	private static final long serialVersionUID = -1840337619819376913L;
	/**
	 * 主键
	 */
	private int billingId;
	/**
	 * 结算开始时间
	 */
	private Date billingBeginTime;
	/**
	 * 结算结束时间
	 */
	private Date billingEndTime;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 结算处理时间
	 */
	private Date billingHandlerTime;
	/**
	 * 结算账户UUID-商户号
	 */
	private Long uuid;
	/**
	 * 结算面额
	 */
	private float faceMoney;
	/**
	 *佣金，手续费
	 */
	private float commission;

	/**
	 * 该结算单对应的分成配置
	 */
	private int shareConfigId;
	
	/**
	 * 用于唯一确定某个时间段内的结算单，防止重复计算
	 */
	private String billingKey;
	
	
	/**
	 * 应结算金额
	 */
	private float realMoney;
	
	/**
	 * 实际到账金额
	 */
	private float arriveMoney;	
	
	/**
	 * 该结算周期的期初余额，就是结算前，该账户中应该有的未结算资金
	 */
	private float beginBalance;
	
	public float getBeginBalance() {
		return beginBalance;
	}

	public void setBeginBalance(float beginBalance) {
		this.beginBalance = beginBalance;
	}

	private long objectId;		//结算对应的业务ID

	
	
	/**
	 * 谁操作
	 */
	private long operator;
	/**
	 * 是否清算处理
	 */
	private boolean isClearDeal = Boolean.TRUE;
	/**
	 * 结算方式
	 */
	private String clearWay;
	/**
	 * 清算状态
	 */
	private String clearStatus;
	/**
	 * 结算状态
	 */
	private String stateStatus;
	/**
	 * 清算类型
	 */
	private String clearType;

	public Billing(){}

	public Billing(long ownerId) {
		this.ownerId = ownerId;
		this.createTime = new Date();
	}
	
	@Override
	public String toString() {
		return new StringBuffer().append(getClass().getName()).append('@').append(Integer.toHexString(hashCode())).append('(').append("billingId=").append(billingId).append(",billingBeginTime=").append(StringTools.getFormattedTime(billingBeginTime)).append("=>").append(StringTools.getFormattedTime(billingEndTime)).append(",uuid=").append(uuid).append(",faceMoney=").append(faceMoney).append(",realMoney").append(realMoney).append(",currentStatus=").append(currentStatus).append(")").toString();
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
	public Long getUuid() {
		return uuid == null ? 0L: uuid;
	}
	public void setUuid(Long uuid) {
		this.uuid = uuid;
	}
	public float getFaceMoney() {
		return faceMoney;
	}
	public void setFaceMoney(float faceMoney) {
		this.faceMoney = faceMoney;
	}
	public float getCommission() {
		return commission;
	}
	public void setCommission(float commission) {
		this.commission = commission;
	}
	public float getRealMoney() {
		return realMoney;
	}
	public void setRealMoney(float realMoney) {
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
	@Override
	public Billing clone() {
		try{
			return (Billing)super.clone();
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	
	public long getOperator() {
		return operator;
	}

	public void setOperator(long operator) {
		this.operator = operator;
	}

	public float getArriveMoney() {
		return arriveMoney;
	}

	public void setArriveMoney(float arriveMoney) {
		this.arriveMoney = arriveMoney;
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

	public boolean isClearDeal() {
		return isClearDeal;
	}

	public void setClearDeal(boolean clearDeal) {
		isClearDeal = clearDeal;
	}

	public String getClearWay() {
		return clearWay;
	}

	public void setClearWay(String clearWay) {
		this.clearWay = clearWay;
	}

	public String getClearStatus() {
		return clearStatus;
	}

	public void setClearStatus(String clearStatus) {
		this.clearStatus = clearStatus;
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
