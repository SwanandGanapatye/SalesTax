package com.swanand.salestax.input.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.Callable;

import com.swanand.salestax.dto.IOrder;
import com.swanand.salestax.dto.Order;
import com.swanand.salestax.exception.SalesTaxException;
import com.swanand.salestax.input.AbstractInputReader;
import com.swanand.salestax.utility.ExceptionHandler;

/**
 * 
 * @author swanand.ganapatye
 *This class is callable thread which reads files and generate Iorder input for processing.
 */

public class FileInputReader extends AbstractInputReader implements Callable<IOrder>{

	private File file ;
	
	/**
	 * @param file the file to set
	 */
	public void setFile(File file) {
		this.file = file;
	}

	public IOrder processInput (Object object) {
		
		IOrder order = new Order();
		try{
			try {
				File file = (File) object;
				order.setOrderId(file.getName());
				BufferedReader br = new BufferedReader(new FileReader(file));
				try {
				    for(String line; (line = br.readLine()) != null; ) {
				    	if(!line.isEmpty()){//skip blank lines
				    		order.getItemBillDetailsList().add(processLine (line));
				    	}
				    }
				    if(order.getItemBillDetailsList().isEmpty()){
				    	throw new SalesTaxException("The file is blank "+file.getName());
				    }
				}finally{
						br.close();
				}
			} catch (IOException e) {
				throw new SalesTaxException("Something went wrong while reading file "+file.getName(),e);
			}
		}catch(Throwable e){
			order.getItemBillDetailsList().clear();
			ExceptionHandler.getInstance().handleThrowable(e);
		}
		return order;
		
	}

	public IOrder call() /*throws Exception*/ {
		if(file == null){
			try {
				throw new SalesTaxException("Something went wrong while reading file ", new NullPointerException());
			} catch (SalesTaxException e) {
				ExceptionHandler.getInstance().handleThrowable(e);
			}
			return null;
		}
		return processInput(file);
	}
	
}
