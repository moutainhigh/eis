package com.maicard.money.service.pp.standard;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.util.HttpUtils;
import com.maicard.exception.RequiredObjectIsNullException;
import com.maicard.money.domain.Pay;
import com.maicard.money.domain.PayChannelMechInfo;
import com.maicard.money.domain.PayMethod;
import com.maicard.money.iface.PayProcessor;
import com.maicard.money.service.PayMethodService;
import com.maicard.money.service.PayService;
import com.maicard.standard.EisError;
import com.maicard.standard.TransactionStandard.TransactionStatus;
@Service
public class QuickSDKPayProcessor extends BaseService implements PayProcessor {

	@Resource
	private PayService payService;
	@Resource
	private PayMethodService payMethodService;
	
	final String successResponse = "success";
	public EisMessage onPay(Pay pay) {
		if(pay == null ){
			throw new RequiredObjectIsNullException("支付对象为空");
		}
		if(pay.getTransactionId() == null){
			throw new RequiredObjectIsNullException("支付对象的订单号为空");
		}
		if(pay.getStartTime() == null){
			pay.setStartTime(new Date());
		}
		pay.setCurrentStatus(TransactionStatus.inProcess.getId());
		EisMessage message = new EisMessage();
		message.setContent(pay.getTransactionId());
		return message;
	}

	public EisMessage onQuery(Pay pay) {
		// TODO Auto-generated method stub
		return null;
	}

	public EisMessage onRefund(Pay pay) {
		// TODO Auto-generated method stub
		return null;
	}

