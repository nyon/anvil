package com.ggf.anvil.screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

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
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
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
import com.ggf.anvil.services.ProfileManager;
import com.ggf.anvil.services.SoundManager;
import com.ggf.anvil.services.SoundManager.AnvilSound;

public class MainMenuScreen extends AbstractScreen
{
  Image         curtain;
  private Image bgndImage;
  TextureAtlas  atlas = new TextureAtlas(Gdx.files.internal("skin/uiskin.atlas"));
  Skin          skin  = new Skin();

  public MainMenuScreen(Anvil game)
  {
    super(game);
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

    // button "start game"
    TextButton startGameButton = new TextButton("Start Game", skin);
    startGameButton.addListener(new ClickListener() {
      public void clicked(InputEvent event, float x, float y)
      {
        SoundManager.play(AnvilSound.CLICK);
        MainMenuScreen.this.game.setScreen(ProfileManager.getInstance().retrieveProfile().getPlayer().newbie ? new LoadingScreen(MainMenuScreen.this.game, MainMenuScreen.this) : new SmitheryScreen(MainMenuScreen.this.game));

      }
    });

    startGameButton.setSize(120, 20);
    startGameButton.setPosition(100, 200);
    fg.addActor(startGameButton);

    TextButton optionGameButton = new TextButton("Settings", skin);
    optionGameButton.setSize(120, 20);
    optionGameButton.setPosition(100, 160);
    optionGameButton.addListener(new ClickListener() {
      public void clicked(InputEvent event, float x, float y)
      {
        SoundManager.play(AnvilSound.CLICK);
        MainMenuScreen.this.game.setScreen(new SettingsScreen(MainMenuScreen.this.game, MainMenuScreen.this));
      }
    });
    fg.addActor(optionGameButton);

    TextButton endGameButton = new TextButton("Quit", skin);
    endGameButton.setSize(120, 20);
    endGameButton.setPosition(100, 80);
    endGameButton.addListener(new ClickListener() {
      public void clicked(InputEvent event, float x, float y)
      {
        SoundManager.play(AnvilSound.CLICK);
        Gdx.app.exit();
      }
    });
    fg.addActor(endGameButton);

    // TextButton weaponStatsButton = new TextButton( "Weapon Stats", skin );
    // weaponStatsButton.setSize(120,20);
    // weaponStatsButton.setPosition(100, 80);
    // weaponStatsButton.addListener( new ClickListener() {
    // public void clicked(InputEvent event, float x, float y) {
    // MainMenuScreen.this.game.setScreen(new
    // WeaponStatsScreen(MainMenuScreen.this.game, new Weapon("Mighty Sword")));
    // }
    // } );
    // fg.addActor( weaponStatsButton );
    TextButton creditsButton = new TextButton("Credits", skin);
    creditsButton.setSize(120, 20);
    creditsButton.setPosition(100, 120);
    creditsButton.addListener(new ClickListener() {
      public void clicked(InputEvent event, float x, float y)
      {
        SoundManager.play(AnvilSound.CLICK);
        MainMenuScreen.this.game.setScreen(new CreditsScreen(MainMenuScreen.this.game, MainMenuScreen.this));
      }
    });
    fg.addActor(creditsButton);

    // retrieve the splash image's region from the atlas
    AtlasRegion curtainRegion = this.getAtlas().findRegion("main-menu/curtain");
    Drawable curtainDrawable = new TextureRegionDrawable(curtainRegion);

    // here we create the splash image actor; its size is set when the
    // resize() method gets called
    curtain = new Image(curtainDrawable, Scaling.stretch);
    curtain.setFillParent(true);

    curtain.addAction(sequence(fadeOut(0.75f), new Action() {
      @Override
      public boolean act(float delta)
      {
        this.getActor().remove();
        return true;
      }
    }));

    stage.addActor(curtain);

  }

  @Override
  public void render(float delta)
  {
    Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
    stage.act(Gdx.graphics.getDeltaTime());
    stage.draw();

  }

  @Override
  public void resize(int width, int height)
  {
    // TODO Auto-generated method stub
    // stage.setViewport(width, height, true);
  }

  @Override
  public void show()
  {
    super.show();

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
}
