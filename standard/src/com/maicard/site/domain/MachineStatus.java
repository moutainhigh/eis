package com.maicard.site.domain;

import java.io.Serializable;
import java.util.Date;

public class MachineStatus implements Serializable {
	
	private static final long serialVersionUID = -5787997011849636106L;

	private long machineId;
	
	private Date lastAccessTime;
	
	private Date lastFullAccessTime;
	
	
	
	
	public MachineStatus(){
		
	}
	
	public MachineStatus(long machineId, Date lastAccessTime, Date lastFullAccessTime) {
		this.machineId = machineId;
		this.lastAccessTime = lastAccessTime;
		this.lastFullAccessTime = lastFullAccessTime;

	}

	public long getMachineId() {
		return machineId;
	}

	public void setMachineId(long machineId) {
		this.machineId = machineId;
	}

	public Date getLastAccessTime() {
		return lastAccessTime;
	}

	public void setLastAccessTime(Date lastAccessTime) {
		this.lastAccessTime = lastAccessTime;
	}

	public Date getLastFullAccessTime() {
		return lastFullAccessTime;
	}

	public void setLastFullAccessTime(Date lastFullAccessTime) {
		this.lastFullAccessTime = lastFullAccessTime;
	}



}
