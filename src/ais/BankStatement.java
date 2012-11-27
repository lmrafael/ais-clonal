package ais;

import java.util.ArrayList;

public class BankStatement extends ArrayList<Expense>{
	
	private static final long serialVersionUID = 7554380207548824421L;

	ArrayList<Expense> expenses;
	
	public BankStatement() {
		this.expenses = new ArrayList<Expense>();
	}

	public ArrayList<Expense> getExpenses() {
		return expenses;
	}

	public void setExpenses(ArrayList<Expense> expenses) {
		this.expenses = expenses;
	}
	
	public void addExpense(Expense expense) {
		this.expenses.add(expense);
	}

	@Override
	public String toString() {
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("BankStatement \n------------------------------------------------------\n");
		for (int i = 0; i < expenses.size(); i++) {
			sBuffer.append(expenses.get(i).getValue() + "\t\t");
			String description = expenses.get(i).getDescription();
			sBuffer.append(description + "\t\t\t");
			if(description.length()<4){
				sBuffer.append("\t");
			}
			sBuffer.append(expenses.get(i).getCountry() + "\n");
		}
		return sBuffer.toString();
	}
	
}
