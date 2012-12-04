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
		ArrayList<String> retorno = new ArrayList<String>();
		retorno.addAll(description);
		return retorno;
	}

	public void setDescription(ArrayList<String> description) {
		this.description = new ArrayList<String>();
		this.description.addAll(description);
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

	@Override
	public String toString() {
		return String.format(
				"[%s, %s, %s, %s]",
				value, description, country, stimulation);
	}
}
