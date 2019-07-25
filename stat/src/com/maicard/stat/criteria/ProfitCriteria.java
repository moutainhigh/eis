package com.maicard.stat.criteria;


import java.util.Date;

import com.maicard.common.base.InviterSupportCriteria;

public class ProfitCriteria extends InviterSupportCriteria {

	private static final long serialVersionUID = -1840337619819376913L;

	/**
	 * 毛利所属商户
	 */
	private long uuid;

	/**
	 * 毛利的统计时间开始，最多只会精确到小时
	 */
	private Date statTimeBegin;
	
	/**
	 * 毛利的统计时间开始，最多只会精确到小时
	 */
	private Date statTimeEnd;


	/**
	 * 利润来源类型,支付利润为payMethod
	 */
	private String objectType;

	/**
	 * 利润来源的ID，如支付利润，则该ID为payMethodId;
	 */
	private int objectId;

	public long getUuid() {
		return uuid;
	}

	public void setUuid(long uuid) {
		this.uuid = uuid;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public int getObjectId() {
		return objectId;
	}

	public void setObjectId(int objectId) {
		this.objectId = objectId;
	}

	public Date getStatTimeBegin() {
		return statTimeBegin;
	}

	public void setStatTimeBegin(Date statTimeBegin) {
		this.statTimeBegin = statTimeBegin;
	}

	public Date getStatTimeEnd() {
		return statTimeEnd;
	}

	public void setStatTimeEnd(Date statTimeEnd) {
		this.statTimeEnd = statTimeEnd;
	}


}
