package com.maicard.common.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import org.apache.commons.lang.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class SpringRedisCacheImpl extends BaseService implements Cache {
	

	@Value("${systemCode}")
	private String systemCode;

	@Autowired(required=false)
	JedisPool jedisPool;

	private String name;

	/**
	 * 默认生存周期
	 */
	private long ttl = 86400;

	private Map<String,Date> cacheTimes = new HashMap<String,Date>();

	static String PREFIX = "SPRING_CACHE_";

	private byte[] tableName;

	boolean CHECK_CONNECTION = false;
	
	
	private Jedis getResource() {

		Jedis connection = jedisPool.getResource();
		if(CHECK_CONNECTION){
			logger.debug("当前REDIS连接池信息:活动连接{}个，休眠连接{}个", jedisPool.getNumActive(),jedisPool.getNumIdle());
		}
		return connection;
	}
	@Override
	public void clear() {
		logger.debug("清理缓存");
		Jedis connection = getResource();
		connection.del(this.tableName);
		connection.close();
		this.cacheTimes.clear();
	}

	@Override
	public void evict(Object arg0) {
		logger.debug("删除缓存:{}", arg0.toString());
		Jedis connection = getResource();

		connection.hdel(tableName, arg0.toString().getBytes());
		connection.close();

		this.cacheTimes.remove(arg0.toString());

	}

	@Override
	public ValueWrapper get(Object arg0) {
		logger.debug("获取缓存:{}", arg0.toString());
		Object o =  get(arg0, Object.class);
		if(o == null) {
			return null;
		}
		return new SimpleValueWrapper(o);	
	}
	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Object arg0, Class<T> arg1) {
		final String keyf = arg0.toString();
		Jedis connection = getResource();



		Object object = null;
		byte[] value = connection.hget(tableName, keyf.getBytes());
		if (value == null) {
			logger.debug("按class获取缓存:{},class={},目标不存在", arg0.toString(), arg1.getName());
			connection.close();
			return null;
		}
		if(this.cacheTimes.get(keyf) == null) {
			logger.warn("获取的缓存对象:{}没有记录时间", keyf);
			this.cacheTimes.put(keyf,  new Date());
		} else {
			Date createTime = this.cacheTimes.get(keyf);
			if( (new Date().getTime() - createTime.getTime()) / 1000 > ttl) {
				logger.debug("获取的缓存对象:{}已过期", keyf);
				this.cacheTimes.remove(keyf);
				connection.hdel(tableName, keyf.getBytes());
				connection.close();
				return null;
			}
		}
		connection.close();
		object =  SerializationUtils.deserialize(value);
		if(object == null) {
			logger.debug("按class获取缓存:{},class={},目标不能被反序列化", arg0.toString(), arg1.getName());
			return null;
		}
		logger.debug("按class获取缓存:{},class={},目标存在", arg0.toString(), arg1.getName());
		return (T)object;
	}

	@Override
	public <T> T get(Object arg0, Callable<T> arg1) {
		logger.warn("不支持按Callable获取缓存:{},class={}", arg0.toString(), arg1.getClass().getName());
		return null;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Object getNativeCache() {
		logger.debug("获取原生缓存1");
		return this;
	}

	@Override
	public void put(Object arg0, Object arg1) {
		logger.debug("放入缓存:{}=>{}", arg0, arg1);
		if(arg0 == null) {
			logger.error("尝试放入缓存的键为空");
			return;
		}
		if(arg1 == null) {
			logger.error("尝试放入缓存的值为空");
			return;
		}

		byte[] keyb = arg0.toString().getBytes();
		byte[] valueb = SerializationUtils.serialize((Serializable)arg1);
		Jedis connection = getResource();

		connection.hset(tableName,keyb, valueb);
		this.cacheTimes.put(arg0.toString(), new Date());
		connection.close();


	}

	@Override
	public ValueWrapper putIfAbsent(Object arg0, Object arg1) {
		logger.debug("放入缓存2:{}=>{}", arg0, arg1);
		put(arg0, arg1);
		return new SimpleValueWrapper(arg1);	
	}

	public void setName(String name) {
		logger.debug("设置缓存:" + name);
		this.name = name;
		this.tableName = (PREFIX + systemCode + "_" + name).getBytes();
	}

	public List<String> listKeys(){
		Jedis connection = getResource();
		Set<byte[]> set = connection.hkeys(this.tableName);
		connection.close();
		List<String> keyList = new ArrayList<String>();
		if(set != null && set.size() > 0) {
			for(byte[] bKey : set) {
				keyList.add(new String(bKey));
			}
		}
		logger.debug("当前缓存中的key总数是:{}", keyList.size());
		/*Set<String> set = this.cacheTimes.keySet();

		List<String> keyList = new ArrayList<String>();
		for(String keyBin : set) {
			keyList.add(keyBin);
		}*/
		return keyList;

	}
	public long getTtl() {
		return ttl;
	}
	public void setTtl(long ttl) {
		this.ttl = ttl;
	}
	public Map<String, Date> getCacheTimes() {
		return cacheTimes;
	}
	public void setCacheTimes(Map<String, Date> cacheTimes) {
		this.cacheTimes = cacheTimes;
	}

}
