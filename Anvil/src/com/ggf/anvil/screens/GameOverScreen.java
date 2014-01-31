package com.ggf.anvil.screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.ggf.anvil.Anvil;
import com.ggf.anvil.services.ProfileManager;

public class GameOverScreen extends AbstractScreen
{
  Image game_over;

  public GameOverScreen(Anvil game)
  {
    super(game);

    messageBox("No money left, you starved to death.");

    game_over = new Image(new Texture(Gdx.files.internal("data/game_over.png")));
    game_over.setFillParent(true);
    game_over.getColor().a = 0f;
    bg.addActor(game_over);

    game_over.addAction(sequence(fadeIn(0.75f), delay(1.5f), fadeOut(0.75f), new Action() {
      @Override
      public boolean act(float delta)
      {
        ProfileManager.getInstance().retrieveProfile().reset();
        GameOverScreen.this.game.setScreen(new MainMenuScreen(GameOverScreen.this.game));
        return true;
      }
    }));
  }
}
