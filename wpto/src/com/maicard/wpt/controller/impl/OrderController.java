package com.maicard.wpt.controller.impl;

import static com.maicard.standard.CommonStandard.frontMessageView;

import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.maicard.common.domain.EisMessage;
import com.maicard.common.util.JsonUtils;
import com.maicard.common.util.NumericUtils;
import com.maicard.exception.EisException;
import com.maicard.product.criteria.ItemCriteria;
import com.maicard.product.domain.Cart;
import com.maicard.product.domain.Item;
import com.maicard.security.domain.User;
import com.maicard.standard.EisError;
import com.maicard.standard.OperateCode;
import com.maicard.standard.SecurityStandard.UserStatus;
import com.maicard.standard.SecurityStandard.UserTypes;
import com.maicard.wpt.controller.basic.AbstractOrderController;

@Controller
@RequestMapping("/order")
public class OrderController extends AbstractOrderController{

	//查询用户购买记录
	@Override
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
		boolean allowView = false;
		if(cart.getUuid() != buyUuid){
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
		//	cart.setMiniItemList(itemList);
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


}
