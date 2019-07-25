package com.maicard.common.service.impl;


import java.util.List;

import javax.annotation.Resource;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.criteria.SecurityLevelCriteria;
import com.maicard.common.dao.SecurityLevelDao;
import com.maicard.common.domain.SecurityLevel;
import com.maicard.common.domain.SiteDomainRelation;
import com.maicard.common.service.SecurityLevelService;

@Service
public class SecurityLevelServiceImpl extends BaseService implements SecurityLevelService {

	@Resource
	private SecurityLevelDao securityLevelDao;
	@Resource
	private CacheManager cacheManager;
	
	private final String cacheName = SecurityLevelCriteria.cacheName;
	private final String cachePrefix = SecurityLevelCriteria.cachePrefix;



	private void initCache(){
		//初始化siteDomainRelation数据
		SecurityLevelCriteria configCriteria = new SecurityLevelCriteria();
		List<SecurityLevel> existSecurityLevelList = list(configCriteria);
		int addCount = 0;
		int existCount = 0;
		logger.info("初始化系统安全级别数据，当前数据库中的系统安全级别数据:" + (existSecurityLevelList == null ? "空" : existSecurityLevelList.size()));
		if(existSecurityLevelList != null && existSecurityLevelList.size() > 0){
			for(SecurityLevel config : existSecurityLevelList){
				String key = cachePrefix + "#" + config.getLevel();
				if(cacheManager.getCache(cacheName).get(key) == null  	|| !(cacheManager.getCache(cacheName).get(key).get() instanceof SiteDomainRelation)){
					logger.debug("将系统安全级别数据[" + config + "]放入缓存");
					addCount++;
					cacheManager.getCache(cacheName).put(key, config);
				} else {
					logger.debug("系统安全级别数据[" + config + "]已存在于缓存中");
					existCount++;
				}

			}
			logger.info("共放入" + addCount + "个系统安全级别数据，已存在" + existCount + "个系统安全级别数据");
		} else {
			logger.error("未找到任何系统配置数据");
		}

	}
	
	
	public int  insert(SecurityLevel securityLevel) {
		int rs = 0;
		try{
			rs = securityLevelDao.insert(securityLevel);
		}catch(Exception e){
			logger.error("插入数据失败:" + e.getMessage());
		}
		if(rs == 1){
			initCache();
		}
		return rs;
	}


	public int update(SecurityLevel securityLevel) {
		int rs = 0;
		try{
			rs =  securityLevelDao.update(securityLevel);
		}catch(Exception e){
			logger.error("更新数据失败:" + e.getMessage());
		}
		if(rs == 1){
			initCache();
		}
		return rs;	
	}

	

	public int delete(int level) {
		int rs = 0;
		try{
			rs =  securityLevelDao.delete(level);
		}catch(Exception e){
			logger.error("删除数据失败:" + e.getMessage());
		}
		if(rs == 1){
			initCache();
		}
		return rs;	
	}

	public SecurityLevel select(int level) {
		SecurityLevel securityLevel =  securityLevelDao.select(level);
		if(securityLevel == null){
			logger.error("找不到level=" + level + "的安全级别");
			return null;
		}
		
		return securityLevel;

	}

	public List<SecurityLevel> list(SecurityLevelCriteria roleCriteria) {
		List<SecurityLevel> securityLevelList =  securityLevelDao.list(roleCriteria);
		if(securityLevelList == null){
			return null;
		}
		return securityLevelList;
	}
	


	public List<SecurityLevel> listOnPage(SecurityLevelCriteria roleCriteria) {
		List<SecurityLevel> securityLevelList =  securityLevelDao.listOnPage(roleCriteria);
		if(securityLevelList == null){
			return null;
		}
		return securityLevelList;	
	}

	
	public int count(SecurityLevelCriteria roleCriteria){
		return securityLevelDao.count(roleCriteria);
	}


}
