package it.polito.ezshop.teamTests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import it.polito.ezshop.model.ProductImpl;

import org.junit.jupiter.api.Test;

public class TestProductImplWhiteBox {
	
	@Test
	public void testConstructors() {
		ProductImpl p1 = new ProductImpl("0000000001", 5);
		ProductImpl p2 = new ProductImpl("0000000002", 6, 2,2);
		
		assertEquals("0000000001", p1.getRFID());
		assertEquals(5, p1.getProductTypeId());
		assertEquals(-1, p1.getTransactionId());
		assertEquals(-1, p1.getReturnId());
		
		assertEquals("0000000002", p2.getRFID());
		assertEquals(6, p2.getProductTypeId());		
		assertEquals(2, p2.getTransactionId());
		assertEquals(2, p2.getReturnId());		
	}
	
	@Test
	public void testSettersGetters() {
		ProductImpl p1 = new ProductImpl();
		
		p1.setRFID("0000000001");
		p1.setProductTypeId(6);
		p1.setTransactionId(2);
		p1.setReturnId(2);
		
		assertEquals("0000000001", p1.getRFID());
		assertEquals(6, p1.getProductTypeId());
		assertEquals(2, p1.getTransactionId());
		assertEquals(2, p1.getReturnId());
	}
	
	
	@Test
	public void testGetCSV() {
		ProductImpl p1 = new ProductImpl("0000000001", 4);
		ProductImpl p2 = new ProductImpl("0000000002", 5, 2, 2);
		
		assertEquals("0000000001,4,-1,-1", p1.getCSV());
		assertEquals("0000000002,5,2,2", p2.getCSV());
	}
	
}
