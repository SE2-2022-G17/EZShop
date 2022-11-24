package it.polito.ezshop.teamTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import it.polito.ezshop.model.TicketEntryImpl;

public class TestTicketEntryImplWhiteBox {
	@Test
	public void testTicketEntryImplSetGet() {
		TicketEntryImpl e = new TicketEntryImpl("barcode1", "description1", 2, 2.4, 0.5, 1, 1);
		
		assertEquals(1, e.getSaleId());
		
		e.setBarCode("barcode2");
		assertEquals("barcode2", e.getBarCode());
		
		
		e.setProductDescription("description2");
		assertEquals("description2", e.getProductDescription());
		
		e.setPricePerUnit(1.4);
		assertEquals(1.4, e.getPricePerUnit());
	}
	@Test
	public void testTicketEntryImplCSV() {
		TicketEntryImpl e = new TicketEntryImpl("barcode1", "description1", 2, 2.4, 0.5, 1, 2);
		
		assertEquals("2-1,barcode1,description1,2,2.4,0.5", e.getCSV());
		
	}
}
