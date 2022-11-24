package it.polito.ezshop.model;

import it.polito.ezshop.data.Product;

public class ProductImpl implements Product, Storable {
	String RFID;
	Integer productTypeId;
	Integer transactionId;
	Integer returnId;
	
	public ProductImpl() {
		super();
	}
	
	public ProductImpl(String RFID, Integer productTypeId) {
		super();
		this.RFID = RFID;
		this.productTypeId = productTypeId;
		this.transactionId = -1;
		this.returnId = -1;
	}

	public ProductImpl(String RFID, Integer productTypeId, Integer transactionId, Integer returnId) {
		super();
		this.RFID = RFID;
		this.productTypeId = productTypeId;
		this.transactionId = transactionId;
		this.returnId = returnId;
	}

	@Override
	public String getRFID() {
		return RFID;
	}

	@Override
	public void setRFID(String RFID) {
		this.RFID = RFID;
	}
	
	@Override
	public Integer getProductTypeId() {
		return productTypeId;
	}

	@Override
	public void setProductTypeId(Integer productTypeId) {
		this.productTypeId = productTypeId;
	}

	@Override
	public Integer getTransactionId() {
		return transactionId;
	}

	@Override
	public void setTransactionId(Integer transactionId) {
		this.transactionId = transactionId;
	}

	@Override
	public Integer getReturnId() {
		return returnId;
	}

	@Override
	public void setReturnId(Integer returnId) {
		this.returnId = returnId;
	}
	
	@Override
	public String getCSV() {
		return RFID + "," + productTypeId + "," + transactionId + "," + returnId;
	}
	
	public static boolean validateRFID(String RFID) {
		if(RFID == null)
			return false;
		
		if(RFID.length() != 12)
			return false;
		
		for(int i = 0; i < RFID.length(); i++) {
			if(!Character.isDigit(RFID.charAt(i)))
				return false;
		}
		
		return true;
	}

}
