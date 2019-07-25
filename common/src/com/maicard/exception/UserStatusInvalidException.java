package com.maicard.exception;

public class UserStatusInvalidException extends RuntimeException{
	private static final long serialVersionUID = 1479751563507307227L;

	public UserStatusInvalidException() {
		super();

	}
	
	public UserStatusInvalidException(String message) {
		super(message);

	}



}
