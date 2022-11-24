package it.polito.ezshop.model;

import it.polito.ezshop.data.*;

public class ProductTypeImpl implements ProductType, Storable{
	Integer id;
	String description;
	String productCode;
	double pricePerUnit;
	String note;
	Integer quantity;
	String location;
	
	public ProductTypeImpl(Integer id, String description, String productCode, double pricePerUnit, String note) {
		super();
		this.id = id;
		this.description = description;
		this.productCode = productCode;
		this.pricePerUnit = pricePerUnit;
		this.note = note;
		this.quantity = 0;
		this.location = "";
	}

	public ProductTypeImpl() {
		super();
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Override
	public String getProductDescription() {
		return description;
	}

	@Override
	public void setProductDescription(String productDescription) {
		this.description = productDescription;
	}
	
	@Override
	public String getBarCode() {
		return productCode;
	}

	@Override
	public void setBarCode(String barCode) {
		this.productCode = barCode;
	}
	
	
	@Override
	public Double getPricePerUnit() {
		return pricePerUnit;
	}

	@Override
	public void setPricePerUnit(Double pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}

	@Override
	public String getNote() {
		return note;
	}

	@Override
	public void setNote(String note) {
		this.note = note;
	}

	
	@Override
	public Integer getQuantity() {
		return quantity;
	}

	@Override
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	@Override
	public String getLocation() {
		return location;
	}

	@Override
	public void setLocation(String location) {
		this.location = location;
	}

	public void updateQuantity(int amount) {
		this.quantity = quantity-amount;
	}
	
	public static boolean validateProductCode(String productCode) {
		if(productCode == null)
			return false;
		String multiplier = "31313131313131313";
		String barcode = productCode;
		
		for(int i = 0; i< productCode.length(); i++) {
			if(!Character.isDigit(productCode.charAt(i)))
				return false;
		}
		
		//NFR4: bar code should be string of digits of either 12, 13, or 14
		if(productCode.length() < 12 || productCode.length() > 14)
			return false;
		
		for(int i = 0; i <= multiplier.length() - productCode.length(); i++)
			barcode = "0" + barcode;
		
		int sum = 0;
		for(int i = 0; i < multiplier.length(); i++) {
			int m = Character.getNumericValue(multiplier.charAt(i));
			int b = Character.getNumericValue(barcode.charAt(i));
			sum += m*b;

		}

        int multipleOfTen = (sum/10)*10;
        if(multipleOfTen != sum)
            multipleOfTen += 10;
        
		int lastDigit = multipleOfTen - sum;

		char actualLastDigit = productCode.charAt(productCode.length()-1);
		if(lastDigit == Character.getNumericValue(actualLastDigit))
		    return true;
			
		return false;
	}

	@Override
	public String getCSV() {
		return id + "," + description + "," + productCode + "," + pricePerUnit + "," + note + "," + quantity + "," + location;
	}

	

}
