package ais;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	private static final int 	INITIAL_MEMORY_CELLS 		= 4;
	private static final double AFFINITY_THRESHOLD 			= 0.55;
	private static final double CLASSIFICATION_THRESHOLD 	= 0.6;
	private static final int 	NEWS_STIMULATION 			= 10;
	private static final int 	MEMORY_STIMULATION 			= 10;
	private static final double CLONING_RATE 				= 20;
	private static final double MUTATION_RATE 				= 15;
	
	public static String newExpensePath = null;
	
	private static ArrayList<BCell> newBCells 			= new ArrayList<BCell>();
	private static ArrayList<BCell> memoryBCells 		= new ArrayList<BCell>();
	private static ArrayList<BCell> toClassify 			= new ArrayList<BCell>();
	private static ArrayList<BCell> classified 			= new ArrayList<BCell>();
	private static ArrayList<BCell> wrong 				= new ArrayList<BCell>();
	
	private static ArrayList<String> descriptionsLib	= new ArrayList<String>();	
	private static ArrayList<String> countriesLib		= new ArrayList<String>();

	public static void main(String[] args) {
		/*Le todas as minhas faturas e poe num array de faturas*/
		ArrayList<BankStatement> bankStatements = new ArrayList<BankStatement>();
		bankStatements.add(read("Bank Statement 01.txt"));
//		bankStatements.add(read("Bank Statement 02.txt"));
//		bankStatements.add(read("Bank Statement 03.txt"));
//		bankStatements.add(read("Bank Statement 04.txt"));
//		bankStatements.add(read("Bank Statement 05.txt"));
//		bankStatements.add(read("Bank Statement 06.txt"));
		/*Inicia o treinamento do meu sistema imunologico*/
		train(bankStatements);

		while (true){
			while(toClassify == null || toClassify.isEmpty()){
				newExpensePath = "New Bank Statement.txt";
//				message("Digite o path do txt com a expense...");
//				Scanner in = new Scanner(System.in);
//				newExpensePath = in.nextLine();
//			    in.close();   
			    /*Recebe uma nova cobrança converte em antigeno e classifica*/
				if(newExpensePath != null && newExpensePath != ""){         
					BankStatement newStatement = read(newExpensePath);
					Expense newExpense = newStatement.getExpenses().get(0);
					BCell antigen = convert(newExpense);
					antigen.setStimulation(0);
					/*Caso tenha sido classificada como falsa coloca num array para esperar validação*/
					if (classify(antigen) == false){
						toClassify.add(antigen);
					}				
				}
				newExpensePath = null;
			}
			int result = 0;
			for (BCell antigen : toClassify) {
				message("Classifique o cobrança " + antigen.toString() + " como:");
				message("1 - Errada");
				message("2 - Correta");
				Scanner in = new Scanner(System.in);
				result = in.nextInt();
			    in.close();   
			    //antigen.setStimulation(result);
			    //classified.add(antigen);
			    //toClassify.remove(antigen);
			}
			for (BCell antigen : classified) {
				if(antigen.getStimulation() > 0){
					updatePopulation(antigen);
					wrong.add(antigen);
					classified.remove(antigen);
				}
			}
		}
	}
	
	public static void train(ArrayList<BankStatement> bankStatements){
		/*Converte despesas em novas celulas B*/
		for (BankStatement bankStatement : bankStatements) {
			for (Expense expense : bankStatement.getExpenses()) {
				BCell bCell = convert(expense);
				newBCells.add(bCell);
				descriptionsLib.addAll(bCell.getDescription());
				countriesLib.add(bCell.getCountry());
				}		
		}
		/*promove um numero determinado de novas celulas B a celulas B de Memoria*/
		for (int i = 0; i < INITIAL_MEMORY_CELLS; i++) {
			BCell promotedBCell = newBCells.get(i);
			promotedBCell.setStimulation(MEMORY_STIMULATION);
			memoryBCells.add(promotedBCell);
			newBCells.remove(i);
		}
		/*Mede a afinidade entre as novas celuas B e as celulas B de Memoria*/
		message("Calculando afnidade entre celulas novas e de memoria.");
		for (BCell newBCell : newBCells) {
			for (BCell memoryBCell : memoryBCells) {
				if(measureAffinity(memoryBCell, newBCell) > AFFINITY_THRESHOLD){
					/*Se a afinidade for maior que um valor definido, clona/muta as celulas*/
					message("Afinidade encontrada, clonando.");
					ArrayList<BCell> clones = clone_mutate(memoryBCell,newBCell);
					message(clones.size() + " clones gerados.");
					int count = 0;
					for (BCell clone : clones) {
						/*Adiciona todos os clones com afinidade maiores que as celulas de memoria ao Array de novas celulas B*/
						if(measureAffinity(clone, newBCell) > measureAffinity(memoryBCell, newBCell)){
							count++;
							newBCells.add(clone);
						}
					}
					message(count + " clones aceitos.");
				}
			}
		}
	}

	/*Mede a afinidade entre duas celulas*/
	public static double measureAffinity(BCell bCell1, BCell bCell2){
		//message("Calculando afinidade entre " + bCell1.toString() + " e " + bCell2.toString());
		double country = 0;
		if(bCell1.getCountry().equals(bCell2.getCountry())){
			country = 0.33;
		}
		
		double bigger = Math.max(bCell1.getValue(), bCell2.getValue());
		double smaller = Math.min(bCell1.getValue(), bCell2.getValue());		
		double value = ( bigger / smaller );
		if (value > 3){
			value = 0;
		} else {
			value = value / 9;
		}
		
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
		description =  ( count / smallerDescription.size() ) / 3;
		
		double ret = country + value + description;
		
		if (ret > 1){
			ret = 1;
		}
		//message("Afinidade: V=" + value + " D=" + description + " C=" + country + "Tot= " + ret);
		return ret;
	}
	
	/*Clona e muta uma celula celulas*/
	public static ArrayList<BCell> clone_mutate(BCell bCell1, BCell bCell2){
		double affinity = measureAffinity(bCell1, bCell2);
		ArrayList<BCell> clones = new ArrayList<BCell>();
		int numClones = (int) Math.round(affinity * CLONING_RATE);
		int numMutate = (int) Math.round((1-affinity)* (bCell1.getDescription().size() + bCell2.getDescription().size()) *  MUTATION_RATE);
		for (int i = 0; i < numClones; i++) {
			BCell clone = bCell1;
			for (int j = 0; j < numMutate; j++) {
				
				double value = Math.random() * (clone.getValue()/4);
				clone.setValue(value);
		
				int countryRandom = (int) Math.round(Math.random() * (countriesLib.size()-1));
				clone.setCountry(countriesLib.get(countryRandom));
				
				ArrayList<String> description = clone.getDescription();
				int cellDescriptionRandom = (int) Math.round(Math.random() * (description.size()-1));
				int descriptionRandom = (int) Math.round(Math.random() * (descriptionsLib.size()-1));
				description.set(cellDescriptionRandom, descriptionsLib.get(descriptionRandom));
			}
			clone.setStimulation(NEWS_STIMULATION);
			clones.add(clone);
		}
		return clones;
	}
	
	//true=correta
	public static boolean classify(BCell antigen){
		ArrayList<BCell> all = getAllBCells();
		for (BCell bCell : all)
		{
			if(measureAffinity(antigen, bCell) > CLASSIFICATION_THRESHOLD){
				message(antigen.toString() + " OK"); 
				return true;
			}
		}
		message(antigen.toString() + " Baaaad"); 
		return false;
	}

	private static ArrayList<BCell> getAllBCells() {
		ArrayList<BCell> all = new ArrayList<BCell>();
		all.addAll(memoryBCells);
		all.addAll(newBCells);
		return all;
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
				newBCells.remove(bestNewBCell);
				bestNewBCell.setStimulation(MEMORY_STIMULATION);
				memoryBCells.add(bestNewBCell);
			}
			for (BCell memoryBCell : memoryBCells) {
				if (measureAffinity(bestMemoryBCell, antigen) > AFFINITY_THRESHOLD){
					memoryBCell.setStimulation(memoryBCell.getStimulation()-1);
				}
			}
			descriptionsLib.addAll(antigen.getDescription());
			countriesLib.add(antigen.getCountry());
		} else {
			ArrayList<BCell> all = getAllBCells();
			for (BCell bCell : all) {
				if (measureAffinity(bCell, antigen) > AFFINITY_THRESHOLD){
					memoryBCells.remove(bCell);
					newBCells.remove(bCell);
					descriptionsLib.removeAll(antigen.getDescription());
					countriesLib.remove(antigen.getCountry());
				}
			}
		}
		for (BCell newBCell : newBCells) {
			newBCell.setStimulation(newBCell.getStimulation()-1);
		}
		ArrayList<BCell> all = getAllBCells();
		for (BCell bCell : all) {
			if (bCell.getStimulation() == 0){
				memoryBCells.remove(bCell);
				newBCells.remove(bCell);
			}
		}
	}

	/*Funcao para ocnverter uma cobranca numa Celula B*/
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
	
	/*Funcao para ler um arquivo de fatura para a memoria*/
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
	
	public static void message(String message){
		System.out.println(message);
	}
}
