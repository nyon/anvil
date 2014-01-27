package com.ggf.anvil.services;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.ggf.anvil.Anvil;

/**
 * The player's profile.
 * <p>
 * This class is used to store the game progress, and is persisted to the file
 * system when the game exists.
 * 
 * @see ProfileManager
 */
public class Profile implements Serializable{
    private Player player;
    public int hour;
    public int day;
    public int weaponsCrafted;
    public Merchant merchant;

    public Profile(){
    	player = new Player("Bob", 23, 3);
    	day = 1;
    	
    	reset();
    }


    public Player getPlayer(){
        return player;
    }


    // Serializable implementation

    @Override
    public void read(Json json, JsonValue jsonData){
        // read the Player
         // player = json.readValue("player", Player.class, jsonData);
        // player.achievements = (java.util.List<String>)json.readValue("achievements", java.util.List.class, String.class, jsonData);
        // FIXME : see below :)
       // player.inventory = (java.util.List<Item>)json.readValue("inventory", java.util.List.class, Item.class, jsonData);
        reset();
    }

    @Override
    public void write(Json json){
    	// FIXME : see below :)
        // json.writeValue( "player", player );
        // json.writeValue("achievements", player.achievements);
        // json.writeValue("inventory", player.inventory);
    }


	public void reset() {
		// TODO Auto-generated method stub
		player.setInitialData();
        hour = 0;
        day = 1;
        weaponsCrafted = 0;
        merchant = Merchant.randomMerchant(1);
	}


	public void incWeaponsCrafted() {
		weaponsCrafted++;
		if(weaponsCrafted >= 1) {
			Anvil.instance.grantAchievement("1_weapon");
		}
		if(weaponsCrafted >= 50) {
			Anvil.instance.grantAchievement("50_weapons");
		}
		if(weaponsCrafted >= 200) {
			Anvil.instance.grantAchievement("200_weapons");
		}
				
	}

}