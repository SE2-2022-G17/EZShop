package it.polito.ezshop.model;

import it.polito.ezshop.data.TicketEntry;

public class ReturnEntryImpl extends TicketEntryImpl{
	Integer returnId;
	
	
	public ReturnEntryImpl(String barcode, String description, int amount, double ppu, double discount,int id, int saleId, int returnId) {
		super(barcode, description,  amount, ppu, discount, id, saleId);
		this.returnId=returnId;
	}
	
	public ReturnEntryImpl() {
		super();
	}

	public Integer getReturnId() {
		return this.returnId;
	}
	
	@Override
	public String getCSV() {
		return  returnId + "-" + id + "," + barcode + "," + description + "," + amount + "," + ppu +"," + discount + ","  + saleId;  
	}
	
}
