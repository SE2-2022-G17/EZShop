package it.polito.ezshop.integrationTests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import it.polito.ezshop.exceptions.UnauthorizedException;

public class TestAccounting {

	@Test
	public void testRecordBalanceUpdateInvalidUser() {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
	    
		 ezShop.reset();
		 assertThrows(it.polito.ezshop.exceptions.UnauthorizedException.class, ()->{
			 ezShop.recordBalanceUpdate(50.0);
		 });
		 
		 ezShop.reset();
		 assertThrows(it.polito.ezshop.exceptions.UnauthorizedException.class, ()->{
			 ezShop.createUser("yeezy", "iamagod", "Cashier");
			 assertNotNull(ezShop.login("yeezy", "iamagod"));
			 ezShop.recordBalanceUpdate(50.0);
		 });
		 
	}
	
	@Test
	public void testRecordBalanceUpdateSuccessful() throws InvalidUsernameException, InvalidPasswordException, UnauthorizedException, InvalidRoleException {
		 EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		 ezShop.reset();

		 ezShop.createUser("airjordan", "chicagobulls", "Administrator");
		 assertNotNull(ezShop.login("airjordan", "chicagobulls"));
		 
		 assertTrue(ezShop.recordBalanceUpdate(6.0));
		 assertFalse(ezShop.recordBalanceUpdate(-10.0));
		 assertTrue(ezShop.recordBalanceUpdate(-4.0));
		
	}

	// --------------- END --------------- //
	
	
	// --------------- getCreditsAndDebits --------------- //
	
	@Test
	public void testGetCreditsAndDebitsInvalidUser() {
		 EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
	       
		 ezShop.reset();
		 assertThrows(it.polito.ezshop.exceptions.UnauthorizedException.class, ()->{
			 ezShop.getCreditsAndDebits(LocalDate.of(2021, 5, 10), LocalDate.of(2021, 5, 19));
		 });
		 
		 ezShop.reset();
		 assertThrows(it.polito.ezshop.exceptions.UnauthorizedException.class, ()->{
			 ezShop.createUser("yeezy", "iamagod", "Cashier");
			 assertNotNull(ezShop.login("yeezy", "iamagod"));
			 ezShop.getCreditsAndDebits(LocalDate.of(2021, 5, 10), LocalDate.of(2021, 5, 19));
		 });
		 
	}
	
	@Test
	public void testGetCreditsAndDebitsSuccessful() throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		 EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
	     ezShop.reset();
	     
		 ezShop.createUser("airjordan", "chicagobulls", "Administrator");
		 assertNotNull(ezShop.login("airjordan", "chicagobulls"));
		 
	     assertEquals(0, ezShop.getCreditsAndDebits(null, null).size());
		 ezShop.recordBalanceUpdate(23);
		 ezShop.recordBalanceUpdate(-10);
		 LocalDate today = LocalDate.now();
		 LocalDate yesterday = today.minusDays(1);
		 LocalDate tomorrow = today.plusDays(1);
	     assertEquals(2, ezShop.getCreditsAndDebits(null, null).size());
	     assertEquals(2, ezShop.getCreditsAndDebits(null, tomorrow).size());
	     assertEquals(2, ezShop.getCreditsAndDebits(yesterday, null).size());
	     assertEquals(2, ezShop.getCreditsAndDebits(tomorrow, yesterday).size());
	     assertEquals(2, ezShop.getCreditsAndDebits(yesterday, tomorrow).size());
	}

	

	// --------------- END --------------- //
	
	
	
	// --------------- computeBalance --------------- //
	
	@Test
	public void testComputeBalanceInvalidUser() {
		 EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
	       
		 ezShop.reset();
		 assertThrows(it.polito.ezshop.exceptions.UnauthorizedException.class, ()->{
			 ezShop.computeBalance();
		 });
		 
		 ezShop.reset();
		 assertThrows(it.polito.ezshop.exceptions.UnauthorizedException.class, ()->{
			 ezShop.createUser("yeezy", "iamagod", "Cashier");
			 assertNotNull(ezShop.login("yeezy", "iamagod"));
			 ezShop.computeBalance();
		 });
	}
	
	
	@Test
	public void testComputeBalanceSuccessful() throws InvalidUsernameException, InvalidPasswordException, UnauthorizedException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.reset();
		
		ezShop.createUser("airjordan", "chicagobulls", "Administrator");
		assertNotNull(ezShop.login("airjordan", "chicagobulls"));
		ezShop.recordBalanceUpdate(23);
		ezShop.recordBalanceUpdate(-10);
		assertEquals(13, ezShop.computeBalance());
		
	}	
	
}
