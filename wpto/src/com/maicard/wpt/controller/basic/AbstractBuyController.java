package com.maicard.wpt.controller.basic;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.ui.ModelMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maicard.common.base.BaseController;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.domain.OpEisObject;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.CenterDataService;
import com.maicard.common.service.GlobalOrderIdService;
import com.maicard.common.util.ContextTypeUtil;
import com.maicard.common.util.HttpUtils;
import com.maicard.common.util.JsonUtils;
import com.maicard.common.util.NumericUtils;
import com.maicard.common.util.StringTools;
import com.maicard.ec.criteria.AddressBookCriteria;
import com.maicard.ec.criteria.InvoiceCriteria;
import com.maicard.ec.domain.AddressBook;
import com.maicard.ec.domain.DeliveryOrder;
import com.maicard.ec.domain.Invoice;
import com.maicard.ec.service.AddressBookService;
import com.maicard.ec.service.DeliveryOrderService;
import com.maicard.ec.service.InvoiceService;
import com.maicard.exception.EisException;
import com.maicard.mb.service.MessageService;
import com.maicard.money.criteria.CouponCriteria;
import com.maicard.money.criteria.CouponModelCriteria;
import com.maicard.money.criteria.PayTypeCriteria;
import com.maicard.money.domain.Coupon;
import com.maicard.money.domain.CouponModel;
import com.maicard.money.domain.Money;
import com.maicard.money.domain.Pay;
import com.maicard.money.domain.PayType;
import com.maicard.money.domain.Price;
import com.maicard.money.iface.PayProcessor;
import com.maicard.money.service.CouponModelService;
import com.maicard.money.service.CouponProcessor;
import com.maicard.money.service.CouponService;
import com.maicard.money.service.MoneyService;
import com.maicard.money.service.PayService;
import com.maicard.money.service.PayTypeService;
import com.maicard.money.service.PriceService;
import com.maicard.product.criteria.CartCriteria;
import com.maicard.product.criteria.ItemCriteria;
import com.maicard.product.domain.Activity;
import com.maicard.product.domain.Cart;
import com.maicard.product.domain.Item;
import com.maicard.product.service.ActivityProcessor;
import com.maicard.product.service.ActivityService;
import com.maicard.product.service.CartService;
import com.maicard.product.service.ItemService;
import com.maicard.product.service.ProductValidator;
import com.maicard.product.service.StockService;
import com.maicard.security.domain.User;
import com.maicard.security.service.AuthorizeService;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.FrontUserService;
import com.maicard.security.service.PartnerService;
import com.maicard.security.service.UuidMapService;
import com.maicard.standard.BasicStatus;
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


/**
 * 实体电商/电商购买流程控制器
 * 主要是支持多种货币、发票、发货等特点
 *
 *
 * @author NetSnake
 * @date 2016年7月24日
 *
 */
@Controller
public abstract class AbstractBuyController extends BaseController{

	@Resource
	protected ActivityService activityService;

	@Resource
	protected AuthorizeService authorizeService;

	@Resource
	protected ApplicationContextService applicationContextService;

	@Resource
	protected CartService cartService;

	@Resource
	protected ConfigService configService;

	@Resource
	protected CouponModelService couponModelService;

	@Resource
	protected CouponService couponService;

	//XXX 来自EC模块的三个服务，使用@Autowired注入服务，以实现在不需要配送服务时，即不注入这三个服务，也能正常工作
	@Autowired(required=false)
	protected AddressBookService addressBookService;
	@Autowired(required=false)
	protected DeliveryOrderService deliveryOrderService;
	@Autowired(required=false)
	protected InvoiceService invoiceService;

	@Autowired(required=false)
	protected UuidMapService uuidMapService;

	@Resource
	protected CenterDataService centerDataService;

	@Resource
	protected GlobalOrderIdService globalOrderIdService;
	@Resource
	protected CertifyService certifyService;

	@Resource
	protected ItemService itemService;
	@Resource
	protected StockService stockService;
	@Resource
	protected FrontUserService frontUserService;

	@Resource
	protected MessageService messageService;

	@Resource
	protected MoneyService moneyService;
	@Resource
	protected PartnerService partnerService;
	@Resource
	protected PayTypeService payTypeService;
	@Resource
	protected PriceService priceService;
	@Resource
	protected PayService payService;

	private SimpleDateFormat sdf = new SimpleDateFormat(CommonStandard.defaultDateFormat);


	//向购物车添加一个商品之后的view
	protected String addView = "cart/list";
	//为结算订单选择配送地址的view
	protected final String deliveryView = "buy/delivery";
	//提交结算的view，在这里进行支付，如显示微信支付的二维码等
	protected final String settleUpView = "buy/settleUp";
	/**
	 * 支付请求后跳转post 页面
	 */
	protected final String postView = "buy/post";







