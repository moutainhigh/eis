package com.maicard.wpt.np;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;

import com.maicard.common.domain.SiteDomainRelation;
import com.maicard.common.service.CookieService;
import com.maicard.common.service.SiteDomainRelationService;
import com.maicard.common.util.NumericUtils;
import com.maicard.common.util.Paging;
import com.maicard.common.util.PreviewToken;
import com.maicard.mb.criteria.MessageCriteria;
import com.maicard.product.criteria.ItemCriteria;
import com.maicard.product.domain.Item;
import com.maicard.product.domain.Product;
import com.maicard.security.criteria.UserRelationCriteria;
import com.maicard.security.domain.User;
import com.maicard.security.domain.UserRelation;
import com.maicard.site.criteria.DocumentCriteria;
import com.maicard.site.domain.Document;
import com.maicard.site.domain.DocumentData;
import com.maicard.site.domain.Node;
import com.maicard.site.service.NodeProcessor;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.DisplayLevel;
import com.maicard.standard.ObjectType;
import com.maicard.standard.MessageStandard.MessageStatus;
import com.maicard.standard.SecurityStandard.UserTypes;
import com.maicard.standard.SiteStandard.DocumentStatus;
import com.maicard.standard.TransactionStandard.TransactionStatus;
import com.maicard.wpt.service.VipService;


/**
 * 含有受保护内容的详情页，例如教育内容<br>
 * 只有订阅用户或VIP才能查看部分内容
 *
 *
 * @author NetSnake
 * @date 2017-05-21
 *
 */
@SuppressWarnings("unused")
@Service
public class ProtectedContentNodeProcessor extends ElectronicBusinessProcessor implements NodeProcessor{

	@Autowired(required=false)
	private VipService vipService;
	
	
	@Resource
	private SiteDomainRelationService siteDomainRelationService;

	@Resource
	private CookieService cookieService;

	

	@Override
	public String index(HttpServletRequest request,
			HttpServletResponse response, ModelMap map) throws Exception {
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
		logger.debug("首页节点处理器,处理节点:" + node.getName());

		//检查当前是否找到了合作方的theme及其uuid
		long sitePartnerId = NumericUtils.parseLong(map.get(DataName.sitePartnerId.toString()));
		logger.debug("当前找到的sitePartnerId=" + sitePartnerId);


		User frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.id);
		if(frontUser != null){
			writeUserInfo(frontUser, map, request);
		}
		writeSideBar(request, response,map,ownerId);
		recommendsList(request, response, map);
		writeCrouselFigureList(request, response, frontUser, map);
		//写入最新通知文档
//		this.writeNotice(map, ownerId);


//		int rows = ServletRequestUtils.getIntParameter(request, "rows", ROWS_PER_PAGE);
//		int page = ServletRequestUtils.getIntParameter(request, "page", 1);
		int rows =10;
		int page =1;
		if(map.get("currentPage")!=null){
			page = Integer.parseInt(map.get("currentPage").toString());
		}
		if(map.get("rowsPerPage")!=null){
			rows = Integer.parseInt(map.get("rowsPerPage").toString());
		}
		Paging paging = new Paging(rows);
		paging.setCurrentPage(page);
		List<Document> documentList = new ArrayList<Document>();
		List<Document> documentList2 = new ArrayList<Document>();



		String search = ServletRequestUtils.getStringParameter(request, "search");
		String title = ServletRequestUtils.getStringParameter(request, "title");
		logger.debug("是否是ProtectedContentNodeProcessor搜索：search="+search+"#title="+title);
		DocumentCriteria documentCriteria = new DocumentCriteria();
		documentCriteria.setCurrentStatus(DocumentStatus.published.getId());
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

