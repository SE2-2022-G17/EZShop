package it.polito.ezshop.model;

import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

import java.util.HashMap;
import it.polito.ezshop.data.*;

import java.lang.Math;



public class SaleTransactionImpl implements SaleTransaction, Storable {
	
	
	Integer ticketN;
	Integer id;
	int entryCounter;
	double discount;
	double rawprice;
	Integer state;
	
	HashMap<Integer, TicketEntryImpl> t_entries = new HashMap<>();
	Integer BalanceOperationId;
	
	/*
	 * NOTE: this class contains setters. However persistency is implemented at a higher level (EzShop), such
	 * setter functions should be called only from EzShop, or at least you must make sure that for one or more calls
	 * to them must correspond an update(Object o) call from EzShop
	 */
	
	public SaleTransactionImpl(int id) {
		this.id = id;
	
		this.rawprice=0;
		//i don't understand what they mean by ticketnumber but i'm going to initialize it as the id used by EzShop class
		this.ticketN=id;
		this.discount = 0;
		//opened state
		this.state = 0;
		this.BalanceOperationId=-1;
		this.entryCounter=0;
	}
	
	public SaleTransactionImpl() {
		super();
	}

	@Override
	public String getCSV() {
		return id + "," + ticketN + "," + entryCounter + "," + discount + "," + BalanceOperationId + "," + state;
	}
	
	public void setEntryCounter(int ec) {
		this.entryCounter=ec;
	}
	public Integer getEntryCounter() {
		return this.entryCounter;
	}
	public void setState(Integer state) {
		this.state=state;
	}
	
	public Integer getState() {
		return this.state;
	}
	
	public void setBalanceOperationId(Integer id) {
		this.BalanceOperationId = id;
	}
	public Integer getBalanceOperationId() {
		return this.BalanceOperationId;
	}
	
	public Integer getTicketNumber() {
		return this.ticketN;
	}

    public void setTicketNumber(Integer ticketNumber) {
    	this.ticketN = ticketNumber;
    }

    public List<TicketEntry> getEntries(){
    	return new ArrayList<TicketEntry>(t_entries.values());
    }
    //need to test?
    public Integer getEntryId(String barCode) {
    	return t_entries.keySet().stream().filter(k -> t_entries.get(k).getBarCode().equals(barCode)).findFirst().orElse(-1);
    }
    

    public void clearEntries() {
    	t_entries.clear();
    }
    
    
    //need to test
    public void loadEntry(TicketEntryImpl e) {
    	t_entries.put(e.getId(), e);
    	rawprice+=e.getPrice();
    	if(e.getId()>entryCounter)
    		entryCounter=e.getId();
    }
    public void setEntries(List<TicketEntry> entries) {
    	this.entryCounter = 0;
    	this.t_entries.clear();
    	for(TicketEntry e : entries) {
    		t_entries.put(entryCounter, (TicketEntryImpl) e);
    		entryCounter++;
    	}
    	updatePrice();
    }
    public Integer compressEntries() {
    	int n=0;
    	int bamount=0;
    	List<String> bcodes = t_entries.values().stream().map(e -> e.getBarCode()).distinct().collect(Collectors.toList());
    	if(bcodes.size()==t_entries.size())
    		return 0;
    	
    	for(String bcode : bcodes) {
    		Integer bentry = t_entries.keySet().stream().filter(k -> t_entries.get(k).getBarCode().equals(bcode)).findFirst().get();
    		bamount = t_entries.get(bentry).getAmount();
    		for(Integer eid : t_entries.keySet().stream().filter(k -> t_entries.get(k).getBarCode().equals(bcode)).collect(Collectors.toList())) {
    			
    			if(eid == bentry) 
    				continue;
    			bamount += t_entries.get(eid).getAmount();
    			n++;
    			
    			t_entries.remove(eid);
    		}
    		
    		t_entries.get(bentry).setAmount(bamount);
    		this.entryCounter= t_entries.keySet().stream().max((a,b) -> a-b).orElse(0);
    	}
    	
    	return n;
    }

    public double getDiscountRate() {
    	return this.discount;
    }

    public void setDiscountRate(double discountRate) {
    	this.discount=discountRate;
    }
    
    
    public double getPrice() {
    	return this.rawprice- this.rawprice*this.discount;
    }

