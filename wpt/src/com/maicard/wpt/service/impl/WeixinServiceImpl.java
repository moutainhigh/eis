package com.maicard.wpt.service.impl;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.springframework.http.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.scheduling.annotation.Async;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.domain.SiteDomainRelation;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.CacheService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.HttpsService;
import com.maicard.common.service.SiteDomainRelationService;
import com.maicard.common.util.AgentUtils;
import com.maicard.common.util.HttpUtils;
import com.maicard.common.util.JsonUtils;
import com.maicard.common.util.NumericUtils;
import com.maicard.http.HttpClientPoolV3;
import com.maicard.money.criteria.CouponModelCriteria;
import com.maicard.money.domain.Coupon;
import com.maicard.money.domain.CouponModel;
import com.maicard.money.service.CouponModelService;
import com.maicard.money.service.CouponService;
import com.maicard.product.criteria.ActivityCriteria;
import com.maicard.product.domain.Activity;
import com.maicard.product.service.ActivityLogService;
import com.maicard.product.service.ActivityProcessor;
import com.maicard.product.service.ActivityService;
import com.maicard.security.criteria.UserCriteria;
import com.maicard.security.domain.User;
import com.maicard.security.domain.UserData;
import com.maicard.security.service.FrontUserService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.ObjectType;
import com.maicard.standard.OperateResult;
import com.maicard.standard.SecurityStandard.UserExtraType;
import com.maicard.standard.SecurityStandard.UserStatus;
import com.maicard.standard.SecurityStandard.UserTypes;
import com.maicard.standard.TransactionStandard.TransactionStatus;
import com.maicard.wpt.constants.WeixinConfig;
import com.maicard.wpt.criteria.AutoResponseModelCriteria;
import com.maicard.wpt.criteria.WeixinRichTextMessageCriteria;
import com.maicard.wpt.domain.AutoResponseModel;
import com.maicard.wpt.domain.CouponModelBaseInfo;
import com.maicard.wpt.domain.WeixinAsyncMessage;
import com.maicard.wpt.domain.WeixinCoupon;
import com.maicard.wpt.domain.WeixinCouponModel;
import com.maicard.wpt.domain.WeixinGroup;
import com.maicard.wpt.domain.WeixinMenu;
import com.maicard.wpt.domain.WeixinMsg;
import com.maicard.wpt.domain.WeixinRichTextMessage;
import com.maicard.wpt.domain.WeixinToken;
import com.maicard.wpt.misc.weixin.Constants;
import com.maicard.wpt.misc.weixin.MD5;
import com.maicard.wpt.service.AutoResponseModelService;
import com.maicard.wpt.service.TulingRobotService;
import com.maicard.wpt.service.WeixinGroupService;
import com.maicard.wpt.service.WeixinPlatformService;
import com.maicard.wpt.service.WeixinRichTextMessageService;
import com.maicard.wpt.service.WeixinService;
import com.maicard.wpt.utils.weixin.StringFormat;

public class WeixinServiceImpl extends BaseService implements WeixinService{


	@Resource
	private AutoResponseModelService autoResponseModelService;

	@Resource
	private CacheService cacheService;
	@Resource 
	private ConfigService configService;
	@Resource 
	private ActivityService activityService;
	@Resource
	private CouponModelService couponModelService;
	@Resource
	private CouponService couponService;
	@Resource
	private FrontUserService frontUserService;
	@Resource
	private HttpsService httpservice;         
	@Resource
	private HttpsService httpsService;
	@Resource
	private SiteDomainRelationService siteDomainRelationService;

	@Resource
	private TulingRobotService tulingRobotService;

	@Resource
	private WeixinGroupService weixinGroupService;

	@Resource
	private WeixinRichTextMessageService weixinRichTextMessageService;
	@Resource
	private ActivityLogService activityLogService;

	@Resource
	private WeixinPlatformService weixinPlatformService;



	private final String accessTokenName = "accessToken";
	private final String couponTokenName = "couponToken";
	//批量发送消息时的延迟等待时间
	private final int batchSendDealySec = 3;

	private static final int NETWORK_RETRY = 3;

	ObjectMapper om = JsonUtils.getNoDefaultValueInstance();






	@Resource 
	private ApplicationContextService applicationContextService;


	private final String defaultNullReplyMessage = "对不起，我不知道您说的什么";

	//	private static String key = "AU9yoKqB8JFPjqUv871u1Mw0RDBhToMT";
	private static String key = "X5ReecXeighRetMerfes8dokUvyewv7Y";

	


	//根据hostname判断是哪个APPID
	@Override
	public String getAppId(long ownerId){
		return configService.getValue(DataName.weixinAppId.toString(),ownerId);
	}


	@Override
	public String getAppSecret(long ownerId){
		return configService.getValue(DataName.weixinAppSecret.toString(),ownerId);
	}

	@Override
	public String getAppToken(long ownerId){
		return configService.getValue(DataName.weixinAppToken.toString(),ownerId);
	}


	@Override
	public String getWeixinPayMechId(long ownerId){
		return configService.getValue(DataName.weixinPayMechId.toString(), ownerId);
	}
	/**
	 * 生成JS页面使用的签名
	 */
	//@Override
	public String makeJsSignature(Long timeStamp,String currentUrl, String nonceStr) {
		long t1 = new Date().getTime();
		String hostName = HttpUtils.parseHostAndPort(currentUrl)[0];
		SiteDomainRelation siteDomainRelation = siteDomainRelationService.getByHostName(hostName);
		if(siteDomainRelation == null){
			logger.error("根据主机名[" + hostName + "]找不到对应的站点对应数据");
			return null;
		}
		String accessToken = weixinPlatformService.getClientAccessTokenByHost(hostName, siteDomainRelation.getOwnerId());
		if(accessToken == null){
			logger.debug("根据主机名[" + hostName + "]找不到合作方accessToken,尝试获取单平台accessToken");
			accessToken=this.getAccessToken(siteDomainRelation.getOwnerId());
		}
		logger.info("token是"+accessToken);
		String url=WeixinConfig.API_PREFIX + "/cgi-bin/ticket/getticket?access_token="+accessToken+"&type=jsapi";
		String result="";
		String jsapi_ticket="";
		String sha1Str="";
		try{
			result=httpservice.httpsGet(url);
		}	catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(result == null){
			logger.error("无法连接到微信服务器");
		}  
		logger.debug("从微信请求生成JS签名ticket的结果:" + result + ",耗时:" + (new Date().getTime() - t1) + "毫秒");
		try{
			jsapi_ticket=om.readTree(result).get("ticket").asText();
		}	catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(StringUtils.isBlank(jsapi_ticket)){
			logger.error("无法从微信获取JS签名");
			return null;
		}
		//logger.info("jsapi_ticket是"+jsapi_ticket);
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("SHA1");
		} catch (NoSuchAlgorithmException e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		sha1Str="jsapi_ticket="+jsapi_ticket+"&noncestr="+nonceStr+"&timestamp="+String.valueOf(timeStamp/ 1000)+"&url="+currentUrl;
		logger.info("sourceString是"+sha1Str);
		messageDigest.update(sha1Str.getBytes());
		sha1Str= StringFormat.getFormattedText(messageDigest.digest());
		logger.info("使用数据[ticket=" + jsapi_ticket + ",nonceStr=" + nonceStr + ",timestamp=" + timeStamp/ 1000 + ",currentUrl=" + currentUrl + "]生成签名:" +sha1Str);


		return 	sha1Str;
	}

