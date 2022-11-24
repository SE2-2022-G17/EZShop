package it.polito.ezshop.teamTests;

import static org.junit.Assert.assertFalse;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import it.polito.ezshop.model.SaleTransactionImpl;
import it.polito.ezshop.model.TicketEntryImpl;
import it.polito.ezshop.data.TicketEntry;
import it.polito.ezshop.model.ProductTypeImpl;

import java.util.List;
import java.util.ArrayList;
import java.util.NoSuchElementException;
public class TestSaleTransactionImpl {
	
	@Test
	public void testValidSetList() {
		int saleId = 1;
		int entryId = 1;
		SaleTransactionImpl sale = new SaleTransactionImpl(saleId);
		
		ArrayList<TicketEntry> set_list = new ArrayList<>();
		set_list.add(new TicketEntryImpl("barcode1", "description1", 1, 1.5, 0, entryId, saleId));
		set_list.add(new TicketEntryImpl("barcode2", "description2", 1, 1.5, 0, entryId+1, saleId));
		set_list.add(new TicketEntryImpl("barcode3", "description3", 1, 1.5, 0, entryId+2, saleId));
		
		sale.setEntries(set_list);
		
		List<TicketEntry> get_list = sale.getEntries();
		
		for(TicketEntry e : get_list) {
			assertTrue(set_list.contains(e));
		}
		
		ArrayList<TicketEntry> set_list2 = new ArrayList<>();
		set_list2.add(new TicketEntryImpl("barcode4", "description4", 1, 1.5, 0, entryId, saleId));
		set_list2.add(new TicketEntryImpl("barcode5", "description5", 1, 1.5, 0, entryId+1, saleId));
		set_list2.add(new TicketEntryImpl("barcode6", "description6", 1, 1.5, 0, entryId+2, saleId));
		
		sale.setEntries(set_list2);
		
		get_list = sale.getEntries();
		for(TicketEntry e : get_list) {
			assertTrue(set_list2.contains(e));
		}
		
	}
	
	@Test 
	public void testSomeNullSetList() {
		int saleId = 1;
		int entryId = 1;
		SaleTransactionImpl sale = new SaleTransactionImpl(saleId);
		
		ArrayList<TicketEntry> set_list = new ArrayList<>();
		set_list.add(new TicketEntryImpl("barcode1", "description1", 1, 1.5, 0, entryId, saleId));
		set_list.add(null);
		set_list.add(new TicketEntryImpl("barcode3", "description3", 1, 1.5, 0, entryId+2, saleId));
		
		assertThrows(NullPointerException.class, ()->sale.setEntries(set_list));
	}
	
	@Test
	public void testNullSetList() {
		int saleId = 1;
		
		SaleTransactionImpl sale = new SaleTransactionImpl(saleId);
		
		assertThrows(NullPointerException.class, ()->sale.setEntries(null));
	}
	
	@Test
	public void testPerformedCompressEntries() {
		int saleId = 1;
		int entryId = 1;
		SaleTransactionImpl sale = new SaleTransactionImpl(saleId);
		
		ArrayList<TicketEntry> set_list = new ArrayList<>();
		set_list.add(new TicketEntryImpl("barcode1", "description1", 1, 1.5, 0, entryId, saleId));
		set_list.add(new TicketEntryImpl("barcode1", "description1", 2, 1.5, 0, entryId+1, saleId));
		set_list.add(new TicketEntryImpl("barcode3", "description3", 1, 1.5, 0, entryId+2, saleId));
		set_list.add(new TicketEntryImpl("barcode4", "description3", 1, 1.5, 0, entryId+3, saleId));
		set_list.add(new TicketEntryImpl("barcode4", "description3", 1, 1.5, 0, entryId+2, saleId));
		set_list.add(new TicketEntryImpl("barcode5", "description3", 1, 1.5, 0, entryId+2, saleId));
		
		
		sale.setEntries(set_list);
		assertEquals(sale.compressEntries(), 2);
		assertEquals(sale.getEntry("barcode1").getAmount(), 3);
		
	}
	
	@Test
	public void testNotPerformedCompressEntries() {
		int saleId = 1;
		int entryId = 1;
		SaleTransactionImpl sale = new SaleTransactionImpl(saleId);
		
		ArrayList<TicketEntry> set_list = new ArrayList<>();
		set_list.add(new TicketEntryImpl("barcode1", "description1", 1, 1.5, 0, entryId, saleId));
		set_list.add(new TicketEntryImpl("barcode2", "description2", 2, 1.5, 0, entryId+1, saleId));
		set_list.add(new TicketEntryImpl("barcode3", "description3", 1, 1.5, 0, entryId+2, saleId));
		
		sale.setEntries(set_list);
		
		assertEquals(sale.compressEntries(), 0);
		
		
	}
	
