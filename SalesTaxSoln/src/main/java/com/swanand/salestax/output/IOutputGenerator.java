package com.swanand.salestax.output;

import java.util.concurrent.Callable;

import com.swanand.salestax.dto.IOrder;

/**
 * 
 * @author swanand.ganapatye
 *
 */
public interface IOutputGenerator extends Callable<Object>{
	
	void setOrder(IOrder order);
	Object generateOutput();

}
