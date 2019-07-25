package com.maicard.security.criteria;

import java.util.Date;

import com.maicard.common.base.Criteria;

public class PasswordLogCriteria extends Criteria {

	private static final long serialVersionUID = 3962022128376021751L;

	private long uuid;

	private String password;

	private Date timeBegin;
	
	private Date timeEnd;

	public long getUuid() {
		return uuid;
	}

	public void setUuid(long uuid) {
		this.uuid = uuid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getTimeBegin() {
		return timeBegin;
	}

	public void setTimeBegin(Date timeBegin) {
		this.timeBegin = timeBegin;
	}

	public Date getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(Date timeEnd) {
		this.timeEnd = timeEnd;
	}
	
	
}
