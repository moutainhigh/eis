package com.maicard.common.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.maicard.common.base.BaseService;
import com.maicard.common.domain.JmsObject;
import com.maicard.common.service.CenterDataService;

/**
 * 基于REDIS中央缓存的分布式锁
 *
 *
 * @author NetSnake
 * @date 2016年4月26日
 *
 */
public class CenterDataServiceImpl extends BaseService implements CenterDataService {



	@Override
	public long delete(String key) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String get(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getExclusive(String key, boolean deleteAfterGet) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long increaseBy(String key, int offset, long backupNumber, long timeSec) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setForce(String key, String value, long timeoutSec) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean setIfNotExist(String key, String value, long timeoutSec) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setBatch(Map<String, String> map, long timeoutSec, boolean force) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<String> listKeys(String pattern) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean setIfSameSign(String key, JmsObject object, long timeoutSec) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void unsetSignIfSameSign(String key, JmsObject object, long lockSec) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHmValue(String hmNameKey, String mapKey, Object value, int timeoutSec) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T>T getHmValue(String tableName, String mapKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long countHm(String tableName) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Set<Object> getHmKeys(String tableName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean lock(String key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean lock(String key, long lockSec) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String pushFromZQueue(String key, boolean rerverse) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addToZQueue(String key, String value, long score) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <E> List<E> getHmValues(String tableName){
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long addToList(String key, String value) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long countList(String listName) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<String> listOnPage(String listName, int begin, int end) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long removeFromList(String listName, long index, String value) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Set<String> pushSetFromZQueue(String key, boolean reverse, int begin, int end) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setHmPlainValue(String tableName, String keyInMap, String text, int timeoutSec) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getHmPlainValue(String tableName, String keyInMap) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String sPop(String setName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long sAdd(String setName, String value) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long sRemove(String setName, String value) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Set<String> sList(String setName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long sCount(String setName) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Set<String> zRange(String key, long beginScore, long endScore) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long zCount(String key, long beginScore, long endScore) {
		// TODO Auto-generated method stub
		return 0;
	}


}
