/**
 * 
 */
package com.swanand.salestax.process;

import java.math.BigDecimal;
import java.util.Properties;
import java.util.concurrent.Callable;

import com.swanand.salestax.dto.IItem;
import com.swanand.salestax.dto.IItemBillDetails;
import com.swanand.salestax.dto.IOrder;

/**
 * @author swanand.ganapatye
 *The callable thread responsible to calculate tax as per the input. It processes the output in the same object.
 *The thread is not final because the same can be used to extend the functionality if CR changes.
 */
public class SalesTaxProcessor implements Callable<IOrder>{
	
	private static BigDecimal salesTaxPrcent;
	private static BigDecimal additionalSalesTaxPrcent;
	
	static{
		try{
			Properties properties = ConfigCache.getPropertis();
			String salesTaxPrcentStr = properties.getProperty("salesTaxPrcent");
			salesTaxPrcent = new BigDecimal(salesTaxPrcentStr);
			String additionalSalesTaxPrcentStr = properties.getProperty("additionalSalesTaxPrcent");
			additionalSalesTaxPrcent = new BigDecimal(additionalSalesTaxPrcentStr);		
		}catch(Exception e){
			salesTaxPrcent =new BigDecimal(10);
			additionalSalesTaxPrcent =new BigDecimal(5);
		}
	}
	
	private IOrder order;
	
	/**
	 * @param order the order to set
	 */
	public void setOrder(IOrder order) {
		this.order = order;
	}

	public IOrder call() throws Exception {
		processOrder (order);
		return order;
	}

	public void processOrder (IOrder order){
		BigDecimal totalSalesTax = BigDecimal.ZERO;
		BigDecimal total = BigDecimal.ZERO;
		for(IItemBillDetails itemBillDetails : order.getItemBillDetailsList()){
			totalSalesTax = totalSalesTax.add(processItemBillDetails ( itemBillDetails));
			total = total.add(itemBillDetails.getTotalPrice());
		}
		order.setTotalSalesTax(totalSalesTax);
		order.setTotal(total);
	}

	private BigDecimal processItemBillDetails(IItemBillDetails itemBillDetails) {
		
		IItem item = itemBillDetails.getItem();
		BigDecimal salesTax = BigDecimal.ZERO;
		if(item.isImported()){
			salesTax = salesTax.add(calculateImportedSalesTax(item).multiply(new BigDecimal(itemBillDetails.getQuantity())));
		}else{
			salesTax = salesTax.add(calculateBasicSalesTax(item).multiply(new BigDecimal(itemBillDetails.getQuantity())));
		}
		item.setPrice(item.getPrice().add(salesTax)); //updated price with tax
		itemBillDetails.setTotalPrice(item.getPrice().multiply(new BigDecimal( itemBillDetails.getQuantity())));
		return salesTax;
	}

	private BigDecimal calculateImportedSalesTax(IItem item) {
		
		BigDecimal salesTax = BigDecimal.ZERO; //basic + additional
		salesTax = salesTax.add(calculateBasicSalesTax (item));
		salesTax = salesTax.add((roundOffSalesTax(item.getPrice().multiply( additionalSalesTaxPrcent.divide(new BigDecimal(100))))));
		return salesTax;
	}

	private BigDecimal calculateBasicSalesTax(IItem item) {
		BigDecimal basicSalesTax = BigDecimal.ZERO;
		if(!item.isExemptedFromSalesTax() ){
			basicSalesTax = basicSalesTax.add(roundOffSalesTax (item.getPrice().multiply(salesTaxPrcent.divide(new BigDecimal(100)))));
		}
		return basicSalesTax;
	}

	private static BigDecimal roundOffSalesTax(BigDecimal input) {
		//TODO make rounding configurable
		
		return input.multiply(new BigDecimal(20.00)).setScale(0, BigDecimal.ROUND_UP).divide(new BigDecimal(20.00));
	}
}
