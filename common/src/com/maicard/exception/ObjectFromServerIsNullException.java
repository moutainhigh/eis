package com.maicard.exception;

public class ObjectFromServerIsNullException extends RuntimeException{

	private static final long serialVersionUID = -7333450079412535379L;

	public ObjectFromServerIsNullException() {
		super();

	}
	
	public ObjectFromServerIsNullException(String message) {
		super(message);

	}



}
