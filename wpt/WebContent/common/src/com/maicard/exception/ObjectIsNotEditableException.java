package com.maicard.exception;

public class ObjectIsNotEditableException extends RuntimeException{

	private static final long serialVersionUID = -7333450079412535379L;

	public ObjectIsNotEditableException() {
		super();

	}
	
	public ObjectIsNotEditableException(String message) {
		super(message);

	}



}
