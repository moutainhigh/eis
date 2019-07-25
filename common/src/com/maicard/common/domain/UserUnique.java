package com.maicard.common.domain;

/**
 * 基于用户和ownerId的唯一性ID
 *
 *
 * @author NetSnake
 * @date 2016年5月22日
 *
 */
public class UserUnique {

	private String userUniqueKey;

	private long uuid;

	private long ownerId;

	public UserUnique() {
	}


	public UserUnique(String userUniqueKey, long uuid, long ownerId) {
		this.userUniqueKey = userUniqueKey;
		this.uuid = uuid;
		this.ownerId = ownerId;
	}

	public String getUserUniqueKey() {
		return userUniqueKey;
	}

	public void setUserUniqueKey(String userUniqueKey) {
		this.userUniqueKey = userUniqueKey;
	}

	public long getUuid() {
		return uuid;
	}

	public void setUuid(long uuid) {
		this.uuid = uuid;
	}

	public long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(long ownerId) {
		this.ownerId = ownerId;
	}

}
