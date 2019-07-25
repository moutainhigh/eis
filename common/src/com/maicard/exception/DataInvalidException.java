package com.maicard.exception;

public class DataInvalidException extends RuntimeException{

	private static final long serialVersionUID = -7333450079412535379L;

	public DataInvalidException() {
		super();

	}
	
	public DataInvalidException(String message) {
		super(message);

	}



}
