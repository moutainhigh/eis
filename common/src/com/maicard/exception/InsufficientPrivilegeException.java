package com.maicard.exception;

public class InsufficientPrivilegeException extends RuntimeException{

	private static final long serialVersionUID = -7333450079412535379L;

	public InsufficientPrivilegeException() {
		super();

	}
	
	public InsufficientPrivilegeException(String message) {
		super(message);

	}



}
