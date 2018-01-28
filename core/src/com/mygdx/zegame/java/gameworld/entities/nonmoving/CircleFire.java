package com.mygdx.zegame.java.gameworld.entities.nonmoving;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.zegame.java.gameworld.entities.Entity;
import com.mygdx.zegame.java.gameworld.planets.Planet;

public class CircleFire extends Entity{

    private Texture texture;
    private Sprite sprite;

    private float radius;

    private float ttl;

    public CircleFire(float rotation, float radius, Planet planet){
        super(planet.getPointOnSurfaceWithRotation(rotation,radius),radius,planet);

        this.name = "CircleFire";
        this.radius = radius;

        this.texture = new Texture("spike2.gif");
        sprite = new Sprite(texture,100,100);

        ttl = 3f;
    }

    @Override
    public void update(float deltaTime){
        ttl -= deltaTime;
        if(ttl< 0){
            this.removeFromPlanet();
        }
    }



    @Override
    public void draw(SpriteBatch spriteBatch) {
        //spriteBatch.begin();
        sprite.setPosition(center.x-sprite.getWidth()/2,center.y-sprite.getHeight()/2);
        sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
        sprite.setScale(radius*2/sprite.getWidth());
        sprite.setRotation(rotationFromCenter-90);

        sprite.draw(spriteBatch);
        //spriteBatch.end();
    }

    @Override
    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.circle(center.x,center.y,radius,50);

        shapeRenderer.end();
    }
}
