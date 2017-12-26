package com.mygdx.zegame.java.deprecated;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
@Deprecated
public class FirstPlanet {

    private int galaxySize;
    private int worldRadius;
    //private Texture texture;
    private Texture textureYellow;

    private Texture texLU,texLD, texRU, texRD;

    private float x, y;

    public FirstPlanet(int galaxySize){
        this.galaxySize = galaxySize;
        this.worldRadius = galaxySize /10;
        this.x = galaxySize /2;
        this.y = galaxySize /2;

        //this.texture = new Texture(Gdx.files.internal("world8K.png"));
        //texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        this.texLU = new Texture("world8KqLU.png");
        this.texLD = new Texture("world8KqLD.png");
        this.texRU = new Texture("world8KqRU.png");
        this.texRD = new Texture("world8KqRD.png");

        texLU.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        texLD.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        texRU.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        texRD.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        this.textureYellow = new Texture(Gdx.files.internal("1010yellow.png"));
    }

    public int getRadious(){
        return worldRadius;
    }

    public void draw(ShapeRenderer shapeRenderer){
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(0,0, galaxySize, galaxySize);
        shapeRenderer.setColor(Color.GRAY);
        shapeRenderer.circle(galaxySize /2, galaxySize /2, worldRadius,1000);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.circle(galaxySize /2, galaxySize /2, 5,1000);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.circle(galaxySize /2, galaxySize /2, worldRadius);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Point);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.point(galaxySize /2, galaxySize /2, 0);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect((galaxySize /2)-5, galaxySize /2 + worldRadius, 10, 50);
        shapeRenderer.end();
    }

    public void draw(SpriteBatch spriteBatch){
        spriteBatch.begin();
        //spriteBatch.draw(texture,(galaxySize/2-worldRadius),(galaxySize/2)-worldRadius, worldRadius*2, worldRadius*2);

        spriteBatch.draw(texLU, galaxySize /2 - worldRadius, galaxySize /2, worldRadius, worldRadius);
        spriteBatch.draw(texLD, galaxySize /2-worldRadius, galaxySize /2-worldRadius, worldRadius, worldRadius);
        spriteBatch.draw(texRU, galaxySize /2, galaxySize /2, worldRadius, worldRadius);
        spriteBatch.draw(texRD, galaxySize /2, galaxySize /2-worldRadius, worldRadius, worldRadius);

        spriteBatch.draw(textureYellow, x-5, y+worldRadius,10,50);
        spriteBatch.end();
    }
}
