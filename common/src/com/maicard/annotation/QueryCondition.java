package com.maicard.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 可查询条件<br/>
 * value=查询级别
 * queryEnum为该查询属性的列表值
 * 
 * @see com.maicard.standard.SystemLevel<br/>
 *
 *
 * @author NetSnake
 * @date 2016年1月15日
 *
 */
@Retention(RetentionPolicy.RUNTIME)  
@Target(ElementType.FIELD)
public @interface QueryCondition {
	//可调用级别 @see SystemLevel
	String[] value() default "";	

}
