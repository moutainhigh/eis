package com.maicard.common.criteria;

import com.maicard.common.base.Criteria;
import com.maicard.standard.CommonStandard;

public class AreaCriteria extends Criteria {


	private static final long serialVersionUID = -2582638475830052512L;
	
	public static final String CACHE_NAME = CommonStandard.cacheNameDocument;

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

}
