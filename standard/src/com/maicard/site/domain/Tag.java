package com.maicard.site.domain;

import com.maicard.annotation.NeedJmsDataSyncP2P;
import com.maicard.common.domain.EisObject;

@NeedJmsDataSyncP2P
public class Tag extends EisObject {


	private static final long serialVersionUID = -6087541110562150023L;

	private long tagId;

	private String tagName;		//标签的具体内容

	private String tagCode;
		

	private int objectCount;

	private double hits;			//命中率
	
	

	public Tag() {
	}
	
	public Tag(String tagName, long ownerId) {
		this.tagName = tagName;
		this.ownerId = ownerId;
	}

	public long getTagId() {
		return tagId;
	}

	public void setTagId(long tagId) {
		this.tagId = tagId;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public String getTagCode() {
		return tagCode;
	}

	public void setTagCode(String tagCode) {
		this.tagCode = tagCode;
	}

	public int getObjectCount() {
		return objectCount;
	}

	public void setObjectCount(int objectCount) {
		this.objectCount = objectCount;
	}

	public double getHits() {
		return hits;
	}

	public void setHits(double hits) {
		this.hits = hits;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (int)(prime * result + tagId);

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
		final Tag other = (Tag) obj;
		if (tagId != other.tagId)
			return false;

		return true;
	}

	@Override
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
				"(" + 
				"tagId=" + "'" + tagId + "'" + 
				"tagName=" + "'" + tagName + "'" + 
				")";
	}


}
