/**
 * 
 */
package com.swanand.salestax.input;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.swanand.salestax.dto.IItem;
import com.swanand.salestax.dto.IItemBillDetails;
import com.swanand.salestax.dto.IOrder;
import com.swanand.salestax.dto.Item;
import com.swanand.salestax.dto.ItemBillDetails;
import com.swanand.salestax.exception.SalesTaxException;
import com.swanand.salestax.process.ConfigCache;

/**
 * @author swanand.ganapatye
 *
 */
public abstract class  AbstractInputReader implements IInputReader{
	
	final static List<String> exemptedList = new LinkedList<String>();

	public abstract IOrder processInput(Object object) throws SalesTaxException;
	
	static  {
		String exmptedStr = ConfigCache.getPropertis().getProperty("exemptedList");
		if(exmptedStr !=null){
		for(String str : exmptedStr.split(","))
			exemptedList.add(str.trim());
		}
	}

	protected IItemBillDetails processLine (String line) throws SalesTaxException{
		
		IItem item = new Item();
		IItemBillDetails itemDetails = new ItemBillDetails();
		try{
			itemDetails.setItem(item);
			Pattern p = Pattern.compile("(^\\d+)|([a-zA-Z ]*at)|(-?\\d+.\\d{2})");//TODO: remove 'at' from regex
			Matcher m = p.matcher(line);
			
			//seems crude code ahead, but it's the most efficient for String operation
			if(m.find()) {//matched for quantity
				itemDetails.setQuantity(Integer.valueOf(m.group()));
			}else{
				throw new SalesTaxException("input line is not as per syntax");
			}
			if(m.find()) {//matched for name
				String itemName = m.group();
				String imported = "imported ";
				if (itemName.toLowerCase().contains(imported)){
					item.setImported(Boolean.TRUE);
					itemName = itemName.replace(imported, "");
				}
				item.setName(itemName.substring(0,itemName.length() -3 ));//to remove last 'at'
				checkSalesExemption(item);
			}else{
				throw new SalesTaxException("input line is not as per syntax");
			}
			if(m.find()) {//matched for price
				item.setPrice(new BigDecimal(m.group()));
			}else{
				throw new SalesTaxException("input line is not as per syntax");
			}
		}catch(Exception e){
			throw new SalesTaxException("input line is not as per syntax",e);
		}
			
		return itemDetails;
		
	}

	private void checkSalesExemption(IItem item) {
		for(String str: exemptedList){
			if(item.getName().contains(str)){
				item.setExemptedFromSalesTax(Boolean.TRUE);
				break;
			}
		}
		
	}

}
