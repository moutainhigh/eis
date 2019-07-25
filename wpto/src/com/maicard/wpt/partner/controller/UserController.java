package com.maicard.wpt.partner.controller;

import static com.maicard.standard.CommonStandard.frontMessageView;
import static com.maicard.standard.CommonStandard.partnerMessageView;
import java.io.IOException;
import java.net.URLDecoder;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.maicard.annotation.IgnoreLoginCheck;
import com.maicard.annotation.IgnorePrivilegeCheck;
import com.maicard.captcha.service.CaptchaService;
import com.maicard.common.base.BaseController;
import com.maicard.common.criteria.SecurityLevelCriteria;
import com.maicard.common.domain.CacheValue;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.domain.GlobalUnique;
import com.maicard.common.domain.SecurityLevel;
import com.maicard.common.service.CacheService;
import com.maicard.common.service.CenterDataService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.CookieService;
import com.maicard.common.service.DataDefineService;
import com.maicard.common.service.DirtyDictService;
import com.maicard.common.service.GlobalUniqueService;
import com.maicard.common.service.SecurityLevelService;
import com.maicard.common.util.IpUtils;
import com.maicard.common.util.Crypt;
import com.maicard.common.util.NumericUtils;
import com.maicard.common.util.RSAUtils;
import com.maicard.common.util.SecurityLevelUtils;
import com.maicard.exception.ObjectNotFoundByIdException;
import com.maicard.exception.RequiredParameterIsNullException;
import com.maicard.exception.UserNotFoundInRequestException;
import com.maicard.mb.domain.UserMessage;
import com.maicard.mb.service.UserMessageService;
import com.maicard.security.criteria.OperateLogCriteria;
import com.maicard.security.criteria.UserCriteria;
import com.maicard.security.domain.OperateLog;
import com.maicard.security.domain.User;
import com.maicard.security.domain.UserData;
import com.maicard.security.domain.UserRoleRelation;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.OperateLogService;
import com.maicard.security.service.PartnerRoleRelationService;
import com.maicard.security.service.PartnerService;
import com.maicard.security.service.UserDataService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.KeyConstants;
import com.maicard.standard.ObjectType;
import com.maicard.standard.Operate;
import com.maicard.standard.OperateCode;
import com.maicard.standard.OperateResult;
import com.maicard.standard.MessageStandard.MessageStatus;
import com.maicard.standard.MessageStandard.UserMessageSendMethod;
import com.maicard.standard.SecurityStandard.UserStatus;
import com.maicard.standard.SecurityStandard.UserTypes;

/**
 * 合作伙伴用户控制器
 * @author NetSnake
 * @date 2012-3-29
 */

@Controller
@RequestMapping("/user")
public class UserController  extends BaseController{	

	@Resource
	private CacheService cacheService;
	@Resource
	private CaptchaService captchaService;
	@Resource
	private CertifyService certifyService;

	@Resource
	private CenterDataService centerDataService;

	@Resource
	private ConfigService configService;
	@Resource
	private CookieService cookieService;


	@Resource
	private DirtyDictService dirtyDictService;

	@Resource
	private DataDefineService dataDefineService;
	@Resource
	private GlobalUniqueService globalUniqueService;
	@Resource
	private SecurityLevelService securityLevelService;
	@Resource
	private OperateLogService operateLogService;
	@Resource
	private UserDataService userDataService;

	@Resource
	private UserMessageService userMessageService;

	@Resource
	private PartnerRoleRelationService partnerRoleRelationService;

	@Resource
	private PartnerService partnerService;	

	private final int securityLevelId =  SecurityLevelUtils.getSecurityLevel();

	private boolean siteRegisterNeedPatchca;

	private final String loginView = "common/user/login";
	private final String secAuthloginView = "common/user/secAuthLogin";
	private final String rsaCacheName = "rsa";
	private final long rsaKeyTtl = 600;
	private SecurityLevel securityLevel;
	private final SimpleDateFormat sdf = new SimpleDateFormat(CommonStandard.defaultDateFormat);

	private boolean GUIDE_ENABLED = true;

	/**
	 * 注册用户默认的角色ID
	 */
	private final int AUTO_RELATION_ROLE_ID = 1000;

	/**
	 * 注册用户默认的扩展类型
	 */
	private final int REGISTER_PARTNER_EXTRA_TYPE_ID = 1;

	//注册时手机验证码有效期
	private int SMS_VALID_SECOND = 120;

	private final String DEFAULT_SMS_VALIDATE_MESSAGE = "短信验证码为${smsCode},请勿将验证码提供给他人。";
	//private final String DEFAULT_FORGET_PASSWORD_SMS_VALIDATE_MESSAGE = "找回密码短信验证码为${smsCode},请勿将验证码提供给他人。";
	//private final String DEFAULT_BIND_PHONE_VALIDATE_MESSAGE = "绑定手机短信验证码为${smsCode},请勿将验证码提供给他人。";

	@PostConstruct
	public void init(){
		securityLevel = securityLevelService.select(securityLevelId);
	}



	//默认请求，给出登录用户的信息
	@RequestMapping(method = RequestMethod.GET)
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		User user = null;
		try{
			user = certifyService.getLoginedUser(request, response, UserTypes.partner.id);
		}catch(Exception e){

		}
		logger.debug("当前用户:" + user);
		if(user == null || user.getUuid() < 1){
			map.put("message", new EisMessage(EisError.userNotFoundInSession.id, "您可能尚未登录，或会话已过期"));
			return CommonStandard.partnerMessageView;	
		}