	@Test
	public void testTrueCheckState() {
		int saleId = 1;
		SaleTransactionImpl sale = new SaleTransactionImpl(saleId);
		
		sale.setState(1);
	
		
		assertTrue(sale.checkState(1,2,3));
		
		
	}
	@Test
	public void testFalseCheckState() {
		int saleId = 1;
		SaleTransactionImpl sale = new SaleTransactionImpl(saleId);
		
		sale.setState(-1);
	
		
		assertFalse(sale.checkState(1,2,3));
		
		
	}
	@Test
	public void testTrueContainsEntry() {
		int saleId = 1;
		int entryId = 1;
		SaleTransactionImpl sale = new SaleTransactionImpl(saleId);
		
		ArrayList<TicketEntry> set_list = new ArrayList<>();
		set_list.add(new TicketEntryImpl("barcode1", "description1", 1, 1.5, 0, entryId, saleId));
		set_list.add(new TicketEntryImpl("barcode2", "description2", 2, 1.5, 0, entryId+1, saleId));
		set_list.add(new TicketEntryImpl("barcode3", "description3", 1, 1.5, 0, entryId+2, saleId));
		
		sale.setEntries(set_list);
		
		assertTrue(sale.containsEntry("barcode1"));
		
		
	}
	
	@Test
	public void testFalseContainsEntry() {
		int saleId = 1;
		int entryId = 1;
		SaleTransactionImpl sale = new SaleTransactionImpl(saleId);
		
		ArrayList<TicketEntry> set_list = new ArrayList<>();
		set_list.add(new TicketEntryImpl("barcode1", "description1", 1, 1.5, 0, entryId, saleId));
		set_list.add(new TicketEntryImpl("barcode2", "description2", 2, 1.5, 0, entryId+1, saleId));
		set_list.add(new TicketEntryImpl("barcode3", "description3", 1, 1.5, 0, entryId+2, saleId));
		
		sale.setEntries(set_list);
		
		assertFalse(sale.containsEntry("barcode6"));
		
		
	}
	@Test
	public void testAddEntry() {
		int saleId = 1;
		int prodId = 1;
		SaleTransactionImpl sale = new SaleTransactionImpl(saleId);
		
		ProductTypeImpl p1 = new ProductTypeImpl(prodId, "description1", "barcode1", 5.5, "note");
		ProductTypeImpl p2 = new ProductTypeImpl(prodId+1, "description2", "barcode2", 5.5, "note");
		ProductTypeImpl p3 = new ProductTypeImpl(prodId+2, "description3", "barcode3", 5.5, "note");
		
		
		assertEquals("barcode1", sale.addEntry(p1, 5).getBarCode());
		assertEquals(7, sale.addEntry(p2, 7).getAmount());
		assertNull(sale.addEntry(p1, 10));
		assertThrows(NullPointerException.class, ()-> sale.addEntry(null, 10));
		assertNull(sale.addEntry(p3, -4) );
		
		
	}
	
	@Test
	public void testUpdateEntry() {
		int saleId = 1;
		int prodId = 1;
		SaleTransactionImpl sale = new SaleTransactionImpl(saleId);
		
		ProductTypeImpl p1 = new ProductTypeImpl(prodId, "description1", "barcode1", 5.5, "note");
		ProductTypeImpl p2 = new ProductTypeImpl(prodId+1, "description2", "barcode2", 5.5, "note");
		ProductTypeImpl p3 = new ProductTypeImpl(prodId+2, "description3", "barcode3", 5.5, "note");
		
		
		sale.addEntry(p1, 5);
		sale.addEntry(p2, 5);
		sale.addEntry(p3, 5);
		
		assertThrows(NoSuchElementException.class, ()-> sale.updateEntry("barcodex", 5));
		assertNull(sale.updateEntry("barcode1", -10));
		
		TicketEntryImpl updated= sale.updateEntry("barcode2", -2);
		
		assertEquals(3, updated.getAmount());
		assertEquals("barcode2", updated.getBarCode());
		
	}
	@Test
	public void testUpdateEntryDouble() {
		int saleId = 1;
		int prodId = 1;
		SaleTransactionImpl sale = new SaleTransactionImpl(saleId);
		
		ProductTypeImpl p1 = new ProductTypeImpl(prodId, "description1", "barcode1", 5.5, "note");
		ProductTypeImpl p2 = new ProductTypeImpl(prodId+1, "description2", "barcode2", 5.5, "note");
		ProductTypeImpl p3 = new ProductTypeImpl(prodId+2, "description3", "barcode3", 5.5, "note");
		
		
		sale.addEntry(p1, 5);
		sale.addEntry(p2, 5);
		sale.addEntry(p3, 5);
		
		assertThrows(NoSuchElementException.class, ()-> sale.updateEntry("barcodex", 5));
		assertNull(sale.updateEntry("barcode1", 10.1));
		
		TicketEntryImpl updated= sale.updateEntry("barcode2", 0.5);
		
		assertEquals(0.5, updated.getDiscountRate());
		assertEquals("barcode2", updated.getBarCode());
		
	}
	
