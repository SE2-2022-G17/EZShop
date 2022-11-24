package it.polito.ezshop.teamTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import it.polito.ezshop.model.PositionImpl;
import it.polito.ezshop.model.ProductTypeImpl;

public class TestPositionImplWhiteBox {
	@Test
	public void testPositionImplSetGet() {
		PositionImpl position = new PositionImpl("1-a-1");
		
		position.setPosition("2-b-2");
		assertEquals("2-b-2", position.getPosition());
	}
	
	@Test
	public void testPositionImplCSV() {
		PositionImpl position = new PositionImpl("1-a-1");		
		assertEquals("1-a-1", position.getCSV());
	}
}
