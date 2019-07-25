package com.maicard.wpt.service.chaoka;


import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maicard.annotation.ProcessMessageObject;
import com.maicard.product.service.StockService;
import com.maicard.product.service.TransactionExecutor;
import com.maicard.common.base.BaseService;
import com.maicard.common.base.Criteria;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.domain.OpEisObject;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.CookieService;
import com.maicard.common.service.SiteDomainRelationService;
import com.maicard.common.service.UuidService;
import com.maicard.common.util.JsonUtils;
import com.maicard.common.util.NumericUtils;
import com.maicard.ec.service.DeliveryOrderService;
import com.maicard.exception.EisException;
import com.maicard.mb.service.EisMessageListener;
import com.maicard.mb.service.MessageService;
import com.maicard.mb.service.UserMessageService;
import com.maicard.money.criteria.CouponModelCriteria;
import com.maicard.money.domain.Coupon;
import com.maicard.money.domain.CouponModel;
import com.maicard.money.domain.Money;
import com.maicard.money.domain.Pay;
import com.maicard.money.service.CouponModelService;
import com.maicard.money.service.CouponProcessor;
import com.maicard.money.service.CouponService;
import com.maicard.money.service.MoneyService;
import com.maicard.money.service.PayService;
import com.maicard.product.service.ActivityProcessor;
import com.maicard.product.service.ActivityService;
import com.maicard.product.service.BusinessProcessor;
import com.maicard.product.service.CartService;
import com.maicard.product.service.ItemLogService;
import com.maicard.product.service.ItemService;
import com.maicard.product.criteria.ActivityCriteria;
import com.maicard.product.criteria.ItemCriteria;
import com.maicard.product.domain.Activity;
import com.maicard.product.domain.Cart;
import com.maicard.product.domain.Item;
import com.maicard.security.domain.User;
import com.maicard.security.service.FrontUserService;
import com.maicard.security.service.UserRelationService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.ObjectType;
import com.maicard.standard.Operate;
import com.maicard.standard.OperateResult;
import com.maicard.standard.TransactionStandard.TransactionStatus;
import com.maicard.standard.TransactionStandard.TransactionType;

/**
 * 虚拟点卡交易处理器
 *
 */
@Service
@ProcessMessageObject({"item","order"})
public class CardTransactionExecutorImpl extends BaseService implements TransactionExecutor,EisMessageListener{

	@Resource
	private ActivityService activityService;

	@Resource
	private ApplicationContextService applicationContextService;
	@Resource
	private CartService cartService;
	@Resource
	private ConfigService configService;
	@Resource
	private CouponService couponService;
	@Resource
	private CouponModelService couponModelService;
	@Resource
	private FrontUserService frontUserService;
	@Resource
	private ItemService itemService;
	@Resource
	private ItemLogService itemLogService;
	@Resource
	private MessageService messageService;
	@Resource
	private MoneyService moneyService;
	@Resource
	private StockService stockService;
	@Resource
	private UuidService uuidService;
	@Resource
	private SiteDomainRelationService siteDomainRelationService;
	@Resource
	private PayService payService;
	@Resource
	private UserRelationService userRelationService;
	private boolean handlerItem;
	@Resource
	private CookieService cookieService;
	@Resource
	private DeliveryOrderService deliveryOrderService;
	@Resource
	private UserMessageService userMessageService;
	//private int processAbortId = TransactionStatus.failed.getId();

	@PostConstruct
	public void init(){
		handlerItem = configService.getBooleanValue(DataName.handlerItem.toString(),0);
	}


	@Override
	public EisMessage begin(Object object) throws Exception{
		Assert.notNull(object);
		if(object instanceof Cart){
			logger.debug("接受Object为Cart的步骤");
			_beginCart((Cart)object);
			return null;
		}
		if(object instanceof Pay){
			logger.debug("接受Object为Pay的步骤");
			_beginPay((Pay)object);
			return null;
		}
		return null;
	}



