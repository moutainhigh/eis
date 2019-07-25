package com.maicard.common.criteria;

import com.maicard.common.base.Criteria;

public class JobCriteria extends Criteria {

	private static final long serialVersionUID = 1L;

	private int runningStatus;

	public JobCriteria() {
	}

	public int getRunningStatus() {
		return runningStatus;
	}

	public void setRunningStatus(int runningStatus) {
		this.runningStatus = runningStatus;
	}



}
