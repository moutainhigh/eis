package com.maicard.common.criteria;

import com.maicard.common.base.Criteria;

public class SiteThemeRelationCriteria extends Criteria {

	private static final long serialVersionUID = 1L;

	private String hostCode;
	
	private long uuid;
	
	


	public SiteThemeRelationCriteria() {
	}

	public SiteThemeRelationCriteria(long ownerId) {
		this.ownerId = ownerId;
	}

	public String getHostCode() {
		return hostCode;
	}

	public void setHostCode(String hostCode) {
		this.hostCode = hostCode;
	}

	public long getUuid() {
		return uuid;
	}

	public void setUuid(long uuid) {
		this.uuid = uuid;
	}
	

}
