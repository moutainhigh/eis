package com.maicard.product.service.impl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.lang.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.EisJob;
import com.maicard.common.util.Crypt;
import com.maicard.common.util.HttpUtils;
import com.maicard.mb.service.MessageService;
import com.maicard.money.criteria.WithdrawCriteria;
import com.maicard.money.dao.WithdrawDao;
import com.maicard.money.domain.Pay;
import com.maicard.money.domain.Withdraw;
import com.maicard.money.service.PayService;
import com.maicard.product.criteria.FailedNotifyCriteria;
import com.maicard.product.domain.FailedNotify;
import com.maicard.product.domain.Item;
import com.maicard.product.service.FailedNotifyService;
import com.maicard.product.service.ItemService;
import com.maicard.product.service.NotifyProcessor;
import com.maicard.product.service.NotifyService;
import com.maicard.security.domain.User;
import com.maicard.security.service.PartnerService;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.TransactionStandard.TransactionStatus;


/**
 * 为支付业务提供合作伙伴异步通知服务
 * 同时检查在指定时间内未成功发送的通知按照策略重发
 * 
 *
 * @author NetSnake
 * @date 2013-8-18 
 */
@Service
public class NotifyServiceImpl extends BaseService implements NotifyService,EisJob   {

	@Resource
	private ApplicationContextService applicationContextService;
	@Resource
	private ConfigService configService;
	@Resource
	private ItemService itemService;
	@Resource
	private MessageService messageService;
	@Resource
	private FailedNotifyService failedNotifyService;
	@Resource
	private PartnerService partnerService;
	@Resource
	private PayService payService;
	
	

	//由于withdrawService也调用了notifyService，所以不能再反向去导入withdrawService，而只能导入withdrawDao
	@Resource
	private WithdrawDao withdrawDao;


	private int maxNotifyCount; //发送异步通知的最大次数

	private int initSendInterval; //初始化发送间隔

	private int maxNotifyMinute;	//发送异步通知的最长时间，早于这个时间的通知，不管是否送达，都不会重新发送
	
	

	final SimpleDateFormat sdf = new SimpleDateFormat(CommonStandard.defaultDateFormat);
	final DecimalFormat df = new DecimalFormat("0.00");

	private boolean handlerPayNotify = false;
	private boolean handlerJmsDataSyncToLocal;

	private int notifySendRetry = 5;		//默认重发次数
	private int notifySendRetryInterval = 10;

	//private final int sleepSecBeforeSend = 5;

	@PostConstruct
	public void init(){
		maxNotifyCount = configService.getIntValue(DataName.maxNotifyCount.toString(),0);
		if(maxNotifyCount == 0){
			maxNotifyCount = 30; //默认最多发送通知次数30
		}
		maxNotifyMinute = configService.getIntValue(DataName.maxNotifyMinute.toString(),0);
		if(maxNotifyMinute == 0){
			maxNotifyMinute = 60; //默认发送时间1小时
		}
		initSendInterval = configService.getIntValue(DataName.initNotifySendInterval.toString(),0);
		if(initSendInterval == 0){
			initSendInterval = 60; //默认发送间隔60秒
		}
		handlerPayNotify = configService.getBooleanValue(DataName.handlerPayNotify.toString(),0);
		handlerJmsDataSyncToLocal = configService.getBooleanValue(DataName.handlerJmsDataSyncToLocal.toString(),0);
	}


