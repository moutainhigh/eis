package com.maicard.money.criteria;

import java.util.Date;

import com.maicard.common.base.InviterSupportCriteria;

public class BankAccountCriteria extends InviterSupportCriteria {

	private static final long serialVersionUID = 4277529726418440098L;
	
	/**
	 * 对公账户
	 */
	public static final String BANK_ACCOUNT_TYPE_COMPANY = "COMPANY";
	
	/**
	 * 对私账户
	 */
	public static final String BAKC_ACCOUNT_TYPE_PERSONAL = "PERSONAL";

	private int bankAccountId;

	private long uuid;	
	
	/**
	 * 使用该帐号的用户类型，是商户还是个人用户
	 */
	private int userTypeId;
	
	private String country;
	
	private String province;
	
	private String city;
	
	private String bankName;	//银行
	
	private String issueBank;	//开户行
	
	private String bankAccountNumber;	//账号
	
	private String bankAccountName;	//开户名
		

	private Date createTime;

	private Date lastUseTime;
	
	private int useCount;

	private String bankAccountType;
	
	public BankAccountCriteria(){}
	
	public BankAccountCriteria(long ownerId){
		this.ownerId = ownerId;
	}
	
	
	public long getUuid() {
		return uuid;
	}

	public void setUuid(long uuid) {
		this.uuid = uuid;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		if(country == null || country.trim().equals("")){
			return;
		}
		this.country = country;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		if(province == null || province.trim().equals("")){
			return;
		}
		this.province = province;
	}

	public int getBankAccountId() {
		return bankAccountId;
	}

	public void setBankAccountId(int bankAccountId) {
		this.bankAccountId = bankAccountId;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		if(city == null || city.trim().equals("")){
			return;
		}
		this.city = city;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		if(bankName == null || bankName.trim().equals("")){
			return;
		}
		this.bankName = bankName;
	}

	public String getIssueBank() {
		return issueBank;
	}

	public void setIssueBank(String issueBank) {
		if(issueBank == null || issueBank.trim().equals("")){
			return;
		}
		this.issueBank = issueBank;
	}

	public String getBankAccountNumber() {
		return bankAccountNumber;
	}

	public void setBankAccountNumber(String bankAccountNumber) {
		if(bankAccountNumber == null || bankAccountNumber.trim().equals("")){
			return;
		}
		this.bankAccountNumber = bankAccountNumber;
	}

	public String getBankAccountName() {
		return bankAccountName;
	}

	public void setBankAccountName(String bankAccountName) {
		if(bankAccountName == null || bankAccountName.trim().equals("")){
			return;
		}
		this.bankAccountName = bankAccountName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastUseTime() {
		return lastUseTime;
	}

	public void setLastUseTime(Date lastUseTime) {
		this.lastUseTime = lastUseTime;
	}

	public int getUseCount() {
		return useCount;
	}

	public void setUseCount(int useCount) {
		this.useCount = useCount;
	}

	public String getBankAccountType() {
		return bankAccountType;
	}

	public void setBankAccountType(String bankAccountType) {
		if(bankAccountType == null || bankAccountType.trim().equals("")){
			return;
		}
		this.bankAccountType = bankAccountType;
	}

	public int getUserTypeId() {
		return userTypeId;
	}

	public void setUserTypeId(int userTypeId) {
		this.userTypeId = userTypeId;
	}

	
}
