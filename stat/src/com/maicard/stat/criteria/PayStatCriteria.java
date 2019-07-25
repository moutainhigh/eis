package com.maicard.stat.criteria;

import java.util.Date;

import com.maicard.common.base.InviterSupportCriteria;

public class PayStatCriteria extends InviterSupportCriteria {

	private static final long serialVersionUID = 1L;
	
	private Date queryBeginTime;
	
	private Date queryEndTime;
	
	/**
	 * 用于手工统计时传递的开始时间参数，正常不使用
	 */
	private String statisticTimeBegin;
	
	/**
	 * 用于手工统计时传递的结束时间参数，正常不使用
	 */
	private String statisticTimeEnd;
	
	private long payStatId;
	
	private String queryType = "day";  //hour小时或者day天
	
	private boolean requireSummary;
	
	
	private String uuidRange;
	
	private long rootUuid;
	
	private long partnerUuid;
	
	private int payMethodId;
	
	private int payTypeId;
	
	private String payCardType;

	
	private boolean groupByInviter;
	
	private boolean groupByDay;
	
	private boolean groupByMonth;
	
	private boolean groupByPayCardType;

	/**
	 * 不进行任何组合
	 */
	private boolean groupByNothing;
	
	/**
	 * 是否按照支付类型统计
	 */
	private boolean groupByPayTypeId;
	
	/**
	 * 是否按照支付通道统计
	 */
	private boolean groupByPayMethodId;

	

	public PayStatCriteria() {
	}
	public PayStatCriteria(long ownerId) {
		this.ownerId = ownerId;
	}



	public long getPayStatId() {
		return payStatId;
	}


	public void setPayStatId(long payStatId) {
		this.payStatId = payStatId;
	}


	public String getQueryType() {
		return queryType;
	}


	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}


	public String getUuidRange() {
		return uuidRange;
	}


	public void setUuidRange(String uuidRange) {
		this.uuidRange = uuidRange;
	}


	public long getRootUuid() {
		return rootUuid;
	}


	public void setRootUuid(long rootUuid) {
		this.rootUuid = rootUuid;
	}


	public long getPartnerUuid() {
		return partnerUuid;
	}


	public void setPartnerUuid(long partnerUuid) {
		this.partnerUuid = partnerUuid;
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


	public boolean isRequireSummary() {
		return requireSummary;
	}


	public void setRequireSummary(boolean requireSummary) {
		this.requireSummary = requireSummary;
	}


	public int getPayMethodId() {
		return payMethodId;
	}


	public void setPayMethodId(int payMethodId) {
		this.payMethodId = payMethodId;
	}


	public boolean isGroupByInviter() {
		return groupByInviter;
	}


	public void setGroupByInviter(boolean groupByInviter) {
		this.groupByInviter = groupByInviter;
	}


	public boolean isGroupByDay() {
		return groupByDay;
	}


	public void setGroupByDay(boolean groupByDay) {
		this.groupByDay = groupByDay;
	}


	public boolean isGroupByPayMethodId() {
		return groupByPayMethodId;
	}


	public void setGroupByPayMethodId(boolean groupByPayMethodId) {
		this.groupByPayMethodId = groupByPayMethodId;
	}

	public boolean isGroupByPayTypeId() {
		return groupByPayTypeId;
	}
	public void setGroupByPayTypeId(boolean groupByPayTypeId) {
		this.groupByPayTypeId = groupByPayTypeId;
	}
	public boolean isGroupByNothing() {
		return groupByNothing;
	}
	public void setGroupByNothing(boolean groupByNothing) {
		this.groupByNothing = groupByNothing;
	}
	public boolean isGroupByMonth() {
		return groupByMonth;
	}
	public void setGroupByMonth(boolean groupByMonth) {
		this.groupByMonth = groupByMonth;
	}
	public int getPayTypeId() {
		return payTypeId;
	}
	public void setPayTypeId(int payTypeId) {
		this.payTypeId = payTypeId;
	}
	public String getStatisticTimeBegin() {
		return statisticTimeBegin;
	}
	public void setStatisticTimeBegin(String statisticTimeBegin) {
		this.statisticTimeBegin = statisticTimeBegin;
	}
	public String getStatisticTimeEnd() {
		return statisticTimeEnd;
	}
	public void setStatisticTimeEnd(String statisticTimeEnd) {
		this.statisticTimeEnd = statisticTimeEnd;
	}
	public String getPayCardType() {
		return payCardType;
	}
	public void setPayCardType(String payCardType) {
		this.payCardType = payCardType;
	}
	public boolean isGroupByPayCardType() {
		return groupByPayCardType;
	}
	public void setGroupByPayCardType(boolean groupByPayCardType) {
		this.groupByPayCardType = groupByPayCardType;
	}




}