    /*
     * this method is in the interface even tho it doesn't make any sense;
     * if this creates problems i'll see if i have to fix it
     */
    public void setPrice(double price) {
    	this.rawprice = price;
    }
    
    public Integer getId() {
    	return this.id;
    }
   
    public boolean checkState(Integer ...states) {
    	for(Integer state : states) {
    		if(this.state.equals(state)) {
    			return true;
    		}
    	}
    	return false;
    }
    public boolean containsEntry(String prodCode) {
    	return t_entries.values().stream().anyMatch(e -> e.getBarCode().equals(prodCode));
    }
    
    public TicketEntryImpl addEntry(ProductTypeImpl prod, int amount) {
    		if(amount<=0)
    			return null;
    		if(containsEntry(prod.getBarCode()))
    			return null;
    		TicketEntryImpl newEntry = new TicketEntryImpl(
    			prod.getBarCode(), prod.getProductDescription(),amount, prod.getPricePerUnit(), 0,this.entryCounter,this.id );
    		
    		t_entries.put(this.entryCounter, newEntry);
    		this.entryCounter++;
    		this.rawprice += newEntry.getPrice();
    		return newEntry;
    	
    }
    public TicketEntryImpl updateEntry(String prodCode, int amount) {
    	Integer to_up= t_entries.keySet().stream().filter(k -> t_entries.get(k).getBarCode().equals(prodCode)).findFirst().get();
    	this.rawprice -= t_entries.get(to_up).getPrice();
    	t_entries.get(to_up).updateAmount(amount);
    	
    	if(t_entries.get(to_up).getAmount()<0)
    		return null;
    	
    	this.rawprice += t_entries.get(to_up).getPrice();
    	
    	if(t_entries.get(to_up).getAmount()==0)
    		return t_entries.remove(to_up);
    	
    	
    	
    	return t_entries.get(to_up);
    }
    
    public TicketEntryImpl updateEntry(String prodCode, double discRate) {
    	Integer to_up= t_entries.keySet().stream().filter(k -> t_entries.get(k).getBarCode().equals(prodCode)).findAny().get();
    	if(discRate <0 || discRate>=1)
    		return null;
    	
    	this.rawprice -= t_entries.get(to_up).getPrice();
    	t_entries.get(to_up).setDiscountRate(discRate);
    	this.rawprice += t_entries.get(to_up).getPrice();
    	
    	if(t_entries.get(to_up).getAmount()==0) 
    		return t_entries.remove(to_up);
    	
    	
    	return t_entries.get(to_up);
    }
    
    public TicketEntryImpl getEntry(String prodCode) {
    	return t_entries.values().stream().filter(e -> e.getBarCode().equals(prodCode)).findAny().orElse(null);
    }
    
    public Integer computePoints() {
    	double price = this.getPrice();
    	price = Math.floor(price/10);
    	return (int) price;
    }
    public boolean applyReturn(List<TicketEntry> returned) {
    	for(TicketEntry e : returned){
    		if(e.getAmount()<0)
    			return false;
    		if(e.getAmount()==0)
    			continue;
    		if(updateEntry(e.getBarCode(), -e.getAmount())==null)
    			return false;
    	}
    	return true;
    }
    public boolean undoReturn(List<TicketEntry> returned) {
    	for(TicketEntry e : returned){
    		if(e.getAmount()<0)
				return false;
    		if(e.getAmount()==0)
				continue;
    		
    		if(!containsEntry(e.getBarCode())) {
    			TicketEntryImpl newEntry= new TicketEntryImpl(e.getBarCode(), e.getProductDescription(),e.getAmount(), 
    					e.getPricePerUnit(), e.getDiscountRate(), this.entryCounter, this.id);
    			t_entries.put(this.entryCounter, newEntry);
    			this.rawprice += newEntry.getPrice();
    		}else {
    			
    			if(updateEntry(e.getBarCode(), e.getAmount())==null)
    				return false;
    			
    		}
    	}
    	return true;
    }
    
    /*
     * PRIVATE METHODS
     */
    private void updatePrice() {
    	rawprice = 0;
    	t_entries.values().stream().forEach(t ->{
    		
    		rawprice += t.getPrice();
    	});
    
    }
}
