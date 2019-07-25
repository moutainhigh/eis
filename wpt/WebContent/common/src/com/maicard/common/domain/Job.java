package com.maicard.common.domain;

/**
 * 各项定时执行的任务的配置数据管理
 *
 *
 * @author NetSnake
 * @date 2017-10-09
 */
public class Job extends EVEisObject  {

	private static final long serialVersionUID = 1L;

	private Integer jobId = 0;
	
	private String jobType;

	private String jobName;

	private String beanName;

	private String serverId;
	
	/**
	 * 执行的时间控制
	 */
	private String schedule;
	
	private Integer runningStatus = 0;
	

	

	public Job() {
	}

	public Integer getJobId() {
		return jobId;
	}

	public void setJobId(Integer jobId) {
		this.jobId = jobId;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName == null ? null : jobName.trim();
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName == null ? null : beanName.trim();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + jobId;

		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Job other = (Job) obj;
		if (jobId != other.jobId)
			return false;

		return true;
	}
	
	@Override
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
			"(" + 
			"jobId=" + "'" + jobId + "'" + 
			")";
	}

	




	public Integer getRunningStatus() {
		return runningStatus;
	}

	public void setRunningStatus(Integer runningStatus) {
		this.runningStatus = runningStatus;
	}

	

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType == null ? null : jobType.trim();
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public String getSchedule() {
		return schedule;
	}

	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}

	
}
