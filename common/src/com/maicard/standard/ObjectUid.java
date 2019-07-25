package com.maicard.standard;

/**
 * 给每种对象分配一个不同的UID，可以用这个UID区分不同对象
 * 
 *
 *
 * @author NetSnake
 * @date 2018-05-07
 */
public enum ObjectUid {
	/**
	 * 用户关联
	 */
	USER_RELATION(1), 
	
	/**
	 * 前端用户
	 */
	FRONT_USER(2);
	
	public int id;
	
	private ObjectUid(int id){
		this.id = id;
	}

}
