package com.maicard.money.service.withdrawProcessor;

import java.io.StringReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ConfigService;
import com.maicard.common.util.HttpUtils;
import com.maicard.exception.RequiredObjectIsNullException;
import com.maicard.http.HttpClientPoolV3;
import com.maicard.http.HttpUtilsV3;
import com.maicard.money.criteria.BankAccountCriteria;
import com.maicard.money.domain.BankAccount;
import com.maicard.money.domain.PayChannelMechInfo;
import com.maicard.money.domain.Withdraw;
import com.maicard.money.service.WithdrawProcessor;
import com.maicard.money.service.WithdrawService;
import com.maicard.standard.EisError;
import com.maicard.standard.OperateResult;
import com.maicard.standard.TransactionStandard.TransactionStatus;

public class AlipayWithdrawProcessor extends BaseService implements WithdrawProcessor{

	@Resource
	private ConfigService configService;

	@Resource
	private WithdrawService withdrawService;

	private final  String inputCharset = "utf-8";


	private final String host = "mapi.alipay.com";
	private final int port = 443;
	private final String payUrl = "https://mapi.alipay.com/gateway.do";
	final String successResponse = "success";


	private static Map<String, PayChannelMechInfo> mechCache = new HashMap<String, PayChannelMechInfo>();
	private final static DecimalFormat df = new DecimalFormat("0.00");


