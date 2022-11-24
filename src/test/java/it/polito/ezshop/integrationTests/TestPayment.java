package it.polito.ezshop.integrationTests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.InvalidCreditCardException;
import it.polito.ezshop.exceptions.InvalidDiscountRateException;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidPaymentException;
import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidProductDescriptionException;
import it.polito.ezshop.exceptions.InvalidProductIdException;
import it.polito.ezshop.exceptions.InvalidQuantityException;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.exceptions.InvalidTransactionIdException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import it.polito.ezshop.exceptions.UnauthorizedException;
import it.polito.ezshop.model.ReturnTransactionImpl;
import it.polito.ezshop.model.SaleTransactionImpl;

class TestPayment {

	// BEGIN double receiveCashPayment(Integer transactionId, double cash)
	
	@Test
	void testUnauthorizedCashReceived() {
		EZShopInterface ezShop = new EZShop();
		
		ezShop.reset();
			
		assertThrows(UnauthorizedException.class, ()-> ezShop.receiveCashPayment(123456789, 200));
	}
	
	@Test
	void testInvalidAmountCashReceived() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new EZShop();
		
		ezShop.reset();
		ezShop.createUser("Jordan", "superwow", "Cashier");
		ezShop.login("Jordan", "superwow");
		
		assertThrows(InvalidPaymentException.class, ()-> ezShop.receiveCashPayment(123456789, -1));
		assertThrows(InvalidPaymentException.class, ()-> ezShop.receiveCashPayment(123456789, 0));
	}
	
	@Test
	void testInvalidTransactionIdCashCashReceived() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new EZShop();
		
		ezShop.reset();
		ezShop.createUser("Jordan", "superwow", "Cashier");
		ezShop.login("Jordan", "superwow");
		
		assertThrows(InvalidTransactionIdException.class, ()-> ezShop.receiveCashPayment(null, 25));
		assertThrows(InvalidTransactionIdException.class, ()-> ezShop.receiveCashPayment(-1, 25));
		assertThrows(InvalidTransactionIdException.class, ()-> ezShop.receiveCashPayment(0, 25));
	}
	
	@Test
	void testInexistantTransactionCashPayment() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidTransactionIdException, InvalidPaymentException, UnauthorizedException {
		EZShopInterface ezShop = new EZShop();
		
		ezShop.reset();
		ezShop.createUser("Jordan", "superwow", "Cashier");
		ezShop.login("Jordan", "superwow");
		
		assertEquals(-1, ezShop.receiveCashPayment(123456789, 25));
	}
	
	@Test
	void testNotEnoughCashPayment() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, UnauthorizedException, InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidTransactionIdException, InvalidQuantityException, InvalidPaymentException, InvalidDiscountRateException {
		EZShopInterface ezShop = new EZShop();
		
		ezShop.reset();
		
		Initializator.initUser(ezShop);
		Initializator.initSaleBase(ezShop);
		Initializator.initProducts(ezShop);
		
		ezShop.addProductToSale(1, "8057014050035", 1);
		ezShop.endSaleTransaction(1);
		SaleTransactionImpl s = (SaleTransactionImpl) ezShop.getSaleTransaction(1);
		
		s.setPrice(25.0);
		assertEquals(-1, ezShop.receiveCashPayment(1, 20.0));
	}
	
	@Test
	void testCashPayment() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, UnauthorizedException, InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidTransactionIdException, InvalidQuantityException, InvalidPaymentException, InvalidDiscountRateException {
		EZShopInterface ezShop = new EZShop();
		
		ezShop.reset();
		
		Initializator.initUser(ezShop);
		Initializator.initSaleBase(ezShop);
		Initializator.initProducts(ezShop);
		
		ezShop.addProductToSale(1, "8057014050035", 1);
		ezShop.endSaleTransaction(1);
		SaleTransactionImpl s = (SaleTransactionImpl) ezShop.getSaleTransaction(1);
		
		s.setPrice(18.0);
		assertEquals(2.0, ezShop.receiveCashPayment(1, 20.0));
	}
	

	// END
	
	// ======================================================
				
	// BEGIN boolean receiveCreditCardPayment(Integer transactionId, String creditCard)
	
	@Test
	void testUnauthorizedCreditCardReceived() {
		EZShopInterface ezShop = new EZShop();
		
		ezShop.reset();
			
		assertThrows(UnauthorizedException.class, ()-> ezShop.receiveCreditCardPayment(123456789, "79927398713"));
	}

	@Test
	void testInvalidCreditCardPayment() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new EZShop();
		
		ezShop.reset();
		ezShop.createUser("Jordan", "superwow", "Cashier");
		ezShop.login("Jordan", "superwow");
		
		assertThrows(InvalidCreditCardException.class, ()-> ezShop.receiveCreditCardPayment(123456789, null));
		assertThrows(InvalidCreditCardException.class, ()-> ezShop.receiveCreditCardPayment(123456789, ""));
		assertThrows(InvalidCreditCardException.class, ()-> ezShop.receiveCreditCardPayment(123456789, "79927398714"));
	}
	
	@Test
	void testInvalidTransactionIdCreditCardPayment() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new EZShop();
		
		ezShop.reset();
		ezShop.createUser("Jordan", "superwow", "Cashier");
		ezShop.login("Jordan", "superwow");
		
		assertThrows(InvalidTransactionIdException.class, ()-> ezShop.receiveCreditCardPayment(null, "79927398713"));
		assertThrows(InvalidTransactionIdException.class, ()-> ezShop.receiveCreditCardPayment(-1, "79927398713"));
		assertThrows(InvalidTransactionIdException.class, ()-> ezShop.receiveCreditCardPayment(0, "79927398713"));
	}
	
	@Test
	void testInexistantTransactionCreditCardPayment() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidTransactionIdException, InvalidPaymentException, UnauthorizedException, InvalidCreditCardException {
		EZShopInterface ezShop = new EZShop();
		
		ezShop.reset();
		ezShop.createUser("Jordan", "superwow", "Cashier");
		ezShop.login("Jordan", "superwow");
		
		assertFalse(ezShop.receiveCreditCardPayment(123456789, "79927398713"));
	}	
	
	@Test
	void testNotEnoughFundsCreditCardPayment() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, UnauthorizedException, InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidTransactionIdException, InvalidQuantityException, InvalidPaymentException, InvalidCreditCardException, InvalidDiscountRateException {
		EZShopInterface ezShop = new EZShop();
		
		ezShop.reset();
		
		Initializator.initUser(ezShop);
		Initializator.initSaleBase(ezShop);
		Initializator.initProducts(ezShop);
		
		ezShop.addProductToSale(1, "8057014050035", 1);
		ezShop.endSaleTransaction(1);
		ezShop.applyDiscountRateToProduct(1, "8057014050035", 0.5);
		ezShop.applyDiscountRateToSale(1, 0.5);
		
		SaleTransactionImpl s = (SaleTransactionImpl) ezShop.getSaleTransaction(1);
		
		s.setPrice(50000.0);
		assertFalse(ezShop.receiveCreditCardPayment(1, "79927398713"));
	}
	
	@Test
	void testCreditCardPayment() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, UnauthorizedException, InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidTransactionIdException, InvalidQuantityException, InvalidPaymentException, InvalidCreditCardException, InvalidDiscountRateException {
		EZShopInterface ezShop = new EZShop();
		
		ezShop.reset();
		
		Initializator.initUser(ezShop);
		Initializator.initSaleBase(ezShop);
		Initializator.initProducts(ezShop);
		
		ezShop.addProductToSale(1, "8057014050035", 1);
		ezShop.endSaleTransaction(1);
		ezShop.applyDiscountRateToProduct(1, "8057014050035", 0.5);
		ezShop.applyDiscountRateToSale(1, 0.5);
		
		SaleTransactionImpl s = (SaleTransactionImpl) ezShop.getSaleTransaction(1);
		
		s.setPrice(15.0);
		assertTrue(ezShop.receiveCreditCardPayment(1, "79927398713"));
	}
	
	
	// END
	
	// ======================================================
				
	// BEGIN double returnCashPayment(Integer returnId)
	
	@Test
	void testUnauthorizedCashRefund() {
		EZShopInterface ezShop = new EZShop();
		
		ezShop.reset();
			
		assertThrows(UnauthorizedException.class, ()-> ezShop.returnCashPayment(123456789));
	}
	
	@Test
	void testInvalidTransactionIdCashReturn() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new EZShop();
		
		ezShop.reset();
		ezShop.createUser("Jordan", "superwow", "Cashier");
		ezShop.login("Jordan", "superwow");
		
		assertThrows(InvalidTransactionIdException.class, ()-> ezShop.returnCashPayment(null));
		assertThrows(InvalidTransactionIdException.class, ()-> ezShop.returnCashPayment(-1));
		assertThrows(InvalidTransactionIdException.class, ()-> ezShop.returnCashPayment(0));
	}
	
	@Test
	void testInexistantTransactionCashReturn() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidTransactionIdException, InvalidPaymentException, UnauthorizedException {
		EZShopInterface ezShop = new EZShop();
		
		ezShop.reset();
		ezShop.createUser("Jordan", "superwow", "Cashier");
		ezShop.login("Jordan", "superwow");
		
		assertEquals(-1, ezShop.returnCashPayment(123456789));
	}
	
	@Test
	void testNotEndedTransactionCashReturn() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidTransactionIdException, InvalidPaymentException, UnauthorizedException, InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidQuantityException {
		EZShopInterface ezShop = new EZShop();
		
		ezShop.reset();
		Initializator.initReturnWithProducts(ezShop);
		
		assertEquals(-1, ezShop.returnCashPayment(1));
	}
	
	@Test
	void testCashReturn() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidTransactionIdException, InvalidPaymentException, UnauthorizedException, InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidQuantityException {
		EZShopInterface ezShop = new EZShop();
		
		ezShop.reset();
		Initializator.initReturnWithProducts(ezShop);
		ezShop.endReturnTransaction(1, true);
		
		assertEquals(2500.0, ezShop.returnCashPayment(1));
	}

	
	// END
	
	// ======================================================
				
	// BEGIN double returnCreditCardPayment(Integer returnId, String creditCard)
	
	@Test
	void testUnauthorizedCreditCardRefund() {
		EZShopInterface ezShop = new EZShop();
		
		ezShop.reset();
			
		assertThrows(UnauthorizedException.class, ()-> ezShop.returnCreditCardPayment(123456789, "79927398713"));
	}
	
	
	@Test
	void testInvalidCardCreditCardReturn() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new EZShop();
		
		ezShop.reset();
		ezShop.createUser("Jordan", "superwow", "Cashier");
		ezShop.login("Jordan", "superwow");
		
		assertThrows(InvalidCreditCardException.class, ()-> ezShop.returnCreditCardPayment(123456789, null));
		assertThrows(InvalidCreditCardException.class, ()-> ezShop.returnCreditCardPayment(123456789, ""));
		assertThrows(InvalidCreditCardException.class, ()-> ezShop.returnCreditCardPayment(123456789, "79927398714"));
	}
	
	@Test
	void testInvalidTransactionIdCreditCardReturn() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new EZShop();
		
		ezShop.reset();
		ezShop.createUser("Jordan", "superwow", "Cashier");
		ezShop.login("Jordan", "superwow");
		
		assertThrows(InvalidTransactionIdException.class, ()-> ezShop.returnCreditCardPayment(null, "79927398713"));
		assertThrows(InvalidTransactionIdException.class, ()-> ezShop.returnCreditCardPayment(-1, "79927398713"));
		assertThrows(InvalidTransactionIdException.class, ()->  ezShop.returnCreditCardPayment(0, "79927398713"));
	}
	
	@Test
	void testInexistantTransactionCreditCardReturn() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidTransactionIdException, InvalidPaymentException, UnauthorizedException, InvalidCreditCardException {
		EZShopInterface ezShop = new EZShop();
		
		ezShop.reset();
		ezShop.createUser("Jordan", "superwow", "Cashier");
		ezShop.login("Jordan", "superwow");
		
		assertEquals(-1, ezShop.returnCreditCardPayment(123456789, "79927398713"));
	}
	
	@Test
	void testNotEndedTransactionCreditCardReturn() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidTransactionIdException, InvalidPaymentException, UnauthorizedException, InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidQuantityException, InvalidCreditCardException {
		EZShopInterface ezShop = new EZShop();
		
		ezShop.reset();
		Initializator.initReturnWithProducts(ezShop);
		
		assertEquals(-1, ezShop.returnCreditCardPayment(1, "79927398713"));
	}
	
	@Test
	void testCreditCardReturn() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidTransactionIdException, InvalidPaymentException, UnauthorizedException, InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidQuantityException, InvalidCreditCardException {
		EZShopInterface ezShop = new EZShop();
		
		ezShop.reset();
		Initializator.initReturnWithProducts(ezShop);
		ezShop.endReturnTransaction(1, true);
		
		assertEquals(2500.0, ezShop.returnCreditCardPayment(1, "79927398713"));
	}
	
	// END
}
