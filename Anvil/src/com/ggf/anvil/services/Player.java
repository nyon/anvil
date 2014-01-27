package com.ggf.anvil.services;

import java.util.ArrayList;
import java.util.List;

import com.ggf.anvil.elements.Item;

public class Player {
	public String name;
	public int level;
	public int gold;
	public int lastGold;
	public boolean newbie;
	public List<String> achievements = new ArrayList<String>();
	public List<Item> inventory = new ArrayList<Item>();

	public Player(){
	}
	
	public void grantAchievement(String achievement) {
		achievements.add(achievement);
	}
	
	public Player(String name, int gold, int level){
		this.name = name;
		this.gold = lastGold = gold;
		this.level = level;
	}
	
	// TODO: Remove to persistent data source
	public void setInitialData(){
		level = 1;
		gold = lastGold = 2000;
		//newbie = true; //FIXME has to be true by default on first startup!
		newbie = false;
		
		this.inventory.clear();
		
		
		

	}
	
	
}
