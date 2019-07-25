package com.maicard.aspect.boss;


import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.maicard.common.base.BaseService;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.util.IpUtils;
import com.maicard.exception.*;
import com.maicard.security.domain.OperateLog;
import com.maicard.security.domain.User;
import com.maicard.security.service.AuthorizeService;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.OperateLogService;
import com.maicard.standard.OperateLogLevel;
import com.maicard.standard.DataName;
import com.maicard.standard.MoneyType;
import com.maicard.standard.OperateResult;
import com.maicard.standard.SecurityStandard.UserTypes;

import org.springframework.http.HttpStatus;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.ui.ModelMap;

/**
 * 系统安全检查切面
 * 
 * 
 * @author NetSnake
 * @date 2012-10-3
 */
@Aspect
public class SystemSecurityCheckAspect extends BaseService{

	@Resource
	private AuthorizeService authorizeService;
	@Resource
	private CertifyService certifyService;
	@Resource
	private ConfigService configService;
	@Resource
	private ApplicationContextService applicationContextService;	
	@Resource
	private OperateLogService operateLogService;

	private String bossTheme;
	
	boolean bossUsePageShowException = false;

	private String bossOperateLogLevel = null;

	private String moneyName;
	private String coinName;
	private String pointName;
	private String scoreName;


	@PostConstruct
	public void init(){
		//bossUsePageShowException = configService.getBooleanValue(DataName.bossUsePageShowException.toString(),0);
		bossTheme = configService.getValue(DataName.bossTheme.toString(),0);
		bossOperateLogLevel = configService.getValue(DataName.bossOperateLogLevel.toString(),0);
		moneyName = configService.getValue(DataName.moneyName.toString(),0);
		coinName = configService.getValue(DataName.coinName.toString(),0);
		pointName = configService.getValue(DataName.pointName.toString(),0);
		scoreName = configService.getValue(DataName.scoreName.toString(),0);
	}

	@Before("execution(* com.maicard.boss..*.*(..)) || execution(* com.maicard..*.boss.controller..*.*(..))")
	public void generalDataInject(JoinPoint joinPoint) throws Throwable{
		ModelMap map = null;
		try{
			map = (ModelMap)joinPoint.getArgs()[2];
		}catch(Exception e){
		}


		if(map != null){
			if(StringUtils.isBlank(bossTheme)){
				bossTheme = "";
			}
			map.put("bossTheme", bossTheme);

			map.put("moneyName", moneyName == null ? MoneyType.money.getName() : moneyName);
			map.put("coinName", coinName == null ? MoneyType.coin.getName() : coinName);
			map.put("pointName", pointName == null ? MoneyType.point.getName() : pointName);
			map.put("scoreName", scoreName == null ? MoneyType.score.getName() : scoreName);

		}
	}


