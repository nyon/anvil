package com.ggf.anvil.elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Slot extends Actor
{

  Texture      slotTexture;
  private Item itemInSlot;

  public Slot()
  {
    this.slotTexture = new Texture(Gdx.files.internal("data/inventory_panel.png"));
    this.setSize(32, 32);
  }

  @Override
  public void act(float delta)
  {
    super.act(delta);
  }

  public void setItem(Item newItem)
  {
    itemInSlot = newItem;
    Vector2 globalPos = localToStageCoordinates(new Vector2(0, 0));
    itemInSlot.setPosition(globalPos.x, globalPos.y);
  }

  public boolean hasItem()
  {
    return itemInSlot != null;
  }

  public Item getItem()
  {
    return itemInSlot;
  }

  public void removeItem()
  {
    itemInSlot = null;
  }

  @Override
  public void draw(SpriteBatch batch, float parentAlpha)
  {
    batch.draw(slotTexture, getX(), getY(), 0, 0, (int) getWidth(), (int) getHeight());

    super.draw(batch, parentAlpha);
  }

}
