package com.maicard.stat.criteria;

import com.maicard.common.base.Criteria;

public class StatCriteria extends Criteria {

	private static final long serialVersionUID = 1L;

	private String beginTime;

	private String endTime;

	private String statTimeMode;

	private String groupBy;

	public StatCriteria() {
	}


	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		if(beginTime != null && !beginTime.trim().equals("")){
			this.beginTime = beginTime.trim();
		}
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		if(endTime != null && !endTime.trim().equals("")){
			this.endTime = endTime.trim();
		}
	}

	public String getStatTimeMode() {
		return statTimeMode;
	}


	public void setStatTimeMode(String statTimeMode) {
		if(statTimeMode != null && !statTimeMode.trim().equals("")){
			this.statTimeMode = statTimeMode.trim();
		}
	}

	public String getGroupBy() {
		return groupBy;
	}


	public void setGroupBy(String groupBy) {
		if(groupBy != null && !groupBy.trim().equals("")){
			this.groupBy = groupBy;
		}
	}









}
