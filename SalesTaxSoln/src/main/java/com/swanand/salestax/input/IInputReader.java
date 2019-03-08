package com.swanand.salestax.input;

import com.swanand.salestax.dto.IOrder;
import com.swanand.salestax.exception.SalesTaxException;

public interface IInputReader {

	IOrder processInput(Object object) throws SalesTaxException;

}
