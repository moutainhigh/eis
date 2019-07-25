package com.maicard.wpt.np;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.maicard.common.base.BaseProcessor;
import com.maicard.common.criteria.CommentCriteria;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.CommentService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.GlobalOrderIdService;
import com.maicard.common.util.Paging;
import com.maicard.money.criteria.PayTypeCriteria;
import com.maicard.money.domain.PayType;
import com.maicard.money.service.MoneyService;
import com.maicard.money.service.PayMethodService;
import com.maicard.money.service.PayService;
import com.maicard.money.service.PayTypeService;
import com.maicard.money.service.PriceService;
import com.maicard.product.domain.Product;
import com.maicard.product.service.ProductService;
import com.maicard.security.criteria.UserRelationCriteria;
import com.maicard.security.domain.User;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.FrontUserService;
import com.maicard.security.service.UserRelationService;
import com.maicard.site.criteria.DocumentCriteria;
import com.maicard.site.criteria.NodeCriteria;
import com.maicard.site.domain.Document;
import com.maicard.site.domain.Node;
import com.maicard.site.service.DocumentNodeRelationService;
import com.maicard.site.service.DocumentService;
import com.maicard.site.service.NodeProcessor;
import com.maicard.site.service.NodeService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.ObjectType;
import com.maicard.standard.SiteStandard.NodeType;

@Service
public class CommonPayNodeProcessor  extends BaseProcessor implements NodeProcessor{
	@Resource
	private ConfigService configService;
	@Resource
	private NodeService nodeService;	
	@Resource
	private DocumentService documentService;
	@Resource
	private GlobalOrderIdService globalOrderIdService;
	@Resource
	private CertifyService certifyService;
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
	@Resource
	private DocumentNodeRelationService documentNodeRelationService;
	@Resource
	private CommentService commentService;
	@Resource
	private UserRelationService userRelationService;
	@Resource
	private FrontUserService frontUserService;
	@Resource
	private PriceService priceService;
//	private int defaultRowsPerPage = 3;
	private int maxIndexPerPage=9;
//	private int rowsPerPage = 10;

