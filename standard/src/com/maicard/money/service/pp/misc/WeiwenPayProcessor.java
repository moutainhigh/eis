package com.maicard.money.service.pp.misc;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ConfigService;
import com.maicard.common.util.HttpUtils;
import com.maicard.common.util.JsonUtils;
import com.maicard.exception.RequiredObjectIsNullException;
import com.maicard.money.domain.Pay;
import com.maicard.money.domain.PayChannelMechInfo;
import com.maicard.money.iface.PayProcessor;
import com.maicard.money.service.PayService;
import com.maicard.standard.EisError;
import com.maicard.standard.Operate;
import com.maicard.standard.TransactionStandard.TransactionStatus;

import org.springframework.stereotype.Service;


/**
 * 微闻支付接口，目前支持微闻和微信二维码扫码支付
 * 
 * 
 * @author NetSnake
 * @date 2013-08-02
 */
@Service
public class WeiwenPayProcessor extends BaseService implements PayProcessor{

	@Resource
	private ConfigService configService;
	@Resource
	private PayService payService;


	private static Map<String, PayChannelMechInfo> mechCache = new HashMap<String, PayChannelMechInfo>();
	
	private final int MAX_NAME_LENGTH = 32;
	private final DecimalFormat df=new DecimalFormat("0.00"); 



