package com.maicard.wpt.np;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;

import com.maicard.common.base.BaseController;
import com.maicard.common.criteria.CommentCriteria;
import com.maicard.common.domain.Comment;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.CacheService;
import com.maicard.common.service.CommentService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.HttpsService;
import com.maicard.common.util.ContextTypeUtil;
import com.maicard.common.util.Paging;
import com.maicard.common.util.PagingUtils;
import com.maicard.common.util.PreviewToken;
import com.maicard.ec.criteria.AddressBookCriteria;
import com.maicard.ec.domain.AddressBook;
import com.maicard.ec.service.AddressBookService;
import com.maicard.ec.service.DeliveryOrderService;
import com.maicard.exception.EisException;
import com.maicard.mb.service.UserMessageService;
import com.maicard.money.domain.Money;
import com.maicard.money.service.MoneyService;
import com.maicard.money.service.PayService;
import com.maicard.money.service.PointExchangeService;
import com.maicard.money.service.PriceService;
import com.maicard.product.criteria.CartCriteria;
import com.maicard.product.domain.Cart;
import com.maicard.product.domain.Product;
import com.maicard.product.service.CartService;
import com.maicard.product.service.ItemService;
import com.maicard.product.service.ProductService;
import com.maicard.security.criteria.UserRelationCriteria;
import com.maicard.security.domain.User;
import com.maicard.security.domain.UserRelation;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.FrontUserService;
import com.maicard.security.service.UserRelationService;
import com.maicard.site.criteria.DocumentCriteria;
import com.maicard.site.criteria.NodeCriteria;
import com.maicard.site.criteria.TagCriteria;
import com.maicard.site.criteria.TagObjectRelationCriteria;
import com.maicard.site.domain.Document;
import com.maicard.site.domain.IncludeNodeConfig;
import com.maicard.site.domain.Node;
import com.maicard.site.domain.Tag;
import com.maicard.site.domain.Template;
import com.maicard.site.service.DocumentNodeRelationService;
import com.maicard.site.service.DocumentService;
import com.maicard.site.service.NodeProcessor;
import com.maicard.site.service.NodeService;
import com.maicard.site.service.TagObjectRelationService;
import com.maicard.site.service.TagService;
import com.maicard.site.service.TemplateService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.ObjectType;
import com.maicard.standard.PriceType;
import com.maicard.standard.SecurityStandard.UserTypes;
import com.maicard.standard.SiteStandard;
import com.maicard.standard.SiteStandard.DocumentStatus;
import com.maicard.standard.SiteStandard.NodeType;
import com.maicard.standard.TransactionStandard.TransactionStatus;
import com.maicard.wpt.service.WeixinMsgService;



/**
 * 通用栏目处理器，有一些标准处理逻辑可重用<br>
 * 一般是其他栏目处理器的父类
 * 
 * 
 * @author NetSnake
 * @date 2015-09-09
 */
@Service
public class WptDefaultNodeProcessor  extends BaseController  implements NodeProcessor{


	@Autowired(required=false)
	protected AddressBookService addressBookService;
	@Autowired(required=false)
	protected DeliveryOrderService deliveryOrderService;
	@Resource
	protected CartService cartService;
	@Resource
	protected CacheService cacheService;
	@Resource
	protected CommentService commentService;
	@Resource
	protected MoneyService moneyService;
	@Resource
	protected ProductService productService;
	@Resource
	protected PriceService priceService;
	@Resource
	protected CertifyService certifyService;
	@Resource
	protected ConfigService configService;
	@Resource
	protected NodeService nodeService;	
	@Resource
	protected DocumentService documentService;
	@Resource
	protected PointExchangeService pointExchangeService;
	@Resource
	protected TagService tagService;
	@Resource
	protected TagObjectRelationService tagObjectRelationService;
	@Resource
	protected TemplateService templateService;
	@Resource
	protected UserRelationService userRelationService;
	@Resource 
	protected HttpsService httpservice;
	@Resource 
	protected WeixinMsgService weixinMsgService;
	@Resource
	protected ItemService itemService;
	@Resource
	protected PayService payService;
	@Resource
	protected FrontUserService frontUserService;
	@Resource
	protected DocumentNodeRelationService documentNodeRelationService;
	@Resource
	protected UserMessageService userMessageService;

	protected int ROWS_PER_PAGE = 10;

	protected SimpleDateFormat sdf = new SimpleDateFormat(CommonStandard.defaultDateFormat);
	protected final int sessionTimeout = CommonStandard.COOKIE_MAX_TTL;

	@PostConstruct
	public void init() {
		ROWS_PER_PAGE = configService.getIntValue(DataName.frontRowsPerPage.toString(), 0);
		if (ROWS_PER_PAGE < 1) {
			ROWS_PER_PAGE = CommonStandard.DEFAULT_FRONT_ROWS_PER_PAGE;
		}

	}

	/**
	 * index处理模式：
	 * 为二级首页模式：
	 * 1、获取本节点下级所有子节点
	 * 2、获取下级所有子节点的文章列表
	 * 3、获取发布到本节点的所有文章
	 */

