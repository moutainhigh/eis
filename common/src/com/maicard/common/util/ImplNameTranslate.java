package com.maicard.common.util;

import org.apache.commons.lang.StringUtils;

import com.maicard.standard.CommonStandard;

public class ImplNameTranslate {
	
	public static String translate(String orgName){
		return StringUtils.uncapitalize(orgName).replace(CommonStandard.implBeanNameSuffix, "");	
	}

}
