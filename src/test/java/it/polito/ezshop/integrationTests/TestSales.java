package it.polito.ezshop.integrationTests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.data.SaleTransaction;
import it.polito.ezshop.model.SaleTransactionImpl;

import it.polito.ezshop.exceptions.UnauthorizedException;
import it.polito.ezshop.exceptions.*;

//import it.polito.ezshop.integrationTests.Initializator;

public class TestSales {

	/*
	 * -------------------------------------------------StartSaleTransaction-----------------------------------------------------------
	 */
	@Test
	public void TestStartSaleTransaction() throws UnauthorizedException, InvalidRoleException, InvalidUsernameException, InvalidPasswordException {
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();
		ez.reset();
		assertThrows(UnauthorizedException.class, () -> ez.startSaleTransaction());
		
		ez.reset();
		ez.createUser("airjordan", "chicagobulls", "Cashier");
		assertNotNull(ez.login("airjordan", "chicagobulls"));
		
		Integer saleId = ez.startSaleTransaction();
		assertEquals(1, saleId);
		
	}
	/*
	 * -------------------------------------------------END-----------------------------------------------------------
	 */
	
	/*
	 * -------------------------------------------------AddProductsToSale-----------------------------------------------------------
	 */
	@Test
	public void TestaddProductToSaleInvalidUser() {
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();	
		ez.reset();
		assertThrows(UnauthorizedException.class, ()->ez.addProductToSale(1, "8057014050035", 1));
	}
	
	@Test
	public void TestaddProductToSaleInvalidTransaction() throws UnauthorizedException,  
	InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, 
	InvalidPricePerUnitException, InvalidProductDescriptionException{
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();	
		ez.reset();
		Initializator.initUser(ez);
		Initializator.initSaleBase(ez);
		
		assertThrows(InvalidTransactionIdException.class, ()->ez.addProductToSale(-1, "8057014050035", 1));
		assertThrows(InvalidTransactionIdException.class, ()->ez.addProductToSale(null, "8057014050035", 1));
		assertFalse(ez.addProductToSale(20000, "8057014050035", 1));
		
		ez.reset();
		Initializator.initSaleWithProductsClosed(ez);
		
		SaleTransactionImpl s = (SaleTransactionImpl) ez.getSaleTransaction(1);
		
		assertEquals(1, s.getState());
		assertFalse(ez.addProductToSale(1,"8057014050035", 1));
		
		
	}
	@Test
	public void TestaddProductToSaleInvalidProductCode() throws UnauthorizedException, InvalidTransactionIdException,InvalidProductCodeException,
	InvalidQuantityException{
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();	
		ez.reset();
		Initializator.initUser(ez);
		Initializator.initSaleBase(ez);
		Initializator.initProducts(ez);
		
		try {
			ez.deleteProductType(2);
		}catch(Exception e) {
			e.printStackTrace();
		}
		assertThrows(InvalidProductCodeException.class, ()->ez.addProductToSale(1, null, 1));
		assertThrows(InvalidProductCodeException.class, ()->ez.addProductToSale(1, "", 1));
		assertThrows(InvalidProductCodeException.class, ()->ez.addProductToSale(1, "805701405928398293892389283", 1));
		assertFalse(ez.addProductToSale(1, "9788838663260", 2));
	}
	@Test
	public void TestaddProductToSaleInvalidQuantity() throws UnauthorizedException, InvalidTransactionIdException,InvalidProductCodeException,
	InvalidQuantityException{
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();	
		ez.reset();
		Initializator.initUser(ez);
		Initializator.initSaleBase(ez);
		Initializator.initProducts(ez);

		assertThrows(InvalidQuantityException.class, ()->ez.addProductToSale(1, "8057014050035", -1));
		assertFalse(ez.addProductToSale(1, "8057014050035", 2000));
	}
	
