package com.maicard.wpt.service;



import com.maicard.wpt.domain.WeixinMsg;
public interface WeixinMsgService {
	public int insert(WeixinMsg message);

	public void insertAsUserMessage(WeixinMsg message, String identify, int status);
}
