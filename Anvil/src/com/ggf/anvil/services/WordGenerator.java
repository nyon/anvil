package com.ggf.anvil.services;

import java.util.ArrayList;
import java.util.Random;

public class WordGenerator
{
  private ArrayList<String> adjective1a, adjective1b, adjective1c, adjective2a, adjective2b, adjective2c;
  private String            noun, adj1, adj2;

  // START KUWE-STYLE INFO: Adjective2 is NOT an adjective. It's very late right
  // now, so just let it go already, will you?
  public WordGenerator()
  {
    adjective1a = new ArrayList<String>();
    adjective1b = new ArrayList<String>();
    adjective1c = new ArrayList<String>();
    adjective2a = new ArrayList<String>();
    adjective2b = new ArrayList<String>();
    adjective2c = new ArrayList<String>();
    fillAdjectives();
    noun = "";
    adj1 = "";
    adj2 = "";
  }

  private void fillAdjectives()
  {
    fillAdjective1a();
    fillAdjective1b();
    fillAdjective1c();
    fillAdjective2a();
    fillAdjective2b();
    fillAdjective2c();

  }

  // a = brilliant b = OK c = shabby
  private void fillAdjective1a()
  {
    adjective1a.add("Epic");
    adjective1a.add("Brilliant");
    adjective1a.add("Mighty");
    adjective1a.add("Great");
    adjective1a.add("Wonderful");
    adjective1a.add("Crazy");
    adjective1a.add("Sparkling");
    adjective1a.add("Shiny");
    adjective1a.add("Lustful");
    adjective1a.add("Precious");

  }

  private void fillAdjective1b()
  {
    adjective1b.add("Colourful");
    adjective1b.add("Stained");
    adjective1b.add("Refurbished");
    adjective1b.add("Simple");
    adjective1b.add("Plain");
    adjective1b.add("Vanilla");
    adjective1b.add("Beginners");
    adjective1b.add("Kind Of OK");
    adjective1b.add("Meh");
    adjective1b.add("Standard");

  }

  private void fillAdjective1c()
  {
    adjective1c.add("Filthy");
    adjective1c.add("Crappy");
    adjective1c.add("Rusty");
    adjective1c.add("Chipped");
    adjective1c.add("Shitty");
    adjective1c.add("Flimsy");
    adjective1c.add("Wobbly");
    adjective1c.add("Old");
    adjective1c.add("Second Hand");
    adjective1c.add("Bent");

  }

  private void fillAdjective2a()
  {
    adjective2a.add("Stabbiness");
    adjective2a.add("Sliciness");
    adjective2a.add("Glamour");
    adjective2a.add("Harmony");
    adjective2a.add("Despair");
    adjective2a.add("Deadliness");
    adjective2a.add("Healing");
    adjective2a.add("Dynamics");
    adjective2a.add("Knighthood");
    adjective2a.add("Glory");

  }

  private void fillAdjective2b()
  {
    adjective2b.add("Averageness");
    adjective2b.add("Swishiness");
    adjective2b.add("Dengel");
    adjective2b.add("Cutting");
    adjective2b.add("Hacking");
    adjective2b.add("Loveliness");
    adjective2b.add("Daily Business");
    adjective2b.add("Handiness");
    adjective2b.add("Controlling");
    adjective2b.add("Hitting");

  }

  private void fillAdjective2c()
  {
    adjective2c.add("Poorness");
    adjective2c.add("Rubbishness");
    adjective2c.add("Scrap");
    adjective2c.add("Oxydation");
    adjective2c.add("Snapping");
    adjective2c.add("Breaking");
    adjective2c.add("Lacking Solidity");
    adjective2c.add("Meh");
    adjective2c.add("Despair");
    adjective2c.add("Hopelessness");

  }

  public String getWord(String noun, Float mod1, Float mod2)
  {
    String myWord = "";
    String myNoun = noun;
    Random rand1 = new Random();
    int adj1Word = rand1.nextInt(10);
    int adj2Word = rand1.nextInt(10);

    if (mod1 > 0.6f)
    {
      if (mod2 > 0.6f && mod2 <= 0.7f)
      {
        adj1 = adjective1b.get(adj1Word);
        adj2 = adjective2a.get(adj2Word);
      }
      if (mod2 > 0.7f && mod2 <= 0.8f)
      {
        adj1 = adjective1a.get(adj1Word);
        adj2 = adjective2b.get(adj2Word);
      }
      if (mod2 > 0.8f)
      {
        adj1 = adjective1a.get(adj1Word);
        adj2 = adjective2a.get(adj2Word);
      }
      if (mod2 > 0.3f && mod2 <= 0.4f)
      {
        adj1 = adjective1c.get(adj1Word);
        adj2 = adjective2b.get(adj2Word);
      }
      if (mod2 > 0.4f && mod2 <= 0.5f)
      {
        adj1 = adjective1b.get(adj1Word);
        adj2 = adjective2c.get(adj2Word);
      }
      if (mod2 > 0.5f && mod2 <= 0.6f)
      {
        adj1 = adjective1b.get(adj1Word);
        adj2 = adjective2b.get(adj2Word);
      }
      if (mod2 > 0.0f && mod2 <= 0.1f)
      {
        adj1 = adjective1c.get(adj1Word);
        adj2 = adjective2c.get(adj2Word);
      }
      if (mod2 > 0.1f && mod2 <= 0.2f)
      {
        adj1 = adjective1a.get(adj1Word);
        adj2 = adjective2c.get(adj2Word);
      }
      if (mod2 > 0.2f && mod2 <= 0.3f)
      {
        adj1 = adjective1c.get(adj1Word);
        adj2 = adjective2a.get(adj2Word);
      }

    }

    if (mod1 > 0.3f && mod1 <= 0.6f)
    {
      if (mod2 > 0.6f)
      {
        adj2 = adjective2a.get(adj2Word);
      }
      if (mod2 > 0.3f && mod2 <= 0.6f)
      {
        adj2 = adjective2b.get(adj2Word);
      }
      if (mod2 <= 0.3f)
      {
        adj2 = adjective2c.get(adj2Word);
      }

    }

    if (mod1 <= 0.3f)
    {
      if (mod2 > 0.6f)
      {
        adj1 = adjective1a.get(adj1Word);
      }
      if (mod2 > 0.3f && mod2 <= 0.6f)
      {
        adj1 = adjective1b.get(adj1Word);
      }
      if (mod2 <= 0.3f)
      {
        adj1 = adjective1c.get(adj1Word);
      }

    }
    if (adj2.length() < 1)
    {
      return myWord = adj1 + " " + myNoun;
    }
    else
    {
      return myWord = adj1 + " " + myNoun + " of " + adj2;
    }
  }

}
