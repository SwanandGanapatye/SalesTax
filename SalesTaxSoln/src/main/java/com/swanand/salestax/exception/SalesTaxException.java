package com.swanand.salestax.exception;


public class SalesTaxException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3216071750584085571L;

	private String message;

	private Exception exception;

	public SalesTaxException(String message, Exception exception) {
		this.message=message;
		this.exception=exception;
	}

	public SalesTaxException(String message) {
		this.message=message;
	}

	/**
	 * @return the message
	 */
	@Override
	public String getMessage() {
		return message;
	}

	/**
	 * @return the exception
	 */
	public Exception getException() {
		return exception;
	}

	

}
