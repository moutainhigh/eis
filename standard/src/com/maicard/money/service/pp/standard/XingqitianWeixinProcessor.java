package com.maicard.money.service.pp.standard;

import java.util.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ConfigService;
import com.maicard.common.util.HttpUtils;
import com.maicard.common.util.NumericUtils;
import com.maicard.exception.RequiredObjectIsNullException;
import com.maicard.money.domain.Pay;
import com.maicard.money.domain.PayChannelMechInfo;
import com.maicard.money.iface.PayProcessor;
import com.maicard.money.service.ChannelService;
import com.maicard.money.service.PayMethodService;
import com.maicard.money.service.PayService;
import com.maicard.standard.ContextType;
import com.maicard.standard.EisError;
import com.maicard.standard.Operate;
import com.maicard.standard.TransactionStandard.TransactionStatus;

/**
 * 星启天的微信支付接口
 * 
 * 
 * @author NetSnake
 * @date 2015-03-04
 */
@Service
public class XingqitianWeixinProcessor extends BaseService implements PayProcessor {

	@Resource
	private ConfigService configService;
	@Resource
	private PayService payService;
	
	@Resource
	private ChannelService channelService;



	@PostConstruct
	public void init(){


	}




	public EisMessage onPay(Pay pay){
		
		


		if(pay == null ){
			throw new RequiredObjectIsNullException("支付对象为空");
		}
		if(pay.getTransactionId() == null){
			throw new RequiredObjectIsNullException("支付对象的订单号为空");
		}

		PayChannelMechInfo payMechInfo = channelService.getChannelInfo(pay);
		if(payMechInfo == null){
			logger.error("找不到ownerId=" + pay.getOwnerId() + "]的星启天支付商户信息");			
			throw new RequiredObjectIsNullException("找不到必须的支付信息");
		}

		//long currentTime = new java.util.Date().getTime();
		pay.setStartTime(new java.util.Date());
		//pay.setEndTime(new java.util.Date(currentTime - 100000000));

		String notifyUrl = null;
		String returnUrl = null;

		if(pay.getNotifyUrl() != null){
			notifyUrl = pay.getNotifyUrl().replaceAll("\\$\\{payMethodId\\}", String.valueOf(pay.getPayMethodId()));
		} else {
			logger.error("准备创建的支付[" + pay.getTransactionId() + "]没有payNotifyUrl");
			return new EisMessage(EisError.REQUIRED_PARAMETER.getId(),"支付对象数据错误[payNotifyUrl]");
		}
		if(pay.getReturnUrl() != null){
			returnUrl = pay.getReturnUrl().replaceAll("\\$\\{payMethodId\\}", String.valueOf(pay.getPayMethodId()));
		} else {
			returnUrl = notifyUrl;
		}

		String orderAmount= String.valueOf(pay.getFaceMoney()* 100).replaceAll("\\.0+", "");

		String payType="32"; //32微信 36QQ
		//String fullAmountFlag="0";



		//String bossType="0";

		String partnerId = payMechInfo.accountId;
		String key = payMechInfo.payKey;
		String szxCheckMd5 = "customerid=" + partnerId + "&sdcustomno=" + pay.getTransactionId() + "&orderAmount=" + orderAmount + "&cardno=" + payType + "&noticeurl=" + notifyUrl + "&backurl=" + returnUrl +key;
		szxCheckMd5 = DigestUtils.md5Hex(szxCheckMd5).toUpperCase();
		String remarks = null;
		try{
			remarks = java.net.URLEncoder.encode(pay.getName(),"GB2312");
		}catch(Exception e){
			e.printStackTrace();
		}
		String requestData = "customerid=" + partnerId + "&sdcustomno=" + pay.getTransactionId() + "&orderAmount=" + orderAmount + "&cardno=" + payType + "&noticeurl=" + notifyUrl + "&backurl=" + returnUrl + "&remarks=" + remarks + "&mark=" + pay.getTransactionId() + "&sign=" + szxCheckMd5;

		pay.setCurrentStatus(TransactionStatus.inProcess.getId());
		String url = payMechInfo.submitUrl + "?" + requestData;
		logger.debug("当前支付环境是:" + pay.getContextType());
		if(pay.getContextType() != null && (pay.getContextType().equals(ContextType.WAP.toString()) || pay.getContextType().equals(ContextType.WEIXIN.toString()))){
			String page = HttpUtils.sendData(url);
			if(page == null){
				logger.error("无法访问星启天的支付地址:" + url);
				return new EisMessage(EisError.systemBusy.id,"无法进行支付");
			}
			String code = "<div class=\"ewm\"><img src=\"";
			int offset = page.indexOf(code);
			if(offset < 0){
				logger.error("无法解析星启天的支付地址:" + url + "]所返回的页面:" + page);
				return new EisMessage(EisError.systemBusy.id,"无法进行支付");
				
			}
			String qrCodeUrl = page.substring(offset + code.length());
			offset = qrCodeUrl.indexOf("\"></div>");
			if(offset < 0){
				logger.error("无法解析星启天的支付地址:" + url + "]所返回的页面:" + page);
				return new EisMessage(EisError.systemBusy.id,"无法进行支付");
			}
			qrCodeUrl = qrCodeUrl.substring(0, offset);
			logger.debug("当前环境手机端，直接返回二维码:" + qrCodeUrl);
			return new EisMessage(Operate.iframe.getId(), qrCodeUrl);
		}
		String html = "<html><body><script>window.location.href=\"" + url + "\";</script></body></html>";
		logger.info("为支付订单[" + pay.getTransactionId() + "]生成跳转HTML:" + html);
		EisMessage message = new EisMessage(Operate.jump.getId(), html);

		return message;
	}

