package com.maicard.common.domain;


/**
 * 位置信息
 *
 * @author NetSnake
 * @date 2013-11-2 
 */
public class Location extends EisObject{



	private static final long serialVersionUID = 1625275541810250975L;
	private String objectType;
	private long objectId;
	private String locationType;
	private int locationId;
	private float x;
	private float y;
	private float z;
	
	private float longitude;	//经度
	private float latitude;		//纬度
	
	



	
	public Location() {
	}
	
	public Location(String objectType, long objectId, String locationType, int locationId, float x, float y, float z){
		this.objectType = objectType;
		this.objectId = objectId;
		this.locationType = locationType;
		this.locationId = locationId;
		this.x = x;
		this.y = y;
		this.z = z;
	}




	public int getLocationId() {
		return locationId;
	}




	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}




	public float getX() {
		return x;
	}




	public void setX(float x) {
		this.x = x;
	}




	public float getY() {
		return y;
	}




	public void setY(float y) {
		this.y = y;
	}




	public float getZ() {
		return z;
	}




	public void setZ(float z) {
		this.z = z;
	}




	public String getLocationType() {
		return locationType;
	}




	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public long getObjectId() {
		return objectId;
	}
	
	public void setObjectId(long objectId) {
		this.objectId = objectId;
	}
	
	@Override
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
				"(" + 
				"objectType=" + "'" + objectType + "',objectId='" + objectId + "',locationType='" + locationType + "',locationId='" + locationId + "',x=" + x + "',y='" + y + "',z='" + z + "'" +
				")";
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	
}