	/**
	 * 签名算法
	 *
	 * @param o 要参与签名的数据对象
	 * @return 签名
	 */
	@Override
	public String getSign(Map<String, Object> map) {
		ArrayList<String> list = new ArrayList<String>();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			if (entry.getValue() != "") {
				list.add(entry.getKey() + "=" + entry.getValue() + "&");
			}
		}
		int size = list.size();
		String[] arrayToSort = list.toArray(new String[size]);
		Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size; i++) {
			sb.append(arrayToSort[i]);
		}
		String result = sb.toString();
		result += "key=" + key;
		System.out.println("加密前的字符串[" + result + "]");
		result = MD5.MD5Encode(result).toUpperCase();
		System.out.println("签名[" + result + "]");

		return result;
	}

	@Override
	public String cardSign(long timeStamp, String currentUrl, String nonceStr, String cardId, long ownerId){
		long t1 = new Date().getTime();
		String hostName = HttpUtils.parseHostAndPort(currentUrl)[0];
		SiteDomainRelation siteDomainRelation = siteDomainRelationService.getByHostName(hostName);
		if(siteDomainRelation == null){
			logger.error("根据主机名[" + hostName + "]找不到对应的站点对应数据");
			return null;
		}
		String accessToken=getAccessToken(siteDomainRelation.getOwnerId());
		logger.info("token是"+accessToken);
		String url = WeixinConfig.API_PREFIX + "/cgi-bin/ticket/getticket?access_token=" + accessToken + "&type=wx_card";
		String result="";
		String api_ticket="";
		String sha1Str="";
		try{
			result=httpservice.httpsGet(url);
		}	catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(result == null){
			logger.error("无法连接到微信服务器");
		}  
		logger.debug("从微信请求生成card签名ticket的结果:" + result + ",耗时:" + (new Date().getTime() - t1) + "毫秒");
		try{
			api_ticket=om.readTree(result).get("ticket").asText();
		}	catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(StringUtils.isBlank(api_ticket)){
			logger.error("无法从微信获取JS签名");
			return null;
		}
		//logger.info("jsapi_ticket是"+jsapi_ticket);
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("SHA1");
		} catch (NoSuchAlgorithmException e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		String appId = getAppId(ownerId);
		sha1Str="api_ticket="+api_ticket+"&app_id=" + appId + "&timestamp="+String.valueOf(timeStamp/ 1000)+ "&noncestr="+nonceStr+"&card_id=" + cardId;
		logger.info("sourceString是"+sha1Str);
		messageDigest.update(sha1Str.getBytes());
		sha1Str= StringFormat.getFormattedText(messageDigest.digest());
		logger.info("使用数据[ticket=" + api_ticket + ",nonceStr=" + nonceStr + ",timestamp=" + timeStamp/ 1000 + ",currentUrl=" + currentUrl + "]生成签名:" +sha1Str);


		return 	sha1Str;
	}

	@Override
	public String getAccessToken(long ownerId){
		/*if(this.accessToken != null){
			logger.info("返回被设置的accessToken:" + this.accessToken);
			return this.accessToken;
		}*/

		final String cacheKeyName = accessTokenName + "#" + ownerId;
		boolean forceClean = false;
		final String checkIpListUrl = WeixinConfig.API_PREFIX + "/cgi-bin/getcallbackip?access_token=";
		final String getAccessTokenUrl=WeixinConfig.API_PREFIX + "/cgi-bin/token?grant_type=client_credential&appid="+ getAppId(ownerId)+"&"+"secret="+getAppSecret(ownerId);
		String access_token="";
		String expires_in="";
		WeixinToken tK= cacheService.get(CommonStandard.cacheNameValidate, cacheKeyName);
		if (tK==null || tK.getExpires_time()<System.currentTimeMillis()){	
			forceClean = true;
		}
		//XXX
		//以下代码虽然可以保证token有效性，但是会延长访问时间几秒钟，因此未启用
		if(!forceClean){
			//检查该令牌是否有效
			String url = checkIpListUrl + tK.getToken();
			try	{
				String result=httpservice.httpsGet(url);
				if(!JsonUtils.getInstance().readTree(result.trim()).path("ip_list").isArray()){
					logger.info("检查微信回调IP[" + url + "]错误,返回:" + result + "，重新刷新微信令牌");
					forceClean = true;
				}
			}
			catch(Exception e){
				logger.info("检查微信回调IP[" + url + "]出错，重新刷新:" + e.getMessage());
				forceClean = true;
			}

		}
		if(forceClean){
			try	{
				String result=httpservice.httpsGet(getAccessTokenUrl);
				access_token=om.readTree(result).get("access_token").asText();
				expires_in=om.readTree(result).get("expires_in").asText();
			}
			catch(Exception e){
				logger.info("从微信服务器取token出错");
				return null;
			}
			logger.debug("更换云ID=" + ownerId + "的access token为"+access_token);
			tK=new WeixinToken();
			tK.setToken(access_token);
			//logger.info("超时时间是"+Long.valueOf(expires_in));
			tK.setExpires_time(new Date().getTime()+Long.valueOf(expires_in)*1000 - 10 * 1000);
			cacheService.put(CommonStandard.cacheNameValidate, cacheKeyName, tK);

		}
		logger.debug("微信AppId ： "+ getAppId(ownerId) + "  getAppSecret(ownerId): " + getAppSecret(ownerId));
		logger.debug("云平台ID=" + ownerId + "的当前access token是:" + tK.getToken());
		return tK.getToken();
	}		



	

	@Override
	public User getUserInfo(String openId, long ownerId){
		String accessToken = getAccessToken(ownerId);
		String infoUrl = WeixinConfig.API_PREFIX + "/cgi-bin/user/info?access_token=" + accessToken + "&openid=" + openId + "&lang=zh_CN";
		logger.info("访问地址[" + infoUrl + "]来获取用户信息" );
		String result = null;
		try {
			result = httpsService.httpsGet(infoUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info(openId + "的用户信息:" + result);
		if(StringUtils.isBlank(result)){
			logger.error("无法获取用户" + openId + "的信息，返回为空");
			return null;
		}
		User user = new User();
		JsonNode jsonNode = null;
		try{
			jsonNode = JsonUtils.getInstance().readTree(result);
		}catch(Exception e){
			e.printStackTrace();
		}
		if(jsonNode == null){
			logger.error("无法解析微信用户信息返回:" + result);
			return null;
		}
		int errCode = jsonNode.path("errcode").asInt();
		if(errCode == 40001){
			logger.error("无法获取用户信息因为access token错误或过期");
			return null;
		}
		String nickName = jsonNode.path("nickname").textValue();
		if(StringUtils.isNotBlank(nickName)){
			Pattern emoji = Pattern.compile ("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]" ,Pattern . UNICODE_CASE | Pattern . CASE_INSENSITIVE ) ;
			Matcher matcher = emoji.matcher(jsonNode.path("nickname").textValue());  

			user.setNickName(matcher.replaceAll(""));
			logger.error("设置用户" + openId + "的昵称为:" + user.getNickName());
		}
		if(jsonNode.path("sex").intValue() == 2){
			user.setGender(UserCriteria.GENDER_GIRL);
		} else {
			user.setGender(UserCriteria.GENDER_BOY);
		}
		String headImageUrl = jsonNode.path("headimgurl").textValue();
		if(StringUtils.isNotBlank(headImageUrl)){
			logger.info("为用户[" + user.getNickName()  + "]设置头像:" + headImageUrl );
			user.setExtraValue(DataName.userHeadPic.toString(), headImageUrl);
		}
		String country = jsonNode.path("country").asText();
		if(StringUtils.isNotBlank(country)){
			logger.debug("为用户[" + openId + "]设置国家:" + country);
			user.setExtraValue(DataName.country.toString(), country);
		}
		String province = jsonNode.path("province").asText();
		if(StringUtils.isNotBlank(province)){
			logger.debug("为用户[" + openId + "]设置省份:" + province);
			user.setExtraValue(DataName.province.toString(), province);
		}			
		String city = jsonNode.path("city").asText();
		if(StringUtils.isNotBlank(city)){
			logger.debug("为用户[" + openId + "]设置城市:" + city);
			user.setExtraValue( DataName.city.toString(), city);
		}
		String language = jsonNode.path("language").asText();
		if(StringUtils.isNotBlank(language)){
			logger.debug("为用户[" + openId + "]设置语言:" + language);
			user.setExtraValue(DataName.language.toString(), language);
		}
		return user;
	}


	@Override
	public void updateUserInfo(User frontUser, String openId) {

		if(frontUser == null){
			logger.error("尝试更新的微信用户是空");
			return;
		}
		if(frontUser.getOwnerId() < 1){
			logger.error("尝试更新的微信用户ownerId为空");
			return;
		}


		if(frontUser.getUserConfigMap() == null){
			frontUser.setUserConfigMap(new HashMap<String,UserData>());
		}
		if(frontUser.getExtraStatus() == 0){

			User weixinUser = getUserInfo(frontUser.getAuthKey(), frontUser.getOwnerId());

			if(weixinUser != null && weixinUser.getUserConfigMap() != null){

				for(UserData ud : weixinUser.getUserConfigMap().values()){
					frontUser.setExtraValue(ud.getDataCode(), ud.getDataValue());
				}
			}



			/*logger.info("为用户[" + frontUser.getNickName() + "/" + frontUser.getUuid() + "]设置头像:" +node.path("headimgurl").textValue() );
			boolean needUpdateHeadPic = true;
			UserData headerData = new UserData();
			headerData.setDataCode(DataName.userHeadPic.toString());
			headerData.setDataValue(node.path("headimgurl").textValue());
			headerData.setCurrentStatus(BasicStatus.normal.getId());
			if(frontUser.getUserConfigMap().get(DataName.userHeadPic.toString()) != null){
				UserData _oldHeaderData = frontUser.getUserConfigMap().get(DataName.userHeadPic.toString());
				if(_oldHeaderData != null){
					if(	_oldHeaderData.getDataValue().equals(headerData.getDataValue())){
						logger.debug("微信用户头像数据未变化，不更新");
						needUpdateHeadPic = false;
					}
					headerData.setDataDefineId(_oldHeaderData.getDataDefineId());
					headerData.setUserDataId(_oldHeaderData.getUserDataId());
				}
				//frontUserService.updateUserHeadPic(frontUser, node.path("headimgurl").textValue());
			}*/
		}

	}


	@Override
	public User createUser(String openId, String identify, long ownerId) {

		if(StringUtils.isNotBlank(identify) &&  identify.indexOf("qrscene") > -1 ){
			identify = identify.replaceAll("qrscene_","");
		}

		if(ownerId <= 1){
			logger.error("无法创建前端用户，ownerId=0");
		}

		User frontUser = new User(ownerId);
		frontUser.setUserConfigMap(new HashMap<String,UserData>());
		//获取用户的更多信息
		updateUserInfo(frontUser, openId);
		frontUser.setAuthKey(openId);
		frontUser.setUserTypeId(UserTypes.frontUser.getId());
		frontUser.setUserExtraTypeId(UserExtraType.tencentUser.getId());
		frontUser.setUsername(openId);
		frontUser.setUserPassword(openId);
		frontUser.setCurrentStatus(UserStatus.normal.getId());
		frontUser.setCreateTime(new Date());
		if(StringUtils.isNotBlank(identify)){
			UserData userData = new UserData();
			userData.setDataCode(DataName.userInviteByCode.toString());
			userData.setDataValue(identify);
			if(frontUser.getUserConfigMap() == null){
				frontUser.setUserConfigMap(new HashMap<String,UserData>());
			}
			frontUser.getUserConfigMap().put(DataName.userInviteByCode.toString(),userData);
			//把邀请码同时也设置为最后一次eventKey
			frontUser.getUserConfigMap().put(DataName.lastEventKey.toString(), userData);
			//根据邀请码查找用户是否属于某个分组
			WeixinGroup weixinGroup = weixinGroupService.findGroupByIdentify(identify);
			if(weixinGroup == null){
				logger.warn("根据识别码[" + identify + "]找不到任何微信分组");
			} else {
				logger.warn("根据识别码[" + identify + "]找到了微信分组:" + weixinGroup);
				frontUser.getUserConfigMap().put(DataName.weixinGroupid.toString(), new UserData(0,DataName.weixinGroupid.toString(), String.valueOf(weixinGroup.getGroupId())));
				frontUser.getUserConfigMap().put(DataName.weixinMenuId.toString(), new UserData(0,DataName.weixinMenuId.toString(), String.valueOf(weixinGroup.getMenuId())));
				if(StringUtils.isNotBlank(weixinGroup.getPageVersion())){
					frontUser.getUserConfigMap().put(DataName.weixinPageVersion.toString(), new UserData(0,DataName.weixinPageVersion.toString(), weixinGroup.getPageVersion()));
				}
				//设置微信分组
				if(weixinGroup.getOutGroupId() < 1){
					createGroup(weixinGroup);
				}
				setUserGroup(frontUser, weixinGroup);
				setTag(frontUser, weixinGroup.getOutGroupId());
			}






		}
		int rs = 0;
		try {
			rs = frontUserService.insert(frontUser);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("使用openId[" + openId + "]注册新用户:" + rs + "/" + frontUser.getUuid());
		if(rs == 1){

			logger.debug("注册新用户成功[" + frontUser.getUuid() + ",检查是否有注册活动");
			ActivityCriteria activityCriteria = new ActivityCriteria(ownerId);
			activityCriteria.setActivityType(ActivityCriteria.ACTIVITY_TYPE_SUBSCRIBE);
			activityCriteria.setCurrentStatus(BasicStatus.normal.getId());
			List<Activity> activityList = activityService.list(activityCriteria);
			if(activityList == null || activityList.size() < 1){
				logger.debug("当前没有定义关注活动");
			} else {
				logger.debug("当前共有" + activityList.size() + "个关注活动，准备执行");
				for(Activity activity : activityList){
					if(activity.getProcessor() == null){
						logger.warn("关注活动[" + activity.getActivityId() + "/" + activity.getActivityCode() + "]对应的处理器是空");
						continue;
					}
					Object object = applicationContextService.getBean(activity.getProcessor());
					if(object == null || !(object instanceof ActivityProcessor)){
						logger.warn("关注活动[" + activity.getActivityId() + "/" + activity.getActivityCode() + "]对应的处理器[" + activity.getProcessor() + "]找不到或者不是ActivityProcessor类型");
						continue;
					}
					ActivityProcessor p = (ActivityProcessor)object;
					p.execute("subscribe", activity, frontUser, null);
				}
			}
		}
		return frontUser;
	}




	@Override
	public void setTag(User frontUser, long tagId) {
		String url = "https://api.weixin.qq.com/cgi-bin/tags/members/batchtagging?access_token=" + this.getAccessToken(frontUser.getOwnerId());;
		HttpClient httpClient = HttpClientPoolV3.getHttpClient(WeixinConfig.API_HOST, WeixinConfig.API_PORT);
		String result = null;
		String data = null;

		ObjectNode objectNode = om.createObjectNode();
		ArrayNode an = objectNode.putArray("openid_list");
		an.add(frontUser.getUsername());
		objectNode.put("tagid", tagId);
		try {
			data = om.writeValueAsString(objectNode);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		System.out.println(	data );
		int rs = 0;
		try{
			PostMethod pm = new PostMethod(url);
			pm.setRequestEntity(new StringRequestEntity(data,"","UTF-8"));
			rs = httpClient.executeMethod(pm);

			result = pm.getResponseBodyAsString();
			//	result = HttpUtilsV3.p(httpClient, url, null);
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("设置用户[" + frontUser.getUuid() + "/" + frontUser.getUsername() + "]到标签:" + tagId + ",返回 Code:" + rs + ", 返回页面:" + result);
	}		



	/**
	 * 发送图文消息到指定用户，异步模式
	 */
	@Override
	@Async
	public void sendMessage(WeixinRichTextMessage weixinRichTextMessage) {




	}

	/**
	 * 发送消息，更新微信的卡券状态，如核销某个卡券
	 * @param coupon
	 */
	@Override
	public void sendCouponNotifyMessage(Coupon coupon){

	}
	/**
	 * 处理文本消息
	 */
	@Override
	public String processTextMessage(User frontUser, HttpServletRequest request, WeixinMsg message, long ownerId) {
		String question = null;
		logger.debug("发送消息 ：" + message.getEventKey() + "     " + message.getContent());
		if(StringUtils.isBlank(message.getContent())){
			question = message.getEventKey();
		} else {
			question = message.getContent();
		}
		if(StringUtils.isBlank(question)){
			return defaultNullReplyMessage;
		}
		//		question = question.trim();
		String result = null;
		AutoResponseModelCriteria autoResponseModelCriteria = new AutoResponseModelCriteria(ownerId);
		autoResponseModelCriteria.setQuestion(question);
		int autoResponseCount = autoResponseModelService.count(autoResponseModelCriteria);
		logger.debug("根据询问内容[" + question + "]得到自动回复模型是" + autoResponseCount + ",ownerId=" + autoResponseModelCriteria.getOwnerId() );
		if(autoResponseCount > 0){
			List<AutoResponseModel> autoResponseModelList = autoResponseModelService.list(autoResponseModelCriteria);
			if(autoResponseModelList == null || autoResponseModelList.size() < 1){
			} else {
				List<WeixinRichTextMessage> weixinRichTextMessageList = new ArrayList<WeixinRichTextMessage>();
				for(AutoResponseModel autoResponseModel : autoResponseModelList){
					/**
					 * @author Pengzhenggang
					 * @data 2016/3/28
					 */
					if (autoResponseModel.getResponseType().equals(ObjectType.activity.name()) && autoResponseModel.getResponseId() > 0) {
						WeixinRichTextMessage weixinRichTextMessage = weixinRichTextMessageService.select(autoResponseModel.getResponseId());
						if (autoResponseModel.getCurrentStatus() == BasicStatus.normal.getId()) {
							Activity activity = activityService.select(autoResponseModel.getResponseId());
							logger.debug("活动处理器 ： " + activity.getProcessor());
							ActivityProcessor activityProcessor = null;
							try {
								activityProcessor = (ActivityProcessor) applicationContextService.getBean(activity.getProcessor());
							} catch (Exception e) {
								if (activityProcessor == null) {
									logger.error("找不到活动[" + activity.getActivityId() + "]指定的处理器[" + activity.getProcessor());
									//									return new EisMessage(EisError.activityClosed.getId(), "活动尚未开放或已结束");
									logger.error("活动可能被关闭了，不能参与");
								}
							}
							logger.debug("request :");
							EisMessage execute = activityProcessor.execute(null, activity, frontUser, request);
							int operateCode = execute.getOperateCode().intValue();
							logger.debug("活动 信息 ：" + execute.getMessage() + "、 成功 ：" + operateCode);
							if (operateCode == OperateResult.success.getId()) {
								weixinRichTextMessage.setContent(execute.getMessage());
								weixinRichTextMessageList.add(weixinRichTextMessage);
								logger.debug("URL ：" + request.getRequestURL().toString());
							} else {
								return execute.getMessage();
							}

						} else {
							logger.debug("活动可能被关闭了，不能参与");
						}
					}

					if(autoResponseModel.getResponseType().equals(ObjectType.richTextMessage.name()) && autoResponseModel.getResponseId() > 0){
						WeixinRichTextMessage weixinRichTextMessage = weixinRichTextMessageService.select(autoResponseModel.getResponseId());
						if(weixinRichTextMessage == null){
							logger.error("找不到针对消息[" + message.getContent() + "]指定的图文回复:" + autoResponseModel.getResponseId());
						} else {
							weixinRichTextMessageList.add(weixinRichTextMessage);
						}	
						//FIXME 暂时未支持其他发送
					}

					if(autoResponseModel.getResponseType().equals("text")){
						//纯文本响应
						if(autoResponseModel.getResponse() != null){
							if (autoResponseModel.getResponse().toString().contains("${uuid}")) {
								//生成匹配模式的正则表达式 
								String patternString = "\\$\\{(uuid)\\}"; 
								Pattern pattern = Pattern.compile(patternString); 
								Matcher matcher = pattern.matcher(autoResponseModel.getResponse().toString());
								//对于特殊含义字符"\","$"，使用Matcher.quoteReplacement消除特殊意义 
								matcher.reset(); 
								return matcher.replaceAll(Matcher.quoteReplacement(String.valueOf(frontUser.getUuid())));
							}
							return autoResponseModel.getResponse().toString();
						}
					}
				}
				String to = message.getFromUserName();
				String from = message.getToUserName();
				String urlPrefix = HttpUtils.generateUrlPrefix(request);
				WeixinRichTextMessage directResponseMessage = weixinRichTextMessageList.get(0);
				directResponseMessage.setTo(to);
				directResponseMessage.setFrom(from);

				if(weixinRichTextMessageList.size() == 1){
					logger.debug("当前只有一个自动应答富文本消息[" + weixinRichTextMessageList.get(0).getWeixinRichTextMessageId() + "],直接返回该消息");
					//					if (directResponseMessage.getUrl().toString().contains("${uuid}")) {
					//						//生成匹配模式的正则表达式 
					//						String patternString = "\\$\\{(uuid)\\}"; 
					//						Pattern pattern = Pattern.compile(patternString); 
					//						Matcher matcher = pattern.matcher(directResponseMessage.getUrl().toString());
					//						//对于特殊含义字符"\","$"，使用Matcher.quoteReplacement消除特殊意义 
					//						matcher.reset(); 
					//						String newUrl = matcher.replaceAll(Matcher.quoteReplacement(String.valueOf(frontUser.getUuid())));
					//						logger.debug("替换上用户信息的链接是：" + newUrl);
					//						directResponseMessage.setUrl(newUrl);
					//					}
					return formatMsgToXml(directResponseMessage,urlPrefix);
				}
				String accessToken = getAccessToken(frontUser.getOwnerId());
				logger.debug("当前定义了" + weixinRichTextMessageList.size() + "个自动应答富文本消息");

				//除了优先级最高的之外，剩下的全部异步发送
				for(int i = 1; i < weixinRichTextMessageList.size(); i++){
					WeixinRichTextMessage weixinRichTextMessage = weixinRichTextMessageList.get(i);
					weixinRichTextMessage.setDelaySec(batchSendDealySec * i);
					weixinRichTextMessage.setTo(to);
					weixinRichTextMessage.setFrom(from);
					new Thread(new WeixinAsyncMessage(weixinRichTextMessage, accessToken, urlPrefix)).start();
				}

				logger.debug("返回第1个自动应答富文本消息[" + weixinRichTextMessageList.get(0).getWeixinRichTextMessageId() + ",其他异步发送");
				return formatMsgToXml(directResponseMessage, urlPrefix);
			}
		}
		/*if(message.getContent().trim().equals("电影票")){
			result =  "<a href=\"" +  HttpUtils.generateUrlPrefix(request) + "/content/user/addressAdd" + CommonStandard.DEFAULT_PAGE_SUFFIX + "?identify=电影票\">请点击此处输入您的收件地址</a>";
			logger.info("用户输入了电影票,返回:" + result);
			return result;
		}*/

		result = tulingRobotService.getAnswer(message.getContent(),frontUser.getOwnerId());  
		return result;
	}

	/**
	 * 处理收到的卡券消息
	 * @param weixinMsg
	 */
	@Override
	public void processCouponMessage(User frontUser, WeixinMsg weixinMsg) {
		if(weixinMsg.getEvent().toLowerCase().equals("shakearoundusershake")){
			//用户开始摇一摇
		}
		if(weixinMsg.getEvent().toLowerCase().equals("user_get_card")){
			//领取卡券，创建对应卡券
			//外部合作商户的卡券产品ID
			String outCouponModelCode = weixinMsg.getCardId();
			CouponModelCriteria couponModelCriteria = new CouponModelCriteria();
			couponModelCriteria.setCouponCode(outCouponModelCode);
			couponModelCriteria.setOwnerId(frontUser.getOwnerId());
			List<CouponModel> couponModelList = couponModelService.list(couponModelCriteria);
			if(couponModelList == null || couponModelList.size() < 1){
				logger.warn("找不到微信端提供的外部卡券产品ID[" + outCouponModelCode + "]所对应的卡券产品");
				return;
			}
			CouponModel couponModel = couponModelList.get(0);
			logger.debug("找到了微信端提供的外部卡券产品ID[" + outCouponModelCode + "]所对应的卡券产品:" + couponModel.getCouponModelId());
			Coupon coupon = new Coupon(couponModel);
			coupon.setUuid(frontUser.getUuid());
			coupon.setCurrentStatus(TransactionStatus.success.getId());
			coupon.setContent(weixinMsg.getUserCardCode());
			logger.debug("通过微信端提供的外部卡券产品ID[" + outCouponModelCode + "]为用户[" + frontUser.getUuid() + "]创建新卡券:" + coupon);
			int rs = couponService.insert(coupon);				
			logger.debug("通过微信端提供的外部卡券产品ID[" + outCouponModelCode + "]为用户[" + frontUser.getUuid() + "]创建新卡券:" + coupon + ",新增结果:" + rs);

		}

	}

	public WeixinCouponModel convertToWeixinCouponModel(CouponModel couponModel){
		WeixinCouponModel weixinModel = new WeixinCouponModel();
		CouponModelBaseInfo baseInfo = new CouponModelBaseInfo();


		//条码
		baseInfo.setCode_type(Constants.CODE_TYPE_BARCODE);
		String brandName = couponModel.getCouponModelDesc();
		if(StringUtils.isBlank(brandName)){
			logger.error("尝试创建的卡券[" + couponModel + "]没有提供说明desc");
			return null;
		}
		if(brandName.length() >= 12){
			brandName = brandName.substring(0,11);
		}

		baseInfo.setBrand_name(brandName);



		String title = couponModel.getCouponModelName();
		if(StringUtils.isBlank(title)){
			logger.error("尝试创建的卡券[" + couponModel + "]没有提供标题名称name");
			return null;
		}
		if(title.length() > 9){
			title = title.substring(0,8);
		}
		baseInfo.setTitle(title);

		String subTitle = couponModel.getCouponModelDesc();
		if(subTitle.length() > 18){
			subTitle = subTitle.substring(0,18);
		}
		baseInfo.setSub_title(subTitle);

		baseInfo.getDynamicDateInfo().setType(Constants.DATE_TYPE_FIX_TIME_RANGE);

		long beginTimeTs = 0;
		if(couponModel.getValidTimeBegin() != null){
			beginTimeTs = couponModel.getValidTimeBegin().getTime() / 1000;
		} else {
			beginTimeTs = new Date().getTime() / 1000 - 1;
		}
		baseInfo.getDynamicDateInfo().setBegin_timestamp(String.valueOf(beginTimeTs));
		long endTimeTs = 0;
		if(couponModel.getValidTimeEnd() == null){
			logger.error("尝试创建的卡券[" + couponModel + "]没有有效期截至时间");
			return null;
		}
		endTimeTs = couponModel.getValidTimeEnd().getTime() / 1000;
		baseInfo.getDynamicDateInfo().setEnd_timestamp(String.valueOf(endTimeTs));

		//将时间信息转换为JSON串并删除之前设置的动态数据
		String dateInfo = null;
		try {
			dateInfo = om.writeValueAsString(baseInfo.getDynamicDateInfo());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		if(dateInfo == null){
			logger.error("无法把日期信息转换为JSON字符串");
			return null;
		}

		baseInfo.setDynamicDateInfo(null);
		baseInfo.setDate_info(dateInfo);

		boolean couponBindToUser = couponModel.getBooleanExtraValue(DataName.couponBindUser.toString());
		boolean couponUseCustomCode = couponModel.getBooleanExtraValue(DataName.couponUseCustomCode.toString());
		if(couponBindToUser){
			baseInfo.setBind_openid(true);
		}
		if(couponUseCustomCode){
			baseInfo.setUse_custom_code(true);
		}



		return weixinModel;
	}
	//http://mmbiz.qpic.cn/mmbiz/5AUSrLn6N5U54Y03x0Wa6eHZRUGsV5CPL1l68nNlzia8Z0dYUlTwjBJsugwYbQU8OFWDuZFFBfnA3a3y6icjMiaSg/0
	/*
	 * 创建卡券
	 */
	@Override
	public int createCouponModel(CouponModel couponModel, String accessToken){
		if(accessToken == null){
			accessToken = getAccessToken(couponModel.getOwnerId());
		}
		String imageUrl = couponModel.getImageUrl();
		if(StringUtils.isBlank(imageUrl)){
			logger.error("尝试创建的卡券[" + couponModel + "]没有图片地址");
			return EisError.REQUIRED_PARAMETER.id;
		}

		String brandName = couponModel.getCouponModelDesc();
		if(StringUtils.isBlank(brandName)){
			logger.error("尝试创建的卡券[" + couponModel + "]没有提供说明desc");
			return EisError.REQUIRED_PARAMETER.id;
		}
		if(brandName.length() >= 12){
			brandName = brandName.substring(0,11);
		}
		String title = couponModel.getCouponModelName();
		if(StringUtils.isBlank(title)){
			logger.error("尝试创建的卡券[" + couponModel + "]没有提供标题名称name");
			return EisError.REQUIRED_PARAMETER.id;
		}
		if(title.length() > 9){
			title = title.substring(0,8);
		}
		String subTitle = couponModel.getCouponModelDesc();
		if(subTitle.length() > 18){
			subTitle = subTitle.substring(0,18);
		}
		long beginTimeTs = 0;
		if(couponModel.getValidTimeBegin() != null){
			beginTimeTs = couponModel.getValidTimeBegin().getTime() / 1000;
		} else {
			beginTimeTs = new Date().getTime() / 1000 - 1;
		}
		long endTimeTs = 0;
		if(couponModel.getValidTimeEnd() == null){
			logger.error("尝试创建的卡券[" + couponModel + "]没有有效期截至时间");
			return EisError.REQUIRED_PARAMETER.id;
		}

		boolean couponBindToUser = false;
		boolean couponUseCustomCode = false;
		if(couponModel.getBooleanExtraValue(DataName.couponBindUser.toString())){
			couponBindToUser = true;
		}
		if(couponModel.getBooleanExtraValue(DataName.couponUseCustomCode.toString())){
			couponUseCustomCode = true;
		}


		//String entityIds = ExtraDataUtils.getStringExtraData(couponModel, DataName.entityShopList.toString());


		logger.debug("尝试创建的卡券[" + couponModel + "]是否绑定到用户:" + couponBindToUser + ",是否使用自定义CODE:" + couponUseCustomCode);
		endTimeTs = couponModel.getValidTimeEnd().getTime() / 1000;
		//2015.12.11 使用bind_openid=true无法正常工作
		StringBuffer sb = new StringBuffer();
		//		sb.append("{\"card\":{\"card_type\":\"CASH\",\"cash\":{\"base_info\":{");
		sb.append("{\"card\":{\"card_type\":\"");
		sb.append(couponModel.getCouponType());
		sb.append("\",\"cash\":{\"base_info\":{");
		/*
		 * 第三方代制需要的子商户ID
		 */
		/*sb.append("\"sub_merchant_info\":{\"merchant_id\":");
		sb.append("410177521");
		sb.append("},");*/

		sb.append("\"logo_url\":\"");	//卡券logo
		sb.append(imageUrl);
		sb.append("\",\"code_type\":\"CODE_TYPE_BARCODE\",");	//卡券Code展示类型
		//是否自定义卡券Code
		if(couponUseCustomCode){
			sb.append("\"use_custom_code\":true, \"get_custom_code_mode\":\"GET_CUSTOM_CODE_MODE_DEPOSIT\",");
		} else {
			sb.append("\"use_custom_code\":false,");
		}		
		/*sb.append("\"location_id_list\":[" + '"');
		if(entityIds != null){
			logger.debug("尝试创建的卡券[" + couponModel + "]需要配置门店信息:" + '"' + entityIds + '"');
			sb.append(entityIds);
		}
		sb.append('"' + "],");*/
		sb.append("\"location_id_list\":[],");

		//leastCost是最少启用金额，单位为分
		String leastCost = null;
		leastCost = couponModel.getExtraValue(DataName.couponLeastCost.toString());

		if(leastCost == null){
			leastCost = "0";
		}

		//reduceCost是减免费用，单位为分
		String reduceCost = null;
		if(couponModel.getGiftMoney() != null && couponModel.getGiftMoney().getChargeMoney() > 0){
			reduceCost = String.valueOf(couponModel.getGiftMoney().getChargeMoney() * 100);
		} else {
			reduceCost = couponModel.getExtraValue(DataName.couponReduceCost.toString());
		}
		/*if(reduceCost == null){
			reduceCost = "5000";
		}*/
		sb.append("\"bind_openid\":");	//是否指定用户领取，填写true或false。默认为false。通常指定特殊用户群体投放卡券或防止刷券时选择指定用户领取。
		sb.append(couponBindToUser);
		sb.append(", \"brand_name\":\"");	//商户名字,字数上限为12个汉字。logo下面一行
		sb.append(brandName);
		sb.append("\",\"title\":\"");	//卡券名，字数上限为9个汉字。(建议涵盖卡券属性、服务及金额)
		sb.append(title);
		sb.append("\",\"sub_title\":\"");	//券名，字数上限为18个汉字。
		//		sb.append(subTitle);
		sb.append(couponModel.getContent());
		sb.append("\",\"date_info\":{\"type\":\"DATE_TYPE_FIX_TIME_RANGE\",\"begin_timestamp\":");
		sb.append(beginTimeTs);
		sb.append(",\"end_timestamp\":");
		sb.append(endTimeTs);
		sb.append("},\"color\":\"Color040\", \"service_phone\": \"400-116-0676\",\"notice\":\"");	//卡券使用提醒，字数上限为16个汉字
		//		sb.append("购买乐视乐次元季度会员领取果倍爽");
		sb.append("请让营业员扫码核销");


		sb.append("\",\"description\":\"");	//卡券使用说明，字数上限为1024个汉字。
		//		代金券说明   满X减X
		sb.append("1、购买可口可乐公司系列产品满" + Integer.parseInt(leastCost)/100 + "元可抵用（包含" + Integer.parseInt(leastCost)/100 + "元）。\n2、微信用户在活动页面勾选“接受活动规则”后方可参与本促销活动，成为参与用户。参与用户知晓并同意其勾选“接受活动规则”的行为视为其已完全阅读、理解并同意本活动规则的全部内容。活动 时间以微信平台系统时间为准。\n3、18周岁以下的未成年人需征得其监护人同意后方可参与本活动。未满12周岁的儿童需在其监护人陪同下参与本活动。\n4、同一微信账号、同一手机号、同一手机设备，符合其中任一条件者均视为同一参与用户。活动期间同一参与用户每天仅可使用三次。");

		if(couponModel.getMemory() == null){
			sb.append("");
		} else {
			sb.append(couponModel.getMemory());
		}
		sb.append("\",\"can_share\":true,\"can_give_friend\":true,\"sku\":{\"quantity\":0}},\"least_cost\":");
		sb.append(leastCost);
		sb.append(",\"reduce_cost\":");
		sb.append(reduceCost);
		sb.append("}}}");
		//		sb.append("\",\"get_limit\":8,\"can_share\":true,\"can_give_friend\":true,\"sku\":{\"quantity\":0}},\"default_detail\":\"凭券0元购果倍爽2盒（200ml*6袋/盒）\"");
		//		sb.append("\",\"can_share\":true,\"can_give_friend\":true,\"sku\":{\"quantity\":0}},\"default_detail\":\"优惠券测试\"");

		//		sb.append("}}}");
		logger.debug("平台id ：" + couponModel.getOwnerId());
		String url = WeixinConfig.API_PREFIX + "/card/create?access_token=" + accessToken;
		String data = sb.toString();
		System.out.println("尝试创建微信卡券:" + data);
		logger.debug("尝试创建微信卡券:" + data);
		HttpClient httpClient = HttpClientPoolV3.getHttpClient(WeixinConfig.API_HOST, WeixinConfig.API_PORT);
		String result = null;
		try{
			PostMethod pm = new PostMethod(url);
			pm.setRequestEntity(new StringRequestEntity(data,"","UTF-8"));
			int rs = httpClient.executeMethod(pm);
			if(rs != HttpStatus.OK.value()){
				logger.error("无法导入微信卡券[" + couponModel + "]的库存，对方服务器未返回200");
				return EisError.networkError.id;
			}
			result = new String(pm.getResponseBody(),"UTF-8");
		}catch(Exception e){
			e.printStackTrace();
		}
		logger.info("创建微信卡券返回结果:" + result);
		if(result == null){
			logger.error("无法创建微信卡券产品:" + result);
			return OperateResult.failed.getId();
		}
		JsonNode jsonNode  = null;
		try {
			jsonNode = JsonUtils.getInstance().readTree(result.trim());
		}catch(Exception e){
			e.printStackTrace();
		}
		if(jsonNode == null){
			logger.error("无法创建微信卡券产品:" + result);
			return OperateResult.failed.getId();
		}
		int errorCode = jsonNode.path("errcode").intValue();
		if(errorCode != 0){
			logger.error("无法创建微信卡券产品，微信返回错误:" + errorCode);
			return OperateResult.failed.getId();
		}
		String weixinCardId = jsonNode.path("card_id").textValue();
		couponModel.setCouponCode(weixinCardId);
		logger.debug("为我方卡券产品设置代码为微信卡券代码:" + couponModel.getCouponCode());
		return OperateResult.success.getId();
	}

	@Override
	public void getCardCodeStatus(long ownerId, String cardId, String code){
		String accessToken =  getAccessToken(ownerId);

		String data = "{\"card_id\":\"" + cardId + "\",\"code\":\"" + code + "\"}";
		String url = "http://api.weixin.qq.com/card/code/get?access_token=" + accessToken;
		HttpClient httpClient = HttpClientPoolV3.getHttpClient(WeixinConfig.API_HOST, WeixinConfig.API_PORT);
		String result = null;
		try{
			PostMethod pm = new PostMethod(url);
			pm.setRequestEntity(new StringRequestEntity(data,"","UTF-8"));
			int rs = httpClient.executeMethod(pm);
			if(rs != HttpStatus.OK.value()){
				logger.error("无法查询卡券[" + cardId + "]的卡[" + code + "]对方服务器未返回200");
			}
			result = new String(pm.getResponseBody(),"UTF-8");
		}catch(Exception e){
			e.printStackTrace();
		}
		logger.debug("微信卡券[" + cardId + "/" + code + "]查询结果:" + result);
	}

	/*
	 * 导入卡券CODE
	 */
	@Override
	public int importCardCode(long ownerId, String cardId, String... card){

		String accessToken =  getAccessToken(ownerId);
		String cardData = null;
		try {
			cardData = JsonUtils.getInstance().writeValueAsString(card);
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
		}
		String data = "{\"card_id\":\"" + cardId + "\",\"code\":" + cardData + "}";

		String url = "http://api.weixin.qq.com/card/code/deposit?access_token=" + accessToken;
		HttpClient httpClient = HttpClientPoolV3.getHttpClient(WeixinConfig.API_HOST, WeixinConfig.API_PORT);
		String result = null;
		JsonNode jsonNode = null;
		try{
			PostMethod pm = new PostMethod(url);
			pm.setRequestEntity(new StringRequestEntity(data,"","UTF-8"));
			int rs = httpClient.executeMethod(pm);
			if(rs != HttpStatus.OK.value()){
				logger.error("无法导入微信卡券[" + cardId + "]的库存，对方服务器未返回200");
				return EisError.networkError.id;
			}
			result = new String(pm.getResponseBody(),"UTF-8");
			jsonNode = JsonUtils.getInstance().readTree(result.trim());

		}catch(Exception e){
			e.printStackTrace();
			return EisError.networkError.getId();
		}
		if(jsonNode == null){
			logger.error("微信未返回合法的JSON数据:" + result);
			return EisError.networkError.id;
		}
		int errorCode = jsonNode.path("errcode").asInt();
		if(errorCode != 0){
			logger.error("无法为卡券[" + cardId + "]添加卡号:" + result);
			return OperateResult.failed.getId();
		}
		logger.info(result);
		return OperateResult.success.getId();
	}

	/*
	 * 列出卡券CODE

	public static void listCardCode(String cardId){
		String data = "{\"card_id\":\"" + cardId + "\",\"code\":[\"11111\",\"22222\",\"33333\",\"44444\",\"55555\"]}";
		String url = "http://api.weixin.qq.com/card/code/checkcode?access_token=" + token;
		HttpClient httpClient = HttpClientPoolV3.getHttpClient(host, port);
		String result = null;
		try{
			PostMethod pm = new PostMethod(url);
			pm.setRequestEntity(new StringRequestEntity(data,"","UTF-8"));
			int rs = httpClient.executeMethod(pm);

			result = new String(pm.getResponseBody(),"UTF-8");
		}catch(Exception e){
			e.printStackTrace();
		}
		logger.info(result);
	}

	public static void getCard(String cardId){
		String url = WeixinConfig.API_PREFIX + "/card/get?access_token=" + token;
		HttpClient httpClient = HttpClientPoolV3.getHttpClient(host, port);
		String cardData = "{\"card_id\":\"" + cardId + "\"}";
		String result = null;
		try{
			PostMethod pm = new PostMethod(url);
			pm.setRequestEntity(new StringRequestEntity(cardData,"","UTF-8"));
			int rs = httpClient.executeMethod(pm);

			result = new String(pm.getResponseBody(),"UTF-8");
			//	result = HttpUtilsV3.p(httpClient, url, null);
		}catch(Exception e){
			e.printStackTrace();
		}
		logger.info(result);
	}
	 */

	//@Override
	public int addCardStock(long ownerId, String weixinCardId, int amount){

		String accessToken =  getAccessToken(ownerId);
		String url = WeixinConfig.API_PREFIX + "/card/modifystock?access_token=" + accessToken;
		HttpClient httpClient = HttpClientPoolV3.getHttpClient(WeixinConfig.API_HOST, WeixinConfig.API_PORT);
		String data = "{\"card_id\": \"" + weixinCardId + "\",\"increase_stock_value\":\"" + amount + "\"}";
		String result = null;
		try{
			PostMethod pm = new PostMethod(url);
			pm.setRequestEntity(new StringRequestEntity(data,"","UTF-8"));
			int rs = httpClient.executeMethod(pm);
			if(rs != HttpStatus.OK.value()){
				logger.error("无法添加微信卡券[" + weixinCardId + "]的库存数量，对方服务器未返回200");
				return EisError.networkError.id;
			}
			result = new String(pm.getResponseBody(),"UTF-8");
			//	result = HttpUtilsV3.p(httpClient, url, null);
		}catch(Exception e){
			e.printStackTrace();
		}
		logger.info("修改微信卡券[" + weixinCardId + "]的库存+" + amount + ",结果:" + result);
		return 0;
	}
	/*
	public static void sendCardMass(){
		String url = WeixinConfig.API_PREFIX + "/cgi-bin/message/mass/send?access_token=" + token;
		HttpClient httpClient = HttpClientPoolV3.getHttpClient(host, port);
		String data = "{\"touser\":[\"oU8Bxw-sYVD-jN7VQDt9f8OOGqbE\",\"oU8Bxw8hZNnK4ik9ZrsoLRDkn5UE\"],\"wxcard\":{\"card_id\":\"pU8Bxw8hG0_mo20E_8ABhILr73XQ\"},\"msgtype\":\"wxcard\"}";
		String result = null;
		try{
			PostMethod pm = new PostMethod(url);
			pm.setRequestEntity(new StringRequestEntity(data,"","UTF-8"));
			int rs = httpClient.executeMethod(pm);

			result = new String(pm.getResponseBody(),"UTF-8");
			//	result = HttpUtilsV3.p(httpClient, url, null);
		}catch(Exception e){
			e.printStackTrace();
		}
		logger.info(result);
	}*/

	@Override
	public int sendWeixinCouponFromCustomInterface(long ownerId, String openId, String cardId, String code){
		HttpClient httpClient = HttpClientPoolV3.getHttpClient(WeixinConfig.API_HOST, WeixinConfig.API_PORT);
		String result = null;
		String accessToken =  getAccessToken(ownerId);
		//获取ticket
		String url  = WeixinConfig.API_PREFIX + "/cgi-bin/ticket/getticket?access_token=" + accessToken + "&type=wx_card";
		try{
			GetMethod method = new GetMethod(url);
			int rs = httpClient.executeMethod(method);
			if(rs != HttpStatus.OK.value()){
				logger.error("无法获取微信客服接口token，对方服务器未返回200");
				return EisError.networkError.id;
			}
			result = new String(method.getResponseBody(),"UTF-8");
			//	result = HttpUtilsV3.p(httpClient, url, null);
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		logger.info(result);
		String ticket = null;
		try {
			ticket = JsonUtils.getInstance().readTree(result.trim()).path("ticket").asText();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("得到客服接口的ticket:" + ticket);

		String timestamp = String.valueOf(new Date().getTime() / 1000);
		String  nonce_str  = DigestUtils.md5Hex(timestamp);
		Map<String,String>map = new HashMap<String,String>();
		if(code != null){
			map.put("code", code);
		}
		map.put("openid",openId);
		map.put("timestamp",timestamp);
		map.put("nonce_str",nonce_str);

		List<String> values = new ArrayList<String>(map.values());
		Collections.sort(values);
		String signSource = "";
		for(String value : values){
			signSource += value;
		}
		String sign = DigestUtils.shaHex(signSource);
		logger.info("校验源:" + signSource + ",校验后:" + sign);
		url = WeixinConfig.API_PREFIX + "/cgi-bin/message/custom/send?access_token=" + accessToken;;

		String data = "{\"touser\":\"" + openId + "\",\"msgtype\":\"wxcard\",\"wxcard\":{\"card_id\":\"" + cardId + "\"},\"card_ext\":{\"code\":\"" + code + "\",\"openid\":\"" + openId + "\",\"timestamp\":\"" + timestamp + "\",\"signature\":\"" + sign + "\"}}}";
		logger.info("请求客服接口发送消息的结果:" + data);
		try{
			PostMethod pm = new PostMethod(url);
			pm.setRequestEntity(new StringRequestEntity(data,"","UTF-8"));
			int rs = httpClient.executeMethod(pm);
			if(rs != HttpStatus.OK.value()){
				logger.error("无法通过客服接口发送微信卡券[" + cardId + "/" + code  + "]，对方服务器未返回200");
				return EisError.networkError.id;
			}
			result = new String(pm.getResponseBody(),"UTF-8");
			//	result = HttpUtilsV3.p(httpClient, url, null);
		}catch(Exception e){
			e.printStackTrace();
		}
		logger.info("通过客服接口发送微信卡券[" + cardId + "/" + code  + "]下发结果:"+result);
		return OperateResult.success.getId();
	}

	@Override
	public int addStockAndSendCustomCouponToWeixin( String openId, Coupon coupon){
		String weixinCardId = null;
		if(NumericUtils.isNumeric(coupon.getExtraCode())){
			logger.debug("优惠券[" + coupon + "]的extraCode是纯数字，尝试使用couponCode[" + coupon.getCouponCode() + "]下发微信卡券");
			weixinCardId = coupon.getCouponCode();
		} else {
			weixinCardId = coupon.getExtraCode();
		}
		int rs = importCardCode(coupon.getOwnerId(), weixinCardId, coupon.getContent());
		if(rs != OperateResult.success.getId()){
			logger.error("无法导入优惠券:" + rs);
			return rs;
		}
		addCardStock(coupon.getOwnerId(),weixinCardId,1);
		getCardCodeStatus(coupon.getOwnerId(), weixinCardId, coupon.getContent());
		//listCardCode(coupon.getExtraCode());
		sendWeixinCouponFromCustomInterface(coupon.getOwnerId(), openId, weixinCardId, coupon.getContent());
		return 0;
	}

	/**
	 * 获取微信卡券的JSAPI访问ticket
	 */
	@Override
	public String getCouponToken(long ownerId){

		String url  = WeixinConfig.API_PREFIX + "/cgi-bin/ticket/getticket?access_token=" + getAccessToken(ownerId) + "&type=wx_card";
		logger.debug("获取微信卡券的JSAPI访问ticket的URL：" + url);
		String coupon_token="";
		String expires_in="";
		WeixinToken tK= cacheService.get(CommonStandard.cacheNameValidate, couponTokenName);;
		boolean forceClean = false;
		if (tK==null || tK.getExpires_time()<System.currentTimeMillis()){	
			forceClean = true;
		}
		logger.debug("forceClean : " + forceClean);
		forceClean = true;
		if(forceClean){
			try	{
				String result=httpservice.httpsGet(url);
				coupon_token=om.readTree(result).get("ticket").asText();
				expires_in=om.readTree(result).get("expires_in").asText();
			}
			catch(Exception e){
				logger.info("从微信服务器取coupon token出错");
				return null;
			}
			logger.info("更换微信coupon token为"+coupon_token);
			tK = new WeixinToken();
			tK.setToken(coupon_token);
			//logger.info("超时时间是"+Long.valueOf(expires_in));
			tK.setExpires_time(new Date().getTime()+Long.valueOf(expires_in)*1000 - 10 * 1000);
			cacheService.put(CommonStandard.cacheNameValidate, couponTokenName, tK);

		}
		logger.debug("最新coupon token是:" + tK.getToken());
		return tK.getToken();

	}


	@Override
	public void makeCouponJsSignature(String timeStamp, String nonceStr, long ownerId, CouponModel couponModel) {

		String couponToken = getCouponToken(ownerId);

		List<String> values = new ArrayList<String>();
		if(couponModel.getContent() != null){
			values.add(couponModel.getContent());
		}
		values.add(couponToken);
		values.add(timeStamp);
		values.add(nonceStr);
		values.add(couponModel.getCouponCode());
		//values.add(frontUser.getUsername());
		Collections.sort(values);

		String signSource = "";
		for(String value : values){
			signSource += value;
		}
		String sign = DigestUtils.shaHex(signSource);
		logger.info("微信卡券[" + couponModel.getCouponCode() + "]校验源:" + signSource + ",校验后:" + sign);		
		couponModel.setMessageId(sign);

		return ;		
	}

	/**
	 * 把系统中的优惠券数据Coupon，转换为微信的卡券CARD
	 * 并将转换后的微信卡券转换为JSON数据，放入系统优惠券中的messageId
	 * 该数据只用于动态转换，不需要进行存储
	 */
	@SuppressWarnings("unused")
	@Override
	public void convertToWeixinCoupon(Coupon coupon, String openId, String timeStamp) {
		if(coupon.getOwnerId() < 1){
			logger.error("尝试签名的优惠券没有ownerId");
			return;
		}
		if(timeStamp == null){
			timeStamp = String.valueOf(new Date().getTime() / 1000);
		}
		String couponToken = getCouponToken(coupon.getOwnerId());
		String weixinCouponId = coupon.getCouponCode();
		String couponSn = coupon.getCouponSerialNumber();
		String  nonce_str  = DigestUtils.md5Hex(timeStamp);

		//FIXME 调试关闭
		couponSn = null;
		coupon.setContent(nonce_str);
		//String appId = getAppId(coupon.getOwnerId());
		List<String> values = new ArrayList<String>();

		//values.add(appId);
		values.add(couponToken);
		values.add(timeStamp);
		values.add(nonce_str);
		values.add(weixinCouponId);
		//FIXME调试关闭了自定义内容
		if(couponSn != null){
			values.add(couponSn);
		}
		/*if(openId != null){
			values.add(openId);
		}*/
		Collections.sort(values);

		String signSource = "";
		for(String value : values){
			signSource += value;
		}
		String sign = DigestUtils.shaHex(signSource);
		logger.debug("使用微信卡券[代码=" + weixinCouponId + "]为用户生成的卡号[" + couponSn + "]校验，校验源[couponToken=" + couponToken + ",timestamp=" + timeStamp + ",nonce_str=" + nonce_str + ",couponId=" + weixinCouponId + ",sn=" + couponSn + ",openId=" + openId + "],signSourceString=" + signSource + ",校验后:" + sign);
		logger.info("微信卡券[代码=" + weixinCouponId + ",卡号=" + couponSn + "，timestamp=" + timeStamp + ",sign=" + sign);
		WeixinCoupon weixinCoupon = new WeixinCoupon(weixinCouponId, couponSn, timeStamp, sign, nonce_str, null, null);
		//	map.put("couponSign", sign);
		//	map.put("couponCode", couponSn);
		//	map.put("couponNonceStr", nonce_str);
		//	map.put("couponTs", timeStamp);
		//	map.put("timestamp", timeStamp);
		/*

		String jsonString = "{\"code\":\"" + coupon.getContent() + "\",";
		if(openId != null){
			jsonString += "openid=\"" + openId + "\",";
		}

		jsonString += "\"timestamp\":\""+ timeStamp +"\",\"signature\":\""+ sign +"\"}";
		coupon.setMessageId(jsonString);
		logger.info("使用微信卡券[" + coupon.getCouponCode() + "]为用户生成的卡号JSON串: " + jsonString);*/
		String jsonString = null;
		try {
			jsonString = JsonUtils.getInstance().writeValueAsString(weixinCoupon);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		coupon.setMessageId(jsonString);
		return ;		
	}


	@Override
	public boolean isWeixinAccess(HttpServletRequest request) {
		String ua =request.getHeader("user-agent");
		if(ua == null){
			return false;
		}
		return AgentUtils.isWeixinAccess(ua.toLowerCase());
	}


	@Override
	public boolean isWeixinAccess(Map<String, String> requestDataMap) {
		return AgentUtils.isWeixinAccess(requestDataMap);
	}


	@Override
	public String generateCouponListString(List<Coupon> couponList) {
		List<WeixinCoupon> list = new ArrayList<WeixinCoupon>();
		for(Coupon coupon : couponList){
			WeixinCoupon weixinCoupon = null;
			try {
				weixinCoupon = JsonUtils.getInstance().readValue(coupon.getMessageId(), WeixinCoupon.class);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(weixinCoupon != null){
				list.add(weixinCoupon);
			}
		}
		String jsonString = null;
		try {
			jsonString = JsonUtils.getInstance().writeValueAsString(list);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		if(jsonString != null){
			return jsonString;
			//	return jsonString.replaceAll("cardExt\":\\{", "cardExt\":'{").replaceAll("\"\\}\\}", "\"}'}");
		}
		return null;
	}


	/**
	 * 查找并发送所有关注时的消息
	 */
	@Override
	public String sendSubscribeMsg(HttpServletRequest request, WeixinMsg weixinMsg, long ownerId) {
		WeixinRichTextMessageCriteria weixinRichTextMessageCriteria = new WeixinRichTextMessageCriteria();
		weixinRichTextMessageCriteria.setOwnerId(ownerId);
		weixinRichTextMessageCriteria.setTrigger(WeixinRichTextMessageCriteria.TRIGGER_MODE_ON_SUBSCRIBE);
		weixinRichTextMessageCriteria.setCurrentStatus(BasicStatus.normal.getId());
		List<WeixinRichTextMessage> weixinRichTextMessageList = weixinRichTextMessageService.list(weixinRichTextMessageCriteria);
		if(weixinRichTextMessageList == null || weixinRichTextMessageList.size() < 1){
			logger.debug("当前未定义任何关注时发送的消息,ownerId=" + ownerId);
			return null;
		}
		String to = weixinMsg.getFromUserName();
		String from = weixinMsg.getToUserName();
		String urlPrefix = HttpUtils.generateUrlPrefix(request);
		WeixinRichTextMessage directResponseMessage = weixinRichTextMessageList.get(0);
		directResponseMessage.setTo(to);
		directResponseMessage.setFrom(from);

		if(weixinRichTextMessageList.size() == 1){
			logger.debug("当前只定义了1个关注时发送的消息[" + weixinRichTextMessageList.get(0).getWeixinRichTextMessageId() + ",直接返回该消息");
			return formatMsgToXml(directResponseMessage,urlPrefix);
		}
		String accessToken = getAccessToken(ownerId);
		logger.debug("当前定义了" + weixinRichTextMessageList.size() + "个关注时发送的消息");

		//除了优先级最高的之外，剩下的全部异步发送
		for(int i = 1; i < weixinRichTextMessageList.size(); i++){
			WeixinRichTextMessage weixinRichTextMessage = weixinRichTextMessageList.get(i);
			weixinRichTextMessage.setDelaySec(batchSendDealySec * i);
			weixinRichTextMessage.setTo(to);
			weixinRichTextMessage.setFrom(from);
			new Thread(new WeixinAsyncMessage(weixinRichTextMessage, accessToken, urlPrefix)).start();
		}

		logger.debug("返回优先级最高的关注时发送消息[" + weixinRichTextMessageList.get(0).getWeixinRichTextMessageId() + ",其他异步发送");
		return formatMsgToXml(directResponseMessage, urlPrefix);
	}

	private String formatMsgToXml(WeixinRichTextMessage weixinRichTextMessage, String urlPrefix){

		String picUrl = weixinRichTextMessage.getPicUrl();
		if(picUrl != null && picUrl.startsWith("/")){
			//把相对地址改为绝对地址
			picUrl = urlPrefix + picUrl;		
		}
		String url = weixinRichTextMessage.getUrl();
		if(url != null && url.startsWith("/")){
			//把相对地址改为绝对地址
			url = urlPrefix + url;		
		}
		StringBuffer sb = new StringBuffer();  
		Date date = new Date();  
		sb.append("<xml><ToUserName><![CDATA[");  
		sb.append(weixinRichTextMessage.getTo());  
		sb.append("]]></ToUserName><FromUserName><![CDATA[");  
		sb.append(weixinRichTextMessage.getFrom());  
		sb.append("]]></FromUserName><CreateTime>");  
		sb.append(date.getTime());  
		if (StringUtils.isNotBlank(weixinRichTextMessage.getPicUrl()) && StringUtils.isNotBlank(weixinRichTextMessage.getUrl())) {
			sb.append("</CreateTime><MsgType><![CDATA[news]]></MsgType>");
			sb.append("<ArticleCount>1</ArticleCount>");  
			sb.append("<Articles>");  
			sb.append("<item>");  
			sb.append("<Title><![CDATA[" + weixinRichTextMessage.getTitle() + "]]></Title>");  
			sb.append("<Description><![CDATA[" + weixinRichTextMessage.getContent() + "]]></Description>");
			sb.append("<PicUrl><![CDATA[" +  picUrl + "]]></PicUrl>");
			sb.append("<Url><![CDATA[" + url + "]]></Url>");  
			sb.append("</item>");
			sb.append("</Articles>"); 
			sb.append("</xml>");  
		} else {
			sb.append("</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[");  
			sb.append(weixinRichTextMessage.getContent());  
			sb.append("]]></Content><FuncFlag>0</FuncFlag></xml>");
		}


		/*sb.append("<item>");  
		sb.append("<Title><![CDATA[" + welcomeBaojieTitle + "]]></Title>");  
		sb.append("<Description><![CDATA[" + welcomeBaojieContent + "]]></Description>");  
		sb.append("<PicUrl><![CDATA[" + welcomeBaojiePicUrl + "]]></PicUrl>");  
		sb.append("<Url><![CDATA[" + welcomeBaojieUrl + "]]></Url>");  
		sb.append("</item>");  */

		return sb.toString();  
	}


	

	@Override
	public int setUserGroup(User frontUser, WeixinGroup weixinGroup) {

		if(weixinGroup.getOutGroupId() < 1){
			//尝试创建该组
			WeixinGroup g2 = createGroup(weixinGroup);
			weixinGroup = g2;
		} 

		if(weixinGroup.getOutGroupId() < 1){
			logger.error("无法把微信用户[" + frontUser.getUuid() + "/" + frontUser.getUsername() + "]移动到微信分组[" + weixinGroup.getGroupId() + "],因为分组外部ID为0");
			return EisError.dataError.id;
		}

		String url = WeixinConfig.API_PREFIX + "/cgi-bin/groups/members/update?access_token=" + this.getAccessToken(frontUser.getOwnerId());

		ObjectNode objectNode = om.createObjectNode();
		objectNode.put("openid", frontUser.getUsername());
		objectNode.put("to_groupid", weixinGroup.getOutGroupId());
		HttpClient httpClient = HttpClientPoolV3.getHttpClient(WeixinConfig.API_HOST, WeixinConfig.API_PORT);
		String result = null;
		String data = null;
		try {
			data = om.writeValueAsString(objectNode);
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
		}
		logger.debug("准备提交数据[" + data + "]把微信用户[" + frontUser.getUuid() + "/" + frontUser.getUsername() + "]移动到微信分组[" + weixinGroup.getGroupId() + "]");
		try{
			PostMethod pm = new PostMethod(url);
			pm.setRequestEntity(new StringRequestEntity(data,"","UTF-8"));
			int rs = httpClient.executeMethod(pm);
			if(rs != HttpStatus.OK.value()){
				logger.error("无法移动微信用户[" + frontUser.getUuid() + "/" + frontUser.getUsername() + "]到分组[" + weixinGroup.getGroupId() + "]，对方服务器未返回200");
				return EisError.networkError.id;
			}
			result = new String(pm.getResponseBody(),"UTF-8");
		}catch(Exception e){
			e.printStackTrace();
		}
		logger.debug("移动微信用户[" + frontUser.getUuid() + "/" + frontUser.getUsername() + "]到分组[" + weixinGroup.getGroupId() + "]，结果:" + result);
		if(result == null){
			logger.error("无法移动微信用户[" + frontUser.getUuid() + "/" + frontUser.getUsername() + "]到分组[" + weixinGroup.getGroupId() + "]，对方服务器返回空");
			return EisError.networkError.id;
		}
		JsonNode jsonNode  = null;
		try {
			jsonNode = JsonUtils.getInstance().readTree(result.trim());
		}catch(Exception e){
			e.printStackTrace();
		}
		if(jsonNode == null){
			logger.error("无法移动微信用户[" + frontUser.getUuid() + "/" + frontUser.getUsername() + "]到分组[" + weixinGroup.getGroupId() + "]，对方服务器返回:" + result);
			return OperateResult.failed.getId();
		}
		int errorCode = jsonNode.path("errcode").intValue();
		if(errorCode != 0){
			logger.error("无法移动微信用户[" + frontUser.getUuid() + "/" + frontUser.getUsername() + "]到分组[" + weixinGroup.getGroupId() + "]，对方服务器返回错误码:" + errorCode);
			return OperateResult.failed.getId();
		}
		return 1;




	}
	@Override
	public  List<WeixinGroup> listWeixinGroup(long ownerId) {
		String url = WeixinConfig.API_PREFIX + "/cgi-bin/groups/get?access_token=" + this.getAccessToken(ownerId);
		String result = null;
		for(int i = 0; i < NETWORK_RETRY; i++){
			try {
				result = httpservice.httpsGet(url);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(result != null){
				break;
			}
			logger.error("无法从微信端获取数据");

		}
		if(result == null){
			logger.error("重试" + NETWORK_RETRY + "次后，仍然无法从微信端获取数据");
			return null;
		}
		List<WeixinGroup> weixinGroupList = new ArrayList<WeixinGroup>();
		Iterator<JsonNode> it = null;
		try {
			JsonNode jsonNode =  om.readTree(result.trim()).path("groups");
			it = jsonNode.elements();

		} catch (IOException e) {
			e.printStackTrace();
		}
		if(it == null){
			logger.error("无法解析微信端返回:" + result);
			return null;
		}
		while(it.hasNext()){
			JsonNode sub = it.next();
			WeixinGroup weixinGroup = new WeixinGroup(ownerId);
			weixinGroup.setOutGroupId(sub.path("id").asLong());
			weixinGroup.setGroupName(sub.path("name").asText());
			weixinGroup.setUserCount(sub.path("count").asInt());
			weixinGroupList.add(weixinGroup);

		}
		return weixinGroupList;
	}


	@Override
	public WeixinGroup createGroup(WeixinGroup weixinGroup) {
		String url = WeixinConfig.API_PREFIX + "/cgi-bin/groups/create?access_token=" + this.getAccessToken(weixinGroup.getOwnerId());

		ObjectNode objectNode = om.createObjectNode();
		ObjectNode groupNode  = objectNode.putObject("group");
		groupNode.put("name", weixinGroup.getGroupName());

		HttpClient httpClient = HttpClientPoolV3.getHttpClient(WeixinConfig.API_HOST, WeixinConfig.API_PORT);
		String result = null;
		String data = null;
		try {
			data = om.writeValueAsString(objectNode);
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
		}
		logger.debug("准备提交数据[" + data + "]以创建微信分组");
		try{
			PostMethod pm = new PostMethod(url);
			pm.setRequestEntity(new StringRequestEntity(data,"","UTF-8"));
			int rs = httpClient.executeMethod(pm);
			if(rs != HttpStatus.OK.value()){
				logger.error("无法创建微信分组[" + weixinGroup.getGroupId() + "/" + weixinGroup.getGroupName() + "]对方服务器未返回200");
				return null;
			}
			result = new String(pm.getResponseBody(),"UTF-8");
		}catch(Exception e){
			e.printStackTrace();
		}
		logger.debug("创建微信分组[" +  weixinGroup.getGroupId() + "/" + weixinGroup.getGroupName() + "]结果:" + result);
		if(result == null){
			logger.error("无法创建微信分组[" + weixinGroup.getGroupId() + "/" + weixinGroup.getGroupName() + "]对方服务器返回空");
			return null;
		}
		WeixinGroup group2 = convertJson2Group(result, weixinGroup.getOwnerId());
		weixinGroup.setOutGroupId(group2.getOutGroupId());

		//同步到本地数据库
		syncGroup2Db(weixinGroup);
		return group2;
	}


	private void syncGroup2Db(WeixinGroup weixinGroup) {
		if(weixinGroup.getGroupId() > 0){
			weixinGroupService.update(weixinGroup);
		} else {
			weixinGroupService.insert(weixinGroup);

		}

	}


	private WeixinGroup convertJson2Group(String result, long ownerId) {
		JsonNode sub = null;
		try {
			sub = JsonUtils.getInstance().readTree(result).path("group");
		} catch (IOException e) {
			e.printStackTrace();
		}

		WeixinGroup weixinGroup = new WeixinGroup(ownerId);
		weixinGroup.setOutGroupId(sub.path("id").asLong());
		weixinGroup.setGroupName(sub.path("name").asText());
		weixinGroup.setUserCount(sub.path("count").asInt());
		return weixinGroup;
	}

	/*@Override
	public int createWeixinButton(List<WeixinButton> weixinButtonList, String accessToken, String menuTag, long ownerId){
		try {
			logger.debug("原始微信菜单:" + om.writeValueAsString(weixinButtonList));
		} catch (JsonProcessingException e2) {
			e2.printStackTrace();
		}
		for(WeixinButton weixinButton : weixinButtonList){
			weixinButton.setCurrentStatus(0);
			weixinButton.setUuid(0);
			weixinButton.setWeixinButtonId(0);
			weixinButton.setParentButtonId(0);
		}
		ObjectNode on = om.createObjectNode();
		on.putPOJO("button", weixinButtonList);

		String menuJsonString = null;
		try {
			menuJsonString = om.writeValueAsString(on);
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
		}
		logger.debug("创建微信公众号菜单[accessToken=" + accessToken + ",menuTag=" + menuTag + "]:菜单:" + menuJsonString);
		if(accessToken == null){
			accessToken = this.getAccessToken(ownerId);
		}
		String url = WeixinConfig.API_PREFIX + "/cgi-bin/menu/create?access_token=" + accessToken;
		HttpClient httpClient = HttpClientPoolV3.getHttpClient(WeixinConfig.API_HOST, WeixinConfig.API_PORT);
		String result = null;
		int rs = 0;
		try{
			PostMethod pm = new PostMethod(url);
			pm.setRequestEntity(new StringRequestEntity(menuJsonString,"","UTF-8"));
			rs = httpClient.executeMethod(pm);

			result = pm.getResponseBodyAsString();
		}catch(Exception e){
			e.printStackTrace();
		}
		logger.debug("创建微信公众号菜单[accessToken=" + accessToken + ",menuTag=" + menuTag + "]，结果:[rs=" + rs + ",result=" + result + "]");
		return 0;
	}
*/
	@Override
	public WeixinMenu createMenu(WeixinMenu weixinMenu) {
		String url = WeixinConfig.API_PREFIX + "/cgi-bin/menu/addconditional?access_token=" + this.getAccessToken(weixinMenu.getOwnerId());

		ObjectNode objectNode = om.createObjectNode();
		ObjectNode groupNode  = objectNode.putObject("group");
		//groupNode.put("name", weixinMenu.getGroupName());

		/*HttpClient httpClient = HttpClientPoolV3.getHttpClient(WeixinConfig.host, WeixinConfig.port);
		String result = null;
		String data = null;
		try {
			data = om.writeValueAsString(objectNode);
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
		}
		logger.debug("准备提交数据[" + data + "]以创建微信分组");
		try{
			PostMethod pm = new PostMethod(url);
			pm.setRequestEntity(new StringRequestEntity(data,"","UTF-8"));
			int rs = httpClient.executeMethod(pm);
			if(rs != HttpStatus.OK.value()){
				logger.error("无法创建微信分组[" + weixinMenu.getGroupId() + "/" + weixinMenu.getGroupName() + "]对方服务器未返回200");
				return null;
			}
			result = new String(pm.getResponseBody(),"UTF-8");
		}catch(Exception e){
			e.printStackTrace();
		}
		logger.debug("创建微信分组[" +  weixinMenu.getGroupId() + "/" + weixinMenu.getGroupName() + "]结果:" + result);
		if(result == null){
			logger.error("无法创建微信分组[" + weixinMenu.getGroupId() + "/" + weixinMenu.getGroupName() + "]对方服务器返回空");
			return null;
		}
		WeixinGroup group2 = convertJson2Group(result, weixinMenu.getOwnerId());
		weixinMenu.setOutGroupId(group2.getOutGroupId());

		//同步到本地数据库
		syncGroup2Db(weixinMenu);
		return group2;*/

		return null;
	}


	/** 处理扫码后进入
	 * 如果有必要，对用户本次行为做标记
	 */
	@Override
	public void processScanMessage(User frontUser, WeixinMsg weixinMsg) {
		if(weixinMsg.getEventKey() == null || weixinMsg.getEventKey().trim().equals("0")){
			logger.info("用户扫码但没有任何KEY");
		}

		if(frontUser.getUserConfigMap() == null){
			frontUser.setUserConfigMap(new HashMap<String,UserData>());
		}
		frontUser.getUserConfigMap().put(DataName.lastEventKey.toString(), new UserData(frontUser.getUuid(), DataName.lastEventKey.toString(),weixinMsg.getEventKey()));
		logger.debug("为用户更新lastEventKey:" + weixinMsg.getEventKey() );	
		try {
			frontUserService.update(frontUser);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	//@Override
	public String getOpenIdByCode(String weixinCode, long ownerId) {
		if(weixinCode == null){
			logger.error("无法通过code获取用户信息，因为code为空");
			return null;
		}
		String authUrl = WeixinConfig.API_PREFIX + "/sns/oauth2/access_token?appid="+ getAppId(ownerId) +"&secret="+ getAppSecret(ownerId) +"&code="+weixinCode+"&grant_type=authorization_code";
		String authResult = null;
		String openId = null;
		int errorCode = 0;
		try {
			authResult=httpsService.httpsGet(authUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("根据code=" + weixinCode + "获取用户信息[" + authUrl + "]，结果:" + authResult);
		if(authResult == null){
			return null;
		}
		JsonNode json = null;
		try {
			json = om.readTree(authResult);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(json == null){
			logger.error("无法解析微信返回数据:" + authResult);
			return null;
		}
		errorCode = json.path("errcode").asInt();
		if(errorCode == 40029){
			logger.error("错误的微信code");

		} else {
			openId = json.get("openid").asText();
			if(openId != null){
				errorCode = 0;
			} 
		}
		/**
		 * XXX 
		 * 微信有时会出现认证返回40029的情况，具体原因不确定
		 * 可能是当前code失效或微信自身的问题
		 * 这时候需要让用户重新访问不带code和state的链接，以重新发起授权请求.
		 * NetSnake, 2016-05-18
		 */
		if(errorCode == 40029){
			return "ERROR#40029";

		}
		return openId;
		/*}
		catch(Exception e){
			logger.error("无法解析微信返回:" + ExceptionUtils.getFullStackTrace(e));
		}*/
	}

}



