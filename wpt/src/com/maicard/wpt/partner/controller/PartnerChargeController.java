package com.maicard.wpt.partner.controller;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.*;

import com.maicard.common.base.BaseController;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.GlobalOrderIdService;
import com.maicard.common.util.ContextTypeUtil;
import com.maicard.common.util.HttpUtils;
import com.maicard.common.util.QrCodeUtils;
import com.maicard.exception.UserNotFoundInRequestException;
import com.maicard.money.criteria.PayTypeCriteria;
import com.maicard.money.domain.Pay;
import com.maicard.money.domain.PayType;
import com.maicard.money.service.MoneyService;
import com.maicard.money.service.PayMethodService;
import com.maicard.money.service.PayService;
import com.maicard.money.service.PayTypeService;
import com.maicard.security.domain.User;
import com.maicard.security.service.AuthorizeService;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.FrontUserService;
import com.maicard.security.service.PartnerService;
import com.maicard.site.service.DocumentService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.ContextType;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.Operate;
import com.maicard.standard.SecurityStandard.UserTypes;
import com.maicard.standard.TransactionStandard.TransactionStatus;

/**
 * 向我方系统中的用户帐号进行充值
 * 支持向前端用户和partner用户的账户充值
 * 
 *
 *
 * @author NetSnake
 * @date 2016年9月17日
 *
 */

@RequestMapping("/partnerCharge")
@Controller
public class PartnerChargeController extends BaseController{

	@Resource
	private AuthorizeService authorizeService;
	@Resource
	private ApplicationContextService applicationContextService;
	@Resource
	private GlobalOrderIdService globalOrderIdService;
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
	
	private String tempUploadPath = null;

	
	@PostConstruct
	public void init(){
		tempUploadPath = applicationContextService.getDataDir() + File.separator + CommonStandard.EXTRA_DATA_TEMP;
	}
	
	
	
	
	
	@RequestMapping(method = RequestMethod.GET)
	public String partnerIndex(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		String view = "charge/partnerIndex";
		map.put("title", "账户充值");
		logger.debug("显示账户充值页面");
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return view;		
		}
		
		PayTypeCriteria payTypeCriteria = new PayTypeCriteria(ownerId);
		payTypeCriteria.setCurrentStatus(BasicStatus.normal.id);
		List<PayType> payTypeList = payTypeService.list(payTypeCriteria);
		
		map.put("payTypeList", payTypeList);
		return view;
	}
	
	//查询指定交易ID的支付状态
	@RequestMapping(value="/get/{transactionId}",method=RequestMethod.GET )
	public String query(HttpServletRequest request, HttpServletResponse response, ModelMap map, @PathVariable("transactionId") String transactionId) throws Exception {

		User user = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
		if(user == null){
			user = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		}
		logger.debug("查询支付订单[" + transactionId + "]");
		Pay pay = payService.select(transactionId);
		if(pay == null){
			logger.warn("找不到指定的支付订单[" + transactionId + "]");

			map.put("message", new EisMessage(EisError.BILL_NOT_EXIST.getId(), "订单不存在"));
			return CommonStandard.frontMessageView;

		} 
		logger.debug("查询到支付订单[" + transactionId + "]是:" + pay);
		if(pay.getPayFromAccount() != user.getUuid()){
			logger.warn("指定的支付订单[" + transactionId + "]属于用户" + pay.getPayFromAccount() + "，不是当前用户" + user.getUuid());
			map.put("message", new EisMessage(EisError.BILL_NOT_EXIST.getId(), "订单不存在"));
			return CommonStandard.frontMessageView;

		}
		if (pay.getCurrentStatus() == TransactionStatus.success.id) {
			map.put("message", new EisMessage(TransactionStatus.success.id, "充值成功"));
		}
		map.put("pay", pay);
		return CommonStandard.frontMessageView;
	}

	//提交一笔支付
	@RequestMapping( method=RequestMethod.POST)
	public String post(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		User user = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());

		if(user == null){
			user = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		}

		if(user == null){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录，请先登录后再进行支付"));
			return CommonStandard.frontMessageView;
		}
		

		int payTypeId = ServletRequestUtils.getIntParameter(request, "payTypeId", 0);
		if(payTypeId <= 0){
			logger.error("未选择支付方式");
			map.put("message", new EisMessage(EisError.dataError.getId(), "请选择支付方式"));
			return returned(user);
		}

		Pay pay = new Pay(user.getOwnerId());
		pay.setPayToAccount(user.getUuid());
		pay.setPayFromAccountType(user.getUserTypeId());
		pay.setPayFromAccount(user.getUuid());
		pay.setPayToAccountType(user.getUserTypeId());
		pay.setName("充值");
		pay.setContextType(ContextTypeUtil.getContextType(request));
		pay.setExtraValue("fromAccountName", user.getNickName() == null ? user.getUsername() : user.getNickName());
		long roleId = ServletRequestUtils.getLongParameter(request, "roleId", 0);
		if(roleId > 0){
			pay.setExtraValue("roleId", String.valueOf(roleId));
		}
		pay.setExtraValue("internal", "true");
		pay.setPayTypeId(payTypeId);
		float faceMoney = ServletRequestUtils.getFloatParameter(request, "faceMoney", 0);
		if(faceMoney <= 0){
			logger.error("未选择支付金额");
			map.put("message", new EisMessage(EisError.dataError.getId(), "请选择支付金额"));
			return returned(user);
		}
		pay.setFaceMoney(faceMoney);
		Map<String,String>requestData = HttpUtils.getRequestDataMap(request);
		pay.setParameter(requestData);

		String payNotifyTemplate = configService.getValue(DataName.payNotifyUrl.toString(), pay.getOwnerId());
		if(payNotifyTemplate == null){
			payNotifyTemplate = CommonStandard.DEFAULT_PAY_NOTIFY_TEMPLATE;
		}
		payNotifyTemplate = payNotifyTemplate.replaceAll("\\$\\{hostUrl\\}", HttpUtils.generateUrlPrefix(request));
		pay.setNotifyUrl(payNotifyTemplate);
		EisMessage payResult = payService.begin(pay);
		map.put("transactionId", pay.getTransactionId());
		if(pay.getContextType() != null && pay.getContextType().equals(ContextType.PC.toString())){
			if(payResult.getOperateCode() == Operate.scanCode.getId() && payResult.getContent() != null){
				//需要把对应的连接转为图片
			
				String qrCodeUrl = null;
				if(payResult.getMessage() != null && payResult.getMessage().indexOf("://") > 0) {
					qrCodeUrl = payResult.getMessage();
				} else {
					qrCodeUrl = payResult.getContent();
				}
				String uuid = UUID.randomUUID().toString();
				String imageName = uuid + ".png";
				String imageFileName = tempUploadPath + "/" + imageName;
				FileUtils.forceMkdir(new File(tempUploadPath));
				QrCodeUtils.encode(qrCodeUrl, null, imageFileName);
				payResult.setMessage("/temp/" + imageName);
				logger.debug("将连接[" + qrCodeUrl + "]存储为图片:" + imageFileName + ",访问链接是:" + payResult.getMessage());
				}
		}
		map.put("message", payResult);

		return returned(user);

	}
	public String returned(User user){
		if (user.getUserTypeId() == UserTypes.frontUser.getId()) {
			return CommonStandard.frontMessageView;	
		}
		return CommonStandard.partnerMessageView;
	}


}
