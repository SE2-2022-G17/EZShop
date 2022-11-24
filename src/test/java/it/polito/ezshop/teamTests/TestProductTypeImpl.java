package it.polito.ezshop.teamTests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import it.polito.ezshop.model.ProductTypeImpl;

public class TestProductTypeImpl {
	
	@Test
	public void testValidateProductCodeNull() {
		assertFalse(ProductTypeImpl.validateProductCode(null));
	}
	
	@Test
	public void testValidateProductCodeLength() {
		assertFalse(ProductTypeImpl.validateProductCode("80040248"));
		assertFalse(ProductTypeImpl.validateProductCode("80570140500"));
		assertFalse(ProductTypeImpl.validateProductCode("805701405003567"));
	}
	
	@Test
	public void testValidateProductCodeWrong() {
		assertFalse(ProductTypeImpl.validateProductCode("1234567891011"));
	}
	
	@Test
	public void testValidateProductCodeSuccessful() {
		assertTrue(ProductTypeImpl.validateProductCode("8057014050035"));
		assertTrue(ProductTypeImpl.validateProductCode("9788838663260"));
	}
	

}
