package com.maicard.wpt.custom.youbao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import com.maicard.common.base.BaseService;
import com.maicard.common.criteria.DataDefineCriteria;
import com.maicard.common.criteria.ExtraDataCriteria;
import com.maicard.common.domain.DataDefine;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.domain.ExtraData;
import com.maicard.common.service.CenterDataService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.DataDefineService;
import com.maicard.common.service.ExtraDataService;
import com.maicard.common.service.GlobalOrderIdService;
import com.maicard.money.service.CouponModelService;
import com.maicard.money.service.CouponProcessor;
import com.maicard.money.service.CouponService;
import com.maicard.money.service.MoneyService;
import com.maicard.money.service.PointExchangeLogService;
import com.maicard.product.domain.Activity;
import com.maicard.product.service.ActivityLogService;
import com.maicard.product.service.ActivityProcessor;
import com.maicard.security.domain.User;
import com.maicard.security.service.FrontUserService;
import com.maicard.standard.EisError;
import com.maicard.standard.ObjectType;
import com.maicard.standard.ServiceStatus;
import com.maicard.wpt.service.WeixinPlatformService;

/**
 * 扫码活动 用户可以参与活动，家乐福的扫码直接发放券的码，物美的先进行一维码的判断，然后，根据判断发放券
 * 
 * 
 * @author Feng
 * @date 2017-03-20
 */

public class ScanActivityProcessor extends BaseService implements ActivityProcessor {
	@Resource
	private ExtraDataService extraDataService;

	@Resource
	private ActivityLogService activityLogService;
	@Resource
	private FrontUserService frontUserService;

	@Resource
	private DataDefineService dataDefineService;
	@Resource
	private CenterDataService centerDataService;
	@Resource
	private ConfigService configService;
	@Resource
	private CouponService couponService;
	@Resource
	private GlobalOrderIdService globalOrderIdService;
	@Resource
	private MoneyService moneyService;
	@Resource
	private PointExchangeLogService pointExchangeLogService;
	@Resource
	private CouponProcessor stockCardCouponProcessor;
	@Resource
	private WeixinPlatformService weixinPlatformService;
	@Resource
	private CouponModelService couponModelService;
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

	@SuppressWarnings("unchecked")
	@Override
	public EisMessage execute(String action, Activity activity, Object targetObject, Object parameter) {

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
			return new EisMessage(EisError.REQUIRED_PARAMETER.getId(), "requestData参数为空");
		}
		HttpServletRequest request = (HttpServletRequest) requestData.get("request");
		HashMap<String, Object> attachment = new HashMap<String, Object>();
		EisMessage resultMessage = new EisMessage();
		attachment.put("activity", activity);
		String state = request.getParameter("state");
		logger.info("获得到的参数是-------:" + state);
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
		// 开始判断属于家乐福还是物美或者是空的情况
		action = state;
		long uuid = frontUser.getUuid();
		if (StringUtils.isBlank(action)) {
			return resultMessage;

		}
		if (action.equals("jlf1")) {
			String data = "jlf1#" + sdf.format(new Date());
			UserInfor(data, frontUser);

			long joincont = 0;

			String receiveCouponKey = "ACTIVITY#" + "#" + action;
			long value = centerDataService.increaseBy(receiveCouponKey, 1, 1, 2592000L);
			joincont = value;
			logger.info("家里福1的活动访问次数是：" + joincont);
			resultMessage.setOperateCode((int) uuid);
			resultMessage.setMessage("jlf1");
			resultMessage.setAttachment(attachment);
			return resultMessage;
		}
		if (action.equals("jlf2")) {
			long joincont = 0;
			String data = "jlf2#" + sdf.format(new Date());
			UserInfor(data, frontUser);
			String receiveCouponKey = "ACTIVITY#" + "#" + action;
			long value = centerDataService.increaseBy(receiveCouponKey, 1, 1, 2592000L);
			joincont = value;
			logger.info("家里福2的活动访问次数是：" + joincont);
			resultMessage.setOperateCode((int) uuid);
			resultMessage.setMessage("jlf2");
			resultMessage.setAttachment(attachment);
			return resultMessage;
		}
		if (action.equals("jlf3")) {

			long joincont = 0;
			String data = "jlf3#" + sdf.format(new Date());
			UserInfor(data, frontUser);
			String receiveCouponKey = "ACTIVITY#" + "#" + action;
			long value = centerDataService.increaseBy(receiveCouponKey, 1, 1, 2592000L);
			joincont = value;
			logger.info("家里福3的活动访问次数是：" + joincont);
			resultMessage.setOperateCode((int) uuid);
			resultMessage.setMessage("jlf3");
			resultMessage.setAttachment(attachment);
			return resultMessage;
		}
		if (action.equals("wm")) {
			// String data= "wumeifwl#"+sdf.format(new Date());
			// UserInfor(data, frontUser);
			// 首先进行判断
			logger.debug("进入到物美");
			// String searchResult = serachUserCards(frontUser, activity);
			// if (searchResult.equals("11")) {
			// resultMessage.setOperateCode((int) uuid);
			// resultMessage.setMessage("11");
			// resultMessage.setAttachment(attachment);
			// return resultMessage;
			// }
			// if (searchResult.equals("12")) {
			// resultMessage.setOperateCode((int) uuid);
			// resultMessage.setMessage("12");
			// resultMessage.setAttachment(attachment);
			// return resultMessage;
			// }
			// if (searchResult.equals("13")) {
			// resultMessage.setOperateCode((int) uuid);
			// resultMessage.setMessage("13");
			// resultMessage.setAttachment(attachment);
			// return resultMessage;
			// }
			// 物美的是通过我传递值到前端页面进而进行判断
			String code = request.getParameter("YWMCODE");
			if (code.equals("11111")) {
				resultMessage.setOperateCode(11);
				resultMessage.setMessage("能发8元券");
				resultMessage.setAttachment(attachment);
				return resultMessage;
			}
			if (code.equals("22222")) {
				resultMessage.setOperateCode(12);
				resultMessage.setMessage("能发40元券");
				resultMessage.setAttachment(attachment);
				return resultMessage;
			}
			if (code.equals("33333")) {
				resultMessage.setOperateCode(13);
				resultMessage.setMessage("能发88元券");
				resultMessage.setAttachment(attachment);
				return resultMessage;
			}

			long joincont = 0;
			String receiveCouponKey = "ACTIVITY#" + "#" + action;
			long value = centerDataService.increaseBy(receiveCouponKey, 1, 1, 2592000L);
			joincont = value;
			logger.info("物美的活动访问次数是：" + joincont);
			resultMessage.setOperateCode((int) uuid);
			resultMessage.setMessage("wm");
			resultMessage.setAttachment(attachment);
			return resultMessage;
		}

