package com.ggf.anvil.screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.removeActor;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ggf.anvil.Anvil;
import com.ggf.anvil.elements.InventoryGrid;
import com.ggf.anvil.elements.Item;
import com.ggf.anvil.elements.Slot;
import com.ggf.anvil.services.Merchant;
import com.ggf.anvil.services.MusicManager.AnvilMusic;
import com.ggf.anvil.services.ProfileManager;
import com.ggf.anvil.services.SoundManager;
import com.ggf.anvil.services.SoundManager.AnvilSound;

public class MarketplaceScreen extends AbstractScreen
{

  private class InventoryItemListener extends ClickListener
  {

    public void clicked(InputEvent event, float x, float y)
    {
      SoundManager.getInstance().play(AnvilSound.CLICK);

      if (!isItemSelected)
      {
        selectedItem = (Item) event.getTarget();
        isItemSelected = true;
        Vector2 v = new Vector2(0, 0);

        event.getTarget().localToStageCoordinates(v);
        originatedPlayer = v.y < 100;

        selectedItem.setPosition(selectedItem.getX() + x - 16, selectedItem.getY() + y - 16);
        selectedItem.setTouchable(Touchable.disabled);
      }
    }
  }

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
      MarketplaceScreen.this.tooltipText = content;
      MarketplaceScreen.this.tooltipX = v.x - MarketplaceScreen.this.font.getBounds(content).width * 0.75f;
      MarketplaceScreen.this.tooltipY = v.y;

