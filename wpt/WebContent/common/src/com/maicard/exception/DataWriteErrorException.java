package com.maicard.exception;

public class DataWriteErrorException extends RuntimeException{

	private static final long serialVersionUID = -7333450079412535379L;

	public DataWriteErrorException() {
		super();

	}
	
	public DataWriteErrorException(String message) {
		super(message);

	}



}
