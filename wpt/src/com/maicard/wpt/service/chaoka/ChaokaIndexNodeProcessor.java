package com.maicard.wpt.service.chaoka;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ConfigService;
import com.maicard.common.util.DisplayUtils;
import com.maicard.common.util.JsonUtils;
import com.maicard.common.util.Paging;
import com.maicard.money.service.PriceService;
import com.maicard.product.service.ProductService;
import com.maicard.security.domain.User;
import com.maicard.security.service.CertifyService;
import com.maicard.site.criteria.DocumentCriteria;
import com.maicard.site.domain.Document;
import com.maicard.site.domain.DocumentData;
import com.maicard.site.domain.Node;
import com.maicard.site.service.DocumentService;
import com.maicard.site.service.NodeProcessor;
import com.maicard.site.service.NodeService;
import com.maicard.site.service.TagService;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.EisError;
import com.maicard.standard.PriceType;
import com.maicard.standard.SecurityStandard.UserTypes;
import com.maicard.standard.SiteStandard.DocumentStatus;

/*
 * 炒卡网首页处理器
 */
@Service
public class ChaokaIndexNodeProcessor extends BaseService implements NodeProcessor{

	@Resource
	private CertifyService certifyService;
	@Resource
	private NodeService nodeService;	
	@Resource
	private DocumentService documentService;
	@Resource
	private ConfigService configService;
	@Resource
	private ProductService productService;
	@Resource
	private TagService tagService;

	@Resource
	protected PriceService priceService;


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
		Node node = null;
		try{
			node = (Node)map.get("node");
		}catch(Exception e){}
		if(node == null){
			logger.debug("map中找不到节点node");
			return CommonStandard.frontMessageView;		
		}
		///////////////////////////// 处理节点数据 ///////////////////////////////////
		logger.debug("处理节点[" + node.getNodeId() + "/" + node.getPath() + "]");

		String defaultTemplateLocation =null;
		try{
			defaultTemplateLocation = node.getTemplateLocation();
		}catch(NullPointerException e){
		}
		if(defaultTemplateLocation == null){
			logger.error("节点[" + node.getNodeId() + "/" + node.getName() + "]未定义默认模版");
			return CommonStandard.frontMessageView;		

		}
		defaultTemplateLocation = defaultTemplateLocation.replaceAll("\\.\\w+$", "");

		User frontUser = certifyService.getLoginedUser(request,response, UserTypes.frontUser.getId());
		logger.info("从Session中得到用户:" + frontUser + ",并放入map");
		map.put("frontUser", frontUser);


		Paging paging = new Paging(10);
		paging.setCurrentPage(1);

		///////////////////////////////////  获取文档  /////////////////////////////	
		//获取焦点广告图
		DocumentCriteria documentCriteria = new DocumentCriteria(ownerId);
		documentCriteria.setCurrentStatus(DocumentStatus.published.getId());
		documentCriteria.setPaging(paging);

		documentCriteria.setNodePath(new String[]{node.getPath()});
		documentCriteria.setDocumentTypeId(171005);//广告
		List<Document> promotionList = documentService.list(documentCriteria);
		logger.debug("获取到首页的广告" + (promotionList == null ? 0 : promotionList.size()) + "条");
		if(promotionList != null && promotionList.size() > 0){
			map.put("promotionList", promotionList);
		}		

		//获取发布到首页的产品公告
		documentCriteria.setDocumentTypeId(171001);//普通文档类型ID
		documentCriteria.setNodePath(new String[]{node.getPath(), "productNews"});
		List<Document> productNewsList = documentService.list(documentCriteria);
		logger.info("获取到发布到首页的产品资讯[" + productNewsList.size() + "]条");			
		if(productNewsList != null){
			map.put("productNewsList", productNewsList);
		}

		//获取发布到首页的网站公告
		documentCriteria.setDocumentTypeId(171001);//普通文档类型ID
		documentCriteria.setNodePath(node.getPath(), "siteNews");
		List<Document> siteNewsList = documentService.list(documentCriteria);
		logger.info("获取到发布到首页的网站资讯[" + siteNewsList.size() + "]条");			
		if(siteNewsList != null){
			map.put("siteNewsList", siteNewsList);
		}
		//////////////////////////获取点卡产品 ////////////////////////
		documentCriteria.setDocumentTypeId(171008);
		documentCriteria.setFlag(1);
		documentCriteria.setPaging(paging);	
		documentCriteria.setNodePath("product");
		List<Document> cardProductList = documentService.listOnPage(documentCriteria);
		List<Document> cardProductList2 = new ArrayList<Document>();
		if(cardProductList != null && cardProductList.size() > 0){
			cardProductList2 = sortingAndClone(cardProductList);	
		}	
		cardProductList = null;
		logger.info("获取到发布到首页的点卡产品[" + cardProductList2.size() + "]条");			
		map.put("cardProductList",cardProductList2);


