package com.maicard.wpt.misc.weixin;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.maicard.common.util.JsonUtils;
import com.maicard.http.HttpClientPoolV3;
import com.maicard.http.HttpUtilsV3;
import com.maicard.wpt.domain.WeixinButton;

public class MenuUtils {

	private static final String host = "api.weixin.qq.com";
	private static final int port = 443;
	private static final String token = "TxRV64-aJtjbOFKwnEqoHSrYu9gSgYG4PgmeIbB1izruQZnmhZV5MX6AqOX2ROKCafgaDwG6YaAvelyRFSTOeiVnQn1VxGalxDY_Nt5ir1H5oEoRuA9Hx1ClPbolbNDzECHaAAAQOT";
	
	
		static final String ourUrl = "http://www.qixingyueqi.com";
//	static final String ourUrl = "http://yht.yuanhuitongweb.com";

	public static void createMenu(){
		String menu = "{\"button\":[{\"name\":\"赚U宝\",\"sub_button\":" + 
				"[{\"type\":\"scancode_waitmsg\",\"name\":\"扫一扫码\",\"key\":\"saoma\"}," +
				"{\"type\":\"view\",\"name\":\"输礼品码\",\"url\":\"" + ourUrl + "/content/saoma/index.shtml\"}," + 
				"{\"type\":\"view\",\"name\":\"每日签到\",\"url\":\"" + ourUrl + "/content/user/sign.shtml\"}," + 
				"{\"type\":\"view\",\"name\":\"U宝游戏\",\"url\":\"" + ourUrl + "/content/youxihuode/xydlp.shtml\"}" + 
				"]},{\"name\":\"兑礼品\",\"sub_button\":[" + 
				"{\"type\":\"view\",\"name\":\"U宝商城\",\"url\":\"" + ourUrl + "/content/youbaoshangcheng/index.shtml\"}," + 
//				"{\"type\":\"view\",\"name\":\"德铂内购会\",\"url\":\"" + ourUrl + "/activity/weida/index.shtml\"}," + 
				"{\"type\":\"view\",\"name\":\"碧然德专区\",\"url\":\"" + ourUrl + "/promotion/birande/index.shtml\"}," + 
				"{\"type\":\"view\",\"name\":\"兑优惠券\",\"url\":\"" + ourUrl + "/content/user/coupon.shtml\"}," + 
//				"{\"type\":\"click\",\"name\":\"附近活动\",\"url\":\"" + ourUrl + "/content/fujinhuodong/index.shtml\"}" + 
				"{\"type\":\"click\",\"name\":\"附近活动\",\"key\":\"领取现金券\"}" + 
				"]}," + 
				"{\"type\":\"view\",\"name\":\"会员中心\",\"url\":\"" + ourUrl + "/content/user/pcenter.shtml\"}" + 
				"]}";

		System.out.println(menu);
		ObjectMapper om = JsonUtils.getInstance();
		ArrayList<WeixinButton> buttons = new ArrayList<WeixinButton>();
		JsonNode jsonNode = null;
		try {
			jsonNode = om.readTree(menu);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		//System.out.println(jsonNode.path("button"));


		JavaType tr = om.getTypeFactory().constructCollectionType(ArrayList.class,WeixinButton.class);

		try {
			buttons = om.readValue(jsonNode.path("button").toString(), tr);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}
		for(WeixinButton b : buttons){
			System.out.println(">>>>>一级菜单" + b);
			if(b.getSubButton() != null && b.getSubButton().size() > 0){
				for(WeixinButton c : b.getSubButton()){
					System.out.println(">>>>>>>>>>>>>>>>>>>二级菜单" + c);

				}
			}
		}
		/*if(buttons != null){
			return;
		}*/

		/*String menu = "{\"button\":" + 
				  "[{\"type\":\"view\",\"name\":\"食材信仰\",\"url\":\"http://www.yixian365.com/content/shicaixinyang/index.shtml\"}," + 
				  "{\"type\":\"view\",\"name\":\"一地一品\",\"url\":\"http://www.yixian365.com/content/product/index.shtml\"}," + 
				  "{\"type\":\"view\",\"name\":\"关于我们\",\"url\":\"http://www.yixian365.com/content/about/aboutus.shtml\"}" + 
				  "]}";		System.out.println(menu);*/
		String url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + token;
		HttpClient httpClient = HttpClientPoolV3.getHttpClient(host, port);
		String result = null;
		int rs = 0;
		try{
			PostMethod pm = new PostMethod(url);
			pm.setRequestEntity(new StringRequestEntity(menu,"","UTF-8"));
			rs = httpClient.executeMethod(pm);

			result = pm.getResponseBodyAsString();
			//	result = HttpUtilsV3.p(httpClient, url, null);
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("HTTP Code:" + rs + ", Return Page:" + result);
	}
	public static void getTag(){
		String url = "https://api.weixin.qq.com/cgi-bin/tags/get?access_token=" + token;
		HttpClient httpClient = HttpClientPoolV3.getHttpClient(host, port);
		String result = null;
		try{
			result = HttpUtilsV3.getData(httpClient, url, null);
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println(result);
	}

	public static void setTag(long tagId, String openId){
		String url = "https://api.weixin.qq.com/cgi-bin/tags/members/batchtagging?access_token=" + token;
		HttpClient httpClient = HttpClientPoolV3.getHttpClient(host, port);
		String result = null;
		String data = null;

		ObjectMapper om = JsonUtils.getInstance();
		ObjectNode objectNode = om.createObjectNode();
		ArrayNode an = objectNode.putArray("openid_list");
		an.add(openId);
		objectNode.put("tagid", tagId);
		try {
			data = om.writeValueAsString(objectNode);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(	data );
		int rs = 0;
		try{
			PostMethod pm = new PostMethod(url);
			pm.setRequestEntity(new StringRequestEntity(data,"","UTF-8"));
			rs = httpClient.executeMethod(pm);

			result = pm.getResponseBodyAsString();
			//	result = HttpUtilsV3.p(httpClient, url, null);
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("HTTP Code:" + rs + ", Return Page:" + result);
	}


	public static void getMenu(){
		String url = "https://api.weixin.qq.com/cgi-bin/get_current_selfmenu_info?access_token=" + token;
		HttpClient httpClient = HttpClientPoolV3.getHttpClient(host, port);
		String result = null;
		try{
			result = HttpUtilsV3.getData(httpClient, url, null);
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println(result);
	}

	public static void deleteAllMenu(){
		String url = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=" + token;
		HttpClient httpClient = HttpClientPoolV3.getHttpClient(host, port);
		String result = null;
		try{
			result = HttpUtilsV3.getData(httpClient, url, null);
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println(result);
	}




	public static void create2(boolean matchMenu){

//		String urlPrefix = "http://youbao.mo4u.cn";

		String urlPrefix = ourUrl;
		WeixinButton button1 = new WeixinButton("scancode_waitmsg", "扫一扫码", "saoma", null,null);
		WeixinButton button2 = new WeixinButton("view", "输礼品码", null, null, urlPrefix + "/content/saoma/index.shtml");
		WeixinButton button3 = new WeixinButton("view", "每日签到", null, null, urlPrefix + "/content/user/sign.shtml");
		WeixinButton button4 = new WeixinButton("view", "U宝游戏", null, null, urlPrefix + "/content/youxihuode/xydlp.shtml");

		WeixinButton buttonLevel1_1 = new WeixinButton(null, "赚U宝", null,null,null);
		buttonLevel1_1.setSubButton(new ArrayList<WeixinButton>());
		buttonLevel1_1.getSubButton().add(button1);
		buttonLevel1_1.getSubButton().add(button2);
		buttonLevel1_1.getSubButton().add(button3);
		buttonLevel1_1.getSubButton().add(button4);

		WeixinButton button2_1 = new WeixinButton("view", "U宝商城", null, null, urlPrefix + "/content/youbaoshangcheng/index.shtml");
		WeixinButton button2_2 = null;
		if(matchMenu){
			button2_2 = new WeixinButton("view", "四达福利会", null, null, urlPrefix + "/promotion/birande/index.shtml?v=2");
		} else {
			button2_2 = new WeixinButton("view", "碧然德专区", null, null, urlPrefix + "/promotion/birande/index.shtml");

		}
		WeixinButton button2_3 = new WeixinButton("view", "兑优惠券", null, null, urlPrefix + "/content/user/coupon.shtml");
		WeixinButton button2_4 = new WeixinButton("click", "附近活动", "领取现金券", null, null);

		WeixinButton buttonLevel1_2 = new WeixinButton(null, "兑礼品", null,null,null);
		buttonLevel1_2.setSubButton(new ArrayList<WeixinButton>());
		buttonLevel1_2.getSubButton().add(button2_1);
		buttonLevel1_2.getSubButton().add(button2_2);
		buttonLevel1_2.getSubButton().add(button2_3);
		buttonLevel1_2.getSubButton().add(button2_4);

		WeixinButton buttonLevel1_3 = new WeixinButton("view", "会员中心", null,null,urlPrefix + "/content/user/pcenter.shtml");


		WeixinButton[] topButton = new WeixinButton[3];
		topButton[0] = buttonLevel1_1;
		topButton[1] = buttonLevel1_2;
		topButton[2] = buttonLevel1_3;



		ObjectMapper om = JsonUtils.getInstance();
		//JsonNode jsonNode = om.readTree(om.writeValueAsBytes(topButton));


		ObjectNode on = om.createObjectNode();		
		on.putPOJO("button", topButton);

		if(matchMenu){

			ObjectNode on2 = on.putObject("matchrule");
			on2.put("tag_id", 106);
		}



		String data = null;
		try {
			data = om.writeValueAsString(on);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(	data );
		String url = "https://api.weixin.qq.com/cgi-bin/menu/addconditional?access_token=" + token;
		HttpClient httpClient = HttpClientPoolV3.getHttpClient(host, port);
		String result = null;
		int rs = 0;
		try{
			PostMethod pm = new PostMethod(url);
			pm.setRequestEntity(new StringRequestEntity(data,"","UTF-8"));
			rs = httpClient.executeMethod(pm);

			result = pm.getResponseBodyAsString();
			//	result = HttpUtilsV3.p(httpClient, url, null);
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("HTTP Code:" + rs + ", Return Page:" + result);




	}

	public static void create_zhongbai(){

		//测试
		//String urlPrefix = "http://youbao.mo4u.cn";
		
		
		//现网
		String urlPrefix = "http://w.zb.yuanhuitongweb.com";

		//String urlPrefix = "http://w.zb.yuanhuitongweb.com";
		WeixinButton button1 = new WeixinButton("view", "会员中心", null, null, "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx9a992fe7e28fc1d8&redirect_uri=http%3a%2f%2fzbweixin.whzb.com%2fMSCard%2fMSCard%2fIndex%3fhash%3dzb&response_type=code&scope=snsapi_base&state=mp123#wechat_redirect");

		
		WeixinButton buttonLevel1_1 = new WeixinButton(null, "会员中心", null,null,null);
		buttonLevel1_1.setSubButton(new ArrayList<WeixinButton>());
		buttonLevel1_1.getSubButton().add(button1);

		WeixinButton button2_1 = new WeixinButton("click", "清扬立减", "投票中百201702",null, null);
		//WeixinButton button2_2 = new WeixinButton("view", "报名参赛", null, null, urlPrefix + "/activity/detail.shtml");

		WeixinButton buttonLevel1_2 = new WeixinButton("click", "清扬立减", "投票中百201702",null, null);;//new WeixinButton(null, "品牌活动", null,null,null);
		//buttonLevel1_2.setSubButton(new ArrayList<WeixinButton>());
		//buttonLevel1_2.getSubButton().add(button2_1);
		//buttonLevel1_2.getSubButton().add(button2_2);

		/*		WeixinMenuButton button2_1 = new WeixinMenuButton("view", "最新海报", null, null, urlPrefix + "/content/dm/index.shtml");

		WeixinMenuButton buttonLevel1_2 = new WeixinMenuButton(null, "促销商品", null,null,null);
		buttonLevel1_2.sub_button = new WeixinMenuButton[1];
		buttonLevel1_2.sub_button[0] = button2_1;*/


		WeixinButton buttonLevel1_3 = new WeixinButton(null, "优惠券", null,null,null);
		buttonLevel1_3.setSubButton(new ArrayList<WeixinButton>());
		//WeixinButton button3_2 = new WeixinButton("view", "我的优惠券", null, null, urlPrefix + "/coupon/listMine.shtml?couponCode=ZHONGBAI_PUBLIC");
	WeixinButton button3_2 = new WeixinButton("view", "我的优惠券", null, null, urlPrefix + "/coupon/listByModel.shtml?couponCode=ZHONGBAI_PUBLIC");
		buttonLevel1_3.getSubButton().add(button3_2);

		//WeixinMenuButton buttonLevel1_3 = new WeixinMenuButton("view", "我的服务", null,null,urlPrefix + "/content/user/pcenter.shtml");


		WeixinButton[] topButton = new WeixinButton[3];
		topButton[0] = buttonLevel1_1;
		topButton[1] = buttonLevel1_2;
		topButton[2] = buttonLevel1_3;



		ObjectMapper om = JsonUtils.getInstance();
		//JsonNode jsonNode = om.readTree(om.writeValueAsBytes(topButton));


		ObjectNode on = om.createObjectNode();		
		on.putPOJO("button", topButton);




		String data = null;
		try {
			data = om.writeValueAsString(on);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(	data );
		String url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + token;
		HttpClient httpClient = HttpClientPoolV3.getHttpClient(host, port);
		String result = null;
		int rs = 0;
		try{
			PostMethod pm = new PostMethod(url);
			pm.setRequestEntity(new StringRequestEntity(data,"","UTF-8"));
			rs = httpClient.executeMethod(pm);

			result = pm.getResponseBodyAsString();
			//	result = HttpUtilsV3.p(httpClient, url, null);
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("HTTP Code:" + rs + ", Return Page:" + result);




	}



	public static void tryMatch(){
		String url = "https://api.weixin.qq.com/cgi-bin/menu/trymatch?access_token=" + token;
		HttpClient httpClient = HttpClientPoolV3.getHttpClient(host, port);
		String data = "{\"user_id\":\"oU8Bxw8hZNnK4ik9ZrsoLRDkn5UE\"}";
		String result = null;
		int rs = 0;
		try{
			PostMethod pm = new PostMethod(url);
			pm.setRequestEntity(new StringRequestEntity(data,"","UTF-8"));
			rs = httpClient.executeMethod(pm);

			result = pm.getResponseBodyAsString();
			//	result = HttpUtilsV3.p(httpClient, url, null);
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("HTTP Code:" + rs + ", Return Page:" + result);
	}

	public static void main(String[] argv){
		create_zhongbai();
		//getTag();
		//setTag(106,"oU8Bxw-sYVD-jN7VQDt9f8OOGqbE");
		//deleteAllMenu();
		//createMenu();
		//tryMatch();
		//create2(false);
		//MenuUtils.createMenu();
		//MenuUtils.getMenu	();
	}
}
