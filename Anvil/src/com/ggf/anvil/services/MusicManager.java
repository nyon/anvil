package com.ggf.anvil.services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.ggf.anvil.Anvil;

/**
 * A service that manages the background music.
 * <p>
 * Only one music may be playing at a given time.
 */
public class MusicManager
{
  /**
   * The available music files.
   */
  public enum AnvilMusic
  {
    MENU("music/menu.ogg"), 
    INTRO("music/intro.mp3"), 
    FORGING("music/bg_music_forging.ogg");

    private String fileName;
    private Music  musicResource;

    private AnvilMusic(String fileName)
    {
      this.fileName = fileName;
    }

    public String getFileName()
    {
      return fileName;
    }

    public Music getMusicResource()
    {
      return musicResource;
    }

    public void setMusicResource(Music musicBeingPlayed)
    {
      this.musicResource = musicBeingPlayed;
    }
  }

  /**
   * Holds the music currently being played, if any.
   */
  private static AnvilMusic musicBeingPlayed;

  /**
   * The volume to be set on the music.
   */
  private static float      volume  = 1f;

  /**
   * Whether the music is enabled.
   */
  private static boolean    enabled = true;

  
  private static MusicManager instance;
  
  /**
   * Creates the music manager.
   */
  private MusicManager()
  {
    
  }
  
  public static MusicManager getInstance() {
    if(instance == null) instance = new MusicManager();
    return instance;
  }
  

  /**
   * Plays the given music (starts the streaming). If there is already a music
   * being played it is stopped automatically.
   */
  public static void play(AnvilMusic music)
  {
    // check if the music is enabled
    if (!enabled) return;

    // check if the given music is already being played
    if (musicBeingPlayed == music) return;

    // do some logging
    Gdx.app.log(Anvil.LOG, "Playing music: " + music.name());

    // stop any music being played
    MusicManager.stop();

    // start streaming the new music
    FileHandle musicFile = Gdx.files.internal(music.getFileName());
    Music musicResource = Gdx.audio.newMusic(musicFile);
    musicResource.setVolume(volume);
    musicResource.setLooping(true);
    musicResource.play();

    // set the music being played
    musicBeingPlayed = music;
    musicBeingPlayed.setMusicResource(musicResource);
  }

  /**
   * Stops and disposes the current music being played, if any.
   */
  public static void stop()
  {
    if (musicBeingPlayed != null)
    {
      Gdx.app.log(Anvil.LOG, "Stopping current music");
      Music musicResource = musicBeingPlayed.getMusicResource();
      musicResource.stop();
      musicResource.dispose();
      musicBeingPlayed = null;
    }
  }

  /**
   * Sets the music volume which must be inside the range [0,1].
   */
  public static void setVolume(float volume)
  {
    Gdx.app.log(Anvil.LOG, "Adjusting music volume to: " + volume);

    // check and set the new volume
    if (volume < 0 || volume > 1f)
    {
      throw new IllegalArgumentException("The volume must be inside the range: [0,1]");
    }
    MusicManager.volume = volume;

    // if there is a music being played, change its volume
    if (musicBeingPlayed != null)
    {
      musicBeingPlayed.getMusicResource().setVolume(volume);
    }
  }

  /**
   * Enables or disabled the music.
   */
  public static void setEnabled(boolean enabled)
  {
    MusicManager.enabled = enabled;

    // if the music is being deactivated, stop any music being played
    if (!enabled)
    {
      stop();
    }
  }

  /**
   * Disposes the music manager.
   */
  public static void dispose()
  {
    Gdx.app.log(Anvil.LOG, "Disposing music manager");
    stop();
  }

  public static void init()
  {
    // do some initialization here if necessary 
  }
}
