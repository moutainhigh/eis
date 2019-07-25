package com.maicard.wpt.custom.youbao;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.maicard.common.base.BaseController;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.util.NumericUtils;
import com.maicard.product.criteria.ActivityCriteria;
import com.maicard.product.domain.Activity;
import com.maicard.product.domain.ActivityReward;
import com.maicard.product.service.ActivityService;
import com.maicard.security.domain.User;
import com.maicard.security.service.CertifyService;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.EisError;
import com.maicard.standard.OperateResult;
import com.maicard.standard.SecurityStandard.UserStatus;
import com.maicard.standard.SecurityStandard.UserTypes;
import com.maicard.wpt.misc.weixin.WeixinCouponUtils;
import com.maicard.wpt.service.WeixinService;

/**
 * 奖品控制器
 * 
 * @author Pengzhenggang
 * @date 2016-10-28
 */
@Controller
@RequestMapping("/gift")
public class GiftController extends BaseController {
	@Resource
	private CertifyService certifyService;
	@Resource
	private ActivityService activityService;
	@Resource
	private WeixinService weixinService;
	/**
	 * 发放红包
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/sendRedPackage", method = RequestMethod.POST)
	public String sendRedPackage(HttpServletRequest request, HttpServletResponse response, ModelMap map)
			throws Exception {
		//////////////////////////// 标准检查流程 ///////////////////////////////////
		User frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());

		if (frontUser == null || frontUser.getCurrentStatus() != UserStatus.normal.getId()) {
			map.put("message", new EisMessage(EisError.userNotFoundInSession.getId(), "您尚未登录，请先登录"));
			return CommonStandard.frontMessageView;
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
			return CommonStandard.frontMessageView;
		}

		if (frontUser.getOwnerId() != ownerId) {
			logger.error("用户[" + frontUser.getUuid() + "]的ownerId[" + frontUser.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
			map.put("message", new EisMessage(EisError.ownerNotMatch.getId(), "您尚未登录，请先登录"));
			return CommonStandard.frontMessageView;
		}
		//////////////////////////// 标准检查流程结束 ///////////////////////////////
		//得到参数
		String giftLevelStr = request.getParameter("giftLevel");	//奖品等级
		String activityCode = request.getParameter("activityCode");	//活动Code
		if (StringUtils.isBlank(giftLevelStr)) {
			logger.error("奖品等级为空[" + giftLevelStr + "]");
			map.put("message", new EisMessage(EisError.dataError.getId(), "奖品等级不能为空"));
			return CommonStandard.frontMessageView;
		}
		if (StringUtils.isBlank(activityCode)) {
			logger.error("活动code不能为空[" + activityCode + "]");
			map.put("message", new EisMessage(EisError.dataError.getId(), "活动code不能为空"));
			return CommonStandard.frontMessageView;
		}
		int giftLevel = 0;
		if (NumericUtils.isNumeric(giftLevelStr)) {
			giftLevel = Integer.parseInt(giftLevelStr);
		}
		//查找活动
		ActivityCriteria activityCriteria = new ActivityCriteria();
		activityCriteria.setOwnerId(frontUser.getOwnerId());
		activityCriteria.setActivityCode(activityCode);
		List<Activity> activityLists = activityService.list(activityCriteria);
		if (activityLists == null || activityLists.size() < 1) {
			logger.error("没找到[" + activityCode + "]活动");
			map.put("message", new EisMessage(EisError.dataError.getId(), "没找到[" + activityCode + "]活动"));
			return CommonStandard.frontMessageView;
		}
		Activity activity = activityLists.get(0);
		//奖品参数
		String[] promotionsArr = activity.getPromotion().split("\\|");
		if (promotionsArr == null || promotionsArr.length < 1) {
			logger.error("无法解析活动对象的数据：" + activity.getPromotion());
			map.put("message", new EisMessage(EisError.dataError.getId(), "无法解析活动对象的数据：" + activity.getPromotion()));
			return CommonStandard.frontMessageView;
		}
		ArrayList<ActivityReward> activityRewardList = new ArrayList<ActivityReward>();
		for (int i = 0; i < promotionsArr.length; i++) {
			logger.debug("奖品 " + i + " : " + promotionsArr[i]);
			ActivityReward activityReward = new ActivityReward(i + 1, promotionsArr[i]);
			activityRewardList.add(activityReward);
		}
		//将奖品排序
		Collections.sort(activityRewardList, new Comparator<ActivityReward>(){
			@Override
			public int compare(ActivityReward o1, ActivityReward o2) {
				if (o1.getRewardRate() >= o2.getRewardRate()) {
					System.out.println(o1.getRewardRate() + ":" + o2.getRewardRate() + "===>" + 1);
					return 1;
				}
				System.out.println(o1.getRewardRate() + ":" + o2.getRewardRate() + "===>" + 0);
				return -1;
			}
		});
		
		logger.debug("奖品长度 ：" + promotionsArr.length + " 奖品 ： " + promotionsArr[promotionsArr.length - 1]);
		String[] data = promotionsArr[promotionsArr.length - 1].split("#");
		if (data == null || data.length < 3) {
			logger.error(activity.getActivityId() + "#活动的奖励数据异常:" + activity.getPromotion());
			map.put("message",  new EisMessage(EisError.dataError.getId(), activity.getActivityId() + "#活动的奖励数据异常:" + activity.getPromotion()));
			return CommonStandard.frontMessageView;
		}
		if (!NumericUtils.isNumeric(data[1])) {
			logger.error(activity.getActivityId() + "#活动的奖励数据异常，第二个不是数字:" + activity.getPromotion());
			map.put("message",  new EisMessage(EisError.dataError.getId(), activity.getActivityId() + "#活动的奖励数据异常，第二个不是数字:" + activity.getPromotion()));
			return CommonStandard.frontMessageView;
		}
		int level = Integer.parseInt(data[1]);	//活动奖品等级
		//奖品等级是否一样，一样就发放红包
		if (giftLevel == level) {
			logger.info("当中奖级别是[" + giftLevel + "]时，抽中的是：G微信-大1024北京物美现金红包测试（三等奖---红包）");
			long timeStamp = System.currentTimeMillis();
			String ts = String.valueOf(timeStamp / 1000);
			final String nonceStr = DigestUtils.md5Hex(ts).toUpperCase();
			logger.debug("nonceStr :" + nonceStr);

			String appid = weixinService.getAppId(frontUser.getOwnerId());
			String mchId = weixinService.getWeixinPayMechId(frontUser.getOwnerId());
			String openId = frontUser.getUsername();

			logger.debug("appid : " + appid);
			logger.debug("mchId : " + mchId);
			logger.debug("openId :" + openId);
			if (StringUtils.isNotBlank(appid) && StringUtils.isNotBlank(mchId) && StringUtils.isNotBlank(openId)) {
				try {
					String sendRedPackMessage = WeixinCouponUtils.sendRedPack(nonceStr, mchId, appid, openId, 100, "58.59.18.108");
					if (sendRedPackMessage != null || sendRedPackMessage != "") {
						NodeList books = parse(sendRedPackMessage);
						if (books != null) {
							for (int i = 0; i < books.getLength(); i++) {
								Node book = books.item(i);
								// logger.info("节点=" + book.getNodeName() + "\ttext=" + book.getFirstChild().getNodeValue());
								if (book.getNodeName().equals("err_code_des")) {
									// logger.info("最终返回的错误代码描述 : 节点=" + book.getNodeName() + "\ttext=" + book.getFirstChild().getNodeValue());
									if (book.getFirstChild().getNodeValue().equals("发放成功")) {
										map.put("message", new EisMessage(OperateResult.success.getId(), book.getFirstChild().getNodeValue() + "，请在公众号中查看"));
										return CommonStandard.frontMessageView;
									} else {
										map.put("message", new EisMessage(OperateResult.success.getId(), book.getFirstChild().getNodeValue()));
										return CommonStandard.frontMessageView;
									}
								}
							}
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			map.put("message", new EisMessage(OperateResult.failed.getId(), "领取奖品等级错误"));
		}
		return CommonStandard.frontMessageView;
	}
	/**
	 * XML格式的字符串读取 java自带的DOM解析
	 * 
	 * @param protocolXML
	 * @return
	 */
	public NodeList parse(String protocolXML) {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document doc = null;
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(new InputSource(new StringReader(protocolXML)));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Element root = doc.getDocumentElement();
		NodeList books = root.getChildNodes();
//		if (books != null) {
//			for (int i = 0; i < books.getLength(); i++) {
//				Node book = books.item(i);
//				// logger.info("节点=" + book.getNodeName() + "\ttext=" + book.getFirstChild().getNodeValue());
//				if (book.getNodeName().equals("err_code_des")) {
//					// logger.info("最终返回的错误代码描述 : 节点=" + book.getNodeName() + "\ttext=" + book.getFirstChild().getNodeValue());
//					return book;
//				}
//			}
//		}
		return books;
	}
}