	@Override
	public EisMessage onWithdraw(Withdraw withdraw) {
		logger.debug("支付宝收到提现/批付请求，转账金额是:" + withdraw.getArriveMoney());

		PayChannelMechInfo alipayMechInfo = _getMechInfo(withdraw.getOwnerId());
		if(alipayMechInfo == null){
			logger.error("找不到ownerId=" + withdraw.getOwnerId() + "]的支付宝商户信息");			
			throw new RequiredObjectIsNullException("找不到必须的支付信息");
		}
		String notifyUrl = withdraw.getExtraValue("notifyUrl");


		Map<String, String> sParaTemp = new HashMap<String, String>();

		sParaTemp.put("service", "alipay.fund.trans.tobank");
		sParaTemp.put("partner", alipayMechInfo.accountId);
		sParaTemp.put("_input_charset", inputCharset);
		sParaTemp.put("alipay_ca_request", "2");	
		sParaTemp.put("notify_url", notifyUrl);	
		sParaTemp.put("out_biz_no", withdraw.getTransactionId());	
		sParaTemp.put("payer_type", "ALIPAY_USERID");	
		sParaTemp.put("payer_account", alipayMechInfo.accountId);
		sParaTemp.put("payee_card_no", withdraw.getBankAccount().getBankAccountNumber());	
		sParaTemp.put("payee_account_name", withdraw.getBankAccount().getBankAccountName());	
		sParaTemp.put("payee_inst_name", withdraw.getBankAccount().getBankName());	
		if(withdraw.getBankAccount().getBankAccountType().equalsIgnoreCase(BankAccountCriteria.BAKC_ACCOUNT_TYPE_PERSONAL)){
			sParaTemp.put("payee_account_type", "2");	
		} else {
			sParaTemp.put("payee_account_type", "1");	
		}	

		sParaTemp.put("time_liness", "T0");	
		sParaTemp.put("amount", df.format(withdraw.getArriveMoney()));	
		sParaTemp.put("memo", "9");	







		List<String> keys = new ArrayList<String>(sParaTemp.keySet());
		Collections.sort(keys);

		StringBuffer sb = new StringBuffer();
		for (String key : keys) {
			String value = sParaTemp.get(key);
			if(StringUtils.isBlank(value)){
				continue;
			}
			sb.append(key);
			sb.append('=');
			sb.append(value);
			sb.append('&');		
		}

		String signString = sb.toString().replaceAll("&$", "");
		signString += alipayMechInfo.payKey; //把拼接后的字符串再与安全校验码直接连接起来
		String sign = DigestUtils.md5Hex(signString);
		logger.debug("根据源字符串[" + signString + "]生成签名:" + sign);



		keys = new ArrayList<String>(sParaTemp.keySet());
		Collections.sort(keys);

		sb = new StringBuffer();
		try{
			for (String key : keys) {
				String value = sParaTemp.get(key);
				if(StringUtils.isBlank(value)){
					continue;
				}
				sb.append(key);
				sb.append('=');
				if(key.equals("payee_account_name") || key.equals("payee_inst_name")){
					sb.append(java.net.URLEncoder.encode(value,"UTF-8"));

				} else {
					sb.append(value);
				}
				sb.append('&');		
			}
		}catch(Exception e){
			e.printStackTrace();

		}

		String queryString = sb.toString().replaceAll("&$", "") + "&sign_type=MD5";


		//long currentTime = new java.util.Date().getTime();
		//pay.setEndTime(new java.util.Date(currentTime - 100000000));

		queryString += "&sign=" + sign;

		String url = payUrl + "?" + queryString;

		logger.debug("请求支付宝提现接口::" + url);
		String result = null;
		HttpClient httpClient = HttpClientPoolV3.getHttpClient(host, port);
		try {
			result = HttpUtilsV3.getData(httpClient,  url, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("支付宝提现接口返回:" + result);

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringElementContentWhitespace(true);
		XPath xpath = XPathFactory.newInstance().newXPath();
		Document document = null;

		boolean isSuccess = false;

		String outOrderId = null;



		try{
			DocumentBuilder db = factory.newDocumentBuilder();
			StringReader sr = new StringReader(result);   	
			InputSource is = new InputSource(sr);   

			document = db.parse(is);

			String tradeStatus = (String) xpath.evaluate( "/alipay/response/alipay/result_code", document, XPathConstants.STRING);

			if(tradeStatus.equals("SUCCESS")){
				isSuccess = true;
			}
			outOrderId = (String) xpath.evaluate( "/alipay/response/alipay/order_id", document, XPathConstants.STRING);

			logger.debug("提现订单[" + withdraw.getTransactionId() + "]根据支付宝提现返回结果是=>支付宝订单号:" + outOrderId + ",交易状态是:" + tradeStatus);
			withdraw.setExtraValue("outOrderId", outOrderId);

		}catch(Exception e){
			e.printStackTrace();
		}
		if(isSuccess){
			withdraw.setCurrentStatus(TransactionStatus.inProcess.id);
			return new EisMessage(OperateResult.success.getId(),"提现提交成功");
		} else {
			withdraw.setCurrentStatus(TransactionStatus.failed.id);
			return new EisMessage(TransactionStatus.failed.getId(),"提现提交失败");

		}
	}

	@Override
	public EisMessage onQuery(Withdraw withdraw) {
		return null;
	}

	@Override
	public Withdraw onResult(String resultString) {
		logger.debug("解析支付宝提现返回结果:" + resultString);

		Withdraw withdraw = new Withdraw();
		Map<String,String> params = HttpUtils.getRequestDataMap(resultString);
		PayChannelMechInfo alipayMechInfo = getMechInfo(withdraw.getOwnerId());
		if(alipayMechInfo == null){
			logger.error("找不到ownerId=" + withdraw.getOwnerId() + "]的支付宝商户信息");			
			throw new RequiredObjectIsNullException("找不到必须的支付信息");
		}

		String tradeStatus = null;


		String outOrderId = null;
		String remoteSign = params.get("sign");



		String transactionId = params.get("out_biz_no");

		tradeStatus =  params.get("status");

		outOrderId = params.get("order_id");

		logger.debug("根据支付宝通知数据得到的订单号是:" + transactionId + ",支付宝订单号:" + outOrderId + ",交易状态是:" + tradeStatus);


		if(StringUtils.isBlank(remoteSign)){
			logger.error("支付宝返回没有sign参数");
			return null;
		}

		withdraw = withdrawService.select(transactionId);

		if(withdraw == null){
			logger.error("找不到指定的支付订单:" + transactionId);
			withdraw = new Withdraw();
			withdraw.setCurrentStatus(EisError.OBJECT_IS_NULL.id);
			return withdraw;
		}
		withdraw.setExtraValue("notifyResponse",successResponse);

		if(withdraw.getCurrentStatus() == TransactionStatus.success.getId() || withdraw.getCurrentStatus() == TransactionStatus.failed.id){
			return withdraw;
		}
		if(withdraw.getCurrentStatus() != TransactionStatus.inProcess.id){
			logger.error("指定的支付订单[" + transactionId + "]状态不是处理中，而是:" + withdraw.getCurrentStatus());
			return withdraw;
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


		signString += alipayMechInfo.payKey; //把拼接后的字符串再与安全校验码直接连接起来

		String mySign = DigestUtils.md5Hex(signString);
		logger.debug("支付宝返回的sign=" + remoteSign + ",我方对源[" + signString + "]校验结果是:" + mySign);

		withdraw.setTransactionId(transactionId);

		if(!mySign.equals(remoteSign)){
			logger.info("支付订单[" + withdraw.getTransactionId() + "]校验失败");
			withdraw.setCurrentStatus(EisError.VERIFY_ERROR.id);
			withdraw.setExtraValue("notifyResponse","fail");
			return withdraw;
		}
		withdraw.setExtraValue("notifyResponse",successResponse);
		if(tradeStatus.equals("SUCCESS")){//交易成功
			logger.info("提现订单[" + withdraw.getTransactionId() + "]交易成功");
			withdraw.setOutOrderId(outOrderId);
			withdraw.setCurrentStatus(TransactionStatus.success.getId());
			withdraw.setEndTime(new Date());

			return withdraw;

		}  else {
			withdraw.setArriveMoney(0);
			withdraw.setCurrentStatus(TransactionStatus.failed.getId());

			return withdraw;
		}
	}
	
	/**
	 * 仅用于临时使用，写死的账户和配置
	 * @return
	 */
	private PayChannelMechInfo _getMechInfo(long ownerId) {

		PayChannelMechInfo alipayMechInfo  = new PayChannelMechInfo();

		alipayMechInfo.accountId = "2088421598828672";

		alipayMechInfo.accountName = "whzhihe@sina.com";

		alipayMechInfo.payKey = "hcbaaoq6zzq6wa69n0cpmr484qs1xb32";





		return alipayMechInfo;
	}

	private PayChannelMechInfo getMechInfo(long ownerId) {

		PayChannelMechInfo alipayMechInfo = null;
		if(mechCache != null && mechCache.size() > 0){
			alipayMechInfo = mechCache.get(String.valueOf(ownerId));
		}
		if(alipayMechInfo == null){		
			alipayMechInfo = new PayChannelMechInfo();
			String key = configService.getValue("offlineAlipayAccountId", ownerId);
			if(key != null){
				alipayMechInfo.accountId = key;
			} else {
				logger.error("找不到ownerId=" + ownerId + "的支付宝信息offlineAlipayAccountId");
				return null;
			}

			String accountName = configService.getValue("offlineAlipayUserName", ownerId);
			if(accountName != null){
				alipayMechInfo.accountName = accountName;
			} else {
				logger.error("找不到ownerId=" + ownerId + "的支付宝信息offlineAlipayUserName");
				return null;
			}
			String payKey = configService.getValue("offlineAlipayKey", ownerId);
			if(payKey != null){
				alipayMechInfo.payKey = payKey;
			} else {
				logger.error("找不到ownerId=" + ownerId + "的支付宝信息offlineAlipayKey");
				return null;
			}		
			synchronized(this){
				mechCache.put(String.valueOf(ownerId), alipayMechInfo);
			}
			logger.debug("把ownerId=" + ownerId + "的支付宝商户信息:" + alipayMechInfo + "放入缓存");


		} else {
			logger.debug("从缓存中获取到ownerId=" + ownerId + "的支付宝商户信息:" + alipayMechInfo);
		}	

		return alipayMechInfo;
	}

	@Override
	public String getDesc() {
		return "支付宝提现处理器";
	}
	
	public static void main(String[] argv){
		
		BankAccount bankAccount = new BankAccount();
		
		
		String bankAccountNumber = "6210985840023147991";
		bankAccountNumber = bankAccountNumber.replaceAll("\\s", "");

		
		
		int i = 1;
		float total = 1;
		float remainMoney = total;
		while(remainMoney > 0){
			float money1 = 0;
			if(remainMoney < 49999){
				money1 = remainMoney;
			} else {
				money1 = 45000 + RandomUtils.nextInt(5000);
			}
			remainMoney -= money1;
			System.out.println("第" + i + "次提现金额:" + money1 + ",剩余:" + remainMoney);
			Withdraw withdraw = new Withdraw();
			withdraw.setExtraValue("notifyUrl", "http://www.baidu.com");
			withdraw.setTransactionId(new Date().getTime() + "");
			withdraw.setArriveMoney(money1);
			withdraw.setBankAccount(bankAccount);
			AlipayWithdrawProcessor instance = new AlipayWithdrawProcessor();
			instance.onWithdraw(withdraw);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			i++;


		}
		/*Withdraw withdraw = new Withdraw();
		withdraw.setExtraValue("notifyUrl", "http://www.baidu.com");
		withdraw.setTransactionId(new Date().getTime() + "");
		withdraw.setArriveMoney(49872.0f);
		withdraw.setBankAccount(bankAccount);
		AlipayWithdrawProcessor instance = new AlipayWithdrawProcessor();
		instance.onWithdraw(withdraw);*/
	}


}
