package it.polito.ezshop.integrationTests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidProductDescriptionException;
import it.polito.ezshop.exceptions.InvalidProductIdException;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import it.polito.ezshop.exceptions.UnauthorizedException;
import it.polito.ezshop.exceptions.InvalidLocationException;

public class TestProductType {
	   
	
		// --------------- createProductType --------------- //
	
		@Test
		public void testCreateProductTypeInvalidDescription() {
	        EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
	        
	        ezShop.reset();
	        
	        assertThrows(it.polito.ezshop.exceptions.InvalidProductDescriptionException.class, () -> {
	        	ezShop.createProductType(null, "8057014050035", 200.0, "DESERT RAT BLUSH");
	        });
	        
	        assertThrows(it.polito.ezshop.exceptions.InvalidProductDescriptionException.class, () -> {
	        	ezShop.createProductType("", "8057014050035", 200.0, "DESERT RAT BLUSH");
	        });
		}
		
		
		@Test 
		public void testCreateProductTypeInvalidBarCode() {
	        EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
	        
	        ezShop.reset();
	        
	        assertThrows(it.polito.ezshop.exceptions.InvalidProductCodeException.class, () ->{
	            ezShop.createProductType("YEEZY 500", null, 200.0, "DESERT RAT BLUSH");
	        });
	        
	        assertThrows(it.polito.ezshop.exceptions.InvalidProductCodeException.class, () ->{
	            ezShop.createProductType("YEEZY 500", "", 200.0, "DESERT RAT BLUSH");
	        });

	        assertThrows(it.polito.ezshop.exceptions.InvalidProductCodeException.class, () ->{
	            ezShop.createProductType("YEEZY 500", "1234567891011", 200.0, "DESERT RAT BLUSH");
	        });
	        
	        assertThrows(it.polito.ezshop.exceptions.InvalidProductCodeException.class, () ->{
	            ezShop.createProductType("YEEZY 500", "1A2B3c4d6F7G8", 200.0, "DESERT RAT BLUSH");
	        });
	        	        
		}
		
		
		@Test
		public void testCreateProductTypeInvalidPrice() {
	        EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();

	        ezShop.reset();
	        assertThrows(it.polito.ezshop.exceptions.InvalidPricePerUnitException.class, ()->{
	        	ezShop.createProductType("YEEZY 500", "8057014050035", -200.0, "DESERT RAT BLUSH");
	        });
	        
	        assertThrows(it.polito.ezshop.exceptions.InvalidPricePerUnitException.class, ()->{
	        	ezShop.createProductType("YEEZY 500", "8057014050035", -0.00001, "DESERT RAT BLUSH");
	        });
	        
	        assertThrows(it.polito.ezshop.exceptions.InvalidPricePerUnitException.class, ()->{
	        	ezShop.createProductType("YEEZY 500", "8057014050035", 0, "DESERT RAT BLUSH");
	        });
		}
		
		
		@Test 
		public void testCreateProductTypeInvalidUser() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
	        EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
	        
	        ezShop.reset();
	        assertThrows(it.polito.ezshop.exceptions.UnauthorizedException.class, ()->{
	        	ezShop.createProductType("YEEZY 500", "8057014050035", 200.0, "DESERT RAT BLUSH");
	        });
	        
