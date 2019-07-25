package com.maicard.money.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.maicard.annotation.NeedJmsDataSyncP2P;
import com.maicard.common.domain.EisObject;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@NeedJmsDataSyncP2P
public class BankAccount extends EisObject {

	private static final long serialVersionUID = -4384275117625705324L;

	private int bankAccountId;

	private String bankAccountCode;
	
	/**
	 * //对公还是对私，合法输入@see BankAccountCriteria
	 */
	private String bankAccountType;		

	private long uuid;	
	
	/**
	 * 使用该帐号的用户类型，是商户还是个人用户
	 */
	private int userTypeId;
	
	private String country;
	
	private String province;
	
	private String city;
	/**
	 * 银行编码
	 */
	private String bankCode;
	/**
	 * 银行名称
	 */
	private String bankName;

	private String subsidiaryCode;
	/**
	 * 开户行
	 */
	private String issueBank;
	/**
	 * 账号
	 */
	private String bankAccountNumber;
	/**
	 * 开户名
	 */
	private String bankAccountName;
	/**
	 * 银行卡类型
	 */
	private String bankCardType;
	/**
	 * 证件号
	 */
	private String encryptIdNum;
	/**
	 * 加密手机号
	 */
	private String encryptMobileNo;

	private Date createTime;

	private Date lastUseTime;
	
	private int useCount;
	
	private int withdrawMethodId;
	
	/**
	 * 认证相关的文件附件
	 */
	private String certifyFile;
	
	
	public String getCertifyFile() {
		return certifyFile;
	}

	public void setCertifyFile(String certifyFile) {
		this.certifyFile = certifyFile;
	}

	@Override
	public BankAccount clone() {
		return (BankAccount)super.clone();
	}
	
	public BankAccount() {
	}

	public BankAccount(long ownerId) {
		this.ownerId = ownerId;
	}

	public long getUuid() {
		return uuid;
	}

	public void setUuid(long uuid) {
		this.uuid = uuid;
	}

	

	@Override
	public String toString(){
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
				"(" + 
				"id=" + "'" + bankAccountId + "'" + 
				"uuid=" + "'" + uuid + "'" + 
				"bankAccountType=" + "'" + bankAccountType + "'" + 
				"bankName=" + "'" + bankName + "'" + 
				"issueBank=" + "'" + issueBank + "'" + 
				"bankAccountNumber=" + "'" + bankAccountNumber + "'" + 
				"bankAccountName=" + "'" + bankAccountName + "'" + 
				")";
	}



	



	public String getCountry() {
		return country;
	}



	public void setCountry(String country) {
		this.country = country;
	}



	public String getProvince() {
		return province;
	}



	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setProvince(String province) {
		this.province = province;
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

	public int getBankAccountId() {
		return bankAccountId;
	}

	public void setBankAccountId(int bankAccountId) {
		this.bankAccountId = bankAccountId;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getIssueBank() {
		return issueBank;
	}

	public void setIssueBank(String issueBank) {
		this.issueBank = issueBank;
	}

	public String getBankAccountNumber() {
		return bankAccountNumber;
	}

	public void setBankAccountNumber(String bankAccountNumber) {
		this.bankAccountNumber = bankAccountNumber;
	}

	public String getBankAccountName() {
		return bankAccountName;
	}

	public void setBankAccountName(String bankAccountName) {
		this.bankAccountName = bankAccountName;
	}

	public String getBankCardType() {
		return bankCardType;
	}

	public void setBankCardType(String bankCardType) {
		this.bankCardType = bankCardType;
	}

	public String getBankAccountType() {
		return bankAccountType;
	}

	public void setBankAccountType(String bankAccountType) {
		this.bankAccountType = bankAccountType;
	}

	public int getWithdrawMethodId() {
		return withdrawMethodId;
	}

	public void setWithdrawMethodId(int withdrawMethodId) {
		this.withdrawMethodId = withdrawMethodId;
	}

	public int getUserTypeId() {
		return userTypeId;
	}

	public void setUserTypeId(int userTypeId) {
		this.userTypeId = userTypeId;
	}

	public String getBankAccountCode() {
		return bankAccountCode;
	}

	public void setBankAccountCode(String bankAccountCode) {
		this.bankAccountCode = bankAccountCode;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getSubsidiaryCode() {
		return subsidiaryCode;
	}

	public void setSubsidiaryCode(String subsidiaryCode) {
		this.subsidiaryCode = subsidiaryCode;
	}

	public String getEncryptIdNum() {
		return encryptIdNum;
	}

	public void setEncryptIdNum(String encryptIdNum) {
		this.encryptIdNum = encryptIdNum;
	}

	public String getEncryptMobileNo() {
		return encryptMobileNo;
	}

	public void setEncryptMobileNo(String encryptMobileNo) {
		this.encryptMobileNo = encryptMobileNo;
	}
}
