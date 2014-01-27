package com.ggf.anvil.elements;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.ggf.anvil.Anvil;


public class Item extends Actor {
	
	public enum ItemStates {
		ON_ANVIL, ON_ANVIL2, IN_INVENTORY, ROTATE
	}
	
	public enum ItemType {
		CROSS_GUARD, GRIP, BLADE, GEM, WEAPON;

		public static String prettyPrint(ItemType type) {
			if(type == CROSS_GUARD) return "Cross guard";
			if(type == GRIP) return "Grip";
			if(type == BLADE) return "Blade";
			if(type == GEM) return "Gem";
			if(type == WEAPON) return "Weapon";
			
			return "Unknown";
		}
	}
	
	//TODO: Moulds brauchen dazu passende Teile-ID, 
	
	public String name;
	public String graphics;
	public ItemType type;
	public float quality;
	public float sharpness, craftsmanship;
	public ArrayList<WeaponEffect> effects = new ArrayList<WeaponEffect>();
	public int price = 0;
	public int basePrice = 0;
	public float anvilFade;
	public ItemStates state = ItemStates.IN_INVENTORY;
	protected Map<ItemStates, Texture> textureMap = new EnumMap<ItemStates, Texture>(ItemStates.class);
	
	public Item(Texture onAnvilTexture, Texture inInventoryTexture) {
		// TODO: on anvil is not necessary every time
		textureMap.put(ItemStates.ON_ANVIL, onAnvilTexture);
		textureMap.put(ItemStates.IN_INVENTORY, inInventoryTexture);
		setSize(32, 32);
	}
	
	public Item(String filePathOnAnvil, String filePathInInventory, String name){	
		
		this(new Texture(Gdx.files.internal(filePathOnAnvil)), 
				filePathInInventory!=null?new Texture(Gdx.files.internal(filePathInInventory)):null);
		this.name = name;
	}
	
	public Item(Texture texture, Texture texture2, String name2) {
		this(texture, texture2);
		this.name = name2;
	}

	public void putTexture(ItemStates state, Texture tex) {
		textureMap.put(state, tex);
	}
	
	@Override
	public void act(float delta) {
		// TODO Auto-generated method stub
		super.act(delta);
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		if(state == ItemStates.ROTATE) {
			Texture texture1 = textureMap.get(ItemStates.ON_ANVIL);
			batch.setColor(getColor());
			if(texture1.getWidth() < 450){
				batch.draw (TextureRegion.split(texture1, texture1.getWidth(), texture1.getHeight())[0][0], getX(), getY(), 0,0, texture1.getWidth(), texture1.getHeight(),0.25f,0.25f,45);	
			} else {
				setX(getX()-14);
				batch.draw (TextureRegion.split(texture1, texture1.getWidth(), texture1.getHeight())[0][0], getX(), getY(), 0,0, texture1.getWidth(), texture1.getHeight(),0.16f,0.16f,45);	
			}
		}
		else if(state == ItemStates.ON_ANVIL) {
			Texture texture1 = textureMap.get(ItemStates.ON_ANVIL);
			batch.setColor(1, 1, 1, 1.0f-anvilFade);
			batch.draw(texture1, getX(), getY(), 0, 0, (int)getWidth(), (int)getHeight());	
			Texture texture2 = textureMap.get(ItemStates.ON_ANVIL2);
			if(texture2 != null) {
				batch.setColor(1, 1, 1, anvilFade);
				batch.draw(texture2, getX(), getY(), 0, 0, (int)getWidth(), (int)getHeight());	
			}
			
		}
		else {
			Texture inventoryTexture = textureMap.get(ItemStates.IN_INVENTORY);
			if(state == ItemStates.IN_INVENTORY && inventoryTexture == null) {
				Texture texture1 = textureMap.get(ItemStates.ON_ANVIL);
				batch.setColor(getColor());
				float inventoryScaling = 32.0f / (float)Math.max(texture1.getWidth(),texture1.getHeight()) * 1.2f;
				batch.draw (TextureRegion.split(texture1, texture1.getWidth(), texture1.getHeight())[0][0], getX() + 8, getY()-4, 0,0, texture1.getWidth(), texture1.getHeight(),inventoryScaling,inventoryScaling,45);
			}
			else {
				Texture textureToDraw = textureMap.get(state);
				batch.draw(textureToDraw, getX(), getY(), 0, 0, (int)getWidth(), (int)getHeight());			
			}
			
		}

		super.draw(batch, parentAlpha);
	}
	
