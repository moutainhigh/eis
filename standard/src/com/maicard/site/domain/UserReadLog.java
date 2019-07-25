package com.maicard.site.domain;

import java.util.Date;

import com.maicard.common.domain.EisObject;

public class UserReadLog extends EisObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8542187681143621751L;
	
	private int userReadLogId;
	private int udid;
	private long uuid;
	private Date readTime;
	
	public int getUserReadLogId() {
		return userReadLogId;
	}
	public void setUserReadLogId(int userReadLogId) {
		this.userReadLogId = userReadLogId;
	}
	public int getUdid() {
		return udid;
	}
	public void setUdid(int udid) {
		this.udid = udid;
	}
	public long getUuid() {
		return uuid;
	}
	public void setUuid(long uuid) {
		this.uuid = uuid;
	}
	public Date getReadTime() {
		return readTime;
	}
	public void setReadTime(Date readTime) {
		this.readTime = readTime;
	}
	
	

}
