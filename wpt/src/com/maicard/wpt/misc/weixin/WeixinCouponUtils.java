package com.maicard.wpt.misc.weixin;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyStore;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.maicard.common.service.HttpsService;
import com.maicard.common.util.JsonUtils;
import com.maicard.http.HttpClientPoolV3;
import com.maicard.money.domain.Coupon;
import com.maicard.money.domain.CouponModel;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.wpt.service.impl.WeixinServiceImpl;

/**
 * 微信卡券管理工具
 *
 *
 * @author NetSnake
 * @date 2015年12月10日
 *
 */
@SuppressWarnings("deprecation")
public class WeixinCouponUtils {

	private static final String host = "api.weixin.qq.com";
	private static final int port = 443;
	public static final String token = "jC-IA-jF9GA5vxx8RMGBtHh-Nl0KC-k8j6Bel3TI6116YWmuwyQwEKOssloMTDOiAq2V0BbagFaHEQVXGsh5pbioSbbU8Z2JzN3sUdYdB-3sVHVsLLNqMs53ymM14Z3vBQDaAGAHWJ";
	private static final SimpleDateFormat sdf = new SimpleDateFormat(CommonStandard.defaultDateFormat);
	private static final SimpleDateFormat ymd = new SimpleDateFormat("yyyymmdd");
	static WeixinServiceImpl weixinService = new WeixinServiceImpl();
	@Resource
	private HttpsService httpsService;
	/**
	 * 新增临时素材
	 * https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE
	 */
	public static void upload(){
		String url = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=" + token + "&type=image";
		File file = new File("C:/Users/Administrator/Desktop/sqh.jpg");
		String result = null;
		try {
//			FileInputStream fileInput = new FileInputStream(file);
			URL urlObj = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestProperty("Conncetion", "Keep-Alive");
			conn.setRequestProperty("Charset", "UTF-8");
			String BOUNDARY = "----------" + System.currentTimeMillis();
			conn.setRequestProperty("Content-Type", "multipart/form-data; boundary="+ BOUNDARY);
			StringBuilder sb = new StringBuilder();
			sb.append("--"); // 必须多两道线
			sb.append(BOUNDARY);
			sb.append("\r\n");
			sb.append("Content-Disposition: form-data;name=\"file\";filename=\"" + file.getName() + "\";filelength=\"" + file.length() + "\"\r\n");
			sb.append("Content-Type:application/octet-stream\r\n\r\n");
			byte[] head = sb.toString().getBytes("utf-8");
			
			// 获得输出流
			OutputStream out = new DataOutputStream(conn.getOutputStream());
			// 输出表头
			out.write(head);
			// 文件正文部分
			// 把文件已流文件的方式 推入到url中
			DataInputStream in = new DataInputStream(new FileInputStream(file));
			int bytes = 0;
			byte[] bufferOut = new byte[1024];
			while ((bytes = in.read(bufferOut)) != -1) {
				out.write(bufferOut, 0, bytes);
			}
			in.close();
			// 结尾部分
			byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
			out.write(foot);
			out.flush();
			out.close();
			StringBuffer buffer = new StringBuffer();
			BufferedReader reader = null;
			
			// 定义BufferedReader输入流来读取URL的响应
			reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				//System.out.println(line);
				buffer.append(line);
			}
			if(result==null){
				result = buffer.toString();
			}
		} catch (IOException e) {
			System.out.println("发送POST请求出现异常！" + e);
			e.printStackTrace();
		} 
		
