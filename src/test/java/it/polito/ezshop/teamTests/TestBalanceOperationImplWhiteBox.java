package it.polito.ezshop.teamTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import it.polito.ezshop.model.BalanceOperationImpl;

public class TestBalanceOperationImplWhiteBox {

	@Test
	public void testBalanceOperationSetGet() {
		BalanceOperationImpl balanceOperation = new BalanceOperationImpl(1, LocalDate.now(), 100.0, "Credit");
		
		balanceOperation.setBalanceId(2);
		assertEquals(2, balanceOperation.getBalanceId());
		balanceOperation.setDate(LocalDate.EPOCH);
		assertEquals(LocalDate.EPOCH, balanceOperation.getDate());
		balanceOperation.setMoney(50.0);
		assertEquals(50.0, balanceOperation.getMoney());
		balanceOperation.setType("Debit");
		assertEquals("Debit", balanceOperation.getType());
	}
	
	@Test
	public void testBalanceOperationImplCSV() {
		BalanceOperationImpl balanceOperation = new BalanceOperationImpl(1, LocalDate.EPOCH, 100.0, "Credit");
		assertEquals("1,"+LocalDate.EPOCH+",100.0,Credit", balanceOperation.getCSV());
	}
	
}
