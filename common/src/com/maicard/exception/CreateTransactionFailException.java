package com.maicard.exception;

public class CreateTransactionFailException extends RuntimeException{

	private static final long serialVersionUID = -7333450079412535379L;

	public CreateTransactionFailException() {
		super();

	}
	
	public CreateTransactionFailException(String message) {
		super(message);

	}



}
