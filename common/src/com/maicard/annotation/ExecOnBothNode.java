package com.maicard.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 拥有该注解的方法，在收到JMS同步请求时，无论在哪个节点，都将会执行。<br>
 * 而正常来说，只有配置为同步本地数据的节点才会执行JMS同步请求
 * 
 *
 *
 * @author NetSnake
 * @date 2017年5月6日
 *
 */
@Retention(RetentionPolicy.RUNTIME)  
@Target(ElementType.METHOD)
public @interface ExecOnBothNode {

}
