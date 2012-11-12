package ais;

import java.util.ArrayList;

public class Affinity {
	
	ArrayList<String> bCell1;
	ArrayList<String> bCell2;
	
	public Affinity(BCell bCell1, BCell bCell2){
		this.bCell1 = bCell1.getSet();
		this.bCell2 = bCell2.getSet();
	}
	
	public float measure(){
		ArrayList<String> bShort;
		ArrayList<String> bLong;
		if (bCell1.size() < bCell2.size()){
			bShort = bCell1;
			bLong = bCell2;			
		} else {
			bShort = bCell2;
			bLong = bCell1;	
		}
		int count = 0;
		for(String bs:bShort){
			for(String bl:bLong)
				if (bs.equals(bl)){
					count++;
					break;
				}
		}
		return count/bShort.size();
	}
}
