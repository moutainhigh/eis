package com.maicard.stat.domain;

import com.maicard.common.domain.EisObject;

/**
 * 
 * 流水、收入和利润统计
 * 
 * 
 * @author NetSnake
 *
 */
public class IncomingStat extends EisObject {
	
	private static final long serialVersionUID = -7774736003596755447L;
	
	private int incomingStatId;	//主键

	private Float totalMoney;		//流水
	private Float shareCost; //分成成本
	private Float fixCost; //固定成本
	private Float otherCost; //其他成本
	private Float grossProfit; //收入/毛利 = 流水 - 分成成本 - 固定成本 - 其他成本	
	private Float taxRate;	//税率
	private Float netProfit;	//利润/纯利
	private String statTime;
	
	public Float getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(Float totalMoney) {
		this.totalMoney = totalMoney;
	}
	public Float getShareCost() {
		return shareCost;
	}
	public void setShareCost(Float shareCost) {
		this.shareCost = shareCost;
	}
	public Float getFixCost() {
		return fixCost;
	}
	public void setFixCost(Float fixCost) {
		this.fixCost = fixCost;
	}
	public Float getOtherCost() {
		return otherCost;
	}
	public void setOtherCost(Float otherCost) {
		this.otherCost = otherCost;
	}
	public Float getGrossProfit() {
		return grossProfit;
	}
	public void setGrossProfit(Float grossProfit) {
		this.grossProfit = grossProfit;
	}
	public Float getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(Float taxRate) {
		this.taxRate = taxRate;
	}
	public Float getNetProfit() {
		return netProfit;
	}
	public void setNetProfit(Float netProfit) {
		this.netProfit = netProfit;
	}
	public String getStatTime() {
		return statTime;
	}
	public void setStatTime(String statTime) {
		this.statTime = statTime;
	}
	public int getIncomingStatId() {
		return incomingStatId;
	}
	public void setIncomingStatId(int incomingStatId) {
		this.incomingStatId = incomingStatId;
	}	
		
	

}
