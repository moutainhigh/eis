package com.maicard.wpt.custom.youbao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.maicard.common.base.BaseController;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.money.service.MoneyService;
import com.maicard.product.criteria.ActivityCriteria;
import com.maicard.product.domain.Activity;
import com.maicard.product.service.ActivityProcessor;
import com.maicard.product.service.ActivityService;
import com.maicard.security.criteria.UserRelationCriteria;
import com.maicard.security.domain.User;
import com.maicard.security.domain.UserRelation;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.UserRelationService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.EisError;
import com.maicard.standard.ObjectType;
import com.maicard.standard.OperateResult;
import com.maicard.standard.SecurityStandard.UserTypes;

/**
 * 用于用户签到并领取奖励
 *
 * @author NetSnake
 * @date 2015年9月21日
 */
@Controller
@RequestMapping(value = "/sign")
public class SignController extends BaseController {

	@Resource
	private ActivityService activityService;
	@Resource
	private CertifyService certifyService;
	@Resource
	private UserRelationService userRelationService;
	@Resource
	private MoneyService moneyService;
	@Resource
	private ApplicationContextService applicationContextService;

	private final String signDateFormat = "yyyyMMdd";

	private final SimpleDateFormat sdf = new SimpleDateFormat(signDateFormat);
	private int maxContinueSignDay = 30;

	@RequestMapping
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		final String view = "sign/list";

		User frontUser = null;

