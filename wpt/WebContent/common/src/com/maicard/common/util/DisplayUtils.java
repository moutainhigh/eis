package com.maicard.common.util;

import org.apache.commons.lang.StringUtils;

public class DisplayUtils {
	
	public static final String[] LEVEL = new String[] {"none","system","platform","user","open"};

	public static boolean canDisplay(String currentLevel, String requireLevel) {
		if(StringUtils.isBlank(currentLevel) || StringUtils.isBlank(requireLevel) || requireLevel.equalsIgnoreCase("open") || currentLevel.equalsIgnoreCase("open")) {
			return true;
		}
		int currentLevelNum = 0;
		int requireLevelNum = 0;
		int i = 0;
		for(String level : LEVEL) {
			if(currentLevel.equalsIgnoreCase(level)) {
				currentLevelNum = i;
			} 
			if(requireLevel.equalsIgnoreCase(level)) {
				requireLevelNum = i;
			} 
			
		}
		return requireLevelNum <= currentLevelNum;
	}

}
