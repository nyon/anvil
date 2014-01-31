package com.ggf.anvil.screens;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ggf.anvil.Anvil;
import com.ggf.anvil.elements.InventoryGrid;
import com.ggf.anvil.elements.Item;
import com.ggf.anvil.elements.Item.ItemType;
import com.ggf.anvil.elements.Slot;
import com.ggf.anvil.elements.WeaponEffect;
import com.ggf.anvil.elements.WeaponEffectList;
import com.ggf.anvil.services.Achievement;
import com.ggf.anvil.services.AchievementManager;
import com.ggf.anvil.services.Player;
import com.ggf.anvil.services.Profile;
import com.ggf.anvil.services.ProfileManager;
import com.ggf.anvil.services.SoundManager;
import com.ggf.anvil.services.SoundManager.AnvilSound;

public class InventoryScreen extends AbstractScreen
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
      InventoryScreen.this.tooltipText = content;
      InventoryScreen.this.tooltipX = v.x - InventoryScreen.this.font.getBounds(content).width * 0.75f;
      InventoryScreen.this.tooltipY = v.y;

      return true;
    }

    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor)
    {
      InventoryScreen.this.tooltipAlpha = 1.0f;
    }

    @Override
    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor)
    {
      // TODO Auto-generated method stub
      InventoryScreen.this.tooltipAlpha = 0.0f;
    }
  }

  private float   tooltipX;
  private float   tooltipY;
  private String  tooltipText;
  private float   tooltipAlpha;

  public Image    bgndImage;
  private Profile profile;

  private Player  player;

  private boolean isItemSelected;
  private Item    selectedItem;

  private Image   anvilButton;

  public InventoryScreen(Anvil game)
  {
    super(game);

    profile = ProfileManager.getInstance().retrieveProfile();
    player = profile.getPlayer();

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

    final InventoryGrid craftGrid = new InventoryGrid(3, 2);
    for (int countx = 0; countx < craftGrid.SLOT_COUNT_H; countx++)
    {
      for (int county = 0; county < craftGrid.SLOT_COUNT_V; county++)
      {
        final Slot slot = craftGrid.getSlot(countx, county);
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
    craftGrid.setPosition(112, 160);

    ig.setPosition(32, 8);
    fg.addActor(ig);
    fg.addActor(craftGrid);

    anvilButton = new Image(new Texture(Gdx.files.internal("data/anvil.png")));
    anvilButton.setPosition(144, 125);
    anvilButton.addListener(new ClickListener() {
      public void clicked(InputEvent event, float x, float y)
      {
        System.out.println("AnvilButton Click");
        ArrayList<Item> selectedItems = craftGrid.getAllItems();

        Item weapon = null;
        ArrayList<Item> gems = new ArrayList<Item>();
        for (Item item : selectedItems)
        {
          if (item.type == ItemType.WEAPON)
          {
            if (weapon == null)
            {
              weapon = item;
            }
            else
            {
              InventoryScreen.this.messageBox("Choose only one weapon");
              return;
            }
          }
          else if (item.type == ItemType.GEM)
          {
            gems.add(item);
          }
        }

        if (weapon == null)
        {
          InventoryScreen.this.messageBox("Select a weapon");
          return;
        }
        if (gems.size() == 0)
        {
          InventoryScreen.this.messageBox("Choose at least one gem");
          return;
        }

        String successMessage = "New effects! ";

        WeaponEffectList effectList = new WeaponEffectList();
        for (Item gem : gems)
        {
          WeaponEffect effect = effectList.getEffectByColor(new Random().nextInt(4));
          weapon.effects.add(effect);
          successMessage += "\n- " + effect.name + " " + effect.bonus;
          craftGrid.removeItem(gem);
          fg.removeActor(gem);
          weapon.calculatePrice();
          Player player = InventoryScreen.this.profile.getPlayer();
          player.inventory.remove(gem);

          craftGrid.removeItem(weapon);
          ig.addItem(weapon);
        }

        InventoryScreen.this.messageBox(successMessage);
      }
    });
    anvilButton.addListener(new TooltipListener("Combine gems"));
    fg.addActor(anvilButton);

    ig.addAllTheItems(player.inventory);
    for (final Item item : player.inventory)
    {
      item.clearListeners();
      item.addListener(new TooltipListener(item.name + " (" + Item.ItemType.prettyPrint(item.type) + "): " + item.price + " $"));
      item.addListener(new InputListener() {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
        {
          if (button == 1)
          {
            InventoryScreen.this.game.setScreen(new WeaponStatsScreen(InventoryScreen.this.game, item));
          }
          return super.touchDown(event, x, y, pointer, button);
        }
      });
      item.addListener(new ClickListener() {

        public void clicked(InputEvent event, float x, float y)
        {
          SoundManager.getInstance().play(AnvilSound.CLICK);
          if (!isItemSelected)
          {
            selectedItem = item;
            isItemSelected = true;

            selectedItem.setPosition(selectedItem.getX() + x - 16, selectedItem.getY() + y - 16);
            selectedItem.setTouchable(Touchable.disabled);
            craftGrid.removeItem(selectedItem);
            ig.removeItem(selectedItem);
          }
        }
      });

      fg.addActor(item);
    }

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

    Image smith = new Image(new Texture(Gdx.files.internal("data/smith.png")));
    smith.scale(-0.2f);
    smith.setPosition(-8, 110);
    fg.addActor(smith);

    Image exit = new Image(new Texture(Gdx.files.internal("data/button_exit.png")));
    exit.setPosition(320 - exit.getWidth() - 1, 240 - exit.getHeight() - 1);
    exit.addListener(new InputListener() {
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
      {
        InventoryScreen.this.game.setScreen(new SmitheryScreen(InventoryScreen.this.game));
        ProfileManager.getInstance().retrieveProfile().hour++;

        SoundManager.getInstance().play(AnvilSound.CLICK);
        return true;
      }
    });
    fg.addActor(exit);

    Map<String, Achievement> achievements = AchievementManager.getInstance().getAchievements();
    int startx = 241;
    int x = startx;
    int y = 180;
    int col = 0;
    for (String achievement : achievements.keySet())
    {
      if (col >= 3)
      {
        col = 0;
        x = startx;
        y -= 24;
      }
      try
      {
        final String name = achievements.get(achievement).name;
        final Image ach = new Image(achievements.get(achievement).texture);
        ach.setX(x);
        ach.setY(y);
        if (!profile.getPlayer().achievements.contains(achievement))
        {
          ach.setColor(1, 1, 1, 0.25f);
        }
        else
        {
          ach.setColor(1, 1, 1, 1.0f);
        }
        ach.addListener(new TooltipListener(name));
        fg.addActor(ach);
        col++;
        x += 24;
      }
      catch (Exception e)
      {

      }

    }
  }

  @Override
  public void render(float delta)
  {
    stage.act(delta);
    stage.draw();
    SpriteBatch batch = stage.getSpriteBatch();
    batch.begin();
    font.setColor(1, 1, 1, 1);
    String money = "" + profile.getPlayer().gold + " $";
    font.draw(batch, money, 20, 225);

    int diff = profile.getPlayer().gold - profile.getPlayer().lastGold;
    font.setScale(0.5f, 0.5f);
    if (diff < 0) font.setColor(Color.RED);
    else if (diff > 0) font.setColor(Color.GREEN);
    else font.setColor(Color.WHITE);
    money = "" + (diff > 0 ? "+" : (diff == 0 ? "+-" : "")) + " " + diff + " $";
    font.draw(batch, money, 20, 205);
    font.setColor(1, 1, 1, 1);

    int hasAchievements = profile.getPlayer().achievements.size();
    int maxAchievements = AchievementManager.getInstance().size();
    font.setScale(0.6f);
    font.draw(batch, "Achievements", 245, 215);
    font.draw(batch, "" + hasAchievements + "/" + maxAchievements, 265, 225);
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
  public void show()
  {

    // here we create the splash image actor; its size is set when the
    // resize() method gets called
    bgndImage = new Image(new Texture(Gdx.files.internal("data/3inventoryBgnd.png")));
    bgndImage.setFillParent(true);

    // and finally we add the actor to the stage
    bg.addActor(bgndImage);
  }
}