	@Test
	public void testGetEntry() {
		int saleId = 1;
		int entryId = 1;
		SaleTransactionImpl sale = new SaleTransactionImpl(saleId);
		
		ArrayList<TicketEntry> set_list = new ArrayList<>();
		set_list.add(new TicketEntryImpl("barcode1", "description1", 1, 1.5, 0, entryId, saleId));
		set_list.add(new TicketEntryImpl("barcode2", "description2", 2, 1.5, 0, entryId+1, saleId));
		set_list.add(new TicketEntryImpl("barcode3", "description3", 1, 1.5, 0, entryId+2, saleId));
		
		sale.setEntries(set_list);
		
		TicketEntryImpl got_entry = sale.getEntry("barcode1");
		TicketEntryImpl not_got_entry = sale.getEntry("barcodex");
		
		
		assertEquals(got_entry.getBarCode(), "barcode1");
		assertNull(not_got_entry);
		
	}
	@Test
	public void testComputePoints() {
		int saleId = 1;
		int entryId = 1;
		SaleTransactionImpl sale = new SaleTransactionImpl(saleId);
		
		ArrayList<TicketEntry> set_list = new ArrayList<>();
		set_list.add(new TicketEntryImpl("barcode1", "description1", 1, 100.0, 0, entryId, saleId));
		set_list.add(new TicketEntryImpl("barcode2", "description2", 2, 100.0, 0, entryId+1, saleId));
		set_list.add(new TicketEntryImpl("barcode3", "description3", 1, 200.0, 0, entryId+2, saleId));
		set_list.add(new TicketEntryImpl("barcode4", "description4", 1, 500.0, 0, entryId+3, saleId));
		
		sale.setEntries(set_list);
		sale.setDiscountRate(0.5);
		
		assertEquals(sale.getPrice(), 500.0);
		assertEquals(50, sale.computePoints());
		
		
		
		sale.updateEntry("barcode1", 0.01);
		sale.updateEntry("barcode2", 0.01);
		
		assertEquals(49, sale.computePoints());
		
		
		
		
	}
	
	@Test
	public void testInvalidApplyReturn() {
		int saleId = 1;
		int entryId = 1;
		SaleTransactionImpl sale = new SaleTransactionImpl(saleId);
		
		ArrayList<TicketEntry> set_list = new ArrayList<>();
		set_list.add(new TicketEntryImpl("barcode1", "description1", 5, 100.0, 0, entryId, saleId));
		set_list.add(new TicketEntryImpl("barcode2", "description2", 5, 100.0, 0, entryId+1, saleId));
		set_list.add(new TicketEntryImpl("barcode3", "description3", 5, 200.0, 0, entryId+2, saleId));
		set_list.add(new TicketEntryImpl("barcode4", "description4", 5, 500.0, 0, entryId+3, saleId));
		
		sale.setEntries(set_list);
		
		ArrayList<TicketEntry> return_list1 = new ArrayList<>();
		return_list1.add(new TicketEntryImpl("barcode1", "description1", 1, 100.0, 0, entryId, saleId));
		return_list1.add(new TicketEntryImpl("barcodex", "description1", 1, 100.0, 0, entryId, saleId));
		
		assertThrows(NoSuchElementException.class, () -> sale.applyReturn(return_list1));
		
		ArrayList<TicketEntry> return_list2 = new ArrayList<>();
		return_list2.add(new TicketEntryImpl("barcode1", "description1", 1, 100.0, 0, entryId, saleId));
		return_list2.add(new TicketEntryImpl("barcode2", "description1", 10, 100.0, 0, entryId, saleId));
		
		assertFalse(sale.applyReturn(return_list2));
		
		ArrayList<TicketEntry> return_list3 = new ArrayList<>();
		return_list3.add(new TicketEntryImpl("barcode3", "description1", 1, 100.0, 0, entryId, saleId));
		return_list3.add(null);
		
		assertThrows(NullPointerException.class, ()-> sale.applyReturn(return_list3));
		
		ArrayList<TicketEntry> return_list4 = new ArrayList<>();
		return_list4.add(new TicketEntryImpl("barcode3", "description1", -5, 100.0, 0, entryId, saleId));
		
		
		assertFalse( sale.applyReturn(return_list4));
		
		assertThrows( NullPointerException.class, () -> sale.applyReturn(null));
		
	}
	@Test
	public void testValidApplyReturn() {
		int saleId = 1;
		int entryId = 1;
		SaleTransactionImpl sale = new SaleTransactionImpl(saleId);
		
		ArrayList<TicketEntry> set_list = new ArrayList<>();
		set_list.add(new TicketEntryImpl("barcode1", "description1", 5, 100.0, 0, entryId, saleId));
		set_list.add(new TicketEntryImpl("barcode2", "description2", 5, 100.0, 0, entryId+1, saleId));
		set_list.add(new TicketEntryImpl("barcode3", "description3", 5, 200.0, 0, entryId+2, saleId));
		set_list.add(new TicketEntryImpl("barcode4", "description4", 5, 500.0, 0, entryId+3, saleId));
		
		sale.setEntries(set_list);
		double price = sale.getPrice();
	
		
		ArrayList<TicketEntry> return_list1 = new ArrayList<>();
		return_list1.add(new TicketEntryImpl("barcode1", "description1", 1, 100.0, 0, entryId, saleId));
		return_list1.add(new TicketEntryImpl("barcode2", "description1", 1, 100.0, 0, entryId, saleId));
		return_list1.add(new TicketEntryImpl("barcode3", "description1", 5, 200.0, 0, entryId, saleId));
		
		
		assertTrue(sale.applyReturn(return_list1));
		
		assertEquals(price - 1200, sale.getPrice());
		
		assertEquals(sale.getEntries().size(), 3);
	}
	
