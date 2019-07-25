package com.maicard.money.service.pp.misc;

import java.text.DecimalFormat;
import java.util.*;

import javax.annotation.Resource;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
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
import com.maicard.money.iface.PayProcessor;
import com.maicard.money.service.PayService;
import com.maicard.standard.EisError;
import com.maicard.standard.Operate;
import com.maicard.standard.TransactionStandard.TransactionStatus;

/**
 * 支付宝原生APP SDK接口<br>
 * 用于自有游戏内调用支付宝支付<br>
 * <br><br>
 * 对PayChannelMechInfo中的字段定义<br>
 * 我方appId: accountId<br>
 * 我方私钥:cryptKey<br>
 * 支付宝方公钥:payKey<br>
 * 
 *
 *
 * @author NetSnake
 * @date 2017年3月3日
 *
 */
@Service
public class AliPaySdkProcessor extends BaseService implements PayProcessor {

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

	final String successResponse = "success";



	private static Map<String, PayChannelMechInfo> mechCache = new HashMap<String, PayChannelMechInfo>();
	
	final String CHARSET = "UTF-8";
	
	DecimalFormat df = new DecimalFormat("0.00");


	public EisMessage onPay(Pay pay){

		Assert.notNull(pay,"尝试进行的支付对象不能为空");
		Assert.notNull(pay.getTransactionId(),"尝试开始的支付对象订单号不能为空");
		Assert.notNull(pay.getName(),"尝试开始的支付对象name不能为空");


		PayChannelMechInfo alipayMechInfo = getMechInfo(pay.getExtraValue("appCode"), pay.getOwnerId());
		if(alipayMechInfo == null){
			logger.error("找不到ownerId=" + pay.getOwnerId() + "]的支付宝商户信息");			
			throw new RequiredObjectIsNullException("找不到必须的支付信息");
		}
		
		String notifyUrl = pay.getNotifyUrl();

		if(StringUtils.isNotBlank(notifyUrl)){
			notifyUrl = notifyUrl.replaceAll("\\$\\{payMethodId\\}", String.valueOf(pay.getPayMethodId()));
		}

		
		//实例化客户端
		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", alipayMechInfo.accountId, alipayMechInfo.cryptKey, "json", CHARSET, alipayMechInfo.peerPublicKey, "RSA2");
		//实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
		AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
		//SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
		AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
		model.setBody(pay.getDescription());
		model.setSubject(pay.getName());
		model.setOutTradeNo(pay.getTransactionId());
		model.setTimeoutExpress("30m");
		model.setTotalAmount(df.format(pay.getFaceMoney()));
		model.setProductCode("QUICK_MSECURITY_PAY");
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
        AlipayTradeAppPayResponse response = null;

		try {
		        //这里和普通的接口调用不同，使用的是sdkExecute
		        response = alipayClient.sdkExecute(request);
		       // System.out.println(response.getBody());//就是orderString 可以直接给客户端请求，无需再做处理。
		    } catch (AlipayApiException e) {
		        e.printStackTrace();
		}

		if(response == null){
			logger.error("支付宝SDK返回空");
			return new EisMessage(EisError.systemBusy.id,"无法发起支付");
		}
		String outOrderId = response.getBody();
		logger.debug("支付宝SDK返回:" + response + ",返回body=" + outOrderId);
		pay.setOutOrderId(outOrderId);
		pay.setCurrentStatus(TransactionStatus.inProcess.getId());
		return new EisMessage(Operate.jump.getId(), outOrderId);

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
		
		PayChannelMechInfo alipayMechInfo = getMechInfo(pay.getExtraValue("appCode"), pay.getOwnerId());
		if(alipayMechInfo == null){
			logger.error("找不到ownerId=" + pay.getOwnerId() + "]的支付宝商户信息");			
			throw new RequiredObjectIsNullException("找不到必须的支付信息");
		}
		

		boolean signOk = false;
		try {
			signOk = AlipaySignature.rsaCheckV1(params, alipayMechInfo.payKey, CHARSET, "RSA2");
		} catch (AlipayApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!signOk){
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

	

		if(signOk){//确认校验无误

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
		return null;
		}


	private PayChannelMechInfo getMechInfo(String appCode, long ownerId) {
		appCode = null;
		String key = null;
		String configPrefix = null;
		if(appCode == null){
			key = String.valueOf(ownerId);
			configPrefix = "";
		} else {
			key = ownerId + "#" + appCode;
			configPrefix = appCode + "_";
		}
		configPrefix = configPrefix.toUpperCase();
		PayChannelMechInfo alipayMechInfo = null;
		if(mechCache != null && mechCache.size() > 0){
			alipayMechInfo = mechCache.get(key);
		}
		if(alipayMechInfo == null){		
			alipayMechInfo = new PayChannelMechInfo();
			String accountId = configService.getValue(configPrefix + "ALIPAY_SDK_ACCOUNT_ID", ownerId);
			if(accountId != null){
				alipayMechInfo.accountId = accountId;
			} else {
				logger.error("找不到ownerId=" + ownerId + "的支付宝信息:" + configPrefix + "ALIPAY_SDK_ACCOUNT_ID");
				return null;
			}

			String privateKey = configService.getValue(configPrefix + "ALIPAY_SDK_PRIVATE_KEY", ownerId);
			if(privateKey != null){
				alipayMechInfo.cryptKey = privateKey;
			} else {
				logger.error("找不到ownerId=" + ownerId + "的支付宝信息:" + configPrefix + "ALIPAY_SDK_PRIVATE_KEY");
				return null;
			}
			String publicKey = configService.getValue(configPrefix + "ALIPAY_SDK_PUBLIC_KEY", ownerId);
			if(publicKey != null){
				alipayMechInfo.payKey = publicKey;
			} else {
				logger.error("找不到ownerId=" + ownerId + "的支付宝信息:" + configPrefix + "ALIPAY_SDK_PUBLIC_KEY");
				return null;
			}		
			String aliPublicKey = configService.getValue(configPrefix + "ALIPAY_ALI_PUBLIC_KEY", ownerId);
			if(aliPublicKey != null){
				alipayMechInfo.peerPublicKey= aliPublicKey;
			} else {
				logger.error("找不到ownerId=" + ownerId + "的支付宝信息:" + configPrefix + "ALIPAY_ALI_PUBLIC_KEY");
				return null;
			}		
			synchronized(this){
				mechCache.put(key, alipayMechInfo);
			}
			logger.debug("把[" + key + "]支付宝商户信息:" + alipayMechInfo + "放入缓存");


		} else {
			logger.debug("从缓存中获取到[" + key  + "]的支付宝商户信息:" + alipayMechInfo);
		}	

		return alipayMechInfo;
	}




	@Override
	public EisMessage onRefund(Pay pay) {
		return null;
	}

	@Override
	public String getDesc() {
		return "支付宝SDK支付处理器" + this.getClass().getSimpleName();
	}



}
