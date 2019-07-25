package com.maicard.wpt.custom.chaoka;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.maicard.common.domain.EisMessage;
import com.maicard.common.domain.OpEisObject;
import com.maicard.common.util.ContextTypeUtil;
import com.maicard.common.util.HttpUtils;
import com.maicard.common.util.StringTools;
import com.maicard.ec.domain.DeliveryOrder;
import com.maicard.exception.EisException;
import com.maicard.money.criteria.PayTypeCriteria;
import com.maicard.money.domain.Money;
import com.maicard.money.domain.Pay;
import com.maicard.money.domain.PayType;
import com.maicard.money.domain.Price;
import com.maicard.money.iface.PayProcessor;
import com.maicard.product.criteria.CartCriteria;
import com.maicard.product.criteria.ItemCriteria;
import com.maicard.product.domain.Activity;
import com.maicard.product.domain.Cart;
import com.maicard.product.domain.Item;
import com.maicard.product.service.ActivityProcessor;
import com.maicard.product.service.ProductValidator;
import com.maicard.security.domain.User;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.MoneyType;
import com.maicard.standard.ObjectType;
import com.maicard.standard.Operate;
import com.maicard.standard.OperateCode;
import com.maicard.standard.OperateResult;
import com.maicard.standard.PriceType;
import com.maicard.standard.SecurityStandard.UserTypes;
import com.maicard.standard.TransactionStandard.TransactionStatus;
import com.maicard.standard.TransactionStandard.TransactionType;
import com.maicard.wpt.controller.basic.AbstractBuyController;

/**
 * 炒卡网的购买处理器
 *
 */
@Controller
@RequestMapping("/buy")
public class BuyController extends AbstractBuyController{
	

