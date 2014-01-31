package com.ggf.anvil.screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.ggf.anvil.Anvil;
import com.ggf.anvil.elements.Hitpoint;
import com.ggf.anvil.elements.Item;
import com.ggf.anvil.elements.Item.ItemStates;
import com.ggf.anvil.elements.WeaponPart;
import com.ggf.anvil.elements.WeaponPrototype;
import com.ggf.anvil.services.MusicManager.AnvilMusic;
import com.ggf.anvil.services.Player;
import com.ggf.anvil.services.ProfileManager;
import com.ggf.anvil.services.SoundManager;
import com.ggf.anvil.services.SoundManager.AnvilSound;
import com.ggf.anvil.services.WordGenerator;

public class ForgingScreen extends AbstractScreen
{
  private Image               bgndImage;
  private WordGenerator       wordGen;
  private WeaponPrototype     weapon;
  private float               degrees;
  private boolean             forgingDone;
  private boolean             checkDone;
  private boolean             exitPrepared;
  private float               calculatedQuality;
  private float               counterQuality;
  private Texture             temperature;
  private Item                grip;
  private Item                crossguard;
  private Item                blade;

  private final static int    MAX_DEGREES = 3141;
  SoundManager                soundMan;

  private ArrayList<Hitpoint> hps         = new ArrayList<Hitpoint>();

  public ForgingScreen(Anvil game, Item grip, Item crossguard, Item blade)
  {
    super(game);

    this.grip = grip;
    this.crossguard = crossguard;
    this.blade = blade;

    grip.clearListeners();
    crossguard.clearListeners();
    blade.clearListeners();

    degrees = MAX_DEGREES;

    font = new BitmapFont(Gdx.files.internal("skin/default.fnt"), false);
    soundMan = SoundManager.getInstance();
    wordGen = new WordGenerator();

  }

  @Override
  public void render(float delta)
  {

    if (!forgingDone)
    {
      boolean done = true;
      calculatedQuality = 1.0f;
      for (Hitpoint hp : hps)
      {
        done = done && hp.health == 0;
        calculatedQuality *= hp.quality;
      }
      if (degrees <= 500)
      {
        calculatedQuality = 0.0f;
        done = true;
      }

      if (done)
      {
        forgingDone = true;
        Image check;
        if (calculatedQuality < 0.05f)
        {
          check = new Image(new Texture(Gdx.files.internal("data/lose.png")));
          soundMan.play(AnvilSound.WEAPONFAIL);

        }
        else
        {
          check = new Image(new Texture(Gdx.files.internal("data/win.png")));
          soundMan.play(AnvilSound.WEAPONSUCCESS);

          weapon.quality = calculatedQuality;
          // TODO: Berechnen aus zerstörten Hitpoints
          weapon.craftsmanship = 0.8f;
          weapon.sharpness = 0.3f;
          weapon.name = wordGen.getWord(blade.name, weapon.quality, weapon.craftsmanship);
          Gdx.app.log(Anvil.LOG, "Weapon: Quality: " + weapon.quality + " Craftsmanship: " + weapon.craftsmanship + " Sharpness: " + weapon.sharpness + " Effects: " + weapon.effects.toString());

        }

        check.setOrigin(32, 32);
        check.setPosition(160 - 32, 120 - 32);
        check.setScale(0.1f);
        check.getColor().a = 0f;
        check.addAction(sequence(parallel(fadeIn(1.0f), scaleTo(1.0f, 1.0f, 1.0f)), parallel(fadeOut(1.0f), scaleTo(3.0f, 3.0f, 1.0f)), new Action() {
          @Override
          public boolean act(float delta)
          {
            // the last action will move to the next screen
            checkDone = true;

            return true;
          }
        }));
        fg.addActor(check);
      }

    }

    stage.act(delta);
    stage.draw();

    SpriteBatch batch = stage.getSpriteBatch();
    batch.begin();
    if (!forgingDone)
    {
      float fader = (MAX_DEGREES - degrees) / (float) MAX_DEGREES;
      for (WeaponPart wp : weapon.parts)
      {
        wp.anvilFade = 1.0f - fader;
      }
      batch.setColor(Color.WHITE);
      batch.draw(temperature, 28, 225 - 64, 19, (int) (64 * (1.0f - fader)), 0, 0, 1.0f, (int) (64 * (1.0f - fader)) / 64.0f);
    }

    if (forgingDone && checkDone && counterQuality <= calculatedQuality)
    {
      counterQuality += delta;
      counterQuality = Math.min(calculatedQuality, counterQuality);

      if (counterQuality >= 0.5f)
      {
        game.grantAchievement("quality_50");
      }
      if (counterQuality >= 0.6f)
      {
        game.grantAchievement("quality_60");
      }
      if (counterQuality >= 0.75f)
      {
        game.grantAchievement("quality_75");
      }
      if (counterQuality >= 0.90f)
      {
        game.grantAchievement("quality_90");
      }
      if (counterQuality >= 0.95f)
      {
        game.grantAchievement("quality_95");
      }

      if (counterQuality == calculatedQuality && !exitPrepared)
      {
        exitPrepared = true;
        stage.addAction(sequence(delay(3.0f), new Action() {
          @Override
          public boolean act(float delta)
          {
            resetItemStates();
            Player p = ProfileManager.getInstance().retrieveProfile().getPlayer();
            if (calculatedQuality >= 0.05f)
            {
              Item new_weapon = weapon.generateItem();
              new_weapon.calculatePrice();
              grip.remove();
              blade.remove();
              crossguard.remove();

              p.inventory.add(new_weapon);
              p.inventory.remove(grip);
              p.inventory.remove(blade);
              p.inventory.remove(crossguard);
              ProfileManager.getInstance().retrieveProfile().hour++;

              game.setScreen(new WeaponStatsScreen(game, new_weapon));
              ProfileManager.getInstance().retrieveProfile().incWeaponsCrafted();

            }
            else
            {
              grip.remove();
              blade.remove();
              crossguard.remove();
              p.inventory.remove(grip);
              p.inventory.remove(blade);
              p.inventory.remove(crossguard);
              ProfileManager.getInstance().retrieveProfile().hour++;
              game.setScreen(new SmitheryScreen(game));
            }

            return true;
          }
        }));
      }

      String text = "" + (int) (counterQuality * 100) + " % Quality";
      font.draw(batch, text, 160 - font.getBounds(text).width / 2, 140);

    }

    batch.end();

    degrees -= delta * 100;
    if (degrees < 500) degrees = 500;
  }