	@Test
	public void TestaddProductToSaleSuccessful() throws UnauthorizedException, InvalidTransactionIdException,InvalidProductCodeException,
	InvalidQuantityException{
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();	
		ez.reset();
		Initializator.initUser(ez);
		Initializator.initSaleBase(ez);
		Initializator.initProducts(ez);

		assertTrue(ez.addProductToSale(1, "8057014050035", 50));
		assertTrue(ez.addProductToSale(1, "8057014050035", 25));
		
		assertEquals(25,ez.getProductTypeByBarCode("8057014050035").getQuantity());
		
	}
	
	/*
	 * -------------------------------------------------END-----------------------------------------------------------
	 */
	
	
	/*
	 * -------------------------------------------------deleteProductFromSale-----------------------------------------------------------
	 */
	
	@Test
	public void TestdeleteProductFromSaleInvalidUser() {
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();	
		ez.reset();
		assertThrows(UnauthorizedException.class, ()->ez.deleteProductFromSale(1, "8057014050035", 1));
	}
	@Test

	public void TestdeleteProductFromSaleInvalidTransaction() throws UnauthorizedException, InvalidTransactionIdException,InvalidProductCodeException,
	InvalidQuantityException{
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();	
		ez.reset();
		Initializator.initUser(ez);
		Initializator.initSaleBase(ez);
		assertThrows(InvalidTransactionIdException.class, ()->ez.deleteProductFromSale(-22, "8057014050035", 1));
		assertThrows(InvalidTransactionIdException.class, ()->ez.deleteProductFromSale(null, "8057014050035", 1));
		assertFalse(ez.deleteProductFromSale(20000, "8057014050035", 1));
		
		ez.reset();
		Initializator.initSaleWithProductsClosed(ez);
		
		SaleTransactionImpl s = (SaleTransactionImpl) ez.getSaleTransaction(1);
		
		assertEquals(1, s.getState());
		assertFalse(ez.deleteProductFromSale(1,"8057014050035", 1));
		
	}
	
	@Test
	public void TestdeleteProductFromSaleInvalidProductCode() throws UnauthorizedException, InvalidTransactionIdException,InvalidProductCodeException,
	InvalidQuantityException{
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();	
		ez.reset();
		Initializator.initSaleWithProducts(ez);
		
		
		assertThrows(InvalidProductCodeException.class, ()->ez.deleteProductFromSale(1, null, 1));
		assertThrows(InvalidProductCodeException.class, ()->ez.deleteProductFromSale(1, "", 1));
		assertThrows(InvalidProductCodeException.class, ()->ez.deleteProductFromSale(1, "805701405928398293892389283", 1));
		assertFalse(ez.deleteProductFromSale(1, "9788838663260", 1));
		
		try {
			ez.deleteProductType(2);
		}catch(Exception e) {
			e.printStackTrace();
		}
		assertFalse(ez.deleteProductFromSale(1, "9788838663260", 1));
		
	}
	
	@Test
	public void TestdeleteProductFromSaleInvalidQuantity() throws UnauthorizedException, InvalidTransactionIdException,InvalidProductCodeException,
	InvalidQuantityException{
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();	
		ez.reset();
		Initializator.initSaleWithProducts(ez);
		
		assertThrows(InvalidQuantityException.class, ()->ez.deleteProductFromSale(1, "8057014050035", -1));
		assertFalse(ez.deleteProductFromSale(1, "8057014050035", 2000));
		
	}
	
