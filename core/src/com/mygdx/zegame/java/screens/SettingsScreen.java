package com.mygdx.zegame.java.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.zegame.java.GameClass;
import com.mygdx.zegame.java.input.Button;
import com.mygdx.zegame.java.input.SettingBar;

import java.util.ArrayList;
import java.util.List;

public class SettingsScreen implements Screen {

    GameClass game;

    List<SettingBar> bars;
    List<Button> buttons;

    Texture background;

    SpriteBatch menuBatch;

    public SettingsScreen(GameClass game){
        this.game = game;
    }

    @Override
    public void show() {
        menuBatch = new SpriteBatch();

        bars = new ArrayList<SettingBar>();
        buttons = new ArrayList<Button>();


        float screenH = Gdx.graphics.getHeight();
        float screenW = Gdx.graphics.getWidth();
        float fromLeftEdge = 5 * screenW / 100;
        float buttonH = screenH / 6.75f;
        float barH = screenH / 13.5f;

        float buttonW = screenW / 4;

        background = new Texture("menus/settings/settingsBG.png");


        bars.add(new SettingBar("Menu music volume", 0, 100, fromLeftEdge, screenH * 65/100, buttonW, barH));
        bars.add(new SettingBar("Game music volume", 0, 100, fromLeftEdge, screenH * 55/100, buttonW, barH));
        bars.add(new SettingBar("Game FX volume", 0, 100, fromLeftEdge, screenH * 45/100, buttonW, barH));

        buttons.add(new Button(screenW * 10/100,screenH * 10/100, buttonW, buttonH, "menus/settings/back_btn.png","menus/settings/back_btn_sel.png"));
        buttons.add(new Button(screenW - (screenW * 10/100) - buttonW,screenH * 10/100, buttonW, buttonH, "menus/settings/save_btn.png","menus/settings/save_btn_sel.png"));

    }

    @Override
    public void render(float delta) {
        handleInputs();

        Gdx.gl.glClearColor(0, 0, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        menuBatch.begin();

        menuBatch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        for(SettingBar sb : bars){
            sb.draw(menuBatch);
        }

        for(Button b : buttons){
            b.draw(menuBatch);
        }
        menuBatch.end();

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

    }

    @Override
    public void dispose() {

    }

    public void handleInputs(){
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            ScreenManager.getInstance().closeAndContinue();
        }

        for(SettingBar sb : bars){
            sb.update(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
        }

        int i = 0;
        for(Button b : buttons){
            boolean isPressed = b.updateMouse(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
            if(isPressed){
                switch(i){
                    //back
                    case 0: ScreenManager.getInstance().closeAndContinue(); break;
                    //save
                }
            }
            i++;
        }
    }
}
