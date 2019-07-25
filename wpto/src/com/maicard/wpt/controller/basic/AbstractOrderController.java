package com.maicard.wpt.controller.basic;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.time.DateUtils;

import com.maicard.common.base.BaseController;
import com.maicard.common.base.UUIDFilenameGenerator;
import com.maicard.common.criteria.CommentConfigCriteria;
import com.maicard.common.domain.Comment;
import com.maicard.common.domain.CommentConfig;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.CommentConfigService;
import com.maicard.common.service.CommentService;
import com.maicard.common.util.ClassUtils;
import com.maicard.common.util.HttpUtils;
import com.maicard.common.util.NumericUtils;
import com.maicard.common.util.Paging;
import com.maicard.common.util.PagingUtils;
import com.maicard.ec.domain.AddressBook;
import com.maicard.ec.domain.DeliveryOrder;
import com.maicard.ec.service.AddressBookService;
import com.maicard.ec.service.DeliveryOrderService;
import com.maicard.exception.EisException;
import com.maicard.mb.service.MessageService;
import com.maicard.money.criteria.CouponCriteria;
import com.maicard.money.domain.Coupon;
import com.maicard.money.service.CouponService;
import com.maicard.product.criteria.CartCriteria;
import com.maicard.product.criteria.ItemCriteria;
import com.maicard.product.domain.Activity;
import com.maicard.product.domain.Cart;
import com.maicard.product.domain.Item;
import com.maicard.product.domain.Product;
import com.maicard.product.domain.ProductData;
import com.maicard.product.service.ActivityService;
import com.maicard.product.service.CartService;
import com.maicard.product.service.ItemService;
import com.maicard.product.service.ProductService;
import com.maicard.security.domain.User;
import com.maicard.security.service.AuthorizeService;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.FrontUserService;
import com.maicard.security.service.PartnerService;
import com.maicard.security.service.UuidMapService;
import com.maicard.site.domain.Document;
import com.maicard.site.service.NodeProcessor;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.ObjectType;
import com.maicard.standard.OperateCode;
import com.maicard.standard.OperateResult;
import com.maicard.standard.PriceType;
import com.maicard.standard.SecurityStandard.UserStatus;
import com.maicard.standard.SecurityStandard.UserTypes;
import com.maicard.standard.TransactionStandard.TransactionStatus;

import org.springframework.ui.ModelMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import static com.maicard.standard.CommonStandard.frontMessageView;

/**
 * 用户交易数据
 * 
 * 
 * @author NetSnake
 *
 */
@Controller
public abstract class AbstractOrderController extends BaseController{


	@Resource
	protected ApplicationContextService applicationContextService;

	@Resource
	protected ActivityService activityService;

	@Resource
	protected AuthorizeService authorizeService;

	@Resource
	protected CartService cartService;

	@Resource
	protected CommentService commentService;

	@Resource
	protected CertifyService certifyService;

	@Resource
	protected CommentConfigService commentConfigService;

	@Autowired(required=false)
	protected DeliveryOrderService deliveryOrderService;
	
	@Autowired(required=false)
	protected AddressBookService addressBookService;
		


	@Autowired(required=false)
	protected UuidMapService uuidMapService;

	@Resource
	protected MessageService messageService;


	@Resource
	protected CouponService couponService;

	@Resource
	protected FrontUserService frontUserService;

	@Resource
	protected ItemService itemService;

	@Resource
	protected PartnerService partnerService;
	


	@Resource
	protected ProductService productService;

	//private static SimpleDateFormat sdf = new SimpleDateFormat(CommonStandard.defaultDateFormat);

	private int LITTLE_DIVISOR = 56;
	private int BIG_DIVISOR = 110;

	private final int CARD_LENGTH = 16;

	private String userUploadDir;

	protected final String listView = "order/index";

	private String globalServiceQrCode = "service_qr_code.png";

	private String[] extraNodeWriter = new String[]{"tuanDefaultProcessor"};

	/**
	 * 1为自动兑奖模式
	 * 2为手工兑奖，只返回客服二维码
	 */
	private int SYSTEM_EXCHANGE_MODE = 1;
	
	protected int[] allowOtherUserView = {};



