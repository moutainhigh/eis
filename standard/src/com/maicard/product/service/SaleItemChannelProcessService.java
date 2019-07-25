package com.maicard.product.service;

import org.springframework.scheduling.annotation.Async;

import com.maicard.product.domain.Item;

public interface SaleItemChannelProcessService {
	
	@Async
	void beginTransaction(Item item, int channelOrder);
}
