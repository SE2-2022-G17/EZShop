package it.polito.ezshop.integrationTests;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.data.SaleTransaction;
import it.polito.ezshop.exceptions.InvalidLocationException;
import it.polito.ezshop.exceptions.InvalidOrderIdException;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidProductDescriptionException;
import it.polito.ezshop.exceptions.InvalidProductIdException;
import it.polito.ezshop.exceptions.InvalidQuantityException;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import it.polito.ezshop.exceptions.UnauthorizedException;
import it.polito.ezshop.model.ProductImpl;
import it.polito.ezshop.model.ProductTypeImpl;
import it.polito.ezshop.model.SaleTransactionImpl;
import it.polito.ezshop.model.TicketEntryImpl;
import it.polito.ezshop.exceptions.InvalidRFIDException;
import it.polito.ezshop.exceptions.InvalidTransactionIdException;

public class TestRFID {
	/*** test recordOrderArrivalRFID ***/
	@Test
	public void testRecordOrderArrivalRFIDUnauthorized() {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
        ezShop.reset();
        
        assertThrows(it.polito.ezshop.exceptions.UnauthorizedException.class, () -> {
        	ezShop.recordOrderArrivalRFID(1, "0000001000");
        });
        
        assertThrows(it.polito.ezshop.exceptions.UnauthorizedException.class, () -> {
        	ezShop.createUser("Mario64", "itsame", "Cashier");
        	ezShop.login("Mario64", "itsame");
        	ezShop.recordOrderArrivalRFID(1, "0000001000");
        });
	}
	
	@Test
	public void testRecordOrderArrivalRFIDInvalidOrderId() throws UnauthorizedException, InvalidLocationException,InvalidQuantityException, InvalidProductIdException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
        ezShop.reset();
        
        ezShop.createUser("mario", "a1b2c3", "Administrator");
		ezShop.login("mario", "a1b2c3");
		ezShop.createProductType("YEEZY 500", "8057014050035", 200.0, "DESERT RAT BLUSH");
		ezShop.updatePosition(1, "1-a-1");
		ezShop.recordBalanceUpdate(1000);
		ezShop.payOrderFor("8057014050035", 20, 15.0);
		
