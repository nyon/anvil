package com.ggf.anvil.screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.forever;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.removeActor;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.rotateTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.ggf.anvil.Anvil;
import com.ggf.anvil.elements.CharAnimation;
import com.ggf.anvil.elements.Item;
import com.ggf.anvil.services.Merchant;
import com.ggf.anvil.services.MusicManager;
import com.ggf.anvil.services.Profile;
import com.ggf.anvil.services.ProfileManager;
import com.ggf.anvil.services.SoundManager;
import com.ggf.anvil.services.SoundManager.AnvilSound;

public class SmitheryScreen extends AbstractScreen
{
  public Image          bgndImage;
  private CharAnimation smith = new CharAnimation();
  private Image         daycycle;
  private Label         daytime;
  private Label         nextRentLabel;
  private Texture       day_empty;
  private Texture       day_full;

  public SmitheryScreen(final Anvil game)
  {
    super(game);
    MusicManager.stop();
  }

  @Override
  public void render(float delta)
  {
    stage.act(delta);
    Profile profile = ProfileManager.getInstance().retrieveProfile();
    daycycle.addAction(sequence(rotateTo(-180.0f * profile.hour / 10.0f, 1.0f)));
    daytime.setText("" + profile.day);
    daytime.setPosition(160 - font.getBounds(daytime.getText()).width / 2, 5);
    while (profile.hour >= 10)
    {
      profile.hour -= 10;
      profile.day++;
      if (profile.day >= 7)
      {
        game.grantAchievement("week_7");
      }
      profile.getPlayer().lastGold = profile.getPlayer().gold;

      profile.merchant = Merchant.randomMerchant(profile.day);

      int nextRent = 160 * (profile.day + 1);
      int rent = 160 * profile.day;
      String rentMessage = "-" + rent + "$";
      nextRentLabel.setText("next rent: -" + nextRent + "$");
      nextRentLabel.setPosition(320 - font.getBounds(nextRentLabel.getText()).width - 8, 8);
      profile.getPlayer().gold -= rent;
      if (profile.getPlayer().gold < 0)
      {
        game.setScreen(new GameOverScreen(game));
      }

      messageBox("Day " + (profile.day) + " ... Paid rent: " + rent + "$");

      Label info = new Label(rentMessage, new Label.LabelStyle(font, Color.RED));
      info.setPosition(160 - font.getBounds(rentMessage).width / 2, 0);
      info.addAction(sequence(parallel(moveTo(info.getX(), info.getY() + 20, 4.0f), fadeOut(4.0f)), removeActor()));
      info.setTouchable(Touchable.disabled);
      fg.addActor(info);
    }

    stage.draw();

    SpriteBatch batch = this.stage.getSpriteBatch();
    batch.begin();

    for (int i = 0; i < 10; i++)
    {
      batch.draw(i < profile.hour ? day_full : day_empty, i * 25 + (i > 4 ? 70 : 0), 0);

    }

    batch.end();
  }

  @Override
  public void show()
  {
    // retrieve the splash image's region from the atlas
    SoundManager.playLooped(AnvilSound.FIRE);

    // here we create the splash image actor; its size is set when the
    // resize() method gets called
    bgndImage = new Image(new Texture(Gdx.files.internal("data/smithery.png")));
    bgndImage.setFillParent(true);

    // and finally we add the actor to the stage
    bg.addActor(bgndImage);

    day_empty = new Texture(Gdx.files.internal("data/day-empty.png"));
    day_full = new Texture(Gdx.files.internal("data/day-full.png"));

    final Image anvil = new Image(new Texture(Gdx.files.internal("data/anvil.png")));
    anvil.setPosition(141, 62);
    anvil.setOrigin(anvil.getWidth() / 2, anvil.getHeight() / 2);
    anvil.addListener(new InputListener() {
      Label label = null;
      {
        label = new Label("Anvil", new Label.LabelStyle(font, Color.WHITE));
        SmitheryScreen.this.fg.addActor(label);
        label.setVisible(false);
      }

      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
      {
        // game.setScreen(new ForgingScreen(game));
        game.setScreen(new BlueprintScreen(game));
        SoundManager.play(AnvilSound.CLICK);
        return true;
      }

      @Override
      public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor)
      {
        anvil.addAction(forever(sequence(scaleTo(1.5f, 1.5f, 0.6f), scaleTo(1.0f, 1.0f, 0.6f))));
        label.setPosition(141, 82);
        label.setVisible(true);

      }

      @Override
      public void exit(InputEvent event, float x, float y, int pointer, Actor toActor)
      {
        anvil.clearActions();
        anvil.setScale(1.0f);
        label.setVisible(false);

      }
    });
    fg.addActor(anvil);

