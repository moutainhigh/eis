package com.maicard.wpt.custom.youbao;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.ui.ModelMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maicard.annotation.IgnoreLoginCheck;
import com.maicard.common.base.BaseController;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.util.ClassUtils;
import com.maicard.common.util.Crypt;
import com.maicard.common.util.HttpUtils;
import com.maicard.common.util.JsonUtils;
import com.maicard.common.util.NumericUtils;
import com.maicard.common.util.Paging;
import com.maicard.http.HttpClientPoolV3;
import com.maicard.money.criteria.CouponCriteria;
import com.maicard.money.criteria.CouponModelCriteria;
import com.maicard.money.domain.Coupon;
import com.maicard.money.domain.CouponModel;
import com.maicard.money.service.CouponModelService;
import com.maicard.money.service.CouponProcessor;
import com.maicard.money.service.CouponService;
import com.maicard.security.domain.User;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.FrontUserService;
import com.maicard.security.service.PartnerService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.OperateResult;
import com.maicard.standard.SecurityStandard.UserStatus;
import com.maicard.standard.SecurityStandard.UserTypes;
import com.maicard.standard.TransactionStandard.TransactionStatus;
import com.maicard.wpt.service.WeixinService;

/**
 * 获取系统或外部的可兑换优惠券列表
 *
 *
 * @author NetSnake
 * @date 2015年11月14日
 *
 */
@Controller
@RequestMapping("/coupon")
public class CouponController extends BaseController {

	@Resource
	private CouponService couponService;
	@Resource
	private CouponModelService couponModelService;
	@Resource
	private CertifyService certifyService;
	@Resource
	private FrontUserService frontUserService;
	@Resource
	private PartnerService partnerService;
	@Resource
	private WeixinService weixinService;

	private final String DEFAULT_COUPON_PROCESSOR = "randomCouponProcessor";

	private static final String host = "api.weixin.qq.com";
	private static final int port = 443;
	@Resource
	private ApplicationContextService applicationContextService;

	private ObjectMapper om = new ObjectMapper();
	private SimpleDateFormat sdf = new SimpleDateFormat(
			CommonStandard.defaultDateFormat);

	@PostConstruct
	public void init() {
		om.setDateFormat(sdf);
	}

