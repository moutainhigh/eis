package com.maicard.wpt.controller.common;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.maicard.common.base.BaseController;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.domain.OpEisObject;
import com.maicard.common.service.GlobalOrderIdService;
import com.maicard.exception.EisException;
import com.maicard.money.service.PriceService;
import com.maicard.product.domain.Item;
import com.maicard.product.service.ItemService;
import com.maicard.product.service.StockService;
import com.maicard.security.domain.User;
import com.maicard.security.service.CertifyService;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.OperateResult;
import com.maicard.standard.PriceType;
import com.maicard.standard.SecurityStandard.UserTypes;
import com.maicard.standard.TransactionStandard.TransactionStatus;
import com.maicard.standard.TransactionStandard.TransactionType;



@Controller
@RequestMapping("/sale")
public class SaleController extends BaseController{
	
	@Resource
	private StockService stockService;
	
	@Resource
	private CertifyService certifyService;
	
	@Resource
	private PriceService priceService;
	
	@Resource
	private GlobalOrderIdService globalOrderIdService;
	
	@Resource
	private ItemService itemService;
	
	/**
	 * 接收用户售卖或寄售的商品
	 */
	@RequestMapping(value = "/add", method=RequestMethod.POST)
	public String index(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		User frontUser =  certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
		if(frontUser == null) {
			logger.error("需要先登录才能购物");
			throw new EisException(EisError.userNotFoundInSession.getId(), "请先登录");		
		}
		String objectType = ServletRequestUtils.getStringParameter(request, "objectType","document");
		long objectId = ServletRequestUtils.getLongParameter(request, "objectId", 0);
		if(objectId < 1) {
			logger.error("无法接收寄售，因为没有提交任何产品objectId");
			throw new EisException(EisError.OBJECT_IS_NULL.getId(), "请选择一个商品");	
		}
		Item item = new Item();
		item.setObjectType(objectType);
		item.setProductId(objectId);
		item.setChargeFromAccount(frontUser.getUuid());
		item.setTransactionTypeId(TransactionType.sale.id);
		OpEisObject targetObject = stockService.writeItemData(item, item.getObjectType(), item.getProductId());
		if(targetObject == null) {
			logger.error("找不到产品类型:{}/{}", item.getObjectType(), item.getProductId());
			map.put("message", new EisMessage(EisError.PARAMETER_ERROR.id,"不支持的产品类型"));
		}
		EisMessage saleMsg = this.processItem(request, item);
		
		logger.info("用户:{}请求销售产品:{}#{}的订单:{}，提交结果:{}", frontUser.getUuid(), objectType,objectId,item.getTransactionId(), saleMsg);
		map.put("message",saleMsg);
		return CommonStandard.frontMessageView;
	}

	private EisMessage processItem(HttpServletRequest request, Item item) {
		String cardInput = ServletRequestUtils.getStringParameter(request, "cardData", null);
		if(StringUtils.isBlank(cardInput)) {
			logger.error("寄售未提交卡密数据");
			return new EisMessage(EisError.PARAMETER_ERROR.id,"请提交卡密");
		}
		priceService.applyPrice(item, PriceType.PRICE_SALE.name());
		item.setCurrentStatus(TransactionStatus.newOrder.id);
		String[] data = cardInput.split("\n|\r\n");
		int cnt = 0;
		for(String card : data) {
			Item i = item.clone();
			i.setTransactionId(globalOrderIdService.generate(item.getTransactionTypeId()));
			String[] cardData = card.split(",|，|;|；|\\s+");
			if(cardData.length < 1) {
				logger.error("第{}张卡密数据错误:{}", i, card);
				continue;
			}
			cnt++;
			item.setExtraValue(DataName.productSerialNumber.name(), cardData[0].trim());
			item.setExtraValue(DataName.productPassword.name(), cardData[1].trim());
			EisMessage saleMsg = itemService.insert(i);
			logger.info("插入第{}张卡密:{}订单的结果是:{}", i, card, saleMsg);
		}
		if(cnt < 1) {
			logger.error("未能插入Item数据");
			return new EisMessage(EisError.systemBusy.id,"系统繁忙，无法提交订单");
		}
		EisMessage msg = new EisMessage(OperateResult.success.id, "已接受" + cnt + "张卡密");
		return msg;
		
	}

}

