package com.maicard.mb.service;

import org.springframework.scheduling.annotation.Async;

import com.maicard.common.domain.EisMessage;

/**
 * 可以处理系统消息的接口
 * 
 * 
 * @author NetSnake
 * @date 2013-3-3
 */
public interface EisMessageListener {
	
	@Async
	public void onMessage(EisMessage eisMessage);

}