	@Test 
	public void TestdeleteProductFromSaleSuccessful() throws UnauthorizedException, InvalidTransactionIdException,InvalidProductCodeException,
	InvalidQuantityException{
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();	
		ez.reset();
		Initializator.initSaleWithProducts(ez);
		
		assertTrue(ez.deleteProductFromSale(1, "8057014050035", 25));
		assertEquals(75, ez.getProductTypeByBarCode("8057014050035").getQuantity());
	}
	
	
	/*
	 * -------------------------------------------------END-----------------------------------------------------------
	 */
	
	
	/*
	 * -------------------------------------------------applyDiscountRateToProduct-----------------------------------------------------------
	 */
	@Test 
	public void TestApplyDiscountRateToProductInvalidUser() {
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();	
		ez.reset();
		assertThrows(UnauthorizedException.class, ()->ez.applyDiscountRateToProduct(1, "8057014050035", 0.5 ));
	}
	@Test
	public void TestApplyDiscountRateToProductInvalidDiscountRate() {
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();	
		ez.reset();
		Initializator.initSaleWithProducts(ez);
		
		
		assertThrows(InvalidDiscountRateException.class, ()->ez.applyDiscountRateToProduct(1, "8057014050035", 1.5 ));
	}
	@Test
	public void TestApplyDiscountRateToProductInvalidTransactionId() throws InvalidTransactionIdException, InvalidDiscountRateException,
	InvalidProductCodeException,UnauthorizedException{
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();	
		ez.reset();
		Initializator.initUser(ez);
		Initializator.initSaleBase(ez);
		
		
		assertThrows(InvalidTransactionIdException.class, ()->ez.applyDiscountRateToProduct(0, "8057014050035", 0.5 ));
		assertThrows(InvalidTransactionIdException.class, ()->ez.applyDiscountRateToProduct(null, "8057014050035", 0.5 ));
		assertFalse(ez.applyDiscountRateToProduct(20000, "8057014050035", 0.5));
		
		ez.reset();
		Initializator.initSaleWithProductsClosed(ez);
		
		SaleTransactionImpl s = (SaleTransactionImpl) ez.getSaleTransaction(1);
		
		assertEquals(1, s.getState());
		assertFalse(ez.applyDiscountRateToProduct(1,"8057014050035", 0.4));

	}
	
