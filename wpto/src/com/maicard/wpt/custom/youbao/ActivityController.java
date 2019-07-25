package com.maicard.wpt.custom.youbao;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.maicard.annotation.IgnoreLoginCheck;
import com.maicard.common.base.BaseController;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.util.HttpUtils;
import com.maicard.common.util.NumericUtils;
import com.maicard.money.criteria.CouponModelCriteria;
import com.maicard.money.criteria.PriceCriteria;
import com.maicard.money.domain.Coupon;
import com.maicard.money.domain.CouponModel;
import com.maicard.money.domain.Price;
import com.maicard.money.service.CouponModelService;
import com.maicard.money.service.CouponService;
import com.maicard.money.service.PriceService;
import com.maicard.product.criteria.ActivityCriteria;
import com.maicard.product.criteria.ActivityLogCriteria;
import com.maicard.product.criteria.CartCriteria;
import com.maicard.product.domain.Activity;
import com.maicard.product.domain.ActivityLog;
import com.maicard.product.domain.Product;
import com.maicard.product.service.ActivityLogService;
import com.maicard.product.service.ActivityProcessor;
import com.maicard.product.service.ActivityService;
import com.maicard.product.service.CartService;
import com.maicard.product.service.ProductService;
import com.maicard.security.domain.User;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.FrontUserService;
import com.maicard.site.domain.Document;
import com.maicard.site.domain.Template;
import com.maicard.site.service.DocumentService;
import com.maicard.site.service.TemplateService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.ObjectType;
import com.maicard.standard.OperateResult;
import com.maicard.standard.PriceType;
import com.maicard.standard.SecurityStandard.UserTypes;
import com.maicard.standard.TransactionStandard.TransactionStatus;
import com.maicard.wpt.domain.WeixinPlatformInfo;
import com.maicard.wpt.service.WeixinPlatformService;

/**
 * 活动控制器
 * 
 * 
 * @author NetSnake
 * @date 2012-11-13
 */

@Controller
@RequestMapping("/activity")
public class ActivityController extends BaseController {

	@Resource
	private ActivityService activityService;
	@Resource
	private ActivityLogService activityLogService;
	@Resource
	private CertifyService certifyService;
	@Resource
	private ConfigService configService;
	@Resource
	private DocumentService documentService;
	@Resource
	private FrontUserService frontUserService;
	@Resource
	private PriceService priceService;
	@Resource
	private ProductService productService;
	@Resource
	private TemplateService templateService;
	@Resource
	private ApplicationContextService applicationContextService;
	@Resource
	private CouponService couponService;
	@Resource
	private CartService cartService;

	@Resource
	private CouponModelService couponModelService;
	@Resource
	private WeixinPlatformService weixinPlatformService;

