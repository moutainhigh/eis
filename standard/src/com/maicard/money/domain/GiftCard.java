package com.maicard.money.domain;

import java.util.Date;

import com.maicard.common.domain.EisObject;

/**
 * 礼品卡、兑换券、库存卡
 * 
 * 
 * @author NetSnake
 *
 */
public class GiftCard extends EisObject {

	private static final long serialVersionUID = -5946825968415363670L;

	private int giftCardId; 		//主键

	private String cardNumber;		//卡号

	private String cardPassword;	//密码

	private String objectType;		//对应的类型，如业务business、产品product

	private Integer objectId;		//对应类型的ID

	private Integer objectExtraId;	//对应类型的扩展ID

	private Date createTime;		//创建时间

	private Date validTime;		//有效期

	private Date usedTime;			//使用时间

	private long usedByUuid;	//被谁使用

	private Integer newStatus;		//锁定时的新状态，替换当前状态

	private Integer flag;

	private float	labelMoney;			//礼品卡对应的面额
	
	private float requestMoney;		//卡余额
	
	private float frozenMoney;			//卡的冻结金额
	
	private float successMoney;			//卡的已使用金额
	
	private int moneyTypeId;		//礼品卡对应的资金类型 @see MoneyType
	
	private String lockGlobalUniqueId;	//锁定时的全局锁定ID
	
	private String processClass;


	public GiftCard() {
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		if(cardNumber != null && !cardNumber.trim().equals("")){
			this.cardNumber = cardNumber.trim();
		}
	}

	public String getCardPassword() {
		return cardPassword;
	}

	public void setCardPassword(String cardPassword) {
		if(cardPassword != null && !cardPassword.trim().equals("")){
			this.cardPassword = cardPassword.trim();
		}	}

	public Integer getObjectId() {
		return objectId == null ? 0 : objectId;
	}

	public void setObjectId(Integer objectId) {
		this.objectId = objectId;
	}

	public Integer getObjectExtraId() {
		return objectExtraId == null ? 0 : objectExtraId;
	}

	public void setObjectExtraId(Integer objectExtraId) {
		this.objectExtraId = objectExtraId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getValidTime() {
		return validTime;
	}

	public void setValidTime(Date validTime) {
		this.validTime = validTime;
	}

	public Date getUsedTime() {
		return usedTime;
	}

	public void setUsedTime(Date usedTime) {
		this.usedTime = usedTime;
	}

	public long getUsedByUuid() {
		return usedByUuid;
	}

	public void setUsedByUuid(long usedByUuid) {
		this.usedByUuid = usedByUuid;
	}


	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}



	public Integer getNewStatus() {
		return newStatus;
	}

	public void setNewStatus(Integer newStatus) {
		this.newStatus = newStatus;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}


	public int getGiftCardId() {
		return giftCardId;
	}

	public void setGiftCardId(int giftCardId) {
		this.giftCardId = giftCardId;
	}

	public String getLockGlobalUniqueId() {
		return lockGlobalUniqueId;
	}

	public void setLockGlobalUniqueId(String lockGlobalUniqueId) {
		this.lockGlobalUniqueId = lockGlobalUniqueId;
	}

	public int getMoneyTypeId() {
		return moneyTypeId;
	}

	public void setMoneyTypeId(int moneyTypeId) {
		this.moneyTypeId = moneyTypeId;
	}

	public String getProcessClass() {
		return processClass;
	}

	public void setProcessClass(String processClass) {
		this.processClass = processClass;
	}

	public float getLabelMoney() {
		return labelMoney;
	}

	public void setLabelMoney(float labelMoney) {
		this.labelMoney = labelMoney;
	}

	public float getRequestMoney() {
		return requestMoney;
	}

	public void setRequestMoney(float requestMoney) {
		this.requestMoney = requestMoney;
	}

	public float getFrozenMoney() {
		return frozenMoney;
	}

	public void setFrozenMoney(float frozenMoney) {
		this.frozenMoney = frozenMoney;
	}

	public float getSuccessMoney() {
		return successMoney;
	}

	public void setSuccessMoney(float successMoney) {
		this.successMoney = successMoney;
	}

}
