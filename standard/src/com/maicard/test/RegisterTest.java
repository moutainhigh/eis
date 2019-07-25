package com.maicard.test;

import java.util.Date;

import org.apache.commons.httpclient.HttpClient;

import com.maicard.common.util.HttpClientPool;
import com.maicard.common.util.HttpMethods;

/**
 * 网站注册测试
 *
 * @author NetSnake
 * @date 2013-9-19 
 */
public class RegisterTest {
	public static void main(String[] argv){
		String host = null;
		int port = 0;
		if(argv.length == 0){
			host = "www.yeele.cn";
		} else {
			host = argv[0];
		}
		if(argv.length > 1){
			try{
			port = Integer.parseInt(argv[1]);
			}catch(Exception e){}
		}
		if(port == 0){
			port = 80;
		}
		String username = new Date().getTime() + "@maicard.com";
		String url1 = "http://" + host + ":" + port + "/quickStart.json?username=" + username + "&userPassword=123456&i=pjyQJRrFm0&s=310";
		String url2 = "http://" + host + ":" + port + "/product/mxqy/310.json";
		long start = new Date().getTime();
		HttpClient httpClient = HttpClientPool.getHttpClient(host, port);
		String result = null;
		System.out.print("Running quick start on[" + host + ":" + port + "]:");
		//httpClient.getParams().setIntParameter("http.socket.timeout",3000);
		try{
			result = HttpMethods.getData(httpClient, url1, null);
		}catch(Exception e){}
		if(result != null && result.indexOf("即将进入该服务器") > 0){
			//成功注册
			result = null;
			try {
				result= HttpMethods.getData(httpClient, url2, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
			//System.err.println(result);
			if(result != null && result.indexOf("http://s1.mxqy.yeele.cn/login.php") > 0){
				System.out.println("register and start success:" + ((new Date().getTime() - start)  + "ms"));
				System.exit(0);
			} else {
				System.out.println("register success, but start fail:" + ((new Date().getTime() - start)  + "ms"));
				System.exit(1);
			}
		}
		System.out.println("register fail:" + ((new Date().getTime() - start)  + "ms"));
		System.exit(1);
	}

}
