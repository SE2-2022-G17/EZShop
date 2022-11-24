package it.polito.ezshop.teamTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import it.polito.ezshop.model.BalanceImpl;
import it.polito.ezshop.model.BalanceOperationImpl;

public class TestBalanceImpl {

	@Test
	public void testRecordBalanceOperationNull() {
		BalanceImpl balance = new BalanceImpl();
		balance.recordBalanceOperation(null);
		assertEquals(0, balance.getBalanceOperations().size());
	}
	
	@Test
	public void testRecordBalanceSuccessful() {
		BalanceImpl balance = new BalanceImpl();
		balance.recordBalanceOperation(new BalanceOperationImpl(1, LocalDate.now(), 30.0, "Credit"));
		assertEquals(1, balance.getBalanceOperations().size());
	}
	
}
