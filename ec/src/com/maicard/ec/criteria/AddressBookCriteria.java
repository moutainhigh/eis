package com.maicard.ec.criteria;

import java.util.Date;

import com.maicard.annotation.QueryCondition;
import com.maicard.common.base.Criteria;


public class AddressBookCriteria extends Criteria {

	private static final long serialVersionUID = 4277529726418440098L;

	private String addressBookId;
	
	@QueryCondition
	private String identify;

	protected long[] uuid;	
	
	@QueryCondition
	private String username;

	protected String country;

	protected String province;
	
	protected String city;

	protected String district;

	protected String address;
	
	protected String mobile;
	
	protected String contact;

	private String tags;

	private boolean defaultAddress;

	private Date createTime;

	private Date lastUseTime;
	
	public AddressBookCriteria(){
		
	}
	
	public AddressBookCriteria(long ownerId){
		this.ownerId  = ownerId;
	}
	public long[] getUuid() {
		return uuid;
	}

	public void setUuid(long... uuid) {
		this.uuid = uuid;
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

	public void setProvince(String province) {
		if(province == null || province.trim().equals("")){
			return;
		}	
		this.province = province;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		if(district == null || district.trim().equals("")){
			return;
		}	
		this.district = district;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public boolean isDefaultAddress() {
		return defaultAddress;
	}

	public void setDefaultAddress(boolean defaultAddress) {
		this.defaultAddress = defaultAddress;
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

	public String getAddress() {
		return address;
	}

	public String getAddressBookId() {
		return addressBookId;
	}

	public void setAddressBookId(String addressBookId) {
		this.addressBookId = addressBookId;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		if(username == null || username.trim().equals("")){
			return;
		}
		this.username = username;
	}

	public String getIdentify() {
		return identify;
	}

	public void setIdentify(String identify) {
		if(identify == null || identify.trim().equals("")){
			return;
		}
		this.identify = identify;
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

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		if(mobile == null || mobile.trim().equals("")){
			return;
		}
		this.mobile = mobile;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		if(contact == null || contact.trim().equals("")){
			return;
		}		this.contact = contact;
	}

}
