package com.ggf.anvil.services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Disposable;
import com.ggf.anvil.Anvil;
import com.ggf.anvil.services.SoundManager.AnvilSound;
import com.ggf.anvil.utils.LRUCache;
import com.ggf.anvil.utils.LRUCache.CacheEntryRemovedListener;

/**
 * A service that manages the sound effects.
 */
public class SoundManager implements CacheEntryRemovedListener<AnvilSound,Sound>, Disposable{
    /**
     * The available sound files.
     */
	
	static SoundManager instance;
	
    public enum AnvilSound{
        CLICK("sound/click.wav"),
        DENGEL1("sound/dengel1.ogg"),
        DENGEL2("sound/dengel2.ogg"),
        DENGEL3("sound/dengel3.ogg"),
        DENGEL4("sound/dengel4.ogg"),
        FIRE("sound/fire_loop.wav"),
        WEAPONSUCCESS("sound/weaponForged.ogg"),
        WEAPONFAIL("sound/weaponFail.ogg"),
        GOLD("sound/gold.ogg"),
        ACHIEVEMENT("sound/achievement.ogg"),
        FART("sound/fart.wav");
        
        
        private final String fileName;

        private AnvilSound(String fileName){
            this.fileName = fileName;
        }

        public String getFileName(){
            return fileName;
        }
    }

    /**
     * The volume to be set on the sound.
     */
    private float volume = 1f;

    /**
     * Whether the sound is enabled.
     */
    private boolean enabled = true;

    /**
     * The sound cache.
     */
    private final LRUCache<AnvilSound,Sound> soundCache;

    /**
     * Creates the sound manager.
     */
    private SoundManager(){
        soundCache = new LRUCache<SoundManager.AnvilSound,Sound>( 10 );
        soundCache.setEntryRemovedListener(this);
    }
    
    synchronized static public SoundManager getInstance(){
    	if(instance == null){
    		instance = new SoundManager();
    	}
    	return instance;
    }

    /**
     * Plays the specified sound.
     */
    public void play(AnvilSound sound){
        // check if the sound is enabled
        if(!enabled) return;

        // try and get the sound from the cache
        Sound soundToPlay = soundCache.get(sound);
        if(soundToPlay == null) {
            FileHandle soundFile = Gdx.files.internal(sound.getFileName());
            soundToPlay = Gdx.audio.newSound(soundFile);
            soundCache.add(sound, soundToPlay);
        }

        // play the sound
        Gdx.app.log(Anvil.LOG, "Playing sound: " + sound.name());
        soundToPlay.play(volume);
    }
    
    public void playLooped(AnvilSound sound){
        // check if the sound is enabled
        if(!enabled) return;

        // try and get the sound from the cache
        Sound soundToPlay = soundCache.get(sound);
        if(soundToPlay == null) {
            FileHandle soundFile = Gdx.files.internal(sound.getFileName());
            soundToPlay = Gdx.audio.newSound(soundFile);
            soundCache.add(sound, soundToPlay);
        }

        // play the sound
        Gdx.app.log(Anvil.LOG, "Looping sound: " + sound.name());
        soundToPlay.loop(volume);
    }

    /**
     * Sets the sound volume which must be inside the range [0,1].
     */
    public void setVolume(float volume){
        Gdx.app.log(Anvil.LOG, "Adjusting sound volume to: " + volume);

        // check and set the new volume
        if(volume < 0 || volume > 1f){
            throw new IllegalArgumentException("The volume must be inside the range: [0,1]");
        }
        this.volume = volume;
    }

    /**
     * Enables or disabled the sound.
     */
    public void setEnabled(boolean enabled){
        this.enabled = enabled;
    }

    // EntryRemovedListener implementation

    @Override
    public void notifyEntryRemoved(AnvilSound key, Sound value){
        Gdx.app.log( Anvil.LOG, "Disposing sound: " + key.name() );
        value.dispose();
    }
    

    /**
     * Disposes the sound manager.
     */
    public void dispose()
    {
        Gdx.app.log(Anvil.LOG, "Disposing sound manager");
        for(Sound sound : soundCache.retrieveAll()){
            sound.stop();
            sound.dispose();
        }
    }
}
