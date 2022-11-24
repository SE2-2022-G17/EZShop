package it.polito.ezshop.integrationTests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.exceptions.InvalidUserIdException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import it.polito.ezshop.exceptions.UnauthorizedException;

public class TestUserManagement {
	
	// ----------- createUser ----------- //
	
	@Test
	public void testCreateUserInvalidUsername() {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
	    
		 ezShop.reset();
		
		 assertThrows(it.polito.ezshop.exceptions.InvalidUsernameException.class, ()->{
			 ezShop.createUser("", "abc", "Administrator");
		 });
		 
		 assertThrows(it.polito.ezshop.exceptions.InvalidUsernameException.class, ()->{
			 ezShop.createUser(null, "a1b2c3", "Administrator");
		 });
		 
	}
	
	@Test
	public void testCreateUserInvalidPassword() {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		
		ezShop.reset();
		 assertThrows(it.polito.ezshop.exceptions.InvalidPasswordException.class, ()->{
			 ezShop.createUser("mario", "", "Administrator");
		 });
		 ezShop.reset();
		 assertThrows(it.polito.ezshop.exceptions.InvalidPasswordException.class, ()->{
			 ezShop.createUser("mario", null, "Administrator");
		 });
	}
	
	
	@Test
	public void testCreateUserInvalidRole() {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		
		ezShop.reset();
		assertThrows(it.polito.ezshop.exceptions.InvalidRoleException.class, ()->{
			ezShop.createUser("mario", "a1b2c3", "");
		});
		ezShop.reset();
		assertThrows(it.polito.ezshop.exceptions.InvalidRoleException.class, ()->{
			ezShop.createUser("mario", "a1b2c3", null);
		});
		ezShop.reset();
		assertThrows(it.polito.ezshop.exceptions.InvalidRoleException.class, ()->{
			ezShop.createUser("mario", "a1b2c3", "Manager");
		});
	}
	
	
	@Test
	public void testCreateUserExistingUsername() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		
		ezShop.reset();
		ezShop.createUser("mario", "a1b2c3", "ShopManager");
		assertEquals(-1, ezShop.createUser("mario", "superwow", "Cashier"));
	}
	
	
	@Test
	public void testCreateUserSuccessful() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		
		ezShop.reset();
		assertEquals(1, ezShop.createUser("mario", "a1b2c3", "ShopManager"));
		assertEquals(2, ezShop.createUser("Jordan", "superwow", "Cashier"));
	}
	
	// ----------- END ----------- //
	
	
	// ----------- deleteUser ----------- //
	
	
	@Test
	public void testDeleteUserInvalidUserId() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		
		ezShop.reset();
		ezShop.createUser("mario", "a1b2c3", "Administrator");
		ezShop.login("mario", "a1b2c3");
		assertThrows(it.polito.ezshop.exceptions.InvalidUserIdException.class, ()->{
			 ezShop.deleteUser(-1);
		});
		assertThrows(it.polito.ezshop.exceptions.InvalidUserIdException.class, ()->{
			 ezShop.deleteUser(null);
		});
	}
	
	
	@Test
	public void testDeleteUserUnauthorized() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		
		ezShop.reset();
		ezShop.createUser("mario", "a1b2c3", "ShopManager");
		ezShop.createUser("Jordan", "superwow", "Cashier");
		assertThrows(it.polito.ezshop.exceptions.UnauthorizedException.class, ()->{
			 ezShop.deleteUser(2);
		});
		ezShop.login("mario", "a1b2c3");
		assertThrows(it.polito.ezshop.exceptions.UnauthorizedException.class, ()->{
			 ezShop.deleteUser(2);
		});
	}
	
	@Test
	public void testDeleteUserSuccessful() throws InvalidUserIdException, UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		
		ezShop.reset();
		ezShop.createUser("mario", "a1b2c3", "Administrator");
		ezShop.createUser("Jordan", "superwow", "Cashier");
		ezShop.login("mario", "a1b2c3");
		assertFalse(ezShop.deleteUser(3));
		assertTrue(ezShop.deleteUser(2));
	}
	
	// ----------- END ----------- //
	
	
	// ----------- getAllUsers ----------- //
	
	@Test
	public void testGetAllUsersUnauthorized() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		
		ezShop.reset();
		ezShop.createUser("Jordan", "superwow", "Cashier");
		assertThrows(it.polito.ezshop.exceptions.UnauthorizedException.class, ()->{
			ezShop.getAllUsers();
		});
		ezShop.login("Jordan", "superwow");
		assertThrows(it.polito.ezshop.exceptions.UnauthorizedException.class, ()->{
			ezShop.getAllUsers();
		});
	}
	
	@Test
	public void testGetAllUsersSuccessful() throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		
		ezShop.reset();
		ezShop.createUser("mario", "a1b2c3", "Administrator");
		ezShop.createUser("Jordan", "superwow", "Cashier");
		ezShop.login("mario", "a1b2c3");
		
		assertNotNull(ezShop.getAllUsers());
		assertEquals(2, ezShop.getAllUsers().size());
	}
	
	// ----------- END ----------- //
	
	
	// ----------- getUser ----------- //
	
	@Test
	public void testGetUserUnauthorized() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		
		ezShop.reset();
		ezShop.createUser("mario", "a1b2c3", "Administrator");
		ezShop.createUser("Jordan", "superwow", "Cashier");

		assertThrows(it.polito.ezshop.exceptions.UnauthorizedException.class, ()->{
			ezShop.getUser(2);
		});
		ezShop.login("Jordan", "superwow");
		assertThrows(it.polito.ezshop.exceptions.UnauthorizedException.class, ()->{
			ezShop.getUser(2);
		});
	}
	
	@Test
	public void testGetUserInvalidUserId() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		
		ezShop.reset();
		ezShop.createUser("mario", "a1b2c3", "Administrator");
		ezShop.login("mario", "a1b2c3");
		assertThrows(it.polito.ezshop.exceptions.InvalidUserIdException.class, ()->{
			ezShop.getUser(-1);
		});
		assertThrows(it.polito.ezshop.exceptions.InvalidUserIdException.class, ()->{
			ezShop.getUser(null);
		});
	}
	
	@Test
	public void testGetUserSuccessful() throws UnauthorizedException, InvalidUserIdException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		
		ezShop.reset();
		ezShop.createUser("mario", "a1b2c3", "Administrator");
		ezShop.createUser("Jordan", "superwow", "Cashier");
		ezShop.login("mario", "a1b2c3");
		
		assertNotNull(ezShop.getUser(2));
		assertNull(ezShop.getUser(3));
	}
	
	
	// ----------- END ----------- //
	
	
	// ----------- updateUserRights ----------- //
	
	@Test
	public void testUpdateUserRightsInvalidUserId () throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		
		ezShop.reset();
		ezShop.createUser("mario", "a1b2c3", "Administrator");
		ezShop.createUser("Jordan", "superwow", "Cashier");
		ezShop.login("mario", "a1b2c3");

		assertThrows(it.polito.ezshop.exceptions.InvalidUserIdException.class, ()->{
			ezShop.updateUserRights(null, "ShopManager");
		});
		assertThrows(it.polito.ezshop.exceptions.InvalidUserIdException.class, ()->{
			ezShop.updateUserRights(-1, "ShopManager");
		});
	}
	
	@Test
	public void testUpdateUserRightsUnauthorized() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		
		ezShop.reset();
		ezShop.createUser("mario", "a1b2c3", "Administrator");
		ezShop.createUser("Jordan", "superwow", "Cashier");

		assertThrows(it.polito.ezshop.exceptions.UnauthorizedException.class, ()->{
			ezShop.updateUserRights(2, "ShopManager");
		});
		ezShop.login("Jordan", "superwow");
		assertThrows(it.polito.ezshop.exceptions.UnauthorizedException.class, ()->{
			ezShop.updateUserRights(2, "ShopManager");
		});
	}
	
	@Test
	public void testUpdateUserRightsInvalidRole() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		
		ezShop.reset();
		ezShop.createUser("mario", "a1b2c3", "Administrator");
		ezShop.createUser("Jordan", "superwow", "Cashier");
		ezShop.login("mario", "a1b2c3");
		
		assertThrows(it.polito.ezshop.exceptions.InvalidRoleException.class, ()->{
			ezShop.updateUserRights(2, "");
		});
		assertThrows(it.polito.ezshop.exceptions.InvalidRoleException.class, ()->{
			ezShop.updateUserRights(2, null);
		});
		assertThrows(it.polito.ezshop.exceptions.InvalidRoleException.class, ()->{
			ezShop.updateUserRights(2, "Manager");
		});
	}
	
	@Test
	public void testUpdateUserRightsSuccessful () throws UnauthorizedException, InvalidUserIdException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		
		ezShop.reset();
		//ezShop.reset();
		ezShop.createUser("mario", "a1b2c3", "Administrator");
		ezShop.createUser("Jordan", "superwow", "Cashier");
		ezShop.login("mario", "a1b2c3");
		
		assertTrue(ezShop.updateUserRights(2, "ShopManager"));
		assertFalse(ezShop.updateUserRights(3, "ShopManager"));
	}
	// ----------- END ----------- //
	
	 
}
