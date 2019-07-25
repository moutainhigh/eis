package com.maicard.security.criteria;


import java.util.Date;

import com.maicard.common.base.Criteria;

public class OperateLogCriteria extends Criteria{

	private static final long serialVersionUID = 7327174202280426630L;

	private String objectType;

	private String objectId;

	private long uuid = 0;

	private String operateCode;
	
	private String[] operateResult;
	
	private String data;
	
	private String ip;
	
	private int serverId;

    
    private Date beginTime;
    
    private Date endTime;
    
    

    public long getUuid() {
        return uuid;
    }

    public void setUuid(long uuid) {
        this.uuid = uuid;
    }

    public String getOperateCode() {
        return operateCode;
    }

    public void setOperateCode(String operateCode) {
    	if(operateCode != null && !operateCode.trim().equals("")){
            this.operateCode = operateCode.trim();

    	}
    }
	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String[] getOperateResult() {
		return operateResult;
	}

	public void setOperateResult(String... operateResult) {
		this.operateResult = operateResult;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}


}