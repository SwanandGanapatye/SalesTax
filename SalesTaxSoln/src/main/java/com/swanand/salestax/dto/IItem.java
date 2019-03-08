/**
 * 
 */
package com.swanand.salestax.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author swanand.ganapatye
 *
 */
public interface IItem extends Serializable{

	String getName();

	void setName(String name);

	BigDecimal getPrice();

	void setPrice(BigDecimal basePrice);

	boolean isExemptedFromSalesTax();

	void setExemptedFromSalesTax(boolean isExemptedFromSalesTax);

	void setImported(boolean isImported);

	boolean isImported();
	
	
	

}
