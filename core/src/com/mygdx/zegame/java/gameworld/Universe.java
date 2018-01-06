package com.mygdx.zegame.java.gameworld;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Plane;
import com.mygdx.zegame.java.gameworld.planets.Planet;

import java.util.ArrayList;
import java.util.List;

public class Universe {
    private int size;

    public List<Planet> planets;

    public Universe(int size){
        this.size = size;
        planets = new ArrayList<Planet>();
    }

    public int getSize(){
        return size;
    }

    public void update(float deltaTime){
        for(Planet p : planets){
            p.update(deltaTime);
        }
    }

    public void draw(SpriteBatch spriteBatch){
        for(Planet p : planets){
            p.draw(spriteBatch);
        }
    }

    public void draw(ShapeRenderer shapeRenderer){
        for(Planet p : planets){
            p.draw(shapeRenderer);
        }
    }
}
