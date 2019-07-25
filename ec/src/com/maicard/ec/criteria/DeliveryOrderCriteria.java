package com.maicard.ec.criteria;

import java.util.Date;


public class DeliveryOrderCriteria extends AddressBookCriteria{

	private static final long serialVersionUID = 3405051693860275076L;

	private String deliveryOrderId;	//快递单号

	private String deliveryCompanyId;	//快递公司
	
	private String currentStatusName;		//当前状态说明
	
	private Date createTime;				//创建时间
	
	private Date closeTime;				//结束时间
		
	private String refOrderId;	//	相关联的订单号
	
	private String memory;		//备注

	public String getDeliveryOrderId() {
		return deliveryOrderId;
	}

	public void setDeliveryOrderId(String deliveryOrderId) {
		this.deliveryOrderId = deliveryOrderId;
	}


	public String getCurrentStatusName() {
		return currentStatusName;
	}

	public void setCurrentStatusName(String currentStatusName) {
		this.currentStatusName = currentStatusName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(Date closeTime) {
		this.closeTime = closeTime;
	}

	public String getRefOrderId() {
		return refOrderId;
	}

	public void setRefOrderId(String refOrderId) {
		this.refOrderId = refOrderId;
	}

	public String getMemory() {
		return memory;
	}

	public void setMemory(String memory) {
		this.memory = memory;
	}

	public String getDeliveryCompanyId() {
		return deliveryCompanyId;
	}

	public void setDeliveryCompanyId(String deliveryCompanyId) {
		this.deliveryCompanyId = deliveryCompanyId;
	}

	
}
