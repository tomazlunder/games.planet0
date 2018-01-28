package com.mygdx.zegame.java.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.mygdx.zegame.java.GameClass;
import com.mygdx.zegame.java.TopScores;
import com.mygdx.zegame.java.input.Button;
import com.mygdx.zegame.java.weapons.ammo.Bullet;

import java.util.ArrayList;
import java.util.List;

public class ScoreScreen implements Screen {

    GameClass game;
    TopScores topScores;

    Texture background;

    SpriteBatch batch;

    List<Button> buttons;


    BitmapFont font35;


    public ScoreScreen(GameClass game){
        this.game = game;
    }

    @Override
    public void show() {
        background = new Texture("menus/scores/scores.png");
        batch = new SpriteBatch();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/micross.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter pa = new FreeTypeFontGenerator.FreeTypeFontParameter();
        pa.size = 35;
        font35 = generator.generateFont(pa);
        generator.dispose();

        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

        float buttonW = width / 4;
        float buttonH = height / 6.75f;

        topScores = new TopScores();
        topScores.getScoresFromFile();

        buttons = new ArrayList<Button>();
        buttons.add(new Button(width * 60f/100f,height * 5f/100f, buttonW, buttonH, "menus/settings/back_btn.png","menus/settings/back_btn_sel.png"));

    }

    @Override
    public void render(float delta) {
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

        batch.begin();

        batch.draw(background, 0, 0, width, height);

        String name;
        int score;
        for(int i = 0; i < 10; i++){
            name = topScores.names.get(i);
            score = topScores.scores.get(i);

            String s = String.format("%10d %20s %10d",i+1,name,score);
            font35.draw(batch,s,width/100f * 5, (height/100f * (80 - i*8)));
        }

        for(Button b : buttons){
            b.updateMouse(Gdx.input.getX(), height - Gdx.input.getY());
            b.draw(batch);
        }

        batch.end();

        handleInputs();
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

        if(buttons.get(0).isActive && Gdx.input.isTouched()){
            ScreenManager.getInstance().closeAndContinue();
        }
    }
}
