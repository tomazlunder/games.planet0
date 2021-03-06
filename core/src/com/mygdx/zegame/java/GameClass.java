package com.mygdx.zegame.java;

import com.badlogic.gdx.*;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.zegame.java.screens.MainMenuScreen;
import com.mygdx.zegame.java.screens.ScreenEnum;
import com.mygdx.zegame.java.screens.ScreenManager;
import com.mygdx.zegame.java.sound.SoundSingleton;

import java.io.OutputStream;


public class GameClass extends Game {

    public SpriteBatch spriteBatch;
    public ShapeRenderer shapeRenderer;

    public int score;


    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        ScreenManager.getInstance().initialize(this);
        ScreenManager.getInstance().showScreen(ScreenEnum.MAIN_MENU);
    }


    @Override
    public void render() {
        handleAppUniversalInputs();
        super.render();

    }

    @Override
    public void dispose() {
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }
        if (spriteBatch != null) {
            spriteBatch.dispose();
        }
    }

    public void handleAppUniversalInputs(){
        if(Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)){
            Gdx.app.exit();
        }
    }

    public void setScreenWOHide(Screen screen){
        if (this.screen != null) this.screen.pause();
        this.screen = screen;
        if (this.screen != null) {
            this.screen.show();
            this.screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
    }

    public void setScreenWOShow(Screen screen){
        if (this.screen != null) this.screen.pause();
        this.screen = screen;
        if (this.screen != null) {
            this.screen.resume();
            this.screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
    }
}


