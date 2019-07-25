package com.maicard.wpt.controller.shared;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maicard.annotation.IgnoreLoginCheck;
import com.maicard.annotation.IgnorePrivilegeCheck;
import com.maicard.common.base.BaseController;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.HttpsService;
import com.maicard.common.service.SiteDomainRelationService;
import com.maicard.common.util.HttpUtils;
import com.maicard.common.util.JsonUtils;
import com.maicard.common.util.NumericUtils;
import com.maicard.money.service.MoneyService;
import com.maicard.product.service.ActivityService;
import com.maicard.security.criteria.UserCriteria;
import com.maicard.security.domain.User;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.FrontUserService;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.OperateResult;
import com.maicard.standard.SecurityStandard.UserTypes;
import com.maicard.wpt.domain.WeixinMsg;
import com.maicard.wpt.service.ScanCodeService;
import com.maicard.wpt.service.WeixinMsgService;
import com.maicard.wpt.service.WeixinPlatformService;
import com.maicard.wpt.utils.weixin.StringFormat;
import static com.maicard.standard.CommonStandard.frontMessageView;

/**
 * 微信第三方公众平台接口
 *
 *
 * @author NetSnake
 * @date 2017年1月20日
 *
 */
@Controller
@RequestMapping(value = "/wxp")
public class WeixinPlatformApiController extends BaseController{

	@Resource
	private ActivityService activityService;
	@Resource
	private CertifyService certifyService;

	@Resource
	private ConfigService configService;

	@Resource
	private SiteDomainRelationService siteDomainRelationService;

	@Resource
	private FrontUserService frontUserService;

	@Resource
	private MoneyService moneyService;


	@Resource
	private ScanCodeService scanCodeService;

	@Resource 
	private WeixinMsgService weixinMsgService;

	@Resource
	private WeixinPlatformService weixinPlatformService;

	@Resource HttpsService httpsService;

	private final String loginUrl = "/user/login.shtml";

	final boolean ENCRYPT_MSG = true;

	//GUIDE_ENABLE = configService.getBooleanValue(DataName.GUIDE_ENABLED.toString(),0);

	@Resource ApplicationContextService applicationContextService;
	//private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5','6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	private ObjectMapper om = JsonUtils.getNoDefaultValueInstance();
	private final SimpleDateFormat sdf = new SimpleDateFormat(CommonStandard.defaultDateFormat);
	/*private static String getFormattedText(byte[] bytes) {  
		int len = bytes.length;  
		StringBuilder buf = new StringBuilder(len * 2);  
		// 把密文转换成十六进制的字符串形式  
		for (int j = 0; j < len; j++) {  
			buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);  
			buf.append(HEX_DIGITS[bytes[j] & 0x0f]);  
		}  
		return buf.toString();  
	}  
	private static String[] stringSort(String [] s) {
		List<String> list = new ArrayList<String>(s.length);
		for (int i = 0; i < s.length; i++) {
			list.add(s[i]);
		}
		Collections.sort(list);
		return list.toArray(s);
	}*/


	/**
	 * 第三方公众平台接收授权信息
	 * 接收腾讯推送的component_verify_ticket
	 */
	@ResponseBody
	@IgnoreLoginCheck
	@RequestMapping(value="/auth")
	public String receiveAuthMessage(HttpServletRequest request,HttpServletResponse response, ModelMap map){
		final String result = "success";
		long ownerId = (long)map.get("ownerId");
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return frontMessageView;		
		}
		String message = HttpUtils.readPostData(request);
		logger.debug("对方提交了数据流:" + message);
		if(StringUtils.isBlank(message)){
			return result;
		}
		try {
			weixinPlatformService.receiveAuthMessage(message, ownerId);
		} catch (Exception e) {
			e.printStackTrace();
		}


