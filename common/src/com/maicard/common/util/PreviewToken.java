package com.maicard.common.util;

import com.maicard.standard.CommonStandard;

public class PreviewToken {
	public static String validate(String token){
		String code = null;
		Crypt crypt = new Crypt();
		crypt.setAesKey(CommonStandard.previewKey);
		try{
			String src = crypt.aesDecrypt(token);
			System.out.println("预览令牌[" + token + "]解密后:" + src);
			String[] data =src.split("\\|");
			long time = Long.parseLong(data[1]);
			System.out.println("令牌超时时间:" + (System.currentTimeMillis() - time));
			if(System.currentTimeMillis() - time < (60 * 1000)){
				return data[0];
			}

		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return code;
	}

	public static String generate(String code){
		Crypt crypt = new Crypt();
		crypt.setAesKey(CommonStandard.previewKey);
		return crypt.aesEncrypt(code + "|" + String.valueOf(System.currentTimeMillis()));
	}
}
