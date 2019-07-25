package com.maicard.money.domain;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
//import com.maicard.annotation.NeedJmsDataSync;
import com.maicard.annotation.NeedJmsDataSyncP2P;
import com.maicard.standard.KeyConstants;
import com.maicard.standard.TransactionStandard.TransactionStatus;
import com.maicard.views.JsonFilterView;

@JsonIgnoreProperties(ignoreUnknown = true)
@NeedJmsDataSyncP2P
public class Coupon extends CouponModel implements Cloneable {

	private static final long serialVersionUID = 976395645315534126L;

	public static final int STATUS_NEW = TransactionStatus.newOrder.id;
	public static final int STATUS_LOCKED = TransactionStatus.auctionSuccess.id;
	public static final int STATUS_USED = TransactionStatus.closed.id;

	@JsonView({JsonFilterView.Partner.class})
	private long couponId;			//PK


	private String transactionId;


	private String couponSerialNumber;		//优惠券序列号

	private String couponPassword;		//优惠券密码

	private long uuid;					//谁领取了该优惠券



	private Date fetchTime;

	private Date useTime;
	
		


	@JsonView({JsonFilterView.Full.class})
	private String lockGlobalUniqueId;	//锁定时的全局锁定ID



	public Coupon() {
	}
	public Coupon(long ownerId) {
		this.ownerId = ownerId;
	}

	public Coupon(CouponModel couponModel) {
		this.couponModelId = couponModel.couponModelId;
		this.couponType = couponModel.couponType;
		this.couponCode = couponModel.couponCode;
		this.couponModelName= couponModel.couponModelName;
		this.couponModelDesc = couponModel.couponModelDesc;
		this.content = couponModel.content;
		this.binContent = couponModel.binContent;
		this.costMoney = couponModel.costMoney;
		this.giftMoney = couponModel.giftMoney;
		this.validTimeBegin = couponModel.validTimeBegin;
		this.validTimeEnd = couponModel.validTimeEnd;
		this.imageUrl = couponModel.imageUrl;
		this.ownerId = couponModel.getOwnerId();
		this.processor = couponModel.processor;
		this.extraCode = couponModel.getExtraCode();
	}



	@Override
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
				"(" + 
				"couponId=" + "'" + couponId + "'," + 
				"couponModelId=" + "'" + couponModelId + "'," + 
				"couponSerialNumber=" + "'" + couponSerialNumber + "'," + 
				"couponPassword=" + "'" + couponPassword + "'," + 
				"currentStatus=" + "'" + currentStatus + "'," + 
				"uuid=" + "'" + uuid + "'," + 
				"ownerId=" + "'" + ownerId + "'" + 
				")";
	}

	@Override
	public Coupon clone() {
		try{
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(byteOut);
			out.writeObject(this);

			ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
			ObjectInputStream in =new ObjectInputStream(byteIn);

			return (Coupon)in.readObject();

		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public boolean isAllZero(){
		if(this.giftMoney == null){
			return true;
		}
		if(this.giftMoney.isAllZero()){
			return true;
		}
		return false;
	}

	public long getCouponId() {
		return couponId;
	}

	public void setCouponId(long couponId) {
		this.couponId = couponId;
	}

	public String getCouponSerialNumber() {
		return couponSerialNumber;
	}

	public void setCouponSerialNumber(String couponSerialNumber) {
		this.couponSerialNumber = couponSerialNumber;
	}

	public String getCouponPassword() {
		return couponPassword;
	}

	public void setCouponPassword(String couponPassword) {
		this.couponPassword = couponPassword;
	}

	public long getUuid() {
		return uuid;
	}

	public void setUuid(long uuid) {
		this.uuid = uuid;
	}

	public String getLockGlobalUniqueId() {
		return lockGlobalUniqueId;
	}
	public void setLockGlobalUniqueId(String lockGlobalUniqueId) {
		this.lockGlobalUniqueId = lockGlobalUniqueId;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public Date getFetchTime() {
		return fetchTime;
	}

	public void setFetchTime(Date fetchTime) {
		this.fetchTime = fetchTime;
	}

	public Date getUseTime() {
		return useTime;
	}

	public void setUseTime(Date useTime) {
		this.useTime = useTime;
	}

	//输出赠送资金的简短说明
	public String getGiftMoneyDesc() {
		if(this.giftMoney == null){
			return null;
		}
		return this.giftMoney.getMoneyBrief();
	}
	
	public String getLockKey() {
		return KeyConstants.LOCKED_COUPON_PREFIX + "#" + this.couponId;
	}
}
