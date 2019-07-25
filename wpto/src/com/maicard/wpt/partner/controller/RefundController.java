package com.maicard.wpt.partner.controller;


import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.maicard.common.base.BaseController;
import com.maicard.common.service.ConfigService;
import com.maicard.money.domain.Pay;
import com.maicard.money.iface.PayProcessor;
import com.maicard.money.service.PayService;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.FrontUserService;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;

/**
 * 退款处理
 *
 *
 * @author NetSnake
 * @date 2016年8月4日
 *
 */

@Controller
@RequestMapping("/refund")
public class RefundController extends BaseController{

	@Resource
	private CertifyService certifyService;
	@Resource
	private ConfigService configService;
	@Resource
	private FrontUserService frontUserService;
	@Resource
	private PayService payService;

	private int rowsPerPage = 10;

	@PostConstruct
	public void init(){		
		rowsPerPage = configService.getIntValue(DataName.partnerRowsPerPage.toString(),0);
		if(rowsPerPage < 1){
			rowsPerPage = CommonStandard.DEFAULT_PARTNER_ROWS_PER_PAGE; 
		}
	}



	
	/**
	 * 根据pay的transactionId进行退款
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String refund(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			String transactionId, String transactionIdType) throws Exception {
		
		Pay pay = payService.select(transactionId);
		if(pay == null){
			logger.error("找不到指定的支付订单:" + transactionId);
		}
		
		PayProcessor payProcessor = payService.getProcessor(pay);
		
		payProcessor.onRefund(pay);
		return CommonStandard.partnerMessageView;
	}

}
