package com.maicard.wpt.aspect.front;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;

import com.maicard.annotation.IgnoreLoginCheck;
import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.domain.SiteDomainRelation;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.CookieService;
import com.maicard.common.service.HttpsService;
import com.maicard.common.service.SiteDomainRelationService;
import com.maicard.common.util.AgentUtils;
import com.maicard.common.util.HttpUtils;
import com.maicard.common.util.NumericUtils;
import com.maicard.exception.RequiredParameterIsNullException;
import com.maicard.security.criteria.UserCriteria;
import com.maicard.security.domain.User;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.FrontUserService;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.SecurityStandard.UserTypes;
import com.maicard.wpt.constants.WeixinConfig;
import com.maicard.wpt.domain.WeixinPlatformInfo;
import com.maicard.wpt.misc.weixin.Constants;
import com.maicard.wpt.service.WeixinPlatformService;
/**
 * 在使用微信访问时，进行自动跳转、注册等操作<br>
 * 使用@Order(2)保证其运行在{@link com.maicard.aspect.front.SiteGeneralDataInjectAspect}之后<br>
 * 以确保能获得一些map中的注入数据，而不需要重复处理
 *
 * @author NetSnake
 * @date 2015年2月27日
 *
 */
@Aspect
@Order(2)
public class WeixinAuthorizeAspect extends BaseService {

	@Resource
	private CertifyService certifyService;
	@Resource
	private ConfigService configService;
	@Resource
	private CookieService cookieService;
	@Resource
	private FrontUserService frontUserService;	
	@Resource
	private SiteDomainRelationService siteDomainRelationService;
	@Resource
	private WeixinPlatformService weixinPlatformService;
	@Resource
	private HttpsService httpsService;


	//private final String loginUrl =

	private Map<Long, String> frontLoginUrlCache = new HashMap<Long, String>();

	private final SimpleDateFormat sdf = new SimpleDateFormat(CommonStandard.defaultDateFormat);

