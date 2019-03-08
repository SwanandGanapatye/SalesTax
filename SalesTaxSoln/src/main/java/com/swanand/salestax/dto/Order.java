/**
 * 
 */
package com.swanand.salestax.dto;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

/**
 * @author swanand.ganapatye
 *
 */
public class Order implements IOrder {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8413140675106407429L;
	/**
	 * 
	 */
	private List<IItemBillDetails> itemBillDetailsList = new LinkedList<IItemBillDetails>();
	private String orderId;

	private BigDecimal totalSalesTax;
	private BigDecimal total;
	/**
	 * @return the itemBillDetailsList
	 */
	public List<IItemBillDetails> getItemBillDetailsList() {
		return itemBillDetailsList;
	}

	/**
	 * @param itemBillDetailsList the itemBillDetailsList to set
	 */
	public void setItemBillDetailsList(List<IItemBillDetails> itemBillDetailsList) {
		this.itemBillDetailsList = itemBillDetailsList;
	}

	/**
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}

	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	/**
	 * @return the totalSalesTax
	 */
	public BigDecimal getTotalSalesTax() {
		return totalSalesTax;
	}

	/**
	 * @param totalSalesTax the totalSalesTax to set
	 */
	public void setTotalSalesTax(BigDecimal totalSalesTax) {
		this.totalSalesTax = totalSalesTax;
	}

	/**
	 * @return the total
	 */
	public BigDecimal getTotal() {
		return total;
	}

	/**
	 * @param total the total to set
	 */
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	//TODO make it decent 
	@Override
	public String toString(){
		for(IItemBillDetails itemDetails :this.itemBillDetailsList){
			IItem item = itemDetails.getItem();
			String imported = item.isImported()? "imported":"";
			String line = itemDetails.getQuantity()+" "+imported+" "+item.getName()+" : "+item.getPrice().multiply(new BigDecimal(itemDetails.getQuantity()));
			return line;
		}
		return orderId;
		
	}
	
}
