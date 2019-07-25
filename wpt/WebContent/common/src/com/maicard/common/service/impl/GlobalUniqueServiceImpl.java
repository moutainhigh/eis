package com.maicard.common.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseService;
import com.maicard.common.criteria.GlobalUniqueCriteria;
import com.maicard.common.dao.GlobalUniqueDao;
import com.maicard.common.domain.GlobalUnique;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.CenterDataService;
import com.maicard.common.service.GlobalUniqueService;
import com.maicard.common.util.NumericUtils;

import static com.maicard.common.criteria.GlobalUniqueCriteria.CACHE_PREFIX;

@Service
public class GlobalUniqueServiceImpl extends BaseService implements GlobalUniqueService {


	@Resource
	private ConfigService configService;
	@Resource
	private GlobalUniqueDao globalUniqueDao;


	@Resource
	private CenterDataService centerDataService;
	
	final long MAX_ORDER_RAND_NUMBER = 99999999;


	@Override
	public boolean exist(GlobalUnique globalUnique) {
		logger.debug("检查全局唯一数据[" + globalUnique + "]是否存在");
		if(StringUtils.isBlank(globalUnique.getData())){
			logger.debug("全局唯一数据中的data为空，无法检查，返回不存在");
			return false;
		}
		int distributedCount = getDistributedCount();

		String key = CACHE_PREFIX + "#" + globalUnique.getOwnerId() + "#" + globalUnique.getData();

		if(distributedCount > 0){
			if(centerDataService.get(key) == null){
				logger.debug("在中央缓存中未找到键:" + key + "," + globalUnique + "将视为不存在");
				return false;
			}
			logger.debug("在中央缓存中找到了键:" + key + "," + globalUnique + "，数据已存在");
			return true;

		}
		try{
			boolean localResult = globalUniqueDao.exist(globalUnique);
			logger.debug("检查本地唯一性数据[" + globalUnique + "]，该数据在本地" + (localResult ? "已经" : "不" + "存在"));
			if(localResult){
				logger.debug("唯一性数据[" + globalUnique + "]，该数据在本地存在，尝试放入中央缓存");
				boolean setSuccess = centerDataService.setIfNotExist(key, globalUnique.getData(), -1);
				if(setSuccess){
					plusDistributedCount(1);
				}
			}
			return localResult;
		}catch(Exception e){
			e.printStackTrace();
			return true;
		}
	}

	@Override
	public boolean create(GlobalUnique globalUnique) {
		boolean createSuccess = false;
		String key = CACHE_PREFIX + "#" + globalUnique.getOwnerId() + "#" + globalUnique.getData();
		if(!centerDataService.setIfNotExist(key, globalUnique.getData(), -1)){
			logger.debug("在中央缓存中未能成功插入键:" + key + "," + globalUnique + "将视为已存在");
			return false;
		}	
		logger.debug("在中央缓存中成功插入键:" + key + "," + globalUnique);
		plusDistributedCount(1);
		createSuccess = true;

		try{
			if( globalUniqueDao.create(globalUnique)){
				return true;
			} 
		}catch(Exception e){
		}
		return createSuccess;
	}

	@Override
	public void syncDbToDistributed() throws Exception{
		if(centerDataService == null){
			logger.warn("当前系统未配置中央缓存服务");
			return;
		}
		List<GlobalUnique> list = globalUniqueDao.list(new GlobalUniqueCriteria());
		if(list == null || list.size() < 1){
			logger.debug("当前数据库中没有全局唯一约束数据");
			return;
		}
		Map<String,String> map = new HashMap<String,String>();
		for(GlobalUnique globalUnique : list){
			map.put(CACHE_PREFIX + "#" + globalUnique.getOwnerId() + "#" + globalUnique.getData(),  globalUnique.getData());
		}
		centerDataService.setBatch(map, -1, true);
		centerDataService.setForce(CACHE_PREFIX + "#COUNT", String.valueOf(map.size()), -1);
		logger.debug("写入" + map.size() + "条全局唯一约束数据到中央缓存");
	}

	@Override
	public void syncDistributedToDb() {
		if(centerDataService == null){
			logger.warn("当前系统未配置中央缓存服务");
			return;
		}


		String pattern = CACHE_PREFIX + "*";

		Set<String> keys =centerDataService.listKeys(pattern);
		if(keys == null || keys.size() < 1){
			logger.debug("中央缓存中没有任何全局唯一数据项");
			return;
		}
		logger.debug("中央缓存中的全局唯一数据项有" + keys.size() + "条");
		int addCount = 0;
		for(String key : keys){
			String[] data = key.split("#");
			if(data == null || data.length < 3){
				continue;
			}
			long ownerId = Long.parseLong(data[1]);
			String value = data[2];
			GlobalUnique globalUnique = new GlobalUnique(value, ownerId);
			int rs = globalUniqueDao.insertIgnore(globalUnique);
			if(rs == 1){
				addCount++;
			}
		}

		logger.debug("写入" + addCount + "条全局唯一约束数据到本机");		
	}

	@Override
	public int getDistributedCount(){
		if(centerDataService == null){
			logger.warn("当前系统未配置中央缓存服务");
			return -1;
		}
		String key = GlobalUniqueCriteria.CACHE_PREFIX + "#COUNT";
		String value = centerDataService.get(key);
		if(value == null || !NumericUtils.isNumeric(value)){
			return -1;
		}
		return Integer.parseInt(value);
	}

	@Override
	public long plusDistributedCount(int count){
		
		String key = GlobalUniqueCriteria.CACHE_PREFIX + "#COUNT";

		return centerDataService.increaseBy(key, count, count, 0);
	}
	
	@Override
	public long incrOrderSequence(int count){
		
		String key = GlobalUniqueCriteria.CACHE_PREFIX + "#ORDER_SEQUENCE";

		long rs = centerDataService.increaseBy(key, count, count, 0);
		if(rs > MAX_ORDER_RAND_NUMBER) {
			centerDataService.setForce(key, "1", 0);
			return 1;
		}
		return rs;
	}

	@Override
	public int count(GlobalUniqueCriteria globalUniqueCriteria) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(GlobalUnique globalUnique) {
		Assert.notNull(globalUnique, "尝试删除的唯一数据不能为空");
		Assert.notNull(globalUnique.getData(), "尝试删除的唯一数据不能为空");
		Assert.isTrue(globalUnique.getOwnerId() > 0, "尝试删除的唯一数据的ownerId不能为0");

		logger.debug("删除本地全局唯一数据:" + globalUnique);
		globalUniqueDao.delete(globalUnique);

		if(globalUnique.getSyncFlag() == 0){
			String key = CACHE_PREFIX + "#" + globalUnique.getOwnerId() + "#" + globalUnique.getData();
			logger.debug("删除中心缓存数据:" + key);
			centerDataService.delete(key);
		}
		return 1;
	}


}
