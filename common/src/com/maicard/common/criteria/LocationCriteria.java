package com.maicard.common.criteria;

import com.maicard.common.base.Criteria;

public class LocationCriteria extends Criteria {

	private static final long serialVersionUID = 1L;
	private String objectType;
	private int objectId;
	private String locationType; 
	private int locationId;

	public LocationCriteria() {
	}


	public String getLocationType() {
		return locationType;
	}


	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}


	public int getLocationId() {
		return locationId;
	}


	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}


	public String getObjectType() {
		return objectType;
	}


	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}


	public int getObjectId() {
		return objectId;
	}


	public void setObjectId(int objectId) {
		this.objectId = objectId;
	}

	

}
