package com.maicard.common.util;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maicard.standard.CommonStandard;

public class EncodeUtils {
	
	protected static final Logger logger = LoggerFactory.getLogger(ClassUtils.class);

	public static String encode(String src){
		try {
			logger.debug("编码前:" + src);
			return java.net.URLEncoder.encode(StringEscapeUtils.escapeHtml(src),CommonStandard.DEFAULT_ENCODING);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		return null;
	}

	public static String decode(String src){
		try {
			return StringEscapeUtils.unescapeHtml(java.net.URLDecoder.decode(src,CommonStandard.DEFAULT_ENCODING));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		return null;
	}

}