	//按cart查询用户的订单
	@RequestMapping(value={"","/index"})
	public String listByCart(HttpServletRequest request, HttpServletResponse response, ModelMap map, CartCriteria cartCriteria) throws Exception {
		long ownerId = NumericUtils.parseLong(map.get("ownerId"));

		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),"系统异常","请尝试访问其他页面或返回首页"));
			return frontMessageView;		
		}
		User frontUser = null;
		try{
			frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
		}catch(Exception e){
		}

		if(frontUser == null || frontUser.getCurrentStatus() != UserStatus.normal.getId()){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录，请先登录"));			
			return frontMessageView;

		}
		if(frontUser.getOwnerId() != ownerId){
			logger.error("用户[" + frontUser.getUuid() + "]的ownerId[" + frontUser.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(), "您尚未登录，请先登录"));			
			return frontMessageView;
		}
		long buyUuid = 0;
		if(uuidMapService != null){
			String appCode = ServletRequestUtils.getStringParameter(request, "appCode");

			buyUuid = uuidMapService.getMoneyUuid(frontUser,appCode);
		} else {
			buyUuid = frontUser.getUuid();
		}

		Cart currentCart = cartService.getCurrentCart(buyUuid, PriceType.PRICE_STANDARD.toString(), CartCriteria.ORDER_TYPE_TEMP, null, frontUser.getOwnerId(), false);
		if(currentCart != null){
			map.put("cartCount", currentCart.getTotalGoods());
		} else {
			map.put("cartCount", 0);
		}

		cartCriteria.setUuid(frontUser.getUuid());
		cartCriteria.setOrderType(CartCriteria.ORDER_TYPE_STORE);

		int totalRows = cartService.count(cartCriteria);
		if(totalRows < 1){
			logger.debug("当前返回的数据数量是0");
			return listView;	
		}

		int rows = ServletRequestUtils.getIntParameter(request, "rows", ROW_PER_PAGE);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);
		Paging paging = null;


		paging = new Paging(rows);
		paging.setCurrentPage(page);

		cartCriteria.setPaging(paging);

		List<Cart> orderList = cartService.listOnPage(cartCriteria);
		logger.debug("用户[" + buyUuid + "]查询到的订单数量是:" + (orderList == null ? "空" : orderList.size()));
		map.put("paging", PagingUtils.generateContentPaging(totalRows, rows, page));

		if(orderList == null || orderList.size() < 1){
			return listView;	
		}
		for(Cart cart : orderList){
			ItemCriteria itemCriteria = new ItemCriteria();
			itemCriteria.setCartId(cart.getCartId());
			if(cart.getCreateTime() != null){
				itemCriteria.setEnterTimeBegin(DateUtils.truncate(cart.getCreateTime(), Calendar.DAY_OF_MONTH));
				itemCriteria.setEnterTimeEnd(DateUtils.ceiling(cart.getCreateTime(), Calendar.DAY_OF_MONTH));
			}
			List<Item> itemList = itemService.list(itemCriteria);
			logger.debug("订单[" + cart.getCartId() + "]对应的交易品数量是:" + (itemList == null ? "空" : itemList.size()));
			if(itemList != null && itemList.size() > 0 ){
				for(Item item : itemList){
					item.setChargeFromAccountName(frontUser.getNickName());		
					applyRefUrl(item);
				}
				cart.setMiniItemList(itemList);
				logger.debug("订单的miniItemList是： " + cart.getMiniItemList());
			}
		}

		map.put("orderList", orderList);

		return listView;	
	}

	//按item查询用户的订单
	@RequestMapping(value={"/item","/item/index"})
	public String listByItem(HttpServletRequest request, HttpServletResponse response, ModelMap map, ItemCriteria itemCriteria) throws Exception {

		final String view = "order/item";
		long ownerId = NumericUtils.parseLong(map.get("ownerId"));

		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),"系统异常","请尝试访问其他页面或返回首页"));
			return frontMessageView;		
		}
		User frontUser = null;
		try{
			frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
		}catch(Exception e){
		}

		if(frontUser == null || frontUser.getCurrentStatus() != UserStatus.normal.getId()){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录，请先登录"));			
			return view;

		}
		if(frontUser.getOwnerId() != ownerId){
			logger.error("用户[" + frontUser.getUuid() + "]的ownerId[" + frontUser.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(), "您尚未登录，请先登录"));			
			return view;
		}
		long buyUuid = 0;
		if(uuidMapService != null){
			String appCode = ServletRequestUtils.getStringParameter(request, "appCode");

			buyUuid = uuidMapService.getMoneyUuid(frontUser,appCode);
		} else {
			buyUuid = frontUser.getUuid();
		}



		itemCriteria.setChargeFromAccount(frontUser.getUuid());

		int totalRows = itemService.count(itemCriteria);
		if(totalRows < 1){
			logger.debug("当前返回的数据数量是0");
			return view;	
		}

		int rows = ServletRequestUtils.getIntParameter(request, "rows", ROW_PER_PAGE);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);
		Paging paging = null;


		paging = new Paging(rows);
		paging.setCurrentPage(page);

		itemCriteria.setPaging(paging);
		//加一个根据Item订单状态查询
		int status = ServletRequestUtils.getIntParameter(request, "status",-1);
		logger.debug("要查询的订单状态{}",status);
		if(status==TransactionStatus.deliveryConfirmed.id || status==TransactionStatus.waitingComment.id ){
			int[] cArr = new int[]{TransactionStatus.deliveryConfirmed.id,TransactionStatus.waitingComment.id};
			itemCriteria.setCurrentStatus(cArr);
		}else if(status==TransactionStatus.commentClosed.id){
			itemCriteria.setCurrentStatus(status);
		}
		List<Item> orderList = itemService.listOnPage(itemCriteria);
		logger.debug("用户[" + buyUuid + "]查询到的Item订单数量是:" + (orderList == null ? "空" : orderList.size()));
		map.put("paging", PagingUtils.generateContentPaging(totalRows, rows, page));


		map.put("orderList", orderList);

		if(extraNodeWriter != null && extraNodeWriter.length > 0){
			for(String nodeWriter : extraNodeWriter){
				NodeProcessor nodeProcessor = applicationContextService.getBeanGeneric(nodeWriter);
				if(nodeProcessor != null){
					nodeProcessor.writeExtraData(map, frontUser, null);
				}
			}
		}

		return view;	
	}

	//查询用户购买记录
	@RequestMapping(value="/get")
	public String get(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		map.put("operate", OperateCode.GET_ORDER.toString());
		long ownerId = NumericUtils.parseLong(map.get("ownerId"));
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			throw new EisException(EisError.systemDataError.id,"系统异常");
		}
		User frontUser =  certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());


		if(frontUser == null || frontUser.getCurrentStatus() != UserStatus.normal.getId()){
			throw new EisException(EisError.userNotFoundInSession.id,"您尚未登录，请先登录");

		}
		if(frontUser.getOwnerId() != ownerId){
			logger.error("用户[" + frontUser.getUuid() + "]的ownerId[" + frontUser.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			throw new EisException(EisError.systemDataError.id,"系统异常");
		}

		String cid = ServletRequestUtils.getStringParameter(request,"cartId",null);
		if(StringUtils.isBlank(cid)){
			logger.debug("尝试查询订单但未提供购物车数字");
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.id,"请提供要查询的交易号"));
			return frontMessageView;
		}
		if(!NumericUtils.isNumeric(cid)){
			logger.debug("尝试查询订单购物车ID不是数字");
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.id,"请提供正确的交易号，交易号是一串纯数字"));
			return frontMessageView;
		}
		int cartId = Integer.parseInt(cid);
		Cart cart = cartService.select(cartId);
		if(cart == null){
			logger.error("找不到cartId=" + cartId + "的购物车订单");
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.id,"找不到指定的订单"));
			return frontMessageView;	
		}
		long buyUuid = 0;
		if(uuidMapService != null){
			String appCode = ServletRequestUtils.getStringParameter(request, "appCode");

			buyUuid = uuidMapService.getMoneyUuid(frontUser,appCode);
		} else {
			buyUuid = frontUser.getUuid();
		}
		boolean isOtherUser = false;
		boolean allowView = false;
		if(cart.getUuid() != buyUuid){
			isOtherUser = true;
			if(allowOtherUserView.length > 0) {
				//允许其他用户查看其他人的哪些状态得订单详情
				for(int status : allowOtherUserView) {
					if(status == cart.getCurrentStatus()) {
						logger.info("当前是其他人:{}查看用户:{}的订单:{}，状态为:{}，允许查看",frontUser.getUuid(), cart.getUuid(), cart.getCartId(), cart.getCurrentStatus());
						allowView = true;
						break;
					}
				}
				map.put("isOtherUser", 1);

			} 
			
		} else {
			allowView = true;
		}
		if(!allowView) {
			{
				logger.error("cartId=" + cartId + "的购物车其用户[" + cart.getUuid() + "]与当前用户[" + buyUuid + "]不一致");
				map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.id,"找不到指定的订单"));
				return frontMessageView;	
			}
		}
		if(cart.getDeliveryOrderId() > 0){
			DeliveryOrder deliveryOrder = deliveryOrderService.select(cart.getDeliveryOrderId());
			if(deliveryOrder == null){
				logger.warn("找不到订单[" + cart.getCartId() + "]对应的配送ID:" + cart.getDeliveryOrderId());
			} else {
				User partner = partnerService.select(deliveryOrder.getDeliveryCompanyId());
				StringBuffer sb = new StringBuffer();
				if(partner != null){
					sb.append("快递公司:").append(partner.getNickName());
					if(StringUtils.isNotBlank(deliveryOrder.getOutOrderId())){
						sb.append(',').append("快递单号:").append(deliveryOrder.getOutOrderId());
					}
				} else {
					sb.append("没有信息");
				}
				deliveryOrder.setMemory(sb.toString());
				map.put("deliveryOrder", deliveryOrder);
			}
		} else {
			logger.warn("订单[" + cart.getCartId() + "]没有对应配送ID");
		}
		
		long addressId = cart.getLongExtraValue("addressBookId");
		if(!isOtherUser && addressId > 0) {
			if(addressBookService != null) {
			AddressBook ab = addressBookService.select(String.valueOf(addressId));
			if(ab == null) {
				logger.error("找不到订单:{}的配送地址:{}", cart.getCartId(), addressId);
			} else {
				map.put("addressBook", ab);
			}
			
			} else {
				logger.error("订单:{}有一个地址但是系统没有注入addressBookService", cart.getCartId());
			}
		}
		ItemCriteria itemCriteria = new ItemCriteria();
		itemCriteria.setCartId(cart.getCartId());
		if(cart.getCreateTime() != null){
			itemCriteria.setEnterTimeBegin(DateUtils.truncate(cart.getCreateTime(), Calendar.DAY_OF_MONTH));
			itemCriteria.setEnterTimeEnd(DateUtils.ceiling(cart.getCreateTime(), Calendar.DAY_OF_MONTH));
		}
		List<Item> itemList = itemService.list(itemCriteria);
		logger.debug("订单[" + cart.getCartId() + "]对应的交易品数量是:" + (itemList == null ? "空" : itemList.size()));
		if(itemList != null && itemList.size() > 0 ){
			
			for(Item item : itemList){
				item.setChargeFromAccountName(frontUser.getNickName());
				//applyRefUrl(item);

			}
			cart.setMiniItemList(itemList);
		}
		

		/*
		for(Item item : itemList){
			if(item.getOwnerId() != frontUser.getOwnerId()){
				logger.error("交易号[" + cid + "]对应的商品[" + item.getTransactionId() + "]其ownerId[" + item.getOwnerId() + "]与查询用户[" + frontUser.getUuid() + "]的ownerId[" + frontUser.getOwnerId() + "]不一致");
				return "item/detail";
			}
		}*/
		map.put("order", cart);

		return "order/detail";

	}

	

	private void applyRefUrl(Item item) {
		if(StringUtils.isNotBlank(item.getExtraValue(DataName.refUrl.toString()))){
			return;
		}
		Document refDocument = null;
		if(item.getPrice() != null && StringUtils.equals(item.getPrice().getPriceType(), PriceType.PRICE_PROMOTION.toString())){
			//交易来自活动，应该跳往活动界面
			String activityCode = item.getPrice().getIdentify();
			if(StringUtils.isBlank(activityCode)){
				logger.debug("促销价格[" + item.getPrice().getPriceId() + "]没有指定的identify，不再查找对应的活动");
			} else {
				Activity activity = activityService.select(activityCode, item.getOwnerId());
				if(activity == null){
					logger.warn("根据促销价格[" + item.getPrice().getPriceId() + "]指定的identify[" + item.getPrice().getIdentify() + "]找不到任何活动");
				} else {
					refDocument = activityService.getRefDocument(activity);
				}
			}					
		}
		if(refDocument == null){
			Product product = productService.select(item.getProductId());
			if(product != null){
				try {
					refDocument = productService.getRefDocument(product);
				} catch (Exception e) {
					e.printStackTrace();
				}					
			}
		}
		if(refDocument != null){
			item.setExtraValue(DataName.refUrl.toString(), refDocument.getViewUrl());
			item.setExtraValue(DataName.refTitle.toString(), refDocument.getTitle());
		}

	}


	//查询用户的交易
	@RequestMapping(value="/getByItem")
	public String getByItem(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		map.put("operate", OperateCode.GET_ORDER.toString());
		long ownerId = NumericUtils.parseLong(map.get("ownerId"));

		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),"系统异常","请尝试访问其他页面或返回首页"));
			return frontMessageView;		
		}
		User frontUser = null;
		try{
			frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
		}catch(Exception e){
		}

		if(frontUser == null || frontUser.getCurrentStatus() != UserStatus.normal.getId()){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录，请先登录"));			
			return frontMessageView;

		}
		if(frontUser.getOwnerId() != ownerId){
			logger.error("用户[" + frontUser.getUuid() + "]的ownerId[" + frontUser.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(), "您尚未登录，请先登录"));			
			return frontMessageView;
		}

		map.put("frontUser", frontUser);

		String tid = ServletRequestUtils.getStringParameter(request, "tid",null);
		if(StringUtils.isBlank(tid)){
			logger.debug("尝试查询订单但未提供交易ID");
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.id,"请提供要查询的交易号"));
			return frontMessageView;
		}

		Item item = itemService.select(tid);
		if(item == null){
			logger.error("找不到transactionId==" + tid + "的交易");
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.id,"找不到指定的订单"));
			return frontMessageView;	
		}
		if(item.getChargeFromAccount() != frontUser.getUuid()){
			logger.error("找不到transactionId==" + tid + "的交易");
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.id,"找不到指定的订单"));
			return frontMessageView;	
		}
		map.put("item", item);
		long deliveryOrderId = item.getLongExtraValue(DataName.deliveryOrderId.toString());
		if(deliveryOrderId > 0){
			DeliveryOrder deliveryOrder = deliveryOrderService.select(deliveryOrderId);
			if(deliveryOrder == null){
				logger.warn("找不到订单[" + tid + "]对应的配送ID:" + deliveryOrderId);
			} else {
				map.put("deliveryOrder", deliveryOrder);
			}
		} else {
			logger.warn("交易[" + tid + "]没有对应配送ID");
		}



		/*Product product = productService.select(item.getProductId());
		if(product != null){
			Document document = null;
			try {
				document = productService.getRefDocument(product);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(document != null){
				item.setExtraValue(DataName.refUrl.toString(), document.getViewUrl());
			}
		}*/
		return "order/itemDetail";

	}

	//显示评论提交界面
	@RequestMapping(value="/addComment")
	public String addComment(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),"系统异常","请尝试访问其他页面或返回首页"));
			return frontMessageView;		
		}
		User frontUser = null;
		try{
			frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
		}catch(Exception e){
		}

		if(frontUser == null || frontUser.getCurrentStatus() != UserStatus.normal.getId()){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录，请先登录"));			
			return frontMessageView;

		}
		if(frontUser.getOwnerId() != ownerId){
			logger.error("用户[" + frontUser.getUuid() + "]的ownerId[" + frontUser.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(), "您尚未登录，请先登录"));			
			return frontMessageView;
		}
		Map<String,String> requestDataMap = HttpUtils.getRequestDataMap(request);
		requestDataMap.put("userAgent", request.getHeader("User-Agent"));
		return _addComment(requestDataMap, frontUser, map);

	}

	private String _addComment(Map<String,String> requestDataMap, User frontUser, ModelMap map){

		final String view = "order/addComment";


		String tid = requestDataMap.get("tid");
		if(StringUtils.isBlank(tid)){
			logger.debug("尝试查询订单但未提供订单ID");
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.id,"请提供要查询的交易ID"));
			return frontMessageView;
		}

		Item item = itemService.select(tid);
		if(item == null){
			logger.error("找不到指定的交易:" + tid);
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.id,"找不到指定的交易"));
			return frontMessageView;

		}
		if(item.getChargeFromAccount() != frontUser.getUuid()){
			logger.error("指定的交易:" + tid + "是:" + item.getChargeFromAccount() + "的订单，不属于用户:" + frontUser.getUuid());
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.id,"找不到指定的交易"));
			return frontMessageView;

		}
		if(item.getCurrentStatus() == TransactionStatus.commentClosed.id){
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.id,"您已经做出了评价，不能再次评价"));
			return frontMessageView;
		}
		if(item.getCurrentStatus() != TransactionStatus.deliveryConfirmed.id && item.getCurrentStatus() != TransactionStatus.waitingComment.id){
			logger.error("交易[" + tid + "]状态是:" + item.getCurrentStatus() + "]，不允许评论");
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.id,"暂时不能评价"));
			return frontMessageView;
		}

		Product product = productService.select(item.getProductId());
		if(product != null){
			if(item.getItemDataMap() == null){
				item.setItemDataMap(new HashMap<String,ProductData>());
			}
			String logo = product.getExtraValue(DataName.productSmallImage.toString());
			if(logo != null){
				item.getItemDataMap().put(DataName.productSmallImage.toString(), new ProductData(DataName.productSmallImage.toString(), logo));
			}
		}

		Cart cart = cartService.select(item.getCartId());
		if(cart == null){
			logger.error("找不到交易[" + item.getTransactionId() + "]对应的订单:" + item.getCartId() );
		} else {
			if(cart.getDeliveryOrderId() > 0){
				DeliveryOrder deliveryOrder = deliveryOrderService.select(cart.getDeliveryOrderId());
				if(deliveryOrder == null){
					logger.error("找不到订单[" + item.getCartId() + "]对应的快递单");
				} else {
					map.put("deliveryOrder",deliveryOrder);
				}
			}
		}

		map.put("item", item);
		CommentConfigCriteria commentConfigCriteria = new CommentConfigCriteria(frontUser.getOwnerId());
		commentConfigCriteria.setObjectType(ObjectType.product.name());
		commentConfigCriteria.setObjectId(item.getProductId());
		commentConfigCriteria.setWithGlobalConfig(true);
		List<CommentConfig> commentConfigList = commentConfigService.list(commentConfigCriteria);
		if(commentConfigList == null || commentConfigList.size() < 1){
			logger.warn("没有针对产品[" + item.getProductId() + "]的评论配置");
		} else {
			CommentConfig commentConfig = commentConfigList.get(0);
			logger.debug("针对产品[" + item.getProductId() + "]的评论配置是:" + commentConfig);
			map.put("commentConfig", commentConfig);
		}

		return view;

	}


	//提交一个评论
	@RequestMapping(value="/submitComment")
	public String submitComment(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),"系统异常","请尝试访问其他页面或返回首页"));
			return frontMessageView;		
		}
		User frontUser = null;
		try{
			frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
		}catch(Exception e){
		}

		if(frontUser == null || frontUser.getCurrentStatus() != UserStatus.normal.getId()){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录，请先登录"));			
			return frontMessageView;

		}
		if(frontUser.getOwnerId() != ownerId){
			logger.error("用户[" + frontUser.getUuid() + "]的ownerId[" + frontUser.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(), "您尚未登录，请先登录"));			
			return frontMessageView;
		}
		Map<String,String> requestDataMap = HttpUtils.getRequestDataMap(request);
		requestDataMap.put("userAgent", request.getHeader("User-Agent"));
		return _submitComment(requestDataMap, frontUser, request, map);

	}
	private String _submitComment(Map<String,String> requestDataMap, User frontUser, HttpServletRequest request, ModelMap map){

		final String view = "order/submitComment";

		Comment comment = new Comment();
		ClassUtils.bindBeanFromMap(comment, requestDataMap);


		String tid = requestDataMap.get("tid");
		if(StringUtils.isBlank(tid)){
			logger.debug("尝试查询订单但未提供订单ID");
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.id,"请提供要查询的交易ID"));
			return frontMessageView;
		}

		Item item = itemService.select(tid);
		if(item == null){
			logger.error("找不到指定的交易:" + tid);
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.id,"请提供要查询的交易ID"));
			return frontMessageView;

		}
		if(item.getCurrentStatus() == TransactionStatus.commentClosed.id){
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.id,"您已经做出了评价，不能再次评价"));
			return frontMessageView;
		}
		if(item.getCurrentStatus() != TransactionStatus.deliveryConfirmed.id && item.getCurrentStatus() != TransactionStatus.waitingComment.id){
			logger.error("交易[" + tid + "]状态是:" + item.getCurrentStatus() + "]，不允许评论");
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.id,"还不能评价"));
			return frontMessageView;
		}

		comment.setOwnerId(frontUser.getOwnerId());
		comment.setUuid(frontUser.getUuid());
		comment.setCreateTime(new Date());
		comment.setObjectType(ObjectType.product.name());
		comment.setObjectId(item.getProductId());

		CommentConfig commentConfig = commentService.getCommentConfig(comment);
		if(commentConfig == null){
			logger.warn("找不到针对对象[" + comment.getObjectType() + "#" + comment.getObjectId() + "]的评论配置，将不允许发布评论");
			map.put("message", new EisMessage(EisError.ACCESS_DENY.getId(), "对不起，暂时不允许发表评论"));
			return frontMessageView;
		}



		comment.setCommentConfigId(commentConfig.getCommentConfigId());

		if(commentConfig.getExtraDataDefine() == null || commentConfig.getExtraDataDefine().size() < 1){
			logger.info("commentConfig[" + commentConfig.getCommentConfigId() + "]未定义扩展数据配置");
		} else {
			if (request instanceof MultipartHttpServletRequest) {
				logger.info("请求中带有附件，使用文件上传处理.");
				this.fileUpload(request, "create", comment, commentConfig);
			}
			for (String key : commentConfig.getExtraDataDefine().keySet()) {
				logger.debug("尝试获取扩展数据:" + key);
				String valueDefine = commentConfig.getExtraDataDefine().get(key);
				if(valueDefine != null && !valueDefine.equalsIgnoreCase("string")){
					logger.debug("扩展数据的定义不应当直接输入:" + valueDefine);
					continue;
				}
				String documentDataStr = null;
				documentDataStr = ServletRequestUtils.getStringParameter(request, key, null);

				if (StringUtils.isBlank(documentDataStr)) {
					logger.debug("数据规范[" + key+ "]没有提交数据");
					continue;
				}
				logger.debug("数据规范[" + key + "]提交的数据是[" + documentDataStr + "]");


				
				logger.debug("尝试插入自定义评论数据[" + key + "]，数据内容:[" 	+ documentDataStr + "]");
				comment.setExtraValue(key, documentDataStr);
			}
		}
		comment.setExtraValue("transactionId", item.getTransactionId());

		comment.setExtraValue(DataName.userRealName.toString(), frontUser.getNickName() == null ? frontUser.getUsername() : frontUser.getNickName());
		comment.setExtraValue(DataName.userHeadPic.toString(), frontUser.getExtraValue(DataName.userHeadPic.toString()));

		Document refDocument = null;
		if(item.getPrice() != null && StringUtils.equals(item.getPrice().getPriceType(), PriceType.PRICE_PROMOTION.toString())){
			//交易来自活动，应该跳往活动界面
			String activityCode = item.getPrice().getIdentify();
			if(StringUtils.isBlank(activityCode)){
				logger.debug("促销价格[" + item.getPrice().getPriceId() + "]没有指定的identify，不再查找对应的活动");
			} else {
				Activity activity = activityService.select(activityCode, item.getOwnerId());
				if(activity == null){
					logger.warn("根据促销价格[" + item.getPrice().getPriceId() + "]指定的identify[" + item.getPrice().getIdentify() + "]找不到任何活动");
				} else {
					refDocument = activityService.getRefDocument(activity);
					logger.warn("根据促销价格[" + item.getPrice().getPriceId() + "]指定的identify[" + item.getPrice().getIdentify() + "]找到的活动对应文档是:" + refDocument);
				}
			}					
		}
		if(refDocument == null){
			Product product = productService.select(item.getProductId());
			if(product != null){
				try {
					refDocument = productService.getRefDocument(product);
				} catch (Exception e) {
					e.printStackTrace();
				}					
			}
		}
		if(refDocument != null){
			comment.setExtraValue(DataName.refUrl.toString(), refDocument.getViewUrl());
			comment.setExtraValue(DataName.refTitle.toString(), refDocument.getTitle());
		}


		EisMessage rs = commentService.insert(comment);
		logger.debug("用户[" + frontUser.getUuid() + "]针对对象[" + comment.getObjectType() + "#" + comment.getObjectId() + "]的评论插入结果:" + rs);
		if (rs.getOperateCode() == OperateResult.success.id) {
			map.put("message",  new EisMessage(OperateResult.success.getId(),"您的评论已提交"));
			item.setCurrentStatus(TransactionStatus.commentClosed.id);
			itemService.changeStatus(item);
			messageService.sendJmsDataSyncMessage(null, "itemService", "changeStatus", item);

			//检查本订单是否都已经是已评论
			/**
			 * 检查该订单对应的交易
			 * 如果有多个交易，检查是否都已经被评论，如果是则将订单状态设置为被评论
			 */
			Cart order = cartService.select(item.getCartId());
			if(order == null){
				logger.warn("找不到交易[" + item.getTransactionId() + "]对应的订单:" + item.getCartId());
				return frontMessageView;
			}

			ItemCriteria itemCriteria = new ItemCriteria(item.getOwnerId());
			if(order.getCreateTime() != null){
				itemCriteria.setEnterTimeBegin(DateUtils.truncate(order.getCreateTime(), Calendar.DAY_OF_MONTH));
				itemCriteria.setEnterTimeEnd(DateUtils.ceiling(order.getCreateTime(), Calendar.DAY_OF_MONTH));
			}
			itemCriteria.setCartId(order.getCartId());
			List<Item> itemList = itemService.list(itemCriteria);

			logger.debug("订单[" + order.getCartId() + "]对应的交易数量是:" + (itemList == null ? "空" : itemList.size()));
			if(itemList == null || itemList.size() < 1){
				logger.error("找不到订单[" + order.getCartId() + "]对应的交易");
				return frontMessageView;
			}
			if(itemList.size() == 1){
				logger.info("订单[" + order.getCartId() + "]对应的交易数量是1,将整个订单状态也改为已评价");
				order.setCurrentStatus(TransactionStatus.commentClosed.id);
				cartService.updateNoNull(order);
				messageService.sendJmsDataSyncMessage(null, "cartService", "updateNoNull", order);

				return frontMessageView;
			} 
			boolean bothConfirmed = true;
			for(Item i : itemList){
				if(i.getCurrentStatus() != TransactionStatus.commentClosed.id ){
					bothConfirmed = false;
					break;
				}
			}
			if(bothConfirmed){
				logger.info("订单[" + order.getCartId()+ "]所有交易都是已评论状态,将该订单状态也改为已评论");
				order.setCurrentStatus(TransactionStatus.commentClosed.id);
				cartService.updateNoNull(order);
				messageService.sendJmsDataSyncMessage(null, "cartService", "updateNoNull", order);
			}
		} else {
			if(rs.getOperateCode() == EisError.dataDuplicate.id){
				map.put("message", new EisMessage(OperateResult.failed.getId(), "对不起，您已经提交了评论，不能再次评论"));
			} else if(rs.getOperateCode() == EisError.subscribeCountError.id){
				map.put("message", new EisMessage(OperateResult.failed.getId(), "对不起，您必须已经购买该产品才能评论"));
			} else if(rs.getOperateCode() == EisError.haveDirtyWord.id){
				map.put("message", rs);
			} else {
				map.put("message", rs);
			}

		}


		return view;

	}




	// 文件上传
	@SuppressWarnings("rawtypes")
	private int fileUpload(HttpServletRequest request, String mode, Comment comment, CommentConfig commentConfig)  {

		Map<String, String> galleryMap = new HashMap<String,String>();
		// 从Spring容器中获取对应的上传文件目录
		// String fileUploadSavePath =
		// ((FileSystemResource)this.getApplicationContext().getBean("uploadSaveDir")).getPath();
		// logger.info("Spring容器中的上传文件目录在:" + fileUploadSavePath);
		// logger.info("尝试为文档[udid="+udid+"]在[" + mode + "]模式下上传附件");
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> map = multiRequest.getFileMap();
		logger.info("上传文件数量:" + (map == null ? "空" : multiRequest.getQueryString()));
		Iterator its = multiRequest.getFileNames();
		int i = 0;
		int addCount = 0;
		int updateCount = 0;
		int ignoreCount = 0;
		while (its.hasNext()) {
			CommonsMultipartFile file = (CommonsMultipartFile) multiRequest.getFile((String) its.next());
			logger.debug("处理上传文件:" + file.getName() + ",大小:" + file.getSize());
			if (file.getSize() == 0) {
				if (i == 0) {
					continue;
				}
				break;
			}
			String key = file.getName();
			String simpleKey = key.replaceAll("\\d+$", "");
			if(commentConfig.getExtraDataDefine() == null || commentConfig.getExtraDataDefine().get(simpleKey) == null){// 不支持的上传文件名
				logger.debug("不支持的上传文件:" + file.getName());
				ignoreCount++;
				break;
			}

			/*
			 * 如果当前为编辑模式,则查找是否有对应的已存在数据 如果有已存在的数据并且本次要上传,则直接更新对应的文件
			 */
			if(userUploadDir == null){
				userUploadDir = request.getSession().getServletContext().getRealPath("/userUpload");

				logger.warn("系统未定义用户保存目录，将文件保存到:" + userUploadDir);

			}
			try {
				FileUtils.forceMkdir(new File(userUploadDir));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			String 	fileUploadSavePath = userUploadDir.replaceAll("/$", "").replaceAll("\\$", "") + File.separator;

			String fileName = UUIDFilenameGenerator.generateWithDatePath(file.getOriginalFilename());
			String fileDest = "";
			String existDocumentData = null;
			if (mode.equals("update") && comment.getData() != null) {
				for (String d2 : comment.getData().keySet()) {
					if (d2.equals(fileName)) {
						existDocumentData = d2;
						break;
					}
				}
				if (existDocumentData != null) {
					fileDest = fileUploadSavePath + File.separator + existDocumentData;
					File _oldFile = new File(fileDest);
					_oldFile.delete();
					updateCount++;
				} else {
					fileDest = fileUploadSavePath + File.separator + fileName;
				}

			} else {
				fileDest = fileUploadSavePath + File.separator + fileName;

			}
			logger.info("userUploadDir:" + userUploadDir + ",fileUploadSavePath:" + fileUploadSavePath + ",fileName:" + fileName);

			logger.info("保存数据文件[" + file.getOriginalFilename() + "]到:" + fileDest);
			File dest = new File(fileDest);
			try {
				file.transferTo(dest);
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
				continue;
			}
			/*if (mode.equals("edit") && existDocumentData != null) {
						// 已经更新了已存在附件的文件,无需其他操作
						continue;
					}*/
			if(key.equals(simpleKey)){
				logger.debug("将单独的附件文件[" + key + "]保存到:" + fileName);
				comment.setExtraValue(key, fileName);
			} else {
				if(galleryMap.get(simpleKey) == null){
					galleryMap.put(simpleKey, fileName);
				} else {
					String fileList = galleryMap.get(simpleKey);
					fileList += ",";
					fileList += fileName;
					galleryMap.put(simpleKey, fileList);
				}
				logger.debug("保存集合文件[" + simpleKey + "]，文件列表:" + galleryMap.get(simpleKey));
			}
			addCount++;

		}
		if(galleryMap.size() > 0){
			for(String key : galleryMap.keySet()){
				logger.debug("放入集合文件[" + key + "]到评论附件，文件列表:" + galleryMap.get(key));
				comment.setExtraValue(key, galleryMap.get(key));
			}
		}
		int totalAffected = addCount + updateCount;
		String message = "完成附件上传,新增 " + addCount + " 个,更新 " + updateCount + " 个, 跳过 " + ignoreCount + " 个。";
		logger.info(message);
		return totalAffected;
	}

	//取消用户的订单
	@RequestMapping(value="/cancel/{orderId}")
	public String cancel(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@PathVariable("orderId") Long orderId) throws Exception {
		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),"系统异常","请尝试访问其他页面或返回首页"));
			return frontMessageView;		
		}
		User frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());

		if(frontUser == null || frontUser.getCurrentStatus() != UserStatus.normal.getId()){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录，请先登录"));			
			return frontMessageView;

		}
		if(frontUser.getOwnerId() != ownerId){
			logger.error("用户[" + frontUser.getUuid() + "]的ownerId[" + frontUser.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(), "您尚未登录，请先登录"));			
			return frontMessageView;
		}

		Cart cart = cartService.select(orderId);
		if(cart == null){
			logger.error("找不到用户要取消的订单:" + orderId);
			map.put("message", new EisMessage(EisError.BILL_NOT_EXIST.getId(), "找不到对应的订单"));			
			return frontMessageView;
		}
		if(cart.getUuid() != frontUser.getUuid()){
			logger.error("用户要取消的订单:" + orderId + "属于用户[" + cart.getUuid() + "]，但当前用户是:" + frontUser.getUuid());
			map.put("message", new EisMessage(EisError.BILL_NOT_EXIST.getId(), "找不到对应的订单"));			
			return frontMessageView;
		}

		if(cart.getCurrentStatus() == TransactionStatus.newOrder.getId() || cart.getCurrentStatus() == TransactionStatus.waitingPay.getId()){
			logger.info("用户要取消的订单:" + orderId + "状态是:" + cart.getCurrentStatus() + "，更新为:" + TransactionStatus.closed.id);
			cart.setCurrentStatus(TransactionStatus.closed.id);
			cartService.update(cart);
			map.put("message", new EisMessage(EisError.statusAbnormal.getId(), "订单已取消"));			
			return frontMessageView;
		} else {
			logger.error("用户要取消的订单:" + orderId + "状态是:" + cart.getCurrentStatus() + "，不能取消");
			map.put("message", new EisMessage(EisError.statusAbnormal.getId(), "订单已无法取消"));			
			return frontMessageView;
		}
	}

	//确认收货
	@RequestMapping(value="/confirmDelivery/{transactionId}", method=RequestMethod.POST)
	public String confirmDelivery(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@PathVariable("transactionId") String transactionId) throws Exception {
		map.put("operate", OperateCode.CONFIRM_DELIVERY.toString());
		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),"系统异常","请尝试访问其他页面或返回首页"));
			return frontMessageView;		
		}
		User frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());

		if(frontUser == null || frontUser.getCurrentStatus() != UserStatus.normal.getId()){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录，请先登录"));			
			return frontMessageView;

		}
		if(frontUser.getOwnerId() != ownerId){
			logger.error("用户[" + frontUser.getUuid() + "]的ownerId[" + frontUser.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(), "您尚未登录，请先登录"));			
			return frontMessageView;
		}

		Cart order = null;
		long orderId = ServletRequestUtils.getLongParameter(request, "orderId", 0);
		if(orderId > 0){

			logger.info("找不到用户要确认的订单:" + transactionId + ",尝试作为orderId查找");
			order = cartService.select(orderId);
			if(order != null){
				return confirmOrderByOrderId(order, map);
			}
			map.put("message", new EisMessage(EisError.BILL_NOT_EXIST.getId(), "找不到对应的交易"));			
			return frontMessageView;
		}

		Item item = itemService.select(transactionId);

		if(item == null){
			logger.error("找不到用户要确认的订单:" + transactionId);
			map.put("message", new EisMessage(EisError.BILL_NOT_EXIST.getId(), "找不到对应的交易"));			
			return frontMessageView;
		}
		if(item.getChargeFromAccount() != frontUser.getUuid()){
			logger.error("用户要确认的订单:" + transactionId + "属于用户[" + item.getChargeFromAccount() + "]，但当前用户是:" + frontUser.getUuid());
			map.put("message", new EisMessage(EisError.BILL_NOT_EXIST.getId(), "找不到对应的订单"));			
			return frontMessageView;
		}

		if(item.getCurrentStatus() != TransactionStatus.delivering.getId() && item.getCurrentStatus() != TransactionStatus.preDelivery.getId()){
			logger.info("用户确认收货的交易:" + transactionId + "状态是:" + item.getCurrentStatus() + "，不能确认收货");
			map.put("message", new EisMessage(EisError.statusAbnormal.getId(), "订单无法修改"));			
			return frontMessageView;

		} 
		logger.info("用户确认收货的交易:" + transactionId + "状态是:" + item.getCurrentStatus() + "，更新为:" + TransactionStatus.waitingComment.id);
		item.setCurrentStatus(TransactionStatus.waitingComment.id);
		itemService.changeStatus(item);
		messageService.sendJmsDataSyncMessage(null, "itemService", "changeStatus", item);
		map.put("message", new EisMessage(OperateResult.success.getId(), "已确认收货"));	

		/**
		 * 检查该订单对应的交易
		 * 如果有多个交易，检查是否都已经被评论，如果是则将订单状态设置为被评论
		 */
		order = cartService.select(item.getCartId());
		if(order == null){
			logger.warn("找不到交易[" + transactionId + "]对应的订单:" + item.getCartId());
			return frontMessageView;
		}

		ItemCriteria itemCriteria = new ItemCriteria(item.getOwnerId());
		if(order.getCreateTime() != null){
			itemCriteria.setEnterTimeBegin(DateUtils.truncate(order.getCreateTime(), Calendar.DAY_OF_MONTH));
			itemCriteria.setEnterTimeEnd(DateUtils.ceiling(order.getCreateTime(), Calendar.DAY_OF_MONTH));
		}
		itemCriteria.setCartId(order.getCartId());
		List<Item> itemList = itemService.list(itemCriteria);

		logger.debug("订单[" + order.getCartId() + "]对应的交易数量是:" + (itemList == null ? "空" : itemList.size()));
		if(itemList == null || itemList.size() < 1){
			logger.error("找不到订单[" + order.getCartId() + "]对应的交易");
			return frontMessageView;
		}
		if(itemList.size() == 1){
			logger.info("订单[" + order.getCartId() + "]对应的交易数量是1,将整个订单状态也改为待评价");
			order.setCurrentStatus(TransactionStatus.waitingComment.id);
			cartService.update(order);
			messageService.sendJmsDataSyncMessage(null, "cartService", "updateNoNull", order);
			return frontMessageView;
		} 
		boolean bothConfirmed = true;
		for(Item i : itemList){
			if(i.getCurrentStatus() != TransactionStatus.waitingComment.id && i.getCurrentStatus() != TransactionStatus.commentClosed.id){
				bothConfirmed = false;
				break;
			}
		}
		if(bothConfirmed){
			logger.info("订单[" + order.getCartId()+ "]所有交易都是已确认收货状态,将该订单状态也改为待评论");
			order.setCurrentStatus(TransactionStatus.waitingComment.id);
			cartService.updateNoNull(order);
			messageService.sendJmsDataSyncMessage(null, "cartService", "updateNoNull", order);
		}

		return frontMessageView;
	}


	private String confirmOrderByOrderId(Cart order, ModelMap map) {


		order.setCurrentStatus(TransactionStatus.waitingComment.id);
		int rs = cartService.updateNoNull(order);
		if(rs == 1){
			messageService.sendJmsDataSyncMessage(null, "cartService", "updateNoNull", order);
			map.put("message", new EisMessage(OperateResult.success.getId(),"订单收货确认成功"));
		} else {
			map.put("message", new EisMessage(OperateResult.failed.getId(),"订单收货确认失败"));

		}
		ItemCriteria itemCriteria = new ItemCriteria(order.getOwnerId());
		if(order.getCreateTime() != null){
			itemCriteria.setEnterTimeBegin(DateUtils.truncate(order.getCreateTime(), Calendar.DAY_OF_MONTH));
			itemCriteria.setEnterTimeEnd(DateUtils.ceiling(order.getCreateTime(), Calendar.DAY_OF_MONTH));
		}
		itemCriteria.setCartId(order.getCartId());
		List<Item> itemList = itemService.list(itemCriteria);

		logger.debug("订单[" + order.getCartId() + "]对应的交易数量是:" + (itemList == null ? "空" : itemList.size()));
		if(itemList == null || itemList.size() < 1){
			logger.warn("找不到订单[" + order.getCartId() + "]对应的交易");
			return frontMessageView;
		}

		for(Item i : itemList){
			if(i.getCurrentStatus() != TransactionStatus.waitingComment.id && i.getCurrentStatus() != TransactionStatus.commentClosed.id){
				i.setCurrentStatus(TransactionStatus.waitingComment.id);
				logger.info("用户确认收货的订单[" + order.getCartId() + ",修改交易:" + i.getTransactionId() + "状态是:" + i.getCurrentStatus() + "，更新为:" + TransactionStatus.waitingComment.id);
				i.setCurrentStatus(TransactionStatus.waitingComment.id);
				itemService.changeStatus(i);
				messageService.sendJmsDataSyncMessage(null, "itemService", "changeStatus", i);
			}
		}


		return frontMessageView;
	}

	/**
	 * 对于某些商品，需要在购买成功后提交兑换
	 */
	@RequestMapping(value="/submitExchange", method=RequestMethod.POST)
	public String submitExchange(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		long ownerId = NumericUtils.parseLong(map.get("ownerId"));

		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),"系统异常","请尝试访问其他页面或返回首页"));
			return frontMessageView;		
		}
		User frontUser = null;
		try{
			frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
		}catch(Exception e){
		}

		if(frontUser == null || frontUser.getCurrentStatus() != UserStatus.normal.getId()){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录，请先登录"));			
			return frontMessageView;

		}
		if(frontUser.getOwnerId() != ownerId){
			logger.error("用户[" + frontUser.getUuid() + "]的ownerId[" + frontUser.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(), "您尚未登录，请先登录"));			
			return frontMessageView;
		}

		String tid = ServletRequestUtils.getStringParameter(request, "tid", null);
		if(StringUtils.isBlank(tid)){
			logger.error("用户[" + frontUser.getUuid() + "]申请兑换但是未提交交易ID");
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(), "请提交正确的数据"));			
			return frontMessageView;
		}


		Item item = itemService.select(tid);
		if(item == null){
			logger.error("找不到transactionId==" + tid + "的交易");
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.id,"找不到指定的订单"));
			return frontMessageView;	
		}
		if(item.getChargeFromAccount() != frontUser.getUuid()){
			logger.error("找不到transactionId==" + tid + "的交易");
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.id,"找不到指定的订单"));
			return frontMessageView;	
		}

		map.put("item", item);
		if(item.getPrice().getMarketPrice() < 100){
			map.put("divisor", LITTLE_DIVISOR);
		} else {
			map.put("divisor", BIG_DIVISOR);
		}


		if(item.getCurrentStatus() == TransactionStatus.failed.id){
			map.put("message", new EisMessage(EisError.statusAbnormal.id,"订单未成功，无法兑换"));
			return frontMessageView;	
		} 
		boolean exchanged = false;
		if(item.getCurrentStatus() == TransactionStatus.closed.id){
			exchanged = true;
		} else if(item.getCurrentStatus() != TransactionStatus.success.id){
			logger.error("交易[" + tid + "]状态是:" + item.getCurrentStatus() + ",不是成功，无法兑换");
			map.put("message", new EisMessage(EisError.statusAbnormal.id,"订单未成功，无法兑换"));
			return frontMessageView;	
		}

		int exchangeMode = 0;
		String serviceQrCode = null;
		if(item.getInviter() > 0){
			User partner = partnerService.select(item.getInviter());
			if(partner == null){
				logger.error("找不到交易[" + item.getTransactionId() + "]对应的合作商户:" + item.getInviter());
			} else {
				exchangeMode = (int)partner.getLongExtraValue("exchangeMode");
				serviceQrCode = partner.getExtraValue("serviceQrCode");
			}
		}
		if(exchangeMode < 1){
			exchangeMode = SYSTEM_EXCHANGE_MODE;
		}
		if(StringUtils.isBlank(serviceQrCode)){
			serviceQrCode = globalServiceQrCode;
		}

		map.put("serviceQrCode", serviceQrCode);
		String realNumber = item.getContent();
		if(realNumber.length() > CARD_LENGTH){
			String oldNumber = realNumber;
			realNumber = realNumber.substring(0, CARD_LENGTH);
			logger.info("将兑换码[" + oldNumber + "]截断为:" + realNumber);
		}
		//item.getExtraValue(DataName.productSerialNumber.toString());

		logger.debug("当前兑奖模式是:" + exchangeMode);
		if(exchangeMode == 2){
			//手工兑奖
			map.put("serviceQrCode", serviceQrCode);
			map.put("message", new EisMessage(OperateResult.success.id,"操作成功"));
			//map.put("message", new EisMessage(OperateResult.accept.id,"请扫码加客服微信领取奖励"));
			map.put(DataName.productSerialNumber.toString(),realNumber);
			item.setExtraValue(DataName.productSerialNumber.toString(),realNumber);
			map.put("item", item);

			return frontMessageView;	
		}
		if(!exchanged){
			Product product = productService.select(item.getProductId());
			if(product == null){
				logger.error("找不到订单[" +item.getTransactionId() + "]对应的产品");
				map.put("message", new EisMessage(EisError.productNotExist.id,"找不到对应产品"));
				return frontMessageView;	
			}
			CouponCriteria couponCriteria = new CouponCriteria(frontUser.getOwnerId());
			couponCriteria.setCouponCode(product.getProductCode());
			couponCriteria.setUuid(frontUser.getUuid());
			couponCriteria.setPaging(new Paging(item.getCount()));
			couponCriteria.setCurrentStatus(BasicStatus.normal.id);
			EisMessage fetchResult = couponService.fetch(couponCriteria);
			if(fetchResult.getOperateCode() != OperateResult.success.getId()){
				logger.error("通过条件[" + couponCriteria + "]获取卡券未成功，返回结果是:" + fetchResult);
				map.put("message", fetchResult);
				return frontMessageView;	
			} 
			List<Coupon> couponList = fetchResult.getAttachmentData("couponList");

			if(couponList == null || couponList.size() < 1){
				logger.error("通过条件[" + couponCriteria + "]得不到任何卡券");
				map.put("message", fetchResult);
				return frontMessageView;	
			} 
			StringBuffer sb = new StringBuffer();
			for(Coupon coupon : couponList){
				sb.append(coupon.getCouponSerialNumber()).append(coupon.getCouponPassword()).append("\n");
			}
			logger.info("为中奖成功用户放入兑换后的卡券:" + sb.toString());
			item.setExtraValue(DataName.productSerialNumber.toString(), sb.toString());


			realNumber = sb.toString();

			String exchangeUrl = product.getExtraValue("exchangeUrl");
			if(StringUtils.isNotBlank(exchangeUrl)){
				logger.debug("放入产品[" + product.getProductId() + "]外部兑换连接:" + exchangeUrl);
				map.put("exchangeUrl", exchangeUrl);
			}
			item.setExtraValue("exchangeUrl", exchangeUrl);

			item.setContent(sb.toString());
			item.setCurrentStatus(TransactionStatus.closed.id);
			itemService.update(item);
		}
		map.put("message", new EisMessage(OperateResult.success.id,"操作成功"));
		//map.put("productSerialNumber", item.getContent());

		map.put("item", item);

		return frontMessageView;	



	}
}
