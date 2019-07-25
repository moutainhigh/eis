package com.maicard.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 本注解只允许放到某个对象（实体类）上<br/>
 * 并指定哪些属性将以加密形式存放在数据库中<br/>
 * 存放时加密，取出时解密
 *
 *
 * @author NetSnake
 * @date 2015年12月23日
 *
 */
@Retention(RetentionPolicy.RUNTIME)  
@Target(ElementType.TYPE)
public @interface EncryptStore {
	
	String[] value();

}
