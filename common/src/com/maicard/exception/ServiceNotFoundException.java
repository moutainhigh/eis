package com.maicard.exception;

public class ServiceNotFoundException extends RuntimeException{

	private static final long serialVersionUID = -7333450079412535379L;

	public ServiceNotFoundException() {
		super();

	}
	
	public ServiceNotFoundException(String message) {
		super(message);

	}



}
