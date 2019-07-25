package com.maicard.product.service;

import org.springframework.scheduling.annotation.Async;

/**
 * 对超时卡密进行处理
 *
 * @author NetSnake
 * @date 2013-7-10 
 */
public interface TimeoutSaleItemService {
	
	@Async
	public void run();
}
