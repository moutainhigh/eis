package com.maicard.exception;

public class ConfigNotFoundException extends RuntimeException{


	private static final long serialVersionUID = 6113709532611350939L;

	public ConfigNotFoundException() {
		super();

	}
	
	public ConfigNotFoundException(String message) {
		super(message);

	}



}