	@Override
	public String index(HttpServletRequest request,
			HttpServletResponse response, ModelMap map) throws Exception {	
		
		Node node = null;
		try{
			node = (Node)map.get("node");
		}catch(Exception e){}

		if(node == null){
			return CommonStandard.frontMessageView;		
		}
		int nodeId = node.getNodeId();
		if(node.getRedirect() > 0){
			nodeId = node.getRedirect();
			node = nodeService.select(nodeId);
			if(node == null){
				return CommonStandard.frontMessageView;		
			}
		}
		logger.debug("处理节点[" + node.getName() + "/" + node.getNodeId());

		/*
		 * 首页数据
		 */
		/*Paging paging = new Paging(defaultRowsPerPage);
		int page=1;
		int num = 0;
		
		//新鲜出炉
		List<Document> listS = new ArrayList<Document>();
//		List<Document> singAndPlayList = new ArrayList<Document>();
		Map<String,Object> mapSoS = new HashMap<String,Object>();
		List<Map<String,Object>> listMapS = new ArrayList<Map<String,Object>>();
		DocumentCriteria dc = new DocumentCriteria();
		while(num<10){
			paging.setCurrentPage(page);
			dc.setPaging(paging);
			listS = documentService.listOnPage(dc);
			logger.debug("新鲜出炉查询："+listS.size());
			for(Document doc:listS){
					if(!(doc.getDocumentTypeId()==171001 || doc.getDocumentTypeId()==171004 ||doc.getDocumentTypeId()==171006)){
						continue;
					}
					mapSoS = new HashMap<String,Object>();
					doc.setContent(null);
					mapSoS.put("document", doc);
					logger.debug("哈哈哈哈"+doc.getUdid());
					writeReadCount(doc, mapSoS);
					writeCommentCount(doc, mapSoS);
					try {
						mapSoS.put("productSmallImage", productService.getProductByDocument(doc).getExtraValue("productSmallImage"));
					} catch (Exception e1) {
						mapSoS.put("productSmallImage","");
					}
					logger.debug("VIP图片"+mapSoS.get("productSmallImage"));
					try {
						mapSoS.put("userName", frontUserService.select(doc.getPublisherId()).getUsername());
						mapSoS.put("userHeadPic", frontUserService.select(doc.getPublisherId()).getExtraValue("userHeadPic"));
					} catch (Exception e) {
						mapSoS.put("userName", "未知作者");
						mapSoS.put("userHeadPic","");
						logger.error("找不到发布人的信息");
					}
					listMapS.add(mapSoS);
					num++;
					if(num>9){
						break;
					}
			}
			if(listS==null || listS.size()<10){
				break;
			}
			page++;
			if(page>10){//避免死循环
				break;
			}
		}
		page=1;
		num=0;
		map.put("recentList", listMapS);
		logger.debug("新鲜出炉数据条数："+listMapS.size());
		
		
		
		//流行弹唱
		
		List<Document> list = new ArrayList<Document>();
//		List<Document> singAndPlayList = new ArrayList<Document>();
		Map<String,Object> mapSo = new HashMap<String,Object>();
		Map<String,Object> mapSo2 = new HashMap<String,Object>();
		List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> listMap2 = new ArrayList<Map<String,Object>>();
		
		DocumentCriteria dc1 = new DocumentCriteria();
		dc1.setNodePath("friend/sing");
		paging = new Paging(12);
		while(num<24){
			paging.setCurrentPage(page);
			dc1.setPaging(paging);
			list = documentService.listOnPage(dc1);
			for(Document doc:list){
				mapSo = new HashMap<String,Object>();
				logger.debug("证明1："+doc);
					if(!(doc.getDocumentTypeId()==171001 || doc.getDocumentTypeId()==171004 ||doc.getDocumentTypeId()==171006)){
						continue;
					}
					doc.setContent(null);
					if(num<13){
//						playAndSingList.add(doc);
						mapSo.put("document", doc);
						writeReadCount(doc, mapSo);
						writeCommentCount(doc, mapSo);
						try {
							mapSo.put("userName", frontUserService.select(doc.getPublisherId()).getUsername());
							mapSo.put("userHeadPic", frontUserService.select(doc.getPublisherId()).getExtraValue("userHeadPic"));
						} catch (Exception e) {
							mapSo.put("userName", "未知作者");
							mapSo.put("userHeadPic","");
							logger.error("找不到发布人的信息");
						}
						listMap.add(mapSo);
					}else{
//						singAndPlayList.add(doc);
						mapSo2.put("document", doc);
						writeReadCount(doc, mapSo2);
						writeCommentCount(doc, mapSo2);
						try {
							mapSo2.put("userName", frontUserService.select(doc.getPublisherId()).getUsername());
							mapSo2.put("userHeadPic", frontUserService.select(doc.getPublisherId()).getExtraValue("userHeadPic"));
						} catch (Exception e) {
							mapSo2.put("userName", "未知作者");
							mapSo2.put("userHeadPic","");
							logger.error("找不到发布人的信息");
						}
						listMap2.add(mapSo2);
					}
					num++;
					if(num>23){
						break;
					}
			}
			if(list==null || list.size()<12){
				break;
			}
			page++;
			if(page>10){//避免死循环
				break;
			}
		}
		page=1;
		num=0;
		map.put("playAndSingList", listMap);
		logger.debug("流行弹唱数据条数："+listMap.size());
		map.put("singAndPlayList", listMap2);
		logger.debug("弹唱数据条数："+listMap2.size());
		//入门20课
		List<Document> listN= new ArrayList<Document>();
		
		listMap = new ArrayList<Map<String,Object>>();
		DocumentCriteria dc2 = new DocumentCriteria();
		dc2.setNodePath("newbie");
		paging = new Paging(12);
		while(num<12){
			paging.setCurrentPage(page);
			dc2.setPaging(paging);
			listN = documentService.listOnPage(dc2);
			for(Document doc:listN){
				mapSo = new HashMap<String,Object>();
					if(!(doc.getDocumentTypeId()==171001 || doc.getDocumentTypeId()==171004 ||doc.getDocumentTypeId()==171006)){
						continue;
					}
					doc.setContent(null);
					mapSo.put("document", doc);
					writeReadCount(doc, mapSo);
					writeCommentCount(doc, mapSo);
					try {
						mapSo.put("userName", frontUserService.select(doc.getPublisherId()).getUsername());
						mapSo.put("userHeadPic", frontUserService.select(doc.getPublisherId()).getExtraValue("userHeadPic"));
					} catch (Exception e) {
						mapSo.put("userName", "未知作者");
						mapSo.put("userHeadPic","");
						logger.error("找不到发布人的信息");
					}
					listMap.add(mapSo);
					num++;
					if(num>11){
						break;
					}
			}
			if(listN==null || listN.size()<12){
				break;
			}
			page++;
			if(page>10){//避免死循环
				break;
			}
		}
		page=1;
		num=0;
		map.put("learnTwentyList", listMap);
		logger.debug("入门20课数据条数："+listMap.size());
		//学习文章
		List<Document> list2= new ArrayList<Document>();
		DocumentCriteria dc3 = new DocumentCriteria();
		dc3.setNodePath("study");
		listMap = new ArrayList<Map<String,Object>>();
		paging = new Paging(12);
		while(num<12){
			paging.setCurrentPage(page);
			dc3.setPaging(paging);
			list2 = documentService.listOnPage(dc3);
			for(Document doc:list2){
				mapSo = new HashMap<String,Object>();
				if(doc!=null){
					if(!(doc.getDocumentTypeId()==171001 || doc.getDocumentTypeId()==171004 ||doc.getDocumentTypeId()==171006)){
						continue;
					}
					doc.setContent(null);
//					studyDocList.add(doc);
					mapSo.put("document", doc);
					writeReadCount(doc, mapSo);
					writeCommentCount(doc, mapSo);
					try {
						mapSo.put("userName", frontUserService.select(doc.getPublisherId()).getUsername());
						mapSo.put("userHeadPic", frontUserService.select(doc.getPublisherId()).getExtraValue("userHeadPic"));
					} catch (Exception e) {
						mapSo.put("userName", "未知作者");
						mapSo.put("userHeadPic","");
						logger.error("找不到发布人的信息");
					}
					listMap.add(mapSo);
					num++;
					if(num>11){
						break;
					}
				}
			}
			if(list2==null || list2.size()<12){
				break;
			}
			page++;
			if(page>10){//避免死循环
				break;
			}
		}
		page=1;
		num=0;
		map.put("studyDocList", listMap);
		logger.debug("学习文章数据条数："+listMap.size());
		//vip最新
		List<Document> list3 = new ArrayList<Document>();
		listMap = new ArrayList<Map<String,Object>>();
		paging = new Paging(defaultRowsPerPage);
		DocumentCriteria dc4 = new DocumentCriteria();
		dc4.setNodePath("vip");
		while(num<10){
			paging.setCurrentPage(page);
			dc4.setPaging(paging);
			list3 = documentService.listOnPage(dc4);
			for(Document doc:list3){
				mapSo = new HashMap<String,Object>();
					if(!(doc.getDocumentTypeId()==171001 || doc.getDocumentTypeId()==171004 ||doc.getDocumentTypeId()==171006)){
						continue;
					}
					doc.setContent(null);
					mapSo.put("document", doc);
					try {
						mapSo.put("productSmallImage", productService.getProductByDocument(doc).getExtraValue("productSmallImage"));
					} catch (Exception e1) {
						mapSo.put("productSmallImage","");
					}
					logger.debug("VIP图片"+mapSo.get("productSmallImage"));
					writeReadCount(doc, mapSo);
					writeCommentCount(doc, mapSo);
					try {
						mapSo.put("userName", frontUserService.select(doc.getPublisherId()).getUsername());
						mapSo.put("userHeadPic", frontUserService.select(doc.getPublisherId()).getExtraValue("userHeadPic"));
					} catch (Exception e) {
						mapSo.put("userName", "未知作者");
						mapSo.put("userHeadPic","");
						logger.error("找不到发布人的信息");
					}
					listMap.add(mapSo);
					num++;
					if(num>9){
						break;
					}
			}
			if(list3==null || list3.size()<10){
				break;
			}
			page++;
			if(page>10){//避免死循环
				break;
			}
		}
		page=1;
		num=0;
		map.put("vipRecentList", listMap);
		logger.debug("vip最新数据条数："+listMap.size());
		//琴友三类
			//原创
		List<Document> list5 = new ArrayList<Document>();
		mapSo = new HashMap<String,Object>();
		listMap = new ArrayList<Map<String,Object>>();
		paging = new Paging(12);
		DocumentCriteria dc5 = new DocumentCriteria();
		dc5.setNodePath("friend/original");
		while(num<12){
			paging.setCurrentPage(page);
			dc5.setPaging(paging);
			list5 = documentService.listOnPage(dc5);
			for(Document doc:list5){
				mapSo = new HashMap<String,Object>();
				if(doc!=null){
					if(!(doc.getDocumentTypeId()==171001 || doc.getDocumentTypeId()==171004 ||doc.getDocumentTypeId()==171006)){
						continue;
					}
					doc.setContent(null);
					mapSo.put("document", doc);
					writeReadCount(doc, mapSo);
					writeCommentCount(doc, mapSo);
					try {
						mapSo.put("userHeadPic", frontUserService.select(doc.getPublisherId()).getExtraValue("userHeadPic"));
					} catch (Exception e) {
						mapSo.put("userName", "未知作者");
						mapSo.put("userHeadPic","");
						logger.error("找不到发布人的信息");
					}
					listMap.add(mapSo);
					num++;
					if(num>11){
						break;
					}
				}
			}
			if(list5==null || list5.size()<12){
				break;
			}
			page++;
			if(page>10){//避免死循环
				break;
			}
		}
		page=1;
		num=0;
		map.put("originalList", listMap);
		logger.debug("原创数据条数："+listMap.size());
			//指弹
		List<Document> list6 = new ArrayList<Document>();
		mapSo = new HashMap<String,Object>();
		listMap = new ArrayList<Map<String,Object>>();
		paging = new Paging(12);
		DocumentCriteria dc6 = new DocumentCriteria();
		dc6.setNodePath("friend/thrum");
		while(num<12){
			paging.setCurrentPage(page);
			dc6.setPaging(paging);
			list6 = documentService.listOnPage(dc6);
			for(Document doc:list6){
				mapSo = new HashMap<String,Object>();
				if(doc!=null){
					if(!(doc.getDocumentTypeId()==171001 || doc.getDocumentTypeId()==171004 ||doc.getDocumentTypeId()==171006)){
						continue;
					}
					doc.setContent(null);
//					fingersList.add(doc);
					mapSo.put("document", doc);
					writeReadCount(doc, mapSo);
					writeCommentCount(doc, mapSo);
					try {
						mapSo.put("userName", frontUserService.select(doc.getPublisherId()).getUsername());
						mapSo.put("userHeadPic", frontUserService.select(doc.getPublisherId()).getExtraValue("userHeadPic"));
					} catch (Exception e) {
						mapSo.put("userName", "未知作者");
						mapSo.put("userHeadPic","");
						logger.error("找不到发布人的信息");
					}
					listMap.add(mapSo);
					num++;
					if(num>11){
						break;
					}
				}
			}
			if(list6==null || list6.size()<12){
				break;
			}
			page++;
			if(page>10){//避免死循环
				break;
			}
		}
		page=1;
		num=0;
		map.put("fingersList", listMap);
		logger.debug("指弹数据条数："+listMap.size());
		*/
		/**
		 * ************************用一个通用的方法把首页可能需要提供数据一次性提供*****************************
		 * 也就是把每个业务节点下面的数据各自取一部分出来展示
		 */
		//查询系统中可能需要展示数据的节点
		NodeCriteria nodeCri = new NodeCriteria();
		nodeCri.setOwnerId(node.getOwnerId());
		nodeCri.setNodeTypeId(NodeType.business.getId());
		List<Node> nodeList = nodeService.list(nodeCri);
		logger.debug("需要显示数据的栏目共有"+(nodeList==null?0:nodeList.size())+"条");
		for(Node nod : nodeList){
			int number = 0;
			int pageNum = 1;
			List<Map<String,Object>> listMAP = new ArrayList<Map<String,Object>>();
			List<Document> docList = new ArrayList<Document>();
			Map<String,Object> hasMap = new HashMap<String,Object>();
			Paging paging = new Paging(maxIndexPerPage);
			DocumentCriteria docCri = new DocumentCriteria();
			docCri.setNodePath(nod.getPath());
			paging.setCurrentPage(pageNum);
			docCri.setPaging(paging);
			docList = documentService.listOnPage(docCri);
			if(docList!=null){
				for(Document doc:docList){
					hasMap = new HashMap<String,Object>();
					if(doc!=null){
						Product product = productService.getProductByDocument(doc);
						if(product==null){
							continue;
						}
						hasMap.put("document", getDocByDelDataMap(doc));
						writeCommentCount(doc, hasMap);
						try {
							hasMap.put("userName", frontUserService.select(doc.getPublisherId()).getUsername());
							hasMap.put("userHeadPic", frontUserService.select(doc.getPublisherId()).getExtraValue("userHeadPic"));
						} catch (Exception e) {
							logger.error("找不到发布人的信息");
						}
						hasMap.put("buy_money", product.getBuyMoney());
						hasMap.put("vipFree", product.getExtraValue("vipFree"));
						listMAP.add(hasMap);
						number++;
						if(number>11){
							break;
						}
					}
				}
			}
			map.put(nod.getAlias(), listMAP);
			logger.debug("节点"+nod.getAlias()+"的listMAP"+listMAP);
		}
		//三种必推荐
		//登录用户和非登录用户都可以使用
//		User frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
//		writeHotSaleList(request, response,frontUser, map);
//		writeNewestList(request, response,frontUser, map);
//		writeRecommendList(request, response,frontUser, map);
//		writeCrouselFigureList(request, response, frontUser, map);
		//***************************************************************************************
		PayTypeCriteria payTypeCriteria = new PayTypeCriteria();
		payTypeCriteria.setCurrentStatus(BasicStatus.normal.getId());
		List<PayType> payTypeList = payTypeService.list(payTypeCriteria);
		if(payTypeList == null || payTypeList.size() < 1){
			logger.error("支付方式列表为空");
			map.put("message", new EisMessage(EisError.serviceUnavaiable.getId(), "系统繁忙，请稍后再试"));
			return CommonStandard.frontMessageView;
		}
		map.put("payTypeList", payTypeList);


		String defaultTemplateLocation =null;
		defaultTemplateLocation = node.getTemplateLocation();
		if(defaultTemplateLocation != null){
			defaultTemplateLocation = defaultTemplateLocation.replaceAll("\\.\\w+$", "");
		}
		logger.debug("COMMONINDEX");
		return defaultTemplateLocation;
	}

	
	protected Map<String,String> getDocByDelDataMap(Document doc){
		logger.debug("拆开"+doc.getExtraValue("productGallery"));
		Map<String,String> map = new HashMap<String,String>();
		/*writePraiseCount(doc);
		writeCommentCount(doc);
		writeReadCount(doc);
		writeFavoriteCount(doc);*/
		map.put("productGallery", doc.getExtraValue("productGallery"));
		map.put("productSmallImage", doc.getExtraValue("productSmallImage"));
		map.put("readCount", doc.getDocumentDataMap().get("readCount").getDataValue());
		map.put("favoriteCount", doc.getDocumentDataMap().get("favoriteCount").getDataValue());
		map.put("commentCount", doc.getDocumentDataMap().get("commentCount").getDataValue());
		map.put("praiseCount", doc.getDocumentDataMap().get("praiseCount").getDataValue());
		map.put("viewUrl", doc.getViewUrl());
		map.put("title", doc.getTitle());
		map.put("uuid", doc.getUdid()+"");
		//是否是轮播图
		if (doc.getDisplayTypeId() == 176005) {
			map.put("ShufflingFigure", "true");
		}else {
			map.put("ShufflingFigure", "false");
		}
		logger.debug("wocaonima"+map);
		return map;
	}
	@Override
	public String detail(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {

		Node node = null;
		try{
			node = (Node)map.get("node");
		}catch(Exception e){}

		if(node == null){
			return CommonStandard.frontMessageView;		
		}
		int nodeId = node.getNodeId();
		if(node.getRedirect() > 0){
			nodeId = node.getRedirect();
			node = nodeService.select(nodeId);
			if(node == null){
				return CommonStandard.frontMessageView;		
			}
		}
		logger.debug("处理节点[" + node.getName() + "/" + node.getNodeId());
		int payTypeId = 0;
		try{
			payTypeId = Integer.parseInt(map.get("documentCode").toString());
		}catch(Exception e){
			e.printStackTrace();
		}
		if(payTypeId == 0){
			return node.getTemplateLocation();
		}
		PayType payType = payTypeService.select(payTypeId);
		if(payType == null || payType.getCurrentStatus() != BasicStatus.normal.getId()){
			map.put("message", "无效的支付方式[" + payTypeId + "]");
			return node.getTemplateLocation();
		}
		map.put("payType", payType);

		PayTypeCriteria payTypeCriteria = new PayTypeCriteria();
		payTypeCriteria.setCurrentStatus(BasicStatus.normal.getId());
		List<PayType> payTypeList = payTypeService.list(payTypeCriteria);
		map.put("payTypeList", payTypeList);



		String defaultTemplateLocation =null;
		try{
			defaultTemplateLocation = node.getTemplateLocation();
			defaultTemplateLocation = defaultTemplateLocation.replaceAll("\\.\\w+$", "");
			defaultTemplateLocation = defaultTemplateLocation.replaceAll("index", "detail");
			defaultTemplateLocation = defaultTemplateLocation.replaceAll("list", "detail");
		}catch(NullPointerException e){
		}
		if(defaultTemplateLocation == null){
			return node.getTemplateLocation();
		}
		logger.debug("COMMONDETAIL");
		return defaultTemplateLocation;
	}
	protected void writeCommentCount(Document document, Map<String,Object> map) {
		//获取评论数
		boolean isProduct = false;
		String productCode = document.getExtraValue(DataName.productCode.toString());

		if(StringUtils.isBlank(productCode)){
			isProduct = false;
		} else {
			isProduct = true;
		}
		CommentCriteria commentCriteria = new CommentCriteria(document.getOwnerId());
		commentCriteria.setCurrentStatus(CommentCriteria.STATUS_PUBLISHED);
		if(isProduct){
			logger.debug("文档[" + document.getUdid() + "]是一个产品文档，获取对应产品[" + productCode + "]的评价");
			Product product = productService.select(productCode, document.getOwnerId());
			if(product == null){
				logger.error("找不到文档[" + document.getUdid() + "]对应的产品:" + productCode);
				return;
			}
			//查看产品是否有评论
			commentCriteria.setObjectType(ObjectType.product.name());
			commentCriteria.setObjectId(product.getProductId());

		} else {
			//查看文章是否有评论
			commentCriteria.setObjectType(ObjectType.document.name());
			commentCriteria.setObjectId(document.getUdid());

		}

		int commentCount = commentService.count(commentCriteria);
		logger.debug("针对对象[" + commentCriteria.getObjectType() + "#" + commentCriteria.getObjectId() + "]的评论数是:" + commentCount);
//		map.put("praiseCount",commentCount);		
		document.setExtraValue("commentCount", String.valueOf(commentCount));
	}
	/**
	 * 写入该文章对应的阅读数
	 * @param document
	 * @param map
	 */
	protected void writeReadCount(Document document, Map<String,Object> map){


		UserRelationCriteria userRelationCriteria = new UserRelationCriteria(document.getOwnerId());
		userRelationCriteria.setObjectType(ObjectType.document.name());
		userRelationCriteria.setRelationType(UserRelationCriteria.RELATION_TYPE_READ);
		userRelationCriteria.setObjectId(document.getUdid());
		int readCount = userRelationService.getRelationCount(userRelationCriteria);
		if(readCount < 1){
			readCount = 1;
		}
		map.put("readCount", readCount);

	}
	
	protected void writeRelationCount(Document document) {
		document.setId(document.getUdid());
		userRelationService.setDynamicData(document);		
	}/*
	private void fetchProduct(Document document,ModelMap map, User frontUser, String transactionToken){ 
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
		
		String productCode = document.getExtraValue(DataName.productCode.toString());
		
		boolean isProduct = false;
		Product product = null;

		if(StringUtils.isBlank(productCode)){
			logger.debug("文档[" + document.getUdid() + "]未定义扩展数据productCode的值，将不进行产品数据和价格数据的处理");
		} else {
			isProduct = true;
			product = productService.select(productCode, ownerId);
			if(product == null){
				logger.warn("找不到产品文档[" + document.getUdid() + "]所定义的产品[" + productCode + "]");
				return;
			}
			if(product.getOwnerId() != ownerId){
				logger.warn("产品文档[" + document.getUdid() + "]所定义的产品[" + productCode + "]其ownerId[" + product.getOwnerId() + "]与系统会话中的[" + ownerId + "]不一致");
				return;
			}
		}
		
		if(!isProduct){
			return;
		}
		boolean isActivity = false;
		if(document.getDocumentTypeCode() != null && document.getDocumentTypeCode().equals("activity")){
			isActivity =true;
		}
		Price price = null;
		if(frontUser != null && transactionToken != null){
			price = priceService.getPriceByToken(product.getProductId(), frontUser.getUuid(), transactionToken);
		} else {
			logger.info("当前没有终端用户信息，不计算特定价格");
		}
		productService.generateProductDocumentData(product,document);
		boolean priceDataResult = false;
		if(price != null){
			priceDataResult = priceService.generatePriceDocumentData(product, document, price);
			logger.debug("根据产品[" + product.getProductId() + "/" + product.getProductCode() + "]和价格[" + price.getPriceId() + "]为文档生成价格结果:" + priceDataResult);
		} else {
			if(isActivity){
				priceDataResult = priceService.generatePriceDocumentData(product, document, PriceType.PRICE_PROMOTION.toString());
				logger.debug("根据产品[" + product.getProductId() + "/" + product.getProductCode() + "]为文档生成活动价格结果:" + priceDataResult);

			} else {
				priceDataResult = priceService.generatePriceDocumentData(product, document, PriceType.PRICE_STANDARD.toString());
				logger.debug("根据产品[" + product.getProductId() + "/" + product.getProductCode() + "]为文档生成标准价格结果:" + priceDataResult);
			}

		}
	}*/
	/*private Document getDocByDelDataMap(Document doc){
		Document document = new Document();
		document.setExtraValue("productGallery", doc.getExtraValue("productGallery"));
		document.setExtraValue("praiseCount", doc.getExtraValue("praiseCount"));
		document.setExtraValue("productSmallImage", doc.getExtraValue("productSmallImage"));
		document.setExtraValue("readCount", doc.getExtraValue("readCount"));
		document.setExtraValue("favoriteCount", doc.getExtraValue("favoriteCount"));
		document.setExtraValue("commentCount", doc.getExtraValue("commentCount"));
		document.setExtraValue("viewUrl", doc.getViewUrl());
		return document;
	}*/
	@Override
	public void writeExtraData(ModelMap map, User frontUser, Map<String, Object> parameter) {
		// TODO Auto-generated method stub
		
	}


}