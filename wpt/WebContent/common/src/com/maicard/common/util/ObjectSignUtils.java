package com.maicard.common.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maicard.common.domain.EisObject;

/**
 * 对Eis对象生成签名
 *
 *
 * @author NetSnake
 * @date 2015年12月25日
 *
 */
public class ObjectSignUtils {

	protected static final Logger logger = LoggerFactory.getLogger(ObjectSignUtils.class);

	/*
	 * 为对象及其数据生成签名，防止篡改
	 */
	public static void makeSign(EisObject eisObject) {
		String src = null;
		src = JsonUtils.toStringFull(eisObject);

		if(src == null){
			logger.error("无法将Scene对象[" + eisObject + "]转换为JSON字符串");
			return;
		}
		String sign = DigestUtils.sha256Hex(src);
		eisObject.setSign(sign);
		return;		
	}

	public static boolean verifySign(EisObject eisObject) {
		if(eisObject.getSign() == null){
			logger.info("尝试校验的EisObject没有签名");
			return false;
		}
		String oldSign = eisObject.getSign();
		EisObject o2 = eisObject.clone();
		o2.setSign(null);
		String src = JsonUtils.toStringFull(o2);
		
		if(src == null){
			logger.error("无法将Scene对象[" + o2 + "]转换为JSON字符串");
			return false;
		}
		String sign = DigestUtils.sha256Hex(src);
		if(sign.equals(oldSign)){
			return true;
		}
		return false;
	}


}
