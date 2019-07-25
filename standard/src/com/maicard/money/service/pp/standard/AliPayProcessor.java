package com.maicard.money.service.pp.standard;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.GlobalOrderIdService;
import com.maicard.common.service.HttpsService;
import com.maicard.common.service.UuidService;
import com.maicard.common.util.HttpUtils;
import com.maicard.exception.RequiredObjectIsNullException;
import com.maicard.money.domain.Pay;
import com.maicard.money.domain.PayChannelMechInfo;
import com.maicard.money.domain.Refund;
import com.maicard.money.iface.PayProcessor;
import com.maicard.money.service.ChannelService;
import com.maicard.money.service.PayService;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.ContextType;
import com.maicard.standard.EisError;
import com.maicard.standard.Operate;
import com.maicard.standard.OperateResult;
import com.maicard.standard.TransactionStandard.TransactionStatus;
import com.maicard.standard.TransactionStandard.TransactionType;

/**
 * 
 * 更新为云用户模式，PC支付从跳转改为支持扫描二维码,NetSnake,2016-06-28
 * 
 * 
 * 支付宝充值接口
 * @author NetSnake
 * @date 2012-8-4
 */
@Service
public class AliPayProcessor extends BaseService implements PayProcessor {

	@Resource
	private ConfigService configService;
	@Resource
	private GlobalOrderIdService globalOrderIdService;
	@Resource
	private HttpsService httpsService;
	@Resource
	private PayService payService;
	@Resource
	private UuidService uuidService;
	

	@Resource
	private ChannelService channelService;

	private String inputCharset = "UTF-8";
	final String successResponse = "success";




	private final String payUrl = "https://mapi.alipay.com/gateway.do";
	private final String refundUrl = "https://mapi.alipay.com/gateway.do";


	public EisMessage onPay(Pay pay){

		Assert.notNull(pay,"尝试进行的支付对象不能为空");
		Assert.notNull(pay.getTransactionId(),"尝试开始的支付对象订单号不能为空");
		Assert.notNull(pay.getName(),"尝试开始的支付对象name不能为空");

		PayChannelMechInfo alipayMechInfo = channelService.getChannelInfo(pay);
		if(alipayMechInfo == null){
			logger.error("找不到ownerId=" + pay.getOwnerId() + "]的支付宝商户信息");			
			throw new RequiredObjectIsNullException("找不到必须的支付信息");
		}


		String subject = pay.getName();
		String body = pay.getDescription();
		String total_fee = new DecimalFormat("0.00").format(pay.getFaceMoney());
	//	String paymethod = null;
	//	String defaultbank = null;
		/*if(pay.getPayTypeId() != 10){ //账户支付
			paymethod = "bankPay";
		} else {
			paymethod = "";

		}*/

		String anti_phishing_key  = "";
		String exter_invoke_ip= "";//request.getLocalAddr();


		String extra_common_param = "";
		String buyer_email = "";


		String royalty_type = "";
		String royalty_parameters ="";
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("payment_type", "1");	//按照支付宝文档，必须为1
		sParaTemp.put("out_trade_no", pay.getTransactionId());
		sParaTemp.put("subject", subject);
		if(StringUtils.isNotBlank(body)){
			sParaTemp.put("body", body);
		}
		sParaTemp.put("total_fee", total_fee);
		sParaTemp.put("paymethod", "");
		sParaTemp.put("defaultbank", "");
		sParaTemp.put("anti_phishing_key", anti_phishing_key);
		sParaTemp.put("exter_invoke_ip", exter_invoke_ip);
		sParaTemp.put("extra_common_param", extra_common_param);
		sParaTemp.put("buyer_email", buyer_email);
		sParaTemp.put("royalty_type", royalty_type);
		sParaTemp.put("royalty_parameters", royalty_parameters);

		String returnUrl = pay.getReturnUrl();
		String notifyUrl = pay.getNotifyUrl();
		
		boolean directJump = false;

		if(pay.getContextType() != null && (pay.getContextType().equals(ContextType.WAP.toString())||pay.getContextType().equals(ContextType.APP.toString()) )){
			sParaTemp.put("service", "alipay.wap.create.direct.pay.by.user");
			directJump = true;
		} else {
			sParaTemp.put("service", "create_direct_pay_by_user");
		}
		sParaTemp.put("partner", alipayMechInfo.accountId);
		if(StringUtils.isNotBlank(returnUrl)){
			returnUrl = returnUrl.replaceAll("\\$\\{payMethodId\\}", String.valueOf(pay.getPayMethodId()));
			sParaTemp.put("return_url", returnUrl);
		}
		if(StringUtils.isNotBlank(notifyUrl)){
			notifyUrl = notifyUrl.replaceAll("\\$\\{payMethodId\\}", String.valueOf(pay.getPayMethodId()));
			sParaTemp.put("notify_url", notifyUrl);
		}
		//sParaTemp.put("seller_email", alipayMechInfo.accountName);
		sParaTemp.put("seller_id", alipayMechInfo.accountId);
		sParaTemp.put("_input_charset", inputCharset);
		//是否使用适配iframe的简易模式
		int qrMode = pay.getFlag();
		sParaTemp.put("qr_pay_mode", String.valueOf(qrMode));
		if(qrMode == 2){
			directJump = true;
		}

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

		String queryString = signString + "&sign_type=MD5";

		signString += alipayMechInfo.payKey; //把拼接后的字符串再与安全校验码直接连接起来
		String sign = DigestUtils.md5Hex(signString);
		logger.debug("根据源字符串[" + signString + "]生成签名:" + sign);

		//long currentTime = new java.util.Date().getTime();
		//pay.setEndTime(new java.util.Date(currentTime - 100000000));

		queryString += "&sign=" + sign;

		String url = payUrl + "?" + queryString;

		logger.debug("为支付生成跳转URL:" + url);

		EisMessage eisMessage = null;
		pay.setCurrentStatus(TransactionStatus.inProcess.getId());
		if(directJump){
			logger.debug("当前模式为直接跳转模式，跳转到:" + url);
			eisMessage = new EisMessage(Operate.jump.getId(), url);
			eisMessage.setContent(queryString);
			return eisMessage;
		}
		logger.debug("当前模式为简易扫码模式，iframe url:" + url);
		return new EisMessage(Operate.iframe.getId(), url);
	}

