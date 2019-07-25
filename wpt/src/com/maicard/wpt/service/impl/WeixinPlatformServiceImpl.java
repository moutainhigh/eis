package com.maicard.wpt.service.impl;

import static com.maicard.wpt.constants.WeixinConfig.ACCESS_TOKEN_CACHE_KEY;
import static com.maicard.wpt.constants.WeixinConfig.API_HOST;
import static com.maicard.wpt.constants.WeixinConfig.API_PORT;
import static com.maicard.wpt.constants.WeixinConfig.API_PREFIX;
import static com.maicard.wpt.constants.WeixinConfig.AUTH_ACCESS_TOKEN_CACHE_KEY;
import static com.maicard.wpt.constants.WeixinConfig.AUTH_REFRESH_TOKEN_CACHE_KEY;
import static com.maicard.wpt.constants.WeixinConfig.BATCH_SEND_DELAY_SEC;
import static com.maicard.wpt.constants.WeixinConfig.COUPON_TOKEN_CACHE_KEY;
import static com.maicard.wpt.constants.WeixinConfig.DEFAULT_NULL_REPLAY_MESSAGE;
import static com.maicard.wpt.constants.WeixinConfig.PREAUTH_CODE_CACHE_KEY;
import static com.maicard.wpt.constants.WeixinConfig.REFRESH_TOKEN_CACHE_KEY;
import static com.maicard.wpt.constants.WeixinConfig.VERIFY_TICKET_CACHE_KEY;
import static com.maicard.wpt.constants.WeixinConfig.WEIXIN_CLIENT_APPID_MAP;
import static com.maicard.wpt.constants.WeixinConfig.WEIXIN_PLATFORM_CACHE_KEY;

import java.io.IOException;
import java.io.StringReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.maicard.common.base.BaseService;
import com.maicard.common.criteria.SiteThemeRelationCriteria;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.domain.SiteThemeRelation;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.CenterDataService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.SiteThemeRelationService;
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
import com.maicard.product.service.ActivityProcessor;
import com.maicard.product.service.ActivityService;
import com.maicard.security.criteria.UserCriteria;
import com.maicard.security.domain.User;
import com.maicard.security.domain.UserData;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.FrontUserService;
import com.maicard.security.service.PartnerService;
import com.maicard.standard.BasicStatus;
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
import com.maicard.wpt.domain.WeixinAsyncMessage;
import com.maicard.wpt.domain.WeixinButton;
import com.maicard.wpt.domain.WeixinClient;
import com.maicard.wpt.domain.WeixinCoupon;
import com.maicard.wpt.domain.WeixinGroup;
import com.maicard.wpt.domain.WeixinMsg;
import com.maicard.wpt.domain.WeixinPlatformInfo;
import com.maicard.wpt.domain.WeixinRichTextMessage;
import com.maicard.wpt.misc.weixin.Constants;
import com.maicard.wpt.misc.weixin.WeixinError;
import com.maicard.wpt.service.AutoResponseModelService;
import com.maicard.wpt.service.TulingRobotService;
import com.maicard.wpt.service.WeixinGroupService;
import com.maicard.wpt.service.WeixinPlatformService;
import com.maicard.wpt.service.WeixinRichTextMessageService;
import com.maicard.wpt.utils.weixin.StringFormat;
import com.maicard.wpt.utils.weixin.aes.WXBizMsgCrypt;


public class WeixinPlatformServiceImpl extends BaseService implements WeixinPlatformService {



	@Resource
	private ApplicationContextService applicationContextService;

	@Resource
	private AutoResponseModelService autoResponseModelService;

	@Resource 
	private ActivityService activityService;

	@Resource
	private ConfigService configService;

	@Resource
	private CenterDataService centerDataService;

	@Resource
	private CertifyService certifyService;

	@Resource
	private CouponModelService couponModelService;
	@Resource
	private CouponService couponService;


	@Resource
	private FrontUserService frontUserService;

	@Resource
	private SiteThemeRelationService siteThemeRelationService;

	@Resource
	private PartnerService partnerService;

	@Resource
	private TulingRobotService tulingRobotService;


	@Resource
	private WeixinGroupService weixinGroupService;


	@Resource
	private WeixinRichTextMessageService weixinRichTextMessageService;

	/**
	 * 当系统是一个第三方公众平台时，存放平台自身的第三方平台信息
	 */
	private static Map<String, WeixinPlatformInfo> weixinPlatformInfoCache = new LinkedHashMap<String, WeixinPlatformInfo>();


	/**
	 * 当系统不是第三方公众平台时，存放系统的公众号信息
	 */
	private static Map<String, WeixinPlatformInfo> weixinSingleInfoCache = new LinkedHashMap<String, WeixinPlatformInfo>();




	private final int VERIFY_TOKEN_TTL = 650;
	private final int DEFAULT_TTL = 3600 * 24;

	ObjectMapper om = JsonUtils.getNoDefaultValueInstance();

	/**
	 * 微信加密消息格式
	 */
	private final 	String xmlFormat = "<xml><ToUserName><![CDATA[toUser]]></ToUserName><Encrypt><![CDATA[%1$s]]></Encrypt></xml>";

