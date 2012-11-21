package ais;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;


public class File {

	public static BankStatement read(String file){
		BankStatement bStatement = new BankStatement();
		try{
			FileInputStream fstream = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;

			while ((strLine = br.readLine()) != null)   {
				String[] strings = strLine.split(";");  
				Expense expense = new Expense();
				expense.setValue(strings[0]);
				expense.setDescription(strings[1]);
				expense.setCountry(strings[2]);
				bStatement.addExpense(expense);
			}

			in.close();
		} catch (Exception e){
			System.err.println("Error: " + e.getMessage());
		}
		return bStatement;
	}
	
}
