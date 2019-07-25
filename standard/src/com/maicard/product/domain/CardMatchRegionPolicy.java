package com.maicard.product.domain;


import com.maicard.common.domain.EisObject;
import com.maicard.product.domain.CardMatchRegionPolicy;

public class CardMatchRegionPolicy extends EisObject{

	private static final long serialVersionUID = -4911068132509284556L;

	private int cardMatchRegionPolicyId; //主键

	private String cardRegion;

	private String cardType;

	private int labelMoney;

	private long processUuid;

	public CardMatchRegionPolicy() {
	}

	public int getCardMatchRegionPolicyId() {
		return cardMatchRegionPolicyId;
	}

	public void setCardMatchRegionPolicyId(int cardMatchRegionPolicyId) {
		this.cardMatchRegionPolicyId = cardMatchRegionPolicyId;
	}

	public String getCardRegion() {
		return cardRegion;
	}

	public void setCardRegion(String cardRegion) {
		this.cardRegion = cardRegion;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public int getLabelMoney() {
		return labelMoney;
	}

	public void setLabelMoney(int labelMoney) {
		this.labelMoney = labelMoney;
	}

	public long getProcessUuid() {
		return processUuid;
	}

	public void setProcessUuid(long processUuid) {
		this.processUuid = processUuid;
	}
}
	
	
