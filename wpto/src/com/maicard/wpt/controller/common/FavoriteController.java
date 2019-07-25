package com.maicard.wpt.controller.common;



import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.maicard.common.base.BaseController;
import com.maicard.common.domain.EOEisObject;
import com.maicard.common.domain.EisMessage;
import com.maicard.security.criteria.UserRelationCriteria;
import com.maicard.security.domain.User;
import com.maicard.security.domain.UserRelation;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.UserRelationService;
import com.maicard.site.domain.Document;
import com.maicard.site.domain.DocumentData;
import com.maicard.site.service.DocumentService;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.util.NumericUtils;
import com.maicard.common.util.Paging;
import com.maicard.common.util.PagingUtils;
import com.maicard.ec.service.DeliveryOrderService;
import com.maicard.exception.EisException;
import com.maicard.money.domain.Price;
import com.maicard.money.service.PriceService;
import com.maicard.product.criteria.ItemCriteria;
import com.maicard.product.domain.Cart;
import com.maicard.product.domain.Item;
import com.maicard.product.service.CartService;
import com.maicard.product.service.ItemService;
import com.maicard.product.service.ProductService;
import com.maicard.product.service.StockService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.ObjectType;
import com.maicard.standard.OperateResult;
import com.maicard.standard.PriceType;
import com.maicard.standard.SecurityStandard.UserStatus;
import com.maicard.standard.SecurityStandard.UserTypes;

/**
 * 用于控制玩家请求收藏/取消收藏
 * @author GHOST
 * @date 2018-11-27
 *
 */
@Controller
@RequestMapping(value = "/favorite")
public class FavoriteController extends BaseController {
	
	@Resource
	private ApplicationContextService applicationContextService;
	
	@Resource
	private StockService stockService;
	
	@Resource
	private ItemService itemService;

	
	@Resource
	private CertifyService certifyService;	
	
	@Resource
	private CartService cartService;
	
	@Resource
	private UserRelationService userRelationService;

	@Resource
	private ProductService productService;

	@Resource
	protected DocumentService documentService;
	

	@Autowired(required=false)
	protected DeliveryOrderService deliveryOrderService;

	@Resource
	protected PriceService priceService;

	protected void writePriceData(Document document, long sitePartnerId, User frontUser, String transactionToken){



		//如果产品需要配送，那么获取一个发货地址
		if(deliveryOrderService != null && document.getBooleanExtraValue(DataName.productNeedDelivery.toString())){
			DocumentData dd = new DocumentData(DataName.defaultFromArea.toString(), deliveryOrderService.getFromArea(document));
			document.getDocumentDataMap().put(dd.getDataCode(), dd);
			logger.debug("为文档[" + document.getUdid() + "]生成发货地附加数据:" + dd.getDataCode() + "=>" + dd.getDataValue() + "]");

		}
		Price price = null;
		if(frontUser != null && transactionToken != null){
			price = priceService.getPriceByToken(document, frontUser.getUuid(), transactionToken);
		} else {
			logger.info("当前没有终端用户信息，不计算特定价格");
		}
		boolean priceDataResult = false;
		if(price != null){
			priceDataResult = priceService.generatePriceExtraData(document, price);
			logger.debug("根据文档:" + document.getId() + "和价格[" + price.getPriceId() + "]为文档生成价格结果:" + priceDataResult);
		} else {

			priceDataResult = priceService.generatePriceExtraData(document, PriceType.PRICE_STANDARD.toString());
			logger.debug("为文档:" + document.getId() + "生成标准价格结果:" + priceDataResult);
		}
	}

	
	/**
	 * 列出用户所有已收藏内容
	 * 
	 * 
	 * @author GHOST
	 * @date 2018-11-27
	 */
	@RequestMapping(value = "/index", method=RequestMethod.GET)
	public String index(HttpServletRequest request, HttpServletResponse response, ModelMap map, UserRelationCriteria userRelationCriteria) {
		int rows = ServletRequestUtils.getIntParameter(request, "rows", ROW_PER_PAGE);		
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);

