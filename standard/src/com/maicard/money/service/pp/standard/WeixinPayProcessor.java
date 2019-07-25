package com.maicard.money.service.pp.standard;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.GlobalOrderIdService;
import com.maicard.common.util.AgentUtils;
import com.maicard.common.util.JsonUtils;
import com.maicard.exception.RequiredObjectIsNullException;
import com.maicard.money.domain.Pay;
import com.maicard.money.domain.PayChannelMechInfo;
import com.maicard.money.iface.PayProcessor;
import com.maicard.money.service.ChannelService;
import com.maicard.money.service.PayService;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.ContextType;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.Operate;
import com.maicard.standard.TransactionStandard.TransactionStatus;

/**
 * 微信支付接口<br>
 * 官方接口
 * 
 * 通道参数存放规则:
 * mech.accountId = WEXIN APP ID 应用ID
 * mech.accountName = WEXIN PAY MECH ID 支付商户的ID
 * mech.payKey = WEIXIN PAY KEY 
 * 
 * 
 * @author NetSnake
 * @date 2015-11-02
 */
@Service
public class WeixinPayProcessor extends BaseService implements PayProcessor {

	@Resource
	private ConfigService configService;
	@Resource
	private GlobalOrderIdService globalOrderIdService;
	
	@Resource
	private PayService payService;

	@Resource
	private ChannelService channelService;

	private final SimpleDateFormat sdf = new SimpleDateFormat(CommonStandard.orderIdDateFormat);


	private int defaultPayOrderTimeoutSec = 24 * 3600;	//默认订单失效时间是24小时
	
	//默认提交地址：https://api.mch.weixin.qq.com/pay/unifiedorder



