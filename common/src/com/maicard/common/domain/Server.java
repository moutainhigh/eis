package com.maicard.common.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Server extends EVEisObject{

	
	
	protected static final long serialVersionUID = 4830499821052971419L;
	
	protected int serverId;
	
	protected String serverName;
	
	protected String systemCode;
	
	protected int systemServerId;

	protected String appCode;
	
	protected Date bootTime;
	
	protected Date updateTime;
	
	protected String runningPath;
	
	protected String contextPath;
	
	protected String ip;
	
	protected String cmd;
	
	/**
	 * 为true则在执行完命令后会恢复服务器状态到之前，目前restart命令不应当为true
	 */
	protected boolean keepOldStatusAfterExec;
	
	@Override
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
			"(" + 
			"serverId=" + "'" + serverId + "'," + 
			"systemCode=" + "'" + systemCode + "'," + 
			"appCode=" + "'" + appCode + "'," + 
			"systemServerId=" + "'" + systemServerId + "'," + 
			"contextPath=" + "'" + contextPath + "'," + 
			"currentStatus=" + "'" + currentStatus + "'," +
			"ip=" + "'" + ip + "'" + 
		")";
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public String getSystemCode() {
		return systemCode;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

	public int getSystemServerId() {
		return systemServerId;
	}

	public void setSystemServerId(int systemServerId) {
		this.systemServerId = systemServerId;
	}

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}


	public String getRunningPath() {
		return runningPath;
	}

	public void setRunningPath(String runningPath) {
		this.runningPath = runningPath;
	}

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public Date getBootTime() {
		return bootTime;
	}

	public void setBootTime(Date bootTime) {
		this.bootTime = bootTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public boolean isKeepOldStatusAfterExec() {
		return keepOldStatusAfterExec;
	}

	public void setKeepOldStatusAfterExec(boolean keepOldStatusAfterExec) {
		this.keepOldStatusAfterExec = keepOldStatusAfterExec;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	
	
}
