package com.maicard.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 拥有该注解的字段可以在管理后台的字段中出现
 * value为该字段属于哪个spring:message的前缀
 *
 *
 * @author NetSnake
 * @date 2016年6月9日
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DisplayColumn {
	
	String value() default "";
	
	

}
