package com.maicard.aspect.front;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.maicard.annotation.AuthorizeProcess;
import com.maicard.common.base.BaseService;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.ConfigService;
import com.maicard.security.domain.User;
import com.maicard.security.processor.AuthorizeProcessor;
import com.maicard.security.service.AuthorizeService;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.FrontUserService;
import com.maicard.site.domain.Document;
import com.maicard.standard.Operate;
import com.maicard.standard.SecurityStandard.UserTypes;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.springframework.ui.ModelMap;

/**
 * 前端访问权限控制切面
 * 
 * 
 * @author NetSnake
 * 
 * 
 * @date 2013-6-16
 */
//@Aspect
public class FrontAuthorizeAspect extends BaseService{
	

	@Resource
	private ApplicationContextService applicationContextService;
	@Resource
	private AuthorizeService authorizeService;
	@Resource
	private CertifyService certifyService;
	@Resource
	private ConfigService configService;
	@Resource
	private FrontUserService frontUserService;	

	


	@Before("execution(* org.springframework.web.servlet.view.*.render(..))")
	public void doAroundForBossSecurityCheck(JoinPoint joinPoint) throws Throwable{
		logger.debug("切入前端授权检查：" + joinPoint.getTarget().getClass().getName() + ",method:" +joinPoint.getSignature().getName());
		logger.debug("有[" + joinPoint.getArgs().length + "]个参数");
		if(joinPoint.getArgs().length > 0){
			for(Object obj : joinPoint.getArgs()){
				logger.debug(">>>>>>>>>>>" + obj.getClass().getName());
			}
		}
		HttpServletRequest request = null;
		ModelMap map = null;
		try{
			map = (ModelMap)joinPoint.getArgs()[0];
			request = (HttpServletRequest)joinPoint.getArgs()[1];
		}catch(Exception e){
			e.printStackTrace();
		}
		if(map == null){
			return;
		}
		if(request == null){
			logger.error("joinPoint中找不到请求request对象");
			return;
		}

		Document document = null;
		String documentKey = null;
		for(String key : map.keySet()){
			Object obj = map.get(key);
			logger.debug(">>>>>>>>>>>>>" + map.get(key).getClass().getName());
			if(obj.getClass() ==  Document.class){
				documentKey = key;
				document = (Document)obj;
				break;
			}
		}
		if(document == null){
			logger.debug("当前请求中不存在需要处理的Document对象");
			return;
		}
		String[] authorizeProcessors = applicationContextService.getBeanNamesForType(AuthorizeProcessor.class);
		logger.debug("当前系统中有[" + (authorizeProcessors == null ? -1 : authorizeProcessors.length) + "]个AuthorizeProcessor.");
		User frontUser = certifyService.getLoginedUser(request, null, UserTypes.frontUser.getId());
		if(frontUser == null){
			frontUser = frontUserService.getUnLoginedUser();
		}
		if(authorizeProcessors != null && authorizeProcessors.length > 0){
			for(String processor : authorizeProcessors){
				try{
					AuthorizeProcessor authorizeProcessor = (AuthorizeProcessor)applicationContextService.getBean(processor);
					AuthorizeProcess authorizeAnnotation = (AuthorizeProcess)applicationContextService.findAnnotationOnBean(processor, AuthorizeProcess.class);
					if(authorizeAnnotation.value() == Document.class){
						document = (Document)authorizeProcessor.filter(frontUser, document, Operate.get.getCode());
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		map.put(documentKey, document);
		logger.debug("XXXXXXX" + documentKey + "=" + map.get(documentKey));


	}
}