	@Test
	public void testInvalidUndoReturn() {
		int saleId = 1;
		int entryId = 1;
		SaleTransactionImpl sale = new SaleTransactionImpl(saleId);
		
		ArrayList<TicketEntry> set_list = new ArrayList<>();
		set_list.add(new TicketEntryImpl("barcode1", "description1", 5, 100.0, 0, entryId, saleId));
		set_list.add(new TicketEntryImpl("barcode2", "description2", 5, 100.0, 0, entryId+1, saleId));
		set_list.add(new TicketEntryImpl("barcode3", "description3", 5, 200.0, 0, entryId+2, saleId));
		set_list.add(new TicketEntryImpl("barcode4", "description4", 5, 500.0, 0, entryId+3, saleId));
		
		sale.setEntries(set_list);
		
		assertThrows(NullPointerException.class, ()-> sale.undoReturn(null));
		
		
		ArrayList<TicketEntry> return_list1 = new ArrayList<>();
		return_list1.add(new TicketEntryImpl("barcode1", "description1", 1, 100.0, 0, entryId, saleId));
		return_list1.add(null);
		return_list1.add(new TicketEntryImpl("barcode3", "description1", 5, 200.0, 0, entryId, saleId));
		
		assertThrows(NullPointerException.class, ()-> sale.undoReturn(return_list1));
		
		ArrayList<TicketEntry> return_list2 = new ArrayList<>();
		return_list2.add(new TicketEntryImpl("barcode1", "description1", 1, 100.0, 0, entryId, saleId));
		return_list2.add(new TicketEntryImpl("barcode3", "description1", -5, 200.0, 0, entryId, saleId));
		
		assertFalse(sale.undoReturn(return_list2));
		
		
	}
	@Test
	public void testValidUndoReturn() {
		int saleId = 1;
		int entryId = 1;
		SaleTransactionImpl sale = new SaleTransactionImpl(saleId);
		
		ArrayList<TicketEntry> set_list = new ArrayList<>();
		set_list.add(new TicketEntryImpl("barcode1", "description1", 5, 100.0, 0, entryId, saleId));
		set_list.add(new TicketEntryImpl("barcode2", "description2", 5, 100.0, 0, entryId+1, saleId));
		set_list.add(new TicketEntryImpl("barcode3", "description3", 5, 200.0, 0, entryId+2, saleId));
		set_list.add(new TicketEntryImpl("barcode4", "description4", 5, 500.0, 0, entryId+3, saleId));
		
		sale.setEntries(set_list);
		double price = sale.getPrice();
	
		
		ArrayList<TicketEntry> return_list1 = new ArrayList<>();
		return_list1.add(new TicketEntryImpl("barcode1", "description1", 1, 100.0, 0, entryId, saleId));
		return_list1.add(new TicketEntryImpl("barcode2", "description1", 1, 100.0, 0, entryId, saleId));
		return_list1.add(new TicketEntryImpl("barcode6", "description1", 5, 100.0, 0, entryId, saleId));
		
		
		assertTrue(sale.undoReturn(return_list1));
		
		assertEquals(price + 700, sale.getPrice());
		
		assertEquals(sale.getEntries().size(), 5);
	}
	
}