	private void _beginPay(Pay pay) {

		Money money = new Money();
		money.setUuid(pay.getPayToAccount());
		money.setChargeMoney(pay.getRealMoney());
		User frontUser = frontUserService.select(money.getUuid());

		String[] transactionIds = pay.getRefBuyTransactionId().split(",");
		Cart cart = null;
		int successFindItemCount = 0;			
		if(StringUtils.isBlank(pay.getRefBuyTransactionId())){
			logger.error("支付订单[" + pay.getTransactionId() + "]没有对应的交易");
		}
		if(pay.getRefBuyTransactionId().length() <= 20 && NumericUtils.isNumeric(pay.getRefBuyTransactionId())){
			//是一个cart订单
			cart = cartService.select(Integer.parseInt(pay.getRefBuyTransactionId()));
			if(cart == null){
				logger.error("支付订单[" + pay.getTransactionId() + "]所对应的cart[" + pay.getRefBuyTransactionId() + "]不存在");
			} else if(cart.getCurrentStatus() != TransactionStatus.waitingPay.id
					&& cart.getCurrentStatus() != TransactionStatus.waitingMatch.id){
				logger.error("支付订单[" + pay.getTransactionId() + "]所对应的cart[" + pay.getRefBuyTransactionId() + "]状态不是等待支付或等待匹配，是:" + cart.getCurrentStatus());
			} else if(cart.getTransactionIds() == null || cart.getTransactionIds().length < 1){
				logger.warn("支付订单[" + pay.getTransactionId() + "]所对应的cart[" + pay.getRefBuyTransactionId() + "]没有关联任何交易");

			} else {
				transactionIds = cart.getTransactionIds();
				logger.debug("支付订单对应的订单号："+transactionIds);
			}

			/*if(cart != null){
				logger.debug("计算用户完成支付后的活动");
				calculatePaySuccessActivity(frontUser, money, cart);
			}*/
		} else {
			//已经不再支持Item直接支付
		}

		/*for(String tid : transactionIds){

			Item item = itemService.select(tid);

			if(item != null){
				logger.info("从数据库或缓存中锁定了对应订单[" + tid + ",currentStatus=" + item.getCurrentStatus() + "]");
				successFindItemCount++;
			} else {
				logger.warn("没有从系统缓存或数据库中找到订单[" + tid + "]或者其状态异常");
			} 

		}*/
		if(cart != null){
			logger.debug("开始购物车结算步骤");
			this._beginCart(cart);
			return;

		}
		if(successFindItemCount == 0 || successFindItemCount != transactionIds.length){
			logger.error("找不到对应的购买订单["  + pay.getRefBuyTransactionId() + "],通知其他节点用户[" + pay.getPayFromAccount() + "]增加充值资金[" + money.getChargeMoney() + "]");
			messageService.sendJmsDataSyncMessage(null, "moneyService", "plusLocal", money);
		} 
	}


