package com.maicard.wpt.domain;

public class CouponModelCash{
	
	private CouponModelBaseInfo base_info;

	public CouponModelBaseInfo getBase_info() {
		if(base_info == null){
			base_info = new CouponModelBaseInfo();
		}
		return base_info;
	}

	public void setBase_info(CouponModelBaseInfo base_info) {
		this.base_info = base_info;
	}
	

}
