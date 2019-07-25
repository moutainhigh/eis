package com.maicard.site.service.impl;

import java.util.*;

import javax.annotation.Resource;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.service.CacheService;
import com.maicard.common.service.ConfigService;
import com.maicard.site.criteria.*;
import com.maicard.site.dao.UserReadLogDao;
import com.maicard.site.domain.UserReadLog;
import com.maicard.site.service.UserReadLogService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;

@Service
public class UserReadLogServiceImpl extends BaseService implements UserReadLogService {

	@Resource
	private CacheService cacheService;
	@Resource
	private ConfigService configService;
	@Resource
	private UserReadLogDao userReadLogDao;
	
	private String cacheName = CommonStandard.cacheNameValidate;
	private String cachePrefix = "userReadLog";



	public int insert(UserReadLog userReadLog) {
		userReadLog.setCurrentStatus(BasicStatus.normal.getId());
		
		int rs = 0;
		try{
			rs =  userReadLogDao.insert(userReadLog);
		}catch(DuplicateKeyException de){
			logger.info("由于唯一约束无法写入新的userReadLog，说明该用户[" + userReadLog.getUuid() + "]已阅读过该文章[" + userReadLog.getUdid() + "]");
			return 0;
		}
		catch(Exception e){
			logger.error("更新数据失败:" + e.getMessage());
		}
		if(rs != 1){
			logger.error("无法写入新的userReadLog");
			return 0;
		}
		cacheService.put(cacheName, cachePrefix + "#" + userReadLog.getUuid()  + "#" + userReadLog.getUdid(),userReadLog);
		return 1;
	}

	public int update(UserReadLog userReadLog) {
		int actualRowsAffected = 0;
		
		int userReadLogId = userReadLog.getUserReadLogId();

		UserReadLog _oldUserReadLog = userReadLogDao.select(userReadLogId);

		if (_oldUserReadLog == null) {
			return 0;
		}
		try{
			actualRowsAffected = userReadLogDao.update(userReadLog);
		}catch(Exception e){
			logger.error("更新数据失败:" + e.getMessage());
		}
		return actualRowsAffected;
	}

	public int delete(int userReadLogId) {
		int actualRowsAffected = 0;

		UserReadLog _oldUserReadLog = userReadLogDao.select(userReadLogId);

		if (_oldUserReadLog != null) {
			
			try{
				actualRowsAffected = userReadLogDao.delete(userReadLogId);
			}catch(Exception e){
				logger.error("删除数据失败:" + e.getMessage());
			}
		}
		return actualRowsAffected;
	}


	

	public UserReadLog select(int userReadLogId) {
		UserReadLog userReadLog =  userReadLogDao.select(userReadLogId);
		
		return userReadLog;
	}


	public List<UserReadLog> list(UserReadLogCriteria userReadLogCriteria) {
		List<Integer> idList = userReadLogDao.listPk(userReadLogCriteria);
		if(idList != null && idList.size() > 0){
			List<UserReadLog> userReadLogList =  new ArrayList<UserReadLog> ();		
			for(int i = 0; i < idList.size(); i++){
				UserReadLog userReadLog = userReadLogDao.select(idList.get(i));
				if(userReadLog != null){
					userReadLog.setId(userReadLog.getUserReadLogId());
					userReadLog.setIndex(i+1);					
					userReadLogList.add(userReadLog);
				}
			}
			idList = null;
			return userReadLogList;
		}
		return null;
	}

	public List<UserReadLog> listOnPage(UserReadLogCriteria userReadLogCriteria) {
		List<Integer> idList = userReadLogDao.listPk(userReadLogCriteria);
		if(idList != null && idList.size() > 0){
			List<UserReadLog> userReadLogList =  new ArrayList<UserReadLog> ();		
			for(int i = 0; i < idList.size(); i++){
				UserReadLog userReadLog = userReadLogDao.select(idList.get(i));
				if(userReadLog != null){
					userReadLogList.add(userReadLog);
				}
			}
			idList = null;
			return userReadLogList;
		}
		return null;
		/*
		List<UserReadLog> userReadLogList =  userReadLogDao.listOnPage(userReadLogCriteria);
		return userReadLogList;
		*/

	}


	public int count(UserReadLogCriteria userReadLogCriteria) {
		return userReadLogDao.count(userReadLogCriteria);
	}
	
	//指定用户是否阅读过指定文章
	@Override
	public UserReadLog readed(UserReadLogCriteria userReadLogCriteria){
		if(userReadLogCriteria == null){
			logger.error("必须的参数userReadLogCriteria为空");
		}
		if(userReadLogCriteria.getUuid() <= 0){
			logger.error("必须的参数userReadLogCriteria的uuid为空");
		}
		if(userReadLogCriteria.getUdid() <= 0){
			logger.error("必须的参数userReadLogCriteria的udid为空");
		}
		//检查缓存中有无对应数据
		UserReadLog userReadLog = cacheService.get(cacheName, cachePrefix + "#" + userReadLogCriteria.getUuid()  + "#" + userReadLogCriteria.getUdid());
		
		if(userReadLog != null){
			logger.info("在缓存中找到了用户[" + userReadLogCriteria.getUuid() + "]对文章[" + userReadLogCriteria.getUdid() + "]的阅读记录");
			return userReadLog;
		}
		logger.info("在缓存中未找到用户[" + userReadLogCriteria.getUuid() + "]对文章[" + userReadLogCriteria.getUdid() + "]的阅读记录，尝试从数据库中查找");
		List<UserReadLog> userReadLogList = list(userReadLogCriteria);
		if(userReadLogList == null || userReadLogList.size() < 1){
			logger.info("在数据库中也未找到用户[" + userReadLogCriteria.getUuid() + "]对文章[" + userReadLogCriteria.getUdid() + "]的阅读记录");
			return null;
		}
		userReadLog = userReadLogList.get(0);
		logger.info("在数据库中找到用户[" + userReadLogCriteria.getUuid() + "]对文章[" + userReadLogCriteria.getUdid() + "]的阅读记录[" + userReadLog.getUserReadLogId() + "]，将其放入缓存并返回");
		cacheService.put(cacheName, cachePrefix + "#" + userReadLog.getUuid()  + "#" + userReadLog.getUdid(),userReadLog);
		return userReadLog;
		
	}


}
