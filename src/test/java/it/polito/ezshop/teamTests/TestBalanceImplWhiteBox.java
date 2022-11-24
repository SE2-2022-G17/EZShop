package it.polito.ezshop.teamTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import it.polito.ezshop.model.BalanceImpl;
import it.polito.ezshop.model.BalanceOperationImpl;

public class TestBalanceImplWhiteBox {

	@Test
	public void testBalanceImplSetGet() {
		BalanceImpl balance = new BalanceImpl();
		
		balance.setAmount(100.0);
		assertEquals(100.0, balance.getAmount());
	}
	
	@Test
	public void testBalanceImplUpdate() {
		BalanceImpl balance = new BalanceImpl();
		balance.updateAmount(50.0);
		assertEquals(50.0, balance.getAmount());
	}
	
	@Test
	public void testGetLastId() {
		BalanceImpl balance = new BalanceImpl();
		assertEquals(0, balance.getLastId());
		balance.recordBalanceOperation(new BalanceOperationImpl(2, LocalDate.EPOCH, 50.0, "Credit"));
		assertEquals(2, balance.getLastId());
	}
}
