package com.maicard.ec.domain;

import java.util.Date;

import com.maicard.annotation.NeedJmsDataSyncP2P;
import com.maicard.common.domain.EVEisObject;


@NeedJmsDataSyncP2P
public class AddressBook extends EVEisObject {

	private static final long serialVersionUID = -4384275117625705324L;

	private long addressBookId;

	protected long uuid;	
	
	private String username;
	
	protected String country;
	
	protected String province;
	
	protected String city;
	
	protected String district;
	
	protected String address;
	
	protected String contact;
	
	protected String phone;
	
	protected String mobile;
	
	protected String postcode;
	
	private String tags;
	

	private Date createTime;

	private Date lastUseTime;
	
	private int useCount;
	
	private String identify;
	
	public AddressBook() {
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
				"uuid=" + "'" + uuid + "'," + 
				"addressBookId=" + "'" + addressBookId + "'," + 
				"currentStatus=" + "'" + currentStatus + "'" + 
				")";
	}



	public long getAddressBookId() {
		return addressBookId;
	}



	public void setAddressBookId(long addressBookId) {
		this.addressBookId = addressBookId;
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



	public String getDistrict() {
		return district;
	}



	public void setDistrict(String district) {
		this.district = district;
	}



	public String getTags() {
		return tags;
	}



	public void setTags(String tags) {
		this.tags = tags;
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



	public String getAddress() {
		return address;
	}



	public void setAddress(String address) {
		this.address = address;
	}



	public String getContact() {
		return contact;
	}



	public void setContact(String contact) {
		this.contact = contact;
	}



	public String getPhone() {
		return phone;
	}



	public void setPhone(String phone) {
		this.phone = phone;
	}



	public String getMobile() {
		return mobile;
	}



	public void setMobile(String mobile) {
		this.mobile = mobile;
	}



	public String getPostcode() {
		return postcode;
	}



	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getIdentify() {
		return identify;
	}

	public void setIdentify(String identify) {
		this.identify = identify;
	}


}
