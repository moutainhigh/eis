package com.maicard.security.domain;


import com.maicard.common.domain.EisObject;

//用户与地区的关联关系
public class UserRegionRelation extends EisObject{


	
	private static final long serialVersionUID = -1040785188667530134L;

	private int userRegionRelationId;

	private long uuid;
	
	private int regionId; //区域ID @see com.maicard.common.domain.Region
	
	private String regionCode;
	
	private int regionRange; //支持的区域列表
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final UserRegionRelation other = (UserRegionRelation) obj;
		if (userRegionRelationId != other.userRegionRelationId)
			return false;

		return true;
	}
	
	@Override
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
			"(" + 
			"userRegionRelationId=" + "'" + userRegionRelationId + "'" + 
			")";
	}

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

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	
	
}
