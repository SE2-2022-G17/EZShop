package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import it.polito.ezshop.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class EZShop implements EZShopInterface {
	
	
	/*
	 * some constants (use and declare them to prevent typing errors when you input string, see method
	 * startSaleTransaction below as example) 
	 */
	//user role constants
	private static final String ADMIN = "Administrator";
	private static final String CASHIER = "Cashier";
	private static final String SHOPMANAGER = "ShopManager";

	
	//SaleTransactionImpl and ReturnTransactionImpl constants
	private static final Integer SALE_STATE_OPEN = 0;
	private static final Integer SALE_STATE_CLOSED = 1;
	private static final Integer SALE_STATE_PAYED = 2;
	
	private static final String CREDIT = "Credit";
	private static final String DEBIT = "Debit";
	//TODO: someone should define also ORDER, SALE, RETURN
	/*
	 * end of constants section
	 */
	


	/*
	 * class attributes (add them as you implement the methods...) variable typeCounter are meant to keep track
	 * of the next id to use for new vairable (they should not be decremented)
	 */
	
	
	Integer logged_user;
	
	HashMap<Integer, Customer> customers = new HashMap<Integer, Customer>();
	HashMap<String, Customer> customerCards = new HashMap<String, Customer>(); //key is the card ID, value is the customer ID (null if none)	
	
	/*
	 *Franco: i needed the ProductType to implement my methods so i added these
	 *the key for productTypes is now an Integer (it used to be BarCode in design), see my post on slack
	 */
	HashMap<Integer, ProductTypeImpl> productTypes = new HashMap<>();
	HashMap<String, ProductImpl> products = new HashMap<>();
	
	/*
	 * Sale and return data structures
	 */
	
	int saleCounter=0;
	int returnCounter=0;
	HashMap<Integer, SaleTransactionImpl> sales = new HashMap<>();
	HashMap<Integer, ReturnTransactionImpl> returns = new HashMap<>();
	
	/*
	 * User data structure
	 */
	HashMap<Integer, UserImpl> users = new HashMap<>();
	
	/*
	 * Product data structure
	 */
	List<PositionImpl> positions = new ArrayList<>();
	HashMap<Integer, OrderImpl> orders = new HashMap<>();


	BalanceImpl balance = new BalanceImpl();
	
	PersistencyManager pm = new PersistencyManager();
	
	/*
	 * Constructor method, for now it does nothing but i think we can't test anything without it
	 */
	public EZShop() {
		
		if(!loadAll()) {
			//load unsuccessfull
			
		}
	}
	
	/*
	 * INTERFACE METHODS
	 * 
	 * 
	 * 
	 * make sure you are consistent to both the API and our design
	 * 
	 */
	
    @Override
    public void reset() {
    	customers.clear();
    	customerCards.clear();
    	productTypes.clear();
    	products.clear();
    	sales.clear();
    	returns.clear();
    	users.clear();
    	orders.clear();
    	positions.clear();
    	balance.setAmount(0.0);
    	balance.clearBalanceOperations();
    	//and Credit Cards? 
    	saleCounter = 0;
    	returnCounter = 0;
    	
    	BalanceOperationImpl balanceOperation = new BalanceOperationImpl();
    	pm.reset(balanceOperation);
    	CustomerImpl customer = new CustomerImpl();
    	pm.reset(customer);
    	Dummy dummy = new Dummy();
    	pm.reset(dummy);
    	OrderImpl order = new OrderImpl();
    	pm.reset(order);
    	PositionImpl position = new PositionImpl();
    	pm.reset(position);
    	ProductTypeImpl productType = new ProductTypeImpl();
    	pm.reset(productType);
    	ProductImpl product = new ProductImpl();
    	pm.reset(product);
    	ReturnEntryImpl returnEntry = new ReturnEntryImpl();
    	pm.reset(returnEntry);
    	ReturnTransactionImpl returnTransaction = new ReturnTransactionImpl();
    	pm.reset(returnTransaction);
    	SaleTransactionImpl saleTransaction = new SaleTransactionImpl();
    	pm.reset(saleTransaction);
    	TicketEntryImpl ticket = new TicketEntryImpl();
    	pm.reset(ticket);
    	UserImpl user = new UserImpl();
    	pm.reset(user);   	
    	
    }

    @Override
    public Integer createUser(String username, String password, String role) throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
    	boolean sameUsername = users
				.values()
				.stream()
				.map(u -> u.getUsername())
				.anyMatch(usrn -> usrn.equals(username));

		if(sameUsername)
			return -1; 
    	
    	if(username==null || username.equals("")) {
    		throw new InvalidUsernameException();
    	}
    	if(password==null || password.equals("")) {
    		throw new InvalidPasswordException();
    	}
    	if(role==null || !(role.equals("Administrator") || role.equals("Cashier") || role.equals("ShopManager"))||role.equals("")){
    		throw new InvalidRoleException();
    	}
    	
    	Integer id;
    	if(users.keySet().size() == 0)
    		id = 1;
    	else
    		id = Collections.max(users.keySet()) + 1; 
    	
    	UserImpl newUser = new UserImpl(id, username, password, role);
		boolean successful = pm.store(newUser);
    	if(!successful)
    		return -1;
    	users.put(id, newUser);
    	return id;
    }

    @Override
    public boolean deleteUser(Integer id) throws InvalidUserIdException, UnauthorizedException {
    	if(id==null || id<=0)
    		throw new InvalidUserIdException();
    	
    	if(logged_user==null || !users.get(logged_user).getRole().equals("Administrator"))
    		throw new UnauthorizedException();
    	
    	UserImpl delUser = users.get(id);
    	if(delUser==null)
    		return false;
    	
		boolean successful = pm.delete(users.get(id));
    	if(!successful) 
    		return false;
    	
    	users.remove(id);
    	return true;
    }

    @Override
    public List<User> getAllUsers() throws UnauthorizedException {
        if(logged_user==null || !users.get(logged_user).checkRole(ADMIN))
    		throw new UnauthorizedException();
    	
    	return users.values().stream().collect(Collectors.toList());
    }

    @Override
    public User getUser(Integer id) throws InvalidUserIdException, UnauthorizedException {
        if(logged_user==null || !users.get(logged_user).checkRole(ADMIN))
    		throw new UnauthorizedException();
    	if(id==null||id<=0)
    		throw new InvalidUserIdException();
    	
    	UserImpl user = users.get(id);
    	
    	return user;
    }

    @Override
    public boolean updateUserRights(Integer id, String role) throws InvalidUserIdException, InvalidRoleException, UnauthorizedException {
        if(logged_user==null || !users.get(logged_user).checkRole(ADMIN))
    		throw new UnauthorizedException();
    	if(id==null||id<=0)
    		throw new InvalidUserIdException();
    	if(role==null || !((role.equals("Administrator") || role.equals("Cashier") || role.equals("ShopManager"))) ||role.equals("")){
    		throw new InvalidRoleException();
    	}
    	
    	UserImpl user = users.get(id);
    	if(user==null)
    		return false;
    	user.setRole(role);

		boolean successful = pm.update(user);
    	if(!successful)
    		return false;
    	
    	return true;
    }

    @Override
    public User login(String username, String password) throws InvalidUsernameException, InvalidPasswordException {
    	if(username == "")
    		throw new InvalidUsernameException();
    	if(username == null)
    		throw new InvalidUsernameException();
    	if(password == "")
    		throw new InvalidPasswordException();
    	if(password == null)
    		throw new InvalidPasswordException();
    	
    	Optional<UserImpl> user = users.values()
    									.stream()
    									.filter(u -> u.getUsername().equals(username))
    									.findFirst();
    	if(user.isPresent()) {
    		if(user.get().getPassword().equals(password)) {
    			logged_user = user.get().getId();
    			return user.get();
    		}
    		else
    			return null;
    	}
    	else { 
    		return null;
    	}
    	
        
    }

    @Override
    public boolean logout() {
    	if(logged_user == null)
    		return false;
    	else {
    		logged_user = null;
    		return true;
    	}
    }

    
    @Override
    public Integer createProductType(String description, String productCode, double pricePerUnit, String note) throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
    	if(description == null)
    		throw new InvalidProductDescriptionException();
    	if(description.equals(""))
    		throw new InvalidProductDescriptionException();
    	if(productCode == null)
    		throw new InvalidProductCodeException();
    	if(productCode.equals(""))
    		throw new InvalidProductCodeException();
    	if(ProductTypeImpl.validateProductCode(productCode) == false)
    		throw new InvalidProductCodeException();
    	if(pricePerUnit <= 0)
    		throw new InvalidPricePerUnitException();
    	if(logged_user == null)
    		throw new UnauthorizedException();
    	if(users.get(logged_user).getRole().equals(CASHIER))
    		throw new UnauthorizedException();
    	if(productTypes.values().stream().map(p -> p.getBarCode()).anyMatch(bc -> bc.equals(productCode)))
    		return -1; 

    	Integer id;
    	if(productTypes.keySet().size() == 0)
    		id = 1;
    	else
    		id = Collections.max(productTypes.keySet()) + 1;
    	
    	ProductTypeImpl newProduct = new ProductTypeImpl(id, description, productCode, pricePerUnit, note);
    	
    	boolean successful = pm.store(newProduct);
    	if(!successful)
    		return -1;
    	
    	productTypes.put(id, newProduct);   	
    	
        return id;
    }

    @Override
    public boolean updateProduct(Integer id, String newDescription, String newCode, double newPrice, String newNote) throws InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
    	if(id == null)
    		throw new InvalidProductIdException();
    	if(id <= 0)
    		throw new InvalidProductIdException();
    	if(newDescription == null)
    		throw new InvalidProductDescriptionException();
    	if(newDescription.equals(""))
    		throw new InvalidProductDescriptionException();
    	if(newCode == null)
    		throw new InvalidProductCodeException();
    	if(newCode.equals(""))
    		throw new InvalidProductCodeException();
    	if(ProductTypeImpl.validateProductCode(newCode) == false)
    		throw new InvalidProductCodeException();
