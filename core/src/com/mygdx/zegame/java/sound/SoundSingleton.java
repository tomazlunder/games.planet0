package com.mygdx.zegame.java.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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
        gameMusic.setVolume(DefaultVolumeSettings.GAME_MUSIC_VOLUME);

        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/arcade_loop.wav"));
        menuMusic.setLooping(true);
        menuMusic.setVolume(DefaultVolumeSettings.MENU_MUSIC_VOLUME);

        //SOUNDS
        menuSelect = Gdx.audio.newSound(Gdx.files.internal("audio/menu_select.wav"));

        //player
        jump = Gdx.audio.newSound(Gdx.files.internal("audio/jump.wav"));
        shoot = Gdx.audio.newSound(Gdx.files.internal("audio/shot.wav"));
        gun_reload = Gdx.audio.newSound(Gdx.files.internal("audio/gun_reload.mp3"));

        footstep = Gdx.audio.newSound(Gdx.files.internal("audio/footstep_amp.wav"));
    }

    public static SoundSingleton getInstance(){
        if(instance == null){
            instance = new SoundSingleton();
        }
        return instance;
    }




}
