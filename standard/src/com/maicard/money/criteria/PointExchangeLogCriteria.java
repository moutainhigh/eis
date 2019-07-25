package com.maicard.money.criteria;

import com.maicard.common.base.Criteria;

public class PointExchangeLogCriteria extends Criteria {


	private static final long serialVersionUID = -2601648257284673750L;
	private float money;		//如果>0，兑换时需要扣除相应的资金
	private long point;		//兑换所需的点数
	
	private String objectType;	//兑换的物品类型，如product
	private long objectId;	//兑换的物品ID
	
	private long uuid;
	
	private String transactionId;

	public long getPoint() {
		return point;
	}
	public void setPoint(long point) {
		this.point = point;
	}
	public String getObjectType() {
		return objectType;
	}
	public void setObjectType(String objectType) {
		if(objectType != null && !objectType.trim().equals("")){
			this.objectType = objectType.trim();
		}
	}
	public long getObjectId() {
		return objectId;
	}
	public void setObjectId(long objectId) {
		this.objectId = objectId;
	}
	
	public float getMoney() {
		return  money;
	}
	public void setMoney(float money) {
		this.money = money;
	}
	public long getUuid() {
		return uuid;
	}
	public void setUuid(long uuid) {
		this.uuid = uuid;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

}
