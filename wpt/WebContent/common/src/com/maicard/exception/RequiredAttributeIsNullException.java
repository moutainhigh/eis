package com.maicard.exception;

public class RequiredAttributeIsNullException extends RuntimeException{

	private static final long serialVersionUID = -7333450079412535379L;

	public RequiredAttributeIsNullException() {
		super();

	}
	
	public RequiredAttributeIsNullException(String message) {
		super(message);

	}



}
