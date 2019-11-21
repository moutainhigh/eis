package com.maicard.money.service.impl;

import java.lang.reflect.Field;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.maicard.common.base.BaseService;
import com.maicard.common.util.JsonUtils;
import com.maicard.money.domain.Pay;
import com.maicard.money.domain.PayChannelMechInfo;
import com.maicard.money.domain.PayMethod;
import com.maicard.money.domain.Withdraw;
import com.maicard.money.domain.WithdrawMethod;
import com.maicard.money.service.ChannelService;
import com.maicard.money.service.PayMethodService;
import com.maicard.money.service.WithdrawMethodService;

@Service
public class ChannelServiceImpl extends BaseService implements ChannelService {

	@Resource
	private PayMethodService payMethodService;

	@Resource
	private WithdrawMethodService withdrawMethodService;

	@Override
	public PayChannelMechInfo getChannelInfo(Pay pay) {
		Assert.notNull(pay,"尝试获取支付参数的pay实例不能为空");
		PayMethod payMethod = pay.getPayMethod();
		if(payMethod == null && pay.getPayMethodId() > 0){
			payMethod = payMethodService.select(pay.getPayMethodId());
		}
		Assert.notNull(payMethod,"尝试获取支付参数的pay实例中的payMethod不能为空");
		logger.debug("当前的支付方式实例是:" + JSON.toJSONString(payMethod));
		PayChannelMechInfo payChannelMechInfo = new PayChannelMechInfo();
		
		if(payMethod.getData() == null || payMethod.getData().size() < 1) {
			logger.error("支付订单:{}对应的payMethod:{}没有任何扩展配置", pay.getTransactionId(), payMethod.getPayMethodId());
			return null;
		}
		
		//使用反射来设置属性
		boolean useReflect = true;
		for(Entry<String,String> entry : payMethod.getData().entrySet()) {
			Field field = null;
			try {
				field = PayChannelMechInfo.class.getDeclaredField(entry.getKey());

				if(field == null) {
					logger.warn("在类:" + PayChannelMechInfo.class.getSimpleName() + "中找不到指定的属性:" + entry.getKey());
					continue;
				}
				field.set(payChannelMechInfo, entry.getValue());
				logger.debug("设置PayChannelMechInfo的属性:" + entry.getKey() + "=>" + entry.getValue());
			}catch(Exception e) {
				//e.printStackTrace();
				logger.warn("在类:" + PayChannelMechInfo.class.getSimpleName() + "中找不到指定的属性异常:" + entry.getKey());

			}
		}
		
		if(useReflect) {
			return payChannelMechInfo;
		}

		String key = payMethod.getExtraValue("accountId");
		if(key != null){
			payChannelMechInfo.accountId = key.trim();
		} else {
			logger.warn("找不到ownerId=" + payMethod + "的配置参数accountId");
			//return null;
		}

		String accountName = payMethod.getExtraValue("accountName");
		if(accountName != null){
			payChannelMechInfo.accountName = accountName.trim();
		} else {
			logger.warn("找不到ownerId=" + payMethod + "的配置参数accountName");
			//return null;
		}
		String payKey = payMethod.getExtraValue("payKey");
		if(payKey != null){
			payChannelMechInfo.payKey = payKey.trim();
		} else {
			logger.error("找不到ownerId=" + payMethod + "的配置参数payKey");
			return null;
		}
		String cryptKey = payMethod.getExtraValue("cryptKey");
		if(cryptKey != null){
			payChannelMechInfo.cryptKey = cryptKey.trim();
		}

		String submitUrl = payMethod.getExtraValue("submitUrl");
		if(submitUrl != null){
			payChannelMechInfo.submitUrl = submitUrl.trim();
		} else {
			logger.error("找不到[" + payMethod + "]的配置参数submitUrl");
			return null;
		}
		String queryUrl = payMethod.getExtraValue("queryUrl");
		if(queryUrl != null){
			payChannelMechInfo.queryUrl = queryUrl.trim();
		} else {
			logger.error("找不到[" + payMethod + "]的配置参数queryUrl，无法进行查询");
		}

		payChannelMechInfo.setInProgressWhenSubmitFail = String.valueOf(payMethod.getBooleanExtraValue("setInProgressWhenSubmitFail"));
		/*
			synchronized(this){
				mechCache.put(String.valueOf(ownerId), heepayMechInfo);
			}*/
		//logger.debug("把ownerId=" + payMethod + "的汇付宝商户信息:" + heepayMechInfo + "放入缓存");



		return payChannelMechInfo;
	}

