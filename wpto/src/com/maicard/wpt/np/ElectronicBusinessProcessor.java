package com.maicard.wpt.np;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;

import com.alibaba.fastjson.JSON;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.util.ClassUtils;
import com.maicard.common.util.NumericUtils;
import com.maicard.common.util.Paging;
import com.maicard.common.util.PagingUtils;
import com.maicard.common.util.PreviewToken;
import com.maicard.common.util.StringTools;
import com.maicard.mb.criteria.MessageCriteria;
import com.maicard.money.domain.Price;
import com.maicard.money.util.MoneyUtils;
import com.maicard.product.criteria.ItemCriteria;
import com.maicard.product.criteria.ProductGroupCriteria;
import com.maicard.product.domain.Cart;
import com.maicard.product.domain.Item;
import com.maicard.product.domain.ProductGroup;
import com.maicard.product.service.ProductGroupService;
import com.maicard.security.criteria.UserRelationCriteria;
import com.maicard.security.domain.User;
import com.maicard.security.domain.UserRelation;
import com.maicard.site.criteria.DocumentCriteria;
import com.maicard.site.domain.Document;
import com.maicard.site.domain.DocumentData;
import com.maicard.site.domain.Node;
import com.maicard.site.service.NodeProcessor;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.MessageStandard.MessageStatus;
import com.maicard.standard.ObjectType;
import com.maicard.standard.PriceType;
import com.maicard.standard.SecurityStandard.UserTypes;
import com.maicard.standard.SiteStandard;
import com.maicard.standard.SiteStandard.DocumentStatus;
import com.maicard.standard.TransactionStandard;

/**
 * 电商处理器
 * @author xiangqiaogao
 * @date 2017/12/8
 */
@SuppressWarnings("unused")
@Service
public class ElectronicBusinessProcessor extends WptDefaultNodeProcessor implements NodeProcessor{





	@Resource
	private ProductGroupService productGroupService;
	
	/**
	 * 写入某个商品的购买记录
	 * 
	 * 
	 * @author GHOST
	 * @date 2018-12-18
	 */
	protected void writeBuyHistory(Document document, ModelMap map) {
		ItemCriteria itemCriteria = new ItemCriteria(document.getOwnerId());
		itemCriteria.setProductIds(document.getUdid());
		itemCriteria.setObjectType(ObjectType.document.name());
		itemCriteria.setEnterTimeBegin(DateUtils.addDays(new Date(), -30));
		Paging paging = new Paging(10);
		paging.setCurrentPage(1);
		itemCriteria.setPaging(paging);
		List<Item> itemList = itemService.listOnPage(itemCriteria);
		logger.debug("商品:{}从时间:{}购买记录有{}条", document.getUdid(), StringTools.getFormattedTime(itemCriteria.getEnterTimeBegin()), itemList.size());
		if(itemList.size() > 0) {
			List<ModelMap> buyHistory = new ArrayList<ModelMap>();
			for(Item item : itemList) {
				ModelMap dataMap = new ModelMap();
				dataMap.put("buyTime", item.getEnterTime());
				User buyUser = frontUserService.select(item.getChargeFromAccount());
				if(buyUser == null) {
					dataMap.put("buyUser", "匿名用户");
				} else {
					dataMap.put("buyUser",  buyUser.getNickName() == null ? buyUser.getUsername() : buyUser.getNickName());
					String avatarUrl = buyUser.getExtraValue("avatarUrl");
					if(avatarUrl != null) {
						dataMap.put("avatarUrl", avatarUrl);
					}
				}
				dataMap.put("currentStatus", item.getCurrentStatus());
				buyHistory.add(dataMap);
			}
			map.put("buyHistory", buyHistory);
		}
		
	}



	/**
	 * 首页获取精品推荐
	 * @param frontUser
	 * @param map
	 * @throws Exception 
	 */

