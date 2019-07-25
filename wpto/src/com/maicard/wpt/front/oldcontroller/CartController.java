package com.maicard.wpt.front.oldcontroller;

import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.ui.ModelMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.*;

import com.maicard.common.base.BaseController;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.GlobalOrderIdService;
import com.maicard.money.service.MoneyService;
import com.maicard.money.service.PayMethodService;
import com.maicard.money.service.PayService;
import com.maicard.money.service.PayTypeService;
import com.maicard.product.criteria.CartCriteria;
import com.maicard.product.criteria.ItemCriteria;
import com.maicard.product.domain.Cart;
import com.maicard.product.domain.Item;
import com.maicard.product.service.ActivityService;
import com.maicard.product.service.CartService;
import com.maicard.product.service.ItemService;
import com.maicard.product.service.ProductService;
import com.maicard.security.domain.User;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.UuidMapService;
import com.maicard.site.service.DocumentService;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.EisError;
import com.maicard.standard.OperateResult;
import com.maicard.standard.PriceType;
import com.maicard.standard.SecurityStandard.UserTypes;
import com.maicard.standard.TransactionStandard.TransactionStatus;

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
	private PayTypeService payTypeService;
	@Resource
	private ProductService productService;

	@Autowired(required=false)
	private UuidMapService uuidMapService;




	/*
	 * 返回当前购物车列表
	 */
	@RequestMapping(method=RequestMethod.GET)
	public String  list(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {	

		User frontUser = null;
		try{
			frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
		}catch(Exception e){
			e.printStackTrace();			
		}
		if(frontUser == null){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录，请先登录后再购买"));
			return "cart/list";
		}

		final String view = "cart/list";

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
				return view;
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
		return view;
	}
	/*
	 * 返回当前购物车列表
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



}
