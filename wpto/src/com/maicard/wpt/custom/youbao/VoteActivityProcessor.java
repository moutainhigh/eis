package com.maicard.wpt.custom.youbao;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.maicard.captcha.service.CaptchaService;
import com.maicard.common.base.BaseService;
import com.maicard.common.base.UUIDFilenameGenerator;
import com.maicard.common.criteria.CommentCriteria;
import com.maicard.common.criteria.DataDefineCriteria;
import com.maicard.common.criteria.ExtraDataCriteria;
import com.maicard.common.domain.DataDefine;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.domain.ExtraData;
import com.maicard.common.service.CenterDataService;
import com.maicard.common.service.CommentService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.DataDefineService;
import com.maicard.common.service.ExtraDataService;
import com.maicard.common.util.NumericUtils;
import com.maicard.http.HttpClientPoolV3;
import com.maicard.http.HttpUtilsV3;
import com.maicard.money.criteria.CouponCriteria;
import com.maicard.money.criteria.CouponModelCriteria;
import com.maicard.money.domain.Coupon;
import com.maicard.money.domain.CouponModel;
import com.maicard.money.service.CouponModelService;
import com.maicard.money.service.CouponProcessor;
import com.maicard.product.criteria.ActivityLogCriteria;
import com.maicard.product.domain.Activity;
import com.maicard.product.domain.ActivityLog;
import com.maicard.product.service.ActivityLogService;
import com.maicard.product.service.ActivityProcessor;
import com.maicard.security.criteria.UserCriteria;
import com.maicard.security.domain.User;
import com.maicard.security.service.FrontUserService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.KeyConstants;
import com.maicard.standard.ObjectType;
import com.maicard.standard.OperateCode;
import com.maicard.standard.OperateResult;
import com.maicard.standard.ServiceStatus;
import com.maicard.wpt.service.WeixinPlatformService;

/**
 * 投票活动 用户可以参与活动，上传自己的照片让别人投票 也可以点击其他用户的照片进行投票
 * 
 * 
 * @author NetSnake
 * @date 2016-12-04
 */
public class VoteActivityProcessor extends BaseService implements ActivityProcessor {
	@Resource
	private ActivityLogService activityLogService;

	@Resource
	private ExtraDataService extraDataService;

	@Resource
	private FrontUserService frontUserService;

	@Resource
	private CommentService commentService;
	@Resource
	private ConfigService configService;
	@Resource
	private DataDefineService dataDefineService;
	@Resource
	private CouponModelService couponModelService;
	@Resource
	private WeixinPlatformService weixinPlatformService;
	@Resource
	private CenterDataService centerDataService;
	@Resource
	private CaptchaService captchaService;

	private String documentUploadSaveDir;

	@PostConstruct
	public void init() {
		documentUploadSaveDir = configService.getValue(DataName.userUploadDir.toString(), 0);
		if (documentUploadSaveDir != null) {
			documentUploadSaveDir = documentUploadSaveDir.replaceAll("/$", "");
		}
	}

	private final SimpleDateFormat sdf = new SimpleDateFormat(CommonStandard.defaultDateFormat);
	private static final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");

