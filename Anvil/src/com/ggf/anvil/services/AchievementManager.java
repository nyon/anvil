package com.ggf.anvil.services;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class AchievementManager
{
  private HashMap<String, Achievement> achievements = new HashMap<String, Achievement>();

  private AchievementManager()
  {
    achievements.put("1_weapon", new Achievement("Craft 1 weapon", new Texture(Gdx.files.internal("data/achievement_anvil_1.png"))));
    achievements.put("50_weapons", new Achievement("Craft 50 weapons", new Texture(Gdx.files.internal("data/achievement_anvil_2.png"))));
    achievements.put("200_weapons", new Achievement("Craft 200 weapons", new Texture(Gdx.files.internal("data/achievement_anvil_3.png"))));

    achievements.put("quality_50", new Achievement("50% Quality weapon crafted", new Texture(Gdx.files.internal("data/achievement_sword_1.png"))));
    achievements.put("quality_60", new Achievement("60% Quality weapon crafted", new Texture(Gdx.files.internal("data/achievement_sword_2.png"))));
    achievements.put("quality_75", new Achievement("75% Quality weapon crafted", new Texture(Gdx.files.internal("data/achievement_sword_3.png"))));
    achievements.put("quality_90", new Achievement("90% Quality weapon crafted", new Texture(Gdx.files.internal("data/achievement_sword_4.png"))));
    achievements.put("quality_95", new Achievement("95% Quality weapon crafted", new Texture(Gdx.files.internal("data/achievement_sword_5.png"))));

    achievements.put("week_7", new Achievement("7 days survived", new Texture(Gdx.files.internal("data/achievement_week_7.png"))));

  }

  public Achievement getAchievement(String name)
  {
    return achievements.get(name);
  }

  public HashMap<String, Achievement> getAchievements()
  {
    return achievements;
  }

  public static AchievementManager instance;

  public static AchievementManager getInstance()
  {
    if (instance == null) instance = new AchievementManager();
    return instance;
  }

  public int size()
  {
    return achievements.size();
  }

}
