package com.maicard.stat.criteria;

import java.util.Date;

import com.maicard.common.base.InviterSupportCriteria;

public class WithdrawStatCriteria extends InviterSupportCriteria {

	private static final long serialVersionUID = 1L;
	
	private Date queryBeginTime;
	
	private Date queryEndTime;
	
	private long withdrawStatId;
	
	private String queryType = "day";  //hour小时或者day天
	
	private boolean requireSummary;
	
	
	private String uuidRange;
	
	private long rootUuid;
	
	private long partnerUuid;
	
	private int withdrawMethodId;
	
	private boolean groupByInviter;
	
	private boolean groupByDay;
	
	/**
	 * 不进行任何组合
	 */
	private boolean groupByNothing;
	
	/**
	 * 是否按照支付类型统计
	 */
	private boolean groupByWithdrawTypeId;
	
	/**
	 * 是否按照支付通道统计
	 */
	private boolean groupByWithdrawMethodId;

	

	public WithdrawStatCriteria() {
	}
	public WithdrawStatCriteria(long ownerId) {
		this.ownerId = ownerId;
	}



	public long getWithdrawStatId() {
		return withdrawStatId;
	}


	public void setWithdrawStatId(long withdrawStatId) {
		this.withdrawStatId = withdrawStatId;
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


	public int getWithdrawMethodId() {
		return withdrawMethodId;
	}


	public void setWithdrawMethodId(int withdrawMethodId) {
		this.withdrawMethodId = withdrawMethodId;
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


	public boolean isGroupByWithdrawMethodId() {
		return groupByWithdrawMethodId;
	}


	public void setGroupByWithdrawMethodId(boolean groupByWithdrawMethodId) {
		this.groupByWithdrawMethodId = groupByWithdrawMethodId;
	}

	public boolean isGroupByWithdrawTypeId() {
		return groupByWithdrawTypeId;
	}
	public void setGroupByWithdrawTypeId(boolean groupByWithdrawTypeId) {
		this.groupByWithdrawTypeId = groupByWithdrawTypeId;
	}
	public boolean isGroupByNothing() {
		return groupByNothing;
	}
	public void setGroupByNothing(boolean groupByNothing) {
		this.groupByNothing = groupByNothing;
	}




}
