package com.maicard.wpt.custom.youbao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.ServletRequestUtils;

import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.GlobalOrderIdService;
import com.maicard.money.criteria.PriceCriteria;
import com.maicard.money.domain.Price;
import com.maicard.money.service.MoneyService;
import com.maicard.money.service.PriceService;
import com.maicard.product.criteria.ItemCriteria;
import com.maicard.product.domain.Activity;
import com.maicard.product.domain.Item;
import com.maicard.product.domain.Product;
import com.maicard.product.service.ActivityProcessor;
import com.maicard.product.service.ActivityService;
import com.maicard.product.service.ItemService;
import com.maicard.product.service.ProductService;
import com.maicard.security.domain.User;
import com.maicard.site.domain.Document;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.ObjectType;
import com.maicard.standard.Operate;
import com.maicard.standard.OperateResult;
import com.maicard.standard.PriceType;
import com.maicard.standard.ServiceStatus;
import com.maicard.standard.TransactionStandard;
import com.maicard.standard.SiteStandard.DocumentStatus;

/**
 * 一般销售活动处理
 *
 *
 * @author NetSnake
 * @date 2016-06-29
 * 
 */
@Service
public class SaleActivityProcessor extends BaseService implements ActivityProcessor {

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

	private final SimpleDateFormat sdf = new SimpleDateFormat(CommonStandard.defaultDateFormat);


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
		Item item = null;
		if(para.get("item") != null && para.get("item") instanceof Item){
			item = (Item)para.get("item");
		}
		
