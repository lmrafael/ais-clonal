package ais;

import java.util.ArrayList;

public class Train {

	public static void start(ArrayList<BankStatement> bankStatements){
		for (BankStatement bankStatement : bankStatements) {
			for (Expense expense : bankStatement.getExpenses()) {
				BCell bCell = new BCell();
				bCell.setValue(Double.parseDouble(expense.getValue()));
				String[] descriptionSplit = expense.getDescription().split(";");
				ArrayList<String> descriptionList = new ArrayList<String>();
				for (int i = 0; i < descriptionSplit.length; i++) {
					descriptionList.add(descriptionSplit[i]);
				}
				bCell.setDescription(descriptionList);
				bCell.setCountry(expense.getCountry());
				//add subject words and sender words to appropriate library
			}

		
		}
	}
	
//	remove Kt random elements from TE
//	and insert into MC
//	FOREACH(mc  MC)
//	mc’s stimulation count ← Ksm
//	FOREACH(te  TE)
//	te’s stimulation count ← Ksb
//	FOREACH(mc  MC)
//	IF(affinity(mc,te) > Ka)
//	clones ← clone_mutate(mc,te)
//	FOREACH(clo clones)
//	IF(affinity(clo,bc) >=
//	affinity(mc,te))
//	BC ← BC ∪ {clo}
	
}