		logger.info("发布到本节点的所有文章数:" + (documentList == null? "空":documentList.size()));
		if(documentList != null){
			for(Document d1 : documentList){
				Document d2 = d1.clone();
				//writeProductData(d2, sitePartnerId, frontUser, null);
				Document document = filterSubscribeData(request,response,d2, frontUser);
				writeFavoriteCount(document);
				//writeCommentCount(document);
				writeReadCount(document);
				document.setContent("");
				documentList2.add(document);
				logger.debug("该文章对应的udid为："+document.getUdid());
			}
			map.put("newsList",documentList2);
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
			logger.error("节点[" + node.getName() + "/" + node.getNodeId() + "]未定义默认模版");
			return CommonStandard.frontMessageView;	

		}
		defaultTemplateLocation = defaultTemplateLocation.replaceAll("\\.\\w+$", "");

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



		String transactionToken = ServletRequestUtils.getStringParameter(request, "tt");
		if(StringUtils.isNotBlank(transactionToken)){
			map.put("tt",transactionToken);
		}
		logger.debug("documentCode:"+documentCode+"#ownerId:"+ownerId);
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
			if(!productSmallImage.startsWith("http://")){
				document.getDocumentDataMap().get(DataName.productSmallImage.toString()).setDataValue("http://"+request.getServerName()+ document.getDocumentDataMap().get("productSmallImage").getDataValue());
			}
		} else{
			document.setExtraValue(DataName.productSmallImage.toString(), "http://"+request.getServerName()+"/style/mobile/images/logo.png");
		}
		
		//writeCommentCount(document, map, 1);
		writeRelatedDocument(document, map);
		writeNeighborDocument(document, node, map);
