package com.maicard.common.domain;

import java.io.Serializable;

/**
 * IP地址段与地区对应关系
 *
 *
 * @author NetSnake
 * @date 2017年1月31日
 *
 */
public class IpLocation implements Serializable {


	private static final long serialVersionUID = -2341039228098639407L;

	/**
	 * IP地址段，一般是前三部分
	 */
	private String ipRange;

	/**
	 * 子网掩码，暂时未用
	 */
	private String mask;

	/**
	 * 国家
	 */
	private String country;

	/**
	 * 省份
	 */
	private String province;

	/**
	 * 城市
	 */
	private String city;

	/**
	 * 运营商
	 */
	private String isp;
	
	/**
	 * 邮编
	 */
	private String postCode;

	/**
	 * 区号
	 */
	private String areaNumber;

	public String getIpRange() {
		return ipRange;
	}

	public void setIpRange(String ipRange) {
		this.ipRange = ipRange;
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

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getAreaNumber() {
		return areaNumber;
	}

	public void setAreaNumber(String areaNumber) {
		this.areaNumber = areaNumber;
	}

	public String getDesc() {
		StringBuffer sb = new StringBuffer();
		if(this.country != null && !this.country.equals("中国")){
			sb.append(this.country).append(' ');
		}
		if(this.province != null){
			sb.append(this.province);
		}	
		if(this.city != null && this.city != this.province){
			sb.append(this.city);
		}	
		if(this.isp != null){
			sb.append(this.isp);
		}	
		return sb.toString();
	}
	
	public String getSimpleDesc() {
		StringBuffer sb = new StringBuffer();
		if(this.province != null){
			sb.append(this.province);
		}	
		if(this.city != null && this.city != this.province){
			sb.append(this.city);
		}	
		return sb.toString();
	}


}
