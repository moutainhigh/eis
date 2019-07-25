package com.maicard.site.criteria;

import com.maicard.common.base.Criteria;

public class TagStatCriteria extends Criteria {

	private static final long serialVersionUID = 1L;
	
	private int tagStatId;

	public TagStatCriteria() {
	}

	public int getTagStatId() {
		return tagStatId;
	}

	public void setTagStatId(int tagStatId) {
		this.tagStatId = tagStatId;
	}

}
