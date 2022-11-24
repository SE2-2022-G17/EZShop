package it.polito.ezshop.model;

import it.polito.ezshop.data.*;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PositionImpl implements Storable {
    private String position;
	private Integer productId;
	
	public PositionImpl(String position) {
		super();
		this.position=position;
	}
	
	
	public PositionImpl() {
		super();
	}


	public String getPosition() {
		return position;
	}
	
	public void setPosition(String position) {
		this.position=position;
	}
	
	public static boolean isValid(String position) {
		if(position==null)
			return false;
		Pattern pattern = Pattern.compile("([1-9][0-9]*|0)-([a-zA-Z]+)-([1-9][0-9]*|0)+");
		Matcher matcher = pattern.matcher(position);
		return  matcher.find();
	}
	
	@Override
	public String getCSV() {
		return position;
	}
}
