package it.polito.ezshop.integrationTests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidQuantityException;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidLocationException;
import it.polito.ezshop.exceptions.InvalidOrderIdException;
import it.polito.ezshop.exceptions.UnauthorizedException;
import it.polito.ezshop.exceptions.InvalidProductDescriptionException;
import it.polito.ezshop.exceptions.InvalidProductIdException;



public class TestOrders {
	
	// ----------- issueOrder ----------- //
	
	@Test
	public void testIssueOrderUnauthorized () throws UnauthorizedException, InvalidProductIdException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		
		ezShop.reset();
		ezShop.createUser("Jordan", "superwow", "Cashier");
		ezShop.createUser("mario", "a1b2c3", "ShopManager");
		ezShop.login("mario", "a1b2c3");
		ezShop.createProductType("YEEZY 500", "8057014050035", 200.0, "DESERT RAT BLUSH");
		ezShop.logout();
		assertThrows(it.polito.ezshop.exceptions.UnauthorizedException.class, ()->{
			 ezShop.issueOrder("8057014050035", 100, 150.0);
		});
		ezShop.login("Jordan", "superwow");
		assertThrows(it.polito.ezshop.exceptions.UnauthorizedException.class, ()->{
			 ezShop.issueOrder("8057014050035", 20, 15.0);
		});
	}
	
	@Test
	public void testIssueOrderInvalidProductCode () throws UnauthorizedException, InvalidProductIdException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		
		ezShop.reset();
		ezShop.createUser("mario", "a1b2c3", "ShopManager");
		ezShop.login("mario", "a1b2c3");
		ezShop.createProductType("YEEZY 500", "8057014050035", 200.0, "DESERT RAT BLUSH");
		assertThrows(it.polito.ezshop.exceptions.InvalidProductCodeException.class, ()->{
			 ezShop.issueOrder("", 100, 150.0);
		});
		assertThrows(it.polito.ezshop.exceptions.InvalidProductCodeException.class, ()->{
			 ezShop.issueOrder(null, 100, 150.0);
		});
		assertThrows(it.polito.ezshop.exceptions.InvalidProductCodeException.class, ()->{
			 ezShop.issueOrder("1234567891011", 20, 15.0);
		});
	}
	
	@Test
	public void testIssueOrderInvalidQuantity () throws UnauthorizedException, InvalidProductIdException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		
		ezShop.reset();
		ezShop.createUser("mario", "a1b2c3", "ShopManager");
		ezShop.login("mario", "a1b2c3");
		ezShop.createProductType("YEEZY 500", "8057014050035", 200.0, "DESERT RAT BLUSH");
		assertThrows(it.polito.ezshop.exceptions.InvalidQuantityException.class, ()->{
			 ezShop.issueOrder("8057014050035", 0, 15.0);
		});

	}
	
	@Test
	public void testIssueOrderInvalidPricePerUnit () throws UnauthorizedException, InvalidProductIdException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		
		ezShop.reset();
		ezShop.createUser("mario", "a1b2c3", "ShopManager");
		ezShop.login("mario", "a1b2c3");
		ezShop.createProductType("YEEZY 500", "8057014050035", 200.0, "DESERT RAT BLUSH");
		assertThrows(it.polito.ezshop.exceptions.InvalidPricePerUnitException.class, ()->{
			 ezShop.issueOrder("8057014050035", 20, 0);
		});

	}
	
	@Test
	public void testIssueOrderSuccessful () throws UnauthorizedException, InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, InvalidProductIdException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		
		ezShop.reset();
		ezShop.createUser("mario", "a1b2c3", "ShopManager");
		ezShop.login("mario", "a1b2c3");
		ezShop.createProductType("YEEZY 500", "8057014050035", 200.0, "DESERT RAT BLUSH");
		assertEquals(1, ezShop.issueOrder("8057014050035", 20, 15.0));
		assertEquals(-1, ezShop.issueOrder("9788838663260", 20, 15.0));
	}
	
	// ----------- END ----------- //
	
	
	// ----------- payOrderFor ----------- //
	
	@Test
	public void testPayOrderForUnauthorized () throws UnauthorizedException, InvalidProductIdException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		
		ezShop.reset();
		ezShop.createUser("mario", "a1b2c3", "Administrator");
		ezShop.createUser("Jordan", "superwow", "Cashier");
		ezShop.login("mario", "a1b2c3");
		ezShop.createProductType("YEEZY 500", "8057014050035", 200.0, "DESERT RAT BLUSH");
		ezShop.recordBalanceUpdate(1000);
		ezShop.logout();
		assertThrows(it.polito.ezshop.exceptions.UnauthorizedException.class, ()->{
			 ezShop.payOrderFor("8057014050035", 20, 15.0);
		});
		ezShop.login("Jordan", "superwow");
		assertThrows(it.polito.ezshop.exceptions.UnauthorizedException.class, ()->{
			 ezShop.payOrderFor("8057014050035", 20, 15.0);
		});
	}
	
	@Test
	public void testPayOrderForInvalidProductCode () throws UnauthorizedException, InvalidProductIdException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		
		ezShop.reset();
		ezShop.createUser("mario", "a1b2c3", "Administrator");
		ezShop.login("mario", "a1b2c3");
		ezShop.createProductType("YEEZY 500", "8057014050035", 200.0, "DESERT RAT BLUSH");
		ezShop.recordBalanceUpdate(1000);
		assertThrows(it.polito.ezshop.exceptions.InvalidProductCodeException.class, ()->{
			 ezShop.payOrderFor("", 20, 15.0);
		});
		assertThrows(it.polito.ezshop.exceptions.InvalidProductCodeException.class, ()->{
			 ezShop.payOrderFor(null, 20, 15.0);
		});
		assertThrows(it.polito.ezshop.exceptions.InvalidProductCodeException.class, ()->{
			 ezShop.payOrderFor("1234567891011", 20, 15.0);
		});
	}
	
	@Test
	public void testPayOrderForInvalidQuantity () throws UnauthorizedException, InvalidProductIdException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		
		ezShop.reset();
		ezShop.createUser("mario", "a1b2c3", "Administrator");
		ezShop.login("mario", "a1b2c3");
		ezShop.createProductType("YEEZY 500", "8057014050035", 200.0, "DESERT RAT BLUSH");
		ezShop.recordBalanceUpdate(1000);
		assertThrows(it.polito.ezshop.exceptions.InvalidQuantityException.class, ()->{
			 ezShop.payOrderFor("8057014050035", 0, 15.0);
		});
		assertThrows(it.polito.ezshop.exceptions.InvalidQuantityException.class, ()->{
			 ezShop.payOrderFor("8057014050035", -1, 15.0);
		});
	}
	
	@Test
	public void testPayOrderForInvalidPricePerUnit () throws UnauthorizedException, InvalidProductIdException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		
		ezShop.reset();
		ezShop.createUser("mario", "a1b2c3", "Administrator");
		ezShop.login("mario", "a1b2c3");
		ezShop.createProductType("YEEZY 500", "8057014050035", 200.0, "DESERT RAT BLUSH");
		ezShop.recordBalanceUpdate(1000);
		assertThrows(it.polito.ezshop.exceptions.InvalidPricePerUnitException.class, ()->{
			 ezShop.payOrderFor("8057014050035", 20, 0);
		});
		assertThrows(it.polito.ezshop.exceptions.InvalidPricePerUnitException.class, ()->{
			 ezShop.payOrderFor("8057014050035", 20, -1);
		});
	}
	
	@Test
	public void testPayOrderForSuccessful () throws UnauthorizedException, InvalidQuantityException, InvalidProductIdException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		
		ezShop.reset();
		ezShop.createUser("mario", "a1b2c3", "Administrator");
		ezShop.login("mario", "a1b2c3");
		ezShop.createProductType("YEEZY 500", "8057014050035", 200.0, "DESERT RAT BLUSH");
		ezShop.createProductType("Air Jordan 1", "9788838663260", 119.99, "Air Jordan 1 Mid");
		ezShop.recordBalanceUpdate(1000);
		assertEquals(1, ezShop.payOrderFor("8057014050035", 20, 15.0));
		assertEquals(2, ezShop.payOrderFor("9788838663260", 2, 10.0));
	}
	
	// ----------- END ----------- //
	
	
	// ----------- payOrder ----------- //
	
	@Test
	public void testPayOrderUnauthorized () throws UnauthorizedException, InvalidQuantityException, InvalidProductIdException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		
		ezShop.reset();
		ezShop.createUser("mario", "a1b2c3", "Administrator");
		ezShop.createUser("Jordan", "superwow", "Cashier");
		ezShop.login("mario", "a1b2c3");
		ezShop.createProductType("YEEZY 500", "8057014050035", 200.0, "DESERT RAT BLUSH");
		ezShop.recordBalanceUpdate(1000);
		ezShop.issueOrder("8057014050035", 20, 15.0);
		ezShop.logout();
		assertThrows(it.polito.ezshop.exceptions.UnauthorizedException.class, ()->{
			 ezShop.payOrder(1);
		});
		ezShop.login("Jordan", "superwow");
		assertThrows(it.polito.ezshop.exceptions.UnauthorizedException.class, ()->{
			 ezShop.payOrder(1);
		});
	}
	
	@Test
	public void testPayOrderInvalidOrderId () throws UnauthorizedException, InvalidQuantityException, InvalidProductIdException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		
		ezShop.reset();
		ezShop.createUser("mario", "a1b2c3", "Administrator");
		ezShop.login("mario", "a1b2c3");
		ezShop.createProductType("YEEZY 500", "8057014050035", 200.0, "DESERT RAT BLUSH");
		ezShop.recordBalanceUpdate(1000);
		ezShop.issueOrder("8057014050035", 20, 15.0);
		assertThrows(it.polito.ezshop.exceptions.InvalidOrderIdException.class, ()->{
			 ezShop.payOrder(0);
		});
		assertThrows(it.polito.ezshop.exceptions.InvalidOrderIdException.class, ()->{
			 ezShop.payOrder(-1);
		});
		assertThrows(it.polito.ezshop.exceptions.InvalidOrderIdException.class, ()->{
			 ezShop.payOrder(null);
		});
	}
	
	@Test
	public void testPayOrderSuccessful () throws UnauthorizedException, InvalidOrderIdException, InvalidQuantityException, InvalidProductIdException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		
		ezShop.reset();
		ezShop.createUser("mario", "a1b2c3", "Administrator");
		ezShop.createUser("Jordan", "superwow", "Cashier");
		ezShop.login("mario", "a1b2c3");
		ezShop.createProductType("YEEZY 500", "8057014050035", 200.0, "DESERT RAT BLUSH");
		ezShop.recordBalanceUpdate(1000);
		ezShop.issueOrder("8057014050035", 20, 15.0);
		ezShop.issueOrder("8057014050035", 200, 150.0);
		assertTrue(ezShop.payOrder(1));
		assertTrue(ezShop.payOrder(1));
		assertFalse(ezShop.payOrder(2));
	}
	
	// ----------- END ----------- //
	
	
	// ----------- recordOrderArrival ----------- //
	
	@Test
	public void testRecordOrderArrivalUnauthorized () throws UnauthorizedException, InvalidLocationException, InvalidQuantityException, InvalidProductIdException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		
		ezShop.reset();
		ezShop.createUser("mario", "a1b2c3", "Administrator");
		ezShop.createUser("Jordan", "superwow", "Cashier");
		ezShop.login("mario", "a1b2c3");
		ezShop.createProductType("YEEZY 500", "8057014050035", 200.0, "DESERT RAT BLUSH");
		ezShop.recordBalanceUpdate(1000);
		ezShop.payOrderFor("8057014050035", 20, 15.0);
		ezShop.updatePosition(1, "1-a-1");
		ezShop.logout();
		assertThrows(it.polito.ezshop.exceptions.UnauthorizedException.class, ()->{
			 ezShop.recordOrderArrival(1);
		});
		ezShop.login("Jordan", "superwow");
		assertThrows(it.polito.ezshop.exceptions.UnauthorizedException.class, ()->{
			 ezShop.recordOrderArrival(1);
		});
	}
	
	@Test
	public void testRecordOrderArrivalInvalidOrderId () throws UnauthorizedException, InvalidLocationException,InvalidQuantityException, InvalidProductIdException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		
		ezShop.reset();
		ezShop.createUser("mario", "a1b2c3", "Administrator");
		ezShop.login("mario", "a1b2c3");
		ezShop.createProductType("YEEZY 500", "8057014050035", 200.0, "DESERT RAT BLUSH");
		ezShop.updatePosition(1, "1-a-1");
		ezShop.recordBalanceUpdate(1000);
		ezShop.payOrderFor("8057014050035", 20, 15.0);
		
		assertThrows(it.polito.ezshop.exceptions.InvalidOrderIdException.class, ()->{
			 ezShop.recordOrderArrival(0);
		});
		assertThrows(it.polito.ezshop.exceptions.InvalidOrderIdException.class, ()->{
			 ezShop.recordOrderArrival(-1);
		});
		assertThrows(it.polito.ezshop.exceptions.InvalidOrderIdException.class, ()->{
			 ezShop.recordOrderArrival(null);
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
			 ezShop.recordOrderArrival(1);
		});
		ezShop.updatePosition(1, "");
		assertThrows(it.polito.ezshop.exceptions.InvalidLocationException.class, ()->{
			 ezShop.recordOrderArrival(1);
		});
	}
	
	@Test
	public void testRecordOrderArrivalSuccessful () throws UnauthorizedException, InvalidOrderIdException, InvalidLocationException, InvalidQuantityException, InvalidProductIdException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		
		ezShop.reset();
		ezShop.createUser("mario", "a1b2c3", "Administrator");
		ezShop.login("mario", "a1b2c3");
		ezShop.createProductType("YEEZY 500", "8057014050035", 200.0, "DESERT RAT BLUSH");
		ezShop.updatePosition(1, "1-a-1");
		ezShop.recordBalanceUpdate(1000);
		ezShop.payOrderFor("8057014050035", 20, 15.0);
		ezShop.issueOrder("8057014050035", 20, 15.0);
		assertTrue(ezShop.recordOrderArrival(1));
		assertTrue(ezShop.recordOrderArrival(1));
		assertFalse(ezShop.recordOrderArrival(2));
	}
	
	// ----------- END ----------- //
	
	
	// ----------- getAllOrders ----------- //
	
	@Test
	public void testGetAllOrdersUnauthorized () throws UnauthorizedException, InvalidOrderIdException, InvalidLocationException, InvalidQuantityException, InvalidProductIdException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		
		ezShop.reset();
		ezShop.reset();
		ezShop.createUser("mario", "a1b2c3", "Administrator");
		ezShop.createUser("Jordan", "superwow", "Cashier");
		ezShop.login("mario", "a1b2c3");
		ezShop.createProductType("YEEZY 500", "8057014050035", 200.0, "DESERT RAT BLUSH");
		ezShop.recordBalanceUpdate(1000);
		ezShop.payOrderFor("8057014050035", 20, 15.0);
		ezShop.issueOrder("8057014050035", 5, 15.0);
		ezShop.logout();
		assertThrows(it.polito.ezshop.exceptions.UnauthorizedException.class, ()->{
			 ezShop.getAllOrders();
		});
		ezShop.login("Jordan", "superwow");
		assertThrows(it.polito.ezshop.exceptions.UnauthorizedException.class, ()->{
			ezShop.getAllOrders();
		});
	}
	
	@Test
	public void testGetAllOrdersSuccessful () throws UnauthorizedException, InvalidOrderIdException, InvalidLocationException, InvalidQuantityException, InvalidProductIdException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		
		ezShop.reset();
		ezShop.reset();
		ezShop.createUser("mario", "a1b2c3", "Administrator");
		ezShop.createUser("Jordan", "superwow", "Cashier");
		ezShop.login("mario", "a1b2c3");
		ezShop.createProductType("YEEZY 500", "8057014050035", 200.0, "DESERT RAT BLUSH");
		ezShop.recordBalanceUpdate(1000);
		ezShop.payOrderFor("8057014050035", 20, 15.0);
		ezShop.issueOrder("8057014050035", 5, 15.0);
		assertNotNull(ezShop.getAllOrders());
		assertEquals(2, ezShop.getAllOrders().size());
	}
	
	// ----------- END ----------- //
	 
}