		System.out.println("查看结果:" + result);
		
	}
	/**
	 * 上传图片接口
	 */
	public static void uploadimg(){
		String url = "https://api.weixin.qq.com/cgi-bin/media/uploadimg?access_token=" + token;
		FileInputStream instream = null;
		String result = null;
		try {
			File file = new File("C:/Users/Administrator/Desktop/coca_logo.png");
			instream = new FileInputStream(file);
		
			URL urlObj = new URL(url);
			// 连接
			HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
			con.setRequestMethod("POST"); // 以Post方式提交表单，默认get方式
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false); // post方式不能使用缓存
			// 设置请求头信息
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");
			// 设置边界
			String BOUNDARY = "----------" + System.currentTimeMillis();
			con.setRequestProperty("Content-Type", "multipart/form-data; boundary="+ BOUNDARY);
			// 第一部分：
			StringBuilder sb = new StringBuilder();
			sb.append("--"); // 必须多两道线
			sb.append(BOUNDARY);
			sb.append("\r\n");
			sb.append("Content-Disposition: form-data;name=\"file\";filename=\"" + file.getName() + "\"\r\n");
			sb.append("Content-Type:application/octet-stream\r\n\r\n");
			byte[] head = sb.toString().getBytes("utf-8");
			// 获得输出流
			OutputStream out = new DataOutputStream(con.getOutputStream());
			// 输出表头
			out.write(head);
			// 文件正文部分
			// 把文件已流文件的方式 推入到url中
			DataInputStream in = new DataInputStream(new FileInputStream(file));
			int bytes = 0;
			byte[] bufferOut = new byte[1024];
			while ((bytes = in.read(bufferOut)) != -1) {
				out.write(bufferOut, 0, bytes);
			}
			in.close();
			// 结尾部分
			byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
			out.write(foot);
			out.flush();
			out.close();
			StringBuffer buffer = new StringBuffer();
			BufferedReader reader = null;
			
			// 定义BufferedReader输入流来读取URL的响应
			reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				//System.out.println(line);
				buffer.append(line);
			}
			if(result==null){
				result = buffer.toString();
			}
		} catch (IOException e) {
			System.out.println("发送POST请求出现异常！" + e);
			e.printStackTrace();
		} 
		
		System.out.println("查看结果:" + result);
	}
	/*
	 * 创建卡券
	 */
	public static int createCouponModel(CouponModel couponModel) {
		String accessToken = token;
		return weixinService.createCouponModel(couponModel, accessToken);
		/*
		 * String imageUrl = couponModel.getImageUrl(); if(imageUrl == null){
		 * System.out.println("尝试创建的卡券[" + couponModel + "]没有图片地址"); return
		 * EisError.requiredDataNotFound.id; }
		 * 
		 * String brandName = couponModel.getCouponModelName(); String title =
		 * "优惠券"; String subTitle = couponModel.getCouponModelName(); long
		 * beginTimeTs = 0; if(couponModel.getValidTimeBegin() == null){
		 * beginTimeTs = couponModel.getValidTimeBegin().getTime() / 1000; }
		 * else { beginTimeTs = new Date().getTime() / 1000 - 1; } long
		 * endTimeTs = 0; if(couponModel.getValidTimeEnd() == null){
		 * System.out.println("尝试创建的卡券[" + couponModel + "]没有有效期截至时间"); return
		 * EisError.requiredDataNotFound.id; } endTimeTs =
		 * couponModel.getValidTimeEnd().getTime() / 1000;
		 * 
		 * String data =
		 * "{\"card\":{\"card_type\":\"CASH\",\"cash\":{\"base_info\":{\"logo_url\":\""
		 * + imageUrl +
		 * "\",\"code_type\":\"CODE_TYPE_BARCODE\",\"use_custom_code\":true, \"get_custom_code_mode\":\"GET_CUSTOM_CODE_MODE_DEPOSIT\", \"brand_name\":\""
		 * + brandName + "\",\"title\":\"" + title + "\",\"sub_title\":\"" +
		 * subTitle +
		 * "\",\"date_info\":{\"type\":\"DATE_TYPE_FIX_TIME_RANGE\",\"begin_timestamp\":"
		 * + beginTimeTs + ",\"end_timestamp\":" + endTimeTs +
		 * "},\"color\":\"Color010\",\"notice\":\"用户到店出示卡券\",\"description\":\"\",\"location_id_list\":[],\"get_limit\":100,\"can_share\":true,\"can_give_friend\":true,\"sku\":{\"quantity\":0}},\"least_cost\":10000,\"reduce_cost\":1000}}}";
		 * String url = "https://api.weixin.qq.com/card/create?access_token=" +
		 * token; System.out.println("尝试创建微信卡券:" + data); HttpClient httpClient
		 * = HttpClientPoolV3.getHttpClient(host, port); String result = null;
		 * try{ PostMethod pm = new PostMethod(url); pm.setRequestEntity(new
		 * StringRequestEntity(data,"","UTF-8")); int rs =
		 * httpClient.executeMethod(pm); if(rs != HttpStatus.OK.value()){
		 * System.out.println("无法导入微信卡券[" + couponModel + "]的库存，对方服务器未返回200");
		 * return EisError.networkError.id; } result = new
		 * String(pm.getResponseBody(),"UTF-8"); }catch(Exception e){
		 * e.printStackTrace(); } System.out.println("创建微信卡券返回结果:" + result);
		 * if(result == null){ System.out.println("无法创建微信卡券产品:" + result);
		 * return OperateResult.failed.getId(); } JsonNode jsonNode = null; try
		 * { jsonNode = JsonUtils.getInstance().readTree(result.trim());
		 * }catch(Exception e){ e.printStackTrace(); } if(jsonNode == null){
		 * System.out.println("无法创建微信卡券产品:" + result); return
		 * OperateResult.failed.getId(); } int errorCode =
		 * jsonNode.path("errcode").intValue(); if(errorCode != 0){
		 * System.out.println("无法创建微信卡券产品，微信返回错误:" + errorCode); return
		 * OperateResult.failed.getId(); } String weixinCardId =
		 * jsonNode.path("card_id").textValue();
		 * couponModel.setCouponCode(weixinCardId);
		 * System.out.println("为我方卡券产品设置代码为微信卡券代码:" +
		 * couponModel.getCouponCode());
		 * 
		 * return OperateResult.success.getId();
		 */
	}

	/*
	 * 导入卡券CODE
	 */
	public static void importCardCode(String cardId, String... card) {
		String cardData = null;
		try {
			cardData = JsonUtils.getInstance().writeValueAsString(card);
			System.out.println("导入卡券CODE : " + card.toString());
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String data = "{\"card_id\":\"" + cardId + "\",\"code\":" + cardData + "}";
		System.out.println(data);

		String url = "http://api.weixin.qq.com/card/code/deposit?access_token=" + token;
		HttpClient httpClient = HttpClientPoolV3.getHttpClient(host, port);
		String result = null;
		try {
			PostMethod pm = new PostMethod(url);
			pm.setRequestEntity(new StringRequestEntity(data, "", "UTF-8"));
			int rs = httpClient.executeMethod(pm);
			System.out.println("请求返回:" + rs);
			result = new String(pm.getResponseBody(), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("添加库存结果:" + result);
	}

	/*
	 * 查看库存
	 */
	public static void getDepositCount(String cardId) {
		String data = "{\"card_id\":\"" + cardId + "\"}";
		System.out.println("查看提交数据:" + data);
		String url = "http://api.weixin.qq.com/card/code/getdepositcount?access_token=" + token;
		HttpClient httpClient = HttpClientPoolV3.getHttpClient(host, port);
		String result = null;
		try {
			PostMethod pm = new PostMethod(url);
			pm.setRequestEntity(new StringRequestEntity(data, "", "UTF-8"));
			int rs = httpClient.executeMethod(pm);
			System.out.println("请求返回:" + rs);

			result = new String(pm.getResponseBody(), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("查看结果:" + result);
	}

	/**
	 * 列出卡券CODE
	 */
	public static void getCardCodeStatus(String cardId, String code) {
		String data = "{\"card_id\":\"" + cardId + "\",\"code\":\"" + code + "\"}";
		System.out.println("查看" + code + "状态，提交数据:" + data);
		String url = "http://api.weixin.qq.com/card/code/get?access_token=" + token;
		HttpClient httpClient = HttpClientPoolV3.getHttpClient(host, port);
		String result = null;
		try {
			PostMethod pm = new PostMethod(url);
			pm.setRequestEntity(new StringRequestEntity(data, "", "UTF-8"));
			int rs = httpClient.executeMethod(pm);
			System.out.println("请求返回:" + rs);

			result = new String(pm.getResponseBody(), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("查看" + code + "状态，结果:" + result);
	}
	/**
	 * 核销CODE
	 * HTTP请求方式: POST
	 * URL:https://api.weixin.qq.com/card/code/consume?access_token=TOKEN
	 */
	public static void consume(String cardId, String code){
		String url = "https://api.weixin.qq.com/card/code/consume?access_token=" + token;
		String data = "{\"card_id\":\"" + cardId + "\",\"code\":\"" + code + "\"}";
		HttpClient httpClient = HttpClientPoolV3.getHttpClient(host, port);
		String result = null;
		try {
			PostMethod pm = new PostMethod(url);
			pm.setRequestEntity(new StringRequestEntity(data, "", "UTF-8"));
			int rs = httpClient.executeMethod(pm);
			System.out.println("请求返回:" + rs);

			result = new String(pm.getResponseBody(), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("结果 ：" + result);
	}
	
	/**
	 * 核查code
	 */
	public static void checkCardCode(String cardId, String code) {
		String data = "{\"card_id\":\"" + cardId + "\",\"code\":[\"" + code + "\"]}";
		String url = "http://api.weixin.qq.com/card/code/checkcode?access_token=" + token;
		HttpClient httpClient = HttpClientPoolV3.getHttpClient(host, port);
		String result = null;
		try {
			PostMethod pm = new PostMethod(url);
			pm.setRequestEntity(new StringRequestEntity(data, "", "UTF-8"));
			int rs = httpClient.executeMethod(pm);
			System.out.println("请求返回:" + rs);

			result = new String(pm.getResponseBody(), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("查看" + code + "是否存在，结果:" + result);
	}

	/*
	 * 删除卡券
	 */
	public static void delete(String cardId) {
		String data = "{\"card_id\":\"" + cardId + "\"}";
		String url = "https://api.weixin.qq.com/card/delete?access_token=" + token;
		HttpClient httpClient = HttpClientPoolV3.getHttpClient(host, port);
		String result = null;
		try {
			PostMethod pm = new PostMethod(url);
			pm.setRequestEntity(new StringRequestEntity(data, "", "UTF-8"));
			int rs = httpClient.executeMethod(pm);
			System.out.println("请求返回:" + rs);

			result = new String(pm.getResponseBody(), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("查看" + cardId + "是否存在，结果:" + result);
	}

	public static void getCard(String cardId) {
		String url = "https://api.weixin.qq.com/card/get?access_token=" + token;
		HttpClient httpClient = HttpClientPoolV3.getHttpClient(host, port);
		String cardData = "{\"card_id\":\"" + cardId + "\"}";
		String result = null;
		try {
			PostMethod pm = new PostMethod(url);
			pm.setRequestEntity(new StringRequestEntity(cardData, "", "UTF-8"));
			int rs = httpClient.executeMethod(pm);
			System.out.println("请求返回:" + rs);

			result = new String(pm.getResponseBody(), "UTF-8");
			// result = HttpUtilsV3.p(httpClient, url, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(result);
	}
	/**
	 * 修改库存接口
	 */
	public static void addCardStock(String cardId, int amount) {
		String url = "https://api.weixin.qq.com/card/modifystock?access_token=" + token;
		HttpClient httpClient = HttpClientPoolV3.getHttpClient(host, port);
		String data = "{\"card_id\": \"" + cardId + "\",\"increase_stock_value\":\"" + amount + "\"}";
		String result = null;
		try {
			PostMethod pm = new PostMethod(url);
			pm.setRequestEntity(new StringRequestEntity(data, "", "UTF-8"));
			int rs = httpClient.executeMethod(pm);
			System.out.println("请求返回:" + rs);

			result = new String(pm.getResponseBody(), "UTF-8");
			// result = HttpUtilsV3.p(httpClient, url, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("修改卡券[" + cardId + "]的库存+" + amount + ",结果:" + result);
	}

	public static void sendCardMass() {
		String url = "https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token=" + token;
		HttpClient httpClient = HttpClientPoolV3.getHttpClient(host, port);
		String data = "{\"touser\":[\"oU8Bxw-sYVD-jN7VQDt9f8OOGqbE\",\"oU8Bxw8hZNnK4ik9ZrsoLRDkn5UE\"],\"wxcard\":{\"card_id\":\"pU8Bxw8hG0_mo20E_8ABhILr73XQ\"},\"msgtype\":\"wxcard\"}";
		String result = null;
		try {
			PostMethod pm = new PostMethod(url);
			pm.setRequestEntity(new StringRequestEntity(data, "", "UTF-8"));
			int rs = httpClient.executeMethod(pm);
			System.out.println("请求返回:" + rs);

			result = new String(pm.getResponseBody(), "UTF-8");
			// result = HttpUtilsV3.p(httpClient, url, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(result);
	}

	public static void sendCardCustom(String openId, String cardId, String code, String token) {
		HttpClient httpClient = HttpClientPoolV3.getHttpClient(host, port);
		String result = null;
		// 获取ticket
		String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + token + "&type=wx_card";
		try {
			GetMethod method = new GetMethod(url);
			int rs = httpClient.executeMethod(method);

			System.out.println("请求返回:" + rs);
			result = new String(method.getResponseBody(), "UTF-8");
			// result = HttpUtilsV3.p(httpClient, url, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(result);
		String ticket = null;
		try {
			ticket = JsonUtils.getInstance().readTree(result.trim()).path("ticket").asText();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("得到ticket:" + ticket);

		String timestamp = String.valueOf(new Date().getTime() / 1000);
		String nonce_str = DigestUtils.md5Hex(timestamp);
		Map<String, String> map = new HashMap<String, String>();
		map.put("code", code);
		map.put("openid", openId);
		map.put("timestamp", timestamp);
		map.put("nonce_str", nonce_str);

		List<String> values = new ArrayList<String>(map.values());
		Collections.sort(values);
		String signSource = "";
		for (String value : values) {
			signSource += value;
		}
		String sign = DigestUtils.shaHex(signSource);
		System.out.println("校验源:" + signSource + ",校验后:" + sign);
		url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + token;

		String data = "{\"touser\":\"" + openId + "\",\"msgtype\":\"wxcard\",\"wxcard\":{\"card_id\":\"" + cardId
				+ "\"},\"card_ext\":{\"code\":\"" + code + "\",\"openid\":\"" + openId + "\",\"timestamp\":\""
				+ timestamp + "\",\"signature\":\"" + sign + "\"}}}";
		System.out.println(data);
		try {
			PostMethod pm = new PostMethod(url);
			pm.setRequestEntity(new StringRequestEntity(data, "", "UTF-8"));
			int rs = httpClient.executeMethod(pm);
			System.out.println("请求返回:" + rs);

			result = new String(pm.getResponseBody(), "UTF-8");
			// result = HttpUtilsV3.p(httpClient, url, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(result);
	}

	/**
	 * 发送红包
	 * 
	 * @author Pengzhenggang
	 * @data 2016/10/17
	 * @param String
	 *            nonceStr 随机字符串
	 * @param String
	 *            mchId 商户号
	 * @param String
	 *            wxappid 公众账号appid
	 * @param String
	 *            reOpenId 用户openid
	 * @param int
	 *            totalAmount 付款金额，单位分
	 * @param String
	 *            clientIp 调用接口的机器Ip地址
	 */
	public static String sendRedPack(String nonceStr, String mchId, String wxappid, String reOpenId,
			int totalAmount, String clientIp) throws Exception {
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		// 读取本机存放的PKCS12证书文件
		String t=Thread.currentThread().getContextClassLoader().getResource("/").getPath();
		String path = t+"apiclient_cert.p12";
		FileInputStream instream = new FileInputStream(new File(path));
//		FileInputStream instream = new FileInputStream(new File("D:/cert/apiclient_cert.p12"));
		try {
			// 指定PKCS12的密码(商户ID)
			keyStore.load(instream, mchId.toCharArray());
		} finally {
			instream.close();
		}
		
		SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, mchId.toCharArray()).build();
		// 指定TLS版本
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[]{ "TLSv1" }, null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
		 CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();

		HttpClient httpClient = HttpClientPoolV3.getHttpClient(host, port);
		String url = "https://api.mch.weixin.qq.com/mmpaymkttransfers/sendredpack";
		/**
		 * 商户订单号 组成：mch_id+yyyymmdd+10位一天内不能重复的数字。
		 */
		Date date = new Date();
		long timeStamp = System.currentTimeMillis();
		String ts = String.valueOf(timeStamp / 1000);
		String mchBillno = mchId + ymd.format(date) + ts;
		
		Map<String,Object> payAPI=new HashMap<String,Object>();
		payAPI.put("act_name", "baojiekoulinghongbao");
		payAPI.put("wxappid", wxappid);
		payAPI.put("client_ip", clientIp);
		payAPI.put("mch_billno", mchBillno);
		payAPI.put("mch_id", mchId);
		payAPI.put("nonce_str", nonceStr);
		payAPI.put("re_openid", reOpenId);
		payAPI.put("remark", "hongbaoqiangbuting");
		payAPI.put("send_name", "baojie");
		payAPI.put("total_amount", totalAmount);
		payAPI.put("total_num", "1");
		payAPI.put("wishing", "gongxifacai");
		String	sign = weixinService.getSign(payAPI);
		
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		sb.append("<nonce_str><![CDATA[" + nonceStr + "]]></nonce_str>");
		sb.append("<sign><![CDATA[" + sign + "]]></sign>");
		sb.append("<mch_billno><![CDATA[" + mchBillno + "]]></mch_billno>");
		sb.append("<mch_id><![CDATA[" + mchId + "]]></mch_id>");
		sb.append("<wxappid><![CDATA[" + wxappid + "]]></wxappid>");
		sb.append("<send_name><![CDATA[baojie]]></send_name>");
		sb.append("<re_openid><![CDATA[" + reOpenId + "]]></re_openid>");
		sb.append("<total_amount><![CDATA[" + totalAmount + "]]></total_amount>");
		sb.append("<total_num><![CDATA[1]]></total_num>");
		sb.append("<wishing><![CDATA[gongxifacai]]></wishing>");
		sb.append("<client_ip><![CDATA[" + clientIp + "]]></client_ip>");
		sb.append("<act_name><![CDATA[baojiekoulinghongbao]]></act_name>");
		sb.append("<remark><![CDATA[hongbaoqiangbuting]]></remark>");
		sb.append("</xml>");
		System.out.println("提交的参数：" + sb.toString());
		HttpPost httpPost = new HttpPost(url);
		StringEntity postEntity = new StringEntity(sb.toString(), "UTF-8");
		httpPost.addHeader("Content-Type", "text/xml");
		httpPost.setEntity(postEntity);
		String result = null;
		try {
			CloseableHttpResponse response = httpclient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity, "UTF-8");
			System.out.println("结果[" + result + "]");
		} catch (Exception e) {
			 e.printStackTrace();
		} finally {
			httpPost.releaseConnection();
		}
		return result;
	}
	/**
	 * 微信卡券互通
	 * @return
	 */
	public static String weixinCouonInterworking(CouponModel couponModel){
//		 1.创建互通卡券
		int rs = createCouponModel(couponModel);
		System.out.println("创建结果： " + rs);
		/** 
		 * 2.审核互通权限
		 * 3. 新建互通任务
		 */
		return null;
	}
	/**
	 * 开发者协助制券（无公众号模式）
	 * 1、创建子商户接口
	 * https://api.weixin.qq.com/card/submerchant/submit?access_token=TOKEN 
	 */
	public static void submerchant(String subMerchantName, String logoUrl, String mediaId, int endTime){
//		int endTime = 1438990559;
		int aClassOfID = 1;
		int twoCategoriesOfID = 101;
//		String data = "{\"info\":{\"brand_name\":\"aaaaaa\",\"logo_url\":\"http://mmbiz.xxxx\",\"protocol\":\"media_id\",\"end_time\":1438990559,\"primary_category_id\":1,\"secondary_category_id\":101\"operator_media_id\":\"\"}}";
//		System.out.println(data);
		StringBuffer sb = new StringBuffer();
		sb.append("{\"info\":{\"brand_name\":\"");
		sb.append(subMerchantName);	//子商户名字
		sb.append("\",\"logo_url\":\"");
		sb.append(logoUrl);	//子商户logo
		sb.append("\",\"protocol\":\"");
		sb.append(mediaId);	//授权函ID
		sb.append("\",\"end_time\":");
		sb.append(endTime);	//授权函有效期截止时间（东八区时间，单位为秒），需要与提交的扫描件一致
		sb.append(",\"primary_category_id\":");
		sb.append(aClassOfID);	//一级类目id,可以通过本文档中接口查询
		sb.append(",\"secondary_category_id\":");
		sb.append(twoCategoriesOfID);	//二级类目id，可以通过本文档中接口查询
		sb.append(",\"operator_media_id\":\"");	//营业执照内登记的经营者身份证彩照或扫描件, 不是必须
		sb.append("\"}}");
		String data = sb.toString();
		System.out.println("提交数据 ：" + data);
		String url = "https://api.weixin.qq.com/card/submerchant/submit?access_token=" + token;
		HttpClient httpClient = HttpClientPoolV3.getHttpClient(host, port);
		String result = null;
		try {
			PostMethod pm = new PostMethod(url);
			pm.setRequestEntity(new StringRequestEntity(data, "", "UTF-8"));
			int rs = httpClient.executeMethod(pm);
			System.out.println("请求返回:" + rs);
			result = new String(pm.getResponseBody(), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("结果:" + result);
	}
	/**
	 * 卡券开放类目查询
	 * https请求方式: GET
	 * https://api.weixin.qq.com/card/getapplyprotocol?access_token=TOKEN
	 */
	public static void getapplyprotocol(){
		String url = "https://api.weixin.qq.com/card/getapplyprotocol?access_token=" + token;
		HttpClient httpClient = HttpClientPoolV3.getHttpClient(host, port);
		String result = null;
		try {
			GetMethod method = new GetMethod(url);
			int rs = httpClient.executeMethod(method);

			System.out.println("请求返回:" + rs);
			result = new String(method.getResponseBody(), "UTF-8");
			// result = HttpUtilsV3.p(httpClient, url, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(result);
	}
	/**
	 * 批量拉取子商户信息
	 */
	public static void batchget(){
		String url = "https://api.weixin.qq.com/card/submerchant/batchget?access_token=" + token;
		HttpClient httpClient = HttpClientPoolV3.getHttpClient(host, port);
		String data = "{  \"begin_id\": 0,  \"limit\": 50,  \"status\": \"APPROVED\" }";
		String result = null;
		try {
			PostMethod pm = new PostMethod(url);
			pm.setRequestEntity(new StringRequestEntity(data, "", "UTF-8"));
			int rs = httpClient.executeMethod(pm);
			System.out.println("请求返回:" + rs);
			result = new String(pm.getResponseBody(), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(result);
	}

	public static void sendCustomCoupon(String openId, Coupon coupon) {

		importCardCode(coupon.getExtraCode(), coupon.getContent());
		addCardStock(coupon.getExtraCode(), 1);
		// listCardCode(coupon.getExtraCode());
//		sendCardCustom(openId, coupon.getExtraCode(), coupon.getContent());
	}
	/**
	 * 创建二维码接口
	 * http请求方式: POST
	 * https://api.weixin.qq.com/card/qrcode/create?access_token=TOKEN
	 */
	public static void qrcodeCreate(String cardId, String code){
		String url = "https://api.weixin.qq.com/card/qrcode/create?access_token=" + token;
		String data = "{\"action_name\":\"QR_CARD\",\"expire_seconds\":1800,\"action_info\":{\"card\":{\"card_id\":\"" + cardId + "\",\"code\":\"" + code + "\"}}}";
		String result = null;
		HttpClient httpClient = HttpClientPoolV3.getHttpClient(host, port);
		try {
			PostMethod pm = new PostMethod(url);
			pm.setRequestEntity(new StringRequestEntity(data, "", "UTF-8"));
			int rs = httpClient.executeMethod(pm);
			System.out.println("请求返回:" + rs);
			result = new String(pm.getResponseBody(), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("结果：" + result);
	}
	/**
	 * 更改code接口
	 * http请求方式: POST
	 * https://api.weixin.qq.com/card/code/update?access_token=TOKEN
	 */
	public static void codeUpdate(String cardId, String code, String newCode){
		String url = "https://api.weixin.qq.com/card/code/update?access_token=" + token;
		String data = "{\"code\": \"" + code + "\",\"card_id\": \"" + cardId + "\",\"new_code\": \"" + newCode + "\"}";
		System.out.println("data : " + data);
		String result = null;
		HttpClient httpClient = HttpClientPoolV3.getHttpClient(host, port);
		try {
			PostMethod pm = new PostMethod(url);
			pm.setRequestEntity(new StringRequestEntity(data, "", "UTF-8"));
			int rs = httpClient.executeMethod(pm);
			System.out.println("请求返回:" + rs);
			result = new String(pm.getResponseBody(), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("结果：" + result);
	}
	/**
	 * 更改卡券信息接口
	 * http请求方式: POST
	 * https://api.weixin.qq.com/card/update?access_token=TOKEN
	 */
	public static void update(){
		String url = "https://api.weixin.qq.com/card/update?access_token=" + token;
		StringBuffer sb = new StringBuffer();
		sb.append("{\"card_id\": \"pU8Bxw4KYSPUooGp0tGmWMjNaVEg\",");
		sb.append("\"cash\": {");
		sb.append("\"base_info\": {");
		sb.append("\"logo_url\": \"http://mmbiz.qpic.cn/mmbiz_png/re9c3hI5K28RmDDY2baK0frILNey37iaBakP5t0JUY8oEe3zTAWvLC4FSAqyw17fiaKV8JYxmSg7PyMZtibSdh32g/0\",");
//		sb.append("\"sub_title\": \"凭券0元购果倍爽2盒\",");
		sb.append("\"color\": \"Color040\",");
		sb.append("\"notice\": \"请让营业员扫码核销\",");
		sb.append("\"description\": \"1、购买可口可乐公司系列产品满10元可抵用（包含10元）。\n2、微信用户在活动页面勾选“接受活动规则”后方可参与本促销活动，成为参与用户。参与用户知晓并同意其勾选“接受活动规则”的行为视为其已完全阅读、理解并同意本活动规则的全部内容。活动 时间以微信平台系统时间为准。\n3、18周岁以下的未成年人需征得其监护人同意后方可参与本活动。未满12周岁的儿童需在其监护人陪同下参与本活动。\n4、同一微信账号、同一手机号、同一手机设备，符合其中任一条件者均视为同一参与用户。活动期间同一参与用户每天仅可使用三次。\n5、代金券使用仅限江浙沪区域物美超市。\",");
		sb.append("\"location_id_list\": [");
		sb.append("]");
		sb.append("}");
		sb.append("}");
		sb.append("}");
//		String data = "{\"card\":{\"card_type\":\"GENERAL_COUPON\",\"general_coupon\":{\"base_info\":{\"sub_merchant_info\":{\"merchant_id\":410177521},\"logo_url\":\"http://mmbiz.qpic.cn/mmbiz_png/5AUSrLn6N5Xp8dEBk5oVzwcibPIIDqjPPSzQtkDtOReR7NGFWUSic4V3mQEKel2PyB0S1Scd6jBPprcwAtehVqMA/0?wx_fmt=png\",\"code_type\":\"CODE_TYPE_BARCODE\",\"use_custom_code\":true, \"get_custom_code_mode\":\"GET_CUSTOM_CODE_MODE_DEPOSIT\",\"location_id_list\":[],\"bind_openid\":false, \"brand_name\":\"凭券0元购果倍爽2盒\",\"title\":\"0元购2盒果倍爽\",\"sub_title\":\"凭券0元购果倍爽2盒\",\"date_info\":{\"type\":\"DATE_TYPE_FIX_TIME_RANGE\",\"begin_timestamp\":1480521600,\"end_timestamp\":1497715199},\"color\":\"Color040\",\"notice\":\"购买乐视乐次元季度会员领取果倍爽\",\"service_phone\": \"4001160676\",\"description\":\"1、凭券可0元购买任意口味12袋，或者任意口味2盒/n2、结账时请向收银员出示本券/n3、不折现、不找零、不挂失、打印或截图无效，卡券可转赠给朋友/n4、本券限用一次/n5、每人限领8张，每人限用8张\",\"get_limit\":8,\"can_share\":true,\"can_give_friend\":true,\"sku\":{\"quantity\":0}},\"default_detail\":\"凭券0元购果倍爽2盒（200ml*6袋/盒）\"}}}";
		System.out.println("data : " + sb.toString());
		String result = null;
		HttpClient httpClient = HttpClientPoolV3.getHttpClient(host, port);
		try {
			PostMethod pm = new PostMethod(url);
			pm.setRequestEntity(new StringRequestEntity(sb.toString(), "", "UTF-8"));
			int rs = httpClient.executeMethod(pm);
			System.out.println("请求返回:" + rs);
			result = new String(pm.getResponseBody(), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("结果：" + result);
	}

	public static void main(String[] argv) throws Exception {
		CouponModel couponModel = new CouponModel();
//		couponModel.setImageUrl("http://mmbiz.qpic.cn/mmbiz_png/5AUSrLn6N5Xp8dEBk5oVzwcibPIIDqjPPSzQtkDtOReR7NGFWUSic4V3mQEKel2PyB0S1Scd6jBPprcwAtehVqMA/0?wx_fmt=png");//乐视
//		couponModel.setImageUrl("https://mmbiz.qlogo.cn/mmbiz/K9ut1HWQ63Lj5IykVC4Ifs1Q71DS6cfq1s5wtW3woJejhHPDlVwbYnGk8XylOWhz6Ic43OTYibzt0U7UItddX3w/0");	//中百
//		couponModel.setImageUrl("http://mmbiz.qpic.cn/mmbiz_jpg/K9ut1HWQ63KzyibHTbj9r2MRelMq7oSdMav2up8LATCBWv2RImxpPJpxkNW1ASukUWHUWQuOdkNF05JvC7iaeBgQ/0");//宝洁
		couponModel.setImageUrl("http://mmbiz.qpic.cn/mmbiz_png/re9c3hI5K28RmDDY2baK0frILNey37iaBakP5t0JUY8oEe3zTAWvLC4FSAqyw17fiaKV8JYxmSg7PyMZtibSdh32g/0");//可口可乐
//		couponModel.setCouponModelName("惊喜连连5元礼");
//		couponModel.setCouponModelDesc("惊喜连连5元礼");
		
//		couponModel.setCouponModelName("可口可乐8元代金券");
//		couponModel.setCouponModelDesc("满20元可用");
		
		couponModel.setContent("点击立即领取代金券将存入微信卡包");
		
//		couponModel.setCouponModelName("可口可乐5元代金券");
//		couponModel.setCouponModelDesc("满15元可用");
		
		couponModel.setCouponModelName("可口可乐2元代金券");
		couponModel.setCouponModelDesc("满10元可用");
		
		couponModel.setOwnerId(200006);
		
//		优惠券类型
		couponModel.setCouponType("CASH");

		couponModel.setData(new HashMap<String, String>());
		couponModel.getData().put(DataName.couponUseCustomCode.toString(),"true");
		 
//		couponModel.getData().put(DataName.couponLeastCost.toString(), "13800");//least_cost代金券专用，表示起用金额（单位为分）,如果无起用门槛则填0。
//		couponModel.getData().put(DataName.couponReduceCost.toString(), "500");//reduce_cost代金券专用，表示减免金额。（单位为分）
		 
		// couponModel.getData().put(DataName.entityShopList.toString(),"279533267");
//		couponModel.getData().put(DataName.couponLeastCost.toString(), "2000");//least_cost代金券专用，表示起用金额（单位为分）,如果无起用门槛则填0。
//		couponModel.getData().put(DataName.couponReduceCost.toString(), "800");//reduce_cost代金券专用，表示减免金额。（单位为分）
		
//		couponModel.getData().put(DataName.couponLeastCost.toString(), "1500");//least_cost代金券专用，表示起用金额（单位为分）,如果无起用门槛则填0。
//		couponModel.getData().put(DataName.couponReduceCost.toString(), "500");//reduce_cost代金券专用，表示减免金额。（单位为分）
		 
		couponModel.getData().put(DataName.couponLeastCost.toString(), "1000");//least_cost代金券专用，表示起用金额（单位为分）,如果无起用门槛则填0。
		couponModel.getData().put(DataName.couponReduceCost.toString(), "200");//reduce_cost代金券专用，表示减免金额。（单位为分）
		
		try {
			couponModel.setValidTimeBegin(sdf.parse("2017-01-19 00:00:00"));
			couponModel.setValidTimeEnd(sdf.parse("2017-01-26 23:59:59"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    /*
	     * /p1hjqwvKgX-dSGBTu6GscTZWZKkE
	     * /p1hjqwnIv_e0SGJntkyb6KC60vkM
	     * /p1hjqwkz_1ie3_L7IIaH3ppKUGWQ
	     */
		
//		新增临时素材
//		upload();
		//创建子商户
//		submerchant("麦卡通", couponModel.getImageUrl(), "n4E4pm_xz4AlF4rzoAuUIVcDslpx_CHwCTPzF5LUGss", 1483113599);
//		卡券开放类目查询
//		getapplyprotocol();
//		批量拉取子商户信息
//		batchget();
		/**
		 * 上传图片
		 */
//		uploadimg();
//		System.out.println("图片：" + instream);
//		创建卡券
//		 int rs = createCouponModel(couponModel);
//		 System.out.println("创建微信卡券结果:" + rs);
//		 pU8Bxwzpojf3K3f9qbsCQ500DCMw
//		String cardId = "pU8Bxw8bEUlKPE7nVCnc7V84teGc";
//		String cardId = "pU8Bxwzpojf3K3f9qbsCQ500DCMw";
		
		
//		修改卡券信息
		update();
//		导入code
//		 importCardCode(cardId, card3);
//		核查code
//		 checkCardCode(cardId, card3);
//		修改库存
//		 addCardStock(cardId,1);
//		查询导入code数目
//		 getDepositCount(cardId);
//		--------------------------------------
//		核销部分
		//1、线下核销
//		1.1查询Code
//		 getCardCodeStatus(cardId, card);
//		1.2核销Code
//		 consume("peDoPt5ppVN2vRM6i89YAUqhLqUE", "7045300001787");
		
		
//		创建二维码接口
//		qrcodeCreate(cardId, card1);
//		更改code
//		codeUpdate(cardId, card3, newCode);;
//		getCard(cardId);
//		delete("p1hjqwpADbcTU-h7c6-E2MdeRgHo");
		
		
		// weixinService.sendWeixinCouponFromCustomInterface(1, "o1hjqwsJqx5KXNKg7odpA1OC6tFs", cardId, card);
		// addCardStock(cardId,1);
//		sendCardCustom("oU8Bxw-sYVD-jN7VQDt9f8OOGqbE","p1hjqwvKgX-dSGBTu6GscTZWZKkE",card);
//		long timeStamp = System.currentTimeMillis();
//		String ts = String.valueOf(timeStamp / 1000);
//		final String nonceStr = DigestUtils.md5Hex(ts).toUpperCase();
//		System.out.println("nonceStr: " + ts);
//		sendRedPack("AAC2BBBCEF4FAF84FF513B54ECC2B38Q", "1298260201", "wxa2e516aa68265bfc", "o1hjqwpMOxEf3kW380wE-t5c5zcI", 100, "192.168.0.1");
	}
}
