/**
 * 
 */
package com.maicard.mb.service;

import org.springframework.scheduling.annotation.Async;

import com.maicard.common.domain.EisMessage;


/**
 * 
 *
 * @author NetSnake
 * @date 2013-9-16 
 */
public interface JmsDataReplicateService {
	
	@Async
	void operate(EisMessage eisMessage);

	Object getValidSiteReplicationObject(Object parameters);

}