	@Override
	protected void writeRecommendList(HttpServletRequest request,HttpServletResponse response,User frontUser, ModelMap map) throws Exception {
		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return;		
		}
		/*ProductCriteria productCriteria = new ProductCriteria();
		productCriteria.setCurrentStatus(BasicStatus.normal.getId());
		productCriteria.setOrderBy(" label_money desc ");
		productCriteria.setOwnerId(ownerId);
		int count = 1;
		List<Map<String,Object>> listMAP = new ArrayList<Map<String,Object>>();
		String transactionToken = ServletRequestUtils.getStringParameter(request, "tt");
		if(StringUtils.isNotBlank(transactionToken)){
			map.put("tt",transactionToken);
		}
		Paging paging = new Paging(rowsPerPage);
		paging.setCurrentPage(count);
		productCriteria.setPaging(paging);
		productCriteria.setOwnerId(ownerId);
		List<Product> indexRecommendList= productService.listOnPage(productCriteria);
//		logger.debug("数量："+indexRecommendList==null?"空":indexRecommendList.size()+"");
		for(Product product:indexRecommendList){
			Map<String,Object> hasMap = new HashMap<String,Object>();
			Document doc = productService.getRefDocument(product);
			if(doc==null){
				continue;
			}
//			writeRelationCount(doc);
			hasMap.put("document", getDocByDelDataMap(doc));
			hasMap.put("buy_money", product.getBuyMoney());
			hasMap.put("vipFree", product.getExtraValue("vipFree"));
			writeCommentCount2(doc, hasMap);
			try {
				User u = frontUserService.select(doc.getPublisherId());
				hasMap.put("userName", u.getUsername());
				hasMap.put("userHeadPic", u.getExtraValue("userHeadPic"));
			} catch (Exception e) {
				logger.error("找不到发布人的信息");
			}
			listMAP.add(hasMap);
		}*/

