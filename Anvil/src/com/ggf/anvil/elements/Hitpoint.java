package com.ggf.anvil.elements;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.ggf.anvil.services.SoundManager;
import com.ggf.anvil.services.SoundManager.AnvilSound;

public class Hitpoint extends Actor {
	Texture[] hitpoint;

	private final int HITPOINTS = 6;
	public WeaponPart wp;
	public float quality;
	public int maxHealth;
	public int health;
	
	private float time;
	private float timeFactor;
	
	private int keycode;
	private final SoundManager soundman = SoundManager.getInstance();

	public Hitpoint() {
		super();
		// TODO Auto-generated constructor stub
		setWidth(32);
		setHeight(32);
		maxHealth = health = ((new Random()).nextInt(3) + 1);
		
		hitpoint = new Texture[HITPOINTS];
		reset();
		for (int i = 1; i <= HITPOINTS; i++)
			hitpoint[i - 1] = new Texture(Gdx.files.internal("data/hitpoint"
					+ i + ".png"));
		
		
		addListener(new InputListener() {		
			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				// TODO Auto-generated method stub
				System.out.println(keycode);
				return true;
			}
			
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				hit();
				
				return true;
			}
		});
		
	}

	public void reset() {
		time = (float) Math.random() * 100.0f;
		timeFactor = (float) Math.random() + 2.0f;
		quality = 1.0f;
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		// TODO Auto-generated method stub
		batch.setColor(getColor());
		batch.draw(hitpoint[Math.min(HITPOINTS-1,(int) Math.floor((float) (0.5f * (Math.sin(time) + 1.0f)) * (HITPOINTS)))],
				getX(), getY());
		super.draw(batch, parentAlpha);
	}

	@Override
	public void act(float delta) {
		// TODO Auto-generated method stub
		time += delta * timeFactor;
		
		super.act(delta);
	}
	
	public int getKeyCode() { return keycode; }
	public void setKeyCode(int keycode) { this.keycode = keycode; System.out.println(this.keycode); }
	
	public void hit() {
		if (health <= 0) return;
		float generalQuality = (float) (0.5f * (Math.sin(time) + 1.0f));
		quality = (quality + generalQuality) / 2.0f;
		Hitpoint.this.health--;
		Hitpoint.this.setColor(1, 1, 1, Hitpoint.this.health
				/ (float) Hitpoint.this.maxHealth);
		if (generalQuality > 0.5f) {
			Bubble b = new Bubble(new Texture(Gdx.files.internal("data/bubble"
					+ ((new Random()).nextInt(3) + 1) + ".png")));
			Vector2 v = new Vector2(Hitpoint.this.getX(), Hitpoint.this.getY());
			// Hitpoint.this.localToStageCoordinates(v);
			b.setPosition(v.x - 32, v.y - 32);
			Hitpoint.this.getStage().addActor(b);
		}
		if (Hitpoint.this.health <= 0) {

			Hitpoint.this.getParent().removeActor(Hitpoint.this);
			Hitpoint.this.setVisible(false);

		}
		int soundind = (new Random()).nextInt(4);
		switch (soundind) {
		case 0:
			soundman.play(AnvilSound.DENGEL1);
			break;
		case 1:
			soundman.play(AnvilSound.DENGEL2);
			break;
		case 2:
			soundman.play(AnvilSound.DENGEL3);
			break;
		case 3:
			soundman.play(AnvilSound.DENGEL4);
			break;
		}
		
		float ox = Hitpoint.this.getX();
		float oy = Hitpoint.this.getY();
		
		Hitpoint.this.addAction(sequence(moveTo(ox,oy-7.0f,0.05f),moveTo(ox,oy,1.0f)));
		
	}

}