	private EisMessage _beginCart(Cart cart) {

		logger.debug("进入结算_beginCart步骤:" + JsonUtils.toStringFull(cart));
		if(cart.getCurrentStatus() == TransactionStatus.waitingMatch.id) {
			return this._beginMatchPay(cart);
			
		}
		if(cart.getMoney() == null){
			logger.error("用户订单[" + cart.getCartId() + "]没有资金，锁定用户所有资金");
			lockAllMoney(cart.getUuid(), cart.getOwnerId());
			return new EisMessage(EisError.moneyRangeError.getId(),"用户订单[" + cart.getCartId() + "]没有资金，锁定用户所有资金");
		}
		if(cart.getMoney() != null){
			logger.debug("开始处理购买订单[" + cart.getCartId() + "],订单即时付款金额:" + cart.getMoney().getChargeMoney() + ",需扣除账户余额:" + cart.getMoney().getTransitMoney() + ",请求优惠券金额:" + cart.getMoney().getGiftMoney() + ",详细:" + cart.getMoney());
		} 

		//要求使用的优惠券ID列表
		String couponIds = cart.getExtraValue("couponIds");
		float lockedCouponMoney = 0;		//成功核销锁定的优惠券总金额
		float remainCouponMoney = 0; 	//订单完成后，还能剩下的卡券金额，应放入账户的marginMoney
		float willUseCouponMoney = 0; //订单将会使用的卡券金额，如果交易失败，应放回账户的marginMoney

		if(couponIds != null){
			logger.debug("订单[" + cart.getCartId() + "]有优惠券信息，将使用优惠券:" + couponIds);
			String[] couponIdData = couponIds.split(",");
			if(couponIdData == null || couponIdData.length < 1){
				logger.debug("订单[" + cart.getCartId() + "]有优惠券信息，但订单数据异常:" + couponIds);
			} else {
				for(String couponData : couponIdData){
					String[] data = couponData.split("#");
					if(data == null){
						logger.error("订单[" + cart.getCartId() + "]的优惠券信息异常:" + couponData);
					} else if (!NumericUtils.isNumeric(data[0])){
						logger.error("订单[" + cart.getCartId() + "]的优惠券信息异常:" + couponData);
					} else {
						long couponId = Long.parseLong(data[0]);
						Coupon coupon = couponService.select(couponId);
						if(coupon == null){
							logger.error("本地数据库找不到订单[" + cart.getCartId() + "]指定的优惠券:" + couponId);
							continue;
						}
						if(coupon.getCouponType() == null || coupon.getCouponType().equals(CouponModelCriteria.COUPON_CHARGE_MONEY)){
							logger.error("优惠券[" + coupon.getCouponId() + "]类型为空或者不是充值类型:" + coupon.getCouponType());
							continue;
						}
						CouponModel couponModel = couponModelService.select(coupon.getCouponModelId());
						if(couponModel == null){
							logger.error("找不到优惠券[" + coupon.getCouponId() + "]对应的产品:" + coupon.getCouponCode());
							continue;
						} 
						String beanName = null;
						if(couponModel.getProcessor() == null){
							logger.error("指定的卡券产品[" + couponModel.getCouponCode() + "]没有指定处理器");
							continue;
						}  
						beanName = couponModel.getProcessor();
						Object object = applicationContextService.getBean(beanName);
						if(object == null){
							logger.error("找不到指定的卡券[" + couponModel.getCouponModelId() + "]处理器:" + beanName);
						} else if(!( object instanceof CouponProcessor)){
							logger.error("指定的卡券[" + couponModel.getCouponModelId() + "]处理器:" + beanName + ",类型不是CouponProcessor,是:" + object.getClass().getName());
						}
						CouponProcessor couponProcessor = (CouponProcessor)object;
						int rs = couponProcessor.consume(coupon);
						if(rs != OperateResult.success.getId()){
							logger.error("无法使用卡券[" + coupon.getCouponId() + "]，返回值:" + rs);
						} else {
							logger.debug("卡券使用成功，金额:" + coupon.getGiftMoneyDesc());
							lockedCouponMoney += coupon.getGiftMoney().getGiftMoney();
						}
					}
				}
			}

			//用户请求使用多少优惠券，比如用户提供了2张100的优惠券，但是只要求使用150元。
			float askUseCouponMoney = cart.getMoney().getGiftMoney();
			if(askUseCouponMoney > 0){
				if(askUseCouponMoney > lockedCouponMoney){
					//用户要求使用的优惠券已经超过了实际优惠券的总金额
					boolean haltWhenAskCouponMoneyExceedReal = configService.getBooleanValue(DataName.haltWhenAskCouponMoneyExceedReal.toString(),cart.getOwnerId());
					logger.error("订单[" + cart.getCartId() + "]总卡券金额是:" + lockedCouponMoney + ",但用户要求使用:" + askUseCouponMoney);
					if(haltWhenAskCouponMoneyExceedReal){
						return new EisMessage(EisError.moneyNotEnough.getId(),"用户订单[" + cart.getCartId() + "]总卡券金额是:" + lockedCouponMoney + ",但用户要求使用:" + askUseCouponMoney);
					}
					willUseCouponMoney = lockedCouponMoney;
				} else {
					willUseCouponMoney = askUseCouponMoney;
				}

			}
			remainCouponMoney = lockedCouponMoney - askUseCouponMoney;
			logger.error("订单[" + cart.getCartId() + "]总卡券金额是:" + lockedCouponMoney + ",用户要求使用:" + askUseCouponMoney + ",本次将使用:" + willUseCouponMoney + ",将剩余:" + remainCouponMoney);

		}

		//用户当前的资金账户
		Money existMoney = moneyService.select(cart.getUuid(), cart.getOwnerId());
		logger.debug("用户当前的资金账户"+existMoney);
		if(existMoney == null){
			existMoney = new Money(cart.getUuid(), cart.getOwnerId());
		}
		//对订单的资金做一个clone，以防止对原有对象做修改
		Money cartMoney = cart.getMoney().clone();
		cartMoney.setOwnerId(cart.getOwnerId());
		/**
		 * cart中的chargeMoney标记的是用户即时网银付款金额，transitMoney标记的是付款前账户中的余额chargeMoney+incomingMoney		
		 * 在一个订单中的money对象，chargeMoney是用户即时支付的资金，transitMoney是用户之前在现金账户(即系统chargeMoney)上的余额
		 * 因此要从用户账上扣除的现金是二者相加
		 */
		float needMinusCash = cartMoney.getChargeMoney() + cartMoney.getTransitMoney();
		//	float _oldGiftMoney = existMoney.getGiftMoney();
		//	boolean forceUseGiftMoneyFirst = configService.getBooleanValue(DataName.forceUseGiftMoneyFirst.toString(), cart.getOwnerId());
		/*if(forceUseGiftMoneyFirst || cart.getBooleanExtraValue(DataName.forceUseGiftMoneyFirst.toString())){

		}*/
		if(cartMoney.isAllZero()){
			logger.error("用户订单[" + cart.getCartId() + "]资金中的数据全部为空，锁定用户所有资金");
			lockAllMoney(cart.getUuid(), cart.getOwnerId());
			return new EisMessage(EisError.moneyRangeError.getId(),"用户订单[" + cart.getCartId() + "]资金中的数据全部为空，锁定用户所有资金");
		}
		//扣款则需要扣除订单数据中的chargeMoney+transitMoney，即扣除用户网银付款资金+账户余额资金
		cartMoney.setChargeMoney(needMinusCash);
		cartMoney.setTransitMoney(0);
		logger.debug("当前交易需要锁定用户资金:" + cartMoney);
		boolean coinPaid = 	cart.getBooleanExtraValue(DataName.coinPaid.toString());

		if(coinPaid){
			logger.debug("当前交易已经支付了coin部分，将资金对象的coin设置为0");
			cartMoney.setCoin(0);
		}
		Money unlockMoney = cartMoney.clone();

		/*
		 * 用来向前端直接发送扣款命令的金额，不包含即时冲入的现金，因为这笔现金根本没有发送到前端
		 * transitMoney在订单支付时表示账户现金余额（付款前），因此此时应扣除这个transitMoney并将其设置为0’
		 */
		Money remoteMinusMoney = cartMoney.clone();
		remoteMinusMoney.setChargeMoney(remoteMinusMoney.getTransitMoney());
		remoteMinusMoney.setTransitMoney(0);

		//FIXME 忘记为什么要这么操作了
		/*if(willUseCouponMoney > 0){
			cartMoney.setCoin(cartMoney.getCoin() - willUseCouponMoney);
			logger.debug("订单[" + cart.getCartId() + "]准备使用卡券金额:" + willUseCouponMoney + ",资金对象中的coin减去对应金额，减去后coin=" + cartMoney.getCoin() );
		}*/



		boolean needMinusMoneyAfterSuccess = false;
		Money minusMoney = new Money(cartMoney.getUuid(), cartMoney.getOwnerId());
		if(cartMoney.getChargeMoney() > 0 || cartMoney.getGiftMoney() > 0){
			needMinusMoneyAfterSuccess = true;
			minusMoney.setFrozenMoney(needMinusCash + cartMoney.getGiftMoney());
			//minusMoney.setChargeMoney(needMinusCash);
			//minusMoney.setGiftMoney(money.getGiftMoney());
		}

		if(cartMoney.isAllZero() && coinPaid){
			//可能是纯coin订单并且已经完成了支付

			logger.info("当前订单[" + cart.getCartId() + "]需要锁定的资金为0，并且已经在前台支付成功");
		} else {

			EisMessage moneyLockResult = moneyService.lock(cartMoney);
			if(moneyLockResult == null){
				logger.error("订单[" + cart.getCartId() + "]无法锁定用户[" + cartMoney.getUuid() + "]的资金，锁定返回空:最新资金:" + cartMoney + ",将使用的卡券金额退回到marginMoney[将用资金:" + willUseCouponMoney + ",未用资金:" + remainCouponMoney);
				unlockMoney = new Money(cart.getUuid(), cart.getOwnerId());
				unlockMoney.setMarginMoney(willUseCouponMoney + remainCouponMoney);
				moneyService.unLock(unlockMoney);
				return new EisMessage(EisError.moneyLockFail.getId(),"无法锁定用户[" + cartMoney.getUuid() + "]的资金，锁定返回空:最新资金:" + cartMoney);
			}
			if(moneyLockResult.getOperateCode() != OperateResult.success.getId()){
				logger.error("订单[" + cart.getCartId() + "]无法锁定用户[" + cartMoney.getUuid() + "]的资金，锁定返回不是成功，是:" + moneyLockResult.getOperateResult() + ",最新资金:" + cartMoney + ",将使用的卡券金额退回到marginMoney[将用资金:" + willUseCouponMoney + ",未用资金:" + remainCouponMoney);
				unlockMoney = new Money(cart.getUuid(), cart.getOwnerId());
				unlockMoney.setMarginMoney(willUseCouponMoney + remainCouponMoney);
				moneyService.unLock(unlockMoney);
				return new EisMessage(EisError.moneyLockFail.getId(),"无法锁定用户[" + cartMoney.getUuid() + "]的资金，锁定返回不是成功，是:" + moneyLockResult.getOperateResult() + ",最新资金:" + cartMoney);
			}
			//扣除之前锁定的资金
			if(needMinusMoneyAfterSuccess){
				logger.debug("需要将用户[" + minusMoney.getUuid() + "]的锁定资金扣除:" + minusMoney.getFrozenMoney() + ",并向前端发送直接扣除命令，扣除资金:" + remoteMinusMoney);
				moneyService.minus(minusMoney);
				logger.debug("扣除之前锁定的资金");
				//messageService.sendJmsDataSyncMessage(null, "moneyService", "minusLocal", remoteMinusMoney);
			}
		}
		logger.debug("断点1");
		int successStatus = TransactionStatus.success.id;
		if(cart.getDeliveryOrderId() > 0){
			successStatus = TransactionStatus.preDelivery.id;
		}
		cart.setCurrentStatus(successStatus);
		cart.setEndTime(new Date());


		//如果订单未拆分，正常执行
		logger.debug("如果订单未拆分，正常执行");
		cartService.update(cart);
		if(cart.getTransactionIds() != null && cart.getTransactionIds().length > 0){
			for(String tid : cart.getTransactionIds()){
				Item item = itemService.selectSimple(tid);
				if(item == null){
					logger.error("找不到订单[" + cart.getCartId() + "]对应的交易:" + tid);
					continue;
				}
				OpEisObject targetObject = stockService.getTargetObject( item.getObjectType(), item.getProductId());

				if(targetObject == null){
					logger.error("找不到交易[" + item.getTransactionId() + "]对应的产品:" + item.getProductId());
					continue;
				} 
				String processorName = targetObject.getExtraValue("processor");
				if(StringUtils.isBlank(processorName)) {
					logger.error("交易[" + item.getTransactionId() + "]对应的产品:" + item.getObjectType() + "#" + item.getProductId() + "未定义业务处理器");
					continue;
				}
				BusinessProcessor  bp = applicationContextService.getBeanGeneric(processorName);
				if(bp == null){
					logger.error("交易[" + item.getTransactionId() + "]对应的产品:" + item.getProductId() + ",找不到指定的业务处理器:" + processorName);
					continue;
				}
				EisMessage chargeResult = bp.startTx(item);

				logger.debug("交易[" + item.getTransactionId() + "]交给业务处理器[" + bp.getClass().getName() + "]的处理结果:" + chargeResult);
				if(chargeResult == null || chargeResult.getOperateCode() != OperateResult.success.id ){
					item.setCurrentStatus(TransactionStatus.failed.id);
				} else {
					//TODO? 删除这个购买订单
					item.setCurrentStatus(chargeResult.getOperateCode());
					//把得到的卡密放到订单中
					List<Item> cardList = chargeResult.getAttachmentData("itemList");
					logger.info("从业务处理器中获取到的itemList=" + JsonUtils.toStringFull(cardList));
					if(cardList != null) {
						StringBuffer sb = new StringBuffer();
						for(Item i : cardList) {
							i.setCartId(cart.getCartId());
							if(i.getTransactionTypeId() == TransactionType.stock.id) {
								sb.append(i.getExtraValue(DataName.productSerialNumber.name())).append(",").append(i.getExtraValue(DataName.productPassword.name())).append("\n");
							}
							itemService.insert(i);
						}
						logger.info("放入卡密数据到订单:" + sb.toString());
						cart.setExtraValue("cardData", sb.toString());
					}
					cart.setCurrentStatus(TransactionStatus.success.id);
					cart.setSyncFlag(0);
					cartService.update(cart);
				} 
				int rs = itemService.changeStatus(item);
				if(rs == 1){
					messageService.sendJmsDataSyncMessage(null, "itemService", "update", item);
				}
			}
		}



		if(remainCouponMoney > 0){
			logger.debug("订单[" + cart.getCartId() + "]处理成功，退回多出的卡券金额:" + remainCouponMoney);
			unlockMoney = new Money(cart.getUuid(), cart.getOwnerId());
			unlockMoney.setCoin(remainCouponMoney);
			moneyService.unLock(unlockMoney);
			messageService.sendJmsDataSyncMessage(null, "moneyService", "plusLocal", unlockMoney);
			logger.debug("断点5");
		}
		/*UserMessage sms = new UserMessage(cart.getOwnerId());
		if (cart.getDeliveryOrderId() > 0) {
			DeliveryOrder deliveryOrder = deliveryOrderService.select(cart.getDeliveryOrderId());
			if (deliveryOrder != null ) {
				sms.setPerferMethod(new String[]{UserMessageSendMethod.sms.toString()});
				logger.debug("订单有配送地址,向用户手机发送短信");
				if (deliveryOrder.getMobile() != null) {
					String smsTemplate = configService.getValue("notifyDeliverGoodsMessage", cart.getOwnerId());
					if(smsTemplate != null){

						sms.setReceiverName(deliveryOrder.getMobile());
						sms.setCurrentStatus(MessageStatus.queue.id);
						sms.setTitle("1");
						sms.setSign("deliveryNotify");
						sms.setContent(smsTemplate);
						int rs = userMessageService.send(sms);
						logger.debug("短信下发到[手机" + deliveryOrder.getMobile() + ",内容:" + smsTemplate + "]，消息服务返回的是:" + rs);

					}

				}
			}

		}*/
		return new EisMessage(OperateResult.success.getId(),"订单[" + cart.getCartId() + "已经处理成功");

	}

