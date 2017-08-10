package com.mygdx.zegame;

public class Physics {

    private static Physics physics = new Physics();
    private Physics(){}

    public static Physics getPhysics(){
        return physics;
    }

    public float distanceTwoPoints(float x1, float y1, float x2, float y2){
        return (float) Math.sqrt(Math.pow(x1-x2,2) + Math.pow(y1-y2,2));
    }
}
