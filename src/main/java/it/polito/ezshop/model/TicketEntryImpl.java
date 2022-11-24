package it.polito.ezshop.model;



import it.polito.ezshop.data.*;

public class TicketEntryImpl implements TicketEntry, Storable{

	String barcode;
	String description;
	int amount;
	double ppu;
	double discount;
	int id;
	Integer saleId;
	
	/*
	 * NOTE: this class contains setters. However persistency is implemented at a higher level (EzShop), such
	 * setter functions should be called only from EzShop, or at least you must make sure that for each call
	 * to them must correspond an update(Object o) call from EzShop
	 */
	
	public TicketEntryImpl( String barcode, String description, int amount, double ppu, double discount, int id, int saleId) {
		
		this.barcode = barcode;
		this.description = description;
		this.amount = amount;
		this.ppu= ppu;
		this.discount = discount;
		this.id = id;
		this.saleId = saleId;
	}
	
	public TicketEntryImpl() {
		super();
	}

	@Override
	public String getCSV() {
		return saleId + "-" + id + "," + barcode + "," + description + "," + amount + "," + ppu +"," + discount; 
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public Integer getSaleId() {
		return this.saleId;
	}
	
	public String getBarCode() {
		return this.barcode;
	}

    public void setBarCode(String barCode) {
    	this.barcode = barCode;
    }

    public String getProductDescription() {
    	return this.description;
    }

    public void setProductDescription(String productDescription) {
    	this.description=productDescription;
    }

    public int getAmount() {
    	return this.amount;
    }

    public void setAmount(int amount) {
    	this.amount = amount;
    }

    public double getPricePerUnit() {
    	return this.ppu;
    }

    public void setPricePerUnit(double pricePerUnit) {
    	this.ppu= pricePerUnit;
    }

    public double getDiscountRate() {
    	return this.discount;
    }

    public void setDiscountRate(double discountRate) {
    	this.discount=discountRate;
    }
    public double getPrice() {
    	return ppu*amount - ppu*amount*discount;
    }
    public void updateAmount(int amount) {
    	this.amount += amount;
    }
}