		final String view = "favorite/index";
		////////////////////////////标准检查流程 ///////////////////////////////////
		User frontUser =  certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());

		if(frontUser == null || frontUser.getCurrentStatus() != UserStatus.normal.getId()){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录，请先登录"));			
			return CommonStandard.frontMessageView;

		}
		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),"系统异常","请尝试访问其他页面或返回首页"));
			return CommonStandard.frontMessageView;		
		}

		if(frontUser.getOwnerId() != ownerId){
			logger.error("用户[" + frontUser.getUuid() + "]的ownerId[" + frontUser.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(), "您尚未登录，请先登录"));			
			return CommonStandard.frontMessageView;
		}
		//////////////////////////// 标准检查流程结束 ///////////////////////////////
		

		userRelationCriteria.setOwnerId(frontUser.getOwnerId());
		userRelationCriteria.setUuid(frontUser.getUuid());
		if(StringUtils.isBlank(userRelationCriteria.getObjectType())) {
			userRelationCriteria.setObjectType(ObjectType.document.name());
		}

		int totalRows = userRelationService.count(userRelationCriteria);
		if(totalRows < 1){
			logger.debug("当前返回的数据数量是0");
			return view;
		}
		Paging paging = new Paging(rows);
		paging.setCurrentPage(page);
		userRelationCriteria.setPaging(paging);
		map.put("paging", PagingUtils.generateContentPaging(totalRows, rows, page));
		List<UserRelation> userRelationList = userRelationService.listOnPage(userRelationCriteria);
		List<EOEisObject> favoriteList = new ArrayList<EOEisObject>();
		if(userRelationList != null && userRelationList.size() > 0){
			for(UserRelation userRelation : userRelationList){
				EOEisObject targetObject = stockService.getTargetObject(userRelation.getObjectType(), userRelation.getObjectId());
				
				if(targetObject != null) {
					if(targetObject instanceof Document) {
						Document document = (Document)targetObject;
						document.setExtraValue("favorited", "true");
						writePriceData(document, 0,frontUser,null);
					}
					favoriteList.add(targetObject);
				}
				
			}
		}
		map.put("newsList", favoriteList);
		return view;
	}


	

	
	@RequestMapping(value = "/add", method=RequestMethod.POST)
	public String add(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		final String view = "favorite/add";

		////////////////////////////标准检查流程 ///////////////////////////////////
		User frontUser =  certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());

		if(frontUser == null || frontUser.getCurrentStatus() != UserStatus.normal.getId()){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录，请先登录"));			
			return CommonStandard.frontMessageView;

		}
		long ownerId = NumericUtils.parseLong(map.get("ownerId"));
		
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),"系统异常","请尝试访问其他页面或返回首页"));
			return CommonStandard.frontMessageView;		
		}

		if(frontUser.getOwnerId() != ownerId){
			logger.error("用户[" + frontUser.getUuid() + "]的ownerId[" + frontUser.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(), "您尚未登录，请先登录"));			
			return CommonStandard.frontMessageView;
		}
		//////////////////////////// 标准检查流程结束 ///////////////////////////////
		
		String objectType = ServletRequestUtils.getStringParameter(request, "objectType", null);
		long objectId = ServletRequestUtils.getLongParameter(request, "objectId", 0);
		
		if(StringUtils.isBlank(objectType)) {
			throw new EisException(EisError.REQUIRED_PARAMETER.id, "请提交正确的参数objectType");
		}
		if(objectId <= 0) {
			throw new EisException(EisError.REQUIRED_PARAMETER.id, "请提交正确的参数objectId");
		}
		
		if(objectType.equalsIgnoreCase(ObjectType.order.name())) {
			//请求关注订单，那把订单找出来得到应关注的实际对象
			Cart order = cartService.select(objectId);
			if(order == null) {
				logger.error("找不到用户要关注的订单:{}", objectId);
				throw new EisException(EisError.REQUIRED_PARAMETER.id, "请提交正确的参数objectId");			
			}
			ItemCriteria itemCriteria = new ItemCriteria(ownerId);
			itemCriteria.setEnterTimeBegin(DateUtils.addMinutes(order.getCreateTime(),-10));
			itemCriteria.setCartId(objectId);
			List<Item> itemList = itemService.list(itemCriteria);
			if(itemList.size() < 1) {
				logger.error("关注订单:{}时找不到任何相关的item", objectId);
				throw new EisException(EisError.systemDataError.id, "系统数据错误");			
			}
			int successCount = 0;
			for(Item item : itemList) {
				UserRelation userRelation = new UserRelation(ownerId);
				userRelation.setObjectType(item.getObjectType());
				userRelation.setObjectId(item.getProductId());
				userRelation.setRelationLimit(UserRelationCriteria.RELATION_LIMIT_UNIQUE);
				userRelation.setRelationType(UserRelationCriteria.RELATION_TYPE_FAVORITE);
				userRelation.setUuid(frontUser.getUuid());
				userRelation.setCurrentStatus(BasicStatus.normal.id);
				int rs = userRelationService.insert(userRelation);
				logger.debug("用户[" + frontUser.getUuid() + "]针对对象[" + userRelation.getObjectType() + "#" + userRelation.getObjectId() + "]的收藏插入结果:" + rs);
				if(rs == 1) {
					successCount++;
				}
				
			}
			if(successCount > 0) {
				map.put("message",  new EisMessage(OperateResult.success.getId(),"收藏成功" + successCount + "个商品"));
			} else {
				map.put("message",  new EisMessage(OperateResult.failed.getId(),"收藏失败"));

			}
			return view;
			
		}

		UserRelationCriteria userRelationCriteria = new UserRelationCriteria(ownerId);
		userRelationCriteria.setObjectType(objectType);
		userRelationCriteria.setObjectId(objectId);
		userRelationCriteria.setUuid(frontUser.getUuid());
		userRelationCriteria.setRelationType(UserRelationCriteria.RELATION_TYPE_FAVORITE);
		int count = userRelationService.count(userRelationCriteria);
		if(count > 0) {
			logger.warn("用户:{}已经有针对对象:{}/{}的收藏关联", frontUser.getUuid(), objectType, objectId);
			map.put("message", new EisMessage(OperateResult.failed.getId(), "您已经收藏了"));
			return view;
		}
		
		UserRelation userRelation = new UserRelation(ownerId);
		userRelation.setObjectType(objectType);
		userRelation.setObjectId(objectId);
		userRelation.setRelationLimit(UserRelationCriteria.RELATION_LIMIT_UNIQUE);
		userRelation.setRelationType(UserRelationCriteria.RELATION_TYPE_FAVORITE);
		userRelation.setUuid(frontUser.getUuid());
		userRelation.setCurrentStatus(BasicStatus.normal.id);
		


		int rs = userRelationService.insert(userRelation);
		logger.debug("用户[" + frontUser.getUuid() + "]针对对象[" + userRelation.getObjectType() + "#" + userRelation.getObjectId() + "]的收藏插入结果:" + rs);
		if (rs == 1) {
			map.put("message",  new EisMessage(OperateResult.success.getId(),"收藏成功"));
		} else {
			
			map.put("message", new EisMessage(OperateResult.failed.id, "收藏失败"));

		}
		return view;
	}

	

	/**
	 * 删除一个评论
	 */
	@RequestMapping(value = "/delete", method=RequestMethod.POST)
	public String delete(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		final String view = "favorite/delete";

		////////////////////////////标准检查流程 ///////////////////////////////////
		User frontUser =  certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());

		if(frontUser == null || frontUser.getCurrentStatus() != UserStatus.normal.getId()){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录，请先登录"));			
			return CommonStandard.frontMessageView;

		}
		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),"系统异常","请尝试访问其他页面或返回首页"));
			return CommonStandard.frontMessageView;		
		}

		if(frontUser.getOwnerId() != ownerId){
			logger.error("用户[" + frontUser.getUuid() + "]的ownerId[" + frontUser.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(), "您尚未登录，请先登录"));			
			return CommonStandard.frontMessageView;
		}
		//////////////////////////// 标准检查流程结束 ///////////////////////////////
		
		String objectType = ServletRequestUtils.getStringParameter(request, "objectType", null);
		long objectId = ServletRequestUtils.getLongParameter(request, "objectId", 0);
		
		if(StringUtils.isBlank(objectType)) {
			throw new EisException(EisError.REQUIRED_PARAMETER.id, "请提交正确的参数objectType");
		}
		if(objectId <= 0) {
			throw new EisException(EisError.REQUIRED_PARAMETER.id, "请提交正确的参数objectId");
		}
		
		if(objectType.equalsIgnoreCase(ObjectType.order.name())) {
			//请求关注订单，那把订单找出来得到应关注的实际对象
			Cart order = cartService.select(objectId);
			if(order == null) {
				logger.error("找不到用户要关注的订单:{}", objectId);
				throw new EisException(EisError.REQUIRED_PARAMETER.id, "请提交正确的参数objectId");			
			}
			ItemCriteria itemCriteria = new ItemCriteria(ownerId);
			itemCriteria.setCartId(objectId);
			itemCriteria.setEnterTimeBegin(DateUtils.addMinutes(order.getCreateTime(),-10));
			List<Item> itemList = itemService.list(itemCriteria);
			if(itemList.size() < 1) {
				logger.error("关注订单:{}时找不到任何相关的item", objectId);
				throw new EisException(EisError.systemDataError.id, "系统数据错误");			
			}
			int successCount = 0;
			for(Item item : itemList) {
				int rs = this.deleteFavorite(item.getObjectType(),  item.getProductId(), frontUser, map);
				if(rs == 1) {
					successCount++;
				}
				
			}
			if(successCount > 0) {
				map.put("message",  new EisMessage(OperateResult.success.getId(),"取消成功" + successCount + "个商品"));
			} else {
				map.put("message",  new EisMessage(OperateResult.failed.getId(),"取消失败"));

			}
			return view;
			
		}

		this.deleteFavorite(objectType, objectId, frontUser, map);
		return view;
	}
	
	protected int deleteFavorite(String objectType, long objectId, User frontUser, ModelMap map) {
		UserRelationCriteria userRelationCriteria = new UserRelationCriteria(frontUser.getOwnerId());
		userRelationCriteria.setObjectType(objectType);
		userRelationCriteria.setObjectId(objectId);
		userRelationCriteria.setUuid(frontUser.getUuid());
		userRelationCriteria.setRelationType(UserRelationCriteria.RELATION_TYPE_FAVORITE);
		List<UserRelation> userRelationList = userRelationService.list(userRelationCriteria);
		if(userRelationList.size() < 0) {
			logger.warn("用户:{}还没有收藏对象:{}/{}", frontUser.getUuid(), objectType, objectId);
			//估计是点错了帮他收藏
			UserRelation userRelation = new UserRelation(frontUser.getOwnerId());
			userRelation.setObjectType(objectType);
			userRelation.setObjectId(objectId);
			userRelation.setRelationLimit(UserRelationCriteria.RELATION_LIMIT_UNIQUE);
			userRelation.setRelationType(UserRelationCriteria.RELATION_TYPE_FAVORITE);
			userRelation.setUuid(frontUser.getUuid());

			int rs = userRelationService.insert(userRelation);
			logger.debug("用户[" + frontUser.getUuid() + "]针对对象[" + userRelation.getObjectType() + "#" + userRelation.getObjectId() + "]的收藏插入结果:" + rs);
			if (rs == 1) {
				map.put("message",  new EisMessage(OperateResult.success.getId(),"收藏成功"));
			} else {
				
				map.put("message", new EisMessage(OperateResult.failed.id, "收藏失败"));

			}
			return rs;
		}
		
		
		
		UserRelation userRelation = userRelationList.get(0);
		
		
		


		int rs = userRelationService.delete(userRelation.getUserRelationId());
		logger.debug("用户[" + frontUser.getUuid() + "]删除收藏对象[" + userRelation.getObjectType() + "#" + userRelation.getObjectId() + "]，结果:" + rs);
		if (rs == 1) {
			map.put("message",  new EisMessage(OperateResult.success.getId(),"取消成功"));
		} else {			
			map.put("message", new EisMessage(OperateResult.failed.id, "取消失败"));
		}
		
		return rs;
	}

	

}
