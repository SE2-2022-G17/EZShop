package it.polito.ezshop.integrationTests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidTransactionIdException;
import it.polito.ezshop.exceptions.UnauthorizedException;
import it.polito.ezshop.model.SaleTransactionImpl;
import it.polito.ezshop.exceptions.InvalidQuantityException;
import it.polito.ezshop.exceptions.InvalidProductIdException;
import it.polito.ezshop.exceptions.InvalidDiscountRateException;


public class TestReturns {
	
	/*
	 * -------------------------------------------------startReturnTransaction-----------------------------------------------------------
	 */
	
	@Test
	public void TestStartReturnTransactionInvalidUser() {
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();
		ez.reset();
		assertThrows(UnauthorizedException.class, () -> ez.startReturnTransaction(1));
	}
	@Test
	public void TestStartReturnTransactionInvalidTransactionId() throws UnauthorizedException, InvalidTransactionIdException{
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();
		ez.reset();
		Initializator.initSaleWithProductsPayed(ez);
		
		assertThrows(InvalidTransactionIdException.class, () -> ez.startReturnTransaction(0));
		assertThrows(InvalidTransactionIdException.class, () -> ez.startReturnTransaction(null));
		assertEquals(-1, ez.startReturnTransaction(3));
		
		ez.reset();
		Initializator.initSaleWithProductsClosed(ez);
		assertEquals(-1, ez.startReturnTransaction(1));
		
		
		
	}
	@Test
	public void TestStartReturnTransactionSuccessful() throws UnauthorizedException, InvalidTransactionIdException{
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();
		ez.reset();
		Initializator.initSaleWithProductsPayed(ez);
		
		assertEquals(1, ez.startReturnTransaction(1));
	}
	/*
	 * -------------------------------------------------END-----------------------------------------------------------
	 */
	
	/*
	 * -------------------------------------------------returnProduct-----------------------------------------------------------
	 */
	@Test
	public void TestReturnProductInvalidUser() {
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();
		ez.reset();
		assertThrows(UnauthorizedException.class, () -> ez.returnProduct(1, "8057014050035", 25));
	}
	
	@Test
	public void TestReturnProductInvalidTransactionId() throws InvalidProductCodeException, UnauthorizedException, InvalidTransactionIdException,
	InvalidQuantityException{
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();
		ez.reset();
		Initializator.initSaleWithProductsPayed(ez);
		
		assertThrows(InvalidTransactionIdException.class, () -> ez.returnProduct(0, "8057014050035", 25));
		assertThrows(InvalidTransactionIdException.class, () -> ez.returnProduct(null, "8057014050035", 25));
		assertFalse(ez.returnProduct(23, "8057014050035", 25));
	}
	
	@Test
	public void TestReturnProductInvalidProductCode() throws InvalidProductCodeException, UnauthorizedException, InvalidTransactionIdException,
	InvalidQuantityException, InvalidProductIdException{
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();
		ez.reset();
		Initializator.initSaleWithProductsPayed(ez);
		Initializator.initReturnBase(ez);
		
		assertThrows(InvalidProductCodeException.class, () -> ez.returnProduct(1, null, 25));
		assertThrows(InvalidProductCodeException.class, () -> ez.returnProduct(1, "", 25));
		assertThrows(InvalidProductCodeException.class, () -> ez.returnProduct(1, "8057014050035839283928", 25));
		assertFalse(ez.returnProduct(1, "9788838663260", 25));
		
		ez.deleteProductType(2);
		assertFalse(ez.returnProduct(1, "9788838663260", 25));
		
	}
	@Test
	public void TestReturnProductInvalidQuantity() throws InvalidProductCodeException, UnauthorizedException, InvalidTransactionIdException,
	InvalidQuantityException, InvalidProductIdException{
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();
		ez.reset();
		Initializator.initSaleWithProductsPayed(ez);
		Initializator.initReturnBase(ez);
		
		assertThrows(InvalidQuantityException.class, () -> ez.returnProduct(1, "8057014050035", -5));
		
		assertFalse(ez.returnProduct(1, "8057014050035", 2000));
		assertTrue(ez.returnProduct(1, "8057014050035", 45));
		assertFalse(ez.returnProduct(1, "8057014050035", 45));
	}
	@Test
	public void TestReturnProductSuccessful() throws InvalidProductCodeException, UnauthorizedException, InvalidTransactionIdException,
	InvalidQuantityException, InvalidProductIdException{
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();
		ez.reset();
		Initializator.initSaleWithProductsPayed(ez);
		Initializator.initReturnBase(ez);
		
		assertThrows(InvalidQuantityException.class, () -> ez.returnProduct(1, "8057014050035", -5));
		
		assertTrue(ez.returnProduct(1, "8057014050035", 25));
		
	}
	/*
	 * -------------------------------------------------END-----------------------------------------------------------
	 */
	
	/*
	 * -------------------------------------------------endReturnTransaction-----------------------------------------------------------
	 */
	@Test
	public void TestEndReturnTransactionInvalidUser() {
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();
		ez.reset();
		assertThrows(UnauthorizedException.class, () -> ez.endReturnTransaction(1, false));
	}
	
