package com.maicard.wpt.custom.youbao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.GlobalOrderIdService;
import com.maicard.money.criteria.PriceCriteria;
import com.maicard.money.domain.Price;
import com.maicard.money.service.MoneyService;
import com.maicard.money.service.PriceService;
import com.maicard.product.domain.Activity;
import com.maicard.product.domain.Product;
import com.maicard.product.service.ActivityProcessor;
import com.maicard.product.service.ActivityService;
import com.maicard.product.service.ItemService;
import com.maicard.product.service.ProductService;
import com.maicard.security.domain.User;
import com.maicard.standard.EisError;
import com.maicard.standard.OperateResult;
import com.maicard.standard.PriceType;

/**
 * 一组商品的展示及促销活动
 * 根据前端控制器提供的identify确定获取哪些商品及其促销价格
 *
 *
 * @author NetSnake
 * @date 2016-06-29
 * 
 */
@Service
public class CollectionSaleActivityProcessor extends BaseService implements ActivityProcessor {

	@Resource
	private ActivityService activityService;
	@Resource
	private GlobalOrderIdService globalOrderIdService;
	@Resource
	private MoneyService moneyService;
	@Resource
	private ItemService itemService;
	@Resource
	private PriceService priceService;
	@Resource
	private ProductService productService;



	@SuppressWarnings("unchecked")
	@Override
	public EisMessage execute(String action, Activity activity, Object targetObject, Object parameter ) {
		User user = null;
		if(targetObject instanceof User){
			user = (User)targetObject;
		}
		if(user == null){
			logger.warn("活动执行对象不是User");
			//	return new EisMessage(EisError.dataError.getId(),"活动执行对象不是User");
		}
		Map<String,Object> para = null;
		try{
			para = (Map<String,Object>)parameter;
		}catch(Exception e){
			e.printStackTrace();
		}
		if(para == null){
			logger.error("活动未提供Map<String,Object>类型的parameter");
			return new EisMessage(EisError.REQUIRED_PARAMETER.getId(),"活动参数错误");
		}
		HttpServletRequest request = null;
		if(para.get("request") != null && para.get("request") instanceof HttpServletRequest){
			request = (HttpServletRequest)para.get("request");
		}
		String identify = null;
		if(para.get("identify") != null){
			identify = para.get("identify").toString();
		}

		return _create(activity, user, identify, request);

	}


	private EisMessage _create(Activity activity, User user, String identify, HttpServletRequest request) {
		if(activity == null){
			logger.error("活动为空");
			return new EisMessage(EisError.systemDataError.getId(), "活动数据异常");
		}

		HashMap<String,Object> attachment = new HashMap<String,Object>();
		EisMessage resultMessage = new EisMessage();
		attachment.put("activity",activity);




		//String identify = ServletRequestUtils.getStringParameter(request, "identify",null);
		if(StringUtils.isBlank(identify)){
			identify = activity.getActivityCode();
		}

		identify = identify.split("_")[0];



		PriceCriteria priceCriteria = new PriceCriteria(activity.getOwnerId());
		priceCriteria.setIdentify(identify);
		priceCriteria.setPriceType(PriceType.PRICE_PROMOTION.toString());
		List<Price> priceList = priceService.list(priceCriteria);
		if(priceList == null || priceList.size() < 1){
			logger.debug("根据识别码[" + identify + "]找不到任何优惠价格数据");
		}
		long uuid = 0;
		if(user != null){
			uuid = user.getUuid();
		}
		Map<String,Product> productMap = new HashMap<String,Product>();
		for(Price price : priceList){
			Product product = productService.select((int)price.getObjectId());
			if(product == null){
				continue;
			}
			Product p = product.clone();
			p.setPrice(price);
			p.setTransactionToken(priceService.generateTransactionToken(price, uuid));
			productMap.put(p.getProductCode(),p);


		}
		logger.debug("本次活动[" + identify + "]共放入" + productMap + "款促销产品");
		attachment.put("productMap", productMap);
		attachment.put("priceType", PriceType.PRICE_PROMOTION);
		attachment.put("identify", identify);
		resultMessage.setAttachment(attachment);
		resultMessage.setOperateCode(OperateResult.success.getId());
		resultMessage.setMessage("活动处理完成");
		return resultMessage;

	}





	@Override
	public EisMessage prepare(Activity activity, User user, Object parameter) {
		// TODO Auto-generated method stub
		return null;
	}



}

