package com.mygdx.zegame.java.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.zegame.java.GameClass;
import com.mygdx.zegame.java.input.Button;
import com.mygdx.zegame.java.sound.SoundSingleton;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainMenuScreen implements Screen {

    Texture texture_bg = new Texture("menus/main/menu_screen2.png");

    //FOR EASY DISPOSAL
    float fromLeftEdge;

    float screenW, screenH;
    float buttonW, buttonH;
    List<Button> buttons;

    OrthographicCamera camera;

    int skip_for_loading;

    long loopId;


    GameClass game;

    public MainMenuScreen(GameClass game) {
        this.game = game;
    }

    @Override
    public void show() {
        this.buttons = new ArrayList<Button>();

        this.screenH = Gdx.graphics.getHeight();
        this.screenW = Gdx.graphics.getWidth();

        //Button settings
        this.fromLeftEdge = 5 * screenW / 100;
        this.buttonH = screenH / 6.75f;
        this.buttonW = screenW / 4;

        buttons.add(new Button(fromLeftEdge,screenH * 65/100,buttonW,buttonH,"menus/main/plays_btn.png","menus/main/plays_btn_sel.png"));
        buttons.add(new Button(fromLeftEdge,screenH * 45/100,buttonW,buttonH,"menus/main/scores_btn.png","menus/main/scores_btn_sel.png"));
        buttons.add(new Button(fromLeftEdge,screenH * 25/100,buttonW,buttonH,"menus/main/settings_btn.png","menus/main/settings_btn_sel.png"));
        buttons.add(new Button(fromLeftEdge,screenH * 5/100,buttonW,buttonH,"menus/main/exit_btn.png","menus/main/exit_btn_sel.png"));


        this.camera = new OrthographicCamera(screenW, screenH);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.zoom = 1;


        camera.update();
        game.spriteBatch.setProjectionMatrix(camera.combined);

        this.skip_for_loading = 2;

        SoundSingleton.getInstance().menuMusic.play();
    }

    @Override
    public void render(float delta) {
        //In case it changed
        screenH = Gdx.graphics.getHeight();
        screenW = Gdx.graphics.getWidth();

        buttonH = screenH / 6.75f;
        buttonW = screenW / 4;

        handleInputs(delta);

        game.spriteBatch.setProjectionMatrix(camera.combined);

        camera.update();


        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        game.spriteBatch.begin();

        game.spriteBatch.draw(texture_bg, 0, 0, screenW, screenH);

        for(Button b : buttons){
            b.draw(game.spriteBatch);
        }

        game.spriteBatch.end();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        SoundSingleton.getInstance().menuMusic.stop();
    }

    @Override
    public void dispose() {
        texture_bg.dispose();
        for(Button b : buttons){
            b.dispose();
        }
    }

    private void handleInputs(float deltaTime) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)) {
            System.out.println("Reloading main menu...");
            this.dispose();
            game.setScreen(new MainMenuScreen(game));
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            playClicked();
        }

        float x = Gdx.input.getX();
        float y = Gdx.input.getY();

        //Button state (mouse over or not update)
        for(Button b : buttons){
            b.updateMouse(x,screenH - y);
        }

        //Button click handling
        if(Gdx.input.justTouched()){
            for(int i = 0; i < buttons.size(); i++){
                if(buttons.get(i).isActive){
                    switch(i){
                        case 0: playClicked(); break;
                        case 1: scoresClicked(); break;
                        case 2: settingsClicked(); break;
                        case 3: exitClicked(); break;
                    }
                }
            }
        }
    }

    private void playClicked() {
        ScreenManager.getInstance().showScreen(ScreenEnum.LOADING);
    }

    private void scoresClicked() {
        ScreenManager.getInstance().pauseAndOpen(ScreenEnum.SCORE);

    }

    private void settingsClicked() {
        ScreenManager.getInstance().pauseAndOpen(ScreenEnum.SETTINGS);
    }

    private void exitClicked() {
        Gdx.app.exit();
    }
}
