package com.maicard.site.domain;

import java.util.Date;

import com.maicard.common.domain.EisObject;

/**
 * 站点广告模型
 * 广告内容是文档，通过UDID与具体文档进行关联
 *
 * @author NetSnake
 * @date 2014年10月7日 
 */
public class Advert  extends EisObject implements Cloneable {

	private static final long serialVersionUID = 8663933236209784969L;

	private int advertId;
	
	
	private long publisherId;

	
	private Date createTime;

	private Date publishTime;

	
	private Date validTime;
	
	private int showCount;
	
	
	private int maxShowCount;

	
	private int moneyPerRead;	//每次阅读，用户可以分享的金钱（分）
	
	
	private int pointPerRead;	//每次阅读，用户可以获得的积分	



	public Advert() {
	}

	

	public long getPublisherId() {
		return publisherId;
	}

	public void setPublisherId(long publisherId) {
		this.publisherId = publisherId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}

	public Date getValidTime() {
		return validTime;
	}

	public void setValidTime(Date validTime) {
		this.validTime = validTime;
	}

	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + advertId;

		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Advert other = (Advert) obj;
		if (advertId != other.advertId)
			return false;

		return true;
	}

	@Override
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
				"(" + 
				"advertId=" + "'" + advertId + "'" + 
				")";
	}

	

	
	@Override
	public Advert clone() {
		try{
			return (Advert)super.clone();
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}



	public int getAdvertId() {
		return advertId;
	}



	public void setAdvertId(int advertId) {
		this.advertId = advertId;
	}



	public int getShowCount() {
		return showCount;
	}



	public void setShowCount(int showCount) {
		this.showCount = showCount;
	}



	public int getMaxShowCount() {
		return maxShowCount;
	}



	public void setMaxShowCount(int maxShowCount) {
		this.maxShowCount = maxShowCount;
	}



	public int getMoneyPerRead() {
		return moneyPerRead;
	}



	public void setMoneyPerRead(int moneyPerRead) {
		this.moneyPerRead = moneyPerRead;
	}



	public int getPointPerRead() {
		return pointPerRead;
	}



	public void setPointPerRead(int pointPerRead) {
		this.pointPerRead = pointPerRead;
	}

}
