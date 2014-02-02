package com.ggf.anvil.services;

import com.badlogic.gdx.graphics.Texture;

public class Achievement
{
  public String  name;
  public Texture texture;

  public Achievement(String name, Texture texture)
  {
    this.name = name;
    this.texture = texture;
  }
}
