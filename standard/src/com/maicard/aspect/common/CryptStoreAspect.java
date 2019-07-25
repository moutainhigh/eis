package com.maicard.aspect.common;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import com.maicard.annotation.EncryptStore;
import com.maicard.common.base.BaseService;
import com.maicard.common.criteria.SecurityLevelCriteria;
import com.maicard.common.service.ConfigService;
import com.maicard.common.util.Crypt;
import com.maicard.common.util.CryptKeyUtils;
import com.maicard.common.util.SecurityLevelUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * 检查DAO层方法，其存放或取出的对象是否需要进行加密解密<br/>
 * 如果该对象中有属性定义了EncryptStore则进行对应操作<br/>
 * @see com.maicard.annotation.EncryptStore
 * 
 * @author NetSnake
 * @date 2015-12-22
 */
@Aspect
public class CryptStoreAspect extends BaseService {

	@Resource
	private ConfigService configService;

	int securityLevel = SecurityLevelUtils.getSecurityLevel();
	String cryptKey = null;
	Crypt crypt = new Crypt();
	
	private final int MODE_ENCRYPT = 1;
	private final int MODE_DECRYPT = 2;

	@PostConstruct
	public void init(){
		try {
			cryptKey = CryptKeyUtils.readAesKey();
		} catch (Exception e) {
			e.printStackTrace();
		}
		crypt.setAesKey(cryptKey);

	}
	@Around("execution(* com.maicard..*.dao.ibatis.*.*(..)) || execution(* com.maicard..*.dao.mybatis.*.*(..))")
	public Object doAroundForCryptStoreAspect(ProceedingJoinPoint joinPoint) throws Throwable{
		if(securityLevel < SecurityLevelCriteria.SECURITY_LEVEL_STRICT){
			if(logger.isDebugEnabled()){
				logger.debug("当前安全级别[" + securityLevel + "]不进行数据库加密");
			}
			return joinPoint.proceed();
		}
		logger.debug("对方法[" + joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "]进行加密存储检查");
		if(joinPoint.getArgs().length > 1 && joinPoint.getArgs()[0].getClass().isAnnotationPresent(EncryptStore.class)){
			if(logger.isDebugEnabled()){
				logger.debug("当前方法的第一个参数有EncryptStore注解，尝试进行加密处理");
			}
			processCrypt(joinPoint.getArgs()[0], MODE_ENCRYPT);
		} 
		Object resultObject = joinPoint.proceed();
		if(resultObject == null){
			return resultObject;
		}
		if(resultObject.getClass().isAnnotationPresent(EncryptStore.class)){
			if(logger.isDebugEnabled()){
				logger.debug("当前方法的返回值是列表或EisObject，尝试进行解密处理");
			}
			processCrypt(resultObject, MODE_DECRYPT);
			return resultObject;
		}
		if(resultObject	instanceof List){
			@SuppressWarnings("unchecked")
			List<Object> resultList = (List<Object>)resultObject;
			if(resultList.size() < 1){
				return resultObject;
			}
			if(resultList.get(0).getClass().isAnnotationPresent(EncryptStore.class)){
				for(Object obj : resultList){
					processCrypt(obj, MODE_DECRYPT);
				}
			}

		}
		return resultObject;
	}
	
	private void processCrypt(Object object, int mode) {
		try{

			String[] attributes = object.getClass().getAnnotation(EncryptStore.class).value();
			for(String attribute : attributes){
				Field field = object.getClass().getDeclaredField(attribute);
				if(field == null){
					field = object.getClass().getField(attribute);
				}
				if(field == null){
					logger.error("找不到指定加密的属性:" + attribute);
					continue;
				}
				String plainValue = null;
				String getterdName = "get" + StringUtils.capitalize(attribute);
				Method getter = object.getClass().getMethod(getterdName, (Class<?>[])null);
				if(getter == null){
					logger.error("找不到指定加密的属性的getter方法:" + getterdName);
					continue;
				}			
				String setterName = "set" + StringUtils.capitalize(attribute);
				Method setter = object.getClass().getMethod(setterName, field.getType());
				if(setter == null){
					logger.error("找不到指定加密的属性的setter方法:" + setterName);
					continue;
				}
				Object plainObject = getter.invoke(object, (Object[])null);
				if(plainObject == null){
					logger.error("指定加密的属性的getter方法未返回值:" + setterName);
					continue;
				}
				plainValue = plainObject.toString();
				//对plainValue进行加密
				
				String cryptedValue = null;
				if(mode == MODE_ENCRYPT){
					cryptedValue = crypt.aesEncrypt(plainValue);
					logger.debug(plainValue + "加密结果:" + cryptedValue);
				} else {
					cryptedValue = crypt.aesDecrypt(plainValue);
					logger.debug(plainValue + "解密结果:" + cryptedValue);
				}

				if(cryptedValue == null){
					logger.error("加密、解密失败");
					cryptedValue = plainValue;
				} 
				setter.invoke(object, cryptedValue);

			}

		}catch(Exception e){
			logger.error("无法对对象[" + object.getClass() + "]进行加解密处理");
			logger.error(ExceptionUtils.getFullStackTrace(e));
			return;
		}
	}


}
