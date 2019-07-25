package com.maicard.exception;

public class DataFormatErrorException extends RuntimeException{

	private static final long serialVersionUID = -7333450079412535379L;

	public DataFormatErrorException() {
		super();

	}
	
	public DataFormatErrorException(String message) {
		super(message);

	}



}
