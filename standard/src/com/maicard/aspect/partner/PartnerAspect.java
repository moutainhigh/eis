package com.maicard.aspect.partner;


import static com.maicard.standard.CommonStandard.DEFAULT_PAGE_SUFFIX;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.maicard.annotation.StrictAuthorize;
import com.maicard.annotation.AllowJsonOutput;
import com.maicard.annotation.IgnoreLoginCheck;
import com.maicard.annotation.IgnorePrivilegeCheck;
import com.maicard.annotation.RequestPrivilege;
import com.maicard.common.base.BaseService;
import com.maicard.common.criteria.IpPolicyCriteria;
import com.maicard.common.criteria.SecurityLevelCriteria;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.domain.SecurityLevel;
import com.maicard.common.domain.SiteDomainRelation;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.CookieService;
import com.maicard.common.service.IpPolicyService;
import com.maicard.common.service.SecurityLevelService;
import com.maicard.common.service.SiteDomainRelationService;
import com.maicard.common.util.IpUtils;
import com.maicard.common.util.NumericUtils;
import com.maicard.common.util.Crypt;
import com.maicard.common.util.CryptKeyUtils;
import com.maicard.common.util.HtmlTagTrim;
import com.maicard.common.util.HttpUtils;
import com.maicard.common.util.SecurityLevelUtils;
import com.maicard.exception.*;
import com.maicard.security.criteria.PrivilegeCriteria;
import com.maicard.security.domain.OperateLog;
import com.maicard.security.domain.User;
import com.maicard.security.service.AuthorizeService;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.OperateLogService;
import com.maicard.security.util.Uri2PrivilegeCriteria;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.MoneyType;
import com.maicard.standard.ObjectType;
import com.maicard.standard.Operate;
import com.maicard.standard.OperateResult;
import com.maicard.standard.SecurityStandard.UserTypes;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.ui.ModelMap;

/**
 * Partner系统切面
 * 
 * @author NetSnake
 * @date 2014-02-04
 */
@SuppressWarnings("unused")
@Aspect
public class PartnerAspect extends BaseService{

	@Resource
	private AuthorizeService authorizeService;
	@Resource
	private CertifyService certifyService;
	@Resource
	private CookieService cookieService;

	@Resource
	private ConfigService configService;

	@Resource
	private ApplicationContextService applicationContextService;	

	@Resource
	private AuthorizeService partnerAuthorizeService;
	@Resource
	private SecurityLevelService securityLevelService;
	@Resource
	private SiteDomainRelationService siteDomainRelationService;
	@Resource
	private IpPolicyService ipPolicyService;

	@Resource
	private OperateLogService operateLogService;

	private static Map<String, PartnerSetting> partnerSettingCache = new ConcurrentHashMap<String, PartnerSetting>();


	private final int securityLevelId =  SecurityLevelUtils.getSecurityLevel();

	boolean bossUsePageShowException = false;
	private final String loginUrl = "/user/login.shtml";

	private String aesKey = null;



	private String systemCode;

	//二次验证登录
	private final String secAuthloginUrl = "/user/secAuth/login.shtml";


	private final String year = new SimpleDateFormat("yyyy").format(new Date());

	private final String partnerAspectExpress = "(execution(* com.maicard..*.common.controller..*.*(..)) || execution(* com.maicard.partner.controller..*.*(..)) || execution(* com.maicard..*.partner.controller..*.*(..)) || execution(* com.maicard..*.partner.basic..*.*(..))) && @annotation(org.springframework.web.bind.annotation.RequestMapping)";

	private final String[] ignoreRedirectUri = new String[]{"/weixinUser/",  "/content/user/login" + DEFAULT_PAGE_SUFFIX,"/content/user/register" + DEFAULT_PAGE_SUFFIX,  "/captcha" + DEFAULT_PAGE_SUFFIX};

	private SecurityLevel securityLevel;

	/**
	 * 记录用户日志，最多存放多少请求数据
	 */
	private final int MAX_RECORD_LENGTH = 1024;

	@PostConstruct
	public void init(){
		securityLevel = securityLevelService.select(securityLevelId);
		systemCode = configService.getSystemCode();

		try {
			aesKey = CryptKeyUtils.readAesKey();
		} catch (Exception e) {
			logger.error("无法读取系统AES密钥");
		}
	}

