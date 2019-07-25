package com.maicard.wpt.controller.common;

import static com.maicard.standard.CommonStandard.frontMessageView;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.PostConstruct;
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

import com.maicard.common.base.BaseController;
import com.maicard.common.domain.EOEisObject;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.GlobalOrderIdService;
import com.maicard.common.util.ContextTypeUtil;
import com.maicard.common.util.HttpUtils;
import com.maicard.common.util.NumericUtils;
import com.maicard.common.util.StringTools;
import com.maicard.ec.criteria.AddressBookCriteria;
import com.maicard.ec.domain.AddressBook;
import com.maicard.ec.domain.DeliveryOrder;
import com.maicard.ec.service.AddressBookService;
import com.maicard.ec.service.DeliveryOrderService;
import com.maicard.ec.service.InvoiceService;
import com.maicard.mb.service.MessageService;
import com.maicard.money.criteria.PayTypeCriteria;
import com.maicard.money.domain.Money;
import com.maicard.money.domain.Pay;
import com.maicard.money.domain.PayType;
import com.maicard.money.domain.Price;
import com.maicard.money.iface.PayProcessor;
import com.maicard.money.service.MoneyService;
import com.maicard.money.service.PayMethodService;
import com.maicard.money.service.PayService;
import com.maicard.money.service.PayTypeService;
import com.maicard.money.service.PriceService;
import com.maicard.product.criteria.CartCriteria;
import com.maicard.product.criteria.ItemCriteria;
import com.maicard.product.domain.Activity;
import com.maicard.product.domain.Cart;
import com.maicard.product.domain.Item;
import com.maicard.product.domain.ProductData;
import com.maicard.product.service.ActivityProcessor;
import com.maicard.product.service.ActivityService;
import com.maicard.product.service.CartService;
import com.maicard.product.service.ItemService;
import com.maicard.product.service.ProductValidator;
import com.maicard.product.service.StockService;
import com.maicard.security.domain.User;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.UuidMapService;
import com.maicard.site.service.DocumentService;
import com.maicard.site.service.NodeService;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.MoneyType;
import com.maicard.standard.ObjectType;
import com.maicard.standard.Operate;
import com.maicard.standard.OperateCode;
import com.maicard.standard.OperateResult;
import com.maicard.standard.PriceType;
import com.maicard.standard.SecurityStandard.UserStatus;
import com.maicard.standard.SecurityStandard.UserTypes;
import com.maicard.standard.TransactionStandard.TransactionStatus;
import com.maicard.standard.TransactionStandard.TransactionType;

@Controller
@RequestMapping(value="/cart")
public class CartController extends BaseController{

	@Resource
	private ActivityService activityService;
	@Resource
	private ApplicationContextService applicationContextService;
	@Resource
	private CartService cartService;
	@Resource
	private CertifyService certifyService;
	@Resource
	private DocumentService documentService;
	@Resource
	private GlobalOrderIdService globalOrderIdService;
	@Resource
	private ItemService itemService;
	@Resource
	private MoneyService moneyService;
	@Resource
	private PayService payService;
	@Resource
	private PayMethodService payMethodService;

	@Resource
	private ConfigService configService;

	@Resource
	private MessageService messageService;


	//XXX 来自EC模块的三个服务，使用@Autowired注入服务，以实现在不需要配送服务时，即不注入这三个服务，也能正常工作
	@Autowired(required=false)
	private AddressBookService addressBookService;


	@Autowired(required=false)
	private DeliveryOrderService deliveryOrderService;

	@SuppressWarnings("unused")
	@Autowired(required=false)
	private InvoiceService invoiceService;



	@Resource
	private PayTypeService payTypeService;

	@Resource
	private StockService stockService;

	@Resource
	private NodeService nodeService;

	@Autowired(required=false)
	private UuidMapService uuidMapService;

	@Resource
	private PriceService priceService;

	//是否只使用已有的资金进行支付，如果是，则资金不足时提示前往付款地址
	static boolean useRemainMoneyOnly = true;