	@Override
	public String index(HttpServletRequest request,
			HttpServletResponse response, ModelMap map) throws Exception {
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
		Node node =  null;
		try{
			node = (Node)map.get("node");
		}catch(Exception e){
			e.printStackTrace();
		}
		if(node == null){
			logger.error("在MAP中找不到节点对象");
			map.put("message", new EisMessage(EisError.nodeNotExist.getId(),"系统异常","请尝试访问其他页面或返回首页"));
			return CommonStandard.frontMessageView;		
		}
		/*
		String siteCode = null;
		if(map.get("siteCode") == null){
			map.put("message", new EisMessage(EisError.systemDataError.getId(),"系统异常","请尝试访问其他页面或返回首页"));
			logger.error("MAP中没有siteCode数据");
		}
		siteCode = map.get("siteCode").toString();

		if(node.getNodeId() == nodeService.getDefaultNode(siteCode, ownerId).getNodeId()){
			logger.error("当前访问的节点[" + node.getNodeId() + "]是系统默认节点，异常访问");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),"系统异常","请尝试访问其他页面或返回首页"));
			return CommonStandard.frontMessageView;
		}*/
		if(node.getOwnerId() != ownerId){
			logger.error("尝试获取的节点[" + node.getNodeId() + "]，其ownerId[" + node.getOwnerId() + "]与系统会话中的[" + ownerId + "]不一致");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(),"系统异常","请尝试访问其他页面或返回首页"));
			return CommonStandard.frontMessageView;
		}
		if(StringUtils.isNotBlank(node.getRedirectTo())){
			logger.debug("节点将重定向到:" + node.getRedirectTo());
			response.setStatus(HttpStatus.TEMPORARY_REDIRECT.value());
			response.sendRedirect(node.getRedirectTo());		
			return null;
		}


		String defaultTemplateLocation =null;
		try{
			defaultTemplateLocation = node.getTemplateLocation();
		}catch(NullPointerException e){
		}
		if(defaultTemplateLocation == null){
			logger.error("节点[" + node.getName() + "/" + node.getNodeId() + "]未定义默认模版");
			return CommonStandard.frontMessageView;

		}	
		defaultTemplateLocation = defaultTemplateLocation.replaceAll("\\.\\w+$", "");


		User frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());


		if(frontUser != null){
			writeUserInfo(frontUser, map, request);
			writeBrowseHistory(frontUser, map);
		}

		int rows = ServletRequestUtils.getIntParameter(request, "rows", ROWS_PER_PAGE);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);


		String search = ServletRequestUtils.getStringParameter(request, "search");
		String title = ServletRequestUtils.getStringParameter(request, "title");
		logger.debug("是否是wptDefaultProcessor搜索：search="+search+"#title="+title);
		DocumentCriteria documentCriteria = new DocumentCriteria();
		documentCriteria.setCurrentStatus(DocumentStatus.published.getId());
		if(search!=null && "search".equals(search)){
			documentCriteria.setTitle(title);
		}else{
			documentCriteria.setNodePath(node.getPath());
		}
		documentCriteria.setOwnerId(ownerId);
		documentCriteria.setDocumentTypeId(171004);
		Paging paging = new Paging(rows);
		paging.setCurrentPage(page);
		documentCriteria.setPaging(paging);
		String[] tags = ServletRequestUtils.getStringParameters(request,"tags");
		String tagsName = ServletRequestUtils.getStringParameter(request,"tags", null);
		String sortName = ServletRequestUtils.getStringParameter(request, "sortName");
		String sortType = ServletRequestUtils.getStringParameter(request, "sortType");
		if(sortName!=null && sortType!=null){
			documentCriteria.setOrderBy(" "+sortName+" "+sortType+" ");
			map.put("sortName", sortName);
			map.put("sortType", sortType);
		}
		if(tags != null && tags.length > 0){
			documentCriteria.setTags(tags);

			List<Long> tagIds = new ArrayList<Long>();
			for(String tag : tags){
				TagCriteria tagCriteria = new TagCriteria(ownerId);
				tagCriteria.setTagName(tag);
				Tag t = tagService.select(tag, ownerId);
				if(t != null){
					tagIds.add(t.getTagId());
				}
			}
			if(tagIds.size() > 0){
				Set<Tag> relationTagSet = new LinkedHashSet<Tag>();

				TagObjectRelationCriteria tagObjectRelationCriteria = new TagObjectRelationCriteria(ownerId);
				tagObjectRelationCriteria.setObjectType(ObjectType.tag.name());
				for(Long objectId : tagIds){
					tagObjectRelationCriteria.setObjectId(objectId);
					List<Tag> tagList = tagObjectRelationService.listTags(tagObjectRelationCriteria);
					logger.debug("根据当前标签[" + objectId + "]作为objectId查询到的关联标签数量是:" + (tagList == null ? "空" : tagList.size()));
					if(tagList != null && tagList.size() > 0){
						relationTagSet.addAll(tagList);
					}
				}


				logger.debug("根据当前标签[" + tagsName + "]查询到的关联标签总数量是:" + (relationTagSet == null ? "空" : relationTagSet.size()));
				map.put("relationTagSet", relationTagSet);
			} else {
				logger.debug("根据当前标签[" + tagsName + "]找不到任何已存在的标签，因此也无法查询关联标签");

			}
		}
		List<Document> documentList = documentService.listOnPage(documentCriteria);
		map.put("paging", paging);
		String jump = ServletRequestUtils.getStringParameter(request, "jump",null);
		logger.debug("当前跳转参数是:" + jump);
		if(documentList.size() > 0 && StringUtils.isNotBlank(jump)){
			//检查是否需要跳转到某个类型的文档
			String[] order = jump.split(",");
			Document[] jumpDocuments = new Document[order.length];
			int i = 0;
			for(String type : order){	
				for(Document document : documentList){
					if(document.getDocumentTypeCode().equalsIgnoreCase(type)){
						logger.debug("找到了第[" + i + "]个需要跳转到指定类型文档:" +  document.getUdid());
						jumpDocuments[i] = document;
					}
				}
				i++;
			}
			for(int j = 0; j < jumpDocuments.length; j++){
				logger.debug("跳转文档顺序[" + j + "=>" + jumpDocuments[j]);
				if(jumpDocuments[j] != null){
					logger.debug("最终确定跳转到文档:" +  jumpDocuments[j].getUdid());
					response.setStatus(HttpStatus.TEMPORARY_REDIRECT.value());
					response.sendRedirect(jumpDocuments[j].getViewUrl());
					return null;
				}
			}

		}
		String transactionToken = ServletRequestUtils.getStringParameter(request, "tt");
		if(StringUtils.isNotBlank(transactionToken)){
			map.put("tt",transactionToken);
		}
		List<Document> documentList2 = new ArrayList<Document>();
		logger.info("发布到本节点的所有文章数:" + (documentList == null? "空":documentList.size()));
		List<Float> newsListPrice = new ArrayList<Float>();
		if(documentList.size() > 0){
			for(Document d1 : documentList){
				Document document = d1.clone();
				writeRelationCount(document);
				//fetchProduct(document, frontUser, ownerId, transactionToken);
				//logger.debug("文章权重 ：" + document.getDisplayIndex());
				documentList2.add(document);
				logger.debug("wptDefaultProcessor下面的document"+document.getUdid());
			}
			map.put("newsList",documentList2);
			map.put("newsListPrice", newsListPrice);
		} 
		if(node.getIncludeNodeSet() != null && node.getIncludeNodeSet().size() > 0){
			logger.debug("本栏目[" + node.getNodeId() + "]自动包含" + node.getIncludeNodeSet().size() + "个子栏目");
			Map<String,String> subNodeLink = new HashMap<String,String>();
			for(IncludeNodeConfig includeNodeConfig : node.getIncludeNodeSet()){
				if(includeNodeConfig.getContextType() != null && !ContextTypeUtil.isMatchedContextType(request,includeNodeConfig.getContextType())){
					logger.info("栏目设置的自动包含子栏目，其应用场景设置为:" + includeNodeConfig.getContextType() + ",与当前应用场景不一致");
					continue;
				}
				Node n = nodeService.select(includeNodeConfig.getNodeId());
				if(n == null){
					logger.error("找不到需要放入栏目[" + node.getNodeId() + "]的子栏目:" + includeNodeConfig.getNodeId());
					continue;
				}
				subNodeLink.put(n.getName(), n.getViewUrl());
				if(includeNodeConfig.getRows() < 1){
					//该自动包含子栏目获取文章数是0，不查找对应文章，仅把该栏目的名称和链接地址写入
					continue;
				}
				documentCriteria.setNodePath(n.getPath());	
				if(includeNodeConfig.getDocumentTypeCode() != null){
					if(includeNodeConfig.getDocumentTypeCode().startsWith("!")){
						documentCriteria.setNoDocumentTypeCode(includeNodeConfig.getDocumentTypeCode().replaceFirst("!", ""));
					} else {
						documentCriteria.setDocumentTypeCode(includeNodeConfig.getDocumentTypeCode());
					}	
				}
				Paging paging2 = new Paging(includeNodeConfig.getRows());
				paging2.setCurrentPage(1);
				documentCriteria.setPaging(paging2);

				List<Document> documentList3 = documentService.listOnPage(documentCriteria);
				logger.debug("自动放入栏目[" + node.getNodeId() + "/" + node.getName() + "]的子栏目[" + n.getNodeId() + "/" + n.getName() + "]文章数量是:" + (documentList3 == null ? "空" : documentList3.size()));
				if(documentList3 != null && documentList3.size() > 0){
					List<Document> documentList4 = new ArrayList<Document>();
					for(Document d2 : documentList3){
						writeRelationCount(d2);
						//fetchProduct(d2, frontUser, ownerId, null);

						/*String productCode = d2.getExtraValue(DataName.productCode.toString());
						boolean isActivity = false;
						if(d2.getDocumentTypeCode() != null && d2.getDocumentTypeCode().equals("activity")){
							isActivity =true;
						}
						if(StringUtils.isNotBlank(productCode) || isActivity){
							//该文档是一个产品文档，应用产品数据
							Product product = productService.select(productCode, ownerId);
							if(product == null){
								logger.warn("找不到文档[" + d2.getUdid() + "]对应的产品:" + productCode);
							} else {
								productService.generateProductDocumentData(product, d2);
								boolean priceDataResult = false;
								if(isActivity){
									priceDataResult = priceService.generatePriceDocumentData(product, d2, PriceType.PRICE_PROMOTION.toString());
									logger.debug("根据产品[" + product.getProductId() + "/" + product.getProductCode() + "]为文档生成活动价格结果:" + priceDataResult);

								} else {
									priceDataResult = priceService.generatePriceDocumentData(product, d2, PriceType.PRICE_STANDARD.toString());
									logger.debug("根据产品[" + product.getProductId() + "/" + product.getProductCode() + "]为文档生成标准价格结果:" + priceDataResult);
								}

							}
						}*/
						documentList4.add(d2);
					}
					String listName = n.getAlias() + "List";
					logger.debug("放入子栏目[" + node.getName() + "/" + n.getAlias() + "]的文章到栏目[" + node.getAlias() + "],列表名是:" + listName);
					map.put(listName, documentList4);
				}

			}
			map.put("subNodeLink", subNodeLink);
		}
		map.put("node",node);

		boolean needPathNodeList = configService.getBooleanValue(DataName.needPathNodeList.toString(),ownerId);
		if(needPathNodeList){
			List<Node> pathNodeList = nodeService.getNodePath(node.getNodeId(), ownerId);
			if(pathNodeList != null && pathNodeList.size() > 0){
				map.put("pathNodeList", pathNodeList);
			}
		}
		writeSideBar(request,response,map,ownerId);
		map.put("pageTitle",node.getName());
		Node navigationNode = nodeService.getDefaultNode(map.get("siteCode").toString(), ownerId).clone();
		navigationNode.setNodeTypeId(SiteStandard.NodeType.business.id);
		writeNavigation(navigationNode,map);
		return defaultTemplateLocation;		
	}


	protected void writeRelationCount(Document document) {
		document.setId(document.getUdid());
		userRelationService.setDynamicData(document);		
	}

	protected void writeNavigation(Node rootNode, ModelMap map){
		List<Node> navigationList = nodeService.generateNavigation(rootNode);
		logger.info("使用根栏目:" + rootNode + ",生成导航:" + navigationList.size() + "个");
		map.put("navigation", navigationList);
	}
	/**
	 * 侧边栏数据和底栏数据
	 * @throws Exception 
	 */
	protected void writeSideBar(HttpServletRequest request,
			HttpServletResponse response,ModelMap map,long ownerId) throws Exception{
		logger.debug("进入侧边栏数据查询");
		/**
		 * ************************用一个通用的方法把首页可能需要提供数据一次性提供*****************************
		 * 也就是把每个业务节点下面的数据各自取一部分出来展示
		 */
		NodeCriteria nodeCri = new NodeCriteria();
		nodeCri.setOwnerId(ownerId);
		nodeCri.setNodeTypeId(NodeType.business.getId());
		List<Node> nodeList = nodeService.list(nodeCri);
		logger.debug("需要显示数据的栏目共有"+(nodeList==null?0:nodeList.size())+"条");
		for(Node nod : nodeList){
			int pageNum = 1;
			List<Map<String,Object>> listMAP = new ArrayList<Map<String,Object>>();
			List<Document> docList = new ArrayList<Document>();
			Map<String,Object> hasMap = new HashMap<String,Object>();
			Paging paging = new Paging(ROWS_PER_PAGE);
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
						//						writeCommentCount(doc);
						//						writeReadCount(doc);
						try {
							hasMap.put("userName", frontUserService.select(doc.getPublisherId()).getUsername());
							hasMap.put("userHeadPic", frontUserService.select(doc.getPublisherId()).getExtraValue("userHeadPic"));
						} catch (Exception e) {
							logger.error("找不到发布人的信息");
						}
						hasMap.put("buy_money", product.getBuyMoney());
						hasMap.put("vipFree", product.getExtraValue("vipFree"));
						listMAP.add(hasMap);
					}
				}
			}
			map.put(nod.getAlias(), listMAP);
			logger.debug("节点"+nod.getAlias()+"的listMAP"+listMAP);
		}
	}
	
	protected void writeUserInfo(User frontUser, ModelMap map, HttpServletRequest request) {

		/*	String key = ""
		cacheService.put(CommonStandard.cacheNameUser, key, value);
		 */
		map.put("frontUser", frontUser);

		Cart cart = cartService.getCurrentCart(frontUser.getUuid(), PriceType.PRICE_STANDARD.toString(), CartCriteria.ORDER_TYPE_TEMP, null, frontUser.getOwnerId(), false);
		if(cart != null){
			map.put("cartCount", cart.getTotalGoods());
		} else {
			map.put("cartCount", 0);
		}

		/*CartCriteria cartCriteria = new CartCriteria(frontUser.getOwnerId());
		cartCriteria.setUuid(frontUser.getUuid());
		cartCriteria.setCurrentStatus(TransactionStatus.inCart.getId());
		cartCriteria.setOrderType(CartCriteria.ORDER_TYPE_TEMP);
		int cartCount = cartService.count(cartCriteria);
		map.put("cartCount", cartCount);*/
		//获取用户资金
		Money money = moneyService.select(frontUser.getUuid(), frontUser.getOwnerId());
		logger.debug("money是否为空 ：　" + (money == null ? "空" : money));
		if(money != null){
			map.put("money", money);
		}

		//获取用户地址本
		if(addressBookService != null){
			AddressBookCriteria addressBookCriteria = new AddressBookCriteria(frontUser.getOwnerId());
			addressBookCriteria.setUuid(frontUser.getUuid());
			addressBookCriteria.setCurrentStatus(BasicStatus.relation.getId());

			AddressBook addressBook = null;
			List<AddressBook> addressBookList = addressBookService.listOnPage(addressBookCriteria);
			if(addressBookList == null || addressBookList.size() < 1){
				logger.debug("未找到用户[" + frontUser.getUuid() + "]的默认地址本，查找其他地址本");
				addressBookCriteria.setCurrentStatus(BasicStatus.normal.getId());
				addressBookList = addressBookService.listOnPage(addressBookCriteria);
				if(addressBookList != null && addressBookList.size() > 0){
					addressBook = addressBookList.get(0);
				} else {
					logger.debug("没有找到用户正常状态的地址本");
				}
			} else {
				addressBook = addressBookList.get(0);
			}
			if(addressBook != null){
				map.put("addressBook", addressBook);
			}
		}
		//获取我的评论
		CommentCriteria commentCriteria = new CommentCriteria(frontUser.getOwnerId());
		commentCriteria.setCurrentStatus(CommentCriteria.STATUS_PUBLISHED);
		commentCriteria.setUuid(frontUser.getUuid());
		List<Comment> commentList = commentService.listOnPage(commentCriteria);
		logger.debug("我的评论数是:" + (commentList == null ? "空" : commentList.size()));
		if(commentList == null || commentList.size() < 1){
		} 	else {
			map.put("comment", commentList);
		}


		int favPage = ServletRequestUtils.getIntParameter(request, "favPage", 0);
		int favRows = ServletRequestUtils.getIntParameter(request, "favRows", 0);
		if(favPage > 0){



			UserRelationCriteria userRelationCriteria = new UserRelationCriteria(frontUser.getOwnerId());
			userRelationCriteria.setUuid(frontUser.getUuid());
			userRelationCriteria.setRelationType(UserRelationCriteria.RELATION_TYPE_FAVORITE);

			int totalRows = userRelationService.count(userRelationCriteria);

			map.put("favPaging", PagingUtils.generateContentPaging(totalRows, favRows, favPage));

			if(totalRows < 1){
				logger.debug("当前返回的收藏数据数量是0");
				return;
			}

			//获取我的收藏
			Paging paging = new Paging(favRows);
			paging.setCurrentPage(favPage);			
			userRelationCriteria.setPaging(paging);

			List<UserRelation> favoriteList = userRelationService.listOnPage(userRelationCriteria);
			map.put("favoriteList", favoriteList);
		}



	}





	@Override
	public String detail(HttpServletRequest request, HttpServletResponse response, ModelMap map	) throws Exception {
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

		User frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());

		//int page = ServletRequestUtils.getIntParameter(request, "page", 1);

		String transactionToken = ServletRequestUtils.getStringParameter(request, "tt");
		if(StringUtils.isNotBlank(transactionToken)){
			map.put("tt",transactionToken);
		}

		Document d1 = documentService.select(documentCode, ownerId);
		if(d1 == null){
			logger.error("根据文档代码[" + d1 + "]找不到文档");
			return CommonStandard.frontMessageView;		
		}
		if(d1.getOwnerId() != ownerId){
			logger.error("尝试获取的文档[" + d1.getUdid() + "]，其ownerId[" + d1.getOwnerId() + "]与系统会话中的[" + ownerId + "]不一致");
			return CommonStandard.frontMessageView;
		}


		Document document = d1.clone();



		//fetchProduct(document, frontUser, ownerId, transactionToken);

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


		String productSmallImage = document.getExtraValue(DataName.productSmallImage.toString());
		if (productSmallImage != null){
			//			if(!productSmallImage.startsWith("http://")){
			//				document.getDocumentDataMap().get(DataName.productSmallImage.toString()).setDataValue("http://"+request.getServerName()+ document.getDocumentDataMap().get("productSmallImage").getDataValue());
			//			}
			document.setExtraValue(DataName.productSmallImage.toString(),productSmallImage);
		} else{
			document.setExtraValue(DataName.productSmallImage.toString(), "http://"+request.getServerName()+"/style/mobile/images/logo.png");
		}

		//writeCommentCount(document, map, 1);
		writeRelatedDocument(document, map);
		writeNeighborDocument(document, node, map);

		applyReadCount(frontUser, document, map);
		writeCommentList(document, map, request);
		writeSideBar(request,response,map,ownerId);
		if(frontUser != null){
			writeBrowseHistory(frontUser, map);
			//			writePurchaseQuery(request,response,frontUser, map);
			writeUserInfo(frontUser, map, request);
			writeMoney(frontUser,map);
			writeCartCount(frontUser,map);
			writeFavoriteCount(document, map);
			writePraiseCount(document);
			//			writeMyGroom(frontUser, map);
		}

		/*map.put("host", "");
		String ua =request.getHeader("user-agent").toLowerCase();
		if (ua.indexOf("micromessenger") > 0) {// 是微信浏览器
			map.put("host","http://" + request.getServerName() + "/wexin/accredit?url="+request.getRequestURL().toString());
		}*/
		logger.debug("当前文档的udid为"+document.getUdid());
		map.put("document",document);
		map.put("node",node);
		map.put("pageType", "detail");//标识为详情页
		//map.put("logo", logo);
		String url=request.getRequestURL().toString();
		if (request.getQueryString()!=null){
			url=url+"?"+request.getQueryString();
		}


		Node navigationNode = nodeService.getDefaultNode(map.get("siteCode").toString(), ownerId).clone();
		navigationNode.setNodeTypeId(SiteStandard.NodeType.business.id);
		writeNavigation(navigationNode,map);
		return getTemplateLocation(document, node, ownerId);

	}



	protected String getTemplateLocation(Document document, Node node, long ownerId) throws Exception{


		String templateLocation =null;
		if(document.getTemplateId() > 0){
			//尝试获取文档自定义模版
			Template template = templateService.select(document.getTemplateId());
			if(template != null && template.getOwnerId() != ownerId){
				logger.error("尝试获取的模版[" + template.getTemplateId() + "]，其ownerId[" + document.getOwnerId() + "]与系统会话中的[" + ownerId + "]不一致");
				throw new EisException ("系统模板异常:" + EisError.ownerNotMatch.id);
			}
			if(template != null && template.getCurrentStatus() == BasicStatus.normal.getId()){
				templateLocation = template.getTemplateLocation();
			}
		}
		if(templateLocation == null){
			logger.warn("文档:{}未定义模版或模版异常,尝试使用其默认节点:{}的模版", document.getUdid(), document.getDefaultNode());
			try{
				templateLocation = document.getDefaultNode().getTemplateLocation();
			}catch(NullPointerException e){
			}
			if(templateLocation == null && node != null){
				templateLocation = node.getTemplateLocation();
				logger.warn("文档:{}未定义模版或模版异常,尝试使用传入节点:{}的模版", document.getUdid(), node.getNodeId());
			}
			if(templateLocation == null){
				logger.error("找不到文档:{}或栏目:{}对应的模板", document.getUdid(), node.getNodeId());
				return "";

			}
			templateLocation = templateLocation.replaceAll("\\.\\w+$", "").replaceAll("jsp$", "");
			templateLocation = templateLocation.replaceAll("index", "detail");
			templateLocation = templateLocation.replaceAll("list", "detail");
		}
		if(templateLocation == null){
			logger.error("找不到文档或节点[" + node.getNodeId() + "]的默认模版");
			return "";

		}
		if(logger.isDebugEnabled())logger.debug("文档[" + document.getUdid() + "]使用模版:" + templateLocation);
		return templateLocation;
	}

	/**
	 * 向map中写入文档的点赞数量
	 * @param document
	 * @param map
	 */
	protected void writePraiseCount(Document document){
		//获取点赞数和收藏数
		UserRelationCriteria userRelationCriteria = new UserRelationCriteria(document.getOwnerId());
		userRelationCriteria.setObjectType(ObjectType.document.name());
		userRelationCriteria.setObjectId(document.getUdid());
		userRelationCriteria.setCurrentStatus(BasicStatus.normal.getId());
		userRelationCriteria.setRelationType(UserRelationCriteria.RELATION_TYPE_PRAISE);
		int praiseCount = userRelationService.count(userRelationCriteria);
		logger.debug("针对文章[" + document.getUdid() + "]的点赞数是:" + praiseCount);
		document.setExtraValue("praiseCount", String.valueOf(praiseCount));
	}


	/**
	 * 向map中写入文档的收藏数量
	 * @param document
	 * @param map
	 */
	protected void writeFavoriteCount(Document document, ModelMap map){
		//获取点赞数和收藏数
		UserRelationCriteria userRelationCriteria = new UserRelationCriteria(document.getOwnerId());
		userRelationCriteria.setObjectType(ObjectType.document.name());
		userRelationCriteria.setObjectId(document.getUdid());
		userRelationCriteria.setCurrentStatus(BasicStatus.normal.getId());
		userRelationCriteria.setRelationType(UserRelationCriteria.RELATION_TYPE_FAVORITE);
		int favoriteCount = userRelationService.count(userRelationCriteria);
		logger.debug("针对文章[" + document.getUdid() + "]的收藏数是:" + favoriteCount);
		map.put("favoriteCount",favoriteCount);		
	}
	/**
	 * 向document中写入文档的收藏数量
	 * @param document
	 * @param map
	 */
	protected void writeFavoriteCount(Document document){
		//获取点赞数和收藏数
		UserRelationCriteria userRelationCriteria = new UserRelationCriteria(document.getOwnerId());
		userRelationCriteria.setObjectType(ObjectType.document.name());
		userRelationCriteria.setObjectId(document.getUdid());
		userRelationCriteria.setCurrentStatus(BasicStatus.normal.getId());
		userRelationCriteria.setRelationType(UserRelationCriteria.RELATION_TYPE_FAVORITE);
		int favoriteCount = userRelationService.count(userRelationCriteria);
		logger.debug("针对文章[" + document.getUdid() + "]的收藏数是:" + favoriteCount);
		//		map.put("favoriteCount",favoriteCount);	
		document.setExtraValue("favoriteCount", String.valueOf(favoriteCount));
	}

	/**
	 * 向map中写入用户的收藏文章列表
	 * @param document
	 * @param map
	 */


	protected void writeCartCount(User frontUser, ModelMap map){
		if(frontUser == null){
			return;
		}
		map.put("cartCount", cartService.count(frontUser.getUuid(), TransactionStatus.inCart.getId()));

	}

	protected void writeMoney(User frontUser, ModelMap map){
		if(frontUser == null){
			return;
		}
		Money money = moneyService.select(frontUser.getUuid(),frontUser.getOwnerId());
		if(money != null){
			map.put("money", money);
		}
	}

	/*protected void writeCommentCount(Document document, ModelMap map, int detail) {
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
		map.put("praiseCount",commentCount);		
		document.setExtraValue("commentCount", String.valueOf(commentCount));
	}

	protected void writeCommentCount(Document document) {
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
	}*/

	/**
	 * 向map中写入与文档有关系的相关文档，根据标签检索
	 * @param document
	 * @param node
	 * @param map
	 * @throws Exception
	 */
	protected void writeRelatedDocument(Document document, ModelMap map) throws Exception{

		boolean noFetchRelatedDocument = configService.getBooleanValue(DataName.noFetchRelatedDocument.toString(), document.getOwnerId());
		if(noFetchRelatedDocument){
			logger.debug("当前系统配置为不获取相关文章");
		} else {
			logger.debug("当前文章关联文章类型是:" + document.getDocumentTypeId() + ",相关标签是:" + document.getTags());
			if(StringUtils.isNotBlank(document.getTags())){
				//获取相关联的产品
				DocumentCriteria documentCriteria = new DocumentCriteria(document.getOwnerId());
				String[] tags =  document.getTags().split(",");
				documentCriteria.setTags(tags);
				documentCriteria.setDocumentTypeId(document.getDocumentTypeId());
				Paging paging = new Paging(CommonStandard.DEFAULT_FRONT_ROWS_PER_PAGE);
				paging.setCurrentPage(1);
				documentCriteria.setPaging(paging);
				List<Document> relatedDocumentList = null;
				try {
					relatedDocumentList = documentService.listOnPage(documentCriteria);
				} catch (Exception e) {
					e.printStackTrace();
				}

				List<Document> relatedDocumentList2 = new ArrayList<Document>();
				if(relatedDocumentList != null && relatedDocumentList.size() > 0){
					for(Document d : relatedDocumentList){
						if(d.getUdid() == document.getUdid()){
							continue;
						}
						relatedDocumentList2.add(d);
					}
				}
				logger.debug("与文档[" + document.getUdid() + "]标签[" + document.getTags() + "]匹配并且类型一致的相关文档数量是:" + relatedDocumentList2.size());

				map.put("relatedDocumentList", relatedDocumentList2);
			}
		}

	}

	/**
	 * 向map中写入上一篇和下一篇文章
	 * @param document
	 * @param node
	 * @param map
	 * @throws Exception
	 */
	protected void writeNeighborDocument(Document document, Node node, ModelMap map) throws Exception{


		boolean noFetchLastAndNextDocument = configService.getBooleanValue(DataName.noFetchLastAndNextDocument.toString(), document.getOwnerId());
		if(noFetchLastAndNextDocument){
			logger.debug("当前系统配置为不获取前后一篇文章");
		} else {
			//获取上一篇文章和下一篇文章
			DocumentCriteria documentCriteria = new DocumentCriteria();
			documentCriteria.setNodePath(node.getPath());	
			documentCriteria.setCurrentStatus(DocumentStatus.published.getId());
			documentCriteria.setOwnerId(document.getOwnerId());
			documentCriteria.setMaxUdid(document.getUdid()-1);	//UDID不能超过本文档的UDID，即上一篇
			Paging paging = new Paging(1);
			documentCriteria.setPaging(paging);
			documentCriteria.getPaging().setCurrentPage(1);
			List<Document> documentList = documentService.listOnPage(documentCriteria);
			if(documentList == null || documentList.size() != 1){
				logger.debug("文档[" + document.getUdid() + "]的上一篇文档列表数量是:" + (documentList == null ? "空" : documentList.size()));
			} else {
				Document lastDocument = documentList.get(0).clone();
				logger.info("文档[" + document.getUdid() + "]的上一篇文档是:" + lastDocument.getUdid());
				map.put("lastDocument", lastDocument);
			}
			documentCriteria.setMaxUdid(0);
			documentCriteria.setMinUdid(document.getUdid() + 1);
			documentCriteria.setOrderBy(" udid ASC");
			documentList = documentService.listOnPage(documentCriteria);
			if(documentList == null || documentList.size() != 1){
				logger.debug("文档[" + document.getUdid() + "]的下一篇文档列表数量是:" + (documentList == null ? "空" : documentList.size()));
			} else {
				Document nextDocument = documentList.get(0).clone();
				logger.info("文档[" + document.getUdid() + "]的下一篇文档是:" + nextDocument.getUdid());
				map.put("nextDocument", nextDocument);
			}
		}
	}
	/**
	 * 写入该文章对应的阅读数
	 * @param document
	 * @param map
	 */
	protected void applyReadCount(User frontUser, Document document, ModelMap map){

		UserRelation userRelation = new UserRelation(document.getOwnerId());
		userRelation.setRelationLimit(UserRelationCriteria.RELATION_LIMIT_UNIQUE);
		userRelation.setObjectType(ObjectType.document.name());
		userRelation.setRelationType(UserRelationCriteria.RELATION_TYPE_READ);
		userRelation.setObjectId(document.getUdid());
		if(frontUser != null){

			userRelation.setUuid(frontUser.getUuid());
			userRelation.setCreateTime(new Date());
			userRelationService.insertAsync(userRelation);
		} else {
			//增加一个匿名阅读数
			userRelationService.plusCachedRelationCount(userRelation);
		}

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

	protected void writeReadCount(Document document){

		UserRelationCriteria userRelationCriteria = new UserRelationCriteria(document.getOwnerId());
		userRelationCriteria.setObjectType(ObjectType.document.name());
		userRelationCriteria.setRelationType(UserRelationCriteria.RELATION_TYPE_READ);
		userRelationCriteria.setObjectId(document.getUdid());
		int readCount = userRelationService.getRelationCount(userRelationCriteria);
		if(readCount < 1){
			readCount = 1;
		}
		document.setExtraValue("readCount", String.valueOf(readCount));

	}

	/**
	 * 向map中写入评论列表，以及好评、中评和差评的数量
	 * @param document
	 * @param map
	 */
	protected void writeCommentList(Document document, ModelMap map,HttpServletRequest request) {



		CommentCriteria commentCriteria = new CommentCriteria(document.getOwnerId());
		commentCriteria.setCurrentStatus(CommentCriteria.STATUS_PUBLISHED);

		//查看文章是否有评论
		commentCriteria.setObjectType(ObjectType.document.name());
		commentCriteria.setObjectId(document.getUdid());
		logger.debug("查看文章评论"+commentCriteria.getOwnerId()+"#"+commentCriteria.getObjectId()+"#"+commentCriteria.getObjectType());

		List<Comment> commentList = commentService.list(commentCriteria);
		int rows = ServletRequestUtils.getIntParameter(request, "rows", 5);		
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);
		Paging paging = new Paging(rows);
		paging.setCurrentPage(page);
		commentCriteria.setPaging(paging);
		List<Comment> commentList4 = commentService.listOnPage(commentCriteria);
		logger.debug("针对对象[" + commentCriteria.getObjectType() + "#" + commentCriteria.getObjectId() + "]的评论数是:" + (commentList == null ? "空" : commentList.size()));
		if(commentList == null || commentList.size() < 1){
			return;
		}
		int totalResults = commentList.size();
		int totalPages = totalResults%rows==0?totalResults/rows:totalResults/rows+1;
		map.put("totalPages", totalPages);
		map.put("totalResults", totalResults);
		List<List<Comment>> commentList2 = commentService.sort(commentList4);
		int goodRankCount = 0;
		int normalRankCount = 0;
		int badRankCount = 0;
		int commentCount = 0;
		for(Comment comment : commentList){
			if(comment.getRank() == CommentCriteria.RANK_GOOD){
				goodRankCount++;
			}
			if(comment.getRank() == CommentCriteria.RANK_NORMAL){
				goodRankCount++;
			}
			if(comment.getRank() == CommentCriteria.RANK_BAD){
				goodRankCount++;
			}
			commentCount++;

		}


		map.put("commentList",commentList2);
		map.put("goodRankCount", goodRankCount);
		map.put("normalRankCount", normalRankCount);
		map.put("badRankCount", badRankCount);
		map.put("commentCount", commentCount);

	}

	/**
	 * 写入最新通知
	 * @param map
	 * @param ownerId
	 */
	protected void writeNotice(ModelMap map, long ownerId){
		final String noticePath = "notice";
		final int ROWS = 3;

		Paging paging = new Paging(ROWS);
		paging.setCurrentPage(1);

		DocumentCriteria documentCriteria = new DocumentCriteria();
		documentCriteria.setCurrentStatus(DocumentStatus.published.getId());
		documentCriteria.setNodePath(noticePath);
		documentCriteria.setOwnerId(ownerId);
		documentCriteria.setPaging(paging);
		List<Document> noticeDocumentList = null;
		try {
			noticeDocumentList = documentService.listOnPage(documentCriteria);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(noticeDocumentList != null && noticeDocumentList.size() > 0){
			map.put("noticeDocumentList", noticeDocumentList);
		}
	}











	protected void writeCommentCount2(Document document, Map<String,Object> map) {
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
		map.put("commentCount",commentCount);		
		document.setExtraValue("commentCount", String.valueOf(commentCount));
	}
	/**
	 * 浏览历史
	 * @param frontUser
	 * @param map
	 */
	protected void writeBrowseHistory(User frontUser,Map<String,Object> map){
		UserRelationCriteria userRelationCriteria = new UserRelationCriteria();
		userRelationCriteria.setObjectType(ObjectType.document.name());
		userRelationCriteria.setRelationType(UserRelationCriteria.RELATION_TYPE_READ);
		userRelationCriteria.setUuid(frontUser.getUuid());
		userRelationCriteria.setOwnerId(frontUser.getOwnerId());
		Paging paging = new Paging(ROWS_PER_PAGE);
		paging.setCurrentPage(1);
		userRelationCriteria.setPaging(paging);
		List<UserRelation> list = userRelationService.listOnPage(userRelationCriteria);
		List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
		for(UserRelation ur:list){
			Document doc = documentService.select((int)ur.getObjectId());
			if(doc== null || doc.getDocumentTypeId()==171002){
				continue;
			}
			/*Product product = productService.getProductByDocument(doc);
			if(product!=null){
				hashMap.put("doc", doc);
				hashMap.put("buyMoney", product.getBuyMoney());
				listMap.add(hashMap);
			}*/
		}
		map.put("browseHistoryList", listMap);
	}
	/**
	 * 我的推荐
	 * @param frontUser
	 * @param map
	 */
	protected void writeMyGroom(User frontUser,Map<String,Object> map){
		UserRelationCriteria userRelationCriteria = new UserRelationCriteria();
		userRelationCriteria.setObjectType(ObjectType.document.name());
		userRelationCriteria.setRelationType("groom");
		userRelationCriteria.setUuid(frontUser.getUuid());
		userRelationCriteria.setOwnerId(frontUser.getOwnerId());
		Paging paging = new Paging(ROWS_PER_PAGE);
		paging.setCurrentPage(1);
		userRelationCriteria.setPaging(paging);
		List<UserRelation> list = userRelationService.listOnPage(userRelationCriteria);
		List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
		for(UserRelation ur :list){
			HashMap<String, Object> hashMap = new HashMap<String,Object>();
			Document doc = documentService.select((int)ur.getObjectId());
			hashMap.put("userRelationId", ur.getUserRelationId());
			hashMap.put("doc", doc);
			listMap.add(hashMap);
		}
		map.put("myGroomList", listMap);
	}
	/**
	 * 首页获取最新上架
	 * @param frontUser
	 * @param map
	 * @throws Exception 
	 */
	protected void writeNewestList(HttpServletRequest request,HttpServletResponse response,User frontUser, ModelMap map) throws Exception {
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
		DocumentCriteria documentCriteria = new DocumentCriteria();
		documentCriteria.setCurrentStatus(DocumentStatus.published.getId());
		documentCriteria.setOrderBy(" publish_time desc ");
		List<Map<String,Object>> listMAP = new ArrayList<Map<String,Object>>();
		String transactionToken = ServletRequestUtils.getStringParameter(request, "tt");
		if(StringUtils.isNotBlank(transactionToken)){
			map.put("tt",transactionToken);
		}
		Paging paging = new Paging(ROWS_PER_PAGE);
		paging.setCurrentPage(1);
		documentCriteria.setPaging(paging);
		documentCriteria.setOwnerId(ownerId);
		List<Document> documentList= documentService.listOnPage(documentCriteria);
		//		logger.debug("数量："+indexHotSaleList==null?"空":indexHotSaleList.size()+"");
		for(Document doc:documentList){
			Map<String,Object> hasMap = new HashMap<String,Object>();
			//			Document doc = document.clone();
			//			writeRelationCount(doc);
			
			hasMap.put("document", doc.clone());
			
			listMAP.add(hasMap);
		}
		logger.debug("最新文档列表:" + listMAP.size());
		map.put("newestDocumentList", listMAP);
	}




	/**
	 * 首页获取精品推荐
	 * @param frontUser
	 * @param map
	 * @throws Exception 
	 */
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
		List<Map<String,Object>> listMAP = new ArrayList<Map<String,Object>>();
		for (Document document : documentList) {
			Map<String,Object> hasMap = new HashMap<String,Object>();
			Document doc = document.clone();
			writeRelationCount(doc);
			hasMap.put("document", doc);
			listMAP.add(hasMap);
		}
		logger.debug("推荐文档列表:" + listMAP.size());
		map.put("recommendDocumentList", listMAP);
		//map.put("indexRecommendList", listMAP);
	}
	protected void writeCrouselFigureList(HttpServletRequest request,
			HttpServletResponse response,User frontUser, ModelMap map) throws Exception {
		logger.debug("首页轮播图");
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
		DocumentCriteria documentCriteria = new DocumentCriteria();
		documentCriteria.setCurrentStatus(130005);
		documentCriteria.setDisplayTypeId(176005);
		documentCriteria.setOwnerId(ownerId);
		documentCriteria.setOrderBy(" last_modified desc ");
		int count = 1;
		List<Map<String,Object>> listMAP = new ArrayList<Map<String,Object>>();
		String transactionToken = ServletRequestUtils.getStringParameter(request, "tt");
		if(StringUtils.isNotBlank(transactionToken)){
			map.put("tt",transactionToken);
		}
		Paging paging = new Paging(ROWS_PER_PAGE);
		paging.setCurrentPage(count);
		documentCriteria.setPaging(paging);
		documentCriteria.setOwnerId(ownerId);
		List<Document> indexRecommendList= documentService.listOnPage(documentCriteria);
		//		logger.debug("数量："+indexRecommendList==null?"空":indexRecommendList.size()+"");
		if(indexRecommendList!=null){
			for(Document doc:indexRecommendList){
				Map<String,Object> hasMap = new HashMap<String,Object>();
				writeRelationCount(doc);
				hasMap.put("document", getDocByDelDataMap(doc));
				listMAP.add(hasMap);
			}
			logger.debug("首页轮播图的数量为"+indexRecommendList.size());
		}
		map.put("indexCrouselFigureList", listMAP);
	}
	protected Map<String,String> getDocByDelDataMap(Document doc){
		logger.debug("拆开"+doc.getExtraValue("productGallery"));
		Map<String,String> map = new HashMap<String,String>();
		writePraiseCount(doc);
		//writeCommentCount(doc);
		writeReadCount(doc);
		writeFavoriteCount(doc);
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
	public void writeExtraData(ModelMap map, User frontUser, Map<String, Object> parameter) {

	}


}
