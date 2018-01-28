package com.mygdx.zegame.java.gameworld.entities;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.zegame.java.gameworld.planets.Planet;
import com.mygdx.zegame.java.physics.Gravity;

public abstract class MovingEntity extends Entity {
    //Vectors are all based on object with sky above and ground below
    protected Vector2 leftUnit, rightUnit, upUnit, downUnit;

    protected Vector2 acceleration;
    protected Vector2 speed;

    protected float maxAcceleration;
    protected float maxSpeed;

    protected float accelerationStep;

    protected Vector2 newPosition;


    protected MovingEntity(float x, float y, float radius, Planet planet)
    {
        super(x,y,radius,planet);
        this.acceleration = new Vector2(0,0);
        this.speed = new Vector2(0,0);
        this.newPosition = new Vector2(x,y);
    }

    protected MovingEntity(Vector2 v, float radius, Planet planet)
    {
        super(v,radius,planet);
        this.acceleration = new Vector2(0,0);
        this.speed = new Vector2(0,0);
        this.newPosition = v;
    }

    public void calcUnitVectors()
    {
        Vector2 planetCenter = nearestPlanet.getPosition();

        //Getting the unit vector pointing from nearest planet center to entity
        //Subtract two points, normalize
        Vector2 calc = this.center.cpy().sub(planetCenter);
        calc.nor();

        //We then rotate it by 90 and -90 degrees to get the unit vectors in the LEFT and RIGHT
        // moving directions (relative to player head up).
        this.upUnit = calc;
        this.downUnit = calc.cpy().rotate(180);
        this.leftUnit = calc.cpy().rotate(90);
        this.rightUnit = calc.cpy().rotate(-90);
    }

    public void calcNewPosition(float deltaTime){
        newPosition.add(acceleration.scl(deltaTime * 60));
    }

    public Vector2 planetToEntity(){
        return (new Vector2(center.x-nearestPlanet.getX(),center.y-nearestPlanet.getY()));
    }


    public float calcGravityForce(){
        return Gravity.gravity(distanceFromCenter(),nearestPlanet);
    }
}
