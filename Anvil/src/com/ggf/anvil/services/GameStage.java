package com.ggf.anvil.services;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class GameStage extends Stage
{

  public GameStage(float width, float height, boolean stretch)
  {
    super();
    this.setCamera(new OrthographicCamera(320, 240));
    this.getCamera().position.set(320 / 2, 240 / 2, 0f);
  }

}