		assertThrows(it.polito.ezshop.exceptions.InvalidOrderIdException.class, ()->{
			 ezShop.recordOrderArrivalRFID(0, "0000001000");
		});
		assertThrows(it.polito.ezshop.exceptions.InvalidOrderIdException.class, ()->{
			 ezShop.recordOrderArrivalRFID(-1, "0000001000");
		});
		assertThrows(it.polito.ezshop.exceptions.InvalidOrderIdException.class, ()->{
			 ezShop.recordOrderArrivalRFID(null, "0000001000");
		});
	}
	
	@Test
	public void testRecordOrderArrivalInvalidPosition () throws UnauthorizedException, InvalidLocationException, InvalidQuantityException, InvalidProductIdException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		
		ezShop.reset();
		ezShop.createUser("mario", "a1b2c3", "Administrator");
		ezShop.login("mario", "a1b2c3");
		ezShop.createProductType("YEEZY 500", "8057014050035", 200.0, "DESERT RAT BLUSH");
		ezShop.recordBalanceUpdate(1000);
		ezShop.payOrderFor("8057014050035", 20, 15.0);
		
		assertThrows(it.polito.ezshop.exceptions.InvalidLocationException.class, ()->{
			 ezShop.recordOrderArrivalRFID(1, "000000001000");
		});
		ezShop.updatePosition(1, "");
		assertThrows(it.polito.ezshop.exceptions.InvalidLocationException.class, ()->{
			 ezShop.recordOrderArrivalRFID(1, "000000001000");
		});
	}
	
	@Test
	public void testRecordOrderArrivalInvalidRFID () throws UnauthorizedException, InvalidOrderIdException, InvalidRFIDException, InvalidLocationException, InvalidQuantityException, InvalidProductIdException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		
		ezShop.reset();
		ezShop.createUser("mario", "a1b2c3", "Administrator");
		ezShop.login("mario", "a1b2c3");
		ezShop.createProductType("YEEZY 500", "8057014050035", 200.0, "DESERT RAT BLUSH");
		ezShop.updatePosition(1, "1-a-1");
		ezShop.recordBalanceUpdate(1000);
		ezShop.payOrderFor("8057014050035", 20, 15.0);
		ezShop.payOrderFor("8057014050035", 20, 15.0);
		
		assertThrows(it.polito.ezshop.exceptions.InvalidRFIDException.class, ()->{
			 ezShop.recordOrderArrivalRFID(1, "1000");
		});
		assertThrows(it.polito.ezshop.exceptions.InvalidRFIDException.class, ()->{
			 ezShop.recordOrderArrivalRFID(1, null);
		});
		assertThrows(it.polito.ezshop.exceptions.InvalidRFIDException.class, ()->{
			 ezShop.recordOrderArrivalRFID(1, "+0000001000");
		});
		ezShop.recordOrderArrivalRFID(1, "000000001000");
		assertThrows(it.polito.ezshop.exceptions.InvalidRFIDException.class, ()->{
			 ezShop.recordOrderArrivalRFID(2, "000000001000");
		});
	}
	
	@Test
	public void testRecordOrderArrivalSuccessful () throws UnauthorizedException, InvalidOrderIdException, InvalidRFIDException, InvalidLocationException, InvalidQuantityException, InvalidProductIdException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		
		ezShop.reset();
		ezShop.createUser("mario", "a1b2c3", "Administrator");
		ezShop.login("mario", "a1b2c3");
		ezShop.createProductType("YEEZY 500", "8057014050035", 200.0, "DESERT RAT BLUSH");
		ezShop.updatePosition(1, "1-a-1");
		ezShop.recordBalanceUpdate(1000);
		ezShop.payOrderFor("8057014050035", 20, 15.0);
		ezShop.issueOrder("8057014050035", 20, 15.0);
		
		assertTrue(ezShop.recordOrderArrivalRFID(1, "000000001000"));
		assertTrue(ezShop.recordOrderArrivalRFID(1, "000000001000"));
		assertFalse(ezShop.recordOrderArrivalRFID(3, "000000002000"));
		assertFalse(ezShop.recordOrderArrivalRFID(2, "000000002000"));
	}
	
	/*** ***/

	
	/*** test addProductToSaleRFID ***/
	
	@Test
	void testUnauthorizedSaleRFID() {
		EZShopInterface ezShop = new EZShop();
		
		ezShop.reset();
			
		assertThrows(UnauthorizedException.class, ()-> ezShop.addProductToSaleRFID(1, "000000001001"));
	}
	
	@Test
	public void testSaleRFIDInvalidTransactionId(){
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();	
		ez.reset();
		Initializator.initUser(ez);
		Initializator.initSaleBase(ez);
        
        assertThrows(InvalidTransactionIdException.class, ()-> ez.addProductToSaleRFID(0, "000000001001"));
        assertThrows(InvalidTransactionIdException.class, ()-> ez.addProductToSaleRFID(-5, "000000001001"));
        assertThrows(InvalidTransactionIdException.class, ()-> ez.addProductToSaleRFID(null, "000000001001"));
	}
	
	@Test
	public void testSaleRFIDInvalidRFID(){
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();
        ez.reset();
        Initializator.initReturnWithProducts(ez);
        
        assertThrows(InvalidRFIDException.class, ()-> ez.addProductToSaleRFID(1, ""));
        assertThrows(InvalidRFIDException.class, ()-> ez.addProductToSaleRFID(1, null));
        assertThrows(InvalidRFIDException.class, ()-> ez.addProductToSaleRFID(1, "0000000010000001"));
        assertThrows(InvalidRFIDException.class, ()-> ez.addProductToSaleRFID(1, "001"));
        assertThrows(InvalidRFIDException.class, ()-> ez.addProductToSaleRFID(1, "1asdasdas1"));
	}
	
	@Test
	public void testTransactionIdNonExistentOrClosed() throws UnauthorizedException, InvalidTransactionIdException, InvalidRFIDException, InvalidQuantityException{
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();	
		ez.reset();
		Initializator.initUser(ez);
		Initializator.RFIDinitProducts(ez);
		
        assertFalse(ez.addProductToSaleRFID(92, "000000001001"));
        
        ez.reset();
        Initializator.RFIDinitSaleWithProductsClosed(ez);
		
		SaleTransactionImpl s = (SaleTransactionImpl) ez.getSaleTransaction(1);
		assertEquals(1, s.getState());
		assertFalse(ez.addProductToSaleRFID(1,"000000001001"));
	}
	
	@Test
	public void testSaleRFIDNonExistent() throws UnauthorizedException, InvalidTransactionIdException, InvalidRFIDException, InvalidQuantityException{
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();	
		ez.reset();
		Initializator.initUser(ez);
		Initializator.initSaleBase(ez);
        
        assertFalse(ez.addProductToSaleRFID(1, "000000001001"));	
	}
	
	
	//franco: these are tests related to addProductToSaleRFID and the modified versions of endSaleTransaction and deleteSaleTransaction
	@Test
	public void testEndSaleTransactionCompatibilityRFID() throws UnauthorizedException, InvalidTransactionIdException, InvalidRFIDException, InvalidQuantityException{
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();
        ez.reset();
        Initializator.RFIDinitSaleWithProducts(ez);
	    
        assertTrue(ez.endSaleTransaction(1));
        
        //persistencty test: endTransaction committed, check that product can't be reused in another sale after a loadAll()
        EZShopInterface ez2 = new it.polito.ezshop.data.EZShop();
        Initializator.initUser(ez2);
        Integer saleid = Initializator.initSaleBase(ez2);
        
        assertFalse(ez2.addProductToSaleRFID(saleid, "000000001000"));
       
	}
	
	@Test
	public void testDeleteSaleTransactionCompatibilityRFID() throws UnauthorizedException, InvalidTransactionIdException, InvalidRFIDException, InvalidQuantityException{
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();
        ez.reset();
        Initializator.RFIDinitSaleWithProducts(ez);
	    
        assertTrue(ez.endSaleTransaction(1));
        
        assertTrue(ez.deleteSaleTransaction(1));
        //rollback: RFID can be reused in a sale
        Integer saleid = Initializator.initSaleBase(ez);
        assertTrue(ez.addProductToSaleRFID(saleid, "000000001000"));
        
        //persistencty test: endTransaction committed and then deleted, check that product can be reused in a sale after a loadAll()
        ez.reset();
        Initializator.RFIDinitSaleWithProducts(ez);
        assertTrue(ez.endSaleTransaction(1));
        assertTrue(ez.deleteSaleTransaction(1));
        
        EZShopInterface ez2 = new it.polito.ezshop.data.EZShop();
        Initializator.initUser(ez2);
        saleid = Initializator.initSaleBase(ez2);
        
        assertTrue(ez2.addProductToSaleRFID(saleid, "000000001000"));
       
	}
	
	/*** ***/
	
	/*** test returnProductRFID ***/
	@Test
	public void testReturnProductRFIDUnauthorized(){
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();
        ez.reset();
        
        assertThrows(UnauthorizedException.class, ()-> ez.returnProductRFID(1, "000000001001"));
        
	}
	@Test
	public void testReturnProductRFIDInvalidTransactionId(){
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();
        ez.reset();
        Initializator.initUser(ez);
        
        assertThrows(InvalidTransactionIdException.class, ()-> ez.returnProductRFID(0, "000000001001"));
        assertThrows(InvalidTransactionIdException.class, ()-> ez.returnProductRFID(-5, "000000001001"));
        assertThrows(InvalidTransactionIdException.class, ()-> ez.returnProductRFID(null, "000000001001"));
	}
	
	@Test
	public void testReturnProductRFIDNonExistentReturn() throws UnauthorizedException, InvalidTransactionIdException, InvalidRFIDException{
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();
        ez.reset();
        Initializator.initReturnWithProducts(ez);
        
        assertFalse(ez.returnProductRFID(7, "000000001001"));
     	
	}
	@Test
	public void testReturnProductRFIDNonExistentProduct() throws UnauthorizedException, InvalidTransactionIdException, InvalidRFIDException{
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();
        ez.reset();
        Initializator.initReturnWithProducts(ez);
        
        assertFalse(ez.returnProductRFID(1, "000000003001"));
     	
	}
	@Test
	public void testReturnProductRFIDInvalidRFID(){
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();
        ez.reset();
        Initializator.initReturnWithProducts(ez);
        
        assertThrows(InvalidRFIDException.class, ()-> ez.returnProductRFID(1, ""));
        assertThrows(InvalidRFIDException.class, ()-> ez.returnProductRFID(1, null));
        assertThrows(InvalidRFIDException.class, ()-> ez.returnProductRFID(1, "00000010000001"));
        assertThrows(InvalidRFIDException.class, ()-> ez.returnProductRFID(1, "001"));
        assertThrows(InvalidRFIDException.class, ()-> ez.returnProductRFID(1, "1asdasdas1"));
	}
	@Test
	public void InitializatorTest() throws UnauthorizedException, InvalidTransactionIdException, InvalidQuantityException, InvalidRFIDException{
		//test that the Initializator RFID functions and addProductToSaleRFID successful don't create errors
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();
        ez.reset();
        
        Initializator.RFIDinitProducts(ez);
        Initializator.initSaleBase(ez);
        assertTrue(ez.addProductToSaleRFID(1, "000000001001"));
        
        try {
            ez.reset();
            Initializator.RFIDinitSaleWithProducts(ez);
            
            ez.reset();
            Initializator.RFIDinitSaleWithProductsPayed(ez);
            
            ez.reset();
            Initializator.RFIDinitReturnWithProducts(ez);
            }catch(Exception e) {
            	assertFalse(true);
            	e.printStackTrace();
         }
        
	}
	
	@Test
	public void testReturnProductRFIDProductNotInSale() throws UnauthorizedException, InvalidTransactionIdException, InvalidRFIDException{
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();
        ez.reset();
        Initializator.RFIDinitSaleWithProductsPayed(ez);;
        Initializator.initReturnBase(ez);
        
        assertFalse(ez.returnProductRFID(1, "000000001006"));
        assertFalse(ez.returnProductRFID(1, "000000002006"));
        
     	
	}
	@Test
	public void testReturnProductRFIDSuccessful() throws UnauthorizedException, InvalidTransactionIdException, InvalidRFIDException{
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();
	    ez.reset();
	    Initializator.RFIDinitSaleWithProductsPayed(ez);
	    Initializator.initReturnBase(ez);
	        
        assertTrue(ez.returnProductRFID(1, "000000001000"));
        assertTrue(ez.returnProductRFID(1, "000000001001"));
        assertTrue(ez.returnProductRFID(1, "000000002000"));
        assertTrue(ez.returnProductRFID(1, "000000002001"));

	}
	@Test
	public void testReturnProductRFIDCantAddTwice() throws UnauthorizedException, InvalidTransactionIdException, InvalidRFIDException{
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();
        ez.reset();
        Initializator.RFIDinitSaleWithProductsPayed(ez);;
        Initializator.initReturnBase(ez);
	        
        assertTrue(ez.returnProductRFID(1, "000000001000"));
        assertFalse(ez.returnProductRFID(1, "000000001000"));
	}
	@Test
	public void testEndReturnTransactionCompatibilityRFID() throws UnauthorizedException, InvalidTransactionIdException, InvalidRFIDException{
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();
        ez.reset();
        Initializator.RFIDinitReturnWithProducts(ez);
	    
        assertTrue(ez.endReturnTransaction(1, false));
        //rollback: RFID can be reused in a return
        Initializator.initReturnBase(ez,1);
        assertTrue(ez.returnProductRFID(2, "000000001000"));
        
        //persistencty test: endTransaction committed, check that product can't be reused in a return
        ez.reset();
        Initializator.RFIDinitReturnWithProducts(ez);
        assertTrue(ez.endReturnTransaction(1, true));
        
        EZShopInterface ez2 = new it.polito.ezshop.data.EZShop();
        Initializator.initUser(ez2);
        Integer returnId = Initializator.initReturnBase(ez2, 1);
        
        assertFalse(ez2.returnProductRFID(returnId, "000000001000"));
       
	}
	@Test
	public void testDeleteReturnTransactionCompatibilityRFID() throws UnauthorizedException, InvalidTransactionIdException, InvalidRFIDException{
		EZShopInterface ez = new it.polito.ezshop.data.EZShop();
        ez.reset();
        Initializator.RFIDinitReturnWithProducts(ez);
	    
        //persistency done
        assertTrue(ez.endReturnTransaction(1, true));
        
        //persistency rollbacked
        
        assertTrue(ez.deleteReturnTransaction(1));
        
        //persistencty test: check that product can be reused in a return
        EZShopInterface ez2 = new it.polito.ezshop.data.EZShop();
        Initializator.initUser(ez2);
        Integer returnId = Initializator.initReturnBase(ez2, 1);
        assertTrue(ez2.returnProductRFID(returnId, "000000001000"));
        
	}
		
	/*** ***/
	
	/*** test deleteProductFromSaleRFID ***/
	@Test
	public void testDeleteProductFromSaleRFIDInvalidTransactionId() {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
        ezShop.reset();
        
        assertThrows(it.polito.ezshop.exceptions.InvalidTransactionIdException.class, () -> {
        	ezShop.deleteProductFromSaleRFID(null, "1000000000");
        });
		
        assertThrows(it.polito.ezshop.exceptions.InvalidTransactionIdException.class, () -> {
        	ezShop.deleteProductFromSaleRFID(-10, "1000000000");
        });
        
        assertThrows(it.polito.ezshop.exceptions.InvalidTransactionIdException.class, () -> {
        	ezShop.deleteProductFromSaleRFID(0, "1000000000");
        });	
	}
	
	@Test
	public void testDeleteProductFromSaleRFIDInvalidRFID() {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
        ezShop.reset();
        
        assertThrows(it.polito.ezshop.exceptions.InvalidRFIDException.class, () -> {
        	ezShop.deleteProductFromSaleRFID(3, null);
        });
        
        assertThrows(it.polito.ezshop.exceptions.InvalidRFIDException.class, () -> {
        	ezShop.deleteProductFromSaleRFID(3, "");
        });
        
        assertThrows(it.polito.ezshop.exceptions.InvalidRFIDException.class, () -> {
        	ezShop.deleteProductFromSaleRFID(3, "100a00b!$0");
        });
        
        assertThrows(it.polito.ezshop.exceptions.InvalidRFIDException.class, () -> {
        	ezShop.deleteProductFromSaleRFID(3, "900000000");
        });
	}
	
	@Test
	public void testDeleteProductFromSaleRFIDUnauthorized() {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
        ezShop.reset();
        
        assertThrows(it.polito.ezshop.exceptions.UnauthorizedException.class, () -> {
        	ezShop.deleteProductFromSaleRFID(3, "100000000000");
        });
        
        assertThrows(it.polito.ezshop.exceptions.UnauthorizedException.class, () -> {
        	ezShop.login("notExistingUser", "dontcare");
        	ezShop.deleteProductFromSaleRFID(3, "100000000000");
        });
	}
	
	@Test
	public void testDeleteProductFromSaleRFIDNotExistingProduct() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidTransactionIdException, InvalidRFIDException, InvalidQuantityException, UnauthorizedException, InvalidProductCodeException, InvalidProductDescriptionException, InvalidPricePerUnitException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
        ezShop.reset();
        Initializator.RFIDinitSaleWithProducts(ezShop);
        
        assertFalse(ezShop.deleteProductFromSaleRFID(1, "000000001006"));
	}
	
	@Test
	public void testDeleteProductFromSaleRFIDInvalidTransaction() throws InvalidTransactionIdException, InvalidRFIDException, InvalidQuantityException, UnauthorizedException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
        ezShop.reset();
        
        Initializator.RFIDinitSaleWithProducts(ezShop);
        
        assertFalse(ezShop.deleteProductFromSaleRFID(2, "000000001005"));        
	}
	
	@Test
	public void testDeleteProductFromSaleRFIDSuccessful() throws InvalidTransactionIdException, InvalidRFIDException, InvalidQuantityException, UnauthorizedException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
        ezShop.reset();
        
        Initializator.RFIDinitSaleWithProducts(ezShop);
        
        ProductImpl product = ((EZShop) ezShop).getProductByRFID("000000001005");
        ProductTypeImpl productType = ((EZShop) ezShop).getProductTypeById(product.getProductTypeId());
        Integer previousQty = productType.getQuantity();

        assertTrue(ezShop.deleteProductFromSaleRFID(1, "000000001005")); 

        product = ((EZShop) ezShop).getProductByRFID("000000001005");
        productType = ((EZShop) ezShop).getProductTypeById(product.getProductTypeId());
        Integer followingQty = productType.getQuantity();
        
        assertEquals(previousQty + 1, followingQty);
        assertEquals(-1, product.getTransactionId());
	}
	
	/*** ***/


}