	@Override
	public EisMessage onPay(Pay pay){
		final int bodySize = 120;
		if(pay == null ){
			throw new RequiredObjectIsNullException("支付对象为空");
		}
		if(pay.getTransactionId() == null){
			throw new RequiredObjectIsNullException("支付对象的订单号为空");
		}
		if(pay.getStartTime() == null){
			pay.setStartTime(new Date());
		}

		if(pay.getParameter() == null){
			logger.error("支付订单[" + pay.getTransactionId() + "]没有附加参数");
			return new EisMessage(EisError.REQUIRED_PARAMETER.getId(),"支付对象没有附加参数");
		}
		if(!(pay.getParameter() instanceof  Map)){
			logger.error("支付订单[" + pay.getTransactionId() + "]的附加参数不是Map对象");
			return new EisMessage(EisError.REQUIRED_PARAMETER.getId(),"支付对象附加参数错误");
		}
		String bodyName = null;
		bodyName = pay.getName();
		if(bodyName != null && bodyName.length() >= bodySize){
			bodyName = bodyName.substring(0, bodySize)+ "...";
		}
		@SuppressWarnings("unchecked")
		Map<String,String> requestData = (Map<String,String>)pay.getParameter();


		String notifyUrl = null;
		if(pay.getNotifyUrl() != null){
			notifyUrl = pay.getNotifyUrl().replaceAll("\\$\\{payMethodId\\}", String.valueOf(pay.getPayMethodId()));
		} else {
			logger.error("准备创建的支付没有payNotifyUrl");
			return new EisMessage(EisError.REQUIRED_PARAMETER.getId(),"支付对象数据错误[payNotifyUrl]");
		}
		String appCode = pay.getExtraValue("appCode");
		logger.debug("当前支付请求的appCode=" + appCode);
		PayChannelMechInfo weixinMechInfo = channelService.getChannelInfo(pay);
		if(weixinMechInfo == null){
			logger.error("找不到ownerId=" + pay.getOwnerId() + "]的微信支付商户信息");			
			throw new RequiredObjectIsNullException("找不到必须的支付信息");
		}
		
		
		String nonceStr = null;
		if(StringUtils.isBlank(pay.getSign())){
			nonceStr = DigestUtils.md5Hex(UUID.randomUUID().toString());
		} else {
			nonceStr = pay.getSign();
		}
		Map<String, String> sPara = new HashMap<String, String>();
		sPara.put("appid", weixinMechInfo.accountId);
		sPara.put("mch_id", weixinMechInfo.accountName);
		sPara.put("device_info", "web");
		sPara.put("nonce_str", nonceStr);
		sPara.put("body", bodyName);


		if(pay.getDescription() != null){
			sPara.put("detail", pay.getDescription());
		}
		sPara.put("out_trade_no", pay.getTransactionId());
		sPara.put("total_fee", "" + (int)(pay.getFaceMoney() * 100));
		sPara.put("spbill_create_ip", requestData.get(DataName.clientIp.toString()));
		sPara.put("time_start", sdf.format(pay.getStartTime()));

		int payOrderTimeoutSec = configService.getIntValue(DataName.payOrderTimeoutSec.toString(), pay.getOwnerId());
		if(payOrderTimeoutSec == 0){
			payOrderTimeoutSec = defaultPayOrderTimeoutSec;
		}
		sPara.put("time_expire", sdf.format(DateUtils.addSeconds(pay.getStartTime(), payOrderTimeoutSec)));
		sPara.put("notify_url", notifyUrl);
		String contextType = null;
		logger.debug("检测到的应用环境是:" + pay.getContextType());
		if(pay.getContextType() == null){
			boolean isWeixinAccess = AgentUtils.isWeixinAccess(requestData.get("user-agent"));
			if(isWeixinAccess){
				//微信内公众号支付
				contextType = "JSAPI";
			} else if(pay.getContextType() != null && pay.getContextType().equals(ContextType.WAP.toString())){
				contextType = "MWEB";
			} else {
				//PC端扫码支付
				contextType = "NATIVE";
			}
		} else {
			if(pay.getContextType().equals(ContextType.APP.toString())){
				contextType = "APP";
			}  /*else if(pay.getContextType().equals(ContextType.PC.toString())){
				contextType = "JSAPI";
			} */else if(pay.getContextType().equals(ContextType.WAP.toString())){
				
				contextType = "MWEB";
				String WXSceneInfo = configService.getValue("WXSceneInfo", pay.getOwnerId());
				logger.debug("WXSceneInfo:{}",WXSceneInfo);
				if (WXSceneInfo!=null) {
					sPara.put("scene_info", WXSceneInfo);
				}
			}else if (pay.getContextType().equalsIgnoreCase("WEIXIN")) {
				contextType = "JSAPI";
			} else {
				contextType = "NATIVE";
			}
		}
		if(appCode != null){
			contextType = "APP";
		}
		sPara.put("trade_type", contextType);
		
		if(contextType.equals("JSAPI")){
			String openId = pay.getExtraValue("openId");
			if (StringUtils.isNotBlank(openId)) {
				openId = pay.getExtraValue("toAccountName");
			}
			if(StringUtils.isNotBlank(openId)){
				logger.debug("当前是微信环境，付款openId=" + pay.getExtraValue("openId"));
				sPara.put("openid", pay.getExtraValue("openId"));
			}
		}
		
		String sign = makeSign(sPara, weixinMechInfo.payKey);
		sPara.put("sign", sign);

		String xml = null;
		try{
			xml  = toXML(sPara);
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		logger.info("微信支付提交数据:" + xml + ",提交URL:" + weixinMechInfo.submitUrl);
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(weixinMechInfo.submitUrl);

		RequestEntity postEntity = null;
		try {
			postEntity = new StringRequestEntity(xml,"text/xml","UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		postMethod.setRequestEntity(postEntity);
		postMethod.setRequestHeader("Content-Type", "text/xml");
		String page = null;
		int sc = 0;
		try {
			sc = httpClient.executeMethod(postMethod);
			BufferedReader br2 = new   BufferedReader(new InputStreamReader(postMethod.getResponseBodyAsStream(),"UTF-8"));
			String temp2 = "";
			StringBuffer sb2 = new StringBuffer(1000);
			while((temp2 = br2.readLine()) != null){
				sb2.append(temp2 + "\n");
			}
			page = sb2.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("请求微信支付[" + pay.getTransactionId() + "]返回结果[HTTP STATUS:" + sc + ",page:" + page + "]");
		if(page == null){
			logger.error("无法获取微信支付返回结果");
			return new EisMessage(EisError.chargeToPeerError.getId(),"微信支付接口异常:" + sc);
		}
		if(!page.startsWith("<xml")){
			logger.error("微信支付返回结果不是XML:" + page);
			return new EisMessage(EisError.chargeToPeerError.getId(),"微信支付接口异常:" + sc);
		}
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringElementContentWhitespace(true);
		DocumentBuilder db = null;
		Document document = null;	
		NodeList list = null;

		try{

			StringReader sr = new StringReader(page);   	
			InputSource is = new InputSource(sr);   

			db = factory.newDocumentBuilder();
			document = db.parse(is);

		}catch(Exception e){
			logger.error(e.getMessage());
		}
		if(document == null){
			logger.error("无法解析微信支付返回结果:" + page);
			return new EisMessage(EisError.chargeToPeerError.getId(),"微信支付接口异常");
		}
		list = document.getChildNodes();
		if(list == null){
			logger.error("无法解析微信支付返回结果:" + page);
			return new EisMessage(EisError.chargeToPeerError.getId(),"微信支付接口异常");	
		}

		list = list.item(0).getChildNodes();
		String resultCode = null;
		String resultMessage = null;
		String outOrderId = null;
		String codeUrl = null;
		for(int i = 0; i < list.getLength(); i++){
			if(list.item(i).getNodeName().equals("return_code")){
				resultCode = list.item(i).getTextContent();
			}
			if(list.item(i).getNodeName().equals("return_msg")){
				resultMessage = list.item(i).getTextContent();
			}
			if(list.item(i).getNodeName().equals("prepay_id")){
				outOrderId = list.item(i).getTextContent();
			}
			if(list.item(i).getNodeName().equals("code_url")){
				codeUrl = list.item(i).getTextContent();
			}
			//FIXME 微信h5测试  mweb_url
			if (list.item(i).getNodeName().equals("mweb_url")) {
				codeUrl = list.item(i).getTextContent();
			}
		}	
		logger.info("微信支付订单[" + pay.getTransactionId() + "]请求结果[返回结果:" + resultCode + ",返回消息:" + resultMessage + ",微信订单号:" + outOrderId + ",扫码二维码地址:" + codeUrl + "]" );
		if(resultCode == null || !resultCode.toLowerCase().equals("success")){
			return new EisMessage(EisError.chargeToPeerError.getId(),"微信支付接口异常");	
		}
		pay.setCurrentStatus(TransactionStatus.inProcess.getId());


		Map<String,String> param = new HashMap<String,String>();
		long timestamp = pay.getStartTime().getTime() / 1000;
		param.put("timeStamp", ""+timestamp);
		param.put("appId", weixinMechInfo.accountId);
		param.put("nonceStr", pay.getSign());
		param.put("package", "prepay_id=" + outOrderId);
		param.put("signType", "MD5");
		String jsSign = makeSign(param, weixinMechInfo.payKey);
		String valueString = "";
		for (Map.Entry<String, String> entry : param.entrySet()) {
			if (entry.getValue() != "") {
				valueString += (entry.getKey() + "=" + entry.getValue() + "&");
			}
		}		
		logger.info("微信支付订单[" + pay.getTransactionId() + "]请求成功,返回[" + valueString + "]签名[" + jsSign + "]，当前访问类型:" + contextType);
		EisMessage message = new EisMessage();
		message.setMessage(outOrderId);
		message.setContent(outOrderId);
		if(contextType.equals("JSAPI")){
			message.setOperateCode(Operate.call.getId());
			message.setContent(valueString);
		} else {
			//FIXME 测试在h5中调用微信
			if (pay.getContextType().equals(ContextType.WAP.toString())) {
				message.setOperateCode(Operate.jump.getId());
			}else {
				message.setOperateCode(Operate.scanCode.getId());
			}
			message.setMessage(codeUrl);
		}
		if(contextType.equals(ContextType.APP.toString())){
			message.setOperateCode(Operate.call.getId());

			//生成APP端的所有请求数据，不把KEY等敏感信息放到APP端
			final String packageValue = "Sign=WXPay";
			final long ts = new Date().getTime() / 1000;
			sPara = new HashMap<String, String>();
			sPara.put("appid", weixinMechInfo.accountId);
			sPara.put("partnerid", weixinMechInfo.accountName);
			sPara.put("prepayid", outOrderId);
			sPara.put("package", packageValue);
			//sPara.put("package", "Sign");
			sPara.put("noncestr", nonceStr);
			sPara.put("timestamp", String.valueOf(ts));
			/*sPara.put("key", weixinMechInfo.payKey);
			ArrayList<String> keyList = new ArrayList<String>();
			for (Map.Entry<String, String> entry : sPara.entrySet()) {
				if (entry.getValue() != "") {
					keyList.add(entry.getKey() + "=" + entry.getValue() + "&");
				}
			}
			int size = keyList.size();
			String[] arrayToSort = keyList.toArray(new String[size]);
			Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < size; i++) {
				sb.append(arrayToSort[i]);
			}
			String signSource = sb.toString();
			signSource += "key=" + weixinMechInfo.payKey;
			String subSequence = (String)signSource.subSequence(0, signSource.length()-1);
			String appSign = DigestUtils.md5Hex(subSequence).toUpperCase();*/
			String appSign = makeSign(sPara, weixinMechInfo.payKey);
			message.setObjectType(weixinMechInfo.accountName);
			message.setMessage(outOrderId);
			message.setContent(nonceStr);
			message.setSign(appSign);
			message.setTimestamp(ts);
			//logger.debug("为APP支付生成签名，签名源:" + subSequence + ",签名结果:" + appSign + ",message.objectType设置为accountName:" + message.getObjectType() + ",message.message设置为outOrderId/prepayId=" + outOrderId + ",content设置为nonceStr:" + nonceStr + ",sign设置为sign:" + appSign + ",timestamp设置为timestamp:" + timestamp);
		} else {
			message.setObjectType(weixinMechInfo.accountId);
			message.setMessageId(pay.getSign());
			message.setSign(jsSign);
			message.setTimestamp(timestamp);
			//message.setTitle(nonceStr);
		}
		logger.debug("message="+JsonUtils.toStringFull(message));
		return message;
	}


	private String toXML(Map<String, String> sPara) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		for(String key : sPara.keySet()){
			sb.append("<" + key + ">");
			sb.append(sPara.get(key));
			sb.append("</" + key + ">");

		}
		sb.append("</xml>");
		return sb.toString();
	}


	private String makeSign(Map<String, String> sPara, String key) {
		ArrayList<String> list = new ArrayList<String>();
		for (Map.Entry<String, String> entry : sPara.entrySet()) {
			if (entry.getValue() != "") {
				list.add(entry.getKey() + "=" + entry.getValue() + "&");
			}
		}
		int size = list.size();
		String[] arrayToSort = list.toArray(new String[size]);
		Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size; i++) {
			sb.append(arrayToSort[i]);
		}
		String result = sb.toString();
		result += "key=" + key;
		logger.info(sPara.get("out_trade_no") + "微信支付签名源串:" + result);
		result = DigestUtils.md5Hex(result).toUpperCase();
		logger.info(sPara.get("out_trade_no") + "微信支付签名结果:" + result);
		return result;
	}

	@Override
	public Pay onResult(String resultString){
		final String successResponse = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";

		logger.debug("支付返回字符串:" + resultString);
		Pay pay = new Pay();
		pay.setCurrentStatus(EisError.UNKNOWN_ERROR.getId());
		Map<String,String> params = new HashMap<String,String>();
		try{
			resultString = java.net.URLDecoder.decode(resultString,"UTF-8");
			String[] param = resultString.split("&");

			for(int i = 0 ; i < param.length; i++){				
				//	logger.debug("XXXXXXXX=>" + param[i]);
				String[] kv = param[i].split("=");
				if(kv.length != 2 || kv[0] == null || kv[1] == null){
					continue;
				}
				params.put(kv[0], kv[1]);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		if(StringUtils.isBlank(params.get("rawContent"))){
			logger.error("微信支付通知中没有rawContent及POST数据");
			return pay;
		}
		String postData = params.get("rawContent").replaceAll("<\\!\\[CDATA\\[", "").replaceAll("\\]\\]>", "");

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringElementContentWhitespace(true);
		DocumentBuilder db = null;
		Document document = null;	
		NodeList list = null;

		try{

			StringReader sr = new StringReader(postData);   	
			InputSource is = new InputSource(sr);   

			db = factory.newDocumentBuilder();
			document = db.parse(is);

		}catch(Exception e){
			logger.error(e.getMessage());
		}
		if(document == null){
			logger.error("无法解析微信支付返回结果:" + postData);
			return pay;
		}
		list = document.getChildNodes();
		if(list == null){
			logger.error("无法解析微信支付返回结果:" + postData);
			return pay;	
		}

		list = list.item(0).getChildNodes();
		String resultCode = null;
		float realMoney = 0f;
		String error = null;
		String transactionId = null;
		String outOrderId = null;
		for(int i = 0; i < list.getLength(); i++){
			if(list.item(i).getNodeName().equals("result_code")){
				resultCode = list.item(i).getTextContent();
			}

			if(list.item(i).getNodeName().equals("out_trade_no")){
				transactionId = list.item(i).getTextContent();
			}
			if(list.item(i).getNodeName().equals("total_fee")){
				realMoney = Float.parseFloat(list.item(i).getTextContent()) / 100;
			}
			if(list.item(i).getNodeName().equals("transaction_id")){
				outOrderId = list.item(i).getTextContent();
			}
			if(list.item(i).getNodeName().equals("err_code_des")){
				error = list.item(i).getTextContent();
			}

		}	
		
		pay = payService.select(transactionId);

		if(pay == null){
			logger.error("找不到指定的支付订单:" + transactionId);
			pay = new Pay();
			pay.setCurrentStatus(EisError.OBJECT_IS_NULL.id);
			return pay;
		}
		pay.setOutOrderId(outOrderId);
		
		pay.setPayResultMessage(successResponse);
		if(resultCode != null && resultCode.equals("SUCCESS")){
			//支付成功
			pay.setCurrentStatus(TransactionStatus.success.getId());
			pay.setRealMoney(realMoney);
		} 
		logger.info("微信支付订单[" + pay.getTransactionId() + "]已返回[返回结果:" + resultCode + ",完成金额:" + pay.getRealMoney() + "元,返回错误:" + error + ",微信订单号:" + pay.getOutOrderId() + "]" );
		return pay;

	}
	


	/*private PayChannelMechInfo getMechInfo(long ownerId){
		PayChannelMechInfo weixinMechInfo = null;
		if(mechCache != null && mechCache.size() > 0){
			weixinMechInfo = mechCache.get(String.valueOf(ownerId));
			if(weixinMechInfo != null){
				logger.debug("从缓存中获取到ownerId=" + ownerId + "的微信商户信息:" + weixinMechInfo);
				return weixinMechInfo;
			}
		}
		weixinMechInfo = new PayChannelMechInfo();
		String key = configService.getValue(DataName.weixinPayKey.toString(), ownerId);
		if(key != null){
			weixinMechInfo.payKey = key;
		}
		String appId = configService.getValue(DataName.weixinAppId.toString(), ownerId);
		if(appId != null){
			weixinMechInfo.accountId = appId;
		}
		String weixinPayMechId = configService.getValue(DataName.weixinPayMechId.toString(), ownerId);
		if(weixinPayMechId != null){
			weixinMechInfo.accountName = weixinPayMechId;
		}
		synchronized(this){
			mechCache.put(String.valueOf(ownerId), weixinMechInfo);
		}
		logger.debug("把ownerId=" + ownerId + "的微信商户信息:" + weixinMechInfo + "放入缓存");
		return weixinMechInfo;
	}

	private PayChannelMechInfo getAppMechInfo(String appCode, long ownerId){

		if(StringUtils.isBlank(appCode)){
			appCode = "";
		} else {
			appCode += "_";
		}
		appCode = appCode.toUpperCase();
		PayChannelMechInfo weixinMechInfo = null;
		if(mechCache != null && mechCache.size() > 0){
			weixinMechInfo = mechCache.get(String.valueOf(ownerId));
			if(weixinMechInfo != null){
				logger.debug("从缓存中获取到ownerId=" + ownerId + "的微信APP商户信息:" + weixinMechInfo);
				return weixinMechInfo;
			}
		}
		weixinMechInfo = new PayChannelMechInfo();
		String key = configService.getValue(appCode + "APP_WEIXIN_PAY_KEY", ownerId);
		if(key != null){
			weixinMechInfo.payKey = key;
		}
		String appId = configService.getValue(appCode + "APP_WEIXIN_APP_ID", ownerId);
		if(appId != null){
			weixinMechInfo.accountId = appId;
		}
		String weixinPayMechId = configService.getValue(appCode + "APP_WEIXIN_PAY_MECH_ID", ownerId);
		if(weixinPayMechId != null){
			weixinMechInfo.accountName = weixinPayMechId;
		}
		String weixinCryptKey = configService.getValue(appCode +"APP_WEIXIN_CRYPT_KEY", ownerId);
		if(weixinCryptKey != null){
			weixinMechInfo.cryptKey = weixinCryptKey;
		}
		synchronized(this){
			mechCache.put(String.valueOf(ownerId), weixinMechInfo);
		}
		logger.debug("把ownerId=" + ownerId + "的微信APP商户信息:" + weixinMechInfo + "放入缓存");
		return weixinMechInfo;
	}*/
	@Override
	public EisMessage onQuery(Pay pay){
		return null;
	}


	@Override
	public EisMessage onRefund(Pay pay) {
		return new EisMessage(-EisError.serviceUnavaiable.id,"接口尚未支持退款");
	}


	@Override
	public String getDesc() {
		return "微信支付官方处理器" + this.getClass().getSimpleName();
	}
}

