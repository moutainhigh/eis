package com.maicard.wpt.criteria;

import com.maicard.common.base.Criteria;

public class WeixinGroupCriteria extends Criteria {

	private static final long serialVersionUID = -6904399137900803811L;

	private long groupId;

	private long outGroupId;

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public long getOutGroupId() {
		return outGroupId;
	}

	public void setOutGroupId(long outGroupId) {
		this.outGroupId = outGroupId;
	}

}
