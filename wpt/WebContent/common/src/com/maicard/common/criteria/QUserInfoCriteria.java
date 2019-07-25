package com.maicard.common.criteria;

import com.maicard.common.base.Criteria;

public class QUserInfoCriteria extends Criteria {

	private static final long serialVersionUID = 1L;
	
	private int di;
	private String qqNumber;
	private String ip;
	private String country;
	private String province;
	private String city;
	private int level;
	
	public int getDi() {
		return di;
	}
	public void setDi(int di) {
		this.di = di;
	}
	public String getQqNumber() {
		return qqNumber;
	}
	public void setQqNumber(String qqNumber) {
		this.qqNumber = qqNumber;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
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
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
}
