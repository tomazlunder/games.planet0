package com.mygdx.zegame.java.gameworld.entities.nonmoving;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.zegame.java.gameworld.entities.Entity;
import com.mygdx.zegame.java.gameworld.planets.Planet;

import javax.xml.bind.annotation.XmlType;

public class PickupShield extends Entity{

    private float DEFAULT_RADIUS = 10f;

    private Texture texture;
    private Sprite sprite;

    private float radius;

    public PickupShield(float rotation, Planet planet){
        super(planet.getPointOnSurfaceWithRotation(rotation, 10),10f,planet);
        this.radius = 10;

        this.texture = new Texture("shield.jpg");
        sprite = new Sprite(texture,200,200);
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        spriteBatch.begin();
        sprite.setPosition(center.x-sprite.getWidth()/2,center.y-sprite.getHeight()/2);
        sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
        sprite.setScale(radius*2/sprite.getWidth());
        sprite.setRotation(rotationFromCenter-90);

        sprite.draw(spriteBatch);
        spriteBatch.end();
    }

    @Override
    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.circle(center.x,center.y,radius,50);

        shapeRenderer.end();
    }
}
