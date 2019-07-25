package com.maicard.common.base;

import java.util.concurrent.Callable;

/**
 * 实现该接口的类，将不应被Spring自动扫描
 *
 *
 * @author NetSnake
 * @date 2017年1月29日
 *
 */
public interface NotComponentThread extends Callable<Object>{

}
