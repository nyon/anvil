package com.ggf.anvil.elements;

public class WeaponEffect {
	public String name, bonus;
	public Float priceChange;
	public int gemColour;
	
	public WeaponEffect(String name, String bonus, Float priceChange, int gemColour){
		this.name = name;
		this.bonus = bonus;
		this.priceChange = priceChange;
		this.gemColour = gemColour;		
	}
}
