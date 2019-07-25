package com.maicard.wpt.np;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;

import com.maicard.common.service.ConfigService;
import com.maicard.common.util.ContextTypeUtil;
import com.maicard.common.util.Paging;
import com.maicard.money.service.PriceService;
import com.maicard.product.criteria.CartCriteria;
import com.maicard.product.domain.Cart;
import com.maicard.product.domain.Product;
import com.maicard.product.service.CartService;
import com.maicard.product.service.ProductService;
import com.maicard.security.domain.User;
import com.maicard.security.service.CertifyService;
import com.maicard.site.criteria.DocumentCriteria;
import com.maicard.site.criteria.TagCriteria;
import com.maicard.site.criteria.TagObjectRelationCriteria;
import com.maicard.site.domain.Document;
import com.maicard.site.domain.IncludeNodeConfig;
import com.maicard.site.domain.Node;
import com.maicard.site.domain.Tag;
import com.maicard.site.service.DocumentService;
import com.maicard.site.service.NodeProcessor;
import com.maicard.site.service.NodeService;
import com.maicard.site.service.TagObjectRelationService;
import com.maicard.site.service.TagService;
import com.maicard.site.service.TemplateService;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.ObjectType;
import com.maicard.standard.PriceType;
import com.maicard.standard.SiteStandard;
import com.maicard.standard.SecurityStandard.UserTypes;
import com.maicard.standard.SiteStandard.DocumentStatus;


/*
 * WPT通用首页处理器
 */
@Service
public class WptIndexNodeProcessor extends WptDefaultNodeProcessor implements NodeProcessor{

	@Resource
	private ProductService productService;
	@Resource
	private NodeService nodeService;	
	@Resource
	private PriceService priceService;
	@Resource
	private DocumentService documentService;
	@Resource
	private ConfigService configService;
	@Resource
	private TagService tagService;
	@Resource
	private TagObjectRelationService tagObjectRelationService;
	@Resource
	private TemplateService templateService;
	@Resource
	private CertifyService certifyService;
	@Resource
	private CartService cartService;
	
	private int rowsPerPage = 10;
//	private int maxIndexPerPage = 12;
	

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
		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return CommonStandard.frontMessageView;		
		}
		if(node.getOwnerId() != ownerId){
			logger.error("尝试获取的节点[" + node.getNodeId() + "]，其ownerId[" + node.getOwnerId() + "]与系统会话中的[" + ownerId + "]不一致");
			return CommonStandard.frontMessageView;
		}
		logger.debug("首页节点处理器,处理节点:" + node.getName());


		User frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
		if(frontUser != null){
			writeUserInfo(frontUser, map);
		}

		
		int rows = ServletRequestUtils.getIntParameter(request, "rows", rowsPerPage);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);


		DocumentCriteria documentCriteria = new DocumentCriteria();
		documentCriteria.setCurrentStatus(DocumentStatus.published.getId());
		documentCriteria.setNodePath(node.getPath());
		documentCriteria.setOwnerId(ownerId);
		Paging paging = new Paging(rows);
		paging.setCurrentPage(page);
		documentCriteria.setPaging(paging);
		String[] tags = ServletRequestUtils.getStringParameters(request,"tags");
		String tagsName = ServletRequestUtils.getStringParameter(request,"tags", null);
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

		

		List<Document> documentList2 = new ArrayList<Document>();
		logger.info("发布到本节点的所有文章数:" + (documentList == null? "空":documentList.size()));
		if(documentList != null){
			for(Document d1 : documentList){
				Document document = d1.clone();
				logger.debug("文章权重 ：" + document.getDisplayIndex());
				documentList2.add(document);
			}
			map.put("newsList",documentList2);
		} 

		//	String contextType = ContextTypeUtil.getContextType(request);
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
				if(includeNodeConfig.getDocumentTypeCode() != null){
					if(includeNodeConfig.getDocumentTypeCode().startsWith("!")){
						documentCriteria.setNoDocumentTypeCode(includeNodeConfig.getDocumentTypeCode().replaceFirst("!", ""));
					} else {
						documentCriteria.setDocumentTypeCode(includeNodeConfig.getDocumentTypeCode());
					}
				}
				documentCriteria.setNodePath(n.getPath());	
				Paging paging2 = new Paging(includeNodeConfig.getRows());
				paging2.setCurrentPage(1);
				documentCriteria.setPaging(paging2);
				
				List<Document> documentList3 = documentService.listOnPage(documentCriteria);
				logger.debug("自动放入栏目[" + node.getNodeId() + "/" + node.getName() + "]的子栏目[" + n.getNodeId() + "/" + n.getName() + "]文章数量是:" + (documentList3 == null ? "空" : documentList3.size()));
				if(documentList3 != null && documentList3.size() > 0){
					List<Document> documentList4 = new ArrayList<Document>();
					for(Document d2 : documentList3){
						String productCode = d2.getExtraValue(DataName.productCode.toString());
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
									priceDataResult = priceService.generatePriceExtraData(d2, PriceType.PRICE_PROMOTION.toString());
									logger.debug("根据产品[" + product.getProductId() + "/" + product.getProductCode() + "]为文档生成活动价格结果:" + priceDataResult);

								} else {
									priceDataResult = priceService.generatePriceExtraData(d2, PriceType.PRICE_STANDARD.toString());
									logger.debug("根据产品[" + product.getProductId() + "/" + product.getProductCode() + "]为文档生成标准价格结果:" + priceDataResult);
								}

							}
						}

						documentList4.add(d2);
					}
					String listName = n.getAlias() + "List";
					logger.debug("放入子栏目[" + node.getName() + "/" + n.getAlias() + "]的文章到栏目[" + node.getAlias() + "],列表名是:" + listName);
					map.put(listName, documentList4);
				}

			}
			map.put("subNodeLink", subNodeLink);
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

		//writeHotSaleList(request, response,frontUser, map);
		writeNewestList(request, response,frontUser, map);
		writeRecommendList(request, response,frontUser, map);
		Node navigationNode = node.clone();
		navigationNode.setNodeTypeId(SiteStandard.NodeType.business.id);
		writeNavigation(navigationNode,map);
		return defaultTemplateLocation;
	}

	@Override
	public String detail(HttpServletRequest request,
			HttpServletResponse response, ModelMap map)
					throws Exception {
		return super.detail(request, response, map);
	}

	private void writeUserInfo(User frontUser, ModelMap map) {

		map.put("frontUser", frontUser);
		Cart cart = cartService.getCurrentCart(frontUser.getUuid(), PriceType.PRICE_STANDARD.toString(), CartCriteria.ORDER_TYPE_TEMP, null, frontUser.getOwnerId(), false);
		if(cart != null){
			map.put("cartCount", cart.getTotalGoods());
		} else {
			map.put("cartCount", 0);
		}

	}

	@Override
	public void writeExtraData(ModelMap map, User frontUser, Map<String, Object> parameter) {
		// TODO Auto-generated method stub
		
	}
	
}



