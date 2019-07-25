package com.maicard.stat.domain;

import com.maicard.common.domain.EisObject;

/**
 * 毛利统计，按日期、商户和收入的方式（是支付毛利还是销售毛利）进行统计
 *
 *
 * @author NetSnake
 * @date 2017-09-07
 */
public class Profit extends EisObject {

	private static final long serialVersionUID = -5607385570386645354L;
	
	/**
	 * 主键
	 */
	private int profitId;
	
	/**
	 * 毛利所属商户
	 */
	private long uuid;
	
	/**
	 * 毛利的统计时间，一般为yyyyMMdd，按天
	 */
	private String statTime;
	
	/**
	 * 统计时间内的毛利
	 */
	private float profit;
	
	
	//objectType 利润来源类型由父类提供
	
	/**
	 * 利润来源的ID，如支付利润，则该ID为payMethodId;
	 */
	private int objectId;
	
	public Profit(){}
	
	public Profit(long ownerId){
		this.ownerId = ownerId;
	}


	public int getProfitId() {
		return profitId;
	}


	public void setProfitId(int profitId) {
		this.profitId = profitId;
	}


	public long getUuid() {
		return uuid;
	}


	public void setUuid(long uuid) {
		this.uuid = uuid;
	}


	public String getStatTime() {
		return statTime;
	}


	public void setStatTime(String statTime) {
		this.statTime = statTime;
	}


	public float getProfit() {
		return profit;
	}


	public void setProfit(float profit) {
		this.profit = profit;
	}


	public int getObjectId() {
		return objectId;
	}


	public void setObjectId(int objectId) {
		this.objectId = objectId;
	}
	
	

}
