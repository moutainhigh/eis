package com.maicard.security.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.criteria.CacheCriteria;
import com.maicard.common.service.CacheService;
import com.maicard.common.service.ConfigService;
import com.maicard.mb.service.MessageService;
import com.maicard.security.criteria.UserOnlineLogCriteria;
import com.maicard.security.dao.UserOnlineLogDao;
import com.maicard.security.domain.UserOnlineLog;
import com.maicard.security.service.OperateLogService;
import com.maicard.security.service.UserOnlineLogService;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.SecurityStandard.UserExtraStatus;

@Service
public class UserOnlineLogServiceImpl extends BaseService implements UserOnlineLogService {

	@Resource
	private UserOnlineLogDao userOnlineLogDao;

	@Resource
	private CacheService cacheService;
	@Resource
	private ConfigService configService;
	@Resource 
	private MessageService messageService;
	@Resource
	private OperateLogService operateLogService;

	private boolean handlerUserDynamicDataUpdate;
	private final String cacheName = CommonStandard.cacheNameUser;
	private final String cachePrefix = "UserOnlineLog";
	
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(CommonStandard.defaultDateFormat);

	@PostConstruct
	public void init(){
		handlerUserDynamicDataUpdate = configService.getBooleanValue(DataName.handlerUserDynamicDataUpdate.toString(),0);
	}

	@Override
	public int insert(UserOnlineLog userOnlineLog) {
		if(userOnlineLog == null){
			return -1;
		}
		if(userOnlineLog.getUuid() < 1){
			return -1;
		}
		if(handlerUserDynamicDataUpdate){
			try{
				return userOnlineLogDao.insert(userOnlineLog);
			}catch(Exception e){
				logger.error("插入数据失败:" + e.getMessage());
			}
		} else {
			if(logger.isDebugEnabled()){
				logger.debug("插入UserOnlineLog缓存[" + userOnlineLog + "]");
			}
			cacheService.put(cacheName, cachePrefix + "#" + userOnlineLog.getUuid(), userOnlineLog);
		}
		return -1;
	}

	@Override
	public int update(UserOnlineLog userOnlineLog) {
		/*if(logger.isDebugEnabled()){
			logger.debug("尝试更新用户[" + userOnlineLog + "]在线记录[" + userOnlineLog.getUserOnlineLogId() + "].");
		}*/
		if(userOnlineLog == null){
			return -1;
		}
		if(userOnlineLog.getUuid() < 1){
			return -1;
		}
		if(handlerUserDynamicDataUpdate){
			try{
				return   userOnlineLogDao.update(userOnlineLog);
			}catch(Exception e){
				logger.error("更新数据失败:" + e.getMessage());
			}
		}else {
			cacheService.put(cacheName, "UserOnlinLog#" + userOnlineLog.getUuid(), userOnlineLog);
		}
		return -1;
	}


	@Override
	public int delete(int userOnlineLogId) {
		try{
			return  userOnlineLogDao.delete(userOnlineLogId);
		}catch(Exception e){
			logger.error("删除数据失败:" + e.getMessage());
		}
		return -1;
	}

	@Override
	public UserOnlineLog select(int userOnlineLogId) {
		UserOnlineLog userOnlineLog = userOnlineLogDao.select(userOnlineLogId);		
		return userOnlineLog;
	}

	@Override
	public List<UserOnlineLog> list(UserOnlineLogCriteria userOnlineLogCriteria) {
		return userOnlineLogDao.list(userOnlineLogCriteria);
	}

	@Override
	public List<UserOnlineLog> listOnPage(UserOnlineLogCriteria userOnlineLogCriteria) {
		return userOnlineLogDao.listOnPage(userOnlineLogCriteria);

	}


	@Override
	public 	int count(UserOnlineLogCriteria userOnlineLogCriteria){
		return userOnlineLogDao.count(userOnlineLogCriteria);
	}

	@Override
	/*
	 * 尝试同步用户在线记录
	 * 
	 */
	public void sync(long uuid, int onlineMode, Date date){
		
		
		
		if(onlineMode == UserExtraStatus.online.getId()){
			if(logger.isDebugEnabled()){
				logger.debug("要求刷新上线记录[" + uuid + ",模式:" + onlineMode + "]");
			}
			UserOnlineLog userOnlineLog = cacheService.get(cacheName, cachePrefix + "#" + uuid);
			if(userOnlineLog == null){
				if(logger.isDebugEnabled()){
					logger.debug("未找到用户[" + uuid + "]的最后在线记录，创建新的在线记录");
				}
				userOnlineLog = new UserOnlineLog();
				userOnlineLog.setUuid(uuid);
				userOnlineLog.setOnlineTime(new Date());
				insert(userOnlineLog);
				return;
			} else {
				if(logger.isDebugEnabled()){
					logger.debug("找到了用户[" + uuid + "]的最后在线记录[" + (userOnlineLog.getOnlineTime() == null ? "空" : simpleDateFormat.format(userOnlineLog.getOnlineTime())) + "=>" + (userOnlineLog.getOfflineTime() == null ? "空" : simpleDateFormat.format(userOnlineLog.getOfflineTime())));
				}
				if(userOnlineLog.getOfflineTime() != null){
					if(logger.isDebugEnabled()){
						logger.debug("用户[" + uuid + "]的最后在线记录离线时间不是空，强制刷新并生成新的在线记录");
					}
					syncToDb(userOnlineLog);
					userOnlineLog = new UserOnlineLog();
					userOnlineLog.setUuid(uuid);
					userOnlineLog.setOnlineTime(new Date());
					insert(userOnlineLog);
					return;
				}
			}
			return;
		}
		if(onlineMode == UserExtraStatus.offline.getId()){
			logger.debug("要求刷新离线记录[" + uuid + "]");
			UserOnlineLog userOnlineLog = cacheService.get(cacheName, cachePrefix + "#" + uuid);
			
			if(userOnlineLog == null){
				if(logger.isDebugEnabled()){
					logger.debug("未找到用户[" + uuid + "]的最后在线记录，忽略刷新离线记录的请求");
				}
				return;
			}
			if(date == null){
				date = new Date();
			}
			userOnlineLog.setOfflineTime(date);
			syncToDb(userOnlineLog);
			return;
		}
		logger.warn("未知的在线记录刷新模式:" + onlineMode);

	}
	
