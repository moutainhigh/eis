package com.maicard.wpt.controller.common;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maicard.annotation.IgnoreLoginCheck;
import com.maicard.captcha.service.CaptchaService;
import com.maicard.common.base.BaseController;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.domain.GlobalUnique;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.CookieService;
import com.maicard.common.service.DataDefineService;
import com.maicard.common.service.DirtyDictService;
import com.maicard.common.service.GlobalUniqueService;
import com.maicard.common.service.UuidService;
import com.maicard.common.util.AgentUtils;
import com.maicard.common.util.IpUtils;
import com.maicard.common.util.Crypt;
import com.maicard.common.util.CryptKeyUtils;
import com.maicard.common.util.JsonUtils;
import com.maicard.common.util.NumericUtils;
import com.maicard.http.HttpClientPoolV3;
import com.maicard.mb.service.UserMessageService;
import com.maicard.money.domain.PayChannelMechInfo;
import com.maicard.money.service.MoneyService;
import com.maicard.money.service.PayService;
import com.maicard.product.service.ItemService;
import com.maicard.product.service.ProductService;
import com.maicard.security.criteria.UserCriteria;
import com.maicard.security.domain.User;
import com.maicard.security.domain.UserData;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.FrontUserService;
import com.maicard.security.service.UserDataService;
import com.maicard.security.service.UserDynamicDataService;
import com.maicard.security.service.UserRelationService;
import com.maicard.site.service.DocumentService;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.Operate;
import com.maicard.standard.OperateCode;
import com.maicard.standard.OperateResult;
import com.maicard.standard.SecurityStandard.UserStatus;
import com.maicard.standard.SecurityStandard.UserTypes;
import com.maicard.wpt.constants.WeixinConfig;
import com.maicard.wpt.service.WeixinService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import static com.maicard.standard.CommonStandard.frontMessageView;

/**
 * 微信自动注册用户控制器
 * @author NetSnake
 * @date 2016-07-01
 */

@Controller
@RequestMapping("/weixinUser")
public class WeixinUserController  extends BaseController{	

	@Resource
	private ProductService productService;
	@Resource
	private CaptchaService captchaService;
	@Resource
	private CertifyService certifyService;
	@Resource
	private ConfigService configService;
	@Resource
	private CookieService cookieService;
	@Resource
	private DataDefineService dataDefineService;
	@Resource
	private DirtyDictService dirtyDictService;
	@Resource
	private FrontUserService frontUserService;
	@Resource
	private DocumentService documentService;
	@Resource
	private GlobalUniqueService globalUniqueService;
	@Resource
	private ItemService itemService;
	@Resource
	private MoneyService moneyService;
	@Resource
	private PayService payService;
	@Resource
	private UserDataService userDataService;
	@Resource
	private UuidService uuidService;
	@Resource
	private UserDynamicDataService userDynamicDataService;
	@Resource
	private UserMessageService userMessageService;
	@Resource
	private UserRelationService userRelationService;      

	ObjectMapper om = JsonUtils.getInstance();

	//使用@Autowired以在某些没有weixinService的情况下使用
	@Autowired(required = false)
	private WeixinService weixinService;

	private boolean siteRegisterNeedPatchca;
	private boolean mailActive;
	private boolean loginAfterRegister;
	String welcomeSms;
	int showCaptchaWhenLoginFailCount;
	boolean userRegisterNeedSmsSign;		//注册时是否需要短信验证
	private SimpleDateFormat sdf = new SimpleDateFormat(CommonStandard.defaultDateFormat);

	private static Map<String, PayChannelMechInfo> mechCache = new HashMap<String, PayChannelMechInfo>();

	private static final String DEFAULT_APP_CODE = "ddz";

	private String aesKey;

	//注册时手机验证码有效期
	private int REGISTER_SMS_TTL = 120;
	private String userUploadDir;

	private final String weixinRegisterUrl = "/weixinUser/register.shtml";
	private final String weixinCreatePasswordUrl = "/weixinUser/createPassword.shtml";


	@PostConstruct
	public void init(){

		siteRegisterNeedPatchca = configService.getBooleanValue(DataName.siteRegisterNeedPatchca.toString(),0);
		loginAfterRegister = configService.getBooleanValue(DataName.siteLoginAfterRegister.toString(),0);
		showCaptchaWhenLoginFailCount = 1000;//configService.getIntValue(DataName.showCaptchaWhenLoginFailCount.toString(),0);
		try {
			aesKey = CryptKeyUtils.readAesKey();
		} catch (Exception e) {
			e.printStackTrace();
		}

		userUploadDir = configService.getValue(DataName.userUploadDir.toString(),0);
		if(userUploadDir != null){
			userUploadDir = userUploadDir.replaceAll("/$", "");
		}


	}


