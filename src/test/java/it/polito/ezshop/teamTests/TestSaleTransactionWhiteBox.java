package it.polito.ezshop.teamTests;


import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import it.polito.ezshop.data.TicketEntry;
import it.polito.ezshop.model.SaleTransactionImpl;
import it.polito.ezshop.model.TicketEntryImpl;

public class TestSaleTransactionWhiteBox {

	@Test
	public void testSaleTransactionImplSetGet() {
		Integer saleId = 1;
		Integer entryId = 1;
		
		SaleTransactionImpl sale = new SaleTransactionImpl(saleId);
		
		assertEquals(saleId, sale.getId());
		
		sale.setEntryCounter(5);
		assertEquals(5, sale.getEntryCounter());
		
		sale.setState(1);
		assertEquals(1, sale.getState());
		
		sale.setBalanceOperationId(15);
		assertEquals(15, sale.getBalanceOperationId());
		
		sale.setTicketNumber(200);
		assertEquals(200, sale.getTicketNumber());
		
		sale.loadEntry(new TicketEntryImpl("barcode1", "description1", 1, 1.5, 0, entryId, saleId));
		sale.loadEntry(new TicketEntryImpl("barcode2", "description2", 1, 1.5, 0, entryId+1, saleId));
		
		assertEquals(entryId, sale.getEntryId("barcode1"));
		assertEquals(entryId+1, sale.getEntryId("barcode2"));
		
		sale.setDiscountRate(0.5);
		
		assertEquals(0.5, sale.getDiscountRate());
		
	}
	@Test
	public void testSaleTransactionImplCSV() {
		Integer saleId = 1;
		
		SaleTransactionImpl sale = new SaleTransactionImpl(saleId);
		sale.setEntryCounter(5);
		sale.setState(1);
		sale.setBalanceOperationId(15);
		sale.setTicketNumber(200);
		sale.setDiscountRate(0.5);
		
		assertEquals("1,200,5,0.5,15,1", sale.getCSV());
	}
	
	@Test
	public void testApplyReturnLoop() {
		int saleId = 1;
		int entryId = 1;
		SaleTransactionImpl sale = new SaleTransactionImpl(saleId);
		
		
		
		ArrayList<TicketEntry> set_list = new ArrayList<>();
		set_list.add(new TicketEntryImpl("barcode1", "description1", 5, 100.0, 0, entryId, saleId));
		set_list.add(new TicketEntryImpl("barcode2", "description2", 5, 100.0, 0, entryId+1, saleId));
		set_list.add(new TicketEntryImpl("barcode3", "description3", 5, 200.0, 0, entryId+2, saleId));
		set_list.add(new TicketEntryImpl("barcode4", "description4", 5, 500.0, 0, entryId+3, saleId));
		set_list.add(new TicketEntryImpl("barcode5", "description5", 5, 500.0, 0, entryId+3, saleId));
		
		sale.setEntries(set_list);
		
		//0 iterations
		ArrayList<TicketEntry> return_list1 = new ArrayList<>();
		assertTrue(sale.applyReturn(return_list1));
		
		//1 iteration
		ArrayList<TicketEntry> return_list2 = new ArrayList<>();
		return_list2.add(new TicketEntryImpl("barcode1", "description1", 1, 100.0, 0, entryId, saleId));
		
		double price = sale.getPrice();
		assertTrue(sale.applyReturn(return_list2));
		assertEquals(price - 100, sale.getPrice());
		assertEquals(4, sale.getEntry("barcode1").getAmount());
		
		//3 iterations
		ArrayList<TicketEntry> return_list3 = new ArrayList<>();
		return_list3.add(new TicketEntryImpl("barcode1", "description1", 1, 100.0, 0, entryId, saleId));
		return_list3.add(new TicketEntryImpl("barcode2", "description1", 0, 100.0, 0, entryId, saleId));
		return_list3.add(new TicketEntryImpl("barcode3", "description1", 5, 200.0, 0, entryId, saleId));
		
		price = sale.getPrice();
		assertTrue(sale.applyReturn(return_list3));
		assertEquals(price - 1100, sale.getPrice());
		assertEquals(4, sale.getEntries().size());
		
		//invalid cases
		ArrayList<TicketEntry> return_list4 = new ArrayList<>();
		return_list4.add(new TicketEntryImpl("barcode1", "description1", -1, 100.0, 0, entryId, saleId));
		
		assertFalse(sale.applyReturn(return_list4));
		
		ArrayList<TicketEntry> return_list5 = new ArrayList<>();
		return_list5.add(new TicketEntryImpl("barcode1", "description1", 1000, 100.0, 0, entryId, saleId));
		
		assertFalse(sale.applyReturn(return_list5));
	}
}
