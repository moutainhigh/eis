package com.maicard.common.criteria;

import com.maicard.common.base.Criteria;

public class LanguageCriteria extends Criteria {

	private static final long serialVersionUID = 1L;
	
	private int languageId;
	
	public LanguageCriteria() {
	}

	public int getLanguageId() {
		return languageId;
	}

	public void setLanguageId(int languageId) {
		this.languageId = languageId;
	}


}