	private final SimpleDateFormat sdf = new SimpleDateFormat(
			CommonStandard.defaultDateFormat);

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/{activityCode}/index")
	@IgnoreLoginCheck
	public String getActivity(HttpServletRequest request,
			HttpServletResponse response, ModelMap map,
			@PathVariable String activityCode,
			@RequestParam(value = "v", required = false) String version)
			throws Exception {

		// String uuidStr = request.getParameter("uuid");
		// ////////////////////////// 标准检查流程 ///////////////////////////////////
		User frontUser = certifyService.getLoginedUser(request, response,
				UserTypes.frontUser.getId());

		// if(frontUser == null || frontUser.getCurrentStatus() !=
		// UserStatus.normal.getId()){
		// map.put("message", new
		// EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录，请先登录"));
		// return CommonStandard.frontMessageView;
		// }
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

		// if(frontUser.getOwnerId() != ownerId){
		// logger.error("用户[" + frontUser.getUuid() + "]的ownerId[" +
		// frontUser.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
		// map.put("message", new EisMessage(EisError.ownerNotMatch.getId(),
		// "您尚未登录，请先登录"));
		// return CommonStandard.frontMessageView;
		// }
		// ////////////////////////// 标准检查流程结束 ///////////////////////////////

		activityCode = activityCode.split("_")[0];

		final String baseView = "activity/" + activityCode + "/index";
		String view = baseView;

		if (version != null && NumericUtils.isIntNumber(version)) {
			view += "_";
			view += version;
			logger.debug("链接中有版本号，使用view:" + view);
		} else {
			if (frontUser != null) {
				String pageVersion = frontUser
						.getExtraValue(DataName.weixinPageVersion.toString());
				if (StringUtils.isBlank(pageVersion)) {
					logger.debug("链接中未提供版本号，用户也没有扩展数据weixinPageVersion");
				} else {
					if (NumericUtils.isIntNumber(pageVersion)) {
						view = baseView + "_" + version.trim();
						logger.debug("链接中未提供版本号，但用户有扩展数据weixinPageVersion="
								+ pageVersion + ",使用view:" + view);
					}

				}
			}
		}

		ActivityCriteria activityCriteria = new ActivityCriteria();
		activityCriteria.setActivityCode(activityCode);
		activityCriteria.setOwnerId(ownerId);
		List<Activity> activityList = activityService.list(activityCriteria);
		if (activityList == null || activityList.size() < 1) {
			logger.error("系统中没有" + activityCode + "活动！");
			return view;
		}
		Activity activity = activityList.get(0);

		ActivityProcessor activityProcessor = applicationContextService
				.getBeanGeneric(activity.getProcessor());
		if (activityProcessor == null) {
			logger.error("找不到活动[" + activity.getActivityId() + "]指定的处理器["
					+ activity.getProcessor());
			map.put("message", new EisMessage(EisError.systemDataError.getId(),
					"系统异常", "找不到指定的数据"));
			return CommonStandard.frontMessageView;
		}

		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("request", request);
		parameterMap.put("map", map);
		parameterMap.put("identify", activityCode);
		logger.info("传参[" + parameterMap + "]");
		long t1 = new Date().getTime();

		String action = request.getParameter("action");
		logger.debug("action : " + (action == null ? "空" : action));
		EisMessage result = activityProcessor.execute(action, activity,
				frontUser, parameterMap);
		logger.debug("活动[" + activity.getActivityId() + "]执行结果: " + result
				+ ",耗时:" + (new Date().getTime() - t1) + "毫秒");
		if (result == null || result.getAttachment() == null
				|| result.getAttachment().size() < 1) {
			map.put("message", result);
			return view;
		}
		Object a2 = result.getAttachment().get("activity");
		if (a2 != null && a2 instanceof Activity) {
			map.put("activity", (Activity) a2);
		}
		Document document = null;
		Object dObject = result.getAttachment().get("document");
		if (dObject != null && dObject instanceof Document) {
			document = (Document) dObject;
			map.put("document", document);
		}
		Object pObject = result.getAttachment().get("product");
		Product product = null;
		if (pObject != null && pObject instanceof Product) {
			product = (Product) pObject;
			map.put("product", product);
		}

		Object productMapObject = result.getAttachment().get("productMap");
		if (productMapObject != null && productMapObject instanceof Map) {
			map.put("productMap", (Map<String, Product>) productMapObject);
		}

		int giftGrade = 0;
		Object giftGradeObject = result.getAttachment().get("giftGrade");
		if (giftGradeObject != null
				&& NumericUtils.isNumeric(giftGradeObject.toString())) {
			giftGrade = Integer.parseInt(giftGradeObject.toString());
		}
		if (giftGrade > 0) {
			/*
			 * CouponCriteria couponCriteria = new CouponCriteria(ownerId); if
			 * (activity.getActivityId() == 30 || activity.getActivityId() ==
			 * 31) {
			 * 
			 * } else{ couponCriteria.setUuid(frontUser.getUuid()); }
			 * List<Coupon> couponList = couponService.list(couponCriteria); if
			 * (couponList == null || couponList.size() < 1) {
			 * logger.debug("没有领取码"); }
			 */
		}

		// result.setAttachment(null);
		map.put("result", result);
		map.put("identify", activityCode);

		String templateLocation = null;
		if (document == null) {
			logger.warn("找不到活动[" + activity.getActivityCode() + "]对应的文章");
		} else {
			if (document.getTemplateId() > 0) {
				// 尝试获取文档自定义模版
				Template template = templateService.select(document
						.getTemplateId());
				if (template != null && template.getOwnerId() != ownerId) {
					logger.error("尝试获取的模版[" + template.getTemplateId()
							+ "]，其ownerId[" + document.getOwnerId()
							+ "]与系统会话中的[" + ownerId + "]不一致");
					return CommonStandard.frontMessageView;
				}
				if (template != null
						&& template.getCurrentStatus() == BasicStatus.normal
								.getId()) {
					templateLocation = template.getTemplateLocation();
				}
			}
			if (templateLocation == null) {
				logger.debug("文档未定义模版或模版异常,尝试使用节点的模版");
				templateLocation = document.getDefaultNode()
						.getTemplateLocation();
				templateLocation = templateLocation.replaceAll("\\.\\w+$", "")
						.replaceAll("jsp$", "");
			}

		}

		if (frontUser != null) {
			map.put("frontUser", frontUser);
			CartCriteria cartCriteria2 = new CartCriteria(
					frontUser.getOwnerId());
			cartCriteria2.setUuid(frontUser.getUuid());
			cartCriteria2.setCurrentStatus(TransactionStatus.inCart.getId());
			cartCriteria2.setOrderType(CartCriteria.ORDER_TYPE_TEMP);
			int cartCount = cartService.count(cartCriteria2);
			map.put("cartCount", cartCount);
		} else {
			map.put("frontUser", frontUser);
			CartCriteria cartCriteria2 = new CartCriteria(activity.getOwnerId());
			// cartCriteria2.setUuid(frontUser.getUuid());
			cartCriteria2.setCurrentStatus(TransactionStatus.inCart.getId());
			cartCriteria2.setOrderType(CartCriteria.ORDER_TYPE_TEMP);
			int cartCount = cartService.count(cartCriteria2);
			map.put("cartCount", cartCount);
		}
		if (templateLocation != null) {
			view = templateLocation;
		}
		return view;

	}

