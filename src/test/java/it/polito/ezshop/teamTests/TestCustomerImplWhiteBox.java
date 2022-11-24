package it.polito.ezshop.teamTests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import it.polito.ezshop.model.CustomerImpl;

class TestCustomerImplWhiteBox {
	
	@Test
	public void testCustomerGetSet() {
		CustomerImpl c = new CustomerImpl(163830, "Mr. Sandman");
		c.setId(987282);
		assertEquals(987282, c.getId());
		c.setCustomerName("Mr. Bean");
		assertEquals("Mr. Bean", c.getCustomerName());
		c.setCustomerCard("0000610305");
		assertEquals("0000610305", c.getCustomerCard());
		c.setPoints(500);
		assertEquals(500, c.getPoints());
	}


	@Test
	public void testCustomerCSV() {
		String initialCSV = "163830,Mr. Sandman,0000610305,5500";
		CustomerImpl c = new CustomerImpl(initialCSV);
		assertEquals(initialCSV, c.getCSV());
	}

}
