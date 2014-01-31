package com.ggf.anvil.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ggf.anvil.Anvil;
import com.ggf.anvil.services.MusicManager.AnvilMusic;
import com.ggf.anvil.services.ProfileManager;

public class IntroScreen extends AbstractScreen
{

  private Texture frames[];
  private float   frame;
  private boolean videoStarted = false;

  public IntroScreen(Anvil game)
  {
    super(game);
    frames = new Texture[602];
    for (int i = 0; i < 602; i++)
    {
      frames[i] = new Texture(Gdx.files.internal("data/movie/movie (" + (i + 2) + ")_t.png"));
    }
    frame = 0.0f;
  }

  @Override
  public void render(float delta)
  {
    // play intro Video
    if (!videoStarted)
    {
      delta = 0.8f;
      videoStarted = true;
    }
    frame += delta * 25;
    stage.act(delta);
    stage.draw();
    SpriteBatch batch = stage.getSpriteBatch();
    batch.begin();
    try
    {
      batch.draw(frames[(int) Math.floor(frame)], 0, 0, 320, 240);
    }
    catch (Exception e)
    {
      ProfileManager.getInstance().retrieveProfile().getPlayer().newbie = false;
      game.setScreen(new SplashScreen(game));
    }
    batch.end();

  }

  @Override
  public void resize(int width, int height)
  {
    // TODO Auto-generated method stub

  }

  @Override
  public void show()
  {
    game.getMusicManager().play(AnvilMusic.INTRO);
  }

  @Override
  public void hide()
  {
    // TODO Auto-generated method stub

  }

  @Override
  public void pause()
  {
    // TODO Auto-generated method stub

  }

  @Override
  public void resume()
  {
    // TODO Auto-generated method stub

  }

  @Override
  public void dispose()
  {

  }

}