		user.setUserPassword(null);
		map.put("user", user);		
		return "common/user/list";
	}

	@RequestMapping(value="/update/{uuid}", method=RequestMethod.GET)
	public String listUser(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@PathVariable("uuid") long uuid,@ModelAttribute("partner") User partner) throws Exception {

		if(uuid == 0){
			throw new RequiredParameterIsNullException("请求中找不到必须的参数[uuid]");
		}
		User partUser = partnerService.select(uuid);
		if(partUser == null){
			throw new ObjectNotFoundByIdException("找不到ID=" + uuid + "的sysUser对象");			
		}
		map.put("partner", partUser.clone());


		return "common/partner/update";
	}

	/*
	 * 显示修改密码的界面
	 */
	@RequestMapping(value="/update/userPassword", method=RequestMethod.GET)
	public String getUpdatePassword(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		final String view = "common/user/update/userPassword";
		writeRsa(request,map);
		return view;
	}
	@RequestMapping(value="/update/userPassword", method=RequestMethod.POST)
	public String postUpdatePassword(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			String oldPassword, String userPassword, String userPassword2) throws Exception {
		final String view = "common/user/update/userPassword";
		EisMessage message = null;
		if(!userPassword.equals(userPassword2)){
			map.put("message", new EisMessage(EisError.twicePasswordNotMatch.id,"密码不一致"));
			writeRsa(request,map);
			return view;
		}

		String decryptedOldPassword = decryptPassword(oldPassword);
		String decryptedPassword = decryptPassword(userPassword);
		writeRsa(request,map);

		if(decryptedPassword == null){
			logger.error("RSA解密密码失败");
			return view;
		}
		if(decryptedOldPassword == null){
			logger.error("RSA解密密码失败");
			return view;
		}
		User partner  = certifyService.getLoginedUser(request, response, UserTypes.partner.id);
		if(partner == null){
			throw new UserNotFoundInRequestException();
		}
		//logger.debug("用户[" + partner.getUuid() + "]请求修改密码，旧密码:" + decryptedOldPassword + ",新密码:" + decryptedPassword);
		//检查旧密码
		if(partner.getUserPassword() == null){
			//重新获取密码
			partner = partnerService.select(partner.getUuid());
		}
		boolean oldPasswordIsOk = false;
		logger.debug("用户[" + partner.getUuid() + "]当前密码是[" + partner.getUserPassword() + "]，提供验证的旧密码是:" + decryptedOldPassword);
		if(partner.getUserPassword().length() == 64){
			//是加密后的密码
			String shaOldPassword = Crypt.passwordEncode(decryptedOldPassword);
			if(shaOldPassword.equals(partner.getUserPassword())){
				oldPasswordIsOk = true;
			}
		} else {
			if(userPassword.equals(partner.getUserPassword())){
				oldPasswordIsOk = true;
			}
		}
		if(!oldPasswordIsOk){
			logger.debug("用户[" + partner.getUuid() + "]当前密码是[" + partner.getUserPassword() + "]，提供验证的旧密码是:" + decryptedOldPassword + "，不一致，不能修改新密码");
			map.put("message", new EisMessage(EisError.oldPasswordNotMatch.id,"旧密码错误"));
			return view;
		}
		EisMessage passwordIsFine = certifyService.passwordIsFine(partner, decryptedPassword);
		if(passwordIsFine.getOperateCode() != OperateResult.success.id){
			logger.info("尝试修改的密码无法通过检查:" + passwordIsFine);
			map.put("message", passwordIsFine);
			return view;
		}
		logger.debug("用户[" + partner.getUuid() + "]输入的旧密码一致，将修改新密码");

		partner.setUserPassword(decryptedPassword);
		/*
		if(partnerService.update(currentPartner) > 0){
			message = new EisMessage(OperateResult.success.id,"操作完成");		
		} else {
			message = new EisMessage(OperateResult.failed.id,"操作失败");
		}*/

		map.put("message", message);	
		return view;
	}

	// 用户注册页面
	@RequestMapping(value="/register", method = RequestMethod.GET)
	@IgnorePrivilegeCheck
	public String getRegister(HttpServletRequest request,
			HttpServletResponse response, ModelMap map) throws Exception {
		final String view = "common/user/register";

		writeRsa(request,map);

		long ownerId = NumericUtils.parseLong(map.get("ownerId"));

		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return CommonStandard.partnerMessageView;
		}

		if(securityLevelId >= SecurityLevelCriteria.SECURITY_LEVEL_DB3){
			logger.warn("系统当前级别是["+ securityLevelId + "]，不允许自行注册");
			return CommonStandard.partnerMessageView;
		}
		if(securityLevelId >= SecurityLevelCriteria.SECURITY_LEVEL_DEMO){
			map.put("needCaptcha", true);
		}
		//检查是否有invite编码
		String inviteCode = cookieService.getCookie(request, "inviteCode");
		if(StringUtils.isBlank(inviteCode)){
			inviteCode =ServletRequestUtils.getStringParameter(request, "i",null);
		}
		if(StringUtils.isNotBlank(inviteCode)){
			logger.debug("注册用户拥有邀请码:" + inviteCode);
			map.put("i", inviteCode);
		}
		return view;
	}


	public static String decodeStr(String encodeStr) {
		byte[] b = encodeStr.getBytes();
		Base64 base64 = new Base64();
		b = base64.decode(b);
		String s = new String(b);
		return s;
	}

	//用户新增注册
	@RequestMapping(method=RequestMethod.POST)
	@IgnoreLoginCheck
	public String create(HttpServletRequest request,HttpServletResponse response, ModelMap map,
			User partner			) throws Exception {
		map.put("operate", OperateCode.USER_REGISTER.toString());

		if(StringUtils.isBlank(partner.getUsername())){
			logger.warn("尝试注册用户但未提交用户名");
			map.put("message", new EisMessage(EisError.dataIllegal.id, "请提交用户名"));
			return partnerMessageView;

		}
		if(dirtyDictService.isDirty(partner.getUsername())){
			logger.warn("尝试注册用户[" + partner.getUsername() + "]名称不能通过敏感词检查");
			map.put("message", new EisMessage(EisError.dataIllegal.id, "您的用户名不合法，请换一个注册"));
			return partnerMessageView;
		}
		if(dirtyDictService.isDirty(partner.getNickName())){
			logger.warn("尝试注册用户[" + partner.getNickName() + "]的昵称不能通过敏感词检查");
			map.put("message", new EisMessage(EisError.dataIllegal.id, "您的昵称不合法，请换一个注册"));
			return partnerMessageView;

		}
		long ownerId = (long)map.get("ownerId");
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return partnerMessageView;		
		}
		partner.setOwnerId(ownerId);

		//检查是否有invite编码
		String inviteCode = ServletRequestUtils.getStringParameter(request, "i",null);
		
		if(StringUtils.isNotBlank(inviteCode)){
			logger.debug("注册商户拥有邀请码:" + inviteCode);
			partner.setExtraValue(DataName.userInviteByCode.toString(), inviteCode);
		}


		String smsRegisterSign = null;
		smsRegisterSign = ServletRequestUtils.getStringParameter(request, "smsRegisterSign", null);
		logger.info("系统配置为用户注册必须手机验证，用户提交的手机验证码是"+smsRegisterSign);

		String key = KeyConstants.REGISTER_SMS_INSTANCE_PREFIX + "#" + ownerId + "#" + partner.getUsername();

		String sign = centerDataService.get(key);
		if(sign == null || !sign.equalsIgnoreCase(smsRegisterSign)){
			logger.warn("用户提交的验证码是:" + smsRegisterSign + ",REDIS中的验证码是:" + sign + ",不一致");
			map.put("message", new EisMessage(EisError.VERIFY_ERROR.id, "对不起，您输入的验证码错误"));
			return partnerMessageView;	
		}
		//手机号就是用户名，自动绑定
		partner.setExtraValue(DataName.userBindPhoneNumber.toString(), partner.getUsername());


		partner.setUserTypeId(UserTypes.partner.id);
		partner.setUserExtraTypeId(REGISTER_PARTNER_EXTRA_TYPE_ID);
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
				map.put("message", new EisMessage(EisError.VERIFY_ERROR.id, "对不起，请输入正确的验证码。"));
				return partnerMessageView;
			}
		}
		logger.debug(">>>PERFORMANCE>>>开始检查唯一性globalUnique");
		if(globalUniqueService.exist(new GlobalUnique(partner.getUsername(), ownerId))){
			map.put("message", new EisMessage(EisError.OBJECT_ALREADY_EXIST.id, "您的用户名已经被占用，请换一个注册"));
			return partnerMessageView;
		}
		logger.debug(">>>PERFORMANCE>>>完成检查唯一性globalUnique");

		logger.debug("尝试注册新用户:" + partner.getUsername() + "/" + partner.getUserPassword() + ".");

		if(StringUtils.isBlank(partner.getUserPassword())){

			map.put("message", new EisMessage(EisError.OBJECT_IS_NULL.id, "请输入正确的密码"));
			return partnerMessageView;

		}
		String decryptedPassword = decryptPassword(partner.getUserPassword());
		writeRsa(request,map);
		if(decryptedPassword == null){
			logger.error("RSA解密密码失败");
			return partnerMessageView;
		}
		partner.setUserPassword(decryptedPassword);

		String ip = IpUtils.getClientIp(request);
		partner.setLastLoginIp(ip);
		partner.setCurrentStatus(UserStatus.normal.id);
		partner.setExtraValue(DataName.userRegisterIp.toString(), ip);
		if(GUIDE_ENABLED){
			partner.setExtraValue(DataName.GUIDE_STEP.toString(), "1");
		}
		
		if(partner.getUsername().matches("^\\d{11}$")){
			String phoneBindSign = "" + (new java.util.Random().nextInt(888888) + 100000);
			partner.setExtraValue(DataName.userPhoneBindSign.toString(), phoneBindSign);
			partner.setExtraValue(DataName.userBindPhoneNumber.toString(), partner.getUsername());
		} 



		logger.debug(">>>PERFORMANCE>>>开始插入用户数据");

		int createRs = partnerService.insert(partner);
		if(createRs != 1){
			map.put("message", new EisMessage(OperateResult.failed.id,"无法注册:" + createRs));
			return partnerMessageView;
		}
		logger.debug(">>>PERFORMANCE>>>完成插入用户数据，结束流程");
		if(AUTO_RELATION_ROLE_ID > 0){
			UserRoleRelation partnerRoleRelation = new UserRoleRelation(ownerId);
			partnerRoleRelation.setUuid(partner.getUuid());
			partnerRoleRelation.setRoleId(AUTO_RELATION_ROLE_ID);
			partnerRoleRelation.setCurrentStatus(BasicStatus.normal.id);
			partnerRoleRelationService.insert(partnerRoleRelation);
		}
		logger.debug("新增用户密码是:" +  partner.getUserPassword());
		logger.debug("尝试在注册后自动登录:" + login(partner, request, response, map));
		map.put("partner", partner);

		String redirectUrl = null;
		long currentGuideStep = partner.getLongExtraValue(DataName.GUIDE_STEP.toString());

		if(currentGuideStep > 0){
			redirectUrl = "/guide.shtml";
		} else {
			redirectUrl = cookieService.getCookie(request, CommonStandard.COOKIE_REDIRECT_COOKIE_NAME + "_f");
			if(redirectUrl == null){
				redirectUrl = request.getHeader("referer") == null ? "/" : request.getHeader("referer");
			}
		}
		redirectUrl = URLDecoder.decode(redirectUrl,"UTF-8");
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
		return partnerMessageView;

	}

	/*// 用户注册提交
	@RequestMapping(value="/register", method = RequestMethod.POST)
	@IgnorePrivilegeCheck
	public String register(HttpServletRequest request,
			HttpServletResponse response, ModelMap map) throws Exception {
		// 复印件上传
		String flag=request.getParameter("flag");
		//判断是企业还是个人  1企业 0个人
		String formFlag="";
		if(flag.equals("1")){
			formFlag="_c";
		}


		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return 	 CommonStandard.partnerMessageView;	
		}

		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		CommonsMultipartFile orginalFile = (CommonsMultipartFile) multipartRequest
				.getFile("userRealNameIdCardFile"+formFlag);// 表单中对应的文件名；

		String fileName = UUIDFilenameGenerator.generateWithDatePath(orginalFile.getOriginalFilename());
		String fileDest = "";
		String userUploadDir = configService.getValue(DataName.userUploadDir.toString(), ownerId);
		fileDest = userUploadDir + File.separator + fileName;

		File dest = new File(fileDest);
		orginalFile.transferTo(dest);

		User partner = new User();

		UserData uc1 = new UserData();
		uc1.setDataCode(DataName.userRealNameIdCardNumber
				.toString());
		uc1.setDataValue(request.getParameter("userRealNameIdCardNumber"+formFlag));
		uc1.setDataDefineId(dataDefineService.select(
				DataName.userRealNameIdCardNumber.toString())
				.getDataDefineId());
		partner.setUserConfigMap(new HashMap<String, UserData>());
		partner.getUserConfigMap().put(
				DataName.userRealNameIdCardNumber.toString(),
				uc1);

		UserData uc2 = new UserData();
		uc2.setDataCode(DataName.userRealNameIdCardFile
				.toString());
		uc2.setDataValue(fileDest);
		uc2.setDataDefineId(dataDefineService.select(
				DataName.userRealNameIdCardFile.toString())
				.getDataDefineId());
		partner.getUserConfigMap().put(
				DataName.userRealNameIdCardFile.toString(), uc2);

		UserData uc3 = new UserData();
		uc3.setDataCode(DataName.userBindPhoneNumber.toString());
		uc3.setDataValue(request.getParameter("userBindPhoneNumber"+formFlag));
		uc3.setDataDefineId(dataDefineService.select(
				DataName.userBindPhoneNumber.toString())
				.getDataDefineId());
		partner.getUserConfigMap().put(
				DataName.userBindPhoneNumber.toString(), uc3);

		UserData uc4 = new UserData();
		uc4.setDataCode(DataName.bankAccount.toString());
		uc4.setDataValue(request.getParameter("bankAccount"+formFlag));
		uc4.setDataDefineId(dataDefineService.select(
				DataName.bankAccount.toString())
				.getDataDefineId());
		partner.getUserConfigMap().put(
				DataName.bankAccount.toString(), uc4);

		partner.setUsername(request.getParameter("username"+formFlag));
		partner.setUserPassword(request.getParameter("userPassword"+formFlag));
		partner.setNickName(request.getParameter("nickName"+formFlag));
		partner.setUserExtraTypeId(Integer.parseInt(request.getParameter("userExtraTypeId"+formFlag)));
		partner.setCurrentStatus(SecurityStandard.UserStatus.unactive.id);
		if (partnerService.insert(partner) > 0) {
			// 注册成功

			map.put("resultInfo", 1);

			return CommonStandard.partnerMessageView;
		} else {
			map.put("resultInfo", 2);
			return CommonStandard.partnerMessageView;
		}

	}*/

	// 检查用户是否已存在可注册
	@RequestMapping(value = "/exist")
	@ResponseBody
	@IgnoreLoginCheck
	public String getExist(HttpServletRequest request,
			HttpServletResponse response, ModelMap map,
			@RequestParam("username") String username) throws Exception {
		logger.debug("检查数据[" + username + "]的全局唯一性");
		if(username == null || username.equals("")){
			return "false";
		}
		long ownerId = NumericUtils.parseLong(map.get("ownerId"));
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return frontMessageView;		
		}
		if(globalUniqueService.exist(new GlobalUnique(username,ownerId))){		
			logger.debug("数据[" + username + "]的全局唯一性检查结果是true，即已存在，返回false，不可注册");
			return "false";		
		}
		UserCriteria frontUserCriteria = new UserCriteria();
		frontUserCriteria.setUsername(username);
		int count = partnerService.count(frontUserCriteria);
		if(count > 0){
			logger.debug("数据[" + username + "]在用户名中已存在，返回false，不可注册");
			return "false";		
		}
		/*UserDataCriteria userDataCriteria = new UserDataCriteria();
		userDataCriteria.setUserTypeId(UserTypes.frontUser.id);
		userDataCriteria.setDataValue(username);
		if(userDataService.valueExist(userDataCriteria)){					
			map.put("message", new EisMessage(EisError.objectAlreadyExist.id, "数据已存在"));
			return frontMessageView;
		}*/
		logger.debug("数据[" + username + "]的在中央缓存和用户名中都没有找到，返回true，可以注册");

		return "true";		

	}


	//修改用户资料
	@RequestMapping(value="/update/{uuid}", method=RequestMethod.POST)
	public String update(HttpServletRequest request, HttpServletResponse response, ModelMap map, @ModelAttribute("user") User user) throws Exception{
		logger.debug("收到资料修改请求");


		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return CommonStandard.partnerMessageView;
		}
		if(user == null || user.getUuid() < 1){
			map.put("message", new EisMessage(EisError.dataError.id, "请提交正确的数据"));
			return CommonStandard.partnerMessageView;
		}

		User oldUser = certifyService.getLoginedUser(request, response, UserTypes.partner.id);
		if(oldUser == null){
			logger.debug("未找到登录用户");
			map.put("message", new EisMessage(EisError.userNotFoundInSession.id, "请先登录"));
			return CommonStandard.partnerMessageView;
		}
		long oldUuid = oldUser.getUuid();
		if(oldUuid != user.getUuid()){
			logger.error("提交修改的UUID[" + user.getUuid() + "]与Session中的UUID[" + oldUuid + "]不一致");
			map.put("message", new EisMessage(EisError.dataError.id, "请提交正确的数据"));
			return CommonStandard.partnerMessageView;
		}

		//获取用户自定义配置数据
		HashMap<String, UserData> userConfigMap = new HashMap<String, UserData>();
		Enumeration<String> parameters = request.getParameterNames();
		while(parameters.hasMoreElements()){
			String configName = parameters.nextElement();
			if(configName.startsWith(CommonStandard.requestUserConfigPrefix)){//是用户配置数据
				String configValue = request.getParameter(configName);
				configName = configName.replaceFirst(CommonStandard.requestUserConfigPrefix, "");
				UserData uc = new UserData();
				uc.setUuid(user.getUuid());
				uc.setCurrentStatus(BasicStatus.normal.id);
				uc.setDataCode(configName);
				uc.setDataValue(configValue);
				logger.info("得到了请求中的用户配置数据:" + configName + ":" + configValue + "/" + uc.getCurrentStatus());
				userConfigMap.put(configName, uc);
			}
		}
		if(userConfigMap != null && userConfigMap.size() > 0){
			user.setUserConfigMap(userConfigMap);
		}


		/* 修改密码是否需要提供旧密码
		 * 如果需要，则检查是否更改了密码
		 * 如果更改，则检查提供了旧密码
		 * 并进一步检查就密码是否与之前的一致
		 */
		boolean needOldPassword = false;
		if(securityLevel.getData() != null 
				&& securityLevel.getData().get(DataName.needOldPasswordWhenChangePassword.toString()) != null
				&& securityLevel.getData().get(DataName.needOldPasswordWhenChangePassword.toString()).equalsIgnoreCase("true")){
			needOldPassword = true;

		}

		if(needOldPassword){
			if( ServletRequestUtils.getStringParameter(request, "oldPassword") == null ||  ServletRequestUtils.getStringParameter(request, "newPassword") == null ||  ServletRequestUtils.getStringParameter(request, "newPassword2") == null){
				//如果没有提供新密码，说明没修改密码
				logger.info("用户未修改密码");

			} else {
				//需要修改密码
				String oldPassword = null;
				try{
					oldPassword  = ServletRequestUtils.getStringParameter(request, "oldPassword");
				}catch(Exception e){
					map.put("message", new EisMessage(OperateResult.failed.id, "请提供旧的密码"));
					return CommonStandard.partnerMessageView;
				}

				if(oldPassword == null || oldPassword.equals("")){
					map.put("message", new EisMessage(OperateResult.failed.id, "请提供旧的密码"));
					return CommonStandard.partnerMessageView;

				}
				//检查旧密码是否正确
				oldPassword = Crypt.passwordEncode(oldPassword);
				User _oldUser = partnerService.select(user.getUuid());
				if(_oldUser == null){
					map.put("message", new EisMessage(OperateResult.failed.id, "系统异常"));
					return CommonStandard.partnerMessageView;
				}
				if(!_oldUser.getUserPassword().equals(oldPassword)){
					map.put("message", new EisMessage(OperateResult.failed.id, "旧密码输入错误"));
					return CommonStandard.partnerMessageView;
				}
				String newPassword = ServletRequestUtils.getStringParameter(request, "newPassword");
				//只修改密码
				user = _oldUser;
				user.setUserPassword(newPassword);


			}
		} else {
			if( ServletRequestUtils.getStringParameter(request, "newPassword") != null &&  ServletRequestUtils.getStringParameter(request, "newPassword2") != null){
				String newPassword = ServletRequestUtils.getStringParameter(request, "newPassword");
				User _oldUser = partnerService.select(user.getUuid());
				if(_oldUser == null){
					map.put("message", new EisMessage(OperateResult.failed.id, "系统异常"));
					return CommonStandard.partnerMessageView;
				}
				//只修改密码
				user = _oldUser;
				user.setUserPassword(newPassword);
			} else {
				logger.info("用户本次提交未修改密码");
			}
		}

		logger.info("异步修改已提交。");
		EisMessage sm = new EisMessage(OperateResult.accept.id, "修改已提交，请查询");
		map.put("message", sm);
		return CommonStandard.partnerMessageView;
	}






	//用户退出登录
	@IgnorePrivilegeCheck
	@RequestMapping(value="/logout", method=RequestMethod.GET)	
	public String logout(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws IOException {
		final String logoutRedirectTo = "/";
		//SiteDomainRelation siteDomainRelation = siteDomainRelationService.getByHostName(request.getServerName());
		logger.debug("处理合作伙伴退出");
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.id);
		if(partner == null){
			logger.debug("请求中未找到合作伙伴，无需退出.");
			response.sendRedirect(logoutRedirectTo);
		}
		//cookieService.removeCookie(request, response, CommonStandard.COOKIE_REDIRECT_COOKIE_NAME + "_p", siteDomainRelation.getCookieDomain());
		certifyService.logout(request, response, partner);
		request.getSession().invalidate();
		map.clear();
		response.sendRedirect(logoutRedirectTo);
		return null;
	}

	@RequestMapping(value="/login", method=RequestMethod.GET)
	@IgnorePrivilegeCheck
	public String getLogin(HttpServletRequest request,HttpServletResponse response, ModelMap map) throws Exception {

		certifyService.getRemeberMeStatus(request, response, map);

		boolean needCaptcha = needCaptchaForLogin(request);
		long ownerId = 0;
		String x509Name = null;

		writeRsa(request,map);

		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return CommonStandard.partnerMessageView;
		}

		if(securityLevelId >= SecurityLevelCriteria.SECURITY_LEVEL_DB3){
			try{
				X509Certificate[]   certs   =   (X509Certificate[])   request.getAttribute( "javax.servlet.request.X509Certificate");
				//logger.info("得到" + certs.length + "个证书");
				if(certs.length > 0){
					String[] dn = certs[0].getSubjectDN().getName().split(",");
					for(int i = 0; i < dn.length; i++){
						String[] tempPair = dn[i].split("=");
						if(tempPair[0].trim().equals("CN")){
							x509Name = tempPair[1].trim();
						}
					}
				}
			}catch(NullPointerException e){
				logger.warn("未能得到用户浏览器证书");
			}
			logger.debug("系统强制要求X509证书登录，得到的证书CN=" + x509Name);
			if(x509Name != null){
				map.put("x509Name", x509Name);
			} 
		}

		map.put("needCaptcha", needCaptcha);
		return loginView;
	}



	//用户登录
	@RequestMapping(value="/login", method=RequestMethod.POST)
	@IgnorePrivilegeCheck
	public String login(HttpServletRequest request, HttpServletResponse response, ModelMap map, 
			final User user) throws Exception {

		long ownerId = 0;
		String x509Name = null;
		int captchaInputErrorCount = 0;
		boolean needCaptcha = false;

		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return loginView;
		}
		String decryptedPassword = decryptPassword(user.getUserPassword());
		writeRsa(request,map);
		if(decryptedPassword == null){
			logger.error("RSA解密密码失败");
			return secAuthloginView;
		}


		if(securityLevelId >= SecurityLevelCriteria.SECURITY_LEVEL_DB3){
			try{
				X509Certificate[]   certs   =   (X509Certificate[])   request.getAttribute( "javax.servlet.request.X509Certificate");
				//logger.info("得到" + certs.length + "个证书");
				if(certs.length > 0){
					String[] dn = certs[0].getSubjectDN().getName().split(",");
					for(int i = 0; i < dn.length; i++){
						String[] tempPair = dn[i].split("=");
						if(tempPair[0].trim().equals("CN")){
							x509Name = tempPair[1].trim();
						}
					}


				}

			}catch(NullPointerException e){}
			if(x509Name != null){
				user.setUsername(x509Name);
				user.setUuid(0);
			} else {
				map.put("message", new EisMessage(EisError.certifyNotFoundInReuest.id, "对不起，找不到您的证书。"));
				saveLoginLog(user.getUsername(), EisError.certifyNotFoundInReuest.id, request, ownerId);
				return loginView;
			}
		}
		needCaptcha = needCaptchaForLogin(request);


		//检查用户登录次数
		int userMayLogin = checkUserMayLogin(user.getUsername(), request);
		if(userMayLogin != 0){
			StringBuffer sb = new StringBuffer();
			sb.append("对不起，暂不允许登录");
			if(userMayLogin > 0){
				sb.append(",请等待" + userMayLogin + "秒后重试");
			}
			logger.debug("由于当前系统限制，不允许用户[" + user.getUsername() + "]登录");
			map.put("message", new EisMessage(EisError.ACCOUNT_LOCKED.id, sb.toString()));
			//FIXME 不允许登录的情况，是否也记录？
			saveLoginLog(user.getUsername(), EisError.ACCOUNT_LOCKED.id, request,ownerId);
			return loginView;
		}
		//OperateLogCriteria operateLogCriteria = new OperateLogCriteria();

		/*int showCaptchaWhenLoginFailCount = configService.getIntValue(DataName.showCaptchaWhenLoginFailCount.toString(), ownerId);

		try{
			captchaInputErrorCount = (Integer)request.getSession().getAttribute("captchaInputErrorCount");

		}catch(Exception e){}
		logger.debug("验证码输入错误次数:" + captchaInputErrorCount + ", 系统允许的不输入验证码登录次数:" + showCaptchaWhenLoginFailCount);
		if(captchaInputErrorCount >= showCaptchaWhenLoginFailCount){
			needCaptcha = true;
		}*/
		/*if(showCaptchaWhenLoginFailCount == -1){
			needCaptcha = false;			
		}*/
		boolean patchcaIsOk = false;
		if(needCaptcha){
			String userInputCaptcha = request.getParameter(DataName.captchaWord.toString());
			if(userInputCaptcha != null){
				patchcaIsOk = captchaService.verify(request, response, null, userInputCaptcha);
			}
			logger.debug("验证码校验结果:" + patchcaIsOk);	

			if(!patchcaIsOk){
				map.put("message", new EisMessage(EisError.AUTH_FAIL.id, "对不起，请输入正确的验证码。"));
				captchaInputErrorCount++;
				logger.info("验证码输入错误，增加验证码输入错误次数到Session:" + captchaInputErrorCount);
				request.getSession().setAttribute("captchaInputErrorCount", captchaInputErrorCount);
				saveLoginLog(user.getUsername(), EisError.captchaError.id, request, ownerId);
				return loginView;
			} else {

			}
		} 
		if(!needCaptcha || patchcaIsOk){
			captchaInputErrorCount = 0;
			logger.info("验证码输入正确，或不需要输入验证码，将验证码输入错误次数清零:" + captchaInputErrorCount);
			request.getSession().setAttribute("captchaInputErrorCount", captchaInputErrorCount);
		}
		user.setUserPassword(decryptedPassword);
		int result = login(user, request, response, map);
		if(result != OperateResult.success.id){
			captchaInputErrorCount++;
			request.getSession().setAttribute("loginErrorCount", captchaInputErrorCount);
			map.put("message", new EisMessage(EisError.AUTH_FAIL.id, "用户名或密码错误"));
			return loginView;
		}
		certifyService.setRememberMe(request, response, map);
		EisMessage loginResult =  new EisMessage(OperateResult.success.id, "登录成功");

		map.put("message", loginResult);
		String returnUrl = cookieService.getCookie(request, CommonStandard.COOKIE_REDIRECT_COOKIE_NAME + "_p");
		if(returnUrl == null || returnUrl.endsWith(".json")){
			returnUrl = "/";
		}
		returnUrl = java.net.URLDecoder.decode(returnUrl,"UTF-8");

		logger.debug("登录成功，将重定向到:" + returnUrl);
		response.sendRedirect(returnUrl);
		return null;
	}


	/*
	 * 已经登录的用户，在请求敏感资源的时候需要再次输入密码，进行二次验证
	 */
	@RequestMapping(value="/secAuth/login", method=RequestMethod.GET)
	@IgnorePrivilegeCheck
	public String getSecAuthLogin(HttpServletRequest request,HttpServletResponse response, ModelMap map) throws Exception {
		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return CommonStandard.partnerMessageView;
		}

		/*User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.id);
		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}*/
		writeRsa(request,map);
		map.put("strictAuthType", ServletRequestUtils.getStringParameter(request, "strictAuthType"));
		map.put("data", ServletRequestUtils.getStringParameter(request, "data"));

		String returnUrl = ServletRequestUtils.getStringParameter(request, "returnUrl");
		map.put("returnUrl", returnUrl);
		
		return secAuthloginView;
	}

	//用户提交二次验证密码进行验证
	@RequestMapping(value="/secAuth/login", method=RequestMethod.POST)
	@IgnorePrivilegeCheck
	public String secAuthlogin(HttpServletRequest request, HttpServletResponse response, ModelMap map, 
			String userPassword) throws Exception {

		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			return secAuthloginView;
		}
		

		map.put("strictAuthType", ServletRequestUtils.getStringParameter(request, "strictAuthType"));
		map.put("data", ServletRequestUtils.getStringParameter(request, "data"));
		
		String returnUrl = ServletRequestUtils.getStringParameter(request, "returnUrl");
		map.put("returnUrl", returnUrl);

		
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.id);
		if(partner == null){
			logger.error("在会话中找不到对应的用户");
			map.put("message", new EisMessage(EisError.userNotFoundInSession.id, "请先登录"));
			return secAuthloginView;
		}
		String decryptedPassword = decryptPassword(userPassword);
		writeRsa(request,map);
		if(decryptedPassword == null){
			logger.error("RSA解密密码失败");
			return secAuthloginView;
		}

		//int rs = certifyService.checkSecAuth(request, response, partner, decryptedPassword);
		int rs = certifyService.strictAuthorize(request, response, partner, decryptedPassword);

		if(rs != OperateResult.success.id){
			map.put("message", new EisMessage(rs, "验证失败"));
			return secAuthloginView;
		}
		map.put("message", new EisMessage(OperateResult.success.id, "验证成功"));
		if(returnUrl == null || returnUrl.endsWith(".json")){
			returnUrl = "/";
		}
		returnUrl = java.net.URLDecoder.decode(returnUrl,"UTF-8");

		logger.debug("二次验证成功，将重定向到:" + returnUrl);
		response.sendRedirect(returnUrl);
		return null;
	}

	private String decryptPassword(String userPassword) {
		KeyPair kp = null; 

		CacheValue co = cacheService.get(CommonStandard.cacheNameSupport, rsaCacheName);
		//如果缓存对象不为空并且没有超时，就获取缓存中的数据
		if(co != null){
			kp = (KeyPair)co.value;
		} 
		if(kp == null){
			logger.error("缓存中的数据不是RSA密钥对数据");
			return null;
		}
		RSAPrivateKey privateKey = (RSAPrivateKey)kp.getPrivate();
		String decryptedPassword = RSAUtils.decrypt(userPassword, privateKey);
		logger.debug("对加密密码[" + userPassword + "]解密后:" + decryptedPassword);		
		if(decryptedPassword == null){
			return null;
		}
		//前端security.js会把明文反向，所以这里也要对应的反向一下
		return new StringBuffer().append(decryptedPassword).reverse().toString();
	}



	/*
	 * 检查用户是否能登录
	 * 如果不能登录将返回一个秒数，告诉用户需要等待多久
	 * 
	 */
	private int checkUserMayLogin(String username, HttpServletRequest request) {
		if(StringUtils.isBlank(username)){
			logger.warn("用户名为空，不允许提交登陆");
			return -1;
		}
		UserCriteria partnerCriteria = new UserCriteria();
		partnerCriteria.setUserTypeId(UserTypes.partner.id);
		partnerCriteria.setUsername(username);
		partnerCriteria.setCurrentStatus(UserStatus.normal.id);

		List<User> partnerList = partnerService.list(partnerCriteria);
		if(partnerList == null || partnerList.size() < 1){
			logger.warn("找不到用户名为[" + username + "]的正常状态partner");
			return -1;
		}
		User partner = partnerList.get(0);
		OperateLogCriteria operateLogCriteria = new OperateLogCriteria();
		operateLogCriteria.setUuid(partner.getUuid());
		operateLogCriteria.setOperateCode(Operate.login.name());
		operateLogCriteria.setObjectType(ObjectType.partner.name());
		if(securityLevel == null){
			logger.warn("系统没有指定级别[" + securityLevelId + "]的安全级别，允许登录");
			return 0;
		}
		//查询指定时间内的用户登陆记录
		int queryDuration = NumericUtils.getNumeric(securityLevel.getData().get(DataName.loginRetryDurationSec.toString()));
		if(queryDuration < 1){
			logger.debug("当前安全级别[" + securityLevel + "]未定义检查多久以内的登录次数");
		} else {
			int maxCount = NumericUtils.getNumeric(securityLevel.getData().get(DataName.loginRetryCountInDuration.toString()));
			if(maxCount < 1){
				logger.debug("当前安全级别[" + securityLevel + "]未定义在" + queryDuration + "秒内登录失败次数的上限");

			} else {
				operateLogCriteria.setBeginTime(DateUtils.addSeconds(new Date(), -queryDuration));
				//得到指定时间内该用户的所有登录操作
				List<OperateLog> loginLogList = operateLogService.list(operateLogCriteria);
				logger.debug("自[" + sdf.format(operateLogCriteria.getBeginTime()) + "]开始，用户[" + partner.getUuid() + "]的登录次数是:" + (loginLogList == null ? "空" : loginLogList.size()));
				//筛选出失败的登录
				int failCount = 0;
				for(OperateLog logLog : loginLogList){
					int operateResult = NumericUtils.getNumeric(logLog.getOperateResult());
					if(operateResult != OperateResult.success.id){
						failCount++;
					}
				}
				if(failCount >= maxCount){
					logger.debug("用户[" + partner.getUuid() + "]自[" + sdf.format(operateLogCriteria.getBeginTime()) + "]开始登录失败次数[" + failCount + "]已超过当前安全级别[" + securityLevel + "]的限制:" + maxCount);
					return queryDuration;
				}
				logger.debug("用户[" + partner.getUuid() + "]自[" + sdf.format(operateLogCriteria.getBeginTime()) + "]开始登录失败次数[" + failCount + "]还没超过当前安全级别[" + securityLevel + "]的限制:" + maxCount);

			}
		}
		return 0;
	}

	private int login(User user, HttpServletRequest request, HttpServletResponse response, ModelMap map) {

		String loginUserName = user.getUsername();
		logger.debug("User[" + loginUserName + "/" + user.getUserPassword() + "] try login.");
		if(user.getUsername() == null || user.getUserPassword() == null){
			logger.error("Passport username or password null.");
			map.put("json", new EisMessage(EisError.usernameOrPasswordIsNull.id, "对不起，请检查您的用户名或密码。"));
			saveLoginLog(user.getUsername(), EisError.usernameOrPasswordIsNull.id, request, user.getOwnerId());
			return EisError.usernameOrPasswordIsNull.id;		
		}
		if(user.getUsername().equals("") || user.getUserPassword().equals("")){
			logger.error("Passport username or password empty.");
			map.put("json", new EisMessage(EisError.usernameOrPasswordIsNull.id, "对不起，请检查您的用户名或密码。"));
			saveLoginLog(user.getUsername(), EisError.usernameOrPasswordIsNull.id, request, user.getOwnerId());
			return EisError.usernameOrPasswordIsNull.id;		
		}
		UserCriteria userCriteria = new UserCriteria();
		userCriteria.setUsername(user.getUsername());
		userCriteria.setUserPassword(user.getUserPassword());
		userCriteria.setCurrentStatus(BasicStatus.normal.id);
		userCriteria.setUserTypeId(UserTypes.partner.id);
		user = certifyService.login(request, response, userCriteria);
		try{
			logger.info("登录合作伙伴的配置数据有[" + (user.getUserConfigMap() == null ? 0 : user.getUserConfigMap().size()) + "]条");
			logger.info("用户的级别方案是：" + user.getUserLevelProject().getUserLevelProjectName());
			/*for(String key : user.getUserLevelProject().getUserLevelConditionMap().keySet()){
				logger.info("用户级别方案配置::::" + key + "====" + user.getUserLevelProject().getUserLevelConditionMap().get(key).getUserLevelConditionValue());
			}*/
		}catch(Exception e){
			logger.info("用户没有任何级别方案配置");
		}
		if(user == null){
			logger.info("用户登录失败");
			saveLoginLog(loginUserName, EisError.AUTH_FAIL.id, request, (long)map.get("ownerId"));
			return EisError.AUTH_FAIL.id;		
		}



		//userService.updateLoginSync(user);
		map.put("message", new EisMessage(OperateResult.success.id, "登录成功"));
		saveLoginLog(user.getUsername(), OperateResult.success.id, request, user.getOwnerId());

		map.put("user", user);
		request.getSession().setAttribute(CommonStandard.sessionUsername, user);
		request.getSession().setAttribute(CommonStandard.sessionTokenName, user.getUuid());

		//将用户的密码替换为明文并发送到消息总线
		user.setUserPassword(user.getUserPassword());

		/*String bbsLoginString = loginBbs(user);			
			if(bbsLoginString != null){
				if(user.getUserConfigMap() == null){
					user.setUserConfigMap(new HashMap<String,UserConfig>());
				}
				UserConfig bbs = new UserConfig();
				bbs.setDataCode(Constants.dataNameSiteBbsLoginUrl);
				bbs.setDataValue(bbsLoginString);
				user.getUserConfigMap().put(bbs.getDataCode(), bbs);
			}*/
		return OperateResult.success.id;		

	}

	/*
	 * 用户登录时是否需要验证码
	 */
	private boolean needCaptchaForLogin(HttpServletRequest request){
		boolean needCaptcha = true;
		if(securityLevelId >= SecurityLevelCriteria.SECURITY_LEVEL_STRICT){
			logger.debug("当前系统安全级别是:" + securityLevelId + ",强制实行验证码登录");
			needCaptcha = true;
		} else {

		}
		request.getSession().setAttribute("needCaptcha", needCaptcha);

		return needCaptcha;

	}


	/*
	 * 获取RSA密钥对
	 * 并向Session中写入私钥系数和指数，供提交时解密
	 * 向前端map中写入公钥系数和指数，供前端security.js加密
	 */
	private void writeRsa(HttpServletRequest request, ModelMap map) {
		KeyPair kp = null; 

		CacheValue co = cacheService.get(CommonStandard.cacheNameSupport, rsaCacheName);
		//如果缓存对象不为空并且没有超时，就获取缓存中的数据
		if(co != null && !co.isExpired()){
			kp = (KeyPair)co.value;
		} else {
			kp = generateAndSaveKeyPair();
		}


		if(kp == null){
			logger.error("无法生成RSA密钥对");
		} else {
			RSAPublicKey publicKey = (RSAPublicKey)kp.getPublic();
			map.put(DataName.publicKeyModulus.toString(), publicKey.getModulus().toString(16));
			map.put(DataName.publicKeyExponent.toString(), publicKey.getPublicExponent().toString(16));
			logger.debug("向map中放入公钥系数:" + publicKey.getModulus().toString() + ",公钥指数:" + publicKey.getPublicExponent().toString());

		}		
	}

	private KeyPair generateAndSaveKeyPair(){
		KeyPair kp = RSAUtils.genKeyPair();
		CacheValue co = new CacheValue(null,kp, DateUtils.addSeconds(new Date(), (int)rsaKeyTtl));
		cacheService.put(CommonStandard.cacheNameSupport, rsaCacheName, co);
		logger.debug("生成新的RSA密钥对并放入缓存，有效期:" + sdf.format(co.expireTime));
		return kp;
	}

	/*
	 * 保存登录失败和错误的日志
	 */
	private void saveLoginLog(String username, int statusCode, HttpServletRequest request, long ownerId) {
		long uuid = 0;
		UserCriteria userCriteria = new UserCriteria();
		userCriteria.setUsername(username);
		List<User> partnerList = partnerService.list(userCriteria);
		if(partnerList != null && partnerList.size() > 0){
			uuid = partnerList.get(0).getUuid();
		}
		try {
			operateLogService.insert(new OperateLog(ObjectType.partner.name(),null, uuid, Operate.login.name(), String.valueOf(statusCode), null, IpUtils.getClientIp(request), configService.getServerId(), ownerId));
		} catch (Exception e) {
			e.printStackTrace();
		}			

	}

	@RequestMapping(value="/registerByPhone", method=RequestMethod.POST)
	@IgnoreLoginCheck
	public String submitRegisterByPhone(HttpServletRequest request, HttpServletResponse response, ModelMap map, String phone,
			@RequestParam(value="eis_captcha",required=false)String captcha)  {
		map.put("operate", OperateCode.SEND_REGISTER_SMS.toString());
		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.id,"找不到对应的地址","请尝试访问其他页面或返回首页"));
			return partnerMessageView;		
		}



		if(StringUtils.isBlank(phone) || !phone.matches("^\\d{11}$")){
			logger.warn("错误的手机号:" + phone);
			map.put("message", new EisMessage(EisError.DATE_FORMAT_ERROR.id, "手机号不正确"));
			return partnerMessageView;		
		}


		String smsTemplate = configService.getValue(DataName.registerSmsValidateMessage.toString(), ownerId);
		if(smsTemplate == null){
			smsTemplate = DEFAULT_SMS_VALIDATE_MESSAGE;
		}

		boolean needCaptchaWhenRegisterByPhone = configService.getBooleanValue(DataName.needCaptchaWhenRegisterByPhone.toString(), ownerId);
		if(needCaptchaWhenRegisterByPhone){
			logger.info("收到用户提交的captcha是"+captcha);
			if (!captchaService.verify(request, response, null, captcha)){
				map.put("message", new EisMessage(EisError.DATE_FORMAT_ERROR.id, "验证码错误"));
				return partnerMessageView;	
			}
		}
		int registerSmsIdelInterval = configService.getIntValue(DataName.registerSmsIdelInterval.toString(), ownerId);
		if(registerSmsIdelInterval > 0){
			//放入一个键值到REDIS，以防止用户重复提交
			String key = KeyConstants.REGISTER_SMS_LOCK_PREFIX + "#" + ownerId + "#" + phone;
			boolean setResult = centerDataService.setIfNotExist(key, sdf.format(new Date()), registerSmsIdelInterval);
			if(!setResult){
				String value = centerDataService.get(key);
				logger.debug("用户手机号[" + phone + "]已在[" + value + "]时提交验证码申请，还没到限制时间" + registerSmsIdelInterval + ",不允许发送注册短信");
				map.put("message", new EisMessage(EisError.operateTooSoon.id, "操作过于频繁，请稍候再试"));
				return partnerMessageView;	
			}
		}

		if(globalUniqueService.exist(new GlobalUnique(phone, ownerId))){
			map.put("message", new EisMessage(EisError.OBJECT_ALREADY_EXIST.id, "您的用户名已经被占用，请换一个注册"));
			return partnerMessageView;
		}
		Random random = new Random();
		String randomCode=String.valueOf(random.nextInt(999999)%(999999-100000+1) + 100000);
		logger.info("短信验证码是"+randomCode);

		String shortMsg = smsTemplate.replaceAll("\\$\\{smsCode\\}", randomCode);

		UserMessage sms = new UserMessage(ownerId);
		sms.setPerferMethod(new String[]{UserMessageSendMethod.sms.toString()});
		sms.setContent(shortMsg);
		sms.setReceiverName(phone);
		sms.setCurrentStatus(MessageStatus.queue.id);
		int rs = userMessageService.send(sms);

		logger.debug("短信下发到[手机" + phone + ",内容:" + shortMsg + "]，消息服务返回的是:" + rs);

		if(rs < 1){
			map.put("message", new EisMessage(OperateResult.failed.id, "短信下发失败"));
			return partnerMessageView;		
		}


		map.put("message",new EisMessage(OperateResult.success.id, "短信下发成功"));
		//为兼容客户端这种不保存Cookie的情况，向REDIS中放入对应数据
		String key = KeyConstants.REGISTER_SMS_INSTANCE_PREFIX + "#" + ownerId + "#" + phone;
		centerDataService.setForce(key, randomCode, SMS_VALID_SECOND);


		return partnerMessageView;		
	}



}
