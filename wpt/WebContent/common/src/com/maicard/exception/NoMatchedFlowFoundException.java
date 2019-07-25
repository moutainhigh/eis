package com.maicard.exception;

public class NoMatchedFlowFoundException extends RuntimeException{

	private static final long serialVersionUID = -7333450079412535379L;

	public NoMatchedFlowFoundException() {
		super();

	}
	
	public NoMatchedFlowFoundException(String message) {
		super(message);

	}



}
