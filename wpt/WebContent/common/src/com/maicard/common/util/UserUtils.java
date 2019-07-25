package com.maicard.common.util;

import java.text.DecimalFormat;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import com.maicard.standard.SecurityStandard.UserTypes;

public class UserUtils {
	
	private static int cryptedPasswordLength = 64;
	
	public static String correctPassword(String password){
		if(StringUtils.isBlank(password)){
			return null;
		}
		if(password.length() == cryptedPasswordLength){
			return password;
		}
		return Crypt.passwordEncode(password);
		
	}
	
	public static String correctLegacyPassword(String password){
		if(StringUtils.isBlank(password)){
			return null;
		}
		if(password.length() == cryptedPasswordLength){
			return password;
		}
		return Crypt.legacyPasswordEncode(password);
		
	}
	
	public static boolean isLegacyPassword(String encryptedPassword){
		if(StringUtils.isBlank(encryptedPassword)){
			return false;
		}
		for(int i = 0; i <  encryptedPassword.length(); i++){
			if( ((int)encryptedPassword.charAt(i)) >= 65 && ((int)encryptedPassword.charAt(i)) <= 90){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 根据UUID的长度判断他是前端用户还是合作商户<br>
	 * 前端用户一般UUID会大于8位数，前三位为服务器ID<br>
	 * 而商户一般小于8位
	 * 
	 */
	public static int getUserType(long uuid){
		if(uuid > 10000000){
			return UserTypes.frontUser.getId();
		} else {
			return UserTypes.partner.getId();
		}
	}
	
	/**
	 * 一个前端用户的UUID最短为8位，由3位服务器ID和最少5位的自增ID组成
	 */
	public static long formatFrontUuid(int serverId, long uuid) {
		Assert.isTrue(serverId >= 100,"前端服务器ID必须大于99");
		return Long.parseLong(serverId + new DecimalFormat("#00000").format(uuid));
	}

}
