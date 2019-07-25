package com.maicard.site.domain;

import java.io.Serializable;
import java.util.Date;

public class TagStat implements Serializable {

	private static final long serialVersionUID = 1L;

	private int tagStatId;

	private int tagId;

	private int hits;

	private Date statHour;

	public TagStat() {
	}

	public int getTagStatId() {
		return tagStatId;
	}

	public void setTagStatId(int tagStatId) {
		this.tagStatId = tagStatId;
	}

	public int getTagId() {
		return tagId;
	}

	public void setTagId(int tagId) {
		this.tagId = tagId;
	}

	public int getHits() {
		return hits;
	}

	public void setHits(int hits) {
		this.hits = hits;
	}

	public Date getStatHour() {
		return statHour;
	}

	public void setStatHour(Date statHour) {
		this.statHour = statHour;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + tagStatId;

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
		final TagStat other = (TagStat) obj;
		if (tagStatId != other.tagStatId)
			return false;

		return true;
	}
	
	@Override
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
			"(" + 
			"tagStatId=" + "'" + tagStatId + "'" + 
			")";
	}
	
}
