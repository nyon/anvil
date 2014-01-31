package com.ggf.anvil.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ggf.anvil.Anvil;
import com.ggf.anvil.elements.InventoryGrid;
import com.ggf.anvil.elements.Item;
import com.ggf.anvil.elements.Item.ItemType;
import com.ggf.anvil.elements.Slot;
import com.ggf.anvil.services.MusicManager.AnvilMusic;
import com.ggf.anvil.services.Player;
import com.ggf.anvil.services.Profile;
import com.ggf.anvil.services.ProfileManager;
import com.ggf.anvil.services.SoundManager;
import com.ggf.anvil.services.SoundManager.AnvilSound;

public class BlueprintScreen extends AbstractScreen
{
  private class TooltipListener extends InputListener
  {
    String content;

    public TooltipListener(String content)
    {
      this.content = content;
    }

    @Override
    public boolean mouseMoved(InputEvent event, float x, float y)
    {
      Vector2 v = new Vector2(x, y);
      event.getTarget().localToStageCoordinates(v);
      BlueprintScreen.this.tooltipText = content;
      BlueprintScreen.this.tooltipX = v.x - BlueprintScreen.this.font.getBounds(content).width * 0.75f;
      BlueprintScreen.this.tooltipY = v.y;

      return true;
    }

    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor)
    {
      BlueprintScreen.this.tooltipAlpha = 1.0f;
    }

    @Override
    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor)
    {
      // TODO Auto-generated method stub
      BlueprintScreen.this.tooltipAlpha = 0.0f;
    }
  }

  private float         tooltipX;
  private float         tooltipY;
  private String        tooltipText;
  private float         tooltipAlpha;

  private Image         bgndImage;
  private Image         anvilButton;
  Skin                  skin;
  ArrayList<TextButton> buttons;
  Player                player;

  TextureAtlas          atlas          = new TextureAtlas(Gdx.files.internal("skin/uiskin.atlas"));

  Slot                  itemSlots[]    = new Slot[3];
  boolean               isItemSelected = false;
  Item                  selectedItem   = null;

  public BlueprintScreen(Anvil game)
  {
    super(game);

    ProfileManager profileManager = ProfileManager.getInstance();
    Profile profile = profileManager.retrieveProfile();
    this.player = profile.getPlayer();

    // here we create the splash image actor; its size is set when the
    // resize() method gets called
    bgndImage = new Image(new Texture(Gdx.files.internal("data/blueprintBgnd.png")));
    bgndImage.setFillParent(true);

    // and finally we add the actor to the stage
    bg.addActor(bgndImage);

    anvilButton = new Image(new Texture(Gdx.files.internal("data/anvil.png")));
    anvilButton.setPosition(258, 165);
    anvilButton.addListener(new ClickListener() {
      public void clicked(InputEvent event, float x, float y)
      {
        SoundManager.getInstance().play(AnvilSound.DENGEL1);
        Item grip = itemSlots[0].getItem();
        Item crossguard = itemSlots[1].getItem();
        Item blade = itemSlots[2].getItem();
        if (grip == null || crossguard == null || blade == null)
        {
          String errorMessage = "At least one part is missing";
          messageBox(errorMessage);
          return;
        }

        if (grip.type != ItemType.GRIP || crossguard.type != ItemType.CROSS_GUARD || blade.type != ItemType.BLADE)
        {
          String errorMessage = "The parts don't match the right type!";
          messageBox(errorMessage);
          return;
        }

        player.inventory.remove(grip);
        player.inventory.remove(crossguard);
        player.inventory.remove(blade);

        BlueprintScreen.this.game.setScreen(new ForgingScreen(BlueprintScreen.this.game, grip, crossguard, blade));
      }
    });

    fg.addActor(anvilButton);

    for (int i = 0; i <= 2; i++)
    {
      itemSlots[i] = new Slot();
      bg.addActor(itemSlots[i]);
    }
    skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

    Gdx.input.setInputProcessor(stage);
    for (int i = 0; i <= 2; i++)
    {
      fg.addActor(itemSlots[i]);
    }

    itemSlots[0].setPosition(34, 161);
    itemSlots[1].setPosition(111, 161);
    itemSlots[2].setPosition(184, 161);

    for (int i = 0; i < 3; i++)
    {
      final Slot itemSlot = itemSlots[i];
      itemSlots[i].addListener(new ClickListener() {

        public void clicked(InputEvent event, float x, float y)
        {
          if (isItemSelected)
          {
            itemSlot.setItem(selectedItem);
            isItemSelected = false;

            selectedItem.setTouchable(Touchable.enabled);
          }

        }
      });
    }

    final InventoryGrid ig = new InventoryGrid(8, 3);
    for (int countx = 0; countx < ig.SLOT_COUNT_H; countx++)
    {
      for (int county = 0; county < ig.SLOT_COUNT_V; county++)
      {
        final Slot slot = ig.getSlot(countx, county);
        slot.addListener(new ClickListener() {

          public void clicked(InputEvent event, float x, float y)
          {
            if (isItemSelected)
            {
              slot.setItem(selectedItem);
              isItemSelected = false;
              selectedItem.setTouchable(Touchable.enabled);
            }

          }
        });
      }
    }

    fg.addActor(ig);
    ig.setPosition(32, 8);
    for (final Item itemToAdd : player.inventory)
    {
      fg.addActor(itemToAdd);
      itemToAdd.clearListeners();
      itemToAdd.addListener(new ClickListener() {

        public void clicked(InputEvent event, float x, float y)
        {
          SoundManager.getInstance().play(AnvilSound.CLICK);
          if (!isItemSelected)
          {
            selectedItem = itemToAdd;
            isItemSelected = true;

            selectedItem.setPosition(selectedItem.getX() + x - 16, selectedItem.getY() + y - 16);
            selectedItem.setTouchable(Touchable.disabled);
          }
        }
      });
      itemToAdd.addListener(new TooltipListener(itemToAdd.name + "(" + Item.ItemType.prettyPrint(itemToAdd.type) + ")"));
    }
    ig.addAllTheItems(player.inventory);

    stage.addListener(new InputListener() {
      public boolean mouseMoved(InputEvent event, float x, float y)
      {
        if (isItemSelected && selectedItem != null)
        {
          selectedItem.setPosition(x - 16, y - 16);
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
        SoundManager.getInstance().play(AnvilSound.CLICK);
        BlueprintScreen.this.game.setScreen(new SmitheryScreen(BlueprintScreen.this.game));
        return true;
      }
    });
    fg.addActor(exit);

  }

  @Override
  public void render(float delta)
  {
    Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
    stage.act(delta);
    stage.draw();

    SpriteBatch batch = stage.getSpriteBatch();
    batch.begin();
    font.setScale(1.0f);
    if (tooltipText != null)
    {
      font.setScale(0.75f);
      font.setColor(1, 1, 1, tooltipAlpha);
      font.draw(batch, tooltipText, tooltipX, tooltipY);
      font.setScale(1.0f);

    }
    batch.end();

  }

  @Override
  public void resize(int width, int height)
  {
    // TODO Auto-generated method stub
    super.resize(width, height);

    itemSlots[0].setPosition(40, 128);
    itemSlots[1].setPosition(104, 128);
    itemSlots[2].setPosition(168, 128);
  }

  @Override
  public void show()
  {
    super.show();
    game.getMusicManager().play(AnvilMusic.FORGING);

  }

}