	public Pay onResult(String resultString) {
		logger.debug("解析QuickSdk返回结果:" + resultString);
		
		Pay pay = new Pay();
		pay.setPayMethodId(103);
		Map<String,String> params = HttpUtils.getRequestDataMap(resultString);

		
		String nt_data = params.get("nt_data");
		if(nt_data == null){
			logger.error("在QuickSdk回调字符串中找不到我方支付相关数据");
			pay.setCurrentStatus(EisError.dataError.id);
			return pay;			
		}

		String remoteSign = params.get("sign");
		if(StringUtils.isBlank(remoteSign)){
			logger.error("QuickSdk返回没有sign参数");
			return null;
		}
		
		String md5Sign = params.get("md5Sign");
		if(StringUtils.isBlank(md5Sign)){
			logger.error("QuickSdk返回没有sign参数");
			return null;
		}

		logger.debug("开始校验支付签名");
		PayMethod payMethod = pay.getPayMethod();
		if(payMethod == null && pay.getPayMethodId() > 0){
			payMethod = payMethodService.select(pay.getPayMethodId());
		}
		Assert.notNull(payMethod,"尝试获取支付参数的pay实例中的payMethod不能为空");
		
		String Callback_Key = payMethod.getExtraValue("Callback_Key");
		String Md5_Key = payMethod.getExtraValue("Md5_Key");
		if (StringUtils.isBlank(Callback_Key) || StringUtils.isBlank(Md5_Key)) {
			logger.error("支付方式{}没有加密数据",payMethod.getPayMethodId());
			return null;
		}
		String remoteMd5Sign = DigestUtils.md5Hex(nt_data+remoteSign+Callback_Key);
		if (!remoteMd5Sign.equals(md5Sign)) {
			logger.debug("CALL_BACK_KEY,加密后的数据为{},返回的{},准备使用Md5_key",remoteMd5Sign,md5Sign);
			remoteMd5Sign = DigestUtils.md5Hex(nt_data+remoteSign+Md5_Key);
		}
		if (!remoteMd5Sign.equals(md5Sign)) {
			logger.debug("Md5_key,加密后的数据为{},返回的{},验证失败",remoteMd5Sign,md5Sign);
			return null;
		}
		
		String decode = decode(nt_data, Callback_Key);
		
		logger.debug("解密后的nt_data为{}",decode);
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringElementContentWhitespace(true);
		DocumentBuilder db = null;
		Document document = null;	
		NodeList list = null;

		try{

			StringReader sr = new StringReader(decode);   	
			InputSource is = new InputSource(sr);   

			db = factory.newDocumentBuilder();
			document = db.parse(is);

		}catch(Exception e){
			logger.error(e.getMessage());
		}
		if(document == null){
			logger.error("无法解析QuickSdk返回结果:" + decode);
			return pay;
		}
		list = document.getElementsByTagName("message");
		if(list == null){
			logger.error("无法解析QuickSdk返回结果:" + decode);
			return pay;	
		}

		list = list.item(0).getChildNodes();
		String resultCode = null;
		float realMoney = 0f;
		String error = null;
		String transactionId = null;
		String outOrderId = null;
		String channel = null;
		String isTest = null;
		String channelUid = null;
		for(int i = 0; i < list.getLength(); i++){
			if(list.item(i).getNodeName().equals("is_test")){
				isTest = list.item(i).getTextContent();
			}
			if(list.item(i).getNodeName().equals("channel")){
				channel = list.item(i).getTextContent();
			}
			if(list.item(i).getNodeName().equals("channel_uid")){
				channelUid = list.item(i).getTextContent();
			}
			if(list.item(i).getNodeName().equals("status")){
				resultCode = list.item(i).getTextContent();
			}

			if(list.item(i).getNodeName().equals("game_order")){
				transactionId = list.item(i).getTextContent();
			}
			if(list.item(i).getNodeName().equals("amount")){
				realMoney = Float.parseFloat(list.item(i).getTextContent()) ;
			}
			if(list.item(i).getNodeName().equals("order_no")){
				outOrderId = list.item(i).getTextContent();
			}
			/*if(list.item(i).getNodeName().equals("err_code_des")){
				error = list.item(i).getTextContent();
			}*/

		}
		
		logger.info("支付订单{}返回的结果为{}",transactionId,resultCode);
		if (resultCode.equals("1")) {
			pay.setRealMoney(0);
			pay.setCurrentStatus(TransactionStatus.failed.getId());
			return pay;
		}
		
		pay = payService.select(transactionId);

		if(pay == null){
			logger.error("找不到指定的支付订单:" + transactionId);
			pay = new Pay();
			pay.setCurrentStatus(EisError.OBJECT_IS_NULL.id);
			return pay;
		}
		pay.setPayResultMessage(successResponse);
		if(pay.getCurrentStatus() == TransactionStatus.success.getId() || pay.getCurrentStatus() == TransactionStatus.failed.id){
			return pay;

		}
		if(pay.getCurrentStatus() != TransactionStatus.inProcess.id){
			logger.error("指定的支付订单[" + transactionId + "]状态不是处理中，而是:" + pay.getCurrentStatus());
			return pay;
		}
		
		logger.info("支付订单[" + pay.getTransactionId() + "]交易成功");
		pay.setTransactionId(transactionId);
		pay.setOutOrderId(outOrderId);

		pay.setRealMoney(realMoney);
		pay.setCurrentStatus(TransactionStatus.success.getId());
		pay.setEndTime(new Date());
		
		pay.setCurrentStatus(TransactionStatus.success.getId());
		
		
		logger.info("QuickSdk支付订单[" + pay.getTransactionId() + "]已返回[返回结果:" + pay.getCurrentStatus() + ",完成金额:" + pay.getRealMoney() + "元,QuickSdk订单号:" + pay.getOutOrderId() + "]" );
		return pay;

		
	}

	public String getDesc() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String encode(String src,String key) { 
		 String charset="utf-8";
		try {
			byte[] data = src.getBytes(charset);
			byte[] keys = key.getBytes();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < data.length; i++) {
				int n = (0xff & data[i]) + (0xff & keys[i % keys.length]);
				sb.append("@" + n);
			}
			return sb.toString();
		}catch (UnsupportedEncodingException e){
			e.printStackTrace();
		}
		return src;
	}
	
	public static String decode(String src,String key) {
		Pattern pattern = Pattern.compile("\\d+");
		 String charset="utf-8";
		if(src == null || src.length() == 0){
			return src;
		}
		Matcher m = pattern.matcher(src);
		List<Integer> list = new ArrayList<Integer>();
		while (m.find()) {
			try {
				String group = m.group();
				list.add(Integer.valueOf(group));
			} catch (Exception e) {
				e.printStackTrace();
				return src;
			}
		}

		if (list.size() > 0) {
			try {
				byte[] data = new byte[list.size()];
				byte[] keys = key.getBytes();

				for (int i = 0; i < data.length; i++) {
					data[i] = (byte) (list.get(i) - (0xff & keys[i % keys.length]));
				}
				return new String(data, charset);
			} catch (UnsupportedEncodingException e){
				e.printStackTrace();
			}
			return src;
		} else {
			return src;
		}
	}
}