	@Around("(execution(* com.maicard.wpt.front.controller..*.*(..))" 
			+ "&& @annotation(org.springframework.web.bind.annotation.RequestMapping)"
			+ "&& !@annotation(com.maicard.annotation.IgnoreWeixinCheck)"
			+ ")")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable{


		long t1 = new Date().getTime();


		logger.debug("开始微信认证拦截:" + joinPoint.getTarget().getClass().getName() + "的方法:" + joinPoint.getSignature().getName());

		ModelMap map = null;
		HttpServletRequest request = null;
		HttpServletResponse response = null;


		try{
			map = (ModelMap)joinPoint.getArgs()[2];
			request  = (HttpServletRequest)joinPoint.getArgs()[0];
			response = (HttpServletResponse)joinPoint.getArgs()[1];
		}catch(Exception e){e.printStackTrace();}
		if(map == null || request == null){
			throw new RequiredParameterIsNullException("系统规范异常");
		}
		if(request.getRequestURI().startsWith("/weixin") || request.getRequestURI().startsWith("/wx")){
			logger.debug("忽略以/weixin或/wx开头的访问检查");
			return joinPoint.proceed();
		}
		String hostName = request.getServerName();

		long ownerId = NumericUtils.parseLong(map.get("ownerId"));
		if(ownerId == 0){
			SiteDomainRelation siteDomainRelation = siteDomainRelationService.getByHostName(hostName);
			if(siteDomainRelation == null){
				logger.error("根据主机名[" + request.getServerName() + "]找不到对应的站点对应数据");
				return null;
			}
			ownerId = siteDomainRelation.getOwnerId();
		} else {
			logger.debug("通过map找到了已存在的ownerId=" + ownerId);
		}
		final boolean weixinNoInfoPrivilege = configService.getBooleanValue(DataName.weixinNoInfoPrivilege.toString(), ownerId);
		if(weixinNoInfoPrivilege){
			logger.debug("当前系统配置为没有微信权限或不需要微信集成，直接返回处理结果");
			return joinPoint.proceed();

		}

		//不自动把微信用户注册为网站用户
		final boolean weixinNotAutoRegister = configService.getBooleanValue(DataName.weixinNotAutoRegister.toString(), ownerId);
		//微信访问，不自动为用户跳转到微信登录界面，用于某些不方便将公众号页面地址设置为我们系统的地址		
		//	final boolean weixinNotAutoLogin = configService.getBooleanValue(DataName.weixinNotAutoLogin.toString(), ownerId);

		long sitePartnerId = NumericUtils.parseLong(map.get(DataName.sitePartnerId.toString()));
		/*if(sitePartnerId == 0){
			//获取当前URL的主机前缀
			String hostCode = hostName.split("\\.")[0];
			SiteThemeRelationCriteria siteThemeRelationCriteria = new SiteThemeRelationCriteria();
			siteThemeRelationCriteria.setHostCode(hostCode);
			siteThemeRelationCriteria.setCurrentStatus(BasicStatus.normal.id);
			SiteThemeRelation siteThemeRelation = siteThemeRelationService.select(siteThemeRelationCriteria);
			if(siteThemeRelation != null){
				map.put(DataName.sitePartnerId.toString(), siteThemeRelation.getUuid());
				logger.debug("为当前主机代码[" + hostCode + ",主机名:" + hostName + "]找到的主题配置是:" + siteThemeRelation.getSiteThemeRelationId() + ",合作方是:" + siteThemeRelation.getUuid() + ", 放入主题代码:" + siteThemeRelation.getThemeCode() + ",替换系统名称:" + siteThemeRelation.getSiteName());
			}

		} else {
			logger.debug("通过map找到了已存在的sitePartnerId=" + sitePartnerId);
		}*/
		boolean isAlreadyWeixinLogin = false;
		//ObjectMapper om = JsonUtils.getInstance();

		String APPID = null;
		String nonceStr = null;
		long timeStamp = 0;
		
		boolean isNewCreateUser = false;

		boolean isWeixinAccess = AgentUtils.isWeixinAccess(request.getHeader("user-agent"));
		logger.debug("当前" + ( isWeixinAccess ? "是" : "不是") + "微信浏览器UA访问");
		if(isWeixinAccess){
			if(sitePartnerId > 0){
				APPID = weixinPlatformService.getClientAppId(sitePartnerId);
			} else {
				WeixinPlatformInfo wxInfo = weixinPlatformService.getSingleWeixinPlatformInfo(ownerId);
				if(wxInfo != null){
					APPID = wxInfo.appId;
				}
			}
			//APPSECRET = weixinPlatformService.getAppSecret(ownerId);
			nonceStr = DigestUtils.md5Hex(UUID.randomUUID().toString());
			timeStamp = System.currentTimeMillis();

		}

		User frontUser = null;
		String openId = null;

		String currentUrl=request.getRequestURL().toString();

		String inviteCode = ServletRequestUtils.getStringParameter(request, "i", null);
		String clearUrl = currentUrl;
		if (request.getQueryString()!=null){
			currentUrl=currentUrl+"?"+request.getQueryString();
			if(isWeixinAccess){
				Map<String,String> data = HttpUtils.getRequestDataMap(request.getQueryString());
				StringBuilder newQueryString = new StringBuilder();
				if(data != null && data.size() > 0){
					for(String key : data.keySet()){
						//不放入微信的数据
						if(key.equals("code") || key.equals("state")){
							continue;
						}
						if(key.equals("rand")){
							//重新生成rand数据
							continue;
						}
						newQueryString.append(key).append('=').append(data.get(key)).append('&');	

					}
					newQueryString.append("rand=").append(new Random().nextInt());
				}
				clearUrl += "?" + newQueryString.toString();
				logger.debug("当前请求我方链接(去除微信代码后):" + clearUrl);
			}
		}
		logger.debug("请求访问我方链接："+ currentUrl);
		String loginUrl = getLoginUrl(currentUrl, ownerId);

		if(!isWeixinAccess){
			//检查是否有测试代码
			boolean createRandomUserForTest = ServletRequestUtils.getBooleanParameter(request, "createRandomUserForTest", false);
			if(createRandomUserForTest){
				//创建一个新用户以进行压力测试
				openId = UUID.randomUUID().toString();
				long t2 = new Date().getTime();
				logger.debug("PERFORMANCE>>>到达创建测试用户前耗时" + (t2 - t1) + "毫秒");
				logger.debug("创建一个测试用随即用户:" + openId);
				frontUser = weixinPlatformService.createUser(openId, null, sitePartnerId, ownerId);
				long t3 = new Date().getTime();
				logger.debug("PERFORMANCE>>>创建测试用户耗时" + (t3 - t2) + "毫秒");
				frontUser = certifyService.forceLogin(request, response, openId);
				request.setAttribute("openId", openId);
				isNewCreateUser = true;

			} else {		
				/*				if(frontUser == null){
					frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
				}
				if(frontUser == null){
					Signature  signature = joinPoint.getSignature();
					MethodSignature methodSignature = (MethodSignature) signature;  
					Method method = methodSignature.getMethod();  
					if(method.isAnnotationPresent(IgnoreLoginCheck.class)){
						logger.debug("用户在非微信环境未登录访问方法:" + joinPoint.getTarget().getClass().getName() + "/" + joinPoint.getSignature().toString() + "，但该方法注解为忽略认证及权限检查，直接访问");
					} else {
						if(request.getRequestURI().endsWith(".json")){
							logger.debug("用户在非微信环境未登录访问方法:" + joinPoint.getTarget().getClass().getName() + "/" + joinPoint.getSignature().toString() + "，该方法未忽略权限检查，并且是JSON防范，直接返回请先登录消息");
							map.put("message", new EisMessage(EisError.userNotFoundInRequest.id,"请先登录"));
							return "";
						}
						logger.debug("用户在非微信环境未登录访问方法:" + joinPoint.getTarget().getClass().getName() + "/" + joinPoint.getSignature().toString() + "，该方法未忽略权限检查，跳转到登录地址:" + loginUrl);
						return certifyService.redirectByResponse(request, response, UserTypes.frontUser.getId(), loginUrl, request.getRequestURI(), siteDomainRelation.getCookieDomain());

					}
				}
				 */
				//logger.debug("当前不是微信访问，也不需要创建随机用户，执行程序并返回");
				return joinPoint.proceed();
			}
		}
		//XXX 会写入eis_passport_f
		//logger.debug("XXX第一次获取前端用户");
		if(frontUser == null){
			frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
		}
		long t5 = new Date().getTime();
		logger.debug("PERFORMANCE>>>获取登录用户后耗时" + (t5 - t1) + "毫秒");


		if(map.get("outUuid") != null){
			openId = map.get("outUuid").toString();
			isAlreadyWeixinLogin = true;
			logger.debug("从map中找到了outUuid=" + openId + ",当前已经登录为微信用户或已经经过检查");
		} else {
			openId = cookieService.getCookie(request, "outUuid");
			if(StringUtils.isNotBlank(openId)){
				isAlreadyWeixinLogin = true;
				map.put("outUuid", openId);
				logger.debug("从Cookie中找到了outUuid=" + openId + ",当前已经登录为微信用户或已经经过检查");
			}

		}
		if(StringUtils.isNotBlank(openId) && openId.startsWith("ERROR")){
			openId = null;
		}
		logger.debug("从Cookie中获取到的用户是:" + (frontUser == null ? "空" : frontUser.getUuid()));

		/*Cookie[] cookies = request.getCookies();
		if(cookies == null || cookies.length < 1){
			logger.warn("当前请求没有任何Cookie");
		} else {
			for(Cookie c : cookies){
				logger.debug("请求中的Cookie:" + c.getName() + "=>" + c.getValue());
			}
		}*/
		String sha1Str = null;
		if(isWeixinAccess){
			sha1Str= weixinPlatformService.makeJsSignature(timeStamp,currentUrl, nonceStr, ownerId,0);
			map.put("appid", APPID);
			map.put("timestamp", String.valueOf(timeStamp/ 1000));
			map.put("signature", sha1Str);
			map.put("nonceStr", nonceStr);
		}
		String weixinCode = ServletRequestUtils.getStringParameter(request, "code");
		String weixinState = ServletRequestUtils.getStringParameter(request, "state");;

		User partner = weixinPlatformService.getPartnerByHost(request.getServerName(), ownerId);
		long partnerUuid = 0;
		if(partner != null){
			partnerUuid = partner.getUuid();
		}
		if(frontUser == null && isWeixinAccess && !isAlreadyWeixinLogin ){
			//该帐号没有调用用户信息的权限，比如订阅号，或者不希望使用微信集成

			if(weixinNoInfoPrivilege){
				logger.debug("当前云帐号[" + ownerId + "]没有访问微信用户信息的接口权限，不尝试重定向" );
				long t6 = new Date().getTime();
				Object object = joinPoint.proceed();
				long t7 = new Date().getTime();
				logger.debug("PERFORMANCE>>>执行目标方法耗时" + (t7 - t6) + "毫秒");

				return object;
			}



			//private final String weixinAccreditUrl = "http://youbao.changxiu.net/weixin/accredit";

			//String redirectUrl = (request.getProtocol().toLowerCase().startsWith("https") ? "https://" : "http://")  + request.getServerName() + ":" + request.getServerPort() + "/weixin/accredit?scope=111&url=" + currentUrl;
			String authRedirectUrl = generateAuthRedirectUrl(weixinState, currentUrl, partner, ownerId);

			if(StringUtils.isBlank(weixinCode)){
				boolean haveRefererCode = false;
				logger.info("在微信请求的URL参数中未找到微信访问code");
				if(request.getRequestURI().endsWith(".json")){
					String referer = request.getHeader("referer");
					logger.info("在微信请求的URL参数中未找到微信访问code，访问我方JSON数据，尝试获取referer中的参数:" + referer);
					if(referer != null){
						Map<String,String> refererDataMap = HttpUtils.getRequestDataMap(referer);
						weixinCode = refererDataMap.get("code");
						weixinState = refererDataMap.get("state");
						if(StringUtils.isBlank(weixinCode)){
							logger.error("在referer中也没有code,返回请先登录消息");
							map.put("message", new EisMessage(EisError.userNotFoundInRequest.id,"请先登录"));
							return "";
						} else {
							haveRefererCode = true;
						}
					}
				}
				if(!haveRefererCode){
					logger.error("在微信请求和referer中都未找到微信访问code，重定向到:" + authRedirectUrl);
					return certifyService.redirectByResponse(request, response, UserTypes.frontUser.getId(), authRedirectUrl, request.getRequestURI(), null);	

				}	
			}
			if(StringUtils.isBlank(weixinState)){
				if(request.getRequestURI().endsWith(".json")){
					map.put("message", new EisMessage(EisError.userNotFoundInRequest.id,"请先登录"));
					return "";
				}
				logger.error("在微信请求中未找到微信访问state，重定向到:" + authRedirectUrl);
				return certifyService.redirectByResponse(request, response, UserTypes.frontUser.getId(), authRedirectUrl, request.getRequestURI(), null);
			}


			openId = weixinPlatformService.getOpenIdByCode(weixinCode, partnerUuid, ownerId);


			if(StringUtils.isNotBlank(openId) && openId.equals("ERROR#40029")){
				logger.error("微信认证出现异常40029，重新加载页面:" + clearUrl);
				return certifyService.redirectByResponse(request, response, UserTypes.frontUser.getId(), clearUrl, null, null);
			}
			if(StringUtils.isNotBlank(openId) && openId.startsWith("ERROR")){
				openId = null;
			}


			if(StringUtils.isBlank(openId)){
				if(request.getRequestURI().endsWith(".json")){
					map.put("message", new EisMessage(EisError.userNotFoundInRequest.id,"请先登录"));
					return "";
				}
				logger.warn("openID错误，重定向到登录地址:" + getLoginUrl(currentUrl,ownerId));
				return certifyService.redirectByResponse(request, response, UserTypes.frontUser.getId(), loginUrl, request.getRequestURI(), null);

			} else {

				map.put("outUuid", openId);
				//使用openId登陆
				UserCriteria userCriteria = new UserCriteria();
				userCriteria.setUserTypeId(UserTypes.frontUser.getId());
				userCriteria.setAuthKey(openId);
				int existCount = frontUserService.count(userCriteria);
				if(existCount <= 0){
					//如果系统没有配置为不自动注册，则使用openId注册新用户
					if(!weixinNotAutoRegister){
						logger.debug("当前系统配置是微信用户自动注册[openId=" + openId + ",ownerId=" + ownerId + "]");

						frontUser = weixinPlatformService.createUser(openId, inviteCode, partnerUuid, ownerId);
						isNewCreateUser = true;
					} else {
						logger.debug("当前系统配置是微信用户不自动注册[openId=" + openId + "]");
					}
				} else {
					//该OpenId用户已存在
					logger.debug("当前系统已存在前端用户[openId=" + openId + "]");
				}
				//XXX
				frontUser = certifyService.forceLogin(request, response, openId);
				if(frontUser == null){
					if(weixinNotAutoRegister){
						logger.debug("在系统中没找到openId=" + openId + "的前端用户，但当前系统配置是微信用户不自动注册");
					} else {
						logger.error("无法使用openId[" + openId + "]登陆，重定向到登录地址:" + loginUrl);
						return certifyService.redirectByResponse(request, response, UserTypes.frontUser.getId(), loginUrl, request.getRequestURI(), null);
					}
				} else {
					logger.info("使用openId[" + openId + "]成功登陆:" + frontUser.getUuid());
					//XXX 微信客户端会出现丢失Cookie的情况，导致登录失败，因此在这里加入一个openId，供后面的认证程序作为备用认证措施,NetSnake,2016-03-06
					request.setAttribute("openId", openId);


				}

			}
		} else {
			if(frontUser == null){
				Signature  signature = joinPoint.getSignature();
				MethodSignature methodSignature = (MethodSignature) signature;  
				Method method = methodSignature.getMethod();  
				if(method.isAnnotationPresent(IgnoreLoginCheck.class)){
					logger.debug("用户未登录访问方法:" + joinPoint.getTarget().getClass().getName() + "/" + joinPoint.getSignature().toString() + "，但该方法注解为忽略认证及权限检查，直接访问");
				} else {
					if(request.getRequestURI().toLowerCase().endsWith(".json")){
						logger.debug("用户未登录访问方法:" + joinPoint.getTarget().getClass().getName() + "/" + joinPoint.getSignature().toString() + "，该方法未忽略权限检查，并且是JSON后缀，直接返回请先登录消息");
						map.put("message", new EisMessage(EisError.userNotFoundInRequest.id,"请先登录"));
						return "";
					}
					logger.debug("用户未登录访问方法:" + joinPoint.getTarget().getClass().getName() + "/" + joinPoint.getSignature().toString() + "，该方法未忽略权限检查，跳转到登录地址:" + loginUrl);
					return certifyService.redirectByResponse(request, response, UserTypes.frontUser.getId(), loginUrl, request.getRequestURI(), null);

				}
			}

		}

		//更新用户的信息					
		if(frontUser != null && isWeixinAccess){
			boolean updateUserInfo = false;
			long currentTs = new Date().getTime();

			if(StringUtils.isBlank(openId)){
				openId = frontUser.getAuthKey();
			}

			long lastUpdateTime = frontUser.getLongExtraValue(DataName.userLastAutoUpdateOutInfoTs.toString());
			if(lastUpdateTime == 0 ||  currentTs - lastUpdateTime > Constants.AUTO_UPDATE_USER_INFO_INTERVAL * 1000){
				logger.debug("上次更新微信用户[" + frontUser.getUuid() + "/" + openId + "]信息时间是:" + (lastUpdateTime == 0 ? "从未" : sdf.format(new Date(lastUpdateTime))) + ",需要更新微信用户信息");
				updateUserInfo = true;
			} else {
				logger.debug("上次更新微信用户[" + frontUser.getUuid() + "/" + openId + "]信息时间是:" + sdf.format(new Date(lastUpdateTime)) + ",还不到设置的更新间隔" + Constants.AUTO_UPDATE_USER_INFO_INTERVAL + "秒，不需要自动更新用户信息");
			}


			if(isNewCreateUser || updateUserInfo){
				weixinPlatformService.updateUserInfo(frontUser, openId, sitePartnerId);
				frontUserService.updateAsync(frontUser);
			}
		}

		map.put("currentTime", sdf.format(new Date()));
		map.put("outUuid", openId);
		request.setAttribute("openId", openId);
		if(weixinNotAutoRegister && isWeixinAccess){
			logger.debug("当前系统配置为不自动为微信用户注册，向Cookie中放入outUuid=" + openId);
			cookieService.addCookie(request, response, "outUuid", openId, CommonStandard.COOKIE_MAX_TTL, null, false);
		}
		map.put("outShareUrl",currentUrl);
		/*map.put("appid", APPID);
		map.put("timestamp", String.valueOf(timeStamp/ 1000));
		map.put("signature", sha1Str);
		map.put("nonceStr", nonceStr);*/
		long t6 = new Date().getTime();
		Object object = joinPoint.proceed();
		long t7 = new Date().getTime();
		logger.debug("完成微信认证拦截，执行主体程序[" + joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().toString() + "]并返回");
		logger.debug("PERFORMANCE>>>执行目标方法耗时" + (t7 - t6) + "毫秒");
		return object;
	}

