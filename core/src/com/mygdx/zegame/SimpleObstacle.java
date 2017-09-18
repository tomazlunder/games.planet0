package com.mygdx.zegame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.HashMap;

public class SimpleObstacle {
    float objectWidth = 1;
    float objectHeigth = 1.5f;

    private ArrayList<Vector2> centers;
    private HashMap<Vector2, Float> rotations;

    float worldCenterXY;

    public SimpleObstacle(float worldRadious, float worldSize){
        this.centers = new ArrayList<Vector2>();
        this.rotations = new HashMap<Vector2, Float>();
        this.worldCenterXY = worldSize/2;
        Vector2 center = new Vector2(worldCenterXY, worldCenterXY);
        float worldCircumference = 2 * worldRadious * (float) Math.PI;
        int numObstacles = (int) (worldCircumference / 100);
        for(int i = 0; i < numObstacles; i++){
            Vector2 point = center.cpy();
            Vector2 transform  =new Vector2(0,worldRadious);
            transform.rotate(i * (360/numObstacles));
            point.add(transform);
            centers.add(point);
            rotations.put(point, calculateRotationFromCenter(point.x,point.y));
        }
    }

    public float calculateRotationFromCenter(float x, float y){
        double theta = Math.atan2(y - worldCenterXY, x - worldCenterXY);
        double angle = Math.toDegrees(theta);
        if(angle < 0 ){
            angle += 360;
        }
        return (float) angle;
    }

    public void draw(float playerX, float playerY, ShapeRenderer shapeRenderer){
        for(Vector2 v : centers){
            Vector2 dist = v.cpy();
            dist.add(new Vector2(playerX, playerY));
            if(dist.len() > 500){
                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                shapeRenderer.setColor(Color.BLACK);
                shapeRenderer.rect(v.x-objectWidth/2, v.y-objectHeigth/2,
                        objectWidth/2, objectHeigth/2,
                        objectWidth, objectHeigth,
                        1,1,
                        rotations.get(v));
                shapeRenderer.end();
            }
        }
    }

}
