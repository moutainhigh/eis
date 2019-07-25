package com.maicard.security.service.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.maicard.annotation.IgnoreJmsDataSync;
import com.maicard.common.base.BaseService;
import com.maicard.common.criteria.CacheCriteria;
import com.maicard.common.domain.Location;
import com.maicard.common.service.CacheService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.LocationService;
import com.maicard.security.dao.FrontUserDao;
import com.maicard.security.dao.PartnerDao;
import com.maicard.security.dao.SysUserDao;
import com.maicard.security.domain.OperateLog;
import com.maicard.security.domain.User;
import com.maicard.security.domain.UserDynamicData;
import com.maicard.security.service.OperateLogService;
import com.maicard.security.service.UserDynamicDataService;
import com.maicard.security.service.UserOnlineLogService;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.Operate;
import com.maicard.standard.SecurityStandard.UserExtraStatus;
import com.maicard.standard.SecurityStandard.UserTypes;

@Service
public class UserDynamicDataServiceImpl extends BaseService implements UserDynamicDataService {

	@Resource
	private CacheService cacheService;
	@Resource
	private ConfigService configService;
	@Resource
	private FrontUserDao frontUserDao;
	@Resource
	private PartnerDao partnerDao;
	@Resource
	private SysUserDao sysUserDao;
	@Resource
	private LocationService locationService;
	@Resource
	private OperateLogService operateLogService;
	@Resource
	private UserOnlineLogService userOnlineLogService;




	private final String cacheName = CommonStandard.cacheNameUser;

	private int userTimeToOffline = 0;

	@PostConstruct
	public void init(){
		userTimeToOffline = configService.getIntValue(DataName.userTimeToOffline.toString(),0);
		if(userTimeToOffline == 0){
			userTimeToOffline = 300; //默认5分钟无任何操作视为下线
		}

	}

	@Override
	public UserDynamicData generateFromUser(User user){
		if(user == null){
			return null;
		}
		if(user.getUuid() == 0){
			return null;
		}
		UserDynamicData userDynamicData= new UserDynamicData();
		userDynamicData.setUserTypeId(user.getUserTypeId());
		userDynamicData.setUuid(user.getUuid());
		if(user.getLastLoginTimestamp() == null){
			userDynamicData.setLastLogin(new Date());
		} else {
			userDynamicData.setLastLogin(user.getLastLoginTimestamp());
		}
		if(user.getLastLoginIp() != null){
			userDynamicData.setLastLoginIp(user.getLastLoginIp());
		}
		userOnlineLogService.sync(userDynamicData.getUuid(), UserExtraStatus.online.getId(), new Date());
		/*LocationCriteria locationCriteria = new LocationCriteria();
			locationCriteria.setObjectType(ObjectType.user.getCode());
			locationCriteria.setObjectId(user.getUuid());
			userDynamicData.setLocation(locationService.select(locationCriteria));*/
		return userDynamicData;

	}

	@Override
	@CachePut(value=cacheName, key="'UserDynamicData#' + #userDynamicData.uuid")
	@IgnoreJmsDataSync
	public UserDynamicData insert(UserDynamicData userDynamicData) {
		if(logger.isDebugEnabled()){
			logger.debug("增加终端用户动态数据[" + userDynamicData.getUuid() + "]");
		}
		return userDynamicData;

	}



	@Override
	@CachePut(value=cacheName, key="'UserDynamicData#' + #userDynamicData.uuid")
	@IgnoreJmsDataSync
	public UserDynamicData update(UserDynamicData userDynamicData) {
		if(logger.isDebugEnabled()){
			logger.debug("更新终端用户动态数据[" + userDynamicData.getUuid() + "]");
		}
		userOnlineLogService.sync(userDynamicData.getUuid(), UserExtraStatus.online.getId(), new Date());
		return userDynamicData;	
	}

	@Override
	@CacheEvict(value=cacheName, key="'UserDynamicData#' + #userDynamicData.uuid")
	@IgnoreJmsDataSync
	public int delete(UserDynamicData userDynamicData) {
		if(logger.isDebugEnabled()){
			logger.debug("删除终端用户动态数据[" + userDynamicData.getUuid() + "]");
		}
		return 1;
	}


	@Override
	@Cacheable(value=cacheName, key="'UserDynamicData#' + #uuid")
	public UserDynamicData select(int userTypeId, long uuid) {
		User user = null;
		if(userTypeId == UserTypes.sysUser.getId()){
			user = sysUserDao.select(uuid);
		} else if(userTypeId == UserTypes.partner.getId()){
			user = partnerDao.select(uuid);
		} else {
			user = frontUserDao.select(uuid);
		}
		if(user == null){
			return null;
		}
		UserDynamicData userDynamicData = generateFromUser(user);		
		return userDynamicData;
	}