	/**
	 * 尝试新增一个微信用户
	 * 如果已存在则登录
	 * 
	 * @param request
	 * @param response
	 * @param map
	 * @param frontUser
	 * @param smsRegisterSign
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value="/create", method=RequestMethod.POST)
	@IgnoreLoginCheck
	public String create(HttpServletRequest request,HttpServletResponse response, ModelMap map,
			User frontUser,
			@RequestParam(value = "smsRegisterSign", required = false)String smsRegisterSign) throws UnsupportedEncodingException {
		if(StringUtils.isBlank(frontUser.getUsername())){
			logger.warn("尝试注册微信用户但用户名为空");
			map.put("message", new EisMessage(EisError.REQUIRED_PARAMETER.getId(), "请输入用户名"));
			return frontMessageView;
		}
		if(dirtyDictService.isDirty(frontUser.getUsername())){
			logger.warn("尝试注册用户[" + frontUser.getUsername() + "]名称不能通过敏感词检查");
			map.put("message", new EisMessage(EisError.dataIllegal.getId(), "您的用户名不合法，请换一个注册"));
			return frontMessageView;
		}
		if(dirtyDictService.isDirty(frontUser.getNickName())){
			logger.warn("尝试注册用户[" + frontUser.getNickName() + "]的昵称不能通过敏感词检查");
			map.put("message", new EisMessage(EisError.dataIllegal.getId(), "您的昵称不合法，请换一个注册"));
			return frontMessageView;

		}
		long ownerId = (long)map.get("ownerId");
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return frontMessageView;		
		}
		frontUser.setOwnerId(ownerId);



		boolean isWeixinAccess = AgentUtils.isWeixinAccess(request.getHeader("user-agent"));
		if(!isWeixinAccess){
			logger.error("当前不是微信环境，不能使用微信注册");
			map.put("message", new EisMessage(EisError.VERSION_UNSUPPORTED.id,"请在微信环境下访问"));
			return frontMessageView;
		}
		userRegisterNeedSmsSign	= configService.getBooleanValue(DataName.userRegisterNeedSmsSign.toString(), ownerId);

		logger.debug("系统配置为用户注册" + (userRegisterNeedSmsSign ? "需要" : "不需要") + "手机验证，用户提交的手机验证码是:"+smsRegisterSign);

		if(userRegisterNeedSmsSign){
			Crypt crypt = new Crypt();
			crypt.setAesKey(aesKey);
			String sourceStr=cookieService.getCookie(request, DataName.smsRegisterSign.toString());
			if(sourceStr == null){
				logger.warn("用户Cookie中没有验证码数据");
				map.put("message", new EisMessage(EisError.VERIFY_ERROR.getId(), "对不起，您输入的验证码错误"));
				return frontMessageView;		
			} 

			String sign=crypt.aesDecrypt(sourceStr);
			logger.info("解密后的验证码数据是"+sign);
			if(sign == null){
				logger.warn("用户Cookie中的验证码数据解密错误:" + sourceStr);
				map.put("message", new EisMessage(EisError.VERIFY_ERROR.getId(), "对不起，您输入的验证码错误"));
				return frontMessageView;	
			}
			String[] data = sign.split("\\|");
			if(data == null || data.length != 4){
				logger.warn("用户Cookie中的验证码数据错误:" + sign);
				map.put("message", new EisMessage(EisError.VERIFY_ERROR.getId(), "对不起，您输入的验证码错误"));
				return frontMessageView;	
			}

			String sign2 = data[2];
			long ts = 0;
			if(NumericUtils.isNumeric(data[3])){
				ts = Long.parseLong(data[3]);
			}
			if(!sign2.equalsIgnoreCase(smsRegisterSign)){
				logger.error("用户提交的手机验证码是:" + smsRegisterSign + ",系统解密后的验证码是:" + sign2);
				map.put("message", new EisMessage(EisError.VERIFY_ERROR.getId(), "对不起，您输入的验证码错误"));
				return frontMessageView;	
			}
			if(ts > 0){
				long ttl = (new Date().getTime() - ts) / 1000;
				logger.debug("手机验证码生成时间是: " + sdf.format(new Date(ts)) + ",已存在" + ttl + "秒，有效期是:" + REGISTER_SMS_TTL);
				if( ttl > REGISTER_SMS_TTL){
					map.put("message", new EisMessage(EisError.requestTimeout.getId(), "验证码超时"));
					return frontMessageView;	
				}
			}
			//手机号就是用户名，自动绑定
			if(frontUser.getUserConfigMap() == null){
				frontUser.setUserConfigMap(new HashMap<String, UserData>());
				frontUser.getUserConfigMap().put(DataName.userBindPhoneNumber.toString(), new UserData(frontUser.getUuid(), DataName.userBindPhoneNumber.toString(), frontUser.getUsername()));
			}

		}
		String openId = cookieService.getCookie(request, "outUuid");
		if(StringUtils.isBlank(openId)){
			logger.error("当前是微信访问但无法获取用户openId");
			map.put("message", new EisMessage(EisError.userNotFoundInRequest.id,"无法获取微信用户信息"));
			return frontMessageView;
		} 		

		UserCriteria userCriteria = new UserCriteria(ownerId);
		//userCriteria.setAuthKey(openId);
		userCriteria.setUserTypeId(UserTypes.frontUser.getId());
		userCriteria.setUsername(frontUser.getUsername());
		List<User> existWeixinUserList = frontUserService.list(userCriteria);
		String autoCreateWeixinPassword = Crypt.passwordEncode(openId);

		if(existWeixinUserList != null && existWeixinUserList.size() > 0){
			logger.debug("已经有用户名为[" + userCriteria.getUsername() + "]的注册用户");
			frontUser = existWeixinUserList.get(0);
			if(StringUtils.isBlank(frontUser.getAuthKey())){
				logger.info("用户名为[" + userCriteria.getUsername() + "]的注册用户还不是微信用户，设置该用户与openId[" + openId + "]绑定");
				frontUser.setAuthKey(openId);
				try {
					frontUserService.update(frontUser);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				if(frontUser.getAuthKey().equals(openId)){
					logger.info("已经有用户[" + frontUser.getUuid() + "]名为[" + userCriteria.getUsername() + "]的注册用户并且openId[" + frontUser.getAuthKey() + "]与当前的openId[" + openId + "]一致，直接使用该用户，此情况不应出现");
				} else {
					logger.error("已经有用户[" + frontUser.getUuid() + "]名为[" + userCriteria.getUsername() + "]的注册用户但是openId[" + frontUser.getAuthKey() + "]与当前的openId[" + openId + "]不一致，不可绑定");

					map.put("message", new EisMessage(EisError.OBJECT_ALREADY_EXIST.getId(), "您的用户名已经被占用，请换一个注册"));
					return frontMessageView;
				}
			}
			if(autoCreateWeixinPassword.equals(frontUser.getUserPassword())){
				logger.debug("找到的微信用户密码是其openId加密，将状态设置为需要修改密码");
				frontUser.setCurrentStatus(UserStatus.needSetPassword.getId());
				try {
					frontUserService.update(frontUser);
				} catch (Exception e) {
					e.printStackTrace();
				}
				map.put("message", new EisMessage(Operate.jump.getId(),"请设置密码"));

				if(!request.getRequestURI().endsWith(".json")){
					logger.debug("找到了对应的微信用户[" + openId + "]但未设置密码，将重定向到登陆前URL:" + weixinCreatePasswordUrl);
					certifyService.redirectByResponse(request, response, UserTypes.frontUser.getId(), weixinCreatePasswordUrl, null, null);		
				}
				map.put("redirectUrl", weixinCreatePasswordUrl);
				return frontMessageView;
			} 
			frontUser.setCurrentStatus(UserStatus.normal.getId());


			if(loginAfterRegister){
				logger.debug("为已经存在的微信用户自动登录:" + login(frontUser, request, response, map));
				map.put("frontUser", frontUser);
			} else {
				map.remove("frontUser");
			}
			map.put("message", new EisMessage(OperateResult.success.getId(),"操作成功"));
			String redirectUrl = cookieService.getCookie(request, CommonStandard.COOKIE_REDIRECT_COOKIE_NAME + "_f");
			if(redirectUrl == null){
				redirectUrl = "/";
			}
			redirectUrl = java.net.URLDecoder.decode(redirectUrl,CommonStandard.DEFAULT_ENCODING);

			if(!request.getRequestURI().endsWith(".json")){
				logger.debug("当前用户登录成功并且不是JSON访问，将重定向到登陆前URL:" + redirectUrl);
				certifyService.redirectByResponse(request, response, UserTypes.frontUser.getId(), redirectUrl, null, null);		
			}
			map.put("redirectUrl", redirectUrl);
			return frontMessageView;

		}

		User weixinUser = weixinService.getUserInfo(openId, ownerId);
		if(weixinUser == null){
			logger.error("当前是微信访问但无法获取用户[openId=" + openId + "]的信息");
			map.put("message", new EisMessage(EisError.userNotFoundInRequest.id,"无法获取微信用户信息"));
			return frontMessageView;
		}
		frontUser.setNickName(weixinUser.getNickName());
		frontUser.setAuthKey(openId);
		frontUser.setUserConfigMap(weixinUser.getUserConfigMap());

		userRegisterNeedSmsSign	= configService.getBooleanValue(DataName.userRegisterNeedSmsSign.toString(), ownerId);


		frontUser.setUserTypeId(UserTypes.frontUser.getId());
		if( siteRegisterNeedPatchca ){
			boolean patchcaIsOk = false;
			try{
				String sessionPatchca = "";
				sessionPatchca = (String)request.getSession(true).getAttribute(CommonStandard.sessionCaptchaName);
				if(!sessionPatchca.equals("")){
					String userInputPatchca = request.getParameter(CommonStandard.sessionCaptchaName);
					logger.debug("session:" + sessionPatchca + ", input patchca:" + userInputPatchca);
					if(userInputPatchca != null){
						if(userInputPatchca.equalsIgnoreCase(sessionPatchca)){
							patchcaIsOk = true;
						}
					}
				}
			}catch(Exception e){}
			try{
				request.getSession().removeAttribute(CommonStandard.sessionCaptchaName);
			}catch(Exception e){}
			if(!patchcaIsOk){
				map.put("message", new EisMessage(EisError.VERIFY_ERROR.getId(), "对不起，请输入正确的验证码。"));
				return frontMessageView;
			}
		}
		logger.debug(">>>PERFORMANCE>>>开始检查唯一性globalUnique");
		if(globalUniqueService.exist(new GlobalUnique(frontUser.getUsername(), ownerId))){
			map.put("message", new EisMessage(EisError.OBJECT_ALREADY_EXIST.getId(), "您的用户名已经被占用，请换一个注册"));
			return frontMessageView;
		}
		logger.debug(">>>PERFORMANCE>>>完成检查唯一性globalUnique");

		logger.debug("尝试注册新用户:" + frontUser.getUsername() + "/" + frontUser.getUserPassword() + ",自动创建的微信密码是:" + autoCreateWeixinPassword);


		if(frontUser.getUserPassword() == null){
			logger.debug("创建的用户未提交密码，将状态设置为需要修改密码，并设置密码为根据openId自动创建的密码:" + autoCreateWeixinPassword);
			frontUser.setUserPassword(autoCreateWeixinPassword);
			frontUser.setCurrentStatus(UserStatus.needSetPassword.getId());
		} else {
			frontUser.setCurrentStatus(UserStatus.normal.getId());
		}
		//检查是否有invite编码
		String inviteCode = cookieService.getCookie(request, "inviteCode");
		if(StringUtils.isNotBlank(inviteCode)){
			logger.debug("正常注册微信用户拥有邀请码:" + inviteCode);
			cookieService.removeCookie(request, response, "inviteCode", null);
			frontUser.setExtraValue(DataName.userInviteByCode.toString(), inviteCode);
		}


		//获取用户注册时的IP
		String ip = IpUtils.getClientIp(request);
		frontUser.setExtraValue(DataName.userRegisterIp.toString(),ip);

		if(mailActive){
			frontUser.setCurrentStatus(UserStatus.unactive.getId());
		}


		logger.debug(">>>PERFORMANCE>>>开始插入用户数据");


		try{
			map.put("message", frontUserService.insert(frontUser));
		}catch(Exception e){
			map.put("message", new EisMessage(EisError.OBJECT_ALREADY_EXIST.getId(), "对不起，数据异常。"));
			logger.error(e.getMessage());
			e.printStackTrace();
			return frontMessageView;

		}
		logger.debug(">>>PERFORMANCE>>>完成插入用户数据，结束流程");
		/*if(fuuid > 0){//注册成功
			if(mailActive){
				map.put("mailActive", mailActive);
				mailActiveSign = Crypt.passwordEncode(Constants.mailActiveKey + fuuid);	
				frontUser = frontUserService.select(fuuid);
				UserData userData = new UserData();
				userData.setDataCode(Constants.DataName.userMailActiveSign.toString());
				userData.setDataDescription("邮箱激活签名");
				userData.setDataValue(mailActiveSign);
				frontUser.getUserConfigMap().put(Constants.DataName.userMailActiveSign.toString(), userData);

				userData = new UserData();
				userData.setDataCode(Constants.DataName.userBindMailBox.toString());
				userData.setDataDescription("绑定/激活邮箱");
				userData.setDataValue(frontUser.getUsername());
				frontUser.getUserConfigMap().put(Constants.DataName.userBindMailBox.toString(), userData);
				frontUserService.update(frontUser);

				String activeUrl = "http://www." + configService.getValue("system_domain") + "/user/" + fuuid + "/postMailActive/" + mailActiveSign;
				frontUserService.sendActivemail(frontUser.getUserConfigMap().get(Constants.DataName.userBindMailBox.toString()).getDataValue(), activeUrl);
				logger.debug("需要邮件激活，为用户[" + frontUser.getUuid() + "]发送邮件到" + frontUser.getUserConfigMap().get(Constants.DataName.userBindMailBox.toString()).getDataValue() + ",激活链接是:" + activeUrl);
				map.put("message", new EisMessage(Constants.OperateResult.success.getId(),"注册成功，请使用邮件等待激活"));
			} else {
				EisMessage sm = new EisMessage(Constants.OperateResult.success.getId(),"注册成功");
				if(request.getSession().getAttribute(Constants.sessionReturnUrlName) != null){
					sm.setMessage((String)request.getSession().getAttribute(Constants.sessionReturnUrlName));
				}
				map.put("message", sm);
				if(loginAfterRegister){
					login(frontUser,request,response,map);
				}


			}
		} else {
			map.put("message", new EisMessage(Constants.OperateResult.failed.getId(), "对不起，系统繁忙，请稍后再试。"));

		}*/
		if(frontUser.getCurrentStatus() == UserStatus.needSetPassword.getId()){
			map.put("message", new EisMessage(Operate.jump.getId(),"请设置密码"));
			if(!request.getRequestURI().endsWith(".json")){
				certifyService.redirectByResponse(request, response, UserTypes.frontUser.getId(), weixinCreatePasswordUrl, null, null);
				return null;
			}
			map.put("redirectUrl", weixinCreatePasswordUrl);
			return frontMessageView;
		}
		logger.debug("注册后是否登录:" + loginAfterRegister);

