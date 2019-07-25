package com.maicard.money.domain;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.maicard.common.domain.EVEisObject;
import com.maicard.views.JsonFilterView;

/**
 * 退款
 *
 *
 * @author NetSnake
 * @date 2016年8月4日
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Refund extends EVEisObject implements Cloneable{

	private static final long serialVersionUID = 1L;
	
	private long refundId;		//主键
	
	private long uuid;

	private String transactionId;
	
	private String refPayTransactionId;
	
	private String memory;

	
	private Date startTime;

	private Date endTime;

	
	
	private String inOrderId;

    @JsonView({JsonFilterView.Partner.class})
	private String outOrderId;


	

	private float money;
	
    @JsonView({JsonFilterView.Partner.class})
	private String notifyUrl;
	
    @JsonView({JsonFilterView.Partner.class})
	private String returnUrl;

	public Refund() {
	}
	
	
	public Refund(long ownerId) {
		this.ownerId = ownerId;
	}
	
	
	public Refund clone(){
		try{
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(byteOut);
			out.writeObject(this);

			ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
			ObjectInputStream in =new ObjectInputStream(byteIn);

			return (Refund)in.readObject();

		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Refund other = (Refund) obj;
		if (transactionId != other.transactionId)
			return false;

		return true;
	}
	
	@Override
	public String toString() {
		return new StringBuffer().append(getClass().getName()).append('@').append(Integer.toHexString(hashCode())).append('(').append("transactionId=").append("'").append(transactionId).append("',").append("uuid=").append("'").append(uuid).append(",").append("currentStatus=").append("'").append(currentStatus).append("'").append(")").toString();
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



	public String getOutOrderId() {
		return outOrderId;
	}



	public void setOutOrderId(String outOrderId) {
		this.outOrderId = outOrderId;
	}




	public String getTransactionId() {
		return transactionId;
	}



	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
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


	public String getSign() {
		return sign;
	}


	public void setSign(String sign) {
		if(sign != null && !sign.trim().equals(""))
			this.sign = sign.trim();
	}



	public String getInOrderId() {
		return inOrderId;
	}


	public void setInOrderId(String inOrderId) {
		this.inOrderId = inOrderId;
	}


	public long getUuid() {
		return uuid;
	}


	public void setUuid(long uuid) {
		this.uuid = uuid;
	}


	public String getRefPayTransactionId() {
		return refPayTransactionId;
	}


	public void setRefPayTransactionId(String refPayTransactionId) {
		this.refPayTransactionId = refPayTransactionId;
	}


	public String getMemory() {
		return memory;
	}


	public void setMemory(String memory) {
		this.memory = memory;
	}




	public float getMoney() {
		return money;
	}


	public void setMoney(float money) {
		this.money = money;
	}


	public long getRefundId() {
		return refundId;
	}

	public void setRefundId(long refundId) {
		this.refundId = refundId;
	}
	
}
