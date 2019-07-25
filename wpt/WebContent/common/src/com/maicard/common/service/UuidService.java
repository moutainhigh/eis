package com.maicard.common.service;

import com.maicard.common.domain.Uuid;


public interface UuidService {
	long insert(Uuid uuid);
	

	/**
	 * 根据指定ID创建一个自增ID序列，liveSec为有效期，为0则使用默认最大有效期1年
	 * 
	 *
	 * @author GHOST
	 * @date 2017-12-06
	 */
	long createById(long typeId, long liveSec);


	/**
	 * 根据指定ID创建一个自增ID序列，minId指定返回的自增ID不能小于这个ID，liveSec为有效期，为0则使用默认最大有效期1年
	 * 
	 *
	 * @author NetSnake
	 * @date 2018-05-07
	 */
	long createById(long typeId, long minId, long liveSec);
	
	

}
