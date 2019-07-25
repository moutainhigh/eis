package com.maicard.exception;

public class RequiredObjectIsNullException extends RuntimeException{

	private static final long serialVersionUID = -7333450079412535379L;

	public RequiredObjectIsNullException() {
		super();

	}
	
	public RequiredObjectIsNullException(String message) {
		super(message);

	}



}
