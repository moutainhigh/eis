package com.maicard.wpt.custom.youbao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.CenterDataService;
import com.maicard.common.util.NumericUtils;
import com.maicard.money.criteria.CouponCriteria;
import com.maicard.money.criteria.CouponModelCriteria;
import com.maicard.money.domain.Coupon;
import com.maicard.money.domain.CouponModel;
import com.maicard.money.service.CouponModelService;
import com.maicard.money.service.CouponProcessor;
import com.maicard.money.service.CouponService;
import com.maicard.product.domain.Activity;
import com.maicard.product.service.ActivityProcessor;
import com.maicard.security.domain.User;
import com.maicard.security.service.FrontUserService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.EisError;
import com.maicard.standard.OperateResult;
import com.maicard.standard.ServiceStatus;
import com.maicard.wpt.service.WeixinPlatformService;

/**
 * 
 * @author Feng 武汉中百扫描二维码送优惠券活动
 */

public class ZbScanCodeActivityProcessor extends BaseService implements
		ActivityProcessor {
	@Resource
	private CouponService couponService;
	@Resource
	private FrontUserService frontUserService;
	@Resource
	private CouponModelService couponModelService;
	@Resource
	private CouponProcessor testCouponProcessor;
	@Resource
	private WeixinPlatformService weixinPlatformService;
	@Resource
	private CenterDataService centerDataService;

	@SuppressWarnings("unchecked")
	@Override
	public EisMessage execute(String action, Activity activity,
			Object targetObject, Object parameter) {
		// 活动检查

		if (activity == null) {
			logger.error("尝试进行的活动为空");
			return new EisMessage(EisError.OBJECT_IS_NULL.getId(), "活动为空");
		}
		User frontUser = null;
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
			return new EisMessage(EisError.REQUIRED_PARAMETER.getId(),
					"requestData参数为空");
		}
		HttpServletRequest request = (HttpServletRequest) requestData
				.get("request");
		HashMap<String, Object> attachment = new HashMap<String, Object>();
		EisMessage resultMessage = new EisMessage();
		attachment.put("activity", activity);
		if (activity.getCurrentStatus() == ServiceStatus.closed.getId()) {
			logger.debug(activity.getActivityId() + "#活动状态是:"
					+ activity.getCurrentStatus() + ",已关闭");
			resultMessage.setAttachment(attachment);
			resultMessage.setOperateCode(EisError.activityClosed.getId());
			resultMessage.setMessage("活动已关闭");
			return resultMessage;
		}
		if (activity.getCurrentStatus() == ServiceStatus.waitingOpen.getId()) {
			logger.debug(activity.getActivityId() + "#活动状态是:"
					+ activity.getCurrentStatus() + ",尚未开启");
			resultMessage.setAttachment(attachment);
			resultMessage.setOperateCode(EisError.notActive.getId());
			resultMessage.setMessage("活动尚未开始");
			return resultMessage;
		}
		if (StringUtils.isBlank(action)) {

			return resultMessage;
		}
		if (action.equals("JOIN")) {
			// int uuid = 0;
			 String identify = request.getParameter("identify");
			// String UserUUID = request.getParameter("uuid");
			// if (NumericUtils.isIntNumber(UserUUID)) {
			// uuid = Integer.parseInt(UserUUID);
			// }
			// if (uuid < 1) {
			// return new EisMessage(OperateResult.failed.getId(),
			// "请提交正确的用户信息");
			// }
			// User user = frontUserService.select(uuid);
			// if (user == null) {
			// return new EisMessage(OperateResult.failed.getId(), "没找到用户["
			// + uuid + "]");
			// }
			// 限制用户领取的次数
			long joinCount = 0;
			logger.info("用户的UUID------------" + frontUser.getUuid());
			String receiveCouponKey = "ACTIVITY#" + frontUser.getUuid() + "#" + identify;
			long value = centerDataService.increaseBy(receiveCouponKey, 1, 1,
					2592000L);
			joinCount = value;
			// 设置参与的过期时间是100天
			logger.debug("当前用户参与的次数是" + joinCount);
			if (joinCount >= 0 ) {
				EisMessage fetchCoupon = fetchCoupon(activity, frontUser,
						parameter);
				return fetchCoupon;
			} else {
				resultMessage.setAttachment(attachment);
				resultMessage.setOperateCode(102007);
				resultMessage.setMessage("领取次数超限");
				return resultMessage;
			}
		}
		return resultMessage;
	}

	@SuppressWarnings("unchecked")
	private EisMessage fetchCoupon(Activity activity, User frontUser,
			Object parameter) {
		// String couponCode = "peDoPt6opOwr8IF2Q2GUFwwOMQ8I";//测试中百的
		// String couponCode = "peDoPtwrxI4L2gEHJ27wg3grnpKU";//现网中百测试的
		// String couponCode = "peDoPt4r_w8qZeJaSqX_8j8apNiU";//宝洁代金券
		CouponModelCriteria couponModelCriteria = new CouponModelCriteria();
		couponModelCriteria.setOwnerId(activity.getOwnerId());
		couponModelCriteria.setCurrentStatus(BasicStatus.normal.getId());
		// couponModelCriteria.setCouponCode(couponCode);
		couponModelCriteria.setIdentify(activity.getActivityCode());

		EisMessage result = new EisMessage(OperateResult.success.getId(), null);

		List<CouponModel> couponModelList = couponModelService
				.list(couponModelCriteria);
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
		EisMessage fetchResult = testCouponProcessor
				.fetch(couponCriteria);
		if (fetchResult.getOperateCode() != OperateResult.success.id) {
			logger.warn("无法从有宝获取到对应的优惠券:" + couponModel.getExtraCode());
			return fetchResult;
		}
		// 调用insert进行判断
		

		Coupon coupon = ((List<Coupon>) fetchResult
				.getAttachmentData("couponList")).get(0);

		Map<String, Object> map = null;
		try {
			map = (Map<String, Object>) parameter;
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (map == null) {
			logger.error("活动未提供Map<String,Object>类型的parameter");
			return new EisMessage(EisError.REQUIRED_PARAMETER.getId(),
					"活动参数错误");
		}

		long timeStamp = System.currentTimeMillis();
		String ts = String.valueOf(timeStamp / 1000);
		if (map != null) {
			map.put("timestamp", timeStamp);

			final String nonceStr = DigestUtils.md5Hex(UUID.randomUUID()
					.toString());
			String currentUrl = null;
			if (map.get("outShareUrl") != null) {
				currentUrl = map.get("outShareUrl").toString();
				final String sha1Str = weixinPlatformService.makeJsSignature(
						timeStamp, currentUrl, nonceStr,
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
		Coupon coupon2 = new Coupon();
		coupon2.setCouponCode(coupon.getCouponCode());
		coupon2.setContent(coupon.getCouponSerialNumber());
		coupon2.setExtraCode(coupon.getExtraCode());
		coupon2.setUuid(frontUser.getUuid());
		coupon2.setCouponModelId(coupon.getCouponModelId());
		coupon2.setOwnerId(coupon.getOwnerId());
		coupon2.setCurrentStatus(coupon.getCurrentStatus());
		int insert = couponService.insert(coupon2);
		logger.debug("用户领取成功：" + insert);
		return result;
	}

	// 设置用户的总参与次数

	@Override
	public EisMessage prepare(Activity arg0, User arg1, Object arg2) {
		// TODO Auto-generated method stub
		return null;
	}

}