		try {
			frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (frontUser == null) {
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录，请先登录后再签到"));
			return CommonStandard.frontMessageView;
		}
		/**
		 * @author Pengzhenggang
		 * @data 2016/3/30
		 */
		UserRelationCriteria userRelationCriteria = new UserRelationCriteria();
		userRelationCriteria.setObjectType(ObjectType.sign.name());
		userRelationCriteria.setUuid(frontUser.getUuid());
		userRelationCriteria.setCurrentStatus(BasicStatus.normal.getId());
		userRelationCriteria.setOwnerId(frontUser.getOwnerId());
		List<UserRelation> userRelationList = userRelationService.list(userRelationCriteria);
		if (userRelationList == null || userRelationList.size() <= 0) {
			userRelationList = new ArrayList<UserRelation>();
			map.put("signCount", 0);
			map.put("continueSignDays", 0);
			map.put("userRelationList", userRelationList);
			return view;
		} else {
			// sortSignRelation(userRelationList);
		}
		int signCount = userRelationService.count(userRelationCriteria);
		logger.info("用户[" + frontUser.getUuid() + "]累计的签到次数是:" + signCount);

		int continueSignDay = 0;
		int continueSignDays = 0;
		// 排序
		Collections.sort(userRelationList, new Comparator<UserRelation>() {
			@Override
			public int compare(UserRelation o1, UserRelation o2) {
				if (o1.getCreateTime().after(o2.getCreateTime())) {
					System.out.println(o1.getCreateTime() + ":" + o2.getCreateTime() + "===>" + 1);
					return -1;
				}
				System.out.println(o1.getCreateTime() + ":" + o2.getCreateTime() + "===>" + 0);
				return 1;
			}
		});
		int afterDay = Integer.parseInt(sdf.format(DateUtils.addDays(new Date(), -1)));
		Date today = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
		logger.info("角色[" + frontUser.getUuid() + "]累计的签到次数是:"
				+ (userRelationList == null ? "空" : userRelationList.size()));
		// 判断是否连续签到，最近一次签到日期是否为昨天，不是则重新签到
		// logger.info("检查最近一次签到日期:" +
		// sdf.format(userRelationList.get(0).getCreateTime()) + "与今天" +
		// sdf.format(today) + "的先后顺序");

		if (!userRelationList.get(0).getCreateTime().before(today)) {
			logger.info("最近一次签到日期" + sdf.format(userRelationList.get(0).getCreateTime()) + "]是今天");
			continueSignDay = 1;
			continueSignDays = continueSignDay;
			// }
			for (int i = userRelationList.size(); i > 0; i--) {
				if (i - 2 == -1) {
					break;
				}
				afterDay = Integer.parseInt(sdf.format(userRelationList.get(i - 2).getCreateTime()));
				int currentDay = Integer.parseInt(sdf.format(userRelationList.get(i - 1).getCreateTime())); // 当前签到日期
				
				logger.debug("afterDay : " + afterDay + "、currentDay : " + currentDay);
				int afterDays = getBeforeDay(userRelationList.get(i - 2).getCreateTime());
				logger.debug("A day later the previous day : [" + afterDays + "]对应签到日期");
				
				if (currentDay != afterDays) {
					logger.info("签到日期[" + currentDay + "]与前一天[" + afterDay + "]不连续,则视为重新签到");
					continueSignDay = 1;
					logger.debug("中途中断过连续签到，重置");
				} else {
					continueSignDay++;
					logger.info("签到日期[" + currentDay + "]与前一天[" + afterDay + "]连续，连续签到次数:" + continueSignDay);
					if (continueSignDay > maxContinueSignDay) {
						logger.info("连续签到最大为30天，第31天则视为重新签到");
						continueSignDay = 1;
						logger.debug("签到超过最大连续签到天数，重置");
					}
				}
				continueSignDays = continueSignDay;
				logger.info("连续签到次数：" + continueSignDay);
			}
			logger.debug("continueSignDays : " + continueSignDays);
		} else {
			continueSignDay = 1;
			continueSignDays = continueSignDay;
			for (int i = userRelationList.size(); i > 0; i--) {
				if (i - 2 == -1) {
					break;
				}
				afterDay = Integer.parseInt(sdf.format(userRelationList.get(i - 2).getCreateTime()));
				int currentDay = Integer.parseInt(sdf.format(userRelationList.get(i - 1).getCreateTime())); // 当前签到日期

				logger.debug("currentDay : " + currentDay + "、 afterDay : " + afterDay);
				int afterDays = getBeforeDay(userRelationList.get(i - 2).getCreateTime());
				logger.debug("A day later the previous day : [" + afterDays + "]对应签到日期");

				if (currentDay != afterDays) {
					logger.info("签到日期[" + currentDay + "]与后一天[" + afterDay + "]不连续,则视为重新签到");
					continueSignDay = 1;
					logger.debug("中途中断过连续签到，重置");
				} else {
					continueSignDay++;
					logger.info("签到日期[" + currentDay + "]与后一天[" + afterDay + "]连续，连续签到次数:" + continueSignDay);
					if (continueSignDay > maxContinueSignDay) {
						logger.info("连续签到最大为30天，第31天则视为重新签到");
						continueSignDay = 1;
						logger.debug("签到超过最大连续签到天数，重置");
					}
				}
				continueSignDays = continueSignDay;
				logger.info("连续签到次数：" + continueSignDay);
			}
			logger.debug("continueSignDays : " + continueSignDays);
		}

		userRelationCriteria.setBeginTime(DateUtils.truncate(new Date(), Calendar.MONTH));

		logger.info("用户[" + frontUser.getUuid() + "]自本月起[" + sdf.format(userRelationCriteria.getBeginTime()) + "]的签到次数是:" + signCount);
		map.put("signCount", signCount);
		map.put("continueSignDays", continueSignDays);
		map.put("userRelationList", userRelationList);
		return view;
	}
	/**
	 * 得到前一天的日期
	 * @author Pengzhenggang
	 * @data 2016/4/13
	 */
	public int getBeforeDay(Date date) {
		int tempDate = Integer.parseInt(sdf.format(date));

		GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
		gcLast.setTime(date);
		gcLast.set(Calendar.DAY_OF_MONTH, 1);
		int day_first = Integer.parseInt(sdf.format(gcLast.getTime()));

		int afterDays = 0;
		if (tempDate == day_first) {
			int day = gcLast.get(Calendar.DATE);
			gcLast.set(Calendar.DATE, day - 1);
			afterDays = Integer.parseInt(sdf.format(gcLast.getTime()));
			logger.debug("A day later the previous day : " + afterDays);
		} else {
			GregorianCalendar cl = (GregorianCalendar) Calendar.getInstance();
			cl.setTime(date);
			cl.roll(Calendar.DATE, -1);
			afterDays = Integer.parseInt(sdf.format(cl.getTime()));
			logger.debug("A day later the previous day  : " + afterDays);
		}
		return afterDays;
	}