	public Pay onResult(String resultString){
		String successResponse = "<result>1</result>";
		logger.info("得到返回字符串:" + resultString);
		Pay pay = new Pay();
		pay.setCurrentStatus(EisError.UNKNOWN_ERROR.getId());
		

		String[] results = resultString.split("&");
		if(results == null || results.length < 10){
			pay.setCurrentStatus(TransactionStatus.failed.getId());
			pay.setPayResultMessage(successResponse);
			return pay;
		}
		HashMap<String, String> resultMap = new HashMap<String,String>();
		for(String value : results){
			String[] result2 = value.split("=");
			if(result2 != null && result2.length == 2){
				if(result2[1] != null && !result2[1].equals("")){
					//logger.info("添加" + result2[0] + "=" + result2[1]);
					resultMap.put(result2[0], result2[1]);
				}
			}
		}



		String sign = resultMap.get("sign");

		if(sign == null || sign.equals("")){
			logger.error("找不到订单签名");
			pay.setCurrentStatus(TransactionStatus.failed.getId());
			return pay;
		}

		String signSource = "merchantAcctId=" + resultMap.get("merchantAcctId") + "&version=" + resultMap.get("version") + "&language=" + resultMap.get("language") + "&signType=" + resultMap.get("signType") + "&payType=" + resultMap.get("payType") + "&bankId=" + resultMap.get("bankId") + "&orderId=" + resultMap.get("orderId") + "&orderTime=" + resultMap.get("orderTime") + "&orderAmount=" + resultMap.get("orderAmount") + "&dealId=" + resultMap.get("dealId") + "&bankDealId=" + resultMap.get("bankDealId") + "&dealTime=" + resultMap.get("dealTime") + "&payAmount=" + resultMap.get("payAmount") + "&fee=" + resultMap.get("fee") + "&payResult=" + resultMap.get("payResult");
		if(resultMap.get("errCode") != null && !resultMap.get("errCode").equals("")){
			signSource += "&errCode=" + resultMap.get("errCode");
		}
		//logger.info("signSource=" + signSource);
		
		
		/*if(!sign.equals(mySign)){			
			pay.setCurrentStatus(TransactionStatus.failed.getId());
			pay.setPayResultMessage(successReceiveMessage);
			return pay;
		}*/

		logger.info("开始支付后处理");
		String transactionId = resultMap.get("sdcustomno");
		pay.setTransactionId(transactionId);
		pay = payService.select(transactionId);

		if(pay == null){
			logger.error("找不到指定的支付订单:" + transactionId);
			pay = new Pay();
			pay.setCurrentStatus(EisError.OBJECT_IS_NULL.id);
			return pay;
		}
		
		PayChannelMechInfo payMechInfo =  channelService.getChannelInfo(pay);
		if(payMechInfo == null){
			logger.error("找不到ownerId=" + pay.getOwnerId() + "]的畅联商户信息");			
			throw new RequiredObjectIsNullException("找不到必须的支付信息");
		}
		signSource += "&key=" + payMechInfo.payKey;
		String mySign = DigestUtils.md5Hex(signSource).toUpperCase();
		pay.setPayResultMessage(successResponse);
		if(pay.getCurrentStatus() == TransactionStatus.success.getId() || pay.getCurrentStatus() == TransactionStatus.failed.id){
			return pay;
		}
		if(pay.getCurrentStatus() != TransactionStatus.inProcess.id){
			logger.error("指定的支付订单[" + transactionId + "]状态不是处理中，而是:" + pay.getCurrentStatus());
			return pay;
		}

		if(resultMap.get("state").equals("1")){//交易成功
			logger.info("交易成功");
			String outOrderId = resultMap.get("sd51no");
			double money = Float.parseFloat(resultMap.get("ordermoney"));

			pay.setOutOrderId(outOrderId);
			float rate = 0f;
			if(pay.getRate() > 1){
				rate = pay.getRate() / 100;
			} else if(pay.getRate() <= 1){
				rate = pay.getRate();
			} else {
				logger.error("异常的费率:" + pay.getRate());
			}
			double realValue = NumericUtils.round(money * rate);
			logger.info("交易成功金额:" + money + ",乘以费率[" + rate + "]后为:" + money * rate + ", 处理后的金额是:" + realValue);
			pay.setRealMoney((float)realValue);
			pay.setCurrentStatus(TransactionStatus.success.getId());
			pay.setEndTime(new Date());
			pay.setPayResultMessage(successResponse);
			return pay;

		}  else {
			logger.info("对方返回交易失败");
			pay.setRealMoney(0);
			pay.setCurrentStatus(TransactionStatus.failed.getId());
			pay.setEndTime(new Date());
			pay.setPayResultMessage(successResponse);
			return pay;
		}

	}

