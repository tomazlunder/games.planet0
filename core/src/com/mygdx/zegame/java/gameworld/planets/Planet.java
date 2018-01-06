package com.mygdx.zegame.java.gameworld.planets;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.zegame.java.gameworld.Universe;
import com.mygdx.zegame.java.gameworld.entities.Entity;
import com.mygdx.zegame.java.physics.collision.BasicCollision;
import com.mygdx.zegame.java.physics.collision.shapes.CircleShape;
import com.mygdx.zegame.java.physics.collision.shapes.CollisionShape;

import java.util.ArrayList;
import java.util.List;

public abstract class Planet implements BasicCollision{
    protected CircleShape circleShape;
    protected Universe universe;

    public List<Entity> entities;

    public Planet(Universe universe, float x, float y, float radius){
        this.universe = universe;
        circleShape = new CircleShape(x,y,radius);

        this.entities = new ArrayList<Entity>();
    }

    @Override
    public CollisionShape getCollisionShape() {
        return circleShape;
    }

    @Override
    public void setPosition(Vector2 v) {
        circleShape.center = v;
    }

    public Vector2 getPosition(){
        return circleShape.center.cpy();
    }

    public float getX(){
        return circleShape.center.x;
    }

    public float getY(){
        return circleShape.center.y;
    }

    public float getRadius(){
        return circleShape.radius;
    }

    public void setRadius(float radius){
        circleShape.radius = radius;
    }

    public Vector2 getPointOnSurfaceWithRotation(float rotation, float distFromGround){
        Vector2 zeroRot = new Vector2(1,0);
        zeroRot.rotate(rotation);
        zeroRot.scl(getRadius() + distFromGround);
        return this.getPosition().add(zeroRot);
    }


    abstract public void draw(SpriteBatch spriteBatch);
    abstract public void draw(ShapeRenderer shapeRenderer);

    public void update(float deltaTime){
        for(Entity e : entities){
            e.update(deltaTime);
        }
    }
}
