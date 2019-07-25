package com.maicard.security.criteria;

import java.util.Date;

import com.maicard.common.base.Criteria;

public class UserOnlineLogCriteria extends Criteria {

	private static final long serialVersionUID = 4908621968537586208L;

	private int userOnlineId;

	private long uuid;

	private Date onlineTime;
	
	private Date offlineTime;
	
	private Date queryBeginTime;
	
	private Date queryEndTime;

	public int getUserOnlineId() {
		return userOnlineId;
	}

	public void setUserOnlineId(int userOnlineId) {
		this.userOnlineId = userOnlineId;
	}

	public long getUuid() {
		return uuid;
	}

	public void setUuid(long uuid) {
		this.uuid = uuid;
	}

	public Date getOnlineTime() {
		return onlineTime;
	}

	public void setOnlineTime(Date onlineTime) {
		this.onlineTime = onlineTime;
	}

	public Date getOfflineTime() {
		return offlineTime;
	}

	public void setOfflineTime(Date offlineTime) {
		this.offlineTime = offlineTime;
	}

	public Date getQueryBeginTime() {
		return queryBeginTime;
	}

	public void setQueryBeginTime(Date queryBeginTime) {
		this.queryBeginTime = queryBeginTime;
	}

	public Date getQueryEndTime() {
		return queryEndTime;
	}

	public void setQueryEndTime(Date queryEndTime) {
		this.queryEndTime = queryEndTime;
	}
}
