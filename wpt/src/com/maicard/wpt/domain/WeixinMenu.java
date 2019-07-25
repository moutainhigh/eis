package com.maicard.wpt.domain;

import java.util.Map;

import com.maicard.common.domain.EisObject;

public class WeixinMenu  extends EisObject{

	private static final long serialVersionUID = 7411148569897553638L;

	private long weixinMenuId;
	
	private long outMenuId;
	
	private String urlPrefix;
	
	private String tagId;
	
	
	private String jsonData;
	
	private WeixinButton[] buttons;

	private long uuid;
	
	private Map<String,String> data;

	public long getWeixinMenuId() {
		return weixinMenuId;
	}


	public void setWeixinMenuId(long weixinMenuId) {
		this.weixinMenuId = weixinMenuId;
	}


	public long getOutMenuId() {
		return outMenuId;
	}


	public void setOutMenuId(long outMenuId) {
		this.outMenuId = outMenuId;
	}


	public String getUrlPrefix() {
		return urlPrefix;
	}


	public void setUrlPrefix(String urlPrefix) {
		this.urlPrefix = urlPrefix;
	}


	public String getJsonData() {
		return jsonData;
	}


	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}


	public WeixinButton[] getButtons() {
		return buttons;
	}


	public void setButtons(WeixinButton[] buttons) {
		this.buttons = buttons;
	}


	public String getTagId() {
		return tagId;
	}


	public void setTagId(String tagId) {
		this.tagId = tagId;
	}


	public long getUuid() {
		return uuid;
	}


	public void setUuid(long uuid) {
		this.uuid = uuid;
	}


	public Map<String, String> getData() {
		return data;
	}


	public void setData(Map<String, String> data) {
		this.data = data;
	}

}
