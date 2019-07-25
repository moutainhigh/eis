package com.maicard.common.service;

import java.util.List;

import com.maicard.annotation.ExecOnBothNode;
import com.maicard.common.criteria.CacheCriteria;

//import com.maicard.annotation.IgnoreJmsDataSync;

public interface CacheService {
	void put(String cacheName, String key, Object value);

	
	List<String> listKeys(String cacheName, String pattern);

	<T>T get(String cacheName, String key);

	<T>T get(String cacheNameAndKey);


	@ExecOnBothNode
	int evict(String cacheName);




	int count(String cacheName);







	//@IgnoreJmsDataSync
	@ExecOnBothNode
	int delete(CacheCriteria cacheCriteria);
	
	//@IgnoreJmsDataSync
	@ExecOnBothNode
	int deleteWithCacheNameAndKey(String cacheNameAndKey);


	String[] getCacheNames();


	/**
	 * 返回一个原生cache类
	 * 
	 * 
	 * @author GHOST
	 * @date 2019-01-17
	 */
	Object getNativeCache(String cacheName);





}
