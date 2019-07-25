package com.maicard.wpt.test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import com.maicard.common.util.HttpUtils;
import com.maicard.http.HttpClientPoolV3;
import com.maicard.http.HttpUtilsV3;

public class CouponModelNotifyTest {

	static final String host = "youbao.changxiu.net";
	static final int port = 80;
	static final long partnerId = 8100013;
	static final String key = "Labvefau";
	public static void main(String argv[]){
		HttpClient httpClient = HttpClientPoolV3.getHttpClient(host, port);

		Map<String,String> map = new HashMap<String,String>();
		map.put("code", "7688200000292");
		map.put("partnerId", String.valueOf(partnerId));
		map.put("currentStatus", "100002");
		map.put("timestamp", String.valueOf(new Date().getTime() / 1000));
		String requestData = HttpUtils.generateRequestString(map);
		System.out.println(requestData);
		String signSource = requestData + "|" + key;
		String sign = DigestUtils.sha256Hex(signSource);
		System.out.println(sign);
		requestData += "&sign=" + sign;

		String url = "http://" + host + ":" + port + "/coupon/notify.json?" + requestData;
		System.out.println(url);
	
		String page = null;
		try {
			page = HttpUtilsV3.getData(httpClient, url, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(page);
		
	}
}
