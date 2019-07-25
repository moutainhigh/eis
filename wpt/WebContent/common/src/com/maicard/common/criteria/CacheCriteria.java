package com.maicard.common.criteria;

import com.maicard.common.base.SyncableCriteria;

/**
 * 系统缓存的条件，主要用于同步
 *
 *
 * @author NetSnake
 * @date 2017-09-18
 */
public class CacheCriteria extends SyncableCriteria{

	private static final long serialVersionUID = -224250042322645594L;

	private String cacheName;
	
	private String key;
	
	public CacheCriteria(){}
	
	public CacheCriteria(String cacheName, String key){
		this.cacheName = cacheName;
		this.key = key;
	}

	public String getCacheName() {
		return cacheName;
	}

	public void setCacheName(String cacheName) {
		this.cacheName = cacheName;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
