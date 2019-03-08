/**
 * 
 */
package com.swanand.salestax.dto;

import java.math.BigDecimal;

/**
 * @author swanand.ganapatye
 *
 */
public class ItemBillDetails implements IItemBillDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3726791819611152613L;

	private IItem item;
	
	private int quantity;
	
	private BigDecimal totalPrice;

	/**
	 * @return the item
	 */
	public IItem getItem() {
		return item;
	}

	/**
	 * @param item the item to set
	 */
	public void setItem(IItem item) {
		this.item = item;
	}

	/**
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the totalPrice
	 */
	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	/**
	 * @param totalPrice the totalPrice to set
	 */
	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}
}
