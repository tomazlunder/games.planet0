package com.mygdx.zegame.java.objects;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public abstract class GObject {
    protected float centerX, centerY;
    protected float worldCenterX, worldCenterY, worldRadius;
    protected boolean collision;
    protected Circle collisionCircle;
    protected float rotationFromCenter;

    public GObject(float centerX, float centerY, float worldCenterX, float worldCenterY){
        this.centerX = centerX;
        this.centerY = centerY;
        this.worldCenterX = worldCenterX;
        this.worldCenterY = worldCenterY;
        this.collision = true;
        calculateRotationFromCenter();
    }

    public boolean getCollision(){return this.collision;}
    public void setCollision(boolean c){this.collision = c;}
    public float getCenterX(){
        return centerX;
    }
    public float getCenterY(){
        return centerY;
    }
    public Vector2 getCenterVector(){return new Vector2(centerX,centerY);}
    public void setCenterX(float x){
        this.centerX = x;
    }
    public void setCenterY(float y){
        this.centerY = y;
    }
    public void setWorldCenterX(float x){this.worldCenterX = x;}
    public void setWorldCenterY(float y){this.worldCenterY = y;}

    public void setWorldXY(float x, float y, float r){
        this.worldCenterX = x;
        this.worldCenterY = y;
    }

    public Circle getCollCircle(){return this.collisionCircle;}

    public void calculateRotationFromCenter(){
        double theta = Math.atan2(this.centerY - this.worldCenterY, this.centerX - this.worldCenterY);
        double angle = Math.toDegrees(theta);
        if(angle < 0 ){
            angle += 360;
        }
        this.rotationFromCenter= (float) angle;
    }

    public float getRotationFromCenter() {
        return this.rotationFromCenter;
    }
}
