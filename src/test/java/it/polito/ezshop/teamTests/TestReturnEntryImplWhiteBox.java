package it.polito.ezshop.teamTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import it.polito.ezshop.model.ReturnEntryImpl;




public class TestReturnEntryImplWhiteBox {
	@Test
	public void testReturnEntryImplSetGet() {
		ReturnEntryImpl e = new ReturnEntryImpl("barcode1", "description1", 2, 2.4, 0.5, 1, 2, 3);
		
		assertEquals(3, e.getReturnId());
	}
	
	@Test
	public void testTicketEntryImplCSV() {
		ReturnEntryImpl e = new ReturnEntryImpl("barcode1", "description1", 2, 2.4, 0.5, 1, 2, 3);
		
		assertEquals("3-1,barcode1,description1,2,2.4,0.5,2", e.getCSV());
		
	}
	
}
