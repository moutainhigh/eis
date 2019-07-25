package com.maicard.common.service;

import java.util.List;

import com.maicard.common.criteria.ConfigCriteria;
import com.maicard.common.domain.Config;

public interface ConfigService {

	int insert(Config config);

	int update(Config config);

	int delete(int configId);
	
	Config select(int configId);
	
	Config get(String configName, long ownerId);
	

	List<Config> list(ConfigCriteria configCriteria);

	List<Config> listOnPage(ConfigCriteria configCriteria);
	
	int count(ConfigCriteria configCriteria);

	boolean getBooleanValue(String configName, long ownerId);

	int getIntValue(String configName, long ownerId);
	
	int getServerId();

	String getSystemCode();

	long getLongValue(String configName, long ownerId);

	String getMoneyNameByCode(String destMoneyType, long ownerId);

	String getValue(String string, long ownerId);

	float getFloatValue(String configName, long ownerId);




}
