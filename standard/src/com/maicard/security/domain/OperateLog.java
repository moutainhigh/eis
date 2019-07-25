package com.maicard.security.domain;

import java.util.Date;

import com.maicard.common.domain.EisObject;


/**
 * 系统操作日志
 *
 *
 * @author NetSnake
 * @date 2015年12月8日
 *
 */
public class OperateLog extends EisObject{

	private static final long serialVersionUID = 1080202741945508270L;
	
	private long operateLogId;		//主键

	//private String objectType;

	private String objectId;

	private long uuid = 0;
	
	private String logVersion;

	private String operateCode;
	
	private String operateResult;

	private Date operateTime;
	
	private String data;
	
	private String methodType;
	
	private String requestMethod;
	
	private Date deadlineTime;
	
	private String ip;
	
	private int serverId;

	public OperateLog(){
	}
	
	public OperateLog(String objectType, String objectId, long uuid, String operate, String operateResult, String data, String ip, int serverId, long ownerId){
		this.objectType = objectType;
		this.objectId = objectId;
		this.uuid = uuid;
		this.operateCode = operate;
		this.operateResult = operateResult;
		this.data = data;
		this.operateTime = new Date();
		this.ip = ip;
		this.serverId = serverId;
		this.ownerId = ownerId;
	
	}
	

	public long getUuid() {
		return uuid;
	}


	public String getOperateCode() {
		return operateCode;
	}

	public void setOperateCode(String operateCode) {
		this.operateCode = operateCode == null ? null : operateCode.trim();
	}

	public Date getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

	

	public void setUuid(long uuid) {
		this.uuid = uuid;
	}

	

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getOperateResult() {
		return operateResult;
	}

	public void setOperateResult(String operateResult) {
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

	public long getOperateLogId() {
		return operateLogId;
	}

	public void setOperateLogId(long operateLogId) {
		this.operateLogId = operateLogId;
	}

	public String getMethodType() {
		return methodType;
	}

	public void setMethodType(String methodType) {
		this.methodType = methodType;
	}

	public String getRequestMethod() {
		return requestMethod;
	}

	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}

	public Date getDeadlineTime() {
		return deadlineTime;
	}

	public void setDeadlineTime(Date deadlineTime) {
		this.deadlineTime = deadlineTime;
	}

	public String getLogVersion() {
		return logVersion;
	}

	public void setLogVersion(String logVersion) {
		this.logVersion = logVersion;
	}

	

}