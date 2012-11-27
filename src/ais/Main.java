package ais;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

import com.sun.xml.internal.ws.api.ha.StickyFeature;

public class Main {

	private static final int 	INITIAL_MEMORY_CELLS 		= 10;
	private static final double AFFINITY_THRESHOLD 			= 10;
	private static final double CLASSIFICATION_THRESHOLD 	= 10;
	private static final int 	NEWS_STIMULATION 			= 10;
	private static final int 	MEMORY_STIMULATION 			= 10;
	private static final double CLONING_RATE 				= 10;
	private static final double MUTATION_RATE 				= 10;
	
	public static String newExpensePath = null;
	
	private static ArrayList<BCell> newBCells 		= new ArrayList<BCell>();
	private static ArrayList<BCell> memoryBCells 	= new ArrayList<BCell>();
	private static ArrayList<BCell> toClassify 		= new ArrayList<BCell>();
	private static ArrayList<BCell> wrong 			= new ArrayList<BCell>();
	

	public static void main(String[] args) {
		
		ArrayList<BankStatement> bankStatements = new ArrayList<BankStatement>();
		bankStatements.add(read("Bank Statement.txt"));
		train(bankStatements);
		while(true){
			if(newExpensePath != null){
				BankStatement newStatement = read(newExpensePath);
				Expense newExpense = newStatement.get(0);
				BCell antigen = convert(newExpense);
				if (classify(antigen) == false){
					toClassify.add(antigen);
				}				
			}
			for (BCell antigen : toClassify) {
				if(antigen.getStimulation() > 0){
					updatePopulation(antigen);
					wrong.add(antigen);
					toClassify.remove(antigen);
				}
			}
		}
	}
	
	public static void train(ArrayList<BankStatement> bankStatements){
		for (BankStatement bankStatement : bankStatements) {
			for (Expense expense : bankStatement.getExpenses()) {
				newBCells.add(convert(expense));
				}		
		}
		for (int i = 0; i < INITIAL_MEMORY_CELLS; i++) {
			BCell promotedBCell = newBCells.get(i);
			promotedBCell.setStimulation(MEMORY_STIMULATION);
			memoryBCells.add(promotedBCell);
			newBCells.remove(i);
		}
		for (BCell newBCell : newBCells) {
			for (BCell memoryBCell : memoryBCells) {
				if(measureAffinity(memoryBCell, newBCell) > AFFINITY_THRESHOLD){
					ArrayList<BCell> clones = clone_mutate(memoryBCell,newBCell);
					for (BCell clone : clones) {
						if(measureAffinity(clone, newBCell) > measureAffinity(memoryBCell, newBCell)){
						newBCells.add(clone);
						}
					}
				}
			}			
		}
	}

	private static BCell convert(Expense expense) {
		BCell bCell = new BCell();
		bCell.setValue(Double.parseDouble(expense.getValue()));
		String[] descriptionSplit = expense.getDescription().split(";");
		ArrayList<String> descriptionList = new ArrayList<String>();
		for (int i = 0; i < descriptionSplit.length; i++) {
			descriptionList.add(descriptionSplit[i]);
		}
		bCell.setDescription(descriptionList);
		bCell.setCountry(expense.getCountry());
		bCell.setStimulation(NEWS_STIMULATION);
		return bCell;
	}	
	
	public static double measureAffinity(BCell bCell1, BCell bCell2){
		
		double country = 0;
		if(bCell1.getCountry().equals(bCell2.getCountry())){
			country = 1;
		}
		
		double bigger = Math.max(bCell1.getValue(), bCell2.getValue());
		double smaller = Math.min(bCell1.getValue(), bCell2.getValue());		
		double value = bigger - smaller;
		value = value / smaller;
			
		double description;
		ArrayList<String> biggerDescription;
		ArrayList<String> smallerDescription;
		if (bCell1.getDescription().size() < bCell2.getDescription().size()){
			biggerDescription = bCell2.getDescription();
			smallerDescription = bCell1.getDescription();			
		} else {
			biggerDescription = bCell1.getDescription();
			smallerDescription = bCell2.getDescription();	
		}
		int count = 0;
		for(String wordBigger:biggerDescription){
			for(String wordSmaller:smallerDescription)
				if (wordSmaller.equals(wordBigger)){
					count++;
					break;
				}
		}
		description =  count/smallerDescription.size();
		
		return country + value + description;
	}
	
	public static ArrayList<BCell> clone_mutate(BCell bCell1, BCell bCell2){
		double affinity = measureAffinity(bCell1, bCell2);
		ArrayList<BCell> clones = new ArrayList<BCell>();
		int numClones = (int) Math.round(affinity * CLONING_RATE);
		int numMutate = (int) Math.round((1-affinity)* /*bc’s feature vector length * */  MUTATION_RATE);
		for (int i = 0; i < numClones; i++) {
			BCell clone = bCell1;
			for (int j = 0; j < numMutate; j++) {
				int p; // = a random point in bcx’s feature vector
				String w // = a random word from the appropriate gene library replace word in bcx’s feature vector at location p with w
			}
			// bcx’s stimulation level ← Ksb
			clones.add(clone);
		}
		return clones;
	}
	
	//true=correta
	public static boolean classify(BCell antigen){
		ArrayList<BCell> all = new ArrayList<BCell>();
		all.addAll(memoryBCells);
		all.addAll(newBCells);
		for (BCell bCell : all)
		{
			if(measureAffinity(antigen, bCell) > CLASSIFICATION_THRESHOLD){
				return true;
			}
		}
		return false;
	}
	
	public static void updatePopulation(BCell antigen){
		if(antigen.getStimulation() == 2){
			double bestAffinity = 0;
			BCell bestNewBCell = null;
			for (BCell newBCell : newBCells) {
				double affinity = measureAffinity(newBCell, antigen);
				if(affinity > AFFINITY_THRESHOLD){
					newBCell.setStimulation(newBCell.getStimulation()+1);
				}
				if(affinity > bestAffinity){
					bestAffinity = affinity;
					bestNewBCell = newBCell;
				}
			}
			ArrayList<BCell> clones = clone_mutate(bestNewBCell, antigen);
			newBCells.addAll(clones);
			bestAffinity = 0;
			for (BCell newBCell : newBCells) {
				double affinity = measureAffinity(newBCell, antigen);
				if(affinity > bestAffinity){
					bestAffinity = affinity;
					bestNewBCell = newBCell;
				}
			}
			bestAffinity = 0;
			BCell bestMemoryBCell = null;
			for (BCell memoryBCell : memoryBCells) {
				double affinity = measureAffinity(memoryBCell, antigen);
				if(affinity > bestAffinity){
					bestAffinity = affinity;
					bestMemoryBCell = memoryBCell;
				}
			}
			if(measureAffinity(bestNewBCell, antigen) > measureAffinity(bestMemoryBCell, antigen)){
				//
			}
		} else {
			
		}
	}
	
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
