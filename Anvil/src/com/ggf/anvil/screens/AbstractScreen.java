package com.ggf.anvil.screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.removeActor;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.ggf.anvil.Anvil;
import com.ggf.anvil.services.GameStage;

/**
 * The base class for all game screens.
 */
public abstract class AbstractScreen implements Screen
{
  // the fixed viewport dimensions
  public static final int   GAME_VIEWPORT_WIDTH  = 320;
  public static final int   GAME_VIEWPORT_HEIGHT = 240;

  protected final Anvil     game;
  protected final GameStage stage;

  protected BitmapFont      font;
  private SpriteBatch       batch;
  private Skin              skin;
  private TextureAtlas      atlas;
  private Table             table;

  protected Group           fg;
  protected Group           bg;

  public AbstractScreen(Anvil game)
  {
    this.game = game;

    this.stage = new GameStage(GAME_VIEWPORT_WIDTH, GAME_VIEWPORT_HEIGHT, true);
    this.font = new BitmapFont(Gdx.files.internal("skin/default.fnt"), false);

    this.fg = new Group();
    this.fg.setWidth(GAME_VIEWPORT_WIDTH);
    this.fg.setHeight(GAME_VIEWPORT_HEIGHT);

    this.bg = new Group();
    this.bg.setWidth(GAME_VIEWPORT_WIDTH);
    this.bg.setHeight(GAME_VIEWPORT_HEIGHT);

    this.stage.addActor(this.bg);
    this.stage.addActor(this.fg);

    // TODO: add input multiplexer
    Gdx.input.setInputProcessor(this.stage);
  }

  public GameStage getStage()
  {
    return stage;
  }

  protected String getName()
  {
    return getClass().getSimpleName();
  }

  // Lazily loaded collaborators

  public BitmapFont getFont()
  {
    if (font == null)
    {
      font = new BitmapFont();
    }
    return font;
  }

  public SpriteBatch getBatch()
  {
    if (batch == null)
    {
      batch = new SpriteBatch();
    }
    return batch;
  }

  public TextureAtlas getAtlas()
  {
    if (atlas == null)
    {
      atlas = new TextureAtlas(Gdx.files.internal("image-atlases/pages.atlas"));
    }
    return atlas;
  }

  protected Skin getSkin()
  {
    if (skin == null)
    {
      FileHandle skinFile = Gdx.files.internal("skin/uiskin.json");
      skin = new Skin(skinFile);
    }
    return skin;
  }

  protected Table getTable()
  {
    if (table == null)
    {
      table = new Table(getSkin());
      table.setFillParent(true);
      if (Anvil.DEV_MODE)
      {
        table.debug();
      }
      stage.addActor(table);
    }
    return table;
  }

  // Screen implementation

  @Override
  public void show()
  {
    Gdx.app.log(Anvil.LOG, "Showing screen: " + getName());

    // set the stage as the input processor
    Gdx.input.setInputProcessor(stage);
  }

  @Override
  public void resize(int width, int height)
  {
    Gdx.app.log(Anvil.LOG, "Resizing screen: " + getName() + " to: " + width + " x " + height);

  }

  @Override
  public void render(float delta)
  {
    // (1) process the game logic

    // update the actors
    stage.act(delta);

    // (2) draw the result

    // clear the screen with the given RGB color (black)
    Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    // draw the actors
    stage.draw();

    // draw the table debug lines
    Table.drawDebug(stage);
  }

  @Override
  public void hide()
  {
    Gdx.app.log(Anvil.LOG, "Hiding screen: " + getName());

    // dispose the screen when leaving the screen;
    // note that the dipose() method is not called automatically by the
    // framework, so we must figure out when it's appropriate to call it
    dispose();
  }

  @Override
  public void pause()
  {
    Gdx.app.log(Anvil.LOG, "Pausing screen: " + getName());
  }

  @Override
  public void resume()
  {
    Gdx.app.log(Anvil.LOG, "Resuming screen: " + getName());
  }

  @Override
  public void dispose()
  {
    Gdx.app.log(Anvil.LOG, "Disposing screen: " + getName());

    // the following call disposes the screen's stage, but on my computer it
    // crashes the game so I commented it out; more info can be found at:
    // http://www.badlogicgames.com/forum/viewtopic.php?f=11&t=3624
    // stage.dispose();

    // as the collaborators are lazily loaded, they may be null
    if (font != null) font.dispose();
    if (batch != null) batch.dispose();
    if (skin != null) skin.dispose();
    if (atlas != null) atlas.dispose();
  }

  protected void messageBox(String message)
  {
    final Image mbox = new Image(new Texture(Gdx.files.internal("data/textbox.png")));
    final Label greet = new Label(message, new Label.LabelStyle(font, Color.WHITE));
    mbox.setPosition(0, 90);
    mbox.setOrigin(mbox.getWidth() / 2, mbox.getHeight() / 2);
    mbox.addListener(new InputListener() {
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
      {
        mbox.clearActions();
        greet.clearActions();
        mbox.addAction(sequence(parallel(fadeOut(0.1f), scaleTo(0, 0, 0.1f)), removeActor()));
        greet.addAction(fadeOut(0.1f));
        return true;
      }
    });
    fg.addActor(mbox);

    greet.setWidth(320);
    greet.setPosition(160 - font.getWrappedBounds(message, 320).width / 2, 120);
    greet.addAction(sequence(delay(3.0f), fadeOut(2.0f), removeActor()));
    mbox.addAction(sequence(delay(3.0f), fadeOut(2.0f), removeActor()));
    greet.setTouchable(Touchable.disabled);
    fg.addActor(greet);

  }
}
