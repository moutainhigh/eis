package com.maicard.product.domain;

import com.maicard.common.domain.EisObject;

public class CardRegion extends EisObject{
	
	private static final long serialVersionUID = -3026422989065215754L;
	
	private int cardRegionId;
	
	private String beginNumber;
	
	private String endNumber;
	
	private int regionId;
	
	private String regionCode;
	
	private float money;
	
	private int productId;
	
	public CardRegion(){
		
	}
	
	public CardRegion( String regionCode, String money, String beginNumber, String endNumber){
		this.regionCode = regionCode;
		this.money  = Float.parseFloat(money);
		this.beginNumber = beginNumber;
		this.endNumber = endNumber;
	}

	public String getBeginNumber() {
		return beginNumber;
	}

	public void setBeginNumber(String beginNumber) {
		this.beginNumber = beginNumber;
	}

	public String getEndNumber() {
		return endNumber;
	}

	public void setEndNumber(String endNumber) {
		this.endNumber = endNumber;
	}

	public int getRegionId() {
		return regionId;
	}

	public void setRegionId(int regionId) {
		this.regionId = regionId;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}


	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	public int getCardRegionId() {
		return cardRegionId;
	}

	public void setCardRegionId(int cardRegionId) {
		this.cardRegionId = cardRegionId;
	}

	public float getMoney() {
		return money;
	}

	public void setMoney(float money) {
		this.money = money;
	}

}