	/**
	 * 针对需要匹配寄售订单的个人二维码
	 * @param cart
	 * @return
	 */
	private EisMessage _beginMatchPay(Cart cart) {
		String tid = cart.getTransactionIds()[0];
		Item buyItem = itemService.select(tid);
		if(buyItem == null) {
			throw new EisException(EisError.OBJECT_IS_NULL.id,"找不到订单:" + cart.getCartId() + "对应的物品:" + tid);
		}
		
		ItemCriteria itemCriteria = new ItemCriteria(cart.getOwnerId());
		//匹配购买订单
		itemCriteria.setTransactionTypeId(TransactionType.buy.id);
		itemCriteria.setCurrentStatus(TransactionStatus.newOrder.id);
		itemCriteria.setNeedRequestMoney(buyItem.getRequestMoney());
		
		List<Item> list = itemService.list(itemCriteria);
		
		
		return null;
	}


	//在资金异常时，锁定用户所有资金
	private void lockAllMoney(long uuid, long ownerId) {
		Money money = moneyService.select(uuid, ownerId);
		if(money == null){
			logger.info("尝试锁定用户[" + uuid + "]的所有资金但用户没有资金账户");
			return;
		}
		EisMessage lockResult = moneyService.lock(money);
		logger.info("尝试锁定用户[" + uuid + "]的所有资金[" + money + ",结果:" + lockResult.getOperateCode());



	}


