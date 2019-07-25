package com.maicard.common.domain;

import java.util.Date;

public class Version extends EVEisObject {
	
	private static final long serialVersionUID = 345874256766932249L;
	
	/**
	 * 版本号
	 */
	private int versionId;
	
	/**
	 * 显示的版本号
	 */
	private String versionName;
	
	/**
	 * 版本说明
	 */
	private String versionDesc;
	
	/**
	 * 特定的渠道ID
	 */
	private long partnerId;

	
	/**
	 * 发布时间
	 */
	private Date publishTime;
	
	
	public int getVersionId() {
		return versionId;
	}

	public void setVersionId(int versionId) {
		this.versionId = versionId;
	}

	public String getVersionDesc() {
		return versionDesc;
	}

	public void setVersionDesc(String versionDesc) {
		this.versionDesc = versionDesc;
	}

	public long getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(long partnerId) {
		this.partnerId = partnerId;
	}


	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public Date getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}	

	
	

}