	@Around(partnerAspectExpress)
	public Object doAroundForPartnerSecurityCheck(ProceedingJoinPoint joinPoint) throws Throwable{

		HttpServletRequest request = null;
		HttpServletResponse response = null;
		ModelMap map = null;


		try{
			request = (HttpServletRequest)joinPoint.getArgs()[0];
			response = (HttpServletResponse)joinPoint.getArgs()[1];
			map = (ModelMap)joinPoint.getArgs()[2];
		}catch(Exception e){}
		if(request == null || response == null){
			logger.debug("方法参数不规范:" + joinPoint.getTarget().getClass().getSimpleName() + ", method:" +joinPoint.getSignature().getName() + ",参数个数:" +  joinPoint.getArgs().length);
			throw new SystemException("系统request或response异常");
		}
		SiteDomainRelation siteDomainRelation = siteDomainRelationService.getByHostName(request.getServerName());
		if(siteDomainRelation == null){
			logger.error("根据主机名[" + request.getServerName() + "]找不到对应的站点对应数据");
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return null;
		}
		long ownerId = siteDomainRelation.getOwnerId();
		logger.debug("当前访问Partner系统的ownerId=" + ownerId);
		if(ownerId < 1){
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return null;
		}

		boolean writeOperateLog = true;
		String operateCode = request.getMethod().toUpperCase();
		if(securityLevelId >= SecurityLevelCriteria.SECURITY_LEVEL_STRICT){
			writeOperateLog = true;
		} else if(operateCode.equals("POST")){
			//对于要求不严格的，至写入POST方法的日志
			writeOperateLog = true;
		}

		
		if(securityLevelId >= SecurityLevelCriteria.SECURITY_LEVEL_STRICT){
			boolean userIpIsForbidden = ipIsForbidden(request, ownerId);
			if(logger.isDebugEnabled()){
				logger.debug("检查用户IP是否合法，检查结果:" + userIpIsForbidden);
			}
			if(userIpIsForbidden){
				if(logger.isDebugEnabled()){
					logger.debug("用户IP不合法，直接返回:" + HttpStatus.NOT_FOUND.value());
				}
				response.setStatus(HttpStatus.NOT_FOUND.value());
				joinPoint = null;
				return null;
			}
		}
		if(map != null){
			PartnerSetting partnerSetting = this.getPartnerSetting(ownerId);
			map.put("ownerId", ownerId);
			map.put("systemCode", systemCode);

			map.put("theme", partnerSetting.theme);
			map.put("commonFooter", partnerSetting.commonFooter);
			map.put("systemName", partnerSetting.systemName);

			map.put("moneyName", partnerSetting.moneyName);
			map.put("coinName", partnerSetting.coinName);
			map.put("pointName", partnerSetting.pointName);
			map.put("scoreName", partnerSetting.scoreName);
			if(securityLevel != null){
				map.put("securityLevelDesc", new StringBuffer().append("当前安全级别:").append(securityLevel.getName()).append(" ").append(securityLevelId).append("级").toString() );
			}

		}
		String executeMethod = joinPoint.getSignature().getName();
		boolean isIgnorePrivilegeCheck = false;

		Signature  signature = joinPoint.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;  
		Method method = methodSignature.getMethod();  

		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
		
		boolean jsonAccess = request.getRequestURI().toLowerCase().endsWith(".json");

		
		//注解方式的权限配置，如果使用了这种模式，则不检查REST模式
		RequestPrivilege requestPrivilege = AnnotationUtils.getAnnotation(method, RequestPrivilege.class);
		
		boolean havePrivilegeFromAnnotation = false;
		if(requestPrivilege != null){
			String objectTypeCode = requestPrivilege.objectTypeCode();
			String operateCodeTemp = requestPrivilege.operateCode();
			logger.info("权限注解请求操作权限{}=>{}",objectTypeCode,operateCodeTemp);
			if(StringUtils.isEmpty(objectTypeCode) || StringUtils.isEmpty(operateCodeTemp)){
				response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
				return null;
			}
			
			if(partner == null) {
				logger.info("当前定义了操作权限:" +objectTypeCode + "=>"+operateCodeTemp + "但没有登录");
				if(jsonAccess) {
					response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
				} else {
					cookieService.addCookie(request, response, CommonStandard.COOKIE_REDIRECT_COOKIE_NAME + "_p", java.net.URLEncoder.encode(request.getRequestURI(),"UTF-8"), CommonStandard.COOKIE_MAX_TTL, siteDomainRelation.getCookieDomain());
					response.sendRedirect(loginUrl);
				}
				return null;
			}
			
			if(!authorizeService.havePrivilege(partner, objectTypeCode, operateCodeTemp)){
				logger.info("用户："+partner.getUuid()+"对操作"+objectTypeCode+":"+operateCodeTemp+"没有权限");
				response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
				return null;
			}
			//当定义了注解权限后就不检查REST权限
			//isIgnorePrivilegeCheck = true;
			havePrivilegeFromAnnotation = true;
		} else {
			//使用REST检查
			if(method.isAnnotationPresent(IgnorePrivilegeCheck.class) || method.isAnnotationPresent(IgnoreLoginCheck.class)){
				isIgnorePrivilegeCheck = true;
			}

		}
		if(map != null && partner != null){
			map.put("welcomeName", partner.getNickName() == null ? partner.getUsername() : partner.getNickName());
		}
		PrivilegeCriteria privilegeCriteria = Uri2PrivilegeCriteria.convert(request);
		if(privilegeCriteria == null){
			privilegeCriteria = new PrivilegeCriteria(ownerId);
		}
		
		if(!isIgnorePrivilegeCheck && partner != null ) {
			boolean userIpIsForbidden = userIpIsForbidden(request, partner);
			if(logger.isDebugEnabled()){
				logger.debug("检查用户通用IP是否合法，检查结果:" + userIpIsForbidden);
			}
			if(userIpIsForbidden){
				if(logger.isDebugEnabled()){
					logger.debug("用户IP不合法，直接返回:" + HttpStatus.NOT_FOUND.value());
				}
				response.setStatus(HttpStatus.NOT_FOUND.value());
				joinPoint = null;
				return null;
			}
		}

		if(isIgnorePrivilegeCheck || havePrivilegeFromAnnotation){
			logger.debug("[" + joinPoint.getTarget().getClass().getName() + "." + executeMethod + "]有IgnorePrivilegeCheck或IgnoreLoginCheck注解，不再进行权限检查");
			//检查提交数据的合法性
			if(!inputIsLegal(request) || havePrivilegeFromAnnotation){
				//对于严格环境，不合法输入写入日志
				if(writeOperateLog){
					//对所有操作都写入日志
					operateLogService.insert(
							new OperateLog(privilegeCriteria.getObjectTypeCode() == null ? ObjectType.unknown.name() : privilegeCriteria.getObjectTypeCode() ,
									privilegeCriteria.getObjectId(), 
									partner == null ? 0 : partner.getUuid(), 
											privilegeCriteria.getOperateCode() == null ? operateCode + " " + Operate.unknown.code :	 operateCode + " " + privilegeCriteria.getOperateCode(), 
													String.valueOf(EisError.dataIllegal.id), 
													null, IpUtils.getClientIp(request), configService.getServerId(), ownerId));

				}
				if(!inputIsLegal(request)) {
					response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
					return null;
				}
			}		
			
			return joinPoint.proceed();
		}

		if(partner == null){
			logger.error("未找到登录用户");
			if(jsonAccess) {
				response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			} else {
				cookieService.addCookie(request, response, CommonStandard.COOKIE_REDIRECT_COOKIE_NAME + "_p", java.net.URLEncoder.encode(request.getRequestURI(),"UTF-8"), CommonStandard.COOKIE_MAX_TTL, siteDomainRelation.getCookieDomain());
				response.sendRedirect(loginUrl);
			}
			return null;
		}
		if(partner.getUserTypeId() != UserTypes.partner.getId()){
			logger.error("尝试登录的用户[" + partner + "]不是partner类型");
			cookieService.addCookie(request, response, CommonStandard.COOKIE_REDIRECT_COOKIE_NAME + "_p", java.net.URLEncoder.encode(request.getRequestURI(),"UTF-8"), CommonStandard.COOKIE_MAX_TTL,siteDomainRelation.getCookieDomain());
			response.sendRedirect(loginUrl);
			return null;		
		}

		if(logger.isDebugEnabled()){
			logger.debug("执行安全检查[" + request.getMethod() + "到URI:" + request.getRequestURI() + ",类:" + joinPoint.getTarget().getClass().toString() + ",方法:" + joinPoint.getSignature().getName() + "]");
		}
		if(!authorizeService.havePrivilege(request, response, UserTypes.partner.getId())){
			if(writeOperateLog){
				//无权访问，写入日志
				operateLogService.insert(
						new OperateLog(privilegeCriteria.getObjectTypeCode() == null ? ObjectType.unknown.name() : privilegeCriteria.getObjectTypeCode() ,
								privilegeCriteria.getObjectId(), 
								partner == null ? 0 : partner.getUuid(), 
										privilegeCriteria.getOperateCode() == null ? operateCode + " " + Operate.unknown.code :	 operateCode + " " + privilegeCriteria.getOperateCode(), 
												String.valueOf(EisError.ACCESS_DENY.id), 
												null, IpUtils.getClientIp(request), configService.getServerId(), ownerId));

			}
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return null;
		}


		//该方法是否指定了严格验证
		StrictAuthorize strictAuthorize = AnnotationUtils.getAnnotation(method, StrictAuthorize.class);
		if(strictAuthorize != null){
			String strictAuthType = strictAuthorize.authType();
			if(strictAuthType == null){
				strictAuthType = CommonStandard.DEFAULT_STRICT_AUTH_MODE;
			}
			String returnUrl = request.getRequestURL().toString();
			if(request.getQueryString() != null){
				returnUrl += "?" + request.getQueryString();
			}
			long strictAuthTtl = strictAuthorize.ttl();
			//根据严格验证确定一个token
			String token = "strict_auth_" + strictAuthorize.token();
			String data = "strictAuthType=" + strictAuthType + "&strictAuthTtl=" + strictAuthTtl + "&strictAuthToken=" + token;
			logger.info("对方法:" + method.getName() + "的严格验证数据是:" + data);
			Crypt crypt = new Crypt();
			crypt.setAesKey(aesKey);
			String cryptData = crypt.aesEncrypt(data);
			Object tokenData = request.getSession().getAttribute(token);

			String url = secAuthloginUrl + "?strictAuthType=" + strictAuthType + "&returnUrl=" + returnUrl + "&data=" + cryptData;
			if(tokenData == null){
				//之前没有做严格验证，需要重定向到验证地址
				logger.info("方法:" + method.getName() + "需要严格验证，但是Session中没有令牌:" + token + ",重定向到验证地址:" + url);
				response.sendRedirect(url);
				return null;
			} else {
				//如果已经做了严格验证，检查ttl，如果ttl已过期或者为0，则删除该token，下次访问需要再次做验证
				long ts = NumericUtils.parseLong(tokenData);
				long currentTs = new Date().getTime() / 1000;

				logger.debug("严格验证的写入ts是:" + ts + ",当前ts=" + currentTs + ",超时是:" + strictAuthTtl);

				if(strictAuthTtl == 0){
					//如果超时是0，则删除验证数据，下次将再次验证
					request.getSession().removeAttribute(token);
				} else {
					if(ts + strictAuthTtl < currentTs){
						logger.info("严格验证的写入时间是:" + ts + ",超时是:" + strictAuthTtl + ",已过期，删除Session中的令牌并重定向到验证地址:" + url);
						request.getSession().removeAttribute(token);
						response.sendRedirect(url);
						return null;
					}
				}
			}
		}
		//检查是否需要二次验证
		boolean needSecAuth = checkNeedSecAuth(request, response, partner);
		if(needSecAuth){
			map.put("message", new EisMessage(EisError.needSecAuth.getId(),"您当前的操作需要进行二次验证"));
			cookieService.addCookie(request, response, CommonStandard.COOKIE_REDIRECT_COOKIE_NAME + "_p", java.net.URLEncoder.encode(request.getRequestURI(),"UTF-8"), CommonStandard.COOKIE_MAX_TTL,siteDomainRelation.getCookieDomain());
			response.sendRedirect(secAuthloginUrl);
			return null;
		}
		if(!inputIsLegal(request)){
			if(writeOperateLog){
				operateLogService.insert(
						new OperateLog(privilegeCriteria.getObjectTypeCode() == null ? ObjectType.unknown.name() : privilegeCriteria.getObjectTypeCode() ,
								privilegeCriteria.getObjectId(), 
								partner == null ? 0 : partner.getUuid(), 
										privilegeCriteria.getOperateCode() == null ? operateCode + " " + Operate.unknown.code :	 operateCode + " " + privilegeCriteria.getOperateCode(), 
												String.valueOf(EisError.dataIllegal.id), 
												null, IpUtils.getClientIp(request), configService.getServerId(), ownerId));

			}
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return null;
		}


		String uri = request.getRequestURI();

		//在安全级别高于最低级的时候，检查一个方法是否允许输出JSON,如果不允许，检查当前访问的后缀是否为json，如果是则返回404
		/*if(securityLevelId > SecurityLevelCriteria.SECURITY_LEVEL_DEMO){
			AllowJsonOutput allowJsonOutput = AnnotationUtils.getAnnotation(method, AllowJsonOutput.class);

			if(allowJsonOutput == null && uri.toLowerCase().endsWith("json")){
				logger.info("当前方法不允许输出JSON，但当前请求为JSON后缀");

				response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
				return null;
			}
		}*/
		if(!uri.endsWith(DEFAULT_PAGE_SUFFIX) && !uri.equals("/") ){
			logger.debug("当前请求[" + uri + "]不是以" + DEFAULT_PAGE_SUFFIX + "结尾，也不是/, 不写入重定向数据");
		} else {
			boolean ignore = false;
			for(String ignoreData : ignoreRedirectUri){
				if(uri.indexOf(ignoreData) > -1){
					logger.debug("当前请求[" + uri + "]与忽略数据[" + ignoreData + "]匹配，不写入重定向数据");
					ignore = true;
					break;
				}
			}
			if(!ignore){
				if (request.getQueryString()!=null){
					uri=uri+"?"+request.getQueryString();
				}
				logger.debug("把当前请求[" + uri + "]写入重定向Cookie");
				cookieService.addCookie(request, response, CommonStandard.COOKIE_REDIRECT_COOKIE_NAME + "_p", java.net.URLEncoder.encode(uri,"UTF-8"), CommonStandard.COOKIE_MAX_TTL, siteDomainRelation.getCookieDomain());
			}
		}
		if(privilegeCriteria.getObjectTypeCode() != null && privilegeCriteria.getObjectTypeCode().equalsIgnoreCase("partnerMenuRoleRelation")){
			//不写入日志，这是请求菜单项
			writeOperateLog = false;
		}


		Object result = joinPoint.proceed();
		String operateResultCode = null;
		if(map.get("message") != null){
			Object message = map.get("message");
			if(message instanceof EisMessage){
				operateResultCode = String.valueOf(((EisMessage)message).getOperateCode());
			}
		}
		if(operateResultCode == null){
			operateResultCode = String.valueOf(OperateResult.accept.id);
		}
		if(writeOperateLog){
			String postData = null;
			if(operateCode.equals("POST")){
				StringBuffer sb = new StringBuffer();
				for(String key : request.getParameterMap().keySet()){
					if(sb.length() > MAX_RECORD_LENGTH){
						break;
					}
					sb.append(key).append(":").append(Arrays.toString(request.getParameterMap().get(key)));
				}
				if(sb.length() > 0){
					postData = sb.toString();
				}
			} else {
				postData = request.getQueryString();
			}

			operateLogService.insert(
					new OperateLog(privilegeCriteria.getObjectTypeCode() == null ? ObjectType.unknown.name() : privilegeCriteria.getObjectTypeCode() ,
							privilegeCriteria.getObjectId(), 
							partner == null ? 0 : partner.getUuid(), 
									privilegeCriteria.getOperateCode() == null ? operateCode + " " + Operate.unknown.code :	 operateCode + " " + privilegeCriteria.getOperateCode(), 
											operateResultCode, 
											postData, IpUtils.getClientIp(request), configService.getServerId(), ownerId));

		}

		return result;



	}


	

