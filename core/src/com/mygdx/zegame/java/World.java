package com.mygdx.zegame.java;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class World {

    private int worldSize;
    private int worldRadius;
    private Texture texture, t2;

    private float x, y;

    public World(int worldSize){
        this.worldSize = worldSize;
        this.worldRadius = worldSize/10;
        this.x = worldSize/2;
        this.y = worldSize/2;

        this.texture = new Texture(Gdx.files.internal("world.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        Pixmap pixmap = new Pixmap(worldRadius*2,worldRadius*2, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.RED);
        pixmap.fillCircle((int) x, (int) y, worldRadius);

        this.t2 = new Texture(pixmap);
    }


    public int getWorldSize(){
        return worldSize;
    }
    public int getRadious(){
        return worldRadius;
    }

    public void draw(ShapeRenderer shapeRenderer){
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

    public void draw(SpriteBatch spriteBatch){
        spriteBatch.begin();
        spriteBatch.draw(texture,(worldSize/2-worldRadius),(worldSize/2)-worldRadius, worldRadius*2, worldRadius*2);
        spriteBatch.end();
    }
}
