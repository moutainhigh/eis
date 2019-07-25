package com.maicard.common.criteria;

import com.maicard.common.base.Criteria;

public class AuditCriteria extends Criteria {

	private static final long serialVersionUID = 1L;
	
	private int aid;
	
	private int auditType; 

	private int auditObjectRefId; //审核对象ID
	
	private int auditRefUserId;	//审核相关用户

	private int auditResult;	//审核结果

	private String  startTime; //开始查询时间
	private String  endTime;	//结束查询时间

	public AuditCriteria() {
	}

	public int getAid() {
		return aid;
	}

	public void setAid(int aid) {
		this.aid = aid;
	}

	public int getAuditType() {
		return auditType;
	}

	public void setAuditType(int auditType) {
		this.auditType = auditType;
	}

	public int getAuditObjectRefId() {
		return auditObjectRefId;
	}

	public void setAuditObjectRefId(int auditObjectRefId) {
		this.auditObjectRefId = auditObjectRefId;
	}

	public int getAuditRefUserId() {
		return auditRefUserId;
	}

	public void setAuditRefUserId(int auditRefUserId) {
		this.auditRefUserId = auditRefUserId;
	}

	public int getAuditResult() {
		return auditResult;
	}

	public void setAuditResult(int auditResult) {
		this.auditResult = auditResult;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

}
