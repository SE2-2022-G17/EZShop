package it.polito.ezshop.teamTests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import it.polito.ezshop.model.ProductImpl;

public class TestProductImpl {

	@Test
	public void testValidateRFID() {
		assertFalse(ProductImpl.validateRFID(null));
		assertFalse(ProductImpl.validateRFID(""));
		assertFalse(ProductImpl.validateRFID("00001"));
		assertFalse(ProductImpl.validateRFID("000000000000000001"));
		assertFalse(ProductImpl.validateRFID("0$0%0x0?01"));
		assertTrue(ProductImpl.validateRFID("0000000003"));
	}
	
}
