package com.maicard.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 授权处理
 * 
 * 
 * @author NetSnake
 *
 */
@Retention(RetentionPolicy.RUNTIME)  
public @interface AuthorizeProcess {
	Class<?> value ();

}