	// 获取并列出存在的积分兑换活动
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(HttpServletRequest request,
			HttpServletResponse response, ModelMap map) {
		User frontUser = null;
		try {
			frontUser = certifyService.getLoginedUser(request, response,
					UserTypes.frontUser.getId());
			// logger.info("从Session中得到用户:" + frontUser.getUuid());
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (frontUser == null) {
			map.put("message",
					new EisMessage(EisError.userNotFoundInSession.getId(),
							"您尚未登录，请先登录"));
			return CommonStandard.frontMessageView;
		}

		return _list(HttpUtils.getRequestDataMap(request), frontUser, map);
	}

	// 获取可能的积分兑换活动，加密形式
	@RequestMapping(value = "/listCrypt", method = RequestMethod.GET)
	public String listCrypt(HttpServletRequest request,
			HttpServletResponse response, ModelMap map,
			@RequestParam("uuid") long uuid,
			@RequestParam("data") String cryptedData) {
		long ownerId = (long) map.get("ownerId");
		if (ownerId < 1) {
			logger.error("系统会话中没有ownerId数据");
			return CommonStandard.frontMessageView;
		}
		User frontUser = frontUserService.select(uuid);
		if (frontUser == null) {
			logger.warn("找不到用户[" + uuid + "]");
			map.put("message",
					new EisMessage(EisError.userNotFoundInSession.getId(),
							"登录失败，请检查您的帐号密码是否正确"));
			return CommonStandard.frontMessageView;
		}
		if (frontUser.getOwnerId() != ownerId) {
			logger.error("用户[" + uuid + "]的ownerid[" + frontUser.getOwnerId()
					+ "]与系统会话中的[" + ownerId + "]不一致");
			map.put("message",
					new EisMessage(EisError.userNotFoundInSystem.getId(),
							"找不到指定的用户"));
			return CommonStandard.frontMessageView;
		}
		if (frontUser.getCurrentStatus() != UserStatus.normal.getId()) {
			logger.warn("用户[" + uuid + "]状态异常:" + frontUser.getCurrentStatus());
			map.put("message",
					new EisMessage(EisError.userNotFoundInSession.getId(),
							"登录失败，请检查您的帐号密码是否正确"));
			return CommonStandard.frontMessageView;
		}

		String cryptKey = null;
		try {
			cryptKey = frontUser.getUserConfigMap()
					.get(DataName.supplierLoginKey.toString()).getDataValue();
		} catch (Exception e) {
			logger.error("在查找用户配置数据时发生异常:" + e.getMessage());
			e.printStackTrace();
		}
		if (StringUtils.isBlank(cryptKey)) {
			logger.warn("找不到用户[" + uuid + "]的登录密钥");
			map.put("message",
					new EisMessage(EisError.userNotFoundInSession.getId(),
							"登录失败，请检查您的帐号密码是否正确"));
			return CommonStandard.frontMessageView;
		}
		logger.warn("用户[" + uuid + "]的登陆密钥是:" + cryptKey);

		Crypt crypt = new Crypt();
		crypt.setAesKey(cryptKey);
		String clearData = null;
		try {
			clearData = crypt.aesDecrypt(cryptedData);
		} catch (Exception e) {
			logger.error("在尝试解密用户请求数据时发生异常:" + e.getMessage());
		}
		if (StringUtils.isBlank(clearData)) {
			logger.warn("无法解密用户[" + uuid + "]数据[密钥:" + cryptKey + "]:"
					+ cryptedData);
			map.put("message",
					new EisMessage(EisError.userNotFoundInSession.getId(),
							"登录失败，请检查您的帐号密码是否正确"));
			return CommonStandard.frontMessageView;
		}
		String view = _list(HttpUtils.getRequestDataMap(request), frontUser,
				map);
		return view;

	}

	private String _list(Map<String, String> requestDataMap, User frontUser,
			ModelMap map) {
		long ownerId = (long) map.get("ownerId");
		if (ownerId < 1) {
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),
					"系统异常"));
			return CommonStandard.frontMessageView;
		}
		final String view = "coupon/list";

		CouponModelCriteria couponModelCriteria = new CouponCriteria();
		ClassUtils.bindBeanFromMap(couponModelCriteria, requestDataMap);
		couponModelCriteria.setUuid(frontUser.getUuid());
		couponModelCriteria.setOwnerId(frontUser.getOwnerId());
		couponModelCriteria.setCurrentStatus(BasicStatus.normal.getId());

		// 推广端或微信传递过来的识别编码
		String identify = null;
		/*
		 * 对识别编码进行分段，以支持多种不同的活动 例如微信端摇一摇过来的连接，可以在微信后台配置上identify=wmyyy_62_1
		 * 这时候，我们可以配置不同的活动对应两级识别， 如wmyyy是一级识别码，总活动，识别为物美摇一摇活动，
		 * 而wmyyy_62是二级识别码，可以精准到门店
		 * 
		 * @Auth NetSnake
		 * 
		 * @Date 2015-12-07
		 */
		String firstIdentify = null;
		String secIdentify = null;
		if (requestDataMap.containsKey("identify")) {
			identify = requestDataMap.get("identify");
		} else {
			/*
			 * 如果URL中没有识别码参数，那么尝试获取referer这个URL中的字段来作为识别码
			 * 因为很多时候，是通过JSON请求本Controller，referer就是用户跳转过来的带识别码的URL
			 * 但是如果referer中没有identify=???的参数，就要把它设置为空，以免把URL的其他数据作为识别码传递
			 */
			identify = requestDataMap.get("referer");

			if (identify != null) {
				String[] data = identify.split("\\?");
				if (data.length > 1) {
					data = data[1].split("&");
					for (String d2 : data) {
						if (d2.startsWith("identify")) {
							identify = d2.replaceAll("^identify=", "");
							break;
						}
					}
				} else {
					identify = null;
				}
			}
		}

		if (StringUtils.isBlank(identify)) {
			couponModelCriteria.setIdentify("default");
		} else {
			String idData[] = identify.split("_");
			if (idData.length > 1) {
				firstIdentify = idData[0];
				if (idData.length > 1) {
					secIdentify = idData[0] + "_" + idData[1];
				}
				logger.debug("当前传递的识别码有" + idData.length + "段，二级匹配识别码是："
						+ secIdentify + ",一级匹配识别码是:" + firstIdentify);
			}
		}

		List<CouponModel> couponModelList = null;
		if (StringUtils.isNotBlank(secIdentify)) {
			couponModelCriteria.setIdentify(secIdentify);
			couponModelList = couponModelService.list(couponModelCriteria);
			logger.debug("通过二级识别码[" + secIdentify + "]获取的优惠券产品数量是:"
					+ (couponModelList == null ? "空" : couponModelList.size()));
		}
		if ((couponModelList == null || couponModelList.size() < 1)
				&& StringUtils.isNotBlank(firstIdentify)) {
			couponModelCriteria.setIdentify(firstIdentify);
			couponModelList = couponModelService.list(couponModelCriteria);
			logger.debug("通过二级识别码[" + secIdentify + "]未能获取到优惠券产品，使用一级识别码["
					+ firstIdentify + "]获取的优惠券产品数量是:"
					+ (couponModelList == null ? "空" : couponModelList.size()));
		}
		if ((couponModelList == null || couponModelList.size() < 1)
				&& StringUtils.isBlank(firstIdentify)) {
			couponModelList = couponModelService.list(couponModelCriteria);
			logger.debug("未提供识别码，使用默认识别码获取的优惠券产品数量是:"
					+ (couponModelList == null ? "空" : couponModelList.size()));
		}

		logger.info("根据识别码[" + couponModelCriteria.getIdentify()
				+ "]返回的优惠券产品列表:"
				+ (couponModelList == null ? "空" : couponModelList.size()));
		if (couponModelList == null || couponModelList.size() < 1) {
			map.put("message",
					new EisMessage(EisError.REQUIRED_PARAMETER.getId(),
							"找不到对应的优惠券产品列表"));
			return CommonStandard.frontMessageView;
		}
		CouponModel couponModel = couponModelList.get(0);

		logger.debug("使用第一个优惠券产品[" + couponModel + "]来处理");
		Object bean = applicationContextService.getBean(couponModel
				.getProcessor());
		if (bean == null) {
			logger.error("找不到优惠券活动:" + couponModel.getCouponModelId() + "的处理器:"
					+ couponModel.getProcessor());
			map.put("message",
					new EisMessage(EisError.REQUIRED_PARAMETER.getId(),
							"找不到对应的活动数据"));
			return CommonStandard.frontMessageView;
		}
		if (!(bean instanceof CouponProcessor)) {
			logger.error("优惠券活动:" + couponModel.getCouponModelId() + "的处理器["
					+ couponModel.getProcessor() + "]不是一个CouponProcessor类");
			map.put("message", new EisMessage(EisError.processorIsNull.getId(),
					"找不到对应的活动数据"));
			return CommonStandard.frontMessageView;
		}
		CouponProcessor couponProcessor = (CouponProcessor) bean;
		couponModelList = null;
		couponModelCriteria
				.setIdentify(identify == null ? "default" : identify);
		couponModelList = couponProcessor.list(couponModelCriteria, map);
		logger.info("由识别码[" + couponModelCriteria.getIdentify() + "]确定的优惠券处理器["
				+ couponModel.getProcessor() + "]返回的优惠券产品列表:"
				+ (couponModelList == null ? "空" : couponModelList.size()));

		/*
		 * if(activity.getAccountLimit() > 0 && couponModelList != null){
		 * //检查该用户是否已经领取了优惠券 for(CouponModel couponModel : couponModelList ){
		 * CouponCriteria fetchedCouponCriteria = new CouponCriteria();
		 * fetchedCouponCriteria.setUuid(frontUser.getUuid());
		 * fetchedCouponCriteria.setCouponCode(couponModel.getCouponCode()); int
		 * fetchedCount = couponService.count(fetchedCouponCriteria);
		 * if(fetchedCount >= activity.getAccountLimit()){ logger.info("用户[" +
		 * frontUser.getUuid() + "已领取过优惠券[" + couponModel.getCouponCode() +
		 * "]次，超过该优惠券可领取次数[" + activity.getAccountLimit() + ",将该优惠券状态设置为已领取");
		 * couponModel.setCurrentStatus(BasicStatus.readOnly.getId()); } } }
		 */

		/*
		 * //如果是微信产品，则产生微信coupon签名，以便前端发起JSAPI String timeStamp =
		 * String.valueOf(new Date().getTime()); String nonceStr =
		 * DigestUtils.md5Hex(String.valueOf(System.currentTimeMillis()));
		 * if(weixinService.isWeixinAccess(requestDataMap)){ for(CouponModel c2
		 * : couponModelList){ weixinService.makeCouponJsSignature(timeStamp,
		 * nonceStr, frontUser, c2); } map.put("timeStamp", timeStamp);
		 * map.put("nonceStr", nonceStr); }
		 */
		map.put("couponModelList", couponModelList);
		return view;

	}

	@RequestMapping(value = "/listByModel", method = RequestMethod.GET)
	@IgnoreLoginCheck
	public String listCouponByModel(HttpServletRequest request,
			HttpServletResponse response, ModelMap map,
			CouponModelCriteria couponModelCriteria,
			@RequestParam(value = "v", required = false) String version) {

		@SuppressWarnings("unused")
		User frontUser = certifyService.getLoginedUser(request, response,
				UserTypes.frontUser.getId());

		/*
		 * if(frontUser == null || frontUser.getCurrentStatus() !=
		 * UserStatus.normal.getId()){ map.put("message", new
		 * EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录，请先登录"));
		 * return CommonStandard.frontMessageView;
		 * 
		 * }
		 */
		long ownerId = 0;
		try {
			ownerId = (long) map.get("ownerId");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if (ownerId < 1) {
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),
					"系统异常", "请尝试访问其他页面或返回首页"));
			return CommonStandard.frontMessageView;
		}

		final String baseView = "coupon/index";
		String view = baseView;
		// final String view = "coupon/index";

		if (version != null && NumericUtils.isIntNumber(version)) {
			view += "_";
			view += version;
			logger.debug("链接中有版本号，使用view:" + view);
		}

		couponModelCriteria.setOwnerId(ownerId);

		if (couponModelCriteria.getCouponCode() == null) {
			logger.error("查看优惠券未提交对应代码");
			map.put("message",
					new EisMessage(EisError.REQUIRED_PARAMETER.getId(),
							"找不到对应的优惠券产品列表"));
			return CommonStandard.frontMessageView;
		}

		List<CouponModel> couponModelList = couponModelService
				.list(couponModelCriteria);
		logger.debug("通过优惠券代码[" + couponModelCriteria.getCouponCode() + "/"
				+ ownerId + "]获取的优惠券产品数量是:"
				+ (couponModelList == null ? "空" : couponModelList.size()));

		logger.info("根据识别码[" + couponModelCriteria.getIdentify()
				+ "]返回的优惠券产品列表:"
				+ (couponModelList == null ? "空" : couponModelList.size()));
		if (couponModelList == null || couponModelList.size() < 1) {
			map.put("message",
					new EisMessage(EisError.REQUIRED_PARAMETER.getId(),
							"找不到对应的优惠券产品列表"));
			return CommonStandard.frontMessageView;
		}
		CouponModel couponModel = couponModelList.get(0);
		logger.debug("使用第一个优惠券产品[" + couponModel + "]来处理");
		Object bean = applicationContextService.getBean(couponModel
				.getProcessor());
		if (bean == null) {
			logger.error("找不到优惠券活动:" + couponModel.getCouponModelId() + "的处理器:"
					+ couponModel.getProcessor());
			map.put("message",
					new EisMessage(EisError.REQUIRED_PARAMETER.getId(),
							"找不到对应的活动数据"));
			return CommonStandard.frontMessageView;
		}
		if (!(bean instanceof CouponProcessor)) {
			logger.error("优惠券活动:" + couponModel.getCouponModelId() + "的处理器["
					+ couponModel.getProcessor() + "]不是一个CouponProcessor类");
			map.put("message", new EisMessage(EisError.processorIsNull.getId(),
					"找不到对应的活动数据"));
			return CommonStandard.frontMessageView;
		}
		CouponProcessor couponProcessor = (CouponProcessor) bean;

		couponModelList = couponProcessor.list(couponModelCriteria, null);

		map.put("couponModelList", couponModelList);
		return view;

	}

	// 使用一个优惠券
	@RequestMapping(value = "/commit", method = RequestMethod.POST)
	public String commit(HttpServletRequest request,
			HttpServletResponse response, ModelMap map) throws Exception {
		User frontUser = null;
		try {
			frontUser = certifyService.getLoginedUser(request, response,
					UserTypes.frontUser.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (frontUser == null) {
			map.put("message",
					new EisMessage(EisError.userNotFoundInSession.getId(),
							"请先登录"));
			return CommonStandard.frontMessageView;
		}
		return _commit(HttpUtils.getRequestDataMap(request), frontUser, map,
				HttpUtils.generateUrlPrefix(request));

	}

	private String _commit(Map<String, String> requestDataMap, User frontUser,
			ModelMap map, String urlPrefix) {

		final String view = "coupon/commit";

		String couponModelCode = requestDataMap.get("couponCode");
		String serialNumber = requestDataMap.get("couponSerialNumber");
		String password = requestDataMap.get("password");

		if (StringUtils.isBlank(couponModelCode)) {
			logger.error("未提交要领取的优惠券代码");
			map.put("message",
					new EisMessage(EisError.REQUIRED_PARAMETER.getId(),
							"请提交正确的数据"));
			return CommonStandard.frontMessageView;
		}
		if (StringUtils.isBlank(serialNumber)) {
			logger.error("未提交要领取的优惠券卡号");
			map.put("message",
					new EisMessage(EisError.REQUIRED_PARAMETER.getId(),
							"请提交正确的数据"));
			return CommonStandard.frontMessageView;
		}
		if (StringUtils.isBlank(password)) {
			logger.error("未提交要领取的优惠券密码");
			map.put("message",
					new EisMessage(EisError.REQUIRED_PARAMETER.getId(),
							"请提交正确的数据"));
			return CommonStandard.frontMessageView;
		}

		CouponModelCriteria couponModelCriteria = new CouponModelCriteria();
		couponModelCriteria.setOwnerId(frontUser.getOwnerId());
		couponModelCriteria.setCouponCode(couponModelCode);
		couponModelCriteria.setCurrentStatus(BasicStatus.normal.getId());
		List<CouponModel> couponModelList = couponModelService
				.list(couponModelCriteria);
		if (couponModelList == null || couponModelList.size() < 1) {
			logger.error("找不到指定的卡券产品:" + couponModelCode);
			map.put("message",
					new EisMessage(EisError.REQUIRED_PARAMETER.getId(),
							"找不到指定的卡券产品:" + couponModelCode));
			return CommonStandard.frontMessageView;
		}
		CouponModel couponModel = couponModelList.get(0);
		String beanName = null;
		if (couponModel.getProcessor() == null) {
			beanName = DEFAULT_COUPON_PROCESSOR;
		} else {
			beanName = couponModel.getProcessor();
		}

		CouponProcessor couponProcessor = applicationContextService
				.getBeanGeneric(beanName);

		if (couponProcessor == null) {
			logger.error("找不到指定的卡券[" + couponModel.getCouponModelId() + "]处理器:"
					+ beanName);
			map.put("message", new EisMessage(EisError.beanNotFound.getId(),
					"指定的卡券产品数据异常"));
			return CommonStandard.frontMessageView;
		}

		Coupon coupon = new Coupon(couponModel);
		coupon.setUuid(frontUser.getUuid());
		coupon.setCouponSerialNumber(serialNumber);
		coupon.setCouponPassword(password);

		int rs = couponProcessor.consume(coupon);
		logger.debug("优惠券领取结果:" + rs + ",卡券状态:" + coupon.getCurrentStatus());
		if (coupon.getCurrentStatus() != TransactionStatus.success.getId()) {
			if (coupon.getCurrentStatus() == EisError.moneyNotEnough.getId()) {
				map.put("message",
						new EisMessage(coupon.getCurrentStatus(), ""));

			} else {
				map.put("message",
						new EisMessage(coupon.getCurrentStatus(), ""));
			}
		} else {
			map.put("message", new EisMessage(OperateResult.success.getId(),
					"领取成功"));
			map.put("coupon", coupon);
		}
		return view;
	}

	// 请求领取一个优惠券
	@RequestMapping(value = "/fetch", method = RequestMethod.POST)
	public String fetch(HttpServletRequest request,
			HttpServletResponse response, ModelMap map) throws Exception {
		User frontUser = null;
		try {
			frontUser = certifyService.getLoginedUser(request, response,
					UserTypes.frontUser.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (frontUser == null) {
			map.put("message",
					new EisMessage(EisError.userNotFoundInSession.getId(),
							"请先登录"));
			return CommonStandard.frontMessageView;
		}
		return _fetch(HttpUtils.getRequestDataMap(request), frontUser, map,
				HttpUtils.generateUrlPrefix(request));

	}

	// 请求获取一个优惠券，加密形式
	@RequestMapping(value = "/fetchCrypt", method = RequestMethod.POST)
	public String fetchCrypt(HttpServletRequest request,
			HttpServletResponse response, ModelMap map,
			@RequestParam("uuid") long uuid,
			@RequestParam("data") String cryptedData) {

		long ownerId = (long) map.get("ownerId");
		if (ownerId < 1) {
			logger.error("系统会话中没有ownerId数据");
			return CommonStandard.frontMessageView;
		}

		User frontUser = frontUserService.select(uuid);
		if (frontUser == null) {
			logger.warn("找不到用户[" + uuid + "]");
			map.put("message",
					new EisMessage(EisError.userNotFoundInSession.getId(),
							"请先登录"));
			return CommonStandard.frontMessageView;
		}
		if (frontUser.getOwnerId() != ownerId) {
			logger.error("用户[" + uuid + "]的ownerid[" + frontUser.getOwnerId()
					+ "]与系统会话中的[" + ownerId + "]不一致");
			map.put("message",
					new EisMessage(EisError.userNotFoundInSystem.getId(),
							"找不到指定的用户"));
			return CommonStandard.frontMessageView;
		}
		if (frontUser.getCurrentStatus() != UserStatus.normal.getId()) {
			logger.warn("用户[" + uuid + "]状态异常:" + frontUser.getCurrentStatus());
			map.put("message",
					new EisMessage(EisError.userNotFoundInSession.getId(),
							"请先登录"));
			return CommonStandard.frontMessageView;
		}
		String cryptKey = null;
		try {
			cryptKey = frontUser.getUserConfigMap()
					.get(DataName.supplierLoginKey.toString()).getDataValue();
		} catch (Exception e) {
			logger.error("在查找用户配置数据时发生异常:" + e.getMessage());
			e.printStackTrace();
		}
		if (StringUtils.isBlank(cryptKey)) {
			logger.warn("找不到用户[" + uuid + "]的登录密钥");
			map.put("message",
					new EisMessage(EisError.userNotFoundInSession.getId(),
							"登录失败，请检查您的帐号密码是否正确"));
			return CommonStandard.frontMessageView;
		}
		logger.warn("用户[" + uuid + "]的登陆密钥是:" + cryptKey);

		Crypt crypt = new Crypt();
		crypt.setAesKey(cryptKey);
		String clearData = null;
		try {
			clearData = crypt.aesDecrypt(cryptedData);
		} catch (Exception e) {
			logger.error("在尝试解密用户请求数据时发生异常:" + e.getMessage());
		}
		if (StringUtils.isBlank(clearData)) {
			logger.warn("无法解密用户[" + uuid + "]数据[密钥:" + cryptKey + "]:"
					+ cryptedData);
			map.put("message",
					new EisMessage(EisError.userNotFoundInSession.getId(),
							"登录失败，请检查您的帐号密码是否正确"));
			return CommonStandard.frontMessageView;
		}
		String view = _fetch(HttpUtils.getRequestDataMap(clearData), frontUser,
				map, HttpUtils.generateUrlPrefix(request));
		if (map.get("message") != null) {
			EisMessage msg = (EisMessage) map.get("message");
			map.remove("message");
			try {
				String cryptedResult = crypt.aesEncrypt(om
						.writeValueAsString(msg));
				logger.info("对message进行加密:" + om.writeValueAsString(msg)
						+ ",结果:" + cryptedResult);
				map.put("message", cryptedResult);
			} catch (Exception e) {
				e.printStackTrace();
				map.put("message", new EisMessage(EisError.dataError.getId(),
						"系统异常"));
				return CommonStandard.frontMessageView;
			}
		}
		return view;

	}

	private String _fetch(Map<String, String> requestDataMap, User frontUser,
			ModelMap map, String urlPrefix) {

		long ownerId = (long) map.get("ownerId");
		if (ownerId < 1) {
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),
					"系统异常"));
			return CommonStandard.frontMessageView;
		}
		final String view = "coupon/fetch";
		int activityId = 0;
		if (StringUtils.isNumeric(requestDataMap.get("activityId"))) {
			activityId = Integer.parseInt(requestDataMap.get("activityId"));
		}
		if (activityId <= 0) {
			logger.error("未提交要领取的优惠券活动ID");
			map.put("message",
					new EisMessage(EisError.REQUIRED_PARAMETER.getId(),
							"请提交正确的数据"));
			return CommonStandard.frontMessageView;
		}
		String couponCode = requestDataMap.get("couponCode");
		if (StringUtils.isBlank(couponCode)) {
			logger.error("未提交要领取的优惠券活动代码");
			map.put("message",
					new EisMessage(EisError.REQUIRED_PARAMETER.getId(),
							"请提交正确的数据"));
			return CommonStandard.frontMessageView;
		}
		String extraCode = requestDataMap.get("extraCode");
		String identify = null;
		if (requestDataMap.containsKey("identify")) {
			identify = requestDataMap.get("identify");
		} else if (requestDataMap.containsKey("referer")) {
			String data[] = requestDataMap.get("referer").split("\\?");
			if (data.length > 1) {
				for (String d2 : data) {
					if (d2.startsWith("identify")) {
						identify = d2.replaceAll("^identify=", "");
						logger.debug("通过访问URL的referer获取到identify:" + identify);
						break;
					}
				}
			}
		}
		if (StringUtils.isBlank(identify)) {
			identify = "default";
		}
		CouponCriteria couponCriteria = new CouponCriteria();
		couponCriteria.setActivityId(activityId);
		couponCriteria.setCouponCode(couponCode);
		couponCriteria.setExtraCode(extraCode);
		couponCriteria.setIdentify(identify);
		couponCriteria.setUuid(frontUser.getUuid());
		EisMessage fetchResult = couponService.fetch(couponCriteria);
		// Coupon coupon = couponService.fetch(activityId, couponCode,
		// extraCode, identify, frontUser, urlPrefix);
		logger.debug("优惠券领取结果:" + fetchResult.getOperateCode());

		if (fetchResult.getOperateCode() != OperateResult.success.getId()) {
			map.put("message", fetchResult);
		} else {
			map.put("message", new EisMessage(OperateResult.success.getId(),
					"领取成功"));
			map.put("coupon", fetchResult.getAttachmentData("couponList"));
		}
		return view;
	}

	// 请求领取一组父产品下的所有优惠券
	@RequestMapping(value = "/fetchBatch", method = RequestMethod.POST)
	public String fetchBatch(HttpServletRequest request,
			HttpServletResponse response, ModelMap map) throws Exception {
		User frontUser = null;
		try {
			frontUser = certifyService.getLoginedUser(request, response,
					UserTypes.frontUser.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (frontUser == null) {
			map.put("message",
					new EisMessage(EisError.userNotFoundInSession.getId(),
							"请先登录"));
			return CommonStandard.frontMessageView;
		}
		return _fetchBatch(HttpUtils.getRequestDataMap(request), frontUser,
				map, HttpUtils.generateUrlPrefix(request));

	}

	// 请求领取一组父产品下的所有优惠券，加密形式
	@RequestMapping(value = "/fetchBatchCrypt", method = RequestMethod.POST)
	public String fetchBatchCrypt(HttpServletRequest request,
			HttpServletResponse response, ModelMap map,
			@RequestParam("uuid") long uuid,
			@RequestParam("data") String cryptedData) {

		long ownerId = (long) map.get("ownerId");
		if (ownerId < 1) {
			logger.error("系统会话中没有ownerId数据");
			return CommonStandard.frontMessageView;
		}

		User frontUser = frontUserService.select(uuid);
		if (frontUser == null) {
			logger.warn("找不到用户[" + uuid + "]");
			map.put("message",
					new EisMessage(EisError.userNotFoundInSession.getId(),
							"请先登录"));
			return CommonStandard.frontMessageView;
		}
		if (frontUser.getOwnerId() != ownerId) {
			logger.error("用户[" + uuid + "]的ownerid[" + frontUser.getOwnerId()
					+ "]与系统会话中的[" + ownerId + "]不一致");
			map.put("message",
					new EisMessage(EisError.userNotFoundInSystem.getId(),
							"找不到指定的用户"));
			return CommonStandard.frontMessageView;
		}
		if (frontUser.getCurrentStatus() != UserStatus.normal.getId()) {
			logger.warn("用户[" + uuid + "]状态异常:" + frontUser.getCurrentStatus());
			map.put("message",
					new EisMessage(EisError.userNotFoundInSession.getId(),
							"请先登录"));
			return CommonStandard.frontMessageView;
		}
		String cryptKey = null;
		try {
			cryptKey = frontUser.getUserConfigMap()
					.get(DataName.supplierLoginKey.toString()).getDataValue();
		} catch (Exception e) {
			logger.error("在查找用户配置数据时发生异常:" + e.getMessage());
			e.printStackTrace();
		}
		if (StringUtils.isBlank(cryptKey)) {
			logger.warn("找不到用户[" + uuid + "]的登录密钥");
			map.put("message",
					new EisMessage(EisError.userNotFoundInSession.getId(),
							"登录失败，请检查您的帐号密码是否正确"));
			return CommonStandard.frontMessageView;
		}
		logger.warn("用户[" + uuid + "]的登陆密钥是:" + cryptKey);

		Crypt crypt = new Crypt();
		crypt.setAesKey(cryptKey);
		String clearData = null;
		try {
			clearData = crypt.aesDecrypt(cryptedData);
		} catch (Exception e) {
			logger.error("在尝试解密用户请求数据时发生异常:" + e.getMessage());
		}
		if (StringUtils.isBlank(clearData)) {
			logger.warn("无法解密用户[" + uuid + "]数据[密钥:" + cryptKey + "]:"
					+ cryptedData);
			map.put("message",
					new EisMessage(EisError.userNotFoundInSession.getId(),
							"登录失败，请检查您的帐号密码是否正确"));
			return CommonStandard.frontMessageView;
		}
		String view = _fetchBatch(HttpUtils.getRequestDataMap(clearData),
				frontUser, map, HttpUtils.generateUrlPrefix(request));
		if (map.get("message") != null) {
			EisMessage msg = (EisMessage) map.get("message");
			map.remove("message");
			try {
				String cryptedResult = crypt.aesEncrypt(om
						.writeValueAsString(msg));
				logger.info("对message进行加密:" + om.writeValueAsString(msg)
						+ ",结果:" + cryptedResult);
				map.put("message", cryptedResult);
			} catch (Exception e) {
				e.printStackTrace();
				map.put("message", new EisMessage(EisError.dataError.getId(),
						"系统异常"));
				return CommonStandard.frontMessageView;
			}
		}
		return view;

	}

	private String _fetchBatch(Map<String, String> requestDataMap,
			User frontUser, ModelMap map, String urlPrefix) {

		long ownerId = (long) map.get("ownerId");
		if (ownerId < 1) {
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),
					"系统异常"));
			return CommonStandard.frontMessageView;
		}
		final String view = "coupon/fetch";

		String couponCode = requestDataMap.get("couponCode");
		if (StringUtils.isBlank(couponCode)) {
			logger.error("未提交要领取的优惠券活动代码");
			map.put("message",
					new EisMessage(EisError.REQUIRED_PARAMETER.getId(),
							"请提交正确的数据"));
			return CommonStandard.frontMessageView;
		}
		String identify = null;
		if (requestDataMap.containsKey("identify")) {
			identify = requestDataMap.get("identify");
		} else if (requestDataMap.containsKey("referer")) {
			String data[] = requestDataMap.get("referer").split("\\?");
			if (data.length > 1) {
				for (String d2 : data) {
					if (d2.startsWith("identify")) {
						identify = d2.replaceAll("^identify=", "");
						logger.debug("通过访问URL的referer获取到identify:" + identify);
						break;
					}
				}
			}
		}
		if (StringUtils.isBlank(identify)) {
			identify = "default";
		}
		logger.debug("尝试批量获取的优惠券条件identify=" + identify);
		CouponCriteria couponCriteria = new CouponCriteria(ownerId);
		couponCriteria.setCouponCode(couponCode);
		couponCriteria.setUuid(frontUser.getUuid());
		couponCriteria.setIdentify(identify);
		EisMessage fetchResult = couponService.fetch(couponCriteria);
		if (fetchResult.getOperateCode() != OperateResult.success.getId()) {
			map.put("message", fetchResult);
		} else {
			List<Coupon> couponList = fetchResult
					.getAttachmentData("couponList");
			logger.debug("批量优惠券领取数量:"
					+ (couponList == null ? "空" : couponList.size()));
			map.put("cardList",
					weixinService.generateCouponListString(couponList));
			map.put("coupon", couponList);
		}

		return view;
	}

	// 获取用户的优惠券
	@RequestMapping(value = "/listMine", method = RequestMethod.GET)
	public String listMine(HttpServletRequest request,
			HttpServletResponse response, ModelMap map) {
		User frontUser = null;
		try {
			frontUser = certifyService.getLoginedUser(request, response,
					UserTypes.frontUser.getId());
			// logger.info("从Session中得到用户:" + frontUser.getUuid());
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (frontUser == null) {
			map.put("message",
					new EisMessage(EisError.userNotFoundInSession.getId(),
							"您尚未登录，请先登录"));
			return CommonStandard.frontMessageView;
		}

		return _listMine(HttpUtils.getRequestDataMap(request), frontUser, map,
				request);
	}

	// 获取用户的优惠券,加密形式
	@RequestMapping(value = "/listMineCrypt", method = RequestMethod.GET)
	public String listMineCrypt(HttpServletRequest request,
			HttpServletResponse response, ModelMap map,
			@RequestParam("uuid") long uuid,
			@RequestParam("data") String cryptedData) {
		long ownerId = (long) map.get("ownerId");
		if (ownerId < 1) {
			logger.error("系统会话中没有ownerId数据");
			return CommonStandard.frontMessageView;
		}
		User frontUser = frontUserService.select(uuid);
		if (frontUser == null) {
			logger.warn("找不到用户[" + uuid + "]");
			map.put("message",
					new EisMessage(EisError.userNotFoundInSession.getId(),
							"登录失败，请检查您的帐号密码是否正确"));
			return CommonStandard.frontMessageView;
		}
		if (frontUser.getOwnerId() != ownerId) {
			logger.error("用户[" + uuid + "]的ownerid[" + frontUser.getOwnerId()
					+ "]与系统会话中的[" + ownerId + "]不一致");
			map.put("message",
					new EisMessage(EisError.userNotFoundInSystem.getId(),
							"找不到指定的用户"));
			return CommonStandard.frontMessageView;
		}
		if (frontUser.getCurrentStatus() != UserStatus.normal.getId()) {
			logger.warn("用户[" + uuid + "]状态异常:" + frontUser.getCurrentStatus());
			map.put("message",
					new EisMessage(EisError.userNotFoundInSession.getId(),
							"登录失败，请检查您的帐号密码是否正确"));
			return CommonStandard.frontMessageView;
		}

		String cryptKey = null;
		try {
			cryptKey = frontUser.getUserConfigMap()
					.get(DataName.supplierLoginKey.toString()).getDataValue();
		} catch (Exception e) {
			logger.error("在查找用户配置数据时发生异常:" + e.getMessage());
			e.printStackTrace();
		}
		if (StringUtils.isBlank(cryptKey)) {
			logger.warn("找不到用户[" + uuid + "]的登录密钥");
			map.put("message",
					new EisMessage(EisError.userNotFoundInSession.getId(),
							"登录失败，请检查您的帐号密码是否正确"));
			return CommonStandard.frontMessageView;
		}
		logger.warn("用户[" + uuid + "]的登陆密钥是:" + cryptKey);

		Crypt crypt = new Crypt();
		crypt.setAesKey(cryptKey);
		String clearData = null;
		try {
			clearData = crypt.aesDecrypt(cryptedData);
		} catch (Exception e) {
			logger.error("在尝试解密用户请求数据时发生异常:" + e.getMessage());
		}
		if (StringUtils.isBlank(clearData)) {
			logger.warn("无法解密用户[" + uuid + "]数据[密钥:" + cryptKey + "]:"
					+ cryptedData);
			map.put("message",
					new EisMessage(EisError.userNotFoundInSession.getId(),
							"登录失败，请检查您的帐号密码是否正确"));
			return CommonStandard.frontMessageView;
		}
		String view = _listMine(HttpUtils.getRequestDataMap(request),
				frontUser, map, request);
		return view;

	}

	private String _listMine(Map<String, String> requestDataMap,
			User frontUser, ModelMap map, HttpServletRequest request) {
		long ownerId = (long) map.get("ownerId");
		if (ownerId < 1) {
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),
					"系统异常"));
			return CommonStandard.frontMessageView;
		}
		final String view = "coupon/listMine";
		CouponCriteria couponCriteria = new CouponCriteria();
		couponCriteria.setUuid(frontUser.getUuid());
		couponCriteria.setOwnerId(frontUser.getOwnerId());
		int rows = 0;
		if (requestDataMap.get("rows") != null
				&& StringUtils.isNumeric(requestDataMap.get("rows"))) {
			rows = Integer.parseInt(requestDataMap.get("rows"));
		}
		if (rows == 0) {
			rows = 10;
		}
		int page = 0;
		if (requestDataMap.get("page") != null
				&& StringUtils.isNumeric(requestDataMap.get("page"))) {
			page = Integer.parseInt(requestDataMap.get("page"));
		}
		if (page == 0) {
			page = 1;
		}
		int currentStatus = 0;
		if (requestDataMap.get("currentStatus") != null
				&& StringUtils.isNumeric(requestDataMap.get("currentStatus"))) {
			currentStatus = Integer.parseInt(requestDataMap
					.get("currentStatus"));
		}
		if (currentStatus > 0) {
			couponCriteria.setCurrentStatus(currentStatus);
		}
		int totalRows = couponService.count(couponCriteria);
		map.put("totalRows", totalRows);
		Paging paging = new Paging(rows);
		couponCriteria.setPaging(paging);
		couponCriteria.getPaging().setCurrentPage(page);
		map.put("page", page);
		map.put("rows", rows);
		List<Coupon> couponList = couponService.listOnPage(couponCriteria);
		logger.info("返回用户[" + couponCriteria.getUuid() + "]已有的优惠券列表:"
				+ (couponList == null ? "空" : couponList.size()));
		if (couponList != null && couponList.size() > 0) {
			for (Coupon coupon : couponList) {
				if (coupon.getContent() != null
						&& StringUtils.isNumeric(coupon.getContent())) {
					coupon.setContent(HttpUtils.generateUrlPrefix(request)
							+ "/barcode/" + coupon.getContent());
				}
			}
		}

		map.put("couponList", couponList);
		return view;

	}

	// 得到合作伙伴需要核销卡券的条码，在微信上进行核销
	@RequestMapping(value = "verification")
	public String verification(HttpServletRequest request,
			HttpServletResponse response, ModelMap map,
			@RequestParam("hxtm") long code,
			@RequestParam("extraCode") String extraCode) {
		logger.debug("核销条码 ：" + code);
		logger.debug("活动编号：" + extraCode);
		long ownerId = 0;
		try {
			ownerId = (long) map.get("ownerId");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if (ownerId < 1) {
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),
					"系统异常", "请尝试访问其他页面或返回首页"));
			return CommonStandard.frontMessageView;
		}

		CouponModelCriteria couponModelCriteria = new CouponModelCriteria();
		// couponModelCriteria.setIdentify("5ymanjian");
		couponModelCriteria.setExtraCode(extraCode);
		List<CouponModel> couponModelList = couponModelService
				.list(couponModelCriteria);
		if (couponModelList == null || couponModelList.size() < 1) {
			map.put("message",
					new EisMessage(EisError.REQUIRED_PARAMETER.getId(),
							"找不到指定的数据"));
			return CommonStandard.frontMessageView;
		}
		CouponModel couponModel = couponModelList.get(0);
		// 微信核销接口
		String accessToken = weixinService.getAccessToken(ownerId);
		String url = "https://api.weixin.qq.com/card/code/consume?access_token="
				+ accessToken;
		String data = "{\"card_id\":\"" + couponModel.getCouponCode()
				+ "\",\"code\":\"" + code + "\"}";
		HttpClient httpClient = HttpClientPoolV3.getHttpClient(host, port);
		String result = null;
		try {
			PostMethod pm = new PostMethod(url);
			pm.setRequestEntity(new StringRequestEntity(data, "", "UTF-8"));
			int rs = httpClient.executeMethod(pm);
			System.out.println("请求返回:" + rs);

			result = new String(pm.getResponseBody(), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("结果 ：" + result);
		if (result == null) {
			logger.error("无法核销:" + result);
			map.put("message", new EisMessage(OperateResult.failed.getId(),
					"无法核销:" + result));
			return CommonStandard.frontMessageView;
		}
		JsonNode jsonNode = null;
		try {
			jsonNode = JsonUtils.getInstance().readTree(result.trim());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (jsonNode == null) {
			logger.error("无法核销微信卡券产品:" + result);
			map.put("message", new EisMessage(OperateResult.failed.getId(),
					"无法核销微信卡券产品:" + result));
			return CommonStandard.frontMessageView;
		}
		int errorCode = jsonNode.path("errcode").intValue();
		if (errorCode != 0) {
			logger.error("无法核销微信卡券产品，微信返回错误:" + errorCode);
			map.put("message", new EisMessage(OperateResult.failed.getId(),
					"无法核销微信卡券产品   " + result));
			return CommonStandard.frontMessageView;
		}
		map.put("message", new EisMessage(OperateResult.success.getId(),
				"核销成功：" + result));
		return CommonStandard.frontMessageView;
	}

	// 合作伙伴修改我方系统中的优惠券状态
	@RequestMapping(value = "/notify")
	public String notify(HttpServletRequest request,
			HttpServletResponse response, ModelMap map,
			@RequestParam("partnerId") long partnerId,
			@RequestParam(value = "sign") String sign,
			@RequestParam(value = "timestamp") String timestamp,
			@RequestParam("code") String couponContent,
			@RequestParam(value = "currentStatus") String currentStatusString) {

		int changeCurrentStatus = NumericUtils.getNumeric(currentStatusString);
		if (changeCurrentStatus == 0) {
			logger.error("未提供要修改的currentStatus");
			map.put("message",
					new EisMessage(EisError.REQUIRED_PARAMETER.getId(),
							"数据提交异常"));
		}

		User user = partnerService.select(partnerId);
		if (user == null) {
			logger.error("根据UUID[" + partnerId + "]找不到合作伙伴");
			map.put("message",
					new EisMessage(EisError.userNotFoundInSystem.getId(),
							"找不到指定的用户"));
			return CommonStandard.frontMessageView;
		}

		if (user.getCurrentStatus() != UserStatus.normal.getId()) {
			logger.error("用户[" + partnerId + "]状态异常[" + user.getCurrentStatus()
					+ "]");
			map.put("message",
					new EisMessage(EisError.userNotFoundInSystem.getId(),
							"找不到指定的用户"));
			return CommonStandard.frontMessageView;
		}
		String requestString = "code=" + couponContent + "&currentStatus="
				+ currentStatusString + "&partnerId=" + partnerId
				+ "&timestamp=" + timestamp;
		if (!verifySubmitSign(requestString, user, sign)) {
			logger.error("用户提交的数据[objectCode=" + couponContent + ",timestamp="
					+ timestamp + ",sign=" + sign + "]无法通过校验");
			map.put("message", new EisMessage(EisError.VERIFY_ERROR.getId(),
					"找不到指定的用户"));
			return CommonStandard.frontMessageView;
		}
		CouponCriteria couponCriteria = new CouponCriteria();
		couponCriteria.setInviters(partnerId);
		couponCriteria.setOwnerId(user.getOwnerId());
		couponCriteria.setContent(couponContent);
		List<Coupon> couponList = couponService.list(couponCriteria);
		if (couponList == null || couponList.size() < 1) {
			logger.error("找不到[content=" + couponContent + ",partnerId="
					+ partnerId + "的coupon数据");
			map.put("message",
					new EisMessage(EisError.REQUIRED_PARAMETER.getId(),
							"找不到指定的数据"));
			return CommonStandard.frontMessageView;
		}
		Coupon coupon = couponList.get(0);
		coupon.setCurrentStatus(changeCurrentStatus);
		int rs = couponService.update(coupon);
		if (rs == 1) {
			logger.info("完成合作方对优惠券[" + coupon + "]修改");
			map.put("message", new EisMessage(OperateResult.success.getId(),
					"修改完成"));
			return CommonStandard.frontMessageView;
		}
		logger.info("无法完成合作方对优惠券[" + coupon + "]的修改:" + rs);
		map.put("message",
				new EisMessage(OperateResult.success.getId(), "修改失败"));
		return CommonStandard.frontMessageView;
	}

	// 校验提交数据的合法性
	private boolean verifySubmitSign(String requestString, User partner,
			String sign) {

		if (partner == null) {
			logger.warn("被校验的partner为空");
			return false;
		}
		if (StringUtils.isBlank(sign)) {
			logger.warn("未提供校验SIGN");
			return false;
		}

		if (partner.getUserConfigMap() == null
				|| partner.getUserConfigMap().get(
						DataName.supplierLoginKey.toString()) == null) {
			logger.error("用户[" + partner.getUuid() + "]配置中没有supplierLoginKey");
			return false;
		}
		String key = partner.getUserConfigMap()
				.get(DataName.supplierLoginKey.toString()).getDataValue();

		if (StringUtils.isBlank(key)) {
			logger.error("合作伙伴[" + partner.getUuid() + "]未配置supplierLoginKey");
			return false;
		}
		Map<String, String> map = HttpUtils.getRequestDataMap(requestString);
		if (map.get("sign") != null) {
			map.remove("sign");
		}

		String src = HttpUtils.generateRequestString(map) + "|" + key;
		String md5 = DigestUtils.sha256Hex(src).toLowerCase();
		logger.debug("检查提交校验，我方对源字符串[" + src + "]的SHA256结果" + md5
				+ ",客户提交的SHA256:" + sign);
		if (md5.equals(sign)) {
			return true;
		}

		return false;
	}

	// 显示一个优惠券产品的详情
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@IgnoreLoginCheck
	public String get(HttpServletRequest request, HttpServletResponse response,
			ModelMap map) {
		User frontUser = null;
		try {
			frontUser = certifyService.getLoginedUser(request, response,
					UserTypes.frontUser.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return _get(HttpUtils.getRequestDataMap(request), frontUser, map);
	}

	private String _get(Map<String, String> requestDataMap, User frontUser,
			ModelMap map) {
		long ownerId = (long) map.get("ownerId");
		if (ownerId < 1) {
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),
					"系统异常"));
			return CommonStandard.frontMessageView;
		}
		final String view = "coupon/detail";

		String couponCode = HttpUtils.getStringValueFromRequestMap(
				requestDataMap, "couponCode");
		if (StringUtils.isBlank(couponCode)) {
			logger.error("未提供要获取的优惠券代码");
			map.put("message",
					new EisMessage(EisError.REQUIRED_PARAMETER.getId(),
							"请提交优惠券代码"));
			return CommonStandard.frontMessageView;
		}

		CouponModelCriteria couponModelCriteria = new CouponCriteria(ownerId);
		couponModelCriteria.setCouponCode(couponCode);
		if (frontUser != null) {
			couponModelCriteria.setUuid(frontUser.getUuid());
		}
		couponModelCriteria.setCurrentStatus(BasicStatus.normal.getId());

		List<CouponModel> couponModelList = null;
		couponModelList = couponModelService.list(couponModelCriteria);
		logger.debug("通过优惠券代码[" + couponCode + "]获取的优惠券产品数量是:"
				+ (couponModelList == null ? "空" : couponModelList.size()));

		if (couponModelList == null || couponModelList.size() < 1) {
			map.put("message",
					new EisMessage(EisError.REQUIRED_PARAMETER.getId(),
							"找不到对应的优惠券产品列表"));
			return CommonStandard.frontMessageView;
		}
		CouponModel couponModel = couponModelList.get(0);

		logger.debug("使用第一个优惠券产品[" + couponModel + "]来处理");
		Object bean = applicationContextService.getBean(couponModel
				.getProcessor());
		if (bean == null) {
			logger.error("找不到优惠券活动:" + couponModel.getCouponModelId() + "的处理器:"
					+ couponModel.getProcessor());
			map.put("message",
					new EisMessage(EisError.REQUIRED_PARAMETER.getId(),
							"找不到对应的活动数据"));
			return CommonStandard.frontMessageView;
		}
		if (!(bean instanceof CouponProcessor)) {
			logger.error("优惠券活动:" + couponModel.getCouponModelId() + "的处理器["
					+ couponModel.getProcessor() + "]不是一个CouponProcessor类");
			map.put("message", new EisMessage(EisError.processorIsNull.getId(),
					"找不到对应的活动数据"));
			return CommonStandard.frontMessageView;
		}
		CouponProcessor couponProcessor = (CouponProcessor) bean;
		CouponCriteria couponCriteria = new CouponCriteria();
		couponCriteria.setCouponCode(couponModel.getCouponCode());
		couponCriteria.setExtraCode(couponModel.getExtraCode());
		EisMessage fetchResult = couponProcessor.fetch(couponCriteria);
		if (fetchResult.getOperateCode() != OperateResult.success.getId()) {
			map.put("message", fetchResult);
		} else {
			List<Coupon> couponList = fetchResult
					.getAttachmentData("couponList");
			map.put("coupon", couponList.get(0));
		}
		return view;

	}

}
