package com.ggf.anvil.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.ggf.anvil.Anvil;
import com.ggf.anvil.services.PreferencesManager;
import com.ggf.anvil.services.ProfileManager;
import com.ggf.anvil.services.SoundManager;

public class SettingsScreen extends AbstractScreen
{
  AbstractScreen     parentScreen;
  PreferencesManager preferences;
  Skin               skin;
  TextureAtlas       atlas = new TextureAtlas(Gdx.files.internal("skin/uiskin.atlas"));
  Image              bgndImage;

  public SettingsScreen(Anvil game, final AbstractScreen parentScreen)
  {
    super(game);
    this.parentScreen = parentScreen;
    preferences = game.getPreferencesManager();

    skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
    skin.addRegions(atlas);

    skin.add("default", new BitmapFont());

    Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
    pixmap.setColor(Color.WHITE);
    pixmap.fill();
    skin.add("white", new Texture(pixmap));

    Texture upButton = new Texture(Gdx.files.internal("ui/02_button_up.png"));
    Texture downButton = new Texture(Gdx.files.internal("ui/02_button_down.png"));
    NinePatch up_patch = new NinePatch(upButton, 7, 7, 7, 7);
    NinePatch down_patch = new NinePatch(downButton, 6, 6, 6, 6);
    NinePatchDrawable up_drawable = new NinePatchDrawable(up_patch);
    NinePatchDrawable down_drawable = new NinePatchDrawable(down_patch);
    TextButtonStyle style = new TextButtonStyle(up_drawable, down_drawable, down_drawable, new BitmapFont());
    // TextButtonStyle textButtonStyle = new TextButtonStyle();
    // textButtonStyle.up = new TextureRegionDrawable(upRegion);
    // textButtonStyle.down = new TextureRegionDrawable(downRegion);
    // textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
    // textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
    // textButtonStyle.font = skin.getFont("default");
    skin.add("default", style);

    final TextButton controllerButton = new TextButton(preferences.isMusicEnabled() ? "Controller: ON" : "Controller: OFF", skin);
    controllerButton.setSize(120, 20);
    controllerButton.setPosition(100, 60);
    controllerButton.addListener(new ClickListener() {
      public void clicked(InputEvent event, float x, float y)
      {
        controllerButton.setText(preferences.isAnvilControllerEnabled() ? "Controller: OFF" : "Controller: ON");
        preferences.setAnvilControllerEnabled(!preferences.isAnvilControllerEnabled());
      }
    });
    fg.addActor(controllerButton);

    final TextButton musicButton = new TextButton(preferences.isMusicEnabled() ? "Music: ON" : "Music: OFF", skin);
    musicButton.setSize(120, 20);
    musicButton.setPosition(100, 95);
    musicButton.addListener(new ClickListener() {
      public void clicked(InputEvent event, float x, float y)
      {
        musicButton.setText(preferences.isMusicEnabled() ? "Music: OFF" : "Music: ON");
        preferences.setMusicEnabled(!preferences.isMusicEnabled());
        SettingsScreen.this.game.getMusicManager().setEnabled(preferences.isMusicEnabled());
      }
    });
    fg.addActor(musicButton);

    final TextButton soundButton = new TextButton(preferences.isSoundEnabled() ? "Sound: ON" : "Sound: OFF", skin);
    soundButton.setSize(120, 20);
    soundButton.setPosition(100, 130);
    soundButton.addListener(new ClickListener() {
      public void clicked(InputEvent event, float x, float y)
      {
        soundButton.setText(preferences.isSoundEnabled() ? "Sound: OFF" : "Sound: ON");
        preferences.setSoundEnabled(!preferences.isSoundEnabled());
        SoundManager.getInstance().setEnabled(preferences.isSoundEnabled());
      }
    });
    fg.addActor(soundButton);

    final TextButton backButton = new TextButton("Play Video", skin);
    backButton.setSize(120, 20);
    backButton.setPosition(100, 165);
    backButton.addListener(new ClickListener() {
      public void clicked(InputEvent event, float x, float y)
      {
        SettingsScreen.this.game.setScreen(new LoadingScreen(SettingsScreen.this.game, SettingsScreen.this.parentScreen));
      }
    });
    fg.addActor(backButton);

    final TextButton resetButton = new TextButton("Reset Save", skin);
    resetButton.setSize(120, 20);
    resetButton.setPosition(100, 200);
    resetButton.addListener(new ClickListener() {
      public void clicked(InputEvent event, float x, float y)
      {
        ProfileManager.getInstance().retrieveProfile().getPlayer().setInitialData();
      }
    });
    fg.addActor(resetButton);

    // EXIT
    Image exit = new Image(new Texture(Gdx.files.internal("data/button_exit.png")));
    exit.setPosition(320 - exit.getWidth() - 1, 240 - exit.getHeight() - 1);
    exit.addListener(new InputListener() {
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
      {
        SettingsScreen.this.game.setScreen(parentScreen);
        return true;
      }
    });
    fg.addActor(exit);

    // retrieve the splash image's region from the atlas
    AtlasRegion bgndRegion = getAtlas().findRegion("main-menu-screen/bgnd-image");
    Drawable bgndDrawable = new TextureRegionDrawable(bgndRegion);

    // here we create the splash image actor; its size is set when the
    // resize() method gets called
    bgndImage = new Image(bgndDrawable, Scaling.stretch);
    bgndImage.setFillParent(true);

    // and finally we add the actor to the stage
    bg.addActor(bgndImage);

  }

  public void render(float delta)
  {
    Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
    stage.act(delta);
    stage.draw();
  }

}
