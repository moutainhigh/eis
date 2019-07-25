package com.maicard.money.criteria;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.maicard.annotation.QueryCondition;

public class CouponCriteria extends CouponModelCriteria implements Cloneable{

	private static final long serialVersionUID = 919072647141694697L;

	@QueryCondition
	private String username;

	private String transactionId;

	private long couponModelId;


	private int fetchCount;

	private int numberLength;

	private String couponSerialNumber;		//优惠券序列号

	private String couponPassword;		//优惠券密码

	private long activityId;

	private boolean fetchNewCoupon;
	


	public CouponCriteria() {
	}

	public CouponCriteria(long ownerId) {
		this.ownerId = ownerId;
	}

	@Override
	public CouponCriteria clone() {
		try{
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(byteOut);
			out.writeObject(this);

			ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
			ObjectInputStream in =new ObjectInputStream(byteIn);

			return (CouponCriteria)in.readObject();

		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
				"(" + 
				"couponModelId=" + "'" + couponModelId + "'," + 
				"couponCode=" + "'" + couponCode + "'," + 
				"uuid=" + "'" + uuid + "'," + 
				"fetchNewCoupon=" + "'" + fetchNewCoupon + "'," + 
				"transactionId=" + "'" + transactionId + "'," + 
				"currentStatus=" + "'" + currentStatus + "'," + 
				"ownerId=" + "'" + ownerId + "'" + 
				")";
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public long getCouponModelId() {
		return couponModelId;
	}

	public void setCouponModelId(long couponModelId) {
		this.couponModelId = couponModelId;
	}


	public int getFetchCount() {
		return fetchCount;
	}

	public void setFetchCount(int fetchCount) {
		this.fetchCount = fetchCount;
	}

	public int getNumberLength() {
		return numberLength;
	}

	public void setNumberLength(int numberLength) {
		this.numberLength = numberLength;
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

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public long getActivityId() {
		return activityId;
	}

	public void setActivityId(long activityId) {
		this.activityId = activityId;
	}

	public boolean isFetchNewCoupon() {
		return fetchNewCoupon;
	}

	public void setFetchNewCoupon(boolean fetchNewCoupon) {
		this.fetchNewCoupon = fetchNewCoupon;
	}



}
