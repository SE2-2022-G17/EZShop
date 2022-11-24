package it.polito.ezshop.integrationTests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import it.polito.ezshop.data.Customer;
import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.InvalidCustomerCardException;
import it.polito.ezshop.exceptions.InvalidCustomerIdException;
import it.polito.ezshop.exceptions.InvalidCustomerNameException;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import it.polito.ezshop.exceptions.UnauthorizedException;

class TestCustomer {
	
	//BEGIN Customer createCard();
	
	@Test
	void testUnauthorizedCardCreation() {
		EZShopInterface ezShop = new EZShop();
			
		ezShop.reset();
			
		assertThrows(UnauthorizedException.class, ()-> ezShop.createCard());
	}
	
	@Test
	void testCreateCard() throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new EZShop();
			
		ezShop.reset();
		ezShop.createUser("Jordan", "superwow", "Cashier");
		ezShop.login("Jordan", "superwow");
		
		String card1, card2;
		for (int i = 0; i < 5; i++) {
			card1 = ezShop.createCard();
			card2 = ezShop.createCard();
			assertTrue(Integer.parseInt(card1) == Integer.parseInt(card2) - 1);
		}
	}			
		
	// END
		
	// ======================================================
	
	//BEGIN Customer createCard();
	
	@Test
	void testUnauthorizedPointModification() {
		EZShopInterface ezShop = new EZShop();
				
		ezShop.reset();
				
		assertThrows(UnauthorizedException.class, ()-> ezShop.modifyPointsOnCard("5678900", 5500));
	}
	
	@Test
	void testBadCardPointModification() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new EZShop();
				
		ezShop.reset();
		ezShop.createUser("Jordan", "superwow", "Cashier");
		ezShop.login("Jordan", "superwow");
				
		assertThrows(InvalidCustomerCardException.class, ()-> ezShop.modifyPointsOnCard("5678900", 5500)); //bad format
		assertThrows(InvalidCustomerCardException.class, ()-> ezShop.modifyPointsOnCard("", 5500));
		assertThrows(InvalidCustomerCardException.class, ()-> ezShop.modifyPointsOnCard(null, 5500));
	}

	@Test
	void testInexistantCardPointModification() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidCustomerCardException, UnauthorizedException {
		EZShopInterface ezShop = new EZShop();
				
		ezShop.reset();
		ezShop.createUser("Jordan", "superwow", "Cashier");
		ezShop.login("Jordan", "superwow");
				
		assertFalse(ezShop.modifyPointsOnCard("1234567890", 5500));
	}
	
	@Test
	void testAddPoints() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidCustomerCardException, UnauthorizedException, InvalidCustomerNameException, InvalidCustomerIdException {
		EZShopInterface ezShop = new EZShop();
				
		ezShop.reset();
		ezShop.createUser("Jordan", "superwow", "Cashier");
		ezShop.login("Jordan", "superwow");
		
		String card = ezShop.createCard();
		ezShop.modifyCustomer(ezShop.defineCustomer("name"), "name", card);
		assertTrue(ezShop.modifyPointsOnCard(card, 5500));
	}
	
	@Test
	void testNotEnoughPoints() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidCustomerCardException, UnauthorizedException, InvalidCustomerNameException, InvalidCustomerIdException {
		EZShopInterface ezShop = new EZShop();
				
		ezShop.reset();
		ezShop.createUser("Jordan", "superwow", "Cashier");
		ezShop.login("Jordan", "superwow");
		
		String card = ezShop.createCard();
		ezShop.modifyCustomer(ezShop.defineCustomer("name"), "name", card);
		ezShop.modifyPointsOnCard(card, 5500);
		assertFalse(ezShop.modifyPointsOnCard(card, -6000));
	}
	
	// END
		
	// ======================================================
	
	//BEGIN Customer attachCardToCustomer(String customerCard, Integer customerId);
	
	@Test
	void testUnauthorizedAttachCard() {
		EZShopInterface ezShop = new EZShop();
		
		ezShop.reset();
		
		assertThrows(UnauthorizedException.class, ()-> ezShop.attachCardToCustomer("1234567890", 1));
	}
	
	@Test
	void testInvalidCustomerAttachCard() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidCustomerIdException, UnauthorizedException, InvalidCustomerNameException {
		EZShopInterface ezShop = new EZShop();
		
		ezShop.reset();
		ezShop.createUser("Jordan", "superwow", "Cashier");
		ezShop.login("Jordan", "superwow");
		
		assertThrows(InvalidCustomerIdException.class, ()-> ezShop.attachCardToCustomer(ezShop.createCard(), 0));
		assertThrows(InvalidCustomerIdException.class, ()-> ezShop.attachCardToCustomer(ezShop.createCard(), -1));
		assertThrows(InvalidCustomerIdException.class, ()-> ezShop.attachCardToCustomer(ezShop.createCard(), null));
	}
	
	@Test
	void testInvalidCardAttachCard() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new EZShop();
		
		ezShop.reset();
		ezShop.createUser("Jordan", "superwow", "Cashier");
		ezShop.login("Jordan", "superwow");
		
		assertThrows(InvalidCustomerCardException.class, ()-> ezShop.attachCardToCustomer(null, 1)); // null
		assertThrows(InvalidCustomerCardException.class, ()-> ezShop.attachCardToCustomer("", 1));         // empty
		assertThrows(InvalidCustomerCardException.class, ()-> ezShop.attachCardToCustomer("123456789", 1)); // one too short
		assertThrows(InvalidCustomerCardException.class, ()-> ezShop.attachCardToCustomer("abcdefghij", 1)); // garbage, right length
		assertThrows(InvalidCustomerCardException.class, ()-> ezShop.attachCardToCustomer("12345678900", 1)); // one too long
	}
	
	@Test
	void testInexistantCustomerAttachCard() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidCustomerIdException, UnauthorizedException, InvalidCustomerNameException, InvalidCustomerCardException {
		EZShopInterface ezShop = new EZShop();
		
		ezShop.reset();
		ezShop.createUser("Jordan", "superwow", "Cashier");
		ezShop.login("Jordan", "superwow");
		
		assertFalse(ezShop.attachCardToCustomer(ezShop.createCard(), 1));
	}
	
	@Test
	void testInexistantCardAttachCard() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidCustomerIdException, UnauthorizedException, InvalidCustomerNameException, InvalidCustomerCardException {
		EZShopInterface ezShop = new EZShop();
		
		ezShop.reset();
		ezShop.createUser("Jordan", "superwow", "Cashier");
		ezShop.login("Jordan", "superwow");

		assertFalse(ezShop.attachCardToCustomer("1234567890", ezShop.defineCustomer("name")));
	}
	
	@Test
	void testAlreadyAssignedCardAttachCard() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidCustomerIdException, UnauthorizedException, InvalidCustomerNameException, InvalidCustomerCardException {
		EZShopInterface ezShop = new EZShop();
		
		ezShop.reset();
		ezShop.createUser("Jordan", "superwow", "Cashier");
		ezShop.login("Jordan", "superwow");

		String card = ezShop.createCard();
		ezShop.attachCardToCustomer(card, ezShop.defineCustomer("name"));
		assertFalse(ezShop.attachCardToCustomer(card, ezShop.defineCustomer("name 2")));
	}
	
	@Test
	void testAttachCard() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidCustomerIdException, UnauthorizedException, InvalidCustomerNameException, InvalidCustomerCardException {
		EZShopInterface ezShop = new EZShop();
		
		ezShop.reset();
		ezShop.createUser("Jordan", "superwow", "Cashier");
		ezShop.login("Jordan", "superwow");
		assertTrue(ezShop.attachCardToCustomer(ezShop.createCard(), ezShop.defineCustomer("name")));
	}
	
	@Test
	void testReplaceCardAttachCard() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidCustomerIdException, UnauthorizedException, InvalidCustomerNameException, InvalidCustomerCardException {
		EZShopInterface ezShop = new EZShop();
		
		ezShop.reset();
		ezShop.createUser("Jordan", "superwow", "Cashier");
		ezShop.login("Jordan", "superwow");
		
		Integer client = ezShop.defineCustomer("name");	
		ezShop.attachCardToCustomer(ezShop.createCard(), client); // assign a first card
		
		assertTrue(ezShop.attachCardToCustomer(ezShop.createCard(), client)); //replace it
	}
	
	// END
	
	// ======================================================		
		
	//BEGIN Customer getCustomer(id);
	
	@Test
	void testUnauthorizedGetCustomer() {
		EZShopInterface ezShop = new EZShop();
		
		ezShop.reset();
		
		assertThrows(UnauthorizedException.class, ()-> ezShop.getCustomer(456789));
	}
	
	@Test
	void testGetInvalidCustomerId() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidCustomerIdException, UnauthorizedException, InvalidCustomerNameException {
		EZShopInterface ezShop = new EZShop();
		
		ezShop.reset();
		ezShop.createUser("Jordan", "superwow", "Cashier");
		ezShop.login("Jordan", "superwow");
		
		assertThrows(InvalidCustomerIdException.class, ()-> ezShop.getCustomer(0));
		assertThrows(InvalidCustomerIdException.class, ()-> ezShop.getCustomer(-1));
		assertThrows(InvalidCustomerIdException.class, ()-> ezShop.getCustomer(null));
	}
	
	@Test
	void testGetInexistantCustomer() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidCustomerIdException, UnauthorizedException, InvalidCustomerNameException {
		EZShopInterface ezShop = new EZShop();
		
		ezShop.reset();
		ezShop.createUser("Jordan", "superwow", "Cashier");
		ezShop.login("Jordan", "superwow");
		
		assertNull(ezShop.getCustomer(546789));
	}
	
	@Test
	void testGetExistingCustomer() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidCustomerIdException, UnauthorizedException, InvalidCustomerNameException {
		EZShopInterface ezShop = new EZShop();
		
		ezShop.reset();
		ezShop.createUser("Jordan", "superwow", "Cashier");
		ezShop.login("Jordan", "superwow");
		
		assertNotNull(ezShop.getCustomer(ezShop.defineCustomer("valid name")));
	}
	
	// END
	
	// ======================================================
	
	// BEGIN Integer defineCustomer(customerName)
	
	@Test
	void testUnauthorizedCustomerCreation() {
		EZShopInterface ezShop = new EZShop();
		ezShop.reset();
		
		assertThrows(UnauthorizedException.class, ()-> ezShop.defineCustomer("whatever valid name"));
	}
	
	@Test
	void testInvalidCustomerNameCreation() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new EZShop();
		
		ezShop.reset();
		ezShop.createUser("Jordan", "superwow", "Cashier");
		ezShop.login("Jordan", "superwow");
		
		assertThrows(InvalidCustomerNameException.class, ()-> ezShop.defineCustomer(""));
		assertThrows(InvalidCustomerNameException.class, ()-> ezShop.defineCustomer(null));
	}
	
	@Test
	void testUniqueCustomerCreation() throws InvalidCustomerNameException, UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidCustomerIdException {
		EZShopInterface ezShop = new EZShop();
		
		ezShop.reset();
		ezShop.createUser("Jordan", "superwow", "Cashier");
		ezShop.login("Jordan", "superwow");
		
		Integer id = ezShop.defineCustomer("Mr. ValidName");
		assertTrue(id > 0);
		Customer c = ezShop.getCustomer(id);
		assertEquals(id, c.getId());
		assertEquals("Mr. ValidName", c.getCustomerName());
	}
	
	@Test
	void testNotUniqueCustomerCreation() throws InvalidCustomerNameException, UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidCustomerIdException {
		EZShopInterface ezShop = new EZShop();
		
		ezShop.reset();
		ezShop.createUser("Jordan", "superwow", "Cashier");
		ezShop.login("Jordan", "superwow");
		
		ezShop.defineCustomer("Mr. ValidName");                   // first call
		assertEquals(-1, ezShop.defineCustomer("Mr. ValidName")); // second call should return -1
	}
	
	// END
	
	// ======================================================
		
	// BEGIN List<Customer> getAllCustomers
	
	@Test
	void testUnauthorizedGetAllCustomers() {
		EZShopInterface ezShop = new EZShop();
		
		ezShop.reset();
		
		assertThrows(UnauthorizedException.class, ()-> ezShop.getAllCustomers());
	}
	
	@Test
	void testGetAllCustomers() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, UnauthorizedException, InvalidCustomerNameException, InvalidCustomerIdException {
		EZShopInterface ezShop = new EZShop();
		
		ezShop.reset();
		ezShop.createUser("Jordan", "superwow", "Cashier");
		ezShop.login("Jordan", "superwow");
		
		List<Customer> customers = ezShop.getAllCustomers();
		assertTrue(customers.isEmpty());
		
		Integer[] ids = new Integer[5];
		for (int i = 0; i < 5; i++) {
			assertEquals(customers.size(), i);
			
			ids[i] = ezShop.defineCustomer("customer no. " + i);
			
			assertEquals(customers.size(), i);    //customers is unchanged until we call the method again
			customers = ezShop.getAllCustomers();
			assertEquals(customers.size(), i+1);
		}
		
		for (int i = 0; i < 5; i++) {
			assertEquals(customers.get(i), ezShop.getCustomer(ids[i]));
		}	
	}

	// END
	
	// ======================================================
			
	// BEGIN boolean modifyCustomer(Integer id, String newCustomerName, String newCustomerCard)
	
	@Test
	void testUnauthorizedCustomerModification() {
		EZShopInterface ezShop = new EZShop();
		ezShop.reset();
		assertThrows(UnauthorizedException.class, ()-> ezShop.modifyCustomer(467, "whatever valid name", "1928290323"));
	}

	@Test
	void testInvalidIdCustomerModification() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidCustomerNameException, UnauthorizedException {
		EZShopInterface ezShop = new EZShop();
		
		ezShop.reset();
		ezShop.createUser("Jordan", "superwow", "Cashier");
		ezShop.login("Jordan", "superwow");
		
		ezShop.defineCustomer("whatever valid name");
		ezShop.defineCustomer("another one for good measure");
		
		assertThrows(InvalidCustomerIdException.class, ()-> ezShop.modifyCustomer(0, "new name", "1928290323"));
		assertThrows(InvalidCustomerIdException.class, ()-> ezShop.modifyCustomer(-1, "new name", "1928290323"));
		assertThrows(InvalidCustomerIdException.class, ()-> ezShop.modifyCustomer(null, "new name", "1928290323"));
	}
	
	@Test
	void testInvalidNameCustomerModification() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidCustomerNameException, UnauthorizedException {
		EZShopInterface ezShop = new EZShop();
		
		ezShop.reset();
		ezShop.createUser("Jordan", "superwow", "Cashier");
		ezShop.login("Jordan", "superwow");
		
		Integer id = ezShop.defineCustomer("whatever valid name");
		ezShop.defineCustomer("another one for good measure");
		
		assertThrows(InvalidCustomerNameException.class, ()-> ezShop.modifyCustomer(id, "", "1928290323"));
		assertThrows(InvalidCustomerNameException.class, ()-> ezShop.modifyCustomer(id, null, "1928290323"));
	}
	
	@Test
	void testInvalidCardCustomerModification() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException {
		EZShopInterface ezShop = new EZShop();
		
		ezShop.reset();
		ezShop.createUser("Jordan", "superwow", "Cashier");
		ezShop.login("Jordan", "superwow");
		
		Integer id = ezShop.defineCustomer("whatever valid name");
		ezShop.defineCustomer("another one for good measure");
		Customer c = ezShop.getCustomer(id);
		
		assertThrows(InvalidCustomerCardException.class, ()-> ezShop.modifyCustomer(id, "new name", "12345789"));  // one too short
		assertEquals(c.getCustomerName(), "whatever valid name"); // error -> name unchanged
		
		assertThrows(InvalidCustomerCardException.class, ()-> ezShop.modifyCustomer(id, "new name", "abcdefghij")); // garbage but right length
		assertEquals(c.getCustomerName(), "whatever valid name"); // error -> name unchanged
		
		assertThrows(InvalidCustomerCardException.class, ()-> ezShop.modifyCustomer(id, "new name", "12345678900")); // one too long
		assertEquals(c.getCustomerName(), "whatever valid name"); // error -> name unchanged
	}
	
	@Test
	void testInexistantCustomerModification() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidCustomerNameException, UnauthorizedException, InvalidCustomerCardException, InvalidCustomerIdException {
		EZShopInterface ezShop = new EZShop();
		
		ezShop.reset();
		ezShop.createUser("Jordan", "superwow", "Cashier");
		ezShop.login("Jordan", "superwow");
		
		assertFalse(ezShop.modifyCustomer(666, "new name", ezShop.createCard()));
	}
	
	@Test
	void testCompleteModification() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidCustomerNameException, UnauthorizedException, InvalidCustomerCardException, InvalidCustomerIdException {
		EZShopInterface ezShop = new EZShop();
		
		ezShop.reset();
		ezShop.createUser("Jordan", "superwow", "Cashier");
		ezShop.login("Jordan", "superwow");
		
		Integer id = ezShop.defineCustomer("whatever valid name");
		ezShop.defineCustomer("another one for good measure");
		String card = ezShop.createCard();
		
		assertTrue(ezShop.modifyCustomer(id, "new name", card));
		Customer c = ezShop.getCustomer(id);
		assertEquals(c.getCustomerName(), "new name");
		assertEquals(c.getCustomerCard(), card);
	}
	
	@Test
	void testNullCardModification() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidCustomerNameException, UnauthorizedException, InvalidCustomerCardException, InvalidCustomerIdException {
		EZShopInterface ezShop = new EZShop();
		
		ezShop.reset();
		ezShop.createUser("Jordan", "superwow", "Cashier");
		ezShop.login("Jordan", "superwow");
		
		Integer id = ezShop.defineCustomer("whatever valid name");
		ezShop.defineCustomer("another one for good measure");
		String card = ezShop.createCard();
		ezShop.modifyCustomer(id, "new name", card);
		
		assertTrue(ezShop.modifyCustomer(id, "new name", null));
		
		Customer c = ezShop.getCustomer(id);
		assertEquals(c.getCustomerName(), "new name");
		assertEquals(c.getCustomerCard(), card);
	}
	
	@Test
	void testEmptyCardModification() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidCustomerNameException, UnauthorizedException, InvalidCustomerCardException, InvalidCustomerIdException {
		EZShopInterface ezShop = new EZShop();
		
		ezShop.reset();
		ezShop.createUser("Jordan", "superwow", "Cashier");
		ezShop.login("Jordan", "superwow");
		
		Integer id = ezShop.defineCustomer("whatever valid name");
		ezShop.defineCustomer("another one for good measure");		
		ezShop.modifyCustomer(id, "new name", ezShop.createCard());
		
		assertTrue(ezShop.modifyCustomer(id, "new name", ""));
		
		Customer c = ezShop.getCustomer(id);
		assertEquals(c.getCustomerName(), "new name");
		assertEquals(c.getCustomerCard(), "");
	}
	
	@Test
	void testAlreadyAssignedCardModification() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidCustomerNameException, UnauthorizedException, InvalidCustomerCardException, InvalidCustomerIdException {
		EZShopInterface ezShop = new EZShop();
		
		ezShop.reset();
		ezShop.createUser("Jordan", "superwow", "Cashier");
		ezShop.login("Jordan", "superwow");
		
		Integer id = ezShop.defineCustomer("whatever valid name");
		Integer id2 = ezShop.defineCustomer("another one for good measure");
		String card = ezShop.createCard();
		String card2 = ezShop.createCard();
		ezShop.modifyCustomer(id, "new name", card);
		ezShop.modifyCustomer(id2, "another one for good measure", card2);
		
		assertFalse(ezShop.modifyCustomer(id2, "new name", card));
		
		Customer c = ezShop.getCustomer(id2);
		assertEquals(c.getCustomerName(), "another one for good measure");
		assertEquals(c.getCustomerCard(), card2);
	}
	
	@Test
	void testSameCardModification() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidCustomerNameException, UnauthorizedException, InvalidCustomerCardException, InvalidCustomerIdException {
		EZShopInterface ezShop = new EZShop();
		
		ezShop.reset();
		ezShop.createUser("Jordan", "superwow", "Cashier");
		ezShop.login("Jordan", "superwow");
		
		Integer id = ezShop.defineCustomer("whatever valid name");
		ezShop.defineCustomer("another one for good measure");
		String card = ezShop.createCard();
		ezShop.modifyCustomer(id, "new name", card);
		
		assertTrue(ezShop.modifyCustomer(id, "new name", card));
		
		Customer c = ezShop.getCustomer(id);
		assertEquals(c.getCustomerName(), "new name");
		assertEquals(c.getCustomerCard(), card);
	}
	
	// END
	
	// ======================================================
				
	// BEGIN boolean deleteCustomer(Integer id)
	
	@Test
	void testUnauthorizedCustomerDeletion() {
		EZShopInterface ezShop = new EZShop();
		ezShop.reset();
		assertThrows(UnauthorizedException.class, ()-> ezShop.deleteCustomer(467));
	}
	
	@Test
	void testInvalidIdCustomerDeletion() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidCustomerNameException, UnauthorizedException {
		EZShopInterface ezShop = new EZShop();
		
		ezShop.reset();
		ezShop.createUser("Jordan", "superwow", "Cashier");
		ezShop.login("Jordan", "superwow");
		
		ezShop.defineCustomer("whatever valid name");
		ezShop.defineCustomer("another one for good measure");
		
		assertThrows(InvalidCustomerIdException.class, ()-> ezShop.deleteCustomer(-1));
		assertThrows(InvalidCustomerIdException.class, ()-> ezShop.deleteCustomer(0));
		assertThrows(InvalidCustomerIdException.class, ()-> ezShop.deleteCustomer(null));
	}
	
	@Test
	void testInexistantCustomerDeletion() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException {
		EZShopInterface ezShop = new EZShop();
		
		ezShop.reset();
		ezShop.createUser("Jordan", "superwow", "Cashier");
		ezShop.login("Jordan", "superwow");
		
		assertFalse(ezShop.deleteCustomer(666));
	}
	
	@Test
	void testCustomerDeletion() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException {
		EZShopInterface ezShop = new EZShop();
		
		ezShop.reset();
		ezShop.createUser("Jordan", "superwow", "Cashier");
		ezShop.login("Jordan", "superwow");
		
		Integer id = ezShop.defineCustomer("whatever valid name");
		ezShop.defineCustomer("another one for good measure");
		
		assertTrue(ezShop.deleteCustomer(id));
		assertNull(ezShop.getCustomer(id));
		assertEquals(ezShop.getAllCustomers().size(), 1);
	}
	
	// END
	
	// ======================================================
}