package com.swanand.salestax.input.file;

import static org.junit.Assert.assertEquals;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.swanand.salestax.dto.IItemBillDetails;
import com.swanand.salestax.dto.IOrder;
import com.swanand.salestax.input.file.FileInputReader;

public class InputTester {
	
	File file;


	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
	public void setUp() throws Exception {
		file = new File("inputFile");
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		bw.write("1 imported bottle of perfume at 27.99\n"+
				"1 bottle of perfume at 18.99\n"+
				"1 packet of headache pills at 9.75\n"+
				"1 box of imported chocolates at 11.25");
		
		bw.flush();
		bw.close();
		
	}

	@Test
	public void testInputFileProcessor(){
		IOrder order = new FileInputReader().processInput(file);
		assertEquals("inputFile", order.getOrderId());
		
		IItemBillDetails details= order.getItemBillDetailsList().get(0);
		assertEquals("bottle of perfume", details.getItem().getName().trim());
		assertEquals(new BigDecimal(27.99).setScale(2, BigDecimal.ROUND_HALF_DOWN), details.getItem().getPrice().setScale(2, BigDecimal.ROUND_HALF_DOWN));
		assertEquals(1, details.getQuantity());
		
		IItemBillDetails details1= order.getItemBillDetailsList().get(1);
		assertEquals("bottle of perfume", details1.getItem().getName().trim());
		assertEquals(new BigDecimal(18.99).setScale(2, BigDecimal.ROUND_HALF_DOWN), details1.getItem().getPrice().setScale(2, BigDecimal.ROUND_HALF_DOWN));
		assertEquals(1, details1.getQuantity());
		
		IItemBillDetails details11= order.getItemBillDetailsList().get(2);
		assertEquals("packet of headache pills", details11.getItem().getName().trim());
		assertEquals(new BigDecimal(9.75).setScale(2, BigDecimal.ROUND_HALF_DOWN), details11.getItem().getPrice().setScale(2, BigDecimal.ROUND_HALF_DOWN));
		assertEquals(1, details11.getQuantity());
		
		IItemBillDetails details111= order.getItemBillDetailsList().get(3);
		assertEquals("box of chocolates", details111.getItem().getName().trim());
		assertEquals(new BigDecimal(11.25).setScale(2, BigDecimal.ROUND_HALF_DOWN), details111.getItem().getPrice().setScale(2, BigDecimal.ROUND_HALF_DOWN));
		assertEquals(1, details111.getQuantity());
	
	}
	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	@After
	public void tearDown() throws Exception {
		file = null;
	}

}
