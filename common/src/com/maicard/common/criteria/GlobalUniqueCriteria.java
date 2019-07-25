package com.maicard.common.criteria;

import com.maicard.common.base.Criteria;

public class GlobalUniqueCriteria extends Criteria {

	private static final long serialVersionUID = 1L;
	
	public static final String CACHE_PREFIX = "GlobalUnique";

	
	private String data;

	public GlobalUniqueCriteria() {
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

}