	//将指定产品放入购物车
	@RequestMapping(value="/add")
	public String add(HttpServletRequest request, HttpServletResponse response, ModelMap map, Item item) throws EisException {

		map.put("operate", OperateCode.ADD_TO_CART.toString());
		User frontUser =  certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
		if(frontUser == null) {
			logger.error("需要先登录才能购物");
			throw new EisException(EisError.userNotFoundInSession.getId(), "请先登录");		
		}
		String identify = ServletRequestUtils.getStringParameter(request, "identify",null);
		//直接购买模式，不与用户的其他购物车关联
		boolean directBuy = ServletRequestUtils.getBooleanParameter(request, "directBuy", false);	
		//是否创建新的购物车，如果是directBuy则将创建新的购物车，否则以这个参数为准
		boolean createNewCart = ServletRequestUtils.getBooleanParameter(request, "createNewCart", false);
		if(createNewCart){
			directBuy = true;
		}
		//是否忽略配送要求
		boolean ignoreDelivery = ServletRequestUtils.getBooleanParameter(request, "ignoreDelivery", false);
		String transactionToken = ServletRequestUtils.getStringParameter(request, "tt", null);
		logger.debug("transactionToken ：" + transactionToken);

		int cartId = ServletRequestUtils.getIntParameter(request, "cartId", 0);
		/*String address = ServletRequestUtils.getStringParameter(request, "address");
		if(StringUtils.isNotBlank(address)){
			//直接为用户添加一个地址
		}*/
		//String phone = ServletRequestUtils.getStringParameter(request, "phone");
		//createNewCart = directBuy ? true : createNewCart;

		logger.debug("当前添加订单模式[directBuy=" + directBuy + "，ignoreDelivery=" + ignoreDelivery + ",cartId=" + cartId + ",transactionToken:" + transactionToken + "]");

		if(StringUtils.isBlank(item.getObjectType())){
			logger.error("无法添加到购物车，因为没有提交任何产品objectType");
			throw new EisException(EisError.OBJECT_IS_NULL.getId(), "请选择一个商品");		
		}

		if(item.getProductId() <= 0 ){
			
			//支持objectId或者productId两种名字
			long productId = ServletRequestUtils.getLongParameter(request,"objectId",0);
			if(productId <= 0) {
				logger.error("无法添加到购物车，因为没有提交任何产品objectId");
				throw new EisException(EisError.OBJECT_IS_NULL.getId(), "请选择一个商品");	
			} 
			item.setProductId((int)productId);
		}



		OpEisObject targetObject = stockService.writeItemData(item, item.getObjectType(), item.getProductId());



		int availableCount = stockService.getAvaiableCount(targetObject.getObjectType(), targetObject.getId());
		logger.debug("尝试购买的产品:{}/{}数量是:{}",item.getObjectType(), item.getProductId(), + availableCount);
		if(availableCount <= 0 && availableCount != -1 ){
			logger.error("尝试购买的产品:{}/{}数量是:{},无法购买",item.getObjectType(), item.getProductId(), + availableCount);
			throw new EisException(EisError.stockEmpty.getId(), "产品无货");		
		}

		long inviter = 0;
		if(frontUser.getInviter() > 0){
			inviter = frontUser.getInviter();
			logger.debug("前端用户[" + frontUser.getUuid() + "]有推荐来源:" + inviter + ",使用该推广来源");
		} else {
			logger.debug("前端用户[" + frontUser.getUuid() + "]没有推荐来源，产品[" + item.getObjectType() + "/" + item.getProductId()+ "]也没有供应商，不设置推广来源");
		}
		if(inviter > 0){
			item.setSupplyPartnerId(inviter);
		}
		item.setTransactionTypeId(TransactionType.buy.getId());
		long ttl = targetObject.getLongExtraValue(DataName.orderTtl.name());
		if(ttl <= 0) {
			logger.warn("产品{}/{}未设置ttl，使用默认TTL:{}", targetObject.getObjectType(), targetObject.getId(), CommonStandard.transactionTtl);
			ttl = CommonStandard.transactionTtl;
		}
		item.setTtl((int)ttl);
		item.setOwnerId(targetObject.getOwnerId());	
		item.setTransactionId(globalOrderIdService.generate(item.getTransactionTypeId()));
		item.setInOrderId(String.valueOf(System.currentTimeMillis()));
		
		if(item.getCount() < 1){
			item.setCount(1);
		}

		item.setName(targetObject.getName());
		/*if(product.getSupplyPartnerId() != 0){
			logger.error("产品[" + productCode + "不是内部产品，不能对外销售");
			map.put("message", new EisMessage(EisError.statusAbnormal.getId(), "错误的请求，产品状态异常"));
			return frontMessageView;	
		}*/

		long buyUuid = 0;
		if(uuidMapService != null){
			String appCode = ServletRequestUtils.getStringParameter(request, "appCode", null);
			buyUuid = uuidMapService.getMoneyUuid(frontUser,appCode);
		} else {
			buyUuid = frontUser.getUuid();
		}
		item.setChargeFromAccount(buyUuid);





		String lastEventKey = frontUser.getExtraValue(DataName.lastEventKey.toString());
		String currentEventKey = item.getExtraValue(DataName.eventKey.toString());
		if(currentEventKey == null && lastEventKey != null){
			logger.debug("为交易设置一个eventKey=" + lastEventKey);
			item.setExtraValue(DataName.eventKey.toString(),  lastEventKey);
		}

		/*if(directBuy){
			item.setCurrentStatus(TransactionStatus.waitingPay.getId());
		} else {*/
		item.setCurrentStatus(TransactionStatus.inCart.getId());
		//	}

		int priceResult;
		Price price = null;
		if(StringUtils.isNotBlank(transactionToken)){
			price = priceService.getPriceByToken(targetObject, buyUuid, transactionToken);
		}
		if(price == null){
			logger.debug("使用标准价格类型为交易[" + item.getTransactionId() + "]生成价格");
			priceResult = priceService.applyPrice(item, PriceType.PRICE_STANDARD.toString());
		} else {
			logger.debug("使用指定价格数据[" + price + "]为交易[" + item.getTransactionId() + "]生成价格");
			if(price.getPriceType().equals(PriceType.PRICE_PROMOTION.toString())){
				//尝试根据price的identify得到对应的活动
				String activityCode = price.getIdentify();
				if(StringUtils.isBlank(activityCode)){
					logger.debug("促销价格[" + price.getPriceId() + "]没有指定的identify，不再查找对应的活动");
				} else {
					Activity activity = activityService.select(activityCode, frontUser.getOwnerId());
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
							parameterMap.put("item", item);
							EisMessage checkResult = processor.execute(Operate.addToCart.getCode(), activity, frontUser, parameterMap);
							if(checkResult == null){
								logger.error("用户[" + frontUser.getUuid() + "]无法通过活动处理器[" + processor + "]的检查，返回结果是空");
								throw new EisException(EisError.activityLimited.getId(), "您无法购买该活动商品，活动异常错误");		
							}

							if(checkResult.getOperateCode() != OperateResult.success.getId()){
								logger.error("用户[" + frontUser.getUuid() + "]无法通过活动处理器[" + processor + "]的检查，返回结果是:" + checkResult);
								throw new EisException(checkResult.getOperateCode(), "无法参与活动");		
							}
							logger.info("用户[" + frontUser.getUuid() + "]通过了活动处理器[" + processor + "]的检查，返回结果是:" + checkResult);

						}
					}
				}				
			}
			priceResult = priceService.applyPrice(item, price);
		}



		String validatorClass = targetObject.getExtraValue(DataName.productValidator.toString());

		if(StringUtils.isNotBlank(validatorClass)){
			ProductValidator productValidator = applicationContextService.getBeanGeneric(StringUtils.uncapitalize(validatorClass));
			if(productValidator == null){
				logger.error("产品[" + item.getProductId() + "]定义了校验器[" + validatorClass + "]但找不到该校验器");
			} else {
				int validateResult = productValidator.validate(Operate.addToCart.getCode(),item, request);
				if(validateResult != OperateResult.success.getId()){
					logger.warn("交易品无法通过校验[" + validateResult + "]");
					if(validateResult ==  EisError.COUNT_LIMIT_EXCEED.id){
						throw new EisException(validateResult, "您购买该产品的次数已超过限制，暂时无法购买");		
					} 
					if(validateResult ==  EisError.moneyNotEnough.id){
						throw new EisException(validateResult, "您的金钱不足，暂时无法购买");		
					}  
					if(validateResult ==  EisError.moneyRangeError.id){
						throw new EisException(validateResult, "您的金钱低于新用户资金，暂时无法购买");		
					} 
				}	
			}

		}

		logger.debug("priceResult ：" + priceResult);
		if(priceResult != OperateResult.success.getId()){
			throw new EisException(priceResult, "产品数据异常，无法加入购物车");		
		}
		EisMessage result = new EisMessage();
		ItemCriteria itemCriteria = new ItemCriteria(frontUser.getOwnerId());
		itemCriteria.setChargeFromAccount(buyUuid);
		itemCriteria.setCurrentStatus(TransactionStatus.inCart.getId());
		HashMap<String,Item> itemMap = cartService.map(itemCriteria);
		int count = 0;
		if(itemMap != null){
			count = itemMap.size();
		}
		result.setOperateResult(count);
		result.setMessage(String.valueOf(item.getCartId()));

		Cart cart = cartService.add(item,directBuy,identify, cartId);

		logger.debug("商品加入购物车结果:" + cart + ",直接购买:" + directBuy);
		if(cart != null){
			result.setOperateCode(OperateResult.success.getId());
			if(!ignoreDelivery){
				/*String fromArea = deliveryOrderService.getFromArea(targetObject);
				if(fromArea != null){
					cart.setExtraValue(DataName.defaultFromArea.toString(), fromArea);

				}*/

			}
			map.put("order", cart);
			result.setMessage("商品已成功加入购物车");
		} else {
			result.setOperateCode(OperateResult.failed.getId());
			result.setMessage("商品无法加入购物车");	
		}
		map.put("message", result);
		if(directBuy){
			StringBuffer sb = new StringBuffer();
			if(ignoreDelivery){
				sb.append("/buy/settleUp");
			} else {
				sb.append("/buy/delivery");
			}
			sb.append(CommonStandard.DEFAULT_PAGE_SUFFIX);
			sb.append('?');
			sb.append("tid=");
			sb.append(item.getTransactionId());
			sb.append("&ignoreDelivery=");
			sb.append(ignoreDelivery);

			logger.info("当前是直接购买模式，跳转到:" + sb.toString());
			map.put("jump", sb.toString());
		}
		ItemCriteria itemCriteria2 = new ItemCriteria(frontUser.getOwnerId());
		itemCriteria2.setChargeFromAccount(frontUser.getUuid());
		itemCriteria2.setCartId(item.getCartId());
		itemMap = cartService.map(itemCriteria2);
		map.put("itemMap", itemMap);
		map.put("cartId", cart.getCartId());

		itemCriteria2 = null;
		return addView;
	}



	/*
	 * 检查是否需要配送
	 * 订单确认
	 */
	@RequestMapping(value="/delivery")
	public String  delivery(HttpServletRequest request, HttpServletResponse response, ModelMap map			) throws Exception {
		logger.debug("进入订单是否需要配送检查");
		////////////////////////////标准检查流程 ///////////////////////////////////
		User frontUser =  certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());


		//////////////////////////// 标准检查流程结束 ///////////////////////////////
		long buyUuid = 0;
		if(uuidMapService != null){
			String appCode = ServletRequestUtils.getStringParameter(request, "appCode");

			buyUuid = uuidMapService.getMoneyUuid(frontUser,appCode);
		} else {
			buyUuid = frontUser.getUuid();
		}
		Money money = moneyService.select(buyUuid, frontUser.getOwnerId());
		if(money != null){
			map.put("money", money);
		}
		long addressBookId = ServletRequestUtils.getLongParameter(request, "addressBookId", 0);
		long selectedAddressBookId = ServletRequestUtils.getLongParameter(request, "selectedAddressBookId", 0);
		//orderId实际是cartId
		long orderId = ServletRequestUtils.getLongParameter(request, "orderId", 0);
		String[] tids = ServletRequestUtils.getStringParameters(request, "tid");
		if(tids == null || tids.length == 0){
			throw new EisException(EisError.REQUIRED_PARAMETER.getId(), "请选择一个商品");		
		}
		ItemCriteria itemCriteria2 = new ItemCriteria(frontUser.getOwnerId());
		itemCriteria2.setChargeFromAccount(buyUuid);
		if(orderId > 0){
			itemCriteria2.setCartId(orderId);
		} else {
			itemCriteria2.setCurrentStatus(TransactionStatus.inCart.getId());
		}
		HashMap<String, Item> cart = cartService.map(itemCriteria2);

		if(cart == null || cart.size() < 1){//购物车是空的
			throw new EisException(EisError.OBJECT_IS_NULL.getId(), "您尚未购买任何产品，请先购买");		
		}

		HashMap<String, Item> submitCart = new HashMap<String, Item>();
		//比对用户勾选提交的订单号与购物车订单号，只支付提交的订单号
		for(String tid : cart.keySet()){
			for(String buyId : tids){				
				if(buyId.equalsIgnoreCase(tid)){
					if(orderId < 1){
						orderId = cart.get(tid).getCartId();
						logger.debug("根据交易[" + buyId + "]设置当前购物车ID:" + orderId);
					}
					logger.info("把交易[" + tid + "]放入待结算购物车，该交易状态是:" + cart.get(tid).getCurrentStatus());
					submitCart.put(buyId, cart.get(tid));
				}
			}
		}

		boolean toSelectDelivery = false;
		boolean toSelectSelfCarry = false;
		for(Item item : submitCart.values()){
			if(item != null){
				OpEisObject product = stockService.getTargetObject(item.getObjectType(), item.getProductId());
				if(product == null){
					logger.error("订单[" + item.getTransactionId() + "]对应产品[" + item.getProductId() + "]不存在");
					continue;
				}

				//检查该交易是否需要送货
				if(product.getBooleanExtraValue(DataName.productNeedDelivery.toString())){
					if(addressBookId == 0){
						logger.info("订单[" + item.getTransactionId() + "/" + item.getProductId() + "]需要配送，但未提交配送地址");
						toSelectDelivery = true;
					}
				}
				if(product.getBooleanExtraValue(DataName.selfCarry.toString())){
					if(addressBookId == 0){
						logger.info("订单[" + item.getTransactionId() + "/" + item.getProductId() + "]支持自提，但未提交自提地址");
						toSelectSelfCarry = true;
					}	

				}

				submitCart.put(item.getTransactionId(), item);
			}
		}

		Cart order = cartService.select(orderId);
		if(order == null){
			logger.error("找不到用户的购物车:" + orderId);
			throw new EisException(EisError.cartIsNull.getId(), "您选择购物车为空");		
		}

		if(toSelectDelivery){
			map.put("needAddressBook", true);
		}
		//计算总金额
		float totalMoney = 0f;
		float totalCoin = 0f;
		float totalPoint = 0f;
		int totalScore = 0;
		//取消了总金额Xcount的算法,NetSnake,2016-7-5
		for(Item item : submitCart.values()){
			totalMoney += item.getRequestMoney();
			totalCoin += item.getFrozenMoney();
			totalPoint += item.getSuccessMoney();
			totalScore += (int) item.getInMoney();
			/*totalMoney += item.getRequestMoney() * item.getCount();
			totalCoin += item.getFrozenMoney() * item.getCount();
			totalPoint += item.getSuccessMoney() * item.getCount();
			totalScore += (int) item.getInMoney() * item.getCount();*/
		}
		float askUseCouponMoney=  order.getFloatExtraValue(DataName.askUseCouponMoney.toString());
		map.put("askUseCouponMoney", askUseCouponMoney);

		String couponIds = order.getExtraValue("couponIds");
		if(couponIds != null){
			map.put("couponTotalMoney", countCouponTotalMoney(couponIds));
		}


		map.put("totalMoney", totalMoney);
		map.put("totalCoin", totalCoin);
		map.put("totalPoint", totalPoint);
		map.put("totalScore", totalScore);
		map.put("orderId", orderId);

		PayTypeCriteria payTypeCriteria = new PayTypeCriteria(frontUser.getOwnerId());
		payTypeCriteria.setCurrentStatus(BasicStatus.normal.getId());
		List<PayType> payTypeList = payTypeService.list(payTypeCriteria);
		if(payTypeList != null && payTypeList.size() > 0){
			map.put("payTypeList", payTypeList);
		}

		InvoiceCriteria invoiceCriteria = new InvoiceCriteria(frontUser.getOwnerId());
		invoiceCriteria.setUuid(frontUser.getOwnerId());
		List<Invoice> invoiceList = invoiceService.list(invoiceCriteria);
		logger.debug("用户[" + frontUser.getUuid() + "]已有发票信息数量是:" + (invoiceList == null ? "空" : invoiceList.size()));
		if(invoiceList != null && invoiceList.size() > 0){
			map.put("invoiceList", invoiceList);

		}

		if(toSelectDelivery){
			AddressBookCriteria addressBookCriteria = new AddressBookCriteria();
			addressBookCriteria.setUuid(frontUser.getUuid());
			List<AddressBook> addressBookList = addressBookService.list(addressBookCriteria);
			logger.info("用户[" + frontUser.getUuid() + "]的地址本" + (addressBookList == null ? "空" : addressBookList.size()) );

			//如果用户选择了地址，或根据用户默认地址，计算运费
			AddressBook defaultAddressBook = null;
			AddressBook selectedAddressBook = null;
			AddressBook firstAddressBook = null;
			long currentAddressBookId = 0;
			if(addressBookList != null && addressBookList.size() > 0){
				firstAddressBook = addressBookList.get(0);
				for(AddressBook addressBook : addressBookList){
					if(addressBook.getAddressBookId() == selectedAddressBookId){
						selectedAddressBook = addressBook;
					}
					if(addressBook.getCurrentStatus() == BasicStatus.relation.getId()){
						defaultAddressBook = addressBook;
					}
				}
			}

			List<AddressBook> addressBookList2 = new ArrayList<AddressBook>();
			if(selectedAddressBook != null){
				addressBookList2.add(selectedAddressBook);
			}
			if(defaultAddressBook != null){
				addressBookList2.add(defaultAddressBook);
			}

			if(addressBookList != null && addressBookList.size() > 0){
				for(AddressBook addressBook : addressBookList){
					if(selectedAddressBook != null && addressBook.getAddressBookId() == selectedAddressBook.getAddressBookId()){
						continue;
					}
					if(defaultAddressBook != null && addressBook.getAddressBookId() == defaultAddressBook.getAddressBookId()){
						continue;
					}
					addressBookList2.add(addressBook);
				}
			}


			if(selectedAddressBook == null && defaultAddressBook == null && firstAddressBook == null){
				logger.error("用户没有一个默认地址，也没有指定一个地址，也未能获取到第一个地址");
			} else {
				if(selectedAddressBook != null){
					currentAddressBookId = selectedAddressBook.getAddressBookId();
				} else if(defaultAddressBook != null){
					currentAddressBookId = defaultAddressBook.getAddressBookId();
				} else {
					currentAddressBookId = firstAddressBook.getAddressBookId();
				}
				Item[] items = new Item[submitCart.size()];
				items = submitCart.values().toArray(items);
				//根据用户默认地址，计算一个配送信息
				DeliveryOrder deliveryOrder = deliveryOrderService.generateDeliveryOrder(currentAddressBookId, items, String.valueOf(orderId), TransactionType.buy.getCode(), null);
				if(deliveryOrder == null){
					logger.error("无法根据用户选择的地址[" + currentAddressBookId + "]计算配送信息");
				} else {
					map.put("deliveryOrder", deliveryOrder);
				}

			}
			map.put("cart",submitCart);
			map.put("message", new EisMessage(EisError.requireSelfCarryAddress.getId(), "请选择或增加配送地址"));
			map.put("addressBookList", addressBookList2);

			return deliveryView;
		}
		if(toSelectSelfCarry){

			AddressBookCriteria addressBookCriteria = new AddressBookCriteria();
			addressBookCriteria.setUuid(frontUser.getUuid());
			List<AddressBook> addressBookList = addressBookService.list(addressBookCriteria);
			//logger.info("用户[" + frontUser.getUuid() + "]的地址本" + addressBookList == null ? "为空" : addressBookList.size())；
			map.put("message", new EisMessage(EisError.requireSelfCarryAddress.getId(), "请选择或增加自提地址"));
			map.put("cart",submitCart);
			map.put("addressBookList", addressBookList);
			return deliveryView;
			//					return "cart/selectSelfCarryAddress";
		}
		//没有需要处理的配送，直接跳转到结算
		/*String forward = "/buy/settleUp" + CommonStandard.DEFAULT_PAGE_SUFFIX + "?orderId=" + orderId + "&";
		for(String tid : submitCart.keySet()){
			forward += "tid=" + tid + "&";
		}
		forward = forward.replaceAll("&$", "");

		logger.info("不需要处理配送，跳转:" + forward);
		response.setStatus(HttpStatus.TEMPORARY_REDIRECT.value());
		response.sendRedirect(forward);		
		return null;*/
		map.put("cart",submitCart);
		return deliveryView;

	}


	private float countCouponTotalMoney(String couponIds) {
		if(couponIds == null){
			return 0;
		}
		float totalCouponMoney = 0;
		for(String couponIdData : couponIds.split(",")){
			String[] data = couponIdData.split("#");
			if(data != null && data.length > 1 && NumericUtils.isNumeric(data[1])){
				totalCouponMoney += Float.parseFloat(data[1]);	
			}
		}
		return totalCouponMoney;
	}





	/*
	 * 结算
	 */
	@RequestMapping(value="/settleUp")
	public String  settleUp(HttpServletRequest request, HttpServletResponse response, ModelMap map
			) throws Exception {
		logger.debug("开始结算步骤");
		map.put("operate", OperateCode.ORDER_SETTLEUP.toString());
		User frontUser =  certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());


		long buyUuid = 0;
		if(uuidMapService != null){
			String appCode = ServletRequestUtils.getStringParameter(request, "appCode");

			buyUuid = uuidMapService.getMoneyUuid(frontUser,appCode);
		} else {
			buyUuid = frontUser.getUuid();
		}

		/////////////////////////获取参数/////////////////////////////////////////////////
		long addressBookId = ServletRequestUtils.getLongParameter(request, "addressBookId", 0);
		boolean ignoreDelivery = false;
		int payTypeId = ServletRequestUtils.getIntParameter(request, "payTypeId", 1);
		int orderId = ServletRequestUtils.getIntParameter(request, "orderId", 0);
		String[] tids = ServletRequestUtils.getStringParameters(request, "tid");
		/////////////////////////////////////////////////////////////////////////////////////
		if(request.getParameter("ignoreDelivery") != null && request.getParameter("ignoreDelivery").equalsIgnoreCase("true")){
			ignoreDelivery = true;
		}		

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

		boolean toSelectDelivery = false;
		boolean toSelectSelfCarry = false;
		String submitOrderIds = "";
		//	logger.info("UUUUUUUUUUsubmitCart大小:" + submitCart.size() + ",当前系统代码是:" + configService.getSystemCode());

		for(Item item : submitCart.values()){
			if(item != null){
				OpEisObject targetObject = stockService.getTargetObject(item.getObjectType(), item.getProductId());
				if(targetObject == null){
					logger.error("订单[" + item.getTransactionId() + "]对应产品[" + item.getProductId() + "]不存在");
					continue;
				}



				if(!ignoreDelivery){
					//检查该交易是否需要送货
					boolean needDelivery = targetObject.getBooleanExtraValue(DataName.productNeedDelivery.toString());
					logger.debug("订单[" + item.getTransactionId() + "]对应的产品[" + item.getObjectType() + item.getProductId() + "]" + (needDelivery ? "" : "不") + "需要配送");
					if(needDelivery){
						if(addressBookId == 0){
							logger.info("订单[" + item.getTransactionId() + "/" + item.getProductId() + "]需要配送，但未提交配送地址");
							toSelectDelivery = true;
						}
					}

					boolean selfCarry = targetObject.getBooleanExtraValue(DataName.selfCarry.toString());
					logger.debug("订单[" + item.getTransactionId() + "]对应的产品[" + item.getObjectType() + item.getProductId() + "]" + (selfCarry ? "" : "不") + "支持自提");
					if(selfCarry){
						if(addressBookId == 0){
							logger.info("订单[" + item.getTransactionId() + "/" + item.getProductId() + "]支持自提，但未提交自提地址");
							toSelectSelfCarry = true;
						}	

					}
					submitOrderIds += item.getTransactionId();
				}
			}
		}
		if((toSelectDelivery || toSelectSelfCarry)&& orderId < 1){
			logger.debug("新订单需要配送或自提，将返回到选择配送信息界面");

			AddressBookCriteria addressBookCriteria = new AddressBookCriteria();
			addressBookCriteria.setUuid(frontUser.getUuid());
			List<AddressBook> addressBookList = addressBookService.list(addressBookCriteria);
			//logger.info("用户[" + frontUser.getUuid() + "]的地址本" + addressBookList == null ? "为空" : addressBookList.size())；
			map.put("cart",submitCart);
			map.put("message", new EisMessage(EisError.requireSelfCarryAddress.getId(), "请选择或增加配送地址"));
			map.put("addressBookList", addressBookList);
			return deliveryView;
		}
		/*	if(toSelectSelfCarry && orderId < 1){
			AddressBookCriteria addressBookCriteria = new AddressBookCriteria();
			addressBookCriteria.setUuid(frontUser.getUuid());
			List<AddressBook> addressBookList = addressBookService.list(addressBookCriteria);
			//logger.info("用户[" + frontUser.getUuid() + "]的地址本" + addressBookList == null ? "为空" : addressBookList.size())；
			map.put("message", new EisMessage(EisError.requireSelfCarryAddress.getId(), "请选择或增加自提地址"));
			map.put("cart",submitCart);
			map.put("addressBookList", addressBookList);
			return deliveryView;
			//					return "cart/selectSelfCarryAddress";
		}*/

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

		/*if(!forcePayWithoutAccountMoney){
			//检查账户余额
		}*/
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
		/*if(needMoney > 0){
			float askUseCouponMoney = order.



			orderMoney.setChargeMoney(needMoney);
		}
		if(needCoin > 0){
			orderMoney.setCoin(needCoin);
		}
		if(needPoint > 0){
			orderMoney.setPoint(needPoint);
		}*/

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
		/*f(addressBookId > 0){
			AddressBook addressBook = addressBookService.select("" + addressBookId);
			if(addressBook == null){
				logger.error("找不到指定的快递地址:" + addressBookId);
				map.put("message", new EisMessage(EisError.billCreateFailed.id,"找不到指定的配送地址"));
				return frontMessageView;
			}
			float totalDeliveryFee = 0f;
			for(Item item : submitCart.values()){
				deliveryOrder =  deliveryOrderService.generateDeliveryOrder(item, addressBook);
				if(deliveryOrder != null){
					totalDeliveryFee += deliveryOrder.getFee().getMoney();
				}
				logger.debug("为交易[" + item.getTransactionId() + "]生成新的配送单:" + deliveryOrder + ",生成日期:" + sdf.format(deliveryOrder.getCreateTime()) );
				deliveryOrderService.insert(deliveryOrder);
				if(order.getDeliveryOrderId() == 0){
					order.setDeliveryOrderId(deliveryOrder.getDeliveryOrderId());
				}

			}
			addressBook.setUseCount(addressBook.getUseCount()+1);
			addressBook.setLastUseTime(new Date());
			addressBookService.update(addressBook);
			needMoney += totalDeliveryFee;
			logger.debug("为订单[" + order.getCartId() + "]应付资金[" + needMoney + "]增加配送费用:" + totalDeliveryFee);
		}*/

		Price orderPrice = new Price(cart.getOwnerId());
		int totalProduct  = 0;
		int totalGoods = 0;
		StringBuffer goodsDesc = new StringBuffer();
		StringBuffer sb = new StringBuffer();

		AddressBook addressBook = null;
		if(addressBookId > 0){
			addressBook = addressBookService.select("" + addressBookId);
			if(addressBook == null){
				logger.error("找不到指定的快递地址:" + addressBookId);
				throw new EisException(EisError.requireDeliveryAddress.id, "找不到指定的配送地址");		
			}
			order.setExtraValue("addressBookId", String.valueOf(addressBookId));
		} else {
			//如果是从我的订单中选择未支付订单跳转过来，则直接获取订单中对应的addressBookId
			addressBookId = order.getLongExtraValue("addressBookId");
			logger.debug("直接从已存在订单[" + order.getCartId() + "]中获取地址本ID:" + addressBookId);
			if(addressBookId > 0){
				addressBook = addressBookService.select("" + addressBookId);
				if(addressBook == null){
					logger.error("找不到已存在订单[" + order.getCartId() + "]所指定的快递地址:" + addressBookId);
					throw new EisException(EisError.requireDeliveryAddress.id, "找不到指定的配送地址");		
				}
			}
		}
		DeliveryOrder totalDeliveryOrder = null;
		for(Item t : submitCart.values()){
			if(addressBook != null){
				deliveryOrder =  deliveryOrderService.generateDeliveryOrder(t, addressBook);
				if(deliveryOrder != null){
					if(deliveryOrder.getFee() != null){
						t.setExtraValue(DataName.deliveryFee.toString(), String.valueOf(deliveryOrder.getFee().getMoney()));
					}
					logger.debug("为交易[" + t.getTransactionId() + "]生成新的配送单:" + deliveryOrder + ",生成日期:" + sdf.format(deliveryOrder.getCreateTime()) );
					deliveryOrderService.insert(deliveryOrder);
					if(totalDeliveryOrder == null){
						totalDeliveryOrder = deliveryOrder.clone();
						totalDeliveryOrder.setFee(new Price());
						totalDeliveryOrder.setDeliveryOrderId(0);
						totalDeliveryOrder.setSyncFlag(0);

					}
					totalDeliveryOrder.setFee(Price.add(totalDeliveryOrder.getFee(), deliveryOrder.getFee()));		

					logger.debug("为交易[" + t.getTransactionId() + "]设置配送数据:deliveryOrderId=" + deliveryOrder.getDeliveryOrderId());
					t.setExtraValue(DataName.deliveryOrderId.toString(), ""+ deliveryOrder.getDeliveryOrderId());
					t.setExtraValue(DataName.deliveryCompanyName.toString(), deliveryOrder.getDeliveryCompany());
				}
			}
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


		if(totalDeliveryOrder != null){
			deliveryOrderService.insert(totalDeliveryOrder);
			totalDeliveryOrder.setRefOrderId(String.valueOf(order.getCartId()));
			logger.debug("订单[" + order.getCartId() + "]配送总费用是:" + totalDeliveryOrder.getFee());
			order.setDeliveryOrderId(totalDeliveryOrder.getDeliveryOrderId());
			order.setExtraValue(DataName.deliveryCompanyName.toString(), totalDeliveryOrder.getDeliveryCompany());
			if(totalDeliveryOrder.getFee() != null){
				order.setExtraValue(DataName.deliveryFee.toString(), String.valueOf(totalDeliveryOrder.getFee().getMoney()));
			}
			if(totalDeliveryOrder.getFee() != null){
				needMoney += totalDeliveryOrder.getFee().getMoney();
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
			//FIXME WHY?
			//pay.setInNotifyUrl("http://www.baidu.com");
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


	@RequestMapping(value="/countDeliveryFee")
	public String  countDeliveryFee(HttpServletRequest request, HttpServletResponse response, ModelMap map
			) throws Exception {
		logger.debug("开始计算配送费");
		User frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());

		//orderId实际是cartId
		long orderId = ServletRequestUtils.getLongParameter(request, "orderId", 0);
		String submitId = ServletRequestUtils.getStringParameter(request, "tid");
		if(StringUtils.isBlank(submitId)){
			throw new EisException(EisError.REQUIRED_PARAMETER.getId(), "您未选择任何商品");		
		}		
		String[] tids = submitId.split(",");
		long addressBookId = ServletRequestUtils.getLongParameter(request, "addressBookId", 0);
		if(orderId < 1){
			if(tids == null || tids.length == 0){
				throw new EisException(EisError.REQUIRED_PARAMETER.getId(), "请提交正确的数据");		
			}
		}
		Cart order = cartService.select(orderId);
		if(order == null){
			logger.warn("找不到用户指定的购物车:" + orderId);
			throw new EisException(EisError.REQUIRED_PARAMETER.getId(), "请提交正确的数据");		
		}

		if(tids == null || tids.length == 0){
			throw new EisException(EisError.REQUIRED_PARAMETER.getId(), "您未选择任何商品");		
		}
		ItemCriteria itemCriteria2 = new ItemCriteria(frontUser.getOwnerId());
		itemCriteria2.setChargeFromAccount(frontUser.getUuid());
		if(orderId > 0){
			itemCriteria2.setCartId(orderId);
		} else {
			itemCriteria2.setCurrentStatus(TransactionStatus.inCart.getId());
		}
		HashMap<String, Item> cart = cartService.map(itemCriteria2);

		if(cart == null || cart.size() < 1){//购物车是空的
			throw new EisException(EisError.OBJECT_IS_NULL.getId(), "您尚未购买任何产品，请先购买");		
		}

		HashMap<String, Item> submitCart = new HashMap<String, Item>();
		//比对用户勾选提交的订单号与购物车订单号，只支付提交的订单号
		for(String tid : cart.keySet()){
			for(String buyId : tids){				
				if(buyId.trim().equalsIgnoreCase(tid)){
					if(orderId < 1){
						orderId = cart.get(tid).getCartId();
						logger.debug("根据交易[" + buyId + "]设置当前购物车ID:" + orderId);
					}
					logger.info("把交易[" + tid + "]放入待结算购物车，该交易状态是:" + cart.get(tid).getCurrentStatus());
					submitCart.put(buyId, cart.get(tid));
				}
			}
		}
		if(submitCart == null || submitCart.size() < 1){//所选订单异常
			throw new EisException(EisError.cartIsNull.getId(), "选择订单异常");		
		}
		Item[] items = new Item[submitCart.size()];
		items = submitCart.values().toArray(items);
		//根据用户提供的地址，计算一个配送信息，如果未提供则使用默认地址
		AddressBook addressBook = null;

		if(addressBookId > 0){
			addressBook = addressBookService.select(String.valueOf(addressBookId));
			if(addressBook == null){
				logger.error("根据用户提交的addressBookId=" + addressBookId + "找不到对应的地址本");
				throw new EisException(EisError.INVALID_ADDRESS_BOOK.getId(), "找不到对应的地址本");		
			}
			if(addressBook.getOwnerId() != frontUser.getOwnerId()){
				logger.error("根据用户提交的addressBookId=" + addressBookId + ",对应的ownerId[" + addressBook.getOwnerId() + "]与用户的[" + frontUser.getOwnerId() + "]不一致");
				throw new EisException(EisError.INVALID_ADDRESS_BOOK.getId(), "找不到对应的地址本");		
			}
			if(addressBook.getUuid() != frontUser.getUuid()){
				logger.error("根据用户提交的addressBookId=" + addressBookId + ",对应的UUID[" + addressBook.getUuid() + "]与用户的[" + frontUser.getUuid() + "]不一致");
				throw new EisException(EisError.INVALID_ADDRESS_BOOK.getId(), "找不到对应的地址本");		
			}

		} else {
			if(addressBookService != null){
				AddressBookCriteria addressBookCriteria = new AddressBookCriteria();
				addressBookCriteria.setUuid(frontUser.getUuid());
				List<AddressBook> addressBookList = addressBookService.list(addressBookCriteria);
				logger.info("用户[" + frontUser.getUuid() + "]的地址本" + (addressBookList == null ? "空" : addressBookList.size()) );

				//根据用户默认地址，计算运费
				if(addressBookList != null && addressBookList.size() > 0){
					for(AddressBook book : addressBookList){
						if(book.getCurrentStatus() == BasicStatus.relation.getId()){
							addressBook = book;
							break;
						}
					}
				}
			}
		}

		if(addressBook == null){
			logger.error("根据用户提交的addressBookId=" + addressBookId + "找不到对应的地址本");
			throw new EisException(EisError.INVALID_ADDRESS_BOOK.getId(), "找不到对应的地址本");		
		}

		DeliveryOrder deliveryOrder = deliveryOrderService.generateDeliveryOrder(addressBook.getAddressBookId(), items, String.valueOf(orderId), TransactionType.buy.getCode(), null);
		if(deliveryOrder == null){
			logger.error("无法根据用户地址[" + addressBook.getAddressBookId() + "]计算配送信息");
		} else {
			map.put("deliveryOrder", deliveryOrder);
			//	order.getData().put("deliveryOrderJson", om.writeValueAsString(deliveryOrder));
		}
		return CommonStandard.frontMessageView;
	}

	//为一个订单设置发票信息
	@RequestMapping (value="/setInvoice",method=RequestMethod.POST)
	public String setInvoice(HttpServletRequest  request, HttpServletResponse response, ModelMap map, 		
			Invoice invoice
			) throws Exception {
		logger.debug("开始定制发票信息");
		User frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());

		int orderId = ServletRequestUtils.getIntParameter(request, "orderId", 0);
		Cart cart = cartService.select(orderId);
		if(cart == null){
			logger.error("找不到指定的购物车:" + orderId);
			throw new EisException(EisError.cartIsNull.getId(), "找不到指定的订单");		

		}

		if(StringUtils.isBlank(invoice.getTitle())){
			//删除发票
			map.put("order", cart);
			cart.setInvoiceInfo(null);
			cartService.update(cart);			
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(), "已取消发票"));
			return null;
		}
		InvoiceCriteria invoiceCriteria = new InvoiceCriteria(frontUser.getOwnerId());
		invoiceCriteria.setUuid(frontUser.getUuid());
		invoiceCriteria.setTitle(invoice.getTitle());
		List<Invoice> invoiceList = invoiceService.list(invoiceCriteria);
		String invoiceInfo = null;
		ObjectMapper om = JsonUtils.getInstance();
		om.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
		if(invoiceList == null || invoiceList.size() < 1){
			//添加新发票
			invoice.setUuid(frontUser.getUuid());
			invoice.setOwnerId(frontUser.getOwnerId());
			int rs = invoiceService.insert(invoice);
			logger.debug("为用户[" + frontUser.getUuid() + "]新增发票[" + invoice.getTitle() + "]结果:" + rs);
			if(rs == 1){

			} else {
				throw new EisException(EisError.DATA_UPDATE_FAIL.getId(), "新增发票失败");		
			}
			invoiceInfo = om.writeValueAsString(invoice);
		} else {
			Invoice i = invoiceList.get(0);
			invoiceInfo = om.writeValueAsString(i);
			logger.debug("相同抬头[" + invoice.getTitle() + "]的发票信息已存在，使用该发票:" + i);
		}

		if(invoiceInfo == null){
			throw new EisException(EisError.DATA_UPDATE_FAIL.getId(), "设置发票失败");		
		}
		map.put("order", cart);
		cart.setInvoiceInfo(invoiceInfo);
		cartService.update(cart);

		map.put("message",new EisMessage(OperateResult.success.getId(),"设置发票成功"));

		return CommonStandard.frontMessageView;
	}

	//确认要使用优惠券的金额
	@RequestMapping(value="/setCouponMoney", method=RequestMethod.POST)
	public String setCouponMoney(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		logger.debug("开始确认要使用优惠券的金额");
		User frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());

		///////////////////////// 获取参数 ///////////////
		long orderId = ServletRequestUtils.getLongParameter(request, "orderId", 0);
		float useMoney = ServletRequestUtils.getFloatParameter(request, "money", 0);

		if(orderId == 0){
			logger.error("未提交要使用优惠券资金的订单号");
			throw new EisException(EisError.REQUIRED_PARAMETER.getId(), "请提交正确的数据");		
		}

		if(useMoney <= 0){
			logger.error("未提交要使用多少优惠券资金");
			throw new EisException(EisError.REQUIRED_PARAMETER.getId(), "请提交正确的数据");		
		}

		Cart order = cartService.select(orderId);
		if(order == null){
			logger.error("找不到指定的购物车[" + orderId + "]");
			throw new EisException(EisError.cartIsNull.getId(), "找不到指定的订单");		
		}
		if(order.getUuid() != frontUser.getUuid()) {
			return null;
		}

		String couponIds = order.getExtraValue("couponIds");
		if(couponIds == null){
			logger.error("指定的购物车[" + order.getCartId() + "]之前没有关联任何优惠券数据");
			throw new EisException(EisError.cartIsNull.getId(), "您当前的订单没有使用优惠券");		
		}

		float totalCouponMoney = countCouponTotalMoney(couponIds);

		if(order.getMoney() == null){
			order.setMoney(new Money(order.getUuid(), order.getOwnerId()));
		}
		if(useMoney > totalCouponMoney){
			logger.error("订单[" + order.getCartId() + "]设置的优惠券总金额是:" + totalCouponMoney + ",比用户要求的:" + useMoney + "少"  );
			throw new EisException(EisError.moneyNotEnough.getId(), "优惠券金额不足");		
		}

		logger.debug("为订单[" + order.getCartId() + "]设置优惠券使用金额:" +useMoney);

		
		order.setExtraValue(DataName.askUseCouponMoney.toString(), String.valueOf(useMoney));

		order.getMoney().setGiftMoney(useMoney);
		cartService.update(order);

		map.put(DataName.askUseCouponMoney.toString(), useMoney);

		return CommonStandard.frontMessageView;
	}




	//请求使用一个优惠券
	//对应订单将被标记使用对应优惠券
	//如果卡券类型是MONEY_CHARGE类型，将把资金冲入资金帐号
	//如果卡券类型是MONEY_NO_CHARGE，将仅作标记但并不直接使用
	@RequestMapping(value="/setCoupon", method=RequestMethod.POST)
	public String setCoupon(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		logger.debug("请求使用优惠券");
		User frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());


		return _setCoupon(HttpUtils.getRequestDataMap(request), frontUser, map, HttpUtils.generateUrlPrefix(request));

	}

	private String _setCoupon(Map<String, String> requestDataMap, User frontUser,	ModelMap map, String urlPrefix){

		final String view = "coupon/commit";

		String couponCode = requestDataMap.get("couponCode");
		String serialNumber = requestDataMap.get("couponSerialNumber");
		String password = requestDataMap.get("couponPassword");
		String orderIdStr = requestDataMap.get("orderId");
		if(StringUtils.isBlank(orderIdStr)){
			logger.error("未提交要使用优惠券对应的交易");
			throw new EisException(EisError.REQUIRED_PARAMETER.getId(), "请提交正确的数据");		
		}

		if(StringUtils.isBlank(couponCode)){
			logger.error("未提交要领取的优惠券代码");
			throw new EisException(EisError.REQUIRED_PARAMETER.getId(), "请提交正确的数据");		
		}
		if(StringUtils.isBlank(serialNumber)){
			logger.error("未提交要领取的优惠券卡号");
			throw new EisException(EisError.REQUIRED_PARAMETER.getId(), "请提交正确的数据");		
		}
		serialNumber = serialNumber.toUpperCase();
		if(StringUtils.isBlank(password)){
			logger.error("未提交要领取的优惠券密码");
			throw new EisException(EisError.REQUIRED_PARAMETER.getId(), "请提交正确的数据");		
		}
		password = password.toUpperCase();
		Cart cart = cartService.select(Long.parseLong(orderIdStr));
		if(cart == null){
			logger.error("找不到指定的交易:" + orderIdStr);
			throw new EisException(EisError.ORDER_DATA_ERROR.getId(), "请提交正确的数据");		
		}

		CouponModelCriteria couponModelCriteria = new CouponModelCriteria();
		couponModelCriteria.setOwnerId(frontUser.getOwnerId());
		couponModelCriteria.setCouponCode(couponCode);
		couponModelCriteria.setCurrentStatus(BasicStatus.normal.getId());
		List<CouponModel> couponModelList = couponModelService.list(couponModelCriteria);
		if(couponModelList == null || couponModelList.size() < 1){
			logger.error("找不到指定的卡券产品:" + couponCode);
			throw new EisException(EisError.ORDER_DATA_ERROR.getId(), "请提交正确的数据");		
		}
		CouponModel couponModel = couponModelList.get(0);
		String beanName = null;
		if(couponModel.getProcessor() == null){
			logger.error("指定的卡券产品[" + couponModel.getCouponCode() + "]没有指定处理器");
			throw new EisException(EisError.beanNotFound.getId(), "指定的卡券产品数据异常");		
		} 
		beanName = couponModel.getProcessor();


		Object object = applicationContextService.getBean(beanName);
		if(object == null){
			logger.error("找不到指定的卡券[" + couponModel.getCouponModelId() + "]处理器:" + beanName);
			throw new EisException(EisError.beanNotFound.getId(), "指定的卡券产品数据异常");		
		}
		if(!( object instanceof CouponProcessor)){
			logger.error("指定的卡券[" + couponModel.getCouponModelId() + "]处理器:" + beanName + ",类型不是CouponProcessor,是:" + object.getClass().getName());
			throw new EisException(EisError.beanNotFound.getId(), "指定的卡券产品数据异常");		
		}

		/*CouponCriteria couponCriteria = new CouponCriteria(frontUser.getOwnerId());
		couponCriteria.setCouponCode(couponModel.getCouponCode());
		couponCriteria.setCouponSerialNumber(serialNumber);
		couponCriteria.setCouponPassword(password);
		//couponCriteria.setUuid(frontUser.getUuid());
		List<Coupon> couponList = couponService.list(couponCriteria);
		Coupon coupon  = null;
		if(couponList != null && couponList.size() > 0){
			coupon = couponList.get(0);
		}
		if(coupon == null){
			logger.warn("在本地系统中找不到卡券[code=" + couponCriteria.getCouponCode() + ",sn=" + serialNumber + ",password=" + password);
			map.put("message", new EisMessage(EisError.beanNotFound.getId(),"找不到指定的卡券"));
			return frontMessageView;
		}*/


		CouponProcessor couponProcessor = (CouponProcessor)object;

		CouponCriteria couponCriteria = new CouponCriteria(frontUser.getOwnerId());
		couponCriteria.setCouponCode(couponCode);
		couponCriteria.setCouponSerialNumber(serialNumber);
		couponCriteria.setCouponPassword(password);
		couponCriteria.setUuid(frontUser.getUuid());
		Coupon coupon = couponProcessor.lock(couponCriteria);

		logger.debug("优惠券校验结果:" + coupon);
		if(coupon == null){
			map.put("message",new EisMessage(OperateResult.failed.getId(),"无效的优惠券"));
			return view;
		}
		if(coupon.getCurrentStatus() == Coupon.STATUS_USED){
			map.put("message",new EisMessage(OperateResult.failed.getId(),"优惠券已被使用"));
		} 
		if(coupon.isAllZero()){
			map.put("message",new EisMessage(OperateResult.failed.getId(),"优惠券已没有余额"));
			return view;
		}


		map.put("coupon", coupon);
		cartService.update(cart);

		if(coupon.getCouponType() != null && coupon.getCouponType().equals(CouponModelCriteria.COUPON_CHARGE_MONEY)){


			//充值
			int chargeResult = couponProcessor.consume(coupon);
			logger.debug("卡券[" + coupon.getCouponId() + "]充值结果:" + chargeResult);
			if(chargeResult == OperateResult.success.getId()){
				Money money = coupon.getGiftMoney().clone();
				money.setOwnerId(frontUser.getOwnerId());
				money.setUuid(frontUser.getUuid());
				moneyService.plus(money);
				logger.debug("卡券充值成功，向用户资金账户添加资金:" + money.getMoneyBrief());
				map.put("message", new EisMessage(OperateResult.success.getId(),"充值成功"));
			} else {
				map.put("message", new EisMessage(OperateResult.failed.getId(),"充值失败"));
			}
		} else {
			String couponIds =  StringTools.addElementNoDuplicate(cart.getExtraValue("couponIds"), coupon.getCouponId() + "#" + coupon.getGiftMoney());
			cart.setExtraValue("couponIds", couponIds);
			logger.debug("为订单[" + cart.getCartId() + "]增加要用的卡券:" + coupon.getCouponId() + ",增加后的扩展数据:" + couponIds);

			map.put("message",new EisMessage(OperateResult.success.getId(),"优惠券有效"));
		}
		return view;

	}

	//为一个订单设置快递信息
	@RequestMapping (value="/setDeliveryAddress",method=RequestMethod.POST)
	public String setDeliveryAddress(HttpServletRequest  request, HttpServletResponse response, ModelMap map			) throws Exception {
		logger.debug("为一个订单设置快递信息");
		User frontUser  = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());


		long orderId = ServletRequestUtils.getLongParameter(request, "orderId", 0);
		long addressBookId = ServletRequestUtils.getLongParameter(request, "addressBookId", 0);


		if(addressBookId < 1){
			throw new EisException(EisError.REQUIRED_PARAMETER.getId(), "请提交地址ID");		
		}

		AddressBook addressBook = null;
		if(addressBookService != null){
			addressBook = addressBookService.select("" + addressBookId);
		}
		if(addressBook == null){
			logger.error("找不到指定的快递地址:" + addressBookId);
			throw new EisException(EisError.INVALID_ADDRESS_BOOK.getId(), "找不到指定的配送地址");		
		}
		Cart cart = cartService.select(orderId);
		if(cart == null){
			logger.error("找不到指定的购物车:" + orderId);
			throw new EisException(EisError.cartIsNull.getId(), "找不到指定的订单");		

		}

		if(cart.getUuid() != frontUser.getUuid()) {
			throw new EisException(EisError.cartIsNull.getId(), "找不到指定的订单");		

		}
		String fromArea = cart.getExtraValue(DataName.defaultFromArea.toString());
		if(fromArea == null){
			logger.warn("订单[" + cart.getCartId() + "]没有快递发货地数据");
		} else {

			/*DeliveryOrder deliveryOrder =  deliveryOrderService.generateDeliveryOrder(addressBook.getAddressBookId(), cart, TransactionType.buy.getCode(), null);

			if(deliveryOrder == null){
				logger.error("无法为订单[" + orderId + "]及地址[" + addressBookId + "]计算运费");
				map.put("message", new EisMessage(OperateResult.failed.getId(), "无法计算运费"));
				return frontMessageView;
			}
			map.put("deliveryOrder", deliveryOrder);*/
		}
		map.put("order", cart);


		return CommonStandard.frontMessageView;
	}


}
