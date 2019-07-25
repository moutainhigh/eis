package com.maicard.common.service;

import com.maicard.common.domain.EisMessage;

public interface EisJob {

	EisMessage start();
	EisMessage stop();
	EisMessage status();
	EisMessage start(String objectType, int... objectIds);

}
