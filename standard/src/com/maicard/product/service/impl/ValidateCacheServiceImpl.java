package com.maicard.product.service.impl;


import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.criteria.CacheCriteria;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.CacheService;
import com.maicard.common.service.Task;
import com.maicard.product.criteria.ValidateCacheCriteria;
import com.maicard.product.dao.ValidateCacheDao;
import com.maicard.product.domain.ValidateCache;
import com.maicard.product.service.ValidateCacheService;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.OperateResult;

@Service
public class ValidateCacheServiceImpl extends BaseService implements ValidateCacheService,Task {

	@Resource
	private ValidateCacheDao validateCacheDao;
	@Resource
	private CacheService cacheService;

	private final String cacheName = CommonStandard.cacheNameProduct;
	private final String cachePrefix = "ValidateCache";


	@Override
	public void insert(ValidateCache validateCache) {	

		if(validateCache == null){
			return;
		}
		if(StringUtils.isBlank(validateCache.getData1())){
			logger.error("尝试插入的验证缓存没有data1");
			return;
		}
		if(StringUtils.isBlank(validateCache.getData2())){
			logger.error("尝试插入的验证缓存没有data2");
			return;
		}
		cacheService.put(cacheName, cachePrefix + "#" + validateCache.getData1() + "#" + validateCache.getData2(), validateCache);		
		if(logger.isDebugEnabled()){
			logger.debug("验证缓存[" + validateCache.getData1() + "/" + validateCache.getData2() + "]已存入缓存系统");
		}
	}



	public void delete(ValidateCache validateCache) {
		if(validateCache == null){
			return;
		}
		if(StringUtils.isBlank(validateCache.getData1())){
			logger.error("尝试删除的验证缓存没有data1");
			return;
		}
		if(StringUtils.isBlank(validateCache.getData2())){
			logger.error("尝试删除的验证缓存没有data2");
			return;
		}
		cacheService.delete(new CacheCriteria(cacheName, cachePrefix + "#" + validateCache.getData1() + "#" + validateCache.getData2()));		
		if(logger.isDebugEnabled()){
			logger.debug("验证缓存[" + validateCache.getData1() + "/" + validateCache.getData2() + "]已从缓存系统中删除");
		}
	}

	public ValidateCache select(ValidateCacheCriteria validateCacheCriteria) {
		if(validateCacheCriteria == null){
			return null;
		}
		if(StringUtils.isBlank(validateCacheCriteria.getData1())){
			logger.error("尝试查找的的验证缓存没有data1");
			return null;
		}
		if(StringUtils.isBlank(validateCacheCriteria.getData2())){
			logger.error("尝试查找的的验证缓存没有data2");
			return null;
		}
		try{
			return cacheService.get(cacheName, cachePrefix + "#" + validateCacheCriteria.getData1()  + "#" + validateCacheCriteria.getData2());	
		}catch(Exception e){
			return null;
		}

	}

	@Override
	public int initValidateCacheData(){
		logger.info("初始化验证缓存数据...");
		List<ValidateCache> validateCacheList = validateCacheDao.initValidateCacheData();
		logger.info("初始化的验证数据有" + (validateCacheList == null ? "空" : validateCacheList.size()));
		if(validateCacheList == null || validateCacheList.size() < 1){
			return 0;
		}

		for(ValidateCache validateCache : validateCacheList){
			insert(validateCache);
		}
		return validateCacheList.size();
	}



	@Override
	public EisMessage start() {
		initValidateCacheData();
		return new EisMessage(OperateResult.success.getId(),"校验缓存初始化完成");
	}



	@Override
	public EisMessage stop() {
		return null;
	}



	@Override
	public EisMessage status() {
		return null;
	}



	@Override
	public EisMessage start(String objectType, int... objectIds) {
		initValidateCacheData();
		return new EisMessage(OperateResult.success.getId(),"校验缓存初始化完成");
	}



	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
