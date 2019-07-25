package com.maicard.common.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonView;
import com.maicard.views.JsonFilterView;

public abstract class JmsObject implements Serializable {

	private static final long serialVersionUID = 1542959398802114706L;
	
    @JsonView({JsonFilterView.Full.class})
	protected int syncFlag = 0;
    
    @JsonView({JsonFilterView.Full.class})
    protected long version;
    
    //@JsonView({JsonFilterView.Full.class})
    protected String sign;
	
	public int getSyncFlag() {
		return syncFlag;
	}
	public void setSyncFlag(int syncFlag) {
		this.syncFlag = syncFlag;
	}
	public long getVersion() {
		return version;
	}
	public void setVersion(long version) {
		this.version = version;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
}
