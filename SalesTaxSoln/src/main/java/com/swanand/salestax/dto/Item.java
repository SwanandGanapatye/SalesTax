/**
 * 
 */
package com.swanand.salestax.dto;

import java.math.BigDecimal;

/**
 * @author swanand.ganapatye
 *
 */
public class Item implements IItem{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3938261342287870890L;
	private String name;
	private BigDecimal price;
	private boolean isExemptedFromSalesTax;
	private boolean isImported;
	
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the basePrice
	 */
	public BigDecimal getPrice() {
		return price;
	}
	/**
	 * @param basePrice the basePrice to set
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	/**
	 * @return the isExemptedFromSalesTax
	 */
	public boolean isExemptedFromSalesTax() {
		return isExemptedFromSalesTax;
	}
	/**
	 * @param isExemptedFromSalesTax the isExemptedFromSalesTax to set
	 */
	public void setExemptedFromSalesTax(boolean isExemptedFromSalesTax) {
		this.isExemptedFromSalesTax = isExemptedFromSalesTax;
	}
	/**
	 * @return the isImportDutyApplicable
	 */
	public boolean isImported() {
		return isImported;
	}
	/**
	 * @param isImportDutyApplicable the isImportDutyApplicable to set
	 */
	public void setImported(boolean isImported) {
		this.isImported = isImported;
	}
}
