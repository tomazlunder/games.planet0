package com.mygdx.zegame.java.gamemodes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.zegame.java.gameworld.Universe;
import com.mygdx.zegame.java.gameworld.entities.Entity;
import com.mygdx.zegame.java.gameworld.entities.moving.player.CirclePlayer;
import com.mygdx.zegame.java.gameworld.entities.nonmoving.CircleSpike;
import com.mygdx.zegame.java.gameworld.planets.Planet;

import java.util.ArrayList;
import java.util.List;

public class GamemodeDemo {
    public List<Entity> spikes;
    public float healthPoints;
    boolean gameOver;
    private CirclePlayer circlePlayer;
    SpriteBatch hudBatch = new SpriteBatch();
    BitmapFont font;

    Texture goTexture;



    public GamemodeDemo(CirclePlayer circlePlayer, Universe universe){

        font = new BitmapFont();
        font.setColor(Color.RED);
        spikes = new ArrayList<Entity>();
        this.circlePlayer = circlePlayer;

        Planet fp = universe.planets.get(0);
        CircleSpike cs = new CircleSpike(fp.getX()-fp.getRadius()-5f,fp.getY(),10f,fp);
        spikes.add(cs);
        fp.entities.add(cs);

        gameOver = false;
        goTexture = new Texture("gameover.jpg");
        healthPoints = 100f;
    }

    public void update(float deltaTime){
        for(Entity e : spikes){
            if(e.getBaseCollision().isCollidingWith(circlePlayer.getBaseCollision())){
                healthPoints-= 15 * deltaTime;
            }
        }
        if(healthPoints <= 0){
            gameOver = true;
        }
    }

    public void drawHud(Camera camera){
        hudBatch.begin();
        font.draw(hudBatch, "Health: " + healthPoints, 20, Gdx.graphics.getHeight()-20);
        font.draw(hudBatch, "FPS: "+Gdx.graphics.getFramesPerSecond(),20,Gdx.graphics.getHeight()-40);
        font.draw(hudBatch, circlePlayer.toString(), 20, Gdx.graphics.getHeight()-60);

        if(gameOver){
            hudBatch.draw(goTexture,0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }

        hudBatch.end();
    }

}
