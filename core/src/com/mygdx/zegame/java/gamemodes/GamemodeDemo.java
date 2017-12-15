package com.mygdx.zegame.java.gamemodes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.zegame.java.gameworld.Universe;
import com.mygdx.zegame.java.gameworld.entities.Entity;
import com.mygdx.zegame.java.gameworld.entities.moving.player.CirclePlayer;
import com.mygdx.zegame.java.gameworld.entities.nonmoving.CircleFire;
import com.mygdx.zegame.java.gameworld.entities.nonmoving.PickupShield;
import com.mygdx.zegame.java.gameworld.planets.Planet;

import java.util.ArrayList;
import java.util.List;

public class GamemodeDemo {
    public List<Entity> fires, shieldPickups;
    boolean gameOver;
    private CirclePlayer circlePlayer;
    SpriteBatch hudBatch;
    BitmapFont font;

    Planet fp;

    Texture goTexture;
    public GamemodeDemo(CirclePlayer circlePlayer, Universe universe){
        this.circlePlayer = circlePlayer;

        //HUD
        hudBatch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.RED);
        goTexture = new Texture("gameover.jpg");

        //Gamemode specific
        gameOver = false;

        this.circlePlayer = circlePlayer;

        fp = universe.planets.get(0);

        fires = new ArrayList<Entity>();
        CircleFire cs = new CircleFire(fp.getX()-fp.getRadius()-5f,fp.getY(),10f,fp);
        fires.add(cs);
        fp.entities.add(cs);  //Drawn with wordld

        shieldPickups = new ArrayList<Entity>();
        PickupShield ps = new PickupShield(0, fp);
        shieldPickups.add(ps);
        fp.entities.add(ps);
    }

    public void update(float deltaTime){
        //Shield pickup
        ArrayList<Entity> toRemove = new ArrayList<Entity>();
        for(Entity e : shieldPickups){
            if(e.getBaseCollision().isCollidingWith(circlePlayer.getBaseCollision())){
                toRemove.add(e);
                fp.entities.remove(e);
                circlePlayer.gainShield();
                System.out.println("[Gamemode]: Shield removed");
            }
        }
        shieldPickups.removeAll(toRemove);

        //Collision with spikes
        for(Entity e : fires){
            if(e.getBaseCollision().isCollidingWith(circlePlayer.getBaseCollision())){
                circlePlayer.takeDamage(30*deltaTime);
            }
        }
        if(circlePlayer.isDead()){
            gameOver = true;
        }
    }

    public void drawHud(Camera camera){
        hudBatch.begin();
        font.draw(hudBatch, "Health: " + circlePlayer.healthPoints, 20, Gdx.graphics.getHeight()-20);
        font.draw(hudBatch, "Shield: " + circlePlayer.shieldPoints, 20, Gdx.graphics.getHeight()-40);
        font.draw(hudBatch, "FPS: "+Gdx.graphics.getFramesPerSecond(),20,Gdx.graphics.getHeight()-60);
        font.draw(hudBatch, circlePlayer.toString(), 20, Gdx.graphics.getHeight()-80);

        if(gameOver){
            hudBatch.draw(goTexture,0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }

        hudBatch.end();
    }

}
