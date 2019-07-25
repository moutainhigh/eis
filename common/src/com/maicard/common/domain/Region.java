package com.maicard.common.domain;

import java.util.HashMap;




public class Region extends EisObject implements Cloneable {
	
	private static final long serialVersionUID = -9137724001164859527L;
	
	private  int regionId;
	private int parentRegionId;
	private String regionCode;
	private String regionValue;
	private String regionName;
	private int supplyPartnerId;
	private String regionType;
	private int level;
	private int refObjectId;	//区域对应的对象类型，如果对应的是产品那就是产品ID
	
	private HashMap<String, Region> subRegionMap;
	
	public int getRegionId() {
		return regionId;
	}
	public void setRegionId(int regionId) {
		this.regionId = regionId;
	}
	public int getParentRegionId() {
		return parentRegionId;
	}
	public void setParentRegionId(int parentRegionId) {
		this.parentRegionId = parentRegionId;
	}
	public String getRegionCode() {
		return regionCode;
	}
	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	public int getSupplyPartnerId() {
		return supplyPartnerId;
	}
	public void setSupplyPartnerId(int supplyPartnerId) {
		this.supplyPartnerId = supplyPartnerId;
	}
	public String getRegionValue() {
		return regionValue;
	}
	public void setRegionValue(String regionValue) {
		this.regionValue = regionValue;
	}
	
	@Override
	public String toString(){
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
				"(" + 
				"regionId='" + regionId + "'" +
				"regionCode=" + "'" + regionCode + "'" + 
				"regionName=" + "'" + regionName + "'" + 
				"regionType=" + "'" + regionType + "'" + 
				")";
		
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public HashMap<String, Region> getSubRegionMap() {
		return subRegionMap;
	}
	public void setSubRegionMap(HashMap<String, Region> subRegionMap) {
		this.subRegionMap = subRegionMap;
	}
	public int getRefObjectId() {
		return refObjectId;
	}
	public void setRefObjectId(int refObjectId) {
		this.refObjectId = refObjectId;
	}
	public String getRegionType() {
		return regionType;
	}
	public void setRegionType(String regionType) {
		this.regionType = regionType;
	}
	
	@Override
	public Region clone() {
		try{
			return (Region)super.clone();
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}


}


