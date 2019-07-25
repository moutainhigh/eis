package com.maicard.common.criteria;

import com.maicard.common.base.Criteria;

public class IpPolicyCriteria extends Criteria implements Cloneable{

	private static final long serialVersionUID = 1L;

	private int ipPolicyId;
	private int ipPolicyType;
	private String ipPolicyReg;
	private String objectType;
	private long objectId;

	private String ipAddress;

	public IpPolicyCriteria() {
	}

	public int getIpPolicyId() {
		return ipPolicyId;
	}

	public void setIpPolicyId(int ipPolicyId) {
		this.ipPolicyId = ipPolicyId;
	}

	public int getIpPolicyType() {
		return ipPolicyType;
	}

	public void setIpPolicyType(int ipPolicyType) {
		this.ipPolicyType = ipPolicyType;
	}

	public String getIpPolicyReg() {
		return ipPolicyReg;
	}

	public void setIpPolicyReg(String ipPolicyReg) {
		this.ipPolicyReg = ipPolicyReg;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public long getObjectId() {
		return objectId;
	}

	public void setObjectId(long objectId) {
		this.objectId = objectId;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	@Override
	public IpPolicyCriteria clone() throws CloneNotSupportedException {
		return (IpPolicyCriteria)super.clone();
	}


}
