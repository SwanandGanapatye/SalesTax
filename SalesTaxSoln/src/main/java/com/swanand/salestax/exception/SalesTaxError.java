/**
 * 
 */
package com.swanand.salestax.exception;

/**
 * @author swanand.ganapatye
 *
 */
public class SalesTaxError extends Error {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3532206682644209526L;

	private String message;

	private Exception exception;

	public SalesTaxError(String message, Exception exception) {
		this.message=message;
		this.exception=exception;
	}

	public SalesTaxError(String message) {
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
