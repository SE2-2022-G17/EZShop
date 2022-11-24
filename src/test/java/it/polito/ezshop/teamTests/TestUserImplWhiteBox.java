package it.polito.ezshop.teamTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import it.polito.ezshop.model.UserImpl;

public class TestUserImplWhiteBox {
	@Test
	public void testUserImplSetGet() {
		UserImpl user = new UserImpl(1, "yeezy", "iamagod", "Cashier");
		
		user.setId(2);
		assertEquals(2, user.getId());
		user.setUsername("manager");
		assertEquals("manager", user.getUsername());
		user.setPassword("a1b4akf42");
		assertEquals("a1b4akf42", user.getPassword());
		user.setRole("ShopManager");
		assertEquals("ShopManager", user.getRole());
		
	}
	
	@Test
	public void testOrderImplCSV() {
		UserImpl user = new UserImpl(1, "yeezy", "iamagod", "Cashier");
		assertEquals("1,yeezy,iamagod,Cashier", user.getCSV());
	}

}
