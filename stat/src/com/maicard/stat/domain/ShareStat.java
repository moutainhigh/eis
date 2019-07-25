package com.maicard.stat.domain;

import com.maicard.common.domain.EisObject;

/**
 * 
 * 渠道或厂商分成统计
 * 
 * 
 * @author NetSnake
 *
 */
public class ShareStat extends EisObject {

	private static final long serialVersionUID = -7774736003596755447L;
	
	private int shareStatId;	//主键

	private Float sharePercent;	//分成比例

	private Float shareMoney;	//实际分成后金额

	private Float totalMoney;		//分成前总金额

	private String objectType;	//分成对象类型，business或product

	private Integer objectId;	//分成对象ID，如business的业务服务器ID

	private String statTime;	//统计时间，小时

	private Integer shareUuid;	//分成给哪个用户

	////////////////////////////////////////////	
	private String productName;
	private String shareUserName;


	public Float getSharePercent() {
		return sharePercent == null ? 0 : sharePercent;
	}

	public void setSharePercent(Float sharePercent) {
		this.sharePercent = sharePercent;
	}

	public Float getShareMoney() {
		return shareMoney == null ? 0f : shareMoney;
	}

	public void setShareMoney(Float shareMoney) {
		this.shareMoney = shareMoney;
	}

	public Float getTotalMoney() {
		return totalMoney == null ? 0f : totalMoney;
	}

	public void setTotalMoney(Float totalMoney) {
		this.totalMoney = totalMoney;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getStatTime() {
		return statTime;
	}

	public void setStatTime(String statTime) {
		this.statTime = statTime;
	}

	public Integer getObjectId() {
		return objectId == null ? 0 : objectId;
	}

	public void setObjectId(Integer objectId) {
		this.objectId = objectId;
	}

	public Integer getShareUuid() {
		return shareUuid == null ? 0 : shareUuid;
	}

	public void setShareUuid(Integer shareUuid) {
		this.shareUuid = shareUuid;
	}


	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getShareUserName() {
		return shareUserName;
	}

	public void setShareUserName(String shareUserName) {
		this.shareUserName = shareUserName;
	}

	public int getShareStatId() {
		return shareStatId;
	}

	public void setShareStatId(int shareStatId) {
		this.shareStatId = shareStatId;
	}
}
