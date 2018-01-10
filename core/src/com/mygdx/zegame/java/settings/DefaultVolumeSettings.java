package com.mygdx.zegame.java.settings;

import com.mygdx.zegame.java.constants.Constants;

import java.util.prefs.Preferences;

public class DefaultVolumeSettings {
    public static final float MENU_MUSIC_MAX_VOLUME = 0.2f;
    public static final float GAME_MUSIC_MAX_VOLUME = 0.4f;
    public static final float GAME_FX_MAX_VOLUME = 1f;

    public static final float DEFAULT_MENU_VOL_PER = 0.5f;
    public static final float DEFAULT_GAME_VOL_PER = 0.5f;
    public static final float GAME_FX_VOLUME = GAME_FX_MAX_VOLUME/2;

    public static final float FX_JUMP_VOLUME = 0.3f;
    public static final float FX_STARTGUN_SHOT = 0.4f;
    public static final float FX_STARTGUN_RELOAD = 0.4f;

    public static final float FX_FOOTSTEP = 1f;

    public void setDefaultPreferences(Preferences prefs){
        prefs.putFloat("MENU_MUSIC_VOLUME", MENU_MUSIC_MAX_VOLUME/2);
        prefs.putFloat("GAME_MUSIC_VOLUME", GAME_MUSIC_MAX_VOLUME/2);
        prefs.putFloat("GAME_FX_VOLUME", GAME_FX_MAX_VOLUME/2);
    }

}