	/*
	 * 检查该对象的访问是否需要二次验证
	 */
	private boolean checkNeedSecAuth(HttpServletRequest request, HttpServletResponse response, User partner) {
		PrivilegeCriteria privilegeCriteria = Uri2PrivilegeCriteria.convert(request);
		if(privilegeCriteria == null){
			logger.error("无法确定当前请求URI对应的对象和操作:" + request.getRequestURI());
			throw new SystemException("访问路径异常");
		}
		String objectType = privilegeCriteria.getObjectTypeCode();
		String operate = privilegeCriteria.getOperateCode();
		if(objectType == null){
			logger.debug("当前URI请求的对象是空，不进行二次验证检查");
			return false;
		}
		if(operate == null){
			logger.debug("当前URI请求的操作是空，不进行二次验证检查");
			return false;
		}
		//读取安全级别中的针对二次认证对象及其操作的配置，以URL字符串形式保存
		String secAuthConfig = SecurityLevelUtils.getConfig(securityLevel, DataName.secAuthObjectAndOperate.toString());
		if(StringUtils.isBlank(secAuthConfig)){
			logger.debug("当前安全级别[" + securityLevel + "]未配置二次验证数据，不进行二次验证检查");
			return false;
		}
		Map<String,String> secAuthConfigMap = HttpUtils.getRequestDataMap(secAuthConfig);
		if(secAuthConfigMap == null || secAuthConfigMap.size() < 1){
			logger.debug("当前安全级别[" + securityLevel + "]配置的二次验证数据无法正常解析:" + secAuthConfig + "，不进行二次验证检查");
			return false;
		}
		if(!secAuthConfigMap.containsKey(objectType)){
			logger.debug("当前安全级别[" + securityLevel + "]不需要对[" + objectType + "]进行二次验证");
			return false;
		}
		String needOperate = secAuthConfigMap.get(objectType);
		if(needOperate == null){
			logger.warn("当前安全级别[" + securityLevel + "]要求对[" + objectType + "]进行二次验证，但没有配置哪些操作需要二次验证");
			return false;
		}
		boolean needSecAuth = false;
		if(needOperate.equals('*')){
			logger.debug("当前安全级别[" + securityLevel + "]要求对[" + objectType + "]进行二次验证，并且其操作码是*，即所有操作都要进行二次验证");
			needSecAuth = true;
		} else {

			String[] needOperateList = needOperate.split(",");
			for(String needOp : needOperateList){
				if(needOp.equalsIgnoreCase(operate)){
					logger.debug("当前安全级别[" + securityLevel + "]要求对[" + objectType + "]进行二次验证，其操作码[" + needOperate + "]也包含了当前操作:" + operate + "，需要进行二次验证");
					needSecAuth = true;
					break;
				}
			}
		}
		if(needSecAuth){
			boolean alreadySecAuthed = certifyService.isSecAuthed(request, response, partner);
			return !alreadySecAuthed;
		}
		logger.debug("当前安全级别[" + securityLevel + "]要求对[" + objectType + "]进行二次验证，其操作码[" + needOperate + "]不包含当前操作:" + operate + "，不进行二次验证");		
		return false;
	}

