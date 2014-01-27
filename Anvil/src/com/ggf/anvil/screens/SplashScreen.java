package com.ggf.anvil.screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.ggf.anvil.Anvil;

/**
 * Shows a splash image and moves on to the next screen.
 */
public class SplashScreen extends AbstractScreen{
    private Image splashImage;
    private Image companyLogo;
    private Group bg;

    
    public SplashScreen(Anvil game){
        super(game);
        
        bg = new Group();
        bg.setWidth(GAME_VIEWPORT_WIDTH);
		bg.setHeight(GAME_VIEWPORT_HEIGHT);
        
        // retrieve the splash image's region from the atlas
        AtlasRegion logoRegion = this.getAtlas().findRegion("splash-screen/company-logo");
        Drawable logoDrawable = new TextureRegionDrawable(logoRegion);

        // here we create the splash image actor; its size is set when the
        // resize() method gets called
        companyLogo = new Image(logoDrawable, Scaling.stretch);
        companyLogo.setFillParent(true);
        
        // this is needed for the fade-in effect to work correctly; we're just
        // making the image completely transparent
        companyLogo.getColor().a = 0f;
        
        // retrieve the splash image's region from the atlas
        AtlasRegion splashRegion = this.getAtlas().findRegion("splash-screen/splash-image");
        Drawable splashDrawable = new TextureRegionDrawable(splashRegion);

        // here we create the splash image actor; its size is set when the
        // resize() method gets called
        splashImage = new Image(splashDrawable, Scaling.stretch);
        splashImage.setFillParent(true);

        // this is needed for the fade-in effect to work correctly; we're just
        // making the image completely transparent
        splashImage.getColor().a = 0f;
        

        // configure the fade-in/out effect on the splash image
       	companyLogo.addAction(sequence(fadeIn(0.75f), delay(1.75f), fadeOut(0.75f), new Action(){
                @Override
                public boolean act(float delta){
                    return true;
                }
            }));
       	
       	
        splashImage.addAction(sequence(delay(3.25f), fadeIn(0.75f), delay(1.75f), fadeOut(0.75f), new Action(){
        	@Override
        	public boolean act(float delta){
        		SplashScreen.this.game.setScreen(new MainMenuScreen(SplashScreen.this.game));
        		return true;
        	}
        }));

        // and finally we add the actor to the stage
        bg.addActor(companyLogo);
        bg.addActor(splashImage);
        stage.addActor(bg);
    }

    @Override
    public void show(){
        super.show();

    }
    
}
