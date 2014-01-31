package com.ggf.anvil.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
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
import com.ggf.anvil.elements.InventoryGrid;
import com.ggf.anvil.elements.Item;
import com.ggf.anvil.elements.Slot;
import com.ggf.anvil.services.Player;
import com.ggf.anvil.services.Profile;
import com.ggf.anvil.services.ProfileManager;

public class SmeltingScreen extends AbstractScreen
{
  Skin                  skin;
  TextureAtlas          atlas          = new TextureAtlas(Gdx.files.internal("skin/uiskin.atlas"));

  private Image         bgndImage;
  ArrayList<TextButton> buttons;
  Player                player;

  Slot                  itemSlots[]    = new Slot[3];
  boolean               isItemSelected = false;
  Item                  selectedItem   = null;

  public SmeltingScreen(Anvil game)
  {
    super(game);
    ProfileManager profileManager = ProfileManager.getInstance();
    Profile profile = profileManager.retrieveProfile();
    this.player = profile.getPlayer();
    skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
    skin.addRegions(atlas);
  }

  @Override
  public void render(float delta)
  {
    Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
    stage.act(delta);
    stage.draw();
  }

  @Override
  public void show()
  {
    super.show();

    // retrieve the splash image's region from the atlas
    AtlasRegion bgndRegion = getAtlas().findRegion("smelting-screen/bgnd-image");
    Drawable bgndDrawable = new TextureRegionDrawable(bgndRegion);

    // here we create the splash image actor; its size is set when the
    // resize() method gets called
    bgndImage = new Image(bgndDrawable, Scaling.stretch);
    bgndImage.setFillParent(true);

    // and finally we add the actor to the stage
    bg.addActor(bgndImage);

    setupGUI();

    Image exit = new Image(new Texture(Gdx.files.internal("data/button_exit.png")));
    exit.setPosition(320 - exit.getWidth() - 1, 240 - exit.getHeight() - 1);
    exit.addListener(new InputListener() {
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
      {
        game.getMusicManager().stop();
        ProfileManager.getInstance().retrieveProfile().hour++;
        game.setScreen(new SmitheryScreen(game));
        return true;
      }
    });
    fg.addActor(exit);

  }

  public void setupGUI()
  {
    Texture upButton = new Texture(Gdx.files.internal("ui/02_button_up.png"));
    Texture downButton = new Texture(Gdx.files.internal("ui/02_button_down.png"));
    NinePatch up_patch = new NinePatch(upButton, 5, 5, 5, 5);
    NinePatch down_patch = new NinePatch(downButton, 5, 5, 5, 5);
    NinePatchDrawable up_drawable = new NinePatchDrawable(up_patch);
    NinePatchDrawable down_drawable = new NinePatchDrawable(down_patch);
    TextButtonStyle style = new TextButtonStyle(up_drawable, down_drawable, up_drawable, new BitmapFont());
    skin.add("default", style);

    TextButton sellButton = new TextButton("Mat -> Part", style);
    sellButton.setSize(88, 20);
    sellButton.setPosition(203, 120);
    fg.addActor(sellButton);

    TextButton grindButton = new TextButton("Part -> Mat", style);
    grindButton.setSize(88, 20);
    grindButton.setPosition(20, 120);
    fg.addActor(grindButton);

    // Inventory
    for (int i = 0; i <= 2; i++)
    {
      itemSlots[i] = new Slot();
      bg.addActor(itemSlots[i]);
    }

    skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

    for (int i = 0; i <= 2; i++)
    {
      fg.addActor(itemSlots[i]);
    }

    itemSlots[0].setPosition(68, 164);
    itemSlots[1].setPosition(212, 164);
    itemSlots[2].setPosition(139, 123);

    for (int i = 0; i <= 2; i++)
    {
      final Slot itemSlot = itemSlots[i];
      itemSlots[i].addListener(new ClickListener() {

        public void clicked(InputEvent event, float x, float y)
        {
          if (isItemSelected)
          {
            itemSlot.setItem(selectedItem);
            isItemSelected = false;
          }

        }
      });
    }

    final InventoryGrid ig = new InventoryGrid(8, 3);
    for (int countx = 0; countx < ig.SLOT_COUNT_H; countx++)
    {
      for (int county = 0; county < ig.SLOT_COUNT_V; county++)
      {
        System.out.println("X " + countx + " Y " + county);
        final Slot slot = ig.getSlot(countx, county);
        slot.addListener(new ClickListener() {

          public void clicked(InputEvent event, float x, float y)
          {
            if (isItemSelected)
            {
              slot.setItem(selectedItem);
              isItemSelected = false;
            }

          }
        });
      }
    }

    fg.addActor(ig);
    ig.setPosition(32, 15);
    for (final Item itemToAdd : player.inventory)
    {
      fg.addActor(itemToAdd);
      itemToAdd.clearListeners();
      itemToAdd.addListener(new ClickListener() {

        public void clicked(InputEvent event, float x, float y)
        {
          selectedItem = itemToAdd;
          isItemSelected = true;

          selectedItem.setPosition(selectedItem.getX() + x + 1, selectedItem.getY() + y + 1);
        }
      });
    }
    ig.addAllTheItems(player.inventory);

    stage.addListener(new InputListener() {
      public boolean mouseMoved(InputEvent event, float x, float y)
      {
        if (isItemSelected && selectedItem != null)
        {
          selectedItem.setPosition(x + 1, y + 1);
        }

        return true;
      }
    });

    Image exit = new Image(new Texture(Gdx.files.internal("data/button_exit.png")));
    exit.setPosition(320 - exit.getWidth() - 1, 240 - exit.getHeight() - 1);
    exit.addListener(new InputListener() {
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
      {
        SmeltingScreen.this.game.setScreen(new SmitheryScreen(SmeltingScreen.this.game));
        return true;
      }
    });
    fg.addActor(exit);

  }
}
