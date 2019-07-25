package com.maicard.security.criteria;

import com.maicard.common.base.Criteria;
/**
 * 玩家位置查询条件
 *
 *
 * @author NetSnake
 * @date 2016年12月22日
 *
 */
public class UserLocationCriteria extends Criteria{

	private static final long serialVersionUID = 1982771604088045383L;

	private long uuid;

	public UserLocationCriteria() {
	}

	public UserLocationCriteria(long ownerId) {
		this.ownerId = ownerId;
	}

	public long getUuid() {
		return uuid;
	}

	public void setUuid(long uuid) {
		this.uuid = uuid;
	}


}