		return resultMessage;
	}

	// 编写一serach方法先去数据库中查询一下这个对应的值
	@SuppressWarnings("unused")
	private String serachUserCards(User frontUser, Activity activity) {
		String sprcies = null;
		User joinUser = frontUserService.select(frontUser.getUuid());

		ExtraDataCriteria extraDataCriteria = new ExtraDataCriteria();

		DataDefine dataDefine = dataDefineService.select("userCardSpecies");
		logger.info("获得的数据定义ID是：" + dataDefine.getDataDefineId());
		if (dataDefine != null) {
			extraDataCriteria.setTableName("activity_data");
			extraDataCriteria.setObjectType(ObjectType.activity.name());
			extraDataCriteria.setObjectId(activity.getActivityId());
			extraDataCriteria.setDataDefineId(dataDefine.getDataDefineId());
			extraDataCriteria.setUuid(frontUser.getUuid());
		}

		List<ExtraData> extraDataList = extraDataService.list(extraDataCriteria);
		logger.info("得到的数据的长度：" + extraDataList.size());
		if (extraDataList.size() < 1) {
			sprcies = "10";
			return sprcies;
		}
		ExtraData extraData = extraDataList.get(0);
		sprcies = extraData.getDataValue();
		logger.debug("获取到用户的卡券种类是 : " + sprcies);
		return sprcies;

	}

	// 新建两个字段用来存取
	@SuppressWarnings("unused")
	private void UserInfor(String cardSpecies, User frontUser) {
		// 增加一个字段用来存取用户的card种类
		DataDefineCriteria dataDefineCriteria = new DataDefineCriteria();
		dataDefineCriteria.setDataCode("userCardSpecies");
		DataDefine dataDefine = dataDefineService.select("userCardSpecies");

		ExtraData extraData = new ExtraData();
		extraData.setTableName("activity_data");
		extraData.setObjectType(ObjectType.activity.name());
		// 这个地方由于时间限制暂时写死
		extraData.setObjectId(40);

		if (dataDefine != null) {
			extraData.setDataDefineId(dataDefine.getDataDefineId());
		}
		extraData.setDataValue(cardSpecies);// 卡券的内容
		extraData.setUuid(frontUser.getUuid());
		// 同上这个地方暂时写死，如果后期修改的话，应该改成处理器，而不是controller
		extraData.setOwnerId(200006);
		int insert = extraDataService.insert(extraData);
		logger.info("保存用户的UUID[" + frontUser.getUuid() + "]用户的卡券种类[" + cardSpecies + "]结果：" + insert);
	}

	@Override
	public EisMessage prepare(Activity activity, User user, Object parameter) {
		// TODO Auto-generated method stub
		return null;
	}

}