	/*
	 * 结算
	 */
	@Override
	@RequestMapping(value="/settleUp")
	public String  settleUp(HttpServletRequest request, HttpServletResponse response, ModelMap map
			) throws Exception {
		logger.debug("开始点卡商城结算步骤");
		map.put("operate", OperateCode.ORDER_SETTLEUP.toString());
		User frontUser =  certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
		if(frontUser == null) {
				logger.error("需要先登录才能购物");
				throw new EisException(EisError.userNotFoundInSession.getId(), "请先登录");		
			}		
		logger.debug("开始用户:{}/{}结算", frontUser.getUuid(), frontUser.getOwnerId());


		long buyUuid = 0;
		if(uuidMapService != null){
			String appCode = ServletRequestUtils.getStringParameter(request, "appCode");

			buyUuid = uuidMapService.getMoneyUuid(frontUser,appCode);
		} else {
			buyUuid = frontUser.getUuid();
		}

		/////////////////////////获取参数/////////////////////////////////////////////////
		int payTypeId = ServletRequestUtils.getIntParameter(request, "payTypeId", 1);
		int orderId = ServletRequestUtils.getIntParameter(request, "orderId", 0);
		String[] tids = ServletRequestUtils.getStringParameters(request, "tid");
		/////////////////////////////////////////////////////////////////////////////////////
		

		HashMap<String, Item> cartMap = null;
		HashMap<String, Item> submitCart = new HashMap<String, Item>();


		ItemCriteria itemCriteria2 = new ItemCriteria(frontUser.getOwnerId());
		itemCriteria2.setChargeFromAccount(buyUuid);
		if(orderId > 0){
			itemCriteria2.setCartId(orderId);
			cartMap = cartService.map(itemCriteria2);
			logger.debug("购物车参数ownerId:"+frontUser.getOwnerId()+"#chargeFromAccount:"+buyUuid+"#orderId:"+orderId);
			logger.debug("根据cartId=" + orderId + "得到的用户购物车，其中交易数量是:" + (cartMap == null ? "空" : cartMap.size()));
			if(cartMap == null || cartMap.size() < 1){
				logger.error("用户请求支付的订单[" + orderId + "]没有对应的交易");
				throw new EisException(EisError.REQUIRED_PARAMETER.getId(), "您未选择任何商品");		
			}
			//对于提供了cartId/orderId的请求，
			//如果同时提交了要购买的交易ID，则把这些交易放入对应的cart
			//否则，直接把所有商品放入
			if(tids == null || tids.length == 0){
				logger.debug("用户未提交任何交易ID但提交了orderId，把cartId=" + orderId + "的所有交易放入待结算购物车");
				for(String tid : cartMap.keySet()){
					Item i = cartMap.get(tid);
					if(i.getCurrentStatus() != TransactionStatus.inCart.id && i.getCurrentStatus() != TransactionStatus.waitingPay.id){
						logger.error("用户请求支付的用户订单[" + orderId + "]对应的交易[" + i.getTransactionId() + "]不是等待支付或暂存状态，而是:" + i.getCurrentStatus());
						throw new EisException(EisError.dataError.getId(), "您选择的订单已结束[" + i.getCurrentStatus() + "]");		
					}
					logger.info("把从数据库中选择的订单[" + tid + "]放入等待支付");
					submitCart.put(tid, cartMap.get(tid));
				}
			} else {
				for(String cartId : cartMap.keySet()){
					for(String buyId : tids){
						logger.debug("XXXXXXXXXXXXXXXX>cartId = " + cartId + ",buyId=" + buyId);
						if(buyId.equalsIgnoreCase(cartId)){
							logger.info("把用户提交的订单[" + cartId + "]状态放入待结算购物车");
							submitCart.put(buyId, cartMap.get(cartId));
						}
					}
				}
			}

		} else {
			if(tids == null || tids.length == 0){
				throw new EisException(EisError.REQUIRED_PARAMETER.getId(), "您未选择任何商品");		
			}
			itemCriteria2.setCurrentStatus(TransactionStatus.inCart.getId());
			cartMap = cartService.map(itemCriteria2);
			if(cartMap == null || cartMap.size() < 1){//购物车是空的
				throw new EisException(EisError.OBJECT_IS_NULL.getId(), "您尚未购买任何产品，请先购买");		
			}
			//比对用户勾选提交的订单号与购物车订单号，只支付提交的订单号
			for(String cartId : cartMap.keySet()){
				for(String buyId : tids){
					if(buyId.equalsIgnoreCase(cartId)){
						logger.info("把用户提交的订单[" + cartId + "]状态放入待结算购物车");
						submitCart.put(buyId, cartMap.get(cartId));
					}
				}
			}
		}
		if(cartMap == null || cartMap.size() < 1){//购物车是空的
			throw new EisException(EisError.OBJECT_IS_NULL.getId(), "您尚未购买任何产品，请先购买");		
		}


		if(submitCart.size() < 1){
			logger.error("在待结算购物车中，找不到任何一个提交订单:" + StringTools.mergeArray(tids));
			throw new EisException(EisError.OBJECT_IS_NULL.getId(), "找不到您提交的订单，请重新购买");		
		}

		String submitOrderIds = "";
		//	logger.info("UUUUUUUUUUsubmitCart大小:" + submitCart.size() + ",当前系统代码是:" + configService.getSystemCode());

		for(Item item : submitCart.values()){
			if(item != null){
				OpEisObject targetObject = stockService.getTargetObject(item.getObjectType(), item.getProductId());
				if(targetObject == null){
					logger.error("订单[" + item.getTransactionId() + "]对应产品[" + item.getProductId() + "]不存在");
					continue;
				}


			}
		}
		

		//把已提交订单设置为等待支付
		//产生一个新的购物车，相当于一个总订单
		Cart cart = null;
		Cart order = null;
		DeliveryOrder deliveryOrder = null;

		if(orderId < 1){
			order = new Cart(frontUser.getOwnerId());
			order.setCurrentStatus(TransactionStatus.waitingPay.getId());
			order.setUuid(buyUuid);
		} else {
			cart = cartService.select(orderId);
			if(cart.getDeliveryOrderId() > 0){
				deliveryOrder = deliveryOrderService.select(cart.getDeliveryOrderId());
			}
			/*addressBookId = cart.getLongExtraValue("addressBookId");
			
			if( (toSelectDelivery || toSelectSelfCarry)  && deliveryOrder == null){
				logger.debug("订单[" + cart.getCartId() + "]需要配送或自提但是并未设置配送信息");
				throw new EisException(EisError.requireDeliveryAddress.getId(), "您的订单需要提供配送信息，请返回选择地址");		
			}*/
			order  = cart.clone();
			if(cart.getOrderType() != null && cart.getOrderType().equals(CartCriteria.ORDER_TYPE_STORE)){
				logger.debug("提交的订单[" + orderId + "]是一个永久订单,不需要新增订单");

			} else {
				order.setCartId(0);
				cartService.insert(order);
				logger.debug("提交的订单[" + orderId + "]是一个临时订单，生成一个新的永久订单:" + order.getCartId());
			}
		}
		order.setCreateTime(new Date());
		order.setLastAccessTime(order.getCreateTime());


		boolean forcePayWithoutAccountMoney = configService.getBooleanValue(DataName.forcePayWithoutAccountMoney.toString(), frontUser.getOwnerId());
		order.setExtraValue(DataName.forcePayWithoutAccountMoney.toString(), String.valueOf(forcePayWithoutAccountMoney));
		Money money = null;
		if(order.getMoney() == null){
			order.setMoney(new Money(buyUuid, frontUser.getOwnerId()));
		}

		money = moneyService.select(buyUuid, frontUser.getOwnerId());	

		if(money == null){			
			money = new Money(buyUuid,frontUser.getOwnerId());
		}
		float needMoney = 0;
		float needCoin = 0f;
		float needPoint = 0f;
		//		String moneyBuyOrderIds = "";
		String moneyBuyName = "";
		int unitCount = 0;

		//购物车/订单的有效期，以购物车中商品最小有效期
		long cartTtl = 0;

		List<Item> settelUpItems = new ArrayList<Item>();
		//取消了总金额Xcount的算法,NetSnake,2016-7-5
		for(Item t : submitCart.values()){
			if(t.getCount() < 1){
				t.setCount(1);
			}
			if(t.getRequestMoney() > 0f){
				moneyBuyName += t.getName();
				needMoney += t.getRequestMoney();
				//needMoney += t.getRequestMoney() * t.getCount();
			}
			if(cartTtl == 0){
				cartTtl = t.getTtl();
			} else {
				cartTtl = t.getTtl() > cartTtl ? cartTtl : t.getTtl();
			}
			unitCount += t.getCount();
			logger.info("结算商品[" + t.getTransactionId() + "]结算金额:" + t.getRequestMoney() + "/" + t.getFrozenMoney() + "/" + t.getSuccessMoney() + "/" + t.getInMoney() + ",数量:" + t.getCount() + ",总件数:" + unitCount);
			needCoin += t.getFrozenMoney();// * t.getCount();
			needPoint += t.getSuccessMoney();// * t.getCount();
			settelUpItems.add(t);


			Price price = t.getPrice();

			if(PriceType.PRICE_PROMOTION.toString().equals(price.getPriceType())){
				//尝试根据price的identify得到对应的活动
				String activityCode = price.getIdentify();
				if(StringUtils.isBlank(activityCode)){
					logger.debug("促销价格[" + price.getPriceId() + "]没有指定的identify，不再查找对应的活动");
				} else {
					Activity activity = activityService.select(activityCode, price.getOwnerId());
					if(activity == null){
						logger.warn("根据促销价格[" + price.getPriceId() + "]指定的identify[" + price.getIdentify() + "]找不到任何活动");
					} else {
						logger.info("根据促销价格[" + price.getPriceId() + "]指定的identify[" + price.getIdentify() + "]找到的活动是:" + activity.getActivityId() + ",处理器是:" + activity.getProcessor());
						//调用活动处理器
						ActivityProcessor processor  = applicationContextService.getBeanGeneric(activity.getProcessor());
						if(processor == null){
							logger.warn("找不到促销价格[" + price.getPriceId() + "]的identify[" + price.getIdentify() + "]对应活动[" + activity.getActivityId() + "]的活动处理器:" + activity.getProcessor());
						} else {
							Map<String,Object> parameterMap = new HashMap<String,Object>();
							parameterMap.put("request", request);
							parameterMap.put("map", map);
							parameterMap.put("item", t);
							EisMessage checkResult = processor.execute(Operate.settleUp.getCode(), activity, frontUser, parameterMap);
							if(checkResult == null){
								logger.error("用户[" + frontUser.getUuid() + "]无法通过活动处理器[" + processor + "]的检查，返回结果是空");
								throw new EisException(EisError.activityLimited.getId(), "您无法购买该活动商品，活动异常错误");		
							}

							if(checkResult.getOperateCode() != OperateResult.success.getId()){
								logger.error("用户[" + frontUser.getUuid() + "]无法通过活动处理器[" + processor + "]的检查，返回结果是:" + checkResult);
								throw new EisException(checkResult.getOperateCode(), "您无法购买该活动商品");		
							}
							logger.info("用户[" + frontUser.getUuid() + "]通过了活动处理器[" + processor + "]的检查，返回结果是:" + checkResult);

						}
					}
				}				
			}


		}
		if(needMoney == 0 && needCoin == 0 && needPoint == 0){
			logger.warn("所购商品总金额为0");
		}
		if(cart.getTtl() == 0){
			cart.setTtl(cartTtl);
		}

		//该订单的coin是否已经由第三方coin支付处理器完成
		Pay coinPay = new Pay();

		boolean coinPayed = order.getBooleanExtraValue(DataName.coinPaid.toString());
		boolean needCoinPay = false;
		PayProcessor coinPayProcessor = null;
		logger.debug("当前订单[" + order.getCartId() + "]是否已经使用coin支付:" + coinPayed);

		String toAccountName = frontUser.getNickName();
		if(toAccountName == null){
			toAccountName = frontUser.getUsername();
		}
		logger.info("needCoin:"+needCoin+"#money.getCoin:"+money.getCoin());
		if(!coinPayed && needCoin > money.getCoin()){
			//需要使用coin进行支付，该coin可能是外部系统的资金比如比特币			
			logger.info("用户[" + buyUuid + "]账户coin:" + money.getCoin() + "低于结算单所需coin[" + needCoin + "]");

			PayTypeCriteria payTypeCriteria = new PayTypeCriteria(cart.getOwnerId());
			payTypeCriteria.setFlag(MoneyType.coin.getId());
			List<PayType> payTypeList = payTypeService.list(payTypeCriteria);
			PayType coinPayType = null;
			if(payTypeList == null || payTypeList.size() < 1){
				logger.info("系统未配置一个[flag=" + MoneyType.coin.getId() + "]的coin支付方式,无法支付所需coin=" + needCoin);
				throw new EisException(EisError.moneyNotEnough.id, "您的资金不足");		

			} else {
				needCoinPay = true;
				coinPayType = payTypeList.get(0);
				Pay fakePay = new Pay();
				fakePay.setPayTypeId(coinPayType.getPayTypeId());
				fakePay.setContextType(ContextTypeUtil.getContextType(request));
				coinPayProcessor = payService.getProcessor(fakePay);
				logger.info("系统当前配置的coin支付方式是:" + coinPayType.getPayTypeId() + ":" + coinPayType.getName() + ",处理器是:" + coinPayProcessor);
				if(coinPayProcessor == null){
					logger.error("找不到系统指定的coin支付方式[" + coinPayType.getPayTypeId() + "对应的处理器");
					throw new EisException(EisError.payProcessorIsNull.id, "找不到指定的支付处理程序");		

				}
			}




			if(needCoinPay){

				coinPay.setTransactionId(globalOrderIdService.generate(TransactionType.pay.getId()));
				coinPay.setPayFromAccount(buyUuid);
				coinPay.setPayFromAccountType(UserTypes.frontUser.getId());
				coinPay.setPayToAccount(buyUuid);
				coinPay.setPayToAccountType(UserTypes.frontUser.getId());
				coinPay.setExtraValue("fromAccountName", toAccountName);
				coinPay.setName(moneyBuyName);
				coinPay.setFaceMoney(needCoin);
				coinPay.setStartTime(new Date());
				coinPay.setPayTypeId(coinPayType.getPayTypeId());
				coinPay.setSign(DigestUtils.md5Hex(UUID.randomUUID().toString()));
				coinPay.setMoneyTypeId(MoneyType.coin.getId());
				coinPay.setContextType(ContextTypeUtil.getContextType(request));
				coinPay.setExtraValue("toAccountName",toAccountName);				
				coinPay.setRefBuyTransactionId(String.valueOf(order.getCartId()));

				if(map.get("outUuid") != null){
					coinPay.setExtraValue("openId",map.get("outUuid").toString());
				} else {
					coinPay.setExtraValue("openId",frontUser.getAuthKey());
				}
				coinPay.setOwnerId(frontUser.getOwnerId());
				Map<String,String>requestData = HttpUtils.getRequestDataMap(request);
				coinPay.setParameter(requestData);
				coinPay.setOwnerId(frontUser.getOwnerId());

				EisMessage payResult = 	coinPayProcessor.onPay(coinPay);
				if(payResult == null || payResult.getOperateCode() != TransactionStatus.success.getId()){
					logger.error("coin支付处理器[" + coinPayProcessor.getDesc() + "]支付未返回成功，无法完成支付，返回值是:" + payResult);

					map.put("message", new EisMessage(OperateResult.failed.getId(),"无法完成支付,支付系统返回:" + payResult.getTitle()));
					for(Item item : submitCart.values()){
						item.setCartId(order.getCartId());
						int rs2 = itemService.update(item);
						logger.debug("当前交易缓存模式为数据库模式，更新数据库中的购买订单[" + item.getTransactionId() + "]:" +  rs2);
						if(rs2 == 1){
							messageService.sendJmsDataSyncMessage(null, "itemService", "update", item);
						}

					}
					return settleUpView;
				}
				coinPayed = true;
				map.put("coinPayed", coinPayed);
				Pay coinPayMsg = coinPay.clone();
				//向系统中发送一个等待支付状态的支付订单，以便于后面的结束
				logger.debug("向系统发送coin支付订单[" + coinPayMsg.getTransactionId() + "]的克隆消息，状态设置为处理中");
				coinPayMsg.setCurrentStatus(TransactionStatus.inProcess.id);
				coinPayMsg.setInviter(frontUser.getInviter());
				payService.insert(coinPayMsg);
				order.setExtraValue(DataName.coinPaid.toString(), "true");

			}
			//map.put("message", new EisMessage(EisError.moneyNotEnough.getId(), "用户资金余额不足"));
			//return "cart/error";	
		}
		/*if(needPoint > money.getPoint()){
			logger.info("用户[" + frontUser.getUuid() + "]账户point:" + money.getPoint() + ",低于结算单所需point[" + needPoint + "]");
			map.put("message", new EisMessage(EisError.moneyNotEnough.getId(), "用户资金余额不足"));
			return "cart/error";	
		}*/

		boolean forceUseGiftMoneyFirst = configService.getBooleanValue(DataName.forceUseGiftMoneyFirst.toString(), frontUser.getOwnerId());

		float askUseCouponMoney = order.getFloatExtraValue(DataName.askUseCouponMoney.toString());

		if(needMoney > 0){
			if(askUseCouponMoney > 0){
				if(askUseCouponMoney > needMoney){
					logger.error("订单[" + order.getCartId() + "]实际应支付:" + needMoney + ",但用户要求使用优惠券:" + askUseCouponMoney);
					map.put("message", new EisMessage(EisError.COUNT_LIMIT_EXCEED.getId(),"优惠券使用金额已超过订单金额"));
					return settleUpView;	
				}
			}
			needMoney = needMoney - askUseCouponMoney;				
			order.getMoney().setChargeMoney(needMoney);

			if(forceUseGiftMoneyFirst){
				float useGiftMoney = 0f;
				if(money.getGiftMoney() > 0){
					if(money.getGiftMoney() >= needMoney){
						useGiftMoney = needMoney;
					} else {
						useGiftMoney = money.getGiftMoney();
					}
					order.getMoney().setGiftMoney(useGiftMoney);
					needMoney -= useGiftMoney;
				}
				logger.debug("当前系统强制优先使用giftMoney，当前用户[" + buyUuid + "]的giftMoney是:" + money.getGiftMoney() + ",共使用giftMoney=" + useGiftMoney);
			}


		}

		//logger.info("结算商品订单[cartId=" + orderId + ",tids=" + submitOrderIds + "],总件数是:" + unitCount + ",订单数量是:" + submitCart.size() + "/" + tids.length + ",需支付money:" + needMoney + ",coin:" + needCoin + ",point:" + needPoint + ",优惠券金额:" + askUseCouponMoney );

		if(needCoin > 0){
			order.getMoney().setCoin(needCoin);
		}
		if(needPoint > 0){
			order.getMoney().setPoint(needPoint);
		}
		Price orderPrice = new Price(cart.getOwnerId());
		int totalProduct  = 0;
		int totalGoods = 0;
		StringBuffer goodsDesc = new StringBuffer();
		StringBuffer sb = new StringBuffer();

		//是否使用匹配订单，并直接付款给匹配上的那个订单的用户
		boolean useMatchPay = false;
		for(Item t : submitCart.values()){
	
			OpEisObject product = stockService.getTargetObject(t.getObjectType(), t.getProductId());
			if(product == null){
				logger.error("找不到购物车中的商品:" + t.getProductId());
				map.put("message", new EisMessage(EisError.productNotExist.getId(),"找不到指定的产品"));
				return settleUpView;	
			}
			int availableCount = stockService.getAvaiableCount(t.getObjectType(), t.getProductId());
			if(availableCount == 0){
				logger.error("商品:" + t.getObjectType()  + "/" + t.getProductId() + "]数量为0，无法购买");
				map.put("message", new EisMessage(EisError.amountError.getId(),"商品数量不足"));
				return settleUpView;		
			}
			if(availableCount != -1){
				logger.debug("准备将产品[" + t.getObjectType()  + "/" + t.getProductId() + "]的数量[" + availableCount + "]-" + t.getCount());
				/*long lockAmount = productService.writeAmount(product, -t.getCount(), false);
				if(lockAmount < -1){
					logger.error("无法修改购物车中的商品:" + product.getProductId() + "/" + product.getProductCode() + "]数量-" + t.getCount() + ",修改返回是:" + lockAmount);
					map.put("message", new EisMessage(EisError.amountError.getId(),"商品数量不足"));
					return settleUpView;		
				}*/
			} else {
				logger.warn("商品[" + t.getObjectType()  + "/" + t.getProductId()  + "]数量是-1，视为无数量限制");
			}
			String validatorClass = product.getExtraValue(DataName.productValidator.toString());
			boolean matchPay = product.getBooleanExtraValue("matchPay");
			if(matchPay) {
				//只要有一个交易是matchPay，那这个订单都用matchPay
				useMatchPay = true;
			}
			if(StringUtils.isNotBlank(validatorClass)){
				ProductValidator productValidator = applicationContextService.getBeanGeneric(StringUtils.uncapitalize(validatorClass));
				if(productValidator == null){
					logger.error("产品[" + t.getProductId() + "]定义了校验器[" + validatorClass + "]但找不到该校验器");
				} else {
					int validateResult = productValidator.validate(Operate.settleUp.getCode(),t, request);
					if(validateResult != OperateResult.success.getId()){
						logger.warn("交易品无法通过校验[" + validateResult + "]");
						if(validateResult ==  EisError.COUNT_LIMIT_EXCEED.id){
							throw new EisException(validateResult, "您购买该产品的次数已超过限制，暂时无法购买");		

						} 
						if(validateResult ==  EisError.moneyNotEnough.id){
							throw new EisException(validateResult, "您的金钱不足，暂时无法购买");		
						}   if(validateResult ==  EisError.moneyRangeError.id){
							throw new EisException(validateResult, "您的金钱低于新用户赠送资金，暂时无法购买");		
						} 
					}	
				}

			}
			Item item = t.clone();
			item.setCurrentStatus(TransactionStatus.waitingPay.getId());
			goodsDesc.append(item.getName()).append(",");
			if(deliveryOrder != null){
				item.setInOrderId(deliveryOrder.getDeliveryOrderId() + "");
			}
			if(item.getRequestMoney() > 0){
				orderPrice.setMoney(orderPrice.getMoney() + item.getRequestMoney());
			}
			/*if(coinPayed){
				item.setFrozenMoney(0);
			} else*/ {
				if(item.getFrozenMoney() > 0){
					orderPrice.setCoin(orderPrice.getCoin() + item.getFrozenMoney());
				}
			}
			if(item.getSuccessMoney() > 0){
				orderPrice.setPoint(orderPrice.getPoint() + item.getSuccessMoney());
			}
			if(item.getInMoney() > 0){
				orderPrice.setScore(orderPrice.getScore() + (int)item.getInMoney());
			}
			totalProduct++;
			totalGoods += item.getCount();
			item.setCartId(order.getCartId());
			sb.append(item.getTransactionId()).append(",");
			logger.debug("deliveryOrder : " + deliveryOrder);

			int rs = itemService.update(item);
			logger.debug("当前交易缓存模式为数据库模式，更新数据库中的购买订单[" + item.getTransactionId() + "]:" +  rs);
			if(rs == 1){
				messageService.sendJmsDataSyncMessage(null, "itemService", "update", item);
			}

		}

		order.setPrice(orderPrice);
		order.setTotalProduct(totalProduct);
		order.setTotalGoods(totalGoods);
		order.setGoodsDesc(goodsDesc.toString().replaceAll(",$", ""));
		order.setCurrentStatus(TransactionStatus.waitingPay.id);
		order.setOrderType(CartCriteria.ORDER_TYPE_STORE);
		order.setTransactionIds(sb.toString().replaceAll(",$", "").split(","));
		order.setSyncFlag(0);
		order.setExtraValue("matchPay", String.valueOf(useMatchPay));
		cartService.update(order);



		map.put("order", order);
		/*for(String cartId : cart.keySet()){
			for(String buyId : orderIds){
				if(buyId.equalsIgnoreCase(cartId)){
					cart.get(cartId).setCurrentStatus(TransactionStatus.waitingPay.getId());
					logger.info("改变订单[" + cartId + "]状态:" + cart.get(cartId).getCurrentStatus() + "=>" + TransactionStatus.waitingPay.getId());
					//cartService.delete(frontUser.getUuid(), cartId);
				}
			}
		}*/
		float payMoney = 0;
		boolean needPay = false;
		BigDecimal bd = new BigDecimal(needMoney);
		bd = bd.setScale(CommonStandard.moneyRoundLength, CommonStandard.moneyRoundType);
		needMoney = bd.floatValue();
		logger.debug("需要的钱："+needMoney);
		if(needMoney <= 0){
			logger.debug("订单[" + order.getCartId() + "]需支付资金是0");
		} else if(forcePayWithoutAccountMoney){
			payMoney = needMoney ;
			needPay = true;
			logger.info("当前系统配置为不考虑账户余额，全额支付，需支付:" + payMoney);
			order.getMoney().setChargeMoney(payMoney);
		} else if( needMoney > (money.getChargeMoney() + money.getIncomingMoney())){
			payMoney = needMoney - money.getChargeMoney() - money.getIncomingMoney();

			order.getMoney().setChargeMoney(payMoney);
			order.getMoney().setTransitMoney(money.getChargeMoney() + money.getIncomingMoney());
			logger.info("用户[" + frontUser.getUuid() + "]账户充值资金:" + money.getChargeMoney() + ",收入资金:" + money.getIncomingMoney() + "，低于结算单所需资金[" + needMoney + "]，还需支付:" + payMoney);
			needPay = true;
		}

		float totalPayMoney = order.getMoney().getChargeMoney()  + order.getMoney().getTransitMoney();
		bd = new BigDecimal(totalPayMoney);
		bd = bd.setScale(CommonStandard.moneyRoundLength, CommonStandard.moneyRoundType);

		cart.setExtraValue("totalPayMoney", String.valueOf(bd.floatValue()));
		if(deliveryOrder != null && deliveryOrder.getFee() != null){
			cart.setExtraValue(DataName.deliveryFee.toString(), String.valueOf(deliveryOrder.getFee().getMoney()));
		}
		if(needPay){

			//如果该商品是p2p支付，尝试使用个人二维码匹配
			if(useMatchPay) {
				order.setCurrentStatus(TransactionStatus.waitingMatch.id);
				EisMessage eisMessage = new EisMessage();
				eisMessage.setOperateCode(Operate.create.getId());
				eisMessage.setAttachment(new HashMap<String, Object>());
				eisMessage.getAttachment().put("order", order);
				eisMessage.setObjectType(ObjectType.order.toString());
				messageService.send(null, eisMessage);
				map.put("message", new EisMessage(OperateResult.waiting.getId(),"请稍等，正在返回支付方式"));
				return settleUpView;	
			}
			Pay pay = new Pay();
			pay.setOwnerId(frontUser.getOwnerId());
			pay.setPayFromAccount(buyUuid);
			pay.setPayToAccount(buyUuid);
			pay.setPayToAccountType(UserTypes.frontUser.getId());
			pay.setPayFromAccountType(UserTypes.frontUser.getId());
			pay.setName(moneyBuyName);
			pay.setFaceMoney(payMoney);
			//			pay.setRefBuyTransactionId(moneyBuyOrderIds.replaceAll(",$", ""));
			pay.setRefBuyTransactionId(String.valueOf(order.getCartId()));
			pay.setStartTime(new Date());
			pay.setSign(DigestUtils.md5Hex(UUID.randomUUID().toString()));
			pay.setContextType(ContextTypeUtil.getContextType(request));
			pay.setExtraValue("toAccountName",toAccountName);
			pay.setExtraValue("fromAccountName",toAccountName);
			pay.setExtraValue("internal", String.valueOf(ServletRequestUtils.getBooleanParameter(request, "internal")));
			if(map.get("outUuid") != null){
				coinPay.setExtraValue("openId",map.get("outUuid").toString());
				pay.setExtraValue("openId",map.get("outUuid").toString());
			} else {
				coinPay.setExtraValue("openId",frontUser.getAuthKey());
				pay.setExtraValue("openId",frontUser.getAuthKey());
			}

			pay.setPayTypeId(payTypeId);

			/*boolean isWeixinAccess = AgentUtils.isWeixinAccess(request.getHeader("user-agent"));
			if(isWeixinAccess){
				pay.setExtraValue("payTypeName", "JSAPI");
			} else {
				pay.setExtraValue("payTypeName", "NATIVE");
			}*/
			Map<String,String>requestData = HttpUtils.getRequestDataMap(request);
			pay.setParameter(requestData);
			pay.setOwnerId(frontUser.getOwnerId());
			pay.setInviter(frontUser.getInviter());
			String payNotifyTemplate = configService.getValue(DataName.payNotifyUrl.toString(), pay.getOwnerId());
			if(payNotifyTemplate == null){
				payNotifyTemplate = CommonStandard.DEFAULT_PAY_NOTIFY_TEMPLATE;
			}
			payNotifyTemplate = payNotifyTemplate.replaceAll("\\$\\{hostUrl\\}", HttpUtils.generateUrlPrefix(request));
			pay.setNotifyUrl(payNotifyTemplate);
			String payReturnUrl = configService.getValue(DataName.payReturnUrl.toString(), pay.getOwnerId());
			if(StringUtils.isNotBlank(payReturnUrl)){
				payReturnUrl = payReturnUrl.replaceAll("\\$\\{hostUrl\\}", HttpUtils.generateUrlPrefix(request));
				pay.setReturnUrl(payReturnUrl);
			}
			
			

			EisMessage payMessage = payService.begin(pay);
			logger.debug("请求开始支付返回的结果是:" + payMessage);
			map.put("message", payMessage);
			map.put("payTransactionId", pay.getTransactionId());
			if(payMessage!=null && payMessage.getOperateCode() == Operate.post.id){
				logger.info("当前订单[" + pay.getTransactionId() + "]需要post 提交支付信息");
				map.put("postData", payMessage.getAttachmentData("postData"));
				map.put("url", payMessage.getMessage());
				return postView;
			}
		} else {
			if(needMoney <= 0){
				logger.info("当前订单[" + submitOrderIds + "]不需要真实资金付款，直接提交订单");
				map.put("message", new EisMessage(OperateResult.accept.getId(),"订单已提交，请稍候查看结果"));
			} else {
				logger.info("用户[" + buyUuid + "]账户充值资金:" + money.getChargeMoney() + ",收入资金:" + money.getIncomingMoney() + "，高于结算单所需资金[" + needMoney + "]，直接提交订单");
				map.put("message", new EisMessage(OperateResult.accept.getId(),"您的账户余额足够，订单已直接提交"));
			}
			order.setCurrentStatus(TransactionStatus.waitingMinusMoney.id);
			EisMessage eisMessage = new EisMessage();
			eisMessage.setOperateCode(Operate.create.getId());
			eisMessage.setAttachment(new HashMap<String, Object>());
			eisMessage.getAttachment().put("order", order);
			eisMessage.setObjectType(ObjectType.order.toString());
			messageService.send(null, eisMessage);
			if(coinPayed){
				coinPay.setRefBuyTransactionId(String.valueOf(order.getCartId()));
				logger.debug("订单只需要使用coin支付并且已经支付成功，向后端发送结束支付订单[" + coinPay.getTransactionId() + "]消息");
				map.put("message", new EisMessage(TransactionStatus.success.getId(),"您的订单已支付成功。"));
				EisMessage m = new EisMessage();
				m.setOperateCode(Operate.close.getId());
				m.setAttachment(new HashMap<String,Object>());
				m.getAttachment().put("pay", coinPay);	
				m.setObjectType(ObjectType.pay.toString());
				messageService.send(null, m);
			}
			return settleUpView;	
		}
		logger.debug("submitCart ：" + submitCart);
		map.put("cart",submitCart);
		/*PayTypeCriteria payType = new PayTypeCriteria(cart.getOwnerId());
		List<PayType> payTypeList = payTypeService.list(payType);
		map.put("payTypeList", payTypeList);
		logger.debug("支付方式提供"+payTypeList==null?"0":payTypeList.size()+"种");*/
		return settleUpView;

	}


}
