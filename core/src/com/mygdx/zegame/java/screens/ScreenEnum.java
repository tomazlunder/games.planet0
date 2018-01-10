package com.mygdx.zegame.java.screens;

import com.badlogic.gdx.Screen;
import com.mygdx.zegame.java.GameClass;

public enum ScreenEnum {

    MAIN_MENU {
        public Screen getScreen(GameClass game) {
            return new MainMenuScreen(game);
        }
    },

    LOADING {
        public Screen getScreen(GameClass game) {
            return new LoadingScreen(game);
        }
    },

    GAME {
        public Screen getScreen(GameClass game) {
            return new GameScreen(game);
        }
    },

    SETTINGS {
        public Screen getScreen(GameClass game) {
            return new SettingsScreen(game);
        }
    };

    public abstract Screen getScreen(GameClass game);
}
