package com.maicard.money.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.maicard.common.domain.EisObject;

/**
 * 产品价格规则
 * 在很多时候，产品价格较复杂的时候，作为产品价格的标准
 * 
 * 
 * @author NetSnake
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PointExchange extends EisObject implements Cloneable{

	private static final long serialVersionUID = -2781195497852783732L;

	private int pointExchangeId;

	private int pointExchangeLogId;
	
	private float marketPrice;	//市场价

	private String transactionId;

	private float money;		//如果>0，兑换时需要扣除相应的资金

	private long coin;		//兑换所需的金币

	private long point;		//兑换所需的点数
	
	private long score;		//兑换所需的积分
	
	
	private String objectType;	//兑换的物品类型，如product
	
	private String exchangeType;	//对话类型，@see com.maicard.standard.PriceType

	private long objectId;	//兑换的物品ID

	private String processClass;


	//非持久化
	private long uuid;
	
	
	private int count;		//兑换的数量

	private Date exchangeTime;
	


	public long getScore() {
		return score;
	}
	public void setScore(long score) {
		this.score = score;
	}

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
	public String getProcessClass() {
		return processClass;
	}
	public void setProcessClass(String processClass) {
		if(processClass != null && !processClass.trim().equals("")){
			this.processClass = processClass.trim();
		}
	}
	public float getMoney() {
		return  money;
	}
	public void setMoney(float money) {
		this.money = money;
	}
	public int getPointExchangeId() {
		return pointExchangeId;
	}
	public void setPointExchangeId(int pointExchangeId) {
		this.pointExchangeId = pointExchangeId;
	}
	public long getUuid() {
		return uuid;
	}
	public void setUuid(long uuid) {
		this.uuid = uuid;
	}
	public long getCoin() {
		return coin;
	}
	public void setCoin(long coin) {
		this.coin = coin;
	}

	@Override
	public String toString(){

		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
				"(" + 
				"pointExchangeId=" + pointExchangeId +
				",uuid=" +  uuid + 
				",money=" + money +
				",coin=" + coin +
				",point=" + point +
				",score=" + score +
				",object=" + objectType + "." + objectId +
				",processClass=" + processClass +
				")";

	}
	public float getMarketPrice() {
		return marketPrice;
	}
	public void setMarketPrice(float marketPrice) {
		this.marketPrice = marketPrice;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public Date getExchangeTime() {
		return exchangeTime;
	}
	public void setExchangeTime(Date exchangeTime) {
		this.exchangeTime = exchangeTime;
	}
	
	@Override
	public PointExchange clone(){
		try{
			return (PointExchange)super.clone();
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
		
	}
	public String getExchangeType() {
		return exchangeType;
	}
	public void setExchangeType(String exchangeType) {
		this.exchangeType = exchangeType;
	}
	public int getPointExchangeLogId() {
		return pointExchangeLogId;
	}
	public void setPointExchangeLogId(int pointExchangeLogId) {
		this.pointExchangeLogId = pointExchangeLogId;
	}

}
