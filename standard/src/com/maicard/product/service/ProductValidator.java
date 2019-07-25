/**
 * 
 */
package com.maicard.product.service;

import com.maicard.product.domain.Item;

/**
 * 对指定交易品进行规则校验
 * 
 *
 * @author NetSnake
 * @date 2013-9-4 
 */
public interface ProductValidator {
	/*
	 * 只应返回4种状态：
	 * 校验成功 OperateResult.success
	 * 
	 * 对象为空 Error.ObjectIsNull
	 * 数据格式错误Error.dataFormatError
	 * 对象已存在Error.objectAlreadyExist
	 * 
	 * 
	 */
	//int validate(String action, Item item);
	
	int getLabelMoneyFromCard(Item item);

	int validate(String action, Item item, Object params);



}
