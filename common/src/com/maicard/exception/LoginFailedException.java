package com.maicard.exception;

public class LoginFailedException extends RuntimeException{
	private static final long serialVersionUID = 1479751563507307227L;

	public LoginFailedException() {
		super();

	}
	
	public LoginFailedException(String message) {
		super(message);

	}



}