	        ezShop.reset();
	        ezShop.createUser("yeezy", "iamagod", "Cashier");
        	ezShop.login("yeezy", "iamagod");
	        assertThrows(it.polito.ezshop.exceptions.UnauthorizedException.class, ()->{
	        	ezShop.createProductType("YEEZY 500", "8057014050035", 200.0, "DESERT RAT BLUSH");
	        });
	        
		}
		
		
		@Test
		public void testCreateProductTypeExistingBarCode() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
	        EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
	        
	        ezShop.reset();
	        ezShop.createUser("airjordan", "chicagobulls", "Administrator");
	        ezShop.login("airjordan", "chicagobulls");
	        ezShop.createProductType("YEEZY 500", "8057014050035", 200.0, "DESERT RAT BLUSH");
	        
	        assertEquals(-1, ezShop.createProductType("Air Jordan 1", "8057014050035", 119.99, "Air Jordan 1 Mid"));
		}
		
		//TODO: error in savings (how can i simulate this?)
		
		@Test
		public void testCreateProductTypeSuccessful() throws InvalidUsernameException, InvalidPasswordException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidRoleException {
			EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
			
			ezShop.reset(); 
	        ezShop.createUser("airjordan", "chicagobulls", "Administrator");
			ezShop.login("airjordan", "chicagobulls");
	        
	        assertEquals(1, ezShop.createProductType("YEEZY 500", "8057014050035", 200.0, "DESERT RAT BLUSH"));
	        assertEquals(2, ezShop.createProductType("Air Jordan 1", "9788838663260", 119.99, "Air Jordan 1 Mid"));
		}
		
	    // --------------- END --------------- //

		
	    // --------------- updateProduct --------------- //

			
		
		@Test
		public void testUpdateProductInvalidId() throws InvalidUsernameException, InvalidPasswordException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidRoleException {
			EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
			
			ezShop.reset();
	        ezShop.createUser("airjordan", "chicagobulls", "Administrator");
	        ezShop.login("airjordan", "chicagobulls");
	        ezShop.createProductType("YEEZY 500", "8057014050035", 200.0, "DESERT RAT BLUSH");
	        
	        assertThrows(it.polito.ezshop.exceptions.InvalidProductIdException.class, ()->{
	        	ezShop.updateProduct(null, "Air Jordan 1", "9788838663260", 119.99, "Air Jordan 1 Mid");
	        });
	        
	        assertThrows(it.polito.ezshop.exceptions.InvalidProductIdException.class, ()->{
	        	ezShop.updateProduct(-63, "Air Jordan 1", "9788838663260", 119.99, "Air Jordan 1 Mid");
	        });
	        
	        assertThrows(it.polito.ezshop.exceptions.InvalidProductIdException.class, ()->{
	        	ezShop.updateProduct(0, "Air Jordan 1", "9788838663260", 119.99, "Air Jordan 1 Mid");
	        });
	        
	        assertThrows(it.polito.ezshop.exceptions.InvalidProductIdException.class, ()->{
	        	ezShop.updateProduct(-1, "Air Jordan 1", "9788838663260", 119.99, "Air Jordan 1 Mid");
	        });        
	            
		}
		
		
		@Test
		public void testUpdateProductInvalidDescription() throws InvalidUsernameException, InvalidPasswordException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidRoleException {
			EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
			
			ezShop.reset();
	        ezShop.createUser("airjordan", "chicagobulls", "Administrator");
	        ezShop.login("airjordan", "chicagobulls");
	        ezShop.createProductType("YEEZY 500", "8057014050035", 200.0, "DESERT RAT BLUSH");
	        
	        assertThrows(it.polito.ezshop.exceptions.InvalidProductDescriptionException.class, ()->{
	        	ezShop.updateProduct(1, null, "9788838663260", 119.99, "Air Jordan 1 Mid");
	        });
	        
	        assertThrows(it.polito.ezshop.exceptions.InvalidProductDescriptionException.class, ()->{
	        	ezShop.updateProduct(1, "", "9788838663260", 119.99, "Air Jordan 1 Mid");
	        });
		}
		
		
		@Test
		public void testUpdateProductInvalidBarCode() throws InvalidUsernameException, InvalidPasswordException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidRoleException {
			EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
	        
			ezShop.reset();
	        ezShop.createUser("airjordan", "chicagobulls", "Administrator");
			ezShop.login("airjordan", "chicagobulls");
	        ezShop.createProductType("YEEZY 500", "8057014050035", 200.0, "DESERT RAT BLUSH");
	        
	        assertThrows(it.polito.ezshop.exceptions.InvalidProductCodeException.class, ()->{
	        	ezShop.updateProduct(1, "Air Jordan 1", null, 119.99, "Air Jordan 1 Mid");
	        });
	        
	        assertThrows(it.polito.ezshop.exceptions.InvalidProductCodeException.class, ()->{
	        	ezShop.updateProduct(1, "Air Jordan 1", "", 119.99, "Air Jordan 1 Mid");
	        });
	        
	        assertThrows(it.polito.ezshop.exceptions.InvalidProductCodeException.class, ()->{
	        	ezShop.updateProduct(1, "Air Jordan 1", "1234567891011", 119.99, "Air Jordan 1 Mid");
	        });
	        assertThrows(it.polito.ezshop.exceptions.InvalidProductCodeException.class, ()->{
	        	ezShop.updateProduct(1, "Air Jordan 1", "1A2B3c4d6F7G8", 119.99, "Air Jordan 1 Mid");
	        });
	        
		}
		
		
		@Test
		public void testUpdateProductInvalidPrice() throws InvalidPasswordException, InvalidPasswordException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidUsernameException, InvalidRoleException {
			EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
			
			ezShop.reset();
	        ezShop.createUser("airjordan", "chicagobulls", "Administrator");
			ezShop.login("airjordan", "chicagobulls");
	        ezShop.createProductType("YEEZY 500", "8057014050035", 200.0, "DESERT RAT BLUSH");
	        
	        assertThrows(it.polito.ezshop.exceptions.InvalidPricePerUnitException.class, ()->{
	        	ezShop.updateProduct(1, "Air Jordan 1", "9788838663260", -119.99, "Air Jordan 1 Mid");
	        });
	        
	        assertThrows(it.polito.ezshop.exceptions.InvalidPricePerUnitException.class, ()->{
	        	ezShop.updateProduct(1, "Air Jordan 1", "9788838663260", -0.00001, "Air Jordan 1 Mid");
	        });
	        
	        assertThrows(it.polito.ezshop.exceptions.InvalidPricePerUnitException.class, ()->{
	        	ezShop.updateProduct(1, "Air Jordan 1", "9788838663260", 0, "Air Jordan 1 Mid");
	        });
		}
		
		
		@Test
		public void testUpdateProductInvalidUser() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
			EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
	        
			ezShop.reset();
			assertThrows(it.polito.ezshop.exceptions.UnauthorizedException.class, ()->{
	        	ezShop.updateProduct(1, "Air Jordan 1", "9788838663260", 119.99, "Air Jordan 1 Mid");
	        });
			
			ezShop.reset();
			ezShop.createUser("yeezy", "iamagod", "Cashier");
			ezShop.login("yeezy", "iamagod");
			
	        assertThrows(it.polito.ezshop.exceptions.UnauthorizedException.class, ()->{      	
	        	ezShop.updateProduct(1, "Air Jordan 1", "9788838663260", 119.99, "Air Jordan 1 Mid");
	        });       	
		} 
		
		
		
		@Test 
		public void testUpdateProductUnexistingId() throws InvalidUsernameException, InvalidPasswordException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidProductIdException, InvalidRoleException {
			EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
			
			ezShop.reset();
	        ezShop.createUser("airjordan", "chicagobulls", "Administrator");
	        ezShop.login("airjordan", "chicagobulls");
	        ezShop.createProductType("YEEZY 500", "8057014050035", 200.0, "DESERT RAT BLUSH");
	        
	        assertFalse(ezShop.updateProduct(2, "Air Jordan 1", "9788838663260", 119.99, "Air Jordan 1 Mid"));
		}
		
		
		@Test
		public void testUpdateProductExistingBarCode() throws InvalidUsernameException, InvalidPasswordException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidProductIdException, InvalidRoleException {
			EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
			
			ezShop.reset();
	        ezShop.createUser("airjordan", "chicagobulls", "Administrator");
			ezShop.login("airjordan", "chicagobulls");
	        ezShop.createProductType("YEEZY 500", "8057014050035", 200.0, "DESERT RAT BLUSH");
	        ezShop.createProductType("Nike Air Max Plus", "9788838663260", 169.99, "");
	        
	        assertFalse(ezShop.updateProduct(1, "Air Jordan 1", "9788838663260", 119.99, "Air Jordan 1 Mid"));		
		}
		
		
		@Test
		public void testUpdateProductSuccessful() throws InvalidUsernameException, InvalidPasswordException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidProductIdException, InvalidRoleException {
			EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
	        
			ezShop.reset();
	        ezShop.createUser("airjordan", "chicagobulls", "Administrator");
			ezShop.login("airjordan", "chicagobulls");
	        ezShop.createProductType("YEEZY 500", "8057014050035", 200.0, "DESERT RAT BLUSH");
	        ezShop.createProductType("Nike Air Max Plus", "9788838663260", 169.99, "");
	        
	        assertTrue(ezShop.updateProduct(1, "Air Jordan 1", "9788891904553", 119.99, "Air Jordan 1 Mid"));		
		}
		
		
		
		// --------------- END --------------- //

		
	    // --------------- deleteProductType --------------- //
		
		
		@Test
		public void testDeleteProductTypeInvalidId() throws InvalidUsernameException, InvalidPasswordException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidRoleException {
			EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
			
			ezShop.reset();
	        ezShop.createUser("airjordan", "chicagobulls", "Administrator");
			ezShop.login("airjordan", "chicagobulls");
	        ezShop.createProductType("YEEZY 500", "8057014050035", 200.0, "DESERT RAT BLUSH");
	        
	        assertThrows(it.polito.ezshop.exceptions.InvalidProductIdException.class, () ->{
	        	ezShop.deleteProductType(null);
	        });
	        
	        assertThrows(it.polito.ezshop.exceptions.InvalidProductIdException.class, () ->{
	        	ezShop.deleteProductType(-10);
	        });
	        
	        assertThrows(it.polito.ezshop.exceptions.InvalidProductIdException.class, () ->{
	        	ezShop.deleteProductType(0);
	        });            
		}
		
		@Test
		public void testDeleteProductTypeInvalidUser() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
			EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
			
			ezShop.reset();
	        assertThrows(it.polito.ezshop.exceptions.UnauthorizedException.class, ()->{
				ezShop.deleteProductType(1);
			});
			
	        ezShop.reset();
	        ezShop.createUser("yeezy", "iamagod", "Cashier");
			ezShop.login("yeezy", "iamagod"); 

			assertThrows(it.polito.ezshop.exceptions.UnauthorizedException.class, ()->{
				ezShop.deleteProductType(1);
			});
		}
		
		
		//TODO: DB problems
		
		
		@Test
		public void testDeleteProductTypeSuccessful() throws InvalidUsernameException, InvalidPasswordException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidProductIdException, InvalidRoleException {
			EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
			
			ezShop.reset();
	        ezShop.createUser("airjordan", "chicagobulls", "Administrator");
			ezShop.login("airjordan", "chicagobulls");
	        ezShop.createProductType("YEEZY 500", "8057014050035", 200.0, "DESERT RAT BLUSH");
	        ezShop.createProductType("Nike Air Max Plus", "9788838663260", 169.99, "");

	        assertTrue(ezShop.deleteProductType(2));
		}
		
		// --------------- END --------------- //
		
		
	    // --------------- getAllProductTypes --------------- //
		
		@Test
		public void testGetAllProductTypesInvalidUser() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
			EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
			
			ezShop.reset();
			assertThrows(it.polito.ezshop.exceptions.UnauthorizedException.class, ()->{
				ezShop.getAllProductTypes();
			});
			
			ezShop.reset();
	        ezShop.createUser("yeezy", "iamagod", "Cashier");
			ezShop.login("yeezy", "iamagod"); 
			assertThrows(it.polito.ezshop.exceptions.UnauthorizedException.class, ()->{
				ezShop.getAllProductTypes();
			});
		}
		
		@Test
		public void testGetAllProductTypesSuccessful() throws InvalidUsernameException, InvalidPasswordException, UnauthorizedException, InvalidRoleException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException {
			EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		
			ezShop.reset();
	        ezShop.createUser("airjordan", "chicagobulls", "Administrator");
			ezShop.login("airjordan", "chicagobulls");
			ezShop.createProductType("YEEZY 500", "8057014050035", 200.0, "DESERT RAT BLUSH");
			ezShop.createProductType("Nike Air Max Plus", "9788838663260", 169.99, "");

			assertNotNull(ezShop.getAllProductTypes());
			assertEquals(2, ezShop.getAllProductTypes().size());
		}
		
		// --------------- END --------------- //

		
		
	    // --------------- getProductTypeByBarCode --------------- //
		
		@Test
		public void testGetProductTypeByBarCodeInvalidCode() throws InvalidUsernameException, InvalidPasswordException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidRoleException {
			EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
			
			ezShop.reset();
	        ezShop.createUser("airjordan", "chicagobulls", "Administrator");
			ezShop.login("airjordan", "chicagobulls");
	        ezShop.createProductType("YEEZY 500", "8057014050035", 200.0, "DESERT RAT BLUSH");
	        
	        assertThrows(it.polito.ezshop.exceptions.InvalidProductCodeException.class, ()->{
	        	ezShop.getProductTypeByBarCode(null);
	        });
	        
	        assertThrows(it.polito.ezshop.exceptions.InvalidProductCodeException.class, ()->{
	        	ezShop.getProductTypeByBarCode("");
	        });
	        
	        assertThrows(it.polito.ezshop.exceptions.InvalidProductCodeException.class, ()->{
	        	ezShop.getProductTypeByBarCode("1234567891011");
	        });
	        
	        assertThrows(it.polito.ezshop.exceptions.InvalidProductCodeException.class, ()->{
	        	ezShop.getProductTypeByBarCode("1A2B3c4d5F%6G");
	        });
		}
		
		
		@Test
		public void testGetProductTypeByBarCodeInvalidUser() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
			EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
			
			ezShop.reset();
	        assertThrows(it.polito.ezshop.exceptions.UnauthorizedException.class, ()->{
				ezShop.getProductTypeByBarCode("8057014050035");
			});

	        ezShop.reset();
	        ezShop.createUser("yeezy", "iamagod", "Cashier");
			ezShop.login("yeezy", "iamagod"); 
			assertThrows(it.polito.ezshop.exceptions.UnauthorizedException.class, ()->{
				ezShop.getProductTypeByBarCode("8057014050035");
			});
			
		}
		
		
		@Test
		public void testGetProductTypeByBarCodeSuccessful() throws InvalidUsernameException, InvalidPasswordException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidRoleException {
			EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
			
			ezShop.reset();
	        ezShop.createUser("airjordan", "chicagobulls", "Administrator");
			ezShop.login("airjordan", "chicagobulls");
	        ezShop.createProductType("YEEZY 500", "8057014050035", 200.0, "DESERT RAT BLUSH");
	        ezShop.createProductType("Nike Air Max Plus", "9788838663260", 169.99, "");

	        assertNotNull(ezShop.getProductTypeByBarCode("8057014050035"));
	        assertNull(ezShop.getProductTypeByBarCode("9788891904553"));
	        
		}
		
		// --------------- END --------------- //

		
		
	    // --------------- getProductTypeByDescription --------------- //
		
		@Test
		public void testGetProductTypesByDescriptionInvalidUser() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
			EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
			
			ezShop.reset();
		    assertThrows(it.polito.ezshop.exceptions.UnauthorizedException.class, ()->{
				ezShop.getProductTypesByDescription("air");
			});
			
		    ezShop.reset();
	        ezShop.createUser("yeezy", "iamagod", "Cashier");
			ezShop.login("yeezy", "iamagod"); 
			assertThrows(it.polito.ezshop.exceptions.UnauthorizedException.class, ()->{
				ezShop.getProductTypesByDescription("air");
			});
		}
		
		@Test
		public void testGetProductTypesByDescriptionSuccessful() throws InvalidUsernameException, InvalidPasswordException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidRoleException {
			EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
			
			ezShop.reset();
	        ezShop.createUser("airjordan", "chicagobulls", "Administrator");
			ezShop.login("airjordan", "chicagobulls");
	        ezShop.createProductType("YEEZY 500", "8057014050035", 200.0, "DESERT RAT BLUSH");
	        ezShop.createProductType("Nike Air Max Plus", "9788838663260", 169.99, "");
	        
	        assertNotNull(ezShop.getProductTypesByDescription("Air"));
	        assertEquals(1, ezShop.getProductTypesByDescription("Air").size());
	        
	        assertNotNull(ezShop.getProductTypesByDescription(""));
	        assertEquals(2, ezShop.getProductTypesByDescription("").size());
	        
	        assertNotNull(ezShop.getProductTypesByDescription(null));
	        assertEquals(2, ezShop.getProductTypesByDescription(null).size());
	        
		}	
		
		// --------------- END --------------- //

		
		
	    // --------------- updateQuantity --------------- //
		
		@Test
		public void testUpdateQuantityInvalidUser() throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
			EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
			
			ezShop.reset();
	        
	        assertThrows(it.polito.ezshop.exceptions.UnauthorizedException.class, ()->{
				ezShop.updateQuantity(1, 20);
			});
	        
			ezShop.reset();
	        ezShop.createUser("yeezy", "iamagod", "Cashier");
			ezShop.login("yeezy", "iamagod"); 
			assertThrows(it.polito.ezshop.exceptions.UnauthorizedException.class, ()->{
				ezShop.updateQuantity(1, 20);
			});
		}
		
		@Test
		public void testUpdateQuantityInvalidProductId() throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
			EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
			
			ezShop.reset();
	        ezShop.createUser("airjordan", "chicagobulls", "Administrator");
			ezShop.login("airjordan", "chicagobulls");
	        ezShop.createProductType("YEEZY 500", "8057014050035", 200.0, "DESERT RAT BLUSH");
	        
	        assertThrows(it.polito.ezshop.exceptions.InvalidProductIdException.class, ()->{
	        	ezShop.updateQuantity(null, 20);
	        });
	        
	        assertThrows(it.polito.ezshop.exceptions.InvalidProductIdException.class, ()->{
	        	ezShop.updateQuantity(0, 20);
	        });
	        
	        assertThrows(it.polito.ezshop.exceptions.InvalidProductIdException.class, ()->{
	        	ezShop.updateQuantity(-1, 20);
	        });
		}
		
		@Test
		public void testUpdateQuantitySuccessful() throws InvalidLocationException, InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
			EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
			
			ezShop.reset();
	        ezShop.createUser("airjordan", "chicagobulls", "Administrator");
			ezShop.login("airjordan", "chicagobulls");
	        ezShop.createProductType("YEEZY 500", "8057014050035", 200.0, "DESERT RAT BLUSH");
	        
	        assertFalse(ezShop.updateQuantity(1, 20));
	        
	        ezShop.updatePosition(1, "1-a-1");
	        
	        assertTrue(ezShop.updateQuantity(1, 20));
	        assertFalse(ezShop.updateQuantity(1, -30));
		}
		
		// --------------- END --------------- //

		
		
	    // --------------- updatePosition --------------- //
		
		@Test
		public void testUpdatePositionInvalidUser() throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
			EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
			
			ezShop.reset();
	        
	        assertThrows(it.polito.ezshop.exceptions.UnauthorizedException.class, ()->{
				ezShop.updatePosition(1, "");
			});
	        
			ezShop.reset();
	        ezShop.createUser("yeezy", "iamagod", "Cashier");
			ezShop.login("yeezy", "iamagod"); 
			assertThrows(it.polito.ezshop.exceptions.UnauthorizedException.class, ()->{
				ezShop.updatePosition(1, "");
			});
		}
		
		@Test
		public void testUpdatePositionInvalidProductId() throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
			EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
			
			ezShop.reset();
	        ezShop.createUser("airjordan", "chicagobulls", "Administrator");
			ezShop.login("airjordan", "chicagobulls");
	        ezShop.createProductType("YEEZY 500", "8057014050035", 200.0, "DESERT RAT BLUSH");
	        
	        assertThrows(it.polito.ezshop.exceptions.InvalidProductIdException.class, ()->{
	        	ezShop.updatePosition(null, "");
	        });
	        
	        assertThrows(it.polito.ezshop.exceptions.InvalidProductIdException.class, ()->{
	        	ezShop.updatePosition(0, "");
	        });
	        
	        assertThrows(it.polito.ezshop.exceptions.InvalidProductIdException.class, ()->{
	        	ezShop.updatePosition(-1, "");
	        });
		}
		
		@Test
		public void testUpdatePositionInvalidPosition() throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
			EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
			
			ezShop.reset();
	        ezShop.createUser("airjordan", "chicagobulls", "Administrator");
			ezShop.login("airjordan", "chicagobulls");
	        ezShop.createProductType("YEEZY 500", "8057014050035", 200.0, "DESERT RAT BLUSH");
	        
	        assertThrows(it.polito.ezshop.exceptions.InvalidLocationException.class, ()->{
	        	ezShop.updatePosition(1, "ak12");
	        });
	        
	        assertThrows(it.polito.ezshop.exceptions.InvalidLocationException.class, ()->{
	        	ezShop.updatePosition(1, "1-a2");
	        });
	        
		}
		
		@Test
		public void testUpdatePositionSuccessful() throws InvalidLocationException, InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
			EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
			
			ezShop.reset();
	        ezShop.createUser("airjordan", "chicagobulls", "Administrator");
			ezShop.login("airjordan", "chicagobulls");
	        ezShop.createProductType("YEEZY 500", "8057014050035", 200.0, "DESERT RAT BLUSH");
	        
	        assertTrue(ezShop.updatePosition(1, "1-a-1"));
	        assertFalse(ezShop.updatePosition(1, "1-a-1"));
	        assertTrue(ezShop.updatePosition(1, "2-b-1"));
		}
		
}
