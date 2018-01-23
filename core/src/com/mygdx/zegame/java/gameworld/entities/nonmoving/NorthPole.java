package com.mygdx.zegame.java.gameworld.entities.nonmoving;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.zegame.java.gameworld.entities.Entity;
import com.mygdx.zegame.java.gameworld.planets.Planet;

public class NorthPole extends Entity {

    Texture texture;

    public NorthPole(Planet planet){
        super(planet.getX(),planet.getY()+planet.getRadius(),0,planet);
        this.collision = false;

        this.name = "CircleFire";
        this.texture = new Texture("sprites/world/northpole.png");



    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        spriteBatch.begin();
        spriteBatch.draw(texture,this.center.x-texture.getWidth()/2, this.center.y);
        spriteBatch.end();
    }

    @Override
    public void draw(ShapeRenderer shapeRenderer) {

    }


}
