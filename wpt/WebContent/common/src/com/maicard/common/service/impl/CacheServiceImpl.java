package com.maicard.common.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.util.Assert;

import com.maicard.annotation.IgnoreJmsDataSync;
//import com.maicard.annotation.IgnoreJmsDataSync;
import com.maicard.common.base.BaseService;
import com.maicard.common.criteria.CacheCriteria;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.CacheService;

/**
 * 注意：使用BothSync将导致消息不断重复发送
 *使用SpringCacheService后
 * @author robin
 *
 */
public class CacheServiceImpl  extends BaseService implements CacheService {

	@Resource
	private CacheManager cacheManager;
	
	@Resource
	private ApplicationContextService applicationContextService;



	@Override
	public void put(String cacheName, String key, Object value) {
		Assert.notNull(cacheName, "尝试插入的缓存系统，名称不能为空");
		Assert.notNull(key, "尝试插入的缓存键不能为空");
		cacheManager.getCache(cacheName).put(key, value);

	}

	@SuppressWarnings("unchecked")
	@Override
	public <T>T get(String cacheName, String key) {
		try{
			return (T)cacheManager.getCache(cacheName).get(key).get();
		}catch(Exception e){

		}
		return null;
	}
	
	
	@Override
	public <T>T get(String cacheNameAndKey) {
		int firstHash = cacheNameAndKey.indexOf("#");
		String cacheName = cacheNameAndKey.substring(0,firstHash);
		String key = cacheNameAndKey.substring(firstHash+1);

		return get(cacheName, key);
	}

	@Override
	public List<String> listKeys(String cacheName, String pattern) {
		Cache cache = cacheManager.getCache(cacheName);
		if(cache == null) {
			logger.error("系统没有配置缓存:" + cacheName);
			return Collections.emptyList();
		}
		Object nativeCache = cache.getNativeCache();
		if(nativeCache == null) {
			logger.error("缓存{}没有原生缓存", cacheName);
			return Collections.emptyList();
		}

		List<String> keys = null;
		List<String> returnKeys = new ArrayList<String>();
		if(nativeCache instanceof SpringRedisCacheImpl) {
			SpringRedisCacheImpl redisCache = (SpringRedisCacheImpl)nativeCache;
			keys = redisCache.listKeys();
		} else {
			logger.error("不支持的缓存类型:{}", nativeCache);
		}
		if(keys == null || keys.size() < 1) {
			logger.debug("原生缓存:{}中的key总数是0",cacheName);
			return Collections.emptyList();
		}
		if(pattern == null) {
			return keys;
		}
		for(String key : keys){			
			if(key.matches(pattern)){
				returnKeys.add(key);
			}
		}	
		logger.debug("原生缓存:{}中的key总数是:{},过滤后的数量是:{}", cacheName, keys.size(), returnKeys.size());
		return returnKeys;
		/*@SuppressWarnings("unchecked")
		List<Object> keys = cacheManager.getCache(cacheName).();
		if(keys == null || keys.size() < 1){
			return null;
		}
		List<String> returnKeys = new ArrayList<String>();
		for(Object key : keys){			
			if(pattern == null || key.toString().matches(pattern)){
				returnKeys.add(key.toString());
			}
		}
		return returnKeys;*/
	}

	@Override
	@IgnoreJmsDataSync
	//@ExecOnBothNode
	public int delete(CacheCriteria cacheCriteria){


		if(cacheManager.getCache(cacheCriteria.getCacheName()) != null){
			cacheManager.getCache(cacheCriteria.getCacheName()).evict(cacheCriteria.getKey());
		} else {
			logger.error("找不到指定的缓存:" + cacheCriteria.getCacheName() + ",无法执行删除操作KEY=" + cacheCriteria.getKey());
		}
		return 1;
	}

	@Override
	@IgnoreJmsDataSync
	//@ExecOnBothNode
	public int deleteWithCacheNameAndKey(String cacheNameAndKey){
		int firstHash = cacheNameAndKey.indexOf("#");
		String cacheName = cacheNameAndKey.substring(0,firstHash);
		String key = cacheNameAndKey.substring(firstHash+1);

		delete(new CacheCriteria(cacheName, key));
		return 1;
	}


	/**
	 * 清除本机所有缓存
	 * 用于某些情况下，缓存未能同步
	 */
	@Override
	@IgnoreJmsDataSync
	//@ExecOnBothNode
	public int evict(String cacheName){
		if(cacheName == null){
			return 1;
		}
		/*if(cacheName.equals("*")){
			logger.debug("当前请求清除所有缓存数据");
			String[] allCacheNames = cacheManager.getCacheNames();
			for(String cn : allCacheNames){
				logger.debug("清空缓存[" + cn + "]");
				cacheManager.getCacheManager().getCache(cn).removeAll();
			}
			return 1;
		}*/

		logger.debug("清空缓存[" + cacheName + "]");
		if(cacheManager.getCache(cacheName) != null){
			cacheManager.getCache(cacheName).clear();
		}
		return 1;

	}


	@Override
	public int count(String cacheName){
		/*Cache cache = cacheManager.getCache(cacheName);
		if(cache != null && cache.getKeysNoDuplicateCheck() != null){
			return cache.getKeysNoDuplicateCheck().size();
		}*/
		return 0;
	}

	@Override
	public String[] getCacheNames() {
		
		Collection<String> set =  cacheManager.getCacheNames();
		String[] names = new String[set.size()];
		int i = 0;
		for(String name : set) {
			names[i] = name;
			i++;
		}
		return names;
		
	}

	
	@Override
	public Object getNativeCache(String cacheName) {
		return 	cacheManager.getCache(cacheName).getNativeCache();

	}




}
