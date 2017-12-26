package com.mygdx.zegame.java.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundSingleton {
    private static SoundSingleton instance = null;

    public Sound mainLoop;

    public Sound jump;
    public Sound shoot;
    public Sound arcade;
    public Sound menuSelect;

    protected SoundSingleton(){
        mainLoop = Gdx.audio.newSound(Gdx.files.internal("audio/main_loop.mp3"));
        jump = Gdx.audio.newSound(Gdx.files.internal("audio/jump.wav"));
        shoot = Gdx.audio.newSound(Gdx.files.internal("audio/shot.wav"));

        arcade = Gdx.audio.newSound(Gdx.files.internal("audio/arcade_loop.wav"));
        menuSelect = Gdx.audio.newSound(Gdx.files.internal("audio/menu_select.wav"));
    }

    public static SoundSingleton getInstance(){
        if(instance == null){
            instance = new SoundSingleton();
        }
        return instance;
    }

}
