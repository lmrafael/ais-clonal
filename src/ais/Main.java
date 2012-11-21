package ais;

import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		
		ArrayList<BankStatement> bankStatements = new ArrayList<BankStatement>();
		bankStatements.add(File.read("Bank Statement.txt"));
		Train.start(bankStatements);
		//	wait until (an e-mail arrives or a user action is intercepted)
		//	ag - convert e-mail into antigen
		//	IF(ag requires classification)
		//	classify(ag)
		//	IF(ag classified as uninteresting)
		//	move ag into user accessible storage
		//	ELSE
		//	allow e-mail to pass through
		//	IF(user has given feedback on ag)
		//	update_population(ag)

	}

}
