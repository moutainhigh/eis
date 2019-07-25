package com.maicard.ec.criteria;

import com.maicard.common.base.Criteria;

public class DeliveryPriceCriteria extends Criteria{

	private static final long serialVersionUID = -6440479160100559328L;

	private long deliveryCompanyId;	//快递公司对应ID
	
	private String fromProvince;

	private String fromArea;	//对应区域表ID
	
	private String toProvince;

	private String toArea;	//对应区域表ID

	private String areaType;	//区域是省份还是地市
	
	private String identify;		//识别信息

	public long getDeliveryCompanyId() {
		return deliveryCompanyId;
	}

	public void setDeliveryCompanyId(long deliveryCompanyId) {
		this.deliveryCompanyId = deliveryCompanyId;
	}

	public String getFromArea() {
		return fromArea;
	}

	public void setFromArea(String fromArea) {
		this.fromArea = fromArea;
	}

	public String getToArea() {
		return toArea;
	}

	public void setToArea(String toArea) {
		this.toArea = toArea;
	}

	public String getAreaType() {
		return areaType;
	}

	public void setAreaType(String areaType) {
		this.areaType = areaType;
	}

	public String getIdentify() {
		return identify;
	}

	public void setIdentify(String identify) {
		this.identify = identify;
	}

	public String getFromProvince() {
		return fromProvince;
	}

	public void setFromProvince(String fromProvince) {
		this.fromProvince = fromProvince;
	}

	public String getToProvince() {
		return toProvince;
	}

	public void setToProvince(String toProvince) {
		this.toProvince = toProvince;
	}

}
