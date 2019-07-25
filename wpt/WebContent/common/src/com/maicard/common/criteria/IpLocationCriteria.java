package com.maicard.common.criteria;

import com.maicard.common.base.Criteria;

public class IpLocationCriteria extends Criteria implements Cloneable{

	private static final long serialVersionUID = -2359055493633984847L;

	private String ip;

	private String mask;

	private String province;

	private String city;

	private String isp;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMask() {
		return mask;
	}

	public void setMask(String mask) {
		this.mask = mask;
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

	public String getIsp() {
		return isp;
	}

	public void setIsp(String isp) {
		this.isp = isp;
	}


}
