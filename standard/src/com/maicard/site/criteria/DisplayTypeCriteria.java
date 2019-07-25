package com.maicard.site.criteria;

import com.maicard.common.base.Criteria;

public class DisplayTypeCriteria extends Criteria {

	private static final long serialVersionUID = 1L;
	
	private int displayTypeId;

	public DisplayTypeCriteria() {
	}

	public int getDisplayTypeId() {
		return displayTypeId;
	}

	public void setDisplayTypeId(int displayTypeId) {
		this.displayTypeId = displayTypeId;
	}
}
