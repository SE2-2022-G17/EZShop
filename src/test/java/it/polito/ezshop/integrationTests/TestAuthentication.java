package it.polito.ezshop.integrationTests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.exceptions.InvalidUsernameException;

public class TestAuthentication {

	@Test
	public void testEmptyNullUsername() {
        EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
       
        ezShop.reset();
        assertThrows(it.polito.ezshop.exceptions.InvalidUsernameException.class, () -> {
        	ezShop.login(null, "whatever");
        });
        assertThrows(it.polito.ezshop.exceptions.InvalidUsernameException.class, () -> {
        	ezShop.login("", "whatever");
        });

	}
	
	
	@Test
	public void testEmptyNullPassword() {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		
		ezShop.reset();
        assertThrows(it.polito.ezshop.exceptions.InvalidPasswordException.class, () -> {
        	ezShop.login("whatever", null);
        });
        assertThrows(it.polito.ezshop.exceptions.InvalidPasswordException.class, () -> {
        	ezShop.login("whatever", "");
        });
	}
	
	@Test
	public void testWrongCredentials() throws InvalidUsernameException, InvalidPasswordException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		
		ezShop.reset();
		assertNull(ezShop.login("wrongUser", "wrongPassword"));
	}
	
	
	@Test
	public void testCorrectCredentials() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		
		ezShop.reset();
		ezShop.createUser("yeezy", "iamagod", "Cashier");
		assertNotNull(ezShop.login("yeezy", "iamagod"));
	}
	
	
	//TODO: test DB problems(however login doesn't interact with DB so...)
	
	@Test
	public void testNoUserLogged() {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		
		ezShop.reset();
		assertFalse(ezShop.logout());
	}
	
	
	@Test
	public void testUserLogged() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		
		ezShop.reset();
		ezShop.createUser("yeezy", "iamagod", "Cashier");
		ezShop.login("yeezy", "iamagod");
		
		assertTrue(ezShop.logout());
	}
	
	
}