  private void resetItemStates()
  {
    if (grip != null) grip.setState(ItemStates.IN_INVENTORY);
    if (crossguard != null) crossguard.setState(ItemStates.IN_INVENTORY);
    if (blade != null) blade.setState(ItemStates.IN_INVENTORY);
  }

  @Override
  public void show()
  {
    super.show();

    // start playing the menu music
    game.getMusicManager().play(AnvilMusic.FORGING);

    // here we create the splash image actor; its size is set when the
    // resize() method gets called
    bgndImage = new Image(new Texture(Gdx.files.internal("data/forgingBgnd.png")));
    bgndImage.setFillParent(true);

    // and finally we add the actor to the stage
    bg.addActor(bgndImage);

    temperature = new Texture(Gdx.files.internal("data/temperature.png"));

    weapon = new WeaponPrototype(grip, crossguard, blade);
    fg.addActor(weapon);

    float width = blade.getWidth() / 7.0f;

    Hitpoint hp = new Hitpoint();
    hp.setKeyCode(Keys.W);
    hp.setX(blade.getX() - 20 + width);
    hp.setY(15 + blade.getY() + blade.getHeight() / 2 - 16);

    hps.add(hp);
    fg.addActor(hp);

    hp = new Hitpoint();
    hp.setKeyCode(Keys.A);
    hp.setX(blade.getX() - 20 + width * 3);
    hp.setY(15 + blade.getY() + blade.getHeight() / 2 - 16);
    hps.add(hp);
    fg.addActor(hp);

    hp = new Hitpoint();
    hp.setKeyCode(Keys.S);
    hp.setX(blade.getX() - 20 + width * 5);
    hp.setY(15 + blade.getY() + blade.getHeight() / 2 - 16);
    hps.add(hp);
    fg.addActor(hp);

    hp = new Hitpoint();
    hp.setKeyCode(Keys.D);
    hp.setX(blade.getX() - 20 + width * 2);
    hp.setY(-15 + blade.getY() + blade.getHeight() / 2 - 16);
    hps.add(hp);
    fg.addActor(hp);

    hp = new Hitpoint();
    hp.setKeyCode(Keys.F);
    hp.setX(blade.getX() - 20 + width * 4);
    hp.setY(-15 + blade.getY() + blade.getHeight() / 2 - 16);
    hps.add(hp);
    fg.addActor(hp);

    hp = new Hitpoint();
    hp.setKeyCode(Keys.G);
    hp.setX(blade.getX() - 20 + width * 6);
    hp.setY(-15 + blade.getY() + blade.getHeight() / 2 - 16);
    hps.add(hp);
    fg.addActor(hp);

    Image exit = new Image(new Texture(Gdx.files.internal("data/button_exit.png")));
    exit.setPosition(320 - exit.getWidth() - 1, 240 - exit.getHeight() - 1);
    exit.addListener(new InputListener() {
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
      {
        game.getMusicManager().stop();
        resetItemStates();
        ProfileManager.getInstance().retrieveProfile().hour++;
        game.setScreen(new SmitheryScreen(game));
        return true;
      }
    });
    fg.addActor(exit);

    Image help = new Image(new Texture(Gdx.files.internal("data/cursor.png")));

    help.setPosition(160 - help.getWidth() / 2, 240 - help.getHeight() / 2);
    help.setOrigin(0, help.getHeight());
    help.addAction(sequence(moveTo(hps.get(0).getX() + 10, hps.get(0).getY() - 2, 1.0f), scaleTo(0.5f, 0.5f, 0.25f), scaleTo(1.0f, 1.0f, 0.25f), fadeOut(0.2f)));
    help.setTouchable(Touchable.disabled);
    fg.addActor(help);
  }

  @Override
  public void hide()
  {
    game.getMusicManager().stop();

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
    game.getMusicManager().stop();

  }

  public void key(int keyCode)
  {
    for (Hitpoint hp : hps)
    {
      if (hp == null) continue;
      if (hp.getKeyCode() == keyCode)
      {
        hp.hit();
      }
    }
  }

}
