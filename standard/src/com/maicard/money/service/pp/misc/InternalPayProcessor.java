package com.maicard.money.service.pp.misc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ConfigService;
import com.maicard.common.util.HttpUtils;
import com.maicard.exception.EisException;
import com.maicard.money.domain.Pay;
import com.maicard.money.iface.PayProcessor;
import com.maicard.money.service.MoneyService;
import com.maicard.money.service.PayService;
import com.maicard.security.domain.User;
import com.maicard.security.service.FrontUserService;
import com.maicard.security.service.PartnerService;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.OperateResult;
import com.maicard.standard.SecurityStandard.UserStatus;
import com.maicard.standard.TransactionStandard.TransactionStatus;

/**
 * 内部系统之间的支付处理器
 * 
 * @author NetSnake
 * @date 2018-12-07
 */
@Service
public class InternalPayProcessor extends BaseService implements PayProcessor {

	@Resource
	private ConfigService configService;
	
	@Resource
	private PayService payService;	
	@Resource
	private FrontUserService frontUserService;
	@Resource
	private MoneyService moneyService;
	
	@Resource
	private PartnerService partnerService;
	
	/**
	 * 内部系统间支付调用时使用的parnter uuid
	 */
	public static long INTERNAL_PAY_PARTNER_ID = 1;

	/**
	 * 内部系统间支付调用时使用的支付类型ID
	 */
	public static int INTERNAL_PAY_TYPE_ID = 999;
	
	private final List<String> signFields = Arrays.asList("orderId","requestMoney","result","successMoney","timestamp","transactionId");
	
	@PostConstruct
	public void init() {
		INTERNAL_PAY_PARTNER_ID = configService.getLongValue(DataName.INTERNAL_PAY_PARTNER_ID.name(),0);
		if(INTERNAL_PAY_PARTNER_ID <= 0) {
			throw new EisException("系统没有定义必须的参数:" + DataName.INTERNAL_PAY_PARTNER_ID.name());
		}
		INTERNAL_PAY_TYPE_ID = configService.getIntValue(DataName.INTERNAL_PAY_TYPE_ID.name(), 0);
		if(INTERNAL_PAY_PARTNER_ID <= 0) {
			throw new EisException("系统没有定义必须的参数:" + DataName.INTERNAL_PAY_TYPE_ID.name());
		}
		
	}

	@Override
	public EisMessage onPay(Pay pay){
		pay.setCurrentStatus(TransactionStatus.inProcess.id);
		return new EisMessage(OperateResult.success.id,"订单创建成功");


	}

	@Override
	public EisMessage onQuery(Pay pay) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pay onResult(String resultString) {
		
		Pay pay = new Pay();
		Map<String,String> params = HttpUtils.getRequestDataMap(resultString);
		
		User partner = partnerService.select(INTERNAL_PAY_PARTNER_ID);
		if(partner == null){
			logger.error("根据UUID[" + pay.getPayFromAccount() + "]找不到合作伙伴");
			return null;
		} 

		if(partner.getCurrentStatus() != UserStatus.normal.getId()){
			logger.error("用户[" + pay.getPayFromAccount() + "]状态异常[" + partner.getCurrentStatus() + "]");
			return null;
		}

		String loginKey = partner.getExtraValue(DataName.supplierLoginKey.toString());
		if(StringUtils.isBlank(loginKey)){
			logger.error("用户[" + partner.getUuid() + "]配置中没有supplierLoginKey:{}", JSON.toJSONString(partner));
			return null;
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
				if(signFields.contains(key)) {
					sb.append(key);
					sb.append('=');
					sb.append(java.net.URLDecoder.decode(value,"UTF-8"));
					sb.append('&');		
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		String signString = sb.toString().replaceAll("&$", "");		


		signString += "&key=";
		signString += loginKey; //把拼接后的字符串再与安全校验码直接连接起来

		String mySign = DigestUtils.md5Hex(signString);
		logger.debug("对方返回的sign=" + remoteSign + ",我方对源[" + signString + "]校验结果是:" + mySign);

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
		// TODO Auto-generated method stub
		return "账户内资金充值处理器" + this.getClass().getSimpleName();
	}
	

}
