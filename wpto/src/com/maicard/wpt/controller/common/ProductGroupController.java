package com.maicard.wpt.controller.common;

import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.maicard.common.base.BaseController;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.util.NumericUtils;
import com.maicard.product.criteria.ProductGroupCriteria;
import com.maicard.product.domain.ProductGroup;
import com.maicard.product.service.ProductGroupService;
import com.maicard.security.service.AuthorizeService;
import com.maicard.site.domain.Document;
import com.maicard.site.service.DocumentService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.EisError;
import com.maicard.standard.ObjectType;

/**
 * 根据用户提交的商品属性，确定对应的商品
 * 例如，手机商品，提交了某款手机的红色款，将指向某个产品
 * 
 * @author GHOST
 * @date 2018-11-25
 *
 */
@Controller
@RequestMapping("/productGroup")
public class ProductGroupController extends BaseController{
	
	@Resource
	private DocumentService documentService;

	@Resource
	private ProductGroupService productGroupService;
	@Resource
	private AuthorizeService authorizeService;


	
	
	@RequestMapping(method= RequestMethod.GET)
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		long ownerId = NumericUtils.parseLong(map.get("ownerId"));

		if (ownerId < 1) {
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(), "系统异常", "请尝试访问其他页面或返回首页"));
			return CommonStandard.partnerMessageView;
		}
		
		
		final String view = CommonStandard.frontMessageView;
		
		String objectType = ServletRequestUtils.getRequiredStringParameter(request, "objectType");
		long objectId = ServletRequestUtils.getLongParameter(request, "objectId");
		String groupValue = ServletRequestUtils.getRequiredStringParameter(request, "groupValue");
		ProductGroupCriteria productGroupCriteria = new ProductGroupCriteria();
		productGroupCriteria.setOwnerId(ownerId);
		productGroupCriteria.setCurrentStatus(BasicStatus.normal.id);
		productGroupCriteria.setObjectType(objectType);
		productGroupCriteria.setObjectId(objectId);
		productGroupCriteria.setGroupValue(groupValue);
		
		List<ProductGroup> productGroupList = productGroupService.listNextGroup(productGroupCriteria);
		logger.info("返回的产品分组数量是:{}",productGroupList.size());
		if(productGroupList.size() > 0) {
			for(ProductGroup pg : productGroupList) {
				//logger.info("pg=" + JSON.toJSONString(pg));
				if(pg.getObjectType().equalsIgnoreCase(ObjectType.document.name())) {
					Document document = documentService.select(Integer.parseInt(pg.getGroupTarget()));
					if(document == null) {
						logger.error("找不到产品分组:{}中对应的文档:{}", pg.getId(), pg.getGroupTarget());
					} else {
						pg.setExtraValue("viewUrl", document.getViewUrl());
						logger.info("产品分组:{}中对应的文档:{}浏览路径是:{}", pg.getId(), pg.getGroupTarget(), document.getViewUrl());
					}
				}
			}
		}
		map.put("productGroupList",productGroupList);
		return view;
	}


}
