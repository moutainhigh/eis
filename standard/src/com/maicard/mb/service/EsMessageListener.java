package com.maicard.mb.service;

import java.util.Map;

import org.springframework.ui.ModelMap;

import com.maicard.mb.domain.EsSession;

/**
 * 可以处理Socket Session消息的接口
 * 
 * 
 * @author NetSnake
 * @date 2013-3-3
 */
public interface EsMessageListener {
	
	void onWsMessage(EsSession esSession, Map<String, String> params, ModelMap map) throws Exception;

}
