package com.maicard.common.criteria;


import com.maicard.common.base.Criteria;



public class RegionCriteria extends Criteria implements Cloneable{
	
	
	private static final long serialVersionUID = 3335078798901559075L;
	private  int[] regionId;
	private int parentRegionId;
	private String regionCode;
	private String regionValue;
	private String regionName;
	private int supplyPartnerId;
	private String regionType;
	private int refObjectId;
	
	public int[] getRegionId() {
		return regionId;
	}
	public void setRegionId(int... regionId) {
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
	public String getRegionType() {
		return regionType;
	}
	public void setRegionType(String regionType) {
		this.regionType = regionType;
	}
	public int getRefObjectId() {
		return refObjectId;
	}
	public void setRefObjectId(int refObjectId) {
		this.refObjectId = refObjectId;
	}
	@Override
	public RegionCriteria clone() {
		try{
			return (RegionCriteria)super.clone();
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	

}


