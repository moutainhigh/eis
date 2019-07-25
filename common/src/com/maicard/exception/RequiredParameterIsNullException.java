package com.maicard.exception;

public class RequiredParameterIsNullException extends RuntimeException{

	private static final long serialVersionUID = -7333450079412535379L;

	public RequiredParameterIsNullException() {
		super();

	}
	
	public RequiredParameterIsNullException(String message) {
		super(message);

	}



}
