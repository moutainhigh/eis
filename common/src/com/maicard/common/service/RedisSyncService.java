package com.maicard.common.service;

/**
 * 用于周期性的把REDIS缓存中的数据写入到数据库中
 * 
 * 
 * @author XX
 * @date 2019-05-16
 *
 */
public interface RedisSyncService {
	
	public void run();

}
