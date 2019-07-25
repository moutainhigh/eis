package com.maicard.stat.domain;

import java.io.Serializable;

public class StatObject  implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int statId;
	
	private String statName;
	
	private int statCount;
	
	private double statSum;
	
	private String statTime;
	

	public int getStatId() {
		return statId;
	}

	public void setStatId(int statId) {
		this.statId = statId;
	}

	public String getStatName() {
		return statName;
	}

	public void setStatName(String statName) {
		this.statName = statName;
	}

	public int getStatCount() {
		return statCount;
	}

	public void setStatCount(int statCount) {
		this.statCount = statCount;
	}

	public String getStatTime() {
		return statTime;
	}

	public void setStatTime(String statTime) {
		this.statTime = statTime;
	}

	public double getStatSum() {
		return statSum;
	}

	public void setStatSum(double statSum) {
		this.statSum = statSum;
	}

	

}
