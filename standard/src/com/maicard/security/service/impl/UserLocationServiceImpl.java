package com.maicard.security.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maicard.common.base.BaseService;
import com.maicard.common.service.CenterDataService;
import com.maicard.common.util.JsonUtils;
import com.maicard.common.util.NumericUtils;
import com.maicard.common.util.Paging;
import com.maicard.security.criteria.UserLocationCriteria;
import com.maicard.security.domain.UserLocation;
import com.maicard.security.service.UserLocationService;
import com.maicard.standard.KeyConstants;

/**
 * 管理用户当前在哪里、做什么
 *
 *
 * @author NetSnake
 * @date 2017年3月11日
 *
 */
public class UserLocationServiceImpl  extends BaseService implements UserLocationService{
	

	@Resource
	private CenterDataService centerDataService;
	
	private final ObjectMapper om = JsonUtils.getNoDefaultValueInstance();
	
	private final int TTL = 3600 * 24;
	
	

	
	@Override
	public void setUserLocation(UserLocation playerLocation) {
		String key =  KeyConstants.PLAYER_LOCATION_KEY_PREFIX + "#" + playerLocation.getOwnerId() + "#" + playerLocation.getUuid();
		if(playerLocation.isWithLocationTypeKey()){
			key += "#" + playerLocation.getLocationType();
		} 
		if(playerLocation.getWsSessionId() != null && playerLocation.getWsSessionId().equals("KEEP")){
			//获取旧的位置信息
			UserLocation _oldLocation = this.getUserLocation(playerLocation.getUuid(), playerLocation.getOwnerId(), playerLocation.getLocationType());
			if(_oldLocation != null){
				playerLocation.setWsSessionId(_oldLocation.getWsSessionId());
			}
		}
		
		String value = null;
		try {
			value = om.writeValueAsString(playerLocation);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		logger.debug("写入玩家当前位置数据到缓存:" + key + "=>" + value);
		centerDataService.setForce(key, value, TTL);		
		
		String countKey =  KeyConstants.PLAYER_LOCATION_COUNT_KEY + "#" + playerLocation.getOwnerId();
		centerDataService.increaseBy(countKey, 1, 1, TTL);

		if(playerLocation.getLocationType() != null && !playerLocation.getLocationType().equals("OFFLINE")){
			//写入玩家在线表
			String tableName = KeyConstants.ONLINE_USER + "_" + playerLocation.getLocationType().toUpperCase();
			logger.debug("向玩家在线表[" + tableName + "]中写入数据:" + playerLocation.getUuid());
			//先删除以保证无重复
			centerDataService.removeFromList(tableName, 0, String.valueOf(playerLocation.getUuid()));
			centerDataService.addToList(tableName, String.valueOf(playerLocation.getUuid()));

		}
	}
	
	@Override
	public void deleteUserLocation(long playerId, long ownerId, String locationType) {
		String key =  KeyConstants.PLAYER_LOCATION_KEY_PREFIX + "#" + ownerId + "#" + playerId;
		if(StringUtils.isNotBlank(locationType)){
			key += "#" + locationType;
		} 		
		logger.debug("删除玩家当前位置数据:" + key);
		centerDataService.delete(key);
		String countKey =  KeyConstants.PLAYER_LOCATION_COUNT_KEY + "#" + ownerId;
		centerDataService.increaseBy(countKey, -1, 0, TTL);

		if(locationType != null){
			//从玩家在线表中删除
			String tableName = KeyConstants.ONLINE_USER + "_" + locationType.toUpperCase();
			logger.debug("删除玩家在线表[" + tableName + "]中的在线数据:" + playerId);
			centerDataService.removeFromList(tableName, 0, String.valueOf(playerId));

		}
	}
	
	@Override
	public UserLocation getUserLocation(long playerId, long ownerId, String locationType) {
		String key =  KeyConstants.PLAYER_LOCATION_KEY_PREFIX + "#" + ownerId + "#" + playerId;
		if(StringUtils.isNotBlank(locationType)){
			key += "#" + locationType;
		} 
		
		String value = centerDataService.get(key);		
		logger.debug("获取玩家当前位置REDIS数据:" + key + "=>" + value);
		if(value == null){
			return null;
		}
		try {
			return om.readValue(value, UserLocation.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public int count(long ownerId){
		String countKey =  KeyConstants.PLAYER_LOCATION_COUNT_KEY + "#" + ownerId;
		String value = centerDataService.get(countKey);		
		if(value == null){
			//动态计算数量
			String key =  KeyConstants.PLAYER_LOCATION_KEY_PREFIX + "#" + ownerId + "*";
			Set<String> keys = centerDataService.listKeys(key);
			int count = 0;
			if(keys == null ||  keys.size() < 1){
				count = 0;
			} else {
				count = keys.size();
			}
			centerDataService.setForce(countKey, String.valueOf(count), TTL);
			return count;
		}
		if(NumericUtils.isIntNumber(value)){
			return Integer.parseInt(value);
		}
		return 0;
	}

	@Override
	public List<UserLocation> listOnPage(UserLocationCriteria playerLocationCriteria) {
		Assert.notNull(playerLocationCriteria,"玩家位置查询条件不能为空");
		Assert.isTrue(playerLocationCriteria.getOwnerId() > 0,"玩家位置查询条件中的ownerId不能为0");
		Assert.notNull(playerLocationCriteria.getPaging(),"玩家位置查询条件中的paging不能为空");
		
		String key =  KeyConstants.PLAYER_LOCATION_KEY_PREFIX + "#" + playerLocationCriteria.getOwnerId() + "*";
		Set<String> keys = centerDataService.listKeys(key);
		if(keys == null ||  keys.size() < 1){
			return Collections.emptyList();
		}
		
		int totalCount = keys.size();
		Paging paging = playerLocationCriteria.getPaging();
		int currentPaging = paging.getCurrentPage();
		
		if(currentPaging < 1){
			currentPaging = 1;
		} 
		
		
		paging.setTotalResults(keys.size());
		int beginOffset = paging.getFirstResult();
		int endOffset = paging.getLastRownum();
		logger.debug("当前共有" + totalCount + ",当前返回第:" + currentPaging + "页，从" + beginOffset + "到" + endOffset + "的数据");
		String[] arrayKeys = new String[keys.size()];
		arrayKeys = keys.toArray(arrayKeys);
		
		/*int startPostion = rowPerPage * currentPaging - rowPerPage;
		if(keys.size() <= startPostion){
			logger.debug("当前一共有" + keys.size() + "条数据，第" + currentPaging + "将从第" + startPostion + "开始，已无更多数据");
			return null;
		}
		String[] arrayKeys = new String[keys.size()];
		arrayKeys = keys.toArray(arrayKeys);
		

		int totalPage = keys.size() / rowPerPage;
		int maxResult = 0;
		if(currentPaging < totalPage){
			maxResult = rowPerPage;
		} else {
			maxResult = keys.size() % rowPerPage;
		}*/

		List<UserLocation> list = new ArrayList<UserLocation>();
		try {
			for(int i = beginOffset; i < endOffset; i++){
				String locationKey = arrayKeys[i];
				String locationData = centerDataService.get(locationKey);
				if(locationData != null){
					list.add(om.readValue(locationData, UserLocation.class));
					//long uuid = Long.parseLong(locationKey.split("#")[2]);
					//list.add(new UserLocation(uuid,locationData));
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
