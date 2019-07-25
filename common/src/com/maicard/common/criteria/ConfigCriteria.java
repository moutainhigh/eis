package com.maicard.common.criteria;

import com.maicard.common.base.Criteria;

public class ConfigCriteria extends Criteria {

	private static final long serialVersionUID = 1L;
	
	private int configId;
	private boolean allConfig;	//true则忽略serverId参数，读取所有serverId配置
	
	
	private String configName;
		
	private int serverId;
	private String configDescription;
	private String configValue;
	private String flag;
	private String[] category;
	
	public ConfigCriteria() {
	}
	public ConfigCriteria(long ownerId) {
		this.ownerId = ownerId;
	}
	
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	
	public String getConfigValue() {
		return configValue;
	}

	public void setConfigValue(String configValue) {
		this.configValue = configValue;
	}

	public String getConfigDescription() {
		return configDescription;
	}

	public void setConfigDescription(String configDescription) {
		this.configDescription = configDescription;
	}



	public int getConfigId() {
		return configId;
	}

	public void setConfigId(int configId) {
		this.configId = configId;
	}

	public String getConfigName() {
		return configName;
	}

	public void setConfigName(String configName) {
		this.configName = configName;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public boolean isAllConfig() {
		return allConfig;
	}

	public void setAllConfig(boolean allConfig) {
		this.allConfig = allConfig;
	}

	public String[] getCategory() {
		return category;
	}

	public void setCategory(String... category) {
		this.category = category;
	}

}
