package com.maicard.wpt.controller.common;


import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maicard.common.base.BaseController;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.HttpsService;
import com.maicard.common.service.SiteDomainRelationService;
import com.maicard.common.util.HttpUtils;
import com.maicard.common.util.NumericUtils;
import com.maicard.money.service.MoneyService;
import com.maicard.product.service.ActivityService;
import com.maicard.security.criteria.UserCriteria;
import com.maicard.security.domain.User;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.FrontUserService;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.SecurityStandard.UserTypes;
import com.maicard.wpt.domain.WeixinMsg;
import com.maicard.wpt.domain.WeixinPlatformInfo;
import com.maicard.wpt.service.ScanCodeService;
import com.maicard.wpt.service.WeixinMsgService;
import com.maicard.wpt.service.WeixinPlatformService;
import com.maicard.wpt.utils.weixin.StringFormat;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;


/**
 * 微信公众号系统，接收微信公众号的各种请求，不是第三方运营平台
 *
 *
 * @author GHOST
 * @date 2018-01-24
 */
@Controller
@RequestMapping(value = "/weixin")
public class WeixinApiController extends BaseController{

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
	private WeixinPlatformService weixinPlatformService;

	@Resource
	private ScanCodeService scanCodeService;

	@Resource WeixinMsgService weixinMsgService;
	@Resource HttpsService httpservice;
	@Resource ApplicationContextService applicationContextService;
	private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5','6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };


	final boolean ENCRYPT_MSG = true;


	/*	private final String welcomeTitle = "恭喜您成为元惠通会员";
	private final String welcomeContent = "元惠通—为您提供最新最全的商超优惠信息，各种好礼换不停！通过购物扫码、签到和游戏多种方式获得U宝和U币，在商城可以兑换您心仪的商品， 更多精彩内容，敬请期待！";
	private final String welcomePicUrl = "http://yht.yuanhuitongweb.com/style/mobile/images/welcome.jpg";
	private final String welcomeUrl = "http://yht.yuanhuitongweb.com/content/user/pcenter" + CommonStandard.DEFAULT_PAGE_SUFFIX;
	 */
	/*private final String welcomeBaojieTitle = "贺物美21周年司庆";
	private final String welcomeBaojieContent = "点击参与宝洁乐享惠不停，抽奖赢取宝洁欢乐家庭电影套票！";
	private final String welcomeBaojiePicUrl = "http://yht.yuanhuitongweb.com/style/mobile/images/welcome_baojie.jpg";
	private final String welcomeBaojieUrl = "http://yht.yuanhuitongweb.com/content/youxihuode/baojie.shtml";


	 */
	private static String getFormattedText(byte[] bytes) {  
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
	}
	public WeixinMsg getMsgEntity(String strXml)//解析xml
	{  
		WeixinMsg msg = new WeixinMsg();  
		try {  
			if (strXml.length() <= 0 || strXml == null)  
				return null;  
			strXml = strXml.replaceAll("<\\!\\[CDATA\\[", "").replaceAll("\\]\\]>", "");
			XStream xstream = new XStream(new DomDriver());
			xstream.ignoreUnknownElements();
			xstream.alias("xml", WeixinMsg.class);
			xstream.fromXML(strXml, msg);
			return msg;
		} catch (Exception e) {  
			logger.error("xml 格式异常: "+ strXml);  
			e.printStackTrace();  
		}  
		return null;  
	}  
	@ResponseBody 
	@RequestMapping(method = RequestMethod.GET)//激活微信服务器
	public String checkSignature(HttpServletRequest request,HttpServletResponse response, ModelMap map,
			@RequestParam(value="signature",required=false) String signature,
			@RequestParam(value="timestamp",required=false) String timestamp,
			@RequestParam(value="nonce",required=false) String nonce,
			@RequestParam(value="echostr",required=false)String echostr){

		long ownerId = NumericUtils.parseLong(map.get("ownerId"));
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return CommonStandard.frontMessageView;		
		}

		String sourceString="";
		String sha1Str="";
		String result="error";
		WeixinPlatformInfo wxInfo = weixinPlatformService.getSingleWeixinPlatformInfo(ownerId);
		if(wxInfo == null){
			logger.error("系统未配置微信公众号参数");
			return null;
		}
		try{
			String[] s=new String[]{wxInfo.appToken,timestamp,nonce};	
			s=stringSort(s);
			for (int i=0;i<s.length;i++)
			{
				sourceString=sourceString+s[i];
			}
			MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
			messageDigest.update(sourceString.getBytes());
			sha1Str=getFormattedText(messageDigest.digest());
		}
		catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		logger.debug("我方根据[appToken=" + wxInfo.appToken + ",timestamp=" + timestamp + ",nonce=" + nonce + "]生成的SHA1验证是:"+sha1Str+",微信提交的SHA1是"+signature);

		if (signature.equals(sha1Str)){
			result=echostr;
		}
		return result;  
	}




	
	/**
	 * 用户消息处理
	 */
	@ResponseBody
	@RequestMapping(method=RequestMethod.POST)//收微信服务端发来的信息（包括用户普通消息/事件等）
	public String  receiveMessage(HttpServletRequest request,HttpServletResponse response, ModelMap map)throws Exception{
		long beginTs = new Date().getTime();
		long ownerId = 0;
		try{
			ownerId = (long) map.get("ownerId");
		}catch(Exception e){

		}
		if(ownerId < 1){
			logger.error("无法获取ownerId");
		}
		final boolean weixinNotAutoRegister = configService.getBooleanValue(DataName.weixinNotAutoRegister.toString(), ownerId);

		/**
		 * 是否保存收到的消息
		 */
		boolean saveMsg = true;
		/**
		 * 向微信服务器响应客户消息的返回数据
		 */
		String textResponse = null;
		String message = HttpUtils.readPostData(request);
		String text = null;
		logger.debug("接收到消息:" + message);
		try {
			text = StringFormat.stripCdata(weixinPlatformService.decrypt(message, ownerId));
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("公众号接收到的消息解密清理后:" + text);
		WeixinMsg weixinMsg= StringFormat.formatXml2WeixinMsg(text);
		if(weixinMsg == null){
			logger.warn("获取到的消息为空或无法转换");
			return null;
		}
		logger.debug("获取到消息:" + JSON.toJSONString(weixinMsg));
		


		//User frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
		//if(frontUser == null){
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
					/*if(StringUtils.isNotBlank(weixinMsg.getEventKey())){
						identify = weixinMsg.getEventKey().replaceAll("qrscene_","");
					}*/

					frontUser = weixinPlatformService.createUser(openId, identify, 0, ownerId);

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
				weixinPlatformService.updateUserInfo(frontUser, openId, 0);
				frontUserService.update(frontUser);
			}
		}
		//}
		switch (weixinMsg.getMsgType()){

		case "text":{
			try {
				textResponse = weixinPlatformService.processTextMessage(frontUser,request, weixinMsg, 0, ownerId);
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
					Date date=new Date(weixinMsg.getCreateTime()*1000);
					SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String str=format.format(date);
					logger.info("微信用户/"+weixinMsg.getFromUserName() + "/" + weixinMsg.getEventKey() + "关注时间:" + str);
					textResponse = processSubscribeActivity(weixinMsg);
					if(textResponse == null){
						textResponse = weixinPlatformService.processSubscribe(frontUser, request, weixinMsg, 0, ownerId);//generateDefaultSubscribeMessage(request, weixinMsg, ownerId);
					}
					if(textResponse != null){
						textResponse = StringFormat.formatText2Xml(weixinMsg.getFromUserName(), weixinMsg.getToUserName(), textResponse);   
					}

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
					textResponse = weixinPlatformService.processTextMessage(frontUser,request, weixinMsg, 0, ownerId);
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
			
			logger.info("对消息[" + weixinMsg.getContent() + "，类型:" + weixinMsg.getMsgType() + ",key=" + weixinMsg.getEventKey() + "]返回内容:" + textResponse + ",加密前明文:" + clearText + ",耗时:" + (new Date().getTime() - beginTs));
		} else {
			logger.info("对消息[" + weixinMsg.getContent() + "，类型:" + weixinMsg.getMsgType() + ",key=" + weixinMsg.getEventKey() + "]返回内容:" + textResponse + ",耗时:" + (new Date().getTime() - beginTs));

		}

		return textResponse;	
		}





	

	@ResponseBody
	@RequestMapping(value="/event" ,method=RequestMethod.GET)//用户分享后回调的动作
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
	@RequestMapping (value="/inviteImage",method=RequestMethod.GET)
	public String makeImage(HttpServletRequest request,HttpServletResponse response, ModelMap map,
			@RequestParam ("key")String key,
			@RequestParam (value="type",required=false)String type){
		
		final String view = "message/weixinQrCode";
		
		long expireTime = 2592000;
		
		long ownerId = (long)map.get("ownerId");
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return CommonStandard.frontMessageView;		
		}
		
		
		
		long sitePartnerId = NumericUtils.parseLong(map.get(DataName.sitePartnerId.toString()));
		String accessToken= null;
		if(sitePartnerId > 0){
			accessToken = weixinPlatformService.getClientAccessToken(sitePartnerId);
		} else {
			accessToken = weixinPlatformService.getSingleAccessToken(ownerId);
		}
		String url="https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="+accessToken;
		String memo = null;

		if(type != null && type.toLowerCase().equals("limit")){
			if(StringUtils.isNumeric(key)){
				type = "QR_LIMIT_SCENE";
			} else {
				type = "QR_LIMIT_STR_SCENE";

			}
		} else {
			type = "QR_SCENE";
		}
		if(StringUtils.isNumeric(key)){
			memo="{\"expire_seconds\":" + expireTime + ",\"action_name\":\"" + type + "\",\"action_info\":{\"scene\":{\"scene_id\":"+key+"}}}";
		} else {
			memo="{\"expire_seconds\":" + expireTime + ",\"action_name\":\"" + type + "\",\"action_info\":{\"scene\":{\"scene_str\":\""+key+"\"}}}";

		}
		String str="";
		ObjectMapper om= new ObjectMapper();
		try{
			logger.info("url:"+url+"|body:"+memo);
			str=httpservice.httpsPost(url, memo);
			logger.info("生成关注二维码的返回是:"+str);
			String ticket=om.readTree(str).get("ticket").asText();
			url="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+ticket;
		}
		catch(Exception e)
		{}
		map.put("inviteImageUrl", url);
		return view;
		//"<img src=\""+url+"\" alt='关注'/>";

	}

	//检查是否是活动带来的关注
	private String processSubscribeActivity( WeixinMsg message) {
		String eventKey = message.getEventKey();
		if(StringUtils.isBlank(eventKey)){
			logger.warn("事件码为空");
			return null;
		}
		//eventKey = eventKey.replaceFirst("^qrscene_", "");
		String[] data = eventKey.split("_");
		if(data == null || data.length < 5){
			logger.warn("不规范的事件码:" + eventKey);
			return null;
		}
		if(!StringUtils.isNumeric(data[2])){
			logger.warn("不规范的事件码:" + eventKey + ",第二部分不是数字:" + data[2]);
			return null;
		}
		int activityId = Integer.parseInt(data[2]);
		String returnMsg = activityService.createActivityBeginUrl(activityId, data[4]);
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
		//String couponToken = weixinService.getCouponToken(ownerId);

		return null;
	}

}

