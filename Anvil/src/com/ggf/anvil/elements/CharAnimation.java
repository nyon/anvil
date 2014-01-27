package com.ggf.anvil.elements;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;



public class CharAnimation {

	public enum Direction { up, down, left, right };
	public enum State { wait, walking };
	
	Direction dir = Direction.down;
	State state = State.walking;
	
	float nextState = 500;
	float nextStateCounter = 0;
	
	float x = 100;
	float y = 100;
	
	float speed = 0.5f;
	
	TextureRegion[] upFrames = new TextureRegion[3];
	TextureRegion[] downFrames = new TextureRegion[3];
	TextureRegion[] leftFrames = new TextureRegion[3];
	TextureRegion[] rightFrames = new TextureRegion[3];
	TextureRegion currentFrame;
	
	Animation upAnimation;
	Animation upIdleAnimation;
	Animation downAnimation;
	Animation downIdleAnimation;
	Animation leftAnimation;
	Animation leftIdleAnimation;
	Animation rightAnimation;
	Animation rightIdleAnimation;
	float stateTime;
	
	public CharAnimation() {
		TextureRegion[][] tmp = TextureRegion.split( new Texture(Gdx.files.internal("data/walking_spritesheet.png")), 32, 48);
		
		upFrames[0] = tmp[1][0];
		upFrames[1] = tmp[1][1];
		upFrames[2] = tmp[1][2];
		
		downFrames[0] = tmp[0][0];
		downFrames[1] = tmp[0][1];
		downFrames[2] = tmp[0][2];
		
		leftFrames[0] = tmp[2][0];
		leftFrames[1] = tmp[2][1];
		leftFrames[2] = tmp[2][2];
		
		rightFrames[0] = tmp[3][0];
		rightFrames[1] = tmp[3][1];
		rightFrames[2] = tmp[3][2];
		
		upAnimation = new Animation(0.30f, upFrames);
		upAnimation.setPlayMode(Animation.LOOP_PINGPONG);
		downAnimation = new Animation(0.30f, downFrames);
		downAnimation.setPlayMode(Animation.LOOP_PINGPONG);
		leftAnimation = new Animation(0.30f, leftFrames);
		leftAnimation.setPlayMode(Animation.LOOP_PINGPONG);
		rightAnimation = new Animation(0.30f, rightFrames);
		rightAnimation.setPlayMode(Animation.LOOP_PINGPONG);
		
		stateTime = 0;	
	}
	
	public void render(SpriteBatch batch) {
		update();
		
		batch.draw(currentFrame, x, y);
	}
	
	public void update() {
		stateTime += Gdx.graphics.getDeltaTime(); 
		Random random = new Random();
		
		//TODO movement
		nextStateCounter += stateTime;
		if( nextStateCounter > nextState ) {
			
			switch( random.nextInt(2) ){
				case 0: state = State.wait; break;
				case 1: state = State.walking; break;
			}
			
			if( random.nextFloat() > 0.8f ) {
				switch( random.nextInt(4) ) {
					case 0: dir = Direction.up; break;
					case 1: dir = Direction.down; break;
					case 2: dir = Direction.left; break;
					case 3: dir = Direction.right; break;
				}
			}	
			
			nextStateCounter = 0;
			nextState = random.nextInt(1000) + 500;
		}		
		
		if( state == State.walking ) {
			switch( dir ) {
				case up: y += checkCollision(x, y + speed) ? speed : 0.0f; break;
				case down: y -= checkCollision(x, y - speed) ? speed : 0.0f; break;
				case left: x -= checkCollision(x - speed, y) ? speed : 0.0f; break;
				case right: x += checkCollision(x + speed, y) ? speed : 0.0f; break;
			}
		}	
		
		Animation currentAnimation = upAnimation; //default
		
	
	
		if( state == State.wait ) {
			switch(dir) {
				case up: currentFrame = upFrames[1]; break;
				case down: currentFrame = downFrames[1]; break;
				case left: currentFrame = leftFrames[1]; break;
				case right: currentFrame= rightFrames[1]; break;
			}
		}
		else
		{
			switch(dir) {
				case up: currentAnimation = upAnimation; break;
				case down: currentAnimation = downAnimation; break;
				case left: currentAnimation = leftAnimation; break;
				case right: currentAnimation= rightAnimation; break;
			}
			
			currentFrame = currentAnimation.getKeyFrame(stateTime, true);	
		}	
		
		
	}
	
	public boolean checkCollision(float x, float y) {
		float x_min = 50;
		float x_max = 260;
		float y_min = 90;
		float y_max = 130;
		
		boolean collision = false;
		
		if( x <= x_min ) {
			collision = true;
			
			dir = Direction.right;
		}
		
		if( x >= x_max ) {
			collision = true;

			dir = Direction.left;
		}
		
		if( y <= y_min ) {
			collision = true;

			dir = Direction.up;
		}
		
		if( y >= y_max ) {
			collision = true;

			dir = Direction.down;
		}
		
		return !collision;
	}
	
}
