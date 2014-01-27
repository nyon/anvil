package com.ggf.anvil.elements;

import java.util.ArrayList;
import java.util.Random;

public class WeaponEffectList {
	
	ArrayList<WeaponEffect> effects = new ArrayList<WeaponEffect>();
	
	public WeaponEffectList(){
		fillWeaponEffects();
	}
	
	public WeaponEffect getEffectByColor(int color){
		ArrayList<WeaponEffect> effectsInColor = new ArrayList<WeaponEffect>();
		for (WeaponEffect effect : effects) {
			if (effect.gemColour == color) {
				effectsInColor.add(effect);
			}
		}
		
		if (effectsInColor.size() > 0) {
			int index = new Random().nextInt(effectsInColor.size());
			
			return effectsInColor.get(index);
		}
		
		return null;
	}

	private void fillWeaponEffects() {
		String myName, myBonus;
		int myColour;
		Float myPriceChange, myBasePriceChange;
		myName = "";
		myBonus = "";
		myColour = 100;
		myPriceChange = 0f;
		myBasePriceChange = 0f;
		
		
		for(int i=0; i<20; i++){
			switch(i){
			case 0: myName = "Strength";
					myColour = 0;
					myBasePriceChange = 0.2f;
					break;
			case 1: myName = "Dexterity";
					myColour = 1;
					myBasePriceChange = 0.2f;
					break;
			case 2: myName = "Luck";
					myColour = 2;
					myBasePriceChange = 0.2f;
					break;
			case 3: myName = "Looting";
					myColour = 3;
					myBasePriceChange = 0.2f;
					break;
			case 4: myName = "Wisdom";
					myColour = 0;
					myBasePriceChange = 0.1f;
					break;
			case 5: myName = "Charisma";
					myColour = 1;
					myBasePriceChange = 0.1f;
					break;
			case 6: myName = "Stabiness";
					myColour = 2;
					myBasePriceChange = 0.1f;
					break;
			case 7: myName = "Sliciness";
					myColour = 3;
					myBasePriceChange = 0.1f;
					break;
			case 8: myName = "Baking";
					myColour = 0;
					myBasePriceChange = -0.2f;
					break;
			case 9: myName = "Falling Down Stairs";
					myColour = 1;
					myBasePriceChange = -0.2f;
					break;
			case 10: myName = "Sneaking";
					myColour = 2;
					myBasePriceChange = 0.3f;
					break;
			case 11: myName = "Shadowbolt";
					myColour = 3;
					myBasePriceChange = 0.1f;
					break;
			case 12: myName = "Opening Beer";
					myColour = 0;
					myBasePriceChange = -0.4f;
					break;
			case 13: myName = "Fireball";
					myColour = 1;
					myBasePriceChange = 0.1f;
					break;
			case 14: myName = "Lightning Strike";
					myColour = 2;
					myBasePriceChange = 0.1f;
					break;
			case 15: myName = "Monster Kill";
					myColour = 3;
					myBasePriceChange = 0.1f;
					break;
			case 16: myName = "C-C-C-C-Combobreaker";
					myColour = 0;
					myBasePriceChange = 0.1f;
					break;
			case 17: myName = "Oexel";
					myColour = 1;
					myBasePriceChange = -0.5f;
					break;
			case 18: myName = "Cursed";
					myColour = 2;
					myBasePriceChange = -0.2f;
					break;
			case 19: myName = "Antaluxia";
					myColour = 3;
					myBasePriceChange = 0.5f;
					break;
			case 20: myName = "Fatality";
					myColour = 0;
					myBasePriceChange = 0.5f;
					break;
			}
			
			for(int j=1; j<=10; j++){
				myBonus = "+" + j;
				myPriceChange = myBasePriceChange + (j/10.0f);
				effects.add(new WeaponEffect(myName, myBonus, myPriceChange, myColour));
			}
		}
	}
}
