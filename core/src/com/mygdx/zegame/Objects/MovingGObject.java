package com.mygdx.zegame.Objects;

import com.badlogic.gdx.math.Vector2;

public abstract class MovingGObject extends GObject {
    protected Vector2 leftUnit, rightUnit, upUnit, downUnit;
    protected float leftSpeed, rightSpeed, upSpeed, downSpeed; //Down speed AKA gravity
    protected Vector2 moveVector;
    protected float worldRadius;

    protected MovingGObject(float x, float y, float wx, float wy, float wr)
    {
        super(x,y,wx,wy);
        this.worldRadius = wr;
    }
    public void setWorldXYR(float x, float y, float r){
        this.worldCenterX = x;
        this.worldCenterY = y;
        this.worldRadius = r;
    }

    public void calculateUnitVectors()
    {
        Vector2 center = new Vector2(this.worldCenterX,worldCenterY);
        Vector2 player = new Vector2(this.centerX, this.centerY);

        //Getting the unit vector pointing from center to player
        //Subtract two points, normalize
        Vector2 calc = player.sub(center);
        calc.scl(1/distanceFromCenter());

        //We then rotate it by 90 and -90 degrees to get the unit vectors in the LEFT and RIGHT
        // moving directions (relative to player head up).
        this.upUnit = calc;
        this.downUnit = calc.cpy().rotate(180);
        this.leftUnit = calc.cpy().rotate(90);
        this.rightUnit = calc.cpy().rotate(-90);
    }

    public float distanceFromCenter()
    {
        Vector2 center = new Vector2(this.worldCenterX, this.worldCenterY);
        return center.dst(new Vector2(this.centerX, this.centerY));
    }

    public void calculateMoveVector(){

        Vector2 moveVector = downUnit.cpy();

        moveVector.add(upUnit.cpy().scl(upSpeed));
        moveVector.add(leftUnit.scl(leftSpeed));
        moveVector.add(rightUnit.scl(rightSpeed));

        this.moveVector = moveVector.cpy();
    }
}