	public EisMessage onQuery(Pay pay){
		return null;
	}






	public static void main(String[] argv){
		XingqitianWeixinProcessor k = new XingqitianWeixinProcessor();
		k.onResult("payAmount=100&dealTime=20130106200029&signType=1&merchantAcctId=1002240103101&errCode=&orderTime=20130106200023&dealId=861559103&version=v2.0&bankId=CMB&fee=1&bankDealId=8750833663&ext1=&payResult=10&orderAmount=100&ext2=&signMsg=FF75FFFA8F7FCA6C17A7BF3E4BE6E8CE&payType=10&language=1&orderId=201301062000231357473623532");



	}




	@Override
	public EisMessage onRefund(Pay pay) {
		// TODO Auto-generated method stub
		return null;
	}




	@Override
	public String getDesc() {
		return "星启天支付处理器" + this.getClass().getSimpleName();
	}

	/*private PayChannelMechInfo getMechInfo(long ownerId){
		PayChannelMechInfo weixinMechInfo = null;
		if(mechCache != null && mechCache.size() > 0){
			weixinMechInfo = mechCache.get(String.valueOf(ownerId));
			if(weixinMechInfo != null){
				logger.debug("从缓存中获取到ownerId=" + ownerId + "的星启天支付商户信息:" + weixinMechInfo);
				return weixinMechInfo;
			}
		}
		weixinMechInfo = new PayChannelMechInfo();
		String key = configService.getValue("XQT_PAY_KEY", ownerId);
		if(key != null){
			weixinMechInfo.payKey = key;
		}
		
		String weixinPayMechId = configService.getValue("XQT_PAY_ID", ownerId);
		if(weixinPayMechId != null){
			weixinMechInfo.accountId = weixinPayMechId;
		}
		String submitUrl = configService.getValue("XQT_SUBMIT_URL", ownerId);
		if(submitUrl != null){
			weixinMechInfo.submitUrl = submitUrl;
		}
		synchronized(this){
			mechCache.put(String.valueOf(ownerId), weixinMechInfo);
		}
		logger.debug("把ownerId=" + ownerId + "的星启天支付商户信息:" + weixinMechInfo + "放入缓存");
		return weixinMechInfo;
	}*/

}
