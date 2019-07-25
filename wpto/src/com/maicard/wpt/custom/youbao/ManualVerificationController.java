package com.maicard.wpt.custom.youbao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maicard.common.base.BaseController;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.util.JsonUtils;
import com.maicard.security.domain.User;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.FrontUserService;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.EisError;
import com.maicard.standard.OperateResult;
import com.maicard.standard.SecurityStandard.UserTypes;
import com.maicard.wpt.service.WeixinPlatformService;

/**
 * 
 * @author Feng 人工核销的接口用来和后台进行通信
 * @version 2017-5-9
 **/
@Controller
@RequestMapping(value = "/manualverification")
public class ManualVerificationController extends BaseController {
	@Resource
	private CertifyService certifyService;
	@Resource
	private WeixinPlatformService weixinPlatformService;
	@Resource
	private FrontUserService frontUserService;

	// 核销的主控接口
	@SuppressWarnings("unused")
	@RequestMapping(value = "/personCheck/index")
	public String manualVerification(HttpServletRequest request,
			HttpServletResponse response, ModelMap map) {
		// ---标准流程验证---//
		User frontUser = certifyService.getLoginedUser(request, response,
				UserTypes.frontUser.getId());

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

		String jsonpack = null;
		try {
			BufferedReader tBufferedReader = new BufferedReader(
					new InputStreamReader(request.getInputStream()));
			StringBuffer tStringBuffer = new StringBuffer();
			String sTempOneLine = new String("");

			while ((sTempOneLine = tBufferedReader.readLine()) != null) {
				tStringBuffer.append(sTempOneLine);
			}
			jsonpack = tStringBuffer.toString();

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		logger.debug("得到的json数据" + jsonpack);

		// 通过Jackson来解析json数据
		ObjectMapper om = JsonUtils.getNoDefaultValueInstance();

		try {
			JsonNode jn = om.readTree(jsonpack);
			// 通过这个活动的编号来确定该发什么样的优惠券
			JsonNode jsonNode = jn.path("data");
			String activityNo = jsonNode.get(0).path("activityNo").asText();
			String code = jn.path("code").asText();
			// this is uuid not uid,I need uuid to send message to users
			String uuid = jn.path("uuid").asText();
			// 通过UUID来获得用户的openID
			User user = frontUserService.select(Long.parseLong(uuid));
			String openid = user.getAuthKey();

			if (code.equals("1")) {
				int result = SendCustomMessage(ownerId, code, openid,
						activityNo, uuid);

				if (result == OperateResult.success.getId()) {
					logger.info("下发领取通知成功" + result);
					map.put("message",
							new EisMessage(OperateResult.success.getId(),
									"下发消息成功:" + result));
					return CommonStandard.frontMessageView;
				}
				if (result == OperateResult.failed.getId()) {
					logger.info("下发领取通知失败" + result);
					map.put("message",
							new EisMessage(OperateResult.failed.getId(),
									"下发领取消息失败:" + result));
					return CommonStandard.frontMessageView;
				}
			}
			// 当人工审核完成，但是不符合本次活动的条件下方法消息告知用户不可以
			if (code.equals("2")) {
				int result = SendCustomMessage(ownerId, code, openid,
						activityNo, uuid);
				if (result == OperateResult.success.getId()) {
					logger.info("下发不符合条件告知消息成功" + result);
					map.put("message",
							new EisMessage(OperateResult.success.getId(),
									"下发不符合条件告知消息成功:" + result));
					return CommonStandard.frontMessageView;
				}
				if (result == OperateResult.failed.getId()) {
					logger.info("下发不符合条件告知消息失败" + result);
					map.put("message",
							new EisMessage(OperateResult.failed.getId(),
									"下发不符合条件告知消息失败:" + result));
					return CommonStandard.frontMessageView;
				}
			}
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 通过存入用户的ownerID然后和组装信息回复用户
		// (String message, int messageType, long sitePartnerId, long ownerId

		return CommonStandard.frontMessageView;
	}

	// 新建一个消息处理的方法
	public int SendCustomMessage(long ownerId, String judgeCode, String openid,
			String activityNo, String uuid) {
		// 增加一个判断用来判断发什么对应的消息
		// 当判断的值是1的时候，下发成功的通知，跳转到用户领取优惠券的页面
		if (judgeCode.equals("1")) {

			String URL = "http://yht.yuanhuitongweb.com/activity/checkSucess/index.shtml?"
					+ activityNo + "&uuid=" + uuid;
			String picurl = "http://yht.yuanhuitongweb.com/style/mobile/images/ScanTicket/guide.png";

	
			
			String messages = "{\"touser\":\""
					+ openid
					+ "\",\"msgtype\":\"news\",\"news\":{\"articles\":[{\"title\":\"上传反馈\",\"description\":\"您符合活动要求请点击领取优惠券\""
					+ "\"url\":\"" + URL + "\",\"picurl\":\"" + picurl
					+ "\"}]}}";

			int result = weixinPlatformService.sendCustomServiceMessage(
					messages, 6, 0, ownerId);
			if (result == OperateResult.success.getId()) {
				return OperateResult.success.getId();
			} else {
				return OperateResult.failed.getId();
			}
		}

		// 当judgeCode是2的时候，表明人工判断的结果是不符合本次活动的要求
		if (judgeCode.equals("2")) {

			String messages = "{\"touser\":\""
					+ openid
					+ "\",\"msgtype\":\"text\":{\"content\":\"您上传的图片不符合本次活动的要求\"}}";
			int result = weixinPlatformService.sendCustomServiceMessage(
					messages, 2, 0, ownerId);
			if (result == OperateResult.success.getId()) {
				return OperateResult.success.getId();
			} else {
				return OperateResult.failed.getId();
			}
		}
		// 当judgeCode是7的时候表明用户上传的图片比较模糊，提示用户重新上传
		// if (judgeCode.equals("7")) {
		// String URL = "";
		// String picurl = "";
		//
		// String messages = "{\"touser\":\""
		// + openid
		// +
		// "\",\"msgtype\":\"news\",\"news\":{\"acticles\":[{\"title\":\"上传反馈\",\"description\":\"您符合活动要求请点击领取优惠券\""
		// + "\"url\":\"" + URL + "\",\"picurl\":\"" + picurl
		// + "\"}]}}";
		//
		// int result = weixinPlatformService.sendCustomServiceMessage(
		// messages, 2, 0, ownerId);
		// if (result == OperateResult.success.getId()) {
		// return OperateResult.success.getId();
		// } else {
		// return OperateResult.failed.getId();
		// }
		//
		// }

		return OperateResult.failed.getId();
	}

}