	@Test
	public void TestEndReturnTransactionInvaliTransactionId() throws UnauthorizedException, InvalidTransactionIdException {
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();
		ez.reset();
		Initializator.initReturnWithProducts(ez);
		
		assertThrows(InvalidTransactionIdException.class, () -> ez.endReturnTransaction(0, false));
		assertThrows(InvalidTransactionIdException.class, () -> ez.endReturnTransaction(null, false));
		assertFalse(ez.endReturnTransaction(23, false));
	}
	
	@Test
	public void TestEndReturnTransactionSuccessful() throws UnauthorizedException, InvalidTransactionIdException, InvalidProductCodeException, 
	InvalidQuantityException, InvalidDiscountRateException{
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();
		ez.reset();
		Initializator.initReturnWithProducts(ez);
		
		assertTrue(ez.endReturnTransaction(1, false));
		assertTrue(ez.deleteReturnTransaction(1));
		assertEquals(50, ez.getProductTypeByBarCode("8057014050035").getQuantity());
		
		ez.reset();
		Initializator.initReturnWithProducts(ez);
		
		assertTrue(ez.endReturnTransaction(1, true));
		assertEquals(75, ez.getProductTypeByBarCode("8057014050035").getQuantity());
		assertTrue(ez.deleteReturnTransaction(1));
		assertEquals(50, ez.getProductTypeByBarCode("8057014050035").getQuantity());
		
		
		ez.reset();
		Initializator.initSaleWithProducts(ez);
		assertTrue(ez.applyDiscountRateToProduct(1, "8057014050035", 0.5));
		assertTrue(ez.endSaleTransaction(1));
		assertTrue(ez.applyDiscountRateToSale(1, 0.5));
		SaleTransactionImpl s = (SaleTransactionImpl) ez.getSaleTransaction(1);
		double price = s.getPrice();
		s.setState(2);
		Initializator.initReturnBase(ez);
		ez.returnProduct(1, "8057014050035", 25);
		
		assertEquals(2500, price);
		assertTrue(ez.endReturnTransaction(1, true));
		assertEquals(75, ez.getProductTypeByBarCode("8057014050035").getQuantity());
		assertEquals(price - 1250, s.getPrice());
		assertEquals(25, s.getEntry("8057014050035").getAmount());
		
		assertTrue(ez.deleteReturnTransaction(1));
		assertEquals(2500, price);
		assertEquals(50,s.getEntry("8057014050035").getAmount());
		
		
		EZShopInterface ez2 = new it.polito.ezshop.data.EZShop();
		Initializator.initUser(ez2);
		assertEquals(50, ez2.getProductTypeByBarCode("8057014050035").getQuantity());
	}
	
	/*
	 * -------------------------------------------------END-----------------------------------------------------------
	 */
	
	/*
	 * -------------------------------------------------deleteReturnTransaction-----------------------------------------------------------
	 */
	
	@Test
	public void TestDeleteReturnTransactionInvalidUser() {
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();
		ez.reset();
		assertThrows(UnauthorizedException.class, () -> ez.deleteReturnTransaction(1));
	}
	
	@Test
	public void TestDeleteReturnTransactionInvalidTransactionId() throws UnauthorizedException, InvalidTransactionIdException {
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();
		ez.reset();
		Initializator.initReturnWithProducts(ez);
		
		assertThrows(InvalidTransactionIdException.class, () -> ez.deleteReturnTransaction(0));
		assertThrows(InvalidTransactionIdException.class, () -> ez.deleteReturnTransaction(null));
		assertFalse(ez.deleteReturnTransaction(23));
	}
	
	@Test
	public void TestDeleteReturnTransactionSuccessful() throws UnauthorizedException, InvalidTransactionIdException, InvalidProductCodeException, 
	InvalidQuantityException, InvalidDiscountRateException{
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();
		ez.reset();
		Initializator.initReturnWithProducts(ez);
		
		assertTrue(ez.endReturnTransaction(1, false));
		
		assertFalse(ez.endReturnTransaction(1, false));
		
		ez.reset();
		Initializator.initSaleWithProducts(ez);
		assertTrue(ez.applyDiscountRateToProduct(1, "8057014050035", 0.5));
		assertTrue(ez.endSaleTransaction(1));
		assertTrue(ez.applyDiscountRateToSale(1, 0.5));
		SaleTransactionImpl s = (SaleTransactionImpl) ez.getSaleTransaction(1);
		double price = s.getPrice();
		s.setState(2);
		Initializator.initReturnBase(ez);
		ez.returnProduct(1, "8057014050035", 25);
		
		
		
		assertEquals(2500, price);
		assertTrue(ez.endReturnTransaction(1, true));
		assertEquals(75, ez.getProductTypeByBarCode("8057014050035").getQuantity());
		assertEquals(price - 1250, s.getPrice());
		assertEquals(25, s.getEntry("8057014050035").getAmount());
		
		
		
		EZShopInterface ez2 = new it.polito.ezshop.data.EZShop();
		Initializator.initUser(ez2);
		assertEquals(75, ez2.getProductTypeByBarCode("8057014050035").getQuantity());
		
	}
	
	/*
	 * -------------------------------------------------END-----------------------------------------------------------
	 */
	
	
	
	
}