	/**
	 * 检查IP是否被拉黑
	 * @param request
	 * @param ownerId
	 * @return
	 */
	private boolean ipIsForbidden(HttpServletRequest request, long ownerId) {
		IpPolicyCriteria ipPolicyCriteria = new IpPolicyCriteria();
		ipPolicyCriteria.setOwnerId(ownerId);

		ipPolicyCriteria.setIpAddress(IpUtils.getClientIp(request));
		return ipPolicyService.isForbidden(ipPolicyCriteria);
	}
	
	/**
	 * 检查用户是否配置了登录IP白名单
	 * @param request
	 * @param partner
	 * @return
	 */
	private boolean userIpIsForbidden(HttpServletRequest request, User partner) {
		if(partner == null) {
			logger.error("要检查登录IP白名单的用户为空");
			return false;
		}
		String whiteList = partner.getExtraValue("commonIpWhiteList");
		if(StringUtils.isBlank(whiteList)) {
			logger.debug("用户:{}未设置IP白名单",partner.getUuid());
			return false;
		}
		if(whiteList.equalsIgnoreCase("0.0.0.0") || whiteList.equals("0")) {
			logger.debug("用户:{}设置的IP白名单是全部允许:{}",partner.getUuid(), whiteList);
			return false;
		}
		String ip = IpUtils.getClientIp(request);

		String[] whiteIps = whiteList.split(",|;| ");
		boolean ipIsValid = false;
		for(String whiteIp : whiteIps){
			if(whiteIp.equals(ip)){
				ipIsValid = true;
				break;
			} 
		}
		if(!ipIsValid){
			logger.error("当前IP:" + ip + "]不在商户[" + partner.getUuid() + "]的通用IP白名单中:" + whiteList + "，禁止访问");
			return true;
		}

		return false;
	}

