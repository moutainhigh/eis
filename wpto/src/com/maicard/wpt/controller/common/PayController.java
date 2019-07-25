package com.maicard.wpt.controller.common;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.maicard.common.base.BaseController;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.GlobalOrderIdService;
import com.maicard.money.domain.Pay;
import com.maicard.money.service.MoneyService;
import com.maicard.money.service.PayMethodService;
import com.maicard.money.service.PayService;
import com.maicard.money.service.PayTypeService;
import com.maicard.security.service.AuthorizeService;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.FrontUserService;
import com.maicard.security.service.PartnerService;
import com.maicard.site.service.DocumentService;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.EisError;
import com.maicard.standard.TransactionStandard.TransactionStatus;

/**
 * 向我方系统中的用户帐号进行充值
 * 支持向前端用户和partner用户的账户充值
 *
 *
 * @author NetSnake
 * @date 2016年9月17日
 *
 */
@Controller
@RequestMapping("/pay")
public class PayController extends BaseController{

	@Resource
	private AuthorizeService authorizeService;
	@Resource
	private ApplicationContextService applicationContextService;
	@Resource
	private GlobalOrderIdService globalOrderIdService;
	@Resource
	private ConfigService configService;
	@Resource
	private CertifyService certifyService;
	@Resource
	private DocumentService documentService;
	@Resource
	private FrontUserService frontUserService;

	@Resource
	private MoneyService moneyService;
	@Resource
	private PartnerService partnerService;
	@Resource
	private PayService payService;
	@Resource
	private PayMethodService payMethodService;
	@Resource
	private PayTypeService payTypeService;

	//查询指定交易ID的支付状态
	@RequestMapping(value="/query/{transactionId}",method=RequestMethod.GET )
	public String query(HttpServletRequest request, HttpServletResponse response, ModelMap map, @PathVariable("transactionId") String transactionId) throws Exception {

		logger.debug("查询支付订单[" + transactionId + "]");
		Pay pay = payService.select(transactionId);
		if(pay == null){
			map.put("message", new EisMessage(EisError.BILL_NOT_EXIST.getId(), "订单不存在"));
		} else {
			EisMessage msg = new EisMessage();
			msg.setOperateCode(pay.getCurrentStatus());
			if(msg.getOperateCode() == TransactionStatus.success.getId()){
				msg.setMessage("交易成功");
			}
			if(msg.getOperateCode() == TransactionStatus.failed.getId()){
				msg.setMessage("交易失败");
			}

			map.put("message", msg);
		}
		return CommonStandard.frontMessageView;
	}

	


}
