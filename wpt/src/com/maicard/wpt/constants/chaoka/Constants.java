package com.maicard.wpt.constants.chaoka;

import com.maicard.standard.EisError;
import com.maicard.standard.TransactionStandard.TransactionStatus;

public class Constants {
	public static final int[] matchPaddingParentProductIds = new int[]{5101125, 5101213};
	public static final int poolItemWeight = 1;
	public static final int minSlowQueueWeight = 10;	//慢充队列的最小优先级，低于此优先级为蓄水池
	public static final int minFastQueueWeight = 20;	//快充队列的最小优先级，低于此优先级为慢充队列
	public static final int minVeryFastQueueWeight = 30;	//极速快充队列的最小优先级，低于此优先级为快充队列

	public static final int rollbackAccount = 300001;
	public static final int rollbackTtl = 300;
	
	//360等使用骏卡充值的订单，在请求金额只剩下这个数字时，无法正常处理，必须调用库存卡密进行匹配补单
	public static final float matchPaddingMoney = 5f;
	public static final long cacheLockTimeout = 500;
	public static boolean useMatchPadding = true;	//是否使用库存卡密进行匹配补单
	public static int minSlowQueueTtl = 40000;	//一般慢充的TTL，低于这个时间则不是慢充
	
	public static final int maxSuccessMoney = 20000;
	
	//卡密商户允许的最大问题卡上限，超过这个不进行官方匹配
	public static final int cardPartnerMaxErrorCount = 10;	
	
	//卡密商户状态缓存前缀
	public static final String cardPartnerStatusCachePrefix = "CardPartner";
	//卡密商户黑单状态缓存前缀
	public static final String cardPartnerBlackStatusCachePrefix = "CardPartnerBlack";
	
	//卡密商户被黑单后，多长时间内视为危险状态
	public static final int cardPartnerBlackStatusKeepSec = 600;
	
	//易宝未能消耗的卡密，多大比例直接返回受限卡而不走通道处理
	public static final int yeepayLimitCardRate = 98;
	
	//15173未能消耗的卡密，多大比例直接返回受限卡而不走通道处理
	public static final int js15173LimitCardRate = 5;

	
	//哪些状态会被骏网官方记录为一次提交
	public static final int[] jcardRecordStatus =  new int[]{
			TransactionStatus.success.getId(),
			TransactionStatus.accept.getId(),
			EisError.cardPasswordError.getId(),
			EisError.cardBeTaked.getId(),
			EisError.invalidCard.getId(),
			EisError.limitedCard.getId(),
			EisError.moneyIsFrozen.getId(),
			EisError.moneyNotEnough.getId(),
			EisError.serialNumberError.getId()
	};
	
	public static final int jcardSiteSameAccountSubmitIdelMinute = 60 * 5;	//骏网官方提交帐号的时间间隔，即每个帐号不能在这个时间内提交




}
