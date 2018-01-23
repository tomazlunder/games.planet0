package com.mygdx.zegame.java.screens;

import com.badlogic.gdx.Screen;
import com.mygdx.zegame.java.GameClass;

public class ScreenManager {
    // Singleton: unique instance
    private static ScreenManager instance;

    // Reference to game
    private GameClass game;

    private Screen previous;


    // Singleton: private constructor
    private ScreenManager() {
        super();
    }

    // Singleton: retrieve instance
    public static ScreenManager getInstance() {
        if (instance == null) {
            instance = new ScreenManager();
        }
        return instance;
    }

    // Initialization with the game class
    public void initialize(GameClass game) {
        this.game = game;
        this.previous = null;
    }

    // Show in the game the screen which enum type is received
    public void showScreen(ScreenEnum screenEnum) {
        System.out.println("[ScreenManager] Changing screen to: " + screenEnum.name());

        // Get current screen to dispose it
        Screen currentScreen = game.getScreen();

        // Show new screen
        Screen newScreen = screenEnum.getScreen(game);

        game.setScreen(newScreen);

        // Dispose previous screen
        if (currentScreen != null) {
            currentScreen.dispose();
        }
    }

    public void pauseAndOpen(ScreenEnum screenEnum){
        System.out.println("[ScreenManager] Changing screen to (temp): " + screenEnum.name());
        previous = game.getScreen();

        Screen newScreen = screenEnum.getScreen(game);
        game.setScreenWOHide(newScreen);
    }

    public void closeAndContinue(){
        if(previous == null){
            System.out.printf("[ScreenManager] closeAndContinue: can't do that, previous = null!");
        }

        Screen currentScreen = game.getScreen();
        game.setScreenWOShow(previous);
        game.getScreen().resume();

        System.out.println("[ScreenManager] closeAndContinue: returning to previous " + game.getScreen().toString());


        currentScreen.dispose();
        previous = null;
    }
}
