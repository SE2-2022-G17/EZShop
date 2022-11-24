package it.polito.ezshop.data;

public interface Product {
	
	String getRFID();
	
	void setRFID(String RFID);
	
	Integer getProductTypeId();
	
	void setProductTypeId(Integer productTypeId);
	
	Integer getTransactionId();
	
	void setTransactionId(Integer transactionId);
	
	Integer getReturnId();
	
	void setReturnId(Integer returnId);
}
