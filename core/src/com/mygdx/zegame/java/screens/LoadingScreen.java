package com.mygdx.zegame.java.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.zegame.java.GameClass;

public class LoadingScreen implements Screen {

    GameClass game;
    Texture loadingTexture;

    int loadingTimeout;

    public LoadingScreen(GameClass game) {
        this.game = game;
    }

    @Override
    public void show() {
        loadingTexture = new Texture("menus/loading/loading.png");
        loadingTimeout = 10;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.spriteBatch.begin();
        game.spriteBatch.draw(loadingTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.spriteBatch.end();

        if (loadingTimeout <= 0) {
            ScreenManager.getInstance().showScreen(ScreenEnum.GAME);
        }

        loadingTimeout--;
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
}