	@SuppressWarnings("unchecked")
	@Override
	public EisMessage execute(String action, Activity activity, Object targetObject, Object parameter) {
		if (activity == null) {
			logger.error("尝试进行的活动为空");
			return new EisMessage(EisError.OBJECT_IS_NULL.getId(), "活动为空");
		}
		User frontUser = null;
		// if (targetObject == null) {
		// logger.error("尝试进行的活动目标对象为空");
		// return new EisMessage(EisError.objectIsNull.getId(), "活动用户为空");
		// }
		// if (!(targetObject instanceof User)) {
		// logger.error("尝试进行的活动目标对象不是用户");
		// return new EisMessage(EisError.objectIsNull.getId(), "活动目标对象不是用户");
		// }
		frontUser = (User) targetObject;

		if (parameter == null) {
			logger.error("尝试进行的活动目标参数为空");
			return new EisMessage(EisError.OBJECT_IS_NULL.getId(), "活动参数为空");
		}

		Map<String, Object> requestData = null;
		try {
			requestData = (Map<String, Object>) parameter;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if (requestData == null) {
			logger.error("参数requestData为空");
			return new EisMessage(EisError.REQUIRED_PARAMETER.getId(), "requestData参数为空");
		}
		HttpServletRequest request = (HttpServletRequest) requestData.get("request");

		HashMap<String, Object> attachment = new HashMap<String, Object>();
		EisMessage resultMessage = new EisMessage();
		attachment.put("activity", activity);
		if (activity.getCurrentStatus() == ServiceStatus.closed.getId()) {
			logger.debug(activity.getActivityId() + "#活动状态是:" + activity.getCurrentStatus() + ",已关闭");
			resultMessage.setAttachment(attachment);
			resultMessage.setOperateCode(EisError.activityClosed.getId());
			resultMessage.setMessage("活动已关闭");
			return resultMessage;
		}
		if (activity.getCurrentStatus() == ServiceStatus.waitingOpen.getId()) {
			logger.debug(activity.getActivityId() + "#活动状态是:" + activity.getCurrentStatus() + ",尚未开启");
			resultMessage.setAttachment(attachment);
			resultMessage.setOperateCode(EisError.notActive.getId());
			resultMessage.setMessage("活动尚未开始");
			return resultMessage;
		}
		// if(activity.getBeginTime() != null &&
		// activity.getBeginTime().after(new Date())){
		// logger.debug(activity.getActivityId() + "#活动开启时间是" +
		// sdf.format(activity.getBeginTime()) + ",尚未开启");
		// resultMessage.setAttachment(attachment);
		// resultMessage.setOperateCode(EisError.notActive.getId());
		// resultMessage.setMessage("活动尚未开始");
		// return resultMessage;
		// }
		// if(activity.getEndTime() != null && activity.getEndTime().before(new
		// Date())){
		// logger.debug(activity.getActivityId() + "#活动结束时间是" +
		// sdf.format(activity.getEndTime()) + ",已结束");
		// resultMessage.setAttachment(attachment);
		// resultMessage.setOperateCode(EisError.activityClosed.getId());
		// resultMessage.setMessage("活动尚未开始");
		// return resultMessage;
		// }

		// 判断字符串是否为空白是就返回true，否就返回false
		if (StringUtils.isBlank(action)) {
			return fetch(activity, frontUser, request);
		}
		if (action.equals("SEARCH")) {
			EisMessage searchResult = search(activity, frontUser, request);
			if (searchResult.getOperateCode() != OperateResult.success.getId()) {
				return searchResult;
			}
			return searchResult;
		}

		if (action.equals("JOIN_CHECK")) {

			if (activity.getBeginTime() != null && activity.getBeginTime().after(new Date())) {
				logger.debug(activity.getActivityId() + "#活动开启时间是" + sdf.format(activity.getBeginTime()) + ",尚未开启");
				resultMessage.setAttachment(attachment);
				resultMessage.setOperateCode(EisError.notActive.getId());
				resultMessage.setMessage("活动尚未开始");
				return resultMessage;
			}
			if (activity.getEndTime() != null && activity.getEndTime().before(new Date())) {
				logger.debug(activity.getActivityId() + "#活动结束时间是" + sdf.format(activity.getEndTime()) + ",已结束");
				resultMessage.setAttachment(attachment);
				resultMessage.setOperateCode(EisError.activityClosed.getId());
				resultMessage.setMessage("活动尚未开始");
				return resultMessage;
			}

			// 检查用户是否能参与上传图片
			int checkResult = joinCheck(activity, frontUser);
			if (checkResult == OperateResult.success.getId()) {
				return new EisMessage(checkResult, "用户可以参与活动");
			} else {
				return new EisMessage(checkResult, "用户不能参与活动");
			}
		}
		if (action.equals("JOIN_GAME")) {
			if (activity.getBeginTime() != null && activity.getBeginTime().after(new Date())) {
				logger.debug(activity.getActivityId() + "#活动开启时间是" + sdf.format(activity.getBeginTime()) + ",尚未开启");
				resultMessage.setAttachment(attachment);
				resultMessage.setOperateCode(EisError.notActive.getId());
				resultMessage.setMessage("活动尚未开始");
				return resultMessage;
			}
			if (activity.getEndTime() != null && activity.getEndTime().before(new Date())) {
				logger.debug(activity.getActivityId() + "#活动结束时间是" + sdf.format(activity.getEndTime()) + ",已结束");
				resultMessage.setAttachment(attachment);
				resultMessage.setOperateCode(EisError.activityClosed.getId());
				resultMessage.setMessage("活动尚未开始");
				return resultMessage;
			}

			String captcha = request.getParameter("captcha");
			String phone = request.getParameter("phone");
			if (captcha == null || captcha == "") {
				return new EisMessage(OperateResult.failed.getId(), "验证码不能为空");
			}
			if (phone == null || phone == "") {
				return new EisMessage(OperateResult.failed.getId(), "手机号不能为空");
			}
			// 检查手机验证码
			// 获取REDIS中的验证码
			String key = KeyConstants.REGISTER_SMS_INSTANCE_PREFIX + "#" + activity.getOwnerId() + "#" + phone;
			String oldSign = centerDataService.get(key);
			if (StringUtils.isBlank(oldSign)) {
				logger.warn("在REDIS中找不到重置密码的sign:" + key);
				return new EisMessage(OperateResult.failed.getId(), "短信验证码失效，请重新操作");
			}
			if (!oldSign.equals(captcha)) {
				logger.warn("在REDIS中找到的重置密码的sign:" + oldSign + ",与用户提交的:" + captcha + "不一致");
				return new EisMessage(OperateResult.failed.getId(), "短信验证码错误");
			}
			UserCriteria userCriteria = new UserCriteria(activity.getOwnerId());
			userCriteria.setUsername(phone);
			List<User> list = frontUserService.list(userCriteria);
			if (list == null || list.size() < 1) {
				return new EisMessage(OperateResult.failed.getId(), "没有找到[" + phone + "]的用户");
			}
			frontUser = list.get(0);
			// 用户上传图片
			logger.info("用户上传图片");
			int joinResult = join(activity, frontUser, request, phone);
			if (joinResult == -1) {
				return new EisMessage(joinResult, "您已上传过照片了！");
			}
			logger.debug("参与结果 ：" + joinResult);
			if (joinResult == OperateResult.success.getId()) {
				// 领取优惠券
				EisMessage fetchCoupon = fetchCoupon(activity, frontUser, parameter);
				return fetchCoupon;
				// return new EisMessage(joinResult, "参与活动成功");
			} else {
				return new EisMessage(joinResult, "参与活动失败");
			}

		}
		if (action.equals("VOTE")) {

			// 用户投票
			EisMessage voteResult = vote(activity, frontUser, request);
			if (voteResult.getOperateCode() == OperateResult.success.getId()) {
				// 领取优惠券
				EisMessage fetchCoupon = fetchCoupon(activity, frontUser, parameter);
				return fetchCoupon;
				// return new EisMessage(voteResult, "参与活动成功");
			} else {
				return voteResult;
			}

		}

		// 返回默认数据显示
		return fetch(activity, frontUser, request);
	}

	// 前端页面上的搜索查询
	private EisMessage search(Activity activity, User frontUser, HttpServletRequest request) {
		if (activity == null) {
			logger.error("活动为空");
			return new EisMessage(EisError.systemDataError.getId(), "活动数据异常");
		}

		HashMap<String, Object> attachment = new HashMap<String, Object>();
		EisMessage resultMessage = new EisMessage();
		attachment.put("activity", activity);

		String identify = ServletRequestUtils.getStringParameter(request, "identify", null);
		if (StringUtils.isBlank(identify)) {
			identify = activity.getActivityCode();
		}

		if (activity.getCurrentStatus() == ServiceStatus.closed.getId()) {
			logger.debug(activity.getActivityId() + "#活动状态是:" + activity.getCurrentStatus() + ",已关闭");
			resultMessage.setAttachment(attachment);
			resultMessage.setOperateCode(EisError.activityClosed.getId());
			resultMessage.setMessage("活动已关闭");
			return resultMessage;
		}
		if (activity.getCurrentStatus() == ServiceStatus.waitingOpen.getId()) {
			logger.debug(activity.getActivityId() + "#活动状态是:" + activity.getCurrentStatus() + ",尚未开启");
			resultMessage.setAttachment(attachment);
			resultMessage.setOperateCode(EisError.notActive.getId());
			resultMessage.setMessage("活动尚未开始");
			return resultMessage;
		}
		// if(activity.getBeginTime() != null &&
		// activity.getBeginTime().after(new Date())){
		// logger.debug(activity.getActivityId() + "#活动开启时间是" +
		// sdf.format(activity.getBeginTime()) + ",尚未开启");
		// resultMessage.setAttachment(attachment);
		// resultMessage.setOperateCode(EisError.notActive.getId());
		// resultMessage.setMessage("活动尚未开始");
		// return resultMessage;
		// }
		// if(activity.getEndTime() != null && activity.getEndTime().before(new
		// Date())){
		// logger.debug(activity.getActivityId() + "#活动结束时间是" +
		// sdf.format(activity.getEndTime()) + ",已结束");
		// resultMessage.setAttachment(attachment);
		// resultMessage.setOperateCode(EisError.activityClosed.getId());
		// resultMessage.setMessage("活动尚未开始");
		// return resultMessage;
		// }
		int index = 0;
		String parameter = request.getParameter("number");
		if (NumericUtils.isIntNumber(parameter)) {
			index = Integer.parseInt(parameter);
		}
		if (index <= 0) {
			resultMessage.setAttachment(attachment);
			resultMessage.setOperateCode(OperateResult.failed.getId());
			resultMessage.setMessage("请提交正确的查询信息");
			return resultMessage;
		}
		logger.debug("要搜索的编号是：" + index);

		ActivityLogCriteria activityLogCriteria = new ActivityLogCriteria();
		activityLogCriteria.setAction(OperateCode.JOIN_GAME.toString());
		activityLogCriteria.setActivityId(activity.getActivityId());
		if (activity.getBeginTime() == null) {
			activityLogCriteria.setBeginTime(DateUtils.addDays(new Date(), -30));
		} else {
			activityLogCriteria.setBeginTime(activity.getBeginTime());
		}
		activityLogCriteria.setIndex(index);
		activityLogCriteria.setCurrentStatus(CommentCriteria.STATUS_PUBLISHED);
		activityLogCriteria.setOwnerId(activity.getOwnerId());
		List<ActivityLog> joinList = activityLogService.like(activityLogCriteria);
		if (joinList.size() < 1) {
			resultMessage.setAttachment(attachment);
			resultMessage.setOperateCode(OperateResult.failed.getId());
			resultMessage.setMessage("没找到编号为" + index + "的信息");
			return resultMessage;
		}
		logger.debug("模糊查询到【" + joinList.size() + "】条");

		for (ActivityLog activityLog : joinList) {
			User joinUser = frontUserService.select(activityLog.getUuid());
			// 统计票数
			ActivityLogCriteria activityLogCriteria2 = new ActivityLogCriteria();
			ExtraDataCriteria extraDataCriteria = new ExtraDataCriteria();
			ExtraDataCriteria extraDataCriteria1 = new ExtraDataCriteria();
			if (joinUser == null) {

			} else {
				activityLog.setExtraValue("joinUser", joinUser.getUsername());
				activityLogCriteria2.setPromotion(joinUser.getUuid() + "");
				extraDataCriteria.setUuid(joinUser.getUuid());
				extraDataCriteria1.setUuid(joinUser.getUuid());
			}
			DataDefine dataDefine = dataDefineService.select("userUpLoadImage");
			DataDefine dataDefine1 = dataDefineService.select("userUploadSummary");
			activityLogCriteria2.setAction(OperateCode.VOTE.toString());
			activityLogCriteria2.setActivityId(activity.getActivityId());
			activityLogCriteria2.setOwnerId(activity.getOwnerId());
			int voteNumber = activityLogService.count(activityLogCriteria2);

			extraDataCriteria.setTableName("activity_data");
			extraDataCriteria.setObjectType(ObjectType.activity.name());
			extraDataCriteria.setObjectId(activity.getActivityId());
			extraDataCriteria1.setTableName("activity_data");
			extraDataCriteria1.setObjectType(ObjectType.activity.name());
			extraDataCriteria1.setObjectId(activity.getActivityId());

			if (dataDefine != null && dataDefine1 != null) {
				extraDataCriteria.setDataDefineId(dataDefine.getDataDefineId());
				extraDataCriteria1.setDataDefineId(dataDefine1.getDataDefineId());

			}
			List<ExtraData> extraDataList = extraDataService.list(extraDataCriteria);
			List<ExtraData> extraDataList1 = extraDataService.list(extraDataCriteria1);
			// 增加心得字段
			extraDataList.addAll(extraDataList1);
			logger.debug("extraDataListSize : " + extraDataList.size());
			activityLog.setExtraDataList(extraDataList);
			activityLog.setFlag(voteNumber);
		}
		logger.debug("需要显示【" + joinList.size() + "】条");
		attachment.put("joinUserList", joinList);

		resultMessage.setAttachment(attachment);
		resultMessage.setOperateCode(OperateResult.success.getId());
		resultMessage.setMessage("活动处理完成");
		return resultMessage;
	}

	@SuppressWarnings("unchecked")
	private EisMessage fetchCoupon(Activity activity, User frontUser, Object parameter) {
		// String couponCode = "peDoPt6opOwr8IF2Q2GUFwwOMQ8I";//测试中百的
		// String couponCode = "peDoPtwrxI4L2gEHJ27wg3grnpKU";//现网中百测试的
		// String couponCode = "peDoPt4r_w8qZeJaSqX_8j8apNiU";//宝洁代金券
		CouponModelCriteria couponModelCriteria = new CouponModelCriteria();
		couponModelCriteria.setOwnerId(activity.getOwnerId());
		couponModelCriteria.setCurrentStatus(BasicStatus.normal.getId());
		// couponModelCriteria.setCouponCode(couponCode);
		couponModelCriteria.setIdentify(activity.getActivityCode());

		EisMessage result = new EisMessage(OperateResult.success.getId(), null);

		List<CouponModel> couponModelList = couponModelService.list(couponModelCriteria);
		logger.debug("优惠券数量 ： " + couponModelList.size());
		if (couponModelList == null || couponModelList.size() < 1) {
			logger.error("优惠券为0");
			result.setContent("系统可领的优惠券为0");
			return result;
		}
		CouponModel couponModel = couponModelList.get(0);

		CouponCriteria couponCriteria = new CouponCriteria();
		couponCriteria.setCouponCode(couponModel.getCouponCode());
		couponCriteria.setExtraCode(couponModel.getExtraCode());
		couponCriteria.setOwnerId(couponModel.getOwnerId());
		EisMessage fetchResult = null;/*testCouponProcessor.fetch(couponCriteria);
		if (fetchResult.getOperateCode() != OperateResult.success.id) {
			logger.warn("无法从有宝获取到对应的优惠券:" + couponModel.getExtraCode());
			return fetchResult;
		}*/

		Coupon coupon = ((List<Coupon>) fetchResult.getAttachmentData("couponList")).get(0);

		Map<String, Object> map = null;
		try {
			map = (Map<String, Object>) parameter;
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (map == null) {
			logger.error("活动未提供Map<String,Object>类型的parameter");
			return new EisMessage(EisError.REQUIRED_PARAMETER.getId(), "活动参数错误");
		}

		long timeStamp = System.currentTimeMillis();
		String ts = String.valueOf(timeStamp / 1000);
		if (map != null) {
			map.put("timestamp", timeStamp);

			final String nonceStr = DigestUtils.md5Hex(UUID.randomUUID().toString());
			String currentUrl = null;
			if (map.get("outShareUrl") != null) {
				currentUrl = map.get("outShareUrl").toString();
				final String sha1Str = weixinPlatformService.makeJsSignature(timeStamp, currentUrl, nonceStr,
						frontUser.getOwnerId(), 0);
				logger.debug("放入新的微信签名数据");
				map.put("signature", sha1Str);
			}
		}
		logger.debug("当前用户是:" + frontUser);
		weixinPlatformService.convertToWeixinCoupon(coupon, 0, null, ts);
		result.getAttachment().put("couponModel", coupon);
		logger.debug("map ： " + map);
		logger.debug("result:" + result);
		return result;
	}

	private EisMessage fetch(Activity activity, User frontUser, HttpServletRequest request) {
		if (activity == null) {
			logger.error("活动为空");
			return new EisMessage(EisError.systemDataError.getId(), "活动数据异常");
		}

		HashMap<String, Object> attachment = new HashMap<String, Object>();
		EisMessage resultMessage = new EisMessage();
		attachment.put("activity", activity);

		String identify = ServletRequestUtils.getStringParameter(request, "identify", null);
		if (StringUtils.isBlank(identify)) {
			identify = activity.getActivityCode();
		}

		if (activity.getCurrentStatus() == ServiceStatus.closed.getId()) {
			logger.debug(activity.getActivityId() + "#活动状态是:" + activity.getCurrentStatus() + ",已关闭");
			resultMessage.setAttachment(attachment);
			resultMessage.setOperateCode(EisError.activityClosed.getId());
			resultMessage.setMessage("活动已关闭");
			return resultMessage;
		}
		if (activity.getCurrentStatus() == ServiceStatus.waitingOpen.getId()) {
			logger.debug(activity.getActivityId() + "#活动状态是:" + activity.getCurrentStatus() + ",尚未开启");
			resultMessage.setAttachment(attachment);
			resultMessage.setOperateCode(EisError.notActive.getId());
			resultMessage.setMessage("活动尚未开始");
			return resultMessage;
		}
		// if(activity.getBeginTime() != null &&
		// activity.getBeginTime().after(new Date())){
		// logger.debug(activity.getActivityId() + "#活动开启时间是" +
		// sdf.format(activity.getBeginTime()) + ",尚未开启");
		// resultMessage.setAttachment(attachment);
		// resultMessage.setOperateCode(EisError.notActive.getId());
		// resultMessage.setMessage("活动尚未开始");
		// return resultMessage;
		// }
		// if(activity.getEndTime() != null && activity.getEndTime().before(new
		// Date())){
		// logger.debug(activity.getActivityId() + "#活动结束时间是" +
		// sdf.format(activity.getEndTime()) + ",已结束");
		// resultMessage.setAttachment(attachment);
		// resultMessage.setOperateCode(EisError.activityClosed.getId());
		// resultMessage.setMessage("活动尚未开始");
		// return resultMessage;
		// }
		// 获取从页面传入的page参数
		int rows = ServletRequestUtils.getIntParameter(request, "rows", CommonStandard.DEFAULT_FRONT_ROWS_PER_PAGE);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);

		// 获取活动的参与人
		ActivityLogCriteria activityLogCriteria = new ActivityLogCriteria();
		activityLogCriteria.setActivityId(activity.getActivityId());
		if (activity.getBeginTime() == null) {
			activityLogCriteria.setBeginTime(DateUtils.addDays(new Date(), -30));
		} else {
			activityLogCriteria.setBeginTime(activity.getBeginTime());
		}
		activityLogCriteria.setAction(OperateCode.JOIN_GAME.toString());
		activityLogCriteria.setOwnerId(activity.getOwnerId());
		activityLogCriteria.setCurrentStatus(CommentCriteria.STATUS_PUBLISHED); // 已审核的状态
		int totalRows = activityLogService.count(activityLogCriteria);
		if (totalRows < 1) {
			logger.info("当前返回的数据条数是0");
			resultMessage.setAttachment(attachment);
			return resultMessage;
		}
		int totalPage = 0; // 总页数
		int x = totalRows / CommonStandard.DEFAULT_FRONT_ROWS_PER_PAGE;
		int y = totalRows % CommonStandard.DEFAULT_FRONT_ROWS_PER_PAGE;
		if (y > 0) {
			totalPage = x + 1;
		} else {
			totalPage = x;
		}
		logger.info("总条数" + String.valueOf(totalRows));
		logger.info("当前页面" + String.valueOf(page));
		logger.info("总页数" + String.valueOf(totalPage));
		logger.info("-------x-----" + String.valueOf(x));
		logger.info("-------y-----" + String.valueOf(y));

		if (page > totalPage) {
			logger.debug("当前数据已经到底了");

			resultMessage.setAttachment(attachment);
			return resultMessage;
		}

		// Paging paging = new Paging(rows);
		// paging.setCurrentPage(page);
		// activityLogCriteria.setPaging(paging);
		//
		// List<ActivityLog> joinList =
		// activityLogService.listOnPage(activityLogCriteria);
		List<ActivityLog> joinList = activityLogService.list(activityLogCriteria);
		logger.debug("一共 " + totalRows + " 行数据，每页显示" + rows + " 行数据，当前是第" + page + "页。 joinList :　" + joinList.size());
		if (joinList == null || joinList.size() < 1) {
			resultMessage.setOperateCode(OperateResult.success.getId());
		}
		for (ActivityLog activityLog : joinList) {

			User joinUser = frontUserService.select(activityLog.getUuid());
			// 统计票数
			ActivityLogCriteria activityLogCriteria2 = new ActivityLogCriteria();
			ExtraDataCriteria extraDataCriteria = new ExtraDataCriteria();
			ExtraDataCriteria extraDataCriteria1 = new ExtraDataCriteria();

			if (joinUser == null) {

			} else {
				activityLog.setExtraValue("joinUser", joinUser.getUsername());
				activityLogCriteria2.setPromotion(joinUser.getUuid() + "");
				extraDataCriteria.setUuid(joinUser.getUuid());
				extraDataCriteria1.setUuid(joinUser.getUuid());
			}

			DataDefine dataDefine = dataDefineService.select("userUpLoadImage");
			DataDefine dataDefine1 = dataDefineService.select("userUploadSummary");
			activityLogCriteria2.setAction(OperateCode.VOTE.toString());
			activityLogCriteria2.setActivityId(activity.getActivityId());
			activityLogCriteria2.setOwnerId(activity.getOwnerId());
			int voteNumber = activityLogService.count(activityLogCriteria2);

			extraDataCriteria.setTableName("activity_data");
			extraDataCriteria.setObjectType(ObjectType.activity.name());
			extraDataCriteria.setObjectId(activity.getActivityId());
			extraDataCriteria1.setTableName("activity_data");
			extraDataCriteria1.setObjectType(ObjectType.activity.name());
			extraDataCriteria1.setObjectId(activity.getActivityId());

			if (dataDefine != null && dataDefine1 != null) {
				extraDataCriteria.setDataDefineId(dataDefine.getDataDefineId());

				extraDataCriteria1.setDataDefineId(dataDefine1.getDataDefineId());
			}

			List<ExtraData> extraDataList = extraDataService.list(extraDataCriteria);
			List<ExtraData> extraDataList1 = extraDataService.list(extraDataCriteria1);
			extraDataList.addAll(extraDataList1);
			logger.debug("extraDataListSize : " + extraDataList.size());
			activityLog.setExtraDataList(extraDataList);
			activityLog.setFlag(voteNumber);
		}

		// 排序
		Collections.sort(joinList, new Comparator<ActivityLog>() {
			@Override
			public int compare(ActivityLog o1, ActivityLog o2) {
				if (o1.getFlag() < o2.getFlag()) {
					logger.info(o1.getFlag() + ":" + o2.getFlag() + "===>" + 1);
					return 1;
				}
				if (o1.getFlag() == o2.getFlag()) {
					logger.info(o1.getFlag() + ":" + o2.getFlag() + "===>" + 0);
					return 0;
				}
				logger.info(o1.getFlag() + ":" + o2.getFlag() + "===>" + -1);
				return -1;
			}
		});

		List<ActivityLog> newList = joinList.subList(rows * (page - 1),
				((rows * page) > totalRows ? totalRows : (rows * page)));

		attachment.put("joinUserList", newList);

		resultMessage.setAttachment(attachment);
		resultMessage.setOperateCode(OperateResult.success.getId());
		resultMessage.setMessage("活动处理完成");
		return resultMessage;

	}

	// 检查用户是否能参与上传图片
	private int joinCheck(Activity activity, User frontUser) {

		int joinLimitCount = (int) activity.getLongExtraValue(DataName.perUserJoinCountLimit.toString());
		if (joinLimitCount < 1) {
			logger.info("活动[" + activity.getActivityId() + "/" + activity.getActivityCode() + "]没有用户参与次数限制");
			return OperateResult.success.getId();
		}
		// 检查参与日志
		ActivityLogCriteria activityLogCriteria = new ActivityLogCriteria();
		activityLogCriteria.setActivityId(activity.getActivityId());
		activityLogCriteria.setUuid(frontUser.getUuid());
		if (activity.getBeginTime() == null) {
			activityLogCriteria.setBeginTime(DateUtils.addDays(new Date(), -30));
		} else {
			activityLogCriteria.setBeginTime(activity.getBeginTime());
		}
		activityLogCriteria.setAction(OperateCode.JOIN_GAME.toString());
		activityLogCriteria.setOwnerId(activity.getOwnerId());
		logger.debug("检查用户[" + frontUser.getUuid() + "]对活动[" + activity.getActivityId() + "/"
				+ activity.getActivityCode() + "]" + OperateCode.JOIN_GAME.toString() + "的记录");
		int count = activityLogService.count(activityLogCriteria);
		logger.info("活动[" + activity.getActivityId() + "/" + activity.getActivityCode() + "]设置的用户参与次数限制是:"
				+ joinLimitCount + ",用户[" + frontUser.getUuid() + "]参与次数是:" + count);
		if (count >= joinLimitCount) {
			return -1;
		}
		return OperateResult.success.getId();
	}

	// 参与活动调用方法
	private int join(Activity activity, User frontUser, HttpServletRequest request, String phone) {
		int checkResult = joinCheck(activity, frontUser);
		logger.debug("join : " + checkResult);
		if (checkResult != OperateResult.success.getId()) {
			return checkResult;
		}
		// TODO 传图
		int fileUpload = 0;
		int userExperience = 0;

		if (request instanceof MultipartHttpServletRequest) {
			logger.info("请求中带有附件，使用文件上传处理.");
			fileUpload = fileUpload(request, "create", activity, frontUser);
			// 用来保存心得的心得是必须要填的
			userExperience = userExperience(request, activity, frontUser);

		} else {
			logger.info("请求中没有附件");
		}

		// TODO 插入一条活动日志&& userExperience > 0
		if (fileUpload > 0 && userExperience > 0) {
			int index = 0;
			ActivityLogCriteria activityLogCriteria = new ActivityLogCriteria();
			activityLogCriteria.setActivityId(activity.getActivityId());
			if (activity.getBeginTime() == null) {
				activityLogCriteria.setBeginTime(DateUtils.addDays(new Date(), -30));
			} else {
				activityLogCriteria.setBeginTime(activity.getBeginTime());
			}
			activityLogCriteria.setAction(OperateCode.JOIN_GAME.toString());
			activityLogCriteria.setOwnerId(activity.getOwnerId());
			int totalRows = activityLogService.count(activityLogCriteria);
			if (totalRows > 0) {
				index = totalRows + 1;
			} else if (totalRows == 0) {
				index = 1;
			}

			// ActivityLog activityLog = new ActivityLog(activity, frontUser,
			// activity.getCurrentStatus());
			ActivityLog activityLog = new ActivityLog();
			activityLog.setIndex(index);
			activityLog.setActivityId(activity.getActivityId());
			activityLog.setLogTime(new Date());
			activityLog.setAccountLimit(activity.getAccountFreeLimit());
			activityLog.setPromotion(activity.getPromotion());
			activityLog.setActivityType(activity.getActivityType());
			activityLog.setCurrentStatus(CommentCriteria.STATUS_WAIT_EXAMINE);
			activityLog.setAction(OperateCode.JOIN_GAME.toString());
			activityLog.setUuid(frontUser.getUuid());
			activityLog.setExtraValue("phone", phone);
			activityLog.setOwnerId(activity.getOwnerId());
			int rs = activityLogService.insert(activityLog);
			if (rs > 0) {
				return OperateResult.success.getId();
			}
		}
		return OperateResult.failed.getId();
	}

	// 图片上传
	private int fileUpload(HttpServletRequest request, String mode, Activity activity, User frontUser) {
		// TODO 上传图片
		// 从Spring容器中获取对应的上传文件目录
		// String fileUploadSavePath =
		// ((FileSystemResource)this.getApplicationContext().getBean("uploadSaveDir")).getPath();
		// logger.info("Spring容器中的上传文件目录在:" + fileUploadSavePath);
		// logger.info("尝试为文档[udid="+udid+"]在[" + mode + "]模式下上传附件");
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
		// String parameter = multiRequest.getParameter("file");
		Iterator<String> its = multiRequest.getFileNames();
		int i = 0;
		int addCount = 0;
		while (its.hasNext()) {
			logger.debug("documentUploadSaveDir : " + documentUploadSaveDir);
			CommonsMultipartFile file = (CommonsMultipartFile) multiRequest.getFile(its.next());// String
			logger.debug("fileSize : " + file.getSize());
			if (file.getSize() == 0) {
				if (i == 0) {
					continue;
				}
				break;
			}
			String key = file.getName();
			/*
			 * 如果当前为编辑模式,则查找是否有对应的已存在数据 如果有已存在的数据并且本次要上传,则直接更新对应的文件
			 */
			String fileUploadSavePath = null;

			fileUploadSavePath = documentUploadSaveDir + File.separator;

			String fileName = CommonStandard.EXTRA_DATA_OPEN_PATH + File.separator
					+ UUIDFilenameGenerator.generateWithDatePath(file.getOriginalFilename());
			String fileDest = fileUploadSavePath + File.separator + fileName;

			logger.info("documentUploadSaveDir:" + documentUploadSaveDir + ",fileUploadSavePath:" + fileUploadSavePath
					+ ",fileName:" + fileName);

			logger.info("保存数据文件[" + file.getOriginalFilename() + "]到:" + fileDest);
			File destDir = new File(fileDest).getParentFile();
			if (!destDir.exists()) {
				logger.info("目标目录不存在，创建:" + destDir.getName());
				try {
					FileUtils.forceMkdir(destDir);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			File dest = new File(fileDest);
			try {
				file.transferTo(dest);
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
				continue;
			}
			/*
			 * if (mode.equals("edit") && existDocumentData != null) { //
			 * 已经更新了已存在附件的文件,无需其他操作 continue; }
			 */
			DataDefineCriteria dataDefineCriteria = new DataDefineCriteria();
			dataDefineCriteria.setDataCode("userUpLoadImage");
			DataDefine dataDefine = dataDefineService.select("userUpLoadImage");
			// 记录用户上传的图片
			ExtraData extraData = new ExtraData();
			extraData.setTableName("activity_data");
			extraData.setObjectType(ObjectType.activity.name());
			extraData.setObjectId(activity.getActivityId());
			if (dataDefine != null) {
				extraData.setDataDefineId(dataDefine.getDataDefineId());
			}
			extraData.setDataValue(fileName);// 图片名字
			extraData.setUuid(frontUser.getUuid());
			extraData.setOwnerId(activity.getOwnerId());
			int insert = extraDataService.insert(extraData);
			logger.info("保存上传的图片[" + file.getOriginalFilename() + "]结果：" + insert);
			addCount++;

		}

		int totalAffected = addCount;
		String message = "完成附件上传,新增 " + addCount + " 个。";
		logger.info(message);
		return totalAffected;
	}

	// 检查用户是否能投票
	private int voteCheck(Activity activity, User frontUser) {
		int joinLimitCount = (int) activity.getLongExtraValue(DataName.perUserVoteCountLimit.toString());
		if (joinLimitCount < 1) {
			logger.info("活动[" + activity.getActivityId() + "/" + activity.getActivityCode() + "]没有用户投票次数限制");
			return OperateResult.success.getId();
		}

		/**
		 * 在Redis中检查
		 */
		long userVoteLog = 0;
		String acitivityLogKey = null;
		if (frontUser == null) {
			acitivityLogKey = "VOTE#" + sdf2.format(new Date()) + "#0";
		} else {
			acitivityLogKey = "VOTE#" + sdf2.format(new Date()) + "#" + frontUser.getUuid();
		}
		String value = centerDataService.get(acitivityLogKey);
		userVoteLog = NumericUtils.parseLong(value);
		// logger.debug("用户[" + frontUser.getUuid() + "]当天的投票次数上限是：" +
		// joinLimitCount + "，当天已参与次数是：" + userVoteLog);
		logger.debug("用户当天的投票次数上限是：" + joinLimitCount + "，当天已参与次数是：" + userVoteLog);
		if (joinLimitCount > 0 && userVoteLog >= joinLimitCount) {
			logger.debug("当前用户参与投票次数上限是 ：" + joinLimitCount + ", 用户已参与投票次数是：" + userVoteLog);
			return OperateResult.failed.getId();
		}

		// 检查参与日志
		/*
		 * ActivityLogCriteria activityLogCriteria = new ActivityLogCriteria();
		 * activityLogCriteria.setActivityId(activity.getActivityId()); if
		 * (frontUser == null) { activityLogCriteria.setUuid(0); } else {
		 * activityLogCriteria.setUuid(frontUser.getUuid()); }
		 * if(activity.getBeginTime() == null){
		 * activityLogCriteria.setBeginTime(DateUtils.addDays(new Date(), -30));
		 * } else { activityLogCriteria.setBeginTime(activity.getBeginTime()); }
		 * activityLogCriteria.setAction(OperateCode.VOTE.toString()); //
		 * logger.debug("检查用户[" + frontUser.getUuid() + "]对活动[" +
		 * activity.getActivityId() + "/" + activity.getActivityCode() + "]" +
		 * OperateCode.VOTE.toString() + "的记录"); int count =
		 * activityLogService.count(activityLogCriteria); // logger.info("活动[" +
		 * activity.getActivityId() + "/" + activity.getActivityCode() +
		 * "]设置的用户投票次数限制是:" + joinLimitCount + ",用户[" + frontUser.getUuid() +
		 * "]已投票次数是:" + count); if(count >= joinLimitCount){ return
		 * OperateResult.failed.getId(); }
		 */
		return OperateResult.success.getId();
	}

	private EisMessage vote(Activity activity, User frontUser, HttpServletRequest request) {

		int checkResult = voteCheck(activity, frontUser);
		if (checkResult != OperateResult.success.getId()) {
			return new EisMessage(checkResult, "投票失败");
		}
		int number = 0; // 被投票人
		int uuid = 0;// 投票操作人
		String uuidStr = request.getParameter("uuid");
		String numberStr = request.getParameter("number");
		if (NumericUtils.isIntNumber(uuidStr)) {
			uuid = Integer.parseInt(uuidStr);
		}
		if (NumericUtils.isIntNumber(numberStr)) {
			number = Integer.parseInt(numberStr);
		}
		if (uuid < 1) {
			return new EisMessage(OperateResult.failed.getId(), "请提交正确的用户信息");
		}
		if (number == 0) {
			return new EisMessage(OperateResult.failed.getId(), "请提交正确的投票信息");
		}
		User user = frontUserService.select(uuid);
		if (user == null) {
			return new EisMessage(OperateResult.failed.getId(), "没找到用户[" + uuid + "]");
		}
		// TODO 插入投票的日志
		/**
		 * 写入Redis
		 */
		// 用户参与次数
		long userVoteLog = 0;
		String acitivityLogKey = "VOTE#" + sdf2.format(new Date()) + "#" + uuid;
		// if (frontUser == null) {
		// acitivityLogKey = "VOTE#" + sdf2.format(new Date()) + "#0";
		// } else {
		// acitivityLogKey = "VOTE#" + sdf2.format(new Date()) + "#" +
		// frontUser.getUuid();
		// }
		String value = centerDataService.get(acitivityLogKey);
		userVoteLog = NumericUtils.parseLong(value);
		// 用户参与上限
		long joinLimitCount = activity.getLongExtraValue(DataName.perUserVoteCountLimit.toString());
		// logger.debug("用户[" + frontUser.getUuid() + "]当天的投票次数上限是：" +
		// joinLimitCount + "，当天已参与次数是：" + userVoteLog);
		logger.debug("用户当天的投票次数上限是：" + joinLimitCount + "，当天已参与次数是：" + userVoteLog);
		if (joinLimitCount > 0 && userVoteLog >= joinLimitCount) {
			logger.debug("当前用户参与投票次数上限是 ：" + joinLimitCount + ", 用户已参与投票次数是：" + userVoteLog);
			return new EisMessage(OperateResult.failed.getId(), "投票次数超限");
		}
		userVoteLog++;
		Date timeOut = DateUtils.truncate(DateUtils.addDays(new Date(), 1), Calendar.DAY_OF_MONTH);
		long timeoutSec = (timeOut.getTime() - new Date().getTime()) / 1000;
		// logger.debug("投票用户[" + frontUser.getUuid() + "]最新投票次数:" + userVoteLog
		// + ",还有" + timeoutSec + "秒超时，超时时间点:" + sdf.format(timeOut));
		logger.debug("投票用户最新投票次数:" + userVoteLog + ",还有" + timeoutSec + "秒超时，超时时间点:" + sdf.format(timeOut));
		centerDataService.setForce(acitivityLogKey, String.valueOf(userVoteLog), timeoutSec);

		// ActivityLog activityLog = new ActivityLog(activity, frontUser,
		// activity.getCurrentStatus());
		ActivityLog activityLog = new ActivityLog();
		activityLog.setActivityId(activity.getActivityId());
		activityLog.setLogTime(new Date());
		activityLog.setAccountLimit(activity.getAccountFreeLimit());
		activityLog.setActivityType(activity.getActivityType());
		activityLog.setCurrentStatus(activity.getCurrentStatus());
		activityLog.setAction(OperateCode.VOTE.toString());
		activityLog.setPromotion(number + "");
		activityLog.setUuid(uuid);
		activityLog.setOwnerId(activity.getOwnerId());
		int rs = activityLogService.insert(activityLog);
		if (rs > 0) {
			return new EisMessage(OperateResult.success.getId(), "投票成功");
		}
		return new EisMessage(OperateResult.failed.getId(), "投票失败");
	}

	@Override
	public EisMessage prepare(Activity arg0, User arg1, Object arg2) {
		return null;
	}

	// 用户心得
	@SuppressWarnings("unused")
	private int userExperience(HttpServletRequest request, Activity activity, User frontUser) {
		// 判断用户能否上传照片，写心得依赖于能否上传
		int num = 0;
		if (OperateResult.success.getId() == joinCheck(activity, frontUser)) {

			// 获取用户使用心得的字段
			String exprience = request.getParameter("exprience");
			// MultipartHttpServletRequest multipartRequest =
			// (MultipartHttpServletRequest) request;
			//
			// String exprience = multipartRequest.getParameter("exprience");

			if (exprience != null && exprience.length() != 0) {
				// 进行数据定义增加扩展心得的字段
				DataDefineCriteria dataDefineCriteria = new DataDefineCriteria();
				dataDefineCriteria.setDataCode("userUploadSummary");
				DataDefine dataDefine = dataDefineService.select("userUploadSummary");
				// 记录用户的心得
				ExtraData extraData = new ExtraData();
				extraData.setTableName("activity_data");
				extraData.setObjectType(ObjectType.activity.name());
				extraData.setObjectId(activity.getActivityId());

				if (dataDefine != null) {
					extraData.setDataDefineId(dataDefine.getDataDefineId());
				}
				extraData.setDataValue(exprience);// 心得内容
				extraData.setUuid(frontUser.getUuid());
				extraData.setOwnerId(activity.getOwnerId());
				int insert = extraDataService.insert(extraData);
				logger.info("保存填写的心得[" + exprience + "]结果：" + insert);
				num++;
			}
		}
		return num;
	}

}
