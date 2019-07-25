package com.maicard.exception;

public class NoMatchedPrivilegeException extends RuntimeException{

	private static final long serialVersionUID = -7333450079412535379L;

	public NoMatchedPrivilegeException() {
		super();

	}
	
	public NoMatchedPrivilegeException(String message) {
		super(message);

	}



}
