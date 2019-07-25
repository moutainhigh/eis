package com.maicard.wpt.custom.youbao;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maicard.common.base.BaseController;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.util.NumericUtils;
import com.maicard.money.criteria.PriceCriteria;
import com.maicard.money.domain.Price;
import com.maicard.money.service.PriceService;
import com.maicard.product.domain.Product;
import com.maicard.product.service.ProductService;
import com.maicard.security.domain.User;
import com.maicard.security.service.AuthorizeService;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.FrontUserService;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.PriceType;
import com.maicard.standard.SecurityStandard.UserStatus;
import com.maicard.standard.SecurityStandard.UserTypes;

import org.springframework.ui.ModelMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 展示优惠专题
 * 
 * 
 * @author NetSnake
 *
 */
@Controller
@RequestMapping("/promotion/")
public class PromotionController extends BaseController{

	@Resource
	private AuthorizeService authorizeService;
	@Resource
	private CertifyService certifyService;
	
	@Resource
	private FrontUserService frontUserService;
	
	@Resource
	private PriceService priceService;
	@Resource
	private ProductService productService;
	
	

	private static ObjectMapper om = new ObjectMapper();
	private static SimpleDateFormat sdf = new SimpleDateFormat(CommonStandard.defaultDateFormat);

	@PostConstruct
	public void init(){
		om.setDateFormat(sdf);
	}


	@RequestMapping(value="/{identify}/index")
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@PathVariable String identify,
			@RequestParam(value="v", required=false)String version
			) throws Exception {
		
		//////////////////////////// 标准检查流程 ///////////////////////////////////
		User frontUser =  certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());

		if(frontUser == null || frontUser.getCurrentStatus() != UserStatus.normal.getId()){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录，请先登录"));			
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
			map.put("message", new EisMessage(EisError.systemDataError.getId(),"系统异常","请尝试访问其他页面或返回首页"));
			return CommonStandard.frontMessageView;		
		}
		
		if(frontUser.getOwnerId() != ownerId){
			logger.error("用户[" + frontUser.getUuid() + "]的ownerId[" + frontUser.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(), "您尚未登录，请先登录"));			
			return CommonStandard.frontMessageView;
		}
		//////////////////////////// 标准检查流程结束 ///////////////////////////////

		identify = identify.split("_")[0];
		
		final String baseView = "promotion/" + identify + "/index";
		String view = baseView;
		if(version != null && NumericUtils.isIntNumber(version)){
			view += "_";
			view += version;
			logger.debug("链接中有版本号，使用view:" + view);			
		} else {
			String pageVersion = frontUser.getExtraValue(DataName.weixinPageVersion.toString());
			if(StringUtils.isBlank(pageVersion)){
				logger.debug("链接中未提供版本号，用户也没有扩展数据weixinPageVersion");			
			} else {
				if(NumericUtils.isIntNumber(pageVersion)){
					view = baseView + "_" + version.trim();
					logger.debug("链接中未提供版本号，但用户有扩展数据weixinPageVersion=" + pageVersion + ",使用view:" + view);
				}

			}
		}
		
		
		PriceCriteria priceCriteria = new PriceCriteria(frontUser.getOwnerId());
		priceCriteria.setIdentify(identify);
		priceCriteria.setPriceType(PriceType.PRICE_PROMOTION.toString());
		List<Price> priceList = priceService.list(priceCriteria);
		if(priceList == null || priceList.size() < 1){
			logger.debug("根据识别码[" + identify + "]找不到任何优惠价格数据");
		}
		Map<String,Product> productMap = new HashMap<String,Product>();
		for(Price price : priceList){
			Product product = productService.select((int)price.getObjectId());
			if(product == null){
				continue;
			}
			Product p = product.clone();
			p.setPrice(price);
			p.setTransactionToken(priceService.generateTransactionToken(price, frontUser.getUuid()));
			productMap.put(p.getProductCode(),p);
			
			
		}
		map.put("productMap", productMap);
		map.put("priceType", PriceType.PRICE_PROMOTION);
		map.put("identify", identify);
		return view;
		

	}

}
