package com.swanand.salestax.output;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import com.swanand.salestax.dto.IItem;
import com.swanand.salestax.dto.IItemBillDetails;

/**
 * 
 * @author swanand.ganapatye
 *This class uses decorator design pattern for output generator. The same functionality can be used by multiple generator clases 
 *like console output generator, file output generator
 */
public abstract class AbstractOutputGenerator implements IOutputGenerator {

	protected String generateLine(IItemBillDetails itemDetails){
		IItem item = itemDetails.getItem();
		String imported = item.isImported()? " imported ":" ";
		String line = itemDetails.getQuantity()+imported+item.getName().trim()+" : "+formatDecimal(item.getPrice().multiply(new BigDecimal(itemDetails.getQuantity())))+"\n";
		return line;
	}
	
	public static String formatDecimal(BigDecimal number) {
		DecimalFormat df = new DecimalFormat("#,###,##0.00");//TODO configure format
		return df.format(number);
		}
	
}
