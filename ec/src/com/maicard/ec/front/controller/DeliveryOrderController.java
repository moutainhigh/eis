package com.maicard.ec.front.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.ui.ModelMap;

import com.maicard.common.base.BaseController;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.GlobalOrderIdService;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.OperateResult;
import com.maicard.standard.SecurityStandard.UserTypes;
import com.maicard.standard.TransactionStandard.TransactionStatus;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.*;

import com.maicard.ec.domain.DeliveryOrder;
import com.maicard.ec.iface.DeliveryTraceProcessor;
import com.maicard.ec.service.DeliveryCompanyService;
import com.maicard.ec.service.DeliveryOrderService;
import com.maicard.money.domain.Price;
import com.maicard.product.criteria.ItemCriteria;
import com.maicard.product.domain.Cart;
import com.maicard.product.domain.Item;
import com.maicard.product.service.CartService;
import com.maicard.product.service.ItemService;
import com.maicard.security.domain.User;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.FrontUserService;
import com.maicard.security.service.PartnerService;

@Controller
@RequestMapping("/deliveryOrder")
public class DeliveryOrderController extends BaseController{

	@Resource
	private ApplicationContextService applicationContextService;
	@Resource
	private GlobalOrderIdService globalOrderIdService;
	@Resource
	private CertifyService certifyService;
	@Resource
	private FrontUserService frontUserService;
	@Resource
	private DeliveryCompanyService deliveryCompanyService;
	@Resource
	private DeliveryOrderService deliveryOrderService;
	@Resource
	private PartnerService partnerService;
	@Resource
	private ItemService itemService;
	@Resource
	private CartService cartService;
	@Resource
	private ConfigService configService;


