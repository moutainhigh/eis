package com.maicard.common.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * 类似短网址生成，用来作为用户的邀请码、识别码等
 * @author NetSnake
 * @date 2012-6-4
 */
public class ShortMd5 {

	public static String encode(String src){
		String key = String.valueOf(System.currentTimeMillis());                 //自定义生成MD5加密字符串前的混合KEY 
		String[] chars = new String[]{          //要使用生成URL的字符 
				"a","b","c","d","e","f","g","h", 
				"i","j","k","l","m","n","o","p", 
				"q","r","s","t","u","v","w","x", 
				"y","z","0","1","2","3","4","5", 
				"6","7","8","9","A","B","C","D", 
				"E","F","G","H","I","J","K","L", 
				"M","N","O","P","Q","R","S","T", 
				"U","V","W","X","Y","Z" 
		}; 
		String md5 = DigestUtils.md5Hex(src + key);


		int hexLen = md5.length(); 
		int subHexLen = hexLen / 8; 
		String[] ShortStr = new String[4]; 

		for (int i = 0; i < subHexLen; i++) { 
			String outChars = ""; 
			int j = i + 1; 
			String subHex = md5.substring(i * 8, j * 8); 
			long idx = Long.valueOf("3FFFFFFF", 16) & Long.valueOf(subHex, 16); 

			for (int k = 0; k < 6; k++) { 
				int index = (int) (Long.valueOf("0000003D", 16) & idx); 
				outChars += chars[index]; 
				idx = idx >> 5; 
			} 
			ShortStr[i] = outChars; 
		}
		int rand = (int)Math.round((Math.random()*100))%3;
		//生成三位随机数
		String shortStr = ShortStr[rand];
		/*for(int i = 0; i < 3; i++){
			int r = (int)(Math.random()*62);
			shortStr += chars[r];
		}*/
		return shortStr;

	}

	public static void main(String[] argv){
		System.out.println(encode(java.util.UUID.randomUUID().toString()));
	}

}
