package com.maicard.common.criteria;

import com.maicard.common.base.Criteria;

public class ThemeCriteria extends Criteria {

	private static final long serialVersionUID = 1L;

	private long themeId;

	
	private String themeCode;	


	public ThemeCriteria() {
	}
	
	public ThemeCriteria(long ownerId) {
		this.ownerId = ownerId;
	}


	public long getThemeId() {
		return themeId;
	}


	public void setThemeId(long themeId) {
		this.themeId = themeId;
	}


	public String getThemeCode() {
		return themeCode;
	}


	public void setThemeCode(String themeCode) {
		this.themeCode = themeCode;
	}


}
