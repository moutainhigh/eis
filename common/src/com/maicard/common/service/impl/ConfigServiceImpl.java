package com.maicard.common.service.impl;

import java.util.List;


import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.criteria.ConfigCriteria;
import com.maicard.common.domain.Config;
import com.maicard.common.service.ConfigService;



@Service
public class ConfigServiceImpl extends BaseService implements ConfigService {

	@Override
	public int insert(Config config) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Config config) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(int configId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Config select(int configId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Config get(String configName, long ownerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Config> list(ConfigCriteria configCriteria) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Config> listOnPage(ConfigCriteria configCriteria) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int count(ConfigCriteria configCriteria) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean getBooleanValue(String configName, long ownerId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getIntValue(String configName, long ownerId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getServerId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getSystemCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getLongValue(String configName, long ownerId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getMoneyNameByCode(String destMoneyType, long ownerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getValue(String string, long ownerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getFloatValue(String configName, long ownerId) {
		// TODO Auto-generated method stub
		return 0;
	}

}
