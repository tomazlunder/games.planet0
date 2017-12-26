package com.mygdx.zegame.java.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.zegame.java.GameClass;
import com.mygdx.zegame.java.sound.SoundSingleton;

import java.util.Arrays;

public class MainMenuScreen implements Screen {

    Texture texture_bg = new Texture("menus/main/menu_screen.png");
    Texture texPlay = new Texture("menus/main/plays_btn.png");
    Texture texPlaySel = new Texture("menus/main/plays_btn_sel.png");

    Texture texScore = new Texture("menus/main/scores_btn.png");
    Texture texScoreSel = new Texture("menus/main/scores_btn_sel.png");

    Texture texSettings = new Texture("menus/main/settings_btn.png");
    Texture texSettingsSel = new Texture("menus/main/settings_btn_sel.png");

    Texture texExit = new Texture("menus/main/exit_btn.png");
    Texture texExitSel = new Texture("menus/main/exit_btn_sel.png");

    //FOR EASY DISPOSAL
    final Texture[] texArray = new Texture[]{texPlay, texPlaySel, texScore, texScoreSel, texSettings, texSettingsSel, texExit, texExitSel, texture_bg};

    float fromLeftEdge;

    final float CLICK_TO = 0.1f;
    float clickTo;

    float screenW, screenH;
    float buttonW, buttonH;

    boolean[] buttonsActive;

    OrthographicCamera camera;

    int skip_for_loading;

    long loopId;


    GameClass game;

    public MainMenuScreen(GameClass game) {
        this.game = game;
    }

    @Override
    public void show() {
        this.buttonsActive = new boolean[]{false, false, false, false};

        this.screenH = Gdx.graphics.getHeight();
        this.screenW = Gdx.graphics.getWidth();

        //Button settings
        this.fromLeftEdge = 5 * screenW / 100;
        this.buttonH = screenH / 6.75f;
        this.buttonW = screenW / 4;

        //To Dispose
        this.clickTo = CLICK_TO;

        this.camera = new OrthographicCamera(screenW, screenH);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.zoom = 1;


        camera.update();
        game.spriteBatch.setProjectionMatrix(camera.combined);

        this.skip_for_loading = 2;

        loopId = SoundSingleton.getInstance().arcade.loop();
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


        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.spriteBatch.begin();

        game.spriteBatch.draw(texture_bg, 0, 0, screenW, screenH);

        if (buttonsActive[0]) {
            game.spriteBatch.draw(texPlaySel, fromLeftEdge, 65 * screenH / 100, buttonW, buttonH);
        } else {
            game.spriteBatch.draw(texPlay, fromLeftEdge, 65 * screenH / 100, buttonW, buttonH);
        }

        if (buttonsActive[1]) {
            game.spriteBatch.draw(texScoreSel, fromLeftEdge, 45 * screenH / 100, buttonW, buttonH);
        } else {
            game.spriteBatch.draw(texScore, fromLeftEdge, 45 * screenH / 100, buttonW, buttonH);
        }

        if (buttonsActive[2]) {
            game.spriteBatch.draw(texSettingsSel, fromLeftEdge, 25 * screenH / 100, buttonW, buttonH);

        } else {
            game.spriteBatch.draw(texSettings, fromLeftEdge, 25 * screenH / 100, buttonW, buttonH);
        }

        if (buttonsActive[3]) {
            game.spriteBatch.draw(texExitSel, fromLeftEdge, 5 * screenH / 100, buttonW, buttonH);
        } else {
            game.spriteBatch.draw(texExit, fromLeftEdge, 5 * screenH / 100, buttonW, buttonH);
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
        SoundSingleton.getInstance().arcade.stop(loopId);
    }

    @Override
    public void dispose() {
        for(Texture t : texArray){
            t.dispose();
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

        boolean[] last = buttonsActive.clone();
        Arrays.fill(buttonsActive, false);

        float x = Gdx.input.getX();
        float y = Gdx.input.getY();
        y = screenH - y;

        if (x >= fromLeftEdge && x < fromLeftEdge + buttonW) {
            if (y > 65 * screenH / 100 && y < 65 * screenH / 100 + buttonH) {
                buttonsActive[0] = true;
            } else if (y > 45 * screenH / 100 && y < 45 * screenH / 100 + buttonH) {
                buttonsActive[1] = true;
            } else if (y > 25 * screenH / 100 && y < 25 * screenH / 100 + buttonH) {
                buttonsActive[2] = true;
            } else if (y > 5 * screenH / 100 && y < 5 * screenH / 100 + buttonH) {
                buttonsActive[3] = true;
            }
        }

        //Mouse over button sound
        if(!Arrays.equals(buttonsActive,last)){
            boolean atLeastOneTrue = false;
            for(boolean b : buttonsActive){
                if(b) atLeastOneTrue = true;
            }
            if(atLeastOneTrue){
                SoundSingleton.getInstance().menuSelect.play();
            }
        }

        if (clickTo > 0) {
            clickTo -= deltaTime;
        }

        if (Gdx.input.isTouched() && clickTo <= 0) {
            clickTo = CLICK_TO;
            if (buttonsActive[0]) {
                playClicked();
            }
            if (buttonsActive[3]) {
                exitClicked();
            }
        }

    }

    private void playClicked() {
        ScreenManager.getInstance().showScreen(ScreenEnum.LOADING);
    }

    private void scoresClicked() {

    }

    private void settingsClicked() {

    }

    private void exitClicked() {
        Gdx.app.exit();
    }
}