		return result;
	}

	/**
	 * 客户公众号统一授权给第三方平台
	 * @throws IOException 
	 */
	@RequestMapping(value="/confirm")
	@IgnorePrivilegeCheck
	public String clientConfirm(HttpServletRequest request,HttpServletResponse response, ModelMap map) throws IOException{
		long ownerId = (long)map.get("ownerId");
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return frontMessageView;		
		}

		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if(partner == null){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.id,"请先登录"));
			return frontMessageView;		
		}
		final String authCode = ServletRequestUtils.getStringParameter(request, "auth_code",null);
		int expire = ServletRequestUtils.getIntParameter(request,"expires_in",0);
		int guideStep = (int)partner.getLongExtraValue(DataName.GUIDE_STEP.toString());
		logger.debug("收到客户确认授权[authCode=" + authCode + ",expire=" + expire);
		if(guideStep > 0){
			guideStep++;
			partner.setExtraValue(DataName.GUIDE_STEP.toString(), String.valueOf(guideStep));
		}
		int rs = weixinPlatformService.updateClientInfo(partner, authCode, expire);
		if(rs != OperateResult.success.id){
			if(rs == EisError.dataDuplicate.id){
				map.put("message", new EisMessage(rs,"您的公众号已由其他帐号授权，授权失败"));
			} else {
				map.put("message", new EisMessage(rs,"您的公众号授权失败"));
			}
			return frontMessageView;
		}
		map.put("message", new EisMessage(rs,"授权成功"));
		response.setStatus(HttpStatus.TEMPORARY_REDIRECT.value());

		if(guideStep > 0){
			response.sendRedirect("/guide/step" + guideStep + CommonStandard.DEFAULT_PAGE_SUFFIX);
		} else {
			response.sendRedirect("/");

		}
		return null;
	}

	/**
	 * 发给客户公众号的消息，也会发送给我们第三方平台
	 * 此处就是接收那些发给公众号的消息
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/msg/{appId}")
	@IgnoreLoginCheck
	public String clientMessage(HttpServletRequest request,HttpServletResponse response, ModelMap map, @PathVariable String appId){
		long ownerId = NumericUtils.parseLong(map.get("ownerId"));
		//此处不能使用map中已存在的sitePartnerId，因为收到微信消息时，访问的是我方第三方平台的域名
		long sitePartnerId = weixinPlatformService.getSitePartnerId(appId, ownerId);

		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return frontMessageView;		
		}

		final boolean weixinNotAutoRegister = configService.getBooleanValue(DataName.weixinNotAutoRegister.toString(), ownerId);
		/**
		 * 是否保存收到的消息
		 */
		boolean saveMsg = true;

		String message = HttpUtils.readPostData(request);
		String text = null;
		/**
		 * 向微信服务器响应客户消息的返回数据
		 */
		String textResponse = null;
		logger.debug("接收到客户公众号[" + appId + "]的消息:" + message);
		try {
			text = StringFormat.stripCdata(weixinPlatformService.decrypt(message, ownerId));
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("客户公众号[" + appId + "]的消息解密清理后:" + text);

		WeixinMsg weixinMsg= StringFormat.formatXml2WeixinMsg(text);
		
		if(weixinMsg == null){
			logger.warn("获取到的消息为空或无法转换");
			return null;
		}
		weixinMsg.setInviter(sitePartnerId);
		weixinMsg.setOwnerId(ownerId);
		try {
			logger.debug("获取到消息:" + om.writeValueAsString(weixinMsg));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}


		String openId = weixinMsg.getFromUserName();
		//使用openId登陆
		UserCriteria userCriteria = new UserCriteria();
		userCriteria.setUserTypeId(UserTypes.frontUser.getId());
		userCriteria.setAuthKey(openId);
		int existCount = frontUserService.count(userCriteria);
		User frontUser = null;
		logger.debug("根据openId=" + openId + "]查询auth_key等于它的用户数量是:" + existCount);
		if(existCount <= 0){
			if(weixinMsg.getMsgType().equals("event") && weixinMsg.getEvent().equalsIgnoreCase("unsubscribe")){
				logger.info("用户取消关注不进行创建操作");
			} else {

				if(!weixinNotAutoRegister){
					logger.debug("当前系统配置是微信用户自动注册");
					//使用openId注册新用户
					String identify = null;
					if(StringUtils.isNotBlank(weixinMsg.getEventKey())){
						identify = weixinMsg.getEventKey().replaceAll("qrscene_","");
					}

					frontUser = weixinPlatformService.createUser(openId, identify, sitePartnerId, ownerId);

				} else {
					logger.debug("当前系统配置是微信用户不自动注册");

				}

			}

		} else {
			//该OpenId用户已存在
			frontUser = certifyService.forceLogin(request, response, openId);
			logger.info("使用openId[" + openId + "]登陆:" + frontUser.getUuid());
		}
		if(frontUser != null){
			weixinMsg.setOwnerId(frontUser.getOwnerId());
			if(existCount > 0){
				weixinPlatformService.updateUserInfo(frontUser, openId, sitePartnerId);
				try {
					frontUserService.updateAsync(frontUser);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		//}
		switch (weixinMsg.getMsgType()){

		case "text":{
			try {
				textResponse = weixinPlatformService.processTextMessage(frontUser,request, weixinMsg, sitePartnerId, ownerId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			textResponse = StringFormat.formatText2Xml(weixinMsg.getFromUserName(), weixinMsg.getToUserName(), textResponse);
			break;
		}
		case "event":{
			if(weixinMsg.getToUserName().equals("gh_3c884a361561")){
				//第三方平台测试专用
				textResponse = StringFormat.formatText2Xml(weixinMsg.getFromUserName(), weixinMsg.getToUserName(), weixinMsg.getEvent() + "from_callback");
				logger.debug("处理第三方平台测试专用,收到的消息:" + text + "，回复消息:" + textResponse);
				break;
			} else {
				switch(weixinMsg.getEvent().toLowerCase()){
				case "subscribe":{
					
					textResponse = weixinPlatformService.processSubscribe(frontUser, request, weixinMsg, sitePartnerId, ownerId);//generateDefaultSubscribeMessage(request, weixinMsg, ownerId);

					/*Date date=new Date(weixinMsg.getCreateTime()*1000);
					SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String str=format.format(date);
					logger.info("微信用户/"+weixinMsg.getFromUserName() + "/" + weixinMsg.getEventKey() + "关注时间:" + str);
					textResponse = processSubscribeActivity(weixinMsg);
					if(textResponse == null){
						textResponse = weixinPlatformService.processSubscribe(request, weixinMsg, sitePartnerId, ownerId);//generateDefaultSubscribeMessage(request, weixinMsg, ownerId);
					}
					if(textResponse != null){
						textResponse = StringFormat.formatText2Xml(weixinMsg.getFromUserName(), weixinMsg.getToUserName(), textResponse);   
					}
					*/

					//发送宝洁的推广消息
					//2015.12.16取消发送,NetSnake
					//					weixinPlatformService.sendMessage(new WeixinRichTextMessage(weixinMsg.getFromUserName(), weixinMsg.getToUserName(), welcomeBaojieTitle, welcomeBaojieContent, welcomeBaojiePicUrl, welcomeBaojieUrl,10, frontUser.getOwnerId())); 
					//
					//					}

					break;
				}
				case"unsubscribe":{
					logger.info(weixinMsg.getFromUserName() + "于[" + sdf.format(new Date(weixinMsg.getCreateTime()*1000)) + "]取消关注");					
					break;
				}
				case"scancode_waitmsg":{
					Object object = scanCodeService.getScanResult(frontUser, weixinMsg, null);
					if(object == null){
						logger.info("未能获得扫码结果");
						String msg = "对不起，扫码失败";
						textResponse = StringFormat.formatText2Xml(weixinMsg.getFromUserName(), weixinMsg.getToUserName(), msg);   
					} else 	if( object instanceof String){
						textResponse = StringFormat.formatText2Xml(weixinMsg.getFromUserName(), weixinMsg.getToUserName(), object.toString());   
					} else 	if( object instanceof EisMessage){
						textResponse = StringFormat.formatText2Xml(weixinMsg.getFromUserName(), weixinMsg.getToUserName(), ((EisMessage)object).getMessage());   
					} else {
						logger.error("未知的扫码返回结果:" + object);
					}

					break;
				}
				case "scan":{
					//扫码关注或进入
					textResponse = weixinPlatformService.processScanMessage(frontUser, request, weixinMsg, 0, ownerId);
					if(textResponse != null){
						textResponse = StringFormat.formatText2Xml(weixinMsg.getFromUserName(), weixinMsg.getToUserName(), textResponse); 
					}
					break;
				}
				case"user_get_card":{
					//获取卡券
					//2015.12.10 暂停使用
					//2015.12.14 启用
					weixinPlatformService.processCouponMessage(frontUser, weixinMsg);
					break;

				}
				case"click":{
					logger.info("自定义单键值"+weixinMsg.getEventKey());
					saveMsg = false;
					textResponse = weixinPlatformService.processTextMessage(frontUser,request, weixinMsg, sitePartnerId, ownerId);
					textResponse = StringFormat.formatText2Xml(weixinMsg.getFromUserName(), weixinMsg.getToUserName(), textResponse); 				
					break;
				}
				case"view":{
					saveMsg = false;
					break;
				}
				default:{

				}

				}
			}
		}
		}	
		if(saveMsg){
			weixinMsgService.insert(weixinMsg);
			weixinMsgService.insertAsUserMessage(weixinMsg, null, 0);
		}
		if(ENCRYPT_MSG){
			String clearText = textResponse;
			final String timeStamp = String.valueOf(System.currentTimeMillis());
			final String  nonce  = DigestUtils.md5Hex(timeStamp);
			try {
				textResponse = weixinPlatformService.encryptMsg(clearText, timeStamp, nonce, ownerId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			logger.info("对消息[" + weixinMsg.getContent() + "，类型:" + weixinMsg.getMsgType() + ",key=" + weixinMsg.getEventKey() + "]返回内容:" + textResponse + ",加密前明文:" + clearText);
		} else {
			logger.info("对消息[" + weixinMsg.getContent() + "，类型:" + weixinMsg.getMsgType() + ",key=" + weixinMsg.getEventKey() + "]返回内容:" + textResponse);

		}

		return textResponse;
	}


	@RequestMapping("/clientAuth")
	@IgnorePrivilegeCheck
	public String clientAuth(HttpServletRequest request,HttpServletResponse response, ModelMap map) throws IOException{
		long ownerId = 0;
		try{
			ownerId = (long) map.get("ownerId");
		}catch(Exception e){

		}
		if(ownerId < 1){
			logger.error("无法获取ownerId");
		}
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		if (partner == null) {
			logger.error("发起授权请求必须先登录合作伙伴，找不到已登录的合作伙伴");
			response.sendRedirect(loginUrl);
			return null;
		}
		String appId = weixinPlatformService.getPlatformAppId(ownerId);
		String preAuthCode = weixinPlatformService.getPreAuthCode(ownerId);
		String protocol = request.getProtocol().startsWith("https") ? "https" : "http";
		String returnUrl = protocol + "://" + request.getServerName() + ":" + request.getServerPort() + "/wxp/confirm.shtml";
		String url = "https://mp.weixin.qq.com/cgi-bin/componentloginpage?component_appid=" + appId + "&pre_auth_code=" + preAuthCode + "&redirect_uri=" + returnUrl;
		response.setStatus(HttpStatus.TEMPORARY_REDIRECT.value());
		logger.debug("重定向到:" + url);
		try {
			response.sendRedirect(url);
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return null;
	}




	@ResponseBody
	@RequestMapping(value="/event" ,method=RequestMethod.GET)//用户分享后回调的动作
	@IgnoreLoginCheck
	public String shareApi(HttpServletRequest request,HttpServletResponse response, ModelMap map,
			@RequestParam("outUuid") String openID,
			@RequestParam("outOperateCode")String operateCode,
			@RequestParam("outShareUrl")String url){
		logger.info("openID是"+openID);
		logger.info("用户进行的是"+operateCode+"的操作");
		logger.info("分享的url是"+url);
		WeixinMsg message=new WeixinMsg();
		message.setCreateTime(Long.valueOf(System.currentTimeMillis()/ 1000));
		message.setURL(url);
		message.setFromUserName(openID);
		message.setEvent(operateCode);
		try{
			weixinMsgService.insert(message);
		}
		catch(Exception e)
		{
			e.getStackTrace();
		}
		return "{}";
	}


	/**
	 * 长期有效的二维码需要是纯数字
	 * 
	 * @param request
	 * @param response
	 * @param map
	 * @param key
	 * @param type
	 * @return
	 */
	@ResponseBody
	@IgnoreLoginCheck
	@RequestMapping (value="/inviteImage",method=RequestMethod.GET)
	public String makeImage(HttpServletRequest request,HttpServletResponse response, ModelMap map,
			@RequestParam ("key")String key,
			@RequestParam (value="type",required=false)String type){
		long ownerId = NumericUtils.parseLong(map.get("ownerId"));
		long sitePartnerId = NumericUtils.parseLong(map.get(DataName.sitePartnerId.toString()));


		String accessToken = null;
		if(sitePartnerId > 0){
			accessToken = weixinPlatformService.getClientAccessToken(sitePartnerId);
		} else {
			accessToken = weixinPlatformService.getSingleAccessToken(ownerId);

		}
		String url="https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="+accessToken;
		String memo = null;

		if(type != null && type.toLowerCase().equals("limit")){
			type = "QR_LIMIT_SCENE";
		} else {
			type = "QR_SCENE";
		}
		if(StringUtils.isNumeric(key)){
			memo="{\"expire_seconds\":604800,\"action_name\":\"" + type + "\",\"action_info\":{\"scene\":{\"scene_id\":"+key+"}}}";
		} else {
			memo="{\"expire_seconds\":604800,\"action_name\":\"" + type + "\",\"action_info\":{\"scene\":{\"scene_str\":\""+key+"\"}}}";

		}
		String str="";
		ObjectMapper om= new ObjectMapper();
		try{
			logger.info("url:"+url+"|body:"+memo);
			str=httpsService.httpsPost(url, memo);
			logger.info("生成关注二维码的返回是:"+str);
			String ticket=om.readTree(str).get("ticket").asText();
			url="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+ticket;
		}
		catch(Exception e)
		{}
		return "<img src=\""+url+"\" alt='关注'/>";

	}

	//检查是否是活动带来的关注
	@SuppressWarnings("unused")
	private String processSubscribeActivity( WeixinMsg message) {
		String eventKey = message.getEventKey();
		if(StringUtils.isBlank(eventKey)){
			logger.warn("事件码为空");
			return null;
		}
		eventKey = eventKey.replaceFirst("^qrscene_", "");
		String[] data = eventKey.split("_");
		if(data == null || data.length < 4){
			logger.warn("不规范的事件码:" + eventKey);
			return null;
		}
		if(!StringUtils.isNumeric(data[1])){
			logger.warn("不规范的事件码:" + eventKey + ",第二部分不是数字:" + data[1]);
			return null;
		}
		int activityId = Integer.parseInt(data[1]);
		String returnMsg = activityService.createActivityBeginUrl(activityId, data[3]);
		logger.info("尝试为" + activityId + "#活动请求活动连接,推广ID:" + data[3] + ",结果:" + returnMsg);

		return returnMsg;
		//	return "<a href=\"http://yht.yuanhuitongweb.com/content/youxihuode/xydlp.shtml\">欢迎参加活动的宝贝，点我参加活动</a>";
	}


	public String getWeixinCouponJsData(HttpServletRequest request,HttpServletResponse response, ModelMap map){
		long ownerId = (long)map.get("ownerId");
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return CommonStandard.frontMessageView;		
		}
		//String couponToken = weixinPlatformService.getCouponToken(ownerId);

		return null;
	}

}