      return true;
    }

    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor)
    {
      MarketplaceScreen.this.tooltipAlpha = 1.0f;
    }

    @Override
    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor)
    {
      MarketplaceScreen.this.tooltipAlpha = 0.0f;
    }
  }

  private float         tooltipX;
  private float         tooltipY;
  private String        tooltipText;
  private float         tooltipAlpha;

  private Image         bgndImage;
  ArrayList<TextButton> buttons;

  boolean               isItemSelected   = false;
  Item                  selectedItem     = null;
  boolean               originatedPlayer = false;
  Label                 merchantMoney    = null;
  Label                 playerMoney      = null;
  Label                 merchantName     = null;
  Label                 playerName       = null;
  Merchant              currentMerchant  = null;

  public MarketplaceScreen(Anvil game)
  {
    super(game);

    currentMerchant = ProfileManager.getInstance().retrieveProfile().merchant;

    // here we create the splash image actor; its size is set when the
    // resize() method gets called
    bgndImage = new Image(new Texture(Gdx.files.internal("data/marketplaceBgnd.png")));
    bgndImage.setFillParent(true);

    // and finally we add the actor to the stage
    bg.addActor(bgndImage);

    buttons = new ArrayList<TextButton>();

    // MERCHANT GRID

    final InventoryGrid mg = new InventoryGrid(8, 3);
    for (int countx = 0; countx < mg.SLOT_COUNT_H; countx++)
    {
      for (int county = 0; county < mg.SLOT_COUNT_V; county++)
      {
        final Slot slot = mg.getSlot(countx, county);
        slot.addListener(new ClickListener() {

          public void clicked(InputEvent event, float x, float y)
          {
            if (isItemSelected)
            {
              if (originatedPlayer)
              {
                System.out.println("SELL " + selectedItem);

                // TODO: TH: show price when picking up, change to selling
                // action later
                // Label myPrice = new Label("+"+selectedItem.price+" $", new
                // Label.LabelStyle(font, Color.WHITE));
                // Vector2 myV = new Vector2(x,y);
                // selectedItem.localToStageCoordinates(myV);
                // myPrice.setPosition(myV.x-20.0f, myV.y);
                // fg.addActor(myPrice);

                if (currentMerchant.gold >= selectedItem.price)
                {
                  SoundManager.getInstance().play(AnvilSound.GOLD);
                  ProfileManager.getInstance().retrieveProfile().getPlayer().gold += selectedItem.price;
                  ProfileManager.getInstance().retrieveProfile().getPlayer().inventory.remove(selectedItem);
                  currentMerchant.inventory.add(selectedItem);
                  currentMerchant.gold -= selectedItem.price;
                  slot.setItem(selectedItem);
                  selectedItem.setTouchable(Touchable.enabled);
                  isItemSelected = false;

                  // fg.removeActor(myPrice);
                  Label catching = new Label("+" + selectedItem.price + " $", new Label.LabelStyle(font, Color.GREEN));
                  Vector2 v = new Vector2(x, y);
                  selectedItem.localToStageCoordinates(v);
                  catching.setPosition(v.x - 20.0f, v.y);
                  catching.addAction(sequence(parallel(moveTo(v.x - 20.0f, v.y + 20.0f, 1.0f), fadeOut(1.0f)), removeActor()));
                  fg.addActor(catching);

                }
                else
                {
                  SoundManager.getInstance().play(AnvilSound.WEAPONFAIL);
                  messageBox("I got no gold left...");
                }

              }
              else
              {
                slot.setItem(selectedItem);
                selectedItem.setTouchable(Touchable.enabled);
                isItemSelected = false;
              }
            }

          }
        });
      }
    }

    mg.setPosition(56, 110);
    fg.addActor(mg);

    merchantMoney = new Label("", new Label.LabelStyle(font, Color.WHITE));
    merchantMoney.setPosition(0, 155);
    fg.addActor(merchantMoney);

    merchantName = new Label("", new Label.LabelStyle(font, Color.WHITE));
    merchantName.setPosition(0, 220);
    fg.addActor(merchantName);

    // INVENTORY GRID

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
              if (!originatedPlayer)
              {
                System.out.println("BUY " + selectedItem);

                // TODO: TH: show price when picking up, change to selling
                // action later
                // Label myPrice2 = new Label("+"+selectedItem.price+" $", new
                // Label.LabelStyle(font, Color.WHITE));
                // Vector2 myV2 = new Vector2(x,y);
                // selectedItem.localToStageCoordinates(myV2);
                // myPrice2.setPosition(myV2.x-20.0f, myV2.y);
                // fg.addActor(myPrice2);

                if (ProfileManager.getInstance().retrieveProfile().getPlayer().gold >= selectedItem.price)
                {
                  SoundManager.getInstance().play(AnvilSound.GOLD);
                  currentMerchant.gold += selectedItem.price;
                  ProfileManager.getInstance().retrieveProfile().getPlayer().gold -= selectedItem.price;
                  slot.setItem(selectedItem);
                  selectedItem.setTouchable(Touchable.enabled);
                  isItemSelected = false;
                  ProfileManager.getInstance().retrieveProfile().getPlayer().inventory.add(selectedItem);
                  currentMerchant.inventory.remove(selectedItem);

                  Label catching = new Label("-" + selectedItem.price + " $", new Label.LabelStyle(font, Color.RED));
                  Vector2 v = new Vector2(x, y);
                  selectedItem.localToStageCoordinates(v);
                  catching.setPosition(v.x - 20.0f, v.y);
                  catching.addAction(sequence(parallel(moveTo(v.x - 20.0f, v.y + 20.0f, 1.0f), fadeOut(1.0f)), removeActor()));
                  fg.addActor(catching);

                }
                else
                {
                  SoundManager.getInstance().play(AnvilSound.WEAPONFAIL);
                  messageBox("Not enough gold, huh?");
                }

              }
              else
              {
                slot.setItem(selectedItem);
                selectedItem.setTouchable(Touchable.enabled);
                isItemSelected = false;

              }
            }

          }
        });
      }
    }

    ig.setPosition(56, 8);
    fg.addActor(ig);

    // Load and set inventory after adding inventory grid to FG
    List<Item> inventory = ProfileManager.getInstance().retrieveProfile().getPlayer().inventory;
    for (Item i : inventory)
    {
      i.clearListeners(); // WICHTIG
      i.addListener(new InventoryItemListener());
      i.addListener(new TooltipListener(i.name + "(" + Item.ItemType.prettyPrint(i.type) + "): " + i.price + " $"));

      fg.addActor(i);
    }
    ig.addAllTheItems(inventory);

    // Load and set inventory after adding inventory grid to FG
    inventory = currentMerchant.inventory;
    for (Item i : inventory)
    {
      i.clearListeners(); // WICHTIG
      i.addListener(new InventoryItemListener());
      i.addListener(new TooltipListener(i.name + "(" + Item.ItemType.prettyPrint(i.type) + "): " + i.price + " $"));
      fg.addActor(i);
    }
    mg.addAllTheItems(inventory);

    playerMoney = new Label("", new Label.LabelStyle(font, Color.WHITE));
    playerMoney.setPosition(0, 52);
    fg.addActor(playerMoney);

    playerName = new Label(ProfileManager.getInstance().retrieveProfile().getPlayer().name + ":", new Label.LabelStyle(font, Color.WHITE));
    playerName.setPosition(0, 60);
    fg.addActor(playerName);

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

    // Message box
    String[] greetings = { "Arr... greetings fellow smith!", "I've got some special Deals here, just for you!", "Hey $NAME$, look!", "Are you just browsing?", "You won't find better prices anywhere else!" };
    String greetMessage = greetings[new Random().nextInt(greetings.length)].replace("$MNAME$", currentMerchant.name).replace("$NAME$", ProfileManager.getInstance().retrieveProfile().getPlayer().name);
    messageBox(greetMessage);

    // EXIT
    Image exit = new Image(new Texture(Gdx.files.internal("data/button_exit.png")));
    exit.setPosition(320 - exit.getWidth() - 1, 240 - exit.getHeight() - 1);
    exit.addListener(new InputListener() {
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
      {
        SoundManager.getInstance().play(AnvilSound.CLICK);
        ProfileManager.getInstance().retrieveProfile().hour++;
        MarketplaceScreen.this.game.setScreen(new SmitheryScreen(MarketplaceScreen.this.game));
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

    int playerGold = ProfileManager.getInstance().retrieveProfile().getPlayer().gold;
    String moneyString = "" + playerGold + "$";
    playerMoney.setText(moneyString);
    playerMoney.setPosition(54 - font.getBounds(moneyString).width, playerMoney.getY());

    moneyString = "" + currentMerchant.gold + "$";
    merchantMoney.setText(moneyString);
    merchantMoney.setPosition(54 - font.getBounds(moneyString).width, merchantMoney.getY());

    String nameString = currentMerchant.name;
    merchantName.setText(nameString);
    merchantName.setPosition(160 - font.getBounds(nameString).width / 2, merchantName.getY());

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
  }

  @Override
  public void show()
  {
    super.show();
    game.getMusicManager().play(AnvilMusic.FORGING);

  }

  @Override
  public void hide()
  {
    // TODO Auto-generated method stub

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
    // TODO Auto-generated method stub

  }

}
