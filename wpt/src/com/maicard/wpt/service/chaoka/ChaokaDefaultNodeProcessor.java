package com.maicard.wpt.service.chaoka;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;

import com.maicard.common.util.PreviewToken;
import com.maicard.security.domain.User;
import com.maicard.site.domain.Document;
import com.maicard.site.domain.Node;
import com.maicard.site.service.NodeProcessor;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.PriceType;
import com.maicard.standard.SecurityStandard.UserTypes;
import com.maicard.standard.SiteStandard.DocumentStatus;
import com.maicard.wpt.np.WptDefaultNodeProcessor;



@Service
public class ChaokaDefaultNodeProcessor  extends WptDefaultNodeProcessor  implements NodeProcessor{





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
		if(map.get("documentCode") != null) {
			documentCode = map.get("documentCode").toString();
		}

		User frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());


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
			document.setExtraValue(DataName.productSmallImage.toString(),productSmallImage);
		} else{
			document.setExtraValue(DataName.productSmallImage.toString(), request.getScheme() + "://"+request.getServerName()+"/style/images/default_product.jpg");
		}

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

		if(document.getDocumentTypeId() == 171007 || document.getDocumentTypeId() == 171008) {
			this.applyProductPrice(document);
		}
		logger.debug("当前文档的udid为"+document.getUdid());
		document.setExtraValue("productId", String.valueOf(document.getUdid()));
		map.put("document",document);
		map.put("node",node);
		

		return getTemplateLocation(document, node, ownerId);

	}

	//对文档应用当前最新
	private void applyProductPrice(Document document) {
		priceService.generatePriceExtraData(document, PriceType.PRICE_STANDARD.name());
		priceService.generatePriceExtraData(document, PriceType.PRICE_SALE.name());
		
	}









}
