package com.ggf.anvil.screens;

import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.ggf.anvil.Anvil;

public class CreditsScreen extends AbstractScreen implements Screen
{
  ArrayList<String> creditentries;
  ArrayList<Label>  labels;
  float             position = 0;
  Skin              skin;
  TextureAtlas      atlas    = new TextureAtlas(Gdx.files.internal("skin/uiskin.atlas"));

  AbstractScreen    parentScreen;

  public CreditsScreen(Anvil game, AbstractScreen parentScreen)
  {
    super(game);
    this.parentScreen = parentScreen;

    creditentries = new ArrayList<String>();
    creditentries.add("Anvil");
    creditentries.add("A Game by GOBI Game Forge");
    creditentries.add("");
    creditentries.add("");
    creditentries.add("Project Lead:");
    creditentries.add("Timm Decker");
    creditentries.add("");
    creditentries.add("Programming Genius:");
    creditentries.add("Malte Husmann");
    creditentries.add("");
    creditentries.add("Graphic Asset Artist:");
    creditentries.add("Franziska Lorz");
    creditentries.add("");
    creditentries.add("Allround-Talent:");
    creditentries.add("Patrick Niethen");
    creditentries.add("");
    creditentries.add("Hardware Division:");
    creditentries.add("Fritz Jacob");
    creditentries.add("");
    creditentries.add("Fun Department:");
    creditentries.add("Jens Trillmann");
    creditentries.add("");
    creditentries.add("We thank especially:");
    creditentries.add("Daniel Vidal");
    creditentries.add("for his awesome graphics :D");
    creditentries.add("");
    creditentries.add("created during the Global Game Jam 2014");
    creditentries.add("at the University of Bremen");
    creditentries.add("");
    creditentries.add("");
    creditentries.add("Thank you for playing!");
    Collections.reverse(creditentries);

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

    labels = new ArrayList<Label>();
    for (String creditstring : creditentries)
    {
      labels.add(new Label(creditstring, skin));

    }

    int x = -400;
    for (Label label : labels)
    {
      label.setX(GAME_VIEWPORT_WIDTH / 2 - (label.getPrefWidth() / 2));
      label.setY(x);
      fg.addActor(label);
      x += 15;
    }

    Image exit = new Image(new Texture(Gdx.files.internal("data/button_exit.png")));
    exit.setPosition(320 - exit.getWidth() - 1, 240 - exit.getHeight() - 1);
    exit.addListener(new InputListener() {
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
      {
        CreditsScreen.this.game.setScreen(CreditsScreen.this.parentScreen);
        return true;
      }
    });
    fg.addActor(exit);

  }

  @Override
  public void render(float delta)
  {
    super.render(delta);
    position += delta * 20;
    for (Label label : labels)
    {
      label.setY(label.getY() + delta * 20);
    }
    stage.draw();
    if (position > 650)
    {
      game.setScreen(parentScreen);
    }
  }

}