	private String generateAuthRedirectUrl(String weixinState, String returnUrl, User partner, long ownerId) {
		String authRedirectUrl = WeixinConfig.OPEN_HOST_PREFIX + "/connect/oauth2/authorize?";
		try {
			returnUrl = URLEncoder.encode(returnUrl,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if(partner != null){
			//使用第三方跳转模式
			authRedirectUrl += "appid="+ partner.getAuthKey() +"&redirect_uri=" + returnUrl + "&response_type=code&scope=snsapi_base&state=" + partner.getAuthKey() + "&component_appid=" + weixinPlatformService.getPlatformAppId(partner.getOwnerId()) + "#wechat_redirect#wechat_redirect";
		} else {
			//单平台模式
			WeixinPlatformInfo wxInfo = weixinPlatformService.getSingleWeixinPlatformInfo(ownerId);
			authRedirectUrl += "appid="+ wxInfo.appId + "&redirect_uri=" + returnUrl + "&response_type=code&scope=snsapi_base&state=123#wechat_redirect";

		}
		return authRedirectUrl;
	}

	private String getLoginUrl(String currentUrl, long ownerId){
		String url = null;
		if(frontLoginUrlCache.get(ownerId) != null){
			url = frontLoginUrlCache.get(ownerId);
			logger.debug("从缓存中返回[ownerId=" + ownerId + "]的登录URL:" + url);
			return url;
		}
		url = configService.getValue(DataName.frontLoginUrl.toString(), ownerId);
		if(StringUtils.isBlank(url)){
			url =  CommonStandard.FRONT_LOGIN_URL;
		}
		if(url.indexOf("returnUrl") > 0){

		} else {
			if(currentUrl != null){
				if(url.indexOf("?") > 0){
					url += "&returnUrl=" + currentUrl;
				} else {
					url += "?returnUrl=" + currentUrl;
				}
			}	
		}
		logger.debug("为[ownerId=" + ownerId + "]获取登录URL:" + url + "，并放入缓存");
		frontLoginUrlCache.put(ownerId, url);
		return url;

	}
}
