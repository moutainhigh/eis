package com.maicard.test;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

import com.maicard.common.util.HttpUtils;
import com.maicard.product.service.impl.ProductServiceImpl;
import com.maicard.standard.CommonStandard;

public class Unicode2Utf8 {
	public static void main(String[] argv){
		String  date =  "\u5c0f\u7ed2";
		
		try{
		byte[] utf8 = date.getBytes("UTF-8");
		String name = new String(utf8,"UTF-8");
		//System.out.println(name);
		}catch(Exception e){
			e.printStackTrace();
		}
		String url = "http://s1.zxy.yeele.cn/user/role_info.php?username=2000082&time=1364796827&serverid=s1&ticket=c16ac692e907be61942ca15277284f94";
		String queryResult = HttpUtils.sendData( url);
		try{
			int offset1 = queryResult.indexOf("nickname\":\"");

			int offset2 = queryResult.indexOf("\",\"online\"");
			
			String character = queryResult.substring(offset1 + 11, offset2).replaceAll("\\", "\\\\\\");
			System.out.println(character);
			byte[] utf8 = character.getBytes("UTF-8");
			String name = new String(utf8,"UTF-8");
			System.out.println(name);
			}catch(Exception e){
				e.printStackTrace();
			}


	}

}
