package com.mygdx.zegame.java.gameworld.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.zegame.java.gameworld.planets.Planet;
import com.mygdx.zegame.java.physics.collision.BasicCollision;
import com.mygdx.zegame.java.physics.collision.shapes.CircleShape;
import com.mygdx.zegame.java.physics.collision.shapes.CollisionShape;

public abstract class Entity implements BasicCollision{
    protected boolean DEFAULT_COLLISION = true;
    private float GROUNDED_ERROR = 0.05f;


    protected Vector2 center;
    protected float radius;

    protected boolean collision;
    protected CircleShape baseCollision;

    protected Planet nearestPlanet;

    protected float rotationFromCenter;

    public Entity(float x, float y, float radius, Planet planet){
        this.center = new Vector2(x,y);
        this.radius = radius;
        this.collision = DEFAULT_COLLISION;
        this.baseCollision = new CircleShape(x,y,radius);
        this.nearestPlanet = planet;

        //this.nearestPlanet.entities.add(this);

        calculateRotationFromCenter();
    }

    public Entity(Vector2 center, float radius, Planet planet){
        this.center = center;
        this.collision = DEFAULT_COLLISION;
        this.baseCollision = new CircleShape(center.x,center.y,radius);
        this.nearestPlanet = planet;

        calculateRotationFromCenter();
    }

    /**
     * Getters and setters
     */
    public boolean getCollision(){return this.collision;}
    public void setCollision(boolean c){this.collision = c;}
    public float getX(){ return center.x; }
    public float getY(){ return center.y; }
    public float getRadius() {return radius;}
    public Vector2 getPosition(){return center.cpy();}
    public void setX(float x){ this.center.x = x; }
    public void setY(float y){ this.center.y = y; }
    public void setRadius(float radius) {this.radius = radius; }
    public void setPosition(Vector2 c){this.center = c;}
    public void setNearestPlanet(Planet p){ nearestPlanet = p; }
    public Planet getNearestPlanet(){ return nearestPlanet; }

    public float getRotationFromCenter() { return this.rotationFromCenter; }

    @Override
    public CollisionShape getCollisionShape() {
        return this.baseCollision;
    }

    public CollisionShape getBaseCollision(){return this.baseCollision;}

    public void calculateRotationFromCenter(){
        if(nearestPlanet == null){
            return;
        }
        double theta = Math.atan2(this.center.y - this.nearestPlanet.getY(), this.center.x - this.nearestPlanet.getX());
        double angle = Math.toDegrees(theta);
        if(angle < 0 ){
            angle += 360;
        }
        this.rotationFromCenter= (float) angle;
    }

    public float distanceFromCenter()
    {
        if(nearestPlanet == null){
            return -1;
        }
        return center.dst(nearestPlanet.getPosition());
    }

    public float heigthFromGround(){
        return (distanceFromCenter()-(nearestPlanet.getRadius()+radius));
    }

    public boolean isGrounded(){
        return (heigthFromGround() < GROUNDED_ERROR);
    }


    /**
     * Basic functions
     */
    public void update(float deltaTime){};

    public abstract void draw(SpriteBatch spriteBatch);
    public abstract void draw(ShapeRenderer shapeRenderer);
}
