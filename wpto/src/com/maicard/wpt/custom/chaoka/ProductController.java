package com.maicard.wpt.custom.chaoka;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.maicard.common.base.BaseController;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.ConfigService;
import com.maicard.product.criteria.ProductCriteria;
import com.maicard.product.domain.Product;
import com.maicard.product.service.ProductService;
import com.maicard.security.service.AuthorizeService;
import com.maicard.security.service.FrontPrivilegeService;
import com.maicard.security.service.PartnerService;
import com.maicard.site.service.DocumentExtraService;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.EisError;

/**
 * 产品展示
 * @author NetSnake
 * @date 2012-12-16
 */

@Controller
@RequestMapping("/product")
public class ProductController extends BaseController{
	@Resource
	private ApplicationContextService applicationContextService;
	@Resource
	private AuthorizeService authorizeService;
	@Resource
	private ConfigService configService;
	@Resource
	private DocumentExtraService documentFetchService;
	@Resource
	private FrontPrivilegeService frontPrivilegeService;
	@Resource
	private PartnerService partnerSerivce;
	@Resource
	private ProductService productService;	



	ObjectMapper om = new ObjectMapper();




	//列出产品
	@RequestMapping(method=RequestMethod.GET)
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map, 
			@ModelAttribute("productCriteria") ProductCriteria productCriteria ) throws Exception {

		productCriteria.setMustInternalProduct(true);
		productCriteria.setProductLevel(1);

		List<Product> productList = productService.listOnPage(productCriteria);
		HashMap<String,Product> productMap = new HashMap<String,Product>();
		for(Product product : productList){
			Product product2 = product.clone();
			product2.setProductDataMap(null);
			productMap.put(product.getProductCode(), product2);
		}
		productList = null;
		map.put("products", productMap);
		return "product/list";

	}

	//列出产品明细，仅支持一级产品
	@RequestMapping(value="/{productCode}", method=RequestMethod.GET)
	public String get(HttpServletRequest request, HttpServletResponse response, ModelMap map, 
			@PathVariable("productCode") String productCode) throws Exception {

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
		Product product =  productService.select(productCode,ownerId);


		if(product == null){
			logger.error("找不到产品[" + productCode + "]");
			return "redirect:/";
		}
		if(product.getSupplyPartnerId() != 0){
			logger.error("试图访问的产品[" + product.getProductCode() + "/" + product.getProductId() + "]不是内部产品");
			return "redirect:/";			
		}
		if(product.getParentProductId() > 0){
			int parentProductId = product.getParentProductId();
			logger.info("尝试访问的是二级产品，尝试跳转到一级产品[" + product.getParentProductId() + "]");
			product = productService.select(product.getParentProductId());
			if(product == null){
				logger.error("找不到父产品[" +parentProductId + "]");
				return "redirect:/";
			}
			if(product.getSupplyPartnerId() != 0){
				logger.error("试图访问的产品[" + product.getProductCode() + "/" + product.getProductId() + "]不是内部产品");
				return "redirect:/";			
			}
		}

		map.put("product", product.clone());
		ProductCriteria productCriteria  = new ProductCriteria();
		productCriteria.setParentProductId(product.getProductId());
		productCriteria.setOrder("a.buy_money ASC");

		List<Product> childProductList = productService.list(productCriteria);
		logger.info("产品[" + product.getProductId() + "]的子产品数量:" + (childProductList == null ? 0 : childProductList.size()));
		HashMap<String, Product> subProductMap = new HashMap<String, Product>();
		for(Product childProduct : childProductList){
			Product product2 = childProduct.clone();
			product2.setProductDataMap(null);
			subProductMap.put(product2.getProductCode(),product2);
		}
		map.put("subProductMap", subProductMap);
		if(product.getRegionMap() != null && product.getRegionMap().size() > 0){
			om.setDateFormat( new SimpleDateFormat(CommonStandard.defaultDateFormat));
			map.put("regionJson", om.writeValueAsString(product.getRegionMap()));
		}

		if(product.getProductTypeId() == 123002){
			//卡密产品
			return "product/saleDetail";

		}
		return "product/buyDetail";

	}

	
}
