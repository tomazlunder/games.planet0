package com.mygdx.zegame.java.gameworld.entities.nonmoving;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.zegame.java.gameworld.entities.Entity;
import com.mygdx.zegame.java.gameworld.planets.FirstPlanet;
import com.mygdx.zegame.java.gameworld.planets.Planet;

public class JetpackDeathzone extends Entity{

    int ttl;
    public Entity source;


    public JetpackDeathzone(float x, float y, float r, Planet firstPlanet, Entity source){
        super(x,y,r,firstPlanet);

        name = "burnzone";
        ttl = 2;
        this.source = source;

    }

    @Override
    public void update(float deltaTime){
        ttl--;
        if(ttl == 0){
            this.removeFromPlanet();
        }
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
    }

    @Override
    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.circle(center.x, center.y, radius, 50);

        shapeRenderer.end();
    }
}
