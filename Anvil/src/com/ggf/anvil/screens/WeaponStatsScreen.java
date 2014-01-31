package com.ggf.anvil.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.ggf.anvil.Anvil;
import com.ggf.anvil.elements.Item;
import com.ggf.anvil.elements.Item.ItemStates;
import com.ggf.anvil.elements.WeaponEffect;
import com.ggf.anvil.services.Player;
import com.ggf.anvil.services.ProfileManager;
import com.ggf.anvil.services.SoundManager;
import com.ggf.anvil.services.SoundManager.AnvilSound;


public class WeaponStatsScreen extends AbstractScreen {
	
	Skin skin;
	TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("skin/uiskin.atlas"));

	private Image bgndImage;
	private Item weapon;

	public WeaponStatsScreen(Anvil game, Item weapon) {
		super(game);
		this.weapon = weapon;
		skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
		skin.addRegions(atlas);
		
		font = new BitmapFont(Gdx.files.internal("skin/default.fnt"), false);
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
    stage.draw();
	    SpriteBatch batch = stage.getSpriteBatch();
	    batch.begin();
	    weapon.setColor(1, 1, 1, 1);
	    weapon.setPosition(250, 50);
	    weapon.setState(ItemStates.ROTATE);
	    weapon.draw(batch, 1.0f);
	    
	    batch.end();
	}

	@Override
	public void show() {
		super.show();
        
		 // retrieve the splash image's region from the atlas
        AtlasRegion bgndRegion = getAtlas().findRegion("weapon-stat-screen/bgnd-image");
        Drawable bgndDrawable = new TextureRegionDrawable(bgndRegion);

        // here we create the splash image actor; its size is set when the
        // resize() method gets called
        bgndImage = new Image(bgndDrawable, Scaling.stretch);
        bgndImage.setFillParent(true);

        // and finally we add the actor to the stage
        bg.addActor(bgndImage);
        
        setupGUI();
        
        Image exit = new Image(new Texture(Gdx.files.internal("data/button_exit.png")));
        exit.setPosition(320-exit.getWidth()-1, 240-exit.getHeight()-1);
        exit.addListener(new InputListener() {
        	@Override
        	public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        		game.getMusicManager().stop();
        		game.setScreen(new SmitheryScreen(game));
        		SoundManager.getInstance().play(AnvilSound.CLICK);
        		return true;
        	}
        });
        fg.addActor(exit);
       
	}
	
	public void setupGUI(){
		 Texture upButton = new Texture(Gdx.files.internal("ui/02_button_up.png"));
	        Texture downButton = new Texture(Gdx.files.internal("ui/02_button_down.png"));
	        NinePatch up_patch = new NinePatch(upButton, 5, 5, 5, 5);
	        NinePatch down_patch = new NinePatch(downButton, 5, 5, 5, 5);
	        NinePatchDrawable up_drawable = new NinePatchDrawable(up_patch);
	        NinePatchDrawable down_drawable = new NinePatchDrawable(down_patch);
	        TextButtonStyle style = new TextButtonStyle(up_drawable, down_drawable, up_drawable, new BitmapFont());
	       // TextButtonStyle textButtonStyle = new TextButtonStyle();
//	        textButtonStyle.up = new TextureRegionDrawable(upRegion);
//	        textButtonStyle.down = new TextureRegionDrawable(downRegion);
//	        textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
//	        textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
//	        textButtonStyle.font = skin.getFont("default");
	        skin.add("default", style);
		LabelStyle statsLabelStyle = new LabelStyle(font, Color.WHITE);
				
		TextButton sellButton = new TextButton("Sell it!", style);
        sellButton.setSize(88, 20);
        sellButton.setPosition(220, 10);
		fg.addActor(sellButton);
		sellButton.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				game.setScreen(new MarketplaceScreen(game));
				return true;
			}
		});
		
		TextButton grindButton = new TextButton("Grind it!", style);
		grindButton.setSize(88, 20);
		grindButton.setPosition(115, 10);
		fg.addActor(grindButton);
		grindButton.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				game.setScreen(new SmitheryScreen(game));
				return true;
			}
		});
		
		TextButton scrapButton = new TextButton("Scrap it!", style);
		scrapButton.setSize(88, 20);
		scrapButton.setPosition(10, 10);
		fg.addActor(scrapButton);
		scrapButton.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				Player p = ProfileManager.getInstance().retrieveProfile().getPlayer();
				p.inventory.remove(weapon);
				game.setScreen(new SmitheryScreen(game));
				return true;
			}
		});
		
		font.setScale(1.0f);
		Label qualityLabel = new Label("Quality: \n" + "    " + (int)(weapon.quality * 100) + "%", statsLabelStyle) {
				public void draw(SpriteBatch batch, float parentAlpha) 
				{
					font.setScale(1.2f);
					super.draw(batch, parentAlpha);
					font.setScale(1.0f);
				};
			};
		
		qualityLabel.setSize(88, 18);
		
		Label swordLabel = new Label("Sword", statsLabelStyle) {
			public void draw(SpriteBatch batch, float parentAlpha) 
			{
				font.setScale(1.2f);
				super.draw(batch, parentAlpha);
				font.setScale(1.0f);
			};
		};
		
		swordLabel.setSize(88, 18);
		qualityLabel.setPosition(226, 180);
			
			
		//Statbox
		String myEffectString = "";
		if(weapon.effects != null && weapon.effects.size() > 0)
		{
			myEffectString += "Effects: \n";
			for(WeaponEffect ef : weapon.effects){
				myEffectString += ef.name + " " + ef.bonus + "\n"; 
			}			
		}
		
		
		
		Label statsLabel = new Label("Name: " + weapon.name + "\n" + 
									"Sharpness: " + weapon.sharpness + "\n" +
									"Craftsmanship: " + weapon.craftsmanship + "\n" + "\n" +
									myEffectString+"Price: "+weapon.price+" $", statsLabelStyle) {
			public void draw(SpriteBatch batch, float parentAlpha) 
			{
				font.setScale(0.6f);
				super.draw(batch, parentAlpha);
				font.setScale(1.0f);
			};
		};
		
		statsLabel.setSize(88, 18);
		font.setScale(0.6f);
		int positionYLabel = 240 - 30 - (int)font.getWrappedBounds(statsLabel.getText(), 320).height/2;
		statsLabel.setPosition(15, positionYLabel);
		fg.addActor(statsLabel);
		fg.addActor(qualityLabel);
//		fg.addActor(swordLabel);
		font.setScale(1.0f);
	}
	
	

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
