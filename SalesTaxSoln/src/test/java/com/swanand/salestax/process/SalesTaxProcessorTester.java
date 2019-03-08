package com.swanand.salestax.process;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.swanand.salestax.dto.IItem;
import com.swanand.salestax.dto.IItemBillDetails;
import com.swanand.salestax.dto.IOrder;
import com.swanand.salestax.dto.Item;
import com.swanand.salestax.dto.ItemBillDetails;
import com.swanand.salestax.dto.Order;

/**
 * Generated code for the test suite <b>SalesTaxProcessorTester</b> located at
 * <i>/SalesTaxSoln/src/test/java/SalesTaxProcessorTester.testsuite</i>.
 *
 * Testclass to test alesTaxProcessor class
 */
public class SalesTaxProcessorTester /*extends TestCase*/ {
	
	IOrder order =  new Order();


	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
	public void setUp() throws Exception {
		order.setOrderId("test3");
		IItem item3_1 = new Item();
		item3_1.setName("bottle of perfume");
		item3_1.setPrice(new BigDecimal(27.99));
		item3_1.setImported(true);
		
		IItem item3_2 = new Item();
		item3_2.setName("bottle of perfume");
		item3_2.setPrice(new BigDecimal(18.99));
		
		IItem item3_3 = new Item();
		item3_3.setName("packet of headache pills");
		item3_3.setPrice(new BigDecimal(9.75));
		item3_3.setExemptedFromSalesTax(true);
		
		IItem item3_4 = new Item();
		item3_4.setName("box of chocolates");
		item3_4.setPrice(new BigDecimal(11.25));
		item3_4.setImported(true);
		item3_4.setExemptedFromSalesTax(true);
		
		IItemBillDetails ibd3_1= new ItemBillDetails();
		ibd3_1.setItem(item3_1);
		ibd3_1.setQuantity(1);
		
		IItemBillDetails ibd3_2= new ItemBillDetails();
		ibd3_2.setItem(item3_2);
		ibd3_2.setQuantity(1);
		
		IItemBillDetails ibd3_3= new ItemBillDetails();
		ibd3_3.setItem(item3_3);
		ibd3_3.setQuantity(1);
		
		IItemBillDetails ibd3_4= new ItemBillDetails();
		ibd3_4.setItem(item3_4);
		ibd3_4.setQuantity(1);
		
		order.getItemBillDetailsList().add(ibd3_1);
		order.getItemBillDetailsList().add(ibd3_2);
		order.getItemBillDetailsList().add(ibd3_3);
		order.getItemBillDetailsList().add(ibd3_4);
	}

	@Test
	public void testSalesTaxProcessor(){
		new SalesTaxProcessor().processOrder(order);
		assertEquals( new BigDecimal(6.70).setScale(2, BigDecimal.ROUND_HALF_DOWN),order.getTotalSalesTax().setScale(2, BigDecimal.ROUND_HALF_DOWN));
		assertEquals( new BigDecimal(74.68).setScale(2, BigDecimal.ROUND_HALF_DOWN),order.getTotal().setScale(2, BigDecimal.ROUND_HALF_DOWN));
	}
	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	@After
	public void tearDown() throws Exception {
		order= null;
	}

}