	@RequestMapping(value = "/hexiao")
	public String hexiao(HttpServletRequest request,
			HttpServletResponse response, ModelMap map) {

		long ownerId = NumericUtils.parseLong(map.get("ownerId"));
		long sitePartnerId = NumericUtils.parseLong(map
				.get(DataName.sitePartnerId.toString()));
		User user = certifyService.getLoginedUser(request, response,
				UserTypes.frontUser.getId());
		if (user == null) {
			map.put("message",
					new EisMessage(EisError.userNotFoundInRequest.getId(),
							"你尚未登录，必须在登陆后才能参加活动"));
			return CommonStandard.frontMessageView;
		}

		CouponModelCriteria couponModelCriteria = new CouponModelCriteria();
		couponModelCriteria.setOwnerId(user.getOwnerId());
		couponModelCriteria.setCurrentStatus(BasicStatus.normal.getId());
		couponModelCriteria.setCouponCode("p1hjqwk--x7zPJRvKyVWzNM8KDKA");

		EisMessage result = new EisMessage(OperateResult.success.getId(), null);
		List<CouponModel> couponModelList = couponModelService
				.list(couponModelCriteria);
		logger.debug("优惠券数量 ： " + couponModelList.size());
		if (couponModelList == null || couponModelList.size() < 1) {
			logger.error("优惠券为0");
			result.setContent("系统可领的优惠券为0");
			map.put("message", result);
			return CommonStandard.frontMessageView;
		}
		CouponModel couponModel = couponModelList.get(0);

		Coupon coupon = new Coupon(couponModel);
		long timeStamp = System.currentTimeMillis();
		String ts = String.valueOf(timeStamp / 1000);

		weixinPlatformService.convertToWeixinCoupon(coupon, 0, null, null);

		logger.debug("map : " + map);
		map.put("timestamp", timeStamp);

		final String nonceStr = DigestUtils
				.md5Hex(UUID.randomUUID().toString());
		String currentUrl = null;
		if (map.get("frontLoginUrl") != null) {
			currentUrl = map.get("frontLoginUrl").toString();
			final String sha1Str = weixinPlatformService.cardSign(timeStamp,
					currentUrl, nonceStr, coupon.getCouponCode(),
					sitePartnerId, ownerId, 0);
			final String signature = weixinPlatformService.makeJsSignature(
					timeStamp, currentUrl, nonceStr, couponModel.getOwnerId(),
					0);
			logger.debug("放入新的微信签名数据");
			map.put("cardSign", sha1Str);
			map.put("signature", signature);

			String appId = null;
			if (sitePartnerId > 0) {
				appId = weixinPlatformService
						.getClientAccessToken(sitePartnerId);
			} else {
				WeixinPlatformInfo wxInfo = weixinPlatformService
						.getSingleWeixinPlatformInfo(ownerId);
				if (wxInfo == null) {
					logger.error("找不到云ID=" + ownerId + "的公众号配置");
				} else {
					appId = wxInfo.appId;
				}

			}
			map.put("appid", appId);
		}
		weixinPlatformService.convertToWeixinCoupon(coupon, 0, null, ts);
		logger.debug("map ： " + map);
		result.getAttachment().put("couponModel", coupon);
		map.put("result", result);
		map.put("nonceStr", nonceStr);
		return "activity/test/hexiao";
	}

