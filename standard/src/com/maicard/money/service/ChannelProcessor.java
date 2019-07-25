package com.maicard.money.service;

import com.maicard.common.domain.EisMessage;

/**
 * 通道的处理器
 */
public interface ChannelProcessor {
	/**
	 * 获取通道的余额
	 */
	EisMessage getRemoteBalance(Object target);
	
	
}
