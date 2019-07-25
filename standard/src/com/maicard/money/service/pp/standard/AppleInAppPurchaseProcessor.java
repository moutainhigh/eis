package com.maicard.money.service.pp.standard;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisMessage;
import com.maicard.exception.RequiredObjectIsNullException;
import com.maicard.money.domain.Pay;
import com.maicard.money.iface.PayProcessor;
import com.maicard.money.service.PayService;
import com.maicard.standard.EisError;
import com.maicard.standard.TransactionStandard.TransactionStatus;

/**
 * 
 * @author henry
 * ios内部支付   客户端只需要一个订单号   在付款完后没有异步通知   需要客户端返回一个字段
 * 然后服务拿这个字段去ios服务器验证  返回一个json
 *
 */
@Service
public class AppleInAppPurchaseProcessor extends BaseService implements PayProcessor{

	@Resource
	private PayService payService;
	@Override
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
		EisMessage message = new EisMessage();
		message.setContent(pay.getTransactionId());
		return message;
	}

	@Override
	public EisMessage onQuery(Pay pay) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EisMessage onRefund(Pay pay) {
		// TODO Auto-generated method stub
		return new EisMessage(-EisError.serviceUnavaiable.id,"接口尚未支持退款");
	}

	/**
	 *格式1
	 * {
	    "receipt": {
	        "original_purchase_date_pst": "2016-12-03 01:11:01 America/Los_Angeles", 
	        "purchase_date_ms": "1480756261254", 
	        "unique_identifier": "96f51b28f628493709966f33a1fe7ba", 
	        "original_transaction_id": "1000000255766", 
	        "bvrs": "82", 
	        "transaction_id": "1000000255766", 
	        "quantity": "1", 
	        "unique_vendor_identifier": "FE358-1362-40FD-870F-DF788AC5", 
	        "item_id": "11822945", 
	        "product_id": "rjkf_itemid_1", 
	        "purchase_date": "2016-12-03 09:11:01 Etc/GMT", 
	        "original_purchase_date": "2016-12-03 09:11:01 Etc/GMT", 
	        "purchase_date_pst": "2016-12-03 01:11:01 America/Los_Angeles", 
	        "bid": "com.xxx.xxx", 
	        "original_purchase_date_ms": "1480756261254"
	    }, 
	    "status": 0
		}
		
		格式2
		{
		    "status": 0, 
		    "environment": "Sandbox", 
		    "receipt": {
		        "receipt_type": "ProductionSandbox", 
		        "adam_id": 0, 
		        "app_item_id": 0, 
		        "bundle_id": "com.xxx.xxx", 
		        "application_version": "84", 
		        "download_id": 0, 
		        "version_external_identifier": 0, 
		        "receipt_creation_date": "2016-12-05 08:41:57 Etc/GMT", 
		        "receipt_creation_date_ms": "1480927317000", 
		        "receipt_creation_date_pst": "2016-12-05 00:41:57 America/Los_Angeles", 
		        "request_date": "2016-12-05 08:41:59 Etc/GMT", 
		        "request_date_ms": "1480927319441", 
		        "request_date_pst": "2016-12-05 00:41:59 America/Los_Angeles", 
		        "original_purchase_date": "2013-08-01 07:00:00 Etc/GMT", 
		        "original_purchase_date_ms": "1375340400000", 
		        "original_purchase_date_pst": "2013-08-01 00:00:00 America/Los_Angeles", 
		        "original_application_version": "1.0", 
		        "in_app": [
		            {
		                "quantity": "1", 
		                "product_id": "rjkf_itemid_1", 
		                "transaction_id": "10000003970", 
		                "original_transaction_id": "10000003970", 
		                "purchase_date": "2016-12-05 08:41:57 Etc/GMT", 
		                "purchase_date_ms": "1480927317000", 
		                "purchase_date_pst": "2016-12-05 00:41:57 America/Los_Angeles", 
		                "original_purchase_date": "2016-12-05 08:41:57 Etc/GMT", 
		                "original_purchase_date_ms": "1480927317000", 
		                "original_purchase_date_pst": "2016-12-05 00:41:57 America/Los_Angeles", 
		                "is_trial_period": "false"
		            }
		        ]
		    }
		}

	 */
	@Override
	public Pay onResult(String resultString) {
		logger.debug("解析ios返回结果:" + resultString);
		Pay pay = new Pay();
		pay.setCurrentStatus(EisError.UNKNOWN_ERROR.getId());
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode node = mapper.readTree(resultString);
			JsonNode statusNode = node.get("status");
			if (statusNode == null) {
				logger.error("解析字符串{}出错,未找到可用的字段status",resultString);
				return pay;
			}
			logger.debug("状态码为{}",statusNode.toString());
			if (statusNode.toString().equals("0")) {
				//成功   检查是在测试环境还是在真实环境
				JsonNode environment = node.get("environment");
				JsonNode receiptNode = node.get("receipt");
				if (receiptNode == null) {
					logger.error("解析字符串{}出错,未找到可用的字段receipt",resultString);
					return pay;
				}
				String transactionId = null;
				String outOrderId =null;
				if (environment == null) {
					//真实环境
					transactionId = replace(receiptNode.get("transaction_id").toString(), "\"");
				}else {
					//测试环境
					JsonNode inAppArr = receiptNode.get("in_app");
					String inApp = inAppArr.toString().substring(1, inAppArr.toString().length()-1);
					JsonNode inAppNode = mapper.readTree(inApp);
					transactionId = replace(inAppNode.get("transaction_id").toString(), "\"");
					
				}
				if (transactionId == null) {
					logger.debug("未获得到订单id");
					pay.setCurrentStatus(EisError.dataError.id);
					return pay;
				}
				pay = payService.select(transactionId);

				if(pay == null){
					logger.error("找不到指定的支付订单:" + transactionId);
					pay = new Pay();
					pay.setCurrentStatus(EisError.OBJECT_IS_NULL.id);
					return pay;
				}
				pay.setPayResultMessage("success");
				
				if(pay.getCurrentStatus() == TransactionStatus.success.getId() || pay.getCurrentStatus() == TransactionStatus.failed.id){
					return pay;

				}
				if(pay.getCurrentStatus() != TransactionStatus.inProcess.id){
					logger.error("指定的支付订单[" + transactionId + "]状态不是处理中，而是:" + pay.getCurrentStatus());
					return pay;
				}
				
				logger.info("支付订单[" + pay.getTransactionId() + "]交易成功");
				//float money = Float.parseFloat(params.get("total_fee"));
				//pay.setTransactionId(params.get("out_trade_no"));
				//pay.setOutOrderId(outOrderId);

				//pay.setRealMoney(money);
				pay.setCurrentStatus(TransactionStatus.success.getId());
				pay.setEndTime(new Date());
				return pay;
			}
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
		
		return null;
	}

	@Override
	public String getDesc() {
		// TODO Auto-generated method stub
		return "IOS应用内支付"+this.getClass().getSimpleName();
	}

	private String replace(String target,String needReplace){
		return target.replace(needReplace, "");
	}
}
