package com.maicard.common.criteria;

import com.maicard.common.base.Criteria;

public class SiteDomainRelationCriteria extends Criteria {

	private static final long serialVersionUID = 1L;

	private String siteCode;
	private String domain;


	public SiteDomainRelationCriteria() {
	}

	public SiteDomainRelationCriteria(long ownerId) {
		this.ownerId = ownerId;
	}
	public String getSiteCode() {
		return siteCode;
	}
	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}


}
