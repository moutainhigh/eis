/**
 * 
 */
package com.maicard.standard;

/**
 * 系统配置的类别
 * 
 *
 * @author NetSnake
 * @date 2013-9-12 
 */
public enum ConfigCategory {
	unknown("未知"),
	jms("消息总线"),
	handler("分布式"),
	security("安全"),
	site("网站"),
	staticize("静态化"),
	system("系统"),
	transaction("交易"),
	business("业务"),
	personal("个人");
	
	private String description;
	
	private ConfigCategory(String description){
		this.description = description;
	}
	
	public ConfigCategory findByCode(String code){
		for(ConfigCategory category : ConfigCategory.values()){
			if(category.getCode().equals(code)){
				return category;
			}
		}
		return unknown;
	}

	public String getCode() {
		return name();
	}

	public String getDescription() {
		return description;
	}
	
	

}
