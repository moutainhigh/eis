package com.maicard.exception;

/**
 * 数据唯一性冲突异常
 *
 *
 * @author NetSnake
 * @date 2016年2月25日
 *
 */
public class DataUniqueConflictException extends RuntimeException{

	private static final long serialVersionUID = -7333450079412535379L;

	public DataUniqueConflictException() {
		super();

	}
	
	public DataUniqueConflictException(String message) {
		super(message);

	}



}
