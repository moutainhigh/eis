/**
 * 对公众平台发送给公众账号的消息加解密示例代码.
 * 
 * @copyright Copyright (c) 1998-2014 Tencent Inc.
 */

// ------------------------------------------------------------------------

package com.maicard.wpt.utils.weixin.aes;

import java.security.MessageDigest;
import java.util.Arrays;

import org.apache.commons.codec.digest.DigestUtils;


/**
 * SHA1 class
 *
 * 计算公众平台的消息签名接口.
 */
class SHA1 {

	/**
	 * 用SHA1算法生成安全签名
	 * @param token 票据
	 * @param timestamp 时间戳
	 * @param nonce 随机字符串
	 * @param encrypt 密文
	 * @return 安全签名
	 * @throws AesException 
	 */
	public static String getSHA1(String token, String timestamp, String nonce, String encrypt) throws AesException
			  {
		try {
			String[] array = new String[] { token, timestamp, nonce, encrypt };
			StringBuffer sb = new StringBuffer();
			// 字符串排序
			Arrays.sort(array);
			for (int i = 0; i < 4; i++) {
				sb.append(array[i]);
			}
			String str = sb.toString();
			// SHA1签名生成
			String sign2 = DigestUtils.sha1Hex(str);

			//String sign2 = DigestUtils.sha1DigestAsHex(str);
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(str.getBytes());
			byte[] digest = md.digest();
			//System.out.println("签名源:" + str.replaceAll("\r\n", ""));
			StringBuffer hexstr = new StringBuffer();
			String shaHex = "";
			for (int i = 0; i < digest.length; i++) {
				shaHex = Integer.toHexString(digest[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexstr.append(0);
				}
				hexstr.append(shaHex);
			}
			String sign =  hexstr.toString();

			System.out.println("对消息[token=" + token + ",timestamp=" + timestamp + ",nonce=" + nonce + ",encrypt=" + encrypt + "]签名结果:" + sign + ",sign2=" + sign2);
			return sign;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AesException(AesException.ComputeSignatureError);
		}
	}
}