	//@Before("execution(* com.maicard.cms.controller..*.*(..))")
	/*
	 * 对后台所有的注解BackPrivilegeCheck进行权限检查
	 * 未登录或无对应权限的，将拒绝执行
	 * 并根据系统配置site.throwException决定出现错误时，是直接抛出异常，还是返回普通错误
	 */
	/*	@Around("@annotation(com.maicard.annotation.BackPrivilegeCheck)")
	public Object doAroundForBackSecurityCheck(ProceedingJoinPoint joinPoint) throws Throwable{
		HttpServletRequest request = null;
		HttpServletResponse response = null;
		try{
			request = (HttpServletRequest)joinPoint.getArgs()[0];
			response = (HttpServletResponse)joinPoint.getArgs()[1];
		}catch(Exception e){}
		if(request == null || response == null){
			throw new ObjectNotFoundByIdException("系统request或response不存在");
		}

		boolean throwException = false;
		Config throwExceptionConfig = configService.get("site.throwException");
		try{			
			throwException = Boolean.parseBoolean(throwExceptionConfig.getConfigValue());
		}catch(Exception e){
			throwException = false;
		}

		String resultTemplate= "";
		try{
			resultTemplate = configService.getValue("site.resultTemplate");			
		}catch(Exception e){}
		if(resultTemplate == null || resultTemplate.equals("")){
			resultTemplate = "/WEB-INF/jsp/backMessage/directResult.jsp";
		}
		resultTemplate = request.getSession().getServletContext().getRealPath(resultTemplate);

		User sysUser = (User)authorizeService.getLoginedUser(request);
		if(sysUser == null){
			if(throwException){
				throw new UserNotFoundInRequestException(applicationContextService.getMessage("exception.UserNotFoundInRequestException"));
			}
			applicationContextService.directResponseException(request, response, new UserNotFoundInRequestException());
			return null;
		}
		String className = joinPoint.getTarget().getClass().getName();
		String actionName;
		actionName = request.getParameter("mode");
		if(actionName == null || actionName.equals("")){
			//如果从请求中得不到mode，则使用函数名字作为mode			
			actionName = joinPoint.getSignature().getName();
		}
		if(actionName.endsWith("ist")){
			actionName = "list";
		}
		if(actionName.indexOf("jsonList") >= 0){
			actionName = "list";
		}
		int authResult = authorizeService.isValidAccess(className, actionName, request);
		auditService.send(new Audit(sysUser.getUuid(), authResult, className + "#" + joinPoint.getSignature().getName()));
		if(authResult == Constants.OperateResult.success.getId()){
			//logger.info("权限检查通过，执行本体");
			return joinPoint.proceed();
		}
		if(authResult == Constants.Error.userNotFoundInSession.getId()){
			if(throwException){
				throw new UserNotFoundInRequestException(applicationContextService.getMessage("exception.UserNotFoundInRequestException"));
			}
			applicationContextService.directResponseException(request, response, new UserNotFoundInRequestException());
			return null;
		}
		if(authResult == Constants.errorNoMatchedPrivilege){
			if(throwException){
				throw new NoMatchedPrivilegeException(applicationContextService.getMessage("exception.NoMatchedPrivilegeException"));
			}
			applicationContextService.directResponseException(request, response, new NoMatchedPrivilegeException());
			return null;
		}
		if(throwException){
			throw new InsufficientPrivilegeException(applicationContextService.getMessage("exception.InsufficientPrivilegeException"));
		}
		applicationContextService.directResponseException(request, response, new InsufficientPrivilegeException());
		return null;
	}  */
	@Around("( execution(* com.maicard.boss.controller..*.*(..))" + "||execution(* com.maicard..*.boss.controller..*.*(..)) )" + "&&(!@annotation(com.maicard.annotation.IgnorePrivilegeCheck))")
	public Object doAroundForBossSecurityCheck(ProceedingJoinPoint joinPoint) throws Throwable{
		logger.debug("切入boss安全检查");

		HttpServletRequest request = null;
		HttpServletResponse response = null;
		ModelMap map = null;


		try{
			request = (HttpServletRequest)joinPoint.getArgs()[0];
			response = (HttpServletResponse)joinPoint.getArgs()[1];
			map = (ModelMap)joinPoint.getArgs()[2];

		}catch(Exception e){}
		if(request == null || response == null || map == null){
			if(logger.isDebugEnabled()){
				logger.debug("系统request、response或map不存在，请检查程序编写规范");
			}
			throw new ObjectNotFoundByIdException("系统request、response或map不存在");
		}

		if(StringUtils.isBlank(bossTheme)){
			bossTheme = "";
		}
		map.put("bossTheme", bossTheme);


		User sysUser = (User)certifyService.getLoginedUser(request, response, UserTypes.sysUser.getId());
		if(sysUser == null){
			if(!bossUsePageShowException){
				response.setStatus(HttpStatus.NOT_FOUND.value());
				return null;
			}
			/*if(bossOperateLogLevel != null && (bossOperateLogLevel.equals(OperateLogLevel.ERROR.toString()) || bossOperateLogLevel.equals(OperateLogLevel.POST.toString()) || bossOperateLogLevel.equals(OperateLogLevel.ALL.toString())) ){
				String data = "";
				if(request.getMethod().equals("POST")){
					for(String key : request.getParameterMap().keySet()){
						String data2 = "";
						if(request.getParameterMap().get(key) != null && request.getParameterMap().get(key).length > 0){
							for(String subData : request.getParameterMap().get(key)){
								data2 = data2 + subData + ",";
							}
						}
						data = data + key + "=" + data2.replaceAll(",$",";");
					}
				} 
				operateLogService.insert(new OperateLog("BOSS",null,0,request.getMethod() + "=>" + request.getRequestURI(), OperateResult.deny.getName(), data, ClientIpAddress.getClientIp(request), configService.getServerId()));
			}*/
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return null;
		}	
		String resultTemplate= "";
		try{
			resultTemplate = configService.getValue("backDirectesultTemplate", sysUser.getOwnerId());			
		}catch(Exception e){}
		if(resultTemplate == null || resultTemplate.equals("")){
			resultTemplate = "/WEB-INF/jsp/backMessage/directResult.jsp";
		}
		resultTemplate = request.getSession().getServletContext().getRealPath(resultTemplate);

		if(logger.isDebugEnabled()){
			logger.debug("执行安全检查[" + request.getMethod() + "到URI:" + request.getRequestURI() + ",类:" + joinPoint.getTarget().getClass().toString() + ",方法:" + joinPoint.getSignature().getName() + "]");
		}
		// authorizeService.havePrivilege(request, response);//FIXME authorizeService.isValidAccess(className, actionName, request, response);
		//auditService.send(new Audit(sysUser.getUuid(), authResult, className + "#" + joinPoint.getSignature().getName()));

		User currentUser = certifyService.getLoginedUser(request, response, UserTypes.sysUser.getId());
		if(currentUser == null){
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return null;
		}
		long ownerId = 0;
		if(currentUser.getOwnerId() < 1){
			ownerId = currentUser.getUuid();
		} else {
			ownerId = currentUser.getOwnerId();
		}
		logger.debug("当前访问的系统用户[" + currentUser.getUuid() + "]的ownerId=" + ownerId);
		map.put("ownerId", ownerId);

		//检查当前用户访问的主机名是否属于该ownerId
		/*if(!siteDomainRelationService.isValidOwnerAccess(request.getServerName(), ownerId)){
			logger.warn("当前平台ID[" + ownerId + "]访问的域名[" + request.getServerName() + "]不属于该ID");
			applicationContextService.directResponseException(request, response, new InsufficientPrivilegeException("无权访问"));
			return null;
		}*/
		if(authorizeService.havePrivilege(request, response, UserTypes.sysUser.getId())){
			Object object = joinPoint.proceed();
			logger.debug("bossOperateLogLevel值是"+bossOperateLogLevel);
			if(bossOperateLogLevel != null && (bossOperateLogLevel.equals(OperateLogLevel.POST.toString()) || bossOperateLogLevel.equals(OperateLogLevel.ALL.toString())) ){
				String data = "";
				if(request.getMethod().equals("POST")){
					if(bossOperateLogLevel.equals(OperateLogLevel.POST.toString()) || bossOperateLogLevel.equals(OperateLogLevel.ALL.toString())){
						for(String key : request.getParameterMap().keySet()){
							String data2 = "";
							if(request.getParameterMap().get(key) != null && request.getParameterMap().get(key).length > 0){
								for(String subData : request.getParameterMap().get(key)){
									data2 = data2 + subData + ",";
								}
							}
							data = data + key + "=" + data2.replaceAll(",$",";");
						}
						operateLogService.insert(new OperateLog("BOSS",null,sysUser.getUuid(),request.getMethod() + "=>" + request.getRequestURI(), OperateResult.success.name(), data, IpUtils.getClientIp(request), configService.getServerId(),0));			
					}
				} else if(bossOperateLogLevel.equals(OperateLogLevel.ALL.toString())){
					operateLogService.insert(new OperateLog("BOSS",null,sysUser.getUuid(),request.getMethod() + "=>" + request.getRequestURI(), OperateResult.success.name(), data, IpUtils.getClientIp(request), configService.getServerId(),0));
				}
			}
			return object;
		}
		logger.debug("用户[" + sysUser.getUuid() + "/" + sysUser.getOwnerId() + "]无权访问" + request.getRequestURI());
		if(bossOperateLogLevel != null && (bossOperateLogLevel.equals(OperateLogLevel.ERROR.toString()) || bossOperateLogLevel.equals(OperateLogLevel.ALL.toString())) ){
			String data = "";
			if(request.getMethod().equals("POST")){
				for(String key : request.getParameterMap().keySet()){
					String data2 = "";
					if(request.getParameterMap().get(key) != null && request.getParameterMap().get(key).length > 0){
						for(String subData : request.getParameterMap().get(key)){
							data2 = data2 + subData + ",";
						}
					}
					data = data + key + "=" + data2.replaceAll(",$",";");
				}
			} 
			operateLogService.insert(new OperateLog("BOSS",null,sysUser.getUuid(),request.getMethod() + "=>" + request.getRequestURI(), OperateResult.deny.name(), data, IpUtils.getClientIp(request), configService.getServerId(),0));	
		}
		response.setStatus(HttpStatus.NOT_FOUND.value());
		return null;
	}




}
