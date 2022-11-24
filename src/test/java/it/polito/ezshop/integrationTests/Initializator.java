package it.polito.ezshop.integrationTests;



import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.model.SaleTransactionImpl;

public class Initializator {
	
	
	
	public static void initUser(EZShopInterface ezShop) {
		try {
		ezShop.createUser("airjordan", "chicagobulls", "Administrator");
		ezShop.login("airjordan", "chicagobulls");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//requires: initUser
	public static void initProducts(EZShopInterface ezShop) {
		try {
			Integer p1Id = ezShop.createProductType("YEEZY 500", "8057014050035", 200.0, "DESERT RAT BLUSH");
			Integer p2Id = ezShop.createProductType("Air Jordan 1", "9788838663260", 119.99, "Air Jordan 1 Mid");
			
			ezShop.getProductTypeByBarCode("8057014050035").setLocation("1-1-1");
			ezShop.getProductTypeByBarCode("9788838663260").setLocation("2-2-2");
			ezShop.updateQuantity(p1Id, 100);
			ezShop.updateQuantity(p2Id, 200);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void initProductsBase(EZShopInterface ezShop) {
		try {
			ezShop.createProductType("YEEZY 500", "8057014050035", 200.0, "DESERT RAT BLUSH");
			ezShop.createProductType("Air Jordan 1", "9788838663260", 119.99, "Air Jordan 1 Mid");
			
			ezShop.getProductTypeByBarCode("8057014050035").setLocation("1-1-1");
			ezShop.getProductTypeByBarCode("9788838663260").setLocation("2-2-2");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	//requires: initUser
	public static Integer initSaleBase(EZShopInterface ezShop)  {
		try {
			return ezShop.startSaleTransaction();
		}catch(Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public static void initSaleWithProducts(EZShopInterface ezShop) {
		try {
			initUser(ezShop);
			initSaleBase(ezShop);
			initProducts(ezShop);
			
			ezShop.addProductToSale(1, "8057014050035", 50);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void initSaleWithProductsClosed(EZShopInterface ezShop) {
		try {
			initUser(ezShop);
			initSaleBase(ezShop);
			initProducts(ezShop);
			
			ezShop.addProductToSale(1, "8057014050035", 50);
			ezShop.endSaleTransaction(1);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void initSaleWithProductsPayed(EZShopInterface ezShop) {
		try {
			initUser(ezShop);
			initSaleBase(ezShop);
			initProducts(ezShop);
			
			ezShop.addProductToSale(1, "8057014050035", 50);
			ezShop.endSaleTransaction(1);
			ezShop.applyDiscountRateToProduct(1, "8057014050035", 0.5);
			ezShop.applyDiscountRateToSale(1, 0.5);
			SaleTransactionImpl s = (SaleTransactionImpl) ezShop.getSaleTransaction(1);
			s.setState(2);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	//requires: initSaleWithProductsPayed
	public static void initReturnBase(EZShopInterface ezShop) {
		try {
		ezShop.startReturnTransaction(1);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static Integer initReturnBase(EZShopInterface ezShop, int saleid) {
		try {
		 return ezShop.startReturnTransaction(saleid);
		}catch(Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public static void initReturnWithProducts(EZShopInterface ezShop) {
		try {
			initSaleWithProductsPayed(ezShop);
			initReturnBase(ezShop);
			ezShop.returnProduct(1, "8057014050035", 25);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//requires initProductsBase, initUser
	public static void initOrderPayed(EZShopInterface ezShop) {
		try {
		Integer orderId1 = ezShop.issueOrder("8057014050035", 100, 1);
		Integer orderId2 = ezShop.issueOrder("8057014050035", 200, 1);
		ezShop.recordBalanceUpdate(300);
		ezShop.payOrder(orderId1);
		ezShop.payOrder(orderId2);
		
		
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	/*
	 * 
	 * RFID SECTION
	 * 
	 * THIS SECTION USES EZSHOP'S METHODS. THESE METHODS WILL WORK ONLY ONCE recordOrderArrivalRFID AND addProductToSaleRFID WORK TOO
	 * 
	 * franco: all these method should now work
	 * 
	 */
	
	
	//If you're using this method to test addProductToSaleRFID you may also have to call initSaleBase afterward
	public static void RFIDinitProducts(EZShopInterface ezShop) {
		try {
			initUser(ezShop);
			initProductsBase(ezShop);
			initOrderPayed(ezShop);
			
			Integer orderId1 = ezShop.issueOrder("8057014050035", 10, 1);
			Integer orderId2 = ezShop.issueOrder("9788838663260", 10, 1);
			ezShop.recordBalanceUpdate(20);
			ezShop.payOrder(orderId1);
			ezShop.payOrder(orderId2);
			ezShop.recordOrderArrivalRFID(orderId1, "000000001000");
			ezShop.recordOrderArrivalRFID(orderId2, "000000002000");
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//the sale id will be 1
	public static void RFIDinitSaleWithProducts(EZShopInterface ezShop) {
		try {
			
			RFIDinitProducts(ezShop);
			initSaleBase(ezShop);
			
			ezShop.addProductToSaleRFID(1, "000000001000");
			ezShop.addProductToSaleRFID(1, "000000001001");
			ezShop.addProductToSaleRFID(1, "000000001002");
			ezShop.addProductToSaleRFID(1, "000000001003");
			ezShop.addProductToSaleRFID(1, "000000001004");
			ezShop.addProductToSaleRFID(1, "000000001005");
				
			
			ezShop.addProductToSaleRFID(1, "000000002000");
			ezShop.addProductToSaleRFID(1, "000000002001");
			ezShop.addProductToSaleRFID(1, "000000002002");
			ezShop.addProductToSaleRFID(1, "000000002003");
			ezShop.addProductToSaleRFID(1, "000000002005");
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void RFIDinitSaleWithProductsClosed(EZShopInterface ezShop) {
		try {
			RFIDinitSaleWithProducts(ezShop);
			
			ezShop.endSaleTransaction(1);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void RFIDinitSaleWithProductsPayed(EZShopInterface ezShop) {
		try {
			RFIDinitSaleWithProducts(ezShop);
			
			ezShop.endSaleTransaction(1);
			SaleTransactionImpl s = (SaleTransactionImpl) ezShop.getSaleTransaction(1);
			s.setState(2);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void RFIDinitReturnWithProducts(EZShopInterface ezShop) {
		try {
			RFIDinitSaleWithProductsPayed(ezShop);
			initReturnBase(ezShop);
			
			ezShop.returnProductRFID(1, "000000001000");
			ezShop.returnProductRFID(1, "000000001001");
			ezShop.returnProductRFID(1, "000000002000");
			ezShop.returnProductRFID(1, "000000002001");
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
}