	//向购物车添加一个商品之后的view
	static final String indexView = "cart/index";
	//为结算订单选择配送地址的view
	static final String deliveryView = "cart/delivery";
	//提交结算的view，在这里进行支付，如显示微信支付的二维码等
	static final String settleUpView = "cart/settleUp";
	/**
	 * 支付请求后跳转post 页面
	 */
	static final String postView = "cart/post";

	static final SimpleDateFormat sdf = new SimpleDateFormat(CommonStandard.defaultDateFormat);

	static String globalValidatorConfig = null;

	ProductValidator globalValidator = null;
	
	@PostConstruct
	public void init() {
		globalValidatorConfig = configService.getValue("globalValidator", 0);

	}
	/*
	 * 返回当前购物车列表
	 */
	@RequestMapping(method=RequestMethod.GET)
	public String  list(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {	

		User frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());

		if(frontUser == null){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录，请先登录后再购买"));
			return indexView;
		}


		long buyUuid = 0;
		if(uuidMapService != null){
			buyUuid = uuidMapService.getMoneyUuid(frontUser,null);
		} else {
			buyUuid = frontUser.getUuid();
		}

		HashMap<String, Item> itemMap = null;
		ItemCriteria itemCriteria2 = new ItemCriteria(frontUser.getOwnerId());
		itemCriteria2.setChargeFromAccount(buyUuid);
		logger.debug("inCart ：" + TransactionStatus.inCart.getId());
		itemCriteria2.setCurrentStatus(TransactionStatus.inCart.getId());
		String priceType = ServletRequestUtils.getStringParameter(request, "priceType");
		if(priceType == null){
			logger.debug("列出购物车未指定价格类型，使用PRICE_STANDARD");
			priceType = PriceType.PRICE_STANDARD.toString();
		} else {
			logger.debug("列出购物车指定价格类型是:" + priceType);
		}

		String buyType = ServletRequestUtils.getStringParameter(request, "buyType");
		if(buyType == null){
			logger.debug("列出购物车未指定价格类型，使用标准类型:" + CartCriteria.BUY_TYPE_NORMAL);
			buyType = CartCriteria.BUY_TYPE_NORMAL;
		} 

		String identify = ServletRequestUtils.getStringParameter(request, "identify");

		logger.debug("列出购物车指定了识别码:" + identify);
		//???? 为什么有identify就去设置priceType？   已注释,NetSnake,2016-05-19
		//priceType = PriceType.PRICE_STANDARD.toString();



		long cartId = ServletRequestUtils.getLongParameter(request, "cartId", 0);
		if(cartId < 1){
			CartCriteria cartCriteria = new CartCriteria(frontUser.getOwnerId());
			cartCriteria.setUuid(frontUser.getUuid());
			cartCriteria.setPriceType(priceType);
			cartCriteria.setBuyType(buyType);
			cartCriteria.setIdentify(identify);
			cartCriteria.setOrderType(CartCriteria.ORDER_TYPE_TEMP);
			List<Cart> cartList = cartService.list(cartCriteria);
			if(cartList == null || cartList.size() < 1){
				logger.info("在系统中找不到用户[" + frontUser.getUuid() + "]的价格类型为[" + priceType + "]的任何购物车");
				map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.getId(), "您当前购物车中没有任何商品"));
				return indexView;
			} else {
				Cart cart = cartList.get(0);
				logger.info("在系统中找到用户[" + frontUser.getUuid() + "]的价格类型为[" + priceType + "]的购物车，数量:" + cartList.size() + ",返回第1个:" + cart.getCartId());
				cartId = cart.getCartId();
				logger.debug("cartId　：" + cartId);
			}
		}
		itemCriteria2.setCartId(cartId);
		itemMap = cartService.map(itemCriteria2);
		logger.debug("itemMap : " + itemMap);
		if(itemMap == null){
			itemMap = new HashMap<String, Item>();
		}

		if(itemMap.size() < 1){//购物车是空的
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.getId(), "您当前购物车中没有任何商品"));
		} else {
			Cart cart = cartService.select(cartId);
			map.put("cart", cart);
		}
		map.put("itemMap", itemMap);
		
		boolean withAddressBook = ServletRequestUtils.getBooleanParameter(request, "withAddressBook", false);
		if(withAddressBook) {
			//获取地址本
			if(addressBookService == null) {
				logger.error("系统未注入addressBookService,不能获取地址本");
			} else {
				AddressBookCriteria addressBookCriteria=new AddressBookCriteria();
				addressBookCriteria.setUuid(frontUser.getUuid());
				addressBookCriteria.setOwnerId(frontUser.getOwnerId());
				List<AddressBook> addressBookList = addressBookService.list(addressBookCriteria);
				map.put("addressBookList", addressBookList);
			}
		}
		return indexView;
	}
	/*
	 * 返回当前购物车数量
	 */
	@ResponseBody
	@RequestMapping(value="/count",method=RequestMethod.GET)
	public String  count(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {	
		int currentStatus = 0;
		if(StringUtils.isNumeric(request.getParameter("currentStatus"))){
			currentStatus = Integer.parseInt(request.getParameter("currentStatus"));
		}
		User frontUser = null;
		try{
			frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
		}catch(Exception e){
			e.printStackTrace();			
		}
		if(frontUser == null){
			return "0";
		}
		logger.debug("购物车内数量"+cartService.count(frontUser.getUuid(), currentStatus) + "");
		return cartService.count(frontUser.getUuid(), currentStatus) + "";

	}
	//修改购物车中的某个产品，例如数量
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public String update(HttpServletRequest request, HttpServletResponse response, ModelMap map, 
			@ModelAttribute("item") Item item) throws Exception {


		User frontUser = null;
		try{
			frontUser = certifyService.getLoginedUser(request, response,UserTypes.frontUser.getId());
			//logger.info("从Session中得到用户:" + frontUser.getUuid());
		}catch(Exception e){
			e.printStackTrace();			
		}
		if(frontUser == null){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录，请先登录后再购买"));
			return CommonStandard.frontMessageView;
		}	
		String transactionId = ServletRequestUtils.getStringParameter(request,"transactionId");
		if(StringUtils.isBlank(transactionId)){
			logger.error("提交修改的交易ID为空");
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.getId(), "未提交要修改的订单ID"));
			return CommonStandard.frontMessageView;
		}
		//只支持修改数量
		int count =ServletRequestUtils.getIntParameter(request, "count");
		if(count == 0){
			logger.warn("没有提交要修改的交易数量[" + transactionId + "]");
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.getId(), "未提交要修改的数量"));
			return CommonStandard.frontMessageView;
		}
		long buyUuid = 0;
		if(uuidMapService != null){
			buyUuid = uuidMapService.getMoneyUuid(frontUser,null);
		} else {
			buyUuid = frontUser.getUuid();
		}

		if(count == 0){
			logger.info("提交修改的交易[" + transactionId + "],数量是0,直接删除该商品");
			try {
				cartService.delete(buyUuid, transactionId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			map.put("message", new EisMessage(OperateResult.success.getId(), "商品已删除"));
			return CommonStandard.frontMessageView;
		}
		item = cartService.select(buyUuid, transactionId);
		if(item == null){
			logger.error("找不到要修改的订单[" + transactionId + "]");
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.getId(), "找不到要修改的订单[" + transactionId + "]"));
			return CommonStandard.frontMessageView;
		}
		if(item.getCurrentStatus() != TransactionStatus.inCart.getId()){

			logger.error("要修改的订单[" + transactionId + "]状态不是inCart是:" + item.getCurrentStatus() + ",不能修改");
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.getId(), "订单[" + transactionId + "]不允许修改"));
			return CommonStandard.frontMessageView;
		}
		logger.debug("把订单[" + item.getTransactionId() + "]数量从" + item.getCount() + "替换为" + count);
		item.setCount(count);


		try {
			cartService.update(item,0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "cart/list";
	}

	//清空购物车
	@RequestMapping(value="/clear", method=RequestMethod.POST)
	public String clear(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {


		User frontUser = null;
		try{
			frontUser = certifyService.getLoginedUser(request, response,UserTypes.frontUser.getId());
		}catch(Exception e){
			e.printStackTrace();			
		}
		if(frontUser == null){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录，请先登录"));
			return CommonStandard.frontMessageView;
		}		

		long buyUuid = 0;
		if(uuidMapService != null){
			buyUuid = uuidMapService.getMoneyUuid(frontUser,null);
		} else {
			buyUuid = frontUser.getUuid();
		}
		try {
			cartService.clear(buyUuid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "cart/list";
	}

	//批量删除购物车中的产品
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	public String delete(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		User frontUser = null;
		try{
			frontUser = certifyService.getLoginedUser(request, response,UserTypes.frontUser.getId());
		}catch(Exception e){
			e.printStackTrace();			
		}
		if(frontUser == null){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录，请先登录"));
			return CommonStandard.frontMessageView;
		}

		String transactionIdStr = ServletRequestUtils.getStringParameter(request,"transactionIds");//多个transactionId以“，”拼接的字符串
		if(StringUtils.isBlank(transactionIdStr)){
			logger.error("提交删除的item ID为空");
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.getId(), "未提交要删除的订单ID"));
			return CommonStandard.frontMessageView;
		}
		long buyUuid = 0;
		if(uuidMapService != null){
			String appCode = ServletRequestUtils.getStringParameter(request, "appCode",null);

			buyUuid = uuidMapService.getMoneyUuid(frontUser,appCode);
		} else {
			buyUuid = frontUser.getUuid();
		}
		String[] transactionIdArr = transactionIdStr.split(",");
		for (String transactionIds : transactionIdArr) {
			try {
				cartService.delete(buyUuid, transactionIds);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "cart/list";
	}

	//将指定产品放入购物车
	@RequestMapping(value="/add")
	public String add(HttpServletRequest request, HttpServletResponse response, ModelMap map, String objectType, long objectId, Item item) throws Exception {

		map.put("operate", OperateCode.ADD_TO_CART.toString());
		////////////////////////////标准检查流程 ///////////////////////////////////
		User frontUser =  certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());

		if(frontUser == null || frontUser.getCurrentStatus() != UserStatus.normal.getId()){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录，请先登录"));			
			return frontMessageView;

		}
		long ownerId = NumericUtils.parseLong(map.get("ownerId"));

		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),"系统异常","请尝试访问其他页面或返回首页"));
			return frontMessageView;		
		}

		if(frontUser.getOwnerId() != ownerId){
			logger.error("用户[" + frontUser.getUuid() + "]的ownerId[" + frontUser.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(), "您尚未登录，请先登录"));			
			return frontMessageView;
		}

		//////////////////////////// 标准检查流程结束 ///////////////////////////////

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
		String transactionToken = ServletRequestUtils.getStringParameter(request, "tt");
		logger.debug("transactionToken ：" + transactionToken);

		int cartId = ServletRequestUtils.getIntParameter(request, "cartId", 0);

		logger.debug("当前添加订单模式[directBuy=" + directBuy + "，ignoreDelivery=" + ignoreDelivery + ",cartId=" + cartId + ",transactionToken:" + transactionToken + "]");

		if(objectType == null) {
			logger.error("添加商品的类型objectType不能为空");
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(), "objectType参数为空"));			
			return frontMessageView;		
		}
		if(objectId <= 0) {
			logger.error("添加商品的objectId不能为0");
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(), "objectId参数为空"));			
			return frontMessageView;		
		}


		int availableCount = stockService.getAvaiableCount(objectType, objectId);
		logger.debug("尝试购买的产品:{}/{}剩余数量是:{}",objectType, objectId, availableCount);
		if(availableCount == 0){
			logger.debug("尝试购买的产品:{}/{}剩余数量是:{},不能再买",objectType, objectId, availableCount);
			map.put("message", new EisMessage(EisError.stockEmpty.getId(), "产品[" + objectType + "/" + objectId + "]无货"));
			return frontMessageView;		
		}

		long inviter = 0;
		if(frontUser.getInviter() > 0){
			inviter = frontUser.getInviter();
			logger.debug("前端用户[" + frontUser.getUuid() + "]有推荐来源:" + inviter + ",使用该推广来源");
		} else {
			logger.debug("前端用户[" + frontUser.getUuid() + "]没有推荐来源，产品[" + objectType + "/" + objectId +  "]也没有供应商，不设置推广来源");
		}
		if(inviter > 0){
			item.setSupplyPartnerId(inviter);
		}
		item.setTransactionTypeId(TransactionType.buy.getId());
		item.setObjectType(objectType);	
		item.setProductId((int)objectId);
		item.setOwnerId(ownerId);	
		item.setTransactionId(globalOrderIdService.generate(item.getTransactionTypeId()));

		if(item.getCount() < 1){
			item.setCount(1);
		}

		EOEisObject targetObject = stockService.writeItemData(item, objectType, objectId);


		long buyUuid = 0;
		if(uuidMapService != null){
			String appCode = ServletRequestUtils.getStringParameter(request, "appCode");
			buyUuid = uuidMapService.getMoneyUuid(frontUser,appCode);
		} else {
			buyUuid = frontUser.getUuid();
		}
		item.setChargeFromAccount(buyUuid);





		String lastEventKey = frontUser.getExtraValue(DataName.lastEventKey.toString());
		String currentEventKey = item.getExtraValue(DataName.eventKey.toString());
		if(currentEventKey == null && lastEventKey != null){
			logger.debug("为交易设置一个eventKey=" + lastEventKey);
			item.getItemDataMap().put(DataName.eventKey.toString(),  new ProductData(DataName.eventKey.toString(), lastEventKey));
		}

		/*if(directBuy){
			item.setCurrentStatus(TransactionStatus.waitingPay.getId());
		} else {*/
		item.setCurrentStatus(TransactionStatus.inCart.getId());
		//	}

		int priceResult  = priceService.applyPrice(item, PriceType.PRICE_STANDARD.toString());

		/*
		if(StringUtils.isNotBlank(transactionToken)){
			price = priceService.getPriceByToken(objectType, objectId, buyUuid, transactionToken);
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
					Activity activity = activityService.select(activityCode, ownerId);
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
								map.put("message", new EisMessage(EisError.activityLimited.id, "您无法购买该活动商品，活动异常错误"));
								return frontMessageView;	
							}

							if(checkResult.getOperateCode() != OperateResult.success.getId()){
								logger.error("用户[" + frontUser.getUuid() + "]无法通过活动处理器[" + processor + "]的检查，返回结果是:" + checkResult);
								map.put("message",checkResult);
								return frontMessageView;	
							}
							logger.info("用户[" + frontUser.getUuid() + "]通过了活动处理器[" + processor + "]的检查，返回结果是:" + checkResult);

						}
					}
				}				
			}
		}*/
		//priceResult = priceService.applyPrice(item, price);

		if(globalValidatorConfig != null) {
			if(globalValidator == null) {
				globalValidator = applicationContextService.getBeanGeneric(globalValidatorConfig);
			}
			if(globalValidator != null) {
				int validateResult = globalValidator.validate(Operate.addToCart.getCode(),item, request);
				if(validateResult != OperateResult.success.getId()){
					logger.warn("交易品无法通过全局校验[" + validateResult + "]");
					if(validateResult ==  EisError.COUNT_LIMIT_EXCEED.id){
						map.put("message", new EisMessage(validateResult,"您购买该产品的次数已超过限制，暂时无法购买"));
					} else if(validateResult ==  EisError.moneyNotEnough.id){
						map.put("message", new EisMessage(validateResult,"您的金钱不足，暂时无法购买"));
					}  else if(validateResult ==  EisError.moneyRangeError.id){
						map.put("message", new EisMessage(validateResult,"您的金钱低于新用户资金，暂时无法购买"));
					} 
					return frontMessageView;					
				}	
			}
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
						map.put("message", new EisMessage(validateResult,"您购买该产品的次数已超过限制，暂时无法购买"));
					} else if(validateResult ==  EisError.moneyNotEnough.id){
						map.put("message", new EisMessage(validateResult,"您的金钱不足，暂时无法购买"));
					}  else if(validateResult ==  EisError.moneyRangeError.id){
						map.put("message", new EisMessage(validateResult,"您的金钱低于新用户资金，暂时无法购买"));
					} 
					return frontMessageView;					
				}	
			}

		}

		logger.debug("priceResult ：" + priceResult);
		if(priceResult != OperateResult.success.getId()){
			map.put("message", new EisMessage(priceResult, "产品数据异常，无法加入购物车"));
			return frontMessageView;	
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
			if(!ignoreDelivery && deliveryOrderService != null){
				String fromArea = deliveryOrderService.getFromArea(item);
				if(fromArea != null){
					cart.setExtraValue(DataName.defaultFromArea.toString(), fromArea);

				}

			}
			map.put("order", cart);
			result.setMessage("商品已成功加入购物车");
		} else {
			result.setOperateCode(OperateResult.failed.getId());
			result.setMessage("商品无法加入购物车");	
		}
		map.put("message", result);
		if(directBuy){
			//检查资金是否足够


			StringBuffer sb = new StringBuffer();
			if(ignoreDelivery){
				sb.append("/cart/settleUp");
			} else {
				sb.append("/buy/delivery");
			}
			sb.append(CommonStandard.DEFAULT_PAGE_SUFFIX);
			sb.append('?');
			sb.append("tid=");
			sb.append(item.getTransactionId());
			sb.append("&ignoreDelivery=");
			sb.append(ignoreDelivery);

			logger.info("当前是直接购买模式，下一步地址:" + sb.toString());
			map.put("jump", sb.toString());
		} else {
			ItemCriteria itemCriteria2 = new ItemCriteria(frontUser.getOwnerId());
			itemCriteria2.setChargeFromAccount(frontUser.getUuid());
			itemCriteria2.setCartId(item.getCartId());
			itemMap = cartService.map(itemCriteria2);
			map.put("itemMap", itemMap);
			map.put("cartId", cart.getCartId());

			itemCriteria2 = null;
		}
		return 	"cart/list";

	}


	/*
	 * 结算
	 */
	@RequestMapping(value="/settleUp")
	public String  settleUp(HttpServletRequest request, HttpServletResponse response, ModelMap map
			) throws Exception {
		logger.debug("开始结算步骤");
		map.put("operate", OperateCode.ORDER_SETTLEUP.toString());
		////////////////////////////标准检查流程 ///////////////////////////////////
		User frontUser =  certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());

		if(frontUser == null || frontUser.getCurrentStatus() != UserStatus.normal.getId()){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录，请先登录"));			
			return frontMessageView;

		}
		long ownerId = NumericUtils.parseLong(map.get("ownerId"));

		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),"系统异常","请尝试访问其他页面或返回首页"));
			return frontMessageView;		
		}

		if(frontUser.getOwnerId() != ownerId){
			logger.error("用户[" + frontUser.getUuid() + "]的ownerId[" + frontUser.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(), "您尚未登录，请先登录"));			
			return frontMessageView;
		}
		//////////////////////////// 标准检查流程结束 ///////////////////////////////
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
				map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(), "您未选择任何商品"));
				return frontMessageView;
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
						map.put("message", new EisMessage(EisError.dataError.getId(), "您选择的订单已结束[" + i.getCurrentStatus() + "]"));
						return frontMessageView;
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
				map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(), "您未选择任何商品"));
				return frontMessageView;
			}
			itemCriteria2.setCurrentStatus(TransactionStatus.inCart.getId());
			cartMap = cartService.map(itemCriteria2);
			if(cartMap == null || cartMap.size() < 1){//购物车是空的
				map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.getId(), "您尚未购买任何产品，请先购买"));
				return frontMessageView;
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
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.getId(), "您尚未购买任何产品，请先购买"));
			return frontMessageView;
		}


		if(submitCart.size() < 1){
			logger.error("在待结算购物车中，找不到任何一个提交订单:" + StringTools.mergeArray(tids));
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.getId(), "找不到您提交的订单，请重新购买"));
			return frontMessageView;
		}

		boolean toSelectDelivery = false;
		boolean toSelectSelfCarry = false;
		String submitOrderIds = "";
		//	logger.info("UUUUUUUUUUsubmitCart大小:" + submitCart.size() + ",当前系统代码是:" + configService.getSystemCode());

		for(Item item : submitCart.values()){
			if(item != null){


				if(!ignoreDelivery){

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
		//把已提交订单设置为等待支付
		//产生一个新的购物车，相当于一个总订单
		Cart order = null;
		DeliveryOrder deliveryOrder = null;

		if(orderId < 1){
			order = new Cart(frontUser.getOwnerId());
			order.setCurrentStatus(TransactionStatus.waitingPay.getId());
			order.setUuid(buyUuid);
		} else {
			Cart cart = cartService.select(orderId);
			if(cart.getDeliveryOrderId() > 0){
				deliveryOrder = deliveryOrderService.select(cart.getDeliveryOrderId());
			}
			if( (toSelectDelivery || toSelectSelfCarry)  && deliveryOrder == null){
				logger.debug("订单[" + cart.getCartId() + "]需要配送或自提但是并未设置配送信息");
				map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.getId(), "您的订单需要提供配送信息，请返回选择地址"));
				return frontMessageView;
			}
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
		money = moneyService.select(buyUuid, ownerId);	

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

			if(price != null && PriceType.PRICE_PROMOTION.toString().equals(price.getPriceType())){
				//尝试根据price的identify得到对应的活动
				String activityCode = price.getIdentify();
				if(StringUtils.isBlank(activityCode)){
					logger.debug("促销价格[" + price.getPriceId() + "]没有指定的identify，不再查找对应的活动");
				} else {
					Activity activity = activityService.select(activityCode, ownerId);
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
								map.put("message", new EisMessage(EisError.activityLimited.id, "您无法购买该活动商品，活动异常错误"));
								return frontMessageView;	
							}

							if(checkResult.getOperateCode() != OperateResult.success.getId()){
								logger.error("用户[" + frontUser.getUuid() + "]无法通过活动处理器[" + processor + "]的检查，返回结果是:" + checkResult);
								map.put("message",checkResult);
								return frontMessageView;	
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
		if(order.getTtl() == 0){
			order.setTtl(cartTtl);
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

			PayTypeCriteria payTypeCriteria = new PayTypeCriteria(order.getOwnerId());
			payTypeCriteria.setFlag(MoneyType.coin.getId());
			List<PayType> payTypeList = payTypeService.list(payTypeCriteria);
			PayType coinPayType = null;
			if(payTypeList.size() < 1){
				logger.info("系统未配置一个[flag=" + MoneyType.coin.getId() + "]的coin支付方式,无法支付所需coin=" + needCoin);
				map.put("message", new EisMessage(EisError.moneyNotEnough.id,"您的资金不足"));
				map.put("needCoin", needCoin);
				return frontMessageView;	

			} 
			needCoinPay = true;
			coinPayType = payTypeList.get(0);
			Pay fakePay = new Pay();
			fakePay.setPayTypeId(coinPayType.getPayTypeId());
			fakePay.setContextType(ContextTypeUtil.getContextType(request));
			coinPayProcessor = payService.getProcessor(fakePay);
			logger.info("系统当前配置的coin支付方式是:" + coinPayType.getPayTypeId() + ":" + coinPayType.getName() + ",处理器是:" + coinPayProcessor);
			if(coinPayProcessor == null){
				logger.error("找不到系统指定的coin支付方式[" + coinPayType.getPayTypeId() + "对应的处理器");
				map.put("message", new EisMessage(-EisError.payProcessorIsNull.id,"找不到指定的支付处理程序"));
				return frontMessageView;	

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

		boolean forceUseGiftMoneyFirst = configService.getBooleanValue(DataName.forceUseGiftMoneyFirst.toString(), ownerId);

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

		Price orderPrice = new Price(order.getOwnerId());
		int totalProduct  = 0;
		int totalGoods = 0;
		StringBuffer goodsDesc = new StringBuffer();
		StringBuffer sb = new StringBuffer();

		AddressBook addressBook = null;
		if(addressBookId > 0){
			addressBook = addressBookService.select("" + addressBookId);
			if(addressBook == null){
				logger.error("找不到指定的快递地址:" + addressBookId);
				map.put("message", new EisMessage(EisError.BILL_CREATE_FAIL.id,"找不到指定的配送地址"));
				return frontMessageView;
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
					map.put("message", new EisMessage(EisError.BILL_CREATE_FAIL.id,"找不到指定的配送地址"));
					return frontMessageView;
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

			int availableCount = stockService.getAvaiableCount(t.getObjectType(),t.getProductId());
			if(availableCount == 0){
				logger.error("交易:{}对应的商品:{}/{}的数量为0，无法购买", t.getTransactionId(), t.getObjectType(), t.getProductId());
				map.put("message", new EisMessage(EisError.amountError.getId(),"商品数量不足"));
				return settleUpView;		
			}
			if(availableCount != -1){
				/*logger.debug("准备将产品[" + product.getProductId() + "]的数量[" + product.getAvailableCount() + "]-" + t.getCount());
				long lockAmount = productService.writeAmount(product, -t.getCount(), false);
				if(lockAmount < -1){
					logger.error("无法修改购物车中的商品:" + product.getProductId() + "/" + product.getProductCode() + "]数量-" + t.getCount() + ",修改返回是:" + lockAmount);
					map.put("message", new EisMessage(EisError.amountError.getId(),"商品数量不足"));
					return settleUpView;		
				}*/
			} else {
				logger.debug("交易:{}对应的商品:{}/{}的数量为-1，无数量限制", t.getTransactionId(), t.getObjectType(), t.getProductId());
			}

			EOEisObject targetObject = stockService.getTargetObject(t.getObjectType(), t.getProductId());
			if(targetObject == null) {

			} else {
				String validatorClass = targetObject.getExtraValue(DataName.productValidator.toString());

				if(StringUtils.isNotBlank(validatorClass)){
					ProductValidator productValidator = applicationContextService.getBeanGeneric(StringUtils.uncapitalize(validatorClass));
					if(productValidator == null){
						logger.error("产品[" + t.getProductId() + "]定义了校验器[" + validatorClass + "]但找不到该校验器");
					} else {
						int validateResult = productValidator.validate(Operate.settleUp.getCode(),t, request);
						if(validateResult != OperateResult.success.getId()){
							logger.warn("交易品无法通过校验[" + validateResult + "]");
							if(validateResult ==  EisError.COUNT_LIMIT_EXCEED.id){
								map.put("message", new EisMessage(validateResult,"您购买该产品的次数已超过限制，暂时无法购买"));
							} else if(validateResult ==  EisError.moneyNotEnough.id){
								map.put("message", new EisMessage(validateResult,"您的金钱不足，暂时无法购买"));
							}  else if(validateResult ==  EisError.moneyRangeError.id){
								map.put("message", new EisMessage(validateResult,"您的金钱低于新用户赠送资金，暂时无法购买"));
							} 
							return frontMessageView;					
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
			logger.debug("更新数据库中的购买订单[" + item.getTransactionId() + "]:" +  rs);
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

		order.setExtraValue("totalPayMoney", String.valueOf(bd.floatValue()));
		if(deliveryOrder != null && deliveryOrder.getFee() != null){
			order.setExtraValue(DataName.deliveryFee.toString(), String.valueOf(deliveryOrder.getFee().getMoney()));
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


			cartService.finish(order);
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
