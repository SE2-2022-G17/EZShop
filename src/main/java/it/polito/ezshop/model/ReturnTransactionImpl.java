package it.polito.ezshop.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.polito.ezshop.data.TicketEntry;

public class ReturnTransactionImpl implements Storable{
	Integer saleId;
	Integer id;
	int entryCounter;
	Integer state;
	boolean commit;
	double rawprice;
	double discount;
	HashMap<Integer, ReturnEntryImpl> r_entries;
	
	Integer BalanceOperationId;
	public ReturnTransactionImpl(Integer id, Integer saleId) {
		this.id=id;
		this.r_entries= new HashMap<>();
		this.saleId=saleId;
		this.state =0;
		this.entryCounter=0;
		this.rawprice = 0;
		this.discount=0;
		this.BalanceOperationId=-1;
		
	}
	
	public ReturnTransactionImpl() {
		super();
	}

	public Integer getSaleId() {
		return this.saleId;
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
	
	public List<TicketEntry> getEntries(){
    	return new ArrayList<TicketEntry>(r_entries.values());
    }
    //need to test?
    public Integer getEntryId(String barCode) {
    	return r_entries.keySet().stream().filter(k -> r_entries.get(k).getBarCode().equals(barCode)).findFirst().orElse(-1);
    }
    

    public void clearEntries() {
    	r_entries.clear();
    }
    
    public void setDiscountRate(double discountRate) {
    	discount = discountRate;
    }
    public Double getDiscountRate() {
    	return this.discount;
    }
    
    public double getPrice() {
    	return	rawprice - rawprice*discount;
    }
    
    public void loadEntry(ReturnEntryImpl e) {
    	r_entries.put(e.getId(), e);
    	if(e.getId()>entryCounter)
    		entryCounter=e.getId();
    }
    public void setEntries(List<ReturnEntryImpl> entries) {
    	this.entryCounter = 0;
    	this.rawprice=0;
    	this.r_entries.clear();
    	for(ReturnEntryImpl e : entries) {
    		//check for null value, throw exception
    		if(e == null)
    			throw new IllegalArgumentException();
    		//add the entry
    		r_entries.put(entryCounter,  e);
    		entryCounter++;
    		this.rawprice += e.getPrice();
    	}
    	return;
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
    	return r_entries.values().stream().anyMatch(e -> e.getBarCode().equals(prodCode));
    }
    
    public ReturnEntryImpl updateEntry(String prodCode, int amount) {
    	Integer to_up= r_entries.keySet().stream().filter(k -> r_entries.get(k).getBarCode().equals(prodCode)).findFirst().get();
    	this.rawprice -= r_entries.get(to_up).getPrice();
    	r_entries.get(to_up).updateAmount(amount);
    	this.rawprice += r_entries.get(to_up).getPrice();
    	
    	
    	if(r_entries.get(to_up).getAmount()<0)
    		return null;
    	
    	if(r_entries.get(to_up).getAmount()==0)
    		return r_entries.remove(to_up);
    	
    	
    	
    	return r_entries.get(to_up);
    }
    
    public ReturnEntryImpl getEntry(String prodCode) {
    	return r_entries.values().stream().filter(e -> e.getBarCode().equals(prodCode)).findAny().orElse(null);
    }
	
	public ReturnEntryImpl addEntry(ProductTypeImpl prod, int amount, double discount) {
		if(discount>=1 || discount<0)
			return null;
		
		if(amount<=0)
			return null;
		if(containsEntry(prod.getBarCode()))
			return null;
		ReturnEntryImpl newEntry = new ReturnEntryImpl(
			prod.getBarCode(), prod.getProductDescription(),amount,prod.getPricePerUnit(), discount, this.entryCounter,this.saleId , this.id);
		
		r_entries.put(this.entryCounter, newEntry);
		this.rawprice+=newEntry.getPrice();
		this.entryCounter++;
		return newEntry;
	
	}
	public boolean getCommit() {
		return this.commit;
	}
	public void setCommit(boolean commit) {
		this.commit = commit;
	}
	
	@Override
	public String getCSV() {
		return id + "," + entryCounter + "," + BalanceOperationId + "," + saleId + "," + state + "," + discount;
	}
}
