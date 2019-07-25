package com.maicard.stat.criteria;

import java.util.Date;

import com.maicard.common.base.InviterSupportCriteria;

public class FrontUserStatCriteria extends InviterSupportCriteria {

	private static final long serialVersionUID = 1L;

	private int frontUserStatId;

	private int refObjectId;

	private int refObjectTypeId;

	private String uuidRange;

	private Date queryBeginTime;

	private Date queryEndTime;

	private String queryType = "day";

	private long rootUuid;

	private boolean requireSummary;

	private boolean groupByInviter;

	private boolean groupByDay;

	private boolean superMode =false;

	private long partnerUuid;

	public FrontUserStatCriteria() {
	}

	public FrontUserStatCriteria(long ownerId) {
		this.ownerId = ownerId;
	}

	public int getFrontUserStatId() {
		return frontUserStatId;
	}

	public void setFrontUserStatId(int frontUserStatId) {
		this.frontUserStatId = frontUserStatId;
	}

	public int getRefObjectId() {
		return refObjectId;
	}

	public void setRefObjectId(int refObjectId) {
		this.refObjectId = refObjectId;
	}

	public int getRefObjectTypeId() {
		return refObjectTypeId;
	}

	public void setRefObjectTypeId(int refObjectTypeId) {
		this.refObjectTypeId = refObjectTypeId;
	}

	public String getUuidRange() {
		return uuidRange;
	}

	public void setUuidRange(String uuidRange) {
		this.uuidRange = uuidRange;
	}

	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}
	public long getRootUuid() {
		return rootUuid;
	}

	public void setRootUuid(long rootUuid) {
		this.rootUuid = rootUuid;
	}

	public boolean isSuperMode() {
		return superMode;
	}

	public void setSuperMode(boolean superMode) {
		this.superMode = superMode;
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

}
