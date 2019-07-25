package com.maicard.stat.domain;


import com.maicard.common.domain.EVEisObject;

public class FrontUserStat extends EVEisObject{

	private static final long serialVersionUID = 1L;

	private long frontUserStatId;

	private int registerCount;

	private int activeCount;

	private float activeRate;
	
	private int loginCount;
	
	
	private int inviter;
	
	
	private String statTime;
	



	public FrontUserStat() {
	}

	

	
	public String getStatTime() {
		return statTime;
	}

	public void setStatTime(String statTime) {
		this.statTime = statTime;
	}

	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int)frontUserStatId;

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
		final FrontUserStat other = (FrontUserStat) obj;
		if (frontUserStatId != other.frontUserStatId)
			return false;

		return true;
	}
	
	@Override
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
			"(" + 
			"frontUserStatId=" + "'" + frontUserStatId + "'" + 
			")";
	}

	public int getRegisterCount() {
		return registerCount;
	}

	public void setRegisterCount(int registerCount) {
		this.registerCount = registerCount;
	}

	public int getActiveCount() {
		return activeCount;
	}

	public void setActiveCount(int activeCount) {
		this.activeCount = activeCount;
	}


	public float getActiveRate() {
		return activeRate;
	}

	public void setActiveRate(float activeRate) {
		this.activeRate = activeRate;
	}

	public int getLoginCount() {
		return loginCount;
	}

	public void setLoginCount(int loginCount) {
		this.loginCount = loginCount;
	}


	public long getFrontUserStatId() {
		return frontUserStatId;
	}



	public void setFrontUserStatId(long frontUserStatId) {
		this.frontUserStatId = frontUserStatId;
	}



	public int getInviter() {
		return inviter;
	}

	public void setInviter(int inviter) {
		this.inviter = inviter;
	}


	
}