	public void calculatePrice(){
		price = basePrice;
		Gdx.app.log(Anvil.LOG, "Price before: " + price);
		for(WeaponEffect ef : effects){
			price += price * ef.priceChange;
		}
		
		Gdx.app.log(Anvil.LOG, "Price after: " + price);
	}

	public void setState(ItemStates state) {
		this.state = state;
		Texture texture = textureMap.get(state);
		if(texture != null)
		{
			setWidth(texture.getWidth());
			setHeight(texture.getHeight());
		}
		
	}
	
	
	public static Item randomItem(int day) {
		Random rand = new Random();
		int rnd = rand.nextInt(29); // 24 items
		int i = 0;
		if (rnd == i++) {
			Item item = new Item("data/01_crossguard.png",
					"data/01_crossguard_mini.png", "Crossguard 1");
			item.type = ItemType.CROSS_GUARD;
			item.price = 50;
			return item;
		} else if (rnd == i++) {

			Item item = new Item("data/02_crossguard.png",
					"data/02_crossguard_mini.png", "Crossguard 2");
			item.type = ItemType.CROSS_GUARD;
			item.price = 40;
			return item;
		} else if (rnd == i++) {

			Item item = new Item("data/03_crossguard.png",
					"data/03_crossguard_mini.png", "Crossguard 3");
			item.type = ItemType.CROSS_GUARD;
			item.price = 30;
			return item;
		} else if (rnd == i++) {

			Item item = new Item("data/04_crossguard.png",
					"data/04_crossguard_mini.png", "Crossguard 4");
			item.type = ItemType.CROSS_GUARD;
			item.price = 70;
			return item;
		} else if (rnd == i++) {

			Item item = new Item("data/05_crossguard.png",
					"data/05_crossguard_mini.png", "Crossguard 5");
			item.type = ItemType.CROSS_GUARD;
			item.price = 60;
			return item;
		} else if (rnd == i++) {

			Item item = new Item("data/06_crossguard.png",
					"data/06_crossguard_mini.png", "Crossguard 6");
			item.type = ItemType.CROSS_GUARD;
			item.price = 80;
			return item;
		} else if (rnd == i++) {
			
			Item item = new Item("data/07_crossguard.png",
					"data/07_crossguard_mini.png", "Crossguard 6");
			item.type = ItemType.CROSS_GUARD;
			item.price = 90;
			return item;
		} else if (rnd == i++) {

			Item item = new Item("data/01_blade.png", "data/01_blade_mini.png",
					"Sword");
			item.type = ItemType.BLADE;
			item.price = 100;
			return item;
		} else if (rnd == i++) {

			Item item = new Item("data/02_blade.png", "data/02_blade_mini.png",
					"Sword");
			item.type = ItemType.BLADE;
			item.price = 150;
			return item;
		} else if (rnd == i++) {

			Item item = new Item("data/03_blade.png", "data/03_blade_mini.png",
					"Sword");
			item.type = ItemType.BLADE;
			item.price = 120;
			return item;
		} else if (rnd == i++) {

			Item item = new Item("data/04_blade.png", "data/04_blade_mini.png",
					"Sword");
			item.type = ItemType.BLADE;
			item.price = 200;
			return item;
		} else if (rnd == i++) {

			Item item = new Item("data/05_blade.png", "data/05_blade_mini.png",
					"Trident");
			item.type = ItemType.BLADE;
			item.price = 170;
			return item;
		} else if (rnd == i++) {

			Item item = new Item("data/06_blade.png", "data/06_blade_mini.png",
					"Spear");
			item.type = ItemType.BLADE;
			item.price = 80;
			return item;
		} else if (rnd == i++) {

			Item item = new Item("data/07_blade.png", "data/07_blade_mini.png",
					"Mace");
			item.type = ItemType.BLADE;
			item.price = 110;
			return item;
		} else if (rnd == i++) {

			Item item = new Item("data/08_blade.png", "data/08_blade_mini.png",
					"Halberd");
			item.type = ItemType.BLADE;
			item.price = 210;
			return item;
		} else if (rnd == i++) {

			Item item = new Item("data/09_blade.png", "data/09_blade_mini.png",
					"Morning Star");
			item.type = ItemType.BLADE;
			item.price = 170;
			return item;
		} else if (rnd == i++) {
			
			Item item = new Item("data/10_blade.png", "data/10_blade_mini.png",
					"Halberd");
			item.type = ItemType.BLADE;
			item.price = 190;
			return item;
		} else if (rnd == i++) {

			Item item = new Item("data/01_grip.png", "data/01_grip_mini.png",
					"Grip 1");
			item.type = ItemType.GRIP;
			item.price = 70;
			return item;
		} else if (rnd == i++) {

			Item item = new Item("data/02_grip.png", "data/02_grip_mini.png",
					"Grip 2");
			item.type = ItemType.GRIP;
			item.price = 90;
			return item;
		} else if (rnd == i++) {

			Item item = new Item("data/03_grip.png", "data/03_grip_mini.png",
					"Grip 3");
			item.type = ItemType.GRIP;
			item.price = 100;
			return item;
		} else if (rnd == i++) {

			Item item = new Item("data/04_grip.png", "data/04_grip_mini.png",
					"Grip 4");
			item.type = ItemType.GRIP;
			item.price = 10;
			return item;
		} else if (rnd == i++) {

			Item item = new Item("data/05_grip.png", "data/05_grip_mini.png",
					"Grip 5");
			item.type = ItemType.GRIP;
			item.price = 30;
			return item;
		} else if (rnd == i++) {

			Item item = new Item("data/06_grip.png", "data/06_grip_mini.png",
					"Grip 6");
			item.type = ItemType.GRIP;
			item.price = 70;
			return item;
		} else if (rnd == i++) {

			Item item = new Item("data/07_grip.png", "data/07_grip_mini.png",
					"Grip 7");
			item.type = ItemType.GRIP;
			item.price = 90;
			return item;
		} else if (rnd == i++) {

			Item item = new Item("data/08_grip.png", "data/08_grip_mini.png",
					"Grip 8");
			item.type = ItemType.GRIP;
			item.price = 50;
			return item;
		} else if (rnd == i++) {

			Item item = new Item("data/matresources/gem_1a.png",
					"data/matresources/gem_1a.png", "Amethyst");
			item.type = ItemType.GEM;
			item.price = 300;
			return item;
		} else if (rnd == i++) {

			Item item = new Item("data/matresources/gem_2a.png",
					"data/matresources/gem_2a.png", "Sapphire");
			item.type = ItemType.GEM;
			item.price = 400;
			return item;
		} else if (rnd == i++) {

			Item item = new Item("data/matresources/gem_3a.png",
					"data/matresources/gem_3a.png", "Aquamarine");
			item.type = ItemType.GEM;
			item.price = 250;
			return item;
		} else if (rnd == i++) {

			Item item = new Item("data/matresources/gem_4a.png",
					"data/matresources/gem_4a.png", "Ruby");
			item.type = ItemType.GEM;
			item.price = 500;
			return item;
		}
		return null;
	}

}
