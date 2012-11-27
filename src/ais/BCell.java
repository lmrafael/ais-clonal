package ais;

import java.util.ArrayList;

public class BCell {
	
	private double 				value;
	private ArrayList<String> 	description;
	private String 				country;
	private int					stimulation;
	
	public BCell() {
		this.description = new ArrayList<String>();
		this.stimulation = 0;
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

	public int getStimulation() {
		return stimulation;
	}

	public void setStimulation(int stimulation) {
		this.stimulation = stimulation;
	}
}
