package com.maicard.security.domain;

import java.util.Date;

import com.maicard.common.domain.EisObject;

public class UserOnlineLog extends EisObject {

	private static final long serialVersionUID = 1L;

	private int userOnlineLogId;

	private long uuid;

	private Date onlineTime;

	private Date offlineTime;
	public UserOnlineLog() {
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



	public int getUserOnlineLogId() {
		return userOnlineLogId;
	}



	public void setUserOnlineLogId(int userOnlineLogId) {
		this.userOnlineLogId = userOnlineLogId;
	}

	@Override
	public String toString(){
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
				"(" + 
				"uuid=" + "'" + uuid + "'" + 
				"id=" + "'" + userOnlineLogId + "'" + 
				")";
	}
}
