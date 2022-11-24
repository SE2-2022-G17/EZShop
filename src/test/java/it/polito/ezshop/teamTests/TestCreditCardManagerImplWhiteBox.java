package it.polito.ezshop.teamTests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import it.polito.ezshop.model.CreditCardManagerImpl;

class TestCreditCardManagerImplWhiteBox {

	/* Content of credit_card.csv:
	 * 79927398713,200
	 * 79232748719,50
	 * 79228374611,10
	 * 79222836458,100
	 */
	
	String validPord = "79927398713",
		 invalidPord = "79927398718",
     validButMissing = "79912938713";
	
	@Test
	void testLuhn() {
		assertFalse(CreditCardManagerImpl.validateCard(null));
		assertFalse(CreditCardManagerImpl.validateCard(""));
		assertFalse(CreditCardManagerImpl.validateCard("67876782"));
		assertTrue(CreditCardManagerImpl.validateCard(validPord));
		assertFalse(CreditCardManagerImpl.validateCard(invalidPord));
	}
	
	@Test
	void testWithdraw() {
		//withdraw from invalid card
		assertFalse(CreditCardManagerImpl.withdrawFromCard(invalidPord, 100));
	
		//withdraw from a valid but inexistent card
		assertFalse(CreditCardManagerImpl.withdrawFromCard(validButMissing, 100));
		
		//withdraw from an existent card without sufficient funds
		assertFalse(CreditCardManagerImpl.withdrawFromCard(validPord, 400));
		
		//withdraw from an existent card
		assertTrue(CreditCardManagerImpl.withdrawFromCard(validPord, 100));
	}
	
	@Test
	void testRefund() {
		//refund an invalid card
		assertFalse(CreditCardManagerImpl.refundCard(invalidPord, 100));
	
		//refund a valid but inexistent card
		assertFalse(CreditCardManagerImpl.refundCard(validButMissing, 100));
		
		//refund an existent card
		assertTrue(CreditCardManagerImpl.refundCard(validPord, 100));
	}
}
