package com.maicard.security.criteria;

import com.maicard.common.base.Criteria;

public class UserRegionRelationCriteria extends Criteria {

	
	private static final long serialVersionUID = -4522180282785141454L;

	private int userRegionRelationId;

	private long uuid;
	
	private int regionId; //区域ID @see com.maicard.common.domain.Region
	
	private int regionRange; //支持的区域列表

	public int getUserRegionRelationId() {
		return userRegionRelationId;
	}

	public void setUserRegionRelationId(int userRegionRelationId) {
		this.userRegionRelationId = userRegionRelationId;
	}

	public long getUuid() {
		return uuid;
	}

	public void setUuid(long uuid) {
		this.uuid = uuid;
	}

	public int getRegionId() {
		return regionId;
	}

	public void setRegionId(int regionId) {
		this.regionId = regionId;
	}

	public int getRegionRange() {
		return regionRange;
	}

	public void setRegionRange(int regionRange) {
		this.regionRange = regionRange;
	}

}
