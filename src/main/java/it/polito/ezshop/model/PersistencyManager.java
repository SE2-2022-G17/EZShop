package it.polito.ezshop.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class PersistencyManager {
	
	//Andrea: to use the store method your ClassImpl must implement Storable interface
	public boolean store(Storable storable) {
		if(storable == null)
			return false;
		
		String file = getFile(storable);
			
		if(file == "")
			return false;
			
		try {
			FileWriter writer = new FileWriter(file, true);
			writer.write(System.getProperty("line.separator")+storable.getCSV());
			writer.close();
		}catch (IOException e) {
			e.printStackTrace();
			return false;
		}
			
		return true;
	}
	
	public boolean delete(Storable storable) {
		if(storable == null)
			return false;
		
		String file = getFile(storable);
		if(file == "")
			return false;
		
		
		String tempFile = "dataCSV/tmp.csv";
		String id = storable.getCSV().split(",")[0];
		
		
		try {
			String currentLine;

			File oldFile = new File(file);
			File newFile = new File(tempFile);

			FileWriter fw = new FileWriter(tempFile, true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);
			
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			

			while((currentLine = br.readLine()) != null) {
			    if(currentLine.startsWith(id)) 
			    	continue;
			    pw.println(currentLine);
			}
			
			pw.flush();
			pw.close();
			fr.close();
			br.close();
			bw.close();
			fw.close();
			
			oldFile.delete();
			File dump = new File(file);
			newFile.renameTo(dump);
						
		}catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
		
	}
	
	public boolean update(Storable storable) {
		if(storable == null)
			return false;
		
		String file = getFile(storable);
		if(file == "")
			return false;
		
		String tempFile = "dataCSV/tmp.csv";
		String id = storable.getCSV().split(",")[0];
		
		
		
		try {
			String currentLine;

			File oldFile = new File(file);
			File newFile = new File(tempFile);

			FileWriter fw = new FileWriter(tempFile, true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);
			
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			

			while((currentLine = br.readLine()) != null) {
			    if(currentLine.startsWith(id)) {
			    	pw.println(storable.getCSV());
			    	continue;
			    }
			    pw.println(currentLine);
			}
			
			pw.flush();
			pw.close();
			fr.close();
			br.close();
			bw.close();
			fw.close();
			
			oldFile.delete();
			File dump = new File(file);
			newFile.renameTo(dump);
						
		}catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
		
	}

	public boolean reset(Storable storable) {
		if(storable == null)
			return false;
		
		String file = getFile(storable);
		if(file == "")
			return false;
		
		String tempFile = "dataCSV/tmp.csv";
		
		try {

			File oldFile = new File(file);
			File newFile = new File(tempFile);	
			
			File dump = new File(file);
			oldFile.delete();
			newFile.renameTo(dump);
						
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
		
	}

	
	public String getFile(Storable storable) {
		if(storable == null)
			return "";
		String type = storable.getClass().getSimpleName();
		String file = "";
		
		switch (type) {
		case "Dummy":
			file = "dataCSV/dummy.csv";
			break;
		case "UserImpl":
			file = "dataCSV/users.csv";
			break;
		case "ProductTypeImpl":
			file = "dataCSV/product_types.csv";
			break;
		case "BalanceOperationImpl":
			file = "dataCSV/balance_operations.csv";
			break;
		case "SaleTransactionImpl":
			file = "dataCSV/sales.csv";
			break;
		case "ReturnTransactionImpl":
			file = "dataCSV/returns.csv";
			break;
		case "TicketEntryImpl":
			file = "dataCSV/ticket_entries.csv";
			break;
		case "ReturnEntryImpl":
			file = "dataCSV/return_entries.csv";
			break;
		case "CustomerImpl":
			file = "dataCSV/customers.csv";
			break;
		case "OrderImpl":
			file = "dataCSV/orders.csv";
			break;
		case "PositionImpl":
			file = "dataCSV/positions";
			break;
		case "ProductImpl":
			file = "dataCSV/products.csv";
		default:
			break;
		}
		
		return file;
	}

}