	// 查看该用户当日能免费参与的活动次数和已参与次数
	@RequestMapping(method = RequestMethod.GET)
	public String list(
			HttpServletRequest request,
			HttpServletResponse response,
			ModelMap map,
			@ModelAttribute("activityCriteria") ActivityLogCriteria activityLogCriteria) {

		int activityId = 0;
		if (StringUtils.isNumeric(request.getParameter("activityId"))) {
			activityId = Integer.parseInt(request.getParameter("activityId"));
		}

		User frontUser = certifyService.getLoginedUser(request, response,
				UserTypes.frontUser.getId());
		if (frontUser == null) {
			map.put("message",
					new EisMessage(EisError.userNotFoundInRequest.getId(),
							"你尚未登录，必须在登陆后才能参加活动"));
			return CommonStandard.frontMessageView;
		}
		int totalFreePlayCount = configService.getIntValue(
				DataName.totalFreePlayCount.toString(), frontUser.getOwnerId());

		activityLogCriteria.setUuid(frontUser.getUuid());
		activityLogCriteria.setBeginTime(DateUtils.truncate(new Date(),
				Calendar.DAY_OF_MONTH));
		int playCount = activityLogService.count(activityLogCriteria);
		logger.info("用户[" + frontUser.getUuid() + "/" + frontUser.getUsername()
				+ "]从" + sdf.format(activityLogCriteria.getBeginTime())
				+ "开始参与" + (activityId == 0 ? "所有" : activityId + "号")
				+ "活动的次数是" + playCount + ",是否付费:"
				+ activityLogCriteria.getPayFeeJoin());
		map.put("playCount", playCount);
		map.put("totalFreePlayCount", totalFreePlayCount);
		return CommonStandard.frontMessageView;
	}

