package com.swanand.salestax.output.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import com.swanand.salestax.dto.IItemBillDetails;
import com.swanand.salestax.dto.IOrder;
import com.swanand.salestax.exception.SalesTaxException;
import com.swanand.salestax.output.AbstractOutputGenerator;
import com.swanand.salestax.process.ConfigCache;
import com.swanand.salestax.utility.ExceptionHandler;

/**
 * 
 * @author swanand.ganapatye
 *The callable thread to generate output file from output object
 */
public class FileOutputGenerator extends AbstractOutputGenerator {
	
	private IOrder order;

	private static String outputDirectoryPath; 
	private static String fileExtension;
	static{
		Properties properties = ConfigCache.getPropertis();
		outputDirectoryPath = (properties.getProperty("outputDirectoryPath")!= null)?properties.getProperty("outputDirectoryPath").trim():".";
		 fileExtension = (properties.getProperty("outputFileExtension")!= null)?properties.getProperty("outputFileExtension").trim():"output"; 
			
	}
	/**
	 * @param order the order to set
	 */
	public void setOrder(IOrder order) {
		this.order = order;
	}

	public File call() throws Exception {
		return generateOutput();
	}

	 public File generateOutput() {
		File file = null;
		try{
			file = new File(outputDirectoryPath+order.getOrderId()+"."+fileExtension);
			BufferedWriter bw;
		    try {
		    	file.createNewFile();
		    	bw = new BufferedWriter(new FileWriter(file));
		    	try{
		    		if(order.getItemBillDetailsList().isEmpty()){
		    			bw.write("Something went wrong for this file");
		    		}else{
						for(IItemBillDetails itemDetails : order.getItemBillDetailsList() ){
							bw.write(generateLine(itemDetails));
						}
						bw.write("Sales Taxes: "+formatDecimal(order.getTotalSalesTax())+"\n");
						bw.write("Total: "+formatDecimal(order.getTotal()));
		    		}
					bw.flush();
		    	}finally{
		    		bw.close();
		    	}
			} catch (IOException e) {
				throw new SalesTaxException("Output file generation failed for file "+order.getOrderId(),e);
			}
		}catch(Exception e){
			ExceptionHandler.getInstance().handleThrowable(e);
		}
	    return file;
	}

	


}
