package it.polito.ezshop.model;

// dummy class to test persistency
public class Dummy implements Storable{
	@Override
	public String getCSV() {
		return "dummy";
	}
}
