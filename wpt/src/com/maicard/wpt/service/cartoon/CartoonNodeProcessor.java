package com.maicard.wpt.service.cartoon;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.ServletRequestUtils;

import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.CenterDataService;
import com.maicard.common.service.CookieService;
import com.maicard.common.service.SiteDomainRelationService;
import com.maicard.common.util.JsonUtils;
import com.maicard.common.util.NumericUtils;
import com.maicard.common.util.Paging;
import com.maicard.common.util.PreviewToken;
import com.maicard.common.util.StringTools;
import com.maicard.mb.criteria.MessageCriteria;
import com.maicard.money.criteria.PriceCriteria;
import com.maicard.money.domain.Price;
import com.maicard.product.service.StockService;
import com.maicard.security.criteria.UserRelationCriteria;
import com.maicard.security.domain.User;
import com.maicard.security.domain.UserRelation;
import com.maicard.site.criteria.DocumentCriteria;
import com.maicard.site.criteria.NodeCriteria;
import com.maicard.site.domain.Document;
import com.maicard.site.domain.Node;
import com.maicard.site.service.NodeProcessor;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.MessageStandard.MessageStatus;
import com.maicard.standard.ObjectType;
import com.maicard.standard.OperateResult;
import com.maicard.standard.SecurityStandard.UserTypes;
import com.maicard.standard.SiteStandard.DocumentStatus;
import com.maicard.standard.SiteStandard.NodeType;
import com.maicard.wpt.np.WptDefaultNodeProcessor;


/**
 * 分级别浏览的处理器
 * 某个级别的用户只能查看指定的内容
 *
 *
 *
 */
@Service
public class CartoonNodeProcessor extends WptDefaultNodeProcessor implements NodeProcessor{



	@Resource
	private SiteDomainRelationService siteDomainRelationService;

	@Resource
	private CenterDataService centerDataService;

	@Resource
	private CookieService cookieService;

	@Resource
	private StockService stockService;


	protected static final String SUBSCRIBE_CACHE_MAP_NAME = "USER_SUBSCRIBE_MAP";
	protected static final int SUBSCRIBE_CACHE_SEC = 3600 * 24;
	protected static final int DEFAULT_VIEW_LEVEL = 1;

	protected static final int MIN_FAVORITE_COUNT = 1000;
	protected static final int MIN_READ_COUNT = 10000;

	protected static final String PRICE_TYPE = "coin";

	private static String UPLOAD_PATH;

	//FIXME
	private static final String FILE_OUTLINK_PATH = "/upload/content";

	//多长时间写入一次最后阅读时间
	private static final int SYNC_SEC = 600;

	@PostConstruct
	public void init() {
		
		UPLOAD_PATH = configService.getValue(DataName.INTER_DATA_DIR.name(),0);
	}

	/**
	 * 获取当前用户的浏览级别
	 * 
	 * 
	 * @author GHOST
	 * @date 2018-10-16
	 */
	protected  int getViewLevel(User frontUser) {
		if(frontUser == null) {
			return 0;
		}
		return 0;
	}

	@Override
	public String index(HttpServletRequest request,
			HttpServletResponse response, ModelMap map) throws Exception {
		Node node = (Node)map.get("node");

		if(node == null){
			return CommonStandard.frontMessageView;
		}
		long ownerId =  NumericUtils.parseLong(map.get("ownerId"));

		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return CommonStandard.frontMessageView;		
		}
		if(node.getOwnerId() != ownerId){
			logger.error("尝试获取的栏目[" + node.getNodeId() + "]，其ownerId[" + node.getOwnerId() + "]与系统会话中的[" + ownerId + "]不一致");
			return CommonStandard.frontMessageView;
		}
		logger.debug("处理栏目:" + node.getName());



