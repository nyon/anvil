package com.ggf.anvil.services;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.ggf.anvil.screens.AbstractScreen;

public class GameStage extends Stage 
{ 
	private AbstractScreen screen;
     public GameStage(AbstractScreen screen, float width, float height, boolean stretch) 
     { 
    	 super();
    	 this.screen = screen;
          this.setCamera(new OrthographicCamera(320, 240));
          this.getCamera().position.set(320/2, 240/2, 0f); 
     } 
     
     @Override
    public boolean keyDown(int keyCode) {
    	// TODO Auto-generated method stub
    	
    	 // TODO: hack
    	 screen.key(keyCode);

    	 return super.keyDown(keyCode);
    }
     
    
     
    
}