	private final String defaultDeliveryTraceProcessor = "kuaidi100DeliveryTraceProcessor";
	/**
	 * 计算运费
	 */
	@RequestMapping(value="/fee")
	public String deliveryFee(HttpServletRequest request, HttpServletResponse response, ModelMap map, 
			@RequestParam(value = "addressBookId", required = false) Long addressBookId,
			String[] orderId) throws Exception {
		if(orderId == null || orderId.length < 1){
			logger.error("尝试计算运费但未提交订单号");
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.id,"请提供订单号"));
			return CommonStandard.frontMessageView;
		}
		if(addressBookId == null || addressBookId < 1){
			logger.error("尝试计算运费但未提交配送地址");
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.id,"请选择配送地址"));
			return CommonStandard.frontMessageView;
		}

		User frontUser = null;
		try{
			frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
		}catch(Exception e){
			e.printStackTrace();			
		}
		if(frontUser == null){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录，请先登录"));
			return CommonStandard.frontMessageView;
		}
		ItemCriteria itemCriteria = new ItemCriteria(frontUser.getOwnerId());
		itemCriteria.setChargeFromAccount(frontUser.getUuid());
		itemCriteria.setCurrentStatus(TransactionStatus.inCart.getId());
		HashMap<String, Item> cart = cartService.map(itemCriteria);

		if(cart == null || cart.size() < 1){//购物车是空的
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.getId(), "您尚未购买任何产品，请先购买"));
			return CommonStandard.frontMessageView;
		}

		List<Item> itemList = new ArrayList<Item>();
		//比对用户勾选提交的订单号与购物车订单号，只计算提交的订单号的费用
		for(String cartId : cart.keySet()){
			for(String buyId : orderId){
				if(buyId.equalsIgnoreCase(cartId)){
					itemList.add(cart.get(cartId));
				}
			}
		}
		if(itemList.size() < 1){
			logger.error("在购物车中，找不到任何一个提交订单:" + orderId);
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.getId(), "找不到您提交的订单，请重新购买"));
			return CommonStandard.frontMessageView;
		}
		
		Item[] items = new Item[itemList.size()];
		items = itemList.toArray(items);

		//FIXME
		Map<String,Price> feeMap = new HashMap<String,Price>();
		DeliveryOrder deliveryOrder = deliveryOrderService.generateDeliveryOrder(addressBookId, items, null, null,null);
		map.put("deliveryOrder",deliveryOrder);


		return null;
	}

	@RequestMapping(value="/traceByCart/{cartId}")
	public String traceByCart(HttpServletRequest request, HttpServletResponse response, ModelMap map, 
			@PathVariable Integer cartId
			) throws Exception {

		final String view = "deliveryOrder/tranceByItem";

		User frontUser = null;
		try{
			frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
		}catch(Exception e){
			e.printStackTrace();			
		}
		if(frontUser == null){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录，请先登录"));
			return CommonStandard.frontMessageView;
		}
		Cart cart = cartService.select(cartId);
		if(cart == null){
			logger.error("找不到指定的Cart:" + cartId);
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.id,"找不到指定的配送单"));
			return CommonStandard.frontMessageView;	
		}
		if(cart.getUuid() != frontUser.getUuid()){
			logger.error("指定的Cart[" + cartId + "]不属于用户[" + frontUser.getUuid() + "]，属于:" + cart.getUuid());
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.id,"找不到指定的配送单"));
			return CommonStandard.frontMessageView;	
		}

		DeliveryOrder deliveryOrder = null;
		if(cart.getDeliveryOrderId() > 0){
			deliveryOrder = deliveryOrderService.select(cart.getDeliveryOrderId());
		}
		if(deliveryOrder == null){
			logger.error("根据Cart[" + cartId + "]指定的配送单ID[" + cart.getDeliveryOrderId() + "]找不到任何配送单信息");
		} else {
			ItemCriteria itemCriteria = new ItemCriteria(frontUser.getOwnerId());
			itemCriteria.setCartId(cartId);
			List<Item> itemList = itemService.list(itemCriteria);
			if(itemList == null || itemList.size() < 1){
				logger.error("根据Cart[" + cartId + "]找不到任何交易数据");
				map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.id,"找不到指定的配送单"));
				return CommonStandard.frontMessageView;	
			}

			Item item = itemList.get(0);


			String outDeliveryId = item.getExtraValue(DataName.deliveryOrderId.toString());
			if(StringUtils.isBlank(outDeliveryId)){
				logger.error("交易订单[" + item.getTransactionId() + "]没有扩展数据deliveryOrderId,无法查询快递单");
				map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.id,"指定的订单没有配送数据"));
				return CommonStandard.frontMessageView;
			}
			String deliveryCompanyName = item.getExtraValue(DataName.deliveryCompanyName.toString());
			if(StringUtils.isBlank(deliveryCompanyName)){
				logger.error("交易订单[" + item.getTransactionId() + "]没有扩展数据deliveryCompanyName,无法查询快递单");
				map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.id,"指定的订单没有配送数据"));
				return CommonStandard.frontMessageView;
			}
			deliveryOrder = new DeliveryOrder(frontUser.getOwnerId());

			deliveryOrder.setDeliveryCompany(deliveryCompanyName);
			deliveryOrder.setOutOrderId(outDeliveryId);
		}
		int rs = query(deliveryOrder, frontUser);
		if(rs != OperateResult.success.getId()){
			map.put("message", new EisMessage(rs, "配送单据查询失败"));
			return CommonStandard.frontMessageView;
		}
		
		map.put("deliveryOrder", deliveryOrder);

		return view;
	}

	@RequestMapping(value="/traceByItem/{transactionId}")
	public String trace(HttpServletRequest request, HttpServletResponse response, ModelMap map, 
			@PathVariable String transactionId
			) throws Exception {

		final String view = "deliveryOrder/traceByItem";

		User frontUser = null;
		try{
			frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
		}catch(Exception e){
			e.printStackTrace();			
		}
		if(frontUser == null){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录，请先登录"));
			return CommonStandard.frontMessageView;
		}
		Item item = itemService.select(transactionId);
		if(item == null){
			logger.error("找不到指定的交易:" + transactionId);
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.id,"找不到指定的配送单"));
			return CommonStandard.frontMessageView;
		}
		if(item.getOwnerId() != frontUser.getOwnerId()){
			logger.error("指定的配送单:" + item + ",其ownerId[" + item.getOwnerId() + "]与当前用户[" + frontUser.getUuid() + "]的ownerId[" + frontUser.getOwnerId() + "]不一致");
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.id,"找不到指定的配送单"));
			return CommonStandard.frontMessageView;
		}
		if(item.getChargeFromAccount() != frontUser.getUuid()){
			logger.error("指定的交易订单:" + item.getTransactionId() + ",其交易用户[" + item.getChargeFromAccount() + "]与当前用户[" + frontUser.getUuid() + "]不一致");
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.id,"找不到指定的配送单"));
			return CommonStandard.frontMessageView;
		}
		String outDeliveryId = item.getExtraValue(DataName.deliveryOrderId.toString());
		if(StringUtils.isBlank(outDeliveryId)){
			logger.error("交易订单[" + item.getTransactionId() + "]没有扩展数据deliveryOrderId,无法查询快递单");
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.id,"指定的订单没有配送数据"));
			return CommonStandard.frontMessageView;
		}
		String deliveryCompanyName = item.getExtraValue(DataName.deliveryCompanyName.toString());
		if(StringUtils.isBlank(deliveryCompanyName)){
			logger.error("交易订单[" + item.getTransactionId() + "]没有扩展数据deliveryCompanyName,无法查询快递单");
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.id,"指定的订单没有配送数据"));
			return CommonStandard.frontMessageView;
		}
		DeliveryOrder deliveryOrder = new DeliveryOrder(frontUser.getOwnerId());
		deliveryOrder.setDeliveryCompany(deliveryCompanyName);
		deliveryOrder.setOutOrderId(outDeliveryId);
		int rs = query(deliveryOrder, frontUser);
		if(rs != OperateResult.success.getId()){
			map.put("message", new EisMessage(rs, "配送单据查询失败"));
			return CommonStandard.frontMessageView;
		}
		map.put("deliveryOrder", deliveryOrder);

		return view;
	}

	@RequestMapping(value="/traceByDeliveryOrder/{deliveryOrderId}")
	public String traceByDeliveryOrder(HttpServletRequest request, HttpServletResponse response, ModelMap map, 
			@PathVariable Long deliveryOrderId
			) throws Exception {

		final String view = "deliveryOrder/tranceByItem";

		User frontUser = null;
		try{
			frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
		}catch(Exception e){
			e.printStackTrace();			
		}
		if(frontUser == null){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录，请先登录"));
			return CommonStandard.frontMessageView;
		}
		DeliveryOrder deliveryOrder = deliveryOrderService.select(deliveryOrderId);
		if(deliveryOrder == null){
			logger.error("找不到指定的配送单:" + deliveryOrderId);
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.id,"找不到指定的配送单"));
			return CommonStandard.frontMessageView;
		}
		if(deliveryOrder.getUuid() != frontUser.getUuid()){
			logger.error("指定的配送单:" + deliveryOrderId + ",其所有者[" + deliveryOrder.getUuid() + "]与当前用户[" + frontUser.getUuid() + "]不一致");
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.id,"找不到指定的配送单"));
			return CommonStandard.frontMessageView;
		}
		String returnUrl = ServletRequestUtils.getStringParameter(request, "returnUrl",null);
		if(StringUtils.isNotBlank(returnUrl)){
			deliveryOrder.setExtraValue("returnUrl", returnUrl);
		}
		String displayType =  ServletRequestUtils.getStringParameter(request, "displayType",null);
		deliveryOrder.setDisplayType(displayType);
		int rs = query(deliveryOrder, frontUser);
		if(rs != OperateResult.success.getId()){
			map.put("message", new EisMessage(rs, "配送单据查询失败"));
			return CommonStandard.frontMessageView;
		}
		map.put("deliveryOrder", deliveryOrder);

		return view;
	}

	private int  query(DeliveryOrder deliveryOrder, User frontUser){

		if(deliveryOrder.getOwnerId() != frontUser.getOwnerId()){
			logger.error("指定的配送单:" + deliveryOrder + ",其ownerId[" + deliveryOrder.getOwnerId() + "]与当前用户[" + frontUser.getUuid() + "]的ownerId[" + frontUser.getOwnerId() + "]不一致");
			return EisError.OBJECT_IS_NULL.id;
		}
		
		if(deliveryOrder.getDeliveryCompany() == null && deliveryOrder.getDeliveryCompanyId() < 1){
			logger.error("尝试查询的配送单没有公司信息");
			return EisError.dataImperfect.id;
		}
		deliveryCompanyService.checkInfo(deliveryOrder);
		if(StringUtils.isBlank(deliveryOrder.getOutOrderId())){
			logger.error("尝试查询的配送单没有外部订单信息");
			return EisError.dataImperfect.id;

		}

		Object bean = applicationContextService.getBean(defaultDeliveryTraceProcessor);
		if(bean == null){
			logger.warn("系统中找不到指定的快递跟踪处理器:" + defaultDeliveryTraceProcessor);
			return EisError.beanNotFound.id;

		}
		if(!(bean instanceof DeliveryTraceProcessor)){
			logger.error("指定的快递跟踪处理器[" + defaultDeliveryTraceProcessor + "]不是一个DeliveryTraceProcessor");
			return EisError.beanNotFound.id;
		} 
		DeliveryTraceProcessor dtp = (DeliveryTraceProcessor)bean;
		dtp.trace(deliveryOrder);

		return OperateResult.success.getId();
	}
}