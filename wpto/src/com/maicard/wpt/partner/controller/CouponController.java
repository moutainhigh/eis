package com.maicard.wpt.partner.controller;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.maicard.common.base.BaseController;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.util.NumericUtils;
import com.maicard.common.util.Paging;
import com.maicard.common.util.PagingUtils;
import com.maicard.common.util.StatusUtils;
import com.maicard.exception.UserNotFoundInRequestException;
import com.maicard.money.criteria.CouponCriteria;
import com.maicard.money.domain.Coupon;
import com.maicard.money.domain.CouponModel;
import com.maicard.money.domain.Money;
import com.maicard.money.service.CouponModelService;
import com.maicard.money.service.CouponProcessor;
import com.maicard.money.service.CouponService;
import com.maicard.money.service.MoneyService;
import com.maicard.money.util.MoneyUtils;
import com.maicard.security.domain.User;
import com.maicard.security.service.AuthorizeService;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.FrontUserService;
import com.maicard.security.service.PartnerService;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.EisError;
import com.maicard.standard.OperateResult;
import com.maicard.standard.SecurityStandard.UserTypes;

/**
 * 核销领取码
 * 
 * @author Pengzhenggang
 * @data 2016-6-6
 */
@Controller
@RequestMapping("/coupon")
public class CouponController extends BaseController {
	
	
	@Resource
	private ApplicationContextService applicationContextService;
	@Resource
	private AuthorizeService authorizeService;
	
	@Resource
	private CertifyService certifyService;
	@Resource
	private CouponService couponService;
	
	@Resource
	private MoneyService moneyService;
	
	@Resource
	private CouponModelService couponModelService;
	
	@Resource
	private PartnerService partnerService;
	@Resource
	private FrontUserService frontUserService;
	
	/**
	 * 默认界面
	 * 列出自身拥有的优惠券/码
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map, CouponCriteria couponCriteria){
		final String view = "common/coupon/index";
		//////////////////////////// 标准检查流程 ///////////////////////////////////
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if (partner == null) {
			// 无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		long ownerId = NumericUtils.parseLong(map.get("ownerId"));
		
		if (ownerId < 1) {
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(), "系统异常", "请尝试访问其他页面或返回首页"));
			return CommonStandard.partnerMessageView;
		}

		if (partner.getOwnerId() != ownerId) {
			logger.error("用户[" + partner.getUuid() + "]的ownerId[" + partner.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(), "您尚未登录，请先登录"));
			return CommonStandard.partnerMessageView;
		}
		
		boolean isPlatformGenericPartner = authorizeService.isPlatformGenericPartner(partner);
		logger.debug("当前合作伙伴[" + partner.getUuid() + "/" + partner.getUsername() + "]" + (isPlatformGenericPartner ? "是" : "不是") + "一般性合作伙伴");
		if(!isPlatformGenericPartner){
			try {
				partnerService.setSubPartner(couponCriteria, partner);
			} catch (Exception e) {
				e.printStackTrace();
				map.put("message", new EisMessage(EisError.ACCESS_DENY.getId(), "认证错误"));
				return CommonStandard.partnerMessageView;
			}
		}
		//////////////////////////// 标准检查流程结束 ///////////////////////////////
		map.put("title", "兑换券管理");
		couponCriteria.setOwnerId(partner.getOwnerId());
		couponCriteria.setInviters(partner.getUuid());
		int rows = ServletRequestUtils.getIntParameter(request, "rows", CommonStandard.DEFAULT_PARTNER_ROWS_PER_PAGE);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);
		Paging paging = new Paging(rows);
		couponCriteria.setPaging(paging);
		couponCriteria.getPaging().setCurrentPage(page);
		
		String paramId = request.getParameter("id");
		if (paramId != null || paramId != "") {
			if (NumericUtils.isNumeric(paramId)) {
				logger.debug("查找的用户是：" + paramId);
				long uuid = Integer.parseInt(paramId);
				couponCriteria.setUuid(uuid);
			}
			
		}
		
		int totalRows = couponService.count(couponCriteria);
		if (totalRows < 1) {
			logger.debug("当前返回的数据行数是0");
		}
		List<Coupon> couponList = couponService.listOnPage(couponCriteria);
		//计算并放入分页
		if (couponCriteria.getPaging() != null) {
			map.put("contentPaging", PagingUtils.generateContentPaging(totalRows, couponCriteria.getPaging().getMaxResults(), couponCriteria.getPaging().getCurrentPage()));
		}
		
		for (Coupon coupon : couponList) {
			User frontUser = frontUserService.select(coupon.getUuid());
			HashMap<String, String> nickName = new HashMap<String, String>();
			if (frontUser != null) {
				nickName.put("nickName", frontUser.getNickName());
				coupon.setData(nickName);
			}
			
			User user = partnerService.select(coupon.getInviter());
			if (user != null) {
				nickName.put("inviterName", user.getNickName());
				coupon.setData(nickName);
				Money money = moneyService.select(user.getUuid(), user.getOwnerId());
				if(money != null){
					map.put("money", money);
				}
			} else {
				nickName.put("inviterName", coupon.getInviter()+"");
				coupon.setData(nickName);
			}
		}
		map.put("statusList",StatusUtils.getAllStatusValue(new String[]{}));
		map.put("total", totalRows);
		map.put("rows", couponList);
		return view;
	}

	/**
	 * 显示一个核销界面
	 */
	@RequestMapping(value = "/use", method = RequestMethod.GET)
	public String use(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		logger.debug("XXXXXXXXXXX 显示一个核销界面 XXXXXXXXXXX");
		final String view = "common/coupon/use";
		//////////////////////////// 标准检查流程 ///////////////////////////////////
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if (partner == null) {
			// 无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		long ownerId = 0;
		try {
			ownerId = (long) map.get("ownerId");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if (ownerId < 1) {
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(), "系统异常", "请尝试访问其他页面或返回首页"));
			return CommonStandard.partnerMessageView;
		}

		if (partner.getOwnerId() != ownerId) {
			logger.error("用户[" + partner.getUuid() + "]的ownerId[" + partner.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(), "您尚未登录，请先登录"));
			return CommonStandard.partnerMessageView;
		}
		//////////////////////////// 标准检查流程结束 ///////////////////////////////