	/*
	 * 提交的所有数据是否合法
	 */
	private boolean inputIsLegal(HttpServletRequest request) {
		/*for(String key :request.getParameterMap().keySet()){
			if(logger.isDebugEnabled()){
				logger.debug("检查输入参数[" + key + "=>" + request.getParameter(key) + "]");
			}
			if(key.equals("title")){
				logger.info("把title参数篡改为test_filter");
				request.getParameterMap().put("title", new String[]{"test_filter"});
			}
			if(!InputFilter.isLegal(key)){
				logger.debug("输入参数[" + key + "]不合法");
				return false;
			}
			if(!InputFilter.isLegal(request.getParameter(key))){
				logger.debug("输入参数[" + key + "]的数据[" + request.getParameter(key) + "]不合法");
				return false;
			}

		}
		 */
		return true;
	}

	/*	String moneyName = configService.getValue(DataName.moneyName.toString(),ownerId);
	String coinName = configService.getValue(DataName.coinName.toString(),ownerId);
	String pointName = configService.getValue(DataName.pointName.toString(),ownerId);
	String scoreName = configService.getValue(DataName.scoreName.toString(),ownerId);
	String commonFooter = configService.getValue(DataName.commonFooter.toString(),ownerId);
	String systemName = configService.getValue(DataName.systemName.toString(), ownerId);
	String theme = configService.getValue(DataName.partnerTheme.toString(), ownerId);*/

