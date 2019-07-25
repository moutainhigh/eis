package com.maicard.exception;

public class ServiceUnavailableException extends RuntimeException{

	private static final long serialVersionUID = -7333450079412535379L;

	public ServiceUnavailableException() {
		super();

	}
	
	public ServiceUnavailableException(String message) {
		super(message);

	}



}
