/**
 * 
 */
package com.swanand.salestax.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author swanand.ganapatye
 *
 */
public interface IOrder extends Serializable {

	List<IItemBillDetails> getItemBillDetailsList();

	void setItemBillDetailsList(List<IItemBillDetails> itemBillDetailsList);

	BigDecimal getTotalSalesTax();

	void setTotalSalesTax(BigDecimal totalSalesTax);

	String getOrderId();

	void setOrderId(String orderId);

	BigDecimal getTotal();

	void setTotal(BigDecimal total);

}