	@Test
	public void TestApplyDiscountRateToProductInvalidProductCode() throws InvalidTransactionIdException, InvalidDiscountRateException,
	InvalidProductCodeException,UnauthorizedException{
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();	
		ez.reset();
		Initializator.initSaleWithProducts(ez);
		
		assertThrows(InvalidProductCodeException.class, ()->ez.applyDiscountRateToProduct(1, null, 0.5));
		assertThrows(InvalidProductCodeException.class, ()->ez.applyDiscountRateToProduct(1, "", 0.5));
		assertThrows(InvalidProductCodeException.class, ()->ez.applyDiscountRateToProduct(1, "805701405928398293892389283", 0.5));
		assertFalse(ez.applyDiscountRateToProduct(1, "9788838663260", 0.5));
		
		try {
			ez.deleteProductType(2);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		assertFalse(ez.applyDiscountRateToProduct(1, "9788838663260", 0.5));
		
	}
	@Test
	public void TestApplyDiscountRateToProductSuccessful() throws InvalidTransactionIdException, InvalidDiscountRateException,
	InvalidProductCodeException,UnauthorizedException{
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();	
		ez.reset();
		Initializator.initSaleWithProducts(ez);
		
		assertTrue(ez.applyDiscountRateToProduct(1, "8057014050035", 0.5));
		assertTrue(ez.applyDiscountRateToProduct(1, "8057014050035", 0.75));
		
	}
	/*
	 * -------------------------------------------------END-----------------------------------------------------------
	 */
	
	/*
	 * -------------------------------------------------applyDiscountRateToSale-----------------------------------------------------------
	 */
	@Test
	public void TestApplyDiscountRateToSaleInvalidUser() {
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();	
		ez.reset();
		assertThrows(UnauthorizedException.class, ()->ez.applyDiscountRateToSale(1,  0.5 ));
	}
	@Test
	public void TestApplyDiscountRateToSaleInvalidDiscountRate() {
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();	
		ez.reset();
		Initializator.initSaleWithProducts(ez);
		assertThrows(InvalidDiscountRateException.class, ()->ez.applyDiscountRateToSale(1,  1.5 ));
		assertThrows(InvalidDiscountRateException.class, ()->ez.applyDiscountRateToSale(1,  -1.5 ));
		
	}
	@Test
	public void TestApplyDiscountRateToSaleInvalidTransactionId() throws InvalidTransactionIdException, InvalidDiscountRateException, UnauthorizedException {
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();	
		ez.reset();
		Initializator.initSaleWithProducts(ez);
		
		assertThrows(InvalidTransactionIdException.class, ()->ez.applyDiscountRateToSale(null,  0.5 ));
		assertThrows(InvalidTransactionIdException.class, ()->ez.applyDiscountRateToSale(-4,  0.5 ));
		assertFalse(ez.applyDiscountRateToSale(2000, 0.4));
		
		
	}
	@Test
	public void TestApplyDiscountRateToSaleSuccessfull() throws InvalidTransactionIdException, InvalidDiscountRateException, UnauthorizedException {
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();	
		ez.reset();
		Initializator.initSaleWithProducts(ez);
		assertTrue(ez.applyDiscountRateToSale(1, 0.5));
		
		assertTrue(ez.applyDiscountRateToSale(1, 0.75));
	}
	
	/*
	 * -------------------------------------------------END-----------------------------------------------------------
	 */
	/*
	 * -------------------------------------------------computePointsForSale-----------------------------------------------------------
	 */
	@Test
	public void TestComputePointsForSaleInvalidUser() {
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();	
		ez.reset();
		assertThrows(UnauthorizedException.class, ()->ez.computePointsForSale(1));
	}
	@Test
	public void TestComputePointsForSaleInvalidTransactionId() throws InvalidTransactionIdException, UnauthorizedException{
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();	
		ez.reset();
		Initializator.initSaleWithProducts(ez);
		
		assertThrows(InvalidTransactionIdException.class, ()->ez.computePointsForSale(0));
		assertThrows(InvalidTransactionIdException.class, ()->ez.computePointsForSale(null));
		assertEquals(-1, ez.computePointsForSale(4));
	}
	@Test
	public void TestComputePointsForSaleSuccessful() throws InvalidTransactionIdException, UnauthorizedException{
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();	
		ez.reset();
		Initializator.initSaleWithProducts(ez);
		
		assertEquals(1000, ez.computePointsForSale(1));
	}
	
	
	
	/*
	 * -------------------------------------------------END-----------------------------------------------------------
	 */
	
	/*
	 * -------------------------------------------------endSaleTransaction-----------------------------------------------------------
	 */
	
	@Test
	public void TestEndSaleTransactionInvalidUser() {
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();	
		ez.reset();
		assertThrows(UnauthorizedException.class, ()->ez.endSaleTransaction(1));
	}
	@Test
	public void TestEndSaleTransactionInvalidTransactionId() throws UnauthorizedException, InvalidTransactionIdException{
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();	
		ez.reset();
		Initializator.initSaleWithProducts(ez);
		
		assertThrows(InvalidTransactionIdException.class, ()->ez.endSaleTransaction(0));
		assertThrows(InvalidTransactionIdException.class, ()->ez.endSaleTransaction(null));
		assertFalse(ez.endSaleTransaction(59));
		
		ez.reset();
		Initializator.initSaleWithProductsClosed(ez);
		assertFalse(ez.endSaleTransaction(1));
		
	}
	
	@Test
	public void TestEndSaleTransactionSuccessfull() throws UnauthorizedException, InvalidTransactionIdException, InvalidProductCodeException{
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();	
		ez.reset();
		
		Initializator.initSaleWithProducts(ez);
		
		assertTrue(ez.endSaleTransaction(1));
		
		EZShopInterface ez2 = new it.polito.ezshop.data.EZShop();
		Initializator.initUser(ez2);
		
		
		assertEquals(50,ez2.getProductTypeByBarCode("8057014050035").getQuantity());
		SaleTransactionImpl s = (SaleTransactionImpl) ez.getSaleTransaction(1);
		assertEquals(1, s.getState());
		assertEquals(50, s.getEntry("8057014050035").getAmount());
		
	}
	
	/*
	 * -------------------------------------------------END-----------------------------------------------------------
	 */
	
	/*
	 * -------------------------------------------------deleteSaleTransaction-----------------------------------------------------------
	 */
	@Test
	public void TestDeleteSaleTransactionInvalidUser() {
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();	
		ez.reset();
		
		assertThrows(UnauthorizedException.class, ()->ez.deleteSaleTransaction(1));
	}
	
	@Test
	public void TestDeleteSaleTransactionInvalidTransactionId() throws UnauthorizedException, InvalidTransactionIdException{
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();	
		ez.reset();
		Initializator.initSaleWithProducts(ez);
		
		assertThrows(InvalidTransactionIdException.class, ()->ez.deleteSaleTransaction(0));
		assertThrows(InvalidTransactionIdException.class, ()->ez.deleteSaleTransaction(null));
		assertFalse(ez.deleteSaleTransaction(59));
	}
	
	@Test
	public void TestDeleteSaleTransactionSuccessful() throws UnauthorizedException, InvalidTransactionIdException, InvalidProductCodeException{
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();	
		ez.reset();
		Initializator.initSaleWithProducts(ez);
		
		assertTrue(ez.deleteSaleTransaction(1));
		assertEquals(100, ez.getProductTypeByBarCode("8057014050035").getQuantity());
		
		ez.reset();
		Initializator.initSaleWithProductsClosed(ez);
		
		assertEquals(50, ez.getProductTypeByBarCode("8057014050035").getQuantity());
		assertTrue(ez.deleteSaleTransaction(1));
		assertEquals(100, ez.getProductTypeByBarCode("8057014050035").getQuantity());
		
		if(ez.getSaleTransaction(1) == null) {
			assertTrue(true);
		}else {
			assertTrue(false);
		}
		
		
		EZShopInterface ez2 = new it.polito.ezshop.data.EZShop();
		Initializator.initUser(ez2);
		
		
		assertEquals(100,ez2.getProductTypeByBarCode("8057014050035").getQuantity());
		if(ez2.getSaleTransaction(1) == null) {
			assertTrue(true);
		}else {
			assertTrue(false);
		}
		
	}
	
	
	/*
	 * -------------------------------------------------END-----------------------------------------------------------
	 */
	
	/*
	 * -------------------------------------------------getSaleTransaction-----------------------------------------------------------
	 */
	@Test
	public void TestgetSaleTransactionInvalidUser() {
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();	
		ez.reset();
		assertThrows(UnauthorizedException.class, ()->ez.getSaleTransaction(1));
	}
	@Test
	public void TestgetSaleTransactionInvalidTransactionId() throws UnauthorizedException, InvalidTransactionIdException{
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();	
		ez.reset();
		Initializator.initSaleWithProducts(ez);
		
		assertThrows(InvalidTransactionIdException.class, ()->ez.getSaleTransaction(0));
		assertThrows(InvalidTransactionIdException.class, ()->ez.getSaleTransaction(null));
		
		if(ez.getSaleTransaction(1) == null) {
			assertTrue(true);
		}else {
			assertTrue(false);
		}
		
		if(ez.getSaleTransaction(3) == null) {
			assertTrue(true);
		}else {
			assertTrue(false);
		}
		
	}
	@Test
	public void TestgetSaleTransactionSuccessful() throws UnauthorizedException, InvalidTransactionIdException{
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();	
		ez.reset();
		Initializator.initSaleWithProductsClosed(ez);
		
		SaleTransaction s = ez.getSaleTransaction(1);
		
		assertEquals(10000, s.getPrice());
		
	}
	
	/*
	 * -------------------------------------------------END-----------------------------------------------------------
	 */
	
	
}
