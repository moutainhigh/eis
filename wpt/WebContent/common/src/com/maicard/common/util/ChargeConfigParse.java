package com.maicard.common.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class ChargeConfigParse {
	public static Map<String,String> parseChargeConfig(String chargeConfig){
		if(StringUtils.isBlank(chargeConfig)){
			return null;
		}
		String[] data = chargeConfig.split("\\|");
		if(data == null || data.length < 1){
			data = chargeConfig.split(",");
		} else {
			data = data[data.length - 1].split(",");
		}
		Map<String,String> configMap = new HashMap<String,String>();
		for(String config : data){
			String[] kv = config.split("=");
			if(kv != null && kv.length == 2){
				configMap.put(kv[0], kv[1]);
			}
		}
		return configMap;
	}
}
