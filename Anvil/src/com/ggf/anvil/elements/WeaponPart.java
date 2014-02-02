package com.ggf.anvil.elements;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class WeaponPart extends Item
{
  public float anvilFade;

  public WeaponPart(Texture onAnvilTexture, Texture inInventoryTexture)
  {
    super(onAnvilTexture, inInventoryTexture);
  }

  @Override
  public void draw(SpriteBatch batch, float parentAlpha)
  {
    Texture texture2 = textureMap.get(State.ON_ANVIL2);
    if (this.state == State.ON_ANVIL && texture2 != null)
    {
      Texture texture1 = textureMap.get(State.ON_ANVIL);
      batch.setColor(1, 1, 1, anvilFade);
      batch.draw(texture2, getX(), getY(), 0, 0, (int) getWidth(), (int) getHeight());
      batch.setColor(1, 1, 1, 1.0f - anvilFade);
      batch.draw(texture1, getX(), getY(), 0, 0, (int) getWidth(), (int) getHeight());

    }
    else
    {
      super.draw(batch, parentAlpha);
    }

  }

}