		DocumentCriteria documentCriteria = new DocumentCriteria();
		documentCriteria.setCurrentStatus(DocumentStatus.published.getId());
		documentCriteria.setNodePath("product");
		Paging paging = new Paging(ROWS_PER_PAGE);
		paging.setCurrentPage(1);
		documentCriteria.setPaging(paging);
		String transactionToken = ServletRequestUtils.getStringParameter(request, "tt");
		if(StringUtils.isNotBlank(transactionToken)){
			map.put("tt",transactionToken);
		}
		//int totalResults = documentService.count(documentCriteria);
		documentCriteria.setOwnerId(ownerId);
		List<Document> documentList = documentService.listOnPage(documentCriteria);
		List<Document> listMAP = new ArrayList<Document>();
		for (Document document : documentList) {
			Document doc = document.clone();
			writeRelationCount(doc);
			writePriceData(doc,0,frontUser,null);
			listMAP.add(doc);
		}
		logger.debug("推荐文档列表:" + listMAP.size());
		map.put("recommendDocumentList", listMAP);
		//map.put("indexRecommendList", listMAP);
	}


	/**
	 * 首页获取最热销
	 * @param frontUser
	 * @param map
	 * @throws Exception 
	 */
	protected void writeHotSaleList(HttpServletRequest request,HttpServletResponse response,User frontUser, ModelMap map) throws Exception {
		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return;		
		}
		/*	ProductCriteria productCriteria = new ProductCriteria(ownerId);
		productCriteria.setCurrentStatus(BasicStatus.normal.getId());
		productCriteria.setOrderBy("(init_count-available_count) desc");
		int count = 1;
		List<Map<String,Object>> listMAP = new ArrayList<Map<String,Object>>();
		String transactionToken = ServletRequestUtils.getStringParameter(request, "tt");
		if(StringUtils.isNotBlank(transactionToken)){
			map.put("tt",transactionToken);
		}
		Paging paging = new Paging(rowsPerPage);
		paging.setCurrentPage(count);
		productCriteria.setPaging(paging);
		productCriteria.setOwnerId(ownerId);
		List<Product> indexHotSaleList= productService.listOnPage(productCriteria);
//		logger.debug("数量信息："+indexHotSaleList==null?"空":indexHotSaleList.size()+"");
		for(Product product:indexHotSaleList){
			Map<String,Object> hasMap = new HashMap<String,Object>();
			Document doc = productService.getRefDocument(product);
			if(doc==null){
				continue;
			}
//			writeRelationCount(doc);
			hasMap.put("document", getDocByDelDataMap(doc));
			hasMap.put("buy_money", product.getBuyMoney());
			hasMap.put("vipFree", product.getExtraValue("vipFree"));
//			writeCommentCount2(doc, hasMap);
			logger.debug("循环Product");
			try {
				User u = frontUserService.select(doc.getPublisherId());
				hasMap.put("userName", u.getUsername());
				hasMap.put("userHeadPic", u.getExtraValue("userHeadPic"));
			} catch (Exception e) {
				logger.error("找不到发布人的信息");
			}
			listMAP.add(hasMap);
		}*/
		DocumentCriteria documentCriteria = new DocumentCriteria();
		documentCriteria.setCurrentStatus(DocumentStatus.published.getId());
		documentCriteria.setNodePath("makeup");
		Paging paging = new Paging(ROWS_PER_PAGE);
		paging.setCurrentPage(1);
		documentCriteria.setPaging(paging);
		String transactionToken = ServletRequestUtils.getStringParameter(request, "tt");
		if(StringUtils.isNotBlank(transactionToken)){
			map.put("tt",transactionToken);
		}
		//int totalResults = documentService.count(documentCriteria);
		documentCriteria.setOwnerId(ownerId);
		List<Document> documentList = documentService.listOnPage(documentCriteria);
		List<Document> listMAP = new ArrayList<Document>();
		for (Document document : documentList) {
			Document doc = document.clone();
			writeRelationCount(doc);
			writePriceData(doc,0,frontUser,null);
			listMAP.add(doc);
		}
		logger.debug("热销商品个数{}",listMAP.size());
		map.put("hotDocumentList", listMAP);
	}
	protected void recommendsList(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		/*logger.debug("进入我的推荐、最新上架、热销、网站推荐");
		long ownerId = (long)map.get("ownerId");
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),"找不到对应的地址","请尝试访问其他页面或返回首页"));
			return ;		
		}
		User frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
		writeHotSaleList(request, response, frontUser, map);
		writeNewestList(request, response,frontUser, map);
		writeRecommendList(request, response,frontUser, map);
		if(frontUser!=null){
			writeMyGroom(frontUser, map);
		}*/
		return ;
	}


	/**
	 * 写入同一种商品不同规格（如不同颜色、型号）的分组信息
	 * 
	 * 
	 * @author GHOST
	 * @date 2018-11-24
	 */
	protected void writeGroupData(Document document) {
		long groupId = document.getLongExtraValue(DataName.productGroupId.name());
		if(groupId <= 0) {
			logger.warn("商品:{}没有分组ID,不查询分组", document.getUdid());
			return;
		}

		List<ProductGroup> productGroupTree = productGroupService.generateTree(ObjectType.document.name(), groupId );
		logger.debug("商品:{}分组:{}对应的分组数据是{}", document.getUdid(), groupId, JSON.toJSONString(productGroupTree));
		document.setExtraObject("groupData", productGroupTree);

		/*Map<Integer,List<String>> groupData = new HashMap<Integer,List<String>>();
		if(productGroupList.size() > 0) {
			for(ProductGroup pg : productGroupList) {
				if(groupData.get(pg.getLevel()) == null) {
					groupData.put(pg.getLevel(), new ArrayList<String>());
				}
				if(!groupData.get(pg.getLevel()).contains(pg.getGroupValue())){
					groupData.get(pg.getLevel()).add(pg.getGroupValue());
				}
			}
		}*/

	}

	protected void writePriceData(Document document, long sitePartnerId, User frontUser, String transactionToken){



		//如果产品需要配送，那么获取一个发货地址
		if(deliveryOrderService != null && document.getBooleanExtraValue(DataName.productNeedDelivery.toString())){
			String value = deliveryOrderService.getFromArea(document);
			document.setExtraValue(DataName.defaultFromArea.toString(), value);
			logger.debug("为文档[" + document.getUdid() + "]生成发货地附加数据:" + DataName.defaultFromArea.toString() + "=>" + value + "]");

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


	protected void writeFavorite(Document document, User frontUser){
		if(frontUser == null){
			return;
		}
		/**
		 * 查找是否关注、点赞了该文档
		 * 如果该文档是一个产品文档，则检查是否关注了该产品
		 */
		UserRelationCriteria userRelationCriteria = new UserRelationCriteria(frontUser.getOwnerId());
		userRelationCriteria.setUuid(frontUser.getUuid());
		userRelationCriteria.setObjectType(ObjectType.document.name());
		userRelationCriteria.setObjectId(document.getUdid());
		userRelationCriteria.setCurrentStatus(BasicStatus.normal.getId());
		List<UserRelation> relationList = userRelationService.list(userRelationCriteria);
		if(relationList.size() < 1){
			logger.info("用户[" + frontUser.getUuid() + "]没有跟对象[" + userRelationCriteria.getObjectType() + "#" + userRelationCriteria.getObjectId() + "]产生任何关联");
		} else {
			boolean favorited = false;
			boolean praised = false;
			for(UserRelation userRelation : relationList){
				if(userRelation.getRelationType().equals(UserRelationCriteria.RELATION_TYPE_FAVORITE)){
					favorited = true;
				} else 	if(userRelation.getRelationType().equals(UserRelationCriteria.RELATION_TYPE_PRAISE)){
					praised = true;
				}
			}
			if(favorited){
				document.setExtraValue("favorited", "true");
			}
			if(praised){
				document.setExtraValue("praised", "true");
			}

		}
	}


	@Override
	public String index(HttpServletRequest request,
			HttpServletResponse response, ModelMap map) throws Exception {
		logger.info("进入:" + this.getClass().getName());
		Node node =  null;
		try{
			node = (Node)map.get("node");
		}catch(Exception e){
			e.printStackTrace();
		}
		if(node == null){
			return CommonStandard.frontMessageView;
		}
		long ownerId =  NumericUtils.parseLong(map.get("ownerId"));

		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return CommonStandard.frontMessageView;		
		}
		if(node.getOwnerId() != ownerId){
			logger.error("尝试获取的节点[" + node.getNodeId() + "]，其ownerId[" + node.getOwnerId() + "]与系统会话中的[" + ownerId + "]不一致");
			return CommonStandard.frontMessageView;
		}
		logger.debug("电商节点处理器,处理节点:" + node.getName());
		
		String defaultTemplateLocation = node.getTemplateLocation();
		if(defaultTemplateLocation == null){
			logger.error("节点[" + node.getName() + "/" + node.getNodeId() + "]未定义默认模版");
			return CommonStandard.frontMessageView;	

		}
		defaultTemplateLocation = defaultTemplateLocation.replaceAll("\\.\\w+$", "");

		//检查当前是否找到了合作方的theme及其uuid
		long sitePartnerId = NumericUtils.parseLong(map.get(DataName.sitePartnerId.toString()));
		logger.debug("当前找到的sitePartnerId=" + sitePartnerId);


		User frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.id);
		if(frontUser != null){
			writeUserInfo(frontUser, map, request);
		}
		//writeSideBar(request, response,map,ownerId);
		//写入最新通知文档
		this.writeNotice(map, ownerId);


		int rows = ServletRequestUtils.getIntParameter(request, "rows", ROWS_PER_PAGE);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);

		Paging paging = new Paging(rows);
		paging.setCurrentPage(page);
		List<Document> documentList = new ArrayList<Document>();
		List<Document> documentList2 = new ArrayList<Document>();
		
		




		String tag = ServletRequestUtils.getStringParameter(request, "tag", null);

		DocumentCriteria documentCriteria = new DocumentCriteria(ownerId);

		String[] documentCodes = ServletRequestUtils.getStringParameters(request, "documentCode");
		
		boolean useDocumentCodeQuery = false;
		if(documentCodes != null && documentCodes.length > 0) {
			documentCriteria.setDocumentCode(documentCodes);
			useDocumentCodeQuery  = true;
		} else {
			documentCriteria.setNodePath(node.getPath());
		}

		
		documentCriteria.setCurrentStatus(DocumentStatus.published.getId());
		if(StringUtils.isNotBlank(tag)) {
			documentCriteria.setTags(tag.split(","));
		}
		
		int totalRows = documentService.count(documentCriteria);
		if(totalRows < 1){
			logger.debug("当前返回的数据数量是0");
			return defaultTemplateLocation;
		}
		documentCriteria.setPaging(paging);
		
		map.put("paging", PagingUtils.generateContentPaging(totalRows, rows, page));
	
		documentList = documentService.listOnPage(documentCriteria);
		logger.info("发布到本节点的所有文章数是:{},是否使用documentCode查询:{}",documentList.size(), useDocumentCodeQuery);
		if(documentList != null){
			for(Document d1 : documentList){
				Document d2 = d1.clone();
				writePriceData(d2, sitePartnerId, frontUser, null);
				//Document document = filterSubscribeData(request,response,d2, frontUser);
				//writeFavoriteCount(d2);


				writeFavorite(d2, frontUser);
				//writeReadCount(d2);
				documentList2.add(d2);
				//logger.debug("该文章对应的udid为："+d2.getUdid());
			}
			map.put("newsList",documentList2);
		}
		if(frontUser!=null){
			map.put("unreadMessage",unreadMessageCount(frontUser.getUuid()));
		}
		boolean isNavigationNode = (node.getNodeTypeId() == SiteStandard.NodeType.navigation.id);
		if(isNavigationNode) {
			writeHotSaleList(request, response,frontUser, map);
			writeNewestList(request, response,frontUser, map);
			writeRecommendList(request, response,frontUser, map);
			Node navigationNode = node.clone();
			navigationNode.setNodeTypeId(SiteStandard.NodeType.business.id);
			writeNavigation(navigationNode,map);
		}
		

		return defaultTemplateLocation;
	}


	@Override
	public String detail(HttpServletRequest request,			HttpServletResponse response, ModelMap map
			) throws Exception {


		long ownerId =  (long)map.get("ownerId");

		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return CommonStandard.frontMessageView;		
		}
		Node node =  null;
		try{
			node = (Node)map.get("node");
		}catch(Exception e){
			e.printStackTrace();
		}
		if(node == null){
			return CommonStandard.frontMessageView;		
		}
		if(node.getNodeId() == nodeService.getDefaultNode(map.get("siteCode").toString(), ownerId).getNodeId()){
			return CommonStandard.frontMessageView;
		}

		if(node.getOwnerId() != ownerId){
			logger.error("尝试获取的节点[" + node.getNodeId() + "]，其ownerId[" + node.getOwnerId() + "]与系统会话中的[" + ownerId + "]不一致");
			return CommonStandard.frontMessageView;
		}




		String documentCode = null;
		try{
			documentCode = map.get("documentCode").toString();
		}catch(Exception e){}

		User frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.id);




		
		Document document = null;
		Document doc = documentService.select(documentCode, ownerId);
		if(doc!=null){
			long refContentId = doc.getLongExtraValue(DataName.refContentId.name());
			if(refContentId > 0) {
				Document refDocument = documentService.select((int)refContentId);
				if(refDocument == null) {
					logger.error("找不到文档:{}指定的参考文档:{}", doc.getUdid(), refContentId);
				} else {
					doc.setContent(refDocument.getContent());
					//获取到参考文档的内容后，删除这个属性，这样下次从缓存读取时就认为没有参考文档，不会再重复读取参考文档了
					doc.setExtraValue(DataName.refContentId.name(), null);
				}
			}
			document = doc.clone();
		}
		if(document == null){
			logger.error("根据文档代码[" + document + "]找不到文档");
			return CommonStandard.frontMessageView;		
		}
		if(document.getOwnerId() != ownerId){
			logger.error("尝试获取的文档[" + document.getUdid() + "]，其ownerId[" + document.getOwnerId() + "]与系统会话中的[" + ownerId + "]不一致");
			return CommonStandard.frontMessageView;
		}




		if(document.getCurrentStatus() != DocumentStatus.published.getId()){
			//允许使用了加密令牌的用户访问
			boolean forceView = false;
			String previewToken = ServletRequestUtils.getStringParameter(request, "preview", null);
			logger.debug("验证预览令牌:" + previewToken);
			if(previewToken != null){
				try{
					String code = PreviewToken.validate(previewToken);
					if(code != null && code.equals(documentCode)){
						logger.info("使用加密预览令牌进行强制浏览");
						forceView = true;
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			} 
			if(!forceView){
				logger.warn("尝试访问未发布的文章[udid=" + documentCode + "],状态是:" + document.getCurrentStatus());
				document = null;
				return CommonStandard.frontMessageView;		
			}
		}
		if(document.getRelatedNodeList() != null){
			if(!document.getRelatedNodeList().contains(node)){
				logger.debug("文档[" + document.getUdid() + "/" + document.getDocumentCode() + "]不应当在节点[" + node.getNodeId() + "/" + node.getPath() + "]中显示");
				return CommonStandard.frontMessageView;		

			}
		}

		if(StringUtils.isNotBlank(document.getRedirectTo())){
			logger.debug("文章将重定向到:" + document.getRedirectTo());
			response.setStatus(HttpStatus.TEMPORARY_REDIRECT.value());
			response.sendRedirect(document.getRedirectTo());		
			return null;
		}


		/*String productSmallImage = document.getExtraValue(DataName.productSmallImage.toString());
		if (productSmallImage != null){
			if(!productSmallImage.startsWith("://")){
				document.setExtraValue(DataName.productSmallImage.toString(),"://"+ request.getServerName()+ productSmallImage);
			}
		} else{
			document.setExtraValue(DataName.productSmallImage.toString(), "://"+request.getServerName()+"/style/mobile/images/logo.png");
		}*/


		writeGroupData(document);
		writeBuyHistory(document, map);
		String transactionToken = ServletRequestUtils.getStringParameter(request, "tt");
		if(StringUtils.isNotBlank(transactionToken)){
			map.put("tt",transactionToken);
		}
		//writeCommentCount(document, map, 1);
		writeRelatedDocument(document, map);
		writeNeighborDocument(document, node, map);
		//writeSideBar(request, response,map,ownerId);

		//applyReadCount(frontUser, document, map);

		writePriceData(document, 0, frontUser, null);
		long relatedQuotaOrderId = ServletRequestUtils.getLongParameter(request, "quotaOrderId", 0);
		if(relatedQuotaOrderId > 0) {
			if(frontUser == null) {
				//要拼单必须先登录
				map.put("message", new EisMessage(EisError.userNotFoundInSession.id,"请先登录"));
				return CommonStandard.frontMessageView;

			} else {
				writeQuotaData(relatedQuotaOrderId, request, document, frontUser, map);
			}
		} 
		if(frontUser != null){
			writeFavorite(document, frontUser);
			writeUserInfo(frontUser, map, request);
			writeFavoriteCount(document);
			writePraiseCount(document);
			writeMoney(frontUser,map);
			writeCartCount(frontUser,map);
			//map.put("unreadMessage",unreadMessageCount(frontUser.getUuid()));
		}
		map.put("pageTitle",document.getTitle() );
		map.put("pageDesc", document.getDesc(0));
		logger.debug("当前文档的udid为"+document.getUdid());
		map.put("document",document);
		map.put("node",node);
		map.put("pageType", "detail");//标识为详情页


		//map.put("logo", logo);
		String url=request.getRequestURL().toString();
		if (request.getQueryString()!=null){
			url=url+"?"+request.getQueryString();
		}
		return getTemplateLocation(document, node, ownerId);

	}




	private void writeQuotaData(long orderId, HttpServletRequest request, Document document, User frontUser, ModelMap map) {


		Cart cart = cartService.select(orderId);
		if(cart == null) {
			logger.error("用户{}提交赞助但是找不到对应的订单cartId={}", frontUser.getUuid(), orderId);
			return;			
		}

		if(cart.getCurrentStatus() != TransactionStandard.TransactionStatus.waitingPay.id) {
			logger.error("用户{}提交赞助但对应的订单{}不是待付款状态而是:{}", frontUser.getUuid(), orderId, cart.getCurrentStatus());
			return;		
		}

		//检查该订单是否拆分为等份，是否支持自由赞助
		if(cart.getSuccessQuota() < 0) {
			cart.setSuccessQuota(0);
		}
		if(cart.getLockedQuota() < 0) {
			cart.setLockedQuota(0);
		}

		String quotaType = cart.getQuotaType();
		if(StringUtils.isBlank(quotaType)) {
			logger.error("用户{}要赞助的订单:{}没有分单模式", frontUser.getUuid(), orderId);
			return;		
		}

		if(cart.getSuccessQuota() + cart.getLockedQuota() >= cart.getTotalQuota()) {
			logger.error("用户{}提交赞助的订单{}的总金额是:{},分单类型是:{},总分单数是:{}，已完成分单是:{},锁定分单是:{}, 已不能再赞助", frontUser.getUuid(), orderId, cart.getMoney(),quotaType, cart.getTotalQuota(), cart.getSuccessQuota(), cart.getLockedQuota());
			return;						
		}	


		if(quotaType.equalsIgnoreCase(DataName.QUOTA_TYPE_PART.name())) {
			//该订单是分单模式			
			map.put("remainSplit", (cart.getTotalQuota() - cart.getSuccessQuota() - cart.getLockedQuota()));
			map.put("totalMoney", MoneyUtils.toUserFormat(cart.getMoney()));
			map.put("totalSplit", cart.getTotalQuota());
			map.put("totalMoney", MoneyUtils.toUserFormat(cart.getMoney()));
		} else {
			//该订单是自由金额模式，把quota X 100得到剩余金额
			map.put("remainMoney", (cart.getTotalQuota() - cart.getSuccessQuota() - cart.getLockedQuota()) / 100);		

		}
		logger.debug("用户{}提交赞助的订单{}的总金额是:{},分单类型是:{},总分单数是:{}，已完成分单是:{}, 锁定分单是:{}", frontUser.getUuid(), orderId, cart.getMoney(),quotaType, cart.getTotalQuota(), cart.getSuccessQuota(), cart.getLockedQuota());

	}

	private int unreadMessageCount(long uuid){
		MessageCriteria mc = new MessageCriteria();
		mc.setReceiverId(uuid);
		mc.setCurrentStatus(MessageStatus.unread.id);
		return userMessageService.count(mc);
	}
}