	/**
	 * 获取第三方平台的access_token
	 */
	@Override
	public String getPlatformAccessToken(long ownerId) {

		//先获取缓存中的accessToken
		String accessToken = _getAccessTokenFromCache(ownerId);
		if(accessToken != null){
			return accessToken;
		}
		//如果缓存不存在，再从微信服务器获取
		String verifyTicket = this.getVerifyTicket(ownerId);
		if(verifyTicket == null){
			logger.error("无法获取ownerId=" + ownerId + "的校验令牌VERIFY_TICKET");
			return null;
		}
		WeixinPlatformInfo weixinPlatformInfo  = this.getWeixinPlatformInfo(ownerId);
		if(weixinPlatformInfo == null){
			logger.error("系统中没有合法的第三方平台帐号配置");
			return null;
		}

		ObjectMapper om = JsonUtils.getInstance();
		ObjectNode objectNode = om.createObjectNode();
		objectNode.put("component_appid", weixinPlatformInfo.appId);
		objectNode.put("component_appsecret", weixinPlatformInfo.appSecret);
		objectNode.put("component_verify_ticket", verifyTicket);

		String postData = null;
		try {
			postData = om.writeValueAsString(objectNode);
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
		}

		final String url = "https://" + WeixinConfig.API_HOST + ":" + WeixinConfig.API_PORT + "/cgi-bin/component/api_component_token";
		String result = null;
		HttpClient httpClient = HttpClientPoolV3.getHttpClient(WeixinConfig.API_HOST, WeixinConfig.API_PORT);
		int rs = 0;
		try{
			PostMethod pm = new PostMethod(url);
			pm.setRequestEntity(new StringRequestEntity(postData,"","UTF-8"));
			rs = httpClient.executeMethod(pm);
			result = pm.getResponseBodyAsString();

		}catch(Exception e){
			e.printStackTrace();
		}
		logger.debug("请求获取第三方平台的ACCESS_TOKEN，发送数据[" + postData + "]，返回结果是:" + rs + ",返回数据是:" + result);
		if(result == null){
			return null;
		}
		JsonNode json = null;
		try {
			json = om.readTree(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(json == null){
			logger.error("无法解析获取第三方平台的ACCESS_TOKEN的返回结果:" + result);
			return null;
		}
		accessToken = json.get("component_access_token").asText();
		int expire = json.get("expires_in").asInt();
		if(StringUtils.isBlank(accessToken)){
			logger.error("无法获取第三方平台的ACCESS_TOKEN,返回结果:" + result);
			return null;
		}
		if(expire <= 0){
			logger.error("无法获取第三方平台的ACCESS_TOKEN过期时间，设置为7000秒");
			expire = 7000;
		} else {
			//减去100秒超时，以防止出现空档
			expire -= 100;
		}
		//放入缓存
		String tableName = WEIXIN_PLATFORM_CACHE_KEY + "#" + ownerId;
		logger.debug("存储[ownerId=" + ownerId + "]的微信第三方平台ACCESS_TOKEN:" + accessToken + "到缓存表[" + tableName + "]的键:" + ACCESS_TOKEN_CACHE_KEY + "，超时:" + expire);
		try {
			centerDataService.setHmValue(tableName, ACCESS_TOKEN_CACHE_KEY, accessToken, expire);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return accessToken;
	}

	private String _getAccessTokenFromCache(long ownerId) {
		String tableName = WEIXIN_PLATFORM_CACHE_KEY + "#" + ownerId;
		String value = centerDataService.getHmValue(tableName, ACCESS_TOKEN_CACHE_KEY);
		logger.debug("从缓存表[" + tableName + "]中获取[" + ACCESS_TOKEN_CACHE_KEY + "]的值是:" + value);
		return value;
	}

	/**
	 * 返回微信第三方公众平台的pre_auth_code
	 */
	@Override
	public String getPreAuthCode(long ownerId) {

		//先获取缓存中的accessToken
		String preAuthCode = _getPreAuthCodeFromCache(ownerId);
		if(preAuthCode != null){
			return preAuthCode;
		}

		final String accessToken = this.getPlatformAccessToken(ownerId);
		if(accessToken == null){
			logger.error("无法获取ownerId=" + ownerId + "的accessToken");
			return null;
		}
		WeixinPlatformInfo weixinPlatformInfo  = this.getWeixinPlatformInfo(ownerId);
		if(weixinPlatformInfo == null){
			logger.error("系统中没有合法的第三方平台帐号配置");
			return null;
		}

		ObjectMapper om = JsonUtils.getInstance();
		ObjectNode objectNode = om.createObjectNode();
		objectNode.put("component_appid", weixinPlatformInfo.appId);
		String postData = null;
		try {
			postData = om.writeValueAsString(objectNode);
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
		}

		final String url = "https://" + WeixinConfig.API_HOST + ":" + WeixinConfig.API_PORT + "/cgi-bin/component/api_create_preauthcode?component_access_token=" + accessToken;
		int rs = 0;
		String result = null;
		HttpClient httpClient = HttpClientPoolV3.getHttpClient(WeixinConfig.API_HOST, WeixinConfig.API_PORT);

		try{
			PostMethod pm = new PostMethod(url);
			pm.setRequestEntity(new StringRequestEntity(postData,"","UTF-8"));
			rs = httpClient.executeMethod(pm);
			result = pm.getResponseBodyAsString();

		}catch(Exception e){
			e.printStackTrace();
		}
		logger.debug("请求获取第三方平台的PREAUTH_CODE，发送数据[" + postData + "]，返回结果是:" + rs + ",返回数据是:" + result);
		if(result == null){
			return null;
		}
		JsonNode json = null;
		try {
			json = om.readTree(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(json == null){
			logger.error("无法解析获取第三方平台的PREAUTH_CODE的返回结果:" + result);
			return null;
		}
		preAuthCode = json.get("pre_auth_code").asText();
		int expire = json.get("expires_in").asInt();
		if(StringUtils.isBlank(accessToken)){
			logger.error("无法获取第三方平台的PREAUTH_CODE,返回结果:" + result);
			return null;
		}
		if(expire <= 0){
			logger.error("无法获取第三方平台的ACCESS_TOKEN过期时间，设置为550秒");
			expire = 550;
		} else {
			//减去100秒超时，以防止出现空档
			expire -= 100;
		}
		//放入缓存
		String tableName = WEIXIN_PLATFORM_CACHE_KEY + "#" + ownerId;
		logger.debug("存储[ownerId=" + ownerId + "]的微信第三方平台PREAUTH_CODE:" + preAuthCode + "到缓存表[" + tableName + "]的键:" + PREAUTH_CODE_CACHE_KEY + "，超时:" + expire);
		try {
			centerDataService.setHmValue(tableName, PREAUTH_CODE_CACHE_KEY, preAuthCode, expire);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return preAuthCode;	
	}

	private String _getPreAuthCodeFromCache(long ownerId) {
		String tableName = WEIXIN_PLATFORM_CACHE_KEY + "#" + ownerId;
		String value = centerDataService.getHmValue(tableName, PREAUTH_CODE_CACHE_KEY);
		logger.debug("从缓存表[" + tableName + "]中获取[" + PREAUTH_CODE_CACHE_KEY + "]的值是:" + value);
		return value;
	}

	/**
	 * 处理来自微信的第三方校验令牌，并存放到缓存中
	 * @throws Exception 
	 * 
	 */
	@Override
	public void receiveAuthMessage(String cryptText, long ownerId) throws Exception{
		String text = this.decrypt(cryptText, ownerId);
		if(text == null){
			logger.error("无法对授权信息[" + cryptText + "]解密");
			return;
		} 
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		StringReader sr = new StringReader(text);
		InputSource is = new InputSource(sr);
		Document document = db.parse(is);

		Element root = document.getDocumentElement();
		NodeList nodelist1 = root.getElementsByTagName("ComponentVerifyTicket");
		if(nodelist1 != null && nodelist1.getLength() > 0){
			//这是一个发送VERIFY_TICKET的请求
			String ticket = StringFormat.stripCdata(nodelist1.item(0).getTextContent());
			if(ticket == null){
				logger.error("获取到的授权令牌为空");
				return;
			}
			String tableName = WEIXIN_PLATFORM_CACHE_KEY + "#" + ownerId;
			logger.debug("存储[ownerId=" + ownerId + "]的微信第三方平台校验令牌:" + ticket + "到缓存表[" + tableName + "]的键:" + VERIFY_TICKET_CACHE_KEY);
			centerDataService.setHmValue(tableName, VERIFY_TICKET_CACHE_KEY, ticket, VERIFY_TOKEN_TTL);
			/*//存放到数据库
			Config config = new Config();
			config.setOwnerId(ownerId);
			config.setConfigName("WEIXIN_PLATFORM_VERIFY_TICKET");
			config.setConfigValue(ticket);
			configService.insert(config);*/
		} else {
			//其他为授权成功或取消授权的通知
			NodeList nodelist2 = root.getElementsByTagName("InfoType");
			if(nodelist2 == null || nodelist2.getLength() < 1){
				logger.warn("无法识别的消息:" + text);
				return;
			}

			String infoType = StringFormat.stripCdata(nodelist2.item(0).getTextContent());

			if(infoType.equalsIgnoreCase("unauthorized")){
				String clientAppId = StringFormat.stripCdata(root.getElementsByTagName("AuthorizerAppid").item(0).getTextContent());
				UserCriteria userCriteria = new UserCriteria(ownerId);
				userCriteria.setAuthKey(clientAppId);
				userCriteria.setExtraStatus(UserStatus.authorized.getId());
				List<User> partnerList = partnerService.list(userCriteria);
				if(partnerList == null || partnerList.size() < 1){
					logger.info("根据客户公众号[" + clientAppId + "]找不到任何已授权合作伙伴");
					return;
				}
				User partner = partnerList.get(0);
				partner.setExtraStatus(UserStatus.normal.getId());
				partnerService.updateNoNull(partner);
				logger.debug("将取消授权的合作伙伴[" + partner.getUuid() + "]的扩展状态改为普通:" + partner.getExtraStatus());
			} else if(infoType.equalsIgnoreCase("authorized")){
				String clientAppId = StringFormat.stripCdata(root.getElementsByTagName("AuthorizerAppid").item(0).getTextContent());
				String authCode = StringFormat.stripCdata(root.getElementsByTagName("AuthorizationCode").item(0).getTextContent());
				long expireIn = NumericUtils.parseLong(StringFormat.stripCdata(root.getElementsByTagName("AuthorizationCodeExpiredTime").item(0).getTextContent()));
				UserCriteria userCriteria = new UserCriteria(ownerId);
				userCriteria.setAuthKey(clientAppId);
				List<User> partnerList = partnerService.list(userCriteria);
				if(partnerList == null || partnerList.size() < 1){
					logger.info("根据客户公众号[" + clientAppId + "]找不到任何合作伙伴，设置该APPID到:" + WEIXIN_CLIENT_APPID_MAP);
					WeixinClient weixinClient = new WeixinClient();
					weixinClient.appId = clientAppId;
					weixinClient.authorizeCode = authCode;
					weixinClient.authorizeCodeExpireTime = new Date(expireIn * 1000);
					centerDataService.setHmValue(WEIXIN_CLIENT_APPID_MAP, clientAppId, weixinClient, DEFAULT_TTL);
					return;
				}
				User partner = partnerList.get(0);
				partner.setExtraStatus(UserStatus.authorized.getId());
				partnerService.updateNoNull(partner);
				logger.debug("将授权的合作伙伴[" + partner.getUuid() + "]的扩展状态改为已授权:" + partner.getExtraStatus());
			} 


		}


	}

	/**
	 * 获取系统缓存中的微信第三方平台校验令牌verify_token
	 */
	@Override
	public String getVerifyTicket(long ownerId){
		String tableName = WEIXIN_PLATFORM_CACHE_KEY + "#" + ownerId;
		String o = centerDataService.getHmValue(tableName, VERIFY_TICKET_CACHE_KEY);
		if(o != null){
			logger.debug("从缓存中返回[ownerId=" + ownerId + "]的verifyTicket:" + o);
			return o;
		}

		return o;

	}

	@Override
	public String getPlatformAppId(long ownerId){
		WeixinPlatformInfo weixinPlatformInfo = getWeixinPlatformInfo(ownerId);
		if(weixinPlatformInfo == null){
			return null;
		}
		return weixinPlatformInfo.appId;
	}


	private WeixinPlatformInfo getWeixinPlatformInfo(long ownerId){
		WeixinPlatformInfo weixinPlatformInfo = null;
		if(weixinPlatformInfoCache != null && weixinPlatformInfoCache.size() > 0){
			weixinPlatformInfo = weixinPlatformInfoCache.get(String.valueOf(ownerId));
		}
		if(weixinPlatformInfo == null){		
			weixinPlatformInfo = new WeixinPlatformInfo();
			String appId = configService.getValue("WEIXIN_PLATFORM_APP_ID", ownerId);
			if(appId != null){
				weixinPlatformInfo.appId = appId;
			} else {
				logger.info("找不到ownerId=" + ownerId + "的第三方公众平台WEIXIN_PLATFORM_APP_ID");
				return null;
			}

			String appSecret = configService.getValue("WEIXIN_PLATFORM_APP_SECRET", ownerId);
			if(appSecret != null){
				weixinPlatformInfo.appSecret = appSecret;
			} else {
				logger.error("找不到ownerId=" + ownerId + "的第三方公众平台WEIXIN_PLATFORM_APP_SECRET");
				return null;
			}
			//第三方平台的signKey设置为appToken,以与单用户系统兼容
			String signKey = configService.getValue("WEIXIN_PLATFORM_SIGN_KEY", ownerId);
			if(signKey != null){
				weixinPlatformInfo.appToken = signKey;
			} else {
				logger.error("找不到ownerId=" + ownerId + "的第三方公众平台WEIXIN_PLATFORM_SIGN_KEY");
				return null;
			}
			String cryptKey = configService.getValue("WEIXIN_PLATFORM_CRYPT_KEY", ownerId);
			if(cryptKey != null){
				weixinPlatformInfo.cryptKey = cryptKey;
			} else {
				logger.error("找不到ownerId=" + ownerId + "的第三方公众平台WEIXIN_PLATFORM_SIGN_KEY");
				return null;
			}	
			synchronized(this){
				weixinPlatformInfoCache.put(String.valueOf(ownerId), weixinPlatformInfo);
			}
			logger.debug("把ownerId=" + ownerId + "的第三方公众平台信息:" + weixinPlatformInfo + "放入缓存");


		} else {
			logger.debug("从缓存中获取到ownerId=" + ownerId + "的第三方公众平台信息:" + weixinPlatformInfo);
		}	

		return weixinPlatformInfo;

	}

	/**
	 * 获取单平台模式
	 * 
	 * 
	 * @param ownerId
	 * @return
	 */
	@Override
	public WeixinPlatformInfo getSingleWeixinPlatformInfo(long ownerId){
		WeixinPlatformInfo weixinPlatformInfo = null;
		if(weixinSingleInfoCache != null && weixinSingleInfoCache.size() > 0){
			weixinPlatformInfo = weixinSingleInfoCache.get(String.valueOf(ownerId));
		}
		if(weixinPlatformInfo == null){		
			weixinPlatformInfo = new WeixinPlatformInfo();
			String appId = configService.getValue("WEIXIN_APP_ID", ownerId);
			if(appId != null){
				weixinPlatformInfo.appId = appId;
			} else {
				logger.error("找不到ownerId=" + ownerId + "的公众号配置WEIXIN_APP_ID");
				return null;
			}

			String appSecret = configService.getValue("WEIXIN_APP_SECRET", ownerId);
			if(appSecret != null){
				weixinPlatformInfo.appSecret = appSecret;
			} else {
				logger.error("找不到ownerId=" + ownerId + "的公众号配置WEIXIN_APP_SECRET");
				return null;
			}
			String appToken = configService.getValue("WEIXIN_APP_TOKEN", ownerId);
			if(appToken != null){
				weixinPlatformInfo.appToken = appToken;
			} else {
				logger.error("找不到ownerId=" + ownerId + "的公众号配置WEIXIN_APP_TOKEN");
				return null;
			}
			String appCryptKey = configService.getValue("WEIXIN_APP_CRYPT_KEY", ownerId);
			if(appCryptKey != null){
				weixinPlatformInfo.cryptKey = appCryptKey;
			} else {
				logger.warn("找不到ownerId=" + ownerId + "的公众号配置WEIXIN_APP_CRYPT_KEY");
			}
			String payMechId = configService.getValue("WEIXIN_PAY_MECH_ID", ownerId);
			if(payMechId != null){
				weixinPlatformInfo.payMechId = payMechId;
			} else {
				logger.warn("找不到ownerId=" + ownerId + "的公众号配置WEIXIN_PAY_MECH_ID");
			}	

			String payKey = configService.getValue("WEIXIN_PAY_KEY", ownerId);
			if(payKey != null){
				weixinPlatformInfo.payKey = payKey;
			} else {
				logger.warn("找不到ownerId=" + ownerId + "的第三方公众平台WEIXIN_PLATFORM_SIGN_KEY");
			}	
			synchronized(this){
				weixinSingleInfoCache.put(String.valueOf(ownerId), weixinPlatformInfo);
			}
			logger.debug("把ownerId=" + ownerId + "的微信公众号配置:" + weixinPlatformInfo + "放入缓存");


		} else {
			logger.debug("从缓存中获取到ownerId=" + ownerId + "的公众号配置:" + weixinPlatformInfo);
		}	

		return weixinPlatformInfo;

	}


	/**
	 * 对微信发来的消息进行解密
	 */
	@Override
	public String decrypt(String message, long ownerId) throws Exception {

		if(message == null){
			logger.warn("尝试解密的微信消息为空");
			return null;
		}
		if(message.indexOf("Encrypt") < 0){
			logger.debug("尝试解密的微信消息没有Encrypt字符串，视为明文，不解密直接返回");
			return message;
		}
		WeixinPlatformInfo weixinPlatformInfo = getWeixinPlatformInfo(ownerId);
		if(weixinPlatformInfo == null){
			logger.warn("系统中没有合法的第三方平台帐号配置,查找单平台配置");
			weixinPlatformInfo = this.getSingleWeixinPlatformInfo(ownerId);
		}
		if(weixinPlatformInfo == null){
			logger.error("系统中没有合法的微信第三方平台和单平台配置");
			return null;
		}
		if(weixinPlatformInfo.cryptKey == null){
			logger.debug("系统中没有公众号加密密钥，使用明文模式");
			return message;
		}
		WXBizMsgCrypt pc = new WXBizMsgCrypt(weixinPlatformInfo.appToken, weixinPlatformInfo.cryptKey, weixinPlatformInfo.appId);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		StringReader sr = new StringReader(message);
		InputSource is = new InputSource(sr);
		Document document = db.parse(is);

		Element root = document.getDocumentElement();
		NodeList nodelist1 = root.getElementsByTagName("Encrypt");
		String encrypt = nodelist1.item(0).getTextContent();
		String fromXML = String.format(xmlFormat, encrypt);

		//对于正常消息，还需要有一些附加数据
		NodeList nodelist2 = root.getElementsByTagName("MsgSignature");
		if(nodelist2 == null || nodelist2.getLength() < 1){
			return pc.decryptSimpleMsg(fromXML);
		}
		String msgSignature = nodelist2.item(0).getTextContent();
		String timestamp = root.getElementsByTagName("CreateTime").item(0).getTextContent();
		String nonce = root.getElementsByTagName("nonce").item(0).getTextContent();

		return pc.decryptMsg(msgSignature, timestamp, nonce, fromXML);
	}

	@Override
	public String encryptMsg(String message, String timeStamp, String nonce, long ownerId) throws Exception {

		if(message == null){
			logger.warn("尝试解密的微信消息为空");
			return null;
		}
		WeixinPlatformInfo weixinPlatformInfo = getWeixinPlatformInfo(ownerId);
		if(weixinPlatformInfo == null){
			logger.warn("系统中没有合法的第三方平台帐号配置,查找单平台公众号配置");
			weixinPlatformInfo = getSingleWeixinPlatformInfo(ownerId);
		} 
		if(weixinPlatformInfo == null){
			logger.error("系统中没有合法的第三方平台帐号配置或单平台微信公众号配置，返回空");
			return null;
		} 

		String returnMessage = null;
		if(weixinPlatformInfo.cryptKey != null){
			WXBizMsgCrypt pc = new WXBizMsgCrypt(weixinPlatformInfo.appToken, weixinPlatformInfo.cryptKey, weixinPlatformInfo.appId);

			String encryptedMsg =  pc.encryptMsg(message, timeStamp, nonce);
			logger.debug("对消息:" + message + ",timeStamp=" + timeStamp + ",nonce=" + nonce + "进行加密后的结果:" + encryptedMsg);
			returnMessage = encryptedMsg;
		} else {
			//明文模式
			logger.debug("当前系统未配置加密密钥，使用明文模式");
			returnMessage = message;
		}
		/*		//解密测试
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		StringReader sr = new StringReader(encryptedMsg);
		InputSource is = new InputSource(sr);
		Document document = db.parse(is);

		Element root = document.getDocumentElement();
		NodeList nodelist1 = root.getElementsByTagName("Encrypt");
		NodeList nodelist2 = root.getElementsByTagName("MsgSignature");

		String encrypt = nodelist1.item(0).getTextContent();
		String msgSignature = nodelist2.item(0).getTextContent();

		String format = "<xml><ToUserName><![CDATA[toUser]]></ToUserName><Encrypt><![CDATA[%1$s]]></Encrypt></xml>";
		String fromXML = String.format(format, encrypt);

		//
		// 公众平台发送消息给第三方，第三方处理
		//

		// 第三方收到公众号平台发送的消息
		String result2 = pc.decryptMsg(msgSignature, timeStamp, nonce, fromXML);
		System.out.println("解密后明文: " + result2);*/

		return returnMessage;
	}
	@Override
	public String getOpenIdByCode(String weixinCode, long sitePartnerId, long ownerId) {
		if(weixinCode == null){
			logger.error("无法通过code获取用户信息，因为code为空");
			return null;
		}

		String openId = null;
		String authUrl = null;

		if(sitePartnerId > 0){
			//使用第三方平台工作模式


			String clientAppId = this.getClientAppId(sitePartnerId);
			if(StringUtils.isBlank(clientAppId)){
				logger.error("合作伙伴:" + sitePartnerId + "没有authKey即appId");
				return null;
			}

			String table = WEIXIN_CLIENT_APPID_MAP + "#" + clientAppId;

			authUrl = WeixinConfig.API_PREFIX + "/sns/oauth2/component/access_token?appid="+ clientAppId +"&code="+weixinCode+"&grant_type=authorization_code&component_appid=" + this.getPlatformAppId(ownerId) + "&component_access_token=" + this.getPlatformAccessToken(ownerId);
			String result = HttpUtils.sendData(authUrl);
			logger.debug("第三方平台方式请求客户端认证accessToken[url=" + authUrl + "],返回:" + result);
			JsonNode jsonNode = null;
			try {
				jsonNode = om.readTree(result);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			if(jsonNode == null){
				logger.error("无法解析第三方平台方式请求客户端认证accessToken[url=" + authUrl + "]的返回结果:" + result);
				return null;
			}
			openId = jsonNode.path("openid").asText();	
			if(openId == null){
				return null;			
			}
			String authAccessToken = jsonNode.path("access_token").asText();	
			int expire = jsonNode.path("expires_in").asInt();
			String refreshToken = jsonNode.path("refresh_token").asText();	
			if(StringUtils.isBlank(authAccessToken)){
				return null;
			}
			try {
				centerDataService.setHmValue(table, AUTH_ACCESS_TOKEN_CACHE_KEY, authAccessToken, expire - 100);
				centerDataService.setHmValue(table, AUTH_REFRESH_TOKEN_CACHE_KEY, refreshToken, 0);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return openId;
		} else {
			//单平台模式

			WeixinPlatformInfo singleWeixinPlatformInfo= this.getSingleWeixinPlatformInfo(ownerId);
			if(singleWeixinPlatformInfo == null){
				logger.error("系统未配置单平台微信公众号数据");
				return null;
			}
			authUrl = WeixinConfig.API_PREFIX + "/sns/oauth2/access_token?appid="+ singleWeixinPlatformInfo.appId +"&secret="+ singleWeixinPlatformInfo.appSecret +"&code="+weixinCode+"&grant_type=authorization_code";
			String authResult = null;
			int errorCode = 0;
			authResult = HttpUtils.sendData(authUrl);
			logger.debug("根据code=" + weixinCode + "获取用户信息[" + authUrl + "]，结果:" + authResult);
			if(authResult == null){
				logger.error("根据code=" + weixinCode + "获取用户信息[" + authUrl + "]得到的结果是空");
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
		}
		/*}
		catch(Exception e){
			logger.error("无法解析微信返回:" + ExceptionUtils.getFullStackTrace(e));
		}*/
	}


	/**
	 * 当客户成功授权后，更新客户信息
	 */
	@Override
	public int updateClientInfo(User partner, String authCode, int expire) {
		//获取或更新用户信息
		return this.updateClientAccessToken(partner, authCode, expire);
	}

	/**
	 * 根据客户的授权码auth_code,来获取用户的信息
	 */
	@Override 
	public int updateClientAccessToken(User partner, String authCode, int expire) {
		//先根据authCode查找系统缓存，看是否能找到authCode对应的appId
		long ownerId = partner.getOwnerId();
		final String accessToken = this.getPlatformAccessToken(ownerId);
		if(accessToken == null){
			logger.error("无法获取ownerId=" + ownerId + "的accessToken");
			return EisError.systemBusy.id;
		}

		WeixinPlatformInfo weixinPlatformInfo  = this.getWeixinPlatformInfo(ownerId);
		if(weixinPlatformInfo == null){
			logger.error("系统中没有合法的第三方平台帐号配置");
			return EisError.systemDataError.id;
		}

		ObjectNode objectNode = om.createObjectNode();
		objectNode.put("component_appid", weixinPlatformInfo.appId);
		objectNode.put("authorization_code", authCode);

		String postData = null;
		try {
			postData = om.writeValueAsString(objectNode);
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
		}

		String url = "https://" + WeixinConfig.API_HOST + ":" + WeixinConfig.API_PORT + "/cgi-bin/component/api_query_auth?component_access_token=" + accessToken;
		int rs = 0;
		String result = null;
		HttpClient httpClient = HttpClientPoolV3.getHttpClient(WeixinConfig.API_HOST, WeixinConfig.API_PORT);

		try{
			PostMethod pm = new PostMethod(url);
			pm.setRequestEntity(new StringRequestEntity(postData,"","UTF-8"));
			rs = httpClient.executeMethod(pm);
			result = pm.getResponseBodyAsString();

		}catch(Exception e){
			e.printStackTrace();
		}
		logger.debug("根据授权码[" + authCode + "]请求获取公众号客户信息，发送数据[" + postData + "]，返回结果是:" + rs + ",返回数据是:" + result);
		if(result == null){
			return EisError.systemBusy.id;
		}
		JsonNode json = null;
		try {
			json = om.readTree(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(json == null){
			logger.error("无法解析获取第三方平台的PREAUTH_CODE的返回结果:" + result);
			return EisError.systemBusy.id;
		}
		String clientAppId = json.path("authorization_info").get("authorizer_appid").asText();
		String clientAccessToken = json.path("authorization_info").get("authorizer_access_token").asText();
		int clientAccessTokenExpire = json.path("authorization_info").get("expires_in").asInt();
		String clientRefreshToken = json.path("authorization_info").get("authorizer_refresh_token").asText();
		logger.info("根据授权码[" + authCode + "]请求获取公众号客户信息[clientAppId=" + clientAppId + ",clientAccessToken=" + clientAccessToken + ",clientAccessTokenExpire=" + clientAccessTokenExpire + ",clientRefreshToken=" + clientRefreshToken);

		UserCriteria userCriteria = new UserCriteria(ownerId);
		userCriteria.setAuthKey(clientAppId);
		List<User> existUserList = partnerService.list(userCriteria);
		if(existUserList != null && existUserList.size() > 0){
			logger.info("根据客户appId=" + clientAppId + "]查询到的合作方已存在" + existUserList.size() + "个");
			if(existUserList.get(0).getUuid() != partner.getUuid()){
				logger.info("根据客户appId=" + clientAppId + "]查询到的合作方已存在并且不是当前用户:" + partner.getUuid() + ",而是:" + existUserList.get(0).getUuid());
				return EisError.dataDuplicate.id;
			}
		}
		//进一步获取授权方的详细信息如名称、头像
		url = "https://" + WeixinConfig.API_HOST + ":" + WeixinConfig.API_PORT + "/cgi-bin/component/api_get_authorizer_info?component_access_token=" + accessToken;

		objectNode = om.createObjectNode();
		objectNode.put("component_appid", weixinPlatformInfo.appId);
		objectNode.put("authorizer_appid", clientAppId);

		postData = null;
		try {
			postData = om.writeValueAsString(objectNode);
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
		}

		result = HttpUtils.postData(url, postData);

		logger.debug("获取公众号信息，发送数据[" + postData + "]到:" + url + ",返回结果是:" + rs + ",返回数据是:" + result);
		if(result != null){
			json = null;
			try {
				json = om.readTree(result);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(json != null){
				String nickName = json.path("authorizer_info").get("nick_name").asText();
				partner.setAuthKey(clientAppId);
				if(StringUtils.isNotBlank(nickName)){
					partner.setNickName(nickName);
					SiteThemeRelationCriteria siteThemeRelationCriteria = new SiteThemeRelationCriteria(ownerId);
					siteThemeRelationCriteria.setUuid(partner.getUuid());
					siteThemeRelationCriteria.setCurrentStatus(BasicStatus.normal.id);
					SiteThemeRelation siteThemeRelation = siteThemeRelationService.select(siteThemeRelationCriteria);
					if(siteThemeRelation != null){
						siteThemeRelation.setSiteName(nickName);
						siteThemeRelationService.updateForUuid(siteThemeRelation);
					} else {
						logger.info("找不到公众号客户[" + partner.getUuid() + "]对应的siteThemeRelation，新建");
						siteThemeRelation = new SiteThemeRelation(ownerId);
						siteThemeRelation.setUuid(partner.getUuid());
						siteThemeRelation.setSiteName(nickName);
						siteThemeRelation.setHostCode(partner.getAuthKey());
						siteThemeRelation.setCurrentStatus(BasicStatus.normal.id);
						siteThemeRelationService.insert(siteThemeRelation);
					}
				}
				String companyName = json.path("authorizer_info").get("principal_name").asText();
				if(StringUtils.isNotBlank(companyName)){
					partner.setExtraValue(DataName.userCompany.toString(), companyName);

				}
			}
		}
		partner.setExtraValue("clientRefreshToken", clientRefreshToken);
		rs = partnerService.update(partner);

		if(rs != 1){
			logger.error("无法更新合作伙伴[" + partner.getUuid() + "]的第三方授权信息");
		}
		//把数据放入缓存
		String table = WEIXIN_CLIENT_APPID_MAP + "#" + clientAppId;
		try {
			centerDataService.setHmValue(table, ACCESS_TOKEN_CACHE_KEY, clientAccessToken, clientAccessTokenExpire - 100);
			centerDataService.setHmValue(table, REFRESH_TOKEN_CACHE_KEY, clientRefreshToken, 0);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return OperateResult.success.id;
	}

	@Override
	public String getClientAccessToken(long sitePartnerId){
		User partner = partnerService.select(sitePartnerId);
		if(partner == null){
			logger.error("在系统中找不到指定的合作伙伴:" + sitePartnerId);
			return null;
		}
		String clientAppId = partner.getAuthKey();
		if(StringUtils.isBlank(clientAppId)){
			logger.error("合作伙伴:" + sitePartnerId + "没有authKey即appId");
			return null;
		}
		String table = WEIXIN_CLIENT_APPID_MAP + "#" + clientAppId;
		String accessToken = centerDataService.getHmValue(table, ACCESS_TOKEN_CACHE_KEY);
		if(accessToken != null){
			logger.debug("从缓存中返回了[uuid=" + sitePartnerId + ",appId=" + clientAppId + "]的accessToken:" + accessToken);
			return accessToken;
		}
		//否则使用refreshToken刷新获取accessToken
		String refreshToken = centerDataService.getHmValue(table, REFRESH_TOKEN_CACHE_KEY);
		if(refreshToken == null){
			refreshToken = partner.getExtraValue("clientRefreshToken");
		}
		if(refreshToken == null){
			logger.error("无法获取[uuid=" + sitePartnerId + ",appId=" + clientAppId + "]的refreshToken，需要重新授权");
			return null;
		}
		return refreshAccessToken(clientAppId, refreshToken, partner.getOwnerId());

	}



	/**
	 * 获取微信卡券的JSAPI访问ticket
	 */
	@Override
	public String getCouponToken(String clientAppId, long sistePartnerId, long ownerId){
		logger.debug("当前获取微信卡券令牌[clientAppId=" + clientAppId + ",sitePartnerId=" + sistePartnerId + ", ownerId=" + ownerId);
		String accessToken = null;
		String tableName = clientAppId == null ? WEIXIN_PLATFORM_CACHE_KEY + "#" + ownerId : WEIXIN_CLIENT_APPID_MAP + "#" + clientAppId;

		String cacheValue = centerDataService.getHmValue(tableName, COUPON_TOKEN_CACHE_KEY);
		logger.debug("从缓存表[" + tableName + "]中获取[" + COUPON_TOKEN_CACHE_KEY + "]的值是:" + cacheValue);
		if(cacheValue != null){
			return cacheValue;
		}
		if(clientAppId != null){			
			accessToken = this.getClientAccessToken(sistePartnerId);
		} else {
			accessToken = this.getSingleAccessToken(ownerId);
		}
		String url  = API_PREFIX + "/cgi-bin/ticket/getticket?access_token=" + accessToken + "&type=wx_card";
		logger.debug("获取微信卡券的JSAPI访问ticket的URL：" + url);
		String newCouponToken = null;
		int expire = 0;

		try	{
			String result = HttpUtils.sendData(url);
			newCouponToken=om.readTree(result).get("ticket").asText();
			expire=om.readTree(result).get("expires_in").asInt();
		}
		catch(Exception e){
			logger.info("从微信服务器取coupon token出错");
			return null;
		}
		logger.info("更换[appId=" + clientAppId + ",partnerId=" + sistePartnerId + ",ownerId=" + ownerId + "]的新couponToken为"+newCouponToken);
		try {
			centerDataService.setHmValue(tableName, COUPON_TOKEN_CACHE_KEY, newCouponToken, expire );
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newCouponToken;


	}


	/**
	 * @version 2
	 * 无法在生成卡券时使用指定的code即卡号，这样会造成JS领取时出现参数错误
	 * 
	 * 把系统中的优惠券数据Coupon，转换为微信的卡券CARD
	 * 并将转换后的微信卡券转换为JSON数据，放入系统优惠券中的messageId
	 * 该数据只用于动态转换，不需要进行存储
	 */
	@Override
	public void convertToWeixinCoupon(Coupon coupon, long sitePartnerId, String openId, String timeStamp) {
		if(coupon.getOwnerId() < 1){
			logger.error("尝试签名的优惠券没有ownerId");
			return;
		}
		if(timeStamp == null){
			timeStamp = String.valueOf(new Date().getTime() / 1000);
		}
		String clientAppId = null;
		if(sitePartnerId > 0){
			clientAppId = this.getClientAppId(sitePartnerId);

		}
		String couponToken = getCouponToken(clientAppId, sitePartnerId, coupon.getOwnerId());
		String weixinCouponId = coupon.getCouponCode();
		String couponSn = null;//不能正确的使用code，必须为空,NetSnake,2017-03-01			coupon.getCouponSerialNumber();
		String  nonce_str  = DigestUtils.md5Hex(timeStamp);

		coupon.setContent(nonce_str);
		//String appId = getAppId(coupon.getOwnerId());
		List<String> values = new ArrayList<String>();

		//values.add(appId);
		values.add(couponToken);
		values.add(timeStamp);
		values.add(nonce_str);
		values.add(weixinCouponId);

		/*if(openId != null){
			values.add(openId);
		}*/
		Collections.sort(values);

		String signSource = "";
		for(String value : values){
			signSource += value;
		}
		String sign = DigestUtils.sha1Hex(signSource);
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
		logger.info("使用微信卡券[" + coupon.getCouponCode() + "]为用户生成的卡号JSON串: " + jsonString);
		coupon.setMessageId(jsonString);
		return ;		
	}
	@Override
	public int addCardStock(long sitePartnerId, long ownerId, String weixinCardId, int amount){

		String accessToken =  null;
		if(sitePartnerId > 0){
			accessToken = this.getClientAccessToken(sitePartnerId);
		} else {
			accessToken = this.getSingleAccessToken(ownerId);
		}
		String url = API_PREFIX + "/card/modifystock?access_token=" + accessToken;
		HttpClient httpClient = HttpClientPoolV3.getHttpClient(API_HOST, API_PORT);
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

	@Override
	public String getSingleAccessToken(long ownerId){



		String table = WEIXIN_CLIENT_APPID_MAP + "#" + ownerId;
		WeixinPlatformInfo weixinPlatformInfo = this.getSingleWeixinPlatformInfo(ownerId);
		if(weixinPlatformInfo == null){
			logger.error("找不到[ownerId=" + ownerId + "]的单平台微信配置数据");
			return null;
		}
		final String getAccessTokenUrl= API_PREFIX + "/cgi-bin/token?grant_type=client_credential&appid="+ weixinPlatformInfo.appId +"&"+"secret="+weixinPlatformInfo.appSecret;


		String singleAccessToken= centerDataService.getHmValue(table, ACCESS_TOKEN_CACHE_KEY);
		if (singleAccessToken != null){	
			return singleAccessToken;
		}
		//重新获取
		String result= HttpUtils.sendData(getAccessTokenUrl);
		logger.debug("请求单平台模式acessToken返回的数据是:" + result);
		try{
			JsonNode jsonNode = om.readTree(result);
			singleAccessToken = jsonNode.get("access_token").asText();
			int expire = om.readTree(result).get("expires_in").asInt() - 100;
			centerDataService.setHmValue(table, ACCESS_TOKEN_CACHE_KEY, singleAccessToken, expire);
		}catch(Exception e){
			e.printStackTrace();
		}
		logger.debug("更换单平台[ownerId=" + ownerId + "]的access token为"+ singleAccessToken);
		return singleAccessToken;
	}	



	@Override
	public String makeJsSignature(long timeStamp,String currentUrl, String nonceStr, long ownerId, int callCount) {
		long t1 = new Date().getTime();
		String hostName = HttpUtils.parseHostAndPort(currentUrl)[0];
		String accessToken = null;
		User partner = this.getPartnerByHost(hostName, ownerId);
		if(partner == null){
			//单平台模式
			accessToken = this.getSingleAccessToken(ownerId);
			logger.debug("根据主机[" + hostName + "]找不到客户端公众号,获取单平台的acessToken:" + accessToken);
		} else {
			//第三方平台模式
			accessToken = this.getClientAccessToken(partner.getUuid());
			logger.debug("根据主机[" + hostName + "]找到的客户端公众号是:" + partner.getUuid() + ",,获取该客户公众号的acessToken:" + accessToken);

		}
		String url= API_PREFIX + "/cgi-bin/ticket/getticket?access_token="+accessToken+"&type=jsapi";
		String result="";
		String jsapi_ticket="";
		String sha1Str="";
		try{
			result= HttpUtils.sendData(url);
		}	catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(result == null){
			logger.error("无法连接到微信服务器");
		}  
		logger.debug("从微信请求生成客户端JS签名ticket的结果:" + result + ",耗时:" + (new Date().getTime() - t1) + "毫秒");
		try{
			JsonNode json = om.readTree(result);
			int code = json.get("errcode").asInt();
			if(code == WeixinError.INVALID_ACCESS_TOKEN){
				//错误的accessToken，先删除该accessToken，再重新发起调用
				this.deleteAccessToken(partner == null ? 0 : partner.getUuid(), ownerId);
				if(callCount == 0){
					logger.info("错误的accessToken，删除后重新调用本方法");
					return this.makeJsSignature(timeStamp, currentUrl, nonceStr, ownerId, 1);
				}
				logger.info("错误的accessToken，删除，但本方法已重新调用，放弃");
				return null;
			}
			jsapi_ticket=json.get("ticket").asText();
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
		sha1Str = StringFormat.getFormattedText(messageDigest.digest());
		logger.info("使用数据[ticket=" + jsapi_ticket + ",nonceStr=" + nonceStr + ",timestamp=" + timeStamp/ 1000 + ",currentUrl=" + currentUrl + "]生成签名:" +sha1Str);


		return 	sha1Str;
	}


	@Override
	public String getClientAccessTokenByHost(String host, long ownerId){
		User partner = getPartnerByHost(host,ownerId);
		if(partner == null){
			return null;
		}
		return this.getClientAccessToken(partner.getUuid());
	}

	@Override
	public User getPartnerByHost(String host, long ownerId){
		String hostPrefix = host.split("\\.")[0];
		SiteThemeRelationCriteria siteThemeRelationCriteria = new SiteThemeRelationCriteria(ownerId);
		siteThemeRelationCriteria.setHostCode(hostPrefix);
		siteThemeRelationCriteria.setCurrentStatus(BasicStatus.normal.id);
		SiteThemeRelation siteThemeRelation = siteThemeRelationService.select(siteThemeRelationCriteria);
		logger.debug("根据主机前缀[" + hostPrefix + "]获得的siteThemeRelation是:" + siteThemeRelation);
		if(siteThemeRelation == null ){
			return null;
		}
		User partner = partnerService.select(siteThemeRelation.getUuid());
		logger.debug("根据siteThemeRelation配置获取[uuid=" + siteThemeRelation.getUuid() + "]的合作伙伴:" + partner);
		return partner;
	}

	/**
	 * 使用客户端授权时得到的refreshToken来重新获取accessToken
	 * @param refreshToken
	 * @return
	 */
	private String refreshAccessToken(String clientAppId, String refreshToken, long ownerId){

		WeixinPlatformInfo weixinPlatformInfo  = this.getWeixinPlatformInfo(ownerId);
		if(weixinPlatformInfo == null){
			logger.error("系统中没有合法的第三方平台帐号配置");
			return null;
		}
		ObjectMapper om = JsonUtils.getInstance();
		ObjectNode objectNode = om.createObjectNode();
		objectNode.put("component_appid", weixinPlatformInfo.appId);
		objectNode.put("authorizer_appid", clientAppId);
		objectNode.put("authorizer_refresh_token", refreshToken);

		String postData = null;
		try {
			postData = om.writeValueAsString(objectNode);
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
		}
		int rs = 0;
		String result = null;
		HttpClient httpClient = HttpClientPoolV3.getHttpClient(API_HOST, API_PORT);
		String url = "https://" + API_HOST + ":" + API_PORT + "/cgi-bin/component/api_authorizer_token?component_access_token=" + this.getPlatformAccessToken(ownerId);
		try{
			PostMethod pm = new PostMethod(url);
			pm.setRequestEntity(new StringRequestEntity(postData,"","UTF-8"));
			rs = httpClient.executeMethod(pm);
			result = pm.getResponseBodyAsString();

		}catch(Exception e){
			e.printStackTrace();
		}
		logger.debug("根据refreshToken[" + refreshToken + "]刷新公众号[" + clientAppId + "]的accessToken，发送数据[" + postData + "]，返回结果是:" + rs + ",返回数据是:" + result);
		if(result == null){
			return null;
		}
		JsonNode json = null;
		try {
			json = om.readTree(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(json == null){
			logger.error("无法解析获取第三方平台刷新客户公众号accessToken的返回结果:" + result);
			return null;
		}
		String clientAccessToken = json.get("authorizer_access_token").asText();
		String newRefreshToken = json.get("authorizer_refresh_token").asText();
		int clientAccessTokenExpire = json.get("expires_in").asInt();
		logger.info("根据refreshToken[" + refreshToken + "]刷新公众号[" + clientAppId + "]的accessToken,返回[clientAccessToken=" + clientAccessToken + ",clientAccessTokenExpire=" + clientAccessTokenExpire);
		String table = WEIXIN_CLIENT_APPID_MAP + "#" + clientAppId;
		try {
			centerDataService.setHmValue(table, ACCESS_TOKEN_CACHE_KEY, clientAccessToken, clientAccessTokenExpire - 100);
			if(newRefreshToken != null){
				centerDataService.setHmValue(table, REFRESH_TOKEN_CACHE_KEY, newRefreshToken, -1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clientAccessToken;
	}
	@Override
	public User createUser(String openId, String identify, long sitePartnerId, long ownerId) {

		if(StringUtils.isNotBlank(identify) &&  identify.indexOf("qrscene") > -1 ){
			identify = identify.replaceAll("qrscene_","");
		}

		if(ownerId <= 1){
			logger.error("无法创建前端用户，ownerId=0");
		}

		User frontUser = new User(ownerId);
		frontUser.setUserConfigMap(new HashMap<String,UserData>());
		//获取用户的更多信息
		frontUser.setAuthKey(openId);
		frontUser.setInviter(sitePartnerId);
		frontUser.setUserTypeId(UserTypes.frontUser.getId());
		frontUser.setUserExtraTypeId(UserExtraType.tencentUser.getId());
		frontUser.setUsername(openId);
		frontUser.setUserPassword(openId);
		frontUser.setCurrentStatus(UserStatus.normal.getId());
		frontUser.setCreateTime(new Date());
		if(StringUtils.isNotBlank(identify)){
			frontUser.setExtraValue(DataName.userInviteByCode.toString(), identify.trim());
			frontUser.setExtraValue(DataName.lastEventKey.toString(), identify.trim());

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
		updateUserInfo(frontUser, openId, sitePartnerId);

		int rs = 0;
		try {
			rs = frontUserService.insert(frontUser);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("使用[openId=" + openId + ",inviter=" + sitePartnerId + ",ownerId=" + ownerId + "]注册新用户:" + rs + ",新UUID=" + frontUser.getUuid());
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
	public void updateUserInfo(User frontUser, String openId, long sitePartnerId) {

		if(frontUser == null){
			logger.error("尝试更新的微信用户是空");
			return;
		}
		if(frontUser.getOwnerId() < 1){
			logger.error("尝试更新的微信用户ownerId为空");
			return;
		}
		if(frontUser.getExtraStatus() == 0){

			User weixinUser = getUserInfo(frontUser.getAuthKey(), sitePartnerId, frontUser.getOwnerId(), 0);

			if(weixinUser != null && weixinUser.getUserConfigMap() != null){
				frontUser.setGender(weixinUser.getGender());
				if(StringUtils.isNotBlank(weixinUser.getNickName())){
					frontUser.setNickName(weixinUser.getNickName());
				}
				for(UserData ud : weixinUser.getUserConfigMap().values()){
					frontUser.setExtraValue(ud.getDataCode(), ud.getDataValue());
				}
			}
			frontUser.setExtraValue(DataName.userLastAutoUpdateOutInfoTs.toString(), String.valueOf(new Date().getTime()));

		}

	}

	@Override
	public User getUserInfo(String openId, long partnerUuid, long ownerId, int callCount){
		String accessToken = null;
		if(partnerUuid > 0){
			accessToken = this.getClientAccessToken(partnerUuid);
			logger.debug("获取第三方平台合作伙伴[" + partnerUuid + "]的accessToken:" + accessToken );
		} else {
			accessToken = this.getSingleAccessToken(ownerId);
			logger.debug("获取单平台模式下[ownerId=" + ownerId + "]的accessToken:" + accessToken );
		}
		String infoUrl = API_PREFIX + "/cgi-bin/user/info?access_token=" + accessToken + "&openid=" + openId + "&lang=zh_CN";
		logger.info("访问地址[" + infoUrl + "]来获取用户信息" );
		String result = null;
		try {
			result = HttpUtils.sendData(infoUrl);
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

		if(errCode == WeixinError.INVALID_ACCESS_TOKEN){
			this.deleteAccessToken(partnerUuid, ownerId);
			//错误的accessToken，先删除该accessToken，再重复调用
			if(callCount == 0){
				logger.info("错误的accessToken，删除后重新调用本方法");
				return this.getUserInfo(openId, partnerUuid, ownerId, 1);
			}
			logger.info("错误的accessToken，删除，但本方法已重新调用，放弃");

			return null;
		}
		String nickName = jsonNode.path("nickname").textValue();
		if(StringUtils.isNotBlank(nickName)){
			Pattern emoji = Pattern.compile ("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]" ,Pattern . UNICODE_CASE | Pattern . CASE_INSENSITIVE ) ;
			Matcher matcher = emoji.matcher(jsonNode.path("nickname").textValue());  

			user.setNickName(matcher.replaceAll(""));
			logger.debug("设置用户" + openId + "的昵称为:" + user.getNickName());
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
	public WeixinGroup createGroup(WeixinGroup weixinGroup) {
		String url = API_PREFIX + "/cgi-bin/groups/create?access_token=";
		if(weixinGroup.getInviter() > 0){
			url += this.getClientAccessToken(weixinGroup.getInviter());
		} else {
			url += this.getSingleAccessToken(weixinGroup.getOwnerId());
		}
		ObjectNode objectNode = om.createObjectNode();
		ObjectNode groupNode  = objectNode.putObject("group");
		groupNode.put("name", weixinGroup.getGroupName());

		HttpClient httpClient = HttpClientPoolV3.getHttpClient(API_HOST, API_PORT);
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

		String url = API_PREFIX + "/cgi-bin/groups/members/update?access_token=";
		if(weixinGroup.getInviter() > 0){
			url += this.getClientAccessToken(weixinGroup.getInviter());
		} else {
			url += this.getSingleAccessToken(frontUser.getOwnerId());
		}

		ObjectNode objectNode = om.createObjectNode();
		objectNode.put("openid", frontUser.getUsername());
		objectNode.put("to_groupid", weixinGroup.getOutGroupId());
		HttpClient httpClient = HttpClientPoolV3.getHttpClient(API_HOST, API_PORT);
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
	public void setTag(User frontUser, long tagId) {
		String accessToken = null;
		if(frontUser.getInviter() > 0){
			accessToken = this.getClientAccessToken(frontUser.getInviter());
		} else {
			accessToken = this.getSingleAccessToken(frontUser.getOwnerId());
		}
		String url = API_PREFIX + "/cgi-bin/tags/members/batchtagging?access_token=" + accessToken;
		HttpClient httpClient = HttpClientPoolV3.getHttpClient(API_HOST, API_PORT);
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

	/*
	 * 创建卡券
	 */
	@Override
	public int createCouponModel(CouponModel couponModel, String accessToken){
		Assert.notNull(accessToken,"尝试创建微信卡券产品的couponAccessToken不能为空");
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
		//sb.append(couponModel.getCouponType());
		sb.append("CASH\",\"cash\":{\"base_info\":{");
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
		String subTitle2 = couponModel.getContent();
		if(StringUtils.isBlank(subTitle2)){
			subTitle2 = title;
		}
		if(subTitle2.length() > 18){
			subTitle2 = subTitle2.substring(0,17);
		}
		sb.append(subTitle2);
		sb.append("\",\"date_info\":{\"type\":\"DATE_TYPE_FIX_TIME_RANGE\",\"begin_timestamp\":");
		sb.append(beginTimeTs);
		sb.append(",\"end_timestamp\":");
		sb.append(endTimeTs);
		sb.append("},\"color\":\"");
		String color = couponModel.getExtraValue("color");
		if(StringUtils.isBlank(color)){
			sb.append("Color040");
		} else {
			sb.append(color);
		}
		sb.append("\"");
		String servicePhone = couponModel.getExtraValue("servicePhone");
		if(StringUtils.isNotBlank(servicePhone)){
			sb.append(",\"service_phone\": \"");
			sb.append(servicePhone);
			sb.append("\"");
		}
		String useNotice = couponModel.getExtraValue("useNotice");
		if(StringUtils.isNotBlank(useNotice)){
			sb.append(",\"notice\": \"");
			sb.append(servicePhone);
			sb.append("\"");
		}
		//		sb.append("购买乐视乐次元季度会员领取果倍爽");


		if(StringUtils.isNotBlank(couponModel.getMemory())){
			sb.append(",\"description\":\"");	//卡券使用说明，字数上限为1024个汉字。
			String description = couponModel.getMemory();
			if(description.length() > 1024){
				description = description.substring(0,1023);
				logger.warn("卡券[" + couponModel.getCouponModelId() + "/" + couponModel.getCouponCode() + "]的memory太长，截断");
			} 
			sb.append(description).append("\"");
		}
		sb.append(",\"can_share\":true,\"can_give_friend\":true,\"sku\":{\"quantity\":0}},\"least_cost\":");
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

	/*
	 * 导入卡券CODE
	 */
	@Override
	public int importCardCode(long sitePartnerId, long ownerId, String cardId, String... card){

		String accessToken = null;
		if(sitePartnerId > 0){
			accessToken = this.getClientAccessToken(sitePartnerId);
		} else {
			accessToken = this.getSingleAccessToken(ownerId);
		}
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
		logger.debug("微信添加卡券库存返回:" + result);
		if(jsonNode == null){
			logger.error("微信未返回合法的JSON数据:" + result);
			return EisError.networkError.id;
		}
		int errorCode = jsonNode.path("errcode").asInt();
		if(errorCode != 0){
			logger.error("无法添加卡券[" + cardId + "]库存，返回错误:" + result);

			if(errorCode == WeixinError.INVALID_CARD_ID){
				return errorCode;
			}
			return OperateResult.failed.getId();
		}

		logger.info(result);
		return OperateResult.success.getId();
	}

	@Override
	public String cardSign(long timeStamp, String currentUrl, String nonceStr, String couponCode, long sitePartnerId,	long ownerId, int callCount) {
		long t1 = new Date().getTime();
		String accessToken = null;
		String appId = null;
		if(sitePartnerId > 0){
			accessToken = this.getClientAccessToken(sitePartnerId);
			appId = this.getClientAppId(sitePartnerId);
		} else {
			accessToken =this.getSingleAccessToken(ownerId);
			WeixinPlatformInfo wxInfo = this.getSingleWeixinPlatformInfo(ownerId);
			if(wxInfo == null){
				logger.error("系统未配置单平台公众号信息");
				return null;
			}
			appId = wxInfo.appId;
		}
		String url = WeixinConfig.API_PREFIX + "/cgi-bin/ticket/getticket?access_token=" + accessToken + "&type=wx_card";
		String result="";
		String api_ticket="";
		String sha1Str="";
		result= HttpUtils.sendData(url);

		if(result == null){
			logger.error("无法连接到微信服务器");
		}  
		logger.debug("从微信请求生成card签名ticket的结果:" + result + ",耗时:" + (new Date().getTime() - t1) + "毫秒");
		JsonNode jsonNode = null;
		try{
			jsonNode = JsonUtils.getInstance().readTree(result);
		}catch(Exception e){
			e.printStackTrace();
		}
		if(jsonNode == null){
			logger.error("无法解析微信端返回:" + result);
			return null;
		}
		int errCode = jsonNode.path("errcode").asInt();

		if(errCode == WeixinError.INVALID_ACCESS_TOKEN){
			this.deleteAccessToken(sitePartnerId, ownerId);
			//错误的accessToken，先删除该accessToken，再重复调用
			if(callCount == 0){
				logger.info("错误的accessToken，删除后重新调用本方法");
				return this.cardSign(timeStamp, currentUrl, nonceStr, couponCode, sitePartnerId, ownerId, 1);
			}
			logger.info("错误的accessToken，删除，但本方法已重新调用，放弃");

			return null;
		}

		api_ticket = jsonNode.get("ticket").asText();

		if(StringUtils.isBlank(api_ticket)){
			logger.error("无法从微信获取JS签名");
			return null;
		}
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("SHA1");
		} catch (NoSuchAlgorithmException e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		sha1Str="api_ticket="+api_ticket+"&app_id=" + appId + "&timestamp="+String.valueOf(timeStamp/ 1000)+ "&noncestr="+nonceStr+"&card_id=" + couponCode;
		logger.info("sourceString是"+sha1Str);
		messageDigest.update(sha1Str.getBytes());
		sha1Str= StringFormat.getFormattedText(messageDigest.digest());
		logger.info("使用数据[ticket=" + api_ticket + ",nonceStr=" + nonceStr + ",timestamp=" + timeStamp/ 1000 + ",currentUrl=" + currentUrl + "]生成签名:" +sha1Str);


		return 	sha1Str;
	}

	@Override 
	public String getClientAppId(long sitePartnerId) {
		String tableName = WEIXIN_PLATFORM_CACHE_KEY;
		String clientAppId = centerDataService.getHmValue(tableName, String.valueOf(sitePartnerId));
		if(clientAppId != null){
			return clientAppId;
		}
		User partner = partnerService.select(sitePartnerId);
		if(partner != null && StringUtils.isNotBlank(partner.getAuthKey())){
			clientAppId = partner.getAuthKey();
			try {
				centerDataService.setHmValue(tableName, String.valueOf(sitePartnerId), clientAppId, -1);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return clientAppId;
		}

		return null;
	}

	@Override 
	public long  getSitePartnerId(String clientAppId, long ownerId) {
		String tableName = WEIXIN_CLIENT_APPID_MAP;

		WeixinClient client = centerDataService.getHmValue(tableName, clientAppId);

		if(client != null){
			logger.debug("从缓存中返回公众号客户:" + client);
			return client.uuid;
		}
		UserCriteria userCriteria = new UserCriteria(ownerId);
		userCriteria.setAuthKey(clientAppId);
		List<User> partnerList = partnerService.list(userCriteria);
		if(partnerList == null || partnerList.size() < 1){
			return 0;
		}
		User partner = partnerList.get(0);
		return partner.getUuid();
	}


	@Override
	public int createWeixinButton(List<WeixinButton> weixinButtonList, String menuTag, long sitePartnerId, long ownerId){
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
		String accessToken = null;
		if(sitePartnerId > 0){
			accessToken = this.getClientAccessToken(sitePartnerId);
		} else {
			accessToken =this.getSingleAccessToken(ownerId);
		}

		logger.debug("创建微信公众号菜单[accessToken=" + accessToken + ",menuTag=" + menuTag + "]:菜单:" + menuJsonString);

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
	
	@Override
	public List<WeixinButton>  getWeixinButton(String menuTag, long sitePartnerId, long ownerId){
				String accessToken = null;
		if(sitePartnerId > 0){
			accessToken = this.getClientAccessToken(sitePartnerId);
		} else {
			accessToken =this.getSingleAccessToken(ownerId);
		}


		String url = WeixinConfig.API_PREFIX + "/cgi-bin/menu/get?access_token=" + accessToken;
		String page = HttpUtils.sendData(url);
		
		logger.debug("获取微信公众号菜单[accessToken=" + accessToken + ",menuTag=" + menuTag + "]，结果:" + page);
		return null;
	}
	/**
	 * 处理文本消息
	 * @throws Exception 
	 */
	@Override
	public String processTextMessage(User frontUser, HttpServletRequest request, WeixinMsg message, long sitePartnerId, long ownerId) {
		String question = null;
		logger.debug("收到消息 ：" + message.getEventKey() + "     " + message.getContent());
		if(StringUtils.isBlank(message.getContent())){
			question = message.getEventKey();
		} else {
			question = message.getContent();
		}
		if(StringUtils.isBlank(question)){

			return DEFAULT_NULL_REPLAY_MESSAGE;
		}
		if(question.equals("TESTCOMPONENT_MSG_TYPE_TEXT")){
			return "TESTCOMPONENT_MSG_TYPE_TEXT_callback";
		}
		if(question.startsWith("QUERY_AUTH_CODE")){
			String data = question.replaceAll("QUERY_AUTH_CODE:", "");
			User partner = partnerService.select(sitePartnerId);
			String clientAppId = partner.getAuthKey();
			if(StringUtils.isBlank(clientAppId)){
				logger.error("合作伙伴:" + sitePartnerId + "没有authKey即appId");
				return null;
			}
			logger.debug("收到第三方平台测试数据:" + question + ",使用得到的QUERY_AUTH_CODE[" + data + "]更新该测试账户[" + sitePartnerId + "]的accessToken");
			this.updateClientAccessToken(partner, data, 300);
			/*String table = WEIXIN_CLIENT_APPID_MAP + "#" + clientAppId;
			try {
				centerDataService.setHmValue(table, ACCESS_TOKEN_CACHE_KEY, data, 300);
			} catch (Exception e) {
				e.printStackTrace();
			}*/
			String content = data + "_from_api";
			ObjectNode on = om.createObjectNode();
			on.put("msgtype","text");
			if(frontUser != null){
				on.put("touser", frontUser.getAuthKey());
			}
			ObjectNode subNode  = on.putObject("text");
			subNode.put("content",content);
			String msgContent = null;
			try {
				msgContent= om.writeValueAsString(on);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			this.sendCustomServiceMessage(msgContent, Constants.WEIXIN_CS_MSG_TEXT, sitePartnerId, ownerId);
			return "";
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
							processVariable(weixinRichTextMessage, frontUser);
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
								if(frontUser != null){
									result = matcher.replaceAll(Matcher.quoteReplacement(String.valueOf(frontUser.getUuid())));
								}
							} else {
								result = autoResponseModel.getResponse().toString();
							}
						}
						return result;
					}
				}
				String to = message.getFromUserName();
				String from = message.getToUserName();
				String urlPrefix = HttpUtils.generateUrlPrefix(request);
				if(sitePartnerId > 0){
					User partner = partnerService.select(sitePartnerId);
					if(partner != null && partner.getAuthKey() != null){
						urlPrefix = urlPrefix.replaceAll(partner.getAuthKey(), "");
						urlPrefix = urlPrefix.replaceAll("://", "://" + partner.getAuthKey() + ".");
					}
				}
				if(weixinRichTextMessageList.size() < 1){
					logger.warn("当前没有任何自动应答消息");
					result = tulingRobotService.getAnswer(message.getContent(),ownerId);  
				} else {
					WeixinRichTextMessage directResponseMessage = weixinRichTextMessageList.get(0);
					directResponseMessage.setTo(to);
					directResponseMessage.setFrom(from);

					if(weixinRichTextMessageList.size() == 1){
						logger.debug("当前只有一个自动应答富文本消息[" + weixinRichTextMessageList.get(0).getWeixinRichTextMessageId() + "],直接返回该消息");
						result = StringFormat.formatMsg2Xml(directResponseMessage,urlPrefix);
					} else {
						String accessToken = null;
						if(sitePartnerId > 0){
							accessToken = this.getClientAccessToken(sitePartnerId);
						} else {
							accessToken = this.getSingleAccessToken(ownerId);
						}
						logger.debug("当前定义了" + weixinRichTextMessageList.size() + "个自动应答富文本消息");

						//除了优先级最高的之外，剩下的全部异步发送
						for(int i = 1; i < weixinRichTextMessageList.size(); i++){
							WeixinRichTextMessage weixinRichTextMessage = weixinRichTextMessageList.get(i);
							weixinRichTextMessage.setDelaySec(BATCH_SEND_DELAY_SEC * i);
							weixinRichTextMessage.setTo(to);
							weixinRichTextMessage.setFrom(from);
							new Thread(new WeixinAsyncMessage(weixinRichTextMessage, accessToken, urlPrefix)).start();
						}

						logger.debug("返回第1个自动应答富文本消息[" + weixinRichTextMessageList.get(0).getWeixinRichTextMessageId() + ",其他异步发送");
						result = StringFormat.formatMsg2Xml(directResponseMessage, urlPrefix);
					}
				}
			}
		} else {
			final boolean weixinNotAutoResponse = configService.getBooleanValue("weixinNotAutoResponse", ownerId);

			if (!weixinNotAutoResponse) {
				result = tulingRobotService.getAnswer(message.getContent(),ownerId);  
			}
		}

		return result;
	}

	/**
	 * 处理文本中的变量
	 * 把模版替换为数据
	 * @param weixinRichTextMessage
	 */
	private void processVariable(WeixinRichTextMessage weixinRichTextMessage, User frontUser) {
		if (frontUser == null || StringUtils.isBlank(weixinRichTextMessage.getUrl())){
			return;
		}
		if(weixinRichTextMessage.getUrl().contains("${uuid}")) {
			weixinRichTextMessage.setUrl(weixinRichTextMessage.getUrl().replaceAll("\\$\\{uuid\\}", String.valueOf(frontUser.getUuid())));
		}

	}
	
	@Override
	public String getCsList(String accessToken){
		
		
		String url = WeixinConfig.API_PREFIX + "/customservice/kfaccount/add?access_token=" + accessToken;
		String result = null;
		logger.info("请求列出客服接口发送消息[url=" + url + "]");
		result = HttpUtils.sendData(url);

		logger.info("请求列出客服接口发送消息[url=" + url + "],返回结果:" + result);
		return result;
	}
	
	@Override
	public int addCs(String csName, String csPassword, String nickName, String accessToken, long ownerId){
		
		String data = "{\"kf_account\":\"" + csName + "\",\"nickname\":\"" + nickName + "\",\"password\":\"" + csPassword + "\"}";
		
		String url = WeixinConfig.API_PREFIX + "/cgi-bin/customservice/getkflist?access_token=" + accessToken;
		String result = null;
		logger.info("请求添加公众号客服接口发送消息[url=" + url + "]，消息数据:" + data);
		result = HttpUtils.postData(url, data);

		logger.info("通过客服添加接口发送消息[" + data + "]，发送结果:"+ result);
		return OperateResult.success.getId();		
	}

	/**
	 * 通过客服接口发送消息
	 * 
	 * @param message
	 */
	@Override
	public int sendCustomServiceMessage(String message, int messageType, long sitePartnerId, long ownerId) {
		String accessToken = null;
		if(sitePartnerId > 0){
			accessToken = this.getClientAccessToken(sitePartnerId);
		} else {
			accessToken = this.getSingleAccessToken(ownerId);
		}
		String csStr = this.getCsList(accessToken);
		if(csStr == null){
			String domain = null;
			if(ownerId > 0){
				domain = configService.getValue("siteDomain", ownerId);
			}
			if(domain == null){
				domain = "test.com";
			}
			int rand = RandomUtils.nextInt(10000) + 10000;
			String csName = "cs" + rand + "@" + domain;
			String csNickName = "客服" + rand;
			String csPassword = DigestUtils.md5Hex(csName);
			int addResult = this.addCs(csName, csPassword, csNickName, accessToken, ownerId);
			if(addResult != OperateResult.success.id){
				logger.error("无法添加公众号客服，返回:" + addResult);
				return addResult;
			}
		} else {
			/*JsonNode jsonNode = om.readTree(csStr);
			joson*/
			
		}
		String url = WeixinConfig.API_PREFIX + "/cgi-bin/message/custom/send?access_token=" + accessToken;
		String result = null;
		logger.info("请求客服接口发送消息[url=" + url + "]，消息数据:" + message);
		result = HttpUtils.postData(url, message);

		logger.info("通过客服接口发送消息[" + message + "]，发送结果:"+ result);
		return OperateResult.success.getId();		
	}

	@Override
	public String processSubscribe(User frontUser, HttpServletRequest request, WeixinMsg weixinMsg, long sitePartnerId, long ownerId) {

		WeixinRichTextMessageCriteria weixinRichTextMessageCriteria = new WeixinRichTextMessageCriteria();
		weixinRichTextMessageCriteria.setOwnerId(ownerId);
		weixinRichTextMessageCriteria.setTrigger(WeixinRichTextMessageCriteria.TRIGGER_MODE_ON_SUBSCRIBE);
		weixinRichTextMessageCriteria.setCurrentStatus(BasicStatus.normal.getId());
		List<WeixinRichTextMessage> weixinRichTextMessageList = weixinRichTextMessageService.list(weixinRichTextMessageCriteria);
		if(weixinRichTextMessageList == null || weixinRichTextMessageList.size() < 1){
			logger.debug("当前未定义任何关注时发送的消息,ownerId=" + ownerId);
			return this.processTextMessage(frontUser, request, weixinMsg, sitePartnerId, ownerId);
		}
		String to = weixinMsg.getFromUserName();
		String from = weixinMsg.getToUserName();
		String urlPrefix = HttpUtils.generateUrlPrefix(request);
		WeixinRichTextMessage directResponseMessage = weixinRichTextMessageList.get(0);
		directResponseMessage.setTo(to);
		directResponseMessage.setFrom(from);

		if(weixinRichTextMessageList.size() == 1){
			logger.debug("当前只定义了1个关注时发送的消息:" + weixinRichTextMessageList.get(0).getWeixinRichTextMessageId() + ",直接返回该消息");
			return StringFormat.formatMsg2Xml(directResponseMessage,urlPrefix);
		}
		String accessToken = null;
		if(sitePartnerId > 0){
			accessToken = this.getClientAccessToken(sitePartnerId);
		} else {
			accessToken =this.getSingleAccessToken(ownerId);
		}
		logger.debug("当前定义了" + weixinRichTextMessageList.size() + "个关注时发送的消息");

		//除了优先级最高的之外，剩下的全部异步发送
		for(int i = 1; i < weixinRichTextMessageList.size(); i++){
			WeixinRichTextMessage weixinRichTextMessage = weixinRichTextMessageList.get(i);
			weixinRichTextMessage.setDelaySec(BATCH_SEND_DELAY_SEC * i);
			weixinRichTextMessage.setTo(to);
			weixinRichTextMessage.setFrom(from);
			new Thread(new WeixinAsyncMessage(weixinRichTextMessage, accessToken, urlPrefix)).start();
		}

		logger.debug("返回优先级最高的关注时发送消息[" + weixinRichTextMessageList.get(0).getWeixinRichTextMessageId() + ",其他异步发送");
		return StringFormat.formatMsg2Xml(directResponseMessage, urlPrefix);
	}

	/** 处理扫码后进入
	 * 如果有必要，对用户本次行为做标记
	 */
	@Override
	public String processScanMessage(User frontUser, HttpServletRequest request, WeixinMsg weixinMsg, long sitePartnerId, long ownerId) {
		if(weixinMsg.getEventKey() == null || weixinMsg.getEventKey().trim().equals("0")){
			logger.info("用户扫码但没有任何KEY");
			return null;
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
		return this.processTextMessage(frontUser, request, weixinMsg, sitePartnerId, ownerId);

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



	@Override
	public void deleteAccessToken(long sitePartnerId, long ownerId){
		if(sitePartnerId > 0){
			User partner = partnerService.select(sitePartnerId);
			if(partner == null){
				logger.error("在系统中找不到指定的合作伙伴:" + sitePartnerId);
				return;
			}
			String clientAppId = partner.getAuthKey();
			if(StringUtils.isBlank(clientAppId)){
				logger.error("合作伙伴:" + sitePartnerId + "没有authKey即appId");
				return;
			}
			String table = WEIXIN_CLIENT_APPID_MAP + "#" + clientAppId;
			logger.debug("删除第三方平台模式下[ownerId=" + partner.getOwnerId() + "]的客户[" + clientAppId + "]的access token");
			try {
				centerDataService.setHmValue(table, ACCESS_TOKEN_CACHE_KEY, null, 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if(ownerId > 0){
			String table = WEIXIN_CLIENT_APPID_MAP + "#" + ownerId;

			logger.debug("删除单平台[ownerId=" + ownerId + "]的access token");
			try {
				centerDataService.setHmValue(table, ACCESS_TOKEN_CACHE_KEY, null, 0);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			logger.error("尝试删除accessToken但sitePartnerId和ownerId都为0");
		}


	}
}