		return view;
	}

	@RequestMapping(value = "/get", method = RequestMethod.POST)
	public String commit(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@RequestParam("receiveCode") String receiveCode) throws Exception {
		logger.debug("XXXXXXXXXX 核销领取码！ XXXXXXXXXX");

		//////////////////////////// 标准检查流程 ///////////////////////////////////
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if (partner == null) {
			// 无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		long ownerId = 0;
		try {
			ownerId = (long) map.get("ownerId");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if (ownerId < 1) {
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(), "系统异常", "请尝试访问其他页面或返回首页"));
			return CommonStandard.partnerMessageView;
		}

		if (partner.getOwnerId() != ownerId) {
			logger.error("用户[" + partner.getUuid() + "]的ownerId[" + partner.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(), "您尚未登录，请先登录"));
			return CommonStandard.partnerMessageView;
		}
		//////////////////////////// 标准检查流程结束 ///////////////////////////////

		if (receiveCode == null || receiveCode.length() < 1) {
			logger.error("请输入领取码！");
			map.put("message", new EisMessage(EisError.dataError.getId(), "您输入的领取码为空，请重新输入领取码！"));
			return CommonStandard.partnerMessageView;
		}
		String subSerialNumber = receiveCode.substring(0, 4);
		String subPassword = receiveCode.substring(4, 8);
		logger.debug("提交的序列号 ：" + subSerialNumber + " 提交密码 ：" + subPassword);

		CouponCriteria couponCriteria = new CouponCriteria();
		couponCriteria.setCouponSerialNumber(subSerialNumber);
		couponCriteria.setCouponPassword(subPassword);
		couponCriteria.setOwnerId(ownerId);
		List<Coupon> couponList = couponService.list(couponCriteria);
		if (couponList == null || couponList.size() < 1) {
			logger.error("您输入的领取码无效！");
			map.put("message", new EisMessage(OperateResult.failed.getId(), "此领取码无效!"));
			return CommonStandard.partnerMessageView;
		}
		Coupon coupon = couponList.get(0);
		String processorName = coupon.getProcessor();
		if(StringUtils.isBlank(processorName)){
			logger.error("卡券产品:" + coupon.getCouponCode() + "没有指定对应的处理器");
			map.put("message", new EisMessage(EisError.dataError.getId(), "卡券产品配置错误"));
			return CommonStandard.partnerMessageView;
		}
		CouponProcessor couponProcessor = applicationContextService.getBeanGeneric(processorName);
		if(couponProcessor == null){
			logger.error("找不到卡券产品:" + coupon.getCouponCode() + "指定处理器:" + processorName);
			map.put("message", new EisMessage(EisError.dataError.getId(), "卡券产品配置错误"));
			return CommonStandard.partnerMessageView;
		}
		int result = couponProcessor.consume(coupon);
		
		if (result == Coupon.STATUS_USED) {
			map.put("message", new EisMessage(result, "此领取码已使用!"));
		} else if (result == OperateResult.success.getId()) {
			long couponModelId = coupon.getCouponModelId();
			CouponModel couponModel = couponModelService.select(couponModelId);
			String couponModelName = couponModel.getCouponModelName();
			map.put("message", new EisMessage(result, "恭喜您抽中" + couponModelName + "！"));
		} else {
			map.put("message", new EisMessage(result, "此领取码无效!"));
		}
		return CommonStandard.partnerMessageView;
	}
	
	/**
	 * 使用账户资金购买指定代码和数量的优惠券
	 * 这些优惠券将被对应的couponProcessor的fetch处理
	 */
	@RequestMapping(value = "/plus", method = RequestMethod.POST)
	public String buy(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			String couponCode) throws Exception {

		//////////////////////////// 标准检查流程 ///////////////////////////////////
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if (partner == null) {
			// 无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		long ownerId = NumericUtils.parseLong(map.get("ownerId"));
		if (ownerId < 1) {
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(), "系统异常", "请尝试访问其他页面或返回首页"));
			return CommonStandard.partnerMessageView;
		}

		if (partner.getOwnerId() != ownerId) {
			logger.error("用户[" + partner.getUuid() + "]的ownerId[" + partner.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(), "您尚未登录，请先登录"));
			return CommonStandard.partnerMessageView;
		}
		//////////////////////////// 标准检查流程结束 ///////////////////////////////

		int fetchCount = ServletRequestUtils.getIntParameter(request, "count", 0);
		CouponModel couponModel = couponModelService.select(couponCode, ownerId);
		
		if (couponModel == null) {
			logger.error("找不到指定的卡券产品:" + couponCode + ",ownerId=" + ownerId);
			map.put("message", new EisMessage(EisError.dataError.getId(), "找不到指定的卡券产品"));
			return CommonStandard.partnerMessageView;
		}
		Money money = couponModel.getCostMoney();
		if(money == null || money.isAllZero()){
			logger.error("卡券产品:" + couponCode + "未配置成本价");
			map.put("message", new EisMessage(EisError.dataError.getId(), "卡券产品配置错误"));
			return CommonStandard.partnerMessageView;		
		}
		// 购买多少个，需要多少钱；购买花的钱
		Money minusMoney = MoneyUtils.doubling(money, fetchCount);
		minusMoney.setUuid(partner.getUuid());
		minusMoney.setOwnerId(ownerId);
		//扣除购买所花费的资金
		EisMessage minusResult = moneyService.minus(minusMoney);
		if(minusResult.getOperateCode() != OperateResult.success.id){
			logger.error("无法扣除资金,扣除资金的返回结果是:" + minusResult);
			map.put("message", minusResult);
			return CommonStandard.partnerMessageView;
		}
		String processorName = couponModel.getProcessor();
		if(StringUtils.isBlank(processorName)){
			logger.error("卡券产品:" + couponCode + "没有指定对应的处理器");
			map.put("message", new EisMessage(EisError.dataError.getId(), "卡券产品配置错误"));
			return CommonStandard.partnerMessageView;
		}
		CouponProcessor couponProcessor = applicationContextService.getBeanGeneric(processorName);
		if(couponProcessor == null){
			logger.error("找不到卡券产品:" + couponCode + "指定处理器:" + processorName);
			map.put("message", new EisMessage(EisError.dataError.getId(), "卡券产品配置错误"));
			return CommonStandard.partnerMessageView;
		}
		CouponCriteria couponCriteria = new CouponCriteria(ownerId);
		couponCriteria.setCouponCode(couponCode);
		couponCriteria.setFetchCount(fetchCount);
		couponCriteria.setUuid(partner.getUuid());
		//领取结果
		EisMessage fetchResult = couponProcessor.fetch(couponCriteria);
		logger.debug("返回获取卡券的结果是:" + fetchResult);
		if (fetchResult.getOperateCode() != OperateResult.success.getId()) {
			logger.debug("领取卡券产品:" + couponCode + "失败");
			map.put("message", new EisMessage(fetchResult.getOperateCode(), fetchResult.getMessage()));
			return CommonStandard.partnerMessageView;
		}
		List<Coupon> couponList = fetchResult.getAttachmentData("couponList");
		map.put("message", fetchResult);
		map.put("couponList", couponList);
		return CommonStandard.partnerMessageView;
	}
}
