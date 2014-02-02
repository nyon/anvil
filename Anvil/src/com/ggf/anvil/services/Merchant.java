package com.ggf.anvil.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.ggf.anvil.elements.Item;

public class Merchant
{

  public String     name;
  public int        gold;
  public List<Item> inventory = new ArrayList<Item>();

  public Merchant(String name, int gold)
  {
    this.name = name;
    this.gold = gold;
  }

  public static Merchant randomMerchant(int day)
  {
    Random rand = new Random();
    String[] names = { "Conan", "Merkor", "Andriel", "Rekul" };
    String[] adjectives = { "filthy", "cruel", "suspicious", "angry", "sneaky", "creative" };
    String name = names[rand.nextInt(names.length)] + ", the " + adjectives[rand.nextInt(adjectives.length)];
    int gold = rand.nextInt(3000 * day) + 500 * day;

    Merchant merchant = new Merchant(name, gold);

    for (int i = 0; i < 15; i++)
    {
      merchant.inventory.add(Item.randomItem(day));
    }

    return merchant;
  }
}