		if(loginAfterRegister){
			logger.debug("新增用户密码是:" +  frontUser.getUserPassword());
			logger.debug("尝试在注册后自动登录:" + login(frontUser, request, response, map));
			map.put("frontUser", frontUser);
		} else {
			map.remove("frontUser");
			map.put("message", new EisMessage(OperateResult.success.getId(),"注册成功"));
		}

		String redirectUrl = cookieService.getCookie(request, CommonStandard.COOKIE_REDIRECT_COOKIE_NAME + "_f");
		if(redirectUrl == null){
			redirectUrl = request.getHeader("referer") == null ? "/" : request.getHeader("referer");
		}
		if(!request.getRequestURI().endsWith(".json")){

			logger.debug("当前用户登录成功并且不是JSON访问，将重定向到登陆前URL:" + redirectUrl);
			response.setStatus(HttpStatus.TEMPORARY_REDIRECT.value());
			try {
				response.sendRedirect(redirectUrl);
			} catch (IOException e) {
				e.printStackTrace();
			}		
			return null;
		}
		map.put("redirectUrl", redirectUrl);
		return frontMessageView;

	}

	/**
	 * 检查微信环境下，一个来访用户是否已经存在
	 * 如果已经登录，直接跳转，如果已经存在存在
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value="/check")
	@IgnoreLoginCheck
	public String checkWeixinUser(HttpServletRequest request,HttpServletResponse response, ModelMap map) throws UnsupportedEncodingException{



		long ownerId = (long)map.get("ownerId");
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return CommonStandard.frontMessageView;		
		}

		User frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
		if(frontUser != null){
			String redirectUrl = cookieService.getCookie(request, CommonStandard.COOKIE_REDIRECT_COOKIE_NAME + "_f");
			if(redirectUrl == null){
				redirectUrl = "/";
			}
			redirectUrl = java.net.URLDecoder.decode(redirectUrl,CommonStandard.DEFAULT_ENCODING);

			//该OpenId用户已存在
			logger.debug("用户已登录，无需检查微信用户状态,跳转到:" + redirectUrl);

			try {
				response.sendRedirect(redirectUrl);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		boolean isWeixinAccess = AgentUtils.isWeixinAccess(request.getHeader("user-agent"));
		if(!isWeixinAccess){
			logger.error("当前不是微信环境，不能使用微信注册");
			map.put("message", new EisMessage(EisError.VERSION_UNSUPPORTED.id,"请在微信环境下访问"));
			return frontMessageView;
		}
		String openId = cookieService.getCookie(request, "outUuid");
		if(StringUtils.isBlank(openId)){
			logger.error("当前是微信访问但无法获取用户openId");
			map.put("message", new EisMessage(EisError.userNotFoundInRequest.id,"无法获取微信用户信息"));
			return frontMessageView;
		} 			
		UserCriteria userCriteria = new UserCriteria();
		userCriteria.setUserTypeId(UserTypes.frontUser.getId());
		userCriteria.setAuthKey(openId);
		List<User> userList = frontUserService.list(userCriteria);
		logger.debug("根据openId=" + openId + "]查询authKey等于它的用户数量是:" + (userList == null ? "空" : userList.size()));
		if(userList == null || userList.size() < 1){
			//发送到填写信息页面
			logger.debug("openId=" + openId + "的用户不存在，跳转到微信用户注册页面:" +  weixinRegisterUrl);
			certifyService.redirectByResponse(request, response, UserTypes.frontUser.getId(), weixinRegisterUrl, null, null);

			return null;
		}
		frontUser = userList.get(0);
		if(frontUser.getUsername() == null || frontUser.getUsername().equals(frontUser.getAuthKey())){
			logger.debug("用户[" + frontUser.getUuid() + "]的username和authKey一样，没有填写过完善信息，跳转到:" + weixinRegisterUrl);
			certifyService.redirectByResponse(request, response, UserTypes.frontUser.getId(), weixinRegisterUrl, null, null);

			return null;
		}
		if(frontUser.getCurrentStatus() == UserStatus.needSetPassword.getId()){
			logger.debug("用户[" + frontUser.getUuid() + "]状态是需要设置密码，跳转到:" + weixinCreatePasswordUrl);
			certifyService.redirectByResponse(request, response, UserTypes.frontUser.getId(), weixinCreatePasswordUrl, null, null);

			return null;

		}
		//已存在该用户,返回到redirectUrl;
		String redirectUrl = cookieService.getCookie(request, CommonStandard.COOKIE_REDIRECT_COOKIE_NAME + "_f");
		if(redirectUrl == null){
			redirectUrl = "/";
		}
		//该OpenId用户已存在
		frontUser = certifyService.forceLogin(request, response, openId);
		logger.debug("openId=" + openId + "的微信用户已存在，登陆:" + frontUser + ",并跳转到:" + redirectUrl);
		certifyService.redirectByResponse(request, response, UserTypes.frontUser.getId(), weixinRegisterUrl, null, null);
		return null;


	}

	@RequestMapping(value="/register")
	@IgnoreLoginCheck
	public String weixinRegister(HttpServletRequest request,HttpServletResponse response, ModelMap map){
		long ownerId = (long)map.get("ownerId");
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return CommonStandard.frontMessageView;		
		}
		return "user/weixinRegister";


	}

	/**
	 * 成功注册但还未设置密码的用户
	 * 显示这个界面来提交密码
	 */
	@RequestMapping(value="/createPassword", method=RequestMethod.GET)
	@IgnoreLoginCheck
	public String getCreatePassword(HttpServletRequest request,HttpServletResponse response, ModelMap map){
		long ownerId = (long)map.get("ownerId");
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return CommonStandard.frontMessageView;		
		}
		return "user/weixinCreatePassword";


	}

	/**
	 * 成功注册但还未设置密码的用户
	 * 提交密码
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value="/createPassword", method=RequestMethod.POST)
	@IgnoreLoginCheck
	public String submitCreatePassword(HttpServletRequest request,HttpServletResponse response, ModelMap map, String userPassword) throws UnsupportedEncodingException{
		long ownerId = (long)map.get("ownerId");
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return CommonStandard.frontMessageView;		
		}

		String openId = cookieService.getCookie(request, "outUuid");
		if(StringUtils.isBlank(openId)){
			logger.error("当前是微信访问但无法获取用户openId");
			map.put("message", new EisMessage(EisError.userNotFoundInRequest.id,"无法获取微信用户信息"));
			return frontMessageView;
		} 	

		UserCriteria userCriteria = new UserCriteria(ownerId);
		userCriteria.setAuthKey(openId);
		userCriteria.setUserTypeId(UserTypes.frontUser.getId());
		userCriteria.setCurrentStatus(UserStatus.needSetPassword.getId());

		List<User> existWeixinUserList = frontUserService.list(userCriteria);
		if(existWeixinUserList == null || existWeixinUserList.size() < 1){
			logger.debug("没有找到authKey=" + openId + "并且状态是需要设置密码的注册用户");
			map.put("message", new EisMessage(EisError.userNotFoundInRequest.id,"找不到用户数据"));
			return frontMessageView;
		}
		User frontUser = existWeixinUserList.get(0);
		frontUser.setUserPassword(userPassword);
		frontUser.setCurrentStatus(UserStatus.normal.getId());	
		frontUser.setAuthKey(openId);
		EisMessage result = null;;
		try {
			result = frontUserService.update(frontUser);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(result == null || result.getOperateCode() != OperateResult.success.getId()){
			logger.error("无法修改用户信息:" + result);
			map.put("message", new EisMessage(EisError.systemDataError.id,"更新数据失败"));
			return frontMessageView;
		}
		map.put("message", result);
		frontUser = certifyService.forceLogin(request, response, openId);

		String redirectUrl = cookieService.getCookie(request, CommonStandard.COOKIE_REDIRECT_COOKIE_NAME + "_f");
		if(redirectUrl == null){
			redirectUrl = "/";
		}
		redirectUrl = java.net.URLDecoder.decode(redirectUrl,CommonStandard.DEFAULT_ENCODING);

		if(!request.getRequestURI().endsWith(".json")){
			logger.debug("修改密码成功并且不是JSON访问，将重定向到登陆前URL:" + redirectUrl);
			certifyService.redirectByResponse(request, response, UserTypes.frontUser.getId(), redirectUrl, null, null);		
		}
		logger.debug("修改密码成功但是JSON访问，将把重定向地址[" + redirectUrl + "]放入map");
		map.put("redirectUrl", redirectUrl);
		return frontMessageView;


	}


	private int login(User user, HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		int loginErrorCount = 0;
		try{
			loginErrorCount = (Integer)request.getSession().getAttribute("loginErrorCount");
		}catch(Exception e){}

		if(user == null){
			logger.error("Passport null.");
			map.put("json", new EisMessage(EisError.AUTH_FAIL.getId(), "对不起，请检查您的用户名或密码。"));
			request.getSession().setAttribute("loginErrorCount", loginErrorCount+1);
			return EisError.AUTH_FAIL.getId();		
		}

		logger.debug("用户[" + user.getUsername() + "/" + user.getUserPassword() + "]尝试登录");
		if(user.getUsername() == null || user.getUserPassword() == null){
			logger.error("Passport username or password null.");
			map.put("json", new EisMessage(EisError.AUTH_FAIL.getId(), "对不起，请检查您的用户名或密码。"));
			request.getSession().setAttribute("loginErrorCount", loginErrorCount+1);
			return EisError.AUTH_FAIL.getId();		
		}
		if(user.getUsername().equals("") || user.getUserPassword().equals("")){
			logger.error("Passport username or password empty.");
			map.put("json", new EisMessage(EisError.AUTH_FAIL.getId(), "对不起，请检查您的用户名或密码。"));
			request.getSession().setAttribute("loginErrorCount", loginErrorCount+1);
			return EisError.AUTH_FAIL.getId();		
		}
		UserCriteria frontUserCriteria = new UserCriteria();
		frontUserCriteria.setUsername(user.getUsername());
		frontUserCriteria.setUserPassword(user.getUserPassword());
		frontUserCriteria.setCurrentStatus(UserStatus.normal.getId());
		frontUserCriteria.setUserTypeId(UserTypes.frontUser.getId());
		User frontUser = certifyService.login(request, response, frontUserCriteria);

		if(frontUser != null){//登录成功
			logger.debug("用户[" + user.getUsername() + "/" + user.getUserTypeId() + "/" + user.getCurrentStatus() + "] 已成功登录");
			map.put("message", new EisMessage(OperateResult.success.getId(), "登录成功"));
			User fUser = frontUser.clone();
			fUser.setMessageId(fUser.getExtraValue(DataName.supplierLoginKey.toString()));
			map.put("frontUser", fUser);
			return OperateResult.success.getId();		

		} else {
			logger.debug("用户[" + user.getUsername() + "无法登录");

		}

		request.getSession().setAttribute("loginErrorCount", loginErrorCount+1);
		return EisError.AUTH_FAIL.getId();		
	}

	/**
	 * 通过客户端提交的一个access_code来进一步获取微信用户信息，如果该用户不存在，则创建
	 * @return
	 */
	@RequestMapping(value="/createByAccessCode"/*, method=RequestMethod.POST*/)
	public String createUserByAccessCode(HttpServletRequest request,HttpServletResponse response, ModelMap map, String accessCode){
		logger.debug("收到创建微信认证用户的请求,accessCode=" + accessCode);
		if(StringUtils.isBlank(accessCode)){
			logger.error("空的accessCode");
			return frontMessageView;
		}
		
		map.put("operate", OperateCode.WEIXIN_USER_SYNC.toString());

		long ownerId = (long)map.get("ownerId");
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),"找不到对应的地址","请尝试访问其他页面或返回首页"));
			return frontMessageView;		
		}
		PayChannelMechInfo mechInfo = getMechInfo(ServletRequestUtils.getStringParameter(request, "appCode",DEFAULT_APP_CODE), ownerId);
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + mechInfo.accountId + "&secret=" + mechInfo.cryptKey + "&code=" + accessCode + "&grant_type=authorization_code";
		logger.debug("准备访问URL[" + url + "]获取用户信息");
		HttpClient httpClient = HttpClientPoolV3.getHttpClient(WeixinConfig.API_HOST, WeixinConfig.API_PORT);
		String result = null;
		try{
			GetMethod method = new GetMethod(url);
			httpClient.executeMethod(method);
			result = method.getResponseBodyAsString();
		}catch(Exception e){
			e.printStackTrace();
		}
		logger.debug("访问微信获取ACCESS_TOKEN返回:" + result);
		JsonNode json = null;
		try{
			json = om.readTree(result);
		}catch(Exception e){
			e.printStackTrace();
		}
		String accessToken = json.get("access_token").textValue();
		String openId = json.get("openid").textValue();
		String unionId = json.get("unionid").textValue();
		logger.debug("获取到用户openId=" + openId + ",unionId=" + unionId + ",accessToken=" + accessToken);

		url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" + openId;
		try{
			GetMethod method = new GetMethod(url);
			httpClient.executeMethod(method);
			result = method.getResponseBodyAsString();
		}catch(Exception e){
			e.printStackTrace();
		}
		logger.debug("访问微信获取用户[openId=" + openId + ",unionId=" + unionId + "]的信息，返回:" + result);
		try{
			json = om.readTree(result);
		}catch(Exception e){
			e.printStackTrace();
		}
		if(StringUtils.isBlank(unionId)){
			unionId = json.get("unionid").textValue();

		}

		UserCriteria frontUserCriteria = new UserCriteria();
		frontUserCriteria.setUsername(unionId);
		frontUserCriteria.setUserTypeId(UserTypes.frontUser.getId());
		//查找是否有同名用户存在，如果没有，创建新用户
		int existCount = frontUserService.count(frontUserCriteria);
		if(existCount < 1){
			logger.info("找不到用户名=" + unionId + "的微信APP前端用户，自动创建");

			User frontUser = new User();
			frontUser.setUsername(unionId);
			frontUser.setCurrentStatus(UserStatus.normal.getId());

			String inviteCode = ServletRequestUtils.getStringParameter(request,"inviteCode", null);
			if(StringUtils.isBlank(inviteCode)){
				inviteCode =ServletRequestUtils.getStringParameter(request, "i",null);
			}
			if(StringUtils.isNotBlank(inviteCode)){
				logger.debug("注册用户拥有邀请码:" + inviteCode);
				frontUser.setExtraValue(DataName.userInviteByCode.toString(), inviteCode);
			}

			String loginKey = Crypt.shortMd5(DigestUtils.md5Hex(frontUser.getUsername() + System.currentTimeMillis()));
			frontUser.setAuthKey(loginKey);			
			logger.info("为新注册用户[" + frontUser.getUsername() + "]生成登陆密钥:" + loginKey);
			if(StringUtils.isBlank(frontUser.getUserPassword())){

				frontUser.setUserPassword(unionId);
				logger.info("尝试注册的用户名和密码都为空，尝试为用户[" + unionId + "]设置与用户名一致的密码:" + frontUser.getUserPassword());
			}
			String nickName = json.path("nickname").textValue();
			if(StringUtils.isNotBlank(nickName)){
				Pattern emoji = Pattern.compile ("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]" ,Pattern . UNICODE_CASE | Pattern . CASE_INSENSITIVE ) ;
				Matcher matcher = emoji.matcher(json.path("nickname").textValue());  
				try {
					nickName = new String( matcher.replaceAll("*").getBytes("ISO-8859-1"),"UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				frontUser.setNickName(nickName);
				logger.debug("设置用户" + openId + "的昵称为:" + frontUser.getNickName());
			}
			if(json.path("sex").intValue() == 2){
				frontUser.setGender(UserCriteria.GENDER_GIRL);
			} else {
				frontUser.setGender(UserCriteria.GENDER_BOY);
			}
			String headImageUrl = json.path("headimgurl").textValue();
			if(StringUtils.isNotBlank(headImageUrl)){
				logger.info("为用户[" + frontUser.getNickName()  + "]设置头像:" + headImageUrl );
				frontUser.setExtraValue(DataName.userHeadPic.toString(), headImageUrl);
			}
			String country = json.path("country").asText();
			if(StringUtils.isNotBlank(country)){
				logger.debug("为用户[" + openId + "]设置国家:" + country);
				frontUser.setExtraValue(DataName.country.toString(), country);
			}
			String province = json.path("province").asText();
			if(StringUtils.isNotBlank(province)){
				logger.debug("为用户[" + openId + "]设置省份:" + province);
				frontUser.setExtraValue(DataName.province.toString(), province);
			}			
			String city = json.path("city").asText();
			if(StringUtils.isNotBlank(city)){
				logger.debug("为用户[" + openId + "]设置城市:" + city);
				frontUser.setExtraValue( DataName.city.toString(), city);
			}
			String language = json.path("language").asText();
			if(StringUtils.isNotBlank(language)){
				logger.debug("为用户[" + openId + "]设置语言:" + language);
				frontUser.setExtraValue(DataName.language.toString(), language);
			}

			frontUser.setOwnerId(ownerId);
			frontUser.setUserTypeId(UserTypes.frontUser.getId());
			if(globalUniqueService.exist(new GlobalUnique(frontUser.getUsername(), ownerId))){
				map.put("message", new EisMessage(EisError.OBJECT_ALREADY_EXIST.getId(), "无法创建新用户"));
				return frontMessageView;				
			}
			int createRs = 0;
			try {
				createRs = frontUserService.insert(frontUser);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(createRs != 1){
				map.put("message", new EisMessage(EisError.OBJECT_ALREADY_EXIST.getId(), "无法创建新用户"));
				return frontMessageView;		
			}
			map.put("username", unionId);
			return frontMessageView;		

		} else {
			logger.info("用户名=" + unionId + "的前端用户已存在");

			frontUserCriteria.setCurrentStatus(UserStatus.normal.getId());
			frontUserService.list(frontUserCriteria);
			List<User> frontUserList = frontUserService.list(frontUserCriteria);
			if(frontUserList == null || frontUserList.size() != 1){
				map.put("message", new EisMessage(EisError.userNotFoundInSystem.id,"找不到指定的用户"));

			} else {
				map.put("username", unionId);


			}
			return frontMessageView;		


		}




	}

	/**
	 * 通过客户端提交的一个access_code来进一步获取微信用户信息，如果该用户不存在，则创建
	 * @return
	 */
	@RequestMapping(value="/createQQUser", method=RequestMethod.POST)
	public String createQQUser(HttpServletRequest request,HttpServletResponse response, ModelMap map, String openId, String gender, String nickName, String headImage){
		logger.debug("收到创建QQ认证用户的请求,openId=" + openId + ",nickName=" + nickName + ",gender=" + gender + ",headImage=" + headImage);
		if(StringUtils.isBlank(openId)){
			logger.error("空的openId");
			return frontMessageView;
		}
		long ownerId = (long)map.get("ownerId");
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),"找不到对应的地址","请尝试访问其他页面或返回首页"));
			return frontMessageView;		
		}


		UserCriteria frontUserCriteria = new UserCriteria();
		frontUserCriteria.setUsername(openId);
		frontUserCriteria.setUserTypeId(UserTypes.frontUser.getId());
		//查找是否有同名用户存在，如果没有，创建新用户
		int existCount = frontUserService.count(frontUserCriteria);
		if(existCount < 1){
			logger.info("找不到用户名=" + openId + "的QQ前端用户，自动创建");

			User frontUser = new User();
			frontUser.setUsername(openId);
			frontUser.setCurrentStatus(UserStatus.normal.getId());

			String inviteCode = ServletRequestUtils.getStringParameter(request,"inviteCode", null);
			if(StringUtils.isBlank(inviteCode)){
				inviteCode =ServletRequestUtils.getStringParameter(request, "i",null);
			}
			if(StringUtils.isNotBlank(inviteCode)){
				logger.debug("注册用户拥有邀请码:" + inviteCode);
				frontUser.setExtraValue(DataName.userInviteByCode.toString(), inviteCode);
			}

			String loginKey = Crypt.shortMd5(DigestUtils.md5Hex(frontUser.getUsername() + System.currentTimeMillis()));
			frontUser.setAuthKey(loginKey);			
			logger.info("为新注册用户[" + frontUser.getUsername() + "]生成登陆密钥:" + loginKey);
			if(StringUtils.isBlank(frontUser.getUserPassword())){

				frontUser.setUserPassword(openId);
				logger.info("尝试注册的用户名和密码都为空，尝试为用户[" + openId + "]设置与用户名一致的密码:" + frontUser.getUserPassword());
			}
			if(StringUtils.isNotBlank(nickName)){
				/*Pattern emoji = Pattern.compile ("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]" ,Pattern . UNICODE_CASE | Pattern . CASE_INSENSITIVE ) ;
				Matcher matcher = emoji.matcher(nickName);  
				try {
					nickName = new String( matcher.replaceAll("*").getBytes("ISO-8859-1"),"UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}*/				frontUser.setNickName(nickName);
				logger.debug("设置用户" + openId + "的昵称为:" + frontUser.getNickName());
			}
			if(gender != null && gender.equals("女")){
				frontUser.setGender(UserCriteria.GENDER_GIRL);
			} else {
				frontUser.setGender(UserCriteria.GENDER_BOY);
			}
			if(StringUtils.isNotBlank(headImage)){
				try {
					headImage = URLDecoder.decode(headImage,"UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				logger.info("为用户[" + frontUser.getNickName()  + "]设置头像:" + headImage );
				frontUser.setExtraValue(DataName.userHeadPic.toString(), headImage);
			}
			String country = ServletRequestUtils.getStringParameter(request, "country",null);
			if(StringUtils.isNotBlank(country)){
				logger.debug("为用户[" + openId + "]设置国家:" + country);
				frontUser.setExtraValue(DataName.country.toString(), country);
			}
			String province = ServletRequestUtils.getStringParameter(request, "province",null);
			if(StringUtils.isNotBlank(province)){
				logger.debug("为用户[" + openId + "]设置省份:" + province);
				frontUser.setExtraValue(DataName.province.toString(), province);
			}			
			String city = ServletRequestUtils.getStringParameter(request, "city",null);
			if(StringUtils.isNotBlank(city)){
				logger.debug("为用户[" + openId + "]设置城市:" + city);
				frontUser.setExtraValue( DataName.city.toString(), city);
			}

			frontUser.setOwnerId(ownerId);
			frontUser.setUserTypeId(UserTypes.frontUser.getId());
			if(globalUniqueService.exist(new GlobalUnique(frontUser.getUsername(), ownerId))){
				map.put("message", new EisMessage(EisError.OBJECT_ALREADY_EXIST.getId(), "无法创建新用户"));
				return frontMessageView;				
			}
			int createRs = 0;
			try {
				createRs = frontUserService.insert(frontUser);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(createRs != 1){
				map.put("message", new EisMessage(EisError.OBJECT_ALREADY_EXIST.getId(), "无法创建新用户"));
				return frontMessageView;		
			}
			map.put("username", openId);
			return frontMessageView;		

		} else {
			logger.info("用户名=" + openId + "的前端用户已存在");

			frontUserCriteria.setCurrentStatus(UserStatus.normal.getId());
			frontUserService.list(frontUserCriteria);
			List<User> frontUserList = frontUserService.list(frontUserCriteria);
			if(frontUserList == null || frontUserList.size() != 1){
				map.put("message", new EisMessage(EisError.userNotFoundInSystem.id,"找不到指定的用户"));

			} else {
				map.put("username", openId);			

			}
			return frontMessageView;		


		}

	}




	private PayChannelMechInfo getMechInfo(String appCode, long ownerId){

		if(StringUtils.isBlank(appCode)){
			appCode = "";
		} else {
			appCode += "_";
		}
		appCode = appCode.toUpperCase();
		PayChannelMechInfo weixinMechInfo = null;
		if(mechCache != null && mechCache.size() > 0){
			weixinMechInfo = mechCache.get(String.valueOf(ownerId));
			if(weixinMechInfo != null){
				logger.debug("从缓存中获取到ownerId=" + ownerId + ",appCode=" + appCode + "的微信APP商户信息:" + weixinMechInfo);
				return weixinMechInfo;
			}
		}
		weixinMechInfo = new PayChannelMechInfo();
		String key = configService.getValue(appCode + "APP_WEIXIN_PAY_KEY", ownerId);
		if(key != null){
			weixinMechInfo.payKey = key;
		}
		String appId = configService.getValue(appCode + "APP_WEIXIN_APP_ID", ownerId);
		if(appId != null){
			weixinMechInfo.accountId = appId;
		}
		String weixinPayMechId = configService.getValue(appCode + "APP_WEIXIN_PAY_MECH_ID", ownerId);
		if(weixinPayMechId != null){
			weixinMechInfo.accountName = weixinPayMechId;
		}
		String weixinCryptKey = configService.getValue(appCode +"APP_WEIXIN_CRYPT_KEY", ownerId);
		if(weixinCryptKey != null){
			weixinMechInfo.cryptKey = weixinCryptKey;
		}
		synchronized(this){
			mechCache.put(String.valueOf(ownerId), weixinMechInfo);
		}
		logger.debug("把ownerId=" + ownerId + "的微信APP商户信息:" + weixinMechInfo + "放入缓存");
		return weixinMechInfo;
	}


}


