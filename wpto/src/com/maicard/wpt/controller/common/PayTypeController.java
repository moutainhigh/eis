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
import com.maicard.common.util.Paging;
import com.maicard.money.criteria.PayTypeCriteria;
import com.maicard.money.domain.PayType;
import com.maicard.money.service.PayTypeService;
import com.maicard.security.service.AuthorizeService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.EisError;

/**
 * 向用户提供当前可用的支付方式
 * 
 *
 *
 * @author NetSnake
 * @date 2016年11月4日
 *
 */
@Controller
@RequestMapping("/payType")
public class PayTypeController extends BaseController{


	@Resource
	private PayTypeService payTypeService;
	@Resource
	private AuthorizeService authorizeService;


	
	@RequestMapping(method= RequestMethod.GET)
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map, PayTypeCriteria payTypeCriteria) throws Exception {
		long ownerId = NumericUtils.parseLong(map.get("ownerId"));

		if (ownerId < 1) {
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(), "系统异常", "请尝试访问其他页面或返回首页"));
			return CommonStandard.partnerMessageView;
		}
		
		
		final String view = "payType/index";
		payTypeCriteria.setOwnerId(ownerId);
		payTypeCriteria.setCurrentStatus(BasicStatus.normal.id);

		int totalRows = payTypeService.count(payTypeCriteria);
		map.put("total", totalRows);

		if(totalRows < 1){
			logger.debug("当前返回的数据数量是0");
			return view;
		}

		int rows = ServletRequestUtils.getIntParameter(request, "rows",
				ROW_PER_PAGE);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);
		Paging paging = new Paging(rows);
		payTypeCriteria.setPaging(paging);
		payTypeCriteria.getPaging().setCurrentPage(page);
		List<PayType> payTypeList = payTypeService.listOnPage(payTypeCriteria);

		map.put("payTypeList",payTypeList);
		return view;
	}


}
