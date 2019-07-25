package com.maicard.exception;

public class PayTypeNotFoundException extends RuntimeException{

	private static final long serialVersionUID = -7333450079412535379L;

	public PayTypeNotFoundException() {
		super();

	}
	
	public PayTypeNotFoundException(String message) {
		super(message);

	}



}
