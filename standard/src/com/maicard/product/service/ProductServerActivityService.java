package com.maicard.product.service;

import org.springframework.scheduling.annotation.Async;

public interface ProductServerActivityService {
	
	@Async
	void updateActivity(int productServerId);

}
