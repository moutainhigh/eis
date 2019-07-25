package com.maicard.common.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 省市县信息
 *
 *
 * @author NetSnake
 * @date 2016年7月5日
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Area extends EisObject{
	
	private static final long serialVersionUID = 7414935877861359072L;

	private long areaId;
	
	private String province;
	
	private String city;
	
	private String county;

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

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public long getAreaId() {
		return areaId;
	}

	public void setAreaId(long areaId) {
		this.areaId = areaId;
	}
	
	

}
