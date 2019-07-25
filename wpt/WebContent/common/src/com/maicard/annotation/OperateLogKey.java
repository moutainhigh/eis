package com.maicard.annotation;

import java.lang.annotation.*;

/**
 * @author xiaomou
 * @date 2018/4/23 11:21
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperateLogKey {

}
