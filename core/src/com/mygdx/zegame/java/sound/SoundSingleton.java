package com.mygdx.zegame.java.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundSingleton {
    private static SoundSingleton instance = null;

    public Sound mainLoop;

    protected SoundSingleton(){
        mainLoop = Gdx.audio.newSound(Gdx.files.internal("audio/main_loop.mp3"));
    }

    public static SoundSingleton getInstance(){
        if(instance == null){
            instance = new SoundSingleton();
        }
        return instance;
    }

}