	@Async
	@Override
	public EisMessage end(Object object) throws Exception{
		return null;
	}

	@Override
	public void onMessage(EisMessage eisMessage) {
		logger.debug("交易处理器收到消息");
		if(!handlerItem){
			logger.debug("本节点不负责处理item业务");
			eisMessage = null;
			return;
		}
		if(eisMessage == null){
			logger.error("得到的消息是空");
			eisMessage = null;
			return;
		}
		if(eisMessage.getObjectType() == null){
			eisMessage = null;
			return;
		}
		if(!eisMessage.getObjectType().equals(ObjectType.item.toString()) &&  !eisMessage.getObjectType().equals(ObjectType.order.toString()) ){
			logger.error("得到的消息处理类型不是item也不是order");
			eisMessage = null;
			return ;
		}
		if(eisMessage.getAttachment() == null){
			logger.debug("消息中没有附件");
			eisMessage = null;
			return;
		}


		Cart item = null;
		if(eisMessage.getAttachment().get("order") != null){
			Object object = eisMessage.getAttachment().get("order");
			if(object instanceof Cart){
				item = (Cart)object;
			} else if(object instanceof LinkedHashMap){
				String textData = null;
				ObjectMapper om = JsonUtils.getInstance();
				try{
					textData = om.writeValueAsString(object);
					item = om.readValue(textData, Cart.class);
				}catch(Exception e){}

			}
		}

		if(eisMessage.getOperateCode() == Operate.create.getId()){
			if(item == null){
				logger.warn("消息中没有找到需要的对象item");
				eisMessage = null;
				return;
			}
			try {
				this.begin(item);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		eisMessage = null;
		return;

	}


	@Override
	public EisMessage lock(Criteria itemCriteria) {
		return null;
	}


	@Override
	public void unLock(Object... item) {

	}

	//计算用户完成支付后的活动
	private void calculatePaySuccessActivity(User frontUser, Money money, Cart cart) {

		ActivityCriteria activityCriteria = new ActivityCriteria(cart.getOwnerId());
		activityCriteria.setActivityType(ActivityCriteria.ACTIVITY_TYPE_PAY_SUCCESS);
		activityCriteria.setCurrentStatus(BasicStatus.normal.getId());
		List<Activity> activityList = activityService.list(activityCriteria);
		if(activityList == null || activityList.size() < 1){
			logger.debug("当前没有定义支付成功活动");
		} else {
			logger.debug("当前定义了" + activityList.size() + "个支付成功活动");
			for(Activity activity : activityList){
				if(activity.getProcessor() == null){
					logger.warn("支付成功活动[" + activity.getActivityId() + "/" + activity.getActivityCode() + "]对应的处理器是空");
					continue;
				}
				ActivityProcessor p = applicationContextService.getBeanGeneric(activity.getProcessor());
				if(p == null){
					logger.warn("支付成功活动[" + activity.getActivityId() + "/" + activity.getActivityCode() + "]对应的处理器[" + activity.getProcessor() + "]找不到或者不是ActivityProcessor类型");
					continue;
				}
				p.execute("pay_success", activity, frontUser, cart);
			}
		}
	}

	/*//修改用户VIP状态
	private void updateVipUser(User frontUser,Product product,String vipType){
		logger.debug("进入VIP订购修改方法");
//		frontUser.setLevel(2);
		try {
			frontUserService.update(frontUser);
			//更新服务器中frontuser缓存

		} catch (Exception e) {
			logger.error("用户更新Vip数据level失败");
			return;
		}
		UserRelationCriteria userRelation = new UserRelationCriteria();
		userRelation.setUuid(frontUser.getUuid());
		userRelation.setObjectId(product.getProductId());
		userRelation.setObjectType("vip");
		userRelation.setOwnerId(frontUser.getOwnerId());
		List<UserRelation> list = userRelationService.list(userRelation);

		if(list!=null && list.size()==1){

			logger.debug("UserRelation中存在对应的数据,执行更新");
			UserRelation userRel = list.get(0);
			userRel.setCreateTime(new Date());
			userRel.setObjectId(product.getProductId());
			userRel.setRelationType(vipType);
			userRel.setCurrentStatus(2);
			userRelationService.update(userRel);
		}else{
			logger.debug("UserRelation中没有对应的数据,执行添加");
			UserRelation userR = new UserRelation();
			userR.setUuid(frontUser.getUuid());
			userR.setObjectId(product.getProductId());
			userR.setObjectType("vip");
			userR.setOwnerId(frontUser.getOwnerId());
			userR.setRelationType(vipType);
			userR.setCreateTime(new Date());
			userR.setCurrentStatus(2);
			userRelationService.insert(userR);
		}
	}*/
}