	@PreDestroy
	@Override
	public void flushUserOnlineLogCache(){
		List<String> keys = cacheService.listKeys(cacheName, null);
		if(keys == null || keys.size() < 1){
			if(logger.isDebugEnabled()){
				logger.debug("尝试刷新的用户动态数据缓存为空");
			}
			return;
		}
		int existCount = 0;
		int removeCount = 0;
		List<UserOnlineLog> flushList = new ArrayList<UserOnlineLog>();
		for(String key : keys){
			if(!key.startsWith(cachePrefix + "#")){
				continue;
			}

			UserOnlineLog userOnlineLog = cacheService.get(cacheName, key);
			
			if(userOnlineLog != null){
				if(userOnlineLog.getOfflineTime() != null){
					if(logger.isDebugEnabled()){
						logger.debug("在线日志[" + userOnlineLog.getUuid() + "/" + userOnlineLog.getUserOnlineLogId() + "]已有离线时间，刷新");
						flushList.add(userOnlineLog);
					}
				}
				existCount++;
			}
		}
		
		int flushCount = 0;

		if(flushList != null || flushList.size() > 0){
			for(UserOnlineLog userOnlineLog : flushList){
				if(syncToDb(userOnlineLog) == 1){
					cacheService.delete(new CacheCriteria(cacheName, cachePrefix + "#" + userOnlineLog.getUuid()));
					flushCount++;
				}
			}
		}
		
		logger.info("用户在线日志缓存中共有" + existCount + "条数据，删除已有离线时间的记录" + removeCount + "条，实际刷新[" + flushCount + "]条");
	}
	
	@Override
	public int syncToDb(UserOnlineLog userOnlineLog){
		if(logger.isDebugEnabled()){
			logger.debug("把用户在线记录[" + userOnlineLog + "]写入数据库[" + (userOnlineLog.getOnlineTime() == null ? "空" : simpleDateFormat.format(userOnlineLog.getOnlineTime())) + "=>" + (userOnlineLog.getOfflineTime() == null ? "空" : simpleDateFormat.format(userOnlineLog.getOfflineTime())));

		}
		int rs = 0;
		if(userOnlineLog.getUserOnlineLogId() > 0){
			//更新已存在的记录
			if(userOnlineLogDao.update(userOnlineLog) == 1){
				rs = 1;
			} else {
				//尝试插入
				if(userOnlineLogDao.insert(userOnlineLog) == 1){
					rs = 1;
				}
			}
		} else {
			if(userOnlineLogDao.updateSameUserAndOnlineTimeLog(userOnlineLog) == 1){
				rs = 1;
			} else {
				if(userOnlineLogDao.insert(userOnlineLog) == 1){
					rs = 1;
				}
			}
		}
		if(rs == 1){
			messageService.sendJmsDataSyncMessage(null, "userOnlineLogService", "syncToDb", userOnlineLog);
		}
		return rs;
		
		
	}
	
	@Override
	public void listCache(){
		List<String> keys = cacheService.listKeys(cacheName, null);
		if(keys == null || keys.size() < 1){
			return;
		}
		for(String key : keys){
			if(!key.startsWith(cachePrefix + "#")){
				continue;
			}
			try{
				//logger.debug("尝试获取UserOnlineLog缓存[" + key + "]");
				UserOnlineLog userOnlineLog = cacheService.get(cacheName, key);
				if(logger.isDebugEnabled()){
					logger.debug("当前在线缓存数据:" + userOnlineLog.getUuid() + (userOnlineLog.getOnlineTime() == null ? "空" : simpleDateFormat.format(userOnlineLog.getOnlineTime())) + "=>" + (userOnlineLog.getOfflineTime() == null ? "空" : simpleDateFormat.format(userOnlineLog.getOfflineTime())));
				}
				}catch(Exception e){
				//e.printStackTrace();
			}

			
		}
	}

	@Override
	@Cacheable(value=cacheName, key="'UserOnlineLog#'+ #uuid")
	public UserOnlineLog getLastOnlineLog(long uuid) {
		return userOnlineLogDao.getLastOnlineLog(uuid);
	}

	@Override
	public long getTotalOnlineTime(UserOnlineLogCriteria userOnlineLogCriteria) {
		return userOnlineLogDao.getTotalOnlineTime(userOnlineLogCriteria);
	}
}
