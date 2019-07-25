package com.maicard.wpt.domain;

import com.maicard.common.domain.EisObject;

public class WeixinInviteCode extends EisObject{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7383310849227055491L;

	private long weixinInviteCodeId;
	
	private long weixinId;	
	
	private String weixinKey;
	
	private String imageToken;
	
	

	public long getWeixinInviteCodeId() {
		return weixinInviteCodeId;
	}

	public void setWeixinInviteCodeId(long weixinInviteCodeId) {
		this.weixinInviteCodeId = weixinInviteCodeId;
	}

	public long getWeixinId() {
		return weixinId;
	}

	public void setWeixinId(long weixinId) {
		this.weixinId = weixinId;
	}

	public String getWeixinKey() {
		return weixinKey;
	}

	public void setWeixinKey(String weixinKey) {
		this.weixinKey = weixinKey;
	}

	public String getImageToken() {
		return imageToken;
	}

	public void setImageToken(String imageToken) {
		this.imageToken = imageToken;
	}
	
	

}
