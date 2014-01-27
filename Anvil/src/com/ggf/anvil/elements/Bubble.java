package com.ggf.anvil.elements;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Bubble extends Image {
	public Bubble(Texture texture) {
		super(texture);
	}

	@Override
	public Actor hit(float x, float y, boolean touchable) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void act(float delta) {
		if(this.getColor().a < 0.02f) this.remove();
		setY(getY()+50*delta);
		this.setOrigin(64, 64);
		this.setScale(this.getScaleX()*(1.0f + delta));
		this.setColor(1, 1, 1, (float) (this.getColor().a - delta*1.5));
		super.act(delta);
	}
}