		////////////////////////// 获取充值产品 ////////////////////////
		documentCriteria.setDocumentTypeId(171007);
		documentCriteria.setPaging(paging);		
		List<Document> chargeProductList = documentService.listOnPage(documentCriteria);

		List<Document> chargeProductList2 = new ArrayList<Document>();
		if(chargeProductList.size() > 0){
			chargeProductList2 = sortingAndClone(chargeProductList);			
		}
		chargeProductList = null;
		logger.info("获取到发布到首页的充值产品[" + chargeProductList2.size() + "]条");			
		map.put("chargeProductList",chargeProductList2);



		logger.debug("首页充值产品数量[" + chargeProductList2.size() + "], 卡密产品数量[" + cardProductList2.size() + "].");


		documentCriteria  = null;
		//获取本节点关联标签所包含的产品
		//processTags(node, map, CommonStandard.ObjectType.product.toString(), allProductList);
		return defaultTemplateLocation;
	}

	private List<Document> sortingAndClone(List<Document> productList) {
		logger.info("处理文档列表:" + productList.size());
		List<Document> productList2 = new ArrayList<Document>();
		for(Document product : productList){
			Document product2 = product.clone();
			if(product.getDocumentDataMap() != null && product.getDocumentDataMap().size() > 0) {
				for(Entry<String,DocumentData> entry : product.getDocumentDataMap().entrySet()) {
					boolean canDisplay = DisplayUtils.canDisplay(entry.getValue().getDisplayLevel(),"user");
					logger.info("检查扩展数据:key={},value={}是否能在user级别显示:{}", entry.getKey(), JsonUtils.toStringFull(entry.getValue()), canDisplay);
					if(!canDisplay) {
						product2.setExtraValue(entry.getKey(),null);
					}
				}
			} else {
				logger.info("文档:{}没有扩展数据", product.getUdid());
			}
			applyProductPrice(product2);
			productList2.add(product2);
		}		
		return productList2;
	}

	/*private void processTags(Node node, ModelMap map, String objectType, List<Product> allProductList){
		if(node == null){
			return;
		}
		if(node.getTags() == null || node.getTags().equals("")){
			return;
		}
		String[] tags = node.getTags().split(CommonStandard.tagSplit);
		if(tags == null || tags.length < 1){
			return;
		}

		for(String tagName : tags){

			List<Product> tagProductList = new ArrayList<Product>();
			for(Product product : allProductList){
				if(product.getTags() == null){
					continue;
				}
				String[] productTags = product.getTags().split(CommonStandard.tagSplit);
				for(String existTag : productTags){
					if(existTag.equals(tagName)){
						tagProductList.add(product);
						break;
					}
				}
			}
			map.put(tagName, tagProductList);
			logger.debug("节点[" + node.getName() + "]的标签:" + tagName + ",共" + tagProductList.size() + "个关联产品");			


		}
	}

	private List<Product> fetchHotProduct(List<Product> allProductList, int fetchCount){
		List<Product> productList = new ArrayList<Product>();

		Paging paging = new Paging();
		paging.setTotalResults(fetchCount);
		paging.setCurrentPage(1);
		//找出关联对象最多的30个产品类型的tag
		TagCriteria tagCriteria = new TagCriteria();
		tagCriteria.setOrders("object_count DESC");
		tagCriteria.setObjectType(CommonStandard.ObjectType.product.toString());
		tagCriteria.setPaging(paging);
		List<Tag> hotTagList = tagService.listOnPage(tagCriteria);
		logger.debug("获取到最热门的产品标签[" + (hotTagList == null ? -1 : hotTagList.size()) + "]个");
		if(hotTagList == null || hotTagList.size() < 1){
			return null;
		}

		for(Tag tag : hotTagList){
			//logger.debug("tag:" + tag.getTagName());
			for(Product product : allProductList){
				if(product.getTags() == null){
					continue;
				}
				boolean fetched = false;
				String[] tags = product.getTags().split(CommonStandard.tagSplit);
				for(String existTag : tags){
					if(existTag.equals(tag.getTagName())){
						productList.add(product);	
						fetched =true;
						break;
					}
				}
				if(fetched){
					break;
				}
			}
		}
		return productList;
	}*/

	@Override
	public String detail(HttpServletRequest request,
			HttpServletResponse response, ModelMap map)
					throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void writeExtraData(ModelMap map, User frontUser, Map<String, Object> parameter) {
		// TODO Auto-generated method stub

	}

	//对文档应用当前最新
	private void applyProductPrice(Document document) {
		priceService.generatePriceExtraData(document, PriceType.PRICE_STANDARD.name());
		priceService.generatePriceExtraData(document, PriceType.PRICE_SALE.name());

	}
}



