package com.maicard.common.domain;

public class IpPolicy extends EisObject {
	
	public static final int BLACK_LIST =1;
	public static final int WHITE_LIST = 2;
	
	private static final long serialVersionUID = -3891789856372762609L;
	private  int ipPolicyId;
	private int ipPolicyType;
	private String ipPolicyReg;
	private String objectType;
	private long objectId;
	
	public int getIpPolicyId() {
		return ipPolicyId;
	}
	public void setIpPolicyId(int ipPolicyId) {
		this.ipPolicyId = ipPolicyId;
	}
	public String getIpPolicyReg() {
		return ipPolicyReg;
	}
	public void setIpPolicyReg(String ipPolicyReg) {
		this.ipPolicyReg = ipPolicyReg;
	}
	public int getIpPolicyType() {
		return ipPolicyType;
	}
	public void setIpPolicyType(int ipPolicyType) {
		this.ipPolicyType = ipPolicyType;
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

	

}