	//从缓存或数据库中获取配置
	private PartnerSetting getPartnerSetting(long ownerId){
		PartnerSetting parnterSetting = null;
		if(partnerSettingCache != null && partnerSettingCache.size() > 0){
			parnterSetting = partnerSettingCache.get(String.valueOf(ownerId));
			if(parnterSetting != null){
				logger.debug("从缓存中获取到ownerId=" + ownerId + "的前端配置信息:" + parnterSetting);
				return parnterSetting;
			}
		}
		parnterSetting = new PartnerSetting();

		String theme = configService.getValue(DataName.partnerTheme.toString(), ownerId);
		parnterSetting.theme = (theme == null ? CommonStandard.DEFAULT_THEME_NAME : theme);


		String moneyName = configService.getValue(DataName.moneyName.toString(), ownerId);		
		parnterSetting.moneyName = (moneyName == null ? MoneyType.money.getName() : moneyName);

		String coinName = configService.getValue(DataName.coinName.toString(), ownerId);
		parnterSetting.coinName = (coinName == null ? MoneyType.coin.getName() : coinName);

		String pointName = configService.getValue(DataName.pointName.toString(), ownerId);
		parnterSetting.pointName = (pointName == null ? MoneyType.point.getName() : pointName);

		String scoreName = configService.getValue(DataName.scoreName.toString(), ownerId);
		parnterSetting.scoreName = (scoreName == null ? MoneyType.score.getName() : scoreName);


		String systemName = configService.getValue(DataName.systemName.toString(), ownerId);
		parnterSetting.systemName = systemName;

		String commonFooter = configService.getValue(DataName.commonFooter.toString(),ownerId);
		parnterSetting.commonFooter = (commonFooter == null ? ("版权所有 &copy; " + year + " " + systemName) : HtmlTagTrim.trimWithBr(commonFooter));



		logger.debug("把ownerId=" + ownerId + "的商户配置信息:" + parnterSetting + "放入缓存");
		synchronized(this){
			partnerSettingCache.put(String.valueOf(ownerId), parnterSetting);
		}
		return parnterSetting;
	}

}


class PartnerSetting {

	public String theme;
	public String moneyName;
	public String coinName;
	public String pointName;
	public String scoreName;
	public String loginUrl;
	public String systemName;
	public String commonFooter;

	@Override
	public String toString() {
		return new StringBuffer().append(getClass().getName()).append('@').append(Integer.toHexString(hashCode())).append('(').append("systemName=").append(systemName).append("',").append("theme=").append("'").append(theme).append("',").append("moneyName=").append("'").append(moneyName).append("',").append("coinName=").append("'").append(coinName).append("',").append("pointName=").append(pointName).append("',").append("scroeName=").append(scoreName).append("',").append("loginUrl=").append(loginUrl).append("')").toString();
	}

}

