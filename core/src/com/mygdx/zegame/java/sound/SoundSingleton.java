package com.mygdx.zegame.java.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.mygdx.zegame.java.GameClass;
import com.mygdx.zegame.java.settings.DefaultVolumeSettings;

public class SoundSingleton {
    private static SoundSingleton instance = null;

    public Music menuMusic;
    public Music gameMusic;

    public Sound jump;
    public Sound shoot;
    public Sound gun_reload;

    public Sound footstep;

    //public Sound arcade;
    public Sound menuSelect;

    protected SoundSingleton(){
        //MUSIC
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/main_loop.mp3"));
        gameMusic.setLooping(true);

        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/arcade_loop.wav"));
        menuMusic.setLooping(true);

        //SOUNDS
        menuSelect = Gdx.audio.newSound(Gdx.files.internal("audio/menu_select.wav"));

        //player
        jump = Gdx.audio.newSound(Gdx.files.internal("audio/jump.wav"));
        shoot = Gdx.audio.newSound(Gdx.files.internal("audio/shot.wav"));
        gun_reload = Gdx.audio.newSound(Gdx.files.internal("audio/gun_reload.mp3"));

        footstep = Gdx.audio.newSound(Gdx.files.internal("audio/footstep_amp.wav"));

        //
        setVolumesFromPrefOrDefault();
    }

    public static SoundSingleton getInstance(){
        if(instance == null){
            instance = new SoundSingleton();
        }
        return instance;
    }

    public void setVolumesFromPrefOrDefault(){
        Preferences prefs = Gdx.app.getPreferences("fri.tomazlunder.planet0.settings.volume");

        float menuPerc = prefs.getFloat("menu_volume_percentage", DefaultVolumeSettings.DEFAULT_MENU_VOL_PER);
        float gamePerc = prefs.getFloat("game_volume_percentage",DefaultVolumeSettings.DEFAULT_GAME_VOL_PER);

        menuMusic.setVolume(menuPerc * DefaultVolumeSettings.MENU_MUSIC_MAX_VOLUME);
        gameMusic.setVolume(gamePerc * DefaultVolumeSettings.GAME_MUSIC_MAX_VOLUME);
    }

    public void saveVolumesAndSet(float menuPercentage, float gamePercentage){
        Preferences prefs = Gdx.app.getPreferences("fri.tomazlunder.planet0.settings.volume");

        prefs.putFloat("menu_volume_percentage", menuPercentage);
        prefs.putFloat("game_volume_percentage", gamePercentage);

        prefs.flush();

        setVolumesFromPrefOrDefault();
    }






}