		if(StringUtils.equalsIgnoreCase(action, Operate.settleUp.name())){
			return _addToCart(activity, user, item);
		} else if(StringUtils.equalsIgnoreCase(action, Operate.addToCart.name())){
			return _addToCart(activity, user, item);
		} else {
			return _view(activity,user,request);
		}


	}




	private EisMessage _addToCart(Activity activity, User user, Item item) {
		HashMap<String,Object> attachment = new HashMap<String,Object>();
		EisMessage resultMessage = new EisMessage();


		Product product = productService.select(item.getProductId());
		if(product == null){
			logger.error(activity.getActivityId() + "#活动关联产品:" + item.getProductId() + "不存在");			
			resultMessage.setAttachment(attachment);
			resultMessage.setOperateCode(EisError.systemDataError.getId());
			resultMessage.setMessage("活动数据异常");
			return resultMessage;
		}




		int userJoinable = checkTotalUserJoinCountLimit(activity, product);
		if(userJoinable != OperateResult.success.getId()){
			logger.warn("无法通过" + activity.getActivityId() + "#活动的总参与人数限制");
			resultMessage.setAttachment(attachment);
			resultMessage.setOperateCode(userJoinable);
			resultMessage.setMessage("已超过活动可参与人数限制");
			return resultMessage;
		}
		long uuid = 0;
		if(user != null){
			uuid = user.getUuid();
		}
		userJoinable = 	checkPerUserJoinCountLimit(activity, uuid, product.getProductId());

		if(userJoinable != OperateResult.success.getId()){
			logger.warn("用户[" + uuid + "]无法通过" + activity.getActivityId() + "#活动的单人参与次数限制");
			resultMessage.setAttachment(attachment);
			resultMessage.setOperateCode(userJoinable);
			resultMessage.setMessage("您已购买相关产品超过活动限制");
			return resultMessage;
		}
		
		int perUserJoinCount = (int)activity.getLongExtraValue(DataName.perUserJoinCountLimit.toString());
		if(item.getCount() > perUserJoinCount){
			logger.error(activity.getActivityId() + "#活动定义的单人参与次数是:" + perUserJoinCount + ",但当前用户的购买数量是:" + item.getCount() + ",已超过");
			resultMessage.setAttachment(attachment);
			resultMessage.setOperateCode(EisError.COUNT_LIMIT_EXCEED.id);
			resultMessage.setMessage("您的购买数量超过活动限制");
			return resultMessage;	
		}

		logger.warn("用户[" + uuid + "]通过了总人数和单人参与次数检查，可以参加" + activity.getActivityId() + "#活动");

		resultMessage.setOperateCode(OperateResult.success.getId());
		resultMessage.setMessage("您可以参与该活动");
		return resultMessage;

	}


	private EisMessage _view(Activity activity, User user, HttpServletRequest request) {
		if(activity == null){
			logger.error("活动为空");
			return new EisMessage(EisError.systemDataError.getId(), "活动数据异常");
		}

		HashMap<String,Object> attachment = new HashMap<String,Object>();
		EisMessage resultMessage = new EisMessage();
		attachment.put("activity",activity);



		

		String identify = ServletRequestUtils.getStringParameter(request, "identify",null);
		if(StringUtils.isBlank(identify)){
			identify = activity.getActivityCode();
		}

		long productId = activity.getLongExtraValue(DataName.refProductId.toString());

		if(productId < 1){
			logger.error(activity.getActivityId() + "#活动未指定关联产品");
			resultMessage.setAttachment(attachment);
			resultMessage.setOperateCode(EisError.systemDataError.getId());
			resultMessage.setMessage("活动数据异常");
			return resultMessage;
		}

		Product product = productService.select(productId);
		if(product == null){
			logger.error(activity.getActivityId() + "#活动关联产品:" + productId + "不存在");			
			resultMessage.setAttachment(attachment);
			resultMessage.setOperateCode(EisError.systemDataError.getId());
			resultMessage.setMessage("活动数据异常");
			return resultMessage;
		}
		
		
		PriceCriteria priceCriteria = new PriceCriteria(product.getOwnerId());
		priceCriteria.setIdentify(identify);
		priceCriteria.setObjectType(ObjectType.product.name());
		priceCriteria.setObjectId(productId);
		priceCriteria.setPriceType(PriceType.PRICE_PROMOTION.toString());
		List<Price> priceList = priceService.list(priceCriteria);

		int availableCount = productService.getAmount(productId);
		logger.debug(activity.getActivityId() + "#活动关联产品:" + productId + "的库存数量是:" + availableCount);

		Product p = product.clone();
		p.setAvailableCount(availableCount);
		Price price = null;
		if(priceList == null || priceList.size() < 1){
			logger.warn("根据识别码[" + identify + "]找不到产品[" + productId + "]任何优惠价格数据,使用标准价格查找");
			priceCriteria.setIdentify(null);
			priceCriteria.setPriceType(PriceType.PRICE_STANDARD.toString());
			priceList = priceService.list(priceCriteria);
			if(priceList == null || priceList.size() < 1){
				logger.error("找不到产品[" + productId + "]的价格数据");
			} else {
				price = priceList.get(0);
			}

		} else {
			price = priceList.get(0);
		}
		if(price != null){
			logger.error("产品[" + productId + "]对应的价格数据是:" + price);
			long uuid = 0;
			if(user != null){
				uuid = user.getUuid();
			}
			p.setTransactionToken(priceService.generateTransactionToken(price, uuid));

		}
		attachment.put("product", p);

		Document document = activityService.getRefDocument(activity);
		if(document != null){
			if(document.getCurrentStatus() != DocumentStatus.published.getId()){
				logger.warn(activity.getActivityId() + "#活动指定的关联文档:" + document.getUdid() + "未发布，状态是:" + document.getCurrentStatus());
			} else {
				productService.generateProductDocumentData(product, document);
				attachment.put("document", document);
			}
		} else {
			logger.warn("找不到" + activity.getActivityId() + "#活动指定的关联文档");
		}

		if(activity.getCurrentStatus() == ServiceStatus.closed.getId()){
			logger.debug(activity.getActivityId() + "#活动状态是:" + activity.getCurrentStatus() + ",已关闭");
			resultMessage.setAttachment(attachment);
			resultMessage.setOperateCode(EisError.activityClosed.getId());
			resultMessage.setMessage("活动已关闭");
			return resultMessage;
		}
		if(activity.getCurrentStatus() == ServiceStatus.waitingOpen.getId()){
			logger.debug(activity.getActivityId() + "#活动状态是:" + activity.getCurrentStatus() + ",尚未开启");
			resultMessage.setAttachment(attachment);
			resultMessage.setOperateCode(EisError.notActive.getId());
			resultMessage.setMessage("活动尚未开始");
			return resultMessage;		
		}
		if(activity.getBeginTime() != null && activity.getBeginTime().after(new Date())){
			logger.debug(activity.getActivityId() + "#活动开启时间是" + sdf.format(activity.getBeginTime()) + ",尚未开启");
			resultMessage.setAttachment(attachment);
			resultMessage.setOperateCode(EisError.notActive.getId());
			resultMessage.setMessage("活动尚未开始");
			return resultMessage;
		}
		if(activity.getEndTime() != null && activity.getEndTime().before(new Date())){
			logger.debug(activity.getActivityId() + "#活动结束时间是" + sdf.format(activity.getBeginTime()) + ",已结束");
			resultMessage.setAttachment(attachment);
			resultMessage.setOperateCode(EisError.activityClosed.getId());
			resultMessage.setMessage("活动尚未开始");
			return resultMessage;
		}






		int userJoinable = checkTotalUserJoinCountLimit(activity, product);
		if(userJoinable != OperateResult.success.getId()){
			logger.warn("无法通过" + activity.getActivityId() + "#活动的总参与人数限制");
			resultMessage.setAttachment(attachment);
			resultMessage.setOperateCode(userJoinable);
			resultMessage.setMessage("您不能参与该活动");
			return resultMessage;
		}
		long uuid = 0;
		if(user != null){
			uuid = user.getUuid();
		}
		userJoinable = 	checkPerUserJoinCountLimit(activity, uuid, product.getProductId());

		if(userJoinable != OperateResult.success.getId()){
			logger.warn("用户[" + uuid + "]无法通过" + activity.getActivityId() + "#活动的单人参与人数限制");
			logger.warn("无法通过" + activity.getActivityId() + "#活动的总参与人数限制");
			resultMessage.setAttachment(attachment);
			resultMessage.setOperateCode(userJoinable);
			resultMessage.setMessage("您不能参与该活动");
			return resultMessage;
		}

		logger.warn("无法通过" + activity.getActivityId() + "#活动的总参与人数限制");
		resultMessage.setAttachment(attachment);
		resultMessage.setOperateCode(OperateResult.success.getId());
		resultMessage.setMessage("活动处理完成");
		return resultMessage;


	}



	/**
	 * 检查所有用户参与该活动的人数限制
	 * @param activity
	 * @param product
	 * @return
	 */
	private int checkTotalUserJoinCountLimit(Activity activity, Product product){
		int maxJoinUserCount = (int)activity.getLongExtraValue(DataName.maxJoinUserCount.toString());
		if(maxJoinUserCount < 1){
			logger.debug("当前活动没有参与人数上限");
			return OperateResult.success.getId();
		}
		ItemCriteria itemCriteria = new ItemCriteria();
		itemCriteria.setProductIds(product.getProductId());
		itemCriteria.setCurrentStatus(TransactionStandard.getSuccessStatus());
		int currentJoinUserCount = itemService.count(itemCriteria);
		//活动有人数限制，检查当前参与人数
		if(currentJoinUserCount > maxJoinUserCount){
			logger.warn("当前活动[" + activity.getActivityId() + "]参与上限是" + maxJoinUserCount + "人，已成功购买人数是" + currentJoinUserCount + "人，无法再次参与");
			return EisError.joinUserCountLimited.getId();
		}
		return OperateResult.success.getId();

	}

	/**
	 * 检查某个用户参与该活动的限制
	 * @param activity
	 * @param product
	 * @return
	 */
	private int checkPerUserJoinCountLimit(Activity activity, long uuid, int productId){
		int perUserJoinCount = (int)activity.getLongExtraValue(DataName.perUserJoinCountLimit.toString());
		if(perUserJoinCount < 1){
			logger.debug("当前活动没有单个用户参与次数上限");
			return OperateResult.success.getId();
		}
		logger.debug("当前活动[" + activity.getActivityId() + "]的单个用户参与上限是:" + perUserJoinCount);
		ItemCriteria itemCriteria = new ItemCriteria();
		itemCriteria.setCurrentStatus(TransactionStandard.getSuccessStatus());
		itemCriteria.setProductIds(productId);
		itemCriteria.setChargeFromAccount(uuid);
		List<Item> itemList = itemService.list(itemCriteria);
		if(itemList == null || itemList.size() < 1){
			logger.debug("用户[" + uuid + "]还没有成功购买产品[" + productId + "]，参与当前活动[" + activity.getActivityId() + "]");
			return OperateResult.success.getId();
		}
		int currentJoinCount = 0;
		for(Item item : itemList){
			currentJoinCount += item.getCount();
		}
		if(currentJoinCount >= perUserJoinCount){
			logger.warn("当前活动[" + activity.getActivityId() + "]预定产品[" + productId + "],单个用户参与上限是" + perUserJoinCount + "人，用户[" + uuid + "]已参与" + currentJoinCount + "次，无法再次参与");
			return EisError.joinUserCountLimited.getId();
		} 
		logger.debug("当前活动[" + activity.getActivityId() + "]预定产品[" + productId + "],单个用户参与上限是" + perUserJoinCount + "人，用户[" + uuid + "]已参与" + currentJoinCount + "次，可以继续");
		return OperateResult.success.getId();

	}


	@Override
	public EisMessage prepare(Activity activity, User user, Object parameter) {
		// TODO Auto-generated method stub
		return null;
	}



}

