package it.polito.ezshop.model;

import it.polito.ezshop.data.*;

public class CustomerImpl implements Customer, Storable {

	Integer id;
	String customerName; 
	String customerCard;
	Integer points;
	

	// Loading an existing customer
	public CustomerImpl(String lineCSV) {
		String[] parts = lineCSV.split(",");
		id = Integer.parseInt(parts[0]);
		customerName = parts[1];
		customerCard = parts[2];
		points = Integer.parseInt(parts[3]);
	}

	// Creating a new customer
	public CustomerImpl(Integer id, String customerName) {
        this.id = id;
        this.customerName = customerName;
        this.customerCard = null;
        this.points = 0;
	}

	public CustomerImpl() {
		super();
	}

	@Override
	public Integer getId() {
		return this.id;
	}
	
	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Override
    public String getCustomerName() {
		return this.customerName;
	}

	@Override
    public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	@Override
    public String getCustomerCard() {
		return this.customerCard;
	}
	
	@Override
    public void setCustomerCard(String customerCard){
		this.customerCard = customerCard;
	}
	
	public String getCSV() {
		return Integer.toString(this.id) + ","
			         + this.customerName + ","
			         + this.customerCard + ","
			         + this.points;
	}

	@Override
	public Integer getPoints() {
		return this.points;
	}

	@Override
	public void setPoints(Integer points) {
		this.points = points;
	}
}
