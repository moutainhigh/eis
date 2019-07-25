package com.maicard.ec.domain;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.maicard.annotation.NeedJmsDataSyncP2P;
import com.maicard.money.domain.Price;

/**
 * 配送单据
 *
 * @author NetSnake
 * @date 2015年8月25日 
 */

@NeedJmsDataSyncP2P
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeliveryOrder extends AddressBook  {

	private static final long serialVersionUID = 4799784518961017943L;


	private long deliveryOrderId;	//PK

	private String outOrderId;	//外部快递单号

	private String deliveryCompany;	//快递公司
		

	private long deliveryCompanyId;	//快递公司在我方平台ID

	private Date createTime;				//创建时间

	private Date closeTime;				//结束时间

	private String refOrderId;	//	相关联的交易订单号

	private String memory;		//备注
	
	private String fromProvince;

	private String fromArea;
	
	private String toProvince;

	private String toArea;

	private int goodsWeight;			//订单重量,以克为单位，计算时应注意转换

	private String limit;			//是否只能走陆运或空运

	private Price fee;				//配送单的费用

	private HashMap<String,String>traceData;		//跟踪数据
	
	private String displayType;			//显示配送跟踪信息的格式


	
	private String brief;		//仅用于显示整个快递单所有数据整合起来的简短信息

	


	public DeliveryOrder(){

	}

	public DeliveryOrder(long ownerId) {
		this.ownerId = ownerId;
		this.createTime = new Date();
	}
	
	public DeliveryOrder(AddressBook addressBook) {
		this.uuid = addressBook.uuid;
		this.country = addressBook.country;
		this.province = addressBook.province;
		this.city = addressBook.city;
		this.district = addressBook.district;
		this.contact = addressBook.contact;
		this.phone = addressBook.phone;
		this.mobile = addressBook.mobile;
		this.postcode = addressBook.postcode;
		this.address = addressBook.address;
		this.ownerId = addressBook.getOwnerId();
		this.createTime = new Date();
	}
	
	@Override
	public DeliveryOrder clone() {
		try{
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(byteOut);
			out.writeObject(this);

			ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
			ObjectInputStream in =new ObjectInputStream(byteIn);

			return (DeliveryOrder)in.readObject();
		
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append(getClass().getName());
		sb.append("@");
		sb.append(Integer.toHexString(hashCode()));
		sb.append("(");
		sb.append("deliveryOrderId=");
		sb.append("'");
		sb.append(deliveryOrderId);
		sb.append("',");
		sb.append("outOrderId=");
		sb.append("'");
		sb.append(outOrderId);
		sb.append("',");
		sb.append("deliveryCompanyId=");
		sb.append("'");
		sb.append(deliveryCompanyId);
		sb.append("',");
		sb.append("goodsWeight=");
		sb.append("'");
		sb.append(goodsWeight);
		sb.append("')");
		sb.append("contact=");
		sb.append("'");
		sb.append(contact);
		sb.append("',");
		
		return sb.toString();
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

	public long getDeliveryOrderId() {
		return deliveryOrderId;
	}

	public void setDeliveryOrderId(long deliveryOrderId) {
		this.deliveryOrderId = deliveryOrderId;
	}

	public String getOutOrderId() {
		return outOrderId;
	}

	public void setOutOrderId(String outOrderId) {
		this.outOrderId = outOrderId;
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


	public String getDeliveryCompany() {
		return deliveryCompany;
	}

	public void setDeliveryCompany(String deliveryCompany) {
		this.deliveryCompany = deliveryCompany;
	}

	public long getDeliveryCompanyId() {
		return deliveryCompanyId;
	}

	public void setDeliveryCompanyId(long deliveryCompanyId) {
		this.deliveryCompanyId = deliveryCompanyId;
	}

	public HashMap<String, String> getTraceData() {
		return traceData;
	}

	public void setTraceData(HashMap<String, String> traceData) {
		this.traceData = traceData;
	}

	public int getGoodsWeight() {
		return goodsWeight;
	}

	public void setGoodsWeight(int goodsWeight) {
		this.goodsWeight = goodsWeight;
	}

	public String getLimit() {
		return limit;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}

	public Price getFee() {
		return fee;
	}

	public void setFee(Price fee) {
		this.fee = fee;
	}
	
	public String getBrief(){
		if(this.brief != null){
			return this.brief;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(this.province == null ?  "" : this.province + " ").append(this.city == null ? "" : this.city + " ").append(this.district == null ? "" : this.district + " ").append(this.address == null ? "" : this.address + " ").append(this.contact == null ? "" : this.contact + " ").append(this.mobile == null ? "" : this.mobile);
		this.brief = sb.toString();
		return this.brief;
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

	

	public String getDisplayType() {
		return displayType;
	}

	public void setDisplayType(String displayType) {
		this.displayType = displayType;
	}


}
