package com.maicard.exception;

public class UserNotFoundInRequestException extends RuntimeException{
	private static final long serialVersionUID = 1479751563507307227L;

	public UserNotFoundInRequestException() {
		super();

	}
	
	public UserNotFoundInRequestException(String message) {
		super(message);

	}



}
