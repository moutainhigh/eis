package com.maicard.aspect.common;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.maicard.common.base.BaseService;
import com.maicard.common.base.DataBaseContextHolder;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.ConfigService;
import com.maicard.security.service.AuthorizeService;
import com.maicard.standard.DataName;

import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;

/*
 * 异常切面
 * 当系统抛出异常时，根据系统配置确定是直接抛出异常还是返回正常错误输出
 */
@Aspect
public class ExceptionAspect extends BaseService{
	@Resource
	private ApplicationContextService applicationContextService;
	@Resource
	private ConfigService configService;
	@Resource
	private AuthorizeService authorizeService;
	
	boolean throwException = false;
	
	@PostConstruct
	public void init(){
		throwException = configService.getBooleanValue(DataName.systemThrowException.toString(),0);
	}

		

	//@AfterThrowing(pointcut = "execution(* com.maicard..*.*(..))", throwing = "t")
	public void afterThrowing(JoinPoint joinPoint, Throwable t) throws Throwable{
		logger.error("捕获到异常:" + t.getClass().getName() + ",动态数据源:" + DataBaseContextHolder.getDbSource() + "]" + t.getMessage());
		StackTraceElement[] trace = t.getStackTrace();
		if(trace != null){
			StringBuffer sb = new StringBuffer();
			sb.append("\n");
			for(int i =0 ; i < trace.length; i++){
				sb.append(trace[i]);
				sb.append("\n");
			}
			logger.error(sb.toString());
		}
		if(t.getClass() == org.springframework.context.NoSuchMessageException.class){
			throw t;
		}
		HttpServletRequest request = null;
		HttpServletResponse response = null;
		try{
			request = (HttpServletRequest)joinPoint.getArgs()[0];
			response = (HttpServletResponse)joinPoint.getArgs()[1];
		}catch(Exception e){}
		if(request == null || response == null){
			throw t;
		}
		
		if(throwException){
			throw t;
		} else {
			applicationContextService.directResponseException(request, response, t);
		}
		
		
	}



}
