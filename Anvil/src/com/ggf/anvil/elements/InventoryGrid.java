package com.ggf.anvil.elements;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.ggf.anvil.elements.Item.State;

public class InventoryGrid extends Group
{
  public int       SLOT_COUNT_H = 8;
  public int       SLOT_COUNT_V = 3;
  final static int SLOT_WIDTH   = 32;

  Slot[][]         slotArray;

  public InventoryGrid(int width, int height)
  {
    super();
    SLOT_COUNT_H = width;
    SLOT_COUNT_V = height;
    slotArray = new Slot[SLOT_COUNT_H][SLOT_COUNT_V];

    for (int x = 0; x < SLOT_COUNT_H; x++)
    {
      for (int y = 0; y < SLOT_COUNT_V; y++)
      {
        slotArray[x][y] = new Slot();
        slotArray[x][y].setPosition(x * SLOT_WIDTH, y * SLOT_WIDTH);
        addActor(slotArray[x][y]);
      }
    }
    setSize(SLOT_WIDTH * SLOT_COUNT_H, SLOT_WIDTH * SLOT_COUNT_V);

  }

  public void addItem(Item item)
  {
    int freeSlotX = 0;
    int freeSlotY = 0;

    for (int x = 0; x < SLOT_COUNT_H; x++)
    {
      for (int y = 0; y < SLOT_COUNT_V; y++)
      {
        if (!slotArray[x][y].hasItem())
        {
          freeSlotX = x;
          freeSlotY = y;
        }
      }
    }

    slotArray[freeSlotX][freeSlotY].setItem(item);
  }

  // TODO Add ALL the items
  public void addAllTheItems(List<Item> itemList)
  {
    for (Item item : itemList)
    {
      item.setState(State.IN_INVENTORY);
      addItem(item);
    }
  }

  public ArrayList<Item> getAllItems()
  {
    ArrayList<Item> list = new ArrayList<Item>();

    for (int x = 0; x < SLOT_COUNT_H; x++)
    {
      for (int y = 0; y < SLOT_COUNT_V; y++)
      {
        if (slotArray[x][y].hasItem())
        {
          list.add(slotArray[x][y].getItem());
        }
      }
    }

    return list;
  }

  public Item getItemInSlot(int x, int y)
  {
    if (x < SLOT_COUNT_H && y < SLOT_COUNT_V)
    {
      return slotArray[x][y].getItem();
    }

    return null;
  }

  public Slot getSlot(int x, int y)
  {
    if (x < SLOT_COUNT_H && y < SLOT_COUNT_V)
    {
      return slotArray[x][y];
    }

    return null;
  }

  public Slot[][] getAllSlots()
  {
    return slotArray;
  }

  public void removeItem(Item item)
  {
    for (int x = 0; x < SLOT_COUNT_H; x++)
    {
      for (int y = 0; y < SLOT_COUNT_V; y++)
      {
        if (slotArray[x][y].getItem() == item)
        {
          slotArray[x][y].removeItem();
        }
        ;
      }
    }
  }

}