	@Override
	public PayChannelMechInfo getChannelInfo(Withdraw withdraw) {
		Assert.notNull(withdraw,"尝试获取支付参数的withdraw实例不能为空");
		WithdrawMethod withdrawMethod = withdraw.getWithdrawMethod();
		if(withdrawMethod == null && withdraw.getWithdrawMethodId() > 0){
			withdrawMethod = withdrawMethodService.select(withdraw.getWithdrawMethodId());
		}
		Assert.notNull(withdrawMethod,"尝试获取支付参数的withdraw实例中的withdrawMethod不能为空,withdrawTypeId=" + withdraw.getWithdrawTypeId() + ",withdrawMethodId=" + withdraw.getWithdrawMethodId());

		if(logger.isDebugEnabled())logger.debug("提现订单:" + withdraw.getTransactionId() + "对应的提现请求的withdrawMethod是:" + JsonUtils.toStringApi(withdrawMethod));
		PayChannelMechInfo payChannelMechInfo = new PayChannelMechInfo();


		if(withdrawMethod.getData() == null || withdrawMethod.getData().size() < 1) {
			logger.error("提现订单:{}对应的withdrawMethod:{}没有任何扩展配置", withdraw.getTransactionId(), withdrawMethod.getWithdrawMethodId());
			return null;
		}
		
		//使用反射来设置属性
		boolean useReflect = true;
		for(Entry<String,String> entry : withdrawMethod.getData().entrySet()) {
			Field field = null;
			try {
				field = PayChannelMechInfo.class.getDeclaredField(entry.getKey());

				if(field == null) {
					logger.warn("在类:" + PayChannelMechInfo.class.getSimpleName() + "中找不到指定的属性:" + entry.getKey());
					continue;
				}
				field.set(payChannelMechInfo, entry.getValue());
				logger.debug("设置PayChannelMechInfo的属性:" + entry.getKey() + "=>" + entry.getValue());
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		if(useReflect) {
			return payChannelMechInfo;
		}

		String key = withdrawMethod.getExtraValue("accountId");
		if(key != null){
			payChannelMechInfo.accountId = key.trim();
		} else {
			logger.error("找不到ownerId=" + withdrawMethod + "的配置参数accountId");
			return null;
		}

		String accountName = withdrawMethod.getExtraValue("accountName");
		if(accountName != null){
			payChannelMechInfo.accountName = accountName.trim();
		} else {
			logger.warn("找不到ownerId=" + withdrawMethod + "的配置参数accountName");
			//return null;
		}
		String smallWithdrawUrl = withdrawMethod.getExtraValue("smallWithdrawUrl");
		if(smallWithdrawUrl != null){
			payChannelMechInfo.smallWithdrawUrl = smallWithdrawUrl.trim();
		} else {
			logger.error("找不到ownerId=" + withdrawMethod + "的配置参数smallWithdrawUrl");
			return null;
		}

		String bigWithdrawUrl = withdrawMethod.getExtraValue("bigWithdrawUrl");
		if(bigWithdrawUrl != null){
			payChannelMechInfo.bigWithdrawUrl = bigWithdrawUrl.trim();
		} else {
			logger.warn("找不到ownerId=" + withdrawMethod + "的配置参数bigWithdrawUrl");
		}

		String withdrawKey = withdrawMethod.getExtraValue("withdrawKey");
		if(withdrawKey != null){
			payChannelMechInfo.withdrawKey = withdrawKey.trim();
		} else {
			logger.warn("找不到ownerId=" + withdrawMethod + "的配置参数withdrawKey");
		}

		String withdrawCryptKey = withdrawMethod.getExtraValue("withdrawCryptKey");
		if(withdrawCryptKey != null){
			payChannelMechInfo.withdrawCryptKey = withdrawCryptKey.trim();
		} else {
			logger.warn("找不到ownerId=" + withdrawMethod + "的配置参数withdrawCryptKey");
		}

		String withdrawQueryUrl = withdrawMethod.getExtraValue("withdrawQueryUrl");
		if(withdrawQueryUrl != null){
			payChannelMechInfo.withdrawQueryUrl = withdrawQueryUrl.trim();
		} else {
			logger.warn("找不到ownerId=" + withdrawMethod + "的配置参数withdrawQueryUrl");
		}

		String peerPublicKey = withdrawMethod.getExtraValue("peerPublicKey");
		if(withdrawQueryUrl != null){
			payChannelMechInfo.peerPublicKey = peerPublicKey.trim();
		}
		/*
			synchronized(this){
				mechCache.put(String.valueOf(ownerId), heepayMechInfo);
			}
			logger.debug("把ownerId=" + ownerId + "的汇付宝商户信息:" + heepayMechInfo + "放入缓存");
		 */

		return payChannelMechInfo;
	}

}
