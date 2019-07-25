package com.maicard.common.criteria;

public class ExtraDataCriteria extends DataDefineCriteria{
	
	private static final long serialVersionUID = 3887098065945374627L;

	private long extraDataId;
	
	
	private long[] uuid;


	public long[] getUuid() {
		return uuid;
	}

	public void setUuid(long... uuid) {
		this.uuid = uuid;
	}

	public long getExtraDataId() {
		return extraDataId;
	}

	public void setExtraDataId(long extraDataId) {
		this.extraDataId = extraDataId;
	}

	
}