	@PreDestroy
	@Override
	@IgnoreJmsDataSync
	public void flushUserVariableDataCache() {
		userOnlineLogService.listCache();
		List<String> keys = cacheService.listKeys(cacheName,null);
		if(keys == null || keys.size() < 1){
			if(logger.isDebugEnabled()){
				logger.debug("尝试刷新的用户动态数据缓存为空");
			}
			return;
		}
		if(logger.isDebugEnabled()){
			logger.debug("当前用户缓存共有[" + keys.size() + "]条");
		}
		List<Location> flushLocationList = new ArrayList<Location>();
		int userCount = 0;
		int removeCount = 0;
		for(String key : keys){
			if(!key.startsWith("UserDynamicData#")){
				continue;
			}
			UserDynamicData userDynamicData = cacheService.get(cacheName,key);

			if(userDynamicData != null){
				userCount++;
				if(userDynamicData.getLocation() != null){
					flushLocationList.add(userDynamicData.getLocation());
				}
				//更新用户数据到数据库
				long currentLastLogin = (new Date().getTime() - userDynamicData.getLastLogin().getTime()) / 1000;
				if(logger.isDebugEnabled()){
					logger.debug("当前动态数据[" + userDynamicData + "]的最后登录时间是:" + userDynamicData.getLastLogin() + ",过去了" + currentLastLogin + "秒");
				}
				if(currentLastLogin > userTimeToOffline){
					//将用户设置为离线，并删除已超时的用户
					userDynamicData.setExtraStatus(UserExtraStatus.offline.getId());
					writeUserDynamicData(userDynamicData);
					userOnlineLogService.sync(userDynamicData.getUuid(), UserExtraStatus.offline.getId(), new Date());
					if(logger.isDebugEnabled()){
						logger.debug("用户[" + userDynamicData.getUuid() + "]已超过[" + userTimeToOffline + "]秒未更新，从缓存中清除.");
					}
					if(userDynamicData.getLocation() != null){
						
						OperateLog operateLog = new OperateLog(
								userDynamicData.getLocation().getLocationType(),
								String.valueOf(userDynamicData.getLocation().getObjectId()),
								userDynamicData.getUuid(),
								Operate.logout.getCode(),
								"成功",
								null,
								userDynamicData.getLastLoginIp(),
								0,0
								);
						try{
							operateLogService.insert(operateLog);
						}catch(Exception e){
							e.printStackTrace();
						}
					}
					cacheService.delete(new CacheCriteria(cacheName, key));
					removeCount++;
				} 
			}
		}
		logger.info("用户动态数据缓存中共有" + userCount + "个数据，删除了超过" + userTimeToOffline + "秒无活动的用户" + removeCount + "个");
		locationService.updateBatch(flushLocationList);

	}

	private int writeUserDynamicData(UserDynamicData userDynamicData ){
		if(userDynamicData == null){
			return -1;
		}
		logger.debug("尝试更新用户[" + userDynamicData.getUuid() + "/" + userDynamicData.getUserTypeId() + "]的动态数据到数据库");
		if(userDynamicData.getUserTypeId() == UserTypes.sysUser.getId()){
			return sysUserDao.updateDynamicData(userDynamicData);
		} else 		if(userDynamicData.getUserTypeId() == UserTypes.partner.getId()){
			return partnerDao.updateDynamicData(userDynamicData);
		} else {
			return frontUserDao.updateDynamicData(userDynamicData);
		}

	}

	@Override
	public void applyToUser(User user) {
		if(user == null){
			return;
		}
		String key = "UserDynamicData#" + user.getUuid();

		UserDynamicData userDynamicData = cacheService.get(cacheName, key);
		
		if(userDynamicData == null){
			logger.info("无法将缓存数据[" + key + "]转换为UserDynamicData对象");
			return;
		}
		user.setLastLoginIp(userDynamicData.getLastLoginIp());
		user.setLastLoginTimestamp(userDynamicData.getLastLogin());

	}

	@Override
	public void applyLocation(long uuid, Location location) {

		String key = "UserDynamicData#" + uuid;
		
		UserDynamicData userDynamicData = cacheService.get(cacheName, key);
		
		if(userDynamicData == null){
			logger.info("无法将缓存数据[" + key + "]转换为UserDynamicData对象");
			return;
		}
		userDynamicData.setLocation(location);
		if(logger.isDebugEnabled()){
			logger.debug("为用户设置新的位置信息:" + location);
		}
		cacheService.put(cacheName, key, userDynamicData);

	}



}
