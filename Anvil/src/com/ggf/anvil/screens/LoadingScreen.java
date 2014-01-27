package com.ggf.anvil.screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.ggf.anvil.Anvil;

public class LoadingScreen extends AbstractScreen {
	Image loadingImage;
	Anvil game;
	AbstractScreen parentscreen;
	Group bg;
	public LoadingScreen(Anvil game, AbstractScreen parentscreen) {
		super(game);
		this.game = game;
		
		bg = new Group();
        bg.setWidth(GAME_VIEWPORT_WIDTH);
		bg.setHeight(GAME_VIEWPORT_HEIGHT);
		
		Image loadingImage;
		// retrieve the splash image's region from the atlas
        AtlasRegion loadingRegion = this.getAtlas().findRegion("intro-video/loading-screen");
        Drawable loadingDrawable = new TextureRegionDrawable(loadingRegion);

        // here we create the splash image actor; its size is set when the
        // resize() method gets called
        loadingImage = new Image(loadingDrawable, Scaling.stretch);
        loadingImage.setFillParent(true);
        
        loadingImage.addAction(sequence(delay(0.05f), new Action(){
        	@Override
        	public boolean act(float delta){
        		LoadingScreen.this.game.setScreen(new IntroScreen(LoadingScreen.this.game));
        		return true;
        	}
        }));
      
		bg.addActor(loadingImage);
		stage.addActor(bg);
		System.out.println("Image loaded!");
	}
	
	@Override
	public void render(float delta){
		super.render(delta);
		System.out.println("next screen");
		//game.setScreen(new IntroScreen(this.game));
	}

}
