package com.maicard.product.service;

import org.springframework.scheduling.annotation.Async;

import com.maicard.money.domain.Pay;
import com.maicard.money.domain.Withdraw;
import com.maicard.product.domain.Item;


public interface NotifyService {
	
	@Async
	void sendNotify(Item item);

	void resendFailedNotify();

	String syncSendNotify(Item item);

	@Async
	void sendNotify(Pay pay);

	String syncSendNotify(Pay pay);
	
	@Async
	void sendNotify(Withdraw withdraw);

	String syncSendNotify(Withdraw withdraw);

}
