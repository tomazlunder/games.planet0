package com.mygdx.zegame.java.gameworld.planets;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.zegame.java.gameworld.Universe;
import com.mygdx.zegame.java.gameworld.entities.Entity;

public class FirstPlanet extends Planet {
    private Texture texLU, texLD, texRU, texRD;
    private Texture textureSky, textureYellow;

    private Sprite spriteSky;

    public FirstPlanet(Universe universe) {
        super(universe,universe.getSize() / 2, universe.getSize() / 2, universe.getSize()/10);

        this.texLU = new Texture("world8KqLU.png");
        this.texLD = new Texture("world8KqLD.png");
        this.texRU = new Texture("world8KqRU.png");
        this.texRD = new Texture("world8KqRD.png");

        texLU.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        texLD.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        texRU.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        texRD.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        this.textureYellow = new Texture(Gdx.files.internal("1010yellow.png"));
        this.textureSky = new Texture("sprites/world/sky1.png");
        spriteSky = new Sprite(textureSky, 8192, 8192);
    }


    @Override
    public void draw(SpriteBatch spriteBatch) {

        spriteBatch.begin();
        float universeSize = universe.getSize();
        float planetRadius = this.getRadius();

        //spriteSky.setPosition(circleShape.center.x - spriteSky.getWidth()/2, circleShape.center.y - spriteSky.getHeight()/2);
        //spriteSky.setScale(planetRadius/250);
        //spriteSky.setOrigin(spriteSky.getWidth()/2, spriteSky.getHeight()/2);
        //spriteSky.draw(spriteBatch);

        spriteBatch.draw(textureSky, circleShape.center.x - 2f*planetRadius, circleShape.center.y - 2f*planetRadius, 4*planetRadius, 4*planetRadius);

        spriteBatch.draw(texLU, circleShape.center.x - planetRadius, circleShape.center.y, planetRadius, planetRadius);
        spriteBatch.draw(texLD, circleShape.center.x - planetRadius, circleShape.center.y - planetRadius, planetRadius, planetRadius);
        spriteBatch.draw(texRU, circleShape.center.x, circleShape.center.y, planetRadius, planetRadius);
        spriteBatch.draw(texRD, circleShape.center.x, circleShape.center.y - planetRadius, planetRadius, planetRadius);

        spriteBatch.draw(textureYellow, (this.circleShape.center.x - 5), (this.circleShape.center.y + planetRadius), 10, 50);
        spriteBatch.end();

        for(Entity e : entities){
            e.draw(spriteBatch);
        }
    }

    @Override
    public void draw(ShapeRenderer shapeRenderer){
        float universeSize = universe.getSize();
        float planetRadius = this.getRadius();
        
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(0,0, universeSize, universeSize);
        shapeRenderer.setColor(Color.GRAY);
        shapeRenderer.circle(circleShape.center.x, circleShape.center.y, planetRadius,1000);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.circle(circleShape.center.x, circleShape.center.y, 5,1000);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.circle(circleShape.center.x, circleShape.center.y, planetRadius);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Point);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.point(circleShape.center.x, circleShape.center.y, 0);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect((circleShape.center.x)-5, circleShape.center.x + planetRadius, 10, 50);
        shapeRenderer.end();

        for(Entity e : entities){
            e.draw(shapeRenderer);
        }
    }

}