;    	if(newPrice <= 0)
    		throw new InvalidPricePerUnitException();
    	if(logged_user == null)
    		throw new UnauthorizedException();
    	if(users.get(logged_user).getRole().equals(CASHIER))
    		throw new UnauthorizedException();
    	if(!productTypes.containsKey(id))
    		return false;
    	
    	boolean sameBarCode = productTypes
    							.values()
    							.stream()
    							.filter(p -> p.getId() != id) // I don't consider the product I want to update
    							.map(p -> p.getBarCode())
    							.anyMatch(bc -> bc.equals(newCode));
    	
    	if(sameBarCode)
    		return false; 
    	
    	ProductTypeImpl updatedProduct = new ProductTypeImpl(id, newDescription, newCode, newPrice, newNote);
    	
    	boolean successful = pm.update(updatedProduct);
    	if(!successful)
    		return false;
    	productTypes.replace(id, updatedProduct);
    	
        return true;
    }

    @Override
    public boolean deleteProductType(Integer id) throws InvalidProductIdException, UnauthorizedException {
    	if(id == null)
    		throw new InvalidProductIdException();
    	if(id <= 0)
    		throw new InvalidProductIdException();
    	if(logged_user == null)
    		throw new UnauthorizedException();
    	if(users.get(logged_user).getRole().equals(CASHIER))
    		throw new UnauthorizedException();
    	
    	boolean successful = pm.delete(productTypes.get(id));
    	if(!successful)
    		return false;
    	
    	productTypes.remove(id);    		
    		
        return true;
    }

    @Override
    public List<ProductType> getAllProductTypes() throws UnauthorizedException {
    	if(logged_user == null)
    		throw new UnauthorizedException();
    	if(users.get(logged_user).getRole().equals(CASHIER))
    		throw new UnauthorizedException();
    	
    	return productTypes.values().stream().collect(Collectors.toList());
    }

    @Override
    public ProductType getProductTypeByBarCode(String barCode) throws InvalidProductCodeException, UnauthorizedException {
    	if(barCode == null)
    		throw new InvalidProductCodeException();
    	if(barCode.equals(""))
    		throw new InvalidProductCodeException();
    	if(ProductTypeImpl.validateProductCode(barCode) == false)
    		throw new InvalidProductCodeException();
    	if(logged_user == null)
    		throw new UnauthorizedException();
    	if(users.get(logged_user).getRole().equals(CASHIER))
    		throw new UnauthorizedException();    	
    	
        return productTypes.values().stream().filter(p -> p.getBarCode().equals(barCode)).findFirst().orElse(null);
    }

    @Override
    public List<ProductType> getProductTypesByDescription(String description) throws UnauthorizedException {
    	if(logged_user == null)
    		throw new UnauthorizedException();
    	if(users.get(logged_user).getRole().equals(CASHIER))
    		throw new UnauthorizedException();  
    	if(description != null) {
    		return productTypes
    				.values()
    				.stream()
    				.filter(p -> p.getProductDescription().contains(description))
    				.collect(Collectors.toList());
    	}
    	else {
    		return productTypes
    				.values()
    				.stream()
    				.filter(p -> p.getProductDescription().contains(""))
    				.collect(Collectors.toList());
    	}
    }

    @Override
    public boolean updateQuantity(Integer productId, int toBeAdded) throws InvalidProductIdException, UnauthorizedException {
        if(logged_user==null || !(users.get(logged_user).getRole().equals("Administrator") || users.get(logged_user).getRole().equals("ShopManager")))
    		throw new UnauthorizedException();
    	if(productId==null || productId<=0)
    		throw new InvalidProductIdException();
    	
    	
    	ProductTypeImpl product = productTypes.get(productId);
    	if(product==null)
    		return false;
		
		//"The product should have a location assigned to it." so if it doesn't I return false
    	if(product.getLocation()==null || product.getLocation().equals(""))
    		return false;
		
    	int quantity = product.getQuantity();
        quantity+=toBeAdded;
        if(quantity<0)
        	return false;
        
    	product.setQuantity(quantity);

		boolean successful = pm.update(product);
    	if(!successful)
    		return false;
    	
    	return true;
    }

    @Override
    public boolean updatePosition(Integer productId, String newPos) throws InvalidProductIdException, InvalidLocationException, UnauthorizedException {
        if(logged_user==null || !(users.get(logged_user).getRole().equals("Administrator") || users.get(logged_user).getRole().equals("ShopManager")))
    		throw new UnauthorizedException();
    	if(productId==null || productId<=0)
    		throw new InvalidProductIdException();
		
    	//"If <newPos> is null or empty it should reset the position of given product type." : so I reset it to the empty string
    	if(newPos==null)
    		newPos="";
    	if(!newPos.equals("") && !PositionImpl.isValid(newPos))
    		throw new InvalidLocationException();
    	
    	if(!newPos.equals("")) {
	    	final String pos = newPos;
	    	boolean hasPosition = productTypes.values().stream().map(p -> p.getLocation()).anyMatch(l -> l.equals(pos));
	
			if(hasPosition)
				return false; 
    	}
    	
		PositionImpl position;
    	if(!positions.contains(newPos)) {
    		position = new PositionImpl(newPos);
    		positions.add(position);
    	}
    	
    	ProductTypeImpl product = productTypes.get(productId);
    	if(product==null)
    		return false;
		
    	product.setLocation(newPos);

		boolean successful = pm.update(product);
    	if(!successful)
    		return false;
    	
    	return true;
    }

    @Override
    public Integer issueOrder(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
    	if(logged_user==null || !(users.get(logged_user).getRole().equals("Administrator") || users.get(logged_user).getRole().equals("ShopManager")))
    		throw new UnauthorizedException();

    	
    	if(productCode==null||productCode.equals("") || !ProductTypeImpl.validateProductCode(productCode))
    		throw new InvalidProductCodeException();
    	if(quantity<=0)
    		throw new InvalidQuantityException();
    	if(pricePerUnit<=0)
    		throw new InvalidPricePerUnitException();
    	
    	boolean sameBarCode = productTypes
							.values()
							.stream()
							.map(p -> p.getBarCode())
							.anyMatch(bc -> bc.equals(productCode));

		if(!sameBarCode)
			return -1; //the product does not exist
		
		Integer id;
    	if(orders.keySet().size() == 0)
    		id = 1;
    	else
    		id = Collections.max(orders.keySet()) + 1;
    	
    	OrderImpl newOrder = new OrderImpl(id, balance.getLastId(), productCode, pricePerUnit, quantity, "ISSUED/ORDERED");
    	orders.put(id, newOrder);

		boolean successful = pm.store(newOrder);
    	if(!successful)
    		return -1;
        
    	return id;
    }

    @Override
    public Integer payOrderFor(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
    	double toBeAdded;
    	if(logged_user==null || !(users.get(logged_user).getRole().equals("Administrator") || users.get(logged_user).getRole().equals("ShopManager")))
    		throw new UnauthorizedException();
		
    	
    	if(productCode==null||productCode.equals("") || !ProductTypeImpl.validateProductCode(productCode))
    		throw new InvalidProductCodeException();
    	if(quantity<=0)
    		throw new InvalidQuantityException();
    	if(pricePerUnit<=0)
    		throw new InvalidPricePerUnitException();
    	
    	toBeAdded=-(pricePerUnit*quantity);
    	if(!recordBalanceUpdate(toBeAdded))
    		return -1;
    	
    	if(getProductTypeByBarCode(productCode)==null)
    		return -1;
    	
    	Integer id;
    	if(orders.keySet().size() == 0)
    		id = 1;
    	else
    		id = Collections.max(orders.keySet()) + 1;
    	
    	OrderImpl newOrder = new OrderImpl(id, balance.getLastId(), productCode, pricePerUnit, quantity, "PAYED");
    	orders.put(id, newOrder);

		boolean successful = pm.store(newOrder);
    	if(!successful)
    		return -1;

    	return id;
    }

    @Override
    public boolean payOrder(Integer orderId) throws InvalidOrderIdException, UnauthorizedException {
        if(logged_user==null || !(users.get(logged_user).getRole().equals("Administrator") || users.get(logged_user).getRole().equals("ShopManager")))
    		throw new UnauthorizedException();
    	if(orderId==null || orderId<=0)
    		throw new InvalidOrderIdException();
    	
    	OrderImpl order = orders.get(orderId);
    	
    	if(order.getStatus().equals("PAYED"))
    		return true;	//if the order is in the PAYED state, the method has no effect
    	
    	if(order== null || !order.getStatus().equals("ISSUED/ORDERED"))
    		return false;
		
    	double toBeAdded=-(order.getPricePerUnit()*order.getQuantity());
    	if(!recordBalanceUpdate(toBeAdded))
    		return false;
    	
    	order.setStatus("PAYED");

		boolean successful = pm.update(order);
    	if(!successful)
    		return false;
    	
    	return true;
    }

    @Override
    public boolean recordOrderArrival(Integer orderId) throws InvalidOrderIdException, UnauthorizedException, InvalidLocationException {
        if(logged_user==null || !(users.get(logged_user).getRole().equals("Administrator") || users.get(logged_user).getRole().equals("ShopManager")))
    		throw new UnauthorizedException();
    	if(orderId==null || orderId<=0)
    		throw new InvalidOrderIdException();
    	
    	OrderImpl order = orders.get(orderId);
    	if(order==null)
    		return false;
		if(order.getStatus().equals("COMPLETED"))
    		return true;
    	else if(!order.getStatus().equals("PAYED"))
    		return false;
    	
    	
		Optional<ProductTypeImpl> product = productTypes.values().stream().filter(p -> p.getBarCode().equals(order.getProductCode())).findFirst();
    	if(product.isPresent()) {
			if(product.get().getLocation()==null || product.get().getLocation().equals(""))
				throw new InvalidLocationException();
		}
		else { 
			return false;
		}
			
		int quantity;
    	quantity=product.get().getQuantity(); 
    	quantity+=order.getQuantity();
    	product.get().setQuantity(quantity);
    	
    	order.setStatus("COMPLETED");

		boolean successful = pm.update(order);
    	if(!successful)
    		return false;
    	
    	return true;
    }

    @Override
    public boolean recordOrderArrivalRFID(Integer orderId, String RFIDfrom) throws InvalidOrderIdException, UnauthorizedException, InvalidLocationException, InvalidRFIDException {
    	if(logged_user==null || !(users.get(logged_user).getRole().equals("Administrator") || users.get(logged_user).getRole().equals("ShopManager")))
    		throw new UnauthorizedException();
    	if(orderId==null || orderId<=0)
    		throw new InvalidOrderIdException();
    	
    	OrderImpl order = orders.get(orderId);
    	if(order==null)
    		return false;
		if(order.getStatus().equals("COMPLETED"))
    		return true;
    	else if(!order.getStatus().equals("PAYED"))
    		return false;
		
		Long RFIDnum = null;
    	if(ProductImpl.validateRFID(RFIDfrom)) {
    		RFIDnum = Long.parseLong(RFIDfrom);
			if(products.get(String.format("%012d",RFIDnum))!=null)
				throw new InvalidRFIDException();
    	}
    	else
    		throw new InvalidRFIDException();
    	
    	
    	
		Optional<ProductTypeImpl> product = productTypes.values().stream().filter(p -> p.getBarCode().equals(order.getProductCode())).findFirst();
    	if(product.isPresent()) {
			if(product.get().getLocation()==null || product.get().getLocation().equals(""))
				throw new InvalidLocationException();
		}
		else { 
			return false;
		}
			
		int quantity;
    	quantity=product.get().getQuantity(); 
    	quantity+=order.getQuantity();
    	product.get().setQuantity(quantity);
    	
    	Integer productTypeId=null;
    	ProductTypeImpl productType=productTypes.values().stream().filter(p -> p.getBarCode().equals(order.getProductCode())).findFirst().orElse(null);
    	if(productType!=null)
    		productTypeId=productType.getId();
    	
    	for(int i=0; i<order.getQuantity(); i++) {
    		//franco: persistency on products is needed, otherwise calling pm.update(product) throws exceptions (the csv file itself is never created)
    		ProductImpl p = new ProductImpl(String.format("%012d",RFIDnum+i), productTypeId);
    		products.put(String.format("%012d",RFIDnum+i), p);
    		if(!pm.store(p))
    			return false;
    	}
    	
    	order.setStatus("COMPLETED");

		boolean successful = pm.update(order);
    	if(!successful)
    		return false;
    	
    	return true;
    }
    
    @Override
    public List<Order> getAllOrders() throws UnauthorizedException {
        if(logged_user==null || !(users.get(logged_user).getRole().equals("Administrator") || users.get(logged_user).getRole().equals("ShopManager")))
    		throw new UnauthorizedException();
    	
        return orders.values().stream().collect(Collectors.toList());
    }

    @Override
    public Integer defineCustomer(String customerName) throws InvalidCustomerNameException, UnauthorizedException {
    	
    	if(logged_user == null || !users.get(logged_user).checkRole(ADMIN, SHOPMANAGER, CASHIER)) 
    		throw new UnauthorizedException();

		if (customerName == null || customerName.equals(""))
			throw new InvalidCustomerNameException();
		
		int[] id = { 0 };
		boolean[] nameAlreadyExists = { false } ;
		customers.forEach( (k, v) -> {
			if (k > id[0])
				id[0] = k;
			if (customerName == v.getCustomerName())
				nameAlreadyExists[0] = true;
		});
		int newId = id[0] + 1;
		
		if (nameAlreadyExists[0]) // The customer's name should be unique.
			return -1;
		
		Customer c = new CustomerImpl(newId, customerName);
		customers.put(newId, c);
		return newId;
    }

    @Override
    public boolean modifyCustomer(Integer id, String newCustomerName, String newCustomerCard) throws InvalidCustomerNameException, InvalidCustomerCardException, InvalidCustomerIdException, UnauthorizedException {
    	
    	if(logged_user == null || !users.get(logged_user).checkRole(ADMIN, SHOPMANAGER, CASHIER)) 
    		throw new UnauthorizedException();
    	
    	if(id == null || id <= 0)
    		throw new InvalidCustomerIdException();
    	
    	if (newCustomerName == null || newCustomerName.equals(""))
			throw new InvalidCustomerNameException();
    	
    	if (newCustomerCard != null && newCustomerCard != "" && !newCustomerCard.matches("[0-9]{10}"))
			throw new InvalidCustomerCardException();
    	
    	Customer customer = customers.get(id);
    	if (customer == null)
    		return false;
		
    	if (newCustomerCard != null) {
    		
    		String oldCustomerCard = customer.getCustomerCard();
    		customerCards.put(oldCustomerCard, null);
    		
    		if (newCustomerCard == "")
    			customer.setCustomerCard("");
    		else {
    			if (!attachCardToCustomer(newCustomerCard, id)) {
    				customerCards.put(oldCustomerCard, getCustomer(id)); // rolling back
    				return false;
    			}
    		}
    	}
    	
		customer.setCustomerName(newCustomerName);
		return true;
    }

    @Override
    public boolean deleteCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
    	
    	if(logged_user == null || !users.get(logged_user).checkRole(ADMIN, SHOPMANAGER, CASHIER)) 
    		throw new UnauthorizedException();
    	
    	if(id == null || id <= 0)
    		throw new InvalidCustomerIdException();
    	
    	if (getCustomer(id) != null) {
    		customers.remove(id); // customer exists and is deleted
    		return true;
    	} else
    		return false; // customer does not exist
    }

    @Override
    public Customer getCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
        
    	if(logged_user == null || !users.get(logged_user).checkRole(ADMIN, SHOPMANAGER, CASHIER)) 
    		throw new UnauthorizedException();
    	
    	if(id == null || id <= 0)
    		throw new InvalidCustomerIdException();
    	
    	return customers.get(id);
    }

    @Override
    public List<Customer> getAllCustomers() throws UnauthorizedException {
    	
    	if(logged_user == null || !users.get(logged_user).checkRole(ADMIN, SHOPMANAGER, CASHIER)) 
    		throw new UnauthorizedException();
    	
    	List<Customer> res = new ArrayList<Customer>();
    	
    	customers.forEach( (k, v) -> {
			res.add(v);
		});
    	
        return res;
    }

    @Override
    public String createCard() throws UnauthorizedException {

    	if(logged_user == null || !users.get(logged_user).checkRole(ADMIN, SHOPMANAGER, CASHIER)) 
    		throw new UnauthorizedException();

    	// retrieving the card with the highest ID
    	long[] maxId = { 0 };
		customerCards.forEach( (k, v) -> {
			if (Long.parseLong(k) > maxId[0])
				maxId[0] = Long.parseLong(k);
		});
    	
		// choosing maxID + 1 as new ID
		String newId = String.format("%10s", Long.toString(maxId[0] + 1)).replace(' ', '0');
		customerCards.put(newId, null);
        return newId;
    }

    @Override
    public boolean attachCardToCustomer(String customerCard, Integer customerId) throws InvalidCustomerIdException, InvalidCustomerCardException, UnauthorizedException {
    	
    	if(logged_user == null || !users.get(logged_user).checkRole(ADMIN, SHOPMANAGER, CASHIER)) 
    		throw new UnauthorizedException();
    	
    	if (customerId == null || customerId <= 0)
			throw new InvalidCustomerIdException();
    	
    	if (customerCard == null || !customerCard.matches("[0-9]{10}")) //customerCardId must be comprised of 10 digits
			throw new InvalidCustomerCardException();
    	
    	if (!customerCards.containsKey(customerCard) || customerCards.get(customerCard) != null)
    		return false; // card does not exist or already in use for a customer
    	
    	Customer customer = getCustomer(customerId);
    	if (customer == null)
    		return false; // customer does not exist
    	
    	customer.setCustomerCard(customerCard);
    	customerCards.put(customerCard, getCustomer(customerId));
    	return true;
    }

    @Override
    public boolean modifyPointsOnCard(String customerCard, int pointsToBeAdded) throws InvalidCustomerCardException, UnauthorizedException {
        
    	if(logged_user == null || !users.get(logged_user).checkRole(ADMIN, SHOPMANAGER, CASHIER)) 
    		throw new UnauthorizedException();
    	
    	if (customerCard == null || !customerCard.matches("[0-9]{10}")) //customerCardId must be comprised of 10 digits
			throw new InvalidCustomerCardException();
    	
    	if (!customerCards.containsKey(customerCard)) // no card with given code
    		return false;
    	
    	Customer c = customerCards.get(customerCard);
    	Integer pointsOnCard = c.getPoints();    	
    	if (pointsOnCard + pointsToBeAdded < 0) // not enough points on the card
    		return false;
    	
    	c.setPoints(pointsOnCard + pointsToBeAdded);
    	return true;
    }

    @Override
    public Integer startSaleTransaction() throws UnauthorizedException {
    	//check authorization
    	if(this.logged_user== null || !this.users.containsKey(logged_user))
    		throw new UnauthorizedException();
    	
    	if(!users.get(logged_user).checkRole(ADMIN, SHOPMANAGER, CASHIER)) 
    		throw new UnauthorizedException();
    	
    	
    	//create new transaction and add to structure
    	saleCounter++;
    	SaleTransactionImpl newSale = new SaleTransactionImpl(saleCounter);
        sales.put(saleCounter, newSale);
        
    	return newSale.getId();
    }

    @Override
    public boolean addProductToSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
    	/*
    	 * PERFORM CHECKS
    	 */
    	//check authorization
    	if(this.logged_user== null || !this.users.containsKey(logged_user)) 
    		throw new UnauthorizedException();
    	
    	if(!users.get(logged_user).checkRole(ADMIN, SHOPMANAGER, CASHIER)) 
    		throw new UnauthorizedException();
    	
    	
    	//check transactionId and SaleTransaction validness
    	if(transactionId == null)
    		throw new InvalidTransactionIdException();
    	
    	if(transactionId<=0) 
    		throw new InvalidTransactionIdException();
    	
    	
    	//check productCode product validness
    	if(productCode == "" || productCode == null || !ProductTypeImpl.validateProductCode(productCode)) 
    		throw new InvalidProductCodeException();
    	
    	
    	//check quantity validness and availability
    	if(amount < 0)
    		throw new InvalidQuantityException();
    	
    	Optional<ProductTypeImpl> to_add = productTypes.values().stream()
    			.filter(p -> p.getBarCode().equals(productCode)).findFirst();
    	if(!to_add.isPresent()) 
    		return false;
    	ProductTypeImpl prod = to_add.get();
    	
    	if(to_add.get().getQuantity()< amount)
    		return false;
    	
    	if(!sales.containsKey(transactionId)) 
    		return false;
    	
    	if(!sales.get(transactionId).checkState(SALE_STATE_OPEN))
    		return false;
    	
    	SaleTransactionImpl sale = sales.get(transactionId);
    	/*
    	 * END OF CHECKS
    	 */
    	
    	if(!sale.containsEntry(prod.getBarCode())) {
    		sale.addEntry(prod, amount);
    		
    	}else{
    		sale.updateEntry(productCode, amount);
    		
    	}
    	
    	//update available quantity of product (specified in api)
    	prod.updateQuantity(+amount);
    	
    	return true;
    }

    @Override
    public boolean addProductToSaleRFID(Integer transactionId, String RFID) throws InvalidTransactionIdException, InvalidRFIDException, InvalidQuantityException, UnauthorizedException{
        //franco: i implemented this "superficially" so that it passes the successful case since i need it for my tests (might be correct)
    	
    	//also big doubt: this method throws InvalidQuantityException, but in API there's no specification on when to throw it (and there's no quantity argument)
    	/*
    	 * PERFORM CHECKS
    	 */
    	//check authorization
    	if(this.logged_user== null || !this.users.containsKey(logged_user)) 
    		throw new UnauthorizedException();
    	
    	if(!users.get(logged_user).checkRole(ADMIN, SHOPMANAGER, CASHIER)) 
    		throw new UnauthorizedException();
    	
    	
    	//check transactionId validness
    	if(transactionId == null)
    		throw new InvalidTransactionIdException();
    	
    	if(transactionId<=0) 
    		throw new InvalidTransactionIdException();
    	
    	//check RFID validness
    	if(RFID==null)
    		throw new InvalidRFIDException();
    	if(!ProductImpl.validateRFID(RFID))
    		throw new InvalidRFIDException();
    	
    	//check return false cases + declare variables:
    	
    	//RFID doesn't exist
    	if(!products.containsKey(RFID))
    		return false;
    	
    	ProductImpl prod = products.get(RFID);
    	
    	//existence and state of the sale
    	if(!sales.containsKey(transactionId))
    		return false;
    	
    	SaleTransactionImpl sale = sales.get(transactionId);
    	
    	if(!sale.checkState(SALE_STATE_OPEN))
    		return false;
    	
    	//additional return false (not specified, but these ones are necessary right?
    	
    	//check that product is not already in another sale
    	if(prod.getTransactionId() != -1)
    		return false;
    	
    	//check that product is related to an existing ProductType (this should always pass, but good practice since you need ProductType info) 
    	if(!productTypes.containsKey(prod.getProductTypeId()))
    		return false;
    	
    	ProductTypeImpl productType = productTypes.get(prod.getProductTypeId());
    	/*
    	 * END OF CHECKS
    	 */
    	
    	if(!sale.containsEntry(productType.getBarCode())) {
    		sale.addEntry(productType, 1);
    		
    	}else{
    		sale.updateEntry(productType.getBarCode(), 1);
    		
    	}
    	
    	//update available quantity of product (specified in api)
    	productType.updateQuantity(+1);
    	
    	//set the transactionId field in the product (needed for checks in other RFID methods)
    	prod.setTransactionId(transactionId);
    	
    	return true;
    	
    }
    
    @Override
    public boolean deleteProductFromSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
    	/*
    	 * PERFORM CHECKS
    	 */
    	//check authorization
    	if(this.logged_user== null || !this.users.containsKey(logged_user)) 
    		throw new UnauthorizedException();
    	
    	if(!users.get(logged_user).checkRole(ADMIN, SHOPMANAGER, CASHIER)) 
    		throw new UnauthorizedException();
  
    	
    	//check transactionId and SaleTransaction validness
    	if(transactionId==null)
    		throw new InvalidTransactionIdException();
        	

    	if(transactionId<=0) 
    		throw new InvalidTransactionIdException();
    	
    	//check productCode product validness
    	if(productCode == "" || productCode == null || !ProductTypeImpl.validateProductCode(productCode)) 
    		throw new InvalidProductCodeException();
    	
    	
    	//check quantity validness and availability
    	if(amount < 0)
    		throw new InvalidQuantityException();
    	
    	Optional<ProductTypeImpl> to_add = productTypes.values().stream()
    			.filter(p -> p.getBarCode().equals(productCode)).findFirst();
    	if(!to_add.isPresent()) 
    		return false;
    	
    	ProductTypeImpl prod = to_add.get();
    	
    	if(!sales.containsKey(transactionId)) 
    		return false;
    	
    	if(!sales.get(transactionId).checkState(SALE_STATE_OPEN))
    		return false;
    	
    	if(!sales.get(transactionId).containsEntry(to_add.get().getBarCode()))
    		return false;
    	
    	if(sales.get(transactionId).getEntry(productCode).getAmount()< amount)
    		return false;
    	
    	SaleTransactionImpl sale = sales.get(transactionId);
    	
    	/*
    	 * END OF CHECKS
    	 */
    	
    	sale.updateEntry(productCode, -amount);
    	
    	
    	
    	//update available quantity of product (specified in api)
    	prod.updateQuantity(-amount);
    	
    	
    	return true;
    }

    @Override
    public boolean deleteProductFromSaleRFID(Integer transactionId, String RFID) throws InvalidTransactionIdException, InvalidRFIDException, InvalidQuantityException, UnauthorizedException{
    	if(transactionId == null)
    		throw new InvalidTransactionIdException();
    	if(transactionId <= 0)
    		throw new InvalidTransactionIdException();
    	if(RFID == null)
    		throw new InvalidRFIDException();
    	if(RFID.equals(""))
    		throw new InvalidRFIDException();
    	if(!ProductImpl.validateRFID(RFID))
    		throw new InvalidRFIDException();
    	if(logged_user== null)  
    		throw new UnauthorizedException();
    	if(!users.containsKey(logged_user))
    		throw new UnauthorizedException();
    	if(!users.get(logged_user).checkRole(ADMIN, SHOPMANAGER, CASHIER)) 
    		throw new UnauthorizedException();
    	
    	
    	if(!products.containsKey(RFID))
    		return false;
    	if(!products.get(RFID).getTransactionId().equals(transactionId))
    		return false;
    	if(!sales.containsKey(transactionId)) 
    		return false;
    	if(!sales.get(transactionId).checkState(SALE_STATE_OPEN))
    		return false;
    	
    	Integer productTypeId = products.get(RFID).getProductTypeId();
    	
    	if(!productTypes.containsKey(productTypeId))
    		return false;
    	String productCode = productTypes.get(productTypeId).getBarCode();
    	
    	Optional<ProductTypeImpl> productTypeToAdd = productTypes.values().stream()
    			.filter(p -> p.getBarCode().equals(productCode)).findFirst();
    	
    	if(!productTypeToAdd.isPresent()) 
    		return false;
    	if(!sales.get(transactionId).containsEntry(productTypeToAdd.get().getBarCode()))
    		return false;
    	if(sales.get(transactionId).getEntry(productCode).getAmount() < 1) //TODO: check if this is correct/useless
    		return false;
    	
    	sales.get(transactionId).updateEntry(productCode, -1);
    	
    	productTypeToAdd.get().updateQuantity(-1);
    	products.get(RFID).setTransactionId(-1);
    	    	
    	return true;
    }

    @Override
    public boolean applyDiscountRateToProduct(Integer transactionId, String productCode, double discountRate) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidDiscountRateException, UnauthorizedException {
    	/*
    	 * PERFORM CHECKS
    	 */
    	//check authorization
    	if(this.logged_user== null || !this.users.containsKey(logged_user)) 
    		throw new UnauthorizedException();
    	
    	if(!users.get(logged_user).checkRole(ADMIN, SHOPMANAGER, CASHIER)) 
    		throw new UnauthorizedException();
  
    	// check discountRate validness
    	
    	
    	if(discountRate >=1 || discountRate<0)
    		throw new InvalidDiscountRateException();
    	
    	//check transactionId and SaleTransaction validness
    	if(transactionId == null)
    		throw new InvalidTransactionIdException();
    	
    	if(transactionId<=0) 
    		throw new InvalidTransactionIdException();
    	
    	
    	//check productCode product validness
    	if(productCode == "" || productCode == null || !ProductTypeImpl.validateProductCode(productCode)) 
    		throw new InvalidProductCodeException();
    	
    	Optional<ProductTypeImpl> to_add = productTypes.values().stream()
    			.filter(p -> p.getBarCode().equals(productCode)).findFirst();
    	if(!to_add.isPresent()) 
    		return false;
    	
    	//check that said product is present as an entry in the sale transaction
    	if(!sales.get(transactionId).containsEntry(productCode))
    		return false;
    	
    	if(!sales.containsKey(transactionId)) 
    		return false;
    	
    	if(!sales.get(transactionId).checkState(SALE_STATE_OPEN))
    		return false;
    	/*
    	 * END OF CHECKS
    	 */
    	
    	sales.get(transactionId).updateEntry(productCode, discountRate);
    	
    	
    	return true;
    }

    @Override
    public boolean applyDiscountRateToSale(Integer transactionId, double discountRate) throws InvalidTransactionIdException, InvalidDiscountRateException, UnauthorizedException {
    	/*
    	 * PERFORM CHECKS
    	 */
    	//check authorization
    	if(this.logged_user== null || !this.users.containsKey(logged_user)) 
    		throw new UnauthorizedException();
    	
    	if(!users.get(logged_user).checkRole(ADMIN, SHOPMANAGER, CASHIER)) 
    		throw new UnauthorizedException();
    	
    	// check discountRate validness
    	if(discountRate >=1 || discountRate<0)
    		throw new InvalidDiscountRateException();
    	
    	//check transactionId and SaleTransaction validness
    	if(transactionId==null)
    		throw new InvalidTransactionIdException();
    	
    	if(transactionId<=0) 
    		throw new InvalidTransactionIdException();
    	
    	if(!sales.containsKey(transactionId)) 
    		return false;
    	
    	if(!sales.get(transactionId).checkState(SALE_STATE_OPEN,  SALE_STATE_CLOSED))
    		return false;
    	/*
    	 * END OF CHECKS
    	 */
    	
    	SaleTransactionImpl newSale = sales.get(transactionId);
    	newSale.setDiscountRate(discountRate);
    	
    	
    	
    	return true;
    }

    @Override
    public int computePointsForSale(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
    	/*
    	 * PERFORM CHECKS
    	 */
    	//check authorization
    	if(this.logged_user== null || !this.users.containsKey(logged_user)) 
    		throw new UnauthorizedException();
    	
    	if(!users.get(logged_user).checkRole(ADMIN, SHOPMANAGER, CASHIER)) 
    		throw new UnauthorizedException();
    	//check transactionId and SaleTransaction validness
    	if(transactionId == null)
    		throw new InvalidTransactionIdException();
    		
    	if(transactionId<=0) 
    		throw new InvalidTransactionIdException();
    	if(!sales.containsKey(transactionId))
    		return -1;
    	/*
    	 * END OF CHECKS
    	 */
    	return sales.get(transactionId).computePoints();
    }

    @Override
    public boolean endSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
    	/*
    	 * PERFORM CHECKS
    	 */
    	//check authorization
    	if(this.logged_user== null || !this.users.containsKey(logged_user)) 
    		throw new UnauthorizedException();
    	
    	if(!users.get(logged_user).checkRole(ADMIN, SHOPMANAGER, CASHIER)) 
    		throw new UnauthorizedException();
    	//check transactionId and SaleTransaction validness
    	if(transactionId == null)
    		throw new InvalidTransactionIdException();
    	
    	if(transactionId<=0) 
    		throw new InvalidTransactionIdException();
    	if(!sales.containsKey(transactionId))
    		return false;
    	if(sales.get(transactionId).checkState(SALE_STATE_CLOSED, SALE_STATE_PAYED))
    		return false;
    	/*
    	 * END OF CHECKS
    	 */
    	
    	SaleTransactionImpl sale = sales.get(transactionId);
    	sale.setState(SALE_STATE_CLOSED);
    	
    	for(TicketEntry e : sales.get(transactionId).getEntries()) {
    		if(!pm.update(productTypes.values().stream().filter(p -> p.getBarCode().equals(e.getBarCode())).findFirst().get()))
    			return false;
    		TicketEntryImpl to_store = new TicketEntryImpl(e.getBarCode(), e.getProductDescription(), e.getAmount(),
    				e.getPricePerUnit(), e.getDiscountRate(), sale.getEntryId(e.getBarCode()), sale.getId());
    		if(!pm.store(to_store))
    			return false;
    	}
    	
    	
    	if(!pm.store(sales.get(transactionId)))
    		return false;
    	
    	//persistency update on products :(
    	for(ProductImpl p : products.values().stream().filter(p-> p.getTransactionId() == transactionId ).collect(Collectors.toList())) {
    		if(!pm.update(p))
    			return false;
    	}
    	
    	return true;
    }

    @Override
    public boolean deleteSaleTransaction(Integer saleNumber) throws InvalidTransactionIdException, UnauthorizedException {
    	/*
    	 * PERFORM CHECKS
    	 */
    	//check authorization
    	if(this.logged_user== null || !this.users.containsKey(logged_user)) 
    		throw new UnauthorizedException();
    	
    	if(!users.get(logged_user).checkRole(ADMIN, SHOPMANAGER, CASHIER)) 
    		throw new UnauthorizedException();
    	//check transactionId and SaleTransaction validness
    	if(saleNumber==null)
    		throw new InvalidTransactionIdException();
    	
    	if(saleNumber<=0) 
    		throw new InvalidTransactionIdException();
    	if(!sales.containsKey(saleNumber))
    		return false;
    	if(sales.get(saleNumber).checkState(SALE_STATE_PAYED))
    		return false;
    	/*
    	 * END OF CHECKS
    	 */
    	
    	/*
    	 * if transaction is not yet closed (not in db), just revert the product quantities to their original values
    	 * otherwise also update productTypes in db and remove the whole transaction
    	 */
    	SaleTransactionImpl sale = sales.get(saleNumber);
    	boolean closed = sale.checkState(SALE_STATE_CLOSED); 
    	for(TicketEntry e : sale.getEntries()) {
    		ProductTypeImpl prod = productTypes.values().stream().filter(p -> p.getBarCode().equals(e.getBarCode())).findFirst().get();
    		prod.updateQuantity(-e.getAmount());
    		if(closed) {
    			if(!pm.update(prod))
    				return false;
    			TicketEntryImpl to_update = new TicketEntryImpl(e.getBarCode(), e.getProductDescription(), e.getAmount(),
        				e.getPricePerUnit(), e.getDiscountRate(), sale.getEntryId(e.getBarCode()), sale.getId());
        		if(!pm.delete(to_update))
        			return false;
    			
    		}
    	}
    	if(closed) {
			if(!pm.delete(sale))
				return false;
		}
    	sales.get(saleNumber).clearEntries();
    	sales.remove(saleNumber);
    	
    	//reverting the ProductImpl salid field to default and updating their persistency if the transaction was closed
    	for(ProductImpl p : products.values().stream().filter(p-> p.getTransactionId() == saleNumber ).collect(Collectors.toList())) {
    		p.setTransactionId(-1);
    		
    		if(closed) {
    			if(!pm.update(p))
    				return false;
    		}
    	}
    	
    	
    	return true;
    }

    @Override
    public SaleTransaction getSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
    	/*
    	 * PERFORM CHECKS
    	 */
    	//check authorization
    	if(this.logged_user== null || !this.users.containsKey(logged_user)) 
    		throw new UnauthorizedException();
    	
    	if(!users.get(logged_user).checkRole(ADMIN, SHOPMANAGER, CASHIER)) 
    		throw new UnauthorizedException();
    	//check transactionId and SaleTransaction validness
    	if(transactionId==null)
    		throw new InvalidTransactionIdException();
    	if(transactionId<=0) 
    		throw new InvalidTransactionIdException();
    	if(!sales.containsKey(transactionId))
    		return null;
    	if(!sales.get(transactionId).checkState(SALE_STATE_CLOSED))
    		return null;
    	/*
    	 * END OF CHECKS
    	 */
    	
    	return sales.get(transactionId);
    }

    @Override
    public Integer startReturnTransaction(Integer saleNumber) throws /*InvalidTicketNumberException,*/InvalidTransactionIdException, UnauthorizedException {
    	/*
    	 * PERFORM CHECKS
    	 */
    	
    	//check authorization
    	if(this.logged_user== null || !this.users.containsKey(logged_user)) 
    		throw new UnauthorizedException();
    	
    	if(!users.get(logged_user).checkRole(ADMIN, SHOPMANAGER, CASHIER)) 
    		throw new UnauthorizedException();
    	//check transactionId and SaleTransaction validness
    	if(saleNumber==null)
    		throw new InvalidTransactionIdException();
    	if(saleNumber<=0) 
    		throw new InvalidTransactionIdException();
    	if(!sales.containsKey(saleNumber))
    		return -1;
    	if(!sales.get(saleNumber).checkState(SALE_STATE_PAYED))
    		return -1;
    	/*
    	 * END OF CHECKS
    	 */
    	returnCounter++;
    	ReturnTransactionImpl newReturn = new ReturnTransactionImpl(returnCounter, saleNumber);
    	newReturn.setDiscountRate(sales.get(saleNumber).getDiscountRate());
        returns.put(returnCounter, newReturn);
    	
    	
    	return returnCounter;
    }

    @Override
    public boolean returnProduct(Integer returnId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
    	/*
    	 * PERFORM CHECKS
    	 */
    	//System.out.println(amount +" " + returnId + " " + productCode + " rcontains: " + returns.containsKey(returnId));
    	//check authorization
    	if(this.logged_user== null || !this.users.containsKey(logged_user)) 
    		throw new UnauthorizedException();
    	
    	if(!users.get(logged_user).checkRole(ADMIN, SHOPMANAGER, CASHIER)) 
    		throw new UnauthorizedException();
  
    	
    	//check transactionId and SaleTransaction validness
    	if(returnId==null)
    		throw new InvalidTransactionIdException();
    	if(returnId<=0) 
    		throw new InvalidTransactionIdException();
    	
    	
    	
    	//check productCode product validness
    	
    	if(productCode == "" || productCode == null || !ProductTypeImpl.validateProductCode(productCode)) 
    		throw new InvalidProductCodeException();
    	
    	//check quantity validness and availability
    	if(amount <= 0)
    		throw new InvalidQuantityException();
    	int tot_amount = amount;
    	if(!returns.containsKey(returnId)) 
    		return false;

    	if(!sales.get(returns.get(returnId).getSaleId()).containsEntry(productCode))
    		return false;
    	if(returns.get(returnId).containsEntry(productCode))
    		tot_amount += returns.get(returnId).getEntry(productCode).getAmount();
    	if(sales.get(returns.get(returnId).getSaleId()).getEntry(productCode).getAmount()<tot_amount) 
    		return false;
    	
    	Optional<ProductTypeImpl> to_add = productTypes.values().stream()
    			.filter(p -> p.getBarCode().equals(productCode)).findFirst();
    	if(!to_add.isPresent()) 
    		return false;
    	
    	
    	
    	
    	if(!returns.get(returnId).checkState(SALE_STATE_OPEN))
    		return false;
    	
    	/*
    	 * END OF CHECKS
    	 */
    	
    	
    	if(!returns.get(returnId).containsEntry(to_add.get().getBarCode())) {
    		returns.get(returnId).addEntry(to_add.get(), amount,sales.get(returns.get(returnId).getSaleId()).getEntry(productCode).getDiscountRate());
    	}else{
    		returns.get(returnId).updateEntry(productCode, amount);
    	}
    	
    	
    	return true;
    }

    @Override
    public boolean returnProductRFID(Integer returnId, String RFID) throws InvalidTransactionIdException, InvalidRFIDException, UnauthorizedException 
    {
    	/*
    	 * PERFORM CHECKS
    	 */
    	//System.out.println(amount +" " + returnId + " " + productCode + " rcontains: " + returns.containsKey(returnId));
    	//check authorization
    	if(this.logged_user== null || !this.users.containsKey(logged_user)) 
    		throw new UnauthorizedException();
    	
    	if(!users.get(logged_user).checkRole(ADMIN, SHOPMANAGER, CASHIER)) 
    		throw new UnauthorizedException();

    	//check transactionIdexception
    	if(returnId==null)
    		throw new InvalidTransactionIdException();
    	if(returnId<=0) 
    		throw new InvalidTransactionIdException();
    	//check RFID exception
    	if(RFID==null)
    		throw new InvalidRFIDException();
    	if(!ProductImpl.validateRFID(RFID))
    		throw new InvalidRFIDException();
    	
    	//check return false invalid values and fetch objects
    	
    	if(!returns.containsKey(returnId))
    		return false;
    	
    	ReturnTransactionImpl ret = returns.get(returnId);
    	SaleTransactionImpl sale = sales.get(ret.getSaleId());
    	
    	if(!products.containsKey(RFID))
    		return false;
    	
    	ProductImpl to_add = products.get(RFID);
    	
    	//this RFID is not associated to the sale
    	if(to_add.getTransactionId() != sale.getId())
    		return false;
    
    	//this RFID is not already associated to a return (not specified but necessary)
    	if(to_add.getReturnId() != -1)
    		return false;
    
    	//check productType exists (not clearly specified but necessary)
    	if(!productTypes.containsKey(to_add.getProductTypeId()))
    		return false;
    	
    	ProductTypeImpl prod = productTypes.get(to_add.getProductTypeId());
    	
    	//additional (should be unnecessary) check about productType in sale 
    	if(!sale.containsEntry(prod.getBarCode()))
    		return false;
    	
    	/*
    	 * END OF CHECKS
    	 */
    	
    	//update/add the return Entry
    	if(!ret.containsEntry(prod.getBarCode())) {
    		returns.get(returnId).addEntry(prod, 1, sale.getEntry(prod.getBarCode()).getDiscountRate());
    	}else{
    		ret.updateEntry(prod.getBarCode(), 1);
    	}
    	
    	//update the RFID entry 
    	to_add.setReturnId(returnId);
    	
        return true;
    }


    @Override
    public boolean endReturnTransaction(Integer returnId, boolean commit) throws InvalidTransactionIdException, UnauthorizedException {
    	/*
    	 * PERFORM CHECKS
    	 */
    	//check authorization
    	if(this.logged_user== null || !this.users.containsKey(logged_user)) 
    		throw new UnauthorizedException();
    	
    	if(!users.get(logged_user).checkRole(ADMIN, SHOPMANAGER, CASHIER)) 
    		throw new UnauthorizedException();
    	//check transactionId and SaleTransaction validness
    	if(returnId == null)
    		throw new InvalidTransactionIdException();
    	if(returnId<=0) 
    		throw new InvalidTransactionIdException();
    	if(!returns.containsKey(returnId))
    		return false;
    	if(returns.get(returnId).checkState(SALE_STATE_CLOSED, SALE_STATE_PAYED))
    		return false;
    	/*
    	 * END OF CHECKS
    	 */
    	
    	//if not committed just revert the state of products and clear everything
    	ReturnTransactionImpl ret = returns.get(returnId);
    	ret.setState(SALE_STATE_CLOSED);
    	
    	List<ProductImpl> retprods = products.values().stream().filter(p-> p.getReturnId() == returnId ).collect(Collectors.toList());
    	
    	ret.setCommit(commit);
    	if(commit == false) {
    		for(ProductImpl p : retprods) {
        		p.setReturnId(-1);
        	}
    		return true;
    	}
    	SaleTransactionImpl s_to_update = sales.get(returns.get(returnId).getSaleId());
    	
    	for(TicketEntry e : ret.getEntries()) {
    		ProductTypeImpl p_to_update = productTypes.values().stream().filter(p -> p.getBarCode().equals(e.getBarCode())).findFirst().get();
    		p_to_update.updateQuantity(-e.getAmount());
    		if(!pm.update(p_to_update))
    			return false;
    		ReturnEntryImpl to_store = new ReturnEntryImpl(e.getBarCode(), e.getProductDescription(), e.getAmount(), e.getPricePerUnit(), 
    				e.getDiscountRate(),ret.getEntryId(e.getBarCode()), ret.getSaleId(), ret.getId());
    		if(!pm.store(to_store))
    			return false;
    	}
    	
    	
    	s_to_update.applyReturn(returns.get(returnId).getEntries());
    	
    	
    	if(!pm.store(returns.get(returnId)))
    		return false;
    	//update sale's persistency
    	if(!pm.update(s_to_update))
    		return false;
    	//update sale's entries persistency
    	for(TicketEntry e : s_to_update.getEntries()) {
    		TicketEntryImpl to_update = new TicketEntryImpl(e.getBarCode(), e.getProductDescription(), e.getAmount(),
    				e.getPricePerUnit(), e.getDiscountRate(), s_to_update.getEntryId(e.getBarCode()), s_to_update.getId());
    		if(!pm.update(to_update))
    			return false;
    	}
    	//update products persitency
    	
    	for(ProductImpl p : retprods) {
    		if(!pm.update(p))
    			return false;
    	}
    	
    	return true;
    }

    @Override
    public boolean deleteReturnTransaction(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {
    	/*
    	 * PERFORM CHECKS
    	 */
    	//check authorization
    	if(this.logged_user== null || !this.users.containsKey(logged_user)) 
    		throw new UnauthorizedException();
    	
    	if(!users.get(logged_user).checkRole(ADMIN, SHOPMANAGER, CASHIER)) 
    		throw new UnauthorizedException();
    	//check transactionId and SaleTransaction validness
    	if(returnId==null)
    		throw new InvalidTransactionIdException();
    	if(returnId<=0) 
    		throw new InvalidTransactionIdException();
    	if(!returns.containsKey(returnId))
    		return false;
    	if(returns.get(returnId).checkState(SALE_STATE_PAYED))
    		return false;
    	/*
    	 * END OF CHECKS
    	 */
    	
    	//if the return is not committed, just remove it from map since it has not affected other data structures ye
    	if(!returns.get(returnId).getCommit()) {
    		returns.get(returnId).clearEntries();
    		returns.remove(returnId);
    		return true;
    	}
    	//otherwise update productTypes and the SaleTransaction
    	SaleTransactionImpl s_to_update = sales.get(returns.get(returnId).getSaleId());
    	ReturnTransactionImpl r_to_delete = returns.get(returnId);
    	
    	for(TicketEntry e : r_to_delete.getEntries()) {
    		ProductTypeImpl p_to_update = productTypes.values().stream().filter(p -> p.getBarCode().equals(e.getBarCode())).findFirst().get();
    		p_to_update.updateQuantity(e.getAmount());
    		if(!pm.update(p_to_update))
    			return false;
    		ReturnEntryImpl r = new ReturnEntryImpl("", "", 
    				0, 0, 0,0, s_to_update.getId(), r_to_delete.getId());
    		if(!pm.delete(r))
    			return false;
    	}
    	
    	s_to_update.undoReturn(returns.get(returnId).getEntries());
    	
    	if(!pm.delete(r_to_delete))
    		return false;
    	
    	
    	if(!pm.update(s_to_update))
    		return false;
    	
    	//update sale's entries persistency
    	for(TicketEntry e : s_to_update.getEntries()) {
    		TicketEntryImpl to_update = new TicketEntryImpl(e.getBarCode(), e.getProductDescription(), e.getAmount(),
    				e.getPricePerUnit(), e.getDiscountRate(), s_to_update.getEntryId(e.getBarCode()), s_to_update.getId());
    		if(!pm.update(to_update))
    			return false;
    	}
    	
    	//deal with rfids 
    	for(ProductImpl p : products.values().stream().filter(p-> p.getReturnId() == returnId).collect(Collectors.toList())) {
    		p.setReturnId(-1);
    		if(!pm.update(p))
    			return false;
    	}
    	returns.get(returnId).clearEntries();
		returns.remove(returnId);
    	
    	return true;	
    	
        
    }

    @Override
    public double receiveCashPayment(Integer ticketNumber, double cash) throws InvalidTransactionIdException, InvalidPaymentException, UnauthorizedException {
    	
    	if(logged_user == null || !users.get(logged_user).checkRole(ADMIN, SHOPMANAGER, CASHIER)) 
    		throw new UnauthorizedException();
    	
    	if(ticketNumber == null || ticketNumber <= 0)
    		throw new InvalidTransactionIdException();
    	
    	if(cash <= 0)
    		throw new InvalidPaymentException();
    	
    	SaleTransactionImpl s = sales.get(ticketNumber);
    	if (s == null)
    		return -1;
    	
    	double price = s.getPrice();
    	if (price > cash)
    		return -1;
    	
    	Integer balanceId = balance.getLastId() + 1;    		
    	BalanceOperationImpl balanceOperation = new BalanceOperationImpl(balanceId, LocalDate.now(), price, CREDIT);
    	if(pm.store(balanceOperation)) {
	    	balance.updateAmount(price);
	    	balance.recordBalanceOperation(balanceOperation);
	    	s.setState(SALE_STATE_PAYED);
	    	s.setBalanceOperationId(balanceId);
	    	//franco: persistency needed on the transaction modification
	    	if(!pm.update(s))
	    		return -1;
	    	
	    	return cash - price;
    	} else
    		return -1;
    }

    @Override
    public boolean receiveCreditCardPayment(Integer ticketNumber, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {
        
    	if(logged_user == null || !users.get(logged_user).checkRole(ADMIN, SHOPMANAGER, CASHIER)) 
    		throw new UnauthorizedException();
    	
    	if(ticketNumber == null || ticketNumber <= 0)
    		throw new InvalidTransactionIdException();
    	
    	if(!CreditCardManagerImpl.validateCard(creditCard))
    		throw new InvalidCreditCardException();
    	
    	SaleTransactionImpl s = sales.get(ticketNumber);
    	if (s == null)
    		return false;
    	
    	double price = s.getPrice();
    	
    	Integer balanceId = balance.getLastId() + 1;    		
    	BalanceOperationImpl balanceOperation = new BalanceOperationImpl(balanceId, LocalDate.now(), price, CREDIT);
    	if(pm.store(balanceOperation) && CreditCardManagerImpl.withdrawFromCard(creditCard, price)) {
	    	balance.updateAmount(price);
	    	balance.recordBalanceOperation(balanceOperation);
	    	s.setState(SALE_STATE_PAYED);
	    	s.setBalanceOperationId(balanceId);
	    	//franco: persistency needed on the transaction modification
	    	if(!pm.update(s))
	    		return false;
	    	
	    	return true;
    	} else
    		return false;
    }

    @Override
    public double returnCashPayment(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {
    	
    	if(logged_user == null || !users.get(logged_user).checkRole(ADMIN, SHOPMANAGER, CASHIER)) 
    		throw new UnauthorizedException();
    	
    	if(returnId == null || returnId <= 0)
    		throw new InvalidTransactionIdException();
    	
    	ReturnTransactionImpl r = returns.get(returnId);
    	if (r == null || !r.checkState(SALE_STATE_CLOSED))
    		return -1;
    	
    	double toRefund = r.getPrice();
    	Integer balanceId = balance.getLastId() + 1;    		
    	BalanceOperationImpl balanceOperation = new BalanceOperationImpl(balanceId, LocalDate.now(), toRefund, DEBIT);
    	if(pm.store(balanceOperation)) {
	    	balance.updateAmount(-toRefund);
	    	balance.recordBalanceOperation(balanceOperation);
	    	r.setState(SALE_STATE_PAYED);
	    	r.setBalanceOperationId(balanceId);
	    	//franco: persistency needed on the transaction modification
	    	if(!pm.update(r))
	    		return -1;
	    	
	    	return toRefund;
    	} else
    		return -1;
    }

    @Override
    public double returnCreditCardPayment(Integer returnId, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {
    	if(logged_user == null || !users.get(logged_user).checkRole(ADMIN, SHOPMANAGER, CASHIER)) 
    		throw new UnauthorizedException();
    	
    	if(returnId == null || returnId <= 0)
    		throw new InvalidTransactionIdException();
    	
    	if(!CreditCardManagerImpl.validateCard(creditCard))
    		throw new InvalidCreditCardException();
    	
    	ReturnTransactionImpl r = returns.get(returnId);
    	if (r == null || !r.checkState(SALE_STATE_CLOSED))
    		return -1;
    	
    	double toRefund = r.getPrice();
    	
    	Integer balanceId = balance.getLastId() + 1;    		
    	BalanceOperationImpl balanceOperation = new BalanceOperationImpl(balanceId, LocalDate.now(), toRefund, DEBIT);
    	if(pm.store(balanceOperation) && CreditCardManagerImpl.refundCard(creditCard, toRefund)) {
	    	balance.updateAmount(-toRefund);
	    	balance.recordBalanceOperation(balanceOperation);
	    	r.setState(SALE_STATE_PAYED);
	    	r.setBalanceOperationId(balanceId);
	    	//franco: persistency needed on the transaction modification
	    	if(!pm.update(r))
	    		return -1;
	    	
	    	return toRefund;
    	} else
    		return -1;
    }

    @Override
    public boolean recordBalanceUpdate(double toBeAdded) throws UnauthorizedException {
    	if(logged_user == null)
    		throw new UnauthorizedException();
    	if(users.get(logged_user).getRole().equals(CASHIER))
    		throw new UnauthorizedException();  
    	if(toBeAdded + balance.getAmount() < 0)
    		return false;
    	
    	Integer balanceId = balance.getLastId() + 1;    	
    	
    	BalanceOperationImpl balanceOperation;
    	if(toBeAdded >= 0)
    		balanceOperation = new BalanceOperationImpl(balanceId, LocalDate.now(), toBeAdded, CREDIT);
    	else
    		balanceOperation = new BalanceOperationImpl(balanceId, LocalDate.now(), -toBeAdded, DEBIT);
    	
    
    	boolean successful = pm.store(balanceOperation);
    	if(!successful)
    		return false;
    	
    	balance.updateAmount(toBeAdded);
    	balance.recordBalanceOperation(balanceOperation);
    	
        return true;
    }

    @Override
    public List<BalanceOperation> getCreditsAndDebits(LocalDate from, LocalDate to) throws UnauthorizedException {
    	if(logged_user == null)
    		throw new UnauthorizedException();
    	if(users.get(logged_user).getRole().equals(CASHIER))
    		throw new UnauthorizedException();  
    	
    	    
    	if(from == null && to == null) {
    		return balance
        			.getBalanceOperations()
        			.stream()
        			.filter(bo -> bo.getDate().isAfter(LocalDate.MIN) && bo.getDate().isBefore(LocalDate.MAX))
        			.collect(Collectors.toList());
    	}
    	
    	if(from == null && to != null) {
    		return balance
        			.getBalanceOperations()
        			.stream()
        			.filter(bo -> bo.getDate().isAfter(LocalDate.MIN) && bo.getDate().isBefore(to))
        			.collect(Collectors.toList());
    	}
    	
    	if(from != null && to == null) {
    		return balance
        			.getBalanceOperations()
        			.stream()
        			.filter(bo -> bo.getDate().isAfter(from) && bo.getDate().isBefore(LocalDate.MAX))
        			.collect(Collectors.toList());
    	}
    	
    	if(from != null && to != null) {
    		if(from.isAfter(to)) {
    			return balance
            			.getBalanceOperations()
            			.stream()
            			.filter(bo -> bo.getDate().isAfter(to) && bo.getDate().isBefore(from))
            			.collect(Collectors.toList());
    		}else {
    			return balance
            			.getBalanceOperations()
            			.stream()
            			.filter(bo -> bo.getDate().isAfter(from) && bo.getDate().isBefore(to))
            			.collect(Collectors.toList());
    		}
    	}   	
    	
    	return null;    	
    }

    @Override
    public double computeBalance() throws UnauthorizedException {
    	if(logged_user == null)
    		throw new UnauthorizedException();
    	if(users.get(logged_user).getRole().equals(CASHIER))
    		throw new UnauthorizedException();
    	
        return balance.getAmount();
    }
    
    public ProductImpl getProductByRFID(String RFID) {
    	return products.get(RFID);
    }
    
    public ProductTypeImpl getProductTypeById(Integer id) {
    	return productTypes.get(id);
    }
    
    private boolean loadAll() {
    	
    	//load users
    	try {
    		File file = new File("dataCSV/users.csv");
    		if(!file.exists())
    			file.createNewFile();
    		
    		FileReader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);
 
            String line;
 
            while ((line = bufferedReader.readLine()) != null) {
            	if(!line.equals("")) {
            		String[] parts = line.split(",");
            		Integer id = Integer.parseInt(parts[0]);
            		users.put(id, new UserImpl(id, parts[1], parts[2], parts[3]));  
            	}
            }
            reader.close();
            bufferedReader.close();  
 
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    	
    	
    	//load productTypes
    	try {
    		File file = new File("dataCSV/product_types.csv");
    		if(!file.exists())
    			file.createNewFile();
    		
    		FileReader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);
 
            String line;
 
            while ((line = bufferedReader.readLine()) != null) {
            	if(!line.equals("")) {
            		String[] parts = line.split(",");
            		Integer id = Integer.parseInt(parts[0]);
            		ProductTypeImpl productType = new ProductTypeImpl(id, parts[1], parts[2], Double.parseDouble(parts[3]), parts[4]);
            	
            		if(parts.length > 5) {
            			productType.setQuantity(Integer.parseInt(parts[5]));
            			if(parts.length > 6)
            				productType.setLocation(parts[6]);
            		}
            		productTypes.put(id, productType);		
            	}
            }
            reader.close();
            bufferedReader.close();  
 
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
 
    	//load products
    	try {
    		File file = new File("dataCSV/products.csv");
    		if(!file.exists())
    			file.createNewFile();
    		
    		FileReader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);
 
            String line;
 
            while ((line = bufferedReader.readLine()) != null) {
            	if(!line.equals("")) {
            		String[] parts = line.split(",");
            		String id = parts[0];
            		ProductImpl product = new ProductImpl(id, Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
            		products.put(id, product);
            	}
            }
            reader.close();
            bufferedReader.close();  
 
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
 
    	
    	//load balance operations
    	try {
    		File file = new File("dataCSV/balance_operations.csv");
    		if(!file.exists())
    			file.createNewFile();
    		
    		FileReader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);
            
            String line;
 
            while ((line = bufferedReader.readLine()) != null) {
            	if(!line.equals("")) {
            		String[] parts = line.split(",");
            		Integer balanceId = Integer.parseInt(parts[0]);
            		BalanceOperationImpl balanceOperation = new BalanceOperationImpl(balanceId, LocalDate.parse(parts[1]), Double.parseDouble(parts[2]), parts[3]); 
            		balance.recordBalanceOperation(balanceOperation);
            		if(balanceOperation.getType().equals(CREDIT))
            			balance.updateAmount(balanceOperation.getMoney());
            		else
            			balance.updateAmount(-balanceOperation.getMoney());
            	}
            }
            reader.close();
            bufferedReader.close();  
            
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    	
    	//load sales and ticketentries
    	
    	try {
    		File file_sale = new File("dataCSV/sales.csv");
    		File file_entry = new File("dataCSV/ticket_entries.csv");
    		
    		if(!file_sale.exists())
    			file_sale.createNewFile();
    		
    		if(!file_entry.exists())
    			file_entry.createNewFile();
    		
    		
    		FileReader readerSale = new FileReader(file_sale);
    		FileReader readerEntry = new FileReader(file_entry);
    		BufferedReader bufferedReaderS = new BufferedReader(readerSale);
    		//BufferedReader bufferedReaderE = new BufferedReader(readerEntry);
    		
    		
    		//bufferedReaderE.mark(0);
    		
    		String lineS;
    		String lineE;
    		
    		
    		while ((lineS = bufferedReaderS.readLine()) != null) {
    			if(lineS.equals("")) 
    				continue;
    			String[] parts = lineS.split(",");
            	Integer saleId = Integer.parseInt(parts[0]);
            	SaleTransactionImpl sale = new SaleTransactionImpl(saleId);
            	sale.setTicketNumber(Integer.parseInt(parts[1]));
            	sale.setEntryCounter(Integer.parseInt(parts[2]));
            	sale.setDiscountRate(Double.parseDouble(parts[3]));
            	sale.setBalanceOperationId(Integer.parseInt(parts[4]));
            	sale.setState(Integer.parseInt(parts[5]));
            	
            	
            	BufferedReader bufferedReaderE = new BufferedReader(new FileReader("dataCSV/ticket_entries.csv"));
            	
            	while((lineE = bufferedReaderE.readLine()) != null) {
            		if(lineE.equals(""))
            			continue;
            		String[] commasplit = lineE.split(",");
            		String[] idsplit = commasplit[0].split("-");
            		if(Integer.parseInt(idsplit[0]) != saleId)
            			continue;
            		TicketEntryImpl entry = new TicketEntryImpl(commasplit[1], commasplit[2], Integer.parseInt(commasplit[3]),
            				Double.parseDouble(commasplit[4]), Double.parseDouble(commasplit[5]), Integer.parseInt(idsplit[1]), saleId);
            		sale.loadEntry(entry);
            		
            	}
            	bufferedReaderE.close();
            	sales.put(saleId, sale);
            	saleCounter = sales.keySet().stream().max((a,b) -> a-b).orElse(0);
            	
            }
    		
            readerSale.close();
            bufferedReaderS.close();  
            readerEntry.close();
            //bufferedReaderE.close();
            
    		
    	}catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    	// load returns and returnentries
    	try {
    		File file_return = new File("dataCSV/returns.csv");
    		File file_entry = new File("dataCSV/return_entries.csv");
    		
    		if(!file_return.exists())
    			file_return.createNewFile();
    		
    		if(!file_entry.exists())
    			file_entry.createNewFile();
    		
    		
    		
    		FileReader readerReturn = new FileReader("dataCSV/returns.csv");
    		FileReader readerEntry = new FileReader("dataCSV/return_entries.csv");
    		BufferedReader bufferedReaderR = new BufferedReader(readerReturn);
    		//BufferedReader bufferedReaderE = new BufferedReader(readerEntry);
    		
    		//bufferedReaderE.mark(0);
    		
    		String lineR;
    		String lineE;
    		
    		while ((lineR = bufferedReaderR.readLine()) != null) {
    			if(lineR.equals(""))
    				continue;
            	String[] parts = lineR.split(",");
            	Integer returnId = Integer.parseInt(parts[0]);
            	Integer saleId = Integer.parseInt(parts[3]);
            	ReturnTransactionImpl ret = new ReturnTransactionImpl(returnId, saleId);
            	ret.setEntryCounter(Integer.parseInt(parts[1]));
            	ret.setBalanceOperationId(Integer.parseInt(parts[2]));
            	ret.setState(Integer.parseInt(parts[4]));
            	ret.setDiscountRate(Double.parseDouble(parts[5]));
            	ret.setCommit(true);
            	
            	BufferedReader bufferedReaderE = new BufferedReader(new FileReader("dataCSV/return_entries.csv"));
            	while((lineE = bufferedReaderE.readLine()) != null) {
            		if(lineE.equals(""))
            			continue;
            		String[] commasplit = lineE.split(",");
            		String[] idsplit = commasplit[0].split("-");
            		if(Integer.parseInt(idsplit[0]) != returnId)
            			continue;
            		ReturnEntryImpl entry = new ReturnEntryImpl(commasplit[1], commasplit[2], Integer.parseInt(commasplit[3]),
            				Double.parseDouble(commasplit[4]), Double.parseDouble(commasplit[5]), Integer.parseInt(idsplit[1]), saleId, returnId);
            		ret.loadEntry(entry);
            		
            	}
            	bufferedReaderE.close();
            	returns.put(returnId, ret);
            	returnCounter = returns.keySet().stream().max((a,b) -> a-b).orElse(0);
            	
            }
            readerReturn.close();
            bufferedReaderR.close();  
            readerEntry.close();
            //bufferedReaderE.close();
            
    		
    	}catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    	
    	
    	// load customers
    	try {
    		File file = new File("dataCSV/customers.csv");
    		if(!file.exists())
    			file.createNewFile();
    		
    		FileReader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);
            
            String line; 
            while ((line = bufferedReader.readLine()) != null) {
            	if(!line.equals("")) {
            		Customer c = new CustomerImpl(line);
                	customers.put(c.getId(), c);
            	}
            }
            reader.close();
            bufferedReader.close();  
            
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    	

    	/*
    	// load customer cards
    	try {
    		File file = new File("dataCSV/customer_cards.csv");
    		if(!file.exists())
    			file.createNewFile();
    		
    		FileReader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);
                
            String line;
 
            while ((line = bufferedReader.readLine()) != null) {
            	if(!line.equals("")) {
            		String[] parts = line.split(",");
            		customerCards.put(parts[0], new CustomerCardImpl(parts[0],
            													 Integer.parseInt(parts[1]),
            													 Integer.parseInt(parts[2])));
            	}
            }
            reader.close();
            bufferedReader.close();  
            
        } catch (IOException e) {
            e.printStackTrace();
            return false;

        }
        */
    	
    	//load orders
    	try {
    		File file = new File("dataCSV/orders.csv");
    		if(!file.exists())
    			file.createNewFile();
    		
    		FileReader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);
 
            String line;
 
            while ((line = bufferedReader.readLine()) != null) {
            	if(!line.equals("")) {
            		String[] parts = line.split(",");
            		Integer id = Character.getNumericValue(parts[0].charAt(0));
            		
            		orders.put(id, new OrderImpl(id, Integer.parseInt(parts[1]), parts[2], Double.parseDouble(parts[3]), Integer.parseInt(parts[4]), parts[5]));  
            	}
            }
            reader.close();
            bufferedReader.close();  
 
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    	//load positions
    	try {
    		File file = new File("dataCSV/positions.csv");
    		if(!file.exists())
    			file.createNewFile();
    		
    		FileReader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);
 
            String line;
            int check=0;
 
            while ((line = bufferedReader.readLine()) != null) {
            	if(!line.equals("") || check==0) {
            		positions.add(new PositionImpl(line));  
            	}
            	else
            		check=1;
            }
            reader.close();
            bufferedReader.close();  
 
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    	

    	//TODO: load here your files  
    	
    	
    	
    	return true;
    	
    }
}