	@Override
	public Pay onResult(String resultString){
		logger.debug("解析支付宝返回结果:" + resultString);
		
		Pay pay = new Pay();
		Map<String,String> params = HttpUtils.getRequestDataMap(resultString);


		String transactionId = params.get("out_trade_no");
		if(transactionId == null){
			logger.error("在支付宝回调字符串中找不到我方订单号数据");
			pay.setCurrentStatus(EisError.dataError.id);
			return pay;			
		}

		String remoteSign = params.get("sign");
		if(StringUtils.isBlank(remoteSign)){
			logger.error("支付宝返回没有sign参数");
			return null;
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
		PayChannelMechInfo alipayMechInfo = channelService.getChannelInfo(pay);

		if(alipayMechInfo == null){
			logger.error("找不到ownerId=" + pay.getOwnerId() + "]的支付宝商户信息");			
			throw new RequiredObjectIsNullException("找不到必须的支付信息");
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


		if(mySign.equals(remoteSign)){//确认校验无误

			if(params.get("trade_status").equals("TRADE_SUCCESS")){//交易成功
				logger.info("支付订单[" + pay.getTransactionId() + "]交易成功");
				String outOrderId = params.get("trade_no");
				float money = Float.parseFloat(params.get("total_fee"));
				pay.setTransactionId(params.get("out_trade_no"));
				pay.setOutOrderId(outOrderId);

				pay.setRealMoney(money);
				pay.setCurrentStatus(TransactionStatus.success.getId());
				pay.setEndTime(new Date());
				/*XXX
				 *  设置lockStatus以保证更新的记录状态一定是正在处理中，如果是其他状态将返回失败
				 *  否则将可能导致订单的重复成功
				 */
				/*pay.setLockStatus(Constants.TransactionStatus.inProcess.getId());
				if(simplTransactionService.update(pay) > 0){
					pay.setLockStatus(0);
					pay.setCurrentStatus(Constants.TransactionStatus.success.getId());
					return pay;
				}*/
				pay.setCurrentStatus(TransactionStatus.success.getId());
				return pay;

			}  else {
				pay.setRealMoney(0);
				pay.setCurrentStatus(TransactionStatus.failed.getId());
				/*simpleTransactionCriteria.setCurrentStatus(Constants.TransactionStatus.failed.getId());
				payList = null;
				payList = simplTransactionService.list(simpleTransactionCriteria);
				if(payList == null || payList.size() < 0){
					logger.info("支付订单[" + pay.getPayOrderId() + "],对方返回交易失败,之前未做更新，更新为失败pay记录");
					pay.setEndTime(new Date());
					simplTransactionService.update(pay);
				} else {
					logger.info("支付订单[" + pay.getPayOrderId() + "],对方返回交易失败,之前已更新，忽略");
				}*/
				return pay;
			}
		}
		logger.info("支付宝支付订单[" + pay.getTransactionId() + "]已返回[返回结果:" + pay.getCurrentStatus() + ",完成金额:" + pay.getRealMoney() + "元,支付宝订单号:" + pay.getOutOrderId() + "]" );
		return pay;

	}

	public EisMessage onQuery(Pay pay){
		Assert.notNull(pay,"尝试进行退款的支付对象不能为空");
		Assert.notNull(pay.getTransactionId(),"尝试进行退款的支付对象订单号不能为空");

		PayChannelMechInfo alipayMechInfo = channelService.getChannelInfo(pay);
		if(alipayMechInfo == null){
			logger.error("找不到ownerId=" + pay.getOwnerId() + "]的支付宝商户信息");			
			throw new RequiredObjectIsNullException("找不到必须的支付信息");
		}


		String subject = pay.getName();
		String body = pay.getDescription();
		String total_fee = String.valueOf(pay.getFaceMoney());
		String paymethod = null;
		String defaultbank = null;
		logger.info("payTypeId:" + pay.getPayTypeId());
		if(pay.getPayTypeId() != 10){ //账户支付
			paymethod = "bankPay";
			defaultbank = pay.getExtraValue("toAccountName");
		} else {
			paymethod = "";
			defaultbank = "";

		}

		String anti_phishing_key  = "";
		String exter_invoke_ip= "";//request.getLocalAddr();


		String extra_common_param = "";
		String buyer_email = "";


		String royalty_type = "";
		String royalty_parameters ="";
		Map<String, String> sParaTemp = new HashMap<String, String>();
		
		sParaTemp.put("service", "refund_fastpay_by_p latform_nopwd");
		sParaTemp.put("partner", alipayMechInfo.accountId);
		sParaTemp.put("_input_charset", inputCharset);
		sParaTemp.put("sign_type", "MD5");
		String notifyUrl = pay.getNotifyUrl();
		if(StringUtils.isNotBlank(notifyUrl)){
			notifyUrl = notifyUrl.replaceAll("\\$\\{payMethodId\\}", String.valueOf(pay.getPayMethodId()));
			sParaTemp.put("notify_url", notifyUrl);
		}
		sParaTemp.put("batch_no", "MD5");
		
		
		
		
		
		
		sParaTemp.put("payment_type", "1");	//按照支付宝文档，必须为1
		sParaTemp.put("out_trade_no", pay.getTransactionId());
		sParaTemp.put("subject", subject);
		if(StringUtils.isNotBlank(body)){
			sParaTemp.put("body", body);
		}
		sParaTemp.put("total_fee", total_fee);
		sParaTemp.put("paymethod", paymethod);
		sParaTemp.put("defaultbank", defaultbank);
		sParaTemp.put("anti_phishing_key", anti_phishing_key);
		sParaTemp.put("exter_invoke_ip", exter_invoke_ip);
		sParaTemp.put("extra_common_param", extra_common_param);
		sParaTemp.put("buyer_email", buyer_email);
		sParaTemp.put("royalty_type", royalty_type);
		sParaTemp.put("royalty_parameters", royalty_parameters);

		String returnUrl = pay.getReturnUrl();
	
		if(StringUtils.isNotBlank(returnUrl)){
			returnUrl = returnUrl.replaceAll("\\$\\{payMethodId\\}", String.valueOf(pay.getPayMethodId()));
			sParaTemp.put("return_url", returnUrl);
		}
		
		sParaTemp.put("seller_email", alipayMechInfo.accountName);
		//是否使用适配iframe的简易模式
		int qrMode = pay.getFlag();
		sParaTemp.put("qr_pay_mode", String.valueOf(qrMode));

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

		String queryString = signString + "&sign_type=MD5";

		signString += alipayMechInfo.payKey; //把拼接后的字符串再与安全校验码直接连接起来
		String sign = DigestUtils.md5Hex(signString);
		logger.debug("根据源字符串[" + signString + "]生成签名:" + sign);

	/*	long currentTime = new java.util.Date().getTime();
		pay.setEndTime(new java.util.Date(currentTime - 100000000));

	*/	queryString += "&sign=" + sign;

		String url = payUrl + "?" + queryString;

		logger.debug("为支付生成跳转URL:" + url);

		
		pay.setCurrentStatus(TransactionStatus.inProcess.getId());
		if(qrMode == 2){
			logger.debug("当前模式为直接跳转模式，跳转到:" + url);
			return new EisMessage(Operate.jump.getId(), url);
		}
		logger.debug("当前模式为简易扫码模式，iframe url:" + url);
		return new EisMessage(Operate.iframe.getId(), url);	}


	



	/**
	 * 获取远程服务器ATN结果,验证返回URL
	 * @param notify_id 通知校验ID
	 * @return 服务器ATN结果
	 * 验证结果集：
	 * invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空 
	 * true 返回正确信息
	 * false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
	 */
	

	@Override
	public EisMessage onRefund(Pay pay) {
		Assert.notNull(pay,"尝试进行的支付对象不能为空");
		Assert.notNull(pay.getTransactionId(),"尝试开始的支付对象订单号不能为空");

		PayChannelMechInfo alipayMechInfo = channelService.getChannelInfo(pay);
		if(alipayMechInfo == null){
			logger.error("找不到ownerId=" + pay.getOwnerId() + "]的支付宝商户信息");			
			throw new RequiredObjectIsNullException("找不到必须的支付信息");
		}

		Refund refund = new Refund(pay.getOwnerId());
		refund.setTransactionId(globalOrderIdService.generate(TransactionType.refund.getId()));
		String inOrderId = new SimpleDateFormat("yyyyMMdd").format(new Date()) + uuidService.createById(pay.getOwnerId(), 0);
		refund.setInOrderId(inOrderId);
		Map<String, String> sParaTemp = new HashMap<String, String>();
		
		sParaTemp.put("service", "refund_fastpay_by_p latform_nopwd");
		sParaTemp.put("partner", alipayMechInfo.accountId);
		sParaTemp.put("_input_charset", inputCharset);
		
		String notifyUrl = pay.getNotifyUrl();
		
		if(StringUtils.isNotBlank(notifyUrl)){
			notifyUrl = notifyUrl.replaceAll("\\$\\{payMethodId\\}", String.valueOf(pay.getPayMethodId()));
			sParaTemp.put("notify_url", notifyUrl);
		}
		sParaTemp.put("batch_ no", refund.getInOrderId());
		sParaTemp.put("refund _date", new SimpleDateFormat(CommonStandard.defaultDateFormat).format(new Date()));
		sParaTemp.put("batch_ num", "1");//写死的退款笔数
		

		String data = pay.getOutOrderId() + "^" + pay.getRealMoney() + "^" + "协商退款";
		sParaTemp.put("detail_ data", data);
		

		
		
		
		
		

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

		String queryString = signString + "&sign_type=MD5";

		signString += alipayMechInfo.payKey; //把拼接后的字符串再与安全校验码直接连接起来
		String sign = DigestUtils.md5Hex(signString);
		logger.debug("根据源字符串[" + signString + "]生成签名:" + sign);

	/*	long currentTime = new java.util.Date().getTime();
		pay.setEndTime(new java.util.Date(currentTime - 100000000));
*/
		queryString += "&sign=" + sign;

		String url = refundUrl + "?" + queryString;

		logger.debug("为退款生成URL:" + url);
		String result = null;
		try {
			result = httpsService.httpsGet(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("退款请求结果:" + result);
		pay.setCurrentStatus(TransactionStatus.refunded.getId());
		
		return new EisMessage(OperateResult.success.getId(), url);
	}

	@Override
	public String getDesc() {
		return "支付宝标准版支付处理器" + this.getClass().getSimpleName();
	}






	/*	public static void main(String[] argv){
		Map<String,String> params = new HashMap<String,String>();
		AliPayProcess a = new AliPayProcess();
		String text = "body=mailimaili&buyer_email=yyuuuau@akkkf.com&buyer_id=2088802168017100&discount=0.00&gmt_create=2012-06-20 22:50:20&gmt_payment=2012-06-20 22:53:00&is_total_fee_adjust=N&notify_id=eada11b0c9cbafddf67ce5ebd85e07fb01&notify_time=2012-06-20 22:53:00&notify_type=trade_status_sync&out_trade_no=201206202243161340203396040&payment_type=1&price=1.37&quantity=1&seller_email=zfb@maicard.com&seller_id=2088501754554150&subject=maili&total_fee=1.37&trade_no=2012062020945010&trade_status=TRADE_SUCCESS&use_coupon=N";
		String sign = a.md5(text);
		System.out.println(sign);

		String resultString = null;
		try{
			System.out.println(java.net.URLDecoder.decode("RqPnCoPT3K9%2Fvwbh3I71k8EbHUHg%2Frf%2BgpHJPOWaq3EuQ6pSheZO9aG9%2B%2Bl60CNuIZE3","UTF-8"));
			resultString = java.net.URLDecoder.decode(text,"UTF-8");
			String[] param = resultString.split("&");

			System.out.println(a.md5(resultString));
			for(int i = 0 ; i < param.length; i++){
				//System.out.println(param[i]);
				String[] kv = param[i].split("=");
				params.put(kv[0], kv[1]);
			}

		}catch(Exception e){e.printStackTrace();}


	}*/


}
