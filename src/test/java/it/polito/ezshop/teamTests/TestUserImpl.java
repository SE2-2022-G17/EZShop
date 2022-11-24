package it.polito.ezshop.teamTests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.jupiter.api.Test;

import it.polito.ezshop.model.UserImpl;

public class TestUserImpl {
	//Franco: i made a method for this so i put the test here (it's also in blackbox
	
	@Test
	public void testCheckRole() {
		int userId = 1;
		String username = "Iaffio";
		String password = "cannicavaddu";
		String role = "Roaster";
		UserImpl user = new UserImpl(userId, username, password,role);
		
		
		assertTrue(user.checkRole(role, "Cashier", "Admin"));
		assertFalse(user.checkRole("Cashier", "Admin"));
		
	}
}