	// 查看该用户是否能参与活动
	@RequestMapping(value = "/{activityId}", method = RequestMethod.GET)
	public String get(HttpServletRequest request, HttpServletResponse response,
			ModelMap map, @PathVariable("activityId") Integer activityId)
			throws Exception {
		logger.debug("Url : " + request.getRequestURL());
		if (activityId == null || activityId == 0) {
			return CommonStandard.frontMessageView;
		}
		Activity activity = activityService.select(activityId);
		if (activity == null) {
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.getId(),
					"活动不存在"));
			return CommonStandard.frontMessageView;
		}
		if (activity.getCurrentStatus() != BasicStatus.normal.getId()) {
			map.put("message", new EisMessage(EisError.activityClosed.getId(),
					"活动尚未开放或已结束"));
			return CommonStandard.frontMessageView;
		}
		if (StringUtils.isBlank(activity.getProcessor())) {
			logger.warn("活动[" + activity.getActivityId() + "]未指定处理器");

			return CommonStandard.frontMessageView;

		}
		User frontUser = certifyService.getLoginedUser(request, response,
				UserTypes.frontUser.getId());
		if (frontUser == null) {
			map.put("message",
					new EisMessage(EisError.userNotFoundInRequest.getId(),
							"你尚未登录，必须在登陆后才能参加活动"));
			return CommonStandard.frontMessageView;
		}
		if (activity.getOwnerId() != frontUser.getOwnerId()) {
			logger.error(activity.getActivityId() + "#活动的ownerId["
					+ activity.getOwnerId() + "]与用户[" + frontUser.getUuid()
					+ "]的ownerId[" + frontUser.getOwnerId() + "]不一致");
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.getId(),
					"活动不存在"));
			return CommonStandard.frontMessageView;
		}
		if (StringUtils.isBlank(activity.getPromotion())) {
			logger.error("活动对象的数据为空");
			return null;
		}
		String[] promotions = activity.getPromotion().split(";");
		if (promotions == null || promotions.length < 1) {
			logger.error("无法解析活动对象的数据:" + activity.getPromotion());
			return null;
		}
		// EisMessage result=null;
		Activity a2 = activity.clone();
		// result = activityService.prepare(a2, frontUser,
		// HttpUtils.getRequestDataMap(request));
		// logger.debug("result.getOperateCode() " + result.getOperateCode() +
		// " price : " + result.getAttachment());

		ActivityProcessor activityProcessor = null;
		try {
			activityProcessor = (ActivityProcessor) applicationContextService
					.getBean(activity.getProcessor());
		} catch (Exception e) {
			if (activityProcessor == null) {
				logger.error("找不到活动[" + activity.getActivityId() + "]指定的处理器["
						+ activity.getProcessor());
				// return new EisMessage(EisError.activityClosed.getId(),
				// "活动尚未开放或已结束");
				logger.error("活动可能被关闭了，不能参与");
			}
		}
		EisMessage result = activityProcessor.prepare(a2, frontUser,
				HttpUtils.getRequestDataMap(request));

		String message = result.getMessage();
		logger.error("message  " + message);
		if (StringUtils.isEmpty(message)) {
			result.setMessage("0");
			logger.debug("message :" + result.getMessage());
		}

		ActivityLogCriteria activityLogCriteria = new ActivityLogCriteria();
		activityLogCriteria.setActivityId((int) a2.getActivityId());
		activityLogCriteria.setActivityType(a2.getActivityType());
		activityLogCriteria.setUuid(frontUser.getUuid());
		List<ActivityLog> activityLogLists = activityLogService
				.list(activityLogCriteria);
		logger.debug("activityLogLists : " + activityLogLists.size());
		if (activityLogLists == null || activityLogLists.size() < 1) {
			map.put("message", "您没参加过这个'" + a2.getActivityName() + "'活动");
		} else {
			ActivityLog activityLog = activityLogLists.get(0);
			map.put("activityLog", activityLog);
		}

		if (a2.getPromotion().indexOf("#") < 1) {
			Product product = productService.select(a2.getPromotion(),
					a2.getOwnerId());
			if (product == null) {
				logger.error("找不到" + activity.getActivityId() + "#活动的对应产品:"
						+ activity.getPromotion());
				return CommonStandard.frontMessageView;
			}
			if (product.getAvailableCount() <= 0) {
				logger.debug(activity.getActivityId() + "#活动的对应产品:"
						+ activity.getPromotion() + "，数量已经不足");
				return CommonStandard.frontMessageView;
			}
			Product p = product.clone();
			PriceCriteria priceCriteria = new PriceCriteria();
			priceCriteria.setObjectId(p.getProductId());
			priceCriteria.setObjectType(ObjectType.product.toString());
			priceCriteria.setPriceType(PriceType.PRICE_PROMOTION.toString());
			priceCriteria.setOwnerId(p.getOwnerId());
			List<Price> priceList = priceService.list(priceCriteria);
			if (priceList == null || priceList.size() < 1) {
				logger.error(activity.getActivityId() + "#活动没有价钱");
				return CommonStandard.frontMessageView;
			}
			Price price = priceList.get(0);

			Map<String, Product> productMap = new HashMap<String, Product>();
			p.setPrice(price);
			p.setTransactionToken(priceService.generateTransactionToken(price,
					frontUser.getUuid()));
			productMap.put(p.getProductCode(), p);
			map.put("productMap", productMap);
		}

		map.put("currentTime", sdf.format(new Date()));
		map.put("activity", a2);
		map.put("message", result);
		// logger.info("信息"+playCount);
		logger.info("返回消息:" + result.getMessageId() + "消耗："
				+ result.getMessageLevel());
		return CommonStandard.frontMessageView;

	}

	// 提交参与活动
	@RequestMapping(value = "/{activityId}", method = RequestMethod.POST)
	@IgnoreLoginCheck
	public String submit(HttpServletRequest request,
			HttpServletResponse response, ModelMap map,
			@PathVariable("activityId") Integer activityId) {
		logger.debug("POST Url : " + request.getRequestURL());
		if (activityId == null || activityId == 0) {
			return CommonStandard.frontMessageView;
		}
		Activity activity = activityService.select(activityId);
		if (activity == null) {
			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.getId(),
					"活动不存在"));
			return CommonStandard.frontMessageView;
		}
		if (activity.getCurrentStatus() != BasicStatus.normal.getId()) {
			map.put("message", new EisMessage(EisError.activityClosed.getId(),
					"活动尚未开放或已结束"));
			return CommonStandard.frontMessageView;
		}
		if (StringUtils.isBlank(activity.getProcessor())) {
			logger.warn("活动[" + activity.getActivityId() + "]未指定处理器");
			map.put("message", new EisMessage(EisError.activityClosed.getId(),
					"活动尚未开放或已结束"));
			return CommonStandard.frontMessageView;
		}

		User frontUser = certifyService.getLoginedUser(request, response,
				UserTypes.frontUser.getId());
		long uuid = 0;
		if (frontUser != null) {
			uuid = frontUser.getUuid();
		}

		/*
		 * if(frontUser == null){ map.put("message", new
		 * EisMessage(EisError.userNotFoundInRequest
		 * .getId(),"你尚未登录，必须在登陆后才能参加活动")); return
		 * CommonStandard.frontMessageView; } if(activity.getOwnerId() !=
		 * frontUser.getOwnerId()){ logger.error(activity.getActivityId() +
		 * "#活动的ownerId[" + activity.getOwnerId() + "]与用户[" +
		 * frontUser.getUuid() + "]的ownerId[" + frontUser.getOwnerId() +
		 * "]不一致"); map.put("message", new
		 * EisMessage(EisError.objectIsNull.getId(),"活动不存在")); return
		 * CommonStandard.frontMessageView; }
		 */
		// EisMessage result=null;
		// result = activityService.execute(activity, frontUser,
		// HttpUtils.getRequestDataMap(request));

		ActivityProcessor activityProcessor = null;
		try {
			activityProcessor = (ActivityProcessor) applicationContextService
					.getBean(activity.getProcessor());
		} catch (Exception e) {
			if (activityProcessor == null) {
				logger.error("找不到活动[" + activity.getActivityId() + "]指定的处理器["
						+ activity.getProcessor());
				// return new EisMessage(EisError.activityClosed.getId(),
				// "活动尚未开放或已结束");
				logger.error("活动可能被关闭了，不能参与");
			}
		}
		EisMessage result = activityProcessor.execute(null, activity,
				frontUser, map);

		logger.debug("result : " + result.getMessage() + " operateCode ： "
				+ result.getOperateCode());
		if (result.getOperateCode() == 710007) {
			Product product = productService.select(activity.getPromotion(),
					activity.getOwnerId());
			if (product == null) {
				logger.error("找不到" + activity.getActivityId() + "#活动的对应产品:"
						+ activity.getPromotion());
				return CommonStandard.frontMessageView;
			}
			if (product.getAvailableCount() <= 0) {
				logger.debug(activity.getActivityId() + "#活动的对应产品:"
						+ activity.getPromotion() + "，数量已经不足");
				return CommonStandard.frontMessageView;
			}

			PriceCriteria priceCriteria = new PriceCriteria();
			priceCriteria.setObjectId(product.getProductId());
			priceCriteria.setObjectType(ObjectType.product.toString());
			priceCriteria.setPriceType(PriceType.PRICE_PROMOTION.toString());
			priceCriteria.setOwnerId(product.getOwnerId());
			List<Price> priceList = priceService.list(priceCriteria);
			if (priceList == null || priceList.size() < 1) {
				logger.error(activity.getActivityId() + "#活动没有价钱");
				return CommonStandard.frontMessageView;
			}
			Price price = priceList.get(0);

			String transactionToken = priceService.generateTransactionToken(
					price, uuid);
			logger.debug("transactionToken ：" + transactionToken);
			map.put("transactionToken", transactionToken);
		}

		ActivityLogCriteria activityLogCriteria = new ActivityLogCriteria();
		activityLogCriteria.setActivityId((int) activity.getActivityId());
		activityLogCriteria.setActivityType(activity.getActivityType());
		activityLogCriteria.setUuid(uuid);

		List<ActivityLog> activityLogLists = activityLogService
				.list(activityLogCriteria);
		ActivityLog activityLog = null;
		if (activityLogLists == null || activityLogLists.size() < 1) {
			map.put("message", "您没参加过这个'" + activity.getActivityName() + "'活动");
		} else {
			activityLog = activityLogLists.get(0);
		}

		map.put("activityLog", activityLog);
		map.put("message", result);
		logger.info("奖品" + result.getReplyMessageId() + "#"
				+ result.getMessageId() + "#" + result.getMessageLevel());
		return CommonStandard.frontMessageView;

	}

	// 活动详情 ////{activityCode} /"+activityCode+" @PathVariable String
	// activityCode
	@RequestMapping(value = "/{activityCode}/detail", method = RequestMethod.GET)
	public String detail(HttpServletRequest request,
			HttpServletResponse response, ModelMap map,
			@PathVariable String activityCode) {
		String view = "activity/" + activityCode + "/detail";
		Date date = new Date();
		String format = sdf.format(date);
		map.put("time", format);
		return view;
	}

	// 宝洁投票分享
	@RequestMapping(value = "/baojieVoteShare", method = RequestMethod.GET)
	public String baojieVoteShare(HttpServletRequest request,
			HttpServletResponse response, ModelMap map) {
		String view = "activity/baojieVoteShare";

		return view;
	}
}
