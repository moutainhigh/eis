package com.maicard.wpt.custom.youbao;


import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import com.maicard.common.base.BaseController;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * 网站关于等
 * @author xiangqiaogao
 *
 */

@Controller
@RequestMapping("/about")
public class AboutController  extends BaseController{	


	@PostConstruct
	public void init(){

	}
	/**
	 *  关于我们
	 *  @author xiangqiaogao
	 *  @date 2017年12月8日
	 * 	@param request
	 * 	@param response
	 * 	@param map
	 * 	@return
	 * 	@throws Exception
	 */
	@RequestMapping(value="/aboutUs")
	public String aboutUs(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		return "website/aboutUs";
	}
	/**
	 *  联系我们
	 *  @author xiangqiaogao
	 *  @date 2017年12月8日
	 * 	@param request
	 * 	@param response
	 * 	@param map
	 * 	@return
	 * 	@throws Exception
	 */
	@RequestMapping(value="/contactUs")
	public String contactUs(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		return "website/contactUs";
	}
	/**
	 *  搜索
	 *  @author xiangqiaogao
	 *  @date 2017年12月8日
	 * 	@param request
	 * 	@param response
	 * 	@param map
	 * 	@return
	 * 	@throws Exception
	 */
	@RequestMapping(value="/search")
	public String search(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		return "website/search";
	}
	/**
	 *  商务合作
	 *  @author xiangqiaogao
	 *  @date 2017年12月8日
	 * 	@param request
	 * 	@param response
	 * 	@param map
	 * 	@return
	 * 	@throws Exception
	 */
	@RequestMapping(value="/cooperation")
	public String cooperation(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		return "website/cooperation";
	}
	
	@RequestMapping(value="/termOfService")
	public String termOfService(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		return "website/termOfService";
	}
	
	@RequestMapping(value="/purchaseGuide")
	public String purchaseGuide(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		return "website/purchaseGuide";
	}
	@RequestMapping(value="/book")
	public String specialShops(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		return "website/book";
	}
}


