package com.maicard.common.service;

import org.springframework.scheduling.annotation.Async;

import com.maicard.common.domain.EisMessage;

public interface Task {

	@Async
	EisMessage start();

	@Async
	EisMessage stop();

	EisMessage status();
	
	@Async
	EisMessage start(String objectType, int... objectIds);
	

	void run();

}
