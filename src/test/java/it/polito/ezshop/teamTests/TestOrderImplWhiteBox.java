package it.polito.ezshop.teamTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import it.polito.ezshop.model.OrderImpl;

public class TestOrderImplWhiteBox {
	@Test
	public void testOrderImplSetGet() {
		OrderImpl order = new OrderImpl(1, 1, "8057014050035", 150.0, 10, "ISSUED/ORDERED");
		
		order.setOrderId(2);
		assertEquals(2, order.getOrderId());
		order.setBalanceId(2);
		assertEquals(2, order.getBalanceId());
		order.setProductCode("9788838663260");
		assertEquals("9788838663260", order.getProductCode());
		order.setPricePerUnit(500.0);
		assertEquals(500.0, order.getPricePerUnit());
		order.setQuantity(3);
		assertEquals(3, order.getQuantity());
		order.setStatus("PAYED");
		assertEquals("PAYED", order.getStatus());
		
	}
	
	@Test
	public void testOrderImplCSV() {
		OrderImpl order = new OrderImpl(1, 1, "8057014050035", 150.0, 10, "ISSUED/ORDERED");
		assertEquals("1,1,8057014050035,150.0,10,ISSUED/ORDERED", order.getCSV());
	}

}
