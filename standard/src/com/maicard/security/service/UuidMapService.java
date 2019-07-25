package com.maicard.security.service;

import com.maicard.security.domain.User;

/**
 * 用来在帐号和角色之间进行UUID映射
 *
 *
 * @author NetSnake
 * @date 2017年2月13日
 *
 */
public interface UuidMapService {
	
	/**
	 * 获取一个用户可能存在的角色子账户UUID
	 * 
	 * @param user
	 * @return
	 */
	long getMoneyUuid(User user, String appCode);

}