	@Override
	@Async
	public void sendNotify(Item item){
		if(item == null){
			logger.warn("尝试发送异步通知的Item为空");
			return;
		}
		int sleep = 1;
		/*if(item.getTransactionTypeId() == TransactionType.buy.getId()){
			sleep = sleepSecBeforeSend * 6; 
		} else {
			sleep = sleepSecBeforeSend;
		}*/
		logger.info("交易[" + item.getTransactionId() + "]进入异步通知发送新线程，睡眠" + sleep + "秒后发送通知.");
		try {
			Thread.sleep(sleep * 1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		if(!handlerJmsDataSyncToLocal && !handlerPayNotify){
			logger.info("本节点不负责发送交易通知，也不负责同步Jms数据，忽略异步通知请求");
			return;
		}

		if(item.getItemDataMap() == null || item.getItemDataMap().size() < 1){
			FailedNotify failedNotify = new FailedNotify(item.getTransactionId());
			failedNotifyService.replace(failedNotify);
			logger.warn("尝试发送异步通知的Item[" + item.getTransactionId() + "]扩展数据为空");
			return;
		}
		if(item.getCurrentStatus() == TransactionStatus.success.getId()){
			item.setRequestMoney(0f);
		}

		boolean isLegacyProduct = false;
		if(item.getSyncFlag() == 0){
			boolean noDistributedNotify = false;
			try{
				noDistributedNotify = Boolean.parseBoolean(item.getItemDataMap().get(DataName.noDistributedNotify.toString()).getDataValue());
			}catch(Exception e){}
			if(noDistributedNotify){
				logger.info("异步通知[" + item.getTransactionId() + "]不需要进行分布式异步通知");				
			}	else {
				logger.info("异步通知[" + item.getTransactionId() + "]需要进行分布式异步通知");
				messageService.sendJmsDataSyncMessage(null, "notifyService", "sendNotify", item);
			}
		} else {
			logger.info("交易[" + item.getTransactionId() + "]的同步标志为1，无需通知其他节点发送异步通知");
		}
		try{
			isLegacyProduct = Boolean.parseBoolean(item.getItemDataMap().get(DataName.legacyProduct.toString()).getDataValue());
		}catch(Exception e){}
		String customNotifyProcessor = null;
		try{
			customNotifyProcessor = item.getItemDataMap().get(DataName.notifyProcessor.toString()).getDataValue();
		}catch(Exception e){

		}
		NotifyProcessor notifyProcessor = null;
		if(StringUtils.isNotBlank(customNotifyProcessor)){
			try{
				notifyProcessor = (NotifyProcessor)applicationContextService.getBean(customNotifyProcessor);
			}catch(Exception e){
				logger.error("交易[" + item.getTransactionId() + "]定义了自定义异步通知器[" + customNotifyProcessor + "]，但无法找到该服务");
			}
		}
		String notifyUrl = null;
		try{
			notifyUrl = item.getItemDataMap().get(DataName.payNotifyUrl.toString()).getDataValue();
		}catch(Exception e){}
		if(notifyUrl == null && notifyProcessor == null){
			logger.warn("找不到Item[" + item.getTransactionId() + "]的payNotifyUrl数据，同时也没有自定义通知处理器");
			return;
		}
		int customNotifySendRetry = 0;
		try{
			customNotifySendRetry = Integer.parseInt(item.getItemDataMap().get(DataName.maxNotifyCount.toString()).getDataValue());
		}catch(Exception e){}
		if(customNotifySendRetry <= 0){
			customNotifySendRetry = notifySendRetry;
		}
		int customNotifySendRetryInterval = 0;
		try{
			customNotifySendRetryInterval = Integer.parseInt(item.getItemDataMap().get(DataName.notifySendRetryInterval.toString()).getDataValue());
		}catch(Exception e){}
		if(customNotifySendRetryInterval <= 0){
			customNotifySendRetryInterval = notifySendRetryInterval;
		}

		boolean sendSuccess = false;
		int sendCount = 0;
		for(int i = 0; i < customNotifySendRetry; i++){		
			sendCount = i+1;
			if(logger.isDebugEnabled()){
				logger.debug("第" + (i+1) + "次为交易[" + item.getTransactionId() + "]发送异步通知，老接口规范:" + isLegacyProduct + "，共尝试" + notifySendRetry + "次");
			}
			String result = null;
			if(notifyProcessor != null ){
				logger.info("交易[" + item.getTransactionId() + "]定义了自定义异步通知器[" + customNotifyProcessor + "]，使用该服务发送异步通知");
				result = notifyProcessor.sendNotify(item);
			} else {
				if(isLegacyProduct){
					result = sendNotifyLegacy(item);
				} else {
					result = sendNotifyCurrent(item);
				}
			}
			logger.debug("第" + (i+1) + "次为交易[" + item.getTransactionId() + "]发送异步通知，老接口规范:" + isLegacyProduct + "，结果:" + result);
			if(isValidResponse(item, result)){
				sendSuccess = true;
				break;
			}			
			try {
				Thread.sleep(customNotifySendRetryInterval * 1000 * (i+1));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	

		}
		if(!sendSuccess){
			FailedNotify failedNotify = new FailedNotify(item.getTransactionId());
			failedNotifyService.replace(failedNotify);
			logger.info("交易[" + item.getTransactionId() + "]在尝试" + sendCount + "次后仍然失败，记录为失败通知");
		} else {
			failedNotifyService.delete(item.getTransactionId());
			logger.info("交易[" + item.getTransactionId() + "]在尝试" + sendCount + "次后发送成功，从失败通知中去除");
		}
		return;

	}

	@Override
	public String syncSendNotify(Item item){
		if(item == null){
			logger.warn("尝试发送异步通知的Item为空");
			return null;
		}
		if(item.getItemDataMap() == null || item.getItemDataMap().size() < 1){
			logger.warn("尝试发送异步通知的Item[" + item.getTransactionId() + "]扩展数据为空");
			return null;
		}
		if(item.getCurrentStatus() == TransactionStatus.success.getId()){
			item.setRequestMoney(0f);
		}
		if(item.getSyncFlag() == 0){
			boolean noDistributedNotify = false;
			try{
				noDistributedNotify = Boolean.parseBoolean(item.getItemDataMap().get(DataName.noDistributedNotify.toString()).getDataValue());
			}catch(Exception e){}
			if(noDistributedNotify){
				logger.info("异步通知[" + item.getTransactionId() + "]不需要进行分布式异步通知");				
			}	else {
				logger.info("异步通知[" + item.getTransactionId() + "]需要进行分布式异步通知");
				messageService.sendJmsDataSyncMessage(null, "notifyService", "sendNotify", item);
			}
		} else {
			logger.info("交易[" + item.getTransactionId() + "]的同步标志为1，无需进行异步通知");
		}
		boolean isLegacyProduct = false;
		try{
			isLegacyProduct = Boolean.parseBoolean(item.getItemDataMap().get(DataName.legacyProduct.toString()).getDataValue());
		}catch(Exception e){}
		String result = null;
		String customNotifyProcessor = null;
		try{
			customNotifyProcessor = item.getItemDataMap().get(DataName.notifyProcessor.toString()).getDataValue();
		}catch(Exception e){

		}
		NotifyProcessor notifyProcessor = null;
		if(StringUtils.isNotBlank(customNotifyProcessor)){
			try{
				notifyProcessor = (NotifyProcessor)applicationContextService.getBean(customNotifyProcessor);
			}catch(Exception e){
				logger.error("交易[" + item.getTransactionId() + "]定义了自定义异步通知器[" + customNotifyProcessor + "]，但无法找到该服务");
			}
		}
		if(notifyProcessor != null ){
			logger.info("交易[" + item.getTransactionId() + "]定义了自定义异步通知器[" + customNotifyProcessor + "]，使用该服务发送异步通知");
			result = notifyProcessor.sendNotify(item);
		} else {
			String notifyUrl = null;
			try{
				notifyUrl = item.getItemDataMap().get(DataName.payNotifyUrl.toString()).getDataValue();
			}catch(Exception e){}
			if(notifyUrl == null){
				logger.warn("找不到Item[" + item.getTransactionId() + "]的payNotifyUrl数据");
				return null;
			}
			if(isLegacyProduct){
				result = sendNotifyLegacy(item);
			} else {
				result = sendNotifyCurrent(item);
			}
		}
		if(isValidResponse(item, result)){
			failedNotifyService.delete(item.getTransactionId());
			logger.info("交易[" + item.getTransactionId() + "]直接发送成功，从失败通知中去除");
		} else {
			FailedNotify failedNotify = new FailedNotify(item.getTransactionId());
			failedNotifyService.replace(failedNotify);
			logger.info("交易[" + item.getTransactionId() + "]直接发送失败，记录为失败通知");
		}
		return result;
	}


	private String sendNotifyCurrent(Item item){
		if(item == null){
			logger.warn("尝试发送异步通知的Item为空");
			return "尝试发送异步通知的Item为空";
		}
		if(item.getItemDataMap() == null || item.getItemDataMap().size() < 1){
			logger.warn("尝试发送异步通知的Item[" + item.getTransactionId() + "]扩展数据为空");
			return "尝试发送异步通知的Item[" + item.getTransactionId() + "]扩展数据为空";
		}
		String notifyUrl = item.getExtraValue(DataName.payNotifyUrl.toString());
		if(notifyUrl == null){
			logger.warn("找不到交易[" + item.getTransactionId() + "]的payNotifyUrl数据");
			return "找不到交易[" + item.getTransactionId() + "]的payNotifyUrl数据";
		}
		String timestamp = String.valueOf(new Date().getTime() / 1000);

		NameValuePair[] requestData = { 
				new NameValuePair("transactionId", item.getTransactionId()),
				new NameValuePair("orderId", item.getInOrderId()),
				new NameValuePair("requestMoney ", String.valueOf(item.getLabelMoney() * item.getCount())),
				new NameValuePair("successMoney", String.valueOf(item.getSuccessMoney())),
				new NameValuePair("operateCode", String.valueOf(item.getOutStatus())),
				new NameValuePair("timestamp", timestamp),
				new NameValuePair("sign", generateNotifySign(item, timestamp))
		};
		StringBuffer sb = new StringBuffer();
		for(NameValuePair pair : requestData){
			sb.append(pair.getName() + "=" + pair.getValue() + "&");
		}
		logger.info("尝试为交易[" + item.getTransactionId() + "]发送异步通知到:" + notifyUrl + "?" + sb.toString());


		String result = null;

		try{
			result = HttpUtils.postData(notifyUrl, requestData);
		}catch(Exception e){
			logger.warn("在发送交易[" + item.getTransactionId() + "]异步通知时发生异常:" + e.getMessage());
			return e.getMessage();
		}
		logger.info("交易[" + item.getTransactionId() + "]通知发送结果:" + result);

		return result;
	}

	//发送旧麦卡系统规范的异步通知
	private String sendNotifyLegacy(Item item){
		if(item == null){
			logger.warn("尝试发送异步通知的Item为空");
			return "尝试发送异步通知的Item为空";
		}
		if(item.getItemDataMap() == null || item.getItemDataMap().size() < 1){
			logger.warn("尝试发送异步通知的Item[" + item.getTransactionId() + "]扩展数据为空");
			return "尝试发送异步通知的Item[" + item.getTransactionId() + "]扩展数据为空";
		}
		String notifyUrl = null;
		try{
			notifyUrl = item.getItemDataMap().get(DataName.payNotifyUrl.toString()).getDataValue();
		}catch(Exception e){}
		if(notifyUrl == null){
			logger.warn("找不到Item[" + item.getTransactionId() + "]的payNotifyUrl数据");
			return "找不到Item[" + item.getTransactionId() + "]的payNotifyUrl数据";
		}
		String szxPay = String.valueOf(item.getSuccessMoney()).replaceAll("\\.0+", "");
		String szxRs = null;
		if(item.getCurrentStatus() == TransactionStatus.success.getId()){
			szxRs = "0";
		} else {
			szxRs = "1";
		}

		String szxDesc = null;		
		try{			
			szxDesc = item.getItemDataMap().get(DataName.legacyOutDesc.toString()).getDataValue();
		}catch(Exception e){}
		if(szxDesc == null){
			szxDesc = "";
		}

		String szxSign = null;		
		try{			
			szxSign = item.getItemDataMap().get(DataName.legacyOutUsername.toString()).getDataValue();
		}catch(Exception e){}
		if(szxSign == null){
			szxSign = "";
		}

		String szxGoods = null;		
		try{			
			szxGoods = item.getItemDataMap().get(DataName.legacyOutGoods.toString()).getDataValue();
		}catch(Exception e){}
		if(szxGoods == null){
			szxGoods = "";
		}

		String szxGoodinfo = "";
		try{
			szxGoodinfo += item.getItemDataMap().get(DataName.productSerialNumber.toString()).getDataValue();
		}catch(Exception e){
			logger.error("无法获取交易的数据productSerialNumber");
		}
		szxGoodinfo += ",";
		try{
			szxGoodinfo += item.getItemDataMap().get(DataName.productPassword.toString()).getDataValue();
		}catch(Exception e){
			logger.error("无法获取交易的数据productPassword");
		}
		szxGoodinfo += ",";
		szxGoodinfo += szxPay;
		szxGoodinfo += ",";
		if(item.getCurrentStatus() == TransactionStatus.success.getId()){
			szxGoodinfo += "0";
		} else {
			szxGoodinfo += "ERROR0040";
		}


		User partner = partnerService.select(item.getChargeFromAccount());
		if(partner == null){
			logger.error("找不到合作伙伴[" + item.getChargeFromAccount() + "]");
			return "找不到合作伙伴[" + item.getChargeFromAccount() + "]";
		}
		String key = null;
		try{
			key = partner.getUserConfigMap().get(DataName.supplierLoginKey.toString()).getDataValue();
		}catch(Exception e){

		}
		if(key == null){
			logger.error("合作伙伴[" + partner.getUuid() + "]未配置supplierLoginKey");
			return "合作伙伴[" + partner.getUuid() + "]未配置supplierLoginKey";
		}




		String src = "5" + item.getInOrderId() + szxSign + item.getChargeFromAccount()  + szxDesc + szxGoods + szxGoodinfo + szxPay + szxRs + key;
		String md5 = DigestUtils.md5Hex(src).toLowerCase();
		logger.info("生成旧规范校验:" + src + "=======>" + md5);

		/*try{
			szxSign =java.net.URLEncoder.encode(szxSign,"UTF-8");
			szxDesc =java.net.URLEncoder.encode(szxDesc,"UTF-8");
			szxGoods =java.net.URLEncoder.encode(szxGoods,"UTF-8");
		}catch(Exception e){}*/

		NameValuePair[] requestData = { 
				new NameValuePair("szxVersion", "5"),
				new NameValuePair("szxOid", item.getInOrderId()),
				new NameValuePair("szxSign", szxSign),
				new NameValuePair("szxUserId", String.valueOf(item.getChargeFromAccount())),
				new NameValuePair("szxDesc", szxDesc),
				new NameValuePair("szxGoods", szxGoods),
				new NameValuePair("szxGoodinfo", szxGoodinfo),
				new NameValuePair("szxPay", szxPay),
				new NameValuePair("szxRs", szxRs),
				new NameValuePair("szxCheckMd5", md5)
		};
		StringBuffer sb = new StringBuffer();
		for(NameValuePair pair : requestData){
			sb.append(pair.getName() + "=" + pair.getValue() + "&");
		}
		logger.info("尝试为交易[" + item.getTransactionId() + "]发送异步通知到:" + notifyUrl + "?" + sb.toString());

		String result = null;

		try{
			result = HttpUtils.postData(notifyUrl, requestData);
		}catch(Exception e){
			logger.warn("在发送交易[" + item.getTransactionId() + "]旧规范异步通知时发生异常:" + e.getMessage());
			return e.getMessage();
		}
		logger.info("交易[" + item.getTransactionId() + "]旧规范通知发送结果:" + result);

		return result;
	}



	private String generateNotifySign(Item item, String timestamp) {
		if(item == null){
			return null;
		}
		User partner = partnerService.select(item.getChargeFromAccount());
		if(partner == null){
			return null;
		}
		String key = null;
		try{
			key = partner.getUserConfigMap().get(DataName.supplierLoginKey.toString()).getDataValue();
		}catch(Exception e){

		}
		if(key == null){
			logger.error("合作伙伴[" + partner.getUuid() + "]未配置supplierLoginKey");
			return null;
		}
		try{
			String src = partner.getUuid() + "|" 
					+ item.getInOrderId() + "|"
					+ timestamp;
			src = src + "|" + key;
			String md5 = DigestUtils.md5Hex(src).toLowerCase();
			if(logger.isDebugEnabled()){
				logger.debug("生成异步通知校验，源字符串[" + src + "]的MD5结果" + md5);
			}
			return md5;

		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}


	@Override
	public EisMessage start() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public EisMessage stop() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public EisMessage status() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public EisMessage start(String objectType, int... objectIds) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * 检查未成功发送的通知，并重新发送
	 * 根据最大重发次数和最大重发时间段来判断是否需要重发
	 * 延时78秒后启动，每分钟检查一次
	 */
	@Override
	//	@Scheduled(initialDelay= 28 * 1000, fixedRate = 30 * 1000) 
	public void resendFailedNotify(){
		
		List<FailedNotify> failedNotifyList = failedNotifyService.list(new FailedNotifyCriteria());
		if(failedNotifyList == null || failedNotifyList.size() < 1){
			logger.info("系统中没有发送失败的通知");
			return;
		}
		for(FailedNotify failedNotify: failedNotifyList){
			if(failedNotify.getTotalSendCount() > maxNotifyCount){
				logger.info("订单[" + failedNotify.getTransactionId() + "]的通知发送次数[" + failedNotify.getTotalSendCount() + "]已超过系统上限" + maxNotifyCount + ",不再发送，并从失败通知中删除");
				failedNotifyService.delete(failedNotify.getTransactionId());
				continue;
			}
			if((new Date().getTime() - failedNotify.getFirstSendTime().getTime()) / 1000 > maxNotifyMinute * 60){
				logger.info("订单[" + failedNotify.getTransactionId() + "]的初次发送时间[" + sdf.format(failedNotify.getFirstSendTime().getTime()) + "]已超过系统规定的最长发送时间" + maxNotifyMinute + "分钟，不再发送，并从失败通知中删除");
				failedNotifyService.delete(failedNotify.getTransactionId());
				continue;
			}

			int notifySendRetryInterval = 0;
			int customNotifyCount = 0;

			if(failedNotify.getObjectType() == null || failedNotify.getObjectType().equalsIgnoreCase("item")){
				//分析订单对应用户的通知规则
				Item item = itemService.select(failedNotify.getTransactionId().trim());
				if(item == null){
					logger.error("找不到失败通知所对应的交易[" + failedNotify.getTransactionId() + "]" );
				}
				if(item.getItemDataMap() == null){
					logger.error("找不到交易[" + failedNotify.getTransactionId() + "]的扩展数据" );
				}
				//查找该交易对应的异步通知策略
				try{
					customNotifyCount = Integer.parseInt(item.getItemDataMap().get(DataName.maxNotifyCount.toString()).getDataValue());
				}catch(Exception e){
					logger.error("找不到交易[" + failedNotify.getTransactionId() + "]通知发送次数maxNotifySendCount");
				}
				try{
					notifySendRetryInterval = Integer.parseInt(item.getItemDataMap().get(DataName.notifySendRetryInterval.toString()).getDataValue());
				}catch(Exception e){
					logger.error("找不到交易[" + failedNotify.getTransactionId() + "]自定义通知发送次数notifySendRetryInterval");
				}

				if(customNotifyCount > 0 && failedNotify.getTotalSendCount() > customNotifyCount){
					logger.info("订单[" + failedNotify.getTransactionId() + "]的通知发送次数[" + failedNotify.getTotalSendCount() + "]已超过自定义发送上限" + customNotifyCount + ",不再发送，并从失败通知中删除");
					failedNotifyService.delete(failedNotify.getTransactionId());
					continue;
				}


				if(notifySendRetryInterval > 0 && failedNotify.getTotalSendCount() > notifySendRetryInterval){
					logger.info("订单[" + failedNotify.getTransactionId() + "]的通知发送次数[" + failedNotify.getTotalSendCount() + "]已超过用户策略上限" + notifySendRetryInterval + ",不再发送，并从失败通知中删除");
					failedNotifyService.delete(failedNotify.getTransactionId());
					continue;
				}

				/*
				 * 发送间隔为初始发送间隔乘以发送总次数
				 * 如果已经发送10次还没成功，那么下一次发送时间间隔应当是10*60=600秒即10分钟
				 */
				int sendInterval = initSendInterval * failedNotify.getTotalSendCount();
				if(failedNotify.getLastSendTime() != null &&  (new Date().getTime() - failedNotify.getLastSendTime().getTime()) / 1000 < sendInterval){
					logger.info("订单[" + failedNotify.getTransactionId() + "]的上次发送时间[" + sdf.format(failedNotify.getLastSendTime()) + "]还没达到通知发送间隔秒数[" + sendInterval + "],本次不发送");
					continue;
				}




				sendNotify(item);		
			} else if(failedNotify.getObjectType().equalsIgnoreCase("pay")){
				Pay pay = payService.select(failedNotify.getTransactionId().trim());
				if(pay == null){
					logger.error("找不到失败通知所对应的交易[" + failedNotify.getTransactionId() + "]" );
				}
				


				int sendInterval = initSendInterval * failedNotify.getTotalSendCount();
				if(failedNotify.getLastSendTime() != null &&  (new Date().getTime() - failedNotify.getLastSendTime().getTime()) / 1000 < sendInterval){
					logger.info("订单[" + failedNotify.getTransactionId() + "]的上次发送时间[" + sdf.format(failedNotify.getLastSendTime()) + "]还没达到通知发送间隔秒数[" + sendInterval + "],本次不发送");
					continue;
				}




				sendNotify(pay);		
			} else {
				//withdraw
				Withdraw withdraw = withdrawDao.select(failedNotify.getTransactionId().trim());
				if(withdraw == null){
					logger.error("找不到失败通知所对应的出款交易[" + failedNotify.getTransactionId() + "]" );
					continue;
				}
				


				int sendInterval = initSendInterval * failedNotify.getTotalSendCount();
				if(failedNotify.getLastSendTime() != null &&  (new Date().getTime() - failedNotify.getLastSendTime().getTime()) / 1000 < sendInterval){
					logger.info("订单[" + failedNotify.getTransactionId() + "]的上次发送时间[" + sdf.format(failedNotify.getLastSendTime()) + "]还没达到通知发送间隔秒数[" + sendInterval + "],本次不发送");
					continue;
				}




				sendNotify(withdraw);	
			}

		}


	}

	private boolean isValidResponse(Item item, String response){
		if(StringUtils.isBlank(response)){
			return false;
		}
		if(response.trim().startsWith(item.getInOrderId()) 
				|| response.trim().equalsIgnoreCase("true") 
				|| response.trim().equalsIgnoreCase("ok")){
			return true;
		}
		return false;
	}

	private boolean isValidResponse(Pay pay, String response){
		if(StringUtils.isBlank(response)){
			return false;
		}
		if(response.trim().startsWith(pay.getInOrderId()) 
				|| response.trim().equalsIgnoreCase("true") 
				|| response.trim().equalsIgnoreCase("ok")
				|| response.trim().equalsIgnoreCase("success")
				|| response.trim().equals("1")){
			return true;
		}
		return false;	}


	@Override
	public void sendNotify(Pay pay) {
		String notifyUrl = pay.getInNotifyUrl();
		if(StringUtils.isBlank(notifyUrl)){
			logger.error("尝试发送的pay对象没有inNotifyUrl");
			return;
		}

		Map<String,String> param = payService.generateClientResponseMap(pay);
		if(param == null || param.size() < 1){
			logger.error("尝试跳转的pay对象生成参数为空");
			return;
		}

		long customNotifySendRetry = pay.getLongExtraValue(DataName.maxNotifyCount.toString());
		if(customNotifySendRetry <= 0){
			customNotifySendRetry = notifySendRetry;
		}
		long customNotifySendRetryInterval = pay.getLongExtraValue(DataName.notifySendRetryInterval.toString());
		if(customNotifySendRetryInterval <= 0){
			customNotifySendRetryInterval = notifySendRetryInterval;
		}

		boolean sendSuccess = false;
		int sendCount = 0;

		for(int i = 0; i < customNotifySendRetry; i++){		
			sendCount = i+1;
			if(logger.isDebugEnabled()){
				logger.debug("第" + (i+1) + "次为交易[" + pay.getTransactionId() + "]发送异步通知，共尝试" + notifySendRetry + "次");
			}
			String result = null;
			result = syncSendNotify(pay);
			logger.debug("第" + (i+1) + "次为交易[" + pay.getTransactionId() + "]发送异步通知，结果:" + result);
			if(isValidResponse(pay, result)){
				sendSuccess = true;
				break;
			}			
			try {
				Thread.sleep(customNotifySendRetryInterval * 1000 * (i+1));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	

		}
		logger.info("交易[" + pay.getTransactionId() + "]通知发送是否成功:" + sendSuccess + ",发送次数:" + sendCount);	
		/*if(!sendSuccess){
			FailedNotify failedNotify = new FailedNotify(item.getTransactionId());
			failedNotifyService.replace(failedNotify);
			logger.info("交易[" + item.getTransactionId() + "]在尝试" + sendCount + "次后仍然失败，记录为失败通知");
		} else {
			failedNotifyService.delete(item.getTransactionId());
			logger.info("交易[" + item.getTransactionId() + "]在尝试" + sendCount + "次后发送成功，从失败通知中去除");
		}
		return;

		logger.info("交易[" + pay.getTransactionId() + "]通知发送结果:" + result);	*/
	}


	@Override
	public String syncSendNotify(Pay pay) {
		String notifyUrl = pay.getInNotifyUrl();
		if(StringUtils.isBlank(notifyUrl)){
			logger.error("尝试发送的pay[" + pay.getTransactionId() + "]没有inNotifyUrl");
			return null;
		}
		String localData = notifyUrl.toLowerCase().replaceAll("https","").replaceAll("http", "");
		if(localData.startsWith("localhost") || localData.startsWith("127.0.0.1")){
			logger.warn("尝试发送的pay[" + pay.getTransactionId() + "]其inNotifyUrl为本地地址");
		}

		Map<String,String> param = payService.generateClientResponseMap(pay);
		if(param == null || param.size() < 1){
			logger.error("尝试跳转的pay对象生成参数为空");
			return null;
		}


		NameValuePair[] requestData = new NameValuePair[param.size()];
		int i = 0;
		for(String key : param.keySet()){
			requestData[i] = new NameValuePair(key, param.get(key));
			i++;
		}

		StringBuffer sb = new StringBuffer();
		for(NameValuePair pair : requestData){
			sb.append(pair.getName() + "=" + pair.getValue() + "&");
		}
		logger.info("尝试为交易[" + pay.getTransactionId() + "]发送异步通知到:" + notifyUrl + "?" + sb.toString());


		String result = null;

		try{
			result = HttpUtils.postData(notifyUrl, requestData);
		}catch(Exception e){
			logger.warn("在发送交易[" + pay.getTransactionId() + "]异步通知时发生异常:" + e.getMessage());
			e.printStackTrace();
			return null;
		}
		logger.info("交易[" + pay.getTransactionId() + "]通知发送结果:" + result);	
		
		if(isValidResponse(pay, result)){
			failedNotifyService.delete(pay.getTransactionId());
			logger.info("支付订单[" + pay.getTransactionId() + "]直接发送成功，从失败通知中去除");
		} else {
			FailedNotify failedNotify = new FailedNotify(pay.getTransactionId());
			failedNotifyService.replace(failedNotify);
			logger.info("交易[" + pay.getTransactionId() + "]直接发送失败，记录为失败通知");
		}
		return result;
	}

	@Override
	@Async
	public void sendNotify(Withdraw withdraw) {
		String notifyUrl = withdraw.getExtraValue("inNotifyUrl");
		if(StringUtils.isBlank(notifyUrl)){
			logger.error("尝试发送的pay对象没有inNotifyUrl");
			return;
		}

		long customNotifySendRetry = withdraw.getLongExtraValue(DataName.maxNotifyCount.toString());
		if(customNotifySendRetry <= 0){
			customNotifySendRetry = notifySendRetry;
		}
		long customNotifySendRetryInterval = withdraw.getLongExtraValue(DataName.notifySendRetryInterval.toString());
		if(customNotifySendRetryInterval <= 0){
			customNotifySendRetryInterval = notifySendRetryInterval;
		}

		boolean sendSuccess = false;
		int sendCount = 0;

		String result = null;
		for(int i = 0; i < customNotifySendRetry; i++){		
			sendCount = i+1;
			if(logger.isDebugEnabled()){
				logger.debug("第" + (i+1) + "次为交易[" + withdraw.getTransactionId() + "]发送异步通知，共尝试" + notifySendRetry + "次");
			}
			result = syncSendNotify(withdraw);
			logger.debug("第" + (i+1) + "次为交易[" + withdraw.getTransactionId() + "]发送异步通知，结果:" + result);
			if(result != null && ( result.trim().equals("true") || result.trim().equals("1") || result.trim().equals("success"))){
				sendSuccess = true;
				break;
			}			
			try {
				Thread.sleep(customNotifySendRetryInterval * 1000 * (i+1));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	

		}
		logger.info("交易[" + withdraw.getTransactionId() + "]通知发送是否成功:" + sendSuccess + ",发送次数:" + sendCount);	
		if(!sendSuccess){
			FailedNotify failedNotify = new FailedNotify(withdraw.getTransactionId());
			failedNotifyService.replace(failedNotify);
			logger.info("交易[" + withdraw.getTransactionId() + "]在尝试" + sendCount + "次后仍然失败，记录为失败通知");
		} else {
			failedNotifyService.delete(withdraw.getTransactionId());
			logger.info("交易[" + withdraw.getTransactionId() + "]在尝试" + sendCount + "次后发送成功，从失败通知中去除");
		}
		logger.info("交易[" + withdraw.getTransactionId() + "]通知发送结果:" + result);	
		return;

	}

	@Override
	public String syncSendNotify(Withdraw withdraw) {
		String notifyUrl = withdraw.getExtraValue("inNotifyUrl");
		if(StringUtils.isBlank(notifyUrl)){
			logger.error("尝试发送的pay对象没有inNotifyUrl");
			return null;
		}

		Map<String,String> param = new HashMap<String,String>();
		param.put("transactionId",withdraw.getTransactionId());
		if(StringUtils.isNotBlank(withdraw.getInOrderId())){
			param.put("orderId",withdraw.getInOrderId());
		}
		param.put("result", String.valueOf(withdraw.getCurrentStatus()));
		param.put("timestamp", String.valueOf(new Date().getTime()));

		User partner = partnerService.select(withdraw.getUuid());
		String loginKey = partner.getExtraValue(DataName.supplierLoginKey.toString());
		if(StringUtils.isBlank(loginKey)){
			logger.error("找不到指定的商户[" + withdraw.getUuid() + "]的密钥");
			return null;
		}

		String batchData = null;

		float successMoney = 0;

		if(withdraw.getTotalRequest() > 0){
			//这是一个批量

			param.put("totalCount", String.valueOf(withdraw.getTotalRequest()));
			param.put("successCount", String.valueOf(withdraw.getSuccessRequest()));

			StringBuffer sb2 = new StringBuffer();
			WithdrawCriteria withdrawCriteria = new WithdrawCriteria(withdraw.getOwnerId());
			withdrawCriteria.setUuid(withdraw.getUuid());
			withdrawCriteria.setParentTransactionId(withdraw.getTransactionId());
			List<Withdraw> subWithdrawList = withdrawDao.list(withdrawCriteria);
			if(logger.isDebugEnabled())logger.debug("查询批付订单[" + withdraw.getTransactionId() + "]的所有子订单数量应为:" + withdraw.getTotalRequest() + ",实际得到的子订单数量是:" + (subWithdrawList == null ? "空" : subWithdrawList.size()));
			if(subWithdrawList == null || subWithdrawList.size() < 1){
				//错误的子订单
			} else {
				for(Withdraw subWithdraw : subWithdrawList){
					if(subWithdraw.getCurrentStatus() == TransactionStatus.inProcess.id || subWithdraw.getCurrentStatus() == TransactionStatus.newOrder.id){
						logger.error("批付子订单[" + subWithdraw.getTransactionId() + "]还未处理或在处理中，不能发送通知");
						return "批付子订单[" + subWithdraw.getTransactionId() + "]还未处理或在处理中，不能发送通知";
					}
					sb2.append(subWithdraw.getTransactionId().replaceAll("^" + withdraw.getTransactionId() + "-","")).append('^');
					sb2.append(subWithdraw.getBankAccount().getBankAccountNumber()).append('^');
					sb2.append(subWithdraw.getBankAccount().getBankAccountName()).append('^');
					sb2.append(df.format(subWithdraw.getWithdrawMoney())).append('^');
					if(subWithdraw.getCurrentStatus() == TransactionStatus.success.id){
						sb2.append('S');
						successMoney += subWithdraw.getArriveMoney();
					} else {
						sb2.append('F');
					}
					sb2.append('|');
				}
				batchData = sb2.toString().replaceAll("\\|$", "");
			}

		} else {

			successMoney = withdraw.getArriveMoney();
		}
		param.put("successMoney", df.format(successMoney));


		if(batchData != null){
			param.put("batchData", batchData);
		}
		List<String> keys = new ArrayList<String>(param.keySet());
		Collections.sort(keys);
		StringBuffer sb = new StringBuffer();
		for (String key : keys) {
			String value = param.get(key);
			if(StringUtils.isBlank(value)){
				continue;
			}
			sb.append(key);
			sb.append('=');
			sb.append(value);
			sb.append('&');		
		}



		String signSource = sb.toString().replaceAll("&$", "");		

		signSource += "&key=" + loginKey;
		String sign = DigestUtils.md5Hex(signSource);
		logger.debug("为提现交易[" + withdraw.getTransactionId() + "]签名，签名源[" + signSource + "]，MD5=" + sign);

		int offset = 3;
		NameValuePair[] requestData = new NameValuePair[param.size() + offset];
		requestData[0] = new NameValuePair("sign",sign);
		requestData[1] = new NameValuePair("beginTime", sdf.format(withdraw.getBeginTime()));
		if(withdraw.getEndTime() == null){
			requestData[2] = new NameValuePair("endTime","");
		} else {
			requestData[2] = new NameValuePair("endTime",sdf.format(withdraw.getEndTime()));
		}

		//	requestData[1] = new NameValuePair("money",new DecimalFormat("0.00").format(withdraw.getArriveMoney()));
		if(batchData != null){
			String cryptKey = partner.getExtraValue(DataName.supplierChargeKey.toString());
			if(cryptKey == null){
				logger.error("无法发送批付通知，商户[" + partner.getUuid() + "]没有配置supplierChargeKey");
				return "无法发送批付通知，商户[" + partner.getUuid() + "]没有配置supplierChargeKey";
			} 
			Crypt crypt = new Crypt();
			crypt.setDes3Key(cryptKey);
			String cryptBatchData = crypt.des3Encrypt(batchData);
			param.put("batchData", cryptBatchData);
		}
		int i = offset;
		for(String key : param.keySet()){
			requestData[i] = new NameValuePair(key, param.get(key));
			i++;
		}
		sb.setLength(0);
		for(NameValuePair data : requestData){
			sb.append(data.getName()).append('=').append(data.getValue()).append('&');
		}

		logger.info("尝试为提现交易[" + withdraw.getTransactionId() + "]发送异步通知到:" + notifyUrl + "?" + sb.toString() + "sign=" + sign);


		String result = null;

		try{
			result = HttpUtils.postData(notifyUrl, requestData);
		}catch(Exception e){
			logger.warn("在发送交易[" + withdraw.getTransactionId() + "]异步通知时发生异常:" + e.getMessage());
			e.printStackTrace();
			return null;
		}
		logger.info("交易[" + withdraw.getTransactionId() + "]通知发送结果:" + result);	
		return result;
	}
}


