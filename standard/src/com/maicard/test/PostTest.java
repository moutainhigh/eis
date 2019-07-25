package com.maicard.test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;

import javax.annotation.Resource;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.HttpsService;
import com.maicard.standard.CommonStandard;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.tree.DefaultDocument;
import org.springframework.cache.Cache.ValueWrapper;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class PostTest {
	@Resource
	static HttpsService httpservice;
	@Resource ApplicationContextService applicationContextService;
	private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5','6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	private static String APPID="wxb50b53a63ab379e5";
	private static String APPSECRET="1e905f5f91864993d90f8a659e2258c8";
	public static String getAccessToken()//得到Token
	{
		String url="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+APPID+"&"+"secret="+APPSECRET;
		String access_token="";
		String expires_in="";
		{	
			try
			{
				System.out.println("token的url是"+url);
				String result=httpservice.httpsGet(url);
				ObjectMapper om= new ObjectMapper();
				access_token=om.readTree(result).get("access_token").asText();
				expires_in=om.readTree(result).get("expires_in").asText();
				System.out.println("更换token为"+access_token);
			}
			catch(Exception e){
				System.out.println("从微信服务器取token出错");
			}
			//logger.info("超时时间是"+Long.valueOf(expires_in));

		}
		return access_token;
	}	
	public  void getMsgEntity()//解析xml
	{  
		String strXml="";
		try {  
			if (strXml.length() <= 0 || strXml == null) { 
			// 将字符串转化为XML文档对象  
			Document document = DocumentHelper.parseText(strXml);  
			// 获得文档的根节点  
			Element root = document.getRootElement();  
			// 遍历根节点下所有子节点  
			Iterator<?> iter = root.elementIterator();  
			// 遍历所有结点  
			//利用反射机制，调用set方法  
			//获取该实体的元类型  
			Class<?> c = Class.forName("com.maicard.o2o.front.controller.youcai.ReceiveMsg");  

			while(iter.hasNext()){  
				Element ele = (Element)iter.next();  
				//获取set方法中的参数字段（实体类的属性）  
				Field field = c.getDeclaredField(ele.getName());  
				//获取set方法，field.getType())获取它的参数数据类型  
				Method method = c.getDeclaredMethod("set"+ele.getName(), field.getType());  
				//调用set方法  
			}  }
		} catch (Exception e) {  
			// TODO: handle exception  
			System.out.println("xml 格式异常: "+ strXml);  
			e.printStackTrace();  
		}  
	}  
	public static void main(String[] argv){
		String token="Uc4fw52VPr9ihpWATWepkUVEpYCWhwLRk1rYTfQNCG4d4mPcrmz9D_RiQovLCQID79WxgAT2tn6ISiYVaS-4GR1pgJJnKr07wyRYGs4Y72o";
		String url="https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token="+token;
		String memo="{\"touser\":[\"oiGzcs5wDFCe6QtcP76xs4qjLxYY\",\"oiGzcs7UhpwaU0BLcsUdMUKHwOlE\"],\"msgtype\":\"text\",\"text\":{\"content\":\"hello from boxer.\"}}";

	}
}
class tokenKey{
	private String Token;
	private Long  expires_time;

	public String getToken() {
		return Token;
	}
	public void setToken(String token) {
		Token = token;
	}
	public Long getExpires_time() {
		return expires_time;
	}
	public void setExpires_time(Long create_time) {
		this.expires_time = create_time;
	}
}