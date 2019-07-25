package com.maicard.common.util;

import java.text.DecimalFormat;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RandStrUtils {
	
	static DecimalFormat df = new DecimalFormat("0");
	static protected final Logger logger = LoggerFactory.getLogger(RandStrUtils.class);

	
	public  static String[] generate(int snLength, int passwordLength, boolean numericOnly) {
		
		String sn = null;
		String password = null;
		if(numericOnly){
			int length = 0;
			int count = 1;
			if(snLength > 10){
				length = snLength / 2;
				count = 2;
			} else {
				length = snLength;
			}
			StringBuffer sb = new StringBuffer();
			for(int j = 0; j < count; j++){
				double i = Math.pow(10,length);
				String baseString = df.format(i);
				long base = Long.parseLong(baseString);
				int randLength = 0;
				if(base >= Integer.MAX_VALUE){
					randLength = 11;
				} else {
					randLength = baseString.length();
				}

				long rand1 = 10 * (1+ RandomUtils.nextInt(Integer.parseInt(df.format(0.9 * Math.pow(10, randLength-2)))));
				sb.append(df.format(i - rand1));
				logger.info("baseString=" + baseString + ",randLength=" + randLength + ",rand1=" + rand1 + ",sn=" + sb.toString());

			}
			sn = sb.toString();
			length = 0;
			count = 1;
			sb.setLength(0);
			if(passwordLength > 0){
				if(passwordLength > 10){
					length = passwordLength / 2;
					count = 2;
				} else {
					length = passwordLength;
				}
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				double i = Math.pow(10,length);
				String baseString = df.format(i);
				long base = Long.parseLong(baseString);
				int randLength = 0;
				if(base >= Integer.MAX_VALUE){
					randLength = 11;
				} else {
					randLength = baseString.length();
				}

				long rand1 = 10 * (1 + RandomUtils.nextInt(Integer.parseInt(df.format(0.9 * Math.pow(10, randLength-2)))));
				sb.append(df.format(i - rand1));
			}
			password = sb.toString();

		} else {
			int rand = RandomUtils.nextInt();
			String salt = String.valueOf(System.currentTimeMillis() + rand);

			String src = DigestUtils.md5Hex(salt);
			if(src.length() >= (snLength + passwordLength)){
				sn = src.substring(0, snLength);
				if(passwordLength > 0){
					password = src.substring(snLength, snLength+passwordLength);
				}
			} else {
				sn = src.substring(0, snLength);
				if(passwordLength > 0){

					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					rand = RandomUtils.nextInt();
					salt = String.valueOf(System.currentTimeMillis() + rand);
					src = DigestUtils.md5Hex(String.valueOf(salt));
					password = src.substring(0,passwordLength);
				}

			}

		}
		String[] result = new String[2];
		result[0] = sn.toUpperCase();
		result[1] = password.toUpperCase();
		return result;
	}
}
