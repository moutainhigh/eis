package com.maicard.exception;

/**
 * 数据与其他数据关联冲突
 *
 *
 * @author NetSnake
 * @date 2016年2月25日
 *
 */
public class DataRelationConflictException extends RuntimeException{

	private static final long serialVersionUID = -1141576678172767577L;

	public DataRelationConflictException() {
		super();

	}
	
	public DataRelationConflictException(String message) {
		super(message);

	}



}
