package com.maicard.product.domain;

import com.maicard.common.domain.EisObject;

/**
 * 我方内部产品间的匹配关系
 * 如哪种点卡(产品)可以充哪种帐号(产品)
 * 
 * 我方产品与外部产品的关系由ioServerMap控制
 * @see com.maicard.product.domain.IoServerMap
 * 
 * @author NetSnake
 *
 */
public class ProductMatch extends EisObject {


	private static final long serialVersionUID = 274091856024091448L;
	
	private int productMatchId; //主键
	private int sourceProductId;
	private int destProductId;
	private int transactionTypeId;
	
	private int weight;
	private int processTypeId;
	private int perfProcessChannelId;
	
	/*
	 * 源产品即卡密产品是否支持部分消费及最小消费金额，0则为不支持部分充值。
	 * 如不支持，则在匹配帐号交易时需要锁定的金额必须大于或等于源产品sourceProduct的充值金额
	 * 如果支持，则在匹配帐号交易时需要锁定的金额必须大于或等于这个值
	 */
	private int minSourcePartUseMoney = 0;
	
	/*
	 * 目标产品即帐号产品是否支持部分充值及最小部分充值金额，0则为不支持部分充值。
	 * 如不支持，则在匹配帐号交易时，卡密交易的金额必须大于等于帐号产品的金额
	 * 如果支持，则在匹配帐号交易时，卡密交易的金额必须大于等于这个值
	 */
	private int minDestPartUseMoney = 0; 
	
	public int getSourceProductId() {
		return sourceProductId;
	}
	public void setSourceProductId(int sourceProductId) {
		this.sourceProductId = sourceProductId;
	}
	public int getDestProductId() {
		return destProductId;
	}
	public void setDestProductId(int destProductId) {
		this.destProductId = destProductId;
	}
	public int getTransactionTypeId() {
		return transactionTypeId;
	}
	public void setTransactionTypeId(int transactionTypeId) {
		this.transactionTypeId = transactionTypeId;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	public int getProductMatchId() {
		return productMatchId;
	}
	public void setProductMatchId(int productMatchId) {
		this.productMatchId = productMatchId;
	}
	public int getProcessTypeId() {
		return processTypeId;
	}
	public void setProcessTypeId(int processTypeId) {
		this.processTypeId = processTypeId;
	}
	public int getMinSourcePartUseMoney() {
		return minSourcePartUseMoney;
	}
	public void setMinSourcePartUseMoney(int minSourcePartUseMoney) {
		this.minSourcePartUseMoney = minSourcePartUseMoney;
	}
	public int getMinDestPartUseMoney() {
		return minDestPartUseMoney;
	}
	public void setMinDestPartUseMoney(int minDestPartUseMoney) {
		this.minDestPartUseMoney = minDestPartUseMoney;
	}
	public int getPerfProcessChannelId() {
		return perfProcessChannelId;
	}
	public void setPerfProcessChannelId(int perfProcessChannelId) {
		this.perfProcessChannelId = perfProcessChannelId;
	}
	
	@Override
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
				"(" + 
				"productMatchId=" +  productMatchId +  
				",sourceProductId=" + sourceProductId + 
				",destProductId=" + destProductId +
				",transactionTypeId=" + transactionTypeId + 
				",perfProcessChannelId=" + perfProcessChannelId + 
				",currentStatus="  + currentStatus + 				
				")";
	}
	
}
