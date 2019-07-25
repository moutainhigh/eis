package com.maicard.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 不使用各种微信环境下的切面等功能
 *
 *
 * @author NetSnake
 * @date 2016-7-13
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface IgnoreWeixinCheck {

}
