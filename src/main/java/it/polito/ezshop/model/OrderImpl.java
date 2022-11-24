package it.polito.ezshop.model;

import it.polito.ezshop.data.*;

public class OrderImpl implements Order, Storable {
	Integer orderId;
	Integer balanceId;
	String productCode;
	double pricePerUnit;
	int quantity;
	String status;
	
	public OrderImpl(Integer orderId, Integer balanceId, String productCode, double pricePerUnit, int quantity, String status) {
		super();
		this.orderId=orderId;
		this.balanceId=balanceId;
		this.productCode=productCode;
		this.pricePerUnit=pricePerUnit;
		this.quantity=quantity;
		this.status=status;
	}

	public OrderImpl() {
		super();
	}

	@Override
	public Integer getBalanceId() {
		return balanceId;
	}

	@Override
	public void setBalanceId(Integer balanceId) {
		this.balanceId=balanceId;
    	return;
		
	}

	@Override
	public String getProductCode() {
		return productCode;
	}

	@Override
	public void setProductCode(String productCode) {
		this.productCode=productCode;
    	return;
	}

	@Override
	public double getPricePerUnit() {
		return pricePerUnit;
	}

	@Override
	public void setPricePerUnit(double pricePerUnit) {
		this.pricePerUnit=pricePerUnit;
    	return;
	}

	@Override
	public int getQuantity() {
		return quantity;
	}

	@Override
	public void setQuantity(int quantity) {
		this.quantity=quantity;
    	return;
	}

	@Override
	public String getStatus() {
		return status;
	}

	@Override
	public void setStatus(String status) {
		this.status=status;
    	return;
	}

	@Override
	public Integer getOrderId() {
		return orderId;
	}

	@Override
	public void setOrderId(Integer orderId) {
		this.orderId=orderId;
    	return;
	}
	
	@Override
	public String getCSV() {
		return orderId + "," + balanceId + "," + productCode + "," + pricePerUnit + "," + quantity + "," + status;
	}

}
