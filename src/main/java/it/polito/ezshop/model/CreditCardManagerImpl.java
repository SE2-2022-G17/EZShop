package it.polito.ezshop.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public final class CreditCardManagerImpl {

	// At the moment, all credit card are saved in credit_card.csv
	
	private CreditCardManagerImpl() { }
	
	 // Luhn algorithm (standard verification)
	public static boolean validateCard(String pord) {
		
		if (pord == null || pord.equals(""))
			return false;
		 
	    int sumDigits = 0;
	    int len = pord.length();
	    for (int i = len - 1; i >= 0; i--)
	    {
	        int digit = Character.getNumericValue(pord.charAt(i));
	 
	        if ((len-1 - i) % 2 == 1)
	        	digit *= 2;
	 
	        sumDigits += digit / 10;
	        sumDigits += digit % 10;
	    }
	    
	    return (sumDigits % 10 == 0);
	}
	
	// This method is only a mockup of banking operations (supposed to be handled by the banks)
	public static boolean withdrawFromCard(String pord, double amount) {
		
		if (!validateCard(pord))
			return false;
		
		boolean canWithdraw = false;
		
		try {
			FileReader reader = new FileReader("dataCSV/credit_card.csv");
            BufferedReader bufferedReader = new BufferedReader(reader);
            
            String line;
            while ((line = bufferedReader.readLine()) != null) {
            	String[] parts = line.split(",");
            	
            	if (pord.equals(parts[0])) { 				 // card found
            		if (amount < Integer.parseInt(parts[1])) // if there are enough funds
            			canWithdraw = true;					 // then we can withdraw
            		break;                                   // no need to check the other cards
            	}
            }
            reader.close();									 // but first, we clean our IO
            bufferedReader.close();  
            
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		return canWithdraw; // remained false if card did not exist or lacked the necessary funds (or file could no be read)
	}
	
	// This method is only a mockup of banking operations (supposed to be handled by the banks)
	public static boolean refundCard(String pord, double amount) {
		
		if (!validateCard(pord))
			return false;
		
		boolean canRefund = false;
		
		try {
			FileReader reader = new FileReader("dataCSV/credit_card.csv");
            BufferedReader bufferedReader = new BufferedReader(reader);
            
            String line;
            while ((line = bufferedReader.readLine()) != null) {
            	String[] parts = line.split(",");
            	
            	if (pord.equals(parts[0])) { 				 // card found
            		canRefund = true;						 // then we can refund
            		break;                                   // no need to check the other cards
            	}
            }
            reader.close();									 // but first, we clean our IO
            bufferedReader.close();  
            
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		return canRefund; // remained false if card did not exist (or file could no be read)
	}
}