	@RequestMapping(value = "/submit")
	public String submitCode(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@RequestParam(value = "signDate", required = false) String signDate) {
		final String view = "sign/submit";

		User frontUser = null;

		try {
			frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (frontUser == null) {
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录，请先登录后再签到"));
			return CommonStandard.frontMessageView;
		}
		int rs = 0;
		UserRelationCriteria userRelationCriteria = new UserRelationCriteria(frontUser.getOwnerId());
		userRelationCriteria.setObjectType(ObjectType.sign.name());
		userRelationCriteria.setUuid(frontUser.getUuid());
		userRelationCriteria.setObjectId(Integer.parseInt(sdf.format(new Date())));
		userRelationCriteria.setCurrentStatus(BasicStatus.normal.getId());
		// List<UserRelation> userRelationList =
		// userRelationService.list(userRelationCriteria);
		int signCount = userRelationService.count(userRelationCriteria);
		logger.info("签到次数" + signCount);
		if (signCount > 0) {
			logger.info("今天已签到");
			map.put("message", new EisMessage(OperateResult.failed.getId(), "今天已签到一次"));
			return view;
		}

		UserRelation userRelation = new UserRelation();
		userRelation.setObjectType(ObjectType.sign.name());
		userRelation.setObjectId(Integer.parseInt(sdf.format(new Date())));
		userRelation.setUuid(frontUser.getUuid());
		userRelation.setCurrentStatus(BasicStatus.normal.getId());
		userRelation.setRelationType(UserRelationCriteria.RELATION_LIMIT_UNIQUE);
		userRelation.setOwnerId(frontUser.getOwnerId());
		try {
			rs = userRelationService.insert(userRelation);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (rs == 1) {
			ActivityCriteria activityCriteria = new ActivityCriteria();
			activityCriteria.setCurrentStatus(BasicStatus.normal.getId());
			activityCriteria.setActivityType("sign");
			List<Activity> activityList = activityService.list(activityCriteria);
			if (activityList == null || activityList.size() < 1) {
				logger.info("系统中没有类型为sign的活动");
				return view;
			}
			/**
			 * @author Pengzhenggang
			 * @data 2016/3/29
			 */
			Activity activity = activityList.get(0);
			ActivityProcessor activityProcessor = null;
			try {
				activityProcessor = (ActivityProcessor) applicationContextService.getBean(activity.getProcessor());
			} catch (Exception e) {
				if (activityProcessor == null) {
					logger.error("找不到活动[" + activity.getActivityId() + "]指定的处理器[" + activity.getProcessor());
					map.put("message", new EisMessage(EisError.activityClosed.getId(), "活动尚未开放或已结束"));
					logger.error("活动可能被关闭了，不能参与");
				}
			}
			EisMessage execute = activityProcessor.execute(null, activity, frontUser, request);
			int operateCode = execute.getOperateCode().intValue();
			logger.debug("活动 信息 ：" + execute.getMessage() + "、 成功 ：" + operateCode);
			if (operateCode == OperateResult.success.getId()) {

				UserRelationCriteria userRelationCriterias = new UserRelationCriteria();
				userRelationCriterias.setObjectType(ObjectType.sign.name());
				userRelationCriterias.setUuid(frontUser.getUuid());
				userRelationCriterias.setCurrentStatus(BasicStatus.normal.getId());
				userRelationCriterias.setOwnerId(frontUser.getOwnerId());
				List<UserRelation> userRelationList = userRelationService.list(userRelationCriterias);
				if (userRelationList == null || userRelationList.size() <= 0) {
					userRelationList = new ArrayList<UserRelation>();
					map.put("signCount", 0);
					map.put("continueSignDays", 0);
					map.put("userRelationList", userRelationList);
					return view;
				} else {
					// sortSignRelation(userRelationList);
				}
				int signCounts = userRelationService.count(userRelationCriteria);
				logger.info("用户[" + frontUser.getUuid() + "]累计的签到次数是:" + signCounts);

				int continueSignDay = 0;
				int continueSignDays = 0;
				// 排序
				Collections.sort(userRelationList, new Comparator<UserRelation>() {
					@Override
					public int compare(UserRelation o1, UserRelation o2) {
						if (o1.getCreateTime().after(o2.getCreateTime())) {
							System.out.println(o1.getCreateTime() + ":" + o2.getCreateTime() + "===>" + 1);
							return -1;
						}
						System.out.println(o1.getCreateTime() + ":" + o2.getCreateTime() + "===>" + 0);
						return 1;
					}
				});
				int afterDay = Integer.parseInt(sdf.format(DateUtils.addDays(new Date(), -1)));
				// Date today = DateUtils.truncate(new Date(),
				// Calendar.DAY_OF_MONTH);
				Date today = DateUtils.truncate(new Date(), Calendar.DATE);
				logger.info("角色[" + frontUser.getUuid() + "]累计的签到次数是:"
						+ (userRelationList == null ? "空" : userRelationList.size()));
				// 判断是否连续签到，最近一次签到日期是否为昨天，不是则重新签到
				// logger.info("检查最近一次签到日期:" +
				// sdf.format(userRelationList.get(0).getCreateTime()) + "与今天" +
				// sdf.format(today) + "的先后顺序");
				if (!userRelationList.get(0).getCreateTime().before(today)) {
					logger.info("最近一次签到日期" + sdf.format(userRelationList.get(0).getCreateTime()) + "]是今天");
					continueSignDay = 1;
					continueSignDays = continueSignDay;
					// }
					for (int i = userRelationList.size(); i > 0; i--) {
						if (i - 2 == -1) {
							break;
						}
						afterDay = Integer.parseInt(sdf.format(userRelationList.get(i - 2).getCreateTime()));
						int currentDay = Integer.parseInt(sdf.format(userRelationList.get(i - 1).getCreateTime())); // 当前签到日期
						
						logger.debug("afterDay : " + afterDay + "、currentDay : " + currentDay);
						int afterDays = getBeforeDay(userRelationList.get(i - 2).getCreateTime());
						logger.debug("A day later the previous day : [" + afterDays + "]对应签到日期");
						
						if (currentDay != afterDays) {
							logger.info("签到日期[" + currentDay + "]与后一天[" + afterDay + "]不连续,则视为重新签到");
							continueSignDay = 1;
							logger.debug("中途中断过连续签到，重置");
						} else {
							continueSignDay++;
							logger.info("签到日期[" + currentDay + "]与后一天[" + afterDay + "]连续，连续签到次数:" + continueSignDay);
							if (continueSignDay > maxContinueSignDay) {
								logger.info("连续签到最大为30天，第31天则视为重新签到");
								continueSignDay = 1;
								logger.debug("签到超过最大连续签到天数，重置");
							}
						}
						continueSignDays = continueSignDay;
						logger.info("连续签到次数：" + continueSignDay);
					}
					logger.debug("continueSignDays : " + continueSignDays);
				}
				
				map.put("continueSignDays", continueSignDays);
				map.put("message", new EisMessage(OperateResult.success.getId(), "恭喜！签到成功！"));
				return view;
			} else {
				return execute.getMessage();
			}
			// for(Activity activity : activityList){
			// activityService.execute(activity, frontUser,
			// HttpUtils.getRequestDataMap(request));
			// }
		} else {
			logger.info("用户[" + frontUser.getUuid() + "]未能对日期[" + userRelation.getObjectId() + "]签到");
			map.put("message", new EisMessage(OperateResult.failed.getId(), "对不起，签到失败"));
		}
		return view;
	}

}
