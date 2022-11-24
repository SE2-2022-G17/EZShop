package it.polito.ezshop.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class BalanceImpl {
	HashMap<Integer, BalanceOperationImpl> operations;
	Double amount;	

	public BalanceImpl() {
		super();
		this.amount = 0.0;
		operations = new HashMap<>();
	}

	public Double getAmount() {
		return amount;
	}
	
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	
	public void updateAmount(Double toBeAdded) {
		this.amount = toBeAdded + this.amount;
	}
	
	public void recordBalanceOperation(BalanceOperationImpl balanceOperation) {
		if(balanceOperation == null)
			return;
		this.operations.put(balanceOperation.getBalanceId(), balanceOperation);
	}
	
	public List<BalanceOperationImpl> getBalanceOperations() {
		return operations.values().stream().collect(Collectors.toList());
	}
	
	public int getLastId() {
		if(operations.size() == 0) {
    		return 0;
    	}
    	else {
    		return Collections.max(operations
    								.values()
    								.stream()
    								.map(bo -> bo.getBalanceId())
    								.collect(Collectors.toList()));
    	}
	}
	
	public void clearBalanceOperations() {
		operations.clear();
	}
	
}
