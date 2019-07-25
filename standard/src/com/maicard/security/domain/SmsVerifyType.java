package com.maicard.security.domain;

public class SmsVerifyType {
	
	private String smsVerifyTypeName;
	
	//是否可以未登录提交
	private boolean anonymousAccess = false;
	
	private String tokenName;
	
	public SmsVerifyType(){
		
	}
	
	public SmsVerifyType(String smsVerifyTypeName, boolean anonymousAccess, String tokenName){
		this.smsVerifyTypeName = smsVerifyTypeName;
		this.anonymousAccess = anonymousAccess;
		this.tokenName = tokenName;
	}

	public String getSmsVerifyTypeName() {
		return smsVerifyTypeName;
	}

	public void setSmsVerifyTypeName(String smsVerifyTypeName) {
		this.smsVerifyTypeName = smsVerifyTypeName;
	}

	public boolean isAnonymousAccess() {
		return anonymousAccess;
	}

	public void setAnonymousAccess(boolean anonymousAccess) {
		this.anonymousAccess = anonymousAccess;
	}

	public String getTokenName() {
		return tokenName;
	}

	public void setTokenName(String tokenName) {
		this.tokenName = tokenName;
	}
	
	

}