//		writeSideBar(request, response,map,ownerId);
		recommendsList(request, response, map);
		applyReadCount(frontUser, document, map);
		writeCommentList(document, map, request);
		
		//writeProductData(document, 0, frontUser, null);
		if(frontUser != null){
//			String vPro = ServletRequestUtils.getStringParameter(request, "vPro", "");
//			logger.debug("订购教学列表"+vPro);
//			if(vPro.length()>1)
//			writePurchaseQuery(request,response,frontUser, map);
			writeUserInfo(frontUser, map, request);
			writeFavoriteCount(document, map);
			writePraiseCount(document);
			writeMoney(frontUser,map);
			writeCartCount(frontUser,map);
		}
		User u = frontUserService.select(document.getPublisherId());
		logger.debug("文章的上传者信息："+u);
		if(u!=null){
			String userHeadPic = u.getExtraValue("userHeadPic");
			String userDescription = u.getExtraValue("userDescription");
			String userName = u.getUsername();
			Map<String,String> m = new HashMap<String,String>();
			m.put("userHeadPic", userHeadPic);
			m.put("userDescription", userDescription);
			m.put("userName", userName);
			logger.debug("文章的上传者信息2："+m);
			map.put("uploadAuthor", m);
		}
		Document d2 = filterSubscribeData(request,response,document, frontUser);
		document = null;

		/*map.put("host", "");
		String ua =request.getHeader("user-agent").toLowerCase();
		if (ua.indexOf("micromessenger") > 0) {// 是微信浏览器
			map.put("host","http://" + request.getServerName() + "/wexin/accredit?url="+request.getRequestURL().toString());
		}*/
		/*Product product = productService.getProductByDocument(d2);
		logger.debug("过滤后的Document"+d2.getUdid());
		if(product!=null && product.getLabelMoney()>0){
			if(frontUser == null){
				d2.setContent("notLogin");
				logger.debug("观看付费产品但没有登录");
			}else{
				String vipFreeStr = product.getExtraValue("vipFree");
				logger.debug("获得对应产品"+product.getProductId()+"产品对应的vipFree为"+vipFreeStr+"用户级别"+frontUser.getLevel());
				int vipFree=0;
				if(vipFreeStr!=null && !"".equals(vipFreeStr)){
					vipFree = Integer.parseInt(vipFreeStr);
				}
				if(frontUser.getLevel()==2){//会员用户
					logger.debug("会员用户");
					if(vipFree==1){
						logger.debug("该产品无论会员还是普通用户都需要购买");
						d2.setContent("allPurchase");
					}
				}else{//非会员用户
					logger.debug("非会员用户");
					ItemCriteria itemCriteria = new ItemCriteria();
					itemCriteria.setOwnerId(ownerId);
					itemCriteria.setProductIds(product.getProductId());
					itemCriteria.setChargeFromAccount(frontUser.getUuid());
					Item item = itemService.selectOneForMatch(itemCriteria);
					logger.debug(item==null?"用户没有购买当前产品":"用户已经购买当前产品");
					if(item==null){
						//没有订购
						if(vipFree==2){
							logger.debug("仅仅会员免费看且普通用户无法购买观看");
							d2.setContent("vipOnly");
						}else if(vipFree == 0){
							logger.debug("会员免费看而普通用户需要购买");
							d2.setContent("vip");
						}else{
							logger.debug("该产品无论会员还是普通用户都需要购买");
							d2.setContent("allPurchase");
						}
					}
				}
			}
			
		}*/
		map.put("pageTitle",d2.getTitle() );
		map.put("pageDesc", d2.getDesc(0));
		logger.debug("当前文档的udid为"+d2.getUdid());
		map.put("document",d2);
		map.put("node",node);
		map.put("pageType", "detail");//标识为详情页
		if(frontUser!=null){
			map.put("unreadMessage",unreadMessageCount(frontUser.getUuid()));
		}
		
		//map.put("logo", logo);
		String url=request.getRequestURL().toString();
		if (request.getQueryString()!=null){
			url=url+"?"+request.getQueryString();
		}
		return getTemplateLocation(d2, node, ownerId);

	}
	


	private int unreadMessageCount(long uuid){
		MessageCriteria mc = new MessageCriteria();
		mc.setReceiverId(uuid);
		mc.setCurrentStatus(MessageStatus.unread.id);
		return userMessageService.count(mc);
	}
	
	/**
	 * 过滤只有登陆或订阅用户才能查看的内容
	 * @param document
	 * @return
	 */
	protected Document filterSubscribeData(HttpServletRequest request,
			HttpServletResponse response, Document document, User frontUser) {
		long objectId = 0;
		String productCode = document.getExtraValue(DataName.productCode.toString());

		UserRelationCriteria userRelationCriteria = new UserRelationCriteria(document.getOwnerId());
		userRelationCriteria.setObjectId(objectId);
		if(StringUtils.isBlank(productCode)){
			userRelationCriteria.setObjectType(ObjectType.document.toString());
			objectId = document.getUdid();

		} else {
			userRelationCriteria.setObjectType(ObjectType.product.toString());
			Product product = productService.select(productCode, document.getOwnerId());
			if(product == null){
				logger.error("找不到文档[" + document.getUdid() + "]所对应的产品:" + productCode);
			} else {
				objectId = product.getProductId();
			}
		}	

		boolean subscribeIsValid = false;
		List<UserRelation> userRelationList = userRelationService.list(userRelationCriteria);
		if(userRelationList == null || userRelationList.size() < 1){
			//没有有效的订阅
			logger.debug("没有有效的订阅");
		} else {
			UserRelation userRelation = userRelationList.get(0);
			String subscribeValidTo = userRelation.getExtraValue("subscribeValidTo");
			if(StringUtils.isNotBlank(subscribeValidTo)){
				//该订阅有一个有效期
				logger.debug("该订阅有一个有效期");
				Date validDate = null;
				try {
					validDate = sdf.parse(subscribeValidTo);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				if(validDate == null){
					logger.error("订阅关系[userRelationId = " + userRelation.getUserRelationId() + "]的有效期数据[" + subscribeValidTo + "]无法解析");
				}
				if(validDate.before(new Date())){
					logger.info("订阅有效期是:" + subscribeValidTo + ",已过期");
					userRelationService.delete(userRelation.getUserRelationId());
				} else {
					subscribeIsValid = true;
				}

			} else {
				//订阅没有有效期，长期有效
				subscribeIsValid = true;
			}
		}
		Document d2 = document.clone();
		d2.setDocumentDataMap(null);
		HashMap<String,DocumentData> documentDataMap = new HashMap<String, DocumentData>();
		
		UserRelation userRelation = new UserRelation();
		if(frontUser!=null){
			if(vipService != null){
				userRelation = vipService.getVipRelation(frontUser);
			}
			logger.debug("过滤器中的userRelation:"+userRelation);
			DocumentData vipLevel = new DocumentData();
			SiteDomainRelation siteDomainRelation = siteDomainRelationService.getByHostName(request.getServerName());
			if(siteDomainRelation == null){
				logger.error("根据主机名[" + request.getServerName() + "]找不到指定的站点关系");
			}
			if(siteDomainRelation!=null){
				if(cookieService.getCookie(request, "frontVipLevel")!=null){//有缓存就要清理
					cookieService.removeCookie(request, response, "frontVipLevel", siteDomainRelation.getCookieDomain());
				}
				if(userRelation==null){
					vipLevel.setDataValue("0");
					cookieService.addCookie(request, response, "frontVipLevel", "0", sessionTimeout, siteDomainRelation.getCookieDomain(),false);
				}else{
					vipLevel.setDataValue(userRelation.getCurrentStatus()+"");
					cookieService.addCookie(request, response, "frontVipLevel", userRelation.getCurrentStatus()+"", sessionTimeout, siteDomainRelation.getCookieDomain(),false);
				}
			}
			documentDataMap.put("vipLevel",vipLevel);
		}
		if(document.getDocumentDataMap() != null && document.getDocumentDataMap().size() > 0){
			for(DocumentData dd : document.getDocumentDataMap().values()){
				DocumentData docData = new DocumentData();
				logger.debug("检查文档扩展数据的访问级别:" + dd.getDataCode() + ",dataDefineId=" + dd.getDataDefineId() + ",displayLevel=" + dd.getDisplayLevel());
				if(dd.getDisplayLevel().equals(DisplayLevel.login.toString()) && frontUser == null){
					//登陆才能访问的内容
					docData = new DocumentData();
					logger.debug("未登录用户无权查看付费产品");
					docData.setDataValue("notLogin");
					documentDataMap.put("payFlag", docData);
					continue;
				} else if(dd.getDisplayLevel().equals(DisplayLevel.subscriber.toString())){
					//检查必须订阅才能访问的内容
					logger.debug("进入付费产品过滤控制器"+frontUser+"#"+objectId+"#"+subscribeIsValid);
					
					Product product = productService.getProductByDocument(document);
					logger.debug("过滤后的Document"+document.getUdid());
					if(product!=null && product.getLabelMoney()>0){
//						logger.debug("标志位："+frontUser+"#"+frontUser.getLevel());
						
						
							docData = new DocumentData();
							if(frontUser == null || objectId == 0){
								logger.debug("未登录用户无权查看付费产品");
								docData.setDataValue("notLogin");
								documentDataMap.put("payFlag", docData);
								continue;
							}
							String vipFreeStr = product.getExtraValue("vipFree");
							logger.debug("获得对应产品"+product.getProductId()+"产品对应的vipFree为"+vipFreeStr+"用户级别"+frontUser.getLevel());
							int vipFree=0;
							if(vipFreeStr!=null && !"".equals(vipFreeStr)){
								vipFree = Integer.parseInt(vipFreeStr);
							}
							if(userRelation!=null && userRelation.getCurrentStatus()==2){//会员用户
								logger.debug("会员用户");
								if(vipFree==1 ){
									ItemCriteria itemCriteria = new ItemCriteria();
									itemCriteria.setOwnerId(product.getOwnerId());
									itemCriteria.setProductIds(product.getProductId());
									itemCriteria.setChargeFromAccount(frontUser.getUuid());
									itemCriteria.setCurrentStatus(TransactionStatus.success.getId());
									Item item = itemService.selectOneForMatch(itemCriteria);
									logger.debug(item==null?"会员用户没有购买当前产品":"用户已经购买当前产品");
									if(item==null){
										logger.debug("该产品无论会员还是普通用户都需要购买");
										docData.setDataValue("allPurchase");
										documentDataMap.put("payFlag", docData);
										continue;
									}
								}
								documentDataMap.put(dd.getDataCode(), dd);
							}else{//非会员用户
								ItemCriteria itemCriteria = new ItemCriteria();
								itemCriteria.setOwnerId(product.getOwnerId());
								itemCriteria.setProductIds(product.getProductId());
								itemCriteria.setChargeFromAccount(frontUser.getUuid());
								itemCriteria.setCurrentStatus(TransactionStatus.success.getId());
								Item item = itemService.selectOneForMatch(itemCriteria);
								logger.debug(item==null?"用户没有购买当前产品":"用户已经购买当前产品");
//								if(item==null || !subscribeIsValid){
									if(item==null){
									//没有订购
									logger.debug("没有订购或者有效期失效");
									if(vipFree==2){
										logger.debug("仅仅会员免费看且普通用户无法购买观看");
										docData.setDataValue("vipOnly");
										documentDataMap.put("payFlag", docData);
									}else if(vipFree == 0){
										logger.debug("会员免费看而普通用户需要购买");
										docData.setDataValue("vip");
										documentDataMap.put("payFlag", docData);
									}else{
										logger.debug("该产品无论会员还是普通用户都需要购买");
										docData.setDataValue("allPurchase");
										documentDataMap.put("payFlag", docData);
									}
									continue;
								}
								documentDataMap.put(dd.getDataCode(), dd);
							}
						
					}else if(product.getLabelMoney()==0){
						documentDataMap.put(dd.getDataCode(), dd);
					}
				} else {
					documentDataMap.put(dd.getDataCode(), dd);
				}

			}
		}
		
		d2.setDocumentDataMap(documentDataMap);
		return d2;
	}
	
	/*protected UserRelation vipExpiredOrNot(User frontUser){
		logger.debug("进入vip过期判定");
		UserRelationCriteria userRelation = new UserRelationCriteria();
		userRelation.setOwnerId(frontUser.getOwnerId());
		userRelation.setUuid(frontUser.getUuid());
		userRelation.setObjectType("VIP");
		List<UserRelation> userRelationList = userRelationService.list(userRelation);
		if(userRelationList.size()<1){
			logger.debug("未找到用户"+frontUser.getUsername()+"的VIP会员关联数据");
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		Calendar cld = Calendar.getInstance();  
		Date d2 = null;
		UserRelation ur = userRelationList.get(0);
		String relationType = ur.getRelationType();
		if("VIP_MONTH".equals(relationType)){
				cld.setTime(ur.getCreateTime());  
			    cld.add(Calendar.MONTH, 1);  
			    d2 = cld.getTime();
			   
		}else
		if("VIP_QUARTER".equals(relationType)){
			cld.setTime(ur.getCreateTime());  
		    cld.add(Calendar.MONTH, 3);  
		    d2 = cld.getTime();
		}else
		if("VIP_YEAR".equals(relationType)){
			cld.setTime(ur.getCreateTime());  
		    cld.add(Calendar.YEAR, 1);  
		    d2 = cld.getTime();
		}
		logger.debug("relationType:"+relationType+"#d2:"+d2);
		if(d2!=null && new Date().getTime() >d2.getTime()){
			 logger.debug("会员到期日期："+sdf.format(d2.getTime()));
		     ur.setCurrentStatus(1);
		     userRelationService.update(ur);
		}
		
		return ur;
	}*/
}