	public EisMessage onPay(Pay pay){
		if(pay == null ){
			throw new RequiredObjectIsNullException("支付对象为空");
		}
		if(pay.getTransactionId() == null){
			throw new RequiredObjectIsNullException("支付对象的订单号为空");

		}

		String notifyUrl = pay.getNotifyUrl();

		
		String returnUrl = pay.getReturnUrl();

		
		
		PayChannelMechInfo payChannelMechInfo = getMechInfo(pay.getOwnerId());
		
		String name = null;
		if(pay.getName() != null){
			name = pay.getName();
		} else {
			name = "支付";
		}
		if(name.length() > MAX_NAME_LENGTH){
			name = name.substring(0,MAX_NAME_LENGTH);
		}
		String	timestamp = String.valueOf(new Date().getTime());
		


		Map<String,String> param = new HashMap<String, String>();
		param.put("payFromAccount", String.valueOf(payChannelMechInfo.accountId));
		param.put("faceMoney", String.valueOf(df.format(pay.getFaceMoney())));
		param.put("name", name);
		param.put("timestamp", timestamp);
		param.put("inOrderId", pay.getTransactionId());
		if(StringUtils.isNotBlank(notifyUrl)){
			notifyUrl = notifyUrl.replaceAll("\\$\\{payMethodId\\}", String.valueOf(pay.getPayMethodId()));
			param.put("inNotifyUrl", notifyUrl);
		}
		if(StringUtils.isNotBlank(returnUrl)){
			returnUrl = returnUrl.replaceAll("\\$\\{payMethodId\\}", String.valueOf(pay.getPayMethodId()));
			param.put("inJumpUrl", returnUrl);
		}
	
		String payTypeName = pay.getExtraValue("payTypeName");
		if(payTypeName != null){
			if(payTypeName.indexOf("微信") >= 0){
				param.put("payTypeId", "1");
			}
		}
		if(param.get("payTypeId") == null){
			param.put("payTypeId", "2");
		}


		ArrayList<String> list = new ArrayList<String>();
		for (String key : param.keySet()) {
			list.add(key);
		}
		int size = list.size();
		String[] arrayToSort = list.toArray(new String[size]);
		Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
		StringBuilder sb = new StringBuilder();


		for (int i = 0; i < size; i++) {
			sb.append(arrayToSort[i]);
			sb.append('=');

			sb.append(param.get(arrayToSort[i]));

			sb.append('&');
		}

		sb.append("key=").append(payChannelMechInfo.payKey);
		String signSource = sb.toString();
		String ourSign = DigestUtils.md5Hex(signSource);
		System.out.println("我方对源[" + signSource + "]签名结果:" + ourSign);

		sb.setLength(0);

		
		for (String key : param.keySet()) {
			sb.append(key);
			sb.append('=');
			if(key.equals("name") || key.equals("description") || key.equals("inJumpUrl")){
				try {
					sb.append(java.net.URLEncoder.encode(param.get(key),"UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} else {
				sb.append(param.get(key));
			}
			sb.append('&');
		}
		String queryString = sb.toString().replaceAll("&$","");



		queryString += "&sign=" + ourSign;

		String url = payChannelMechInfo.submitUrl + "?" + queryString;
		logger.debug("为支付订单[" + pay.getTransactionId() + "]发送支付请求，地址:" + url);
		String page = null;
		try{
			page = HttpUtils.postData(url, "");
		}catch(Exception e){
			logger.error("无法访问微闻支付接口:" + e.getMessage());
			pay.setCurrentStatus(EisError.serviceUnavaiable.getId());
			return new EisMessage(TransactionStatus.failed.getId(),"无法完成支付，无法访问对方API接口");
		}
		logger.debug("微闻支付接口返回页面:" + page);
		if(page == null){
			logger.error("无法访问炒卡网API接口，返回页面为空");
			pay.setCurrentStatus(EisError.serviceUnavaiable.getId());
			return new EisMessage(TransactionStatus.failed.getId(),"无法完成支付，无法访问对方API接口");
		}
		ObjectMapper om = JsonUtils.getInstance();
		EisMessage resultMessage = null;
		try{
			JsonNode rootNode = om.readValue(page, JsonNode.class);    
			resultMessage = om.readValue(rootNode.path("message").toString(), EisMessage.class);
		}catch(Exception e){
			logger.error("无法将炒卡网API接口返回页面解析为对象:" + e.getMessage());
			pay.setCurrentStatus(EisError.serviceUnavaiable.getId());
			return new EisMessage(TransactionStatus.failed.getId(),"无法完成支付，对方API接口异常");
		}

		if(resultMessage == null){
			logger.error("无法将返回数据中的message转换为EisMessage对象");
			pay.setCurrentStatus(EisError.serviceUnavaiable.getId());
			return new EisMessage(TransactionStatus.failed.getId(),"无法完成支付，对方API接口异常");			
		}

		if(resultMessage.getOperateCode() == Operate.iframe.getId()){
			logger.debug("支付订单[" + pay.getTransactionId() + "]已提交到微闻支付系统");
			EisMessage msg = new  EisMessage(TransactionStatus.inProcess.getId(), pay.getTransactionId());
			msg.setMessage(resultMessage.getMessage());
			pay.setCurrentStatus(TransactionStatus.inProcess.getId());
			return msg;
		}
		pay.setCurrentStatus(TransactionStatus.failed.getId());
		return new EisMessage(TransactionStatus.failed.getId(),"无法完成支付");
	}

	





	public EisMessage onQuery(Pay pay){
		
		return new EisMessage(EisError.UNKNOWN_ERROR.getId(),"无法完成查询");

	}

	public Pay onResult(String resultString){
		


		logger.debug("解析微闻返回结果:" + resultString);

		Pay pay = new Pay();
		Map<String,String> params = HttpUtils.getRequestDataMap(resultString);
		PayChannelMechInfo payChannelMechInfo = getMechInfo(pay.getOwnerId());
		if(payChannelMechInfo == null){
			logger.error("找不到ownerId=" + pay.getOwnerId() + "]的微闻商户信息");			
			throw new RequiredObjectIsNullException("找不到必须的支付信息");
		}

		String transactionId = params.get("orderId");
		String outOrderId = params.get("transactionId");
		final String responseString = "true";

	

		String tradeStatus = params.get("result");

		float realMoney = 0f;

		String remoteSign = params.get("sign");
		
		

		pay = payService.select(transactionId);

		if(pay == null){
			logger.error("找不到指定的支付订单:" + transactionId);
			pay = new Pay();
			pay.setCurrentStatus(EisError.OBJECT_IS_NULL.id);
			return pay;
		}
		pay.setPayResultMessage(responseString);
		if(pay.getCurrentStatus() == TransactionStatus.success.getId() || pay.getCurrentStatus() == TransactionStatus.failed.id){
			return pay;
		}
		if(pay.getCurrentStatus() != TransactionStatus.inProcess.id){
			logger.error("指定的支付订单[" + transactionId + "]状态不是处理中，而是:" + pay.getCurrentStatus());
			return pay;
		}



		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);

		StringBuffer sb = new StringBuffer();
		try{
			for (String key : keys) {
				String value = params.get(key);
				if(StringUtils.isBlank(value) || key.equals("sign_type") || key.equals("sign")){
					continue;
				}
				sb.append(key);
				sb.append('=');
				sb.append(java.net.URLDecoder.decode(value,"UTF-8"));
				sb.append('&');		
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		String signString = sb.toString().replaceAll("&$", "");		


		signString += "&key=";
		signString += payChannelMechInfo.payKey; //把拼接后的字符串再与安全校验码直接连接起来

		String mySign = DigestUtils.md5Hex(signString);
		logger.debug("微闻返回的sign=" + remoteSign + ",我方对源[" + signString + "]校验结果是:" + mySign);

		pay.setTransactionId(transactionId);

		if(!mySign.equals(remoteSign)){
			logger.info("支付订单[" + pay.getTransactionId() + "]校验失败");
			pay.setCurrentStatus(EisError.VERIFY_ERROR.id);
			pay.setPayResultMessage("fail");
			return pay;
		}
		pay.setOutOrderId(outOrderId);

		pay.setPayResultMessage(responseString);
		if(tradeStatus != null && tradeStatus.equals("710010")){//交易成功
			realMoney = Float.parseFloat(params.get("successMoney"));
			logger.info("支付订单[" + pay.getTransactionId() + "]交易成功,成功金额:" + realMoney);

			pay.setRealMoney(realMoney);
			pay.setCurrentStatus(TransactionStatus.success.getId());
			pay.setEndTime(new Date());
			return pay;

		}  else {
			pay.setRealMoney(0);
			pay.setCurrentStatus(TransactionStatus.failed.getId());
			return pay;
		}



	}





	@Override
	public EisMessage onRefund(Pay pay) {
		// TODO Auto-generated method stub
		return null;
	}





	@Override
	public String getDesc() {
		return "微闻支付处理器" + this.getClass().getSimpleName();
	}
	private PayChannelMechInfo getMechInfo(long ownerId) {
		

		PayChannelMechInfo payChannel = null;
		if(mechCache != null && mechCache.size() > 0){
			payChannel = mechCache.get(String.valueOf(ownerId));
		}
		if(payChannel == null){		
			payChannel = new PayChannelMechInfo();
			String submitUrl = configService.getValue("weiwenSubmitUrl", ownerId);
			if(submitUrl != null){
				payChannel.submitUrl = submitUrl;
			} else {
				logger.error("找不到ownerId=" + ownerId + "的微闻支付信息submitUrl");
				return null;
			}				
			payChannel.submitUrl = submitUrl;
			String accountId = configService.getValue("weiwenPayAccountId", ownerId);
			if(accountId != null){
				payChannel.accountId = accountId;
			} else {
				logger.error("找不到ownerId=" + ownerId + "的微闻支付信息weiwenPayAccountId");
				return null;
			}			
			String payKey = configService.getValue("weiwenPayKey", ownerId);
			if(payKey != null){
				payChannel.payKey = payKey;
			} else {
				logger.error("找不到ownerId=" + ownerId + "的微闻支付信息weiwenPayKey");
				return null;
			}		
			synchronized(this){
				mechCache.put(String.valueOf(ownerId), payChannel);
			}
			logger.debug("把ownerId=" + ownerId + "的微闻支付商户信息:" + payChannel + "放入缓存");


		} else {
			logger.debug("从缓存中获取到ownerId=" + ownerId + "的微闻支付商户信息:" + payChannel);
		}	

		return payChannel;
	}



}
