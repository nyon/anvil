package com.ggf.anvil.elements;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.ggf.anvil.elements.Item.ItemStates;
import com.ggf.anvil.elements.Item.ItemType;

public class WeaponPrototype extends Group {

	public ArrayList<WeaponPart> parts;
	public ArrayList<Hitpoint> hps;
	public float quality;
	public float sharpness, craftsmanship;
	public ArrayList<WeaponEffect> effects;
	public WeaponEffectList effectPresets;
	public String name;
	public Texture texture;
	public int price;
	public boolean useAnvilController = false;
	public int gripPrice, crossguardPrice, bladePrice;
	
	
	
	public WeaponPrototype(Item grip, Item crossguard, Item blade) {
		gripPrice = grip.price;
		crossguardPrice = crossguard.price;
		bladePrice = blade.price;
		name = "Sword";
		parts = new ArrayList<WeaponPart>();
		hps = new ArrayList<Hitpoint>();
		quality = 0f;
		
		int myCount = 0;
		Random randCount = new Random();
		myCount = randCount.nextInt(2);
		effectPresets = new WeaponEffectList();
		effects = new ArrayList<WeaponEffect>();
		
		//Generate Effects randomly
		ArrayList<Integer> chosen = new ArrayList<Integer>();
		for(int i = 0; i<myCount; i++){
			int effectIndex;
			Random rand = new Random();
			effectIndex = rand.nextInt(200);
			
			
			if(effectPresets.effects.get(effectIndex) != null && !(chosen.contains(effectPresets.effects.get(effectIndex)))){
				effects.add(effectPresets.effects.get(effectIndex));
			}
			chosen.add(effectIndex);
		}
		
		calculateBasePrice();

		
	
		float crossguardX = 64;
		float crossguardY = 90;
		
		grip.setState(ItemStates.ON_ANVIL);
		grip.setX(crossguardX -crossguard.getWidth()/2- grip.getWidth());
		grip.setY(crossguardY-grip.getHeight()/2);
		
		crossguard.setState(ItemStates.ON_ANVIL);	
		crossguard.setX(crossguardX-crossguard.getWidth()/2);
		crossguard.setY(crossguardY-crossguard.getHeight()/2);
		
		
		blade.setState(ItemStates.ON_ANVIL);
		// TODO: glowing
//		blade.putTexture(ItemStates.ON_ANVIL2, new Texture(Gdx.files.internal("data/sword_blade_glowing.png")));
		blade.setX(crossguardX);
		blade.setY(crossguardY-crossguard.getHeight()/2);
		addActor(grip);
		addActor(blade);
		addActor(crossguard);	
		
		
		
		TextureData td;
		td = grip.textureMap.get(ItemStates.ON_ANVIL).getTextureData();
		td.prepare();
		Pixmap gripPix = td.consumePixmap();
		

		td = crossguard.textureMap.get(ItemStates.ON_ANVIL).getTextureData();
		td.prepare();
		Pixmap crossguardPix = td.consumePixmap();

		td = blade.textureMap.get(ItemStates.ON_ANVIL).getTextureData();
		td.prepare();
		Pixmap bladePix = td.consumePixmap();
		
		int swordWidth = gripPix.getWidth() + bladePix.getWidth();
		int swordHeight = Math.max(gripPix.getHeight(),Math.max(crossguardPix.getHeight(), bladePix.getHeight()));
		
		Pixmap swordPix = new Pixmap(swordWidth, swordHeight, Pixmap.Format.RGBA8888);
		swordPix.drawPixmap(gripPix, 0, swordHeight / 2 - gripPix.getHeight()/2);
		swordPix.drawPixmap(bladePix, gripPix.getWidth(), swordHeight / 2 - bladePix.getHeight()/2);
		swordPix.drawPixmap(crossguardPix, gripPix.getWidth() - crossguardPix.getWidth()/2, swordHeight / 2 - crossguardPix.getHeight()/2);
	
		texture = new Texture( swordPix );

		
		
	}
	
	public Item generateItem() {
		Item item = new Item(texture, null,name);
		item.quality = this.quality;
		item.craftsmanship = this.craftsmanship;
		item.sharpness = this.sharpness;
		item.effects = this.effects;
		item.basePrice = this.price;
		item.type = ItemType.WEAPON;
		
		return item;
	}
	
	//TODO: rausnehmen
	public void calculateBasePrice(){
		price = 0;
		price = (int)((gripPrice + crossguardPrice + bladePrice) + ((1.0f+quality)*(1.0f+quality)*(gripPrice + crossguardPrice + bladePrice)));
	}
	
	public float getMaxHeight() {
		float height = super.getHeight();
		for(Actor a : this.getChildren()) {
			height = Math.max(height, a.getHeight());
		}
		return height;
	}

	
	public float getMaxWidth() {
		float width = super.getWidth();
		for(Actor a : this.getChildren()) {
			width = Math.max(width, a.getWidth());
		}
		return width;
	}
	

}
