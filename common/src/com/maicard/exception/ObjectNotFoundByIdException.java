package com.maicard.exception;

public class ObjectNotFoundByIdException extends RuntimeException{

	private static final long serialVersionUID = -7333450079412535379L;

	public ObjectNotFoundByIdException() {
		super();

	}
	
	public ObjectNotFoundByIdException(String message) {
		super(message);

	}



}
