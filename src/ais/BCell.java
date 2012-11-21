package ais;

import java.util.ArrayList;

public class BCell {
	
	private double 				value;
	private ArrayList<String> 	description;
	private String 				country;
	
	public BCell() {
		this.description = new ArrayList<String>();
	}
	
	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public ArrayList<String> getDescription() {
		return description;
	}

	public void setDescription(ArrayList<String> description) {
		this.description = description;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
}
