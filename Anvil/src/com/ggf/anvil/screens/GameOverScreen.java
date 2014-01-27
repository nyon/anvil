package com.ggf.anvil.screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.removeActor;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.ggf.anvil.Anvil;
import com.ggf.anvil.services.ProfileManager;

public class GameOverScreen extends AbstractScreen {
	Image game_over;
	Group bg, fg;
	public GameOverScreen(Anvil game) {
		super(game);
		
		bg = new Group();
		bg.setWidth(GAME_VIEWPORT_WIDTH);
		bg.setHeight(GAME_VIEWPORT_HEIGHT);
		
		fg = new Group();
		fg.setWidth(GAME_VIEWPORT_WIDTH);
		fg.setHeight(GAME_VIEWPORT_HEIGHT);
		
		messageBox("No money left, you starved to death.");
		
		game_over = new Image(new Texture(Gdx.files.internal("data/game_over.png")));
		game_over.setFillParent(true);
		game_over.getColor().a = 0f;
		bg.addActor(game_over);
		
		game_over.addAction(sequence(fadeIn(0.75f), delay(1.5f), fadeOut(0.75f), new Action(){
			@Override
			public boolean act(float delta){
				ProfileManager.getInstance().retrieveProfile().reset();
				GameOverScreen.this.game.setScreen(new MainMenuScreen(GameOverScreen.this.game));
				return true;
			}
		}));
		
		stage.addActor(bg);
		stage.addActor(fg);
	}
	
	private void messageBox(String message) {
		final Image mbox = new Image(new Texture(Gdx.files.internal("data/textbox.png")));
		final Label greet = new Label(message, new Label.LabelStyle(font, Color.WHITE));
		mbox.setPosition(0, 90);
		mbox.setOrigin(mbox.getWidth()/2, mbox.getHeight()/2);
		mbox.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				mbox.clearActions();
				greet.clearActions();
				mbox.addAction(sequence(parallel(fadeOut(0.1f),scaleTo(0,0,0.1f)),removeActor()));
				greet.addAction(fadeOut(0.1f));
				return true;
			}
		});
		fg.addActor(mbox);
		
 		
 		greet.setWidth(320);
 		greet.setPosition(160 - font.getWrappedBounds(message,320).width/2, 120);
 		greet.addAction(sequence(delay(3.0f),fadeOut(2.0f),removeActor()));
 		mbox.addAction(sequence(delay(3.0f),fadeOut(2.0f),removeActor()));
 		greet.setTouchable(Touchable.disabled);
 		fg.addActor(greet);
		
	}

}
