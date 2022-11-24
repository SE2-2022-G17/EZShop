package it.polito.ezshop.teamTests;


import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import it.polito.ezshop.model.ReturnTransactionImpl;
import it.polito.ezshop.model.ReturnEntryImpl;


public class TestReturnTransactionImplWhiteBox {

	@Test
	public void testReturnTransactionImplSetGet() {
		Integer returnId = 1;
		Integer saleId = 1;
		Integer entryId = 1;
		
		ReturnTransactionImpl ret = new ReturnTransactionImpl(returnId, saleId);
		
		assertEquals(saleId, ret.getSaleId());
		
		ret.setCommit(true);
		assertTrue( ret.getCommit());
		
		ret.setEntryCounter(200);
		assertEquals(200, ret.getEntryCounter());
		
		ret.setState(2);
		assertEquals(2, ret.getState());
		assertTrue(ret.checkState(2,1,2));
		assertFalse(ret.checkState(1,10,1000));
		
		ret.setBalanceOperationId(69);
		assertEquals(69, ret.getBalanceOperationId());
		

		ret.loadEntry(new ReturnEntryImpl("barcode1", "description1", 1, 1.5, 0, entryId, saleId, returnId));
		ret.loadEntry(new ReturnEntryImpl("barcode2", "description2", 1, 1.5, 0, entryId+1, saleId, returnId));
		
		assertEquals(1, ret.getEntryId("barcode1"));
		assertEquals(-1,ret.getEntryId("asd"));
		assertEquals("barcode1", ret.getEntry("barcode1").getBarCode());
		
		ret.clearEntries();
		assertEquals(-1, ret.getEntryId("barcode1"));
		
	}
	
	@Test
	public void testSaleTransactionImplCSV() {
		Integer saleId = 1;
		Integer returnId = 1;
		ReturnTransactionImpl ret = new ReturnTransactionImpl(returnId, saleId);
		ret.setEntryCounter(5);
		ret.setState(1);
		ret.setBalanceOperationId(15);
		ret.setDiscountRate(0.5);
		
		assertEquals("1,5,15,1,1,0.5", ret.getCSV());
	}
}
