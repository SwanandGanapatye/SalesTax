/**
 * 
 */
package com.swanand.salestax.utility;

import javax.swing.JOptionPane;

import com.swanand.salestax.exception.SalesTaxError;
import com.swanand.salestax.exception.SalesTaxException;



/**
 * @author swanand.ganapatye
 *
 */
public class ExceptionHandler implements IExceptionHandler{
	
	/**
	 * The class is responsible for handling the exception in this application.
	 */
private static ExceptionHandler exceptionHandler = null;
	
	private ExceptionHandler(){
		
	}
	
	public static ExceptionHandler getInstance(){
		if (exceptionHandler == null){
			synchronized (ExceptionHandler.class) {
				if (exceptionHandler == null){
					exceptionHandler = new ExceptionHandler();
				}
			}
		}
		return exceptionHandler;
	}

	public void handleThrowable (Throwable t){
		logThrowable(t);
		if (t instanceof Error  ){
			if(t.getMessage()!= null){
				JOptionPane.showMessageDialog(null, t.getMessage());
			}
			JOptionPane.showMessageDialog(null, "Something went wrong badly. \n Application is shutting down.");//handle through constants class
			System.exit(0);
		}if (t instanceof SalesTaxException  ){
			SalesTaxException f = (SalesTaxException)t;
			JOptionPane.showMessageDialog(null, f.getMessage());//handle through constants class
		}else{
			JOptionPane.showMessageDialog(null, "Something went wrong. \n But application is still running.");
		}
		
	}

	private void logThrowable(Throwable t) {
		// TODO implement logging machanism
		t.printStackTrace();
		if(t instanceof SalesTaxException ){
			SalesTaxException e = (SalesTaxException)t;
			if(e.getException() != null){
				System.out.println("Caused by:");
				e.getException().printStackTrace();
			}
		}
		if( t instanceof SalesTaxError){
			SalesTaxError e = (SalesTaxError)t;
			if(e.getException() != null){
				System.out.println("Caused by:");
				e.getException().printStackTrace();
			}
		}
	}

	public void showPopUp(String message, Throwable t) {
		JOptionPane.showMessageDialog(null, message);
		System.out.println("Popup shown for ");
		logThrowable(t);
	}

}
