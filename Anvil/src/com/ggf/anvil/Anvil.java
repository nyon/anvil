package com.ggf.anvil;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.removeActor;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.ggf.anvil.screens.AbstractScreen;
import com.ggf.anvil.screens.MainMenuScreen;
import com.ggf.anvil.screens.SplashScreen;
import com.ggf.anvil.services.Achievement;
import com.ggf.anvil.services.AchievementManager;
import com.ggf.anvil.services.MusicManager;
import com.ggf.anvil.services.Player;
import com.ggf.anvil.services.PreferencesManager;
import com.ggf.anvil.services.ProfileManager;
import com.ggf.anvil.services.SoundManager;
import com.ggf.anvil.services.SoundManager.AnvilSound;
import com.ggf.anvil.services.WordGenerator;

public class Anvil extends Game
{

  // constant useful for logging
  public static final String  LOG      = Anvil.class.getSimpleName();

  // whether we are in development mode
  public static final boolean DEV_MODE = false;

  // a libgdx helper class that logs the current FPS each second
  private FPSLogger           fpsLogger;

  // services
  private PreferencesManager  preferencesManager;
  private ProfileManager      profileManager;
  private WordGenerator       wordGen;

  public static Anvil         instance;

  @Override
  public void create()
  {
    instance = this;
    // TODO: remove below
    Texture.setEnforcePotImages(false);

    Gdx.app.log(Anvil.LOG, "Creating game on " + Gdx.app.getType());

    MusicManager.init();
    SoundManager.init();
    
    // create the preferences manager
    preferencesManager = new PreferencesManager();

    // create the music manager
    MusicManager.setVolume(preferencesManager.getVolume());
    MusicManager.setEnabled(preferencesManager.isMusicEnabled());
    SoundManager.setEnabled(preferencesManager.isSoundEnabled());
    System.out.println("Music is " + (preferencesManager.isMusicEnabled() ? "on." : "off"));
    System.out.println("Sound is " + (preferencesManager.isSoundEnabled() ? "on." : "off"));

    // create the profile manager
    // profileManager = new ProfileManager();
    // profileManager.retrieveProfile();

    // create the helper objects
    fpsLogger = new FPSLogger();

    wordGen = new WordGenerator();

    // introScreen = new IntroScreen(this);
    // this.setScreen(introScreen);
  }

  @Override
  public void dispose()
  {
    super.dispose();
    Gdx.app.log(Anvil.LOG, "Disposing game");

    // dipose some services
    ProfileManager.getInstance().persist();
    
    // dispose manager
    MusicManager.dispose();
    SoundManager.dispose();
  }

  @Override
  public void render()
  {
    super.render();

    // output the current FPS
    if (DEV_MODE) fpsLogger.log();
  }

  @Override
  public void resize(int width, int height)
  {
    super.resize(width, height);
    Gdx.app.log(Anvil.LOG, "Resizing game to: " + width + " x " + height);
    System.out.println(wordGen.getWord("Sword", 0.7f, 0.1f));

    // show the splash screen when the game is resized for the first time;
    // this approach avoids calling the screen's resize method repeatedly
    if (getScreen() == null)
    {
      boolean nervigeSplashscreens = false;
      if (nervigeSplashscreens)
      {
        setScreen(new SplashScreen(this));
      }
      else
      {
        setScreen(new MainMenuScreen(this));
      }
    }
  }

  @Override
  public void pause()
  {
    super.pause();
    Gdx.app.log(Anvil.LOG, "Pausing game");

    // persist the profile, because we don't know if the player will come
    // back to the game
    // profileManager.persist();
  }

  @Override
  public void resume()
  {
    super.resume();
    Gdx.app.log(Anvil.LOG, "Resuming game");
  }

  @Override
  public void setScreen(Screen screen)
  {
    super.setScreen(screen);
    Gdx.app.log(Anvil.LOG, "Setting screen: " + screen.getClass().getSimpleName());
  }

  /**
   * @return the preferencesManager
   */
  public PreferencesManager getPreferencesManager()
  {
    return preferencesManager;
  }

  /**
   * @return the profileManager
   */
  public ProfileManager getProfileManager()
  {
    return profileManager;
  }
  
  public void grantAchievement(String name)
  {
    Achievement a = AchievementManager.getInstance().getAchievement(name);
    if (a == null) return;
    Player player = ProfileManager.getInstance().retrieveProfile().getPlayer();
    if (player.achievements.contains(name)) return;
    player.grantAchievement(name);

    Image check = new Image(new Texture(Gdx.files.internal("data/win.png")));
    check.setOrigin(32, 32);
    check.setPosition(160 - 32, 120 - 32);
    check.setScale(0.1f);
    check.getColor().a = 0f;
    check.addAction(sequence(parallel(fadeIn(0.2f), scaleTo(1.0f, 1.0f, 0.2f)), parallel(fadeOut(0.2f), scaleTo(3.0f, 3.0f, 0.2f)), removeActor()));
    ((AbstractScreen) this.getScreen()).getStage().addActor(check);

    SoundManager.play(AnvilSound.ACHIEVEMENT);

    Image ach = new Image(a.texture);
    ach.setOrigin(12, 12);
    ach.setPosition(160 - 12, 120 - 12);
    ach.addAction(sequence(delay(3.0f), parallel(fadeOut(1.0f)), removeActor()));
    ((AbstractScreen) this.getScreen()).getStage().addActor(ach);

    BitmapFont font = new BitmapFont(Gdx.files.internal("skin/default.fnt"), false);
    Label achievement_get = new Label("Achievement get", new Label.LabelStyle(font, Color.WHITE));
    achievement_get.setPosition(160 - font.getBounds("Achievement get").width / 2, 90);
    achievement_get.addAction(sequence(delay(3.0f), parallel(fadeOut(1.0f)), removeActor()));
    ((AbstractScreen) this.getScreen()).getStage().addActor(achievement_get);

    Label achievement_get2 = new Label(a.name, new Label.LabelStyle(font, Color.WHITE));
    achievement_get2.setPosition(160 - font.getBounds(a.name).width / 2, 70);
    achievement_get2.addAction(sequence(delay(3.0f), parallel(fadeOut(1.0f)), removeActor()));
    ((AbstractScreen) this.getScreen()).getStage().addActor(achievement_get2);

  }

}
