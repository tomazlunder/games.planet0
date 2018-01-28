package com.mygdx.zegame.java.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.mygdx.zegame.java.GameClass;
import com.mygdx.zegame.java.TopScores;

public class GameOverScreen implements Screen {

    Texture background;

    GameClass game;

    BitmapFont font, font50;

    SpriteBatch batch;

    String name;

    TopScores topScores;

    String placement;



    public GameOverScreen(GameClass game){
        this.game = game;
    }

    @Override
    public void show() {
        background = new Texture("menus/gameover/gameover.png");
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font.getData().setScale(5);

        batch = new SpriteBatch();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/micross.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter pa = new FreeTypeFontGenerator.FreeTypeFontParameter();
        pa.size = 50;
        font50 = generator.generateFont(pa);
        generator.dispose();

        topScores = new TopScores();
        topScores.getScoresFromFile();
        placement = topScores.getPlacement(game.score);

        Gdx.input.getTextInput(new Input.TextInputListener() {
            @Override
            public void input(String text) {
                topScores.addNameScore(text,game.score);
                topScores.saveScores();
                name = text;

            }

            @Override
            public void canceled() {
                name = "unknown";
            }
        }, "ENTER YOUR NAME", "", "");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

        batch.begin();


        batch.draw(background,0,0, width, height);

        font50.draw(batch, "SCORE: " + game.score, width/100 * 5, height/100 * 70);
        font50.draw(batch, "RATING: " + placement, width/100 * 5, height/100 * 65);
        //font50.draw(batch, "ENTER YOUR NAME ...", width/100 * 5, height/100 * 55);


        batch.end();

        if(name != null){
            ScreenManager.getInstance().showScreen(ScreenEnum.MAIN_MENU);
        }

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
