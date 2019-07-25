package com.maicard.money.domain;

import com.maicard.common.domain.EisObject;

public class PayChannel extends EisObject {

	private static final long serialVersionUID = 1L;

	private int payChannelId;

	private int payTypeId;

	private String name;

	private String description;

	private String contactName;

	private String contactPhone;

	private String processClass;

	private int flag;
	public PayChannel() {
	}

	public int getPayChannelId() {
		return payChannelId;
	}

	public void setPayChannelId(int payChannelId) {
		this.payChannelId = payChannelId;
	}

	public int getPayTypeId() {
		return payTypeId;
	}

	public void setPayTypeId(int payTypeId) {
		this.payTypeId = payTypeId;
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

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getProcessClass() {
		return processClass;
	}

	public void setProcessClass(String processClass) {
		this.processClass = processClass;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + payChannelId;

		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final PayChannel other = (PayChannel) obj;
		if (payChannelId != other.payChannelId)
			return false;

		return true;
	}
	
	@Override
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
			"(" + 
			"payChannelId=" + "'" + payChannelId + "'" + 
			")";
	}
}
