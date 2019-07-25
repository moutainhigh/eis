package com.maicard.site.criteria;

import java.util.Date;

import com.maicard.common.base.Criteria;

public class AdvertCriteria extends Criteria {

	
	private static final long serialVersionUID = -8240091325403203658L;

	private int advertId;
	
	private long publisherId;

	private Date createTime;

	private Date publishTime;

	private Date validTime;
	
	private int showCount;
	
	private int maxShowCount;

	public int getAdvertId() {
		return advertId;
	}

	public void setAdvertId(int advertId) {
		this.advertId = advertId;
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
}
