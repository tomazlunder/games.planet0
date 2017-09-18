package com.mygdx.zegame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class World {


    private int worldSize;
    private int worldRadius;

    //ShapeRenderer shapeRenderer;

    public World(int worldSize){
        this.worldSize = worldSize;
        this.worldRadius = worldSize/10;

    }

    public int getWorldSize(){
        return worldSize;
    }
    public int getRadious(){
        return worldRadius;
    }

    public void drawWorldSimple(ShapeRenderer shapeRenderer){
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(0,0, worldSize, worldSize);
        shapeRenderer.setColor(Color.GRAY);
        shapeRenderer.circle(worldSize /2, worldSize /2, worldRadius,1000);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.circle(worldSize /2, worldSize /2, 5,1000);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.circle(worldSize /2, worldSize /2, worldRadius);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Point);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.point(worldSize /2, worldSize /2, 0);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect((worldSize/2)-5, worldSize/2 + worldRadius, 10, 50);
        shapeRenderer.end();
    }
}
