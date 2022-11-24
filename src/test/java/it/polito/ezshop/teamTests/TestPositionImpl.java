package it.polito.ezshop.teamTests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import it.polito.ezshop.model.PositionImpl;

public class TestPositionImpl {
	
	@Test
	public void testIsValidNull() {
		assertFalse(PositionImpl.isValid(null));
	}
	
	@Test
	public void testIsValidWrong() {
		assertFalse(PositionImpl.isValid("a2-1"));
	}
	
	@Test
	public void testIsValidSuccessful() {
		assertTrue(PositionImpl.isValid("1-a-1"));
		assertTrue(PositionImpl.isValid("2-b-2"));
	}
}
