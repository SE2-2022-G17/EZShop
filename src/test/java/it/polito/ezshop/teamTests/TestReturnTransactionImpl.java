package it.polito.ezshop.teamTests;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

import it.polito.ezshop.data.TicketEntry;
import it.polito.ezshop.model.ProductTypeImpl;
import it.polito.ezshop.model.ReturnTransactionImpl;
import it.polito.ezshop.model.ReturnEntryImpl;

public class TestReturnTransactionImpl {
	
	@Test
	public void testValidSetList() {
		int saleId = 1;
		int returnId = 1;
		int entryId = 1;
		ReturnTransactionImpl ret = new ReturnTransactionImpl(returnId, saleId);
		
		ArrayList<ReturnEntryImpl> set_list = new ArrayList<>();
		set_list.add(new ReturnEntryImpl("barcode1", "description1", 1, 1.5, 0, entryId, saleId, returnId));
		set_list.add(new ReturnEntryImpl("barcode2", "description2", 1, 1.5, 0, entryId+1, saleId, returnId));
		set_list.add(new ReturnEntryImpl("barcode3", "description3", 1, 1.5, 0, entryId+2, saleId, returnId));
		
		ret.setEntries(set_list);
		
		List<TicketEntry> get_list = ret.getEntries();
		
		for(TicketEntry e : get_list) {
			assertTrue(set_list.contains(e));
		}
		
		ArrayList<ReturnEntryImpl> set_list2 = new ArrayList<>();
		set_list2.add(new ReturnEntryImpl("barcode4", "description4", 1, 1.5, 0, entryId, saleId, returnId));
		set_list2.add(new ReturnEntryImpl("barcode5", "description5", 1, 1.5, 0, entryId+1, saleId, returnId));
		set_list2.add(new ReturnEntryImpl("barcode6", "description6", 1, 1.5, 0, entryId+2, saleId, returnId));
		
		ret.setEntries(set_list2);
		
		get_list = ret.getEntries();
		for(TicketEntry e : get_list) {
			assertTrue(set_list2.contains(e));
		}
		
	}
	
	@Test 
	public void testSomeNullSetList() {
		int saleId = 1;
		int entryId = 1;
		int returnId = 1;
		ReturnTransactionImpl ret = new ReturnTransactionImpl(returnId, saleId);
		
		ArrayList<ReturnEntryImpl> set_list = new ArrayList<>();
		set_list.add(new ReturnEntryImpl("barcode1", "description1", 1, 1.5, 0, entryId, saleId, returnId));
		set_list.add(null);
		set_list.add(new ReturnEntryImpl("barcode3", "description3", 1, 1.5, 0, entryId+2, saleId, returnId));
		
		assertThrows(IllegalArgumentException.class, ()->ret.setEntries(set_list));
	}
	
	@Test
	public void testNullSetList() {
		int saleId = 1;
		int returnId = 1;
		ReturnTransactionImpl ret = new ReturnTransactionImpl(returnId, saleId);
		
		assertThrows(NullPointerException.class, ()->ret.setEntries(null));
	}
	
	
	
	@Test
	public void testAddEntry() {
		int saleId = 1;
		int returnId = 1;
		int prodId = 1;
		ReturnTransactionImpl ret = new ReturnTransactionImpl(returnId, saleId);
		
		ProductTypeImpl p1 = new ProductTypeImpl(prodId, "description1", "barcode1", 5, "note");
		ProductTypeImpl p2 = new ProductTypeImpl(prodId+1, "description2", "barcode2", 5, "note");
		ProductTypeImpl p3 = new ProductTypeImpl(prodId+2, "description3", "barcode3", 5, "note");
		ProductTypeImpl p4 = new ProductTypeImpl(prodId+3, "description4", "barcode4", 5, "note");
		
		double price = ret.getPrice();
		
		assertEquals("barcode1", ret.addEntry(p1, 5, 0.5).getBarCode());
		assertEquals(price + 12.5, ret.getPrice());
		assertEquals(7, ret.addEntry(p2, 7, 0).getAmount());
		assertEquals(price + 12.5 + 35, ret.getPrice());
		assertNull(ret.addEntry(p1, 10, 0.2));
		assertThrows(NullPointerException.class, ()-> ret.addEntry(null, 10,0.2));
		assertNull(ret.addEntry(p3, -4,0.2) );
		assertNull(ret.addEntry(p4, 10, 1.1));
		
	}
	
	
	@Test
	public void testUpdateEntry() {
		int saleId = 1;
		int prodId = 1;
		int returnId = 1;
		ReturnTransactionImpl ret = new ReturnTransactionImpl(returnId,saleId);
		
		ProductTypeImpl p1 = new ProductTypeImpl(prodId, "description1", "barcode1", 5.5, "note");
		ProductTypeImpl p2 = new ProductTypeImpl(prodId+1, "description2", "barcode2", 5.5, "note");
		ProductTypeImpl p3 = new ProductTypeImpl(prodId+2, "description3", "barcode3", 5.5, "note");
		
		
		
		ret.addEntry(p1, 5, 0.1);
		ret.addEntry(p2, 5, 0);
		ret.addEntry(p3, 5, 0.1);
		
		assertThrows(NoSuchElementException.class, ()-> ret.updateEntry("barcodex", 5));
		assertNull(ret.updateEntry("barcode1", -10));
		
		double price = ret.getPrice();
		
		ReturnEntryImpl updated= ret.updateEntry("barcode2", -2);
		
		assertEquals(3, updated.getAmount());
		assertEquals("barcode2", updated.getBarCode());
		assertEquals(price - 11, ret.getPrice());
	}
}
