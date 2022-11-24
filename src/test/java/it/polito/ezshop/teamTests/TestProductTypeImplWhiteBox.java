package it.polito.ezshop.teamTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import it.polito.ezshop.model.ProductTypeImpl;

public class TestProductTypeImplWhiteBox {
	@Test
	public void testProductTypeImplSetGet() {
		ProductTypeImpl product = new ProductTypeImpl(1, "YEEZY 500", "8057014050035", 200.0, "DESERT RAT BLUSH");
		
		product.setId(2);
		assertEquals(2, product.getId());
		product.setProductDescription("Air Jordan 1");
		assertEquals("Air Jordan 1", product.getProductDescription());
		product.setBarCode("9788838663260");
		assertEquals("9788838663260", product.getBarCode());
		product.setPricePerUnit(500.0);
		assertEquals(500.0, product.getPricePerUnit());
		product.setNote("");
		assertEquals("", product.getNote());
		product.setLocation("here");
		assertEquals("here", product.getLocation());
		product.setQuantity(3);
		assertEquals(3, product.getQuantity());
		
		product.updateQuantity(2);
		assertEquals(1, product.getQuantity());
	}
	
	@Test
	public void testProductTypeImplCSV() {
		ProductTypeImpl product = new ProductTypeImpl(1, "YEEZY 500", "8057014050035", 200.0, "DESERT RAT BLUSH");
		assertEquals("1,YEEZY 500,8057014050035,200.0,DESERT RAT BLUSH,0,", product.getCSV());
	}

}
