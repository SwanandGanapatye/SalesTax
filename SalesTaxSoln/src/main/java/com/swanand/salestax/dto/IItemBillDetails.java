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
public interface IItemBillDetails extends Serializable {

	IItem getItem();

	void setItem(IItem item);

	int getQuantity();

	void setQuantity(int quantity);

	BigDecimal getTotalPrice();

	void setTotalPrice(BigDecimal totalPrice);

}
