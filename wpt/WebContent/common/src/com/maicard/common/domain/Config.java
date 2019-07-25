package com.maicard.common.domain;

import com.maicard.annotation.EncryptStore;

@EncryptStore("configValue")
public class Config extends EisObject {

	private static final long serialVersionUID = 1L;

	private Integer configId = 0;

	private String configName;

	private String configValue;

	private String configDescription;

	private Integer flag;

	private Integer serverId = 0;

	private String category;	

	private String categoryDescription;

	public Config() {
	}

	public Config(long ownerId) {
		this.ownerId = ownerId;
	}
	
	public Config(String configName, String configValue, long ownerId) {
		this.configName = configName;
		this.configValue = configValue;
		this.configDescription = configValue;
		this.ownerId = ownerId;
	}

	

	public Integer getConfigId() {
		return configId;
	}

	public void setConfigId(Integer configId) {
		this.configId = configId;
	}

	public String getConfigName() {
		return configName;
	}

	public void setConfigName(String configName) {
		this.configName = configName;
	}

	public String getConfigValue() {
		return configValue;
	}

	public void setConfigValue(String configValue) {
		this.configValue = configValue == null ? null : configValue.trim();
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + configId;

		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Config other = (Config) obj;
		if (configId != other.configId)
			return false;

		return true;
	}

	@Override
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
				"(" + 
				"configId=" + "'" + configId + "'," + 
				"configName=" + "'" + configName + "'," + 
				"configValue=" + "'" + configValue + "'," + 
				"serverId=" + "'" + serverId + "'," + 
				"ownerId=" + "'" + ownerId + "'" + 
				")";
	}


	public String getConfigDescription() {
		return configDescription;
	}

	public void setConfigDescription(String configDescription) {
		this.configDescription = configDescription == null ? null : configDescription.trim();
	}

	public Integer getServerId() {
		return serverId;
	}

	public void setServerId(Integer serverId) {
		this.serverId = serverId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCategoryDescription() {
		return categoryDescription;
	}

	public void setCategoryDescription(String categoryDescription) {
		this.categoryDescription = categoryDescription;
	}

}