		User frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.id);
		if(frontUser != null){
			writeUserInfo(frontUser, map, request);
		}
		//writeSideBar(request, response,map,ownerId);
		writeRecommendList(request, response, frontUser,map);
		writeCrouselFigureList(request, response, frontUser, map);
		int rows = ServletRequestUtils.getIntParameter(request, "rows", ROWS_PER_PAGE);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);


		if(node.getNodeTypeId() == NodeType.navigation.id || node.getNodeTypeId() == NodeType.business.id) {
			applyExtraInfo(node);
			if(node.getNodeTypeId() == NodeType.business.id) {
				applyPrice(node, frontUser);
			}
		}

		Paging paging = new Paging(rows);
		paging.setCurrentPage(page);

		if(node.getNodeTypeId() == NodeType.navigation.id) {

			String filter = ServletRequestUtils.getStringParameter(request, "filter");

			String sort = ServletRequestUtils.getStringParameter(request, "sort");

			//列出当前栏目的所有子栏目
			NodeCriteria nodeCriteria = new NodeCriteria(node.getOwnerId());
			nodeCriteria.setPriceType(PRICE_TYPE);
			if(filter != null) {
				if(filter.equalsIgnoreCase("story")) {
					nodeCriteria.setCategory("unfinished");
					if(sort != null) {
						nodeCriteria.setSort(sort);
					} 

				}
				if(filter.equalsIgnoreCase("finished")) {
					nodeCriteria.setCategory("finished");
					if(sort.equalsIgnoreCase("1coin")) {
						nodeCriteria.setMinPrice(1);
						nodeCriteria.setMaxPrice(1);
					}
				}
				if(filter.equalsIgnoreCase("rank")) {
					if(sort.equalsIgnoreCase("hot")) {
						nodeCriteria.setUserRelation(UserRelationCriteria.RELATION_TYPE_READ);
					}
					if(sort.equalsIgnoreCase("recent")) {
						nodeCriteria.setOrderBy("last_modified DESC");
					}
					if(sort.equalsIgnoreCase("week")) {
						//从周排名缓存中获取数据
						//nodeCriteria.setOrderBy("lastModified DESC");
					}

				}
				if(filter.equalsIgnoreCase("free")) {
					nodeCriteria.setMaxPrice(0);
				}
			}

			logger.debug("设置查询条件:filter=" + filter + ",sort=" + sort + ",JSON=" + JsonUtils.toStringFull(nodeCriteria));
			nodeCriteria.setParentNodeId(node.getNodeId());
			if(frontUser != null) {
				nodeCriteria.setMaxViewLevel((int)frontUser.getLevel());
			}
			if(nodeCriteria.getMaxViewLevel() <= 0) {
				nodeCriteria.setMaxViewLevel(DEFAULT_VIEW_LEVEL);
			}
			int totalResults = nodeService.count(nodeCriteria);
			int totalPages = totalResults%rows==0?totalResults/rows:totalResults/rows+1;
			map.put("totalPages", totalPages);
			map.put("totalResults", totalResults);


			nodeCriteria.setPaging(paging);
			List<Node> subNodeList = nodeService.listOnPageClone(nodeCriteria);
			logger.info("当前栏目:{}/{}是导航栏目，放入子栏目共{}个", node.getName(), node.getNodeId(), subNodeList.size());

			//查找最后一章
			for(Node subNode : subNodeList) {
				getLastestDoc(subNode);

				applyExtraInfo(subNode);

				if(subNode.getNodeTypeId() == NodeType.business.id) {
					applyPrice(subNode, frontUser);
				}
			}
			map.put("subNodeList", subNodeList);

		} else {

			List<Document> documentList = new ArrayList<Document>();
			List<Document> documentList2 = new ArrayList<Document>();



			String search = ServletRequestUtils.getStringParameter(request, "search");
			String title = ServletRequestUtils.getStringParameter(request, "title");
			logger.debug("是否是ProtectedContentNodeProcessor搜索：search="+search+"#title="+title);
			DocumentCriteria documentCriteria = new DocumentCriteria();
			documentCriteria.setCurrentStatus(DocumentStatus.published.getId());
			if(node.getNodeTypeId() == NodeType.business.id) {
				documentCriteria.setOrderBy(" publish_time ASC");
			}
			if(frontUser != null) {
				documentCriteria.setMaxViewLevel((int)frontUser.getLevel());
			}
			if(documentCriteria.getMaxViewLevel() <= 0) {
				documentCriteria.setMaxViewLevel(DEFAULT_VIEW_LEVEL);
			}
			if(search!=null && "search".equals(search)){
				documentCriteria.setTitle(title);
			}else{
				documentCriteria.setNodePath(node.getPath());
			}


			int totalResults = documentService.count(documentCriteria);
			documentCriteria.setOwnerId(ownerId);
			documentCriteria.setPaging(paging);


			documentList = documentService.listOnPage(documentCriteria);
			int totalPages = totalResults%rows==0?totalResults/rows:totalResults/rows+1;
			map.put("totalPages", totalPages);
			map.put("totalResults", totalResults);

			logger.info("发布到本栏目的所有文章数:" + (documentList == null? "空":documentList.size()));
			if(documentList != null){
				for(Document d1 : documentList){
					Document document = d1.clone();
					//writeProductData(null, d2, 0, frontUser, null);

					checkBuyLink(node, document, frontUser);
					document.setContent("");
					documentList2.add(document);
				}
				map.put("newsList",documentList2);
			}
		}
		if(frontUser!=null){
			map.put("unreadMessage",unreadMessageCount(frontUser.getUuid()));
		}

		String defaultTemplateLocation =null;
		try{
			defaultTemplateLocation = node.getTemplateLocation();
		}catch(NullPointerException e){
		}
		if(defaultTemplateLocation == null){
			logger.error("栏目[" + node.getName() + "/" + node.getNodeId() + "]未定义默认模版");
			return "";
		}
		defaultTemplateLocation = defaultTemplateLocation.replaceAll("\\.\\w+$", "");

		return defaultTemplateLocation;
	}



	protected void applyExtraInfo(Node node) {
		float point = node.getFloatExtraValue("point");
		if(point <= 0) {
			node.setExtraValue("point", "9.0");
		}
		String author = node.getExtraValue("author");
		if(author == null) {
			node.setExtraValue("author", "-");
		}
		long favoriteCount = node.getLongExtraValue("favoriteCount");
		if(favoriteCount < MIN_FAVORITE_COUNT) {
			node.setExtraValue("favoriteCount", String.valueOf(MIN_FAVORITE_COUNT + favoriteCount));
		}
		long readCount = node.getLongExtraValue("readCount");
		if(readCount < MIN_READ_COUNT) {
			node.setExtraValue("readCount", String.valueOf(MIN_READ_COUNT + readCount));
		}


	}

	protected void applyPrice(Node node, User frontUser){
		if(node.getNodeTypeId() == NodeType.business.id) {
			float cost = 0f;
			if(node.getExtraValue(DataName.price.name()) == null) {
				//没有设置价格，检查价格
				PriceCriteria priceCriteria = new PriceCriteria(node.getOwnerId());
				priceCriteria.setObjectType(ObjectType.node.name());
				priceCriteria.setObjectId(node.getNodeId());
				priceCriteria.setCurrentStatus(BasicStatus.normal.id);
				List<Price> priceList = priceService.list(priceCriteria);
				Price price = null;
				if(priceList.size() > 0) {
					price = priceList.get(0);
					node.setExtraValue("price", String.valueOf(price.getCoin()));
					logger.info("为栏目#{}设置价格为:{}", node.getNodeId(), price.getCoin());
					cost = price.getCoin();
				} else {
					logger.warn("找不到栏目#{}的价格配置", node.getNodeId());
				}
				node.setExtraValue("price", String.valueOf(cost));
			} else {
				cost = node.getFloatExtraValue("price");
			}

			if(cost > 0) {
				//检查用户是否已经购买
				if(frontUser != null) {

					UserRelation userRelation = getSubscribe(frontUser, node.getNodeId(), node.getNodeId());
					logger.debug("用户#{}对栏目#{}的订购关系是:{}", frontUser.getUuid(), node.getNodeId(), userRelation);
					if(userRelation != null) {
						node.setCurrentStatus(BasicStatus.normal.id);				
					} else {
						node.setCurrentStatus(BasicStatus.special.id);
					}
				} else {
					//需要购买
					node.setCurrentStatus(BasicStatus.special.id);
				}
			} else {
				node.setCurrentStatus(BasicStatus.normal.id);
			}

		}
	}

	/**
	 * 检查文档是直接看还是需要购买
	 * 
	 * 
	 * @author GHOST
	 * @date 2018-10-22
	 */
	private void checkBuyLink(Node node, Document document, User frontUser) {

		float cost = 0f;
		if(document.getExtraValue(DataName.price.name()) == null) {
			//没有设置价格，检查价格
			PriceCriteria priceCriteria = new PriceCriteria(document.getOwnerId());
			priceCriteria.setObjectType(ObjectType.document.name());
			priceCriteria.setObjectId(document.getUdid());
			priceCriteria.setCurrentStatus(BasicStatus.normal.id);
			List<Price> priceList = priceService.list(priceCriteria);
			Price price = null;
			if(priceList.size() > 0) {
				price = priceList.get(0);
				document.setExtraValue("price", String.valueOf(price.getCoin()));
				logger.info("为文档#{}设置价格为:{}", document.getUdid(), price.getCoin());
				cost = price.getCoin();
			} else {
				logger.warn("找不到文档#{}的价格配置", document.getUdid());
			}
			document.setExtraValue("price", String.valueOf(cost));
		} else {
			cost = document.getFloatExtraValue("price");
		}

		if(cost > 0) {
			//检查用户是否已经购买
			if(frontUser != null) {

				UserRelation userRelation = getSubscribe(frontUser, node.getNodeId(), document.getUdid());
				logger.debug("用户#{}对文档#{}的订购关系是:{}", frontUser.getUuid(), document.getUdid(), userRelation);
				if(userRelation != null) {
					document.setCurrentStatus(BasicStatus.normal.id);				
				} else {
					document.setCurrentStatus(BasicStatus.special.id);
				}
			} else {
				//需要购买
				document.setCurrentStatus(BasicStatus.special.id);
			}
		} else {
			document.setCurrentStatus(BasicStatus.normal.id);
		}

	}

	//确定一个用户是否有对应文档或栏目的订阅关系
	protected UserRelation getSubscribe(User frontUser, long nodeId, long documentId) {


		Assert.isTrue(frontUser.getUuid() > 0, "读取订阅关系的uuid不能为0");
		Assert.isTrue(nodeId > 0 && documentId > 0, "读取订阅关系的nodeId和documentId不能为0");


		//先查找缓存中是否有对栏目的完整权限
		String mapKey = frontUser.getUuid() + "#NODE#" + nodeId;

		UserRelation nodeRelation = centerDataService.getHmValue(SUBSCRIBE_CACHE_MAP_NAME, mapKey);
		if(nodeRelation != null) {
			logger.info("缓存信息，用户#{}有订阅栏目:{}:{}", frontUser.getUuid(), nodeId, nodeRelation);
			return nodeRelation;
		}
		//缓存中没有针对栏目的订阅数据，从数据库查找
		UserRelationCriteria userRelationCriteria = new UserRelationCriteria(frontUser.getOwnerId());
		userRelationCriteria.setUuid(frontUser.getUuid());
		userRelationCriteria.setObjectType(ObjectType.node.name());
		userRelationCriteria.setObjectId(nodeId);
		userRelationCriteria.setCurrentStatus(BasicStatus.normal.id);
		userRelationCriteria.setRelationType(UserRelationCriteria.RELATION_TYPE_SUBSCRIBE);
		List<UserRelation> userRelationList = userRelationService.list(userRelationCriteria);
		if(userRelationList.size() > 0) {
			nodeRelation = userRelationList.get(0);
			logger.info("数据库信息，用户#{}有订阅栏目:{}:{}", frontUser.getUuid(), nodeId, nodeRelation);
			try {
				centerDataService.setHmPlainValue(SUBSCRIBE_CACHE_MAP_NAME, mapKey, JsonUtils.toStringFull(nodeRelation), SUBSCRIBE_CACHE_SEC);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return nodeRelation;
		} 


		//检查是否有订阅单独的章节
		mapKey = frontUser.getUuid() + "#DOCUMENT#" + nodeId;

		UserRelation docuemntRelation = centerDataService.getHmValue(SUBSCRIBE_CACHE_MAP_NAME, mapKey);
		if(docuemntRelation != null) {
			logger.info("缓存信息，用户#{}有订阅文章:{}:{}", frontUser.getUuid(), documentId, docuemntRelation);
			return docuemntRelation;
		}
		userRelationCriteria = new UserRelationCriteria(frontUser.getOwnerId());
		userRelationCriteria.setUuid(frontUser.getUuid());
		userRelationCriteria.setObjectType(ObjectType.document.name());
		userRelationCriteria.setObjectId(documentId);
		userRelationCriteria.setCurrentStatus(BasicStatus.normal.id);
		userRelationList = userRelationService.list(userRelationCriteria);
		if(userRelationList.size() > 0) {
			docuemntRelation = userRelationList.get(0);
			logger.info("数据库信息，用户#{}有订阅文章:{}:{}", frontUser.getUuid(), nodeId, docuemntRelation);
			try {
				centerDataService.setHmPlainValue(SUBSCRIBE_CACHE_MAP_NAME, mapKey, JsonUtils.toStringFull(docuemntRelation), SUBSCRIBE_CACHE_SEC);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return docuemntRelation;
		} 
		logger.debug("用户#{}没有订阅栏目:{}，也没有订阅文章:{}", frontUser.getUuid(), nodeId, documentId);
		return null;

	}

	/**
	 * 获取一个栏目最后发表的文章
	 * 
	 * 
	 * @author GHOST
	 * @date 2018-10-22
	 */
	private void getLastestDoc(Node node) {
		long lastDocumentId = node.getLongExtraValue("lastUdid");
		boolean needFetch = false;
		if(lastDocumentId > 0) {
			String lastDocumentUrl = node.getExtraValue("lastDocumentUrl");
			if(lastDocumentUrl == null) {
				//重新获取
				needFetch = true;			
			}
		} else {
			needFetch = true;
		}
		logger.debug("是否需要为栏目:{}获取最后一篇文档:{}", node.getNodeId(), needFetch);
		if(needFetch) {
			DocumentCriteria documentCriteria = new DocumentCriteria(node.getOwnerId());
			documentCriteria.setNodePath(node.getPath());
			documentCriteria.setOrderBy(" publish_time DESC");
			Paging paging = new Paging(1);
			documentCriteria.setPaging(paging);
			List<Document> documentList = null;
			try {
				documentList = documentService.listOnPage(documentCriteria);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(documentList != null && documentList.size() > 0) {
				Document lastDocument = documentList.get(0);
				node.setExtraValue("lastUdid", String.valueOf(lastDocument.getUdid()));
				node.setExtraValue("lastDocumentTitle", lastDocument.getTitle());
				node.setExtraValue("lastDocumentUrl", lastDocument.getViewUrl());
				logger.debug("栏目:#{}最后一篇文章是:{}/{}/{}", node.getNodeId(), lastDocument.getUdid(), lastDocument.getTitle(), lastDocument.getViewUrl());
			}

		}

	}

	@Override
	public String detail(HttpServletRequest request,			HttpServletResponse response, ModelMap map
			) throws Exception {
		long ownerId =  (long)map.get("ownerId");

		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return CommonStandard.frontMessageView;		
		}
		Node node = (Node)map.get("node");

		if(node == null){
			return CommonStandard.frontMessageView;		
		}
		if(node.getNodeId() == nodeService.getDefaultNode(map.get("siteCode").toString(), ownerId).getNodeId()){
			return CommonStandard.frontMessageView;
		}

		if(node.getOwnerId() != ownerId){
			logger.error("尝试获取的栏目[" + node.getNodeId() + "]，其ownerId[" + node.getOwnerId() + "]与系统会话中的[" + ownerId + "]不一致");
			return CommonStandard.frontMessageView;
		}


		String documentCode = null;
		try{
			documentCode = map.get("documentCode").toString();
		}catch(Exception e){}

		User frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.id);




		logger.debug("用户:{}访问详情:{}", (frontUser == null ? "空" : frontUser.getUuid()), documentCode);
		Document document = null;
		Document doc = documentService.select(documentCode, ownerId);
		if(doc!=null){
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
				logger.debug("文档[" + document.getUdid() + "/" + document.getDocumentCode() + "]不应当在栏目[" + node.getNodeId() + "/" + node.getPath() + "]中显示");
				return CommonStandard.frontMessageView;		

			}
		}

		if(node.getNodeTypeId() == NodeType.business.id) {

			int privilege = stockService.checkPrivilege(document, frontUser);
			if(privilege != OperateResult.success.id) {
				//无权查看
				map.put("message", new EisMessage(privilege));
				//return;
			}

			//检查有没有图片数据
			map.put("downloadPrefix", FILE_OUTLINK_PATH + "/" + document.getMimeType() + "/");
			if(document.getExtraValue("fileList") == null) {
				logger.info("文档:{}还没有fileList，读取", document.getUdid());
				File file = new File(UPLOAD_PATH + "/content/" + document.getMimeType() + "/");
				int count = 0;
				if(file.exists()) {
					File[] files = file.listFiles();
					logger.debug("动漫详情:{}的目录:{}下文件数量是:{}", document.getUdid(), file.getAbsolutePath(), count);
					count = files.length;
					document.setExtraValue("picCount", String.valueOf(count));
					List<String> fileList = new ArrayList<String>();
					for(File subFile : files) {						
						fileList.add(subFile.getName());						
					}
					Collections.sort(fileList, new Comparator<String>() {
						@Override
						public int compare(String o1, String o2) {
							int n1 = NumericUtils.parseInt(o1.split("\\.")[0]);
							int n2 = NumericUtils.parseInt(o2.split("\\.")[0]);
							if(n1 > n2) {
								return 1;
							}
							return -1;
						}});
					logger.debug("动漫详情:{}的目录:{}文件列表是:{}", document.getUdid(), file.getAbsolutePath(), JsonUtils.toStringFull(fileList));
					map.put("fileList", fileList);
					//只有doc进行了缓存
					doc.setExtraValue("fileList", JsonUtils.toStringFull(fileList));

				} else {
					logger.debug("动漫详情:{}的目录:{}不存在", document.getUdid(), file.getAbsolutePath());

				}

			} else {
				@SuppressWarnings("unchecked")
				List<String> fileList = JsonUtils.getInstance().readValue(document.getExtraValue("fileList"), List.class);
				logger.info("文档:{}已存在fileList:{}", document.getUdid(), (fileList == null ? "空" : fileList.size()));
				map.put("fileList", fileList);

			}
		}


		//writeCommentCount(document, map, 1);
		writeNeighborDocument(document, node, map);
		writeRecommendList(request, response, frontUser,map);
		applyReadCount(frontUser, document, map);
		writeCommentList(document, map, request);

		if(frontUser != null){
			writeUserInfo(frontUser, map, request);
			writeFavoriteCount(document);
			writePraiseCount(document);
			writeMoney(frontUser,map);
			writeCartCount(frontUser,map);
		}


		map.put("pageTitle",document.getTitle() );
		map.put("pageDesc", document.getDesc(0));
		logger.debug("当前文档的udid为"+document.getUdid());
		map.put("document",document);
		map.put("node",node);
		map.put("pageType", "detail");//标识为详情页
		if(frontUser!=null){
			map.put("unreadMessage",unreadMessageCount(frontUser.getUuid()));
		}


		return getTemplateLocation(document, node, ownerId);

	}



	private int unreadMessageCount(long uuid){
		MessageCriteria mc = new MessageCriteria();
		mc.setReceiverId(uuid);
		mc.setCurrentStatus(MessageStatus.unread.id);
		return userMessageService.count(mc);
	}

	/**
	 * 更新用户阅读某篇动漫的记录
	 * 如果之前已经阅读过，更新最后时间
	 * 阅读针对的是栏目而非文章
	 * 
	 * 
	 * @author GHOST
	 * @date 2018-10-25
	 */
	@Override
	protected void applyReadCount(User frontUser, Document document, ModelMap map){
		UserRelationCriteria userRelationCriteria = new UserRelationCriteria(document.getOwnerId());
		userRelationCriteria.setObjectType(ObjectType.node.name());
		userRelationCriteria.setRelationType(UserRelationCriteria.RELATION_TYPE_READ);
		userRelationCriteria.setObjectId(document.getUdid());
		int readCount = userRelationService.getRelationCount(userRelationCriteria);
		if(readCount < MIN_READ_COUNT){
			readCount += MIN_READ_COUNT;
		}
		if(frontUser != null) {
			userRelationCriteria = new UserRelationCriteria(document.getOwnerId());
			userRelationCriteria.setUuid(frontUser.getUuid());
			userRelationCriteria.setObjectType(ObjectType.node.name());
			userRelationCriteria.setObjectId(document.getDefaultNode().getNodeId());
			userRelationCriteria.setRelationType(UserRelationCriteria.RELATION_TYPE_READ);

			List<UserRelation> userRelationList = userRelationService.list(userRelationCriteria);
			if(userRelationList.size() < 1) {
				//新增一个记录
				UserRelation userRelation = new UserRelation(frontUser.getOwnerId());
				userRelation.setUuid(frontUser.getUuid());
				userRelation.setRelationLimit(UserRelationCriteria.RELATION_LIMIT_UNIQUE);
				userRelation.setObjectType(ObjectType.node.name());
				userRelation.setRelationType(UserRelationCriteria.RELATION_TYPE_READ);
				userRelation.setObjectId(document.getDefaultNode().getNodeId());
				userRelation.setCreateTime(new Date());
				userRelation.setCurrentStatus(BasicStatus.normal.id);
				userRelationService.insertAsync(userRelation);
				readCount++;
				logger.debug("用户#{}之前没有阅读过栏目:{},新增，该栏目总阅读数增加为:{}", frontUser.getUuid(), userRelation.getObjectId(), readCount);
			} else {
				//之前有阅读过
				UserRelation userRelation = userRelationList.get(0);
				if(userRelation.getLastUse() != null && new Date().getTime() - userRelation.getLastUse().getTime() < SYNC_SEC * 1000) {
					//时间太短就不刷新了
					logger.debug("用户#{}之前阅读过栏目:{},但上次更新时间太短，忽略更新最后阅读时间", frontUser.getUuid(), userRelation.getObjectId());
			} else {
				logger.debug("用户#{}之前阅读过栏目:{},上次更新时间是{}，更新最后阅读时间", frontUser.getUuid(), userRelation.getObjectId(), StringTools.getFormattedTime(userRelation.getLastUse()));
					userRelation.setLastUse(new Date());
					userRelationService.update(userRelation);
			}

			}
		}

		map.put("readCount", readCount);



	}


}