    final Image chest = new Image(new Texture(Gdx.files.internal("data/chest.png")));
    chest.setPosition(16, 96);
    chest.setOrigin(chest.getWidth() / 2, chest.getHeight() / 2);
    chest.addListener(new InputListener() {

      Label label = null;

      {
        label = new Label("Inventory", new Label.LabelStyle(font, Color.WHITE));
        SmitheryScreen.this.fg.addActor(label);
        label.setVisible(false);
      }

      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
      {
        game.setScreen(new InventoryScreen(game));
        SoundManager.play(AnvilSound.CLICK);
        return true;
      }

      @Override
      public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor)
      {
        chest.addAction(forever(sequence(scaleTo(1.5f, 1.5f, 0.6f), scaleTo(1.0f, 1.0f, 0.6f))));
        label.setPosition(16, 146);
        label.setVisible(true);

      }

      @Override
      public void exit(InputEvent event, float x, float y, int pointer, Actor toActor)
      {
        chest.clearActions();
        chest.setScale(1.0f);
        label.setVisible(false);

      }
    });
    fg.addActor(chest);

    final Image table = new Image(new Texture(Gdx.files.internal("data/table.png")));
    table.setPosition(152, 152);
    table.setOrigin(table.getWidth() / 2, table.getHeight() / 2);
    table.addListener(new InputListener() {

      Label label = null;

      {
        label = new Label("Blueprint", new Label.LabelStyle(font, Color.WHITE));
        SmitheryScreen.this.fg.addActor(label);

        label.setVisible(false);
      }

      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
      {
        game.setScreen(new BlueprintScreen(game));
        SoundManager.play(AnvilSound.CLICK);
        return true;
      }

      @Override
      public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor)
      {
        table.addAction(forever(sequence(scaleTo(1.5f, 1.5f, 0.6f), scaleTo(1.0f, 1.0f, 0.6f))));
        label.setPosition(152, 192);
        label.setVisible(true);

      }

      @Override
      public void exit(InputEvent event, float x, float y, int pointer, Actor toActor)
      {
        table.clearActions();
        table.setScale(1.0f);
        label.setVisible(false);

      }
    });
    fg.addActor(table);

    final Image grinding_stone = new Image(new Texture(Gdx.files.internal("data/grinding_stone.png")));
    grinding_stone.setPosition(56, 36);
    grinding_stone.setOrigin(grinding_stone.getWidth() / 2, grinding_stone.getHeight() / 2);
    grinding_stone.addListener(new InputListener() {

      Label label = null;

      {
        label = new Label("Grinding stone", new Label.LabelStyle(font, Color.WHITE));
        SmitheryScreen.this.fg.addActor(label);
        label.setVisible(false);
      }

      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
      {
        // game.setScreen(new InventoryScreen(game));

        Profile p = ProfileManager.getInstance().retrieveProfile();
        String grindMessage = "*grinds hand*";
        for (Item i : p.getPlayer().inventory)
        {
          if (i.type == Item.Type.WEAPON)
          {
            grindMessage = "*grinds weapons*";
            i.price *= 1.01f;
          }
        }
        Label info = new Label(grindMessage, new Label.LabelStyle(font, Color.WHITE));
        info.setPosition(70 - font.getBounds(grindMessage).width / 2, 50);
        info.addAction(sequence(parallel(moveTo(info.getX(), info.getY() + 20, 4.0f), fadeOut(4.0f)), removeActor()));
        info.setTouchable(Touchable.disabled);
        fg.addActor(info);

        p.hour++;
        SoundManager.play(AnvilSound.CLICK);
        return true;
      }

      @Override
      public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor)
      {
        grinding_stone.addAction(forever(sequence(scaleTo(1.5f, 1.5f, 0.6f), scaleTo(1.0f, 1.0f, 0.6f))));
        label.setPosition(26, 86);
        label.setVisible(true);

      }

      @Override
      public void exit(InputEvent event, float x, float y, int pointer, Actor toActor)
      {
        grinding_stone.clearActions();
        grinding_stone.setScale(1.0f);
        label.setVisible(false);

      }
    });
    fg.addActor(grinding_stone);

    final Image forge = new Image(new Texture(Gdx.files.internal("data/forge.png")));
    forge.setPosition(256, 148);
    forge.setOrigin(forge.getWidth() / 2, forge.getHeight() / 3);
    forge.addListener(new InputListener() {

      Label label = null;

      {
        label = new Label("Forge", new Label.LabelStyle(font, Color.WHITE));
        SmitheryScreen.this.fg.addActor(label);
        label.setVisible(false);
      }

      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
      {
        game.setScreen(new SmeltingScreen(game));
        SoundManager.play(AnvilSound.CLICK);
        return true;
      }

      @Override
      public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor)
      {
        forge.addAction(forever(sequence(scaleTo(1.5f, 1.5f, 0.6f), scaleTo(1.0f, 1.0f, 0.6f))));
        label.setPosition(206, 168);
        label.setVisible(true);

      }

      @Override
      public void exit(InputEvent event, float x, float y, int pointer, Actor toActor)
      {
        forge.clearActions();
        forge.setScale(1.0f);
        label.setVisible(false);

      }
    });
    fg.addActor(forge);

    final Image door = new Image(new Texture(Gdx.files.internal("data/door.png")));
    door.setPosition(36, 171);
    door.setOrigin(door.getWidth() / 2, 0);
    door.addListener(new InputListener() {

      Label label = null;

      {
        label = new Label("Marketplace", new Label.LabelStyle(font, Color.WHITE));
        SmitheryScreen.this.fg.addActor(label);
        label.setVisible(false);
      }

      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
      {
        game.setScreen(new MarketplaceScreen(game));
        SoundManager.play(AnvilSound.CLICK);
        return true;
      }

      @Override
      public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor)
      {
        door.addAction(forever(sequence(scaleTo(1.5f, 1.0f, 0.6f), scaleTo(1.0f, 1.0f, 0.6f))));
        label.setPosition(66, 168);
        label.setVisible(true);

      }

      @Override
      public void exit(InputEvent event, float x, float y, int pointer, Actor toActor)
      {
        door.clearActions();
        door.setScale(1.0f);
        label.setVisible(false);

      }
    });
    fg.addActor(door);

    fg.addActor(new Actor() {
      @Override
      public void act(float delta)
      {
        // TODO Auto-generated method stub
        super.act(delta);

      }

      @Override
      public void draw(SpriteBatch batch, float parentAlpha)
      {
        // TODO Auto-generated method stub
        super.draw(batch, parentAlpha);

        batch.setColor(1, 1, 1, 1);

        smith.render(batch);
      }
    });

    // EXIT
    Image exit = new Image(new Texture(Gdx.files.internal("data/button_exit.png")));
    exit.setPosition(320 - exit.getWidth() - 1, 240 - exit.getHeight() - 1);
    exit.addListener(new InputListener() {
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
      {
        SmitheryScreen.this.game.setScreen(new MainMenuScreen(SmitheryScreen.this.game));
        SoundManager.play(AnvilSound.CLICK);
        SoundManager.setEnabled(false);

        MusicManager.stop();
        return true;
      }
    });
    fg.addActor(exit);

    daycycle = new Image(new Texture(Gdx.files.internal("data/daycycle.png")));
    daycycle.setPosition(160 - daycycle.getWidth() / 2, -daycycle.getHeight() / 2);
    // daycycle.setOrigin(daycycle.getWidth()/2, daycycle.getHeight()/2);
    daycycle.setOrigin(daycycle.getWidth() / 2, daycycle.getHeight() / 2);
    // daycycle.setVisible(true);
    fg.addActor(daycycle);

    daytime = new Label("", new Label.LabelStyle(font, Color.BLACK));
    daytime.setPosition(160, 0);
    daytime.setTouchable(Touchable.disabled);
    fg.addActor(daytime);

    int nextDay = ProfileManager.getInstance().retrieveProfile().day + 1;
    nextRentLabel = new Label("next rent: -" + (nextDay * 160) + "$", new Label.LabelStyle(font, Color.RED));
    nextRentLabel.setPosition(320 - font.getBounds(nextRentLabel.getText()).width - 8, 8);
    nextRentLabel.setTouchable(Touchable.disabled);
    fg.addActor(nextRentLabel);

  }

}
